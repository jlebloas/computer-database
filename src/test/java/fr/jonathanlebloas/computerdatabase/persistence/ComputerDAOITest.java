package fr.jonathanlebloas.computerdatabase.persistence;

import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.persistence.impl.ComputerDAO;

public class ComputerDAOITest {

	private static ComputerDAO computerDAO = ComputerDAO.INSTANCE;

	private DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private static Runtime runtime = Runtime.getRuntime();

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
	public void testCount() throws PersistenceException {
		int nb = computerDAO.count();

		assertThat(nb, IsEqual.equalTo(10));
	}

	@Test
	public void testFind() throws PersistenceException {

		Computer c = Computer.builder().id(5).name("CM-5").introduced(LocalDate.parse("1991-01-01", df))
				.company(company2).build();

		Computer founded = computerDAO.find(5);
		assertThat(founded, IsEqual.equalTo(c));

		// Check if find return null if there's no computer
		Computer expectedNull = computerDAO.find(-1216461261261216555L);
		assertThat(expectedNull, IsNull.nullValue());
		expectedNull = computerDAO.find(Long.MAX_VALUE);
		assertThat(expectedNull, IsNull.nullValue());
		expectedNull = computerDAO.find(Long.MIN_VALUE);
		assertThat(expectedNull, IsNull.nullValue());
		expectedNull = computerDAO.find(0);
		assertThat(expectedNull, IsNull.nullValue());
	}

	@Test
	public void testCreate() throws PersistenceException {

		int nb = computerDAO.count();

		// Create 4 computers add them and check if they're in base
		Computer c1 = Computer.builder().name("A name").introduced(LocalDate.parse("1999-12-13", df)).build();
		Computer c2 = Computer.builder().name("A 2nd name").company(company1).build();
		Computer c3 = Computer.builder().name("A other name").company(company2).build();
		Computer c4 = Computer.builder().name("A other other name").introduced(LocalDate.parse("1999-12-13", df))
				.discontinued(LocalDate.parse("2015-12-15", df)).company(company2).build();
		computerDAO.create(c1);
		computerDAO.create(c2);
		computerDAO.create(c3);
		computerDAO.create(c4);

		assertThat(computerDAO.find(0), IsNull.nullValue());
		assertThat(computerDAO.find(c1.getId()), IsEqual.equalTo(c1));
		assertThat(computerDAO.find(c2.getId()), IsEqual.equalTo(c2));
		assertThat(computerDAO.find(c3.getId()), IsEqual.equalTo(c3));
		assertThat(computerDAO.find(c4.getId()), IsEqual.equalTo(c4));

		// Check the numbers of computers
		assertThat(computerDAO.count(), IsEqual.equalTo(nb + 4));
	}

	@Test
	public void testUpdate() throws PersistenceException {
		Computer c = computerDAO.find(5);
		c.setIntroduced(LocalDate.parse("1997-02-15", df));
		c.setDiscontinued(LocalDate.parse("1999-12-13", df));

		computerDAO.update(c);
		assertThat(computerDAO.find(c.getId()), IsEqual.equalTo(c));

		assertThat(computerDAO.find(c.getId()).getIntroduced(), IsEqual.equalTo(LocalDate.parse("1997-02-15", df)));
		assertThat(computerDAO.find(c.getId()).getDiscontinued(), IsEqual.equalTo(LocalDate.parse("1999-12-13", df)));
	}

	/**
	 * Test that an if an SQLException is raised, the exception is wrap into an
	 * PersistenceException.
	 *
	 * @throws PersistenceException
	 */
	@Test(expected = PersistenceException.class)
	public void testCreateWithException() throws PersistenceException {
		// A wrong date that should throw an Exception
		Computer c = Computer.builder().name("A name").company(Company.builder().id(0).name("A name").build()).build();
		computerDAO.create(c);
	}

	/**
	 * Test that an if an SQLException is raised, the exception is wrap into an
	 * PersistenceException.
	 *
	 * @throws PersistenceException
	 */
	@Test(expected = PersistenceException.class)
	public void testUpdateWithException() throws PersistenceException {
		// A wrong date that should throw an Exception
		Computer c = computerDAO.find(5);
		c.setDiscontinued(LocalDate.parse("1900-12-13", df));

		computerDAO.update(c);
	}

	@Test
	public void testDelete() throws PersistenceException {
		int nb = computerDAO.count();

		Computer c = computerDAO.find(6);
		computerDAO.delete(c);

		// Check the numbers of computers now
		assertThat(computerDAO.count(), IsEqual.equalTo(nb - 1));
		assertThat(computerDAO.find(6), IsNull.nullValue());
	}

	@Test
	public void testList() throws PersistenceException {
		int nb = computerDAO.count();

		List<Computer> expectedList = generateList();

		List<Computer> returnedList = computerDAO.list();

		assertThat(returnedList.size(), IsEqual.equalTo(nb));
		assertThat(returnedList, IsEqual.equalTo(expectedList));
	}

	@Test
	public void testPopulatePageOfComputer() throws PersistenceException {
		Page<Computer> page = new Page<>(1, 10);

		computerDAO.populateItems(page);
		List<Computer> expectedList = generateList();

		assertThat(page.getItems().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getItems(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testPopulatePageEmpty() throws PersistenceException {
		Page<Computer> page = new Page<>(50, 10);

		computerDAO.populateItems(page);

		assertThat(page.getItems().size(), IsEqual.equalTo(0));
	}

	@Test
	public void testFindByNameString() throws PersistenceException {
		assertThat(computerDAO.findByName("Apple").size(), IsEqual.equalTo(4));

		assertThat(computerDAO.findByName("CM-200").size(), IsEqual.equalTo(1));

		assertThat(computerDAO.findByName("CM-200").get(0), IsEqual.equalTo(computerDAO.find(3)));
	}

	/**
	 * Generate a list used representing the data
	 *
	 * @return
	 */
	private List<Computer> generateList() {
		List<Computer> temp = new ArrayList<Computer>();

		Computer c1 = Computer.builder().id(1).name("MacBook Pro 15.4 inch").company(company1).build();

		Computer c2 = Computer.builder().id(2).name("CM-2a").company(company2).build();

		Computer c3 = Computer.builder().id(3).name("CM-200").company(company2).build();

		Computer c4 = Computer.builder().id(4).name("CM-5e").company(company2).build();

		Computer c5 = Computer.builder().id(5).name("CM-5").introduced(LocalDate.parse("1991-01-01", df))
				.company(company2).build();

		Computer c6 = Computer.builder().id(6).name("MacBook Pro").introduced(LocalDate.parse("2006-01-10", df))
				.company(company1).build();

		Computer c7 = Computer.builder().id(7).name("Apple IIe").build();

		Computer c8 = Computer.builder().id(8).name("Apple IIc").build();

		Computer c9 = Computer.builder().id(9).name("Apple IIGS").build();

		Computer c10 = Computer.builder().id(10).name("Apple IIc Plus").build();

		temp.add(c1);
		temp.add(c2);
		temp.add(c3);
		temp.add(c4);
		temp.add(c5);
		temp.add(c6);
		temp.add(c7);
		temp.add(c8);
		temp.add(c9);
		temp.add(c10);

		return temp;
	}
}
