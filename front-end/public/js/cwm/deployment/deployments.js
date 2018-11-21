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
	
	Deployment.init(url);
	
	var $deploymentsGrid = $('#deployments-grid-data').bootgrid({
	    url: url + Deployment.getUrl('listOfDeployments'),
	    formatters: {
	    	'name': function(column, row) {
	    		return '<button type="button" class="btn btn-link command-details" data-row-id="' + row.id + '">' + row.name + '</button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	request.searchPhrase = $('form[name="formFilterDeployment"]').serializeArray();
	    	return Deployment.requestFilter(request);
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
		$deploymentsGrid.find(".command-details").on("click", function(e) {
	    	var $form = $('form[name="formDeployment"]');
	    	var deploymentId = $(this).data("row-id");
	    	$('#deployments-pills li:first-child a').tab('show');
	    	$('#resources-grid-data').bootgrid('destroy');
	    	
	    	Deployment.edit(deploymentId, {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($form, [response.body]);
		        		$form.find('input[name="deploymentTime"]').val(new Date(response.body.deploymentTime).toLocaleString());
		        		
		        		var $resourcesGrid = $('#resources-grid-data').bootgrid({
						    url: url + Deployment.getUrl('listOfResourcesInDeployment'),
						    formatters: {
						    	'commands': function(column, row) {
						    		return '<button type="button" class="btn btn-sm btn-dark command-resource-download" data-row-deployment-id="' + deploymentId + '" data-row-resource-id="' + row.id + '" data-toggle="tooltip" title="Download resource"><span class="fas fa-cloud-download-alt"></span></button>';
						        }
						    },
						    fieldHandler: function(request) {
						    	request.deploymentId = deploymentId;
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
					        sortable: false,
					        sorting: false
						}).on("loaded.rs.jquery.bootgrid", function() {
							$resourcesGrid.find(".command-resource-download").on("click", function(e) {
								/**
								 * The button is found in bootstrap table's toolbar for the assets
								 * Opens modal for filtering assets
								 * CAUTION: when debugging with XDEBUG Ajax response is error instead of Success.
								 **/
								var resourceId = $(this).data("row-resource-id");
								
								Deployment.getSingleDeploymentResourceContent($(this).data("row-deployment-id"), resourceId, {
									success: function(response) {           	
						            	if(response.code >= 400) {
							        		if(response.body.hasOwnProperty('exception'))
							        			response.reason = response.body.message + '. ' + response.body.exception;
							            	toastr["error"](response.reason);
							        	} else {
							        		var blob = new Blob([response.body]);
										    var link = document.createElement('a');
										    link.href = window.URL.createObjectURL(blob);
										    link.download = resourceId;
										    link.click();
							        	}
									}
								});
							});
						});
		        		
		        		$('#deployments-pills li:last-child a').removeClass('disabled');
		        	}
				}
			});
		});
	});
	
	$('button[name="btnNew"]').on('click', function() {
		$('input[name="files"]').trigger('click');
	});
	
	$('button[name="btnFilterSearch"]').on('click', function() {
		var $modal = $('#deploymentFilterSeachModal');
		$modal.find('.modal-title').text('Filter deployments');
		$modal.modal('show');
	});
	
	$('button[name="btnBatchDelete"]').on('click', function() {
		bootbox.confirm({
		    title: 'Delete deployments',
		    message: 'Do you really want to delete the selected deployments"?',
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
	            	
	            	var selectedRows = $deploymentsGrid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, rowData) {
	            		requests.push(
	            			Deployment.erase(rowData)
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
	            				toastr["success"]('Selected deployments successfully deleted');
	        					
	        				$deploymentsGrid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
	
	$('#deploymentModal').on('show.bs.modal', function (e) {});
	
	$('#deploymentModal').on('hidden.bs.modal', function (e) {
		$('form[name="formDeployment"]').validate().resetForm();
		
		$('form[name="formDeployment"]').trigger("reset");
		
		$('form[name="formDeployment"]').find('.form-control')
			.each(function () { $(this).removeClass('is-invalid'); });
		
		$('form[name="formDeployment"]').find('input[name="action"]').val('new');
	});
	
	$('form[name="formDeployment"]').validate({
		submitHandler: function(form) {
			var $deploymentId = $('form[name="formDeployment"]').find('input[name="id"]');

			Deployment.save($(form).serializeArray(), {
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
	    				$deploymentsGrid.bootgrid('reload');
	    				$('#deploymentModal').modal('hide');
	            	}
				}
			});
		}
	});
	
	$('form[name="formFilterDeployment"]').validate({
		submitHandler: function(form) {
			$deploymentsGrid.bootgrid('reload');
			$('#deploymentFilterSeachModal').modal('hide');
		}
	});
	
	$('input[name="files"]').fileupload({
		url: url + Deployment.getUrl('save'),
		type: 'POST',
		dataType: 'json',
		autoUpload: true,
		acceptFileTypes: /(\.|\/)(xml|zip|bpmn|bpmn20|bar)$/i,
		limitMultiFileUploadSize: 1000000,
		singleFileUploads: true,
		multipart: true,
		start: function(e) {
			$("body").mLoading('show');
		},
        done: function(e, data) {
        	// data.result
            // data.textStatus;
            // data.jqXHR;
        	if(data.result.code >= '400') {
            	toastr["error"](data.result.body.message + '. ' + data.result.body.exception);
			} else {
				toastr["success"](data.result.reason + '. Deployment uploaded');
				$deploymentsGrid.bootgrid('reload');
        	}
        },
		fail: function(e, data) {
			// data.errorThrown
            // data.textStatus;
            // data.jqXHR;
			toastr["error"](data.jqXHR.responseText);
        },
        progressall: function(e, data) {
        	// data.loaded 
        	// data.total
        	console.log('Progress all loaded: ' + data.loaded + ' of total: ' + data.total + '(' + parseInt(data.loaded / data.total * 100, 10) + '%)');
        },
        stop: function (e) {
        	$("body").mLoading('hide');
        }
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});