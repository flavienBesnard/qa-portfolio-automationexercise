package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class LoginPage extends BasePage {
    private static final By EMAIL_INPUT =
            By.cssSelector("input[data-qa='login-email']");

    private static final By PASSWORD_INPUT =
            By.cssSelector("input[data-qa=\"login-password\"]");

    private static final By LOGIN_BUTTON =
            By.cssSelector("[data-qa=\"login-button\"]");

    private static final By INVALID_CREDS_ERROR =
            By.xpath("//*[contains(normalize-space(),'Your email or password is incorrect!')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(EMAIL_INPUT);
        visible(PASSWORD_INPUT);
        System.out.println("[STEP] assert loaded effectué");

    }

public void login(String email, String password) {
        assertLoaded();
    System.out.println("[STEP] goToLogin()");
    var emailInput = visible(EMAIL_INPUT);
    System.out.println("[STEP] emailInput initialisé");
    emailInput.clear();
    System.out.println("[STEP] emailInput clear");
    emailInput.sendKeys(email);

    var passwordInput = visible(PASSWORD_INPUT);
    passwordInput.clear();
    passwordInput.sendKeys(password);

    click(LOGIN_BUTTON);
}



public boolean isInvalidCredentialsErrorVisible() {
        assertLoaded();
        return isVisible(INVALID_CREDS_ERROR, Duration.ofSeconds(2));
}


}

