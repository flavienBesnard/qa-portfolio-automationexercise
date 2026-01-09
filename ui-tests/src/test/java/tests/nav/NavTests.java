package tests.nav;
import core.pages.ProductsPage;
import core.test.BaseTest;
import org.testng.annotations.Test;
import core.pages.HomePage;
import static org.assertj.core.api.Assertions.assertThat;

public class NavTests extends BaseTest {


    /**
     * CT-ID : CT-NAV-001
     * EX-ID : EX-12
     *
     * Objectif : Navigation - aller sur la page products via la navbar
     * Critères de réussite :   - La page "products" s'affiche
     *                          - La liste des produits est visible
     *                          - Aucun message d'erreur  / mauvaise redirection
     *                          - Aucun comportement inattendu n'est observé
     *
     * Préconditions : 1. L'utilisateur est sur la page d'accueil (Home)
     *
     * Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     */


    @Test(groups = {"ui","nav","regression"}, description = "CT-NAV-001 / EX-12 / Navigation - aller sur la page products via la navbar")

    public void go_to_products_from_home_by_nav() {
        // Précondition
        HomePage home = new HomePage(driver()).open();

        // Action
        ProductsPage products = home.goToProducts();

        // Vérification : isLoaded appel la liste des produits
        assertThat(products.isLoaded()).isTrue();
    }

}

