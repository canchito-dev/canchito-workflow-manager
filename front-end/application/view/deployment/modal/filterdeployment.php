<!-- Modal -->
<div class="modal fade" id="deploymentFilterSeachModal" name="deploymentFilterSeachModal" tabindex="-1" role="dialog" aria-labelledby="deploymentFilterSeachModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="deploymentFilterSeachModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formFilterDeployment" name="formFilterDeployment" novalidate="novalidate">
				<div class="modal-body">
    				<div class="container-fluid">
    					<div class="form-group">
							<label class="font-weight-bold" for="nameLike">Name:</label>
							<input type="text" class="form-control" id="nameLike" name="nameLike" placeholder="Name" 
								data-rule-maxlength="255">
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