<div class="row">
	<div class="col-4">
        <table id="process-instances-grid-data" name="process-instances-grid-data" class="table table-condensed table-hover table-striped">
            <thead>
                <tr>
                    <th data-column-id="id" data-type="string" data-formatter="id" data-order="desc" data-identifier="true">ID</th>
                </tr>
            </thead>
        </table>
	</div>
	
	<div class="col-8">
		<div class="row mt-1">
			<div class="col">
    			<div class="btn-group" role="group" aria-label="Basic example">
    				<button type="button" class="btn btn-secondary" id="btnActivateProcessDefinition" name="btnActivateProcessDefinition" data-toggle="tooltip" title="Activate"><i class="fas fa-toggle-on"></i></button>
    				<button type="button" class="btn btn-secondary" id="btnSuspendProcessDefinition" name="btnSuspendProcessDefinition" data-toggle="tooltip" title="Suspend"><i class="fas fa-toggle-off"></i></button>
                </div>
			</div>
		</div>
		
		<form id="formProcessInstance" name="formProcessInstance" novalidate="novalidate">
    		<div class="form-row mt-1">
    			<div class="col-6">
    				<div class="form-group">
    					<label for="id" class="font-weight-bold">Id:</label>
    					<input type="text" class="form-control" id="id" name="id" placeholder="Id:" readonly>
    				</div>
    				<div class="form-group">
    					<label for="businessKey" class="font-weight-bold">Business Key:</label>
    					<input type="text" class="form-control" id="businessKey" name="businessKey" placeholder="Business Key" readonly>
    				</div>
    			</div>
    			
    			<div class="col-6">
    				<div class="form-group">
    					<label for="name" class="font-weight-bold">Name:</label>
    					<input type="text" class="form-control" id="name" name="name" placeholder="Name" readonly>
    				</div>
					<div class="form-group">
						<label for="started" class="font-weight-bold">Started:</label>
						<input type="text" class="form-control" id="started" name="started" placeholder="Started" readonly>
					</div>
    			</div>
    		</div>
		</form>
		
		<div class="row mt-1">
			<div class="col">
    			<img id="imgProcessInstanceDiagram" name="imgProcessInstanceDiagram" alt="" src="">
			</div>
		</div>
	</div>
</div>