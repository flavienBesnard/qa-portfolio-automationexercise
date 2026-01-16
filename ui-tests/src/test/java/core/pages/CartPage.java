package core.pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CartPage extends BasePage {
    private static final By CART_TABLE = By.id("cart_info");
    // id^ permet de récupérer tout les id commençant par product-
    private static final By CART_ROWS = By.cssSelector(".table.table-condensed tbody tr[id^='product-']");
    private static final By BTN_PROCEED_TO_CHECKOUT = By.cssSelector("a.btn.btn-default.check_out");
    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(CART_TABLE);
    }
    public boolean hasAtLeastOneItem() {
        assertLoaded();
        return !driver.findElements(CART_ROWS).isEmpty();
    }


    public CheckoutPage proceedToCheckout() {
        assertLoaded();
    click(BTN_PROCEED_TO_CHECKOUT);
    return new CheckoutPage(driver);
    }

    public void deleteItemToCartByProductId(String productId) {
        assertLoaded();
        By deleteBtn = By.cssSelector(".cart_delete a.cart_quantity_delete[data-product-id=\"" + productId + "\"]");
        By productRow = By.cssSelector(".table.table-condensed tbody tr[id=\"product-" + productId + "\"]");
        click(deleteBtn);
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.invisibilityOfElementLocated(productRow));
    }

    public void clearCart() {
        assertLoaded();
        for (String id : getProductIds()) {
            deleteItemToCartByProductId(id);
        }
        dismissGoogleVignetteIfPresent();
        // new WebDriverWait(driver,Duration.ofSeconds(8)).until(d -> productCount() ==0);
    }

    public int productCount() {
        assertLoaded();
        return driver.findElements(CART_ROWS).size();
    }

    public boolean isProductPresent(String productId)
    {
        By ProductRow = By.cssSelector("tr[id=\"product-" + productId + "\"]");
        return driver.findElements(ProductRow).stream().anyMatch(WebElement::isDisplayed);
    }

    public List<String> getProductIds() {
        assertLoaded();
    List<WebElement> rows = driver.findElements(CART_ROWS);
    List<String> ids = new ArrayList<>();
    for (WebElement row : rows) {
        String idAttr = row.getAttribute("id"); // ex : product-1
        if (idAttr != null && idAttr.startsWith("product-")) // vérification supplémentaire que l'on a seulement des id avec product-
        {
            ids.add(idAttr.substring("product-".length())); // on garde seulement le numéro
        }
    }
    return ids;

    }

    public int getQuantity(String productId) {
        assertLoaded();
        WebElement row = driver.findElement(By.cssSelector("tr[id=\"product-" + productId + "\"]"));
        WebElement qtyEl = row.findElement(By.cssSelector("td.cart_quantity button.disabled"));
        String text = qtyEl.getText().trim();

        /*
        sur button la quantité est dans le texte -> getText()
        Sur input la valeur est dans l'attribut value
        si getText vide, on lit l'attribut value pour éviter une casse si le composant button change en input
         */
        if (text.isEmpty()) {
           String value = qtyEl.getAttribute("value");
           text = (value != null) ? value.trim() : "";
        }
        return Integer.parseInt(text.replaceAll("\\D+", ""));
        }

    public Map<String, Integer> getQuantitiesByProductId() {
        assertLoaded();
        Map<String, Integer> map = new LinkedHashMap<>();
        for (String id : getProductIds()) {
            map.put(id, getQuantity(id));
        }
        return map;
    }





}
