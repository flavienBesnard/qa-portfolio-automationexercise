package core.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * BasePage : socle technique POM Centralise waits + actions génériques pour réduire la duplication
 * et améliorer la stabilité (site public : overlays / pub)
 */
public abstract class BasePage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(2);
    private static final By CONSENT_DIALOG = By.cssSelector(".fc-dialog-container");
    private static final By CONSENT_ACCEPT_BTN =
            By.cssSelector(
                    "button.fc-button.fc-cta-consent.fc-primary-button, button[aria-label=\"Autoriser\"]");
    private static final By HOME_LINK = By.cssSelector(".shop-menu.pull-right a[href=\"/\"]");
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    // ouvre une URL
    protected void open(String url) {

        driver.get(url);
    }

    protected WebElement visible(By locator) {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {

        return new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected boolean exists(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    protected boolean isVisible(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected boolean isVisible(By locator) {
        try {
            new WebDriverWait(driver, SHORT_TIMEOUT)
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
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
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            Boolean inView =
                    (Boolean)
                            js.executeScript(
                                    """
                                                const r = arguments[0].getBoundingClientRect();
                                                const vh = window.innerHeight || document.documentElement.clientHeight;
                                                return r.top >= 0 && r.bottom <= vh;
                                            """,
                                    el);

            if (Boolean.TRUE.equals(inView)) return;

            js.executeScript(
                    """
                                arguments[0].scrollIntoView({block:'center', inline:'nearest'});
                                // compense un header sticky éventuel (ajuste -80 si besoin)
                                window.scrollBy(0, -80);
                            """,
                    el);

        } catch (Exception ignored) {
        }
    }

    protected boolean dismissGoogleVignetteLight() {
        try {
            String href =
                    (String) ((JavascriptExecutor) driver).executeScript("return window.location.href");
            if (href == null || !href.contains("google_vignette")) return false;

            ((JavascriptExecutor) driver)
                    .executeScript(
                            """
                                      const href = window.location.href;
                                      if (!href.includes('google_vignette')) return;
                                      const clean = href.split('#')[0];
                                      history.replaceState(null, '', clean);
                                    """);

            dismissAdOverlaysBestEffort();
            driver.switchTo().defaultContent();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    protected boolean dismissGoogleVignetteIfPresent() {
        boolean cleaned = dismissGoogleVignetteLight();
        if (cleaned) {
            driver.switchTo().defaultContent();
            waitForDomStable();
        }
        return cleaned;
    }

    protected void waitForDomStable() {
        try {
            new WebDriverWait(driver, SHORT_TIMEOUT)
                    .until(
                            d -> {
                                try {
                                    String rs =
                                            (String) ((JavascriptExecutor) d).executeScript("return document.readyState");
                                    return "interactive".equals(rs) || "complete".equals(rs);
                                } catch (Exception e) {
                                    return true; // best effort
                                }
                            });

            new WebDriverWait(driver, SHORT_TIMEOUT)
                    .until(
                            d -> {
                                try {
                                    String href =
                                            (String)
                                                    ((JavascriptExecutor) d).executeScript("return window.location.href");
                                    return href == null || !href.contains("google_vignette");
                                } catch (Exception e) {
                                    return true;
                                }
                            });
        } catch (TimeoutException ignored) {
        }
    }

    protected void dismissAdOverlaysBestEffort() {
        try {
            ((JavascriptExecutor) driver)
                    .executeScript(
                            """
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
        } catch (Exception ignored) {
        }
    }

    /**
     * Ferme l'overlay de consentement si présent Le test doit rester stable, si absent on ne fait
     * rien
     */
    protected void dismissConsentIfPresent() {

        if (driver.findElements(CONSENT_DIALOG).isEmpty()) {
            return;
        }
        try {
            WebDriverWait w = new WebDriverWait(driver, SHORT_TIMEOUT);
            WebElement btn = w.until(ExpectedConditions.elementToBeClickable(CONSENT_ACCEPT_BTN));
            btn.click();
            w.until(ExpectedConditions.invisibilityOfElementLocated(CONSENT_DIALOG));

        } catch (RuntimeException ignored) {

        }
    }

    /**
     * Solution contre le google_vignette qui faisait dysfonctionner les tests
     *
     * @param locator
     * @param expectedUrlPart
     */
    protected void clickAndWaitUrlContainsFast(By locator, String expectedUrlPart) {

        for (int attempt = 1; attempt <= 2; attempt++) {

            // pre-clean
            dismissGoogleVignetteLight();

            // IMPORTANT: before APRÈS pre-clean
            String before = currentHrefSafe();

            // click
            WebElement el = clickable(locator);
            scrollIntoViewCentered(el);

            try {
                safeClick(el);
            } catch (Exception e) {
                dismissGoogleVignetteIfPresent();
                if (attempt == 2) throw e;
                continue;
            }

            // étape 1: fail fast (nav démarre ?)
            boolean started = waitUrlChangeOrContains(before, expectedUrlPart, Duration.ofMillis(1200));
            if (!started) {
                dismissGoogleVignetteIfPresent();
                if (attempt == 2) {
                    throw new TimeoutException(
                            "Navigation non démarrée après click (probablement click bloqué / scroll / overlay).");
                }
                continue;
            }

            // étape 2: atteindre la cible (retry si timeout)
            try {
                waitUntilUrlContains(expectedUrlPart, Duration.ofSeconds(6));
                return;
            } catch (TimeoutException e) {
                if (attempt == 2) throw e;
                // retry
            }
        }
    }

    protected void safeClick(WebElement el) {
        try {
            el.click();
        } catch (Exception e) {
            // fallback JS (utile si overlay léger, sticky header, etc.)
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private boolean waitUrlChangeOrContains(String before, String expectedPart, Duration timeout) {
        try {
            WebDriverWait w = new WebDriverWait(driver, timeout);
            w.pollingEvery(Duration.ofMillis(150));
            return w.until(
                    d -> {
                        dismissGoogleVignetteLight();
                        String now = currentHrefSafe();
                        if (now == null) return false;
                        return (expectedPart != null && now.contains(expectedPart))
                                || (before != null && !now.equals(before));
                    });
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void waitUntilUrlContains(String expectedPart, Duration timeout) {
        WebDriverWait w = new WebDriverWait(driver, timeout);
        w.pollingEvery(Duration.ofMillis(200));
        w.until(
                d -> {
                    dismissGoogleVignetteLight();
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

    protected void clickAndWaitVisibleFast(By clickLocator, By marker, Duration timeout) {
        for (int attempt = 1; attempt <= 2; attempt++) {

            // pre-clean
            dismissConsentIfPresent();
            dismissGoogleVignetteLight();
            dismissAdOverlaysBestEffort();

            // click (ton click() a déjà 1 retry + JS fallback)
            click(clickLocator);

            // wait marker (avec nettoyage pendant l'attente)
            boolean ok = waitVisibleWithCleanup(marker, timeout);
            if (ok) return;

            // retry heavy si ça n'a pas marché
            dismissGoogleVignetteIfPresent();
            dismissAdOverlaysBestEffort();

            if (attempt == 2) {
                throw new TimeoutException("Home marker not visible after click: " + marker);
            }
        }
    }

    protected boolean waitVisibleWithCleanup(By marker, Duration timeout) {
        try {
            WebDriverWait w = new WebDriverWait(driver, timeout);
            w.pollingEvery(Duration.ofMillis(200));
            return w.until(
                    d -> {
                        dismissGoogleVignetteLight();
                        dismissAdOverlaysBestEffort();
                        dismissConsentIfPresent();
                        List<WebElement> els = d.findElements(marker);
                        return !els.isEmpty() && els.get(0).isDisplayed();
                    });
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * click standard Permet de contourner les vignettes de pub google si elles sont présentes
     * (#google_vignette). Si le clic n'a pas fonctionné on nettoie puis on réessaie.
     */
    protected void click(By locator) {

        try {

            dismissGoogleVignetteLight();
            WebElement el = clickable(locator);
            scrollIntoViewCentered(el);
            safeClick(el);

        } catch (StaleElementReferenceException
                 | ElementClickInterceptedException
                 | org.openqa.selenium.TimeoutException e) {
            // un seul retry
            dismissGoogleVignetteIfPresent();
            WebElement el = clickable(locator);
            scrollIntoViewCentered(el);
            safeClick(el);
        }
    }

    public void clearAndSendKeys(By locator, String value) {
        if (value == null) throw new IllegalArgumentException("la valeur ne doit pas être nulle");
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(value);
    }

    public HomePage goToHome() {
        //  click(HOME_LINK);
        // clickAndWaitUrlContainsFast(HOME_LINK,"/");
        clickAndWaitVisibleFast(HOME_LINK, HomePage.HOME_CAROUSEL, Duration.ofSeconds(8));
        HomePage home = new HomePage(driver);
        home.assertLoaded();
        return home;
    }

    // Renvoie le titre de la page
    protected String title() {
        return driver.getTitle();
    }
}
