package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;

/**
 * Service interface used to manipulate companies
 */
public interface CompanyService {

	/**
	 * List all the companies
	 *
	 * @return
	 * @throws ServiceException
	 */
	List<Company> listCompanies() throws ServiceException;

	/**
	 * Retrieves the Company with the given id
	 *
	 * @param id
	 * @return
	 * @throws EmptyNameException
	 * @throws ServiceException
	 */
	Company find(long id) throws CompanyNotFoundException, ServiceException;

	/**
	 * Retrieves the list of Company with s in their name
	 *
	 * @param name
	 * @return
	 * @throws EmptyNameException
	 * @throws ServiceException
	 */
	List<Company> find(String s) throws EmptyNameException, ServiceException;

	/**
	 * Return the number of pages given the number of elements per pages
	 *
	 * @param nb
	 * @return The number of pages possible
	 * @throws ServiceException
	 */
	int getNbPages(int nb) throws ServiceException;

	/**
	 * Return the Page at index containing nb elements or an empty page if the
	 * index doesn't exist. The first page index is 1
	 *
	 * @param index
	 * @return the page asked
	 * @throws ServiceException
	 */
	Page<Company> getPage(int index, int nb) throws ServiceException;

}