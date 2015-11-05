package fr.jonathanlebloas.computerdatabase.controller;

import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.ATTR_COMPANIES;

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
import fr.jonathanlebloas.computerdatabase.utils.ServletUtil;
import fr.jonathanlebloas.computerdatabase.validation.Validator;

@WebServlet("/computer/add")
public class AddComputerServlet extends HttpServlet {

	private static final long serialVersionUID = 9049278320734561410L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

	private static final String PATH_ILLEGAL_VIEW = "/WEB-INF/views/403.jsp";
	private static final String PATH_ADD_VIEW = "/WEB-INF/views/addComputer.jsp";

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
		LOGGER.info("Add Computer : GET");

		servletUtil.prepareAttrs(request);

		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(PATH_ADD_VIEW);
		dispatch.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Add Computer : POST");
		String companyId = servletUtil.getStringFromRequest(request, "companyId");

		// Return 403 if the companyId is not valid
		if (!Validator.isPositivInteger(companyId)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			LOGGER.warn("Illegal company given");

			getServletContext().getRequestDispatcher(PATH_ILLEGAL_VIEW).forward(request, response);
			return;
		}

		// Check the different fields and set errors param, so if it's not
		// acceptable redirect on the add page with given inputs and errors
		// messages
		if (!servletUtil.isAccecptable(request)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

			request.setAttribute(ATTR_COMPANIES, servletUtil.getCompanies());
			servletUtil.transferComputerParameterToAttributes(request);

			LOGGER.warn("Not acceptable computer given");

			getServletContext().getRequestDispatcher(PATH_ADD_VIEW).forward(request, response);
			return;
		}

		// We can now create the computer
		ComputerDTO computerDTO = servletUtil.getComputerDto(request);

		LOGGER.info("Add Computer : dto {} ", computerDTO);
		Computer computer = computerMapper.fromDTO(computerDTO);
		computerService.create(computer);

		getServletContext().getRequestDispatcher("/").forward(request, response);
	}

}
