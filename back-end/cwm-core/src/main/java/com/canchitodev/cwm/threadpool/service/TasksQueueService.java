/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2017, canchito-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author 		José Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
package com.canchitodev.cwm.threadpool.service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.ApplicationContext;
import org.apache.log4j.Logger;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canchitodev.cwm.configuration.properties.QueueConfigurationProperties;
import com.canchitodev.cwm.configuration.properties.QueueConfigurationProperties.Queue;
import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.exception.GenericException;
import com.canchitodev.cwm.exception.ObjectNotFoundException;
import com.canchitodev.cwm.threadpool.runnable.GenericTaskRunnable;
import com.canchitodev.cwm.threadpool.utils.ExecutorServiceUtils;
import com.canchitodev.cwm.utils.StrongUuidGenerator;
import com.canchitodev.cwm.utils.enums.BehaviorTaskStatus;

/**
 * <p>The TaskQueueService is in charge of managing the different queues for executing TaskRunnable(s).
 * Remember that a TaskRunnable is generated from an asynchronous service invocation, and it is mainly
 * used for executing long-running tasks. The main purpose of the TaskQueueService is to solve the 
 * below points.</p>
 * 
 * <p>Quoted from Frederik Heremans comment (https://community.alfresco.com/thread/220468-modelling-an-async-user-wait-on-a-long-running-service-task):</p>
 * <i>When you have long-running tasks executed by Activiti's job-executor, you should consider that:
 * 	<ul>
 * 		<li>
 * 			Executing a service-task (or any other task) keeps a transaction open until a wait-state/process-end/async-task 
 * 			is reached. If you have long-running operations, make sure your DB doesn't time out.
 * 		</li>
 * 		<li>
 * 			When a jobs is running for 5 minutes, the job aquisistion-thread assumes the job-executor that was running the job, 
 * 			has either died or has failed. The lock of the job is removed and the job will be executed by another thread in the 
 * 			executor-pool. This timeout-setting can be  raised, if that is required.
 * 		</li>
 * 		<li>
 * 			Long-running tasks modeled IN the activiti-process always keep a transaction open and a job-executor thread occupied. 
 * 			Better practice is to use a queue-signal approach where the long-running operation is executed outside of Activit. When 
 * 			the long-running task is completed, it should signal the execution, which has a receive-task modeled in.
 * 		</li>
 * 	</ul>
 * </i>
 **/
@Service
public class TasksQueueService {
	
	private static final Logger logger = Logger.getLogger(TasksQueueService.class);

	private List<Queue> queues;
	private QueueConfigurationProperties queueConfigProperty;
	private HashMap<String, ThreadPoolExecutor> executors;
	private Object lock;
	
	@Autowired
	private GenericTaskService genericTaskService;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private StrongUuidGenerator strongUuidGenerator;
	
	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	public TasksQueueService(QueueConfigurationProperties queueConfigProperty) {
		this.queueConfigProperty = queueConfigProperty;
		this.queues = this.queueConfigProperty.getQueue();
		this.executors = new HashMap<String, ThreadPoolExecutor>();
		this.lock = new Object();
	}

	@PostConstruct
	private void loadQueue() {
		// Create the queues
		for (Queue queue : this.queues) {
			logger.info("Starting executor for '" + queue.getBeanId() 
				+ "' [pool-name: '" + queue.getPoolName() + "']"
				+ "' [core-pool-size: '" + queue.getCorePoolSize() + "']"
				+ "' [maximum-pool-size: '" + queue.getMaximumPoolSize() + "']"
				+ "' [keep-alive-time: '" + queue.getKeepAliveTime() + "']");
			this.executors.put(
					queue.getBeanId(),
					ExecutorServiceUtils.createDefaultExecutorService(
							queue.getPoolName(), 
							queue.getCorePoolSize(), 
							queue.getMaximumPoolSize(), 
							queue.getKeepAliveTime())
			);
		}
		
		/**
		 * Once the queues are created, add those tasks that were previously created to the queue, so that they can be process.
		 * To be processed, the runnable needs to be re-created and re-submitted
		 **/
		List<GenericTaskEntity> tasks = this.genericTaskService.findAll();
		for (GenericTaskEntity task : tasks) {
			logger.info("Re-building task queues. Adding task " + task.toString());
			this.createRunnableAndExecuteIt(task);
		}
	}
	
