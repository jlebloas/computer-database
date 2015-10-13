package fr.jonathanlebloas.computerdatabase.service.exceptions;

public class CompanyNotFoundException extends CompanyException {

	private static final long serialVersionUID = -9134002018046537161L;

	public CompanyNotFoundException() {
		super("The company does not exist.");
	}

}