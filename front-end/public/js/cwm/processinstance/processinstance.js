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
var ProcessInstance = (function() {
    "use strict";

    // locally scoped Object
	var processInstance = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'processInstance/getSingleProcessInstance',
		listOfProcessInstances: 'processInstance/getListOfProcessInstances',
		activateOrSuspendProcessInstance: 'processInstance/activateOrSuspendProcessInstance',
		deleteProcessInstance: 'processInstance/deleteProcessInstance',
		startProcessInstance: 'processInstance/startProcessInstance',
		getDiagramForProcessInstance: 'processInstance/getDiagramForProcessInstance'
	};
	
	// Public variables and functions
	processInstance.init = function(url) {
		URLs.main = url || URLs.listOfProcessInstances;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url - The URL to get
     * @return The requested URL
	 **/
	processInstance.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'edit':
				return URLs.edit;
			case 'listOfProcessInstances':
				return URLs.listOfProcessInstances;
			case 'activateOrSuspendProcessInstance':
				return URLs.activateOrSuspendProcessInstance;
			case 'deleteProcessInstance':
				return URLs.deleteProcessInstance;
			case 'startProcessInstance':
				return URLs.startProcessInstance;
			case 'getDiagramForProcessInstance':
				return URLs.getDiagramForProcessInstance;
		}
	};
    
	/**
	 * Edits and updates the process instance information determined by the parameter processInstanceId
	 * @param processInstanceId - The id of the process instance to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processInstance.edit = function(processInstanceId, ajaxSettings) {
		var url = URLs.main + URLs.edit;
		var data = { 'id' : processInstanceId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Activates/Suspends a process instance
	 * @param data			- A set of key/value pairs with the process instance and the new status information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processInstance.activateOrSuspendProcessInstance = function(data, ajaxSettings) {
		var url = URLs.main + URLs.activateOrSuspendProcessInstance;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Deletes a process instance determined by the parameter processInstanceId
	 * @param processInstanceId - The id of the process instance to delete
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processInstance.deleteProcessInstance = function(processInstanceId, ajaxSettings) {
		var url = URLs.main + URLs.deleteProcessInstance;
		var data = { 'id' : processInstanceId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Starts a new process instance
	 * @param data - A set of key/value pairs with the user information
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processInstance.startProcessInstance = function(data, ajaxSettings) {
		var url = URLs.main + URLs.startProcessInstance;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Get the diagram for a process instance based on the provided process instance id
	 * @param processInstanceId - The id of the process instance which diagram to get
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processInstance.getDiagramForProcessInstance = function(processInstanceId, ajaxSettings) {
		var url = URLs.main + URLs.getDiagramForProcessInstance;
		var data = { 'id' : processInstanceId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the processInstance information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    processInstance.requestFilter = function(request) {
    	if(request.order.hasOwnProperty('id')) {
    		request.order = request.order.id;
    		request.sort = 'id';
    	} else if(request.order.hasOwnProperty('processDefinitionKey')) {
    		request.order = request.order.name;
    		request.sort = 'processDefinitionKey';
    	} else if(request.order.hasOwnProperty('processDefinitionId')) {
    		request.order = request.order.name;
    		request.sort = 'processDefinitionId';
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
    

    return processInstance;
}());