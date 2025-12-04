package application;

import base.BaseTest;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.ApplicationPage;

import java.time.Duration;


public class ApplicationTest extends BaseTest {

    ApplicationPage applicationPage;

    @DataProvider(name = "SuccessApplication")
    public Object[][] SuccessApplication() {
        return new Object[][]{
                {"Rahma Shawkat", "01234567890", "C://Users//DELL//Downloads//Walid.pdf", true}
        };
    }

    @DataProvider(name = "EmptyFieldsApplication")
    public Object[][] emptyFieldsData() {
        return new Object[][] {
                {"", "01234567890", "C://Users//DELL//Downloads//Walid.pdf", "Full Name is required"},
                {"Rahma Shawkat", "", "C://Users//DELL//Downloads//Walid.pdf", "Phone is required"}
        };
    }

    @DataProvider(name = "DuplicateApplication")
    public Object[][] duplicateApplicationData() {
        return new Object[][]{
                {"Rahma Shawkat", "01234567890", "C://Users//DELL//Downloads//Walid.pdf",
                        "You already applied for this listing"}
        };
    }

    @Test(dataProvider = "SuccessApplication")
    public void applyForListingTest(String fullName, String phone, String filePath, boolean expectedSuccess) {
        loginAsTenant();
        applicationPage = new ApplicationPage(driver);
        applicationPage.openListingAndApply(1);

        if (!fullName.isEmpty()) applicationPage.setFullName(fullName);
        if (!phone.isEmpty()) applicationPage.setPhone(phone);
        if (!filePath.isEmpty()) applicationPage.uploadDocument(filePath);

        applicationPage.clickSubmit();

    }

    @Test(dataProvider = "EmptyFieldsApplication")
    public void applyWithEmptyFieldsTest(String fullName, String phone, String filePath, String expectedAlert) {
        loginAsTenant();
        applicationPage = new ApplicationPage(driver);
        applicationPage.openListingAndApply(1);

        if (!fullName.isEmpty()) applicationPage.setFullName(fullName);
        if (!phone.isEmpty()) applicationPage.setPhone(phone);
        if (!filePath.isEmpty()) applicationPage.uploadDocument(filePath);

        applicationPage.clickSubmit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        System.out.println("Alert text: " + alert.getText()); // ✅ تطبع الرسالة للمراجعة
        Assert.assertEquals(alert.getText(), expectedAlert);
        alert.accept();


    }
    @Test
    public void applyWithoutLoginRedirectTest() {

        applicationPage = new ApplicationPage(driver);
        applicationPage.openListingAndApply(1);

        wait.until(ExpectedConditions.urlContains("/login"));

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Redirected to login page: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("/login"), "Donot Nevigate to Login Page");
    }

    @Test(dataProvider = "DuplicateApplication")
    public void duplicateApplicationTest(String fullName, String phone, String filePath, String expectedAlert) {

        loginAsTenant();
        applicationPage = new ApplicationPage(driver);
        applicationPage.openListingAndApply(1);

        applicationPage.setFullName(fullName);
        applicationPage.setPhone(phone);
        applicationPage.uploadDocument(filePath);
        applicationPage.clickSubmit();

        System.out.println("First application submitted");

        applicationPage.openListingAndApply(1);
        applicationPage.setFullName(fullName);
        applicationPage.setPhone(phone);
        applicationPage.uploadDocument(filePath);
        applicationPage.clickSubmit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        System.out.println("Expected alert from assert: " + expectedAlert);

        try {
            wait.until(ExpectedConditions.alertIsPresent());
            Alert alert = driver.switchTo().alert();
            System.out.println("Actual alert appeared: " + alert.getText());

            Assert.assertEquals(alert.getText(), expectedAlert, "Duplicate was inserted!");
            alert.accept();

        } catch (TimeoutException e) {
            System.out.println("No alert appeared");
            Assert.fail("The duplicated application was insert");
        }
    }

    @Test
    public void deleteApplicationTest() {
        loginAsTenant();

        driver.get("http://localhost:3000/tenant");
        applicationPage = new ApplicationPage(driver);

        By deleteButtonLocator = applicationPage.getDeleteButton();

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(deleteButtonLocator));
            WebElement deleteButton = driver.findElement(deleteButtonLocator);

            System.out.println("Delete button is now visible. Proceeding to delete... 🗑");
            deleteButton.click();

            try {
                wait.until(ExpectedConditions.alertIsPresent());
                driver.switchTo().alert().accept();
            } catch (TimeoutException ignored) {}

            wait.until(ExpectedConditions.stalenessOf(deleteButton));

            System.out.println("delete application successful");
        } catch (TimeoutException e) {
            Assert.fail("No application found to delete or deletion failed!");
        }
    }

}


