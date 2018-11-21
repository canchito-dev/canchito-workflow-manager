<div class="row">
	<div class="col-4">
        <table id="process-definitions-grid-data" name="process-definitions-grid-data" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-type="string" data-visible="false" data-order="desc" data-identifier="true">ID</th>
                    <th data-column-id="name" data-formatter="name">Name</th>
                </tr>
            </thead>
        </table>
	</div>
	
	<div class="col-8">
		<ul class="nav nav-pills nav-justified" id="process-pills-tab" role="tablist">
			<li class="nav-item">
				<a class="nav-link active" id="process-details-tab" data-toggle="pill" href="#process-details" role="tab" aria-controls="process-details" aria-selected="true">Details</a>
			</li>
			<li class="nav-item">
				<a class="nav-link disabled" id="process-candicate-tab" data-toggle="pill" href="#process-candicate" role="tab" aria-controls="process-candicate" aria-selected="false">Candidates Starters</a>
			</li>
		</ul>
		
		<div class="tab-content" id="process-pills-tabContent">
			<div class="tab-pane fade show active" id="process-details" role="tabpanel" aria-labelledby="process-details-tab"><!-- start process-details-tab -->
        		<div class="row mt-1">
        			<div class="col">
            			<div class="btn-group" role="group" aria-label="Basic example">
            				<button type="button" class="btn btn-secondary" id="btnActivateProcessDefinition" name="btnActivateProcessDefinition" data-toggle="tooltip" title="Activate"><i class="fas fa-toggle-on"></i></button>
            				<button type="button" class="btn btn-secondary" id="btnSuspendProcessDefinition" name="btnSuspendProcessDefinition" data-toggle="tooltip" title="Suspend"><i class="fas fa-toggle-off"></i></button>
            				<button type="button" class="btn btn-secondary" id="btnDownloadResource" name="btnDownloadResource" data-toggle="tooltip" title="Download resource"><i class="fas fa-cloud-download-alt"></i></button>
            				<button type="button" class="btn btn-secondary" id="btnStartProcessInstance" name="btnStartProcessInstance" data-toggle="tooltip" title="Start process instance"><i class="fas fa-play-circle"></i></button>
                        </div>
        			</div>
        		</div>
        		
        		<form id="formProcessDefinition" name="formProcessDefinition" novalidate="novalidate">
        			<input type="hidden" class="form-control" id="id" name="id">
        			<input type="hidden" class="form-control" id="startFormDefined" name="startFormDefined">
            		<div class="form-row mt-1">
            			<div class="col-6">
            				<div class="form-group">
            					<label for="name" class="font-weight-bold">Name:</label>
            					<input type="text" class="form-control" id="name" name="name" placeholder="Name" readonly>
            				</div>
            				<div class="form-group">
            					<label for="key" class="font-weight-bold">Key:</label>
            					<input type="text" class="form-control" id="key" name="key" placeholder="Key" readonly>
            				</div>
            				<div class="form-group">
            					<label for="description" class="font-weight-bold">Description:</label>
            					<textarea class="form-control" id="description" name="description" placeholder="Description" readonly rows="3"></textarea>
            				</div>
            			</div>
            			
            			<div class="col-6">
            				<div class="form-group">
            					<label for="category" class="font-weight-bold">Category:</label>
            					<div class="input-group">
            						<input type="text" class="form-control" id="category" name="category" placeholder="Category">
            						<div class="input-group-append">
            							<button type="submit" class="btn btn-dark" id="btnUpdateCategory" name="btnUpdateCategory" data-toggle="tooltip" title="Update category"><i class="far fa-save"></i></button>
            						</div>
            					</div>
            				</div>
            				<div class="form-group">
            					<label for="version" class="font-weight-bold">Version:</label>
            					<input type="number" class="form-control" id="version" name="version" placeholder="Version" readonly>
            				</div>
            			</div>
            		</div>
        		</form>
        		
        		<div class="row">
        			<div class="col">
        			<?php require APP . 'view/diagramviewer/diagramviewer.php'; ?>
        			</div>
        		</div>
			</div><!-- end process-details-tab -->
			
			<div class="tab-pane fade" id="process-candicate" role="tabpanel" aria-labelledby="process-candicate-tab"><!-- start process-candidate-starters-tab -->
				<table id="candidate-starters-grid-data" class="table table-condensed table-hover table-striped">
                    <thead>
                        <tr>
                            <th data-column-id="name" data-formatter="name">Name</th>
                            <th data-column-id="family" data-formatter="family">Family</th>
                            <th data-column-id="type">Type</th>
                            <th data-column-id="candidate-starters-grid-commands" data-formatter="commands" data-sortable="false">Commands</th>
                        </tr>
                    </thead>
                </table>
			</div><!-- end process-candidate-starters-tab -->
		</div>
	</div>
</div>