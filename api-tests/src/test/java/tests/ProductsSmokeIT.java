package tests;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.qameta.allure.*;

@Epic("API")
@Feature("Produits")
@Story("Liste des produits")
@Severity(SeverityLevel.NORMAL)
public class ProductsSmokeIT {

  @Test(groups = {"api", "smoke"}, description = "Smoke API — Vérifier que /productsList répond 200")
  public void products_endpoint_should_return_200() {
    String base = System.getProperty(
        "API_BASE_URL",
        System.getenv().getOrDefault("API_BASE_URL", "https://automationexercise.com/api")
    );

    Allure.step("Appeler GET /productsList sur " + base);
    given().baseUri(base)
      .when().get("/productsList")
      .then().statusCode(200);
  }
}