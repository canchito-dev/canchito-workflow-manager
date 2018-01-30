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
	
	User.init(url);
	
	var $grid = $('#grid-data').bootgrid({
	    url: url + 'users/getListOfUsers',
	    formatters: {
	        "commands": function(column, row) {
	            return '<button type="button" class="btn btn-sm btn-dark command-edit" data-row-id="' + row.id + '" data-toggle="tooltip" title="Edit"><span class="fas fa-edit"></span></button> ' + 
	                '<button type="button" class="btn btn-sm btn-dark command-delete" data-row-id="' + row.id + '" data-toggle="tooltip" title="Delete"><span class="fas fa-trash-alt"></span></button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	return User.requestFilter(request);
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<button type="button" class="btn btn-dark" id="btnNew" name="btnNew" data-toggle="tooltip" title="New"><i class="fas fa-plus" aria-hidden="true"></i></button>' +
            		'<button type="button" class="btn btn-dark" id="btnBatchDelete" name="btnBatchDelete" data-toggle="tooltip" title="Delete"><i class="fas fa-trash-alt" aria-hidden="true"></i></button></div>' +
            		'<!--Your Button goes here-->' +
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>'
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$grid.find(".command-edit").on("click", function(e) {
	        $('form[name="formUser"]').find('input[name="action"]').val('update');
	        
	        var $userId = $('form[name="formUser"]').find('input[name="id"]');
			$userId.addClass('disabled');
			$userId.attr('readonly', true).attr("disabled", true);
			
			User.edit($(this).data("row-id"), {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($('form[name="formUser"]'), [response.body]);
		        		
		        		//id of the user must be displayed without the tenant
		        		var userId = $('form[name="formUser"]').find('input[name="id"]').val();
		        		userId = userId.split('@', 1);
		        		$('form[name="formUser"]').find('input[name="id"]').val(userId);
		        		
		        		var $modal = $('#userModal');
						$modal.find('.modal-title').text('Edit user');
						$modal.modal('show');
		        	}
				}
			});
			
	    }).end().find(".command-delete").on("click", function(e) {
	    	var userId = $(this).data("row-id");
	    	
	    	bootbox.confirm({
			    title: 'Delete user',
			    message: 'Do you really want to delete user with id "' + userId + '"?',
			    buttons: {
			        'confirm': {
			            label: 'Yes',
			            className: 'btn-danger'
			        },
			        'cancel': {
			            label: 'No',
			            className: 'btn-default'
			        }
			    },
			    callback: function(result) {
			        if (result) {
			        	return User.erase(userId, {
							success: function(response) {           	
					    		if(response.code >= '400') {
				            		if(response.body.hasOwnProperty('exception'))
				            			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
				            	} else {
				    				toastr["success"]('User successfully deleted');
				    				$grid.bootgrid('reload');
				            	}
							}
						});
					}
			    }
			});
	    });
	});
	
	$('button[name="btnNew"]').on('click', function() {
		$('form[name="formUser"]').find('input[name="action"]').val('new');
		var $modal = $('#userModal');
		$modal.find('.modal-title').text('New user');
		$modal.modal('show');
	});
	
	$('button[name="btnBatchDelete"]').on('click', function() {
		bootbox.confirm({
		    title: 'Delete users',
		    message: 'Do you really want to delete the selected users"?',
		    buttons: {
		        'confirm': {
		            label: 'Yes',
		            className: 'btn-danger'
		        },
		        'cancel': {
		            label: 'No',
		            className: 'btn-default'
		        }
		    },
		    callback: function(result) {
		        if (result) {
	            	$("body").mLoading('show');
	            	
	            	var selectedRows = $grid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, rowData) {
	            		requests.push(
	            			User.erase(rowData)
	        			);
	        		});

	        		$.when.apply(this, requests)
	        			.then(function() {
	        				var hasErrors = false;
	                    	var responseReasons = [];
	        				
	        				$.each(requests, function(key, request) {
	                			if(request.responseJSON.code >= '400') {
	                				hasErrors = true;
	        	            		if(request.responseJSON.body.hasOwnProperty('exception'))
	        	            			responseReasons.push(request.responseJSON.body.message + '. ' + request.responseJSON.body.exception + '<br>');
	        	            	}
	        				});
	        				
	        				if(hasErrors) {
	        					toastr["error"](responseReasons);
	        				} else
	            				toastr["success"]('Selected users successfully deleted');
	        					
	        				$grid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
	
	$('#userModal').on('hidden.bs.modal', function (e) {
		$('form[name="formUser"]').validate().resetForm();
		
		$('form[name="formUser"]').trigger("reset");
		
		$('form[name="formUser"]').find('.form-control')
			.each(function () { $(this).removeClass('is-invalid'); });
		
		$('form[name="formUser"]').find('input[name="action"]').val('new');
		
		var $userId = $('form[name="formUser"]').find('input[name="id"]');
		$userId.removeClass('disabled');
		$userId.attr('readonly', false).attr("disabled", false);
	});
	
	$('form[name="formUser"]').validate({
		rules: {
			confirmPassword: {
				equalTo: '#password'
			}
		},
		submitHandler: function(form) {
			var $userId = $('form[name="formUser"]').find('input[name="id"]');
			$userId.removeClass('disabled');
			$userId.attr('readonly', false).attr("disabled", false);
			
			User.save($(form).serializeArray(), {
				complete: function() {
	            	if($('form[name="formUser"]').find('input[name="action"]').val() == 'update') {
	            		var $userId = $('form[name="formUser"]').find('input[name="id"]');
	        			$userId.addClass('disabled');
	        			$userId.attr('readonly', true).attr("disabled", true);
	            	}
	            	
	            	$("body").mLoading('hide');
	            },
				success: function(response) {           	
					if(response.code >= 400) {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"](response.reason);
	    				$grid.bootgrid('reload');
	    				$('#userModal').modal('hide');
	            	}
				}
			});
		}
	});
});