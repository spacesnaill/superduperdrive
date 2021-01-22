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

	private final static int webDriverWaitTimeout = 3;

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
		driver.manage().deleteAllCookies();
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
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);

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
	public void noteInteractionTests() {
		signUp();
		signIn();

		userCreatesNewNote();
		userEditsNote();
		userDeletesNote();
	}

	private void userCreatesNewNote() {
		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();

		String noteTitle = "Note";
		String noteDescription = "Hello World";

		createNote(noteTitle, noteDescription);

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		// Check to make sure it saved and now renders the new note
		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();
		Assertions.assertEquals(noteTitle, driver.findElement(By.xpath("//tbody/tr/th")).getText());
		Assertions.assertEquals(noteDescription, driver.findElement(By.xpath("//td[2]")).getText());
	}

	private void userEditsNote() {
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);
		driver.get("http://localhost:" + this.port + "/home");

		switchToNotesTab();

		WebElement editButton = driver.findElement(By.xpath("//button[contains(.,'Edit')]"));
		editButton.click();

		String newNoteTitle = "Edited Note Title";
		String newNoteDescription = "I edited the description!";

		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		WebElement noteDescriptionInput = driver.findElement(By.id("note-description"));
		wait.until(ExpectedConditions.visibilityOf(noteTitleInput));

		noteTitleInput.clear();
		noteDescriptionInput.clear();

		noteTitleInput.sendKeys(newNoteTitle);
		noteDescriptionInput.sendKeys(newNoteDescription);
		noteDescriptionInput.submit();

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();

		String noteTitleXpath = String.format("//tbody/tr/th[contains(.,'%s')]", newNoteTitle);
		String noteDescriptionXpath = String.format("//td[2][contains(.,'%s')]", newNoteDescription);
		Assertions.assertEquals(newNoteTitle, driver.findElement(By.xpath(noteTitleXpath)).getText());
		Assertions.assertEquals(newNoteDescription, driver.findElement(By.xpath(noteDescriptionXpath)).getText());
	}

	private void userDeletesNote() {
		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();

		String noteTitle = "Groceries";
		String noteDescription = "Carrots, chocolate, eggs";

		createNote(noteTitle, noteDescription);

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();

		// Verify the note exists now
		String noteTitleXpath = String.format("//tbody/tr/th[contains(.,'%s')]", noteTitle);
		String noteDescriptionXpath = String.format("//td[2][contains(.,'%s')]", noteDescription);
		System.out.println(driver.findElement(By.xpath(noteTitleXpath)).getText());
		Assertions.assertEquals(noteTitle, driver.findElement(By.xpath(noteTitleXpath)).getText());
		Assertions.assertEquals(noteDescription, driver.findElement(By.xpath(noteDescriptionXpath)).getText());

		WebElement deleteNoteButton = driver.findElement(By.xpath("//a[contains(@href, '/result/deletenote/2')]"));
		deleteNoteButton.click();

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		driver.get("http://localhost:" + this.port + "/home");
		switchToNotesTab();

		System.out.println(driver.getPageSource());
		// Note should no longer exist, as it has been deleted
		// findElements will not throw an error like findElement does when it can't find something
		// so we can check the list to see if it is length 0, which means it couldn't find any matching elements
		Assertions.assertEquals(0, driver.findElements(By.xpath(noteTitleXpath)).size());
		Assertions.assertEquals(0, driver.findElements(By.xpath(noteDescriptionXpath)).size());
	}

	// Adding, editing, and deleting credentials tests
	@Test
	public void credentialInteractionTests() {
		signUp();
		signIn();

		userCreatesNewCredential();
		userEditsCredential();
		userDeletesCredential();
	}

	private void userEditsCredential() {
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);
		driver.get("http://localhost:" + this.port + "/home");
		switchToCredentialsTab();

		String newCredentialUrl = "http://www.yahoo.com";
		String newCredentialUsername = "newusername";
		String newCredentialPassword = "dskfshdivxocj93";

		String credentialUrlXpath = String.format("//table[@id='credentialTable']/tbody/tr/th[contains(.,'%s')]", newCredentialUrl);
		String credentialUsernameXpath = String.format("//table[@id='credentialTable']/tbody/tr/td[2][contains(.,'%s')]", newCredentialUsername);

		WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
		WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
		WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));

		driver.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/td/button[contains(.,'Edit')]")).click();
		wait.until(ExpectedConditions.visibilityOf(credentialUrlInput));

		credentialUrlInput.clear();
		credentialUsernameInput.clear();
		credentialPasswordInput.clear();

		credentialUrlInput.sendKeys(newCredentialUrl);
		credentialUsernameInput.sendKeys(newCredentialUsername);
		credentialPasswordInput.sendKeys(newCredentialPassword);
		credentialPasswordInput.submit();

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		driver.get("http://localhost:" + this.port + "/home");
		switchToCredentialsTab();

		Assertions.assertEquals(newCredentialUrl, driver.findElement(By.xpath(credentialUrlXpath)).getText());
		Assertions.assertEquals(newCredentialUsername, driver.findElement(By.xpath(credentialUsernameXpath)).getText());
		Assertions.assertNotNull(driver.findElement(By.xpath("//td[3]")).getText());

		credentialPasswordInput = driver.findElement(By.id("credential-password"));
		driver.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/td/button[contains(.,'Edit')]")).click();
		wait.until(ExpectedConditions.visibilityOf(credentialPasswordInput));
		String credentialPasswordDecrypted = credentialPasswordInput.getAttribute("value");

		Assertions.assertEquals(newCredentialPassword, credentialPasswordDecrypted);
	}

	private void userDeletesCredential() {

	}

	private void userCreatesNewCredential() {
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);
		driver.get("http://localhost:" + this.port + "/home");
		switchToCredentialsTab();

		String credentialUrl = "https://www.google.com";
		String credentialUsername = "funnyfish";
		String credentialPassword = "Mx3@R6C9jt62";

		String credentialUrlXpath = String.format("//table[@id='credentialTable']/tbody/tr/th[contains(.,'%s')]", credentialUrl);
		String credentialUsernameXpath = String.format("//table[@id='credentialTable']/tbody/tr/td[2][contains(.,'%s')]", credentialUsername);

		createCredential(credentialUrl, credentialUsername, credentialPassword);

		Assertions.assertEquals("Result", driver.getTitle());
		Assertions.assertEquals("Success", driver.findElement(By.tagName("h1")).getText());

		driver.get("http://localhost:" + this.port + "/home");
		switchToCredentialsTab();
		Assertions.assertEquals(credentialUrl, driver.findElement(By.xpath(credentialUrlXpath)).getText());
		Assertions.assertEquals(credentialUsername, driver.findElement(By.xpath(credentialUsernameXpath)).getText());
		Assertions.assertNotNull(driver.findElement(By.xpath("//td[3]")).getText());

		driver.findElement(By.xpath("//table[@id='credentialTable']/tbody/tr/td/button[contains(.,'Edit')]")).click();
		WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));
		wait.until(ExpectedConditions.visibilityOf(credentialPasswordInput));
		String credentialPasswordDecrypted = credentialPasswordInput.getAttribute("value");

		Assertions.assertEquals(credentialPassword, credentialPasswordDecrypted);
	}

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
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);

		// Click on the Notes tab button
		WebElement notesNavButton = driver.findElement(By.id("nav-notes-tab"));
		wait.until(ExpectedConditions.visibilityOf(notesNavButton));
		notesNavButton.click();

		// Verify we've switched tabs and the elements within are visible
		WebElement addNoteButton = driver.findElement(By.id("add-note"));
		wait.until(ExpectedConditions.visibilityOf(addNoteButton));
	}

	private void createNote(String title, String description) {
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);

		WebElement addNoteButton = driver.findElement(By.id("add-note"));
		addNoteButton.click();

		WebElement noteTitleInput = driver.findElement(By.id("note-title"));
		WebElement noteDescriptionInput = driver.findElement(By.id("note-description"));

		wait.until(ExpectedConditions.visibilityOf(noteTitleInput));

		noteTitleInput.sendKeys(title);
		noteDescriptionInput.sendKeys(description);
		noteDescriptionInput.submit();
	}

	private void switchToCredentialsTab() {
		WebDriverWait wait  = new WebDriverWait(driver, webDriverWaitTimeout);

		// Click on the Credentials tab button
		WebElement credentialsNavButton = driver.findElement(By.id("nav-credentials-tab"));
		wait.until(ExpectedConditions.visibilityOf(credentialsNavButton));
		credentialsNavButton.click();

		// Verify we've switched tabs and the elements within are visible
		WebElement addCredentialButton = driver.findElement(By.id("add-credential"));
		wait.until(ExpectedConditions.visibilityOf(addCredentialButton));
	}

	private void createCredential(String url, String username, String password) {
		WebDriverWait wait = new WebDriverWait(driver, webDriverWaitTimeout);

		WebElement addCredentialButton = driver.findElement(By.id("add-credential"));
		addCredentialButton.click();

		WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
		WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
		WebElement credentialPassword = driver.findElement(By.id("credential-password"));

		wait.until(ExpectedConditions.visibilityOf(credentialUrlInput));

		credentialUrlInput.sendKeys(url);
		credentialUsernameInput.sendKeys(username);
		credentialPassword.sendKeys(password);
		credentialPassword.submit();
	}
}
