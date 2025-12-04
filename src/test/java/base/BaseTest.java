package base;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.ApplicationPage;
import pages.LoginPage;
import pages.RegisterPage;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected ApplicationPage applicationPage;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        Logger.getLogger("org.openqa.selenium.manager").setLevel(Level.OFF);
        Logger.getLogger("org.openqa.selenium.devtools").setLevel(Level.OFF);
        driver = new EdgeDriver();
        this.driver.get("http://localhost:3000/");
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        applicationPage = new ApplicationPage(driver);
        driver.manage().window();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void loginAsAdmin() {
        LoginPage login = new LoginPage(this.driver);
        login.openLoginPage();
        login.login("rahma@example.com", "1234");
    }

    public void loginAsLandlord() {
        LoginPage login = new LoginPage(this.driver);
        login.openLoginPage();
        login.login("hoor@example.com", "1234");
    }

    public void loginAsTenant() {
        driver.get("http://localhost:3000/login");
        loginPage.login("rana@gmail.com", "1234");

        // ✅ استخدام الـ wait من BaseTest
        wait.until(ExpectedConditions.urlContains("/tenant"));

        // جلب وحفظ التوكن في localStorage
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userToken = (String) js.executeScript("return window.localStorage.getItem('userToken');");
        System.out.println("Token: " + userToken);
        Assert.assertNotNull(userToken, "Login failed or token is null!");

        js.executeScript("window.localStorage.setItem('userToken', arguments[0]);", userToken);
    }



}
