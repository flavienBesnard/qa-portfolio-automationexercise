package core.config;

public final class ApiConfig {
    private ApiConfig() {}

    public static String apiBaseUrl() {
        String v = System.getProperty("API_BASE_URL");
        if (v==null || v.isBlank()) {
            v = System.getenv("API_BASE_URL");
        }
        return (v == null || v.isBlank())
        ? "https://automationexercise.com/api"
        : v;
    }
}
