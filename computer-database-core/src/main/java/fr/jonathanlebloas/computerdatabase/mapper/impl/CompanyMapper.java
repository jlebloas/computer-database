package fr.jonathanlebloas.computerdatabase.mapper.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

		companyDto.setId("" + company.getId());

		companyDto.setName(company.getName());

		return companyDto;
	}

	@Override
	public List<CompanyDTO> toDTO(List<Company> list) {
		List<CompanyDTO> temp = new ArrayList<>();
		for (Iterator<Company> iterator = list.iterator(); iterator.hasNext();) {
			Company company = iterator.next();

			temp.add(toDTO(company));
		}
		return temp;
	}

	@Override
	public Company fromDTO(CompanyDTO dto) {
		return Company.builder().id(Long.parseLong(dto.getId())).name(dto.getName()).build();
	}

	@Override
	public List<Company> fromDTO(List<CompanyDTO> DTOList) {

		List<Company> temp = new ArrayList<>();
		for (Iterator<CompanyDTO> iterator = DTOList.iterator(); iterator.hasNext();) {
			CompanyDTO dto = iterator.next();

			temp.add(fromDTO(dto));
		}
		return temp;
	}

}
