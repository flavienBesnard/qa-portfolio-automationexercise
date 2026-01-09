package tests.smoke;

import core.test.BaseTest;
import core.pages.*;
import flows.AuthFlow;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class SmokeCheckoutTests extends BaseTest {
    /**
     * CT-ID : CT-CHECKOUT-001
     * EX-ID : EX-19
     *
     * Objectif : Accéder au formulaire de paiement depuis le panier
     * Critères de réussite :    - Le formulaire de paiement s'affiche avec tous les champs attendus visibles (nom, carte, CVC, expiration)
     *                          - Aucun comportement inattendu n'est observé
     * Préconditions : 1. Utilisateur connecté
     *                2. Panier contient au moins 1 produit
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> moyen de contournement présent dans le code
     *
     */
    @Test(groups = {"ui","checkout","smoke"}, description = "CT-CHECKOUT-001 / EX-19 / Accéder au formulaire de paiement depuis le panier")
    public void access_to_payment_form_from_cart() {
        // Préconditions : utilisateur connecté  + panier contient un produit
        HomePage home = AuthFlow.loginAsTestUser(driver());
        assertThat(home.isLoggedIn()).isTrue();
        ProductsPage products = home.goToProducts();
        products.addProductToCartById("1");
        CartPage cart = products.viewCartFromModal();
        assertThat(cart.hasAtLeastOneItem()).isTrue();
        // Action : déclenchement du parcours checkout jusqu'à la page de paiement
        CheckoutPage checkout = cart.proceedToCheckout();
        PaymentPage payment = checkout.placeOrder();
        // Vérification : preuve de succès = formulaire de paiement visible (EX-19)
        assertThat(payment.hasPaymentForm()).isTrue();



    }

}
