package core.pages;

import core.config.Config;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


/**
 * HomePage d'automationExercise
 * Responsabilité :
 * - ouvrir la home
 * - navigations depuis la home (ex : aller au login)
 */

public class HomePage extends BasePage {

    private static final By BODY = By.tagName("body");
    private static final By LOGIN_LINK = By.cssSelector("a[href=\"/login\"]");
    private static final By LOGOUT_LINK = By.cssSelector("a[href=\"/logout\"]");
    private static final By CART_LINK = By.cssSelector("a[href=\"/view_cart\"]");
   // private static final By CONSENT_DIALOG = By.cssSelector("div.fc-dialog-scrollable-content");
    //private static final By CONSENT_ACCEPT_BUTTON = By.cssSelector("button.fc-button fc-cta-consent fc-primary-button, button[aria-label=\"Autoriser\"]");
    private static final By PRODUCTS_LINK = By.cssSelector("a[href=\"/products\"]");
    private static final By PRODUCTS_LIST = By.cssSelector(".features_items");
    public HomePage(WebDriver driver) {
        super(driver);
    }


   //  Ouvre la page d'accueil et attend que le body soit visible (signe de chargement)

    public HomePage open() {
      open(Config.baseUrl());
      visible(BODY);
      // ferme l'overlay de consentement si présent dès l'ouverture de la page
      dismissConsentIfPresent();
      dismissGoogleVignetteIfPresent();
      return this;
    }

/**
 * Navigation : aller sur la page login
 */

public LoginPage goToLogin() {
    clickAndWaitUrlContainsFast(LOGIN_LINK,"/login");
    return new LoginPage(driver);
}

    public ProductsPage goToProducts() {

        clickAndWaitUrlContainsFast(PRODUCTS_LINK,"/products");
        return new ProductsPage(driver);
    }



    public CartPage goToCart() {
        clickAndWaitUrlContainsFast(CART_LINK,"/view_cart");
        return new CartPage(driver);
    }

// si logout visible, alors l'utilisateur est connecté
public boolean isLoggedIn() {
    return isVisible(LOGOUT_LINK, Duration.ofSeconds(2));
}


public boolean titleContains(String text) {
    return title() != null && title().toLowerCase().contains(text.toLowerCase());
}
}
