package fr.jonathanlebloas.computerdatabase.controller;

import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@Controller
public class DeleteComputerServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerServlet.class);
	private static final String PATH_REDIRECT_VIEW = "redirect:../dashboard";
	public static final String PARAM_DELETE_ID = "selection";

	@Autowired
	private ComputerService computerService;

	@RequestMapping(path = "/computer/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = PARAM_DELETE_ID, required = true) final String selection) {
		LOGGER.info("Delete computers = {}", selection);

		// Stream the ids
		Stream<Long> stream = Arrays.stream(selection.split(",")).map(Long::parseLong);

		// Find and delete the computers (and log them)
		stream.map(computerService::find).map(computerService::delete)
				.forEach((r) -> LOGGER.info("Computer deleted: {}", r));

		return PATH_REDIRECT_VIEW;
	}
}