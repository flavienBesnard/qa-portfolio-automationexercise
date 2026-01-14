package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage extends BasePage {
    private static final By PRODUCTS_LIST = By.cssSelector(".features_items");
    private static final By VIEW_CART_LINK = By.cssSelector(".modal-body a[href=\"/view_cart\"]");
    private static final By CONTINUE_SHOPPING_BTN = By.cssSelector(".btn.btn-success.close-modal.btn-block");
    private static final By MODAL = By.cssSelector("#cartModal.modal.show");
    private static final By PRODUCT_INFORMATION = By.cssSelector(".product-details .product-information");
    public ProductsPage(WebDriver driver) {
        super(driver);
    }


    public void assertLoaded() {
        visible(PRODUCTS_LIST);

    }

private By addToCartByProductId(String productId) {
    dismissGoogleVignetteIfPresent();
         assertLoaded();
        return By.cssSelector("a.btn.btn-default.add-to-cart[data-product-id='" + productId + "']");
}


public void viewProductDetailsByProductId(String productId) {
        assertLoaded();
        By viewProduct =  By.cssSelector(".choose .nav.nav-pills.nav-justified a[href='/product_details/" + productId + "']");
       // click(viewProduct);
    clickAndWaitUrlContainsFast(viewProduct, "/product_details/" + productId);
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_INFORMATION));

}

public String getProductNameById(String productId) {
        By productName = By.xpath(
                "//a[data-product-id='" + productId + "' and contains(@class, 'add-to-cart')]/ancestor::div[contains(@class,'single-products')]//div[contains(@class,'productinfo')]//p");
        return driver.findElement(productName).getText();
    }


    public void addProductToCartById(String productId) {
         assertLoaded();
         By AddToCartButton = addToCartByProductId(productId);
         click(AddToCartButton);

        // Preuve d'action : on attend l'apparition de la confirmation "Added" qui apparaît après "Add to cart"
        By cartModal = By.cssSelector("#cartModal, .modal-content");

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(cartModal));

    }



    public  CartPage viewCartFromModal() {
        assertLoaded();
    click(VIEW_CART_LINK);
    return new CartPage(driver);
    }

    public void continueShoppingFromModal() {
        assertLoaded();
        click(CONTINUE_SHOPPING_BTN);
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.invisibilityOfElementLocated(MODAL));

    }

}


