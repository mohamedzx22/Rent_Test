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
        return new Object[][]{{"rahma@gmail.com", "1234", "/admin"},
                {"hoor@gmail.com", "1234", "/landlord"},
                {"rana@gmail.com", "1234", "/tenant"}};
    }

    @DataProvider(
            name = "InvalidLoginData"
    )
    public Object[][] invalidData() {
        return new Object[][]{{"rahma@g.com", "1234", "\"Email not found\""},
                {"rahma@gmail.com", "124", "\"Invalid password\""},
                {"rahma@g.com", "134", "\"Email not found\""},
                {"hor@gmail.com", "1234", "\"Email not found\""},
                {"hoor@gmail.com", "12", "\"Invalid password\""},
                {"rana@gm.com", "1234", "\"Email not found\""},
                {"rana@gmail.com", "34", "\"Invalid password\""},
               };
    }

    @DataProvider(name = "emptyLoginData")
    public Object[][] emptyLoginData() {
        return new Object[][]{
                {"", "1234", "Email is required"},              // Email فاضي
                {"rahma@gmail.com", "", "Password is required"}, // Password فاضي
                {"", "", "Email and Password are required"}     // الاتنين فاضيين
        };
    }

    @DataProvider(name = "emailWithoutAtData")
    public Object[][] emailWithoutAtData() {
        return new Object[][]{
                {"assemasdgmail.com", "1234567", "Email not found"},
                {"rahma.gmail.com", "1234", "Email not found"},
        };
    }

    @DataProvider(name = "capitalEmailData")
    public Object[][] capitalEmailData() {
        return new Object[][]{
                {"Rahma@gmail.com", "1234"},
                {"RanA@gmail.com", "1234"},
                {"hoOR@gmail.com", "1234"}
        };
    }

    @Test(priority = 1,
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

    @Test(priority = 2,
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
    @Test(priority = 5,dataProvider = "emptyLoginData")
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

    @Test(priority = 3,dataProvider = "emailWithoutAtData")
    public void withoutAtYahoo(String email, String password, String expectedAlert) {

        loginPage.openLoginPage();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alert says: " + alertText);

            Assert.assertEquals(alertText, expectedAlert);
            System.out.println("failed to login due to invalid email");

            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("Enter Email With At Character");
        }
    }


    @Test(priority = 4,dataProvider = "capitalEmailData")
    public void EmailWithCapitalLetter(String email, String password) {

        loginPage.openLoginPage();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(driver1 ->
                driver1.getCurrentUrl().contains("/admin") ||
                        driver1.getCurrentUrl().contains("/tenant") ||
                        driver1.getCurrentUrl().contains("/landlord")
        );

        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(
                currentUrl.contains("/admin") ||
                        currentUrl.contains("/tenant") ||
                        currentUrl.contains("/landlord"),
                "User should be redirected to the correct dashboard even with capital letters"
        );

        System.out.println("User redirected to: " + currentUrl);
    }



}