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
	
	ProcessInstance.init(url);
	
	var $processInstancesGrid = $('#process-instances-grid-data').bootgrid({
	    url: url + ProcessInstance.getUrl('listOfProcessInstances'),
	    formatters: {
	    	'id': function(column, row) {
	    		return '<button type="button" class="btn btn-link command-details" data-row-id="' + row.id + '">' + row.id + '</button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	request.searchPhrase = $('form[name="formFilterProcessInstance"]').serializeArray();
	    	return ProcessInstance.requestFilter(request);
	    },
	    templates: {
            header: '<div id="{{ctx.id}}" class="{{css.header}}"><div class="row"><div class="col-sm-12 actionBar">' +
            		'<!--Your Button goes here-->' +
            		'<div class="btn-group mr-4" role="group" aria-label="...">' +
            		'<button type="button" class="btn btn-dark" id="btnBatchDelete" name="btnBatchDelete" data-toggle="tooltip" title="Delete"><i class="fas fa-trash-alt" aria-hidden="true"></i></button>' +
            		'<!--Your Button goes here-->' +
            		'</div>' + 
            		'<p class="{{css.search}}"></p><p class="{{css.actions}}"></p></div></div></div>'
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$processInstancesGrid.find(".command-details").on("click", function(e) {
	    	var $form = $('form[name="formProcessInstance"]');
	    	var processInstanceId = $(this).data("row-id");
	    	$('div[name="bpmnModel"]').html('');
	    	$('#process-pills-tab li:first-child a').tab('show'); // Select first tab
	    	$('#process-pills-tab a:eq(1)').removeClass('disabled');
	    	
	    	ProcessInstance.edit(processInstanceId, {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($form, [response.body]);
		        		$form.find('input[name="started"]').val(new Date(response.body.started).toLocaleString());
		        		
		        		ProcessInstance.getDiagramForProcessInstance(processInstanceId, {
		    				success: function(response) {           	
		    	            	if(response.code >= 400) {
		    		        		if(response.body.hasOwnProperty('exception'))
		    		        			response.reason = response.body.message + '. ' + response.body.exception;
		    		            	toastr["error"](response.reason);
		    		        	} else {
		    		        		$('img[name="imgProcessInstanceDiagram"]').attr('src', response.body);
		    		        	}
		    				}
		    			});
		        	}
				}
			});
		});
	});
	
	$('button[name="btnBatchDelete"]').on('click', function() {
		bootbox.confirm({
		    title: 'Delete process instances',
		    message: 'Do you really want to delete the selected process instances"?',
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
	            	
	            	var selectedRows = $processInstancesGrid.bootgrid('getSelectedRows');
	            	var requests = [];
	            	
	            	$.each(selectedRows, function(key, rowData) {
	            		requests.push(
	            			ProcessInstance.deleteProcessInstance(rowData)
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
	            				toastr["success"]('Selected process instances successfully deleted');
	        					
	        				$processInstancesGrid.bootgrid('reload');
	        				$("body").mLoading('hide');
	                	}
	        		);
				}
		    }
		});
	});
});