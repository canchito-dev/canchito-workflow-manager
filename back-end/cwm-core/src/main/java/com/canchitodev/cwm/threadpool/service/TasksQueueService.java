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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.threadpool.utils.AcquireTaskThread;
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
	
	private HashMap<String, AcquireTaskThread> acquireTaskThread;
	
	@Value("${server.tenant-Id}")
	private String serverTenantId = "canchito-dev.com";
	
	@Autowired
	private TaskQueueFactory taskQueueFactory;
	
	private Object lock;
	
	@Autowired
	private GenericTaskService genericTaskService;
	
	@Autowired
	private StrongUuidGenerator strongUuidGenerator;

	@Autowired
	public TasksQueueService() {
		this.acquireTaskThread = new HashMap<String, AcquireTaskThread>();
	}

	@PostConstruct
	private void loadQueue() {
		Map<String, TaskQueue> queues = this.taskQueueFactory.getAllTaskQueues();
		
		Set<String> keys = queues.keySet();
		
		for (Iterator<String> i = keys.iterator(); i.hasNext(); ) {
			String beanName = (String) i.next();
			
			TaskQueue taskQueue = (TaskQueue) queues.get(beanName);
			String asyncThreadName = "acquire-" + taskQueue.getPoolName();

			logger.info("Initializing acquire task thread '" + asyncThreadName + "'");
			this.acquireTaskThread.put(
					asyncThreadName, 
					new AcquireTaskThread(
							this.serverTenantId,
							this.strongUuidGenerator.getNextId(), 
							taskQueue
					)
			);
			
			logger.info("Starting async task thread '" + asyncThreadName + "'");
			this.acquireTaskThread.get(asyncThreadName).start();
		}
	}
	
	/**
	 * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted. 
	 * Invocation has no additional effect if already shut down. This method does not wait for previously submitted tasks 
	 * to complete execution.
	 **/
	@PreDestroy
	public void onShutDown() {
		Set<String> keys = acquireTaskThread.keySet();
		
		for (Iterator<String> key = keys.iterator(); key.hasNext(); ) {
			String taskName = (String) key.next();
			logger.info("Shutting down acquire task thread '" + taskName + "'");
			this.acquireTaskThread.get(taskName).setShutDown(true);
		}		
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
		}
	}
}