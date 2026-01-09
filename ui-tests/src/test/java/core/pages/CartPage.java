package core.pages;


import org.openqa.selenium.*;


public class CartPage extends BasePage {
    private static final By CART_TABLE = By.id("cart_info");
    private static final By CART_ROWS = By.cssSelector(".table.table-condensed tbody tr");
    private static final By FIRST_QTY = By.cssSelector(".table.table-condensed tbody tr .cart_quantity");
    private static final By BTN_PROCEED_TO_CHECKOUT = By.cssSelector("a.btn.btn-default.check_out");
    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        visible(CART_TABLE);
        return true;
    }
    public boolean hasAtLeastOneItem() {
        isLoaded();
        return driver.findElements(CART_ROWS).size() > 0;
    }

    public int firstItemQuantity() {
        isLoaded();
        String qtyText = visible(FIRST_QTY).getText().trim();
        return Integer.parseInt(qtyText);
    }

    public CheckoutPage proceedToCheckout() {
        isLoaded();
    click(BTN_PROCEED_TO_CHECKOUT);
    return new CheckoutPage(driver);
    }
}
