package fr.jonathanlebloas.computerdatabase.utils;

public class StringUtils {

	/**
	 * Return true if a string is empty or null
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		if (s == null || "".equals(s.trim())) {
			return true;
		} else {
			return false;
		}
	}
}
