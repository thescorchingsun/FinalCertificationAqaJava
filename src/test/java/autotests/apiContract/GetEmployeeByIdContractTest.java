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
public class GetEmployeeByIdContractTest extends BaseTest {

    private final String FIRSTNAME = "Olga";
    private final String SURNAME = "Bobrova";
    private final String POSITION = "Engineer";
    private final String CITY = "Murmansk";

    @BeforeEach
    @Step("Создание сотрудника и получение его по id")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по id, статус код " + HttpCode.OK)
    public void getEmployeeByIdStatusCode200Test() {
        step("Получение информации о сотруднике и проверка статус кода " + HttpCode.OK, () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
                    then().log().all().
                    statusCode(HttpCode.OK);
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по id, Content-Type")
    public void getEmployeeByIdContentTypeTest() {
        step("Получение информации о сотруднике и проверка Content-Type ", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
                    then().log().all().
                    header("Content-Type", equalTo("application/json"));
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по id, типы параметров в ответе, 1 вариант")
    public void getEmployeeByIdResponseTypesVariant1Test() {
        step("Получение информации о сотруднике по id и проверка типов параметров в ответе ", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
                    then().log().all().
                    body("city", isA(String.class)).
                    body("name", isA(String.class)).
                    body("surname", isA(String.class)).
                    body("position", isA(String.class)).
                    body("id", isA(Integer.class));
        });
    }

    @Test
    @DisplayName("GET. Информация о сотруднике по id, типы параметров в ответе, 2 вариант")
    public void getEmployeeByIdResponseTypesVariant2Test() {
        step("Получение информации о сотруднике по id и проверка типов параметров в ответе ", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
                    as(EmployeeResponse.class);
        });
    }

    @Test
    @DisplayName("GET. Для найденного сотрудника по id поля не равны null")
    public void getEmployeeByNameNotNullFieldsTest() {
        step("Получение информации о сотруднике по id и проверка, что поля не равны 0 ", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
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
    @DisplayName("GET. Сотрудник не найден по id, статус код " + HttpCode.NOT_FOUND)
    public void getEmployeeByIdNotFoundStatusCode404Test() {
        step("Получение информации о не найденном по id сотруднике, проверка статус кода " + HttpCode.NOT_FOUND, () -> {
            given().
                    when().
                    get("/employee/" + 5555555).
                    then().log().all().
                    statusCode(HttpCode.NOT_FOUND);
        });
    }

    @Test
    @DisplayName("GET. Сотрудник не найден по id, тип параметра в ответе")
    public void getEmployeeByIdNotFoundResponseTypesTest() {
        step("Получение информации о не найденном по id сотруднике, проверка типа параметра в ответе", () -> {
            given().
                    when().
                    get("/employee/" + 5555555).
                    then().log().all().
                    body("error", isA(String.class));
        });
    }

    @Test
    @DisplayName("GET. Получение сотрудника по id, скорость ответа, не более 1300 миллисекунд")
    public void getEmployeeByNameResponseTimeTest() {
        step("Получение информации по id сотрудника, проверка скорости ответа", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
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
