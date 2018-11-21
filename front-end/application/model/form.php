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

class Form extends HttpClient {
	
	private $uri = array(
	    'GET_PROCESS_DEFINITION_FORM_DATA' => 'form/form-data?processDefinitionId={processDefinitionId}',
	    'GET_TASK_FORM_DATA' => 'form/form-data?taskId={taskId}',
	    'SUBMIT_FORM_DATA' => 'form/form-data'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BPMN_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the form data of the process definition
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_form_data
	 * @param string $processDefinitionId	- The id of the process definition to get
	 * @return	200 - Indicates that form data could be queried.
	 * 			404 -Indicates that form data could not be found.
	 **/
	public function getProcessDefinitionFormData($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['GET_PROCESS_DEFINITION_FORM_DATA']);
		return $this->get($uri);
	}
	
	/**
	 * Get the form data of the task
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_form_data
	 * @param string $taskId	- The id of the task to get
	 * @return	200 - Indicates that form data could be queried.
	 * 			404 -Indicates that form data could not be found.
	 **/
	public function getTaskFormData($taskId) {
	    $uri = str_replace('{taskId}', $taskId, $this->uri['GET_TASK_FORM_DATA']);
	    return $this->get($uri);
	}
	
	/**
	 * Submits the form data
	 * @link https://www.flowable.org/docs/userguide/index.html#_submit_task_form_data
	 * @param array $body		- The request body as JSON
	 *     Request body for task form:
	 *         {
	 *             "taskId" : "5",
	 *             "properties" : [
	 *                 {
	 *                     "id" : "room",
	 *                     "value" : "normal"
	 *                 }
	 *             ]
	 *         }
	 *     Request body for start event form:
	 *     {
	 *         "processDefinitionId" : "5",
	 *         "businessKey" : "myKey",
	 *         "properties" : [
	 *             {
	 *                 "id" : "room",
	 *                 "value" : "normal"
	 *             }
	 *         ]
	 *     }
	 * @return	200 - Indicates request was successful and the form data was submitted
	 * 			400 - Indicates an parameter was passed in the wrong format. The status-message contains additional information.
	 **/
	public function submitFormData($body = []) {
	    $this->setJsonBody($body);
	    return $this->post($this->uri['SUBMIT_FORM_DATA']);
	}
}