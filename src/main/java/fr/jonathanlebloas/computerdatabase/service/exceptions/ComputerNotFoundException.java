package fr.jonathanlebloas.computerdatabase.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The computer does not exist")
public class ComputerNotFoundException extends ComputerException {

	private static final long serialVersionUID = -4550781599952711581L;

	public ComputerNotFoundException() {
		super("The computer does not exist.");
	}

}
