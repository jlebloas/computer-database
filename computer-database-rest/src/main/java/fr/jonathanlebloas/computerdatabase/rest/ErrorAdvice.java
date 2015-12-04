package fr.jonathanlebloas.computerdatabase.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller Advice to handle errors and return json containing their
 * description
 */
@ControllerAdvice(annotations = RestController.class)
public class ErrorAdvice {

	/**
	 * Handle the Validations errors and set the status 400
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	ResponseEntity<List<String>> handleException(MethodArgumentNotValidException ex) {
		return new ResponseEntity<>(
				ex.getBindingResult().getAllErrors().stream().map(ErrorAdvice::mapError).collect(Collectors.toList()),
				HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle the Validations errors and set the status 400
	 *
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	ResponseEntity<List<String>> handleException(IllegalArgumentException ex) {
		List<String> list = new ArrayList<String>();
		list.add(ex.getMessage());
		return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Format message error
	 *
	 * @param e
	 * @return
	 */
	private static String mapError(ObjectError e) {
		if (e instanceof FieldError) {
			return ((FieldError) e).getField() + ": " + e.getDefaultMessage();
		} else {
			return e.getDefaultMessage();
		}
	}
}
