package fr.jonathanlebloas.computerdatabase.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The company does not exist")
public class CompanyNotFoundException extends CompanyException {

	private static final long serialVersionUID = -9134002018046537161L;

	public CompanyNotFoundException() {
		super("The company does not exist.");
	}

}
