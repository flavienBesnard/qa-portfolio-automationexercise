package core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DeleteAccountPage extends BasePage {
    private static final By ACCOUNT_DELETE_CONFIRMATION =
            By.cssSelector("[data-qa=\"account-deleted\"]");

    public DeleteAccountPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAccountDeleted() {
        return visible(ACCOUNT_DELETE_CONFIRMATION).isDisplayed();
    }
}
