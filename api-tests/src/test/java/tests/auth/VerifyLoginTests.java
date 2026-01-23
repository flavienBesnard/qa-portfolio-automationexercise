package tests.auth;

import core.config.ApiConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class VerifyLoginTests {
    /**
     * CT-ID : CT-API-002 EX-ID : EX-29
     *
     * <p>Objectif : Vérification d'existence d'un utilisateur (email + mot de passe corrects) via API
     * <p>
     * Critères de réussite :
     * - Le code HTTP = 200
     * - Le corps de réponse contient "User exists !"
     * - l'API confirme seulement l'existence d'un user
     * - Aucune connexon réelle n'est effectuée
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Un compte utilisateur valide existe dans le système
     * 2. Endpoint /api/verifyLogin disponible
     */

    @Test(groups = {"api", "regression", "auth"}, description = "CT-API-002 / EX-29 / Vérification d'existence d'un utilisateur (email + mot de passe corrects) via API")
    public void should_verifyLogin_return_200_and_user_exists_message() {
        String baseUrl = ApiConfig.apiBaseUrl();
        // Action
        String email = ApiConfig.testUserEmail();
        String password = ApiConfig.testUserPassword();
        
        Response response = given().baseUri(baseUrl).contentType("application/x-www-form-urlencoded").formParam("email", email).formParam("password", password).when().post("verifyLogin");
        String body = response.asString();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(body).isNotBlank();
        // Vérification
        JsonPath json =
                new JsonPath(body);

        assertThat(json.getString("message")).contains("User exists");
        assertThat(json.getInt("responseCode")).isEqualTo(200);

    }
}
