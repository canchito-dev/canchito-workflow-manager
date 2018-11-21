<!-- Modal -->
<div class="modal fade" id="groupFilterSeachModal" name="groupFilterSeachModal" tabindex="-1" role="dialog" aria-labelledby="groupFilterSeachModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="groupFilterSeachModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formFilterGroup" name="formFilterGroup" novalidate="novalidate">
				<div class="modal-body">
    				<div class="container-fluid">
    				
        				<div class="form-group">
    						<label class="font-weight-bold" for="id">Group Id:</label>
    						<div class="input-group mb-2 mb-sm-0">
    							<input type="text" class="form-control" id="id" name="id" placeholder="Group Id" 
    								data-rule-maxlength="255">
    							<div class="input-group-append">
									<span class="input-group-text" id="basic-userId-addon">@<?php echo $_SESSION['TENANT_ID']; ?></span>
								</div>
    						</div>
    					</div>
    					<div class="form-group">
    						<label class="font-weight-bold" for="nameLike">Name:</label>
    						<input type="text" class="form-control" id="nameLike" name="nameLike" placeholder="Name">
    					</div>
    					<div class="form-group">
    						<label class="font-weight-bold" for="type">Type:</label>
    						<input type="text" class="form-control" id="type" name="type" placeholder="Type">
    					</div>
    				
    				</div>
    			</div>
    			<div class="modal-footer">
    				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
    				<button type="submit" class="btn btn-primary">Filter</button>
    			</div>
			</form>
		</div>
	</div>
</div>
<!-- Modal -->