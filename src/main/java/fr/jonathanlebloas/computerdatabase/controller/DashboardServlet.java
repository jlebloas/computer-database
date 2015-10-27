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

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 959108770761115729L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

	private static final ComputerMapper COMPUTERMAPPER = ComputerMapper.INSTANCE;

	private static final ComputerService COMPUTERSERVICE = ComputerServiceImpl.INSTANCE;


	public DashboardServlet() {
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int pageIndex = getPage(request);
		int size = getSize(request);

		LOGGER.info("Dashboard : GET index={} size={}", pageIndex, size);

		try {
			Page<Computer> page = COMPUTERSERVICE.getPage(pageIndex, size);
			List<ComputerDTO> computers = COMPUTERMAPPER.toDTO(page.getItems());

			request.setAttribute("computers", computers);

			int pageCount = COMPUTERSERVICE.getNbPages(size);
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("page", pageIndex);

			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
			dispatch.forward(request, response);
		} catch (ServiceException e) {
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/WEB-INF/views/500.jsp");
			dispatch.forward(request, response);
		}
	}

	private int getPage(HttpServletRequest request) {
		try {
			int index = Integer.valueOf(request.getParameter("page"));
			if (index > 0) {
				return index;
			}
		} catch (Exception e) {
			LOGGER.info("Wrong format for parameter page, given : {}", request.getParameter("page"));
		}
		return 1;
	}

	private int getSize(HttpServletRequest request) {
		try {
			int size = Integer.valueOf(request.getParameter("size"));
			if (size >= 10) {
				return Integer.valueOf(request.getParameter("size"));
			}
		} catch (Exception e) {
			LOGGER.info("Wrong format for parameter size, given : {}", request.getParameter("size"));
		}
		return 10;
	}
}
