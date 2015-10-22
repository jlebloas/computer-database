package fr.jonathanlebloas.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;

public class CompanyServiceImplTest {
	private static CompanyServiceImpl service;
	private CompanyDAO dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		service = CompanyServiceImpl.getInstance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		service = null;
	}

	@Before
	public void setUp() throws Exception {
		dao = mock(CompanyDAO.class);
		service.setCompanyDAO(dao);
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
	}

	@Test
	public void testGetIntance() {
		assertThat(service, IsSame.sameInstance(CompanyServiceImpl.getInstance()));
	}

	@Test(expected = ServiceException.class)
	public void testListCompaniesServiceException() throws ServiceException, PersistenceException {
		doThrow(new PersistenceException()).when(dao).list();
		service.listCompanies();
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLongCompanyNotFoundException()
			throws PersistenceException, CompanyNotFoundException, ServiceException {
		doReturn(null).when(dao).find(0);
		service.find(0);
	}

	@Test(expected = ServiceException.class)
	public void testFindLongServiceException() throws ServiceException, PersistenceException, CompanyNotFoundException {
		doThrow(new PersistenceException()).when(dao).find(anyLong());
		service.find(42L);
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLong() throws PersistenceException, CompanyNotFoundException, ServiceException {
		Company company = new Company();
		doReturn(new Company()).when(dao).find(0);
		assertThat(company, IsSame.sameInstance(service.find(42L)));
	}

	@Test(expected = EmptyNameException.class)
	public void testFindStringEmptyNameException() throws EmptyNameException, ServiceException, PersistenceException {
		service.find("");
	}

	@Test
	public void testFindString() throws PersistenceException, EmptyNameException, ServiceException {
		List<Company> list = new ArrayList<>();
		doReturn(list).when(dao).findByName(anyString());
		assertThat(list, IsSame.sameInstance(service.find("aPartOfName")));
	}

	@Test
	public void testGetNbPages() throws PersistenceException, ServiceException {
		assertThat(0, IsEqual.equalTo(service.getNbPages(0)));

		doReturn(0).when(dao).count();
		assertThat(0, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(10).when(dao).count();
		assertThat(1, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(11).when(dao).count();
		assertThat(2, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(39).when(dao).count();
		assertThat(4, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(40).when(dao).count();
		assertThat(4, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(41).when(dao).count();
		assertThat(5, IsEqual.equalTo(service.getNbPages(10)));

		doReturn(41).when(dao).count();
		assertThat(2, IsEqual.equalTo(service.getNbPages(40)));

		doReturn(41).when(dao).count();
		assertThat(9, IsEqual.equalTo(service.getNbPages(5)));
	}

	@Test
	public void testGetPage() throws PersistenceException, ServiceException {
		doReturn(52).when(dao).count();
		doReturn(null).when(dao).populate(any());

		Page<Company> page = service.getPage(6, 10);

		assertEquals(50, page.getBeginIndex());
		assertEquals(10, page.getNb());
	}

	@Test
	public void testGetPageEmpty() throws PersistenceException, ServiceException {
		doReturn(52).when(dao).count();

		assertTrue(service.getPage(-1, 10).getItems().isEmpty());
		assertTrue(service.getPage(7, 10).getItems().isEmpty());
		assertTrue(service.getPage(42, 10).getItems().isEmpty());
	}

}
