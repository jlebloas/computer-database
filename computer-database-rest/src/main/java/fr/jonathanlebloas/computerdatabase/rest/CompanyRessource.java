package fr.jonathanlebloas.computerdatabase.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;

@RestController
@RequestMapping(path = "/company")
public class CompanyRessource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRessource.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyMapper companyMapper;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<CompanyDTO> list() {
		LOGGER.info("List computers}");
		return companyMapper.toDTO(companyService.listCompanies());
	}

	@RequestMapping(path = "/{id:\\d+}", method = RequestMethod.GET, produces = "application/json")
	public CompanyDTO find(@PathVariable("id") final long id) {
		LOGGER.info("Find computer with id : {}", id);
		return companyMapper.toDTO(companyService.find(id));
	}

	@RequestMapping(path = "/{id:\\d+}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") final long id) {
		LOGGER.info("Delete computer with id: {}", id);
		if (companyService.exist(id)) {
			companyService.delete(id);
		}
	}

	@RequestMapping(path = "/page", method = RequestMethod.GET, produces = "application/json")
	public List<CompanyDTO> getPage(@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") final int size,
			@RequestParam(value = "direction", required = false, defaultValue = "ASC") final Direction direction,
			@RequestParam(value = "field", required = false, defaultValue = "id") final String field,
			@RequestParam(value = "search", required = false, defaultValue = "") final String search) {

		final PageRequest pageRequest = new PageRequest(page, size, direction, field);
		LOGGER.info("Get page with search: {} search :{}", pageRequest, search);

		// Get the page of Computer from the service and then map it on a page
		// of CompanyDTO with a converter based on the companyMapper
		return companyService.getPage(pageRequest, search).map(new Converter<Company, CompanyDTO>() {
			@Override
			public CompanyDTO convert(Company company) {
				return companyMapper.toDTO(company);
			};
		}).getContent();
	}
}
