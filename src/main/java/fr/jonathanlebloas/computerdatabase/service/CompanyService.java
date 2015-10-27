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
	 * Return the number of pages given the number of elements per pages
	 *
	 * @param nb
	 * @return The number of pages possible
	 */
	int getNbPages(int nb);

	/**
	 * Return the Page at index containing nb elements or an empty page if the
	 * index doesn't exist. The first page index is 1
	 *
	 * @param index
	 * @return the page asked
	 */
	Page<Company> getPage(int index, int nb);

}