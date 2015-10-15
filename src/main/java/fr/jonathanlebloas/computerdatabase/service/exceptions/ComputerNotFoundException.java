package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class ComputerNotFoundException extends ComputerException {

	private static final long serialVersionUID = -4550781599952711581L;

	public ComputerNotFoundException() {
		super("The computer does not exist.");
	}

}
