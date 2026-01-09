package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProductsPage extends BasePage {
    private static final By PRODUCTS_LIST = By.cssSelector(".features_items");
    private static final By VIEW_CART_LINK = By.cssSelector(".modal-body a[href=\"/view_cart\"]");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

public boolean isLoaded() {
        visible(PRODUCTS_LIST);
        return true;
}

private By addToCartByProductId(String productId) {
        return By.cssSelector("a.btn.btn-default.add-to-cart[data-product-id='" + productId + "']");
}

    public void addProductToCartById(String productId) {
         By AddToCartButton = addToCartByProductId(productId);
         click(AddToCartButton);

        // Preuve d'action : on attend l'apparition de la confirmation "Added" qui apparaît après "Add to cart"
        By cartModal = By.cssSelector("#cartModal, .modal-content");

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(cartModal));

    }



    public  CartPage viewCartFromModal() {
    click(VIEW_CART_LINK);
    return new CartPage(driver);
    }


}


