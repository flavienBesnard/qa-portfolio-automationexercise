package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductDetailsPage extends BasePage {

    private static final By PRODUCT_INFORMATION =
            By.cssSelector(".product-details .product-information");
    private static final By PRODUCT_IMAGE = By.cssSelector(".product-details .view-product img");
    private static final By RATING_IMG =
            By.cssSelector(".product-details .product-information img[src*='rating']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(PRODUCT_INFORMATION);
    }

    public String productName() {
        assertLoaded();
        WebElement productInformation = driver.findElement(PRODUCT_INFORMATION);
        return productInformation.findElement(By.tagName("h2")).getText();
    }

    public String price() {
        assertLoaded();
        WebElement productInformation = driver.findElement(PRODUCT_INFORMATION);
        return productInformation.findElement(By.tagName("span")).getText();
    }

    public boolean isProductImageDisplayed() {
        assertLoaded();
        List<WebElement> productImage = driver.findElements(PRODUCT_IMAGE);
        return !productImage.isEmpty() && productImage.getFirst().isDisplayed();
    }

    public boolean isRatingDisplayed() {
        assertLoaded();
        List<WebElement> rating = driver.findElements(RATING_IMG);
        return !rating.isEmpty() && rating.getFirst().isDisplayed();
    }

    public String availability() {
        assertLoaded();
        WebElement productInformation = driver.findElement(PRODUCT_INFORMATION);
        return productInformation
                .findElement(By.xpath(".//p[b[normalize-space()='Availability:']]"))
                .getText();
    }

    public String condition() {
        assertLoaded();
        WebElement productInformation = driver.findElement(PRODUCT_INFORMATION);
        return productInformation
                .findElement(By.xpath(".//p[b[normalize-space()='Condition:']]"))
                .getText();
    }

    public String brand() {
        assertLoaded();
        WebElement productInformation = driver.findElement(PRODUCT_INFORMATION);
        return productInformation
                .findElement(By.xpath(".//p[b[normalize-space()='Brand:']]"))
                .getText();
    }
}
