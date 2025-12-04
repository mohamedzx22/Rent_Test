package base;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.LoginPage;
import pages.RegisterPage;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        driver = new EdgeDriver();
        this.driver.get("http://localhost:3000/");
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        driver.manage().window();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass(alwaysRun = true)
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
        LoginPage login = new LoginPage(this.driver);
        login.openLoginPage();
        login.login("rana@gmail.com", "1234");
    }
}
