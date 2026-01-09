package core.config;

import java.net.URL;

public class Config {
    private Config() {}

    private static String sysOrEnv(String key) {
        String v = System.getProperty(key);
        if (v == null || v.isBlank()) v = System.getenv(key);
        return (v == null) ? "" : v.trim();
    }
    public static String baseUrl() {
        String v = sysOrEnv("BASE_URL");
        return v.isBlank() ? "https://automationexercise.com" : v;
    }

    public static URL remoteUrlOrNull() {
        String v = sysOrEnv("SELENIUM_REMOTE_URL");
        if (v.isBlank()) return null;
        try { return new URL(v); }
        catch (Exception e) { throw new IllegalArgumentException("SELENIUM_REMOTE_URL invalide: " +v, e); }
    }

    public static boolean headless() {
        String v = sysOrEnv("HEADLESS");
        return !v.isBlank() && Boolean.parseBoolean(v);
    }

    public static boolean hasTestUserCredentials() {
        return !sysOrEnv("TEST_USER_EMAIL").isBlank() && !sysOrEnv("TEST_USER_PASSWORD").isBlank();
    }

    public static String testUserEmail() {
        // TEST_USER_EMAIL à définir en tant que variable d'environnement
        return requiredSysOrEnv("TEST_USER_EMAIL");
    }

    public static String testUserPassword()
    {
        // TEST_USER_PASSWORD à définir en tant que variable d'environnement
        return requiredSysOrEnv("TEST_USER_PASSWORD");
    }

    /**
     * Récupère une variable d'environnement obligatoire et si elle n'existe pas -> erreur explicite
     * @param key
     * @return
     */
    private static String requiredSysOrEnv(String key) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("variable d'environnement manquante : " + key + "\n" +
                    "\n définis la variable d'environnement avec : setx " + key + " \"valeur\" sur windows puis relancer le projet" +
                    "\n ou export " + key + "=\"valeur\" sur mac/linux puis relancer le projet");
        }
        return value;
    }
}
