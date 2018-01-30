$(document).ready(function() {
	// override jquery validate plugin defaults
	$.validator.setDefaults({
		debug: false,
		focusInvalid: true,
		focusCleanup: false,
	    highlight: function(element) {
	        $(element).addClass('is-invalid');
	    },
	    unhighlight: function(element) {
	        $(element).removeClass('is-invalid');
	    },
	    validClass: 'is-valid',
	    errorElement: 'div',
	    errorClass: 'invalid-feedback',
	    errorPlacement: function(error, element) {
	        if(element.parent('.input-group').length) {
	            error.insertAfter(element.parent());
	        } else {
	            error.insertAfter(element);
	        }
	    },
		invalidHandler: function(event, validator) {
		    var errors = validator.numberOfInvalids();
		    if (errors) {
		      var message = errors == 1
		        ? 'You missed 1 field. It has been highlighted'
		        : 'You missed ' + errors + ' fields. They have been highlighted';
		      toastr["error"](message);
		    }
		}
	});
});