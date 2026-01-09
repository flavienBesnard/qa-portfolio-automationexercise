package tests;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import core.config.ApiConfig;
public class SmokeProductsTests {
  /**
   * CT-ID : CT-API-001
   * EX-ID : EX-24
   *
   * Objectif : Liste des produits disponible via API
   * Critères de réussite :     - Le code de réponse HTTP est 200
   *                            - La réponse contient une structure JSON valide
   *                            - La liste des produits est présente et non vide (au moins 1 produit est retourné)
   *                            - Aucun comportement inattendu n'est observé
   * Préconditions : 1. Endpoint /api/productsList disponible
   *
   *
   */

  @Test(groups = {"api", "smoke"}, description = "CT-API-001 / EX-24 / Liste des produits disponible via API")
  public void get_productsList_returns_200() {
   // Préconditions
    String baseUrl = ApiConfig.apiBaseUrl();
    // Action
    Response response = given().baseUri(baseUrl).when().get("/productsList");
    // Vérification
    assertThat(response.statusCode()).isEqualTo(200);
    JsonPath json = new JsonPath(response.asString()); // content type faux (text/html), parsing via body
    assertThat(json.getInt("responseCode")).isEqualTo(200);
    assertThat(json.getList("products")).isNotEmpty();



  }
}