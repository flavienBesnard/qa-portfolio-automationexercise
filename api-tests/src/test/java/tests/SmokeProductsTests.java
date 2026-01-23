package tests;

import core.config.ApiConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeProductsTests {
    /**
     * CT-ID : CT-API-001 EX-ID : EX-24
     *
     * <p>Objectif : Liste des produits disponible via API
     * <p>
     * Critères de réussite :
     * - Code HTTP = 200 (comportement de l'API)
     * - Body JSON : responseCode = 200
     * - La réponse contient une structure JSON valide
     * - La liste des produits est présente et non vide (au moins 1 produit est retourné)
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions : 1. Endpoint /api/productsList disponible
     */
    @Test(
            groups = {"api", "smoke"},
            description = "CT-API-001 / EX-24 / Liste des produits disponible via API")
    public void should_productsList_return_responseCode_200_and_products_not_empty() {
        // Préconditions
        String baseUrl = ApiConfig.apiBaseUrl();
        // Action
        Response response = given().baseUri(baseUrl).when().get("productsList");
        // Vérification
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.asString()).isNotBlank();
        JsonPath json =
                new JsonPath(response.asString()); // content type faux (text/html), parsing via body
        assertThat(json.getInt("responseCode")).isEqualTo(200);
        assertThat(json.getList("products")).isNotEmpty();
    }
}
