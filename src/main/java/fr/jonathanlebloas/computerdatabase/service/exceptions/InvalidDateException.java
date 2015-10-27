package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class InvalidDateException extends ServiceException {

	private static final long serialVersionUID = -672589633176508752L;

	public InvalidDateException() {
		super("Your date is invalid");
	}

	public InvalidDateException(String message) {
		super(message);
	}


}
