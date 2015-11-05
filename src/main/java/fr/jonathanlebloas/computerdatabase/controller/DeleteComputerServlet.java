package fr.jonathanlebloas.computerdatabase.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.utils.ServletUtil;


@WebServlet("/computer/delete")
public class DeleteComputerServlet extends HttpServlet {

	private static final long serialVersionUID = 9049278320734561410L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteComputerServlet.class);

	private static final String PATH_REDIRECT_VIEW = "../dashboard";

	public static final String PARAM_DELETE_ID = "selection";

	@Autowired
	private ComputerService computerService;

	@Autowired
	private ServletUtil servletUtil;

	@Override
	public void init() throws ServletException {
		super.init();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String selection = servletUtil.getStringFromRequest(request, PARAM_DELETE_ID);

		LOGGER.info("Delete computers = {}", selection);

		try {
			// Stream the ids
			Stream<Long> stream = Arrays.stream(selection.split(",")).map(Long::parseLong);

			// Find and delete the computers (and log them)
			stream.map(computerService::find).map(computerService::delete)
					.forEach((r) -> LOGGER.info("Computer deleted: {}", r));

			response.sendRedirect(PATH_REDIRECT_VIEW);

		} catch (NumberFormatException e) {
			LOGGER.warn("Delete Computer : error parsing an id", e);
			throw new IllegalArgumentException();
		} catch (ComputerNotFoundException e) {
			LOGGER.warn("Delete Computer failed : the computer doesn't exist", e);
			throw new IllegalArgumentException();
		}
	}
}