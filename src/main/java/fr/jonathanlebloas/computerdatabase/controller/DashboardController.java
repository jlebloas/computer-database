package fr.jonathanlebloas.computerdatabase.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.NullHandling;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@Controller
public class DashboardController {

	private static final String PATH_DASHBOARD_VIEW = "dashboard";

	private static final String PARAM_PAGE = "page";
	private static final String PARAM_SIZE = "size";
	private static final String PARAM_SEARCH = "search";
	private static final String PARAM_ORDER = "order";
	private static final String PARAM_DIRECTION = "direction";

	private static final String ATTR_PAGE = "page";
	private static final String ATTR_PAGE_RANGE = "pageRange";
	private static final String ATTR_SEARCH = "search";
	private static final String ATTR_ORDER = "order";
	private static final String ATTR_COMPUTERS = "computers";
	private static final String ATTR_COLUMNS = "columns";

	private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

	/**
	 * Max age of cookies in seconds
	 */
	private static final int COOKIES_MAX_AGE = 300;

	/**
	 * The maximum of pages for page menu, preferably odd
	 */
	private static final int MAX_INDEXES_MENU = 7;

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
		final String orderField = getOrderField(request, orderParam);
		final Direction direction = getDirection(request, directionParam);

		LOGGER.info("Dashboard : GET index={} size={} search={} order={} direction={}", pageIndex, size, search, orderField,
				direction);

		Order order = new Order(direction, orderField, NullHandling.NULLS_LAST);
		PageRequest pageRequest = new PageRequest(pageIndex, size, new Sort(order));
		Page<Computer> page = computerService.getPage(pageRequest, search);
		List<ComputerDTO> computers = computerMapper.toDTO(page.getContent());

		model.addAttribute(ATTR_COMPUTERS, computers);
		model.addAttribute(ATTR_PAGE, page);
		model.addAttribute(ATTR_PAGE_RANGE, getRange(page));
		model.addAttribute(ATTR_SEARCH, search);
		model.addAttribute(ATTR_ORDER, order);
		model.addAttribute(ATTR_COLUMNS, generateOrderColumns(page));

		prepareCookies(response, pageIndex, size, search, orderField, direction);

		return PATH_DASHBOARD_VIEW;
	}

	/**
	 * Return a list that contains the indexes for the pages menu
	 *
	 * @return
	 */
	public List<Integer> getRange(Page<?> page) {
		int ofset = MAX_INDEXES_MENU / 2;

		// If there is not enough pages
		if (page.getTotalPages() <= MAX_INDEXES_MENU) {
			return IntStream.range(0, page.getTotalPages()).boxed().collect(Collectors.toList());
		}
		// If we're at the beginning
		if (page.getNumber() <= ofset - 1) {
			return IntStream.range(0, MAX_INDEXES_MENU).boxed().collect(Collectors.toList());
		}
		// If we're at the end
		if (page.getNumber() >= page.getTotalPages() - ofset) {
			return IntStream.range(page.getTotalPages() - MAX_INDEXES_MENU, page.getTotalPages()).boxed()
					.collect(Collectors.toList());
		} else {
			// common case
			return IntStream.range(page.getNumber() - ofset, page.getNumber() + ofset + 1).boxed()
					.collect(Collectors.toList());
		}
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
			return parsed >= 0 ? parsed : 0;
		} else {
			return getIntFromCookieOrDefault(request, PARAM_PAGE, 0);
		}
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param sizeParam
	 * @return
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
			return getFromCookieOrDefault(request, PARAM_SEARCH, "");
		}
	}

	/**
	 * If there is not a given value in parameter try to get one from cookie or
	 * return default value
	 *
	 * @param request
	 * @param orderParam
	 * @return
	 */
	private String getOrderField(HttpServletRequest request, Optional<String> orderParam) {
		if (orderParam.isPresent()) {
			return orderParam.get();
		} else {
			return getFromCookieOrDefault(request, PARAM_ORDER, "id");
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

	private static String getFromCookieOrDefault(HttpServletRequest request, String paramName, String defaultValue) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> paramName.equals(c.getName()))
					.findFirst();
			if (cookie.isPresent()) {
				return cookie.get().getValue();
			}
		}
		return defaultValue;
	}

	private static void prepareCookies(HttpServletResponse response, int pageIndex, int size, String search,
			String order, Direction direction) {
		List<Cookie> cookies = new ArrayList<>();
		cookies.add(new Cookie(PARAM_PAGE, "" + pageIndex));
		cookies.add(new Cookie(PARAM_SIZE, "" + size));
		cookies.add(new Cookie(PARAM_SEARCH, search));
		cookies.add(new Cookie(PARAM_ORDER, order));
		cookies.add(new Cookie(PARAM_DIRECTION, direction.name()));

		cookies.stream().forEach(c -> {
			c.setMaxAge(COOKIES_MAX_AGE);
			response.addCookie(c);
		});
	}

	private static List<Order> generateOrderColumns(Page<Computer> page) {
		List<Order> columns = new ArrayList<>();
		columns.add(new Order(generateDirection("name", page), "name"));
		columns.add(new Order(generateDirection("introduced", page), "introduced"));
		columns.add(new Order(generateDirection("discontinued", page), "discontinued"));
		columns.add(new Order(generateDirection("company.name", page), "company.name"));

		return columns;
	}

	/**
	 * Generate the opposite direction of the given field or default ASC
	 *
	 * @param index
	 * @param page
	 * @return
	 */
	private static Direction generateDirection(String field, Page<Computer> page) {
		Order order = page.getSort().getOrderFor(field);
		if (order != null && order.getDirection() == Direction.ASC) {
			return Direction.DESC;
		} else {
			return Direction.ASC;
		}
	}
}
