package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final By EMAIL_INPUT_LOGIN = By.cssSelector("input[data-qa='login-email']");

    private static final By PASSWORD_INPUT_LOGIN =
            By.cssSelector("input[data-qa=\"login-password\"]");

    private static final By LOGIN_BUTTON = By.cssSelector("[data-qa=\"login-button\"]");

    private static final By INVALID_CREDS_ERROR =
            By.xpath("//*[contains(normalize-space(),'Your email or password is incorrect!')]");

    private static final By NAME_INPUT_SIGNUP = By.cssSelector("input[data-qa=\"signup-name\"]");

    private static final By EMAIL_INPUT_SIGNUP = By.cssSelector("input[data-qa=\"signup-email\"]");

    private static final By SIGN_UP_BTN = By.cssSelector("[data-qa=\"signup-button\"]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(EMAIL_INPUT_LOGIN);
        visible(PASSWORD_INPUT_LOGIN);
    }

    public void login(String email, String password) {
        assertLoaded();
        clearAndSendKeys(EMAIL_INPUT_LOGIN, email);
        clearAndSendKeys(PASSWORD_INPUT_LOGIN, password);

        click(LOGIN_BUTTON);
    }

    public AccountInformationPage signup(String name, String email) {
        assertLoaded();
        clearAndSendKeys(NAME_INPUT_SIGNUP, name);
        clearAndSendKeys(EMAIL_INPUT_SIGNUP, email);

        clickAndWaitUrlContainsFast(SIGN_UP_BTN, "/signup");
        AccountInformationPage accountInfo = new AccountInformationPage(driver);
        accountInfo.assertLoaded();
        return accountInfo;
    }

    public boolean isInvalidCredentialsErrorVisible() {
        assertLoaded();
        return isVisible(INVALID_CREDS_ERROR, Duration.ofSeconds(2));
    }
}
