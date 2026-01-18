package tests.auth;

import core.data.SignupData;
import core.data.SignupDataFactory;
import core.pages.*;
import core.test.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignupTests extends BaseTest {


    /**
     * CT-ID : CT-SIGNUP-001
     * EX-ID : EX-04
     *
     * Objectif : Création de compte avec des données valides
     * Critères de réussite :  - Le compte est créé avec succès
     *                         - L'utilisateur est automatiquement connecté
     *                         - Le nouvel utilisateur apparaît désormais comme existant (visible via l'API verifyLogin / impossible de recréer le même compte)
     *                         - Aucun comportement inattendu n'est observé
     *
     * Préconditions : 1. L'utilisateur n'est pas connecté
     *                 2. L'adresse email utilisée pour l'inscription n'existe pas encore dans le système
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */

   @Test(groups = {"ui","signup","regression"},description = "CT-SIGNUP-001 / EX-04 / Création de compte avec des données valides")
   public void sign_up_with_valid_data() {
       SignupData data = SignupDataFactory.validDefaultUnique();
        HomePage home = new HomePage(driver()).open();
        LoginPage login = home.goToLogin();
       AccountInformationPage informationPage =  login.signup(data.getName(), data.getEmail());
       informationPage.assertLoaded();
       informationPage.fillMandatoryFields(data);
      AccountCreatedPage account = informationPage.submitCreateAccount();
       assertThat(account.isAccountCreated()).isTrue();
       HomePage homeAfter = account.goToHome();
       assertThat(homeAfter.isLoggedIn()).isTrue();
       assertThat(homeAfter.nameAccountConnected()).isEqualTo(data.getName());
       DeleteAccountPage deleteAccount =  home.deleteAccount();
       assertThat(deleteAccount.isAccountDeleted()).isTrue();

   }



}
