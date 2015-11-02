package fr.jonathanlebloas.computerdatabase.integration;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class DashBoardITest {

	private static URI siteBase;
	private static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		siteBase = new URI("http://localhost:18080/computer-database/dashboard");
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		driver.get(siteBase.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDefaultSizeAndPage() {
		// Check the default number of computer is 10
		verifyCountComputerList(10);

		// Get the number of link in the pagination menu and verify the count
		verifyCountMenuLinks();
	}

	@Test
	public void testAddThenDeleteComputer() {
		int nb = parseComputerCount();

		WebElement addLink = driver.findElement(By.linkText("Add Computer"));
		addLink.click();

		WebElement computerName = driver.findElement(By.name("computerName"));
		WebElement introduced = driver.findElement(By.name("introduced"));
		WebElement discontinued = driver.findElement(By.name("discontinued"));
		WebElement companyId = driver.findElement(By.name("companyId"));

		// Write the different fields
		computerName.sendKeys("A computer IT test");
		introduced.sendKeys("2015-02-10");
		discontinued.sendKeys("2016-01-01");
		companyId.sendKeys("A computer IT test");
		new Select(companyId).selectByIndex(3);

		// Submit the form
		driver.findElement(By.tagName("form")).submit();

		// Verify the count of computers nows
		assertThat(parseComputerCount(), equalTo(nb + 1));
		// Also verify the number of menu links
		verifyCountMenuLinks();

		// Now Delete the computer we've just added
		nb = parseComputerCount();

		// Go to the last page to get the last added computer
		driver.findElement(By.linkText("»")).click();

		// Toggle edit mode
		driver.findElement(By.linkText("Edit")).click();

		// Get last computer line
		WebElement listComputers = driver.findElement(By.id("results"));
		List<WebElement> computers = listComputers.findElements(By.tagName("tr"));
		WebElement lastRow = computers.get(computers.size() - 1);

		// Check the computer that was added
		List<WebElement> fields = lastRow.findElements(By.tagName("td"));
		assertThat(fields.get(1).getText(), equalTo("A computer IT test"));
		assertThat(fields.get(2).getText(), equalTo("2015-02-10"));
		assertThat(fields.get(3).getText(), equalTo("2016-01-01"));
		assertThat(fields.get(4).getText(), equalTo("RCA"));

		// Click on the delete checkbox
		fields.get(0).findElement(By.name("cb")).click();

		// Submit the delete form
		driver.findElement(By.id("deleteSelected")).click();
		driver.switchTo().alert().accept();

		// Verify the count of computers nows
		assertThat(parseComputerCount(), equalTo(nb - 1));
		// Also verify the number of menu links
		verifyCountMenuLinks();
	}

	private void verifyCountComputerList(int expected) {
		WebElement listComputer = driver.findElement(By.id("results"));

		List<WebElement> computers = listComputer.findElements(By.tagName("tr"));

		assertThat(computers.size(), equalTo(expected));
	}

	private void verifyCountMenuLinks() {
		List<WebElement> pages = driver.findElement(By.className("pagination")).findElements(By.tagName("li"));

		assertThat(pages.size(), equalTo(11));
	}

	private int parseComputerCount() {
		WebElement title = driver.findElement(By.id("homeTitle"));
		int nb = Integer.parseInt(title.getText().split(" Computers found")[0]);

		return nb;
	}
}
