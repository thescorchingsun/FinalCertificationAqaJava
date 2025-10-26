package autotests;

import io.qameta.allure.Issue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
@Slf4j
public class Tests extends BaseTest {

    private final String URL_CATALOG = "https://www.saucedemo.com/inventory.html";
    private final String USERNAME_STANDARD = "standard_user";
    private final String USERNAME_BLOCKED = "locked_out_user";
    private final String PASSWORD = "secret_sauce";
    private final String EXPECTED_RESULT_LOCKED_OUT_TEXT = "Epic sadface: Sorry, this user has been locked out.";
    private final String EXPECTED_RESULT_TITLE_TEXT = "Checkout: Complete!";
    private final List<String> PRODUCTS = List.of("Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt", "Sauce Labs Onesie");
    private final int ITEM_IN_CARD = 3;
    private final String FIRST_NAME = "Elena";
    private final String LAST_NAME = "Bobrova";
    private final String POSTAL_CODE = "239000";
    private final String EXPECTED_RESULT_PRICE_TOTAL = "Total: 58.29";


    @Test
    @DisplayName("Успешная авторизация со стандартным пользователем")
    @Tags({@Tag("smoke"), @Tag("regress")})
    @Issue("JIRA-1")
    public void loginStandardUserTest() {
        loginPage.openLoginPage()
                .typeUsername(USERNAME_STANDARD)
                .typePassword(PASSWORD)
                .clickLoginButton()
                .assertCurrentUrl(URL_CATALOG);
    }

    @Test
    @DisplayName("Ошибка авторизация с заблокированным пользователем")
    @Tags({@Tag("smoke"), @Tag("regress")})
    @Issue("JIRA-2")
    public void loginByBlockedUserTest() {
        loginPage.openLoginPage()
                .typeUsername(USERNAME_BLOCKED)
                .typePassword(PASSWORD)
                .clickLoginErrorTextButton()
                .assertEquals(EXPECTED_RESULT_LOCKED_OUT_TEXT);
    }

    @DisplayName("E2E тест. Авторизация и добавление товаров в корзину")
    @Tags({@Tag("smoke"), @Tag("regress")})
    @Issue("JIRA-3")
    @ParameterizedTest(name = "E2E сценарий под пользователем {0}")
    @CsvSource({
            "standard_user, secret_sauce",
            "performance_glitch_user, secret_sauce"
    })
    public void e2eTest(String username, String password) {
        loginPage.openLoginPage()
                .successfulAuth(username, password)
                .isProductDisplayed(PRODUCTS)
                .addProductsToCart(PRODUCTS)
                .clickCartButton()
                .assertCountItemsInCart(ITEM_IN_CARD)
                .clickCheckoutButton()
                .typeFirstNameInput(FIRST_NAME)
                .typeLastNameInput(LAST_NAME)
                .postalCodeInputInput(POSTAL_CODE)
                .clickContinueButton()
                .assertCheckTotalPrice(EXPECTED_RESULT_PRICE_TOTAL)
                .clickFinishButton()
                .assertCheckTitle(EXPECTED_RESULT_TITLE_TEXT);

    }
}
