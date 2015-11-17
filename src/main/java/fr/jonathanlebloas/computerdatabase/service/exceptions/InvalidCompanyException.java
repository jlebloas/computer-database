package fr.jonathanlebloas.computerdatabase.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid company")
public class InvalidCompanyException extends ComputerException {

	private static final long serialVersionUID = 8645350515542731082L;

	public InvalidCompanyException(String message) {
		super(message);
	}

	public InvalidCompanyException() {
		super("The company is invalid");
	}

}
