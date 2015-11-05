package fr.jonathanlebloas.computerdatabase.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsSame;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.jonathanlebloas.computerdatabase.model.Company;
import fr.jonathanlebloas.computerdatabase.model.Page;
import fr.jonathanlebloas.computerdatabase.sort.Sort.Direction;
import fr.jonathanlebloas.computerdatabase.persistence.exceptions.PersistenceException;
import fr.jonathanlebloas.computerdatabase.persistence.impl.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.EmptyNameException;
import fr.jonathanlebloas.computerdatabase.service.exceptions.ServiceException;
import fr.jonathanlebloas.computerdatabase.service.impl.CompanyServiceImpl;
import fr.jonathanlebloas.computerdatabase.sort.CompanySort;

/**
 * Test Company service Use PowerMock to mock the CompanyDAO
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompanyDAO.class })
@PowerMockIgnore("javax.management.*")
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

	/**
	 * Don't test if the page contains the items because the items are just set
	 * calling the DAO
	 *
	 * @throws PersistenceException
	 * @throws ServiceException
	 */
	@Test
	public void testPopulatePage() throws PersistenceException, ServiceException {
		doReturn(52).when(dao).count(any());
		PowerMockito.doNothing().when(dao).populateItems(any());

		Page<Company> page = new Page<>(6, 10, "", CompanySort.getSort(1, Direction.ASC));
		service.populatePage(page);

		assertEquals(6, page.getIndex());
		assertEquals(10, page.getSize());
		assertEquals(52, page.getNbTotalElement());
		assertEquals(6, page.getNbTotalPages());

		page = new Page<>(5, 100, "", CompanySort.getSort(1, Direction.ASC));
		service.populatePage(page);

		assertEquals(5, page.getIndex());
		assertEquals(100, page.getSize());
		assertEquals(52, page.getNbTotalElement());
		assertEquals(1, page.getNbTotalPages());

	}

	@Test
	public void testPopulateWrongPage() throws PersistenceException, ServiceException {
		doReturn(52).when(dao).count(any());
		PowerMockito.doNothing().when(dao).populateItems(any());

		Page<Company> page = new Page<>(0, -12, "", CompanySort.getSort(1, Direction.ASC));
		service.populatePage(page);

		assertEquals(1, page.getIndex());
		assertEquals(10, page.getSize());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateIllegalOrder() throws PersistenceException, ServiceException {
		Page<Company> page = new Page<>(1, 10, "", CompanySort.getSort(3, Direction.ASC));
		service.populatePage(page);
	}
}
