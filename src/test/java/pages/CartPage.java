package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By checkoutButton = By.id("checkout");
    private final By cartItem = By.className("cart_item");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Нажать на кнопку checkout")
    public CheckoutOnePage clickCheckoutButton() {
        driver.findElement(checkoutButton).click();
        return new CheckoutOnePage(driver);
    }

    // Проверки в тесте
    @Step("Проверить кол-во товаров в корзине")
    public CartPage assertCountItemsInCart(int item) {
        int countItem = driver.findElements(cartItem).size();
        Assertions.assertEquals(countItem,item);
        return this;
    }

}
