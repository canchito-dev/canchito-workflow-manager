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
package com.canchitodev.cwm.flowable.eventhandlers;

import org.apache.log4j.Logger;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableVariableEventImpl;
import org.flowable.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.flowable.variable.service.impl.persistence.entity.HistoricVariableInstanceEntityImpl;
import org.flowable.variable.service.impl.persistence.entity.VariableInstanceEntityImpl;

public class DefaultEventHandler implements FlowableEventListener {
	
	private static final Logger logger = Logger.getLogger(DefaultEventHandler.class);

	@Override
	public void onEvent(FlowableEvent  event) {
		switch ((FlowableEngineEventType) event.getType()) {
			case ENGINE_CREATED:
				/**
				 * The process-engine that dispatched this event has been created and is ready for use.
				 * The process-engine this listener is attached to, has been created and is ready for API-calls.
				 **/
				logger.info("A new Flowable engine has been created");
				break;

			case ENGINE_CLOSED:
				/**
				 * The process-engine that dispatched this event has been closed and cannot be used anymore.
				 * The process-engine this listener is attached to, has been closed. API-calls to the engine are no longer possible.
				 **/
				logger.info(event.toString());
				break;
				
			case ENTITY_CREATED:
				/**
				 * New entity is created. A new entity is created. The new entity is contained in the event.
				 **/
				FlowableEntityEventImpl entityCreated = (FlowableEntityEventImpl) event;
				if(entityCreated.getEntity() instanceof ProcessDefinitionEntityImpl) {}
				if(entityCreated.getEntity() instanceof ExecutionEntityImpl) {}
				if(entityCreated.getEntity() instanceof VariableInstanceEntityImpl) {}
				if(entityCreated.getEntity() instanceof HistoricVariableInstanceEntityImpl) {}
				if(entityCreated.getEntity() instanceof HistoricProcessInstanceEntityImpl) {}
				logger.info(event.toString());
				break;

			case ENTITY_INITIALIZED:
				/**
				 * New entity has been created and all child-entities that are created as a result of the creation of this particular 
				 * entity are also created and initialized.
				 * A new entity has been created and is fully initialized. If any children are created as part of the creation of an 
				 * entity, this event will be fired AFTER the create/initialization of the child entities as opposed to the ENTITY_CREATE event.
				 **/
				FlowableEntityEventImpl entityInitialized = (FlowableEntityEventImpl) event;
				if(entityInitialized.getEntity() instanceof ProcessDefinitionEntityImpl) {}
				if(entityInitialized.getEntity() instanceof DeploymentEntityImpl) {}
				if(entityInitialized.getEntity() instanceof ExecutionEntityImpl) {}
				if(entityInitialized.getEntity() instanceof VariableInstanceEntityImpl) {}
				if(entityInitialized.getEntity() instanceof HistoricProcessInstanceEntityImpl) {}
				logger.info(event.toString());
				break;

			case ENTITY_UPDATED:
				/**
				 * Existing entity us updated. An existing is updated. The updated entity is contained in the event.
				 **/
				logger.info(event.toString());
				break;

			case ENTITY_DELETED:
				/**
				 * Existing entity is deleted. An existing entity is deleted. The deleted entity is contained in the event.
				 **/
				FlowableEntityEventImpl entityDeleted = (FlowableEntityEventImpl) event;
				if(entityDeleted.getEntity() instanceof ProcessDefinitionEntityImpl) {}
				if(entityDeleted.getEntity() instanceof DeploymentEntityImpl) {}
				logger.info(event.toString());
				break;

			case ENTITY_SUSPENDED:
				/**
				 * Existing entity has been suspended. An existing entity is suspended. The suspended entity is contained in the event. 
				 * Will be dispatched for ProcessDefinitions, ProcessInstances and Tasks.
				 **/
				logger.info(event.toString());
				break;

			case ENTITY_ACTIVATED:
				/**
				 * Existing entity has been activated. An existing entity is activated. The activated entity is contained in the event. 
				 * Will be dispatched for ProcessDefinitions, ProcessInstances and Tasks.
				 **/
				logger.info(event.toString());
				break;

			case TIMER_SCHEDULED:
				/**
				 * A Timer has been scheduled.
				 **/
				logger.info(event.toString());
				break;

			case TIMER_FIRED:
				/**
				 * Timer has been fired successfully. A timer has been fired. The event contains the job that was executed?
				 **/
				logger.info(event.toString());
				break;

			case JOB_CANCELED:
				/**
				 * Timer has been cancelled (e.g. user task on which it was bounded has been completed earlier than expected)
				 * A job has been canceled. The event contains the job that was canceled. Job can be canceled by API call, task 
				 * was completed and associated boundary timer was canceled, on the new process definition deployment.
				 **/
				logger.info(event.toString());
				break;

			case JOB_EXECUTION_SUCCESS:
				/**
				 * A job has been successfully executed. A job has been executed successfully. The event contains the job that was executed.
				 **/
				logger.info(event.toString());
				break;

			case JOB_EXECUTION_FAILURE:
				/**
				 * A job has been executed, but failed. Event should be an instance of a {@link ActivitiExceptionEvent}.
				 * The execution of a job has failed. The event contains the job that was executed and the exception.
				 **/
				logger.info(event.toString());
				break;

			case JOB_RETRIES_DECREMENTED:
				/**
				 * The retry-count on a job has been decremented. The number of job retries have been decremented due to a failed 
				 * job. The event contains the job that was updated.
				 **/
				logger.info(event.toString());
				break;

			case JOB_RESCHEDULED:
				/**
				 * The job has been rescheduled.
				 **/
				logger.info(event.toString());
				break;

			default:
			case CUSTOM:
				/**
				 * An event type to be used by custom events. These types of events are never thrown by the engine itself, only be 
				 * an external API call to dispatch an event.
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_STARTED:
				/**
				 * An activity is starting to execute. This event is dispatch right before an activity is executed.
				 * An activity is starting to execute
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_COMPLETED:
				/**
				 * An activity has been completed successfully. An activity is completed successfully
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_CANCELLED:
				/**
				 * An activity has been cancelled because of boundary event. An activity is going to be cancelled. There can be 
				 * three reasons for activity cancellation (MessageEventSubscriptionEntity, SignalEventSubscriptionEntity, TimerEntity).
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_SIGNAL_WAITING:
				/**
			     * A boundary, intermediate, or subprocess start signal catching event has started.
			     **/
				logger.info(event.toString());
				break;

			case ACTIVITY_SIGNALED:
				/**
				 * An activity has received a signal. Dispatched after the activity has responded to the signal.
				 * An activity received a signal
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_COMPENSATE:
				/**
				 * An activity is about to be executed as a compensation for another activity. The event targets the activity that is 
				 * about to be executed for compensation. An activity is about to be compensated. The event contains the id of the 
				 * activity that is will be executed for compensation.
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_MESSAGE_WAITING:		  
				/**
				 * A boundary, intermediate, or subprocess start message catching event has started and it is waiting for message.
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_MESSAGE_RECEIVED:
				/**
				 * An activity has received a message event. Dispatched before the actual message has been received by the activity. 
				 * This event will be either followed by a {@link #ACTIVITY_SIGNALLED} event or
				 * {@link #ACTIVITY_COMPLETE} for the involved activity, if the message was delivered successfully.
				 * An activity received a message. Dispatched before the activity receives the message. When received, a ACTIVITY_SIGNAL 
				 * or ACTIVITY_STARTED will be dispatched for this activity, depending on the type (boundary-event or event-subprocess start-event)
				 **/
				logger.info(event.toString());
				break;

			case ACTIVITY_MESSAGE_CANCELLED:
				/**
			     * An activity has received a message event. Dispatched before the actual message has been received by the activity. This event will be either followed by a {@link #ACTIVITY_SIGNALLED} event or
			     * {@link #ACTIVITY_COMPLETED} for the involved activity, if the message was delivered successfully.
			     **/
				logger.info(event.toString());
				break;

			case ACTIVITY_ERROR_RECEIVED:
				/**
				 * An activity has received an error event. Dispatched before the actual error has been received by the activity. 
				 * This event will be either followed by a {@link #ACTIVITY_SIGNALLED} event or
				 * {@link #ACTIVITY_COMPLETE} for the involved activity, if the error was delivered successfully.
				 * An activity has received an error event. Dispatched before the actual error has been handled by the activity. 
				 * The event’s activityId contains a reference to the error-handling activity. This event will be either followed 
				 * by a ACTIVITY_SIGNALLED event or ACTIVITY_COMPLETE for the involved activity, if the error was delivered successfully.
				 **/
				logger.info(event.toString());
				break;

			case HISTORIC_ACTIVITY_INSTANCE_CREATED:		  
				/**
				 * A event dispatched when a {@link HistoricActivityInstance} is created. 
				 * This is a specialized version of the {@link ActivitiEventType#ENTITY_CREATED} and {@link ActivitiEventType#ENTITY_INITIALIZED} 
				 * event, with the same use case as the {@link ActivitiEventType#ACTIVITY_STARTED}, but containing slightly different data.
				 * 
				 * Note this will be an {@link ActivitiEntityEvent}, where the entity is the {@link HistoricActivityInstance}.
				 *  
				 * Note that history (minimum level ACTIVITY) must be enabled to receive this event.  
				 **/
				logger.info(event.toString());
				break;

			case HISTORIC_ACTIVITY_INSTANCE_ENDED:		  
				/**
				 * A event dispatched when a {@link HistoricActivityInstance} is marked as ended. 
				 * his is a specialized version of the {@link ActivitiEventType#ENTITY_UPDATED} event,
				 * with the same use case as the {@link ActivitiEventType#ACTIVITY_COMPLETED}, but containing
				 * slightly different data (e.g. the end time, the duration, etc.). 
				 *  
				 * Note that history (minimum level ACTIVITY) must be enabled to receive this event.  
				 **/
				logger.info(event.toString());
				break;

			case SEQUENCEFLOW_TAKEN:
				/**
				 * Indicates the engine has taken (ie. followed) a sequence flow from a source activity to a target activity.
				 **/
				logger.info(event.toString());
				break;

//			case UNCAUGHT_BPMN_ERROR:
//				/**
//				 * When a BPMN Error was thrown, but was not caught within in the process. The process did not have any handlers 
//				 * for that specific error. The event’s activityId will be empty.
//				 **/
//				logger.info(event.toString());
//				break;

			case VARIABLE_CREATED:
				/**
				 * A new variable has been created.. The event contains the variable name, value and related execution and task (if any).
				 **/
				FlowableVariableEventImpl variableCreated = (FlowableVariableEventImpl) event;
				logger.info(variableCreated.toString());
				break;

			case VARIABLE_UPDATED:
				/**
				 * An existing variable has been updated. The event contains the variable name, updated value and related execution and task (if any).
				 **/
				logger.info(event.toString());
				break;

			case VARIABLE_DELETED:
				/**
				 * An existing variable has been deleted. The event contains the variable name, last known value and related execution and task (if any).
				 **/
				logger.info(event.toString());
				break;

			case TASK_CREATED:
				/**
				 * A task has been created. This is thrown when task is fully initialized (before TaskListener.EVENTNAME_CREATE). This is 
				 * dispatched after the ENTITY_CREATE event. In case the task is part of a process, this event will be fired before the task 
				 * listeners are executed.
				 **/
				logger.info(event.toString());
				break;

			case TASK_ASSIGNED:
				/**
				 * A task has been assigned to a user. This is thrown alongside with an {@link #ENTITY_UPDATED} event.
				 * The event contains the task
				 **/
				logger.info(event.toString());
				break;

			case TASK_COMPLETED:
				/**
				 * A task has been completed. Dispatched before the task entity is deleted ( {@link #ENTITY_DELETED}). If the task is part of a 
				 * process, this event is dispatched before the process moves on, as a result of the task completion. In that case, a 
				 * {@link #ACTIVITY_COMPLETED} will be dispatched after an event of this type for the activity corresponding to the task.
				 * In case the task is part of a process, this event will be fired before the process has moved on and will be followed by a 
				 * ACTIVITY_COMPLETE event, targeting the activity that represents the completed task.
				 **/
				logger.info(event.toString());
				break;

			case PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT:
				/**
			     * A process has been cancelled. Dispatched when process instance is deleted by
			     * 
			     * @see org.flowable.engine.impl.RuntimeServiceImpl#deleteProcessInstance(java.lang.String, java.lang.String), before DB delete.
			     **/
				FlowableEntityEventImpl processCompletedWithTerminateEndEvent = (FlowableEntityEventImpl) event;
				logger.info(processCompletedWithTerminateEndEvent.toString());
				break;

			case PROCESS_COMPLETED_WITH_ERROR_END_EVENT:		  
				/**
				 * A process has been completed with an error end event.
				 **/
				logger.info(event.toString());
				break;
				
			case PROCESS_CREATED:
				/**
			     * A process instance has been created. All basic properties have been set, but variables not yet.
			     **/
				FlowableEntityEventImpl processCreated = (FlowableEntityEventImpl) event;
				logger.info(processCreated.toString());
				break;

			case PROCESS_STARTED:
			    /**
			     * A process instance has been started. Dispatched when starting a process instance previously created. The event
			     * PROCESS_STARTED is dispatched after the associated event ENTITY_INITIALIZED.
			     **/
				FlowableProcessStartedEventImpl processStarted = (FlowableProcessStartedEventImpl) event;
				if(processStarted.getEntity() instanceof ExecutionEntityImpl) {}
				logger.info(event.toString());
				break;

			case PROCESS_COMPLETED:
				/**
				 * A process has been completed. Dispatched after the last activity is ACTIVITY_COMPLETED. Process is completed when it reaches 
				 * state in which process instance does not have any transition to take.
				 **/
				logger.info(event.toString());
				break;

			case PROCESS_CANCELLED:
				/**
				 * A process has been cancelled. Dispatched before the process instance is deleted from runtime. Process instance is cancelled by 
				 * API call RuntimeService.deleteProcessInstance
				 * @see org.activiti.engine.impl.RuntimeServiceImpl#deleteProcessInstance(java.lang.String, java.lang.String), before DB delete.
				 **/
				logger.info(event.toString());
				break;

			case HISTORIC_PROCESS_INSTANCE_CREATED:		  
				/**
				 * A event dispatched when a {@link HistoricProcessInstance} is created. 
				 * This is a specialized version of the {@link ActivitiEventType#ENTITY_CREATED} and 
				 * {@link ActivitiEventType#ENTITY_INITIALIZED} event, with the same use case as the 
				 * {@link ActivitiEventType#PROCESS_STARTED}, but containing slightly different data 
				 * (e.g. the start time, the start user id, etc.). 
				 * 
				 * Note this will be an {@link ActivitiEntityEvent}, where the entity is the {@link HistoricProcessInstance}.
				 *  
				 * Note that history (minimum level ACTIVITY) must be enabled to receive this event.  
				 **/
				FlowableEntityEventImpl historicProcessInstanceCreated = (FlowableEntityEventImpl) event;
				if(historicProcessInstanceCreated.getEntity() instanceof HistoricProcessInstanceEntityImpl) {}
				logger.info(event.toString());
				break;

			case HISTORIC_PROCESS_INSTANCE_ENDED:		  
				/**
				 * A event dispatched when a {@link HistoricProcessInstance} is marked as ended. 
				 * his is a specialized version of the {@link ActivitiEventType#ENTITY_UPDATED} event,
				 * with the same use case as the {@link ActivitiEventType#PROCESS_COMPLETED}, but containing
				 * slightly different data (e.g. the end time, the duration, etc.). 
				 *  
				 * Note that history (minimum level ACTIVITY) must be enabled to receive this event.  
				 **/
				logger.info(event.toString());
				break;

//			case MEMBERSHIP_CREATED:
//				/**
//				 * A new membership has been created. A user has been added to a group. The event contains the ids of the user and group involved.
//				 **/
//				logger.info(event.toString());
//				break;
//
//			case MEMBERSHIP_DELETED:
//				/**
//				 * A single membership has been deleted. A user has been removed from a group. The event contains the ids of the user and group involved.
//				 **/
//				logger.info(event.toString());
//				break;
//
//			case MEMBERSHIPS_DELETED:
//				/**
//				 * All memberships in the related group have been deleted. No individual {@link #MEMBERSHIP_DELETED} events will be dispatched due to 
//				 * possible performance reasons. The event is dispatched before the memberships are deleted, so they can still be accessed in the 
//				 * dispatch method of the listener.
//				 **/
//				logger.info(event.toString());
//				break;
				
			case MULTI_INSTANCE_ACTIVITY_STARTED:
				/**
			     * A multi-instance activity is starting to execute. This event is dispatched right before an activity is executed.
			     */
				logger.info(event.toString());
				break;

			case MULTI_INSTANCE_ACTIVITY_COMPLETED:
			    /**
			     * A multi-instance activity has been completed successfully.
			     */
				logger.info(event.toString());
				break;

			case MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION:
			    /**
			     * A multi-instance activity has met its condition and completed successfully.
			     */
				logger.info(event.toString());
				break;

			case MULTI_INSTANCE_ACTIVITY_CANCELLED:
			    /**
			     * A multi-instance activity has been cancelled.
			     */
				logger.info(event.toString());
				break;
		}
	}

	/**
	 * Determines the behavior in case the onEvent(..) method throws an exception when an event is dispatched. 
	 * In case false is returned, the exception is ignored. When true is returned, the exception is not ignored 
	 * and bubbles up, effectively failing the current ongoing command.
	 * @see org.activiti.engine.delegate.event.ActivitiEventListener#isFailOnException()
	 **/
	@Override
	public boolean isFailOnException() {
		/**
		 * The logic in the onEvent method of this listener is not critical, exceptions can be ignored if logging fails...
		 **/
		return false;
	}

	@Override
	public String getOnTransaction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		// TODO Auto-generated method stub
		return false;
	}
}