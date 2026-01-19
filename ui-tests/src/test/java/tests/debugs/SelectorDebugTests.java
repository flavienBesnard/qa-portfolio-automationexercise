package tests.debugs;

import core.test.BaseTest;
import core.pages.HomePage;
import core.pages.CartPage;
import core.pages.ProductsPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SelectorDebugTests extends BaseTest {
    @Test(groups={"debug"})
    public void debug_cart_selector() throws InterruptedException {
        HomePage home = new HomePage(driver()).open();
        // va sur la page produit
        ProductsPage products = home.goToProducts();

        products.addProductToCartById("1");

        CartPage cart = products.viewCartFromModal();

        assertThat(cart.hasAtLeastOneItem()).isTrue();
        assertThat(cart.getQuantity("1")).isEqualTo(1);

    }


}
