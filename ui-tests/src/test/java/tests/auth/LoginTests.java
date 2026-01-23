package tests.auth;

import core.pages.HomePage;
import core.pages.LoginPage;
import core.test.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class LoginTests extends BaseTest {
    /**
     * CT-ID : CT-AUTH-002 EX-ID : EX-01
     * <p>
     * Objectif : Connexion échouée avec mot de passe invalide
     * <p>
     * Critère de réussite :
     * - La connexion échoue
     * - Un message d'erreur approprié est affiché ("Your email or password is incorrect")
     * - L'utilisateur reste sur la page de login
     * - Aucun accès au fonctionnalités réservées aux utilisateurs connectés n'est accordé
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions :
     * 1. Le compte utilisateur existe avec un email connu
     * 2. L'utilisateur n'est pas connecté
     * <p>
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(
            groups = {"ui", "auth", "regression"},
            description = "CT-AUTH-002 / EX-01 / Login KO -> message d'erreur")
    public void login_with_invalid_credentials_shows_error() {
        // Préconditions
        HomePage home = new HomePage(driver()).open();

        // Action
        LoginPage login = home.goToLogin();
        login.login("emailInvalide@exemple.com", "mdp_faux");

        // Vérification
        assertThat(login.isInvalidCredentialsErrorVisible())
                .as("l'erreur 'Your email or password is incorrect' doit être visible")
                .isTrue();
    }
}
