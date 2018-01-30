<!-- Modal -->
<div class="modal fade" id="userModal" name="userModal" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="userModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formUser" name="formUser" novalidate="novalidate">
				<input type="hidden" id="action" name="action">
				<div class="modal-body">
    				<div class="container-fluid">
    					<div class="form-row">
    						<div class="col">
								<div class="form-group">
									<label class="font-weight-bold" for="firstName">First name:</label>
									<input type="text" class="form-control" id="firstName" name="firstName" placeholder="First name" 
										data-rule-maxlength="255" data-rule-required="true" required>
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="lastName">Last name:</label>
									<input type="text" class="form-control" id="lastName" name="lastName" placeholder="Last name" required>
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="email">Email:</label>
									<input type="email" class="form-control" id="email" name="email" placeholder="Email"
										data-rule-email="true" data-rule-maxlength="255" required>
								</div>
							</div>
    						<div class="col">
								<div class="form-group">
									<label class="font-weight-bold" for="id">Username:</label>
									<div class="input-group mb-2 mb-sm-0">
										<input type="text" class="form-control" id="id" name="id" placeholder="Username" 
											data-rule-maxlength="255" required>
										<div class="input-group-addon">@<?php echo $_SESSION['TENANT_ID']; ?></div>
									</div>
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="password">Password:</label>
									<input type="password" class="form-control" id="password" name="password" placeholder="Password"
										data-rule-maxlength="255" data-toggle="password">
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="confirmPassword">Confirm password:</label>
									<input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirm password"
										data-rule-maxlength="255" data-toggle="password">
								</div>
							</div>
    					</div>
    				</div>
    			</div>
    			<div class="modal-footer">
    				<button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
    				<button type="submit" class="btn btn-primary">Save</button>
    			</div>
			</form>
		</div>
	</div>
</div>
<!-- Modal -->