package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 7853558355369792429L;

	public ServiceException() {
		super("An error occured");
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

}
