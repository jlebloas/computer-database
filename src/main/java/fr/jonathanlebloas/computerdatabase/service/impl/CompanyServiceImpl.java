package fr.jonathanlebloas.computerdatabase.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.DBConnection;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.persistence.impl.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.persistence.impl.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.utils.StringUtils;

/**
 * Service used to manipulate companies Singleton
 */
public enum CompanyServiceImpl implements CompanyService {
	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	private CompanyDAO companyDAO = CompanyDAO.INSTANCE;
	private ComputerDAO computerDAO = ComputerDAO.INSTANCE;

	@Override
	public List<Company> listCompanies() {
		LOGGER.debug("List companies");
		try {
			return companyDAO.list();

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during listing of companies", e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public Company find(long id) {
		LOGGER.debug("Find company with id : {}", id);
		try {
			Company company = companyDAO.find(id);
			if (company == null) {
				throw new CompanyNotFoundException();
			} else {
				return company;
			}
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred finding the company with id : " + id, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public List<Company> find(String s) {
		LOGGER.debug("Find companies with {} in their name", s);
		try {
			if (StringUtils.isEmpty(s)) {
				throw new EmptyNameException();
			}

			return companyDAO.findByName(s);
		} catch (PersistenceException e) {
			LOGGER.error("An error occurred finding the companies having '" + s + "' in their name", e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public void populatePage(Page<Company> page) {
		LOGGER.debug("Populating the companies page : {}", page);
		try {
			if (page.getIndex() < 1) {
				page.setIndex(1);
			}
			if (page.getSize() <= 0) {
				page.setSize(10);
			}

			int nbElements = companyDAO.count(page.getSearch());
			int maxPerpage = page.getSize();
			int nbTotalPages = (nbElements + maxPerpage - 1) / maxPerpage;

			page.setNbTotalElement(nbElements);

			page.setNbTotalPages(nbTotalPages);

			companyDAO.populateItems(page);

			LOGGER.debug("Companies page populated : {}", page);

		} catch (PersistenceException e) {
			LOGGER.error("An error occurred during while populating the companies page : " + page, e);
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

	@Override
	public void delete(Company c) {
		Connection connection = DBConnection.INSTANCE.getConnection();
		try {
			connection.setAutoCommit(false);

			Company company = companyDAO.find(c.getId());
			if (company == null) {
				throw new CompanyNotFoundException();
			}

			computerDAO.deleteWithCompanyId(c.getId());
			companyDAO.delete(c);

			connection.commit();

		} catch (PersistenceException | SQLException e) {
			LOGGER.error("An error occurred during while deleting the company : " + c, e);

			DBConnection.INSTANCE.rollbackConnection();
			throw new ServiceException();

		} finally {
			DBConnection.INSTANCE.closeConnection();
		}
	}

}
