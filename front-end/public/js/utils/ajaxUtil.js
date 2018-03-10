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
var Ajax = (function() {
    "use strict";

    // locally scoped Object
	var ajax = {};
	
	// Private variables and functions	
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
	var setAjaxSettings = function(settings) {
		if (typeof settings !== "undefined")
			return $.extend(true, {}, ajaxSettings, settings);
		return ajaxSettings;
	}
	
	// Public variables and functions
	/**
	 * Executes Ajax call
	 * @param url		- The URL to call during the Ajax request.
	 * @param data		- The data to pass to the Ajax request.
	 * @param settings	- A set of key/value pairs that configure the Ajax request.
	 **/
	ajax.executeAjax = function(url, data, settings) {
		var ajaxSettings = $.extend(setAjaxSettings(settings), {
			url: url,
			data: data
		});
		return $.ajax(ajaxSettings);
	};

    return ajax;
}());