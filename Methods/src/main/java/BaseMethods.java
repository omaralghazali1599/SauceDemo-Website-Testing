import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import java.io.File;
public class BaseMethods {
    private WebDriver driver;
    private static By UsernameElement = By.id("user-name");
    private static By PasswordElement = By.id("password");
    private static By LoginButtonElement = By.id("login-button");
    public BaseMethods(WebDriver driver){this.driver=driver;}

    public void SendLoginCredentials (String Text1, String Text2){
        WebElement Username = driver.findElement(UsernameElement);
        Username.sendKeys(Text1);

        WebElement Password = driver.findElement(PasswordElement);
        Password.sendKeys(Text2);

        WebElement LoginButton = driver.findElement(LoginButtonElement);
        LoginButton.click();

    }
    public void takeSnapShot(WebDriver driver,String fileWithPath) throws Exception{
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)driver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }
}
