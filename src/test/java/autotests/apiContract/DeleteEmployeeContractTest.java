package autotests.apiContract;

import entities.EmployeeRequest;
import helper.EnvHelper;
import helper.HttpCode;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
public class DeleteEmployeeContractTest extends BaseTest {

    private final String FIRSTNAME = "Olga";
    private final String SURNAME = "Bobrova";
    private final String POSITION = "Engineer";
    private final String CITY = "Murmansk";

    @BeforeEach
    @Step("Создание сотрудника и получение его по id")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
        envHelper = new EnvHelper();
    }

    @Test
    @DisplayName("DELETE. Удаление сотрудника, статус код 200")
    public void deleteEmployeeStatusCode200Test() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().
                delete("/employee/" + createdEmployeeId).
                then().log().all().
                statusCode(HttpCode.OK);
    }

    @Test
    @DisplayName("DELETE. Удаление сотрудника, Content-Type")
    public void deleteEmployeeContentTypeTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().
                delete("/employee/" + createdEmployeeId).
                then().log().all().
                header("Content-Type", equalTo("application/json"));
    }

    @Test
    @DisplayName("DELETE. Удаление сотрудника, текст и тип сообщения")
    public void deleteEmployeeCheckMessageTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().
                delete("/employee/" + createdEmployeeId).
                then().log().all().
                body("message", equalTo("Deleted")).
                body("message", isA(String.class));
    }

    @Test
    @DisplayName("DELETE. Сотрудник не найден по id, статус код 404")
    public void deleteEmployeeNotFoundStatusCode404Test() throws SQLException {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().
                delete("/employee/" + 778676767).
                then().log().all().
                statusCode(HttpCode.NOT_FOUND);

        employeeHelperDB.deleteEmployee(createdEmployeeId);
    }

    @Test
    @DisplayName("DELETE. Удаление сотрудника, скорость ответа, не более 1300 миллисекунд")
    public void deleteEmployeeResponseTimeTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                when().
                delete("/employee/" + createdEmployeeId).
                then().log().all().
                time(lessThan(1300L));
    }
}