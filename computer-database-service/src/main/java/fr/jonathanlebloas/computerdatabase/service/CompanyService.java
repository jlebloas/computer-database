package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.jonathanlebloas.computerdatabase.model.Company;

/**
 * Service interface used to manipulate companies
 */
public interface CompanyService {

	/**
	 * List all the companies
	 *
	 * @return
	 */
	List<Company> listCompanies();

	/**
	 * Retrieves the Company with the given id
	 *
	 * @param id
	 * @return
	 */
	Company find(long id);

	/**
	 * Return the page asked
	 *
	 * @param pageable
	 * @param search
	 * @return
	 */
	Page<Company> getPage(Pageable pageable, String search);

	/**
	 * Delete the given company
	 */
	void delete(Company c);

}