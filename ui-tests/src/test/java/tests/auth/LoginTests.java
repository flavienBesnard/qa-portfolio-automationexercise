package tests.auth;

import core.pages.HomePage;
import core.pages.LoginPage;
import core.test.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Feature Auth - Login
 * Contient les tests de régression Auth
 */




public class LoginTests extends BaseTest {
    /**
     * CT-ID : CT-AUTH-002
     * EX-ID : EX-01
     *
     * Objectif : Connexion échouée avec mot de passe invalide
     * Critère de réussite : - La connexion échoue
     *                       - Un message d'erreur approprié est affiché ("Your email or password is incorrect")
     *                       - L'utilisateur reste sur la page de login
     *                       - Aucun accès au fonctionnalités réservées aux utilisateurs connectés n'est accordé
     *                       - Aucun comportement inattendu n'est observé
     */

    @Test(groups = {"ui","auth","regression"}, description = "CT-AUTH-002 / EX-01 / Login KO -> message d'erreur")

    public void login_with_invalid_credentials_shows_error() {

        HomePage home = new HomePage(driver()).open();

        LoginPage login = home.goToLogin();
        assertThat(login.isLoaded())
                .as("La page de login doit être chargé")
                .isTrue();
        login.login("emailInvalide@exemple.com","mdp_faux");

        assertThat(login.isInvalidCredentialsErrorVisible())
                .as("l'erreur 'Your email or password is incorrect' doit être visible")
                .isTrue();


    }


}
