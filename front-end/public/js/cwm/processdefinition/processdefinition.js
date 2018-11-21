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
var ProcessDefinition = (function() {
    "use strict";

    // locally scoped Object
	var processDefinition = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'processDefinition/getSingleProcessDefinition',
		listOfProcessDefinitions: 'processDefinition/getListOfProcessDefinitions',
		updateCategory: 'processDefinition/updateCategory',
		getSingleProcessDefinitionResourceContent: 'processDefinition/getSingleProcessDefinitionResourceContent',
		getProcessDefinitionBpmnModel: 'processDefinition/getProcessDefinitionBpmnModel',
		activateOrSuspendProcessDefinition: 'processDefinition/activateOrSuspendProcessDefinition',
		getAllCandidateStarters: 'processDefinition/getAllCandidateStarters',
		addCandidateStarter: 'processDefinition/addCandidateStarter',
		deleteCandidateStarter: 'processDefinition/deleteCandidateStarter'
	};
	
	// Public variables and functions
	processDefinition.init = function(url) {
		URLs.main = url || URLs.listOfProcessDefinitions;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url - The URL to get
     * @return The requested URL
	 **/
	processDefinition.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'edit':
				return URLs.edit;
			case 'listOfProcessDefinitions':
				return URLs.listOfProcessDefinitions;
			case 'updateCategory':
				return URLs.updateCategory;
			case 'getSingleProcessDefinitionResourceContent':
				return URLs.getSingleProcessDefinitionResourceContent;
			case 'getProcessDefinitionBpmnModel':
				return URLs.getProcessDefinitionBpmnModel;
			case 'activateOrSuspendProcessDefinition':
				return URLs.activateOrSuspendProcessDefinition;
			case 'getAllCandidateStarters':
				return URLs.getAllCandidateStarters;
			case 'addCandidateStarter':
				return URLs.addCandidateStarter;
			case 'deleteCandidateStarter':
				return URLs.deleteCandidateStarter;
		}
	};
    
	/**
	 * Edits and updates the process definition information determined by the parameter processDefinitionId
	 * @param processDefinitionId - The id of the process definition to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processDefinition.edit = function(processDefinitionId, ajaxSettings) {
		var url = URLs.main + URLs.edit;
		var data = { 'id' : processDefinitionId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes the process definition information determined by the parameter processDefinitionId
	 * @param data - A set of key/value pairs with the process definition information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processDefinition.updateCategory = function(data, ajaxSettings) {
		var url = URLs.main + URLs.updateCategory;
		return Ajax.executeAjax(url, data);
    };
    
	/**
	 * Gets the resource content determined by the parameter processDefinitionId
	 * @param processDefinitionId	- The id of the process definition to get
	 * @param ajaxSettings 			- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processDefinition.getSingleProcessDefinitionResourceContent = function(processDefinitionId, ajaxSettings) {
		var url = URLs.main + URLs.getSingleProcessDefinitionResourceContent;
		var data = { 'id' : processDefinitionId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Gets the BPMN model determined by the parameter processDefinitionId
	 * @param processDefinitionId	- The id of the process definition to get
	 * @param ajaxSettings			- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processDefinition.getProcessDefinitionBpmnModel = function(processDefinitionId, ajaxSettings) {
		var url = URLs.main + URLs.getProcessDefinitionBpmnModel;
		var data = { 'id' : processDefinitionId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Activates/Suspends a process definition
	 * @param data			- A set of key/value pairs with the process definition and the new status information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processDefinition.activateOrSuspendProcessDefinition = function(data, ajaxSettings) {
		var url = URLs.main + URLs.activateOrSuspendProcessDefinition;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Get all candidates starter for a process definition based on the provided process definition id
	 * @param processDefinitionId	- The id of the process definition to get the identity links for.
	 * @param ajaxSettings 			- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	processDefinition.getAllCandidateStarters = function(processDefinitionId, ajaxSettings) {
		var url = URLs.main + URLs.getAllCandidateStarters;
		var data = { 'id' : processDefinitionId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Adds a candidate starter to a process definition
	 * @param processDefinitionId	- The id of the process definition to which a candidate starter will be added.
	 * @param family               	- Either users or groups, depending on the type of identity link.
	 * @param identityId           	- Either the userId or groupId of the identity to add as candidate starter.
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processDefinition.addCandidateStarter = function(processDefinitionId, family, identityId, ajaxSettings) {
		var url = URLs.main + URLs.addCandidateStarter;
		var data = { 'processDefinitionId': processDefinitionId };
		if(family == 'user') 
			data['user'] = identityId; 
		else 
			data['group'] = identityId;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes a candidate starter from a process definition
	 * @param processDefinitionId	- The id of the process definition to which a candidate starter will be deleted.
	 * @param family               	- Either users or groups, depending on the type of identity link.
	 * @param identityId           	- Either the userId or groupId of the identity to remove as candidate starter.
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    processDefinition.deleteCandidateStarter = function(processDefinitionId, family, identityId, ajaxSettings) {
		var url = URLs.main + URLs.deleteCandidateStarter;
		var data = {
			'processDefinitionId': processDefinitionId,
			'family': family,
			'identityId': identityId
		};
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the processDefinition information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    processDefinition.requestFilter = function(request) {
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
    

    return processDefinition;
}());