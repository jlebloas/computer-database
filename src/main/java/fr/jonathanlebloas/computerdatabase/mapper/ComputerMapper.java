package fr.jonathanlebloas.computerdatabase.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;

public enum ComputerMapper {
	INSTANCE;

	private static DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private ComputerMapper() {
	}

	public static ComputerDTO mapModelToDTO(Computer computer) {
		ComputerDTO computerDTO = new ComputerDTO();

		computerDTO.setId("" + computer.getId());

		computerDTO.setName(computer.getName());

		if (computer.getManufacturer() != null) {
			computerDTO.setManufacturerName(computer.getManufacturer().getName());
		} else {
			computerDTO.setManufacturerName(null);
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

	public static List<ComputerDTO> mapModelListToDTO(List<Computer> list) {
		List<ComputerDTO> temp = new ArrayList<>();
		for (Iterator<Computer> iterator = list.iterator(); iterator.hasNext();) {
			Computer computer = iterator.next();

			temp.add(mapModelToDTO(computer));
		}
		return temp;
	}

	public static Computer mapDTOToModel(ComputerDTO dto) {
		Computer computer = new Computer();
		Company company = null;
		if (dto.getManufacturerId() != null && dto.getManufacturerName() != null) {
			company = CompanyMapper
					.mapDTOToModel(new CompanyDTO(dto.getManufacturerId(), dto.getManufacturerName()));
		}

		// TODO HandleParsing on date and long
		if (dto.getId() != null) {
			computer.setId(Long.parseLong(dto.getId()));
		}

		computer.setManufacturer(company);
		computer.setName(dto.getName());
		if (dto.getIntroduced() != null) {
			computer.setIntroduced(LocalDate.parse(dto.getIntroduced(), df));
		}
		if (dto.getDiscontinued() != null) {
			computer.setDiscontinued(LocalDate.parse(dto.getDiscontinued()));
		}

		return computer;
	}

	public static List<Computer> mapDTOToModel(List<ComputerDTO> DTOList) {

		List<Computer> temp = new ArrayList<>();
		for (Iterator<ComputerDTO> iterator = DTOList.iterator(); iterator.hasNext();) {
			ComputerDTO dto = iterator.next();

			temp.add(mapDTOToModel(dto));
		}
		return temp;
	}

}
