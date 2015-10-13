package fr.jonathanlebloas.computerdatabase.model;

import java.util.List;

/**
 * Class used for pagination that contains a number of item from an index
 * 
 * @param <T>
 */
public class Page<T> {

	private int beginIndex;

	private int nb;

	private List<T> items;

	public Page(int beginIndex, int nb, List<T> items) {
		super();
		this.beginIndex = beginIndex;
		this.nb = nb;
		this.items = items;
	}

	public Page(int beginIndex, int nb) {
		super();
		this.beginIndex = beginIndex;
		this.nb = nb;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(int beginIndex) {
		this.beginIndex = beginIndex;
	}

	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

}
