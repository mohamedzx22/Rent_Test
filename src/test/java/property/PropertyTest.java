package property;

import base.BaseTest;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.PropertyPage;

public class PropertyTest extends BaseTest {
    public PropertyTest() {
    }

    @DataProvider(
            name = "validPropertyData"
    )
    public static Object[][] validPropertyData() {
        String basePath = System.getProperty("user.dir") + "/src/test/java/resources/";
        return new Object[][]{{"Nice Apartment", "1200", "Cairo", "Spacious 2BHK apartment", basePath + "Heliopolis Apartment.jpg"},
                {"Studio Flat", "800", "Giza", "Cozy studio flat near metro", basePath + "Giza Apartment.jpg"},
                {"Luxury Villa", "5000", "Alexandria", "Beachfront villa with pool", basePath + "Alexandria Apartment.jpg"}
        };
    }

    @DataProvider(
            name = "invalidPropertyData"
    )
    public static Object[][] invalidPropertyData() {
        String basePath = System.getProperty("user.dir") + "/src/test/java/resources/";
        return new Object[][]{{"", "1500", "Cairo", "Nice place", basePath + "Heliopolis Apartment.jpg", "EMPTY_TITLE"},
                {"Nice Apartment", "", "Cairo", "Nice place", basePath + "Heliopolis Apartment.jpg", "EMPTY_PRICE"},
                {"Nice Apartment", "1500", "", "Nice place", basePath + "Heliopolis Apartment.jpg", "EMPTY_LOCATION"},
                {"Nice Apartment", "1500", "Cairo", "", basePath + "Heliopolis Apartment.jpg", "EMPTY_DESCRIPTION"},
                {"Nice Apartment", "1500", "Cairo", "Nice place", "", "EMPTY_IMAGE"},
                {"Nice Apartment", "-500", "Cairo", "Nice place", basePath + "Heliopolis Apartment.jpg", "NEGATIVE_PRICE"},
                {"Nice Apartment", "0", "Cairo", "Nice place", basePath + "Heliopolis Apartment.jpg", "ZERO_PRICE"},
                {"123456", "1500", "Cairo", "Nice place", basePath + "Heliopolis Apartment.jpg", "NUMERIC_TITLE"},
                {"Nice Apartment", "1500", "999999", "Nice place", basePath + "Heliopolis Apartment.jpg", "NUMERIC_LOCATION"},
                {"Nice Apartment", "1500", "Cairo", "a".repeat(2000), basePath + "Heliopolis Apartment.jpg", "LONG_DESCRIPTION"},
                {"Nice Apartment", "1500", "Cairo", "a", basePath + "Heliopolis Apartment.jpg", "SHORT_DESCRIPTION"}
        };
    }

    @DataProvider(
            name = "postsVisibilityData"
    )
    public Object[][] postsVisibilityData() {
        return new Object[][]{{"Nice Apartment", false},
                {"Cairo Fella", true},
                {"C1",  true},
               // {"mahdi top top", true}
                };
    }

    @Test(
            dataProvider = "postsVisibilityData"
    )
    public void testPostsVisibility(String postTitle, boolean isApproved) {
        PropertyPage propertyPage = new PropertyPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        propertyPage.openListPage();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/listings"));

        if (isApproved) {
            WebElement postElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h3[contains(text(),'" + postTitle + "')]")
            ));
            Assert.assertTrue(postElement.isDisplayed(), "Approved post '" + postTitle + "' should be visible in Latest Listings");
        } else {
            List<WebElement> postElements = driver.findElements(By.xpath("//h3[contains(text(),'" + postTitle + "')]"));
            Assert.assertTrue(postElements.isEmpty(), "Pending post '" + postTitle + "' should NOT be visible in Latest Listings");
        }
    }



    @Test(
            dataProvider = "validPropertyData"
    )
    public void testAddValidProperty(String title, String price, String location, String description, String imagePath) throws InterruptedException {
        loginAsLandlord();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/landlord"));
        PropertyPage propertyPage = new PropertyPage(this.driver);
        propertyPage.openAddPropertyPage();
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/add-property"));
        propertyPage.enterTitle(title);
        propertyPage.enterPrice(price);
        propertyPage.enterLocation(location);
        propertyPage.enterDescription(description);
        propertyPage.clickSelectImage();
        propertyPage.uploadImage(imagePath);
        propertyPage.submitProperty();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/landlord"));
        Assert.assertTrue(this.driver.getCurrentUrl().contains("/landlord"));
    }

    @Test(
            dataProvider = "invalidPropertyData"
    )
    public void testInvalidPropertyInputs(String title, String price, String location, String description, String imagePath, String testCase) throws InterruptedException {
        loginAsLandlord();
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/landlord"));
        PropertyPage page = new PropertyPage(this.driver);
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10L));
        this.driver.get("http://localhost:3000/add-property");
        ((JavascriptExecutor)this.driver).executeScript("window.scrollTo(0, 0);", new Object[0]);
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/add-property"));
        page.enterTitle(title);
        page.enterPrice(price);
        page.enterLocation(location);
        if (!description.isEmpty()) {
            page.enterDescription(description);
        }

        if (!imagePath.isEmpty()) {
            page.clickSelectImage();
            page.uploadImage(imagePath);
        }

        try {
            page.submitProperty();
        } catch (Exception e) {
            System.out.println("Submit issue for case: " + testCase + " - " + e.getMessage());
        }

        if (testCase.equals("EMPTY_TITLE")) {
            WebElement titleField = this.driver.findElement(page.getTitleInputLocator());
            ((JavascriptExecutor)this.driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", new Object[]{titleField});
            Assert.assertTrue(titleField.isDisplayed(), "Empty title field should be visible after submit");
        } else if (testCase.equals("EMPTY_PRICE")) {
            Assert.assertEquals(page.getFieldValidationMessage("price"), "Please fill out this field.");
        } else if (testCase.equals("EMPTY_LOCATION")) {
            Assert.assertEquals(page.getFieldValidationMessage("location"), "Please fill out this field.");
        } else if (!testCase.equals("EMPTY_DESCRIPTION") && !testCase.equals("EMPTY_IMAGE")) {
            if (testCase.equals("NEGATIVE_PRICE") || testCase.equals("ZERO_PRICE") || testCase.equals("NUMERIC_TITLE") || testCase.equals("NUMERIC_LOCATION") || testCase.equals("SHORT_DESCRIPTION")) {
                WebDriverWait shortWait = new WebDriverWait(this.driver, Duration.ofSeconds(2L));
                boolean alertAppeared = false;

                try {
                    shortWait.until(ExpectedConditions.alertIsPresent());
                    this.driver.switchTo().alert().accept();
                    alertAppeared = true;
                } catch (TimeoutException var12) {
                    alertAppeared = false;
                }

                if (!alertAppeared) {
                    Assert.fail("BUG: Invalid case '" + testCase + "' was ACCEPTED by the system!");
                } else {
                    System.out.println("Invalid case '" + testCase + "' correctly NOT accepted.");
                }
            }
        } else {
            try {
                wait.until(ExpectedConditions.alertIsPresent());
                this.driver.switchTo().alert().accept();
            } catch (Exception var13) {
                Assert.fail("Expected alert did not appear for " + testCase);
            }
        }

        this.driver.get("http://localhost:3000/add-property");
        ((JavascriptExecutor)this.driver).executeScript("window.scrollTo(0, 0);", new Object[0]);
        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/add-property"));
    }
}
