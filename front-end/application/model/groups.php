<?php
/**
 * This content is released under the MIT License (MIT)
 *
 * Copyright (c) 2018, canchito-dev
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
 * @copyright	Copyright (c) 2018, canchito-dev (http://www.canchito-dev.com)
 * @license		http://opensource.org/licenses/MIT	MIT License
 * @link			https://github.com/canchito-dev/canchito-workflow-manager
 **/
namespace Application\Model;

use Application\Libs\Http\HttpClient;

class Groups extends HttpClient {
	
	private $uri = array(
			'GET_A_SINGLE_GROUP' => 'identity/groups/{groupId}',
			'GET_A_LIST_OF_GROUPS' => 'identity/groups',
			'UPDATE_A_GROUP' => 'identity/groups/{groupId}',
			'CREATE_A_GROUP' => 'identity/groups',
			'DELETE_A_GROUP' => 'identity/groups/{groupId}',
			'GET_GROUP_MEMBERS' => 'identity/users?memberOfGroup={groupId}',
			'ADD_MEMBER_TO_GROUP' => 'identity/groups/{groupId}/members',
			'DELETE_MEMBER_FROM_GROUP' => 'identity/groups/{groupId}/members/{userId}'
	);
	
	public function __construct()  {
	    parent::__construct();
	}
	
	public function __destruct() {}
	
	/**
	 * Get the group based on the provided group id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_single_group
	 * @param string $groupId	- The id of the group to get
	 * @return	200 - Indicates the group exists and is returned
	 * 			404 - Indicates the requested group does not exist
	 **/
	public function getSingleGroup($groupId) {
		$uri = str_replace('{groupId}', $groupId, $this->uri['GET_A_SINGLE_GROUP']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of groups following the provided query parameters (if they exists)
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_list_of_groups
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates the requested groups were returned
	 **/
	public function getListOfGroups($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_GROUPS']);
	}
	
	/**
	 * Updates a group information
	 * @link http://www.flowable.org/docs/userguide/index.html#_update_a_group
	 * @param string $groupId	- The id of the group to update
	 * @param array $body		- The request body as JSON
	 * @return	200 - Indicates the group was updated
	 * 			404 - Indicates the requested group was not found
	 * 			409 - Indicates the requested group was updated simultaneously.
	 **/
	public function updateGroup($groupId, $body = []) {
		$this->setJsonBody($body);
		$uri = str_replace('{groupId}', $groupId, $this->uri['UPDATE_A_GROUP']);
		return $this->put($uri);
	}
	
	/**
	 * Creates a new group
	 * @link http://www.flowable.org/docs/userguide/index.html#_create_a_group
	 * @param array $body		- The request body as JSON
	 * @return	201 - Indicates the group was created
	 * 			400 - Indicates the id of the group was missing
	 **/
	public function createGroup($body = []) {
		$this->setJsonBody($body);
		return $this->post($this->uri['CREATE_A_GROUP']);
	}
	
	/**
	 * Deletes a group
	 * @param string $groupId	- The id of the group to delete
	 * @link http://www.flowable.org/docs/userguide/index.html#_delete_a_group
	 * @return	204 - Indicates the group was found and  has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested group was not found
	 **/
	public function deleteGroup($groupId) {
		$uri = str_replace('{groupId}', $groupId, $this->uri['DELETE_A_GROUP']);
		return $this->delete($uri);
	}
	
	/**
	 * Gets a list of members binded to a group based on the provided group id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_members_in_a_group
	 * @param string $groupId	- The id of the group to which users will be listed
	 * @return	200 - Indicates the requested group members were returned
	 **/
	public function getGroupMembers($query = []) {
	    if(!empty($query)) $this->setQuery($query);
	    return $this->get($this->uri['GET_GROUP_MEMBERS']);
	}
	
	/**
	 * Add a user as a member of a group
	 * @link http://www.flowable.org/docs/userguide/index.html#_add_a_member_to_a_group
	 * @param string $groupId	- The id of the group to which the user will be added too
	 * @param string $userId	- The id of the user which will be added to the group
	 * @return	201 - Indicates the group was found and the member has been added
	 * 			404 - Indicates the userId was not included in the request body
	 * 			404 - Indicates the requested group was not found
	 * 			409 - Indicates the requested user is already a member of the group
	 **/
	public function addMemberToGroup($groupId, $userId) {
	    $this->setJsonBody([ 'userId' => $userId ]);
	    $uri = str_replace('{groupId}', $groupId, $this->uri['ADD_MEMBER_TO_GROUP']);
	    return $this->post($uri);
	}
	
	/**
	 * Deletes a user from a group
	 * @param string $groupId	- The id of the group from which the user will be deleted
	 * @param string $userId	- The id of the user which will be deleted from the group
	 * @link http://www.flowable.org/docs/userguide/index.html#_delete_a_member_from_a_group
	 * @return	204 - Indicates the group was found and the member has been deleted. The response
	 *                body is left empty intentionally
	 * 			404 - Indicates the requested group was not found or that the user is not a member the 
	 *                group. The status description contains additional information about the error
	 **/
	public function deleteMemberFromGroup($groupId, $userId) {
	    $uri = str_replace('{groupId}', $groupId, $this->uri['DELETE_MEMBER_FROM_GROUP']);
	    $uri = str_replace('{userId}', $userId, $uri);
	    return $this->delete($uri);
	}
}