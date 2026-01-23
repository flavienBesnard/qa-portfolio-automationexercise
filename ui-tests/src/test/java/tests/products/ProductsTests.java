package tests.products;

import core.pages.HomePage;
import core.pages.ProductDetailsPage;
import core.pages.ProductsPage;
import core.test.BaseTest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductsTests extends BaseTest {
    /**
     * CT-ID : CT-PRODUCTS-005 EX-ID : EX-11
     *
     * <p>Objectif : Affichage des informations minimales sur la fiche produit
     * Critères de réussite :
     * - Le nom du produit est affiché et non vide
     * - Le prix est affiché dans un format lisible (ex : Rs. 500)
     * - Des informations sur le produit sont visible (availability, condition,brand et notation)
     * - Une image du produit est visible
     * - Aucun comportement inattendu n'est observé
     *
     * <p>Préconditions : 1. L'utilisateur est sur la page "Products"
     *
     * <p>Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */
    @Test(
            groups = {"ui", "products", "regression"},
            description =
                    "CT-PRODUCTS-005 / EX-11 / Affichage des informations minimales sur la fiche produit")
    public void products_details_display_minimum_information() {
        // Préconditions
        HomePage home = new HomePage(driver()).open();
        ProductsPage products = home.goToProducts();
        // Action
        String productNameInProductPage = products.getProductNameById("1");
        ProductDetailsPage productDetails = products.viewProductDetailsByProductId("1");
        // Vérifications
        assertThat(productDetails.productName()).isNotBlank();
        assertThat(productDetails.price()).isNotBlank();
        assertThat(productDetails.productName()).isEqualTo(productNameInProductPage);
        assertThat(productDetails.price()).contains("Rs.");
        assertThat(productDetails.availability()).isNotBlank();
        assertThat(productDetails.brand()).isNotBlank();
        assertThat(productDetails.condition()).isNotBlank();
        assertThat(productDetails.isProductImageDisplayed()).isTrue();
        assertThat(productDetails.isRatingDisplayed()).isTrue();
    }
}
