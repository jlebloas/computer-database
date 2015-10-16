package fr.jonathanlebloas.computerdatabase.persistence;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.jonathanlebloas.computerdatabase.ITest;
import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.connection.DBConnectionITest;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;

@Category(ITest.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DBConnection.class })
public class ComputerDAOITest {
	private ComputerDAO computerDAO;
	private CompanyDAO companyDAO;
	private DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;

	private static Runtime runtime = Runtime.getRuntime();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		// Clear the whole test db before leaving
		// try {
		// runtime.exec("src/test/resources/config/db/script.sh");
		// } catch (IOException ioe) {
		// ioe.printStackTrace();
		// }
	}

	@Before
	public void setUp() throws Exception {
		// Clear the whole test db for every tests
		try {
			// Run scriptsh that re-stup the test db and synchronize the process
			runtime.exec("src/test/resources/config/db/script.sh").waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// Mock the db connection, get a connection on the test database
		PowerMockito.mockStatic(DBConnection.class);
		when(DBConnection.getConnection()).thenReturn(DBConnectionITest.getConnection());

		// Set the tested DAO
		computerDAO = ComputerDAO.getInstance();

		companyDAO = CompanyDAO.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		computerDAO = null;
	}

	@Test
	public void testCount() throws SQLException, PersistenceException {
		int nb = computerDAO.count();

		assertThat(nb, IsEqual.equalTo(10));
	}

	@Test
	public void testFind() throws SQLException, PersistenceException {

		Computer c = new Computer(5, "CM-5", LocalDate.parse("1991-01-01", df), null,
				companyDAO.find(2));
		Computer founded = computerDAO.find(5);
		assertThat(founded, IsEqual.equalTo(c));

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

		assertThat(nb, IsEqual.equalTo(10));

		// Create 5 computers
		Computer c1 = new Computer();
		Computer c2 = new Computer("A name", LocalDate.parse("1999-12-13", df), null, null);
		Computer c3 = new Computer("A 2nd name", null, null, companyDAO.find(1));
		Computer c4 = new Computer("A other name", null, null, companyDAO.find(2));
		Computer c5 = new Computer("A 5th name", LocalDate.parse("1999-12-13", df), LocalDate.parse("2015-12-15", df),
				companyDAO.find(2));
		computerDAO.create(c1);
		computerDAO.create(c2);
		computerDAO.create(c3);
		computerDAO.create(c4);
		computerDAO.create(c5);

		assertThat(computerDAO.find(c1.getId()), IsEqual.equalTo(c1));
		assertThat(computerDAO.find(c2.getId()), IsEqual.equalTo(c2));
		assertThat(computerDAO.find(c3.getId()), IsEqual.equalTo(c3));
		assertThat(computerDAO.find(c4.getId()), IsEqual.equalTo(c4));
		assertThat(computerDAO.find(c5.getId()), IsEqual.equalTo(c5));

		// Check the numbers of computers
		nb = computerDAO.count();
		assertThat(nb, IsEqual.equalTo(15));
	}

	@Test(expected = PersistenceException.class)
	public void testCreateWithException() throws PersistenceException {
		// A wrong date that should throw an Exception
		Computer c = new Computer("A name", LocalDate.parse("1969-12-13", df), null, companyDAO.find(2));
		computerDAO.create(c);
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

		assertThat(nb, IsEqual.equalTo(10));

		Computer c = computerDAO.find(6);
		computerDAO.delete(c);

		// Check the numbers of computers now
		nb = computerDAO.count();
		assertThat(nb, IsEqual.equalTo(9));
	}

	@Test
	public void testList() throws PersistenceException {
		int nb = computerDAO.count();

		Computer c1 = new Computer(1, "MacBook Pro 15.4 inch", null, null, companyDAO.find(1));
		Computer c2 = new Computer(2, "CM-2a", null, null, companyDAO.find(2));
		Computer c3 = new Computer(3, "CM-200", null, null, companyDAO.find(2));
		Computer c4 = new Computer(4, "CM-5e", null, null, companyDAO.find(2));
		Computer c5 = new Computer(5, "CM-5", LocalDate.parse("1991-01-01", df), null, companyDAO.find(2));
		Computer c6 = new Computer(6, "MacBook Pro", LocalDate.parse("2006-01-10", df), null, companyDAO.find(1));
		Computer c7 = new Computer(7, "Apple IIe", null, null, null);
		Computer c8 = new Computer(8, "Apple IIc", null, null, null);
		Computer c9 = new Computer(9, "Apple IIGS", null, null, null);
		Computer c10 = new Computer(10, "Apple IIc Plus", null, null, null);

		// Create 5 computers
		List<Computer> expectedList = new ArrayList<Computer>();
		expectedList.add(c1);
		expectedList.add(c2);
		expectedList.add(c3);
		expectedList.add(c4);
		expectedList.add(c5);
		expectedList.add(c6);
		expectedList.add(c7);
		expectedList.add(c8);
		expectedList.add(c9);
		expectedList.add(c10);

		List<Computer> returnedList = computerDAO.list();

		assertThat(returnedList.size(), IsEqual.equalTo(nb));
		assertThat(returnedList.size(), IsEqual.equalTo(expectedList.size()));
		assertThat(returnedList, IsEqual.equalTo(expectedList));
	}

	@Test
	public void testPopulatePageOfComputer() throws PersistenceException {
		Page<Computer> page = new Page<>(0, 10);

		computerDAO.populate(page);
		List<Computer> expectedList = generateList();

		assertThat(page.getItems().size(), IsEqual.equalTo(expectedList.size()));
		assertThat(page.getItems(), IsEqual.equalTo(expectedList));
	}

	@Test
	public void testPopulatePageEmpty() throws PersistenceException {
		Page<Computer> page = new Page<>(50, 10);

		computerDAO.populate(page);

		assertThat(page.getItems().size(), IsEqual.equalTo(0));
	}

	@Test
	public void testFindByNameString() throws PersistenceException {
		assertThat(computerDAO.findByName("Apple").size(), IsEqual.equalTo(4));

		assertThat(computerDAO.findByName("CM-200").size(), IsEqual.equalTo(1));

		assertThat(computerDAO.findByName("CM-200").get(0), IsEqual.equalTo(computerDAO.find(3)));
	}

	private List<Computer> generateList() throws PersistenceException {
		List<Computer> temp = new ArrayList<Computer>();

		Computer c1 = new Computer(1, "MacBook Pro 15.4 inch", null, null, companyDAO.find(1));
		Computer c2 = new Computer(2, "CM-2a", null, null, companyDAO.find(2));
		Computer c3 = new Computer(3, "CM-200", null, null, companyDAO.find(2));
		Computer c4 = new Computer(4, "CM-5e", null, null, companyDAO.find(2));
		Computer c5 = new Computer(5, "CM-5", LocalDate.parse("1991-01-01", df), null, companyDAO.find(2));
		Computer c6 = new Computer(6, "MacBook Pro", LocalDate.parse("2006-01-10", df), null, companyDAO.find(1));
		Computer c7 = new Computer(7, "Apple IIe", null, null, null);
		Computer c8 = new Computer(8, "Apple IIc", null, null, null);
		Computer c9 = new Computer(9, "Apple IIGS", null, null, null);
		Computer c10 = new Computer(10, "Apple IIc Plus", null, null, null);

		// Create 5 computers
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
