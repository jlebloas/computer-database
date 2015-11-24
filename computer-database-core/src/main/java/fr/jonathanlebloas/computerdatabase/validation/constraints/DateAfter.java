package fr.jonathanlebloas.computerdatabase.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import fr.jonathanlebloas.computerdatabase.validation.validators.DateAfterValidator;

/**
 * Validation annotation to validate that the after date is after the before
 * one. The two given date must be dates !
 *
 * Simple Example :
 *
 * @DateAfter(before = "beforeDateField", after =afterndDateField", message = "The afterDateField must be after the beforeDateField")
 *
 * Example, compare more than 1 pair of fields:
 * @FieldMatch.List({
 * 		@FieldMatch(before = "beforeDateField", after = "afterDateField", message =
 *                   "The afterDateField must be after the beforeDateField"),
 * 		@FieldMatch(before = "secondBeforeDateField", after = "secondAfterDateField", message =
 *                   "The secondBeforeDateField must be after the secondAfterDateField")
 * })
 */
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = DateAfterValidator.class)
@Documented
public @interface DateAfter {
	String message() default "The after date must be after the before one";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The before field
	 */
	String before();

	/**
	 * @return The after field
	 */
	String after();

	/**
	 * Defines several <code>@DateAfter</code> annotations on the same element
	 *
	 * @see DateAfter
	 */
	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		DateAfter[] value();
	}
}
