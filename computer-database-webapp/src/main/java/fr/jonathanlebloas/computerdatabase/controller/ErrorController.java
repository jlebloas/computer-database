package fr.jonathanlebloas.computerdatabase.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/error/")
public class ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

	@RequestMapping(path = "400")
	public String errror400() {
		LOGGER.info("Error 400");
		return "400";
	}

	@RequestMapping(path = "401")
	public String errror401() {
		LOGGER.info("Error 401");
		return "401";
	}

	@RequestMapping(path = "403")
	public String errror403() {
		LOGGER.info("Error 403");
		return "403";
	}

	@RequestMapping(path = "404")
	public String errror404() {
		LOGGER.info("Error 404");
		return "404";
	}

	@RequestMapping(path = "500")
	public String errror500() {
		LOGGER.info("Error 500");
		return "500";
	}
}
