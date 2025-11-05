package autotests.ui;

import config.BrowserSettings;
import io.qameta.allure.Step;
import listener.AllureLogsAttachment;
import listener.AllureLogsExtension;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.LoginPage;
import pages.ProductsPage;

@ExtendWith(AllureLogsExtension.class)
@Slf4j
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected ProductsPage productsPage;
    protected LoginPage loginPage;

    @BeforeEach
    @Step("Запуск браузера и инициализация страниц")
    public void setUp() throws Exception {
        log.info("setUp");
        // Для локального запуска выбрать тип браузера
        driver = new BrowserSettings().browserSettingsConfig("firefox");
        AllureLogsAttachment.setDriver(driver);
        // Инициализация страниц
        loginPage = new LoginPage(driver);
        productsPage = new ProductsPage(driver);
    }

    @AfterEach
    @Step("Очистка куки и закрытие браузера")
    public void tearDown() {
        log.info("tearDown");
        if (driver != null) {
            driver.manage().deleteAllCookies();
            driver.quit();
        }
    }

}
