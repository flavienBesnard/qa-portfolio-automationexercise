package flows;

import core.config.Config;
import core.pages.HomePage;
import core.pages.LoginPage;
import org.openqa.selenium.WebDriver;

public class AuthFlow {
    /**
     * Flows : enchaînement d'actions réutilisatable (sans dépendre d'un autre test)
     */
    private AuthFlow() {
    }

    /**
     * Objectif : factoriser le login (précondition fréquente) et garder les tests lisibles.
     */
    public static HomePage loginAsTestUser(WebDriver driver) {
        HomePage home = new HomePage(driver).open();
        LoginPage login = home.goToLogin();
        login.login(Config.testUserEmail(), Config.testUserPassword());
        return home;
    }
}
