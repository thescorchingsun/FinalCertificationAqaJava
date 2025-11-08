package autotests.ui;

import helper.MethodForScreen;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Epic("Screenshot Tests")
@Feature("SauceDemo Application")
@Story("User Authentication & Cart")
public class ScreenTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(ScreenTest.class);

    private MethodForScreen screenHelper;

    private final Path EXPECTED_DIR = Path.of("src/test/resources/screens/");
    private final Path DIFF_DIR = Path.of("target/screens/");
    private final String USERNAME_STANDARD = "standard_user";
    private final String PASSWORD = "secret_sauce";

    @BeforeEach
    public void init() {
        screenHelper = new MethodForScreen(driver);
    }

    @Test
    @DisplayName("Проверка UI страницы авторизации")
    @Tags({@Tag("screenshot"), @Tag("regress")})
    public void screenLoginPageTest() throws IOException {
        loginPage.openLoginPage();
        screenHelper.compareScreens(
                EXPECTED_DIR.resolve("loginPageExpected.png"),
                DIFF_DIR,
                "loginPage"
        );
    }

    @Test
    @DisplayName("Проверка UI страницы каталога товаров")
    @Tags({@Tag("screenshot"), @Tag("regress")})
    @Disabled("Flaky тест. При локальном запуске работает стабильно, при удаленном запуске может падать. ")
    public void screenCatalogPageTest() throws IOException {
        loginPage.openLoginPage()
                .successfulAuth(USERNAME_STANDARD, PASSWORD)
                .waitUntilAllImagesLoaded();
        screenHelper.compareScreens(
                EXPECTED_DIR.resolve("catalogPageExpected.png"),
                DIFF_DIR,
                "catalogPage"
        );
    }
}