package fr.jonathanlebloas.computerdatabase.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import fr.jonathanlebloas.computerdatabase.dto.CompanyDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.CompanyMapper;
import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;

@Path("/company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class CompanyRessource {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyRessource.class);

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyMapper companyMapper;

	@GET
	public List<CompanyDTO> list() {
		LOGGER.info("List computers}");
		return companyMapper.toDTO(companyService.listCompanies());
	}

	@GET
	@Path("{id}")
	public CompanyDTO find(@PathParam("id") final long id) {
		LOGGER.info("Find computer with id : {}", id);
		return companyMapper.toDTO(companyService.find(id));
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") final long id) {
		LOGGER.info("Delete computer with id: {}", id);
		companyService.delete(id);
	}

	@GET
	@Path("page")
	public List<CompanyDTO> getPage(@DefaultValue("0") @QueryParam("page") final int page,
			@DefaultValue("10") @QueryParam("size") final int size,
			@DefaultValue("ASC") @QueryParam("direction") final Direction direction,
			@DefaultValue("id") @QueryParam("field") final String field,
			@DefaultValue("") @HeaderParam("search") final String search) {

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
