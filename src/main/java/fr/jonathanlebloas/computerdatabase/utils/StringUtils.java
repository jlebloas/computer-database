package fr.jonathanlebloas.computerdatabase.utils;

public final class StringUtils {

	private StringUtils() {

	}

	/**
	 * Return true if a string is empty or null
	 *
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.trim().isEmpty();
	}
}
