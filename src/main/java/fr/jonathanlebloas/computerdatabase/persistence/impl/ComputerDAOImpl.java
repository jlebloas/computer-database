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
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

/**
 * DAO used for computers Singleton
 */
@Repository
public class ComputerDAOImpl implements ComputerDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ComputerDAOImpl.class);

	/**
	 * Map a sort field to database column
	 */
	private static final Map<String, String> COLUMN_ORDER = new HashMap<>();

	static {
		COLUMN_ORDER.put("id", "c.id");
		COLUMN_ORDER.put("name", "c.name");
		COLUMN_ORDER.put("introduced", "c.introduced");
		COLUMN_ORDER.put("discontinued", "c.discontinued");
		COLUMN_ORDER.put("companyName", "m.name");
	}

	private ComputerRowMapper rowMapper = new ComputerRowMapper();

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public Computer find(long id) throws PersistenceException {
		LOGGER.trace("Finding computer with id: {}", id);

		try {
			final String sql = "SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.id = :id";

			SqlParameterSource namedParameters = new MapSqlParameterSource("id", id);

			return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, rowMapper);

		} catch (DataAccessException se) {
			LOGGER.error("Error while finding the computer with id : " + id, se);
			throw new PersistenceException("SQL exception during find by id : " + id, se);
		}
	}

	@Override
	public void create(Computer computer) throws PersistenceException {
		LOGGER.trace("Creating computer : {}", computer);

		try {
			final String sql = "INSERT INTO computer(name, introduced, discontinued, company_id) VALUES (:name, :introduced, :discontinued, :companyId)";

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("name", computer.getName());
			namedParameters.addValue("introduced", computer.getIntroduced());
			namedParameters.addValue("discontinued", computer.getDiscontinued());
			namedParameters.addValue("companyId", computer.getCompany() != null ? computer.getCompany().getId() : null);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedParameterJdbcTemplate.update(sql, namedParameters, keyHolder);

			computer.setId(keyHolder.getKey().longValue());

		} catch (DataAccessException se) {
			LOGGER.error("Error while creating the computer : " + computer, se);
			throw new PersistenceException("SQL exception during creation of a computer : " + computer.toString(), se);
		}
	}

	@Override
	public void update(Computer computer) throws PersistenceException {
		LOGGER.trace("Updating computer : {}", computer);

		try {
			final String sql = "UPDATE computer c SET c.name = :name, c.introduced = :introduced, c.discontinued = :discontinued, c.company_id = :companyId WHERE c.id = :id";

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("id", computer.getId());
			namedParameters.addValue("name", computer.getName());
			namedParameters.addValue("introduced", computer.getIntroduced());
			namedParameters.addValue("discontinued", computer.getDiscontinued());
			namedParameters.addValue("companyId", computer.getCompany() != null ? computer.getCompany().getId() : null);

			this.namedParameterJdbcTemplate.update(sql, namedParameters);

		} catch (DataAccessException se) {
			LOGGER.error("Error while updating the computer : " + computer, se);
			throw new PersistenceException("SQL exception during update of a computer : " + computer.toString(), se);
		}
	}

	@Override
	public void delete(Computer computer) throws PersistenceException {
		LOGGER.trace("Deleting computer : {}", computer);

		try {
			final String sql = "DELETE FROM computer WHERE id = :id";

			SqlParameterSource namedParameters = new MapSqlParameterSource("id", computer.getId());

			this.namedParameterJdbcTemplate.update(sql, namedParameters);

		} catch (DataAccessException se) {
			LOGGER.error("Error while deleting the computer : " + computer, se);
			throw new PersistenceException("SQL exception during deletion of a computer : " + computer.toString(), se);
		}
	}

	@Override
	public void deleteWithCompanyId(long id) throws PersistenceException {
		LOGGER.trace("Deleting computers with company id : {}", id);

		try {
			final String sql = "DELETE FROM computer WHERE company_id = :companyId";

			SqlParameterSource namedParameters = new MapSqlParameterSource("companyId", id);

			this.namedParameterJdbcTemplate.update(sql, namedParameters);

		} catch (DataAccessException se) {
			LOGGER.error("Error while deleting the computers with company id : " + id, se);
			throw new PersistenceException("SQL exception during deletion of a computers with company id : " + id, se);
		}
	}

	@Override
	public int count(String search) throws PersistenceException {
		LOGGER.trace("Counting computers");

		try {
			final String sql = "SELECT count(*) FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.name LIKE :search OR m.name LIKE :search";

			SqlParameterSource namedParameters = new MapSqlParameterSource("search", "%" + search + "%");

			return this.namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);

		} catch (DataAccessException se) {
			LOGGER.error("Error while counting the computers", se);
			throw new PersistenceException("SQL exception during count of computers ", se);
		}
	}

	@Override
	public List<Computer> list() throws PersistenceException {
		LOGGER.trace("Listing computers");

		try {
			final String sql = "SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id = m.id";

			return this.namedParameterJdbcTemplate.query(sql, rowMapper);

		} catch (DataAccessException se) {
			LOGGER.error("Error while listing the computers", se);
			throw new PersistenceException("SQL exception during listing of computers ", se);
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
	public void populateItems(Page<Computer> page) throws PersistenceException {
		LOGGER.trace("Populating computers page : {}", page);

		try {
			final StringBuilder sql = new StringBuilder(
					"SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id=m.id WHERE c.name LIKE :search OR m.name LIKE :search ORDER BY ");
			sql.append(COLUMN_ORDER.get(page.getSort().getField()));
			sql.append(" ");
			sql.append(page.getSort().getDirection().name());
			sql.append(" LIMIT :beginIndex, :size");

			int beginIndex = (page.getIndex() - 1) * page.getSize();

			MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("search", "%" + page.getSearch() + "%");
			namedParameters.addValue("beginIndex", beginIndex);
			namedParameters.addValue("size", page.getSize());

			List<Computer> items = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters, rowMapper);

			page.setItems(items);

			LOGGER.trace("Populated page : {}", page);

		} catch (DataAccessException se) {
			LOGGER.error("Error while populating the computers page : " + page, se);
			throw new PersistenceException("SQL exception during sublisting of computers ", se);
		}
	}

	@Override
	public List<Computer> findByName(String name) throws PersistenceException {
		LOGGER.trace("Searching computers with name : {}", name);

		try {
			final String sql = "SELECT c.id, c.name, c.introduced, c.discontinued, c.company_id, m.name FROM computer c LEFT JOIN company m ON c.company_id = m.id WHERE c.name LIKE :name";

			SqlParameterSource namedParameters = new MapSqlParameterSource("name", "%" + name + "%");

			List<Computer> list = this.namedParameterJdbcTemplate.query(sql.toString(), namedParameters, rowMapper);

			return list;

		} catch (DataAccessException se) {
			LOGGER.error("Error while searching computers with name : " + name, se);
			throw new PersistenceException("SQL exception during Computer findByName : " + name, se);
		}
	}

	private static final class ComputerRowMapper implements RowMapper<Computer> {
		@Override
		public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
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
