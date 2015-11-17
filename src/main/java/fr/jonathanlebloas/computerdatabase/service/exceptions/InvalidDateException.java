package fr.jonathanlebloas.computerdatabase.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Date invalid")
public class InvalidDateException extends ServiceException {

	private static final long serialVersionUID = -672589633176508752L;

	public InvalidDateException() {
		super("Your date is invalid");
	}

	public InvalidDateException(String message) {
		super(message);
	}
}
