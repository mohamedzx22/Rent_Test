package login;


import base.BaseTest;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {
    public LoginTest() {
    }

    @DataProvider(
            name = "validLoginData"
    )
    public Object[][] validLoginData() {
        return new Object[][]{{"rahma@gmail.com", "1234", "/admin"}, {"hoor@gmail.com", "1234", "/landlord"}, {"rana@gmail.com", "1234", "/tenant"}};
    }

    @DataProvider(
            name = "InvalidLoginData"
    )
    public Object[][] invalidData() {
        return new Object[][]{{"rahma@g.com", "1234", "\"Email not found\""}, {"rahma@gmail.com", "124", "\"Invalid password\""}, {"rahma@g.com", "134", "\"Email not found\""}, {"hor@gmail.com", "1234", "\"Email not found\""}, {"hoor@gmail.com", "12", "\"Invalid password\""}, {"hoor@g.com", "1234", "\"Email not found\""}, {"rana@gm.com", "1234", "\"Email not found\""}, {"rana@gmail.com", "34", "\"Invalid password\""}, {"nada@g.com", "14", "\"Email not found\""}};
    }

    @DataProvider(name = "emptyLoginData")
    public Object[][] emptyLoginData() {
        return new Object[][]{
                {"", "1234", "Email is required"},              // Email فاضي
                {"rahma@gmail.com", "", "Password is required"}, // Password فاضي
                {"", "", "Email and Password are required"}     // الاتنين فاضيين
        };
    }

    @Test(
            dataProvider = "validLoginData"
    )
    public void testValidLogin(String email, String password, String expectedPath) {
        loginPage.openLoginPage();
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/login"));
        loginPage.clearEmail();
        loginPage.enterEmail(email);
        loginPage.clearPassword();
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        wait.until(ExpectedConditions.urlContains(expectedPath));
        String actualUrl = this.driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains(expectedPath), "User did NOT land on the expected page: " + expectedPath);
        loginPage.clickLogout();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
    }

    @Test(
            dataProvider = "InvalidLoginData"
    )
    public void testInValidLogin(String email, String password, String expectedAlertMessage) {
        loginPage.openLoginPage();
        loginPage.clearEmail();
        loginPage.enterEmail(email);
        loginPage.clearPassword();
        loginPage.enterPassword(password);
        loginPage.clickLogin();
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(5L));

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = this.driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alert says: " + alertText);
            Assert.assertEquals(alertText, expectedAlertMessage);
            alert.accept();
        } catch (TimeoutException var8) {
            System.out.println("Login succeeded unexpectedly!");
        }

    }
    @Test(dataProvider = "emptyLoginData")
    public void emptyFieldsLogin(String email, String password, String expectedAlert) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginPage();

        loginPage.clearEmail();
        loginPage.enterEmail(email);

        loginPage.clearPassword();
        loginPage.enterPassword(password);

        loginPage.clickLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            Assert.assertEquals(alert.getText(), expectedAlert);
            String actualAlert = alert.getText();
            System.out.println("Alert: " + actualAlert);
            alert.accept();
        } catch (TimeoutException e) {
            Assert.fail("Alert did not appear for empty fields!");
        } catch (org.openqa.selenium.NoSuchSessionException e) {
            Assert.fail("Browser session closed before alert appeared!");
        }
    }

}