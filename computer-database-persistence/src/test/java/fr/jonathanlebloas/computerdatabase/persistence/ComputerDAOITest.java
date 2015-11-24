package fr.jonathanlebloas.computerdatabase.persistence;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-applicationContext.xml")
@Transactional
public class ComputerDAOITest {

	private static Runtime runtime = Runtime.getRuntime();

	@Autowired
	private ComputerDAO computerDAO;

	private DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	Company company1;
	Company company2;

	@Before
	public void setUp() throws Exception {
		// Clear the whole test db for every tests
		try {
			// Run a script sh that re-setup the test db and synchronize the
			// process
			runtime.exec("src/test/resources/config/db/script.sh").waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		company1 = Company.builder().id(1).name("Apple Inc.").build();
		company2 = Company.builder().id(2).name("Thinking Machines").build();
	}

	@Test
	public void testFind() {

		Computer c = Computer.builder().id(5).name("CM-5").introduced(LocalDate.parse("1991-01-01", df))
				.company(company2).build();

		Computer founded = computerDAO.findOne(5L);
		assertThat(founded, IsEqual.equalTo(c));
	}

	@Test
	public void testFindEmpty() {
		assertThat(computerDAO.findOne(-1216461261261216555L), IsNull.nullValue());
	}

	@Test
	public void testCreate() {

		long nb = computerDAO.count();

		// Create 4 computers add them and check if they're in base
		Computer c1 = Computer.builder().name("A name").introduced(LocalDate.parse("1999-12-13", df)).build();
		Computer c2 = Computer.builder().name("A 2nd name").company(company1).build();
		Computer c3 = Computer.builder().name("A other name").company(company2).build();
		Computer c4 = Computer.builder().name("A other other name").introduced(LocalDate.parse("1999-12-13", df))
				.discontinued(LocalDate.parse("2015-12-15", df)).company(company2).build();
		computerDAO.save(c1);
		computerDAO.save(c2);
		computerDAO.save(c3);
		computerDAO.save(c4);

		assertThat(computerDAO.findOne(c1.getId()), IsEqual.equalTo(c1));
		assertThat(computerDAO.findOne(c2.getId()), IsEqual.equalTo(c2));
		assertThat(computerDAO.findOne(c3.getId()), IsEqual.equalTo(c3));
		assertThat(computerDAO.findOne(c4.getId()), IsEqual.equalTo(c4));

		// Check the numbers of computers
		assertThat(computerDAO.count(), IsEqual.equalTo(nb + 4));
	}

	@Test
	public void testUpdate() {
		Computer c = computerDAO.findOne(5L);
		c.setIntroduced(LocalDate.parse("1997-02-15", df));
		c.setDiscontinued(LocalDate.parse("1999-12-13", df));

		computerDAO.save(c);

		assertThat(computerDAO.findOne(c.getId()), IsEqual.equalTo(c));

		assertThat(computerDAO.findOne(c.getId()).getIntroduced(), IsEqual.equalTo(LocalDate.parse("1997-02-15", df)));
		assertThat(computerDAO.findOne(c.getId()).getDiscontinued(),
				IsEqual.equalTo(LocalDate.parse("1999-12-13", df)));
	}

	/**
	 * Test that an if an SQLException is raised, the exception is wrap into an
	 * PersistenceException.
	 *
	 * @throws PersistenceException
	 */
	@Test(expected = DataAccessException.class)
	public void testCreateWithException() {
		// A wrong date that should throw an Exception
		Computer c = Computer.builder().name("A name").company(Company.builder().id(0).name("A name").build()).build();
		computerDAO.save(c);
		computerDAO.flush();
	}

	/**
	 * Test that an if an SQLException is raised, the exception is wrap into an
	 * PersistenceException.
	 *
	 * @throws PersistenceException
	 */
	@Test(expected = DataAccessException.class)
	public void testUpdateWithException() {
		// A wrong date that should throw an Exception
		Computer c = computerDAO.findOne(5L);
		c.setDiscontinued(LocalDate.parse("0000-12-13", df));

		computerDAO.save(c);
		computerDAO.flush();
	}

	@Test
	public void testDelete() {
		long nb = computerDAO.count();

		Computer c = computerDAO.findOne(6L);
		computerDAO.delete(c);

		// Check the numbers of computers now
		assertThat(computerDAO.count(), IsEqual.equalTo(nb - 1));
		assertThat(computerDAO.findOne(6L), IsNull.nullValue());
	}

	@Test
	public void testDeleteWithCompanyId() {
		long nb = computerDAO.count();

		computerDAO.removeByCompany(Company.builder().id(2).name("Thinking Machines").build());

		// Check the numbers of computers now
		assertThat(computerDAO.count(), IsEqual.equalTo(nb - 4));
	}

	@Test
	public void testfindAll() {
		int nb = (int) computerDAO.count();

		List<Computer> expectedList = generateList();

		List<Computer> returnedList = computerDAO.findAll();

		assertThat(returnedList.size(), IsEqual.equalTo(nb));
		assertThat(returnedList, IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPage() {
		PageRequest pageRequest = new PageRequest(0, 10);

		Page<Computer> page = computerDAO.findAll(pageRequest);
		List<Computer> expectedList = generateList();

		assertThat(page.getContent().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getContent(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPageWithSearch() {
		PageRequest pageRequest = new PageRequest(0, 10);
		Page<Computer> page = computerDAO.findByNameContainingOrCompany_NameContaining(pageRequest, "CM", "CM");

		// "CM something" computers only are expected (c2, c3, c4, c5)
		List<Computer> expectedList = generateSubList();

		assertThat(page.getContent().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getContent(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPageWithSearchAndSortByNameAsc() {
		PageRequest pageRequest = new PageRequest(0, 10, new Sort(Direction.ASC, "name"));
		Page<Computer> page = computerDAO.findByNameContainingOrCompany_NameContaining(pageRequest, "CM", "CM");

		List<Computer> expectedList = generateSubList();

		expectedList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

		assertThat(page.getContent().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getContent(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPageWithSearchAndSortByNameDesc() {
		PageRequest pageRequest = new PageRequest(0, 10, new Sort(Direction.DESC, "name"));
		Page<Computer> page = computerDAO.findByNameContainingOrCompany_NameContaining(pageRequest, "CM", "CM");

		List<Computer> expectedList = generateSubList();

		expectedList.sort((c1, c2) -> c2.getName().compareTo(c1.getName()));

		assertThat(page.getContent().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getContent(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPageWithSearchAndLimit() {
		PageRequest pageRequest = new PageRequest(1, 2);
		Page<Computer> page = computerDAO.findByNameContainingOrCompany_NameContaining(pageRequest, "CM", "CM");

		// c4 and c5 only are expected
		List<Computer> expectedList = generateSubSubList();

		assertThat(page.getContent().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getContent(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testGetPageEmpty() {
		PageRequest pageRequest = new PageRequest(49, 10);
		Page<Computer> page = computerDAO.findByNameContainingOrCompany_NameContaining(pageRequest, "", "");

		assertThat(page.getContent().size(), IsEqual.equalTo(0));
	}

	/**
	 * Generate a list used representing the data
	 *
	 * @return
	 */
	private List<Computer> generateList() {
		List<Computer> temp = new ArrayList<Computer>();

		Computer c1 = Computer.builder().id(1).name("MacBook Pro 15.4 inch").company(company1).build();

		List<Computer> subList = generateSubList();

		Computer c6 = Computer.builder().id(6).name("MacBook Pro").introduced(LocalDate.parse("2006-01-10", df))
				.company(company1).build();

		Computer c7 = Computer.builder().id(7).name("Apple IIe").build();

		Computer c8 = Computer.builder().id(8).name("Apple IIc").build();

		Computer c9 = Computer.builder().id(9).name("Apple IIGS").build();

		Computer c10 = Computer.builder().id(10).name("Apple IIc Plus").build();

		temp.add(c1);
		for (Computer computer : subList) {
			temp.add(computer);
		}
		temp.add(c6);
		temp.add(c7);
		temp.add(c8);
		temp.add(c9);
		temp.add(c10);

		return temp;
	}

	/**
	 * Generate a list used representing the data with CM in their name for
	 * search purpose
	 *
	 * @return
	 */
	private List<Computer> generateSubList() {
		List<Computer> temp = new ArrayList<Computer>();

		Computer c2 = Computer.builder().id(2).name("CM-2a").company(company2).build();

		Computer c3 = Computer.builder().id(3).name("CM-200").company(company2).build();

		List<Computer> subsubList = generateSubSubList();

		temp.add(c2);
		temp.add(c3);
		for (Computer computer : subsubList) {
			temp.add(computer);
		}

		return temp;
	}

	/**
	 * Generate a list used representing the data with CM in their name for
	 * search purpose
	 *
	 * @return
	 */
	private List<Computer> generateSubSubList() {
		List<Computer> temp = new ArrayList<Computer>();

		Computer c4 = Computer.builder().id(4).name("CM-5e").company(company2).build();

		Computer c5 = Computer.builder().id(5).name("CM-5").introduced(LocalDate.parse("1991-01-01", df))
				.company(company2).build();

		temp.add(c4);
		temp.add(c5);

		return temp;
	}
}
