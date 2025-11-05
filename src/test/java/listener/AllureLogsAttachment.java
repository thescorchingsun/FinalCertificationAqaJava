package listener;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Получение дополнительных данных в Allure отчете при падении UI теста
 */
public class AllureLogsAttachment {


    private static WebDriver driver;

    // Передача текущего WebDriver из теста
    public static void setDriver(WebDriver webDriver) {
        driver = webDriver;
    }

    // Получение кода страницы
    @Attachment(value = "Page source", type = "text/plain")
    public static byte[] pageSource() {
        if (driver == null) return new byte[0];
        return driver.getPageSource().getBytes(StandardCharsets.UTF_8);
    }

    // Получение скриншота страницы
    @Attachment(value = "Page screen", type = "image/png")
    public static byte[] pageScreen() {
        if (driver == null) return new byte[0];
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    // Получение логов из chrome браузера
    @Attachment(value = "Browser logs", type = "text/plain")
    public static String getLogs() {
        if (driver == null) return "";

        try {
            List<LogEntry> logs = driver.manage().logs().get(LogType.BROWSER).getAll();
            return logs.stream()
                    .map(log -> log.getLevel() + ": " + log.getMessage())
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            return "Не удалось получить логи браузера: " + e.getMessage();
        }
    }
}
