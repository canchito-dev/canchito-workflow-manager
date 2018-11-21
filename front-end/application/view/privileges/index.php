<div class="row">
	<div class="col-4">
        <table id="privileges-grid-data" name="privileges-grid-data" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-type="string" data-visible=false data-order="desc" data-identifier="true">ID</th>
                    <th data-column-id="name" data-formatter="name">Name</th>
                </tr>
            </thead>
        </table>
	</div>
	
	<div class="col-8">
		<form id="formPrivilege" name="formPrivilege" novalidate="novalidate">
    		<div class="form-row">
    			<div class="col-6">
    				<input type="hidden" id="id" name="id">
    				<div class="form-group">
    					<label class="font-weight-bold" for="name">Name:</label>
    					<input type="text" class="form-control" id="name" name="name" placeholder="Name" 
    						data-rule-maxlength="255" data-rule-required="true" required>
    				</div>
    			</div>
    		</div>
    		
    		<ul class="nav nav-pills nav-justified" id="privilege-pills-tab" role="tablist">
    			<li class="nav-item">
    				<a class="nav-link active" id="privilege-users-tab" data-toggle="pill" href="#privilege-users" role="tab" aria-controls="privilege-users" aria-selected="true">Users</a>
    			</li>
    			<li class="nav-item">
    				<a class="nav-link" id="privilege-groups-tab" data-toggle="pill" href="#privilege-groups" role="tab" aria-controls="privilege-groups" aria-selected="false">Groups</a>
    			</li>
    		</ul>
    		
    		<div class="tab-content" id="privilege-pills-tabContent">
    			<div class="tab-pane fade show active" id="privilege-users" role="tabpanel" aria-labelledby="privilege-users-tab"><!-- start privilege-users-tab -->
            		<table id="users-grid-data" name="users-grid-data" class="table table-condensed table-hover table-striped">
                        <thead>
                            <tr>
                                <th data-column-id="id" data-type="string" data-order="desc" data-identifier="true">ID</th>
                                <th data-column-id="firstName">First name</th>
                                <th data-column-id="lastName">Last name</th>
                                <th data-column-id="email">E-Mail</th>
                            </tr>
                        </thead>
                    </table>
    			</div><!-- end privilege-users-tab -->
    			
    			<div class="tab-pane fade" id="privilege-groups" role="tabpanel" aria-labelledby="privilege-groups-tab"><!-- start privilege-groupss-tab -->
    				<table id="groups-grid-data" name="groups-grid-data" class="table table-condensed table-hover table-striped">
                        <thead>
                            <tr>
                                <th data-column-id="id" data-type="string" data-order="desc" data-identifier="true">ID</th>
                                <th data-column-id="name">Name</th>
                                <th data-column-id="type">Type</th>
                            </tr>
                        </thead>
                    </table>
    			</div><!-- end privilege-groupss-tab -->
    		</div>
    	</form>
	</div>
</div>