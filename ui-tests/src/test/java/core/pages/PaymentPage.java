package core.pages;
import org.openqa.selenium.*;


public class PaymentPage extends BasePage {

    private static final By PAYMENT_INFO = By.cssSelector(".payment-information");
    private static final By NAME_ON_CARD = By.cssSelector("[data-qa=\"name-on-card\"]");
    private static final By CARD_NUMBER = By.cssSelector("[data-qa=\"card-number\"]");
    private static final By CVC = By.cssSelector("[data-qa=\"cvc\"]");
    private static final By EXPIRATION_MONTH = By.cssSelector("[data-qa=\"expiry-month\"]");
    private static final By EXPIRATION_YEAR = By.cssSelector("[data-qa=\"expiry-year\"]");
    private static final By PAY_AND_CONFIRM_ORDER = By.cssSelector("[data-qa=\"pay-button\"]");
    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        visible(PAYMENT_INFO);
        visible(NAME_ON_CARD);
        return true;
    }

    public boolean hasPaymentForm() {
        isLoaded();
        visible(NAME_ON_CARD);
        visible(CARD_NUMBER);
        visible(CVC);
        visible(EXPIRATION_MONTH);
        visible(EXPIRATION_YEAR);
        visible(PAY_AND_CONFIRM_ORDER);
        return true;
    }
}
