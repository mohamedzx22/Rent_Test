package pendinglandlords;

import base.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import pages.PendingLandlordsPage;

import java.time.Duration;

public class PendingLandlordsTest extends BaseTest {

    PendingLandlordsPage pending;

    @Test
    public void approveLandlord() {

        loginAsAdmin();  // من BaseTest

        wait.until(ExpectedConditions.urlContains("/admin"));

        pending = new PendingLandlordsPage(driver);
        pending.clickApprove();
    }

    @Test
    public void rejectLandlord() {

        loginAsAdmin();

        wait.until(ExpectedConditions.urlContains("/admin"));

        pending = new PendingLandlordsPage(driver);
        pending.clickReject();
    }
}
