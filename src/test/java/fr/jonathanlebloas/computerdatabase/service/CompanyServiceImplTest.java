package fr.jonathanlebloas.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.persistence.impl.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.service.impl.CompanyServiceImpl;

/**
 * Test Company service Use PowerMock to mock the CompanyDAO
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompanyDAO.class })
public class CompanyServiceImplTest {
	private CompanyServiceImpl service = CompanyServiceImpl.INSTANCE;

	private CompanyDAO dao = Mockito.mock(CompanyDAO.class);

	@Before
	public void setUp() throws Exception {
		// Set the mocked DAO to the service
		Field field = CompanyServiceImpl.class.getDeclaredField("companyDAO");
		field.setAccessible(true);
		field.set(service, dao);
	}

	/**
	 * Test that the PersistenceException is wrapped into a ServiceException
	 *
	 * @throws ServiceException
	 * @throws PersistenceException
	 */
	@Test(expected = ServiceException.class)
	public void testListCompaniesServiceException() throws ServiceException, PersistenceException {
		doThrow(new PersistenceException()).when(dao).list();
		service.listCompanies();
	}

	/**
	 * Test that the PersistenceException is wrapped into a ServiceException
	 *
	 * @throws ServiceException
	 * @throws PersistenceException
	 */
	@Test(expected = ServiceException.class)
	public void testFindLongServiceException() throws ServiceException, PersistenceException, CompanyNotFoundException {
		doThrow(new PersistenceException()).when(dao).find(anyLong());
		service.find(42L);
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLongCompanyNotFoundException()
			throws PersistenceException, CompanyNotFoundException, ServiceException {
		doReturn(null).when(dao).find(0);
		service.find(0);
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLong() throws PersistenceException, CompanyNotFoundException, ServiceException {
		doReturn(null).when(dao).find(0);
		service.find(0);
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

	// TODO Refactor Page System
	@Ignore
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
