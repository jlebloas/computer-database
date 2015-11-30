package fr.jonathanlebloas.computerdatabase.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fr.jonathanlebloas.computerdatabase.model.Computer;
import fr.jonathanlebloas.computerdatabase.repository.ComputerDAO;
import fr.jonathanlebloas.computerdatabase.service.impl.ComputerServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class CompupterServiceImplTest {

	@InjectMocks
	private ComputerServiceImpl service;

	@Mock
	private ComputerDAO computerDAO;

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateIllegalException() {
		service.update(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindLong() {
		doReturn(false).when(computerDAO).exists(any());
		service.update(Computer.builder().name("test").id(0).build());
	}
}
