<!-- Modal -->
<div class="modal fade" id="userFilterSeachModal" name="userFilterSeachModal" tabindex="-1" role="dialog" aria-labelledby="userFilterSeachModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="userFilterSeachModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formFilterUser" name="formFilterUser" novalidate="novalidate">
				<div class="modal-body">
    				<div class="container-fluid">
    					<div class="form-group">
							<label class="font-weight-bold" for="firstNameLike">First name:</label>
							<input type="text" class="form-control" id="firstNameLike" name="firstNameLike" placeholder="First name" 
								data-rule-maxlength="255">
						</div>
						<div class="form-group">
							<label class="font-weight-bold" for="lastNameLike">Last name:</label>
							<input type="text" class="form-control" id="lastNameLike" name="lastNameLike" placeholder="Last name">
						</div>
						<div class="form-group">
							<label class="font-weight-bold" for="emailLike">Email:</label>
							<input type="text" class="form-control" id="emailLike" name="emailLike" placeholder="Email"
								data-rule-maxlength="255">
						</div>
						<div class="form-group">
							<label class="font-weight-bold" for="userId">Username:</label>
							<div class="input-group mb-2 mb-sm-0">
								<input type="text" class="form-control" id="userId" name="id" placeholder="Username" 
									data-rule-maxlength="255">
								<div class="input-group-addon">@<?php echo $_SESSION['TENANT_ID']; ?></div>
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