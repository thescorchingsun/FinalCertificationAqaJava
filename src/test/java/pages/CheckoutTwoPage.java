package pages;

import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutTwoPage extends BasePage {

    private final By finishButton = By.id("finish");
    private final By totalPrice = By.xpath("//div[@class='summary_total_label']");

    public CheckoutTwoPage(WebDriver driver) {
        super(driver);
    }

    @Step("Проверить сумму заказа")
    public CheckoutTwoPage assertCheckTotalPrice(String priceTotal) {

        String actualPriceTotal = driver.findElement(totalPrice).getText().replace("$","");
        Assertions.assertEquals(priceTotal, actualPriceTotal);
        return this;
    }

    @Step("Нажать на кнопку finish")
    public CheckoutCompletePage clickFinishButton() {
        driver.findElement(finishButton).click();
        return new CheckoutCompletePage(driver);
    }

}
