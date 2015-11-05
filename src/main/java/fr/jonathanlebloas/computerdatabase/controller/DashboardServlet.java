package fr.jonathanlebloas.computerdatabase.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import fr.jonathanlebloas.computerdatabase.sort.ComputerSort;
import fr.jonathanlebloas.computerdatabase.sort.Sort.Direction;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {


	private static final String PATH_DASHBOARD_VIEW = "/WEB-INF/views/dashboard.jsp";

	private static final String PARAM_SIZE = "size";
	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_ORDER = "order";
	private static final String PARAM_DIRECTION = "direction";

	private static final String ATTR_PAGE = "page";
	private static final String ATTR_ORDER_INDEX = "orderIndex";
	private static final String ATTR_COMPUTERS = "computers";
	private static final String ATTR_COLUMNS = "columns";

	private static final long serialVersionUID = 959108770761115729L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardServlet.class);

	private static final ComputerMapper COMPUTERMAPPER = ComputerMapper.INSTANCE;

	private static final ComputerService COMPUTERSERVICE = ComputerServiceImpl.INSTANCE;


	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int pageIndex = getPage(request);
		int size = getSize(request);
		String search = getSearch(request);
		int order = getOrderField(request);
		Direction direction = getDirection(request);

		LOGGER.info("Dashboard : GET index={} size={} search={}", pageIndex, size, search);

		Page<Computer> page = new Page<>(pageIndex, size, search, ComputerSort.getSort(order, direction));
		COMPUTERSERVICE.populatePage(page);
		List<ComputerDTO> computers = COMPUTERMAPPER.toDTO(page.getItems());

		List<OrderColumn> columns = generateOrderColumns(page);
		request.setAttribute(ATTR_COMPUTERS, computers);
		request.setAttribute(ATTR_PAGE, page);
		request.setAttribute(ATTR_ORDER_INDEX, ComputerSort.getFieldIndex(page.getSort().getField()));
		request.setAttribute(ATTR_COLUMNS, columns);

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

	private int getOrderField(HttpServletRequest request) {
		String param = request.getParameter(PARAM_ORDER);

		if (param == null) {
			return 1;
		}
		if (!param.matches("^\\d*$")) {
			throw new IllegalArgumentException("The order asked is not an number");
		}

		int order = Integer.valueOf(param);
		if (order > 0) {
			return order;
		}

		return order;
	}

	private Direction getDirection(HttpServletRequest request) {
		String param = request.getParameter(PARAM_DIRECTION);

		if (param == null) {
			return Direction.ASC;
		}

		// Don't catch IllegalParameter exception
		return Direction.valueOf(param);
	}

	private List<OrderColumn> generateOrderColumns(Page<Computer> page) {
		List<OrderColumn> columns = new ArrayList<>();
		columns.add(new OrderColumn("Computer name", 2, generateDirection(2, page)));
		columns.add(new OrderColumn("Introduced date", 3, generateDirection(3, page)));
		columns.add(new OrderColumn("Discontinued date", 4, generateDirection(4, page)));
		columns.add(new OrderColumn("Company", 5, generateDirection(5, page)));

		return columns;
	}

	/**
	 * Generate the order of the given index Column given the current page
	 *
	 * @param index
	 * @param page
	 * @return
	 */
	private Direction generateDirection(int index, Page<Computer> page) {
		if (ComputerSort.getFieldIndex(page.getSort().getField()) == index
				&& page.getSort().getDirection() == Direction.ASC) {
			return Direction.DESC;
		} else {
			return Direction.ASC;
		}
	}

	public class OrderColumn {
		private String name;
		private int index;
		private Direction direction;

		public OrderColumn(String name, int index, Direction direction) {
			super();
			this.name = name;
			this.index = index;
			this.direction = direction;
		}

		public String getName() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public Direction getDirection() {
			return direction;
		}
	}
}
