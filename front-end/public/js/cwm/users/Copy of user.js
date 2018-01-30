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
var User = (function() {
    "use strict";

    // locally scoped Object
	var user = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		edit: 'users/getSingleUser',
		delete: 'users/deleteUser',
		save: 'users/saveUser'
	};
	
	var ajaxSettings = {
		type: 'POST',
        async: true,
        contentType: 'application/x-www-form-urlencoded',
        dataType: 'json',
        beforeSend: function(jqXHR, settings) {
        	$("body").mLoading('show');
        	return {
        		jqXHR: jqXHR, 
        		settings: settings
        	};
        },
        error: function(jqXHR, textStatus, errorThrown) {
        	return {
        		jqXHR: jqXHR,
        		textStatus: textStatus,
        		errorThrown: errorThrown
        	};
        },
        complete: function(jqXHR, textStatus) {
        	$("body").mLoading('hide');
        	return {
        		jqXHR: jqXHR, 
        		textStatus : textStatus
        	};
        },
        success: function(response, textStatus, jqXHR) {
        	return {
        		response: response,
        		textStatus: textStatus,
        		jqXHR: jqXHR
        	};
        }
	};
	
	/**
	 * Checks if the users sent new Ajax setting for overriding the default one
	 * @param settings - A set of key/value pairs that configure the Ajax request.
	 **/
	var checkAjaxSettings = function(settings) {
		if (typeof settings !== "undefined")
			ajaxSettings = $.extend(ajaxSettings, settings);
	}
	
	var ajax = function(url, data) {
		ajaxSettings = $.extend(ajaxSettings, {
			url: url,
			data: data
		});
		return $.ajax(ajaxSettings);
	};
	
	// Public variables and functions
	user.init = function(url) {
		URLs.main = url;
	};
    
	/**
	 * Edits and updates the user information determined by the parameter userId
	 * @param userId - The id of the user to update
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	user.edit = function(userId, ajaxSettings) {
		checkAjaxSettings(ajaxSettings);
		var url = URLs.main + URLs.edit;
		var data = { 'id' : userId };
		return ajax(url, data);
    };
    
    /**
	 * Deletes the user information determined by the parameter userId
	 * @param userId - The id of the user to update
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    user.erase = function(userId, ajaxSettings) {
		checkAjaxSettings(ajaxSettings);
		var url = URLs.main + URLs.delete;
		var data = { 'id' : userId };
		return ajax(url, data);
    };
    
    /**
	 * Creates a new user based on the information passed by the parameter data
	 * @param data - A set of key/value pairs with the user information
	 * @param ajaxSettings - A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    user.save = function(data, ajaxSettings) {
		checkAjaxSettings(ajaxSettings);
		var url = URLs.main + URLs.save;
		return ajax(url, data);
    };
    
    /**
	 * Transform the request to a valid Flowable Rest request
	 * @param request - A set of key/value pairs with the user information
     * @return The reqeust transformed to a valid Flowable Rest request
	 **/
    user.requestFilter = function(request) {
    	var sort = undefined;
    	var order = undefined;
    	var start = (request.current - 1) * request.rowCount;
    	
    	if(request.sort.hasOwnProperty('id')) {
    		order = request.sort.id;
    		sort = 'id';
    	} else if(request.sort.hasOwnProperty('firstName')) {
    		order = request.sort.firstName;
    		sort = 'firstName';
    	} else if(request.sort.hasOwnProperty('lastName')) {
    		order = request.sort.lastName;
    		sort = 'lastName';
    	} else if(request.sort.hasOwnProperty('email')) {
    		order = request.sort.email;
    		sort = 'email';
    	}
    	
    	if(request.hasOwnProperty('searchPhrase')) {
    		if(request.searchPhrase != '') {
    			return {
		    			sort : sort, 
		    			order : order, 
		    			start : start, 
		    			size : request.rowCount,
		    			emailLike : '%' + request.searchPhrase + '%'
		    	};
    		}
    	}
    	
    	return {
    			sort : sort, 
    			order : order, 
    			start : start, 
    			size : request.rowCount
    	};
    }

    return user;
}());