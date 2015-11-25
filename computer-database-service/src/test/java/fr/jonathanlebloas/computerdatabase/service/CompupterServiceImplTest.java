package fr.jonathanlebloas.computerdatabase.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Matchers.any;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

public class CompupterServiceImplTest {

	@InjectMocks
	private ComputerServiceImpl service;

	@Mock
	private ComputerDAO computerDAO;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		// Set the mocked DAO to the service
		Field field = ComputerServiceImpl.class.getDeclaredField("computerDAO");
		field.setAccessible(true);
		field.set(service, computerDAO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateIllegalException() {
		service.update(null);
	}

	@Test(expected = RuntimeException.class)
	public void testFindLong() {
		doReturn(false).when(computerDAO).exists(any());
		service.update(Computer.builder().name("test").id(0).build());
	}
}
