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
 * @link			https://github.com/canchito-dev/canchito-workflow-manager
 **/
namespace Application\Model;

use Application\Libs\Http\HttpClient;

class Users extends HttpClient {
	
	private $uri = array(
			'GET_A_SINGLE_USER' => 'identity/users/{userId}',
			'GET_A_LIST_OF_USERS' => 'identity/users',
			'UPDATE_A_USER' => 'identity/users/{userId}',
			'CREATE_A_USER' => 'identity/users',
			'DELETE_A_USER' => 'identity/users/{userId}',
			'GET_USER_PICTURE' => 'identity/users/{userId}/picture',
			'UPDATE_USER_PICTURE' => 'identity/users/{userId}/picture',
			'LIST_USER_INFO' => 'identity/users/{userId}/info',
			'GET_USER_INFO' => 'identity/users/{userId}/info/{key}',
			'UPDATE_USER_INFO' => 'identity/users/{userId}/info/{key}',
			'CREATE_USER_INFO' => 'identity/users/{userId}/info',
			'DELETE_USER_INFO' => 'identity/users/{userId}/info/{key}'
	);
	
	public function __construct()  {
	    parent::__construct();
	}
	
	public function __destruct() {}
	
	/**
	 * Get the user based on the provided user id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_single_user
	 * @param string $userId	- The id of the user to get
	 * @return	200 - Indicates the user exists and is returned
	 * 			404 - Indicates the requested user does not exist
	 **/
	public function getSingleUser($userId) {
		$uri = str_replace('{userId}', $userId, $this->uri['GET_A_SINGLE_USER']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of users following the provided query parameters (if they exists)
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_list_of_users
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates the requested users were returned
	 **/
	public function getListOfUsers($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_USERS']);
	}
	
	/**
	 * Updates a user information
	 * @link http://www.flowable.org/docs/userguide/index.html#_update_a_user
	 * @param string $userId	- The id of the user to update
	 * @param array $body		- The request body as JSON
	 * @return	200 - Indicates the user was updated
	 * 			404 - Indicates the requested user was not found
	 * 			409 - Indicates the requested user was updated simultaneously.
	 **/
	public function updateUser($userId, $body = []) {
		$this->setJsonBody($body);
		$uri = str_replace('{userId}', $userId, $this->uri['UPDATE_A_USER']);
		return $this->put($uri);
	}
	
	/**
	 * Creates a new user
	 * @link http://www.flowable.org/docs/userguide/index.html#_create_a_user
	 * @param array $body		- The request body as JSON
	 * @return	201 - Indicates the user was created
	 * 			400 - Indicates the id of the user was missing
	 **/
	public function createUser($body = []) {
		$this->setJsonBody($body);
		return $this->post($this->uri['CREATE_A_USER']);
	}
	
	/**
	 * Deletes a user
	 * @param string $userId	- The id of the user to delete
	 * @link http://www.flowable.org/docs/userguide/index.html#_delete_a_user
	 * @return	204 - Indicates the user was found and  has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested user was not found
	 **/
	public function deleteUser($userId) {
		$uri = str_replace('{userId}', $userId, $this->uri['DELETE_A_USER']);
		return $this->delete($uri);
	}
}