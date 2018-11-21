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
var FormUtil = (function() {
    "use strict";

	var formUtil = {};
    
    formUtil.mapRestVariablesToForm = function(form, variables) {
		$(form).find('.form-control').each(function(index, currentElement) {
			var result = $.grep(variables, function(element, index){ 
				return element.hasOwnProperty(currentElement.id);
			});
			if (result.length == 1)
				formUtil.mapVariableToForm(result[0][currentElement.id], currentElement);
		});
    };
    
    formUtil.mapFlowableVariablesToForm = function(form, variables) {
    	$(form).find('.form-control').each(function(index, currentElement) {
			var result = $.grep(variables, function(element) { 
				return element.name == currentElement.id; 
			});
			
			if (result.length == 1)
				formUtil.mapVariableToForm(result[0].value, currentElement);
		});
    };
    
    formUtil.mapVariableToForm = function(value, currentElement) {
    	if(value == undefined || value == null || value == 'null')
			value = '';
		
		switch(currentElement.type) {
			case "checkbox":
			case "radio":
				var currentElementValue = typeof value === 'boolean' ? Boolean($(currentElement).val()) : $(currentElement).val();
				$(currentElement).prop('checked', currentElementValue == value);
				break;
			default:
				$(currentElement).val(value);
				break;
		}
    };

    return formUtil;
}());