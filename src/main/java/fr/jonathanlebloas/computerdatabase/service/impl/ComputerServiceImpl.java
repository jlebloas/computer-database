package fr.jonathanlebloas.computerdatabase.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.impl.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.service.ComputerService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidCompanyException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidDateException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.DateUtils;
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
		}
	}

	@Override
	public String getComputerDetails(Computer c) throws ComputerNotFoundException, ServiceException {
		LOGGER.debug("Getting details of the computer : {}", c);
		try {
			if (c == null) {
				return "";
			}

			Computer computer = computerDAO.find(c.getId());
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				return computer.toString();
			}
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during getting details of computer : " + c, e);
			throw new ServiceException();
		}
	}

	@Override
	public Computer find(long id) throws ComputerNotFoundException, ServiceException {
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
		}
	}

	@Override
	public List<Computer> find(String s) throws EmptyNameException, ServiceException {
		LOGGER.debug("Find computers with {} in their name", s);
		try {
			if (StringUtils.isEmpty(s)) {
				throw new EmptyNameException();
			}

			return computerDAO.findByName(s);
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred finding the computers having '" + s + "' in their name", e);
			throw new ServiceException();
		}
	}

	@Override
	public Computer create(Computer c)
			throws EmptyNameException, ServiceException, InvalidDateException, InvalidCompanyException {
		LOGGER.debug("Create computer : {}", c);
		try {
			if (c == null) {
				return null;
			}
			validate(c);

			computerDAO.create(c);

			return c;

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during creation of the computer : " + c, e);
			throw new ServiceException();
		} catch (CompanyNotFoundException e) {
			LOGGER.warn("The company in the computer is invalid : " + c, e);
			throw new InvalidCompanyException();
		}
	}

	@Override
	public Computer update(Computer c) throws EmptyNameException, ComputerNotFoundException, ServiceException,
			InvalidDateException, InvalidCompanyException {
		LOGGER.debug("Update computer : {}", c);
		try {
			if (c == null) {
				return null;
			}

			// Check the Computer already exists
			if (computerDAO.find(c.getId()) == null) {
				throw new ComputerNotFoundException();
			}
			validate(c);

			computerDAO.update(c);
			return c;

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during update of the computer : " + c, e);
			throw new ServiceException();
		} catch (CompanyNotFoundException e) {
			LOGGER.warn("The company in the computer is invalid : " + c, e);
			throw new InvalidCompanyException();
		}
	}

	@Override
	public Computer delete(Computer c) throws ComputerNotFoundException, ServiceException {
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
		}
	}

	@Override
	public int getNbPages(int nb) throws ServiceException {
		LOGGER.debug("Get number of computer pages with nb={}", nb);
		try {
			if (nb == 0) {
				return 0;
			}

			int total = computerDAO.count();
			int maxPerpage = nb;
			return (total + maxPerpage - 1) / maxPerpage;
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred while getting the number of pages of computers", e);
			throw new ServiceException();
		}
	}

	@Override
	public Page<Computer> getPage(int index, int nb) throws IndexOutOfBoundsException, ServiceException {
		LOGGER.debug("Get the computer page {}", index);
		try {
			int beginIndex = (index - 1) * nb;
			Page<Computer> page = new Page<Computer>(beginIndex, nb);

			int nbPages = this.getNbPages(nb);
			if (0 < index && index <= nbPages) {
				computerDAO.populate(page);
			} else {
				page.setItems(new ArrayList<Computer>());
			}

			return page;
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during while getting the computer page : " + index, e);
			throw new ServiceException();
		}
	}

	private void validate(Computer c) throws EmptyNameException, InvalidDateException, InvalidCompanyException,
			CompanyNotFoundException, ServiceException, PersistenceException {

		if (StringUtils.isEmpty(c.getName())) {
			throw new EmptyNameException();
		}

		// Check dates
		if (c.getIntroduced() != null && DateUtils.isValid(c.getIntroduced())) {
			throw new InvalidDateException("The introduced date of the computer is invalid.");
		}
		if (c.getDiscontinued() != null && DateUtils.isValid(c.getDiscontinued())) {
			throw new InvalidDateException("The discontinued date of the computer is invalid.");
		}
		// To set discontinued introduced must be set and lower than
		// discontinued
		if (c.getDiscontinued() != null) {
			if (c.getIntroduced() == null || c.getIntroduced().compareTo(c.getDiscontinued()) >= 0) {
				throw new InvalidDateException("The computer must be discontinued after he was introduced");
			}
		}
	}
}
