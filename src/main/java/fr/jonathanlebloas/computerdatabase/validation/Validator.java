package fr.jonathanlebloas.computerdatabase.validation;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidDateException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Validator
 *
 */
public final class Validator {

	private static final Logger LOGGER = LoggerFactory.getLogger(Validator.class);

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private Validator() {
	}

	/**
	 * Return if the given string is a positiv integer
	 *
	 * @param str
	 * @return
	 */
	public static boolean isPositivInteger(String str) {
		return str.matches("\\d+");
	}

	/**
	 * Return if the given string is a valid date
	 *
	 * @param str
	 * @return
	 */
	public static boolean isValid(String stringFromRequest) {
		// First check with a regex

		if (stringFromRequest == null || !stringFromRequest.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			return false;
		}

		try {
			// Second complete check on the date
			return isValid(LocalDate.parse(stringFromRequest, df));

		} catch (DateTimeParseException ex) {
			LOGGER.warn("The given date is invalid : " + stringFromRequest);
			return false;
		}
	}

	/**
	 * Return if the date is valid for the database
	 *
	 * @param date
	 * @return
	 */
	public static boolean isValid(LocalDate date) {
		return Timestamp.valueOf(date.atStartOfDay()).getTime() > 0L
				&& Timestamp.valueOf(date.atStartOfDay()).getTime() < 2147480047000L;
	}

	/**
	 * Check the order between the introduced date and the discontinued one both
	 * should be date
	 *
	 * @param introduced
	 * @param discontinued
	 * @return
	 */
	public static boolean isOrderValid(String introduced, String discontinued) {
		return LocalDate.parse(introduced, df).isBefore(LocalDate.parse(discontinued, df));
	}

	/**
	 * Return if a computer is valid
	 *
	 * @param c
	 */
	public void validateComputer(Computer c) {
		if (c == null) {
			throw new ServiceException();
		}

		if (StringUtils.isEmpty(c.getName())) {
			throw new EmptyNameException();
		}

		// Check dates
		if (c.getIntroduced() != null && isValid(c.getIntroduced())) {
			throw new InvalidDateException("The introduced date of the computer is invalid.");
		}
		if (c.getDiscontinued() != null && isValid(c.getDiscontinued())) {
			throw new InvalidDateException("The discontinued date of the computer is invalid.");
		}
		// To set discontinued introduced must be set and lower than
		// discontinued
		if (c.getDiscontinued() != null) {
			if (c.getIntroduced() == null || c.getIntroduced().compareTo(c.getDiscontinued()) >= 0) {
				throw new InvalidDateException("The computer must be discontinued after he was introduced");
			}
		}
	}
}
