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
	
	Privilege.init(url);
	
	var $privilegesGrid = $('#privileges-grid-data').bootgrid({
	    url: url + Privilege.getUrl('getListOfPrivileges'),
	    formatters: {
	    	'name': function(column, row) {
	    		return '<button type="button" class="btn btn-link command-details" data-row-id="' + row.id + '">' + row.name + '</button>';
	        }
	    },
	    fieldHandler: function(request) {
	    	return Privilege.requestFilter(request);
	    },
	    responseHandler: function (response) {
	    	if(response.hasOwnProperty('code')) {
	    		if(response.code >= 400) {
	        		if(response.body.hasOwnProperty('exception'))
	        			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
	        	} else {
	        		return {
	    	    		total : response.body.total,
	    	    		current : this.orgRequest.current,
	    	    		rowCount : this.orgRequest.rowCount,
	    	    		rows : response.body.data
	    	    	};
	        	}
	    	} else {
	    		return {
		    		total : response.body.total,
		    		current : this.orgRequest.current,
		    		rowCount : this.orgRequest.rowCount,
		    		rows : response.body.data
		    	};
	    	}
        }
	}).on("loaded.rs.jquery.bootgrid", function() {
	    /* Executes after data is loaded and rendered */
		$privilegesGrid.find(".command-details").on("click", function(e) {
	    	var $form = $('form[name="formPrivilege"]');
	    	var privilegeId = $(this).data("row-id");
	    	$('#privilege-pills-tab li:first-child a').tab('show');
	    	$usersGrid.bootgrid('clear');
    		$groupsGrid.bootgrid('clear');
	    	
	    	Privilege.getSinglePrivilege(privilegeId, {
				success: function(response) {           	
	            	if(response.code >= 400) {
		        		if(response.body.hasOwnProperty('exception'))
		        			response.reason = response.body.message + '. ' + response.body.exception;
		            	toastr["error"](response.reason);
		        	} else {
		        		FormUtil.mapRestVariablesToForm($form, [response.body]);
		        		$usersGrid.bootgrid('append', response.body.users);
		        		$groupsGrid.bootgrid('append', response.body.groups);
		        	}
				}
			});
		});
	});
	
	var $usersGrid = $('#users-grid-data').bootgrid({ ajax: false });
	var $groupsGrid = $('#groups-grid-data').bootgrid({ ajax: false });
});