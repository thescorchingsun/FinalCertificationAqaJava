package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductsPage extends BasePage {

    private final By shoppingCartButton = By.id("shopping_cart_container");
    private final By productName = By.className("inventory_item_name");
    private final By productCard = By.className("inventory_item");
    private final By addToCartButton = By.tagName("button");

    private final Duration TIMEOUT = Duration.ofSeconds(10);

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Проверить, отображаются ли продукты на странице")
    public ProductsPage isProductDisplayed(List<String> expectedProducts) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCard));

        List<String> displayedProductNames = driver.findElements(productName)
                .stream()
                .map(WebElement::getText)
                .toList();

        for (String expected : expectedProducts) {
            Assertions.assertTrue(displayedProductNames.contains(expected),
                    "Продукт не найден на странице: " + expected);
        }
        return this;
    }

    @Step("Добавить указанные продукты в корзину")
    public ProductsPage addProductsToCart(List<String> products) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCard));

        List<WebElement> productElements = driver.findElements(productCard);

        for (WebElement productElement : productElements) {
            String productNameText = productElement.findElement(productName).getText();

            if (products.contains(productNameText)) {
                WebElement addButton = productElement.findElement(addToCartButton);
                addButton.click();
            }
        }
        return this;
    }

    @Step("Нажать на кнопку Корзина")
    public CartPage clickCartButton() {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(shoppingCartButton));

        driver.findElement(shoppingCartButton).click();
        return new CartPage(driver);
    }

    @Step("Проверить, что текущий URL совпадает с ожидаемым: {expectedUrl}")
    public ProductsPage assertCurrentUrl(String expectedUrl) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT);
        wait.until(ExpectedConditions.urlToBe(expectedUrl));

        String actualUrl = driver.getCurrentUrl();
        Assertions.assertEquals(expectedUrl, actualUrl,
                "Ожидался URL: " + expectedUrl + ", но получен: " + actualUrl);
        return this;
    }

}
