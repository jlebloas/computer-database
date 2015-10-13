package fr.jonathanlebloas.computerdatabase.utils;

import java.util.Date;

public class DateUtils {

	/**
	 * Return if the date is valid for the database
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isValid(Date date) {
		return date.getTime() <= 0L || date.getTime() >= 2147480047000L;
	}
}
