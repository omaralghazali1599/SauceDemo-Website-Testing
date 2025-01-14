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

public class Standard_user {

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



        // Send login credentials for "standard_user"
        methods.SendLoginCredentials("standard_user", "secret_sauce");

        // Take a snapshot before login
        methods.takeSnapShot(driver, ".\\Login_Standard_user.png");
    }

    @Test(priority = 2, dependsOnMethods = "testLoginAndSnapshot")
    public void testAddProductToCart() {
        // Add the first product to the cart
        WebElement firstProduct = driver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        firstProduct.click();

        // Navigate to the cart
        WebElement cartIcon = driver.findElement(By.className("shopping_cart_link"));
        cartIcon.click();
    }

    @Test(priority = 3, dependsOnMethods = "testAddProductToCart")
    public void testCheckoutProcess() {
        // Proceed to checkout
        WebElement checkout = driver.findElement(By.id("checkout"));
        checkout.click();

        // Fill in user details
        WebElement username = driver.findElement(By.id("first-name"));
        username.sendKeys("Omar");

        WebElement lastname = driver.findElement(By.id("last-name"));
        lastname.sendKeys("Al-Ghazali");

        WebElement postalCode = driver.findElement(By.id("postal-code"));
        postalCode.sendKeys("19311");

        WebElement continueButton = driver.findElement(By.id("continue"));
        continueButton.click();

        // Validate total price
        WebElement totalPriceElement = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]"));
        String totalPrice = totalPriceElement.getText();
        System.out.println("Total Price: " + totalPrice);
    }

    @Test(priority = 4, dependsOnMethods = "testCheckoutProcess")
    public void testOrderCompletion() {
        // Complete the order
        WebElement finish = driver.findElement(By.id("finish"));
        finish.click();

        // Validate the order confirmation message
        String expectedMessage = "Thank you for your order!";
        String actualMessage = driver.findElement(By.className("complete-header")).getText();
        Assert.assertTrue(actualMessage.contains(expectedMessage), "Order confirmation failed.");
    }

    @Test(priority = 5, dependsOnMethods = "testOrderCompletion")
    public void testLogout() {
        // Navigate back to products
        WebElement backHome = driver.findElement(By.id("back-to-products"));
        backHome.click();

        // Open the hamburger menu and log out
        WebElement hamburgerIcon = driver.findElement(By.id("react-burger-menu-btn"));
        hamburgerIcon.click();

        WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
        logout.click();

        // Validate successful logout
        Assert.assertTrue(driver.getCurrentUrl().contains("saucedemo.com"), "Logout failed.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
