package fr.jonathanlebloas.computerdatabase.persistence;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

public interface DAO<T> {

	/**
	 * Allow to find by id in the db
	 *
	 * @param id
	 * @return
	 */
	public default T find(long id) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Allow to create object in the db
	 *
	 * @param obj
	 */
	public default void create(T obj) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Allow to update an Object in the db
	 *
	 */
	public default void update(T obj) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Allow to delete an Object in the db
	 *
	 * @param obj
	 */
	public default void delete(T obj) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the number of Object in the db that matches
	 *
	 * @param obj
	 */
	public default int count(String search) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Get the list of all Object
	 *
	 * @param obj
	 */
	public default List<T> list() throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Populate the item list of the page
	 *
	 * @param page
	 */
	public default void populateItems(Page<T> page) throws PersistenceException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Return the list of items having the String s in their name
	 *
	 * @param s
	 * @return
	 */
	public default List<T> findByName(String s) throws PersistenceException {
		throw new UnsupportedOperationException();
	}
}
