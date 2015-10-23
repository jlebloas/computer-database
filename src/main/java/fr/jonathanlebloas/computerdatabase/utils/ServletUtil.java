package fr.jonathanlebloas.computerdatabase.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;

public class ServletUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServletUtil.class);

	private static String getStringFromRequest(HttpServletRequest request, String name) {
		String string = request.getParameter(name);
		LOGGER.debug("{} : {}", name, string);

		if (StringUtils.isEmpty(string)) {
			LOGGER.warn("Can find parameter : " + name);
			return null;
		}

		return string.trim();
	}

	public static ComputerDTO getComputerDto(HttpServletRequest request) {
		String name = getStringFromRequest(request, "computerName");
		String introduced = getStringFromRequest(request, "introduced");
		String discontinued = getStringFromRequest(request, "discontinued");
		String manufacturerId = getStringFromRequest(request, "manufacturerId");
		if ("0".equals(manufacturerId)) {
			manufacturerId = null;
		}
		String manufacturerName = getStringFromRequest(request, "manufacturerName");

		return new ComputerDTO(null, name, introduced, discontinued, manufacturerId, manufacturerName);
	}
}
