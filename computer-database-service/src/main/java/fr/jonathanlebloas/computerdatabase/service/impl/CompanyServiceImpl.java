package fr.jonathanlebloas.computerdatabase.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	@Transactional(readOnly = true)
	public List<Company> listCompanies() {
		LOGGER.debug("List companies");
		return companyDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Company find(long id) {
		LOGGER.debug("Find company with id : {}", id);
		return companyDAO.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Company> getPage(Pageable pageable, String search) {
		LOGGER.debug("Populating the companies page : {}", pageable);
		return companyDAO.findByNameContaining(pageable, search);
	}

	@Override
	public void delete(Company c) {
		LOGGER.debug("Delete the company : {}", c);
		computerDAO.removeByCompany(c);
		companyDAO.delete(c);
	}
}
