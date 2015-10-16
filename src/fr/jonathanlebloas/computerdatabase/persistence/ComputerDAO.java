package fr.jonathanlebloas.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

/**
 * DAO used for computers Singleton
 */
public class ComputerDAO extends DAO<Computer> {

	private static ComputerDAO instance = new ComputerDAO();

	private ComputerRowMapper rowMapper = new ComputerRowMapper();

	private static final Logger logger = LoggerFactory.getLogger(ComputerDAO.class);

	private ComputerDAO() {
		super();
	}

	/**
	 * @return the unique instance
	 */
	public static ComputerDAO getInstance() {
		return instance;
	}

	@Override
	public Computer find(long id) throws PersistenceException {
		logger.trace("Finding computer with id: {}", id);
		Connection connect = DBConnection.getConnection();

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
			logger.error("Error while finding the computer with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return computer;
	}

	@Override
	public Computer create(Computer obj) throws PersistenceException {
		logger.trace("Creating computer : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"INSERT INTO computer(name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			prepared.setString(1, obj.getName());

			// Set the introduced or null if needed
			if (obj.getIntroduced() == null) {
				prepared.setNull(2, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(2, new Timestamp(obj.getIntroduced().getTime()));
			}

			// Set the discontinued or null if needed
			if (obj.getDiscontinued() == null) {
				prepared.setNull(3, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(3, new Timestamp(obj.getDiscontinued().getTime()));
			}

			// Set the company_id foreign key on company
			if (obj.getManufacturer() == null) {
				prepared.setNull(4, java.sql.Types.BIGINT);
			} else {
				prepared.setLong(4, obj.getManufacturer().getId());
			}
			prepared.executeUpdate();

			ResultSet rs = prepared.getGeneratedKeys();
			if (rs.first()) {
				obj.setId(rs.getLong(1));
			} else {
				throw new SQLException("The computer was not inserted : " + obj.toString());
			}

		} catch (SQLException se) {
			logger.error("Error while creating the computer : " + obj, se);
			throw new PersistenceException("SQL exception during creation of a computer : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return obj;
	}

	@Override
	public Computer update(Computer obj) throws PersistenceException {
		logger.trace("Updating computer : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement(
					"UPDATE computer c SET c.name = ?, c.introduced = ?, c.discontinued = ?, c.company_id = ? WHERE c.id = ?");
			prepared.setString(1, obj.getName());

			// Set the introduced or null if needed
			if (obj.getIntroduced() == null) {
				prepared.setNull(2, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(2, new Timestamp(obj.getIntroduced().getTime()));
			}

			// Set the discontinued or null if needed
			if (obj.getDiscontinued() == null) {
				prepared.setNull(3, java.sql.Types.TIMESTAMP);
			} else {
				prepared.setTimestamp(3, new Timestamp(obj.getDiscontinued().getTime()));
			}

			// Set the company_id foreign key on company
			if (obj.getManufacturer() == null) {
				prepared.setNull(4, java.sql.Types.BIGINT);
			} else {
				prepared.setLong(4, obj.getManufacturer().getId());
			}

			prepared.setLong(5, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			logger.error("Error while updating the computer : " + obj, se);
			throw new PersistenceException("SQL exception during update of a computer : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return obj;
	}

	@Override
	public void delete(Computer obj) throws PersistenceException {
		logger.trace("Deleting computer : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("DELETE FROM computer WHERE id=?");
			prepared.setLong(1, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			logger.error("Error while deleting the computer : " + obj, se);
			throw new PersistenceException("SQL exception during deletion of a computer : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

	}

	@Override
	public int count() throws PersistenceException {
		logger.trace("Counting computers");
		Connection connect = DBConnection.getConnection();

		int tmp = 0;
		try {
			Statement stmt = connect.createStatement();
			ResultSet result = stmt.executeQuery("SELECT count(*) FROM computer");
			if (result.first()) {
				tmp = result.getInt(1);
			}

		} catch (SQLException se) {
			logger.error("Error while counting the computers", se);
			throw new PersistenceException("SQL exception during count of computers ", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return tmp;
	}

	@Override
	public List<Computer> list() throws PersistenceException {
		logger.trace("Listing computers");
		Connection connect = DBConnection.getConnection();

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
			logger.error("Error while listing the computers", se);
			throw new PersistenceException("SQL exception during listing of computers ", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return list;
	}

	@Override
	public void populate(Page<Computer> page) throws PersistenceException {
		logger.trace("Populating computers page : {}", page);
		Connection connect = DBConnection.getConnection();

		// A temporary list
		List<Computer> list = new ArrayList<Computer>();
		try {
			PreparedStatement prepared = connect.prepareStatement(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id LIMIT ?,?");

			prepared.setInt(1, page.getBeginIndex());
			prepared.setInt(2, page.getNb());

			ResultSet rs = prepared.executeQuery();

			// Populate the temporary list then set it to the page
			while (rs.next()) {
				Computer computer = rowMapper.mapRow(rs);

				list.add(computer);
			}

			page.setItems(list);

		} catch (SQLException se) {
			logger.error("Error while populating the computers page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of computers ", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

	}

	@Override
	public List<Computer> findByName(String name) throws PersistenceException {
		logger.trace("Searching computers with name : {}", name);
		Connection connect = DBConnection.getConnection();

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
			logger.error("Error while searching computers with name : " + name, se);
			throw new PersistenceException("SQL exception during Computer findByName : " + name, se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return list;
	}

	private class ComputerRowMapper implements RowMapper<Computer> {

		@Override
		public Computer mapRow(ResultSet rs) throws SQLException {
			Computer computer = new Computer();

			computer.setId(rs.getLong(1));
			computer.setName(rs.getString(2));
			computer.setIntroduced(rs.getDate(3));
			computer.setDiscontinued(rs.getDate(4));

			Company company = null;
			if (rs.getLong(5) != 0) {
				company = new Company(rs.getLong(5), rs.getString(6));
			}
			computer.setManufacturer(company);

			return computer;
		}

	}

}
