package tests.smoke;

import core.test.BaseTest;
import core.config.Config;
import core.pages.HomePage;
import core.pages.LoginPage;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SmokeAuthTests extends BaseTest {
    /**
     * CT-ID : CT-AUTH-001
     * EX-ID : EX-02
     *
     * Objectif : Connexion valide avec un compte existant
     * Critères de réussite :    - l'utilisateur est connecté et redirigé vers la page d'accueil
     *                          - Aucun comportement inattendu n'est observé
     *
     * Préconditions : 1. Un compte utilisateur existe sur le site avec email/mot de passe valide
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */


    @Test(groups = {"ui","auth","smoke"}, description = "CT-AUTH-001 / EX-02 / Login OK -> Connexion valide avec un compte existant")

    public void login_with_good_credentials_is_ok() {
//Action : On va sur la page de login, et on se connecte avec les identifiants renseignés en variables d'environnement
    HomePage home = new HomePage(driver()).open();
    LoginPage login = home.goToLogin();

    login.login(Config.testUserEmail(),Config.testUserPassword());

    // Vérification : On vérifie que l'utilisateur est connecté
    assertThat(home.isLoggedIn())
            .as("l'utilisateur est connecté")
            .isTrue();

    }
}
