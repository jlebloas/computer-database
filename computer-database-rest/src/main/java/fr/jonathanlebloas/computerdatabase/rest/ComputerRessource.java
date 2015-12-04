package fr.jonathanlebloas.computerdatabase.rest;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.jonathanlebloas.computerdatabase.dto.ComputerDTO;
import fr.jonathanlebloas.computerdatabase.mapper.impl.ComputerMapper;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

@RestController
@RequestMapping(path = "/computer")
public class ComputerRessource {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerRessource.class);

	@Autowired
	private ComputerService computerService;

	@Autowired
	private ComputerMapper computerMapper;

	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public List<ComputerDTO> list() {
		LOGGER.info("List computers}");
		return computerMapper.toDTO(computerService.listComputers());
	}

	@RequestMapping(path = "/{id:\\d+}", method = RequestMethod.GET, produces = "application/json")
	public ComputerDTO find(@PathVariable("id") final long id) {
		LOGGER.info("Find computer with id : {}", id);
		return computerMapper.toDTO(computerService.find(id));
	}

	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public void create(@Valid @RequestBody final ComputerDTO c) {
		LOGGER.info("Create computer : {}", c);
		computerService.create(computerMapper.fromDTO(c));
	}

	@RequestMapping(path = "/{id:\\d+}", method = RequestMethod.POST, consumes = "application/json")
	public void update(@Valid @RequestBody final ComputerDTO c, @PathVariable("id") final long id) {
		LOGGER.info("Update computer : {}", c);
		c.setId(Long.toString(id));
		computerService.update(computerMapper.fromDTO(c));
	}

	@RequestMapping(path = "/{id:\\d+}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") final long id) {
		LOGGER.info("Delete computer with id: {}", id);
		if (computerService.exist(id)) {
			computerService.delete(id);
		}
	}

	@RequestMapping(path = "/page", method = RequestMethod.GET, produces = "application/json")
	public List<ComputerDTO> getPage(
			@RequestParam(value = "page", required = false, defaultValue = "0") final int page,
			@RequestParam(value = "size", required = false, defaultValue = "10") final int size,
			@RequestParam(value = "direction", required = false, defaultValue = "ASC") final Direction direction,
			@RequestParam(value = "field", required = false, defaultValue = "id") final String field,
			@RequestParam(value = "search", required = false, defaultValue = "") final String search) {

		final PageRequest pageRequest = new PageRequest(page, size, direction, field);
		LOGGER.info("Get page with search: {} search :{}", pageRequest, search);

		// Get the page of Computer from the service and then map it on a page
		// of ComputerDTO with a converter based on the computerMapper
		return computerService.getPage(pageRequest, search).map(new Converter<Computer, ComputerDTO>() {
			@Override
			public ComputerDTO convert(Computer computer) {
				return computerMapper.toDTO(computer);
			}
		}).getContent();
	}
}
