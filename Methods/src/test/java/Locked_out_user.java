import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class Locked_out_user {

    private WebDriver driver;
    private WebDriverWait wait;
    private BaseMethods methods;

    @BeforeClass
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        methods = new BaseMethods(driver);
    }

    @Test(priority = 1)
    public void testLoginAndSnapshot() throws Exception {
        driver.get("https://www.saucedemo.com/");

        // Capture and print the page title
        String title = driver.getTitle();
        System.out.println("Page Title: " + title);



        // Send login credentials for "locked_out_user"
        methods.SendLoginCredentials("locked_out_user", "secret_sauce");

        // Take a snapshot before login
        methods.takeSnapShot(driver, ".\\Login_Locked_Out_User.png");

        // Validate the error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")));
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";
        String actualError = errorMessage.getText();
        Assert.assertEquals(actualError, expectedError, "Error message mismatch for locked-out user.");
    }

    @Test(priority = 2, dependsOnMethods = "testLoginAndSnapshot")
    public void testErrorMessageDisplayed() {
        WebElement errorMessage = driver.findElement(By.cssSelector("[data-test='error']"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message was not displayed.");
    }

    @Test(priority = 3, dependsOnMethods = "testLoginAndSnapshot")
    public void testCartInteractionBlocked() {
        boolean isCartAccessible = driver.findElements(By.className("shopping_cart_link")).size() > 0;
        Assert.assertFalse(isCartAccessible, "Cart interaction should be blocked for locked-out users.");
    }

    @Test(priority = 4, dependsOnMethods = "testLoginAndSnapshot")
    public void testPageElementsAfterLogin() {
        WebElement loginBox = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginBox.isDisplayed(), "Login button should still be visible after error.");
    }

    @Test(priority = 5, dependsOnMethods = "testLoginAndSnapshot")
    public void testLogoutFromLockedOutUser() {
        WebElement hamburgerIcon = driver.findElement(By.id("react-burger-menu-btn"));
        hamburgerIcon.click();

        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        logout.click();

        // Validate successful logout
        WebElement loginButton = driver.findElement(By.id("login-button"));
        Assert.assertTrue(loginButton.isDisplayed(), "Logout was unsuccessful, login button not found.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
