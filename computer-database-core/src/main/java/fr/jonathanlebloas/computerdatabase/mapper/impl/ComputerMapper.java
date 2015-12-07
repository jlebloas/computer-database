package fr.jonathanlebloas.computerdatabase.mapper.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.Mapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

@Component
public class ComputerMapper implements Mapper<Computer, ComputerDTO> {

	@Autowired
	private CompanyMapper companyMapper;

	private DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private ComputerMapper() {
	}

	@Override
	public ComputerDTO toDTO(Computer computer) {
		if (computer==null) {
			return null;
		}
		ComputerDTO computerDTO = new ComputerDTO();

		computerDTO.setId(Long.toString(computer.getId()));

		computerDTO.setName(computer.getName());

		Company company = computer.getCompany();
		if (company != null) {
			computerDTO.setCompanyName(company.getName());
			computerDTO.setCompanyId(Long.toString(company.getId()));
		} else {
			computerDTO.setCompanyName(null);
			computerDTO.setCompanyId("0");
		}
		computerDTO.setName(computer.getName());
		if (computer.getDiscontinued() != null) {
			computerDTO.setDiscontinued(computer.getDiscontinued().format(df));
		} else {
			computerDTO.setDiscontinued(null);
		}
		if (computer.getIntroduced() != null) {
			computerDTO.setIntroduced(computer.getIntroduced().format(df));
		} else {
			computerDTO.setIntroduced(null);
		}
		return computerDTO;
	}

	@Override
	public Computer fromDTO(ComputerDTO dto) {
		Company company = null;
		if (dto.getCompanyId() != null) {
			company = companyMapper.fromDTO(new CompanyDTO(dto.getCompanyId(), dto.getCompanyName()));
			company = company.getId() <= 0L ? null : company;
		}

		Computer computer = Computer.builder().name(dto.getName()).company(company).build();
		if (dto.getId() != null) {
			computer.setId(Long.parseLong(dto.getId()));
		}

		computer.setName(dto.getName());
		if (!StringUtils.isEmpty(dto.getIntroduced())) {
			computer.setIntroduced(LocalDate.parse(dto.getIntroduced(), df));
		}
		if (!StringUtils.isEmpty(dto.getDiscontinued())) {
			computer.setDiscontinued(LocalDate.parse(dto.getDiscontinued()));
		}

		return computer;
	}
}
