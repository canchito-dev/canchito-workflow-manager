<div class="row">
	<div class="col-4">
        <table id="deployments-grid-data" name="deployments-grid-data" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-type="string" data-visible="false" data-order="desc" data-identifier="true">ID</th>
                    <th data-column-id="name" data-formatter="name">Name</th>
                </tr>
            </thead>
        </table>
	</div>
	
	<div class="col-8">
		<ul class="nav nav-tabs nav-pills nav-fill" id="deployments-pills" name="deployments-pills" role="tablist">
			<li class="nav-item">
				<a class="nav-link active" id="details-tab" data-toggle="tab" href="#details" role="tab" aria-controls="details" aria-selected="true">Details</a>
			</li>
			<li class="nav-item">
				<a class="nav-link disabled" id="resources-tab" data-toggle="tab" href="#resources" role="tab" aria-controls="resources" aria-selected="false">Resources</a>
			</li>
        </ul>
        <div class="tab-content" id="deployments-pills-content">
			<div class="tab-pane fade show active" id="details" role="tabpanel" aria-labelledby="details-tab">
				<form id="formDeployment" name="formDeployment" novalidate="novalidate">
					<input type="hidden" class="form-control" id="id" name="id">
					<div class="form-row mt-1">
						<div class="col-6">
							<div class="form-group">
								<label for="name" class="font-weight-bold">Name:</label>
								<input type="text" class="form-control" id="name" name="name" placeholder="Name" readonly>
							</div>
							<div class="form-group">
								<label for="category" class="font-weight-bold">Category:</label>
								<input type="text" class="form-control" id="category" name="category" placeholder="Category" readonly>
							</div>
							<div class="form-group">
								<label for="deploymentTime" class="font-weight-bold">Deployment Time:</label>
								<input type="text" class="form-control" id="deploymentTime" name="deploymentTime" placeholder="Deployment Time" readonly>
							</div>
						</div>
					</div>
				</form>
			</div>
			
			<div class="tab-pane fade" id="resources" role="tabpanel" aria-labelledby="resources-tab">
				<table id="resources-grid-data" name="resources-grid-data" class="table table-condensed table-hover table-striped mt-1">
                    <thead>
                        <tr>
                            <th data-column-id="id" data-type="string" data-order="desc" data-identifier="true">ID</th>
                            <th data-column-id="mediaType">Media</th>
                            <th data-column-id="type">type</th>
                            <th data-column-id="commands" data-formatter="commands" data-sortable="false">Commands</th>
                        </tr>
                    </thead>
                </table>
			</div>
        </div>
	</div>
</div>
<div class="d-none">
    <input type="file" id="files" name="files" accept="application/xml, application/zip, application/bar, application/bpmn20, application/bpmn" multiple>
</div>