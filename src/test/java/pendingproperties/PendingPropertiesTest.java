package pendingproperties;


import base.BaseTest;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import pages.PendingPropertiesPage;

public class PendingPropertiesTest extends BaseTest {

    PendingPropertiesPage pending;

    @Test
    public void approveProperty() {

        loginAsAdmin();

        wait.until(ExpectedConditions.urlContains("/admin"));

        pending = new PendingPropertiesPage(driver);

        pending.clickPendingProperties();
        pending.clickApprove();
    }

    @Test
    public void cancelProperty() {

        loginAsAdmin();

        wait.until(ExpectedConditions.urlContains("/admin"));

        pending = new PendingPropertiesPage(driver);

        pending.clickPendingProperties();
        pending.clickReject();
    }
}

