package fr.jonathanlebloas.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;

/**
 * Service used to manipulate computers Singleton
 */
@Transactional
@Service
public class ComputerServiceImpl implements ComputerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceImpl.class);

	@Autowired
	private ComputerDAO computerDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Computer> listComputers() {
		LOGGER.debug("List computers");
		return computerDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public String getComputerDetails(Computer c) {
		LOGGER.debug("Getting details of the computer : {}", c);
		Computer computer = computerDAO.findOne(c.getId());
		if (computer == null) {
			throw new ComputerNotFoundException();
		} else {
			return computer.toString();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Computer find(long id) {
		LOGGER.debug("Find computer with id : {}", id);
		Computer computer = computerDAO.findOne(id);
		if (computer == null) {
			throw new ComputerNotFoundException();
		} else {
			return computer;
		}
	}

	@Override
	public void create(Computer c) {
		LOGGER.debug("Create computer : {}", c);
		computerDAO.save(c);
	}

	@Override
	public void update(Computer c) {
		LOGGER.debug("Update computer : {}", c);
		if (c == null) {
			throw new IllegalArgumentException("The computer is null");
		}
		// Check the Computer already exists
		if (computerDAO.findOne(c.getId()) == null) {
			throw new ComputerNotFoundException();
		}
		computerDAO.save(c);
	}

	@Override
	public void delete(Computer c) {
		LOGGER.debug("Delete computer : {}", c);
		computerDAO.delete(c);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Computer> getPage(Pageable pageable, String search) {
		LOGGER.debug("Populating the computers page : {}", pageable);
		return computerDAO.findByNameContainingOrCompany_NameContaining(pageable, search, search);
	}
}