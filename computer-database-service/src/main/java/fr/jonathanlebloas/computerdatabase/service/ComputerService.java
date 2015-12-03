package fr.jonathanlebloas.computerdatabase.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.jonathanlebloas.computerdatabase.model.Computer;

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
	 * Create a new computer
	 *
	 * @param c
	 */
	void create(Computer c);

	/**
	 * Update the given computer
	 *
	 * @param c
	 */
	void update(Computer c);

	/**
	 * Delete the given computer
	 *
	 * @param c
	 */
	void delete(Computer c);

	/**
	 * Delete the computer with id
	 *
	 * @param id
	 */
	void delete(long id);

	/**
	 * Return the page asked
	 *
	 * @param pageable
	 * @param search
	 * @return
	 */
	Page<Computer> getPage(Pageable pageable, String search);

	/**
	 * Return if the computer exist
	 *
	 * @param id
	 * @return
	 */
	boolean exist(long id);
}
