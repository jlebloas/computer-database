package fr.jonathanlebloas.computerdatabase.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The name is mendatory")
public class EmptyNameException extends ServiceException {

	private static final long serialVersionUID = -4035282771478091323L;

	public EmptyNameException() {
		super("The name is mendatory");
	}

}
