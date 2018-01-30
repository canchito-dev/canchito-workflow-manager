<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="CANCHITO-WORKFLOW-MANAGER (CWM) is a powerful and yet light-weight and easy to use solution for handling workflows and business processes. At its core is a high performance open-source business process engine based on Flowable (https://www.flowable.org/) with the flexibility and scalability to handle a wide variety of critical processes.">
    <meta name="keywords" content="flowable, bpmn, workflow, activiti, bpmn-engine, workflow-engine, open-source projects, HTML, CSS, Bootstrap, jQuery, JavaScript, Java, PHP, framework">
    <meta name="author" content="José Carlos Mendoza Prego www.linkedin.com/in/jcmendozaprego - www.canchito-dev.com">
    <meta name="robots" content="all"/>
    <link rel="canonical" href="" />
    <link rel="icon" href="<?php echo URL; ?>img/header.canchitodev.png">
    
    <title><?php echo SITE_TITLE; ?></title>

    <!-- Bootstrap: Latest compiled and minified CSS ================================================== -->
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css" integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
	
	<!-- jquery-mloading: Latest compiled and minified CSS ================================================== -->
	<link href="<?php echo URL; ?>css/jquery-mloading/jquery.mloading.min.css" rel="stylesheet" type="text/css">
	
	<!-- Google Material Icons: Latest compiled and minified CSS ================================================== -->
	<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
	
	<!-- toastr: Latest compiled and minified CSS ================================================== -->
	<link href="<?php echo URL; ?>css/toastr/toastr.min.css" rel="stylesheet" type="text/css">

<?php 
	if(isset($localCss)) {
		foreach ($localCss as $css) { 
?>
    <link href="<?php echo URL; ?>css/<?php echo $css; ?>.css" rel="stylesheet" type="text/css">
<?php 
		} 
	}
?>

    <!-- CSS -->
    <link rel="stylesheet" type="text/css" href="<?php echo URL; ?>css/cwm.css" >
</head>
<body>
<div class="container-fluid"><!-- START OF MAIN -->

<?php if(isset($breadcrumbs)) { ?>
	<nav class="breadcrumb"><!-- START OF BREADCRUMBS -->
<?php
	foreach ($breadcrumbs as $link => $uri) {
		if($uri == '' || empty($uri)) {
?>
		<span class="breadcrumb-item active"><?php echo $link; ?></span>
<?php } else { ?>
		<a class="breadcrumb-item" href="<?php echo URL . $uri; ?>"><?php echo $link; ?></a>
<?php
		}
	}
?>
	</nav><!-- END OF BREADCRUMBS -->
<?php } ?>