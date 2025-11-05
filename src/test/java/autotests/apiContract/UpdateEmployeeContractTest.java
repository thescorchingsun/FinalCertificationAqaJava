package autotests.apiContract;

import entities.EmployeeRequest;
import entities.ValidationErrorResponse;
import helper.HttpCode;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
public class UpdateEmployeeContractTest extends BaseTest {

    private final String FIRSTNAME = "Olga";
    private final String SURNAME = "Romashkina";
    private final String POSITION = "Engineer";
    private final String CITY = "Murmansk";

    private final String FIRSTNAME_2 = "James";
    private final String SURNAME_2 = "Taylor";
    private final String POSITION_2 = "Firefighter";
    private final String CITY_2 = "Madrid";


    @BeforeEach
    @Step("Создание сотрудника и получение его по id")
    public void setUp() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME);
        createdEmployeeId = employeeHelperDB.createEmployee(employeeRequest);
    }

    @Test
    @DisplayName("PUT. Обновление сотрудника по всем полям, статус код 200")
    public void putEmployeeUpdateAllFieldsStatusCode200Test() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, SURNAME_2);

        createdEmployeeId = given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(updatedRequest).
                when().
                put("/employee/" + createdEmployeeId).
                then().log().all().
                statusCode(HttpCode.OK).
                extract().
                jsonPath().
                getInt("id");
        given().
                when().
                get("/employee/" + createdEmployeeId).
                then().log().all().
                body("name", equalTo(FIRSTNAME_2)).
                body("surname", equalTo(SURNAME_2)).
                body("position", equalTo(POSITION_2)).
                body("city", equalTo(CITY_2));
    }

    @Test
    @DisplayName("PUT. Обновление сотрудника по всем полям, Content-type")
    public void putEmployeeUpdateContentTypeTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, SURNAME_2);

        createdEmployeeId = given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(updatedRequest).
                when().
                put("/employee/" + createdEmployeeId).
                then().log().all().
                header("Content-Type", equalTo("application/json")).
                extract().
                jsonPath().
                getInt("id");
    }

    @Test
    @DisplayName("PUT. Обновление сотрудника по всем полям, проверка текста и типа успешного сообщения")
    public void putEmployeeUpdateCheckMessageTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME_2, POSITION_2, SURNAME_2);

        createdEmployeeId = given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(updatedRequest).
                when().
                put("/employee/" + createdEmployeeId).
                then().log().all().
                body("message", equalTo("Employee updated successfully")).
                body("message", isA(String.class)).
                extract().
                jsonPath().
                getInt("id");
    }


    @Test
    @DisplayName("PUT. Ошибка при обновлении сотрудника без обязательного поля position. Статус код 400, текст и тип ошибки")
    public void putEmployeeWithoutPositionCheckBodyStatusCode400Test() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                body(new EmployeeRequest(FIRSTNAME_2, CITY_2, SURNAME_2)).
                contentType(ContentType.JSON).
                header("Authorization", "Bearer " + token).
                when().
                put("/employee/" + createdEmployeeId).
                then().log().all().
                statusCode(HttpCode.BAD_REQUEST).
                body("error", equalTo("Invalid field types")).
                body("error", isA(String.class)).
                extract().
                body().as(ValidationErrorResponse.class);
    }

    @Test
    @DisplayName("PUT. Cотрудник не найден по id . Статус код 404, тип ошибки ")
    public void putEmployeeNotFoundStatusCode404Test() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        given().
                body(new EmployeeRequest(CITY, FIRSTNAME, POSITION, SURNAME)).
                contentType(ContentType.JSON).
                header("Authorization", "Bearer " + token).
                when().
                put("/employee/" + "24234234").
                then().log().all().
                statusCode(HttpCode.NOT_FOUND).
                body("error", isA(String.class)).
                extract().
                body().as(ValidationErrorResponse.class);
    }

    @Test
    @DisplayName("PUT. Обновить сотрудника, скорость ответа, не более 1300 миллисекунд")
    public void putUpdateEmployeeResponseTimeTest() {
        String token = authHelper.getToken(envHelper.getAdminLogin(), envHelper.getAdminPassword());

        EmployeeRequest updatedRequest = new EmployeeRequest(CITY_2, FIRSTNAME, POSITION_2, SURNAME);

        createdEmployeeId = given().
                header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                body(updatedRequest).
                when().
                put("/employee/" + createdEmployeeId).
                then().log().all().
                time(lessThan(1300L)).
                extract().
                jsonPath().
                getInt("id");
    }

    @AfterEach
    @Step("Удаление сотрудника после теста")
    public void tearDown() throws SQLException {
        if (createdEmployeeId != -1) {
            employeeHelperDB.deleteEmployee(createdEmployeeId);
        }
    }
}