</div><!-- END OF MAIN -->

<!-- FOOTER MAIN -->
<footer class="footer bg-dark">
	<div class="container">
		<span class="text-muted">&copy; 2018 canchito-dev, Inc.</span>
	</div>
</footer>
<!-- FOOTER MAIN -->

	<!-- define the project's URL (to make AJAX calls possible, even when using this in sub-folders etc) ================================================== -->
    <script> var url = "<?php echo URL; ?>"; </script>
    
	<!-- Bootstrap core JavaScript ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS -->
	<script src="http://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	
	<!-- jquery-mloading: Latest compiled and minified JS ================================================== -->
	<script src="<?php echo URL; ?>js/jquery-mloading/jquery.mloading.min.js" type="text/javascript"></script>
	
	<!-- jquery-validation: Latest compiled and minified JS ================================================== -->
	<script src="<?php echo URL; ?>js/jquery-validation/jquery.validate.min.js" type="text/javascript"></script>
	<script src="<?php echo URL; ?>js/jquery-validation/additional-methods.min.js" type="text/javascript"></script>
	<script src="<?php echo URL; ?>js/jquery-validation/jquery.validate.defaults.min.js" type="text/javascript"></script>
	
	<!-- toastr: Latest compiled and minified JS ================================================== -->
	<script src="<?php echo URL; ?>js/toastr/toastr.min.js" type="text/javascript"></script>
	
    <!-- ajax util: Latest compiled and minified JS ================================================== -->
	<script src="<?php echo URL; ?>js/utils/ajaxUtil.min.js" type="text/javascript"></script>
    
<?php 
	if(isset($localJs)) {
		foreach ($localJs as $js) { 
?>
    <script src="<?php echo URL; ?>js/<?php echo $js; ?>.js" type="text/javascript"></script>
<?php 
		} 
	}
?>
    
    <!-- Mini-Framework-Mvc ================================================== -->
    <script src="<?php echo URL; ?>js/cwm.js" type="text/javascript"></script>
</body>
</html>
