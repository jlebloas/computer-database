package fr.jonathanlebloas.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.DBConnection;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.persistence.impl.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Service used to manipulate computers Singleton
 */
public enum ComputerServiceImpl implements ComputerService {
	INSTANCE;

	private ComputerDAO computerDAO = ComputerDAO.INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerServiceImpl.class);

	@Override
	public List<Computer> listComputers() throws ServiceException {
		LOGGER.debug("List computers");
		try {
			return computerDAO.list();
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during listing of computer", e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public String getComputerDetails(Computer c) {
		LOGGER.debug("Getting details of the computer : {}", c);
		try {
			Computer computer = computerDAO.find(c.getId());
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				return computer.toString();
			}
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during getting details of computer : " + c, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public Computer find(long id) {
		LOGGER.debug("Find computer with id : {}", id);
		try {
			Computer computer = computerDAO.find(id);
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				return computer;
			}
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred finding the computer with id : " + id, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public List<Computer> find(String s) {
		LOGGER.debug("Find computers with {} in their name", s);
		try {
			if (StringUtils.isEmpty(s)) {
				throw new EmptyNameException();
			}

			return computerDAO.findByName(s);
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred finding the computers having '" + s + "' in their name", e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public Computer create(Computer c) {
		LOGGER.debug("Create computer : {}", c);
		try {
			// The validation is made by the controlers
			computerDAO.create(c);

			return c;

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during creation of the computer : " + c, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public Computer update(Computer c) {
		LOGGER.debug("Update computer : {}", c);
		try {
			if (c == null) {
				throw new IllegalArgumentException("The computer is null");
			}

			// Check the Computer already exists
			if (computerDAO.find(c.getId()) == null) {
				throw new ComputerNotFoundException();
			}

			computerDAO.update(c);
			return c;

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during update of the computer : " + c, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public Computer delete(Computer c) {
		LOGGER.debug("Delete computer : {}", c);
		try {
			if (c == null) {
				return null;
			}

			Computer computer = computerDAO.find(c.getId());
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				computerDAO.delete(computer);
				return computer;
			}
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during deletion of the computer : " + c, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public void populatePage(Page<Computer> page) {
		LOGGER.debug("Populating the computers page : {}", page);
		try {
			if (page.getIndex() < 1) {
				page.setIndex(1);
			}
			if (page.getSize() <= 0) {
				page.setSize(10);
			}

			int total = computerDAO.count(page.getSearch());
			int maxPerpage = page.getSize();
			int nbTotalPages = (total + maxPerpage - 1) / maxPerpage;

			page.setNbTotalElement(total);

			page.setNbTotalPages(nbTotalPages);

			computerDAO.populateItems(page);

			LOGGER.debug("Computers page populated : {}", page);

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during while populating the computers page : " + page, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

}
