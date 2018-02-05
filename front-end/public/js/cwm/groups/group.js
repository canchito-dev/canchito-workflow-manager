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
var Group = (function() {
    "use strict";

    // locally scoped Object
	var group = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'groups/getSingleGroup',
		listOfGroups: 'groups/getListOfGroups',
		erase: 'groups/deleteGroup',
		save: 'groups/saveGroup',
		getMembers: 'groups/getGroupMembers',
		addMemberToGroup: 'groups/addMemberToGroup',
		deleteMemberFromGroup: 'groups/deleteMemberFromGroup'
	};
	
	// Public variables and functions
	group.init = function(url) {
		URLs.main = url || URLs.listOfGroups;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url 		- The URL to get
     * @return The requested URL
	 **/
	group.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'edit':
				return URLs.edit;
			case 'listOfGroups':
				return URLs.listOfGroups;
			case 'erase':
				return URLs.erase;
			case 'save':
				return URLs.save;
			case 'getMembers':
				return URLs.getMembers;
			case 'addMemberToGroup':
				return URLs.addMemberToGroup;
			case 'deleteMemberFromGroup':
				return URLs.deleteMemberFromGroup;
		}
	};
    
	/**
	 * Edits and updates the group information determined by the parameter groupId
	 * @param groupId 		- The id of the group to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	group.edit = function(groupId, ajaxSettings) {
		var url = URLs.main + URLs.edit;
		var data = { 'id' : groupId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes the group information determined by the parameter groupId
	 * @param groupId 		- The id of the group to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    group.erase = function(groupId, ajaxSettings) {
		var url = URLs.main + URLs.erase;
		var data = { 'id' : groupId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Creates a new group based on the information passed by the parameter data
	 * @param data 			- A set of key/value pairs with the group information
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    group.save = function(data, ajaxSettings) {
		var url = URLs.main + URLs.save;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Gets a list of members binded to a group based on the provided group id
	 * @param groupId 		- The id of the group to which users will be listed
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    group.getMembers = function(groupId, ajaxSettings) {
		var url = URLs.main + URLs.getMembers;
		var data = { 'id' : groupId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Add a user as a member of a group
	 * @param groupId 		- The id of the group to which the user will be added too
	 * @param userId 		- The id of the user which will be added to the group
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    group.addMemberToGroup = function(groupId, userId, ajaxSettings) {
		var url = URLs.main + URLs.addMemberToGroup;
		var data = { 'id' : groupId, 'userId' : userId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Deletes a user from a group
	 * @param groupId 		- The id of the group from which the user will be deleted
	 * @param userId 		- The id of the user which will be deleted from the group
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    group.deleteMemberFromGroup = function(groupId, userId, ajaxSettings) {
		var url = URLs.main + URLs.deleteMemberFromGroup;
		var data = { 'id' : groupId, 'userId' : userId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the group information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    group.requestFilter = function(request) {
    	if(request.order.hasOwnProperty('id')) {
    		request.order = request.order.id;
    		request.sort = 'id';
    	} else if(request.order.hasOwnProperty('name')) {
    		request.order = request.order.name;
    		request.sort = 'name';
    	} else if(request.sort.hasOwnProperty('type')) {
    		request.order = request.order.type;
    		request.sort = 'type';
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

    return group;
}());