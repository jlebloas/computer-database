package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class InvalidCompanyException extends ComputerException {

	private static final long serialVersionUID = 8645350515542731082L;

	public InvalidCompanyException(String message) {
		super(message);
	}

	public InvalidCompanyException() {
		super("The computer's company is invalid");
	}

}
