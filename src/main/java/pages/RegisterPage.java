package pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class RegisterPage {
    WebDriver driver;

    private By usernameField = By.id("username");
    private By emailField    = By.name("email");
    private By passwordField = By.cssSelector("input[placeholder='Password']"); // مُحدد CSS
    private By confirmPass   = By.xpath("//input[@placeholder='Confirm Password']"); // مُحدد XPath
    private By phoneField    = By.cssSelector("input[placeholder='Phone Number']"); // مُحدد CSS
    private By roleSelect    = By.tagName("select"); // مُحدد Tag Name
    private By signupBtn     = By.xpath("//button[text()='Create Account']"); // مُحدد XPath
    private By registerLink  = By.xpath("//a[text()='Register']"); // رابط التسجيل على الصفحة الرئيسية

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }


    public void openRegisterPage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement registerLinkElement = wait.until(ExpectedConditions.elementToBeClickable(registerLink));
        registerLinkElement.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField));
    }

    public void enterUsername(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameField)
        );
        usernameElement.sendKeys(username);
    }

    public void enterEmail(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    public void enterPassword(String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPass)).sendKeys(password);
    }

    public void enterPhone(String phone) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneField)).sendKeys(phone);
    }

    public void chooseType(String type) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(roleSelect));
        Select select = new Select(dropdown);
        select.selectByVisibleText(type); // "Tenant" أو "Landlord"
    }

    public void clickSignup() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(signupBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", createBtn);

        try { Thread.sleep(300); } catch (InterruptedException e) { }

        createBtn.click();
    }
}