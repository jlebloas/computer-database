/**
 * Validation functions
 */

/**
 * Check the computerName and display the associated error message
 * 
 * @returns {Boolean}
 */
function validName() {
	var name = $("form div input[name=computerName]").val();

	if (!name) {
		$("form").find("div").has("input[name=computerName]").addClass(
				"has-error");
		$("#computerNameErrorMessage").removeClass("sr-only");
		return false;
	}
	
	$("form").find("div").has("input[name=computerName]").removeClass("has-error");
	$("#computerNameErrorMessage").addClass("sr-only");
	return true;
}

/**
 * Check the introduced date and display the associated error message
 * 
 * @returns {Boolean}
 */
function validIntroduced(introduced) {
	if (introduced != null && introduced != '' && Number.isNaN(introduced)) {
		$("form").find("div").has("input[name=introduced]").addClass("has-error");
		$("#introducedErrorStatus").removeClass("sr-only");
		return false;
	}
	
	$("form").find("div").has("input[name=introduced]").removeClass("has-error");
	$("#introducedErrorStatus").addClass("sr-only");
	return true;
}

/**
 * Check the discontinued date and display associated error messages
 * Also display error message if discontinued is set but not introduced
 * 
 * @returns {Boolean}
 */
function validDiscontinued(discontinued, isValidIntroduced) {
	var temp = true;

	if (discontinued != null && discontinued != '') {
		// There is a discontinued value 
		
		if (Number.isNaN(discontinued)) {
			// A wrong value 
			$("form").find("div").has("input[name=discontinued]").addClass("has-error");
			$("#discontinuedErrorStatus").removeClass("sr-only");
			return false;
			
		} else if (!isValidIntroduced) {
			// If there's a valid discontinued value there must be a introduced value
			$("form").find("div").has("input[name=introduced]").addClass("has-error");
			$("#introducedEmptyErrorStatus").removeClass("sr-only");
			return false;
		}
	}

	$("form").find("div").has("input[name=discontinued]").removeClass("has-error");
	$("#discontinuedErrorStatus").addClass("sr-only");
	$("#introducedEmptyErrorStatus").addClass("sr-only");
	return true;
}

/**
 * Check the dates order and display the associated error message
 * 
 * @returns {Boolean}
 */
function validDateOrder(introduced, discontinued) {
	if (introduced != null && introduced != '' && discontinued != null && discontinued != '' && introduced > discontinued) {
		$("form").find("div").has("input[name=discontinued]").addClass("has-error");
		$("#orderErrorStatus").removeClass("sr-only");
		return false;
	}

	$("#orderErrorStatus").addClass("sr-only");
	return true
}


/**
 * Return boolean in order to stop POST form sending and display errors
 * @returns {Boolean}
 */
function checkForm() {
	// Get dates
	introduced = $("form div input[name=introduced]").val().trim();
	if (introduced) {
		introduced = Date.parse(introduced);
	}
	var discontinued = $("form div input[name=discontinued]").val().trim();
	if (discontinued) {
		discontinued = Date.parse(discontinued);
	}
		
	var isValidName =  validName();	
	var isValidIntroduced = validIntroduced(introduced);
	
	// Only check order if discontinued and introduced are valid
	var isValidDiscontinuedAndOrder = validDiscontinued(discontinued, isValidIntroduced) && validDateOrder(introduced, discontinued);
	
	return isValidName && isValidIntroduced && isValidDiscontinuedAndOrder;
}