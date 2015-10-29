package fr.jonathanlebloas.computerdatabase.controller;

import java.io.IOException;
import java.util.List;

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
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

	private static final String PATH_DASHBOARD_VIEW = "/WEB-INF/views/dashboard.jsp";

	private static final String PARAM_SIZE = "size";
	private static final String PARAM_SEARCH = "search";

	private static final String ATTR_PAGE = "page";

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
		String search = getSearch(request);

		LOGGER.info("Dashboard : GET index={} size={} search={}", pageIndex, size, search);

		Page<Computer> page = new Page<>(pageIndex, size, search);
		COMPUTERSERVICE.populatePage(page);
		List<ComputerDTO> computers = COMPUTERMAPPER.toDTO(page.getItems());

		request.setAttribute("computers", computers);
		request.setAttribute(ATTR_PAGE, page);

		getServletContext().getRequestDispatcher(PATH_DASHBOARD_VIEW).forward(request, response);
	}

	private int getPage(HttpServletRequest request) {
		String param = request.getParameter(ATTR_PAGE);

		if (param == null) {
			return 1;
		}

		if (!param.matches("^\\d*$")) {
			throw new IllegalArgumentException("The page asked is not an number");
		}

		int page = Integer.valueOf(param);
		if (page > 0) {
			return page;
		}

		return 1;
	}

	private int getSize(HttpServletRequest request) {
		String param = request.getParameter(PARAM_SIZE);

		if (param == null) {
			return 10;
		}

		if (!param.matches("^\\d*$")) {
			throw new IllegalArgumentException("The size asked is not an number");
		}

		int size = Integer.valueOf(param);
		if (size > 0) {
			return size;
		}

		return 10;
	}

	private String getSearch(HttpServletRequest request) {
		String param = request.getParameter(PARAM_SEARCH);

		if (param == null) {
			return "";
		}

		return param;
	}
}
