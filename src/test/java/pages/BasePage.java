package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class BasePage {

    protected WebDriver driver;
    private final int DEFAULT_TIMEOUT = 20;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        waitForPageToLoad();
    }

    /**
     * Метод ожидает полной загрузки страницы (document.readyState == "complete")
     */
    protected void waitForPageToLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }
}
