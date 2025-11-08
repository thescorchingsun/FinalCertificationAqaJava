package helper;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MethodForScreen {

    private static final Logger log = LoggerFactory.getLogger(MethodForScreen.class);

    private final WebDriver driver;

    public MethodForScreen(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Сравнить UI текущей страницы с эталоном {expectedPath}")
    public void compareScreens(Path expectedPath, Path diffDir, String screenName) throws IOException {

        // Пути для диффов
        Path diffImage = diffDir.resolve(screenName + "_diff.png");
        Path markedImage = diffDir.resolve(screenName + "_marked.png");
        Path transparentMarked = diffDir.resolve(screenName + "_transparent.png");

        Files.createDirectories(expectedPath.getParent());
        Files.createDirectories(diffDir);

        // Текущий скриншот страницы
        BufferedImage actualImage = new AShot().takeScreenshot(driver).getImage();

        // Если эталона нет — создать и завершить тест
        if (!Files.exists(expectedPath)) {
            log.warn("Эталонный скриншот отсутствует. Создаю новый: {}", expectedPath + " Перезапусти тест повторно.");
            ImageIO.write(actualImage, "png", expectedPath.toFile());
            throw new AssertionError("Эталон отсутствовал и был создан. Проверь изображение: " + expectedPath);
        }

        // Загрузить эталон и сравнить
        BufferedImage expectedImage = ImageIO.read(expectedPath.toFile());
        ImageDiff diff = new ImageDiffer().makeDiff(expectedImage, actualImage);

        // Сохранить результаты сравнения
        ImageIO.write(diff.getDiffImage(), "png", diffImage.toFile());
        ImageIO.write(diff.getMarkedImage(), "png", markedImage.toFile());
        ImageIO.write(diff.getTransparentMarkedImage(), "png", transparentMarked.toFile());

        // Проверка
        log.info("Результат сравнения '{}': есть отличия = {}", screenName, diff.hasDiff());
        assertFalse(diff.hasDiff(), "Обнаружены отличия на странице '" + screenName + "'. См. diff: " + diffImage);
    }



}
