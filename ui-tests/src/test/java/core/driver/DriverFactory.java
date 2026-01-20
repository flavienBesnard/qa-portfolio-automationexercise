package core.driver;

import core.config.Config;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     * DriverFactory :
     * - 1 driver par test (ThreadLocal) pour éviter les collisions entre tests.
     * - Local (ChromeDriver ou RemoteWebDriver).
     * - Options Chrome orentiées stabilité (désactivation password manager / notifications).
     */
    private DriverFactory() {}

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    public static void initDriver() {
        if (DRIVER.get() != null) {
            return;
        }

        ChromeOptions options = new ChromeOptions();

        // ✅ Désactive les popups Chrome liées aux mots de passe
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--guest");
        if (Config.headless()) {
            options.addArguments("--headless=new");
        }

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver;

        if (Config.remoteUrlOrNull() != null) {
            driver = new RemoteWebDriver(Config.remoteUrlOrNull(), options);
        } else {
            driver = new ChromeDriver(options);
        }

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver non initialisé. Appelle DriverFactory.initDriver() avant."
            );
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}