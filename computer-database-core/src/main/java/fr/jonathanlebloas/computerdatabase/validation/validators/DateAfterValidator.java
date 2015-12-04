package fr.jonathanlebloas.computerdatabase.validation.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.utils.StringUtils;
import fr.jonathanlebloas.computerdatabase.validation.constraints.DateAfter;

public class DateAfterValidator implements ConstraintValidator<DateAfter, Object> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateAfterValidator.class);

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private String beforeFieldName;
	private String afterFieldName;

	@Override
	public void initialize(DateAfter constraintAnnotation) {
		beforeFieldName = constraintAnnotation.before();
		afterFieldName = constraintAnnotation.after();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		String firstObj;
		String secondObj;
		try {
			firstObj = BeanUtils.getProperty(value, beforeFieldName);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Bad usage of the annotation. Can't acces field: " + beforeFieldName);
		}
		try {
			secondObj = BeanUtils.getProperty(value, afterFieldName);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Bad usage of the annotation. Can't acces field: " + afterFieldName);
		}

		// The check is only valid if the two given date are not empty
		if (StringUtils.isEmpty(firstObj) || StringUtils.isEmpty(secondObj)) {
			return true;
		}

		final LocalDate firstDate = LocalDate.parse(firstObj, df);
		final LocalDate secondDate = LocalDate.parse(secondObj, df);

		if (firstDate != null && secondDate != null && !firstDate.isBefore(secondDate)) {
			LOGGER.warn("The order given dates are invalid : " + value);
			return false;
		}
		return true;
	}
}
