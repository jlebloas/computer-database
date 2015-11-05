package fr.jonathanlebloas.computerdatabase.controller;

import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.ATTR_COMPANIES;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.PARAM_COMPUTER_ID;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.utils.ServletUtil;
import fr.jonathanlebloas.computerdatabase.validation.Validator;

@WebServlet("/computer/edit")
public class EditComputerServlet extends HttpServlet {

	private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerServlet.class);

	private static final long serialVersionUID = -7358727501089563696L;

	private static final String PATH_ILLEGAL_VIEW = "/WEB-INF/views/403.jsp";
	private static final String PATH_NOT_FOUND_VIEW = "/WEB-INF/views/404.jsp";
	private static final String PATH_UPDATE_VIEW = "/WEB-INF/views/editComputer.jsp";

	@Autowired
	private ComputerMapper computerMapper;

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Edit Computer : GET");

		// Check validity id
		String raw_id = servletUtil.getStringFromRequest(request, "id");

		if (!Validator.isPositivInteger(raw_id)) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);

			LOGGER.warn("Invalid computer id asked");
			getServletContext().getRequestDispatcher(PATH_NOT_FOUND_VIEW).forward(request, response);
			return;
		}
		long computerId = Long.parseLong(raw_id);

		try {
			Computer computer = computerService.find(computerId);

			servletUtil.prepareEditAttrs(request, computer);

			RequestDispatcher dispatch = getServletContext().getRequestDispatcher(PATH_UPDATE_VIEW);
			dispatch.forward(request, response);

		} catch (ComputerNotFoundException e) {
			LOGGER.warn("Invalid computer id asked", e);
			getServletContext().getRequestDispatcher(PATH_ILLEGAL_VIEW).forward(request, response);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Edit Computer : POST");
		String companyId = servletUtil.getStringFromRequest(request, ServletUtil.PARAM_COMPANY_ID);

		// Check validity id
		String raw_id = servletUtil.getStringFromRequest(request, PARAM_COMPUTER_ID);
		if (!Validator.isPositivInteger(raw_id)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			LOGGER.warn("Invalid computer id asked");
			getServletContext().getRequestDispatcher(PATH_ILLEGAL_VIEW).forward(request, response);
			return;
		}
		long computerId = Long.parseLong(raw_id);

		// Return 403 if the companyId is not valid
		if (!Validator.isPositivInteger(companyId)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);

			LOGGER.warn("Illegal company given");

			getServletContext().getRequestDispatcher(PATH_ILLEGAL_VIEW).forward(request, response);
			return;
		}

		// Check the different fields and set errors param, so if it's not
		// acceptable redirect on the edit page with given inputs and errors
		// messages
		if (!servletUtil.isAccecptable(request)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

			request.setAttribute(ATTR_COMPANIES, servletUtil.getCompanies());
			request.setAttribute(PARAM_COMPUTER_ID, computerId);
			servletUtil.transferComputerParameterToAttributes(request);

			LOGGER.warn("Not acceptable computer given");

			getServletContext().getRequestDispatcher(PATH_UPDATE_VIEW).forward(request, response);
			return;
		}

		// We can now update the computer
		ComputerDTO computerDTO = servletUtil.getComputerDto(request);

		LOGGER.info("Edit Computer : dto {} ", computerDTO);
		Computer computer = computerMapper.fromDTO(computerDTO);
		computerService.update(computer);

		getServletContext().getRequestDispatcher("/").forward(request, response);
	}

}