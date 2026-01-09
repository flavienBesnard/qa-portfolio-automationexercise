package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
/**
 * BasePage : socle technique POM
 * Centralise waits + actions génériques pour réduire la duplication et améliorer la stabilité (site public : overlays / pub)
 *
 */
public abstract class BasePage {


    protected final WebDriver driver;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(2);

    protected BasePage(WebDriver driver) {
        this.driver = driver;

    }
    // ouvre une URL
    protected void open(String url) {

        driver.get(url);
    }

    protected WebElement visible(By locator) {
        return new WebDriverWait(driver,DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    protected WebElement clickable(By locator) {

        return new WebDriverWait(driver,DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean exists(By locator) {
        return !driver.findElements(locator).isEmpty();
    }



    protected boolean isVisible(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected List<WebElement> all(By locator) {
        return driver.findElements(locator);
    }

// --- SCROLL ---
    protected void scrollIntoViewCentered(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center', inline:'nearest'});", el);
    }

    protected void scrollIntoViewCentered(By locator) {
        WebElement el = visible(locator);
        scrollIntoViewCentered(el);
    }




    // on recharge la même URL sans #google_vignette pour retrouver la page normale
    protected void dismissGoogleVignetteIfPresent() {
        String url = driver.getCurrentUrl();
        if(url != null && url.contains("#google_vignette")) {
            driver.get(url.replace("#google_vignette",""));
        }

    }


    /**
     * Ferme l'overlay de consentement si présent
     * Le test doit rester stable, si absent on ne fait rien
     */
    protected void dismissConsentIfPresent() {

        By consent_dialog = By.cssSelector(".fc-dialog-container");
        By consent_accept_button = By.cssSelector("button.fc-button.fc-cta-consent.fc-primary-button, button[aria-label=\"Autoriser\"]");

if (driver.findElements(consent_dialog).isEmpty()) {
    return;
}
        try {
    WebDriverWait w = new WebDriverWait(driver, SHORT_TIMEOUT);
    WebElement btn = w.until(ExpectedConditions.elementToBeClickable(consent_accept_button));
    btn.click();
w.until(ExpectedConditions.invisibilityOfElementLocated(consent_dialog));

        } catch (RuntimeException ignored) {

        }
    }




    /**
     *  click standard
     *  Permet de contourner les vignettes de pub google si elles sont présentes (#google_vignette).
     *  Si le clic n'a pas fonctionné on nettoie puis on réessaie.
     *
     */


    protected void click(By locator) {

try {

      dismissGoogleVignetteIfPresent();
        WebElement el = clickable(locator);
        scrollIntoViewCentered(el);
            el.click();
            return;
        } catch (StaleElementReferenceException | ElementClickInterceptedException first) {
    // un seul retry
    dismissGoogleVignetteIfPresent();
    WebElement el = clickable(locator);
    scrollIntoViewCentered(el);
    el.click();
    return;
}


}






    // Renvoie le titre de la page
    protected String title() {
        return driver.getTitle();
    }






}
