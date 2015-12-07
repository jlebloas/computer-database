package fr.jonathanlebloas.computerdatabase.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDTO {

	@JsonProperty("page")
	private int index = 0;
	private int size = 10;
	private String direction = "ASC";
	private String field = "id";
	private String search = "";

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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + index;
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
		PageDTO other = (PageDTO) obj;
		if (direction == null) {
			if (other.direction != null) {
				return false;
			}
		} else if (!direction.equals(other.direction)) {
			return false;
		}
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		if (index != other.index) {
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
		return "PageDTO [index=" + index + ", size=" + size + ", direction=" + direction + ", field=" + field
				+ ", search=" + search + "]";
	}
}
