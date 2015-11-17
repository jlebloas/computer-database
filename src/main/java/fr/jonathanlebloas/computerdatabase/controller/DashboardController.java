package fr.jonathanlebloas.computerdatabase.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.sort.ComputerSort;
import fr.jonathanlebloas.computerdatabase.sort.Sort.Direction;

@Controller
public class DashboardController {

	private static final String PATH_DASHBOARD_VIEW = "dashboard";

	private static final String PARAM_PAGE = "page";
	private static final String PARAM_SIZE = "size";
	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_ORDER = "order";
	private static final String PARAM_DIRECTION = "direction";

	private static final String ATTR_PAGE = "page";
	private static final String ATTR_ORDER_INDEX = "orderIndex";
	private static final String ATTR_COMPUTERS = "computers";
	private static final String ATTR_COLUMNS = "columns";

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	/**
	 * Max age of cookies in seconds
	 */
	private static final int COOKIES_MAX_AGE = 300;

	@Autowired
	private MessageSource messages;

	@Autowired
	private ComputerService computerService;

	@Autowired
	private ComputerMapper computerMapper;

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:dashboard";
	}

	/**
	 * Handler of the dashboard
	 *
	 * @param pageIndexParam
	 * @param sizeParam
	 * @param searchParam
	 * @param orderParam
	 * @param directionParam
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path = "/dashboard", method = RequestMethod.GET)
	public String dashboardHandler(@RequestParam(value = PARAM_PAGE) final Optional<String> pageIndexParam,
			@RequestParam(value = PARAM_SIZE) final Optional<String> sizeParam,
			@RequestParam(value = PARAM_SEARCH) final Optional<String> searchParam,
			@RequestParam(value = PARAM_ORDER) final Optional<String> orderParam,
			@RequestParam(value = PARAM_DIRECTION) final Optional<Direction> directionParam, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {

		final int pageIndex = getPageIndex(request, pageIndexParam);
		final int size = getSize(request, sizeParam);
		final String search = getSearch(request, searchParam);
		final int order = getOrderField(request, orderParam);
		final Direction direction = getDirection(request, directionParam);

		LOGGER.info("Dashboard : GET index={} size={} search={} order={} direction={}", pageIndex, size, search, order,
				direction);

		Page<Computer> page = new Page<>(pageIndex, size, search, ComputerSort.getSort(order, direction));
		computerService.populatePage(page);
		List<ComputerDTO> computers = computerMapper.toDTO(page.getItems());

		List<OrderColumn> columns = generateOrderColumns(page);
		model.addAttribute(ATTR_COMPUTERS, computers);
		model.addAttribute(ATTR_PAGE, page);
		model.addAttribute(ATTR_ORDER_INDEX, ComputerSort.getFieldIndex(page.getSort().getField()));
		model.addAttribute(ATTR_COLUMNS, columns);

		prepareCookies(response, pageIndex, size, search, order, direction);

		return PATH_DASHBOARD_VIEW;
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param pageIndexParam
	 * @return
	 */
	private int getPageIndex(HttpServletRequest request, Optional<String> pageIndexParam) {
		if (pageIndexParam.isPresent()) {
			int parsed = Integer.parseInt(pageIndexParam.get());
			return parsed > 0 ? parsed : 1;
		} else {
			return getIntFromCookieOrDefault(request, PARAM_PAGE, 1);
		}
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param sizeParam
	 * @returnw<
	 */
	private int getSize(HttpServletRequest request, Optional<String> sizeParam) {
		if (sizeParam.isPresent()) {
			int parsed = Integer.parseInt(sizeParam.get());
			return parsed > 0 ? parsed : 10;
		} else {
			return getIntFromCookieOrDefault(request, PARAM_SIZE, 10);
		}
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param searchParam
	 * @return
	 */
	private String getSearch(HttpServletRequest request, Optional<String> searchParam) {
		if (searchParam.isPresent()) {
			return searchParam.get();
		} else {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				Optional<Cookie> cookie = Arrays.stream(request.getCookies())
						.filter(c -> PARAM_SEARCH.equals(c.getName())).findFirst();
				if (cookie.isPresent()) {
					return cookie.get().getValue();
				}
			}
		}
		// Default
		return "";
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param orderParam
	 * @return
	 */
	private int getOrderField(HttpServletRequest request, Optional<String> orderParam) {
		if (orderParam.isPresent()) {
			int parsed = Integer.parseInt(orderParam.get());
			return parsed > 0 ? parsed : 1;
		} else {
			return getIntFromCookieOrDefault(request, PARAM_ORDER, 1);
		}
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param directionParam
	 * @return
	 */
	private static Direction getDirection(HttpServletRequest request, Optional<Direction> directionParam) {
		if (directionParam.isPresent()) {
			return directionParam.get();
		} else {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				Optional<Cookie> cookie = Arrays.stream(request.getCookies())
						.filter(c -> PARAM_DIRECTION.equals(c.getName())).findFirst();
				if (cookie.isPresent()) {
					// Don't catch IllegalParameter exception
					return Direction.valueOf(cookie.get().getValue());
				}
			}
		}
		// Default
		return Direction.ASC;
	}

	private static int getIntFromCookieOrDefault(HttpServletRequest request, String paramName, int defaultValue) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			Optional<Cookie> cookie = Arrays.stream(cookies).filter(c -> paramName.equals(c.getName())).findFirst();
			if (cookie.isPresent()) {
				// Don't catch NumberFormatException exception
				int parsed = Integer.parseInt(cookie.get().getValue());
				return parsed > 0 ? parsed : defaultValue;
			}
		}
		return defaultValue;
	}

	private static void prepareCookies(HttpServletResponse response, int pageIndex, int size, String search, int order,
			Direction direction) {
		List<Cookie> cookies = new ArrayList<>();
		cookies.add(new Cookie(PARAM_PAGE, "" + pageIndex));
		cookies.add(new Cookie(PARAM_SIZE, "" + size));
		cookies.add(new Cookie(PARAM_SEARCH, search));
		cookies.add(new Cookie(PARAM_ORDER, "" + order));
		cookies.add(new Cookie(PARAM_DIRECTION, direction.name()));

		cookies.stream().forEach(c -> {
			c.setMaxAge(COOKIES_MAX_AGE);
			response.addCookie(c);
		});
	}

	private List<OrderColumn> generateOrderColumns(Page<Computer> page) {
		Locale locale = LocaleContextHolder.getLocale();

		List<OrderColumn> columns = new ArrayList<>();
		columns.add(new OrderColumn(messages.getMessage("computer.name", null, locale), 2, generateDirection(2, page)));
		columns.add(new OrderColumn(messages.getMessage("computer.introduced", null, locale), 3, generateDirection(3, page)));
		columns.add(new OrderColumn(messages.getMessage("computer.discontinued", null, locale), 4, generateDirection(4, page)));
		columns.add(new OrderColumn(messages.getMessage("computer.company", null, locale), 5, generateDirection(5, page)));

		return columns;
	}

	/**
	 * Generate the order of the given index Column given the current page
	 *
	 * @param index
	 * @param page
	 * @return
	 */
	private static Direction generateDirection(int index, Page<Computer> page) {
		if (ComputerSort.getFieldIndex(page.getSort().getField()) == index
				&& page.getSort().getDirection() == Direction.ASC) {
			return Direction.DESC;
		} else {
			return Direction.ASC;
		}
	}

	public static final class OrderColumn {
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
