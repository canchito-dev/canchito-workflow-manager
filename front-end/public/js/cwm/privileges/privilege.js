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
var Privilege = (function() {
    "use strict";

    // locally scoped Object
	var privilege = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		getSinglePrivilege: 'privileges/getSinglePrivilege',
		getListOfPrivileges: 'privileges/getListOfPrivileges',
		getUsersWithPrivilege: 'privileges/getUsersWithPrivilege',
		removePrivilegeFromUser: 'privileges/removePrivilegeFromUser',
		addUserPrivilege: 'privileges/addUserPrivilege',
		getGroupsWithPrivilege: 'privileges/getGroupsWithPrivilege',
		removePrivilegeFromGroup: 'privileges/removePrivilegeFromGroup',
		addGroupPrivilege: 'privileges/addGroupPrivilege'
	};
	
	// Public variables and functions
	privilege.init = function(url) {
		URLs.main = url || URLs.listOfPrivileges;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url 		- The URL to get
     * @return The requested URL
	 **/
	privilege.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'getSinglePrivilege':
				return URLs.getSinglePrivilege;
			case 'getListOfPrivileges':
				return URLs.getListOfPrivileges;
			case 'getUsersWithPrivilege':
				return URLs.getUsersWithPrivilege;
			case 'removePrivilegeFromUser':
				return URLs.removePrivilegeFromUser;
			case 'addUserPrivilege':
				return URLs.addUserPrivilege;
			case 'getGroupsWithPrivilege':
				return URLs.getGroupsWithPrivilege;
			case 'removePrivilegeFromGroup':
				return URLs.removePrivilegeFromGroup;
			case 'addGroupPrivilege':
				return URLs.addGroupPrivilege;
		}
	};
    
	/**
	 * Edits and updates the privilege information determined by the parameter privilegeId
	 * @param privilegeId 	- The id of the privilege to update
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	privilege.getSinglePrivilege = function(privilegeId, ajaxSettings) {
		var url = URLs.main + URLs.getSinglePrivilege;
		var data = { 'id' : privilegeId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Returns the users which have the requested privilege id
	 * @param privilegeId 	- The id of the privilege
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	privilege.getUsersWithPrivilege = function(privilegeId, ajaxSettings) {
		var url = URLs.main + URLs.getUsersWithPrivilege;
		var data = { 'id' : privilegeId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Removes the users which have the requested privilege id
	 * @param privilegeId 	- The id of the privilege
	 * @param userId 		- The id of the user
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	privilege.removePrivilegeFromUser = function(privilegeId, userId, ajaxSettings) {
		var url = URLs.main + URLs.removePrivilegeFromUser;
		var data = { 
			'id' : privilegeId,
			'userId' : userId
		};
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Creates the privilege to the user
	 * @param data - A set of key/value pairs with the user information
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    privilege.addUserPrivilege = function(data, ajaxSettings) {
		var url = URLs.main + URLs.addUserPrivilege;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Returns the groups which have the requested privilege id
	 * @param privilegeId 	- The id of the privilege
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	privilege.getGroupsWithPrivilege = function(privilegeId, ajaxSettings) {
		var url = URLs.main + URLs.getGroupsWithPrivilege;
		var data = { 'id' : privilegeId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Removes the groups which have the requested privilege id
	 * @param privilegeId 	- The id of the privilege
	 * @param groupId 		- The id of the group
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	privilege.removePrivilegeFromGroup = function(privilegeId, groupId, ajaxSettings) {
		var url = URLs.main + URLs.removePrivilegeFromGroup;
		var data = { 
			'id' : privilegeId,
			'groupId' : groupId
		};
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Creates the privilege to the group
	 * @param data - A set of key/value pairs with the group information
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    privilege.addGroupPrivilege = function(data, ajaxSettings) {
		var url = URLs.main + URLs.addGroupPrivilege;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the privilege information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    privilege.requestFilter = function(request) {
    	if(request.order.hasOwnProperty('id')) {
    		request.order = request.order.id;
    		request.sort = 'id';
    	} else if(request.order.hasOwnProperty('name')) {
    		request.order = request.order.firstName;
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
    }

    return privilege;
}());