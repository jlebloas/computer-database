package fr.jonathanlebloas.computerdatabase.sort;

import java.util.HashMap;
import java.util.Map;

import fr.jonathanlebloas.computerdatabase.sort.Sort.Direction;

public class CompanySort {

	private static final Map<Integer, String> MAP_COLUMN_ORDER = new HashMap<>();

	static {
		MAP_COLUMN_ORDER.put(1, "id");
		MAP_COLUMN_ORDER.put(2, "name");
	}

	private CompanySort() {
	}

	/**
	 * Return the sort given the index of field given and the direction
	 *
	 * @param n
	 * @param direction
	 * @return
	 */
	public static Sort getSort(int n, Direction direction) {
		if (MAP_COLUMN_ORDER.containsKey(n)) {
			return new Sort(direction, MAP_COLUMN_ORDER.get(n));
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Return the index of the given field or throw IllegalArgumentException
	 * otherwise
	 *
	 * @param field
	 * @return
	 */
	public static int getFieldIndex(String field) {
		for (Integer key : MAP_COLUMN_ORDER.keySet()) {
			if (MAP_COLUMN_ORDER.get(key).equals(field)) {
				return key;
			}
		}
		throw new IllegalArgumentException();
	}
}