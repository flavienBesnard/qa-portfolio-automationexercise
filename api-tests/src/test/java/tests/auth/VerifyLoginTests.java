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
     * - Code HTTP = 200 (comportement de l'API)
     * - Body JSON : responseCode = 200
     * - Body JSON : message contient "User exists"
     * - l'API confirme seulement l'existence d'un user
     * - Aucune connexon réelle n'est effectuée
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Un compte utilisateur valide existe dans le système
     * 2. Endpoint /api/verifyLogin disponible
     */

    @Test(groups = {"api", "regression", "auth"}, description = "CT-API-002 / EX-29 / Vérification d'existence d'un utilisateur (email + mot de passe corrects) via API")
    public void should_verifyLogin_return_responseCode_200_and_user_exists_message() {
        String baseUrl = ApiConfig.apiBaseUrl();
        // Action
        String email = ApiConfig.testUserEmail();
        String password = ApiConfig.testUserPassword();

        Response response = given().baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email).formParam("password", password)
                .when().post("verifyLogin");
        // Vérification
        String body = response.asString();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(body).isNotBlank();
        JsonPath json =
                new JsonPath(body);

        assertThat(json.getString("message")).contains("User exists");
        assertThat(json.getInt("responseCode")).isEqualTo(200);

    }

    /**
     * CT-ID : CT-API-008 EX-ID : EX-28
     *
     * <p>Objectif : Vérification login avec identifiants invalides
     * <p>
     * Critères de réussite :
     * - Code HTTP = 200 (comportement de l'API)
     * - Body JSON : responseCode = 404
     * - Body JSON : message contient "User not found"
     * - Aucun succès ni "User exists!" n'est retourné
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Endpoint /api/verifyLogin disponible
     * 2. Choisir un email/password qui n'existe pas
     */
    @Test(groups = {"api", "regression", "auth"}, description = "CT-API-008 / EX-28 / Vérification login avec identifiants invalides")
    public void should_verifyLogin_return_responseCode_404_and_user_not_found_message_when_credentials_invalid() {
        // Préconditions
        String baseUrl = ApiConfig.apiBaseUrl();
        String email = "emailInvalide@email.com";
        String password = "mdpfaux";
        // Actions
        Response response = given().baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email).formParam("password", password)
                .when().post("verifyLogin");

        // Vérification
        String body = response.asString();
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(body).isNotBlank();
        JsonPath json = new JsonPath(body);

        assertThat(json.getString("message")).contains("User not found");
        assertThat(json.getString("message")).doesNotContain("User exists");
        assertThat(json.getInt("responseCode")).isEqualTo(404);


    }
}
