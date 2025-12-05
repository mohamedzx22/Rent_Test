package pages;


import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PropertyPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    By titleInput = By.name("title");
    By priceInput = By.name("price");
    By locationInput = By.name("location");
    By descInput = By.xpath("//*[@id='root']/div/div[1]/div/form/div[3]/textarea");
    By selectImageBtn = By.xpath("//button[contains(.,'Select Image')]");
    By fileInput = By.xpath("//input[@type='file']");
    By submitButton = By.cssSelector("button.submit-btn");
    By addProprtyBtn = By.xpath("//*[@id='root']/div/div[1]/div/div[2]/div/div/button");
    By ListBtn = By.xpath("/html/body/div/div/section/div/div/nav/div[1]/div/a[3]");

    public PropertyPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(3L));
        this.js = (JavascriptExecutor)driver;
    }

    public By getTitleInputLocator() {
        return this.titleInput;
    }

    public void openAddPropertyPage() {
        this.js.executeScript("window.scrollTo(0, 0);", new Object[0]);
        this.driver.findElement(this.addProprtyBtn).click();
    }

    public void openListPage() {
        this.driver.findElement(this.ListBtn).click();
    }

    public void enterTitle(String title) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("title")));
        this.driver.findElement(this.titleInput).sendKeys(new CharSequence[]{title});
    }

    public void enterPrice(String price) {
        this.driver.findElement(this.priceInput).sendKeys(new CharSequence[]{price});
    }

    public void enterLocation(String location) {
        this.driver.findElement(this.locationInput).sendKeys(new CharSequence[]{location});
    }

    public void enterDescription(String text) {
        WebElement label = (WebElement)this.wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//label[text()='Detailed Description']")));
        this.js.executeScript("arguments[0].scrollIntoView({behavior:'auto', block:'center'});", new Object[]{label});
        WebElement textarea = this.driver.findElement(By.xpath("//label[text()='Detailed Description']/following-sibling::textarea"));
        this.wait.until(ExpectedConditions.elementToBeClickable(textarea));
        textarea.clear();
        textarea.sendKeys(new CharSequence[]{text});
    }

    public void clickSelectImage() {
        WebElement element = this.driver.findElement(this.selectImageBtn);
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", new Object[]{element});
    }

    public void uploadImage(String filePath) {
        ((WebElement)this.wait.until(ExpectedConditions.presenceOfElementLocated(this.fileInput))).sendKeys(new CharSequence[]{filePath});
    }

    public void submitProperty() {
        WebElement submit = (WebElement)this.wait.until(ExpectedConditions.presenceOfElementLocated(this.submitButton));
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", new Object[]{submit});
        ((JavascriptExecutor)this.driver).executeScript("arguments[0].click();", new Object[]{submit});
    }

    public String getFieldValidationMessage(By fieldLocator) {
        WebElement field = this.driver.findElement(fieldLocator);
        return (String)this.js.executeScript("return arguments[0].validationMessage;", new Object[]{field});
    }

    public String getFieldValidationMessage(String fieldName) {
        return this.getFieldValidationMessage(By.name(fieldName));
    }
}
