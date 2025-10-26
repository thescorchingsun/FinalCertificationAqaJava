package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutOnePage extends BasePage {

    private final By firstNameInput = By.id("first-name");
    private final By lastNameInput = By.id("last-name");
    private final By postalCodeInput = By.id("postal-code");
    private final By continueButton = By.id("continue");

    public CheckoutOnePage(WebDriver driver) {
        super(driver);
    }

    @Step("Заполнить поле First Name")
    public CheckoutOnePage typeFirstNameInput(String firstName) {
        driver.findElement(firstNameInput).sendKeys(firstName);
        return this;
    }

    @Step("Заполнить поле Last Name")
    public CheckoutOnePage typeLastNameInput(String lastName) {
        driver.findElement(lastNameInput).sendKeys(lastName);
        return this;
    }

    @Step("Заполнить поле Zip/Postal Code")
    public CheckoutOnePage postalCodeInputInput(String postalCode) {
        driver.findElement(postalCodeInput).sendKeys(postalCode);
        return this;
    }

    @Step("Нажать на кнопку Сontinue")
    public CheckoutTwoPage clickContinueButton() {
        driver.findElement(continueButton).click();
        return new CheckoutTwoPage(driver);
    }
}
