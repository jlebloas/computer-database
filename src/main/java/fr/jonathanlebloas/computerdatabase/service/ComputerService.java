package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;

public interface ComputerService {

	/**
	 * Return the full list of all the computers
	 *
	 * @return
	 */
	List<Computer> listComputers();

	/**
	 * Gives the details of the given computer
	 *
	 * @param c
	 * @return
	 */
	String getComputerDetails(Computer c);

	/**
	 * Find the computer with the given id
	 *
	 * @param id
	 * @return
	 */
	Computer find(long id);

	/**
	 * Retrieves the List of Computer with the parameter in their name
	 *
	 * @param s
	 * @return The list
	 */
	List<Computer> find(String s);

	/**
	 * Create a new computer
	 *
	 * @param c
	 * @return
	 */
	Computer create(Computer c);

	/**
	 * Update the given computer
	 *
	 * @param c
	 * @return
	 */
	Computer update(Computer c);

	/**
	 * Delete the given computer
	 *
	 * @param c
	 * @return
	 */
	Computer delete(Computer c);

	/**
	 * Fill the page with all elements
	 *
	 * The authorized indexes are 1,2,3,4,6
	 *
	 * @param page
	 */
	void populatePage(Page<Computer> page);

}