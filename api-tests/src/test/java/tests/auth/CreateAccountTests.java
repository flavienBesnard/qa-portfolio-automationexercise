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

public class CreateAccountTests {

    /**
     * CT-ID : CT-API-004 EX-ID : EX-30
     *
     * <p>Objectif :  Création d'un compte utilisateur via l'API /createAccount
     * <p>
     * Critères de réussite :
     * - Code HTTP = 200 (comportement de l'API)
     * - Body JSON : responseCode = 201
     * - Body JSON : message contient "User created"
     * - Le compte est bien créé
     * - Une seconde tentative avec le même email provoque un message de type "Email already exists"
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Endpoint /api/createAccount disponible
     * 2. Générer un email unique (ex : via Faker)
     * 3. Préparer un body complet avec : name, email, password, title, birth_date, country etc)
     */
    @Test(groups = {"api", "auth", "regression"}, description = "CT-API-004 / EX-30 / Création d'un compte utilisateur via l'API /createAccount")
    public void should_createAccount_return_responseCode_201_and_user_created_message() {
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

        //Action
        try {


            Response responseCreate = given().baseUri(baseUrl)
                    .contentType("application/x-www-form-urlencoded")
                    .formParams(payload)
                    .when().post("createAccount");

            // Vérification
            String body = responseCreate.asString();

            assertThat(responseCreate.statusCode()).isEqualTo(200);
            assertThat(body).isNotBlank();

            JsonPath json = new JsonPath(body);

            assertThat(json.getInt("responseCode")).isEqualTo(201);
            assertThat(json.getString("message")).contains("User created");

            Response responseCreate2 = given().baseUri(baseUrl)
                    .contentType("application/x-www-form-urlencoded")
                    .formParams(payload)
                    .when().post("createAccount");
            String body2 = responseCreate2.asString();

            assertThat(responseCreate2.statusCode()).isEqualTo(200);
            assertThat(body2).isNotBlank();

            JsonPath json2 = new JsonPath(body2);

            assertThat(json2.getString("message")).contains("Email already exists");
        } finally {

            try {

                // cleanup
                Response responseDelete = given().baseUri(baseUrl)
                        .contentType("application/x-www-form-urlencoded")
                        .formParam("email", email).formParam("password", password)
                        .when().delete("deleteAccount");
                assertThat(responseDelete.statusCode()).isEqualTo(200);
            } catch (Exception ignored) {
                // le cleanup ne doit pas faire échouer le test
            }
        }

    }


}