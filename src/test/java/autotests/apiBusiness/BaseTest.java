package autotests.apiBusiness;

import com.github.javafaker.Faker;
import helper.EmployeeHelperDB;
import helper.EnvHelper;
import io.qameta.allure.Step;
import listener.AllureTestWatcher;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.sql.SQLException;

import static io.restassured.RestAssured.baseURI;

@ExtendWith(AllureTestWatcher.class)
@Slf4j
public class BaseTest {

    Faker faker = new Faker();
    protected static EnvHelper envHelper;
    static EmployeeHelperDB employeeHelperDB;
    static int createdEmployeeId = -1;
    static int employeeId;

    @BeforeAll
    @Step("Инициализация окружения")
    public static void setUri() throws SQLException, IOException {
        envHelper = new EnvHelper();
        baseURI = envHelper.getApiBaseUrl();
        employeeHelperDB = new EmployeeHelperDB();
    }
}
