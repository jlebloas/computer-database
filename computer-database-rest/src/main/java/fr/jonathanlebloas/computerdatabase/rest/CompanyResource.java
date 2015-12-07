package fr.jonathanlebloas.computerdatabase.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.dto.PageDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.mapper.impl.PageMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;

@RestController
@RequestMapping(path = "/company")
public class CompanyResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyResource.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private PageMapper pageMapper;

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
	public List<CompanyDTO> getPage(@ModelAttribute PageDTO page) {
		LOGGER.info("Get company page : {}", page);
		PageRequest pageRequest = pageMapper.fromDTO(page);

		// Get the page of Computer from the service and then map it on a page
		// of CompanyDTO with a converter based on the companyMapper
		return companyService.getPage(pageRequest, page.getSearch()).map(new Converter<Company, CompanyDTO>() {
			@Override
			public CompanyDTO convert(Company company) {
				return companyMapper.toDTO(company);
			}
		}).getContent();
	}
}
