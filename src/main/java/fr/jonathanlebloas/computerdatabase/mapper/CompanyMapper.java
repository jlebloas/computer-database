package fr.jonathanlebloas.computerdatabase.mapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.model.Company;

public enum CompanyMapper {
	INSTANCE;

	private CompanyMapper() {
	}

	public static CompanyDTO mapModelToDTO(Company company) {
		CompanyDTO companyDto = new CompanyDTO();

		companyDto.setId("" + company.getId());

		companyDto.setName(company.getName());

		return companyDto;
	}

	public static List<CompanyDTO> mapModelListToDTO(List<Company> list) {
		List<CompanyDTO> temp = new ArrayList<>();
		for (Iterator<Company> iterator = list.iterator(); iterator.hasNext();) {
			Company company = iterator.next();

			temp.add(mapModelToDTO(company));
		}
		return temp;
	}

	public static Company mapDTOToModel(CompanyDTO dto) {
		Company company = new Company();

		// TODO HandleParsing on date and long
		company.setName(dto.getName());
		company.setId(Long.parseLong(dto.getId()));

		return company;
	}

	public static List<Company> mapDTOToModel(List<CompanyDTO> DTOList) {

		List<Company> temp = new ArrayList<>();
		for (Iterator<CompanyDTO> iterator = DTOList.iterator(); iterator.hasNext();) {
			CompanyDTO dto = iterator.next();

			temp.add(mapDTOToModel(dto));
		}
		return temp;
	}

}
