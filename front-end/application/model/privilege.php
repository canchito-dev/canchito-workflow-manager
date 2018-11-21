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

class Privilege extends HttpClient {
	
	private $uri = array(
		'GET_A_SINGLE_PRIVILEGE' => 'privileges/{privilegeId}',
		'GET_A_LIST_OF_PRIVILEGES' => 'privileges',
	    'GET_USERS' => 'privileges/{privilegeId}/users',
	    'REMOVE_PRIVILEGE_FROM_USER' => 'privileges/{privilegeId}/users/{userId}',
	    'ADD_PRIVILEGE_TO_USER' => 'privileges/{privilegeId}/users',
	    'GET_GROUPS' => 'privileges/{privilegeId}/groups',
		'ADD_PRIVILEGE_TO_GROUP' => 'privileges/{privilegeId}/groups',
		'REMOVE_PRIVILEGE_FROM_GROUP' => 'privileges/{privilegeId}/group/{groupId}'
	);
	
	public function __construct()  {
	    parent::__construct(CWM_BASE_REST_URL);
	}
	
	public function __destruct() {}
	
	/**
	 * Get the privilege based on the provided privilege id
	 * @param string $privilegeId	- The id of the privilege to get
	 * @return	200 - Indicates the privilege was found and returned
	 * 			404 - Indicates the requested privilege was not found
	 **/
	public function getSinglePrivilege($privilegeId) {
	    $uri = str_replace('{privilegeId}', $privilegeId, $this->uri['GET_A_SINGLE_PRIVILEGE']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of privileges following the provided query parameters (if they exists)
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates the requested privileges were returned
	 **/
	public function getListOfPrivileges($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_PRIVILEGES']);
	}
	
	/**
	 * Get the users with the assigned privilege
	 * @param string $privilegeId	- The id of the privilege to get
	 * @return	200 - Indicates the privilege was found and returned
	 * 			404 - Indicates the requested privilege was not found
	 **/
	public function getUsersWithPrivilege($privilegeId) {
	    $uri = str_replace('{privilegeId}', $privilegeId, $this->uri['GET_USERS']);
	    return $this->get($uri);
	}
	
	/**
	 * Removes a privilege from a user
	 * @param string $privilegeId	- The id of the privilege to delete
	 * @param string $userId	      - The id of the user to delete
	 * @return	204 - Indicates the privilege was found and  has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested privilege was not found
	 **/
	public function removePrivilegeFromUser($privilegeId, $userId) {
	    $uri = str_replace('{privilegeId}', $privilegeId, $this->uri['REMOVE_PRIVILEGE_FROM_USER']);
	    $uri = str_replace('{userId}', $userId, $uri);
	    return $this->delete($uri);
	}
	
	/**
	 * Add a privilege to a user
	 * @param string $privilegeId	- The id of the privilege
	 * @param array $body		    - The request body as JSON
	 * @return	201 - Indicates the privilege was created
	 * 			400 - Indicates the id of the privilege was missing
	 **/
	public function addUserPrivilege($privilegeId, $body = []) {
	    $this->setJsonBody($body);
	    return $this->post($this->uri['ADD_PRIVILEGE_TO_USER']);
	}
	
	/**
	 * Get the groups with the assigned privilege
	 * @param string $privilegeId	- The id of the privilege to get
	 * @return	200 - Indicates the privilege was found and returned
	 * 			404 - Indicates the requested privilege was not found
	 **/
	public function getGroupsWithPrivilege($privilegeId) {
	    $uri = str_replace('{privilegeId}', $privilegeId, $this->uri['GET_GROUPS']);
	    return $this->get($uri);
	}
	
	/**
	 * Removes a privilege from a group
	 * @param string $privilegeId	- The id of the privilege to delete
	 * @param string $groupId	      - The id of the group to delete
	 * @return	204 - Indicates the privilege was found and  has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested privilege was not found
	 **/
	public function removePrivilegeFromGroup($privilegeId, $groupId) {
	    $uri = str_replace('{privilegeId}', $privilegeId, $this->uri['REMOVE_PRIVILEGE_FROM_GROUP']);
	    $uri = str_replace('{groupId}', $groupId, $uri);
	    return $this->delete($uri);
	}
	
	/**
	 * Add a privilege to a group
	 * @param string $privilegeId	- The id of the privilege
	 * @param array $body		    - The request body as JSON
	 * @return	201 - Indicates the privilege was created
	 * 			400 - Indicates the id of the privilege was missing
	 **/
	public function adGroupPrivilege($privilegeId, $body = []) {
	    $this->setJsonBody($body);
	    return $this->post($this->uri['ADD_PRIVILEGE_TO_GROUP']);
	}
}