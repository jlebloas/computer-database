package fr.jonathanlebloas.computerdatabase.persistence;

import java.util.List;

import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

public abstract class DAO<T> {

	/**
	 * Allow to find by id in the db
	 *
	 * @param id
	 * @return
	 */
	public abstract T find(long id) throws PersistenceException;

	/**
	 * Allow to create object in the db
	 *
	 * @param obj
	 */
	public abstract T create(T obj) throws PersistenceException;

	/**
	 * Allow to update an Object in the db
	 *
	 * @param obj
	 */
	public abstract T update(T obj) throws PersistenceException;

	/**
	 * Allow to delete an Object in the db
	 *
	 * @param obj
	 */
	public abstract void delete(T obj) throws PersistenceException;

	/**
	 * Get the number of Object in the db
	 *
	 * @param obj
	 */
	public abstract int count() throws PersistenceException;

	/**
	 * Get the list of all Object
	 *
	 * @param obj
	 */
	public abstract List<T> list() throws PersistenceException;

	/**
	 * Populate the item list of the page
	 *
	 * @param page
	 * @return the populated page
	 */
	public abstract Page<T> populate(Page<T> page) throws PersistenceException;

	/**
	 * @param s
	 * @return Return the list of items having the String s in their name
	 */
	public abstract List<T> findByName(String s) throws PersistenceException;
}
