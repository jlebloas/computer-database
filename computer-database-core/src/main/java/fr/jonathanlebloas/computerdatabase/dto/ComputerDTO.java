package fr.jonathanlebloas.computerdatabase.dto;

import javax.validation.GroupSequence;

import org.hibernate.validator.constraints.Length;

import fr.jonathanlebloas.computerdatabase.validation.constraints.DateAfter;
import fr.jonathanlebloas.computerdatabase.validation.constraints.IsDate;
import fr.jonathanlebloas.computerdatabase.validation.constraints.IsFirstNotEmptyIfSecondNotEmpty;
import fr.jonathanlebloas.computerdatabase.validation.groups.OrderCheck;

// Define a group GroupSequence in order to do the validation in a custom order.
// The globals check are done first not empty name, valid dates and introduced set if discontinued too.. then check if discontinued is set the introduced must be too and finally check the date order
@GroupSequence({ ComputerDTO.class, OrderCheck.class })
@IsFirstNotEmptyIfSecondNotEmpty(first = "introduced", second = "discontinued", message = "The introduced date must be set if the discontinued is set")
@DateAfter(before = "introduced", after = "discontinued", message = "The discontinued must be after the introduced", groups = OrderCheck.class)
public class ComputerDTO {

	private String id;

	@Length(min = 1, max = 255)
	private String name;

	@IsDate
	private String introduced;
	@IsDate
	private String discontinued;

	private String companyId;
	private String companyName;

	public ComputerDTO() {
	}

	public ComputerDTO(String id, String name, String introduced, String discontinued, String companyId,
			String companyName) {
		super();
		this.id = id;
		this.name = name;
		this.introduced = introduced;
		this.discontinued = discontinued;
		this.companyId = companyId;
		this.companyName = companyName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discontinued == null) ? 0 : discontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((introduced == null) ? 0 : introduced.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
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
		ComputerDTO other = (ComputerDTO) obj;
		if (discontinued == null) {
			if (other.discontinued != null) {
				return false;
			}
		} else if (!discontinued.equals(other.discontinued)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (introduced == null) {
			if (other.introduced != null) {
				return false;
			}
		} else if (!introduced.equals(other.introduced)) {
			return false;
		}
		if (companyId == null) {
			if (other.companyId != null) {
				return false;
			}
		} else if (!companyId.equals(other.companyId)) {
			return false;
		}
		if (companyName == null) {
			if (other.companyName != null) {
				return false;
			}
		} else if (!companyName.equals(other.companyName)) {
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
		return "ComputerDTO [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", companyId=" + companyId + ", companyName=" + companyName + "]";
	}
}
