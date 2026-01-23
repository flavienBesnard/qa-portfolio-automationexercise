package flows;

import core.data.SignupData;
import core.pages.AccountCreatedPage;
import core.pages.AccountInformationPage;
import core.pages.HomePage;
import core.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class SignupFlow {
    /**
     * Flows : enchaînement d'actions réutilisatable (sans dépendre d'un autre test)
     */
    private SignupFlow() {
    }

    /**
     * Objectif : factoriser le signup et garder les tests lisibles.
     */
    public static AccountCreatedPage signupToAccountCreatedWithSignupData(
            WebDriver driver, SignupData data) {
        HomePage home = new HomePage(driver).open();
        LoginPage login = home.goToLogin();
        AccountInformationPage accountInfo = login.signup(data.getName(), data.getEmail());
        accountInfo.assertLoaded();
        accountInfo.fillMandatoryFields(data);
        AccountCreatedPage accountCreated = accountInfo.submitCreateAccount();
        accountCreated.assertLoaded();
        return accountCreated;
    }

    public static HomePage signupToHomeWithSignupData(WebDriver driver, SignupData data) {
        AccountCreatedPage accountCreated = signupToAccountCreatedWithSignupData(driver, data);
        HomePage homeAfter = accountCreated.goToHome();
        homeAfter.assertLoaded();
        return homeAfter;
    }
}
