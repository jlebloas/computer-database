package fr.jonathanlebloas.computerdatabase.controller.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;

@Component
public class ComputerControllerUtil {

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private CompanyService companyService;

	/**
	 * Return the prepared list of companies for the dropDown list
	 *
	 * @param request
	 * @return
	 */
	public List<CompanyDTO> getCompanies() {
		List<Company> companies = companyService.listCompanies();
		companies.add(0, Company.builder().id(0).name("--").build());
		return companyMapper.toDTO(companies);
	}
}
