package fr.jonathanlebloas.computerdatabase.validation.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import fr.jonathanlebloas.computerdatabase.utils.StringUtils;
import fr.jonathanlebloas.computerdatabase.validation.constraints.IsFirstNotEmptyIfSecondNotEmpty;

public class IsFirstNotEmptyIfSecondNotEmptyValidator
		implements ConstraintValidator<IsFirstNotEmptyIfSecondNotEmpty, Object> {

	private String firstFieldName;
	private String secondFieldName;

	@Override
	public void initialize(IsFirstNotEmptyIfSecondNotEmpty constraintAnnotation) {
		firstFieldName = constraintAnnotation.first();
		secondFieldName = constraintAnnotation.second();
	}

	@Override
	public boolean isValid(final Object value, final ConstraintValidatorContext context) {
		String firstString;
		String secondString;
		try {
			firstString = BeanUtils.getProperty(value, firstFieldName);
		} catch (final Exception e) {
			throw new RuntimeException("Bad usage of the annotation. Can't acces field: " + firstFieldName);
		}
		try {
			secondString = BeanUtils.getProperty(value, secondFieldName);
		} catch (final Exception e) {
			throw new RuntimeException("Bad usage of the annotation.  Can't acces field: " + secondFieldName);
		}

		if (!StringUtils.isEmpty(secondString) && StringUtils.isEmpty(firstString)) {
			return false;
		}

		return true;
	}
}
