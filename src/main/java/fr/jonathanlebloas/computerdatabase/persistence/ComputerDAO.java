package fr.jonathanlebloas.computerdatabase.persistence;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

public interface ComputerDAO extends DAO<Computer> {

	/**
	 * Delete computers with given company id
	 *
	 * @param id
	 * @throws PersistenceException
	 */
	void deleteWithCompanyId(long id) throws PersistenceException;
}
