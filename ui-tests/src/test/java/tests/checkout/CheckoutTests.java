package tests.checkout;

import core.data.SignupData;
import core.data.SignupDataFactory;
import core.pages.*;
import core.test.BaseTest;
import flows.SignupFlow;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTests extends BaseTest {

    /**
     * CT-ID : CT-CHECKOUT-003
     * EX-ID : EX-18
     *
     * Objectif : Affichage de l'adresse enregistrée au checkout
     * Critères de réussite : - Le nom de l'utilisateur est affiché correctement
     *                        - L'adresse (rue,ville, code postal et pays) est affichée et aucun champ attendu n'est vide
     *                        - L'adresse affichée correspond à celle enregistrée dans le compte
     *                        - Aucun comportement inattendu n'est observé
     *
     * Préconditions :   1. Utilisateur connecté
     *                   2. Une adresse complète est enregistrée dans le compte (nom,rue,ville,code postal..)
     *                   3. Panier contenant au moins un produit
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */

    @Test(groups = {"ui","checkout","regression"}, description = "CT-CHECKOUT-003 / EX-18 / Affichage de l'adresse enregistrée au checkout")

    public void checkout_should_display_saved_address() {
        //Préconditions
        SignupData data = SignupDataFactory.validDefaultUnique();
       HomePage home = SignupFlow.signupToHomeWithSignupData(driver(),data);
        assertThat(home.isLoggedIn()).isTrue();
        assertThat(home.nameAccountConnected()).isEqualTo(data.getName());
        ProductsPage products = home.goToProducts();
        products.addProductToCartById("1");
        // Action
        CartPage cart = products.viewCartFromModal();
        CheckoutPage checkout = cart.proceedToCheckout();
        // Vérification
        checkout.assertAddressDeliveryDisplayedIsCorrect(data);
        checkout.assertAddressDeliveryAndBillingAreTheSame();

        // Nettoyage
         HomePage homeAfter = checkout.goToHome();
         DeleteAccountPage delete = homeAfter.deleteAccount();
         assertThat(delete.isAccountDeleted()).isTrue();

    }

}
