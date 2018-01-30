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
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js" integrity="sha384-b/U6ypiBEHpOf/4+1nzFpr53nxSS+GLCkfwBdFNTxtclqqenISfwAzpKaMNFNmj4" crossorigin="anonymous"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js" integrity="sha384-h0AbiXch4ZDo7tp9hKZ4TsHbi047NrKGLO3SEJAg45jXxnGIfYzk4Si90RDIqNm1" crossorigin="anonymous"></script>
	
	<!-- font-awesome: Latest compiled and minified JS ================================================== -->
	<script defer src="https://use.fontawesome.com/releases/v5.0.1/js/all.js"></script>
    <!-- 	<script src="https://use.fontawesome.com/9f2ac9fd56.js"></script> -->
	
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
