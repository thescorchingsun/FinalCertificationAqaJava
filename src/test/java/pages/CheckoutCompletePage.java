package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutCompletePage extends BasePage {

    private final By titleText = By.xpath("//div/span[@data-test='title']");
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    @Step("Проверить текст статуса заказа")
    public void assertCheckTitle(String text) {
        Assertions.assertEquals(text, driver.findElement(titleText).getText());
    }
}
