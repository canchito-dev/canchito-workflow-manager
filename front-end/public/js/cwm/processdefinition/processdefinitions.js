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
	
	ProcessDefinition.init(url);
	Group.init(url);
	User.init(url);
	ProcessInstance.init(url);
	Form.init(url);
	Deployment.init(url);
	
	var $processDefinitionsGrid = $('#process-definitions-grid-data').bootgrid({
	    url: url + ProcessDefinition.getUrl('listOfProcessDefinitions'),
	    formatters: {
	    	'name': function(column, row) {
	    		return '<button type="button" class="btn btn-link command-details" data-row-id="' + row.id + '">' + row.name + ' (v' + row.version + ')' + '</button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	request.searchPhrase = $('form[name="formFilterProcessDefinition"]').serializeArray();
	    	return ProcessDefinition.requestFilter(request);
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<button type="button" class="btn btn-dark" id="btnFilterSearch" name="btnFilterSearch" data-toggle="tooltip" title="Filter"><i class="fas fa-search" aria-hidden="true"></i></button>' +
            		'<!--Your Button goes here-->' +
            		'</div>' + 
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>'
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$processDefinitionsGrid.find(".command-details").on("click", function(e) {
	    	var $form = $('form[name="formProcessDefinition"]');
	    	var processDefinitionId = $(this).data("row-id");
	    	$('div[name="bpmnModel"]').html('');
	    	$('#process-pills-tab li:first-child a').tab('show'); // Select first tab
	    	$('#process-pills-tab a:eq(1)').removeClass('disabled');
	    	
	    	ProcessDefinition.edit(processDefinitionId, {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($form, [response.body]);
		        		var data = JSON.parse('{"processDefinitionId":"' + processDefinitionId + '"}');
			            _showProcessDiagram(data);
			            $candidateStarters.bootgrid('reload');
		        	}
				}
			});
		});
	});
	
	$('button[name="btnFilterSearch"]').on('click', function() {
		var $modal = $('#processDefinitionFilterSeachModal');
		$modal.find('.modal-title').text('Filter process definitions');
		$modal.modal('show');
	});
	
	$('form[name="formFilterProcessDefinition"]').validate({
		submitHandler: function(form) {
			$processDefinitionsGrid.bootgrid('reload');
			$('#processDefinitionFilterSeachModal').modal('hide');
		}
	});
	
	$('form[name="formProcessDefinition"]').validate({
		submitHandler: function(form) {
			ProcessDefinition.updateCategory($(form).serializeArray(), {
				success: function(response) {           	
					if(response.code >= 400) {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"](response.reason);
	    				$processDefinitionsGrid.bootgrid('reload');
	            	}
				}
			});
		}
	});
	
	var $candidateStarters = $('#candidate-starters-grid-data').bootgrid({
	    url: url + ProcessDefinition.getUrl('getAllCandidateStarters'),
        rowCount: [5, 10, 25, 50, -1], // rows per page int or array of int (-1 represents "All")
	    formatters: {
	    	"name": function(column, row) {
	            return (row.user == null) ? row.group : row.user;
	        },
	    	"family": function(column, row) {
	            return (row.user == null) ? 'group' : 'user';
	        },
	    	"commands": function(column, row) {
	            return '<button type="button" class="btn btn-sm btn-dark command-delete-candidate-starter" data-user-id="' + row.user + '" data-group-id="' + row.group + '" data-toggle="tooltip" title="Delete"><span class="fas fa-trash-alt"></span></button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	var id = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
	    	if(id != '' && id != undefined) request.id = id;
	    	return request;
	    },
	    responseHandler: function (response) {
        	return {
	    		total : response.body.length,
	    		current : this.orgRequest.current,
	    		rowCount : this.orgRequest.rowCount,
	    		rows : response.body
	    	};
        },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<select id="addCandidateStarterUser" name="addCandidateStarterUser" placeholder="Add a user"></select>' +
            		'<button type="button" class="btn btn-dark" id="btnAddUser" name="btnAddUser" data-toggle="tooltip" title="Add user"><i class="fas fa-plus" aria-hidden="true"></i></button></div>' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<select id="addCandidateStarterGroup" name="addCandidateStarterGroup" placeholder="Add a group"></select>' +
            		'<button type="button" class="btn btn-dark" id="btnAddGroup" name="btnAddGroup" data-toggle="tooltip" title="Add group"><i class="fas fa-plus" aria-hidden="true"></i></button></div>' +
            		'<!--Your Button goes here-->' +
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>',
            search: '<div class="{{css.search}} d-none"><div class="input-group"><span class="input-group-addon"><i class="{{css.icon}} {{css.iconSearch}}"></i></span> <input type="text" class="{{css.searchField}}" placeholder="{{lbl.search}}" /></div></div>',
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$candidateStarters.find(".command-delete-candidate-starter").on("click", function(e) {
	    	var userId = $(this).data("user-id");
	    	var groupId = $(this).data("group-id");
	    	var processDefinitionId = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
	    	var candidateStarterId = (userId == null) ? groupId : userId;
	    	var family = (userId == null) ? 'groups' : 'users';
	    	
	    	bootbox.confirm({
			    title: 'Remove candidate starter',
			    message: 'Do you really want to candidate starter with id "' + candidateStarterId + '" from process definition "' + processDefinitionId + '"?',
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
			        	return ProcessDefinition.deleteCandidateStarter(processDefinitionId, family, candidateStarterId, {
							success: function(response) {           	
					    		if(response.code >= '400') {
				            		if(response.body.hasOwnProperty('exception'))
				            			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
				            	} else {
				    				toastr["success"]('Candidate starter successfully removed');
				    				$candidateStarters.bootgrid('reload');
				            	}
							}
						});
					}
			    }
			});
	    });
	});
	
	$('select[name="addCandidateStarterUser"]').selectize({
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
		},
		onChange: function(value) {
			if(value != '' || value != undefined || value != null) {  		
				$('button[name="btnAddUser"]').data('user-id', value);
			}
		}
	});
	
	$('select[name="addCandidateStarterGroup"]').selectize({
		valueField: 'id',
		labelField: 'name',
		searchField: 'name',
	    sortField: 'name',
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
				return '<option value="' + item.groupId + '" data-data="' + JSON.stringify(item) + '">' + item.name + '</option>';
			},
			item: function(item, escape) {
				return '<div>' + item.name + '</div>';
			}
		},
		load: function(query, callback) {
			if (!query.length) return callback();
			
			Ajax.executeAjax(
				url + Group.getUrl('listOfGroups'), 
				{ 'nameLike': '%' + query + '%' },
				{
					type: 'POST',
		            dataType: 'json',
					error: function(res) {
						toastr["error"]('Could not load groups');
						console.log(res);
					},
					success: function(res) {
						if(res.code >= '400') {
							toastr["error"]('Could not load groups');
							return false;
						} else {
							callback(res.body.data.slice(0,15));
						}
					}
				}
			);
		},
		onChange: function(value) {
			if(value != '' || value != undefined || value != null) {	
				$('button[name="btnAddGroup"]').data('group-id', value);
			}
		}
	});
	
	$('button[name="btnAddUser"]').bind('click', function() {
		var $button = $(this);
    	var processDefinitionId = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
		var userId = $button.data('user-id');
		
		ProcessDefinition.addCandidateStarter(processDefinitionId, 'user', userId, {
			success: function(response) {           	
	    		if(response.code >= '400') {
            		if(response.body.hasOwnProperty('exception'))
            			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
            	} else {
    				toastr["success"]('User successfully set as candidate starter for process definition with id "' + processDefinitionId + '"');
    				$candidateStarters.bootgrid('reload');
    				$button.removeData('user-id');
    				$('select[name="addCandidateStarterUser"]')[0].selectize.clear(true);
            	}
			}
		});
	});
	
	$('button[name="btnAddGroup"]').bind('click', function() {
		var $button = $(this);
    	var processDefinitionId = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
		var groupId = $button.data('group-id');
		
		ProcessDefinition.addCandidateStarter(processDefinitionId, 'group', groupId, {
			success: function(response) {           	
	    		if(response.code >= '400') {
            		if(response.body.hasOwnProperty('exception'))
            			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
            	} else {
    				toastr["success"]('Group successfully set as candidate starter for process definition with id "' + processDefinitionId + '"');
    				$candidateStarters.bootgrid('reload');
    				$button.removeData('group-id');
    				$('select[name="addCandidateStarterGroup"]')[0].selectize.clear(true);
            	}
			}
		});
	});
	
	$('button[name="btnStartProcessInstance"]').on('click', function() {
		var processDefinitionId = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
		var startFormDefined = $('form[name="formProcessDefinition"]').find('input[name="startFormDefined"]').val();
		
		if(startFormDefined === "true") {
			Form.getProcessDefinitionFormData(processDefinitionId, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	            		var formData = response.body;
	            		Deployment.getSingleDeploymentResourceContent(formData.deploymentId, formData.formKey, {
							success: function(response) {           	
				            	if(response.code >= 400) {
					        		if(response.body.hasOwnProperty('exception'))
					        			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
					        	} else {
					        		$('div[name="submitFormContainer"]').html(response.body);
					        		var $modal = $('#submitFormModal');
					        		$modal.find('.modal-title').text('Start process instance');
					        		$modal.modal('show');
					        	}
							}
						});
	            	}
				}
			});
		} else {
			ProcessInstance.startProcessInstance({ processDefinitionId : processDefinitionId }, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"]('Process instance for process definition with id "' + processDefinitionId + '" successfully started');
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
			var processDefinitionId = $('form[name="formProcessDefinition"]').find('input[name="id"]').val();
			
			$.each(elements, function(index, obj) {
				values.push({
					name : $(obj).attr('name'),
					value : $(obj).val()
				});
			});
			
			var formData = { 
					processDefinitionId : processDefinitionId,
					variables : JSON.stringify(values)
			};
			
			ProcessInstance.startProcessInstance(formData, {
				success: function(response) {           	
		    		if(response.code >= '400') {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"]('Process instance for process definition with id "' + processDefinitionId + '" successfully started');
	            	}
				}
			});
			$('#submitFormModal').modal('hide');
		}
	});
});