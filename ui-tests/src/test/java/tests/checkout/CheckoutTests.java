package tests.checkout;

import core.data.PaymentData;
import core.data.PaymentDataFactory;
import core.data.SignupData;
import core.data.SignupDataFactory;
import core.pages.*;
import core.test.BaseTest;
import flows.AuthFlow;
import flows.SignupFlow;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutTests extends BaseTest {

    /**
     * CT-ID : CT-CHECKOUT-003 EX-ID : EX-18
     *
     * <p>Objectif : Affichage de l'adresse enregistrée au checkout Critères de réussite : - Le nom de
     * l'utilisateur est affiché correctement - L'adresse (rue,ville, code postal et pays) est
     * affichée et aucun champ attendu n'est vide - L'adresse affichée correspond à celle enregistrée
     * dans le compte - Aucun comportement inattendu n'est observé
     *
     * <p>Préconditions : 1. Utilisateur connecté 2. Une adresse complète est enregistrée dans le
     * compte (nom,rue,ville,code postal..) 3. Panier contenant au moins un produit
     *
     * <p>Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme
     * de contournement présent dans le code
     */
    @Test(
            groups = {"ui", "checkout", "regression"},
            description = "CT-CHECKOUT-003 / EX-18 / Affichage de l'adresse enregistrée au checkout")
    public void checkout_should_display_saved_address() {
        // Préconditions
        SignupData data = SignupDataFactory.validDefaultUnique();
        HomePage home = SignupFlow.signupToHomeWithSignupData(driver(), data);
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

    /**
     * CT-ID : CT-CHECKOUT-002 EX-ID : EX-20
     *
     * <p>Objectif : Vérification des champs obligatoires du formulaire de paiement
     * Critères de réussite :
     * - Le paiement n'est pas traité
     * - Un message d'erreur ou une indication visuelle apparaît pour les champs manquant
     * - L'utilisateur reste sur la même page de paiement
     * - Aucunmessage de succès ni confirmation de commande n'est affiché
     * - Aucun comportement inattendu n'est observé
     *
     * <p>Préconditions :
     * 1. Utilisateur connecté
     * 2. Panier contenant au moins 1 produit
     * 3. Formulaire de paiement affiché (depuis CT-CHECKOUT-001)
     *
     * <p>Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(
            groups = {"ui", "checkout", "regression"},
            description =
                    "CT-CHECKOUT-002 / EX-20 / Vérification des champs obligatoires du formulaire de paiement")
    public void checkout_should_not_confirm_order_when_payment_card_number_is_missing() {
        // Préconditions
        PaymentData data = PaymentDataFactory.missingCardNumber();
        HomePage home = AuthFlow.loginAsTestUser(driver());
        ProductsPage products = home.goToProducts();
        products.addProductToCartById("1");
        CartPage cart = products.viewCartFromModal();
        CheckoutPage checkout = cart.proceedToCheckout();
        PaymentPage payment = checkout.placeOrder();
        payment.assertPaymentFormVisible();

        // Action
        payment.fillMandatoryFields(data);

        // Vérification
        payment.assertCardNumberIsRequiredOnSubmit();
    }
}
