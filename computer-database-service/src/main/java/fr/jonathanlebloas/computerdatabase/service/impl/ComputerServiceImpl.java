package fr.jonathanlebloas.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;

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
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public List<Computer> listComputers() {
		LOGGER.debug("List computers");
		return computerDAO.findAll();
	}

	@Override
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public String getComputerDetails(Computer c) {
		LOGGER.debug("Getting details of the computer : {}", c);
		Computer computer = computerDAO.findOne(c.getId());
		return computer.toString();
	}

	@Override
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public Computer find(long id) {
		LOGGER.debug("Find computer with id : {}", id);
		return computerDAO.findOne(id);
	}

	@Override
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public boolean exist(long id) {
		LOGGER.debug("Exist computer with id : {}", id);
		return computerDAO.exists(id);
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
	public void create(Computer c) {
		LOGGER.debug("Create computer : {}", c);
		if (computerDAO.exists(c.getId())) {
			throw new IllegalArgumentException("The computer already exists");
		}
		computerDAO.save(c);
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
	public void update(Computer c) {
		LOGGER.debug("Update computer : {}", c);
		if (c == null) {
			throw new IllegalArgumentException("You must specify a computer");
		}
		// Check the Computer already exists
		if (!computerDAO.exists(c.getId())) {
			throw new IllegalArgumentException("Computer with id " + c.getId() + " not found !");
		}
		computerDAO.save(c);
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
	public void delete(Computer c) {
		LOGGER.debug("Delete computer : {}", c);
		computerDAO.delete(c);
	}

	@Override
	@Secured({ "ROLE_ADMIN" })
	public void delete(long id) {
		LOGGER.debug("Delete computer with id: {}", id);
		computerDAO.delete(id);
	}

	@Override
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public Page<Computer> getPage(Pageable pageable, String search) {
		LOGGER.debug("Populating the computers page : {}", pageable);
		return computerDAO.findByNameContainingOrCompany_NameContaining(pageable, search, search);
	}
}
