package core.data;

import net.datafaker.Faker;

public final class SignupDataFactory {
    private SignupDataFactory() {
    }

    public static SignupData validDefaultUnique() {
        Faker faker = new Faker();

        String email = "qa+" + System.currentTimeMillis() + "@example.com";
        String password = generateSafePassword(faker);

        return new SignupData(
                faker.name().name(),
                email,
                "Mr",
                password,
                "16",
                "1",
                "1997",
                faker.name().firstName(),
                faker.name().lastName(),
                faker.company().name(),
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                "Canada",
                faker.address().state(),
                faker.address().city(),
                faker.address().zipCode(),
                "06" + faker.number().digits(8));
    }

    private static String generateSafePassword(Faker faker) {
        String core = faker.regexify("[a-z]{6}[0-9]{2}");
        // On garantit les règles majuscule, minuscule + un caractère spécial avec "Aa!"
        return "Aa!" + core;
    }
}
