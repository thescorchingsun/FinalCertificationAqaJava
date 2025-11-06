package autotests.apiContract;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import helper.HttpCode;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.lessThan;

@Slf4j
public class GetEmployeeByNameContractTest extends BaseTest {

    private final String FIRSTNAME = "Valentina";
    private final String SURNAME = "Romashkina";
    private final String POSITION = "Artist";
    private final String CITY = "Abakan";

    @BeforeEach
    @Step("Создание сотрудника и получение его по id")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по имени, статус код " + HttpCode.OK)
    public void getEmployeeByNameStatusCode200Test() {
        step("Получение информации о сотруднике по имени и проверка статус кода " + HttpCode.OK, () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    then().log().all().
                    statusCode(HttpCode.OK).
                    body("name", equalTo(FIRSTNAME));
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по имени, Content-Type")
    public void getEmployeeByNameContentTypeTest() {
        step("Получение информации о сотруднике по имени и проверка Content-Type", () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    then().log().all().
                    header("Content-Type", equalTo("application/json"));
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по имени, типы параметров в ответе, 1 вариант")
    public void getEmployeeByNameResponseTypesVariant1Test() {
        step("Получение информации о сотруднике по имени и проверка типов параметров в ответе", () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    then().log().all().
                    body("city", isA(String.class)).
                    body("name", isA(String.class)).
                    body("surname", isA(String.class)).
                    body("position", isA(String.class)).
                    body("id", isA(Integer.class));
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по имени, типы параметров в ответе, 2 вариант")
    public void getEmployeeByNameResponseTypesVariant2Test() {
        step("Получение информации о сотруднике по имени и проверка типов параметров в ответе", () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    as(EmployeeResponse.class);
        });
    }

    @Test
    @DisplayName("GET. Для найденного сотрудника по имени поля не равны null")
    public void getEmployeeByNameNotNullFieldsTest() {
        step("Получение информации о сотруднике по имени и проверка, что поля не равны 0 ", () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    then().log().all().
                    statusCode(HttpCode.OK).
                    body("id", notNullValue()).
                    body("name", notNullValue()).
                    body("surname", notNullValue()).
                    body("city", notNullValue()).
                    body("position", notNullValue());
        });
    }

    @Test
    @DisplayName("GET. Сотрудник не найден по имени, статус код " + HttpCode.NOT_FOUND)
    public void getEmployeeByNameNotFoundStatusCode404Test() {
        step("Получение информации о не найденном по имени сотруднике, проверка статус кода " + HttpCode.NOT_FOUND, () -> {
            given().
                    when().
                    get("/employee/name/" + "unknownName878787").
                    then().log().all().
                    statusCode(HttpCode.NOT_FOUND);
        });
    }

    @Test
    @DisplayName("GET. Сотрудник не найден по имени, тип параметра в ответе")
    public void getEmployeeByNameNotFoundResponseTypesTest() {
        step("Получение информации о не найденном по имени сотруднике, проверка типа параметра в ответе", () -> {
            given().
                    when().
                    get("/employee/name/" + "unknownName878787").
                    then().log().all().
                    body("error", isA(String.class));
        });
    }

    @Test
    @DisplayName("GET. Получение сотрудника по имени, скорость ответа, не более 1300 миллисекунд")
    public void getEmployeeByNameResponseTimeTest() {
        step("Получение информации по имени сотрудника, проверка скорости ответа", () -> {
            given().
                    when().
                    get("/employee/name/" + FIRSTNAME).
                    then().log().all().
                    time(lessThan(1300L));
        });
    }

    @AfterEach
    @Step("Удаление сотрудника после теста")
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }
    }
}
