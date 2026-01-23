package core.pages;

import core.data.SignupData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutPage extends BasePage {
    private static final By CHECKOUT_INFO = By.cssSelector("[data-qa=\"checkout-info\"]");
    private static final By BTN_PLACE_ORDER = By.cssSelector(".btn.btn-default.check_out");
    private static final By CART_ROWS =
            By.cssSelector(".table.table-condensed tbody tr[id^='product-']");
    private static final By TOTAL_CART =
            By.xpath(
                    "//b[normalize-space()='Total Amount']/ancestor::tr//p[contains(@class,'cart_total_price')]");
    private static final By ADDRESS_DELIVERY = By.id("address_delivery");
    private static final By ADDRESS_INVOICE = By.id("address_invoice");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void assertLoaded() {
        visible(CHECKOUT_INFO);
    }

    /**
     * placeOrder est l'action qui amène à la page de paiement
     */
    public PaymentPage placeOrder() {
        assertLoaded();
        click(BTN_PLACE_ORDER);
        PaymentPage payment = new PaymentPage(driver);
        payment.assertLoaded();
        return payment;
    }

    public BigDecimal lineTotal(String productId) {
        assertLoaded();
        By row =
                By.cssSelector("tbody tr[id='product-" + productId + "'] td.cart_total p.cart_total_price");
        String textTotalLine = driver.findElement(row).getText();
        // TODO : améliorer le parsing pour les décimales
        String totalLine = textTotalLine.replaceAll("[^0-9]", "");
        return new BigDecimal(totalLine);
    }

    public BigDecimal totalCartExpected() {
        assertLoaded();
        BigDecimal totalExpected = BigDecimal.ZERO;
        for (String id : getProductIds()) {
            totalExpected = totalExpected.add(lineTotal(id));
        }
        return totalExpected;
    }

    public BigDecimal totalCart() {
        assertLoaded();
        String totalCartBrut = driver.findElement(TOTAL_CART).getText();
        String totalCartClean = totalCartBrut.replaceAll("[^0-9]", "");
        return new BigDecimal(totalCartClean);
    }

    public BigDecimal unitPrice(String productId) {
        assertLoaded();
        By row = By.cssSelector(" tbody tr[id='product-" + productId + "']  td.cart_price");
        String priceRowBrut = driver.findElement(row).getText();
        String priceRow = priceRowBrut.replaceAll("[^0-9]", "");
        return new BigDecimal(priceRow);
    }

    public BigDecimal getQuantity(String productId) {
        assertLoaded();
        WebElement row = driver.findElement(By.cssSelector("tbody tr[id='product-" + productId + "']"));
        WebElement qtyEl = row.findElement(By.cssSelector("td.cart_quantity button.disabled"));
        String textqty = qtyEl.getText().trim();
    /*
    sur button la quantité est dans le texte -> getText()
    Sur input la valeur est dans l'attribut value
    si getText vide, on lit l'attribut value pour éviter une casse si le composant button change en input
     */
        if (textqty.isEmpty()) {
            String value = qtyEl.getAttribute("value");
            textqty = (value != null) ? value.trim() : "";
        }
        return new BigDecimal(textqty);
    }

    public void assertUnitPriceMultiplyByQuantityEqualTotalLine() {
        assertLoaded();
        for (String id : getProductIds()) {

            assertThat(unitPrice(id).multiply(getQuantity(id)))
                    .as("Le total de la ligne n'est pas égal à unitprice*quantity")
                    .isEqualByComparingTo(lineTotal(id));
        }
    }

    public List<String> getProductIds() {
        assertLoaded();
        List<WebElement> rows = driver.findElements(CART_ROWS);
        List<String> ids = new ArrayList<>();
        for (WebElement row : rows) {
            String idAttr = row.getAttribute("id"); // ex : product-1
            if (idAttr != null
                    && idAttr.startsWith(
                    "product-")) // vérification supplémentaire que l'on a seulement des id avec product-
            {
                ids.add(idAttr.substring("product-".length())); // on garde seulement le numéro
            }
        }
        return ids;
    }

    public void assertAddressDeliveryDisplayedIsCorrect(SignupData data) {
        assertLoaded();

        WebElement addressDelivery = driver.findElement(ADDRESS_DELIVERY);
        assertThat(
                addressDelivery
                        .findElement(By.cssSelector("li.address_firstname.address_lastname"))
                        .getText()
                        .trim())
                .isEqualTo(data.getTitle() + ". " + data.getFirstName() + " " + data.getLastName());

        List<WebElement> addressElement =
                addressDelivery.findElements(By.cssSelector("li.address_address1.address_address2"));

        List<String> addressList = new ArrayList<>();
        for (WebElement el : addressElement) {
            String text = el.getText().trim();
            if (!text.isEmpty()) addressList.add(text);
        }
        assertThat(addressList).contains(data.getCompany());
        assertThat(addressList).contains(data.getAddress());
        assertThat(addressList).contains(data.getAddress2());

        assertThat(
                addressDelivery
                        .findElement(By.cssSelector("li.address_city.address_state_name.address_postcode"))
                        .getText()
                        .trim())
                .isEqualTo(data.getCity() + " " + data.getState() + " " + data.getZipcode());

        assertThat(
                addressDelivery.findElement(By.cssSelector("li.address_country_name")).getText().trim())
                .isEqualTo(data.getCountry());

        assertThat(addressDelivery.findElement(By.cssSelector("li.address_phone")).getText().trim())
                .isEqualTo(data.getMobileNumber());
    }

    private List<String> addressLiLinesWithoutTitle(By addressBlock) {
        WebElement box = visible(addressBlock);
        List<WebElement> lis = box.findElements(By.tagName("li"));

        List<String> lines = new ArrayList<>();
        for (WebElement li : lis) {
            if (!li.findElements(By.tagName("h3")).isEmpty())
                continue; // si le li contient h3 on le saute et on passe au suivant
            String text = li.getText().trim();
            if (!text.isEmpty()) lines.add(text);
        }
        return lines;
    }

    public void assertAddressDeliveryAndBillingAreTheSame() {
        assertLoaded();
        List<String> delivery = addressLiLinesWithoutTitle(ADDRESS_DELIVERY);
        List<String> billing = addressLiLinesWithoutTitle(ADDRESS_INVOICE);
        assertThat(delivery).isEqualTo(billing);
    }
}
