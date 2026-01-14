package tests.cart;

import core.test.BaseTest;
import core.pages.*;
import flows.AuthFlow;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CartTests extends BaseTest {

    /**
     * CT-ID : CT-CART-004
     * EX-ID : EX-16
     *
     * Objectif : Suppression d'un produit du panier
     * Critères de réussite :  - Le produit supprimé disparaît immédiatement de la liste
     *                         - Les autres produits restent inchangés
     *                         - Aucun comportement inattendu n'est observé
     *
     * Préconditions : 1. Utilisateur connecté
     *                 2. Panier contenant au moins 2 produits différents
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(groups = {"ui","cart","regression"}, description = "CT-CART-004 / EX-16 / Suppression d'un produit du panier")

    public void remove_product_from_cart() {
        // Précondition
        HomePage home = AuthFlow.loginAsTestUser(driver());
        ProductsPage products = home.goToProducts();
        String removeId = "1";
        products.addProductToCartById("1");
        products.continueShoppingFromModal();
        products.addProductToCartById("2");
       CartPage cart =  products.viewCartFromModal();
        assertThat(cart.productCount()).isGreaterThan(1);
        List<String> beforeIds = cart.getProductIds();
        Map<String, Integer> qtyBefore = cart.getQuantitiesByProductId();
        assertThat(beforeIds).contains(removeId);
        assertThat(qtyBefore).containsKeys(removeId);

        // Action
        cart.deleteItemToCartByProductId(removeId);

        // Vérification
        assertThat(cart.isProductPresent(removeId)).isFalse();
        List<String> afterIds = cart.getProductIds();
        Map<String, Integer> qtyAfter = cart.getQuantitiesByProductId();
        // vérification que tout les autres éléments sont encore en place sauf celui supprimé
        List<String> expectedAfter = beforeIds.stream().filter(id -> !id.equals(removeId)).toList();
        assertThat(afterIds).containsExactlyElementsOf(expectedAfter);
        Map<String, Integer> qtyAfterExpected = new LinkedHashMap<>(qtyBefore);
        qtyAfterExpected.remove(removeId);
        assertThat(qtyAfter).isEqualTo(qtyAfterExpected);



    }


    /**
     * CT-ID : CT-CART-002
     * EX-ID : EX-17
     *
     * Objectif : Total du panier calculé correctement
     * Critères de réussite :   - Pour chaque ligne, le sous-total = prix unitaire*quantité
     *                          - Le total général du panier = somme de tous les sous-totaux des produits
     *                          - Aucun comportement inattendu n'est observé
     *
     * Préconditions : 1. Utilisateur connecté
     *                 2. Panier vide au démarrage
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(groups = {"ui","cart","regression"}, description = "CT-CART-002 / EX-17 / Total du panier calculé correctement")
    public void cart_total_is_correct() {
        // Préconditions
        HomePage home = AuthFlow.loginAsTestUser(driver());
        CartPage cart =  home.goToCart();
        cart.clearCart();
        // Action
        ProductsPage products = home.goToProducts();
       // products.assertLoaded();
        products.addProductToCartById("1");
        products.continueShoppingFromModal();
        products.addProductToCartById("1");
        products.continueShoppingFromModal();
        products.addProductToCartById("2");
        cart = products.viewCartFromModal();
        CheckoutPage checkout = cart.proceedToCheckout();
        BigDecimal expectedTotal = checkout.totalCartExpected();
        BigDecimal actualTotalCart = checkout.totalCart();
        // Vérifications
        assertThat(expectedTotal).isEqualByComparingTo(actualTotalCart);
        checkout.assertUnitPriceMultiplyByQuantityEqualTotalLine();



    }

}
