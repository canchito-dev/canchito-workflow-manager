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

class Deployment extends HttpClient {
	
	private $uri = array(
			'GET_A_SINGLE_DEPLOYMENT' => 'repository/deployments/{deploymentId}',
			'GET_A_LIST_OF_DEPLOYMENTS' => 'repository/deployments',
			'CREATE_A_NEW_DEPLOYMENT' => 'repository/deployments',
			'DELETE_A_DEPLOYMENT' => 'repository/deployments/{deploymentId}',
			'LIST_RESOURCES_IN_DEPLOYMENT' => 'repository/deployments/{deploymentId}/resources',
			'GET_DEPLOYMENT_RESOURCE' => 'repository/deployments/{deploymentId}/resources/{resourceId}',
			'GET_DEPLOYMENT_RESOURCE_CONTENT' => 'repository/deployments/{deploymentId}/resourcedata/{resourceId}'
	);
	
	public function __construct()  {
	    parent::__construct();
	}
	
	public function __destruct() {}
	
	/**
	 * Get the deployment based on the provided deployment id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_deployment
	 * @param string $deploymentId	- The id of the deployment to get
	 * @return	200 - Indicates the deployment was found and returned
	 * 			404 - Indicates the requested deployment was not found
	 **/
	public function getSingleDeployment($deploymentId) {
	    $uri = str_replace('{deploymentId}', $deploymentId, $this->uri['GET_A_SINGLE_DEPLOYMENT']);
		return $this->get($uri);
	}
	
	/**
	 * Gets a list of deployments following the provided query parameters (if they exists)
	 * @link http://www.flowable.org/docs/userguide/index.html#_list_of_deployments
	 * @param array $filters	- URL query parameters. Check Flowable's official documentation for 
	 * 							  a list of available query parameters
	 * @return	200 - Indicates the requested users were returned
	 **/
	public function getListOfDeployments($filters = []) {
		$this->setQuery($filters);
		return $this->get($this->uri['GET_A_LIST_OF_DEPLOYMENTS']);
	}
		
	/**
	 * Creates a new deployment. The request body should contain data of type multipart/form-data. There should 
	 * be exactly one file in the request, any additional files will be ignored. The deployment name is the name 
	 * of the file-field passed in. If multiple resources need to be deployed in a single deployment, compress 
	 * the resources in a zip and make sure the file-name ends with .bar or .zip.
	 * 
	 * An additional parameter (form-field) can be passed in the request body with name tenantId. The value of 
	 * this field will be used as the id of the tenant this deployment is done in.
	 * @link http://www.flowable.org/docs/userguide/index.html#_create_a_new_deployment
	 * @param array $multipart		- The file to upload
	 * @return	201 - Indicates the deployment was created
	 * 			400 - Indicates there was no content present in the request body or the content mime-type is not supported for deployment. The status-description contains additional information
	 **/
	public function createDeployment($multipart = []) {
	    $this->setMultipart($multipart);
		return $this->post($this->uri['CREATE_A_NEW_DEPLOYMENT']);
	}
	
	/**
	 * Deletes a deployment
	 * @param string $deploymentId	- The id of the deployment to delete
	 * @link http://www.flowable.org/docs/userguide/index.html#_delete_a_deployment
	 * @return	204 - Indicates the deployment was found and has been deleted. Response-body is intentionally empty
	 * 			404 - Indicates the requested deployment was not found
	 **/
	public function deleteDeployment($deploymentId) {
		$uri = str_replace('{deploymentId}', $deploymentId, $this->uri['DELETE_A_DEPLOYMENT']);
		return $this->delete($uri);
	}
	    
    /**
     * Gets a list of resources associated to a deployment and referenced by the provided id
     * @link http://www.flowable.org/docs/userguide/index.html#_list_resources_in_a_deployment
     * @param array $deploymentId	- The id of the deployment which resources are to get
     * @return	200 - Indicates the deployment was found and the resource list has been returned
     * 			404 - Indicates the requested deployment was not found
     **/
	public function getListOfResourcesInDeployment($deploymentId) {
	    $uri = str_replace('{deploymentId}', $deploymentId, $this->uri['LIST_RESOURCES_IN_DEPLOYMENT']);
	    return $this->get($uri);
	}
	
	/**
	 * Gets a resource from the deployment based on the provided deployment and resource id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_deployment_resource
	 * @param string $deploymentId	- The id of the deployment to get
	 * @param string $resourceId	- The id of the resource to get
	 * @return	200 - The id of the deployment the requested resource is part of
	 * 			404 - The id of the resource to get. Make sure you URL-encode the resourceId in case it contains 
	 *                forward slashes. Eg: use diagrams%2Fmy-process.bpmn20.xml instead of diagrams/Fmy-process.bpmn20.xml
	 **/
	public function getSingleDeploymentResource($deploymentId, $resourceId) {
	    $uri = str_replace('{deploymentId}', $deploymentId, $this->uri['GET_DEPLOYMENT_RESOURCE']);
	    $uri = str_replace('{resourceId}', $resourceId, $uri);
	    return $this->get($uri);
	}
	
	/**
	 * Gets a resource from the deployment based on the provided deployment and resource id
	 * @link http://www.flowable.org/docs/userguide/index.html#_get_a_deployment_resource_content
	 * @param string $deploymentId	- The id of the deployment to get
	 * @param string $resourceId	- The id of the resource to get
	 * @return	200 - The id of the deployment the requested resource is part of
	 * 			404 - The id of the resource to get. Make sure you URL-encode the resourceId in case it contains
	 *                forward slashes. Eg: use diagrams%2Fmy-process.bpmn20.xml instead of diagrams/Fmy-process.bpmn20.xml
	 **/
	public function getSingleDeploymentResourceContent($deploymentId, $resourceId) {
	    $uri = str_replace('{deploymentId}', $deploymentId, $this->uri['GET_DEPLOYMENT_RESOURCE_CONTENT']);
	    $uri = str_replace('{resourceId}', $resourceId, $uri);
	    return $this->get($uri);
	}
}