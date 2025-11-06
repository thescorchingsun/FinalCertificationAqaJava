package autotests.apiContract;

import entities.EmployeeRequest;
import helper.AuthHelper;
import helper.EmployeeHelperDB;
import helper.HttpCode;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
public class CreateEmployeeContractTest extends BaseTest {

    private final String FIRSTNAME = "Ivan";
    private final String SURNAME = "Sverchkov";
    private final String POSITION = "Dispatcher";
    private final String CITY = "Petrozavodsk";

    @BeforeEach
    public void setUp() throws SQLException, IOException {
        authHelper = new AuthHelper();
        employeeHelperDB = new EmployeeHelperDB();
    }

    @Test
    @DisplayName("POST. Создание сотрудника, статус код " + HttpCode.CREATED)
    public void createEmployeeCode201Test() {
        String token = getAdminToken();

        step("Создание сотрудника и проверка статус кода " + HttpCode.CREATED, () -> {
            createdEmployeeId = given().
                    body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    statusCode(HttpCode.CREATED).
                    extract().
                    jsonPath().
                    getInt("id");
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника, Content-Type")
    public void createEmployeeContentTypeTest() {
        String token = getAdminToken();

        step("Создание сотрудника и проверка Content-Type", () -> {
            createdEmployeeId = given().
                    body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    header("Content-Type", equalTo("application/json")).
                    extract().
                    jsonPath().
                    getInt("id");
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника, проверка текста и типа успешного сообщения")
    public void createEmployeeCheckMessageTest() {
        String token = getAdminToken();

        step("Создание сотрудника и проверка сообщения об успешном создании", () -> {
            createdEmployeeId = given().
                    body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    body("message", equalTo("Employee created successfully")).
                    body("message", isA(String.class)).
                    extract().
                    jsonPath().
                    getInt("id");
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника с проверкой полей")
    public void createEmployeeWithFieldCheckTest() {
        String token = getAdminToken();

        step("Создание сотрудника", () -> {
            createdEmployeeId = given().
                    body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    extract().
                    jsonPath().
                    getInt("id");
        });
        step("Проверка полей созданного сотрудника", () -> {
            given().
                    when().
                    get("/employee/" + createdEmployeeId).
                    then().log().all().
                    body("city", equalTo(CITY)).
                    body("name", equalTo(FIRSTNAME)).
                    body("surname", equalTo(SURNAME)).
                    body("position", equalTo(POSITION));
        });
    }

    @Test
    @DisplayName("POST. Ошибка создание сотрудника без имени, сообщение об ошибке")
    public void createEmployeeWithOutNameErrorMessageTest() {
        String token = getAdminToken();

        step("Ошибка создания сотрудника и проверка сообщения", () -> {
            given().
                    body(new EmployeeRequest(CITY, "", POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    body("error", equalTo("Missing required fields"));
        });
    }

    @Test
    @DisplayName("POST. Создание сотрудника, скорость ответа, не более 1300 миллисекунд")
    public void createEmployeeResponseTimeTest() {
        String token = getAdminToken();

        step("Создание сотрудника и проверка скорости ответа", () -> {
            createdEmployeeId = given().
                    body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                    contentType(ContentType.JSON).
                    header("Authorization", "Bearer " + token).
                    when().
                    post("/employee").
                    then().log().all().
                    time(lessThan(1300L)).
                    extract().
                    jsonPath().
                    getInt("id");
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