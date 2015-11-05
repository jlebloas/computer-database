package fr.jonathanlebloas.computerdatabase.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.persistence.RowMapper;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

/**
 * DAO used for companies Singleton
 */
@Repository
public class CompanyDAOImpl implements CompanyDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDAOImpl.class);

	public static final Map<String, String> COLUMN_ORDER = new HashMap<>();

	static {
		COLUMN_ORDER.put("id", "c.id");
		COLUMN_ORDER.put("name", "c.name");
	}

	private CompanyRowMapper rowMapper = new CompanyRowMapper();

	@Autowired
	private DataSource ds;

	@Override
	public Company find(long id) throws PersistenceException {
		LOGGER.trace("Finding company with id: {}", id);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("SELECT c.id, c.name FROM company c WHERE id=?");

			prepared.setLong(1, id);

			ResultSet rs = prepared.executeQuery();

			Company company = null;

			if (rs.first()) {
				company = rowMapper.mapRow(rs);
			}

			return company;

		} catch (SQLException se) {
			LOGGER.error("Error while finding the company with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);
		}
	}

	@Override
	public void create(Company company) throws PersistenceException {
		LOGGER.trace("Creating company : {}", company);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("INSERT INTO company(name) VALUES (?)",
					Statement.RETURN_GENERATED_KEYS);
			prepared.setString(1, company.getName());
			prepared.executeUpdate();

			ResultSet rs = prepared.getGeneratedKeys();
			if (rs.first()) {
				company.setId(rs.getLong(1));
			} else {
				throw new SQLException("The company was not inserted : " + company.toString());
			}

		} catch (SQLException se) {
			LOGGER.error("Error while creating the company : " + company, se);
			throw new PersistenceException("SQL exception during creation of company : " + company.toString(), se);
		}
	}

	@Override
	public Company update(Company obj) throws PersistenceException {
		LOGGER.trace("Updating company : {}", obj);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("UPDATE company c SET c.name = ? WHERE c.id = ?");
			prepared.setString(1, obj.getName());
			prepared.setLong(2, obj.getId());

			prepared.executeUpdate();

			return obj;
		} catch (SQLException se) {
			LOGGER.error("Error while updating the company : " + obj, se);
			throw new PersistenceException("SQL exception during update of company : " + obj.toString(), se);
		}
	}

	@Override
	public void delete(Company obj) throws PersistenceException {
		LOGGER.trace("Deleting company : {}", obj);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("DELETE FROM company WHERE id=?");
			prepared.setLong(1, obj.getId());

			prepared.executeUpdate();

		} catch (SQLException se) {
			LOGGER.error("Error while deleting the company : " + obj, se);
			throw new PersistenceException("SQL exception during deletion of company : " + obj.toString(), se);
		}
	}

	@Override
	public int count(String search) throws PersistenceException {
		LOGGER.trace("Counting companies");

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("SELECT count(*) FROM company c WHERE c.name LIKE ?");

			prepared.setString(1, "%" + search + "%");

			ResultSet result = prepared.executeQuery();

			int tmp = 0;

			if (result.first()) {
				tmp = result.getInt(1);
			}

			return tmp;

		} catch (SQLException se) {
			LOGGER.error("Error while counting the companies", se);
			throw new PersistenceException("SQL exception during count of companies", se);
		}
	}

	@Override
	public List<Company> list() throws PersistenceException {
		LOGGER.trace("Listing companies");

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect.prepareStatement("SELECT c.id, c.name FROM company c");

			ResultSet rs = prepared.executeQuery();

			List<Company> list = new ArrayList<Company>();

			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);
				list.add(company);
			}

			return list;

		} catch (SQLException se) {
			LOGGER.error("Error while listing the companies", se);
			throw new PersistenceException("SQL exception during listing of companies", se);
		}
	}

	/**
	 * Populate the item list of the page
	 *
	 * Available indexes of sort are the keys of COLUMN_ORDER
	 *
	 * @param page
	 */
	@Override
	public void populateItems(Page<Company> page) throws PersistenceException {
		LOGGER.trace("Populating companies page : {}", page);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect
					.prepareStatement("SELECT c.id, c.name FROM company c WHERE c.name LIKE ? ORDER BY "
							+ COLUMN_ORDER.get(page.getSort().getField()) + " " + page.getSort().getDirection().name()
							+ " LIMIT ?, ?");

			int beginIndex = (page.getIndex() - 1) * page.getSize();

			prepared.setString(1, "%" + page.getSearch() + "%");
			prepared.setInt(2, beginIndex);
			prepared.setInt(3, page.getSize());

			ResultSet rs = prepared.executeQuery();

			// Populate the temporary list then set it to the page
			List<Company> list = new ArrayList<Company>();
			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);
				list.add(company);
			}

			page.setItems(list);

			LOGGER.trace("Populated page : {}", page);

		} catch (SQLException se) {
			LOGGER.error("Error while populating the companies page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of companies", se);
		}
	}

	@Override
	public List<Company> findByName(String name) throws PersistenceException {
		LOGGER.trace("Searching companies with name : {}", name);

		try {
			Connection connect = DataSourceUtils.getConnection(ds);

			PreparedStatement prepared = connect
					.prepareStatement("SELECT c.id, c.name FROM company c WHERE c.name LIKE ?");
			prepared.setString(1, "%" + name + "%");

			ResultSet rs = prepared.executeQuery();

			List<Company> list = new ArrayList<Company>();

			while (rs.next()) {
				Company company = rowMapper.mapRow(rs);

				list.add(company);
			}

			return list;

		} catch (SQLException se) {
			LOGGER.error("Error while searching companies with name : " + name, se);
			throw new PersistenceException("SQL exception during a Company findByName : " + name, se);
		}
	}

	private class CompanyRowMapper implements RowMapper<Company> {
		@Override
		public Company mapRow(ResultSet rs) throws SQLException {
			Company company = Company.builder().id(rs.getLong(1)).name(rs.getString(2)).build();

			return company;
		}
	}

}
