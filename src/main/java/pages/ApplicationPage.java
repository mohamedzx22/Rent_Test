package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ApplicationPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // --------------- Locators -----------------
    private By applyNowButton = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/div[3]/div[2]/div[3]/button");
    private By fullNameInput = By.name("fullName");
    private By phoneInput = By.name("phone");
    private By fileInput = By.cssSelector("input[type='file']");
    private By submitButton = By.cssSelector("button[type='submit']");
    private By deleteButton = By.xpath("//*[@id=\"root\"]/div/div[1]/div/div[2]/div/div/div[1]/button");
    //*[@id="root"]/div/div[1]/div/div[2]/div/div/div[2]/button
    // --------------- Constructor -----------------
    public ApplicationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // --------------- Actions -----------------
    public void openListingAndApply(int postIndex) {
        driver.get("http://localhost:3000/listings");
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(applyNowButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public void setFullName(String fullName) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(fullNameInput));
        input.clear();
        input.sendKeys(fullName);
    }

    public void setPhone(String phone) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
        input.clear();
        input.sendKeys(phone);
    }

    public void uploadDocument(String filePath) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
        input.sendKeys(filePath);
    }

    public void clickSubmit() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    public boolean isDeleteApplicationPresent() {
        try {
            driver.findElement(deleteButton);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void clickDeleteApplication() {
        driver.findElement(deleteButton).click();
    }

    public By getDeleteButton() {
        return deleteButton;
    }
}
