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
        dismissGoogleVignetteIfPresent();

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


    protected boolean dismissGoogleVignetteIfPresent() {
        try {
            String href = (String) ((JavascriptExecutor) driver)
                    .executeScript("return window.location.href");

            if (href == null || !href.contains("google_vignette")) return false;

            // Nettoyage hash sans reload
            ((JavascriptExecutor) driver).executeScript("""
            const href = window.location.href;
            if (!href.includes('google_vignette')) return;
            const clean = href.split('#')[0];
            history.replaceState(null, '', clean);
        """);

            // Best effort: enlever les overlays
            dismissAdOverlaysBestEffort();

            // Resync "on reprend la main"
            driver.switchTo().defaultContent();
            waitForDomStable();
            return true;

        } catch (Exception ignored) {
            return false;
        }
    }

    protected void waitForDomStable() {
        try {
            new WebDriverWait(driver, SHORT_TIMEOUT).until(d -> {
                try {
                    String rs = (String) ((JavascriptExecutor) d).executeScript("return document.readyState");
                    return "interactive".equals(rs) || "complete".equals(rs);
                } catch (Exception e) {
                    return true; // best effort
                }
            });

            new WebDriverWait(driver, SHORT_TIMEOUT).until(d -> {
                try {
                    String href = (String) ((JavascriptExecutor) d).executeScript("return window.location.href");
                    return href == null || !href.contains("google_vignette");
                } catch (Exception e) {
                    return true;
                }
            });
        } catch (TimeoutException ignored) {}
    }

    protected void dismissAdOverlaysBestEffort() {
        try {
            ((JavascriptExecutor) driver).executeScript("""
            const selectors = [
              "iframe[id^='aswift_']",
              "iframe[id^='google_ads_iframe']",
              "iframe[src*='googleads']",
              "iframe[src*='doubleclick']",
              "ins.adsbygoogle"
            ];
            selectors.forEach(sel => document.querySelectorAll(sel).forEach(el => {
              el.style.display='none';
              el.style.visibility='hidden';
              el.style.pointerEvents='none';
              try { el.remove(); } catch(e) {}
            }));
        """);
        } catch (Exception ignored) {}
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
     * Solution contre le google_vignette qui faisait dysfonctionner les tests
     * @param locator
     * @param expectedUrlPart
     */
    protected void clickAndWaitUrlContainsFast(By locator, String expectedUrlPart) {
        String before = currentHrefSafe();

        for (int attempt = 1; attempt <= 2; attempt++) {
            // pre-clean
            dismissConsentIfPresent();
            dismissGoogleVignetteIfPresent();
           // dismissAdOverlaysBestEffort();

            // click
            WebElement el = clickable(locator);
            scrollIntoViewCentered(el);

            try {
                el.click();
            } catch (Exception e) {
                // click intercepté => clean + retry
                dismissGoogleVignetteIfPresent();
            //    dismissAdOverlaysBestEffort();
                if (attempt == 2) throw e;
                continue;
            }

            // ÉTAPE 1 : attendre TRÈS court que "quelque chose" se passe (URL change)
            boolean started = waitUrlChangeOrContains(before, expectedUrlPart, Duration.ofMillis(1200));
            if (!started) {
                // navigation n'a pas démarré => probablement bloqué par vignette/overlay
                dismissGoogleVignetteIfPresent();
              //  dismissAdOverlaysBestEffort();
                if (attempt == 2) {
                    throw new org.openqa.selenium.TimeoutException(
                            "La navigation n'a pas commencé après le click (bloqué par google_vignette ou overlay)");
                }
                continue;
            }

            // ÉTAPE 2 : navigation démarrée => wait normal mais plus court qu'un timeout global
            waitUntilUrlContains(expectedUrlPart, Duration.ofSeconds(6));
            return;
        }
    }

    private boolean waitUrlChangeOrContains(String before, String expectedPart, Duration timeout) {
        try {
            WebDriverWait w = new WebDriverWait(driver, timeout);
            w.pollingEvery(Duration.ofMillis(150));
            return w.until(d -> {
                dismissGoogleVignetteIfPresent();
              //  dismissAdOverlaysBestEffort();
                String now = currentHrefSafe();
                if (now == null) return false;
                return (expectedPart != null && now.contains(expectedPart)) || (before != null && !now.equals(before));
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void waitUntilUrlContains(String expectedPart, Duration timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout);
        w.pollingEvery(Duration.ofMillis(200));
        w.until(d -> {
            dismissGoogleVignetteIfPresent();
          //  dismissAdOverlaysBestEffort();
            String now = currentHrefSafe();
            return now != null && now.contains(expectedPart);
        });
    }

    private String currentHrefSafe() {
        try {
            return (String) ((JavascriptExecutor) driver).executeScript("return window.location.href");
        } catch (Exception e) {
            return driver.getCurrentUrl();
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
    dismissGoogleVignetteIfPresent();
    el.click();

} catch (StaleElementReferenceException | ElementClickInterceptedException | org.openqa.selenium.TimeoutException e) {

    // un seul retry
    dismissGoogleVignetteIfPresent();
    WebElement el = clickable(locator);
    scrollIntoViewCentered(el);
    el.click();

     }


}








    // Renvoie le titre de la page
    protected String title() {
        return driver.getTitle();
    }




}
