package fr.jonathanlebloas.computerdatabase.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.impl.CompanyServiceImpl;
import fr.jonathanlebloas.computerdatabase.validation.Validator;

public final class ServletUtil {

	public static final String PARAM_COMPUTER_ID = "computerId";
	public static final String PARAM_COMPUTER_NAME = "computerName";
	public static final String PARAM_INTRODUCED = "introduced";
	public static final String PARAM_DISCONTINUED = "discontinued";
	public static final String PARAM_COMPANY_ID = "companyId";

	private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtil.class);

	private static final CompanyMapper COMPANY_MAPPER = CompanyMapper.INSTANCE;

	private static final CompanyService COMPANY_SERVICE = CompanyServiceImpl.INSTANCE;

	public static final String ATTR_COMPANIES = "companies";
	public static final String ATTR_ORDER_ERROR = "orderError";
	public static final String ATTR_DISCONTINUED_ERROR = "discontinuedError";
	public static final String ATTR_INTRODUCED_EMPTY_ERROR = "introducedEmptyError";
	public static final String ATTR_INTRODUCED_ERROR = "introducedError";
	public static final String ATTR_COMPUTER_NAME_ERROR = "computerNameError";

	private ServletUtil() {
	}

	public static String getStringFromRequest(HttpServletRequest request, String name) {
		String string = request.getParameter(name);
		if (string == null) {
			throw new IllegalArgumentException("The argument '" + name + "' is not provided");
		}
		LOGGER.trace("{} : {}", name, string);

		if (StringUtils.isEmpty(string)) {
			LOGGER.debug("Empty parameter : " + name);
			return null;
		}

		return string.trim();
	}

	public static ComputerDTO getComputerDto(HttpServletRequest request) {
		// The Id can be null for add
		String computerId = request.getParameter(PARAM_COMPUTER_ID);

		// Parameters mandatory
		String name = getStringFromRequest(request, PARAM_COMPUTER_NAME);
		String introduced = getStringFromRequest(request, PARAM_INTRODUCED);
		String discontinued = getStringFromRequest(request, PARAM_DISCONTINUED);
		String companyId = getStringFromRequest(request, PARAM_COMPANY_ID);

		Company company = null;
		if ("0".equals(companyId)) {
			companyId = null;
		} else {
			long idParsed = Long.parseLong(companyId);
			company = COMPANY_SERVICE.find(idParsed);
		}

		if (company == null) {
			return new ComputerDTO(computerId, name, introduced, discontinued, null, null);
		} else {
			return new ComputerDTO(computerId, name, introduced, discontinued, Long.toString(company.getId()),
					company.getName());
		}
	}

	/**
	 * Return if the company id given exist
	 *
	 * @param request
	 * @return
	 */
	public static boolean isLegalCompany(HttpServletRequest request) {
		return Validator.isPositivInteger(getStringFromRequest(request, "companyId"));
	}


	/**
	 * Return the prepared list of companies for the dropDown list
	 *
	 * @param request
	 * @return
	 */
	public static List<CompanyDTO> getCompanies() {
		List<Company> companies = COMPANY_SERVICE.listCompanies();
		companies.add(0, Company.builder().id(0).name("--").build());
		return COMPANY_MAPPER.toDTO(companies);
	}

	/**
	 * Method used in order to give back entries form of the user
	 *
	 * @param request
	 */
	public static void transferComputerParameterToAttributes(HttpServletRequest request) {
		request.setAttribute(PARAM_COMPUTER_NAME, request.getParameter(PARAM_COMPUTER_NAME));
		request.setAttribute(PARAM_INTRODUCED, request.getParameter(PARAM_INTRODUCED));
		request.setAttribute(PARAM_DISCONTINUED, request.getParameter(PARAM_DISCONTINUED));
		request.setAttribute(PARAM_COMPANY_ID, request.getParameter(PARAM_COMPANY_ID));
	}

	public static void prepareAttrs(HttpServletRequest request) {
		request.setAttribute(ATTR_COMPUTER_NAME_ERROR, false);
		request.setAttribute(ATTR_INTRODUCED_ERROR, false);
		request.setAttribute(ATTR_INTRODUCED_EMPTY_ERROR, false);
		request.setAttribute(ATTR_DISCONTINUED_ERROR, false);
		request.setAttribute(ATTR_ORDER_ERROR, false);
		request.setAttribute(ATTR_COMPANIES, getCompanies());
	}

	public static void prepareEditAttrs(HttpServletRequest request, Computer computer) {
		prepareAttrs(request);
		request.setAttribute(PARAM_COMPUTER_ID, computer.getId());
		request.setAttribute(PARAM_COMPUTER_NAME, computer.getName());
		request.setAttribute(PARAM_INTRODUCED, computer.getIntroduced());
		request.setAttribute(PARAM_DISCONTINUED, computer.getDiscontinued());

		Company company = computer.getCompany();
		request.setAttribute(PARAM_COMPANY_ID, company == null ? 0 : company.getId());
	}

	/**
	 * Return if the request parameters computerName, introduced, discontinued
	 * are acceptable
	 *
	 * @param request
	 * @return
	 */
	public static boolean isAccecptable(HttpServletRequest request) {
		boolean acceptable = true;
		// Check the introduced date
		if (StringUtils.isEmpty(getStringFromRequest(request, PARAM_COMPUTER_NAME))) {
			LOGGER.warn("Name not acceptable");
			request.setAttribute(ATTR_COMPUTER_NAME_ERROR, true);
			acceptable = false;
		}

		// Check the introduced date
		String introduced = getStringFromRequest(request, PARAM_INTRODUCED);
		String discontinued = getStringFromRequest(request, PARAM_DISCONTINUED);
		if (introduced != null) {
			if (!Validator.isValid(introduced)) {
				LOGGER.warn("Introduced not acceptable");
				request.setAttribute(ATTR_INTRODUCED_ERROR, true);
				acceptable = false;
			} else {
				// Introduced date is valid

				// Now check the discontinued date
				if (discontinued != null) {
					if (!Validator.isValid(discontinued)) {
						LOGGER.warn("Discontinued not acceptable");
						request.setAttribute(ATTR_DISCONTINUED_ERROR, true);
						acceptable = false;

					} else if (!Validator.isOrderValid(introduced, discontinued)) {
						// Discontinued date is valid

						// Check the order between dates
						LOGGER.warn("The discontinued date is greater than the introduced one : {} >= {}", introduced,
								discontinued);
						request.setAttribute(ATTR_ORDER_ERROR, true);
						acceptable = false;
					}
				}
			}
		} else if (discontinued != null) {
			// The introduced must be set if the discontinued is set
			LOGGER.warn("The discontinued date is set but not the introduced one!");
			request.setAttribute(ATTR_INTRODUCED_EMPTY_ERROR, true);
			acceptable = false;
		}
		return acceptable;
	}
}
