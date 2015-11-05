package fr.jonathanlebloas.computerdatabase.sort;

public class Sort {
	public static enum Direction {
		ASC, DESC
	}

	public final Direction direction;
	public final String field;

	protected Sort(Direction d, String s) {
		this.direction = d;
		this.field = s;
	}

	public Direction getDirection() {
		return direction;
	}

	public String getField() {
		return field;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((field == null) ? 0 : field.hashCode());
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
		Sort other = (Sort) obj;
		if (direction != other.direction) {
			return false;
		}
		if (field == null) {
			if (other.field != null) {
				return false;
			}
		} else if (!field.equals(other.field)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Sort [direction=" + direction + ", field=" + field + "]";
	}
}
