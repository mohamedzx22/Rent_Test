package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PendingLandlordsPage {
    WebDriver driver;
    WebDriverWait wait;

    public PendingLandlordsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    By pendingPropertiesBtn = By.xpath("//button[contains(., 'Pending Landlords')]");
    By approveBtn = By.xpath("//button[contains(@class,'approve-btn')]");
    By rejectBtn = By.xpath("//button[contains(@class,'reject-btn')]");

    public void clickApprove() {
        WebElement approveElement = wait.until(ExpectedConditions.elementToBeClickable(approveBtn));
        approveElement.click();
    }

    public void clickReject() {
        WebElement rejectElement = wait.until(ExpectedConditions.elementToBeClickable(rejectBtn));
        rejectElement.click();
    }
}
