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
    public ProductsPage(WebDriver driver) {
        super(driver);
    }


    public void assertLoaded() {
        visible(PRODUCTS_LIST);

    }

private By addToCartByProductId(String productId) {
        return By.cssSelector("a.btn.btn-default.add-to-cart[data-product-id='" + productId + "']");
}


public ProductDetailsPage viewProductDetailsByProductId(String productId) {
        assertLoaded();
        By viewProduct =  By.cssSelector(".choose .nav.nav-pills.nav-justified a[href='/product_details/" + productId + "']");
       // click(viewProduct);
    clickAndWaitUrlContainsFast(viewProduct, "/product_details/" + productId);
    ProductDetailsPage productDetails = new ProductDetailsPage(driver);
    productDetails.assertLoaded();
    return productDetails;
}

public String getProductNameById(String productId) {
        assertLoaded();
        WebElement addToCartBtn = driver.findElement(addToCartByProductId(productId));

        return addToCartBtn.findElement(By.xpath("./ancestor::div[contains(@class,'productinfo')]/p")).getText();
    }


    public void addProductToCartById(String productId) {
         assertLoaded();
         By AddToCartButton = addToCartByProductId(productId);
         click(AddToCartButton);

        // Preuve d'action : on attend l'apparition de la confirmation "Added" qui apparaît après "Add to cart"
        By cartModal = By.cssSelector("#cartModal.modal.show");

        new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(cartModal));

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


