package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ComputerNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidCompanyException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.InvalidDateException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;

public interface ComputerService {

	/**
	 * Return the full list of all the computers
	 *
	 * @return
	 * @throws ServiceException
	 */
	List<Computer> listComputers() throws ServiceException;

	/**
	 * Gives the details of the given computer
	 *
	 * @param c
	 * @return
	 * @throws ComputerNotFoundException
	 * @throws ServiceException
	 */
	String getComputerDetails(Computer c) throws ComputerNotFoundException, ServiceException;

	/**
	 * Find the computer with the given id
	 *
	 * @param id
	 * @return
	 * @throws ComputerNotFoundException
	 * @throws ServiceException
	 */
	Computer find(long id) throws ComputerNotFoundException, ServiceException;

	/**
	 * Retrieves the List of Computer with the parameter in their name
	 *
	 * @param s
	 * @return The list
	 * @throws EmptyNameException
	 * @throws ServiceException
	 */
	List<Computer> find(String s) throws EmptyNameException, ServiceException;

	/**
	 * Create a new computer
	 *
	 * @param c
	 * @return
	 * @throws EmptyNameException
	 * @throws InvalidDateException
	 * @throws ServiceException
	 * @throws InvalidCompanyException
	 */
	Computer create(Computer c)
			throws EmptyNameException, ServiceException, InvalidDateException, InvalidCompanyException;

	/**
	 * Update the given computer
	 *
	 * @param c
	 * @return
	 * @throws EmptyNameException
	 * @throws ComputerNotFoundException
	 * @throws InvalidDateException
	 * @throws ServiceException
	 * @throws InvalidCompanyException
	 */
	Computer update(Computer c) throws EmptyNameException, ComputerNotFoundException, ServiceException,
			InvalidDateException, InvalidCompanyException;

	/**
	 * Delete the given computer
	 *
	 * @param c
	 * @return
	 * @throws ComputerNotFoundException
	 * @throws InvalidDateException
	 */
	Computer delete(Computer c) throws ComputerNotFoundException, ServiceException;

	/**
	 * Return the number of pages given nb the number of elements per pages
	 *
	 * @param nb
	 * @return The number of pages possible
	 * @throws InvalidDateException
	 */
	int getNbPages(int nb) throws ServiceException;

	/**
	 * Return the Page at index containing nb elements or an empty page if the
	 * index doesn't exist. The first page index is 1
	 *
	 * @param index
	 * @param nb
	 * @return the page asked
	 * @throws InvalidDateException
	 */
	Page<Computer> getPage(int index, int nb) throws ServiceException;

}