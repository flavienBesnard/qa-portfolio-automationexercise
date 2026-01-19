package core.pages;
import core.data.PaymentData;
import org.openqa.selenium.*;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentPage extends BasePage {

    private static final By PAYMENT_INFO = By.cssSelector(".payment-information");
    private static final By NAME_ON_CARD = By.cssSelector("[data-qa=\"name-on-card\"]");
    private static final By CARD_NUMBER = By.cssSelector("[data-qa=\"card-number\"]");
    private static final By CVC = By.cssSelector("[data-qa=\"cvc\"]");
    private static final By EXPIRATION_MONTH = By.cssSelector("[data-qa=\"expiry-month\"]");
    private static final By EXPIRATION_YEAR = By.cssSelector("[data-qa=\"expiry-year\"]");
    private static final By PAY_AND_CONFIRM_ORDER = By.cssSelector("[data-qa=\"pay-button\"]");
    private static final By ORDER_SUCCESS = By.cssSelector(".alert-success.alert");
    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(PAYMENT_INFO);
        visible(NAME_ON_CARD);
    }

    public void assertPaymentFormVisible() {
        assertLoaded();
        visible(NAME_ON_CARD);
        visible(CARD_NUMBER);
        visible(CVC);
        visible(EXPIRATION_MONTH);
        visible(EXPIRATION_YEAR);
        visible(PAY_AND_CONFIRM_ORDER);
    }

    public void fillMandatoryFields(PaymentData data) {
        assertLoaded();
        clearAndSendKeys(NAME_ON_CARD,data.getNameOnCard());
        clearAndSendKeys(CARD_NUMBER, data.getCardNumber());
        clearAndSendKeys(CVC, data.getCvc());
        clearAndSendKeys(EXPIRATION_MONTH,data.getExpiryMonth());
        clearAndSendKeys(EXPIRATION_YEAR,data.getExpiryYear());
    }

    public void payAndConfirmOrder() {
        assertLoaded();
        click(PAY_AND_CONFIRM_ORDER);
    }

    public void assertCardNumberIsRequiredOnSubmit() {
        assertLoaded();
        WebElement cardNumber = visible(CARD_NUMBER);
        String value = cardNumber.getAttribute("value");
        assertThat(value).isBlank();

        payAndConfirmOrder();
        assertLoaded();
        assertThat(isVisible(ORDER_SUCCESS)).isFalse();
        WebElement cardNumberAfter = visible(CARD_NUMBER);
        String msg = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].validationMessage;", cardNumberAfter);
        assertThat(msg).isNotBlank();


    }
}
