package core.test;


import core.driver.DriverFactory;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

// classe mère pour les tests UI
public abstract class BaseTest {
    /**
     * BaseTest :
     * - Initialise/termine le driver à chaque test pour l'isolation
     * - En cas d'échec, capture une preuve (screenshort) pour facilier le debug
     * - Quit dans un finally pour éviter les drivers "orphelins".
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
    }
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        try {
        // preuve seulement si le test échoue (afin que les fichiers ne soient pas trop lourds)
        if (!result.isSuccess()) {

                var driver = DriverFactory.getDriver();

            if (driver instanceof TakesScreenshot ts) {
                byte[] png = ts.getScreenshotAs(OutputType.BYTES);

                Allure.addAttachment(
                        "Screenshot - failure",
                        "image/png",
                        new ByteArrayInputStream(png),
                        ".png"

                );
            }

            }
        }
            finally {
            DriverFactory.quitDriver();
        }

}

protected org.openqa.selenium.WebDriver driver() {
    return DriverFactory.getDriver();
    }
}