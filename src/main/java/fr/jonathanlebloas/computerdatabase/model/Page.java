package fr.jonathanlebloas.computerdatabase.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Class used for pagination
 *
 * @param <T>
 */
public class Page<T> {
	public enum Direction {
		ASC, DESC
	};

	/**
	 * The maximum of pages for page menu, preferably odd
	 */
	private static final int MAX_INDEXES_MENU = 7;

	/**
	 * The index of the page. The first index is 1
	 */
	private int index;

	/**
	 * The size of page asked
	 */
	private int size;

	/**
	 * The nb total of element
	 */
	private int nbTotalElement;

	/**
	 * The number total of pages
	 */
	private int nbTotalPages;

	/**
	 * The name pattern of the element
	 */
	private String search;

	/**
	 * The field index to sort the page
	 */
	private int order;

	/**
	 * The direction of the order sort
	 */
	private Direction direction;

	/**
	 * The items of the page
	 */
	private List<T> items;

	public Page(int index, int size) {
		this(index, size, "", 1, Direction.ASC);
	}

	public Page(int index, int size, String search) {
		this(index, size, search, 1, Direction.ASC);
	}

	public Page(int index, int size, String search, int order, Direction direction) {
		super();
		this.index = index;
		this.size = size;
		this.search = search;
		this.order = order;
		this.direction = direction;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getNbTotalElement() {
		return nbTotalElement;
	}

	public void setNbTotalElement(int nbTotalElement) {
		this.nbTotalElement = nbTotalElement;
	}

	public int getNbTotalPages() {
		return nbTotalPages;
	}

	public void setNbTotalPages(int nbTotalPages) {
		this.nbTotalPages = nbTotalPages;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	/**
	 * Return a list that contains the indexes for the pages menu
	 *
	 * @return
	 */
	public List<Integer> getRange() {
		int ofset = MAX_INDEXES_MENU / 2;

		// If there is not enough pages
		if (nbTotalPages <= MAX_INDEXES_MENU) {
			return IntStream.range(1, nbTotalPages + 1).boxed().collect(Collectors.toList());
		}
		// If we're at the beginning
		if (index <= ofset) {
			return IntStream.range(1, MAX_INDEXES_MENU + 1).boxed().collect(Collectors.toList());
		}
		// If we're at the end
		if (index >= nbTotalPages - ofset) {
			return IntStream.range(nbTotalPages - MAX_INDEXES_MENU + 1, nbTotalPages + 1).boxed()
					.collect(Collectors.toList());
		} else {
			// common case
			return IntStream.range(index - ofset, index + ofset + 1).boxed().collect(Collectors.toList());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + index;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + nbTotalElement;
		result = prime * result + nbTotalPages;
		result = prime * result + order;
		result = prime * result + ((search == null) ? 0 : search.hashCode());
		result = prime * result + size;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Page<?> other = (Page<?>) obj;
		if (direction != other.direction) {
			return false;
		}
		if (index != other.index) {
			return false;
		}
		if (items == null) {
			if (other.items != null) {
				return false;
			}
		} else if (!items.equals(other.items)) {
			return false;
		}
		if (nbTotalElement != other.nbTotalElement) {
			return false;
		}
		if (nbTotalPages != other.nbTotalPages) {
			return false;
		}
		if (order != other.order) {
			return false;
		}
		if (search == null) {
			if (other.search != null) {
				return false;
			}
		} else if (!search.equals(other.search)) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Page [index=" + index + ", size=" + size + ", nbTotalElement=" + nbTotalElement + ", nbTotalPages="
				+ nbTotalPages + ", search=" + search + ", order=" + order + ", direction=" + direction + ", items="
				+ (items == null ? "null" : "size=" + items.size()) + "]";
	}
}
