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
		String firstName = "Jane";
		String lastName = "Doe";
		String userName = "Janedoe";
		String password = "password";

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


	// Adding, editing, and deleting credentials tests

}
