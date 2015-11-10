package fr.jonathanlebloas.computerdatabase.persistence.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.CompanyDAO;
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

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Company find(long id) throws PersistenceException {
		LOGGER.trace("Finding company with id: {}", id);

		try {
			final String sql = "SELECT c.id, c.name FROM company c WHERE id=:id";

			SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

			return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, rowMapper);

		} catch (DataAccessException se) {
			LOGGER.error("Error while finding the company with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);
		}
	}

	@Override
	public void create(Company company) throws PersistenceException {
		LOGGER.trace("Creating company : {}", company);

		try {
			final String sql = "INSERT INTO company (name) VALUES (:name)";

			SqlParameterSource namedParameters = new MapSqlParameterSource("name", company.getName());

			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);
			company.setId(keyHolder.getKey().longValue());

		} catch (DataAccessException se) {
			LOGGER.error("Error while creating the company : " + company, se);
			throw new PersistenceException("SQL exception during creation of company : " + company.toString(), se);
		}
	}

	@Override
	public void update(Company company) throws PersistenceException {
		LOGGER.trace("Updating company : {}", company);

		try {
			final String sql = "UPDATE company c SET c.name = :name WHERE c.id = :id";

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", company.getId());
			namedParameters.addValue("name", company.getName());

			this.namedParameterJdbcTemplate.update(sql, namedParameters);

		} catch (DataAccessException se) {
			LOGGER.error("Error while updating the company : " + company, se);
			throw new PersistenceException("SQL exception during update of company : " + company.toString(), se);
		}
	}

	@Override
	public void delete(Company company) throws PersistenceException {
		LOGGER.trace("Deleting company : {}", company);

		try {
			final String sql = "DELETE FROM company WHERE id = :id";

			SqlParameterSource namedParameters = new MapSqlParameterSource("id", company.getId());

			this.namedParameterJdbcTemplate.update(sql, namedParameters);

		} catch (DataAccessException se) {
			LOGGER.error("Error while deleting the company : " + company, se);
			throw new PersistenceException("SQL exception during deletion of company : " + company.toString(), se);
		}
	}

	@Override
	public int count(String search) throws PersistenceException {
		LOGGER.trace("Counting companies");

		try {
			final String sql = "SELECT count(*) FROM company c WHERE c.name LIKE :search";

			SqlParameterSource namedParameters = new MapSqlParameterSource("search", "%" + search + "%");

			return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);

		} catch (DataAccessException se) {
			LOGGER.error("Error while counting the companies", se);
			throw new PersistenceException("SQL exception during count of companies", se);
		}
	}

	@Override
	public List<Company> list() throws PersistenceException {
		LOGGER.trace("Listing companies");

		try {
			final String sql = "SELECT c.id, c.name FROM company c";

			return this.namedParameterJdbcTemplate.query(sql, rowMapper);

		} catch (DataAccessException se) {
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
			final StringBuilder sql = new StringBuilder("SELECT c.id, c.name FROM company c WHERE c.name LIKE :search ORDER BY ");
			sql.append(COLUMN_ORDER.get(page.getSort().getField()));
			sql.append(" ");
			sql.append(page.getSort().getDirection().name());
			sql.append(" LIMIT :beginIndex, :size");

			int beginIndex = (page.getIndex() - 1) * page.getSize();

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("search", "%" + page.getSearch() + "%");
			namedParameters.addValue("beginIndex", beginIndex);
			namedParameters.addValue("size", page.getSize());

			List<Company> items = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters, rowMapper);

			page.setItems(items);

			LOGGER.trace("Populated page : {}", page);

		} catch (DataAccessException se) {
			LOGGER.error("Error while populating the companies page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of companies", se);
		}
	}

	@Override
	public List<Company> findByName(String name) throws PersistenceException {
		LOGGER.trace("Searching companies with name : {}", name);

		try {
			final String sql = "SELECT c.id, c.name FROM company c WHERE c.name LIKE :name";

			SqlParameterSource namedParameters = new MapSqlParameterSource("name", "%" + name + "%");

			List<Company> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters, rowMapper);

			return list;

		} catch (DataAccessException se) {
			LOGGER.error("Error while searching companies with name : " + name, se);
			throw new PersistenceException("SQL exception during a Company findByName : " + name, se);
		}
	}

	private static final class CompanyRowMapper implements RowMapper<Company> {
		@Override
		public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
			Company company = Company.builder().id(rs.getLong("id")).name(rs.getString("name")).build();
			return company;
		}
	}
}
