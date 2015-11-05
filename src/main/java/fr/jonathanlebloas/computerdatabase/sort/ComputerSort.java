package fr.jonathanlebloas.computerdatabase.sort;

import java.util.HashMap;
import java.util.Map;

import fr.jonathanlebloas.computerdatabase.sort.Sort.Direction;

public class ComputerSort {
	public static final Map<Integer, String> MAP_COLUMN_ORDER = new HashMap<>();

	static {
		MAP_COLUMN_ORDER.put(1, "id");
		MAP_COLUMN_ORDER.put(2, "name");
		MAP_COLUMN_ORDER.put(3, "introduced");
		MAP_COLUMN_ORDER.put(4, "discontinued");
		MAP_COLUMN_ORDER.put(5, "companyName");
	}

	private ComputerSort() {
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
