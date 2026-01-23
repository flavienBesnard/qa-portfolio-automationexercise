package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentDonePage extends BasePage {
    public static final By ORDER_PLACED = By.cssSelector("[data-qa=\"order-placed\"]");
    public static final By CONFIRMATION_MSG =
            By.xpath(
                    "//p[contains(normalize-space(),'Congratulations! Your order has been confirmed!')]");
    public static final By DOWNLOAD_INVOICE = By.cssSelector("a[href*=\"/download_invoice\"]");

    public PaymentDonePage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(ORDER_PLACED));
        visible(ORDER_PLACED);
        visible(CONFIRMATION_MSG);
    }

    public String getDownloadInvoiceHref() {
        assertLoaded();
        return visible(DOWNLOAD_INVOICE).getAttribute("href");
    }
}
