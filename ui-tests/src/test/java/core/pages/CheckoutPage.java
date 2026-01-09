package core.pages;

import org.openqa.selenium.*;


public class CheckoutPage extends BasePage {
    private static final By CHECKOUT_INFO = By.cssSelector("[data-qa=\"checkout-info\"]");
    private static final By BTN_PLACE_ORDER = By.cssSelector(".btn.btn-default.check_out");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        visible(CHECKOUT_INFO);
        return true;
    }

    /**
     * placeOrder est l'action qui amène à la page de paiement
     */
    public PaymentPage placeOrder() {
        isLoaded();
    click(BTN_PLACE_ORDER);
    return new PaymentPage(driver);
    }



}
