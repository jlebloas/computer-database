package fr.jonathanlebloas.computerdatabase.mapper.impl;

import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.mapper.Mapper;
import fr.jonathanlebloas.computerdatabase.model.Company;

@Component
public class CompanyMapper implements Mapper<Company, CompanyDTO> {

	private CompanyMapper() {
	}

	@Override
	public CompanyDTO toDTO(Company company) {
		if (company == null) {
			return null;
		}

		CompanyDTO companyDto = new CompanyDTO();

		companyDto.setId(Long.toString(company.getId()));

		companyDto.setName(company.getName());

		return companyDto;
	}

	@Override
	public Company fromDTO(CompanyDTO dto) {
		return Company.builder().id(Long.parseLong(dto.getId())).name(dto.getName()).build();
	}
}
