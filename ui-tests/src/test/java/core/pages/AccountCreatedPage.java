package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;

public class AccountCreatedPage extends BasePage {
 public static final By ACCOUNT_CREATED = By.cssSelector("[data-qa=\"account-created\"]");
    public AccountCreatedPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAccountCreated() {
        return isVisible(ACCOUNT_CREATED, Duration.ofSeconds(2));
    }
}
