package fr.jonathanlebloas.computerdatabase.rest;

import javax.validation.Validator;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Rest application
 */
public class RestApplication extends ResourceConfig {

	@Autowired
	public RestApplication(Validator validator) {
		register(RequestContextFilter.class);
		register(ComputerRessource.class);
		register(CompanyRessource.class);

		// Validation.
		register(ValidationConfig.class);
		// Send validation errors to the client.
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
	}
}
