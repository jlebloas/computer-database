package fr.jonathanlebloas.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.DAO;
import fr.jonathanlebloas.computerdatabase.persistence.DBConnection;
import fr.jonathanlebloas.computerdatabase.persistence.RowMapper;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

/**
 * DAO used for computers Singleton
 */
public enum ComputerDAO implements DAO<Computer> {
	INSTANCE;

	private ComputerRowMapper rowMapper = new ComputerRowMapper();

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAO.class);

	public static final Map<Integer, String> COLUMN_ORDER = new HashMap<>();

	static {
		COLUMN_ORDER.put(1, "c.id");
		COLUMN_ORDER.put(2, "c.name");
		COLUMN_ORDER.put(3, "c.introduced");
		COLUMN_ORDER.put(4, "c.discontinued");
		COLUMN_ORDER.put(6, "m.name");
	}

	@Override
	public Computer find(long id) throws PersistenceException {
		LOGGER.trace("Finding computer with id: {}", id);
		Connection connect = DBConnection.INSTANCE.getConnection();

		Computer computer = null;

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.id=?");
			prepared.setLong(1, id);

			ResultSet rs = prepared.executeQuery();

			// If there's a result
			if (rs.first()) {
				computer = rowMapper.mapRow(rs);
			}

		} catch (SQLException se) {
			LOGGER.error("Error while finding the computer with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);

		}

		return computer;
	}

	@Override
	public void create(Computer computer) throws PersistenceException {
		LOGGER.trace("Creating computer : {}", computer);
		Connection connect = DBConnection.INSTANCE.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"INSERT INTO computer(name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			prepared.setString(1, computer.getName());

			// Set the introduced or null if needed
			if (computer.getIntroduced() == null) {
				prepared.setNull(2, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(2, Timestamp.valueOf(computer.getIntroduced().atStartOfDay()));
			}

			// Set the discontinued or null if needed
			if (computer.getDiscontinued() == null) {
				prepared.setNull(3, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(3, Timestamp.valueOf(computer.getDiscontinued().atStartOfDay()));
			}

			// Set the company_id foreign key on company
			if (computer.getCompany() == null) {
				prepared.setNull(4, java.sql.Types.BIGINT);
			} else {
				prepared.setLong(4, computer.getCompany().getId());
			}
			prepared.executeUpdate();

			ResultSet rs = prepared.getGeneratedKeys();
			if (rs.first()) {
				computer.setId(rs.getLong(1));
			} else {
				throw new SQLException("The computer was not inserted : " + computer.toString());
			}

		} catch (SQLException se) {
			LOGGER.error("Error while creating the computer : " + computer, se);
			throw new PersistenceException("SQL exception during creation of a computer : " + computer.toString(), se);
		}
	}

	@Override
	public Computer update(Computer obj) throws PersistenceException {
		LOGGER.trace("Updating computer : {}", obj);
		Connection connect = DBConnection.INSTANCE.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"UPDATE computer c SET c.name = ?, c.introduced = ?, c.discontinued = ?, c.company_id = ? WHERE c.id = ?");
			prepared.setString(1, obj.getName());

			// Set the introduced or null if needed
			if (obj.getIntroduced() == null) {
				prepared.setNull(2, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(2, Timestamp.valueOf(obj.getIntroduced().atStartOfDay()));
			}

			// Set the discontinued or null if needed
			if (obj.getDiscontinued() == null) {
				prepared.setNull(3, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(3, Timestamp.valueOf(obj.getDiscontinued().atStartOfDay()));
			}

			// Set the company_id foreign key on company
			if (obj.getCompany() == null) {
				prepared.setNull(4, java.sql.Types.BIGINT);
			} else {
				prepared.setLong(4, obj.getCompany().getId());
			}

			prepared.setLong(5, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			LOGGER.error("Error while updating the computer : " + obj, se);
			throw new PersistenceException("SQL exception during update of a computer : " + obj.toString(), se);
		}

		return obj;
	}

	@Override
	public void delete(Computer obj) throws PersistenceException {
		LOGGER.trace("Deleting computer : {}", obj);
		Connection connect = DBConnection.INSTANCE.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("DELETE FROM computer WHERE id=?");
			prepared.setLong(1, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			LOGGER.error("Error while deleting the computer : " + obj, se);
			throw new PersistenceException("SQL exception during deletion of a computer : " + obj.toString(), se);
		}

	}

	public void deleteWithCompanyId(long id) throws PersistenceException {
		LOGGER.trace("Deleting computer with company id : {}", id);
		Connection connect = DBConnection.INSTANCE.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("DELETE FROM computer WHERE company_id=?");
			prepared.setLong(1, id);

			prepared.executeUpdate();

		} catch (SQLException se) {
			LOGGER.error("Error while deleting the computers with company id : " + id, se);
			throw new PersistenceException("SQL exception during deletion of a computers with company id : " + id, se);
		}

	}

	@Override
	public int count(String search) throws PersistenceException {
		LOGGER.trace("Counting computers");
		Connection connect = DBConnection.INSTANCE.getConnection();

		int tmp = 0;
		try {

			PreparedStatement prepared = connect.prepareStatement(
					"SELECT count(*) FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.name LIKE ? OR m.name LIKE ?");

			prepared.setString(1, "%" + search + "%");
			prepared.setString(2, "%" + search + "%");

			ResultSet result = prepared.executeQuery();

			if (result.first()) {
				tmp = result.getInt(1);
			}

		} catch (SQLException se) {
			LOGGER.error("Error while counting the computers", se);
			throw new PersistenceException("SQL exception during count of computers ", se);
		}

		return tmp;
	}

	@Override
	public List<Computer> list() throws PersistenceException {
		LOGGER.trace("Listing computers");
		Connection connect = DBConnection.INSTANCE.getConnection();

		List<Computer> list = new ArrayList<Computer>();
		try {
			PreparedStatement prepared = connect.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id");

			ResultSet rs = prepared.executeQuery();

			while (rs.next()) {
				Computer computer = rowMapper.mapRow(rs);

				list.add(computer);
			}
		} catch (SQLException se) {
			LOGGER.error("Error while listing the computers", se);
			throw new PersistenceException("SQL exception during listing of computers ", se);
		}

		return list;
	}

	/**
	 * Populate the item list of the page
	 *
	 * Available indexes of sort are the keys of COLUMN_ORDER
	 *
	 * @param page
	 */
	@Override
	public void populateItems(Page<Computer> page) throws PersistenceException {
		LOGGER.trace("Populating computers page : {}", page);
		Connection connect = DBConnection.INSTANCE.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.name LIKE ? OR m.name LIKE ? ORDER BY "
							+ COLUMN_ORDER.get(page.getOrder()) + " " + page.getDirection().name() + " LIMIT ?, ?");

			int beginIndex = (page.getIndex() - 1) * page.getSize();

			prepared.setString(1, "%" + page.getSearch() + "%");
			prepared.setString(2, "%" + page.getSearch() + "%");
			prepared.setInt(3, beginIndex);
			prepared.setInt(4, page.getSize());

			ResultSet rs = prepared.executeQuery();

			// Populate the temporary list then set it to the page
			List<Computer> list = new ArrayList<Computer>();
			while (rs.next()) {
				Computer computer = rowMapper.mapRow(rs);

				list.add(computer);
			}

			page.setItems(list);
			LOGGER.trace("Populated page : {}", page);

		} catch (SQLException se) {
			LOGGER.error("Error while populating the computers page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of computers ", se);
		}
	}

	@Override
	public List<Computer> findByName(String name) throws PersistenceException {
		LOGGER.trace("Searching computers with name : {}", name);
		Connection connect = DBConnection.INSTANCE.getConnection();

		List<Computer> list = new ArrayList<Computer>();
		try {
			PreparedStatement prepared = connect.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.name LIKE ?");
			prepared.setString(1, "%" + name + "%");

			ResultSet rs = prepared.executeQuery();

			while (rs.next()) {
				Computer computer = rowMapper.mapRow(rs);

				list.add(computer);
			}

		} catch (SQLException se) {
			LOGGER.error("Error while searching computers with name : " + name, se);
			throw new PersistenceException("SQL exception during Computer findByName : " + name, se);
		}

		return list;
	}

	private class ComputerRowMapper implements RowMapper<Computer> {

		@Override
		public Computer mapRow(ResultSet rs) throws SQLException {
			Computer computer = Computer.builder().id(rs.getLong(1)).name(rs.getString(2)).build();

			if (rs.getDate(3) != null) {
				computer.setIntroduced(rs.getDate(3).toLocalDate());
			}
			if (rs.getDate(4) != null) {
				computer.setDiscontinued(rs.getDate(4).toLocalDate());
			}

			Company company = null;
			if (rs.getLong(5) != 0) {
				company = Company.builder().id(rs.getLong(5)).name(rs.getString(6)).build();
			}
			computer.setCompany(company);

			return computer;
		}

	}
}
