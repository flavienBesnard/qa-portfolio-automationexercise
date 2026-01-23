package core.data;

public class PaymentData {

    private final String nameOnCard;
    private final String cardNumber;
    private final String cvc;
    private final String expiryMonth;
    private final String expiryYear;

    public PaymentData(
            String nameOnCard, String cardNumber, String cvc, String expiryMonth, String expiryYear) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.cvc = cvc;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvc() {
        return cvc;
    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }
}
