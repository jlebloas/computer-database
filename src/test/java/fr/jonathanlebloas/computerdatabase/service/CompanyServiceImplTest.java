package fr.jonathanlebloas.computerdatabase.service;

import static org.mockito.Mockito.doReturn;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.jonathanlebloas.computerdatabase.repository.CompanyDAO;
import fr.jonathanlebloas.computerdatabase.service.exceptions.CompanyNotFoundException;
import fr.jonathanlebloas.computerdatabase.service.impl.CompanyServiceImpl;

public class CompanyServiceImplTest {

	@InjectMocks
	private CompanyServiceImpl service;

	@Mock
	private CompanyDAO companyDAO;

	@Mock
	private CompanyDAO computerDAO;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		// Set the mocked DAO to the service
		Field field = CompanyServiceImpl.class.getDeclaredField("companyDAO");
		field.setAccessible(true);
		field.set(service, companyDAO);
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLongCompanyNotFoundException() {
		doReturn(null).when(companyDAO).findOne(0L);
		service.find(0);
	}

	@Test(expected = CompanyNotFoundException.class)
	public void testFindLong() {
		doReturn(null).when(companyDAO).findOne(0L);
		service.find(0);
	}
}
