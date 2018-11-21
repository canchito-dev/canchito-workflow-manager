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
	$("body").mLoading({
		text: "Loading...",
		mask: true
	});
	$("body").mLoading('hide');
	
	$('[data-toggle="tooltip"]').tooltip();
	
	totalTasks();
	window.setInterval(totalTasks(), 30000);
});

function totalTasks() {		
	Ajax.executeAjax(
		url + 'task/getTotalTasks', 
		{ limit: 12, offset: 0, sort: 'name', order: 'asc' }, 
		{
			beforeSend: function(jqXHR, settings) {},
			success: function(response) {
				if(response.code >= 400) {
	        		if(response.body.hasOwnProperty('exception'))
	        			response.reason = response.body.message + '. ' + response.body.exception;
	            	toastr["error"](response.reason);
	        	} else {
	        		$.each($('span[name="badgeInbox"]'), function(index, badge) {
	        			$(badge).html(response.body.assignee);
	        		});
	        		$.each($('span[name="badgeOwner"]'), function(index, badge) {
	        			$(badge).html(response.body.owner);
	        		});
	        		$.each($('span[name="badgeInvolved"]'), function(index, badge) {
	        			$(badge).html(response.body.involved);
	        		});
	        		$.each($('span[name="badgeGroup"]'), function(index, badge) {
	        			$(badge).html(response.body.group);
	        		});
	        		$.each($('span[name="badgeUnassigned"]'), function(index, badge) {
	        			$(badge).html(response.body.unassigned);
	        		});
	        	}
			},
	        complete: function(jqXHR, textStatus) {}
		}
	);
}