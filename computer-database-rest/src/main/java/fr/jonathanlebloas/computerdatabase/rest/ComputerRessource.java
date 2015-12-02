package fr.jonathanlebloas.computerdatabase.rest;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@Path("/computer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class ComputerRessource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRessource.class);

	@Autowired
	private ComputerService computerService;

	@Autowired
	private ComputerMapper computerMapper;

	@GET
	public List<ComputerDTO> list() {
		LOGGER.info("List computers}");
		return computerMapper.toDTO(computerService.listComputers());
	}

	@GET
	@Path("{id}")
	public ComputerDTO find(@PathParam("id") final long id) {
		LOGGER.info("Find computer with id : {}", id);
		return computerMapper.toDTO(computerService.find(id));
	}

	@PUT
	public void create(@Valid final ComputerDTO c) {
		LOGGER.info("Create computer : {}", c);
		computerService.create(computerMapper.fromDTO(c));
	}

	@POST
	@Path("{id}")
	public void update(@Valid final ComputerDTO c, @PathParam("id") final long id) {
		LOGGER.info("Update computer : {}", c);
		c.setId("" + id);
		computerService.update(computerMapper.fromDTO(c));
	}

	@DELETE
	@Path("{id}")
	public void delete(@PathParam("id") final long id) {
		LOGGER.info("Delete computer with id: {}", id);
		computerService.delete(id);
	}

	@GET
	@Path("page")
	public List<ComputerDTO> getPage(@DefaultValue("0") @QueryParam("page") final int page,
			@DefaultValue("10") @QueryParam("size") final int size,
			@DefaultValue("ASC") @QueryParam("direction") final Direction direction,
			@DefaultValue("id") @QueryParam("field") final String field,
			@DefaultValue("") @QueryParam("search") final String search) {

		final PageRequest pageRequest = new PageRequest(page, size, direction, field);
		LOGGER.info("Get page with search: {} search :{}", pageRequest, search);

		// Get the page of Computer from the service and then map it on a page
		// of ComputerDTO with a converter based on the computerMapper
		return computerService.getPage(pageRequest, search).map(new Converter<Computer, ComputerDTO>() {
			@Override
			public ComputerDTO convert(Computer computer) {
				return computerMapper.toDTO(computer);
			};
		}).getContent();
	}
}
