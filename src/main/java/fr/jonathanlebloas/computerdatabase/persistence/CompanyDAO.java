package fr.jonathanlebloas.computerdatabase.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

/**
 * DAO used for companies Singleton
 */
public class CompanyDAO extends DAO<Company> {

	private static CompanyDAO instance = new CompanyDAO();

	private CompanyRowMapper rowMapper = new CompanyRowMapper();

	private static final Logger logger = LoggerFactory.getLogger(CompanyDAO.class);

	private CompanyDAO() {
		super();
	}

	/**
	 * @return the unique instance
	 */
	public static CompanyDAO getInstance() {
		return instance;
	}

	@Override
	public Company find(long id) throws PersistenceException {
		logger.trace("Finding company with id: {}", id);
		Connection connect = DBConnection.getConnection();

		Company company = null;
		try {
			PreparedStatement prepared = connect.prepareStatement("SELECT c.id, c.name FROM company c WHERE id=?");

			prepared.setLong(1, id);

			ResultSet rs = prepared.executeQuery();

			if (rs.first()) {
				company = rowMapper.mapRow(rs);
			}

		} catch (SQLException se) {
			logger.error("Error while finding the company with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return company;
	}

	@Override
	public Company create(Company obj) throws PersistenceException {
		logger.trace("Creating company : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("INSERT INTO company(name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);
			prepared.setString(1, obj.getName());
			prepared.executeUpdate();

			ResultSet rs = prepared.getGeneratedKeys();
			if (rs.first()) {
				obj.setId(rs.getLong(1));
			} else {
				throw new SQLException("The company was not inserted : " + obj.toString());
			}

		} catch (SQLException se) {
			logger.error("Error while creating the company : " + obj, se);
			throw new PersistenceException("SQL exception during creation of company : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return obj;
	}

	@Override
	public Company update(Company obj) throws PersistenceException {
		logger.trace("Updating company : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("UPDATE company SET name = ? WHERE id = ?");
			prepared.setString(1, obj.getName());
			prepared.setLong(2, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			logger.error("Error while updating the company : " + obj, se);
			throw new PersistenceException("SQL exception during update of company : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return obj;
	}

	@Override
	public void delete(Company obj) throws PersistenceException {
		logger.trace("Deleting company : {}", obj);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("DELETE FROM company WHERE id=?");
			prepared.setLong(1, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			logger.error("Error while deleting the company : " + obj, se);
			throw new PersistenceException("SQL exception during deletion of company : " + obj.toString(), se);

		} finally {
			DBConnection.closeConnection(connect);
		}

	}

	@Override
	public int count() throws PersistenceException {
		logger.trace("Counting companies");
		Connection connect = DBConnection.getConnection();

		int tmp = 0;
		try {
			Statement stmt = connect.createStatement();
			ResultSet result = stmt.executeQuery("SELECT count(*) FROM company");

			if (result.first()) {
				tmp = result.getInt(1);
			}

		} catch (SQLException se) {
			logger.error("Error while counting the companies", se);
			throw new PersistenceException("SQL exception during count of companies", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return tmp;
	}

	@Override
	public List<Company> list() throws PersistenceException {
		logger.trace("Listing companies");
		Connection connect = DBConnection.getConnection();

		List<Company> list = new ArrayList<Company>();
		try {
			PreparedStatement prepared = connect.prepareStatement("SELECT c.id, c.name FROM company c");

			ResultSet rs = prepared.executeQuery();

			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);
				list.add(company);
			}

		} catch (SQLException se) {
			logger.error("Error while listing the companies", se);
			throw new PersistenceException("SQL exception during listing of companies", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return list;
	}

	@Override
	public Page<Company> populate(Page<Company> page) throws PersistenceException {
		logger.trace("Populating companies page : {}", page);
		Connection connect = DBConnection.getConnection();

		try {
			PreparedStatement prepared = connect.prepareStatement("SELECT c.id, c.name FROM company c LIMIT ?,?");

			prepared.setInt(1, page.getBeginIndex());
			prepared.setInt(2, page.getNb());

			ResultSet rs = prepared.executeQuery();

			List<Company> list = new ArrayList<Company>();
			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);
				list.add(company);
			}

			page.setItems(list);

		} catch (SQLException se) {
			logger.error("Error while populating the companies page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of companies", se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return page;
	}

	@Override
	public List<Company> findByName(String name) throws PersistenceException {
		logger.trace("Searching companies with name : {}", name);
		Connection connect = DBConnection.getConnection();

		List<Company> list = new ArrayList<Company>();
		try {
			PreparedStatement prepared = connect
					.prepareStatement("SELECT c.id, c.name FROM company c WHERE name LIKE ?");
			prepared.setString(1, "%" + name + "%");

			ResultSet rs = prepared.executeQuery();

			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);

				list.add(company);
			}

		} catch (SQLException se) {
			logger.error("Error while searching companies with name : " + name, se);
			throw new PersistenceException("SQL exception during a Company findByName : " + name, se);

		} finally {
			DBConnection.closeConnection(connect);
		}

		return list;
	}

	private class CompanyRowMapper implements RowMapper<Company> {

		@Override
		public Company mapRow(ResultSet rs) throws SQLException {
			Company company = new Company();

			company.setId(rs.getLong(1));
			company.setName(rs.getString(2));

			return company;
		}

	}

}
