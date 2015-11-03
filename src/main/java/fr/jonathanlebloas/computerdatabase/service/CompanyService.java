package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;

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
	 * Retrieves the list of Company with s in their name
	 *
	 * @param name
	 * @return
	 */
	List<Company> find(String s);

	/**
	 * Fill the page with all elements
	 *
	 * index order possibles are 1 and 2
	 *
	 * @param page
	 */
	void populatePage(Page<Company> page);

}