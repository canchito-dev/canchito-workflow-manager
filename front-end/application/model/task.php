<?php
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
 * @author 		Jose Carlos Mendoza Prego
 * @copyright	Copyright (c) 2017, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
namespace Application\Model;

use Application\Libs\Http\HttpClient;

class Task extends HttpClient {
	
	private $uri = array(
	    'GET_A_SINGLE_TASK' => 'runtime/tasks/{taskId}',
	    'GET_A_LIST_OF_TASKS' => 'runtime/tasks',
	    'UPDATE_TASK' => 'runtime/tasks/{taskId}',
	    'DELETE_TASK' => 'runtime/tasks/{taskId}?cascadeHistory={cascadeHistory}&deleteReason={deleteReason}',
	    'TASK_ACTION' => 'runtime/tasks/{taskId}',
	    'GET_ALL_VARIABLES_FOR_A_TASK' => 'runtime/tasks/{taskId}/variables'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BPMN_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the task based on the provided task id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_a_task
	 * @param string $taskId	- The id of the task to get
	 * @return	200 - Indicates the task was found and returned
	 * 			404 - Indicates the requested task was not found
	 **/
	public function getSingleTask($taskId) {
	    $uri = str_replace('{taskId}', $taskId, $this->uri['GET_A_SINGLE_TASK']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of tasks following the provided query parameters (if they exists)
	 * @link https://www.flowable.org/docs/userguide/index.html#restTasksGet
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates the requested users were returned
	 *          400 - Indicates a parameter was passed in the wrong format or that delegationState has an invalid value (other than pending and resolved). 
	 *                The status-message contains additional information.
	 **/
	public function getListOfTasks($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_TASKS']);
	}
	
	/**
	 * Updates a task information
	 * @link https://www.flowable.org/docs/userguide/index.html#_update_a_task
	 * @param string $taskId	- The id of the task to update
	 * @param array $body		- The request body as JSON
	 * @return	200 - Indicates the task was updated
	 * 			404 - Indicates the requested task was not found
	 * 			409 - Indicates the requested task was updated simultaneously.
	 **/
	public function updateTask($taskId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{taskId}', $taskId, $this->uri['UPDATE_TASK']);
	    return $this->put($uri);
	}
	
	/**
	 * Deletes a task
	 * @param string $taskId           - The id of the task to delete. Required
	 * @param boolean $cascadeHistory  - Whether or not to delete the HistoricTask instance when deleting the task (if applicable). If not provided, this value defaults to false. Not Required
	 * @param string $deleteReason     - Reason why the task is deleted. This value is ignored when cascadeHistory is true. Not required
	 * @link https://www.flowable.org/docs/taskguide/index.html#_delete_a_task
	 * @return	204 - Indicates the task was found and  has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested task was not found
	 **/
	public function deleteTask($taskId, $cascadeHistory = FALSE, $deleteReason = 'not specified') {
	    $uri = str_replace('{taskId}', $taskId, $this->uri['DELETE_TASK']);
	    $uri = str_replace('{cascadeHistory}', $cascadeHistory, $uri);
	    $uri = str_replace('{deleteReason}', $deleteReason, $uri);
	    return $this->delete($uri);
	}
	
	/**
	 * Sets task action
	 * @link https://www.flowable.org/docs/userguide/index.html#_task_actions
	 * @param string $taskId    - The id of the task
	 * @param array $body		- The request body as JSON
	 *     Complete a task - Body JSON:
	 *         {
	 *             "action" : "complete",
	 *             "variables" : []        => Completes the task. Optional variable array can be passed in using the variables property
	 *         }
	 *     Claim a task - Body JSON:
	 *         {
	 *             "action" : "claim",
	 *             "assignee" : "userWhoClaims"
	 *         }
	 *     Delegate a task - Body JSON:
	 *         {
	 *             "action" : "delegate",
	 *             "assignee" : "userToDelegateTo"
	 *         }
	 *     Resolve a task - Body JSON
	 *         { "action" : "resolve" }
	 * @return	200 - Indicates the action was executed.
	 * 			400 - When the body contains an invalid value or when the assignee is missing when the action requires it.
	 *          404 - Indicates the requested task was not found.
	 *          409 - Indicates the action cannot be performed due to a conflict. Either the task was updates simultaneously or the task was claimed by another user, in case of the claim action.
	 **/
	public function taskAction($taskId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{taskId}', $taskId, $this->uri['TASK_ACTION']);
	    return $this->post($uri);
	}
	
	/**
	 * Get all variables for a task, both global and local
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_all_variables_for_a_task
	 * @param string $taskId	- The id of the task which variables to get
	 * @return	200 - Indicates the task was found and the requested variables are returned.
	 * 			404 - Indicates the requested task was not found.
	 **/
	public function getAllVariablesForATask($taskId) {
	    $uri = str_replace('{taskId}', $taskId, $this->uri['GET_ALL_VARIABLES_FOR_A_TASK']);
	    return $this->get($uri);
	}
}