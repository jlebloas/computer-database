package fr.jonathanlebloas.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.mapper.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.impl.CompanyServiceImpl;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidCompanyException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidDateException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.ServletUtil;

@WebServlet("/computer/add")
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 9049278320734561410L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AddComputerServlet.class);

	private ComputerService computerService;
	private CompanyService companyService;

	public AddComputerServlet() {
		this.computerService = ComputerServiceImpl.INSTANCE;
		this.companyService = CompanyServiceImpl.INSTANCE;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Add Computer : GET");

		try {

			List<Company> companies = companyService.listCompanies();
			companies.add(0, Company.builder().id(0).name("--").build());

			List<CompanyDTO> companiesDTO = CompanyMapper.mapModelListToDTO(companies);

			request.setAttribute("companies", companiesDTO);

			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp");
			dispatch.forward(request, response);
		} catch (ServiceException e) {
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp");
			dispatch.forward(request, response);
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOGGER.info("Add Computer : POST");
		Computer computer = null;

		try {
			ComputerDTO computerDTO = ServletUtil.getComputerDto(request);
			LOGGER.info("Add Computer : dto {} ", computerDTO);
			computer = ComputerMapper.mapDTOToModel(computerDTO);
			computerService.create(computer);

			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/");
			dispatch.forward(request, response);

		} catch (ServiceException e) {
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp");
			dispatch.forward(request, response);
		} catch (InvalidCompanyException e) {
			LOGGER.warn("Add Computer : company invalid on {}", computer);
		} catch (EmptyNameException e) {
			LOGGER.warn("The name of the computer is empty");
		} catch (InvalidDateException e) {
			LOGGER.warn("The date is invalid {}", e.getMessage());
		}
	}
}