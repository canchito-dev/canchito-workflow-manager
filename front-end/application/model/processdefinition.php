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

class ProcessDefinition extends HttpClient {
	
	private $uri = array(
	    'GET_A_SINGLE_PROCESS_DEFINITION' => 'repository/process-definitions/{processDefinitionId}',
	    'GET_A_LIST_OF_PROCESS_DEFINITIONS' => 'repository/process-definitions',
	    'UPDATE_CATEGORY' => 'repository/process-definitions/{processDefinitionId}',
	    'GET_PROCESS_DEFINITION_RESOURCE_CONTENT' => 'repository/process-definitions/{processDefinitionId}/resourcedata',
	    'GET_PROCESS_DEFINITION_BPMN_MODEL' => 'repository/process-definitions/{processDefinitionId}/model',
	    'ACTIVATE_OR_SUSPEND' => 'repository/process-definitions/{processDefinitionId}',
	    'GET_ALL_CANDIDATE_STARTERS' => 'repository/process-definitions/{processDefinitionId}/identitylinks',
	    'ADD_CANDIDATE_STARTER' => 'repository/process-definitions/{processDefinitionId}/identitylinks',
	    'DELETE_CANDIDATE_STARTER' => 'repository/process-definitions/{processDefinitionId}/identitylinks/{family}/{identityId}'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BPMN_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the process definition based on the provided process definition id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_a_process_definition
	 * @param string $processDefinitionId	- The id of the process definition to get.
	 * @return	200 - Indicates the process definition was found and returned.
	 * 			404 - Indicates the requested process definition was not found.
	 **/
	public function getSingleProcessDefinition($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['GET_A_SINGLE_PROCESS_DEFINITION']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of process definitions following the provided query parameters (if they exists)
	 * @link https://www.flowable.org/docs/userguide/index.html#_list_of_process_definitions
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates request was successful and the process definitions are returned
	 * 			400 - Indicates a parameter was passed in the wrong format or that latest is used with other parameters other 
	 *                than key and keyLike. The status-message contains additional information.
	 **/
	public function getListOfProcessDefinitions($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_PROCESS_DEFINITIONS']);
	}
		
	/**
	 * Updates a process definition category
	 * @link https://www.flowable.org/docs/userguide/index.html#_update_category_for_a_process_definition
	 * @param string $processDefinitionId	- The id of the process definition to update
	 * @param array $body		- The request body as JSON
	 * @return	200 - Indicates the process was category was altered.
	 * 			400 - Indicates no category was defined in the request body.
	 *          401 - Indicates the requested process definition was not found.
	 **/
	public function updateCategory($processDefinitionId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['UPDATE_CATEGORY']);
	    return $this->put($uri);
	}
	
	/**
	 * Gets a resource from the deployment based on the provided deployment and resource id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_a_process_definition_resource_content
	 * @param string $processDefinitionId	- The id of the process definition to get the resource data for.
	 * @return	200 - Indicates both deployment and resource have been found and the resource data has been returned.
	 * 			404 - Indicates the requested deployment was not found or there is no resource with the given id present in the deployment. The status-description contains additional information.
	 **/
	public function getSingleProcessDefinitionResourceContent($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['GET_PROCESS_DEFINITION_RESOURCE_CONTENT']);
	    return $this->get($uri);
	}
	
	/**
	 * Get the process definition model based on the provided process definition id.
	 * The response body is a JSON representation of the org.flowable.bpmn.model.BpmnModel and contains the full process definition model.
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_a_process_definition_bpmn_model
	 * @param string $processDefinitionId	- The id of the process definition to get the model for.
	 * @return	200 - Indicates the process definition was found and the model is returned.
	 * 			404 - Indicates the requested process definition was not found.
	 **/
	public function getProcessDefinitionBpmnModel($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['GET_PROCESS_DEFINITION_BPMN_MODEL']);
	    return $this->get($uri);
	}
	
	/**
	 * Activates/Suspends a process definition
	 * @link https://www.flowable.org/docs/userguide/index.html#_activate_a_process_definition
	 * @link https://www.flowable.org/docs/userguide/index.html#_suspend_a_process_definition
	 * @param string $processDefinitionId	- The id of the process definition to activate or suspend
	 * @param array $body		- The request body as JSON
	 *                             {
	 *                                 "action" : "suspend",                   - Action to perform. Either activate or suspend. Required.
	 *                                 "includeProcessInstances" : "false",    - Whether or not to suspend/activate running process-instances for this process-definition. If omitted, the process-instances are left in the state they are. Not required.
	 *                                 "date" : "2013-04-15T00:42:12Z"         - Date (ISO-8601) when the suspension/activation should be executed. If omitted, the suspend/activation is effective immediately. Not required
	 *                             }
	 * @return	200 - Indicates the process was suspended.
	 * 			404 - Indicates the requested process definition was not found.
	 *          409 - Indicates the requested process definition is already suspended.
	 **/
	public function activateOrSuspendProcessDefinition($processDefinitionId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['ACTIVATE_OR_SUSPEND']);
	    return $this->put($uri);
	}
	
	/**
	 * Get all candidates starter for a process definition based on the provided process definition id
	 * @link https://www.flowable.org/docs/userguide/index.html#_get_all_candidate_starters_for_a_process_definition
	 * @param string $processDefinitionId	- The id of the process definition to get the identity links for.
	 * @return	200 - Indicates the process definition was found and the requested identity links are returned.
	 * 			404 - Indicates the requested process definition was not found.
	 **/
	public function getAllCandidateStarters($processDefinitionId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['GET_ALL_CANDIDATE_STARTERS']);
	    return $this->get($uri);
	}
	
	/**
	 * Adds a candidate starter to a process definition
	 * @link https://www.flowable.org/docs/userguide/index.html#_add_a_candidate_starter_to_a_process_definition
	 * @param string $processDefinitionId	- The id of the process definition to which a candidate starter will be added.
	 * @param array $body		- The request body as JSON
	 *     Request body (user):    { "user" : "kermit" }
	 *     Request body (group):   { "groupId" : "sales" }
	 * @return	201 - Indicates the process definition was found and the identity link was created.
	 * 			400 - Indicates the requested process definition was not found.
	 **/
	public function addCandidateStarter($processDefinitionId, $body = []) {
	    $this->setJsonBody($body);
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['ADD_CANDIDATE_STARTER']);
	    return $this->post($uri);
	}
	
	/**
	 * Deletes a candidate starter from a process definition
	 * @link https://www.flowable.org/docs/userguide/index.html#_delete_a_candidate_starter_from_a_process_definition
	 * @param $processDefinitionId	- The id of the process definition to which a candidate starter will be deleted.
	 * @param $family               - Either users or groups, depending on the type of identity link.
	 * @param $identityId           - Either the userId or groupId of the identity to remove as candidate starter.
	 * @return	204 - Indicates the process definition was found and the identity link was removed. The response body is intentionally empty.
	 * 			404 - Indicates the requested process definition was not found or the process definition doesn’t have an identity-link that matches the url.
	 **/
	public function deleteCandidateStarter($processDefinitionId, $family, $identityId) {
	    $uri = str_replace('{processDefinitionId}', $processDefinitionId, $this->uri['DELETE_CANDIDATE_STARTER']);
	    $uri = str_replace('{family}', $family, $uri);
	    $uri = str_replace('{identityId}', $identityId, $uri);
	    return $this->delete($uri);
	}
}