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
$(document).ready(function() {
	$('#bootsignin').bootsignin({
		showPasswordToggler: true,
		validateForm: true,
		template: url + 'authentication/getSignInWebComponent',
		prepareFormData: function($form) {
			return { 
				username:  $form.find('input[name="email"]').val(), 
				password: md5($form.find('input[name="password"]').val())
			};
		},
		ajaxSettings: {
            type: "POST",
            url: url + 'authentication/signIn',
            contentType: 'application/x-www-form-urlencoded',
            dataType: 'json',
            cache: false,
            beforeSend: function(jqXHR, settings) {
            	$("body").mLoading('show');
            },
            error: function(jqXHR, textStatus, errorThrown) {},
            complete: function(jqXHR, textStatus) {
            	$("body").mLoading('hide');
            },
            success: function(response, textStatus, jqXHR) {            	
            	if(response.code >= 400)
	            	toastr["error"](response.reason + '.');
    			else {
    				toastr["success"](response.reason);
            		location.href = url + response.redirect;
            	}
            }
        }
	});
});