package fr.jonathanlebloas.computerdatabase.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static CompanyServiceImpl instance = new CompanyServiceImpl();

	private CompanyDAO companyDAO = CompanyDAO.getInstance();

	private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);

	private CompanyServiceImpl() {
		super();
	}

	/**
	 * @return the unique instance
	 */
	public static CompanyServiceImpl getInstance() {
		return instance;
	}

	void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Override
	public List<Company> listCompanies() throws ServiceException {
		logger.debug("List companies");
		try {
			return companyDAO.list();
		} catch (PersistenceException e) {
			logger.error("An error occurred during listing of companies", e);
			throw new ServiceException();
		}
	}

	@Override
	public Company find(long id) throws CompanyNotFoundException, ServiceException {
		logger.debug("Find company with id : {}", id);
		try {
			Company company = companyDAO.find(id);
			if (company == null) {
				throw new CompanyNotFoundException();
			} else {
				return company;
			}
		} catch (PersistenceException e) {
			logger.error("An error occurred finding the company with id : " + id, e);
			throw new ServiceException();
		}
	}

	@Override
	public List<Company> find(String s) throws EmptyNameException, ServiceException {
		logger.debug("Find companies with {} in their name", s);
		try {
			if (StringUtils.isEmpty(s)) {
				throw new EmptyNameException();
			}

			return companyDAO.findByName(s);
		} catch (PersistenceException e) {
			logger.error("An error occurred finding the companies having '" + s + "' in their name", e);
			throw new ServiceException();
		}
	}

	@Override
	public int getNbPages(int nb) throws ServiceException {
		logger.debug("Get number of companies pages with nb={}", nb);
		try {
			if (nb == 0) {
				return 0;
			}

			int total = companyDAO.count();
			int maxPerpage = nb;
			return (total + maxPerpage - 1) / maxPerpage;
		} catch (PersistenceException e) {
			logger.error("An error occurred while getting the number of pages of companies", e);
			throw new ServiceException();
		}
	}

	@Override
	public Page<Company> getPage(int index, int nb) throws ServiceException {
		logger.debug("Get the companies page {}", index);
		try {
			int beginIndex = (index - 1) * nb;
			Page<Company> page = new Page<Company>(beginIndex, nb);

			int nbPages = this.getNbPages(nb);
			if (0 < index && index <= nbPages) {
				companyDAO.populate(page);
			} else {
				page.setItems(new ArrayList<Company>());
			}

			return page;
		} catch (PersistenceException e) {
			logger.error("An error occurred during while getting the companies page : " + index, e);
			throw new ServiceException();
		}
	}

}
