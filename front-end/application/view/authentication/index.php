<div class="container">
	<div class="row justify-content-center">
	    <div class="col-4">
	    	<form id="formSignIn" name="formSignIn" novalidate>
			  <div class="form-group">
			    <label class="font-weight-bold" for="username">Username:</label>
			    <input type="email" class="form-control" id="username" name="username" aria-describedby="username" placeholder="Username" required>
			  </div>
			  <div class="form-group">
			    <label class="font-weight-bold" for="password">Password:</label>
			    <input class="form-control" id="password" name="password" placeholder="Password" required 
			    	data-rule-maxlength="255" data-toggle="password">
			  </div>
			  <button type="submit" class="btn btn-primary">Log In</button>
			</form>
	    </div>
	</div>
</div>