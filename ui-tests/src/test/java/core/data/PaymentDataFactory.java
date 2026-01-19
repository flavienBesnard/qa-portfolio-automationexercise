package core.data;
import net.datafaker.Faker;

public class PaymentDataFactory {

    private PaymentDataFactory() {

    }

    public static PaymentData missingCardNumber() {
        Faker faker = new Faker();
        return new PaymentData(
                faker.name().fullName(),
                "",
                faker.number().digits(3),
                "01",
                "2030"

        );
    }
}
