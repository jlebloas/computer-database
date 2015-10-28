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
	 * The items of the page
	 */
	private List<T> items;

	public Page(int index, int size) {
		super();
		this.index = index;
		this.size = size;
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
		result = prime * result + index;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + nbTotalElement;
		result = prime * result + nbTotalPages;
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
		if (size != other.size) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Page [index=" + index + ", size=" + size + ", nbTotalElement=" + nbTotalElement + ", nbTotalPages="
				+ nbTotalPages + ", items=" + (items == null ? "null" : "size=" + items.size()) + "]";
	}
}
