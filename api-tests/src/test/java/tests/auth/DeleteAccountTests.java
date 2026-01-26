package tests.auth;

import core.config.ApiConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.datafaker.Faker;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteAccountTests {

    /**
     * CT-ID : CT-API-005 EX-ID : EX-31
     *
     * <p>Objectif :  Suppression d'un compte utilisateur via l'API /deleteAccount
     * <p>
     * Critères de réussite :
     * - Code HTTP = 200 (comportement de l'API)
     * - Body JSON : responseCode = 200
     * - Body JSON : message contient "Account deleted"
     * - Si l'on retente une suppression avec le même email -> message "Account not found"
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Endpoint /api/deleteAccount disponible
     * 2. Disposer  d'un compte existant à supprimer (créé via CT-API-004 ou préexistant)
     */
    @Test(groups = {"api", "auth", "regression"}, description = "CT-API-005 / EX-31 / Suppression d'un compte utilisateur via l'API /deleteAccount")
    public void should_deleteAccount_return_responseCode_200_and_account_deleted_message() {
        // Préconditions
        String baseUrl = ApiConfig.apiBaseUrl();
        Faker faker = new Faker();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String email = "qa+" + timestamp + "@example.com";
        String name = "qa+" + timestamp;
        String password = "qa+" + timestamp + "!!";
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("name", name);
        payload.put("email", email);
        payload.put("password", password);
        payload.put("title", "Mr");
        payload.put("birth_date", "10");
        payload.put("birth_month", "2");
        payload.put("birth_year", "1990");
        payload.put("firstname", faker.name().firstName());
        payload.put("lastname", faker.name().lastName());
        payload.put("company", faker.company().name());
        payload.put("address1", faker.address().streetAddress());
        payload.put("address2", faker.address().secondaryAddress());
        payload.put("country", "Canada");
        payload.put("zipcode", faker.address().zipCode());
        payload.put("state", faker.address().state());
        payload.put("city", faker.address().city());
        payload.put("mobile_number", faker.number().digits(10));


        Response responseCreate = given().baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded")
                .formParams(payload)
                .when().post("createAccount");
        String body = responseCreate.asString();
        assertThat(responseCreate.statusCode()).isEqualTo(200);
        assertThat(body).isNotBlank();
        JsonPath json = new JsonPath(body);
        assertThat(json.getInt("responseCode")).isEqualTo(201);
        assertThat(json.getString("message")).contains("User created");
        // Action
        Response responseDelete = given().baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email).formParam("password", password)
                .when().delete("deleteAccount");
        // Vérification
        String bodyDelete = responseDelete.asString();
        assertThat(responseDelete.statusCode()).isEqualTo(200);
        assertThat(bodyDelete).isNotBlank();
        JsonPath jsonDelete = new JsonPath(bodyDelete);

        assertThat(jsonDelete.getInt("responseCode")).isEqualTo(200);
        assertThat(jsonDelete.getString("message")).contains("Account deleted");


        Response responseDelete2 = given().baseUri(baseUrl)
                .contentType("application/x-www-form-urlencoded")
                .formParam("email", email).formParam("password", password)
                .when().delete("deleteAccount");
        String bodyDelete2 = responseDelete2.asString();
        assertThat(responseDelete2.statusCode()).isEqualTo(200);
        assertThat(bodyDelete2).isNotBlank();

        JsonPath jsonDelete2 = new JsonPath(bodyDelete2);
        assertThat(jsonDelete2.getString("message")).contains("Account not found");


    }


}
