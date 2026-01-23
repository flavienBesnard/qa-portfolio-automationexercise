package tests.auth;

import core.data.SignupData;
import core.data.SignupDataFactory;
import core.pages.AccountCreatedPage;
import core.pages.DeleteAccountPage;
import core.pages.HomePage;
import core.test.BaseTest;
import flows.SignupFlow;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignupTests extends BaseTest {

    /**
     * CT-ID : CT-SIGNUP-001 EX-ID : EX-04
     * <p>
     * Objectif : Création de compte avec des données valides
     * Critères de réussite : - Le compte est créé avec succès
     * - L'utilisateur est automatiquement connecté
     * - Aucun comportement inattendu n'est observé
     * <p>
     * Préconditions : 1. L'utilisateur n'est pas connecté
     * 2. Adresse email unique
     * <p>
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(
            groups = {"ui", "signup", "regression"},
            description = "CT-SIGNUP-001 / EX-04 / Création de compte avec des données valides")
    public void sign_up_with_valid_data() {
        // Préconditions
        SignupData data = SignupDataFactory.validDefaultUnique();
        // Action
        AccountCreatedPage accountCreated =
                SignupFlow.signupToAccountCreatedWithSignupData(driver(), data);

        // Vérification
        assertThat(accountCreated.isAccountCreated()).isTrue();
        HomePage homeAfter = accountCreated.goToHome();
        assertThat(homeAfter.isLoggedIn()).isTrue();
        assertThat(homeAfter.nameAccountConnected()).isEqualTo(data.getName());

        // Nettoyage
        DeleteAccountPage deleteAccount = homeAfter.deleteAccount();
        assertThat(deleteAccount.isAccountDeleted()).isTrue();
    }
}
