package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private final static String firstName = "Jane";
	private final static String lastName = "Doe";
	private final static String userName = "Janedoe";
	private final static String password = "password";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();

	}

	@BeforeEach
	public void beforeEach() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--no-sandbox");
		options.addArguments("--headless");

		this.driver = new ChromeDriver(options);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// Signup and login flow tests
	@Test
	public void cannotAccessHomePageWhileNotLoggedIn() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	// This could technically use the helper functions, but they would circumvent some button interactions that I want
	// to test.
	@Test
	public void userSignUpAndLogIn() {
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait wait = new WebDriverWait(driver, 3);

		// Go to sign up page
		WebElement signUpButton = driver.findElement(By.id("signup-link"));

		signUpButton.click();
		wait.until(ExpectedConditions.titleContains("Sign Up"));

		Assertions.assertEquals("Sign Up", driver.getTitle());

		// Signing up
		WebElement firstNameInput = driver.findElement(By.id("inputFirstName"));
		WebElement lastNameInput = driver.findElement(By.id("inputLastName"));
		WebElement userNameInput = driver.findElement(By.id("inputUsername"));
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));

		firstNameInput.sendKeys(firstName);
		lastNameInput.sendKeys(lastName);
		userNameInput.sendKeys(userName);
		passwordInput.sendKeys(password);
		passwordInput.submit();

		// Return to Log In page
		WebElement backToLoginButton = driver.findElement(By.id("login-link"));

		backToLoginButton.click();
		//wait.until(ExpectedConditions.titleContains("Login"));

		Assertions.assertEquals("Login", driver.getTitle());

		// Logging in
		WebElement usernameLogin = driver.findElement(By.id("inputUsername"));
		WebElement passwordLogin = driver.findElement(By.id("inputPassword"));

		usernameLogin.sendKeys(userName);
		passwordLogin.sendKeys(password);
		passwordLogin.submit();
		wait.until(ExpectedConditions.titleContains("Home"));

		Assertions.assertEquals("Home", driver.getTitle());

		// Logging out
		WebElement logout = driver.findElement(By.id("logout"));

		logout.click();
		wait.until(ExpectedConditions.titleContains("Login"));

		Assertions.assertEquals("Login", driver.getTitle());

		// Verify home page is no longer accessible after log out
		cannotAccessHomePageWhileNotLoggedIn();
	}


	// Adding, editing, and deleting notes tests
	@Test
	public void notesTests() {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		signUp();
		signIn();
		driver.get("http://localhost:" + this.port + "/home");

		switchToNotesTab();

		WebElement addNoteButton = driver.findElement(By.id("add-note"));
		addNoteButton.click();

		String noteTitle = "Note";
		String noteDescription = "Hello World";

		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		WebElement noteDescriptionInput = driver.findElement(By.id("note-description"));

		wait.until(ExpectedConditions.visibilityOf(noteTitleInput));

		noteTitleInput.sendKeys(noteTitle);
		noteDescriptionInput.sendKeys(noteDescription);
		noteDescriptionInput.submit();

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		// Check to make sure it saved and now renders the new note
		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();
		Assertions.assertEquals(noteTitle, driver.findElement(By.xpath("//tbody/tr/th")).getText());
		Assertions.assertEquals(noteDescription, driver.findElement(By.xpath("//td[2]")).getText());
	}

	// Adding, editing, and deleting credentials tests


	// Helper Functions
	private void signUp() {
		driver.get("http://localhost:" + this.port + "/signup");

		WebElement firstNameInput = driver.findElement(By.id("inputFirstName"));
		WebElement lastNameInput = driver.findElement(By.id("inputLastName"));
		WebElement userNameInput = driver.findElement(By.id("inputUsername"));
		WebElement passwordInput = driver.findElement(By.id("inputPassword"));

		firstNameInput.sendKeys(firstName);
		lastNameInput.sendKeys(lastName);
		userNameInput.sendKeys(userName);
		passwordInput.sendKeys(password);
		passwordInput.submit();
	}

	private void signIn() {
		driver.get("http://localhost:" + this.port + "/login");

		// Logging in
		WebElement usernameLogin = driver.findElement(By.id("inputUsername"));
		WebElement passwordLogin = driver.findElement(By.id("inputPassword"));

		usernameLogin.sendKeys(userName);
		passwordLogin.sendKeys(password);
		passwordLogin.submit();
	}

	private void switchToNotesTab() {
		WebDriverWait wait = new WebDriverWait(driver, 3);

		// Click on the Notes tab button
		WebElement notesNavButton = driver.findElement(By.id("nav-notes-tab"));
		wait.until(ExpectedConditions.visibilityOf(notesNavButton));
		notesNavButton.click();

		// Verify we've switched tabs and the elements within are visible
		WebElement addNoteButton = driver.findElement(By.id("add-note"));
		wait.until(ExpectedConditions.visibilityOf(addNoteButton));
	}

	private void switchToCredentialsTab() {
		WebDriverWait wait = new WebDriverWait(driver, 3);


	}
}
