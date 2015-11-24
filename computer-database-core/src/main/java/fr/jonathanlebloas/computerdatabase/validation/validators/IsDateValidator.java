package fr.jonathanlebloas.computerdatabase.validation.validators;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.utils.StringUtils;
import fr.jonathanlebloas.computerdatabase.validation.constraints.IsDate;

public class IsDateValidator implements ConstraintValidator<IsDate, String> {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsDateValidator.class);

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	public void initialize(IsDate constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String value, final ConstraintValidatorContext context) {
		// Empty
		if (StringUtils.isEmpty(value)) {
			return true;
		}

		// Check null and first date check with with a regex
		if (value == null || !value.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			return false;
		}

		try {
			// Second complete check on the date
			return isValid(LocalDate.parse(value, df));

		} catch (DateTimeParseException ex) {
			LOGGER.warn("The given date is invalid : " + value);
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
}
