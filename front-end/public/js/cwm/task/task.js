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
var Task = (function() {
    "use strict";

    // locally scoped Object
	var task = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'task/getSingleTask',
		getListOfTasks: 'task/getListOfTasks',
		erase: 'task/deleteTask',
		update: 'task/updateTask',
		completeTask: 'task/completeTask',
		claimTask: 'task/claimTask',
		delegateTask: 'task/delegateTask',
		resolveTask: 'task/resolveTask',
		getAllVariablesForATask: 'task/getAllVariablesForATask'
	};
	
	// Public variables and functions
	task.init = function(url) {
		URLs.main = url || URLs.listOfTasks;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url - The URL to get
     * @return The requested URL
	 **/
	task.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'edit':
				return URLs.edit;
			case 'getListOfTasks':
				return URLs.getListOfTasks;
			case 'erase':
				return URLs.deleteTask;
			case 'update':
				return URLs.updateTask;
			case 'getAllVariablesForATask':
				return URLs.getAllVariablesForATask;
		}
	};
    
	/**
	 * Edits and updates the task information determined by the parameter task id
	 * @param taskId 		- The id of the task to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	task.edit = function(taskId, ajaxSettings) {
		var url = URLs.main + URLs.edit;
		var data = { 'id' : taskId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes the task information determined by the parameter taskId
	 * @param taskId 		- The id of the task to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    task.erase = function(taskId, ajaxSettings) {
		var url = URLs.main + URLs.erase;
		var data = { 'id' : taskId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Creates a new task based on the information passed by the parameter data
	 * @param data 			- A set of key/value pairs with the task information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    task.update = function(data, ajaxSettings) {
		var url = URLs.main + URLs.update;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Executes a task action based on the information passed by the parameter data
	 * @param action		- The task action to execute. Possible values are complete, claim, delegate or resolve
	 * @param data 			- A set of key/value pairs with the task information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    task.taskAction = function(action, data, ajaxSettings) {
		var url = URLs.main;
		switch(action) {
			case 'complete':
				url = url + URLs.completeTask;
				break;
			case 'claim':
				url = url + URLs.claimTask;
				break;
			case 'delegate':
				url = url + URLs.delegateTask;
				break;
			case 'resolve':
				url = url + URLs.resolveTask;
				break;
		}
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Gets all variable of the task determined by the parameter task id
	 * @param taskId 		- The id of the task
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	task.getAllVariablesForATask = function(taskId, ajaxSettings) {
		var url = URLs.main + URLs.getAllVariablesForATask;
		var data = { 'id' : taskId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the task information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    task.requestFilter = function(request) {
    	if(request.order.hasOwnProperty('id')) {
    		request.order = request.order.id;
    		request.sort = 'id';
    	} else if(request.order.hasOwnProperty('name')) {
    		request.order = request.order.name;
    		request.sort = 'name';
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

    return task;
}());