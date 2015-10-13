package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class EmptyNameException extends Exception {

	private static final long serialVersionUID = -4035282771478091323L;

	public EmptyNameException() {
		super("The name is mendatory");
	}

}
