package core.config;

public final class ApiConfig {
    private ApiConfig() {
    }

    public static String apiBaseUrl() {
        String v = System.getProperty("API_BASE_URL");
        if (v == null || v.isBlank()) {
            v = System.getenv("API_BASE_URL");
        }
        return (v == null || v.isBlank()) ? "https://automationexercise.com/api" : v;
    }

    public static String testUserEmail() {
        // TEST_USER_EMAIL à définir en tant que variable d'environnement
        return requiredSysOrEnv("TEST_USER_EMAIL");
    }

    public static String testUserPassword() {
        // TEST_USER_PASSWORD à définir en tant que variable d'environnement
        return requiredSysOrEnv("TEST_USER_PASSWORD");
    }

    private static String requiredSysOrEnv(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "variable d'environnement manquante : "
                            + key
                            + "\n"
                            + "\n définis la variable d'environnement avec : setx "
                            + key
                            + " \"valeur\" sur windows puis relancer le projet"
                            + "\n ou export "
                            + key
                            + "=\"valeur\" sur mac/linux puis relancer le projet");
        }
        return value;
    }
}
