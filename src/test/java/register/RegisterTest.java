package register;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.RegisterPage;

public class RegisterTest extends BaseTest {


    @DataProvider(name = "registerData")
    public Object[][] registerData() {
        return new Object[][] {
                {"Rama", "rama@gmail.com", "1234", "1234", "01012345678", "Tenant"},
                {"Ruba", "ruba@gmail.com", "abcd", "abcd", "01198765432", "Landlord"}
        };
    }
    @DataProvider(name = "emptyFieldData")
    public Object[][] emptyFieldData() {
        return new Object[][] {
                {"", "r3@gmail.com", "1234", "1234", "01122233344", "Tenant", "Username is required"},
                {"Rahma", "", "1234", "1234", "01122233344", "Tenant", "Email is required"},
                {"Rahma", "r3@gmail.com", "", "1234", "01122233344", "Tenant", "Password is required"},
                {"Rahma", "r3@gmail.com", "1234", "", "01122233344", "Tenant", "Confirm password is required"},
                {"Rahma", "r3@gmail.com", "1234", "1234", "", "Tenant", "Phone number is required"},
                {"Rahma", "r3@gmail.com", "1234", "1234", "01122233344", "", "Role selection is required"}
        };
    }
    @DataProvider(name = "shortPasswordData")
    public Object[][] shortPasswordData() {
        return new Object[][] {
                {"Nada", "nada@gmail.com", "123", "123", "01012345678", "Tenant", "Password must be at least 7 characters"},
                //{"Omar", "omar@gmail.com", "abc", "abc", "01122233344", "Landlord", "Password must be at least 7 characters"}
        };
    }
    @DataProvider(name = "longPasswordData")
    public Object[][] longPasswordData() {
        return new Object[][] {
                {"Nada", "nada@gmail.com",
                        "12345678901234567890123456789", "12345678901234567890123456789",
                        "01011122233", "Tenant", "Password must be less than 25 characters"},

                /*{"Ali", "ali@gmail.com",
                        "abcdefghijklmnopqrstuvwxyz123456", "abcdefghijklmnopqrstuvwxyz123456",
                        "01144455566", "Landlord", "Password must be less than 25 characters"}*/
        };
    }
    @DataProvider(name = "mismatchPasswordData")
    public Object[][] mismatchPasswordData() {
        return new Object[][] {
                {"Nada", "nada@gmail.com", "1234567", "12345678", "01012345678", "Tenant", "Passwords do not match."},
                //{"Ali", "ali@gmail.com", "abcdefg", "abcdafg", "01111111111", "Landlord", "Passwords do not match."}
        };
    }
    @DataProvider(name = "invalidUsernameChar")
    public Object[][] invalidUsernameData() {
        return new Object[][] {
                {"Nadia!", "Nadia@gmail.com", "1234567", "1234567", "01012345678", "Tenant", "Username must not contain special characters"},
                //{"Ali@", "ali@gmail.com", "abcdefg", "abcdefg", "01111111111", "Landlord", "Username must not contain special characters"},
        };
    }
    @DataProvider(name = "existingEmailData")
    public Object[][] existingEmailData() {
        return new Object[][] {
                {"Rahma", "rahma@gmail.com", "1234", "1234", "01011122233", "Tenant", "Email already exists"},
                //{"Reem", "reem@gmail.com", "1234", "1234", "01144455566", "Landlord", "Email already exists"}
        };
    }
    @DataProvider(name = "phoneShortData")
    public Object[][] phoneShortData() {
        return new Object[][] {
                {"Ruba", "ruba@gmail.com", "1234567", "1234567", "0101234567", "Tenant", "Phone number must be exactly 11 digits long."},
        };
    }
    @DataProvider(name = "phoneLongData")
    public Object[][] phoneLongData() {
        return new Object[][] {
                {"Ranaa", "ranaa@gmail.com", "abcdefg", "abcdefg", "010123456789", "Landlord", "Phone number must be exactly 11 digits long."}
        };
    }
    @DataProvider(name = "allZeroPhoneData")
    public Object[][] allZeroPhoneData() {
        return new Object[][] {
                {"Rema", "rema@gmail.com", "1234567", "1234567", "00000000000", "Tenant", "Phone number cannot be all zeros."}
        };
    }
    @DataProvider(name = "existingPhoneData")
    public Object[][] existingPhoneData() {
        return new Object[][] {
                {"Remas", "remas@gmail.com", "1234567", "1234567", "01229874536", "Tenant", "Phone number already exists"},
        };
    }

    @Test(dataProvider = "registerData")
    public void successfulRegistration(String username, String email, String password, String confirmPassword, String phone, String role) {
        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(alert.getText(), "Registered Successfully!");
        alert.accept();
    }


    @Test(dataProvider = "emptyFieldData")
    public void emptyFieldRegistration(String username, String email, String password, String confirmPassword, String phone, String role, String expectedAlert) {
        registerPage.openRegisterPage();
        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        if(!role.isEmpty()) {
            registerPage.chooseType(role);
        }
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "shortPasswordData")
    public void shortPasswordRegistration(String username, String email, String password,
                                          String confirmPassword, String phone, String role,
                                          String expectedAlert) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "longPasswordData")
    public void longPasswordRegistration(String username, String email, String password,
                                         String confirmPassword, String phone, String role,
                                         String expectedAlert) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@placeholder='Username']")
        ));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "mismatchPasswordData")
    public void mismatchPasswordRegistration(String username, String email, String password,
                                             String confirmPassword, String phone, String role,
                                             String expectedAlertMessage) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Mismatch Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlertMessage);
        alert.accept();
    }

    @Test(dataProvider = "invalidUsernameChar")
    public void invalidUsernameRegistration(String username, String email, String password,
                                            String confirmPassword, String phone, String role,
                                            String expectedAlertMessage) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlertMessage);
        alert.accept();
    }

    @Test(dataProvider = "existingEmailData")
    public void existingEmailRegistration(String username, String email, String password,
                                          String confirmPassword, String phone, String role,
                                          String expectedAlertMessage) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlertMessage);
        alert.accept();
    }

    @Test(dataProvider = "phoneShortData")
    public void phoneLessThan11Digits(String username, String email, String password,
                                      String confirmPassword, String phone, String role,
                                      String expectedAlert) {

        registerPage.openRegisterPage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(alert.getText(), expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "phoneLongData")
    public void phoneMoreThan11Digits(String username, String email, String password,
                                      String confirmPassword, String phone, String role,
                                      String expectedAlert) {

        registerPage.openRegisterPage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(alert.getText(), expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "allZeroPhoneData")
    public void phoneAllZerosTest(String username, String email, String password,
                                  String confirmPassword, String phone, String role,
                                  String expectedAlert) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        // نستنى Alert
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlert);
        alert.accept();
    }

    @Test(dataProvider = "existingPhoneData")
    public void existingPhoneRegistration(String username, String email, String password,
                                          String confirmPassword, String phone, String role,
                                          String expectedAlertMessage) {

        registerPage.openRegisterPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Username']")));

        registerPage.enterUsername(username);
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.enterConfirmPassword(confirmPassword);
        registerPage.enterPhone(phone);
        registerPage.chooseType(role);
        registerPage.clickSignup();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();

        String actualAlert = alert.getText();
        System.out.println("Alert: " + actualAlert);

        Assert.assertEquals(actualAlert, expectedAlertMessage);
        alert.accept();
    }



}
