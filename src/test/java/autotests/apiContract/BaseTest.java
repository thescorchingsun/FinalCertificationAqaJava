package autotests.apiContract;

import helper.AuthHelper;
import helper.EmployeeHelperDB;
import helper.EnvHelper;

import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;


import static io.restassured.RestAssured.baseURI;

@Slf4j
public class BaseTest {

    static AuthHelper authHelper;
    protected static EnvHelper envHelper;
    static EmployeeHelperDB employeeHelperDB;
    static int createdEmployeeId = -1;


    @BeforeAll
    @Step("Получение базового URL")
    public static void setUri() throws SQLException, IOException {
        envHelper = new EnvHelper();
        baseURI = envHelper.getApiBaseUrl();
        employeeHelperDB = new EmployeeHelperDB();
        authHelper = new AuthHelper();
    }

}
