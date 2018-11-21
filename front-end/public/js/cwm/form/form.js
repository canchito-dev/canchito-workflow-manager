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
var Form = (function() {
    "use strict";

    // locally scoped Object
	var form = {};
	
	// Private variables and functions
	var URLs = {
		main: '',
		getProcessDefinitionFormData: 'form/getProcessDefinitionFormData',
		getTaskFormData: 'form/getTaskFormData',
		submitFormData: 'form/submitFormData'
	};
	
	// Public variables and functions
	form.init = function(url) {
		URLs.main = url || URLs.getProcessDefinitionFormData;
	};
	
	/**
	 * Returns a specific URL based on the url parameter
	 * @param url 		- The URL to get
     * @return The requested URL
	 **/
	form.getUrl = function(url) {
		url = url || 'main';
		switch(url) {
			case 'main':
				return URLs.main;
			case 'getProcessDefinitionFormData':
				return URLs.getProcessDefinitionFormData;
			case 'getTaskFormData':
				return URLs.getTaskFormData;
			case 'submitFormData':
				return URLs.submitFormData;
		}
	};
    
	/**
	 * Get the form data of the process definition
	 * @param string processDefinitionId	- The id of the process definition to get
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	form.getProcessDefinitionFormData = function(processDefinitionId, ajaxSettings) {
		var url = URLs.main + URLs.getProcessDefinitionFormData;
		var data = { 'processDefinitionId' : processDefinitionId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
	/**
	 * Get the form data of the task
	 * @param string taskId	- The id of the task to get
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
	form.getTaskFormData = function(taskId, ajaxSettings) {
		var url = URLs.main + URLs.getTaskFormData;
		var data = { 'taskId' : taskId };
		return Ajax.executeAjax(url, data, ajaxSettings);
    };
    
    /**
	 * Submits the form data
	 * @param array data		- The request body as JSON
	 * @param ajaxSettings 	- A set of key/value pairs that configure the Ajax request. Optional.
     * @return The ajax request response
	 **/
    form.submitFormData = function(data, ajaxSettings) {
		var url = URLs.main + URLs.submitFormData;
		return Ajax.executeAjax(url, data, ajaxSettings);
    };

    return form;
}());