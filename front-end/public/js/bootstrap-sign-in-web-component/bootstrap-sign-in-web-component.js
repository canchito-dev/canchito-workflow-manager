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
 * @link		http://canchito-dev/projects/bootstrap-sign-in-web-component
 **/
(function( $ ) {

    'use strict';
    
    // BOOT SIGN IN INTERNAL FIELDS
    // ====================

    var namespace = "bs.bootsignin";
    
    // BOOT SIGN IN PUBLIC CLASS DEFINITION
    // ====================

    /**
     * Represents the jQuery BootSignIn plugin.
     *
     * @class BootSignIn
     * @constructor
     * @param element {Object} The corresponding DOM element.
     * @param options {Object} The options to override default settings.
     **/
    var BootSignIn = function(element, options) {
		this.$element = $(element);
        this.$origin = this.$element.clone();
		this.options = $.extend(true, {}, BootSignIn.defaults, this.$element.data(), options);
		var that = this;
		
		var getSettings = {
			url: this.options.template,
			data: {},
			success: function (template, textStatus, jqXHR) {
				var $template = $(template);
				
				// ad the element
				that.$element.wrapInner($template);
				
				// add the password toggler button if enabled
				if(that.options.showPasswordToggler) {
					var $passwordField = $template.find('input[type="password"]');
					that.initPasswordToggler($passwordField, that.options.passwordToggler);
				}
				
				// init the form by adding the form validation if enabled, and the submit function if ajax is enabled
				if(that.options.validateForm) {
					var $form = $template.find('form');
					that.initForm($form, that.options.formErrorMessages);
				}
			},
			dataType: 'html'
		}
		
		$.get(getSettings);
    };
    
    BootSignIn.defaults = {
        version: '1.0.0',

        /**
         * Enables the show password toggler button. Disabled by default
         *
         * @property showPasswordToggler
         * @type Boolean
         * @default false
         * @for defaults
         **/
        showPasswordToggler: false,

        /**
         * Defines whether enable/disable password toggler button and its properties
         *
         * @property passwordToggler
         * @type Boolean
         * @default false
         * @for defaults
         **/
        passwordToggler: {
        	append: 'append',				// can be prepend or append
            iconPrefix: 'fas',		
            iconShow: 'fa-eye',
            iconHide: 'fa-eye-slash',
            tooltip: 'Show/Hide password'
        },

        /**
         * Defines whether should be submitter via an asynchronous HTTP (Ajax) request. Enabled by default.
         *
         * @property ajax
         * @type Boolean
         * @default false
         * @for defaults
         **/
        ajax: true,

        /**
         * Ajax request settings that shall be used for server-side communication.
         * All setting except data, error, success and url can be overridden.
         * For the full list of settings go to http://api.jquery.com/jQuery.ajax/.
         *
         * @property ajaxSettings
         * @type Object
         * @for defaults
         * @since 1.2.0
         **/
        ajaxSettings: {
            /**
             * Specifies the HTTP method which shall be used when sending data to the server.
             * Go to http://api.jquery.com/jQuery.ajax/ for more details.
             * This setting is overriden for backward compatibility.
             *
             * @property method
             * @type String
             * @default "POST"
             * @for ajaxSettings
             **/
        	type: 'POST',
            async: true,
            contentType: 'application/x-www-form-urlencoded',
            dataType: 'json',
            beforeSend: function(jqXHR, settings) {
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
        },

        /**
         * The location of the template.
         *
         * @property template
         * @type Object
         * @for defaults
         **/
        template: 'template.html',

        /**
         * Enables the form validation. Disabled by default
         *
         * @property validateForm
         * @type Boolean
         * @default false
         * @for defaults
         **/
        validateForm: false,

        /**
         * Error messages to be shown if there are errors during the form submittion.
         *
         * @property formErrorMessages
         * @type Boolean
         * @default false
         * @for defaults
         **/
        formErrorMessages: {
        	email: 'Empty or incorrect email',
        	password: 'Empty or incorrect password'
        },

        /**
         * Function called before submitting the form. Can be used for doing additional things to the form data
         *
         * @property prepareFormData
         * @type Boolean
         * @default false
         * @for defaults
         **/
        prepareFormData: function($form) {
        	return {};
        }
    };
    
    /**
     * Added the password toggler to the password field
     *
     * @param $passwordField	- password field
     * @param options			- the options to format the password toggler button
     **/
    BootSignIn.prototype.initPasswordToggler = function($passwordField, options) {
		var $icon = $(['<div class="input-group-' + options.append + '" data-visible="false"><span class="input-group-text" title="' + options.tooltip + '" style="cursor: pointer;"><i class="' 
			+ options.iconPrefix + ' ' + options.iconShow + '" aria-hidden="true"></i></span></div>'].join('\n'));
		
		$icon.off('click').on('click', $.proxy(function() {
            this.togglePassword($passwordField, $icon, options);
        }, this));
		
		$passwordField.wrap('<div class="input-group"></div>');
		
		$icon.insertAfter($passwordField);
	};
	
	/**
     * Toggles the password field between showing and hidding the password
     *
     * @param $passwordField	- password field
     * @param $icon				- password toggler button
     * @param options			- the options to format the password toggler button
     **/
	BootSignIn.prototype.togglePassword = function($passwordField, $icon, options) {
		if(!$icon.data('visible')) {
			$passwordField.attr('type', 'text');
			$icon.find('i')
            	.removeClass(options.iconShow)
            	.addClass(options.iconHide);
		} else {
			$passwordField.attr('type', 'password');
			$icon.find('i')
            	.removeClass(options.iconHide)
            	.addClass(options.iconShow);
		}
		
		$icon.data('visible', $icon.data('visible') ? false : true);
	};
	
	/**
     * Toggles the password field between showing and hidding the password
     *
     * @param $form		- the form to initialize
     * @param options	- the options to initialize the form
     **/
	BootSignIn.prototype.initForm = function($form, options) {
		var $error = undefined;
		for (var key in options) {
			$error = $(['<div class="invalid-feedback">' + options[key] + '</div>'].join('\n'));
			var $input = $form.find('input[type="' + key + '"]');
			
			if($input.parent().hasClass('input-group'))
				$input = $input.parent().find('[class^=input-group-append]');
			
			$error.insertAfter($input);
		}
		
		$form.addClass('needs-validation');
		$form.attr('novalidate', 'novalidate');
		
		$form.off('submit').on('submit', $.proxy(function(event) {
			event.preventDefault();
			event.stopPropagation();

			if($form[0].checkValidity() === false)			
				$form.addClass('was-validated');
			else {
				if(this.options.ajax) {
					var data = this.options.prepareFormData($form);
					var settings = $.extend(true, {}, this.options.ajaxSettings, {
						data: data
					});
					$.ajax(settings);
				}
			}
        }, this));
	};
	
	// Private function for debugging.
    function debug(event) {
    	if(!BootSignIn.defaults.debug) return;
    	
        if (window.console && window.console.log) {
            window.console.log(event.type + '.' + event.namespace + ': ', event);
        }
    };
	
	$.fn.bootsignin = function(options) {
		
		return this.each(function(index) {
			var $this = $(this),
				instance = $this.data(namespace);
			if(!instance)
				$this.data(namespace, (instance = new BootSignIn(this, options)));
		});
	};
	
	$(function () {
        $('[data-toogle="bootsignin"]').bootsignin();
    });
}( jQuery ));