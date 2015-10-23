package fr.jonathanlebloas.computerdatabase.model;

import java.time.LocalDate;

import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * A computer has a name, the date when it was introduced, eventually the date
 * when it was discontinued, and the company. The name is mandatory
 */
public class Computer {

	private long id;

	private String name;

	private LocalDate introduced;

	private LocalDate discontinued;

	private Company company;

	private Computer() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getIntroduced() {
		return introduced;
	}

	public void setIntroduced(LocalDate introduced) {
		this.introduced = introduced;
	}

	public LocalDate getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(LocalDate discontinued) {
		this.discontinued = discontinued;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Computer other = (Computer) obj;
		if (discontinued == null) {
			if (other.discontinued != null) {
				return false;
			}
		} else if (!discontinued.equals(other.discontinued)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (introduced == null) {
			if (other.introduced != null) {
				return false;
			}
		} else if (!introduced.equals(other.introduced)) {
			return false;
		}
		if (company == null) {
			if (other.company != null) {
				return false;
			}
		} else if (!company.equals(other.company)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
				+ ", company=" + company + "]";
	}

	/**
	 * @return the builder of the class
	 */
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Computer computer;

		private Builder() {
			computer = new Computer();
		}

		public Builder id(long id) {
			computer.id = id;
			return this;
		}

		public Builder name(String name) {
			computer.name = name;
			return this;
		}

		public Builder introduced(LocalDate introduced) {
			computer.introduced = introduced;
			return this;
		}

		public Builder discontinued(LocalDate discontinued) {
			computer.discontinued = discontinued;
			return this;
		}

		public Builder company(Company company) {
			computer.company = company;
			return this;
		}

		public Computer build() {
			// Validation
			if (computer.id < 0) {
				throw new IllegalArgumentException("id is invalid : " + computer.id);
			}
			if (StringUtils.isEmpty(computer.name)) {
				throw new IllegalArgumentException("name is invalid : " + computer.name);
			}
			return computer;
		}
	}
}
