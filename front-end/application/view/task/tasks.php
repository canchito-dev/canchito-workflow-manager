<div class="row">
	<div class="col-4">
        <table id="tasks-grid-data" name="tasks-grid-data" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-type="string" data-visible="false" data-order="desc" data-identifier="true">ID</th>
                    <th data-column-id="name" data-formatter="name">Name</th>
                </tr>
            </thead>
        </table>
	</div>
	
	<div class="col-8">
		<div class="row mt-1">
			<div class="col">
    			<div class="btn-group" role="group" aria-label="Basic example">
    				<button type="button" class="btn btn-secondary" id="btnCompleteTask" name="btnCompleteTask" data-toggle="tooltip" title="Complete Task"><i class="fas fa-toggle-on"></i></button>
    				<button type="button" class="btn btn-secondary" id="btnClaimTask" name="btnClaimTask" data-toggle="tooltip" title="Claim Task"><i class="fas fa-toggle-off"></i></button>
                </div>
			</div>
		</div>
        		
		<form id="formTask" name="formTask" novalidate="novalidate">
			<input type="hidden" class="form-control" id="id" name="id">
			<input type="hidden" class="form-control" id="formKey" name="formKey">
			<input type="hidden" class="form-control" id="processDefinitionId" name="processDefinitionId">
    		<div class="form-row mt-1">
    			<div class="col-6">
    				<div class="form-group">
    					<label for="name" class="font-weight-bold">Name:</label>
    					<input type="text" class="form-control" id="name" name="name" placeholder="Name" required>
    				</div>
    				<div class="form-group">
    					<label for="owner" class="font-weight-bold">Owner:</label>
    					<select id="owner" name="owner" placeholder="Owner"></select>
    				</div>
    				<div class="form-group">
    					<label for="assignee" class="font-weight-bold">Assignee:</label>
    					<select id="assignee" name="assignee" placeholder="Assignee"></select>
    				</div>
    				<div class="form-group">
    					<label for="description" class="font-weight-bold">Description:</label>
    					<textarea class="form-control" id="description" name="description" placeholder="Description" rows="3"></textarea>
    				</div>
    			</div>
    			
    			<div class="col-6">
    				<div class="form-group">
    					<label for="category" class="font-weight-bold">Category:</label>
    					<input type="text" class="form-control" id="category" name="category" placeholder="Category">
    				</div>
    				<div class="form-group">
    					<label for="priority" class="font-weight-bold">Priority:</label>
    					<input type="number" class="form-control" id="priority" name="priority" placeholder="Priority">
    				</div>
    				<div class="form-group">
    					<label for="createTime" class="font-weight-bold">Created date:</label>
    					<input type="text" class="form-control" id="createTime" name="createTime" placeholder="Created date">
    				</div>
    				<div class="form-group">
    					<label for="dueDate" class="font-weight-bold">Due date:</label>
    					<input type="text" class="form-control" id="dueDate" name="dueDate" placeholder="Due date">
    				</div>
    			</div>
    		</div>
		</form>
	</div>
</div>
<script>
	var filterField = '<?php echo $filterField; ?>';
	var filterValue = '<?php echo $filterValue; ?>';
</script>