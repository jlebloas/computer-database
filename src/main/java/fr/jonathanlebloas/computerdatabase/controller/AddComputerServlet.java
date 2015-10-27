package fr.jonathanlebloas.computerdatabase.controller;

import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.ATTR_COMPANIES;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.getCompanies;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.getComputerDto;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.isAccecptable;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.isLegalCompany;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.prepareAttrs;
import static fr.jonathanlebloas.computerdatabase.utils.ServletUtil.transferComputerParameterToAttributes;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

@WebServlet("/computer/add")
public class AddComputerServlet extends HttpServlet {

	private static final long serialVersionUID = 9049278320734561410L;

	private static final String PATH_ILLEGAL_VIEW = "/WEB-INF/views/403.jsp";
	private static final String PATH_ADD_VIEW = "/WEB-INF/views/addComputer.jsp";


	private static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

	private static final ComputerMapper COMPUTER_MAPPER = ComputerMapper.INSTANCE;

	private static final ComputerService COMPUTER_SERVICE = ComputerServiceImpl.INSTANCE;

	public AddComputerServlet() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Add Computer : GET");

		prepareAttrs(request);

		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(PATH_ADD_VIEW);
		dispatch.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Add Computer : POST");

		// Return 403 if the companyId is not valid
		if (!isLegalCompany(request)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			LOGGER.warn("Illegal company given");

			getServletContext().getRequestDispatcher(PATH_ILLEGAL_VIEW).forward(request, response);
			return;
		}

		// Redirect if error
		if (!isAccecptable(request)) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

			request.setAttribute(ATTR_COMPANIES, getCompanies());
			transferComputerParameterToAttributes(request);

			LOGGER.warn("Not acceptable computer given");

			getServletContext().getRequestDispatcher(PATH_ADD_VIEW).forward(request, response);
			return;
		}

		// We can now create the computer
		ComputerDTO computerDTO = getComputerDto(request);

		LOGGER.info("Add Computer : dto {} ", computerDTO);
		Computer computer = COMPUTER_MAPPER.fromDTO(computerDTO);
		COMPUTER_SERVICE.create(computer);

		getServletContext().getRequestDispatcher("/").forward(request, response);
	}

}