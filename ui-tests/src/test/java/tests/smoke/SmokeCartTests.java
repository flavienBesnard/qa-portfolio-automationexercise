package tests.smoke;

import core.pages.*;
import core.test.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
public class SmokeCartTests extends BaseTest {
    /**
     * CT-ID : CT-CART-001
     * EX-ID : EX-13
     *
     * Objectif : Ajout d'un produit au panier depuis l'onglet Produits
     * Critères de réussite :    - Le produit apparaît dans le panier avec quantité = 1
     *                          - Aucun comportement inattendu n'est observé
     * Préconditions : 1. Aucun article dans le panier --> Session navigateur neuve --> panier vide
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(groups = {"ui","cart","smoke"}, description = "CT-CART-001 / EX-13 / Ajout d'un produit au panier depuis l'onglet produit")
    public void add_product_to_cart_should_add_item_in_cart_with_qty_1()  {
    // Action : On va sur la page products, on ajoute le premier produit au panier et on se redirige vers le panier
    HomePage home = new HomePage(driver()).open();
    ProductsPage products = home.goToProducts();
    // on ajoute le premier article
    products.addProductToCartById("1");
    CartPage cart = products.viewCartFromModal();
    // Vérification : on vérifie qu'il y a un article dans le panier et que la quantité est égale à 1
    assertThat(cart.hasAtLeastOneItem()).isTrue();
    assertThat(cart.getQuantity("1")).isEqualTo(1);



    }
}
