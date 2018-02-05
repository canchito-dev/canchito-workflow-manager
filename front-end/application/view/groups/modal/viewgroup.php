<!-- Modal -->
<div class="modal fade" id="groupModal" name="groupModal" tabindex="-1" role="dialog" aria-labelledby="groupModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="groupModalLabel">Modal title</h5>
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form id="formGroup" name="formGroup" novalidate="novalidate">
				<input type="hidden" id="action" name="action">
				<div class="modal-body">
    				<div class="container-fluid">
						<ul class="nav nav-pills nav-justified" id="group-pills-tab" role="tablist">
							<li class="nav-item">
								<a class="nav-link active" id="group-details-tab" data-toggle="pill" href="#group-details" role="tab" aria-controls="group-details" aria-selected="true">Details</a>
							</li>
							<li class="nav-item">
								<a class="nav-link disabled" id="group-users-tab" data-toggle="pill" href="#group-users" role="tab" aria-controls="group-users" aria-selected="false">Users</a>
							</li>
							<li class="nav-item">
								<a class="nav-link disabled" id="group-processes-tab" data-toggle="pill" href="#group-processes" role="tab" aria-controls="group-processes" aria-selected="false">Processes</a>
							</li>
						</ul>
                        <div class="tab-content" id="group-pills-tabContent">
							<div class="tab-pane fade show active" id="group-details" role="tabpanel" aria-labelledby="group-details-tab"><!-- start group-details-tab -->
								<div class="form-group">
									<label class="font-weight-bold" for="id">Group Id:</label>
									<div class="input-group mb-2 mb-sm-0">
										<input type="text" class="form-control" id="id" name="id" placeholder="Group Id" 
											data-rule-maxlength="255" required>
										<div class="input-group-addon">@<?php echo $_SESSION['TENANT_ID']; ?></div>
									</div>
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="name">Name:</label>
									<input type="text" class="form-control" id="name" name="name" placeholder="Name" required>
								</div>
								<div class="form-group">
									<label class="font-weight-bold" for="type">Type:</label>
									<input type="text" class="form-control" id="type" name="type" placeholder="Type" required>
								</div>
							</div><!-- end group-details-tab -->
							
							<div class="tab-pane fade" id="group-users" role="tabpanel" aria-labelledby="group-users-tab"><!-- start group-users-tab -->
								<table id="users-grid-data" class="table table-condensed table-hover table-striped">
                                    <thead>
                                        <tr>
                                            <th data-column-id="id" data-type="string" data-order="desc" data-identifier="true" data-visible="false">ID</th>
                                            <th data-column-id="firstName">First Name</th>
                                            <th data-column-id="lastName">Last Name</th>
                                            <th data-column-id="users-grid-commands" data-formatter="commands" data-sortable="false">Commands</th>
                                        </tr>
                                    </thead>
                                </table>
							</div><!-- end group-users-tab -->
							
							<div class="tab-pane fade" id="group-processes" role="tabpanel" aria-labelledby="group-processes-tab"><!-- start group-processes-tab -->
							process
							</div><!-- end group-processes-tab -->
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