package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.name("user-name");
    private final By passwordInput = By.name("password");
    private final By loginButtonContainer = By.cssSelector("#login_button_container");
    private final By loginButton = By.name("login-button");

    private final String BASE_URL = "https://www.saucedemo.com/";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу авторизации")
    public LoginPage openLoginPage() {
        driver.get(BASE_URL);
        return this;
    }

    @Step("Ввести в поле username")
    public LoginPage typeUsername(String username) {
        driver.findElement(usernameInput).sendKeys(username);
        return this;
    }

    @Step("Ввести в поле пароль")
    public LoginPage typePassword(String password) {
        driver.findElement(passwordInput).sendKeys(password);
        return this;
    }

    @Step("Нажать на кнопку Login")
    public ProductsPage clickLoginButton() {
        driver.findElement(loginButton).click();
        return new ProductsPage(driver);
    }

    @Step("Нажать на кнопку Login, появляется сообщение об ошибке")
    public LoginPage clickLoginErrorTextButton() {
        driver.findElement(loginButton).click();
        return this;
    }

    @Step("Успешная авторизация")
    public ProductsPage successfulAuth(String username, String password) {
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(loginButton).click();
        return new ProductsPage(driver);
    }

    // проверки в тесте
    @Step("Проверить текст ошибки")
    public void assertEquals(String text) {
        Assertions.assertEquals(text, driver.findElement(loginButtonContainer).getText());
    }


}
