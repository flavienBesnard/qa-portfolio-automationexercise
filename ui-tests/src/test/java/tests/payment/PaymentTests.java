package tests.payment;

import core.config.Config;
import core.data.PaymentData;
import core.data.PaymentDataFactory;
import core.pages.*;
import core.test.BaseTest;
import flows.AuthFlow;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTests extends BaseTest {
    /**
     * CT-ID : CT-PAYMENT-001 EX-ID : EX-20
     *
     * <p>Objectif : Paiement réussi et confirmation de commande affiché
     * Critères de réussite :
     * - Un message de succès de type "Congratulations! Your order has been confirmed!" est affiché
     * - invoice téléchargeable
     * - Aucun comportement inattendu n'est observé
     *
     * <p>Préconditions :
     * 1. Utilisateur connecté
     * 2. Panier contient au moins 1 produit
     * 3. Formulaire de paiement affiché (depuis CT-CHECKOUT-001)
     *
     * <p>Note stabilité : - Test exécuté sur un site public (pubs/overlays possibles) --> mécanisme de contournement présent dans le code
     *
     * <p>Note : Selenium n'encourage pas à télécharger les fichiers, l'approche recommandé est de valider le lien côté UI puis via HTTP plutôt que WebDriver
     */
    @Test(
            groups = {"ui", "payment", "regression"},
            description = "CT-PAYMENT-001 / EX-20 / Paiement réussi et confirmation de commande affiché")
    public void should_payment_work_and_display_confirmation() throws Exception {
        // Préconditions
        PaymentData data = PaymentDataFactory.validDefaultUnique();
        HomePage home = AuthFlow.loginAsTestUser(driver());
        ProductsPage products = home.goToProducts();
        products.addProductToCartById("1");
        CartPage cart = products.viewCartFromModal();
        CheckoutPage checkout = cart.proceedToCheckout();
        PaymentPage payment = checkout.placeOrder();
        payment.assertPaymentFormVisible();
        // Action
        payment.fillMandatoryFields(data);
        PaymentDonePage paymentDone = payment.payAndConfirmOrderAndWaitDone();

        // Vérifications

        // on vérifie qu'on le lien "Download Invoice" est visible
        String href = paymentDone.getDownloadInvoiceHref();
        assertThat(href).contains("/download_invoice/");

        // on s'assure que l'URL est complète sinon  on la construit
        URI invoiceUri = URI.create(href.startsWith("http") ? href : (Config.baseUrl() + href));

        // réutilisation de la session Selenium via les cookies
        String cookieHeader =
                driver().manage().getCookies().stream()
                        .map(c -> c.getName() + "=" + c.getValue())
                        .collect(Collectors.joining("; "));

        // Outil java pour envoyer une requête HTTP
        HttpClient client =
                HttpClient.newBuilder()
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .connectTimeout(Duration.ofSeconds(10))
                        .build();

        // on fait un get vers l'URL de l'invoice
        HttpRequest req =
                HttpRequest.newBuilder(invoiceUri).header("Cookie", cookieHeader).GET().build();

        // on envoi la requête et on récupère la réponse
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        // L'invoice est bien téléchargeable côté serveur
        assertThat(resp.statusCode()).isEqualTo(200);
        assertThat(resp.body()).isNotBlank();
    }
}
