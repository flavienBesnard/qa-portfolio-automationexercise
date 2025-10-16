package tests;

import io.qameta.allure.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.net.URL;

@Epic("UI")
@Feature("Accueil")
@Story("Titre de la page")
@Severity(SeverityLevel.NORMAL)
public class HomeSmokeIT {

  WebDriver driver;

  @Test(groups = {"ui", "smoke"}, description = "Smoke UI — Le titre de la home contient 'automation'")
  public void home_title_contains_automationexercise() throws Exception {
    String remote = System.getProperty("SELENIUM_REMOTE_URL", System.getenv("SELENIUM_REMOTE_URL"));
    if (remote != null && !remote.isBlank()) {
      driver = new RemoteWebDriver(new URL(remote), new ChromeOptions());
    } else {
      driver = new ChromeDriver();
    }

    String base = System.getProperty(
        "BASE_URL",
        System.getenv().getOrDefault("BASE_URL", "https://automationexercise.com")
    );

    Allure.step("Ouvrir la page d'accueil: " + base);
    driver.get(base);
    Allure.step("Vérifier le titre");
    Assert.assertTrue(driver.getTitle().toLowerCase().contains("automation"));
  }

  @AfterMethod(alwaysRun = true)
  public void quit() { if (driver != null) driver.quit(); }
}