package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
/** Вспомогательный класс для управления конфигурацией окружения */
public class EnvHelper {

    private final Properties properties;

    public EnvHelper() throws IOException {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "env.properties";

        properties = new Properties();
        properties.load(new FileInputStream(appConfigPath));
    }

    // Для API тестов
    public String getConnectionString() {
        return properties.getProperty("db.connectionString");
    }

    public String getLogin() {
        return properties.getProperty("db.login");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }

    public String getAdminLogin() {
        return properties.getProperty("admin.login");
    }

    public String getAdminPassword() {
        return properties.getProperty("admin.password");
    }

    public String getApiBaseUrl() {
        return properties.getProperty("api.base.url");
    }

    // Для UI тестов
    public String getUiBaseUrl() {
        return properties.getProperty("ui.base.url");
    }


}
