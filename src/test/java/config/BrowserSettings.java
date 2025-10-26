package config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Level;
@Slf4j
public class BrowserSettings {
    private static final Logger log = LoggerFactory.getLogger(BrowserSettings.class);

    public WebDriver browserSettingsConfig(String browser) throws IOException {

        // Настройки логирования браузера и производительности
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);

        WebDriver driver;
        switch (browser.toLowerCase()) {
            case ("chrome"):
                WebDriverManager.chromedriver().setup();

                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--headless");// без запуска браузера для CICD
                chromeOptions.addArguments("--disable-gpu"); // иногда нужно для Windows CI
                chromeOptions.addArguments("--no-sandbox"); // для Linux CI
                chromeOptions.addArguments("--disable-dev-shm-usage"); // для Docker
                chromeOptions.setCapability("goog:loggingPrefs", logPrefs);

                driver = new ChromeDriver(chromeOptions);
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                log.info("Название браузера: " + browser);
                break;
            case ("firefox"):
                WebDriverManager.firefoxdriver().setup();

                FirefoxDriverService service = new GeckoDriverService.Builder()
                        .withLogLevel(FirefoxDriverLogLevel.DEBUG)
                        .withLogOutput(System.out)
                        .build();

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--headless");
                firefoxOptions.addArguments("--disable-gpu");
                firefoxOptions.addArguments("--no-sandbox");
                firefoxOptions.addArguments("--disable-dev-shm-usage");

                driver = new FirefoxDriver(service, firefoxOptions);
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                log.info("Название браузера: " + browser);
                break;
            default:
                throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
        return driver;
    }
}
