package fr.jonathanlebloas.computerdatabase.utils;

import java.sql.Timestamp;
import java.time.LocalDate;

public class DateUtils {

	/**
	 * Return if the date is valid for the database
	 *
	 * @param date
	 * @return
	 */
	public static boolean isValid(LocalDate date) {
		return Timestamp.valueOf(date.atStartOfDay()).getTime() <= 0L
				|| Timestamp.valueOf(date.atStartOfDay()).getTime() >= 2147480047000L;
	}
}
