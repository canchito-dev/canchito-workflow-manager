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
	
	Task.init(url);
	User.init(url);
	Form.init(url);
	Deployment.init(url);
	
	var $tasksGrid = $('#tasks-grid-data').bootgrid({
	    url: url + Task.getUrl('getListOfTasks'),
	    formatters: {
	    	'name': function(column, row) {
	    		return '<button type="button" class="btn btn-link command-details" data-row-id="' + row.id + '">' + row.name + '</button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	request[filterField] = filterValue;
	    	request.searchPhrase = $('form[name="formFilterTask"]').serializeArray();
	    	return Task.requestFilter(request);
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<button type="button" class="btn btn-dark" id="btnSave" name="btnSave" data-toggle="tooltip" title="Save"><i class="far fa-save" aria-hidden="true"></i></button>' +
            		'<button type="button" class="btn btn-dark" id="btnBatchDelete" name="btnBatchDelete" data-toggle="tooltip" title="Delete"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>' +
            		'<!--Your Button goes here-->' +
            		'</div>' + 
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>'
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$tasksGrid.find(".command-details").on("click", function(e) {			
	    	var $form = $('form[name="formTask"]');
	    	var taskId = $(this).data("row-id");
			resetForm($form);
	    	
	    	Task.edit(taskId, {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($form, [response.body]);
		        		$form.find('input[name="createTime"]').val(new Date(response.body.createTime).toLocaleString());
		        		if(response.body.dueDate != 'null' && response.body.dueDate != null)
		        			$form.find('input[name="dueDate"]').val(new Date(response.body.dueDate).toLocaleString());
		        		
		        		if(response.body.assignee != 'null' && response.body.assignee != null && response.body.assignee != '') {
			        		User.edit(response.body.assignee, {
			    				success: function(response) {           	
			    	            	if(response.code >= 400) {
			    		        		if(response.body.hasOwnProperty('exception'))
			    		        			response.reason = response.body.message + '. ' + response.body.exception;
			    		            	toastr["error"](response.reason);
			    		        	} else {
			    		        		var $selectizedControl = $form.find('select[name="assignee"]')[0].selectize;
			    		        		$selectizedControl.addOption(response.body);
			    		        		$selectizedControl.setValue(response.body.id, true);
			    		        	}
			    				}
			    			});	
		        		}
		        		
		        		if(response.body.owner != 'null' && response.body.owner != null && response.body.owner != '') {
			        		User.edit(response.body.owner, {
			    				success: function(response) {           	
			    	            	if(response.code >= 400) {
			    		        		if(response.body.hasOwnProperty('exception'))
			    		        			response.reason = response.body.message + '. ' + response.body.exception;
			    		            	toastr["error"](response.reason);
			    		        	} else {
			    		        		var $selectizedControl = $form.find('select[name="owner"]')[0].selectize;
			    		        		$selectizedControl.addOption(response.body);
			    		        		$selectizedControl.setValue(response.body.id, true);
			    		        	}
			    				}
			    			});	
		        		}
		        	}
				}
			});
		});
	});
	
	$('button[name="btnBatchDelete"]').on('click', function() {
		bootbox.confirm({
		    title: 'Delete tasks',
		    message: 'Do you really want to delete the selected tasks"?',
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
	            	
	            	var selectedRows = $tasksGrid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, rowData) {
	            		requests.push(
	            			Task.erase(rowData)
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
	            				toastr["success"]('Selected tasks successfully deleted');
	        					
	        				$tasksGrid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
	
	$('select[name="owner"], select[name="assignee"]').selectize({
		valueField: 'id',
		labelField: 'firstName',
		searchField: 'firstName',
	    sortField: 'firstName',
	    inputClass: 'selectize-form-control selectize-input', 
	    dropdownParent: 'body',
		options: [],
		create: false,
		persist: false,
		loadThrottle: null,
		preload: false,
		hideSelected: true,
		render: {
			option: function(item, escape) {
				return '<option value="' + item.userId + '" data-data="' + JSON.stringify(item) + '">' + item.firstName + ' ' + item.lastName + '</option>';
			},
			item: function(item, escape) {
				return '<div>' + item.firstName + ' ' + item.lastName + '</div>';
			}
		},
		load: function(query, callback) {
			if (!query.length) return callback();
			
			Ajax.executeAjax(
				url + User.getUrl('listOfUsers'), 
				{ 'firstNameLike': '%' + query + '%' },
				{
					type: 'POST',
		            dataType: 'json',
					error: function(res) {
						toastr["error"]('Could not load users');
						console.log(res);
					},
					success: function(res) {
						if(res.code >= '400') {
							toastr["error"]('Could not load users');
							return false;
						} else {
							callback(res.body.data.slice(0,15));
						}
					}
				}
			);
		}
	});
	
	$('button[name="btnSave"]').on('click', function() {
		$('form[name="formTask"]').submit();
	});
	
	$('form[name="formTask"]').validate({
		submitHandler: function(form) {
			Task.update($(form).serializeArray(), {
				success: function(response) {           	
					if(response.code >= 400) {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"](response.reason);
	    				$tasksGrid.bootgrid('reload');
	    				resetForm($(form));
	            	}
				}
			});
		}
	});
	
	$('button[name="btnCompleteTask"]').on('click', function() {
		var $form = $('form[name="formTask"]');
		var taskId = $form.find('input[name="id"]').val();
		var formKey = $form.find('input[name="formKey"]').val();
		var processDefinitionId = $form.find('input[name="processDefinitionId"]').val();
		
		if(formKey != null && formKey != 'null' && formKey != '') {
			Form.getProcessDefinitionFormData(processDefinitionId, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	            		var formData = response.body;
	            		Deployment.getSingleDeploymentResourceContent(formData.deploymentId, formKey, {
							success: function(response) {           	
				            	if(response.code >= 400) {
					        		if(response.body.hasOwnProperty('exception'))
					        			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
					        	} else {
					        		$('div[name="submitFormContainer"]').html(response.body);
					        		
					        		Task.getAllVariablesForATask(taskId, {
					        			success: function(response) {
					        				if(response.code >= 400) {
								        		if(response.body.hasOwnProperty('exception'))
								        			response.reason = response.body.message + '. ' + response.body.exception;
								            	toastr["error"](response.reason);
								        	} else {
								        		FormUtil.mapFlowableVariablesToForm($('form[name="formSubmitForm"]'), response.body);
								        		
								        		var $modal = $('#submitFormModal');
								        		$modal.find('.modal-title').text('Complete task');
								        		$modal.modal('show');
								        	}
					        			}
					        		});
					        	}
							}
						});
	            	}
				}
			});
		} else {
			Task.taskAction('complete' , { id : taskId, action : 'complete' }, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"]('Task with id "' + taskId + '" successfully completed');
	    				$tasksGrid.bootgrid('reload');
	    				resetForm($form);
	    				resetForm($('form[name="formTask"]'));
	            	}
				}
			});
		}
	});
	
	$('form[name="formSubmitForm"]').validate({
		submitHandler: function(form) {
			var $form = $(form);
			var values = [];
			var elements = $form.find('.is-form-element');
			var taskId = $('form[name="formTask"]').find('input[name="id"]').val();
			
			$.each(elements, function(index, obj) {
				values.push({
					name : $(obj).attr('name'),
					value : $(obj).val()
				});
			});
			
			var formData = { 
					id : taskId,
					action : 'complete',
					variables : JSON.stringify(values)
			};
			
			Task.taskAction('complete' , formData, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"]('Task with id "' + taskId + '" successfully completed');
	            	}
				}
			});
			$tasksGrid.bootgrid('reload');
			resetForm($form);
			resetForm($('form[name="formTask"]'));
			$('#submitFormModal').modal('hide');
		}
	});
	
	$('button[name="btnClaimTask"]').on('click', function() {
		var taskId = $('form[name="formTask"]').find('input[name="id"]').val();
		var formData = { 
				id : taskId,
				action : 'claim'
		}
		Task.taskAction('claim' , formData, {
			success: function(response) {           	
	    		if(response.code >= '400') {
            		if(response.body.hasOwnProperty('exception'))
            			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
            	} else {
    				toastr["success"]('Task with id "' + taskId + '" successfully claimed');
    				$tasksGrid.bootgrid('reload');
    				resetForm($form);
            	}
			}
		});
	});
});

/**
 * Resets the form
 **/
function resetForm(form) {
	var $form = $(form);
	$form.validate().resetForm();
	$form.trigger("reset");
	$form.find('.is-form-element')
		.each(function () { $(this).removeClass('is-invalid'); });

	var selectized = $form.find('select[name="assignee"]');
	if(selectized.length > 0) selectized[0].selectize.clearOptions();
	
	selectized = $form.find('select[name="owner"]')
	if(selectized.length > 0) selectized[0].selectize.clearOptions();
	
	$('div[name="submitFormContainer"]').html('');
}