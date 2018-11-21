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

class ProcessInstance extends HttpClient {
	
	private $uri = array(
	    'GET_A_SINGLE_PROCESS_INSTANCE' => 'runtime/process-instances/{processInstanceId}',
	    'GET_A_LIST_OF_PROCESS_INSTANCES' => 'runtime/process-instances',
	    'ACTIVATE_OR_SUSPEND' => 'runtime/process-instances/{processInstanceId}',
	    'DELETE_PROCESS_INSTANCE' => 'runtime/process-instances/{processInstanceId}',
	    'START_PROCESS_INSTANCE' => 'runtime/process-instances',
	    'GET_DIAGRAM_FOR_PROCESS_INSTANCE' => 'runtime/process-instances/{processInstanceId}/diagram'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BPMN_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the process instance based on the provided process instance id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_a_process_instance
	 * @param string $processInstanceId	- The id of the process instance to get.
	 * @return	200 - Indicates the process instance was found and returned.
	 * 			404 - Indicates the requested process instance was not found.
	 **/
	public function getSingleProcessInstance($processInstanceId) {
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['GET_A_SINGLE_PROCESS_INSTANCE']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of process instances following the provided query parameters (if they exists)
	 * @link https://www.flowable.org/docs/userguide/index.html#restProcessInstancesGet
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates request was successful and the process instances are returned
	 * 			400 - Indicates a parameter was passed in the wrong format or that latest is used with other parameters other 
	 *                than key and keyLike. The status-message contains additional information.
	 **/
	public function getListOfProcessInstances($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_PROCESS_INSTANCES']);
	}
	
	/**
	 * Activates/Suspends a process instance
	 * @link https://www.flowable.org/docs/userguide/index.html#_activate_or_suspend_a_process_instance
	 * @param string $processInstanceId	- The id of the process instance to activate or suspend
	 * @param array $body		- The request body as JSON
	 *                             {
	 *                                 "action" : "suspend"    - Action to perform. Either activate or suspend. Required
	 *                             }
	 * @return	200 - Indicates the process was suspended.
	 * 			404 - Indicates the requested process instance was not found.
	 *          400 - Indicates an invalid action was supplied.
	 *          409 - Indicates the requested process instance is already suspended.
	 **/
	public function activateOrSuspendProcessInstance($processInstanceId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['ACTIVATE_OR_SUSPEND']);
	    return $this->put($uri);
	}
	
	/**
	 * Deletes a process instance
	 * @param string $processInstanceId	- The id of the process instance to delete
	 * @link https://www.flowable.org/docs/userguide/index.html#_delete_a_process_instance
	 * @return	204 - Indicates the process instance was found and deleted. Response body is left empty intentionally.
	 * 			404 - Indicates the requested process instance was not found.
	 **/
	public function deleteProcessInstance($processInstanceId) {
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['DELETE_PROCESS_INSTANCE']);
	    return $this->delete($uri);
	}
	
	/**
	 * Starts a new process instance
	 * @link https://www.flowable.org/docs/userguide/index.html#_start_a_process_instance_2
	 * @param array $body		- The request body as JSON
	 * @return	201 - Indicates the process instance was created.
	 * 			400 - Indicates either the process-definition was not found (based on id or key), no process is started by sending the given message or an invalid variable has been passed. Status description contains additional information about the error.
	 **/
	public function startProcessInstance($body = []) {
	    $this->setJsonBody($body);
	    return $this->post($this->uri['START_PROCESS_INSTANCE']);
	}
	
	/**
	 * Get the diagram for a process instance based on the provided process instance id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_diagram_for_a_process_instance
	 * @param string $processInstanceId	- The id of the process instance to get.
	 * @return	200 - Indicates the process instance was found and the diagram was returned.
	 *          400 - Indicates the requested process instance was not found but the process doesn’t contain any graphical information (BPMN:DI) and no diagram can be created.
	 * 			404 - Indicates the requested process instance was not found.
	 **/
	public function getDiagramForProcessInstance($processInstanceId) {
	    $this->setHeaders([
	        'Content-Disposition' => 'inline',
	        'Content-Time' => 'image/png'
	    ]);
	    $this->setJsonDecodeBody(FALSE);
	    $this->setStream(TRUE);
	    $uri = str_replace('{processInstanceId}', $processInstanceId, $this->uri['GET_DIAGRAM_FOR_PROCESS_INSTANCE']);
 	    return $this->get($uri);
	}
}