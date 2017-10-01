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
package com.canchitodev.cwm.behavior;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.log4j.Logger;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.bpmn.behavior.TaskActivityBehavior;
import org.flowable.engine.impl.bpmn.helper.ErrorPropagation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.canchitodev.cwm.domain.GenericTaskEntity;
import com.canchitodev.cwm.threadpool.service.GenericTaskService;
import com.canchitodev.cwm.threadpool.service.TasksQueueService;
import com.canchitodev.cwm.utils.enums.BehaviorTaskStatus;

public abstract class AbstractTaskCanchitoBehavior extends TaskActivityBehavior {

	private static final Logger logger = Logger.getLogger(AbstractTaskCanchitoBehavior.class);
	
	private static final long serialVersionUID = -5810695509959387845L;

	@Autowired
	protected TasksQueueService tasksQueueService;
	
	@Autowired
	private GenericTaskService genericTaskService;
	
	protected final AtomicLong counter = new AtomicLong();
	
	/**
	 * Used for validating the parameters sent to the task behavior
	 * @param execution - Execution used in JavaDelegates and ExecutionListeners.
	 * @throws IllegalArgumentException
	 **/
	protected abstract void validateParameters(DelegateExecution execution) throws IllegalArgumentException;
	
	/**
	 * Submits a task to its respective queue for its execution
	 * @param execution	- Execution used in JavaDelegates and ExecutionListeners.
	 * @param details 	- The detail information needed for execution the task 
	 * @param beanId	- The id of the bean which will be used for executing the task
	 **/
	protected void submitTask(DelegateExecution execution, JSONObject details, String beanId) {
		// Create generic task entity that will be used in the runnable
		GenericTaskEntity task = new GenericTaskEntity();
		task.setDetails(details);
		task.setExecutionId(execution.getId());
		task.setPriority(this.getTaskPriority(execution));
		task.setProcessDefinitionId(execution.getProcessDefinitionId());
		task.setProcessInstanceId(execution.getProcessInstanceId());
		task.setTenantId(execution.getTenantId());
		task.setBeanId(beanId);
		this.tasksQueueService.submit(task);
	}
	
	public void trigger(DelegateExecution execution, String signalName, Object signalData) {
		this.checkSignal(execution);
		leave(execution);
	}
	
	/**
	 * The method searches for a variable called priority. If it exists, it returns its value
	 * as an Integer and it is used as the task's priority.
	 * @param execution - Execution used in JavaDelegates and ExecutionListeners.
	 * @return The value of the variable priority if found. 0 if not found
	 **/
	protected Integer getTaskPriority(DelegateExecution execution) {
		if(execution.hasVariable("priority"))
			return Integer.parseInt(execution.getVariable("priority").toString());
		else
			return 0;
	}

	/**
	 * This method verifies if the runnable successfully finished or not.
	 * If there was an error, it uses Activiti's error propagation function.
	 * @param execution - Execution used in JavaDelegates and ExecutionListeners.
	 **/
	protected void checkSignal(DelegateExecution execution) {		
		GenericTaskEntity task = this.genericTaskService.findByExecutionId(execution.getId());
		
		if(task == null)
			this.propagateException(execution);
		
		logger.info("Validating signal for task '" + task.toString() + "'");

		this.genericTaskService.delete(task);
		
		// If there was an error during the execution of the task, we throw an error
		if(BehaviorTaskStatus.get(task.getStatus()).equals(BehaviorTaskStatus.ERROR))
			this.propagateException(execution, "runnableError");		
	}
	
	/**
	 * This method calls Activiti's error propagation method.
	 * <strong>Note:</strong> It should only be called from within Activiti's trigger method
	 * @param execution 	- Execution used in JavaDelegates and ExecutionListeners.
	 * @param errorVariable - The name of the Activiti's variable which has the error message
	 **/
	public void propagateException(DelegateExecution execution, String errorVariable) {
		BpmnError error = new BpmnError("bpmnError", "Error when execution id '" + execution.getId() + "' "
				+ "- Exception: " + execution.getVariable(errorVariable));
		ErrorPropagation.propagateError(error, execution);
	}
	
	/**
	 * This method calls Activiti's error propagation method.
	 * <strong>Note:</strong> It should only be called from within Activiti's trigger method
	 * @param execution - Execution used in JavaDelegates and ExecutionListeners.
	 **/
	public void propagateException(DelegateExecution execution) {
		BpmnError error = new BpmnError("bpmnError", "Error when execution id '" + execution.getId() + "'");
		ErrorPropagation.propagateError(error, execution);
	}
	
	/**
	 * Creates a BpmnError (special exception that can be used to throw a BPMN Error) and throws it
	 * @param execution - Execution used in JavaDelegates and ExecutionListeners.
	 * @param message	- The message that should be thrown
	 **/
	public void throwException(DelegateExecution execution, String message) {
		this.throwException("Error when execution id '" + execution.getId() + "' - Exception: " + message);	
	}
	
	/**
	 * Creates a BpmnError (special exception that can be used to throw a BPMN Error) and throws it
	 * @param message - The message that should be thrown
	 **/
	public void throwException(String message) {
		throw new BpmnError("bpmnError", message);	
	}
}