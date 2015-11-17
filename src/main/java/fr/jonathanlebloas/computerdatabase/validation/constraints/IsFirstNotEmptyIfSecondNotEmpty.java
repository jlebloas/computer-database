package fr.jonathanlebloas.computerdatabase.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import fr.jonathanlebloas.computerdatabase.validation.validators.IsFirstNotEmptyIfSecondNotEmptyValidator;

/**
 * Validation annotation to check if first field is set when the second is set
 *
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = IsFirstNotEmptyIfSecondNotEmptyValidator.class)
@Documented
public @interface IsFirstNotEmptyIfSecondNotEmpty {
	String message() default "The after date must be after the before one";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The first field
	 */
	String first();

	/**
	 * @return The second field
	 */
	String second();

	/**
	 * Defines several <code>@IsFirstNotEmptyIfSecondNotEmpty</code> annotations on the same element
	 *
	 * @see IsFirstNotEmptyIfSecondNotEmpty
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		IsFirstNotEmptyIfSecondNotEmpty[] value();
	}
}
