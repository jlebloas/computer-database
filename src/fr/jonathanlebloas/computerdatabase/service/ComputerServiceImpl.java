package fr.jonathanlebloas.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
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
public class ComputerServiceImpl implements ComputerService {

	public static final int DEFAULT_MAX_ITEMS = 10;

	private static ComputerServiceImpl instance = new ComputerServiceImpl();

	private CompanyService companyService = CompanyServiceImpl.getIntance();

	private ComputerDAO computerDAO = ComputerDAO.getInstance();

	private ComputerServiceImpl() {
		super();
	}

	/**
	 * @return the unique instance
	 */
	public static ComputerServiceImpl getInstance() {
		return instance;
	}

	@Override
	public List<Computer> listComputers() throws ServiceException {
		try {
			return computerDAO.list();
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public String getComputerDetails(Computer c) throws ComputerNotFoundException, ServiceException {
		try {
			Computer computer = computerDAO.find(c.getId());
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				return computer.toString();
			}
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public Computer find(long id) throws ComputerNotFoundException, ServiceException {
		try {
			Computer computer = computerDAO.find(id);
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				return computer;
			}
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public List<Computer> find(String s) throws EmptyNameException, ServiceException {
		try {
			if (StringUtils.isEmpty(s)) {
				throw new EmptyNameException();
			}

			return computerDAO.findByName(s);
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public Computer create(Computer c) throws EmptyNameException, ServiceException, InvalidDateException,
			ComputerNotFoundException, InvalidCompanyException {
		try {
			validate(c);

			return computerDAO.create(c);

		} catch (PersistenceException e) {
			throw new ServiceException();
		} catch (CompanyNotFoundException e) {
			throw new InvalidCompanyException();
		}
	}

	@Override
	public Computer update(Computer c) throws EmptyNameException, ComputerNotFoundException, ServiceException,
			InvalidDateException, InvalidCompanyException {
		try {
			// Check the Computer already exists
			if (computerDAO.find(c.getId()) == null) {
				throw new ComputerNotFoundException();
			}
			validate(c);

			computerDAO.update(c);
			return c;

		} catch (PersistenceException e) {
			throw new ServiceException();
		} catch (CompanyNotFoundException e) {
			throw new InvalidCompanyException();
		}
	}

	@Override
	public Computer delete(Computer c) throws ComputerNotFoundException, ServiceException {
		try {
			Computer computer = computerDAO.find(c.getId());
			if (computer == null) {
				throw new ComputerNotFoundException();
			} else {
				computerDAO.delete(computer);
				return computer;
			}
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public int getNbPages() throws ServiceException {
		try {
			int total = computerDAO.count();
			int maxPerpage = DEFAULT_MAX_ITEMS;
			return (total + maxPerpage - 1) / maxPerpage;
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public Page<Computer> getPage(int index) throws IndexOutOfBoundsException, ServiceException {
		try {
			int beginIndex = (index - 1) * DEFAULT_MAX_ITEMS;
			Page<Computer> page = new Page<Computer>(beginIndex, DEFAULT_MAX_ITEMS);

			int nbPages = this.getNbPages();
			if (0 < index && index <= nbPages) {
				computerDAO.populate(page);
			} else {
				page.setItems(new ArrayList<Computer>());
			}

			return page;
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	private void validate(Computer c) throws ComputerNotFoundException, EmptyNameException, InvalidDateException,
			InvalidCompanyException, CompanyNotFoundException, ServiceException, PersistenceException {

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
		// Check the company exist
		if (c.getManufacturer() != null
				&& !c.getManufacturer().equals(companyService.find(c.getManufacturer().getId()))) {
			throw new InvalidCompanyException();
		}
	}
}
