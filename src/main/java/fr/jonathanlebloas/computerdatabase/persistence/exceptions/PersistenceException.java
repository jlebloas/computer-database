package fr.jonathanlebloas.computerdatabase.persistence.exceptions;

public class PersistenceException extends Exception {

	private static final long serialVersionUID = 754704626136828945L;

	public PersistenceException(Throwable cause) {
		super(cause);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

}
