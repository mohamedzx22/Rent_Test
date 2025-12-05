package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    WebDriver driver;
    By emailInput = By.cssSelector("input[placeholder='Email']");
    By passwordInput = By.xpath("//input[@placeholder='Password']");
    By loginBtn = By.xpath("//button[@type='submit']");
    By loginLink = By.xpath("//a[@href='/login']");
    By logoutBtn = By.xpath("//*[@id='root']/div/section/div/div/nav/div[2]/button");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openLoginPage() {
        this.driver.findElement(this.loginLink).click();
    }

    public void clickLogin() {
        this.driver.findElement(this.loginBtn).click();
    }

    public void clickLogout() {
        this.driver.findElement(this.logoutBtn).click();
    }

    public void enterEmail(String email) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterPassword(String pass) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        driver.findElement(passwordInput).sendKeys(pass);
    }


    public void clearEmail() {
        this.driver.findElement(this.emailInput).clear();
    }

    public void clearPassword() {
        WebElement pwd = driver.findElement(passwordInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", pwd);
    }

    public void login(String email, String password) {
        this.clearEmail();
        this.enterEmail(email);
        this.clearPassword();
        this.enterPassword(password);
        this.clickLogin();
    }
}