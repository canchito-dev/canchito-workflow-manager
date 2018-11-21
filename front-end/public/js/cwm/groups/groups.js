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
	
	Group.init(url);
	
	var $groupsGrid = $('#groups-grid-data').bootgrid({
	    url: url + Group.getUrl('listOfGroups'),
	    formatters: {
	        "commands": function(column, row) {
	            return '<button type="button" class="btn btn-sm btn-dark command-edit" data-row-id="' + row.id + '" data-toggle="tooltip" title="Edit"><span class="fas fa-edit"></span></button> ' + 
	                '<button type="button" class="btn btn-sm btn-dark command-delete" data-row-id="' + row.id + '" data-toggle="tooltip" title="Delete"><span class="fas fa-trash-alt"></span></button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	request.searchPhrase = $('form[name="formFilterGroup"]').serializeArray();
	    	return Group.requestFilter(request);
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<button type="button" class="btn btn-dark" id="btnNew" name="btnNew" data-toggle="tooltip" title="New"><i class="fas fa-plus" aria-hidden="true"></i></button>' +
            		'<button type="button" class="btn btn-dark" id="btnBatchDelete" name="btnBatchDelete" data-toggle="tooltip" title="Delete"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>' +
            		'<button type="button" class="btn btn-dark" id="btnFilterSearch" name="btnFilterSearch" data-toggle="tooltip" title="Filter"><i class="fas fa-search" aria-hidden="true"></i></button>' +
            		'<!--Your Button goes here-->' +
            		'</div>' + 
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>'
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$groupsGrid.find(".command-edit").on("click", function(e) {
	        $('form[name="formGroup"]').find('input[name="action"]').val('update');
			
			Group.edit($(this).data("row-id"), {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($('form[name="formGroup"]'), [response.body]);
		        		
		        		//id of the group must be displayed without the tenant
		        		var groupId = $('form[name="formGroup"]').find('input[name="id"]').val();
		        		groupId = groupId.split('@', 1);
		        		$('form[name="formGroup"]').find('input[name="id"]').val(groupId);
		        		
		        		var $modal = $('#groupModal');
						$modal.find('.modal-title').text('Edit group');
						$modal.modal('show');
		        	}
				}
			});
			
	    }).end().find(".command-delete").on("click", function(e) {
	    	var groupId = $(this).data("row-id");
	    	
	    	bootbox.confirm({
			    title: 'Delete group',
			    message: 'Do you really want to delete group with id "' + groupId + '"?',
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
			        	return Group.erase(groupId, {
							success: function(response) {           	
					    		if(response.code >= '400') {
				            		if(response.body.hasOwnProperty('exception'))
				            			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
				            	} else {
				    				toastr["success"]('Group successfully deleted');
				    				$groupsGrid.bootgrid('reload');
				            	}
							}
						});
					}
			    }
			});
	    });
	});
	
	$('button[name="btnNew"]').on('click', function() {
		$('form[name="formGroup"]').find('input[name="action"]').val('new');
		var $modal = $('#groupModal');
		$modal.find('.modal-title').text('New group');
		$modal.modal('show');
	});
	
	$('button[name="btnFilterSearch"]').on('click', function() {
		var $modal = $('#groupFilterSeachModal');
		$modal.find('.modal-title').text('Filter groups');
		$modal.modal('show');
	});
	
	$('button[name="btnBatchDelete"]').on('click', function() {
		bootbox.confirm({
		    title: 'Delete groups',
		    message: 'Do you really want to delete the selected groups"?',
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
	            	
	            	var selectedRows = $groupsGrid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, rowData) {
	            		requests.push(
	            			Group.erase(rowData)
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
	            				toastr["success"]('Selected groups successfully deleted');
	        					
	        				$groupsGrid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
	
	$('#groupModal').on('show.bs.modal', function (e) {
		if($('form[name="formGroup"]').find('input[name="action"]').val() == 'update') {
    		var $groupId = $('form[name="formGroup"]').find('input[name="id"]');
			$groupId.addClass('disabled');
			$groupId.attr('readonly', true).attr("disabled", true);
			
			$('#group-pills-tab a:eq(1)').removeClass('disabled');	// Enable user binding
			
			$usersGrid.bootgrid('reload');
    	}
	});
	
	$('#groupModal').on('hidden.bs.modal', function (e) {
		$('form[name="formGroup"]').validate().resetForm();
		
		$('form[name="formGroup"]').trigger("reset");
		
		$('form[name="formGroup"]').find('.form-control')
			.each(function () { $(this).removeClass('is-invalid'); });
		
		$('form[name="formGroup"]').find('input[name="action"]').val('new');
		
		var $groupId = $('form[name="formGroup"]').find('input[name="id"]');
		$groupId.removeClass('disabled');
		$groupId.attr('readonly', false).attr("disabled", false);
		
		$('#group-pills-tab li:first-child a').tab('show'); // Select first tab
		$('#group-pills-tab a:eq(1)').addClass('disabled');	// Disable by default user binding
	});
	
	$('form[name="formGroup"]').validate({
		submitHandler: function(form) {
			var $groupId = $('form[name="formGroup"]').find('input[name="id"]');
			$groupId.removeClass('disabled');
			$groupId.attr('readonly', false).attr("disabled", false);
			
			Group.save($(form).serializeArray(), {
				complete: function() {	            	
	            	$("body").mLoading('hide');
	            },
				success: function(response) {           	
					if(response.code >= 400) {
	            		if(response.body.hasOwnProperty('exception'))
	            			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
	            	} else {
	    				toastr["success"](response.reason);
	    				$groupsGrid.bootgrid('reload');
	    				$('#groupModal').modal('hide');
	            	}
				}
			});
		}
	});
	
	$('form[name="formFilterGroup"]').validate({
		submitHandler: function(form) {
			$groupsGrid.bootgrid('reload');
			$('#groupFilterSeachModal').modal('hide');
		}
	});
	
	var $usersGrid = $('#users-grid-data').bootgrid({
	    url: url + Group.getUrl('getMembers'),
        rowCount: [5, 10, 25, 50, -1], // rows per page int or array of int (-1 represents "All")
	    formatters: {
	        "commands": function(column, row) {
	            return '<button type="button" class="btn btn-sm btn-dark command-unbind-user" data-row-id="' + row.id + '" data-toggle="tooltip" title="Unbind"><span class="fas fa-trash-alt"></span></button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	User.init();
	    	request = User.requestFilter(request);
	    	request.memberOfGroup = $('form[name="formGroup"]').find('input[name="id"]').val();
	    	return request;
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<select id="bindUser" name="bindUser" placeholder="Bind a user"></select>' +
            		'<button type="button" class="btn btn-dark" id="btnBindUser" name="btnBindUser" data-toggle="tooltip" title="Bind user"><i class="fas fa-plus" aria-hidden="true"></i></button>' +
            		'<button type="button" class="btn btn-dark" id="btnBatchUnbindUser" name="btnBatchUnbindUser" data-toggle="tooltip" title="Unbind users"><i class="fas fa-trash-alt" aria-hidden="true"></i></button></div>' +
            		'<!--Your Button goes here-->' +
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>',
            search: '<div class="{{css.search}} d-none"><div class="input-group"><span class="input-group-addon"><i class="{{css.icon}} {{css.iconSearch}}"></i></span> <input type="text" class="{{css.searchField}}" placeholder="{{lbl.search}}" /></div></div>',
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$usersGrid.find(".command-unbind-user").on("click", function(e) {
	    	var userId = $(this).data("row-id");
	    	var groupId = $('form[name="formGroup"]').find('input[name="id"]').val();
	    	
	    	bootbox.confirm({
			    title: 'Unbind user from group',
			    message: 'Do you really want to unbind user with id "' + userId + '" from group "' + groupId + '"?',
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
			        	return Group.deleteMemberFromGroup(groupId, userId, {
							success: function(response) {           	
					    		if(response.code >= '400') {
				            		if(response.body.hasOwnProperty('exception'))
				            			response.reason = response.body.message + '. ' + response.body.exception;
					            	toastr["error"](response.reason);
				            	} else {
				    				toastr["success"]('User successfully unbinded from group');
				    				$usersGrid.bootgrid('reload');
				            	}
							}
						});
					}
			    }
			});
	    });
	});
	
	$('select[name="bindUser"]').selectize({
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
				//id of the user without the tenant
        		var userId = userId = value.split('@', 1);        		
				$('button[name="btnBindUser"]').data('user-id', userId);
			}
		}
	});
	
	$('button[name="btnBindUser"]').bind('click', function() {
		var $button = $(this);
		var groupId = $('form[name="formGroup"]').find('input[name="id"]').val();
		var userId = $button.data('userId')[0];
		
		Group.addMemberToGroup(groupId, userId, {
			success: function(response) {           	
	    		if(response.code >= '400') {
            		if(response.body.hasOwnProperty('exception'))
            			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
            	} else {
    				toastr["success"]('User successfully binded to group with id "' + groupId + '"');
    				$usersGrid.bootgrid('reload');
    				$button.removeData('user-id');
    				$('select[name="bindUser"]')[0].selectize.clear(true);
            	}
			}
		});
	});
	
	$('button[name="btnBatchUnbindUser"]').on('click', function() {
    	var groupId = $('form[name="formGroup"]').find('input[name="id"]').val();
    	
		bootbox.confirm({
		    title: 'Unbind users from group',
		    message: 'Do you really want to unbind these users from group id "' + groupId + '"?',
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
	            	
	            	var selectedRows = $usersGrid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, userId) {
	            		requests.push(
	            			Group.deleteMemberFromGroup(groupId, userId)
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
	            				toastr["success"]('Selected users successfully unbinded from group');
	        					
	        				$usersGrid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
});