	/**
	 * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. 
	 * Invocation has no additional effect if already shut down. This method does not wait for previously submitted tasks 
	 * to complete execution.
	 **/
	@PreDestroy
	public void cleanUp() {
		for (Queue queue : this.queues) {
			logger.info("Shutting down executor for '" + queue.getBeanId() + "'");
			this.executors.get(queue.getBeanId()).shutdown();
		}		
	}
	
	/**
	 * Adds a new task to its respective queue. This is done in two steps:
	 * <ul>
	 * 	<ol>Save the task in the database</ol>
	 * 	<ol>Call the threadpool's execute function</ol>
	 * </ul>
	 * @param task	- The task that has to be save and executed
	 **/
	private void createRunnableAndExecuteIt(GenericTaskEntity task) {
		GenericTaskRunnable runnable = this.applicationContext.getBean(GenericTaskRunnable.class);
		runnable.setTask(task);
		logger.info("Submitting task " + task.toString());
		this.executors.get(task.getBeanId()).execute(runnable);
	}
	
	/**
	 * Adds a new task to its respective queue.
	 * @param task	- The task that has to be save and executed
	 **/
	public void submit(GenericTaskEntity task) {
		synchronized (lock) {
			if(task.getUuid() == null)
				task.setUuid(this.strongUuidGenerator.getNextId());
			
			if(task.getStatus() == null)
				task.setStatus(BehaviorTaskStatus.WAITING.getStatus());
			
			this.genericTaskService.save(task);
			this.createRunnableAndExecuteIt(task);
		}
	}
	
	/**
	 * Deletes a task from its respective queue
	 * @param task	- The task that has to be deleted
	 * @throws GenericException
	 */
	public void delete(GenericTaskEntity task) throws GenericException {
		synchronized (lock) {
			GenericTaskRunnable runnable = this.applicationContext.getBean(GenericTaskRunnable.class);
			runnable.setTask(task);
			if(this.executors.get(task.getBeanId()).remove(runnable)) {
				this.genericTaskService.delete(task);
				logger.info("Delete task " + task.toString());
			} else {
				String msg = "Could not delete task " + task.toString();
				logger.error(msg);
				throw new GenericException(msg);
			}
		}
	}
	
	
	/**
	 * Updates the task priority related to the execution id. This is done by:
	 * <ol>
	 * 	<li>Searching the task in the queue</li>
	 * 	<li>Removing it from the queue</li>
	 * 	<li>Re-submitting the task again with the modified priority</li>
	 * 	<li>Update the "priority" variable in Activiti's process instance</li>
	 * </ol>
	 * @param executionId	- the execution id which task priority will be changed
	 * @param priority		- the value to which the priority will be changed too
	 * @throws ObjectNotFoundException
	 **/
	public void changeTaskPriorityByExecutionId(String executionId, Integer priority) throws ObjectNotFoundException {
		synchronized (lock) {
			GenericTaskEntity task = this.genericTaskService.findByExecutionId(executionId);
			
			if(task == null)
				throw new ObjectNotFoundException("No task related to execution id '" + executionId + "' found");
			
			GenericTaskRunnable runnable = this.applicationContext.getBean(GenericTaskRunnable.class);
			runnable.setTask(task);
			
			PriorityBlockingQueue<Runnable> workQueue = (PriorityBlockingQueue<Runnable>) this.executors.get(task.getBeanId()).getQueue();
			
			if(workQueue.contains(runnable)) {
				workQueue.remove(runnable);
				task.setPriority(priority);
				this.runtimeService.setVariable(task.getExecutionId(), "priority", priority);
				this.submit(task);
			}
		}
	}
}