<!-- Fixed navbar -->
<nav class="navbar navbar-expand-md fixed-top navbar-dark bg-dark">
	<a class="navbar-brand" href="<?php echo URL; ?>home">
    	<img src="<?php echo URL; ?>img/header.canchitodev.png" width="25" height="25" alt="">
  	</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarCollapse">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item">
				<a class="nav-link font-weight-bold" href="<?php echo URL; ?>home">Home</a>
			</li>
			<li class="nav-item dropdown font-weight-bold">
				<a class="nav-link dropdown-toggle" href="<?php echo URL; ?>" id="ddMenuProcesses" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Processes
				</a>
				<div class="dropdown-menu" aria-labelledby="ddMenuProcesses">
					<a class="dropdown-item" href="<?php echo URL; ?>job-monitor">Job Monitor</a>
					<div class="dropdown-divider"></div>
					<h6 class="dropdown-header">Process Management</h6>
				  	<a class="dropdown-item" href="<?php echo URL; ?>process-deployments">Process Deployments</a>
				  	<a class="dropdown-item" href="<?php echo URL; ?>process-definitions">Process Definitions</a>
					<div class="dropdown-divider"></div>
					<h6 class="dropdown-header">Process Monitor</h6>
				  	<a class="dropdown-item" href="<?php echo URL; ?>running-instances">Running Instances</a>
				  	<a class="dropdown-item" href="<?php echo URL; ?>historic-instances">Historic Instances</a>
				</div>
			</li>
			<li class="nav-item dropdown font-weight-bold">
				<a class="nav-link dropdown-toggle" href="<?php echo URL; ?>" id="ddMenuManagement" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					Management
				</a>
				<div class="dropdown-menu" aria-labelledby="ddMenuManagement">
					<h6 class="dropdown-header">Identity Access</h6>
				  	<a class="dropdown-item" href="<?php echo URL; ?>users">Users</a>
				  	<a class="dropdown-item" href="<?php echo URL; ?>groups">Groups</a>
					<div class="dropdown-divider"></div>
					<h6 class="dropdown-header">Folder Management</h6>
				  	<a class="dropdown-item" href="<?php echo URL; ?>folder-handlers">Folder Handlers</a>
				</div>
			</li>
		</ul>
		<ul class="navbar-nav">
			<li class="nav-item dropdown font-weight-bold">
				<a class="nav-link dropdown-toggle" href="<?php echo URL; ?>" id="ddMenuUser" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<i class="material-icons">person</i>
				</a>
				<div class="dropdown-menu dropdown-menu-right" aria-labelledby="ddMenuUser">
					<h6 class="dropdown-header">Tasks Management</h6>
					<a class="dropdown-item" href="<?php echo URL; ?>tasks/inbox">Inbox <span class="badge badge-secondary">0</span></a>
					<a class="dropdown-item" href="<?php echo URL; ?>tasks/my-tasks">My Tasks <span class="badge badge-secondary">0</span></a>
					<a class="dropdown-item" href="<?php echo URL; ?>tasks/involved-tasks">Involved Tasks <span class="badge badge-secondary">0</span></a>
					<a class="dropdown-item" href="<?php echo URL; ?>tasks/group-tasks">Group Tasks <span class="badge badge-secondary">0</span></a>
					<div class="dropdown-divider"></div>
					<h6 class="dropdown-header">Account Management</h6>
				  	<a class="dropdown-item" href="<?php echo URL; ?>tasks/profile">Profile</a>
				  	<a class="dropdown-item" href="<?php echo URL; ?>authentication/signout">Sign Out</a>
				</div>
			</li>
		</ul>
	</div>
</nav>
<!-- Fixed navbar -->