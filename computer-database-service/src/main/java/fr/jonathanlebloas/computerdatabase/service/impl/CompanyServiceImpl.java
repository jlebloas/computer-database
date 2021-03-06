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

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.repository.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.CompanyService;

/**
 * Service used to manipulate companies Singleton
 */
@Service
@Transactional
public class CompanyServiceImpl implements CompanyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

	@Autowired
	private CompanyDAO companyDAO;

	@Autowired
	private ComputerDAO computerDAO;

	@Secured({ "ROLE_USER" })
	@Override
	@Transactional(readOnly = true)
	public List<Company> listCompanies() {
		LOGGER.debug("List companies");
		return companyDAO.findAll();
	}

	@Secured({ "ROLE_USER" })
	@Override
	@Transactional(readOnly = true)
	public Company find(long id) {
		LOGGER.debug("Find company with id : {}", id);
		return companyDAO.findOne(id);
	}

	@Override
	@Secured({ "ROLE_USER" })
	@Transactional(readOnly = true)
	public boolean exist(long id) {
		LOGGER.debug("Exist computer with id : {}", id);
		return companyDAO.exists(id);
	}

	@Secured({ "ROLE_USER" })
	@Override
	@Transactional(readOnly = true)
	public Page<Company> getPage(Pageable pageable, String search) {
		LOGGER.debug("Populating the companies page : {}", pageable);
		return companyDAO.findByNameContaining(pageable, search);
	}

	@Secured({ "ROLE_ADMIN" })
	@Override
	public void delete(Company c) {
		LOGGER.debug("Delete the company : {}", c);
		computerDAO.removeByCompany(c);
		companyDAO.delete(c);
	}

	@Secured({ "ROLE_ADMIN" })
	@Override
	public void delete(long id) {
		LOGGER.debug("Delete the company with id: {}", id);
		computerDAO.removeByCompany_Id(id);
		companyDAO.delete(id);
	}
}
