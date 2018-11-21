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
 * @link		https://github.com/canchito-dev/canchito-workflow-manager
 **/
var Deployment = (function() {
    "use strict";

    // locally scoped Object
	var deployment = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'deployment/getSingleDeployment',
		listOfDeployments: 'deployment/getListOfDeployments',
		erase: 'deployment/deleteDeployment',
		save: 'deployment/saveDeployment',
		listOfResourcesInDeployment: 'deployment/getListOfResourcesInDeployment',
		getSingleDeploymentResourceContent: 'deployment/getSingleDeploymentResourceContent'
	};
	
	// Public variables and functions
	deployment.init = function(url) {
		URLs.main = url || URLs.listOfDeployments;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url 		- The URL to get
     * @return The requested URL
	 **/
	deployment.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'edit':
				return URLs.edit;
			case 'listOfDeployments':
				return URLs.listOfDeployments;
			case 'erase':
				return URLs.erase;
			case 'save':
				return URLs.save;
			case 'listOfResourcesInDeployment':
				return URLs.listOfResourcesInDeployment;
			case 'getSingleDeploymentResourceContent':
				return URLs.getSingleDeploymentResourceContent;
		}
	};
    
	/**
	 * Edits and updates the deployment information determined by the parameter deployment id
	 * @param deploymentId 		- The id of the deployment to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	deployment.edit = function(deploymentId, ajaxSettings) {
		var url = URLs.main + URLs.edit;
		var data = { 'id' : deploymentId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes the deployment information determined by the parameter deploymentId
	 * @param deploymentId 		- The id of the deployment to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    deployment.erase = function(deploymentId, ajaxSettings) {
		var url = URLs.main + URLs.erase;
		var data = { 'id' : deploymentId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Creates a new deployment based on the information passed by the parameter data
	 * @param data 			- A set of key/value pairs with the deployment information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    deployment.save = function(data, ajaxSettings) {
		var url = URLs.main + URLs.save;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the deployment information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    deployment.requestFilter = function(request) {
    	if(request.order.hasOwnProperty('id')) {
    		request.order = request.order.id;
    		request.sort = 'id';
    	} else if(request.order.hasOwnProperty('name')) {
    		request.order = request.order.name;
    		request.sort = 'name';
    	} else if(request.sort.hasOwnProperty('category')) {
    		request.order = request.order.type;
    		request.sort = 'category';
    	}
    	
    	if(request.hasOwnProperty('searchPhrase')) {
    		if(request.searchPhrase.length > 0) {
	    		request.searchPhrase.forEach(function(value, key) { 
	    			if(value.value != '' && value.value != undefined)
	    				request[value.name] = (value.name.endsWith('Like')) ? '%' + value.value + '%' : value.value;
	    		});
    		}
    		delete request.searchPhrase;
    	}
    	
    	return request;
    };
    
	/**
	 * Gets the resource content determined by the parameter deployment id and the resource id
	 * @param deploymentId 		- The id of the deployment to get
	 * @param resourceId 		- The id of the resource to get
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	deployment.getSingleDeploymentResourceContent = function(deploymentId, resourceId, ajaxSettings) {
		var url = URLs.main + URLs.getSingleDeploymentResourceContent;
		var data = { 
			'deploymentId' : deploymentId,
			'resourceId' : resourceId
		};
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    

    return deployment;
}());