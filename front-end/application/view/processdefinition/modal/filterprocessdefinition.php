<!-- Modal -->
<div class="modal fade" id="processDefinitionFilterSeachModal" name="processDefinitionFilterSeachModal" tabindex="-1" role="dialog" aria-labelledby="processDefinitionFilterSeachModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="processDefinitionFilterSeachModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formFilterProcessDefinition" name="formFilterProcessDefinition" novalidate="novalidate">
				<div class="modal-body">
    				<div class="container-fluid">
    					<div class="row">
    						<div class="col-6">
    							<div class="form-group">
        							<label class="font-weight-bold" for="nameLike">Name:</label>
        							<input type="text" class="form-control" id="nameLike" name="nameLike" placeholder="Name" 
        								data-rule-maxlength="255">
        						</div>
    							<div class="form-group">
        							<label class="font-weight-bold" for="keyLike">Key:</label>
        							<input type="text" class="form-control" id="keyLike" name="keyLike" placeholder="Key" 
        								data-rule-maxlength="255">
        						</div>
    						</div>
    						<div class="col-6">
    							<div class="form-group">
        							<label class="font-weight-bold" for="resourceNameLike">Resource Name:</label>
        							<input type="text" class="form-control" id="resourceNameLike" name="resourceNameLike" placeholder="Resource Name" 
        								data-rule-maxlength="255">
        						</div>
    							<div class="form-group">
        							<label class="font-weight-bold" for="categoryLike">Category:</label>
        							<input type="text" class="form-control" id="categoryLike" name="categoryLike" placeholder="Category" 
        								data-rule-maxlength="255">
        						</div>
    						</div>
    					</div>
    				</div>
    			</div>
    			<div class="modal-footer">
    				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
    				<button type="reset" class="btn btn-secondary">Clear</button>
    				<button type="submit" class="btn btn-primary">Filter</button>
    			</div>
			</form>
		</div>
	</div>
</div>
<!-- Modal -->