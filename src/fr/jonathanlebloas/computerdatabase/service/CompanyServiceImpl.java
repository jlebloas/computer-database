package fr.jonathanlebloas.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Service used to manipulate companies Singleton
 */
public class CompanyServiceImpl implements CompanyService {

	public static final int DEFAULT_MAX_ITEMS = 10;

	private static CompanyServiceImpl instance = new CompanyServiceImpl();

	private CompanyDAO companyDAO = CompanyDAO.getInstance();

	private CompanyServiceImpl() {
		super();
	}

	/**
	 * @return the unique instance
	 */
	public static CompanyServiceImpl getIntance() {
		return instance;
	}

	@Override
	public List<Company> listCompanies() throws ServiceException {
		try {
			return companyDAO.list();
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public Company find(long id) throws CompanyNotFoundException, ServiceException {
		try {
			Company company = companyDAO.find(id);
			if (company == null) {
				throw new CompanyNotFoundException();
			} else {
				return company;
			}
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public List<Company> find(String name) throws EmptyNameException, ServiceException {
		try {
			if (StringUtils.isEmpty(name)) {
				throw new EmptyNameException();
			}

			return companyDAO.findByName(name);
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public int getNbPages() throws ServiceException {
		try {
			int total = companyDAO.count();
			int maxPerpage = DEFAULT_MAX_ITEMS;
			return (total + maxPerpage - 1) / maxPerpage;
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

	@Override
	public Page<Company> getPage(int index) throws ServiceException {
		try {
			int beginIndex = (index - 1) * DEFAULT_MAX_ITEMS;
			Page<Company> page = new Page<Company>(beginIndex, DEFAULT_MAX_ITEMS);

			int nbPages = this.getNbPages();
			if (0 < index && index <= nbPages) {
				companyDAO.populate(page);
			} else {
				page.setItems(new ArrayList<Company>());
			}

			return page;
		} catch (PersistenceException e) {
			throw new ServiceException();
		}
	}

}
