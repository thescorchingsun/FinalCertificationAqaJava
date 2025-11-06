package autotests.apiContract;

import helper.HttpCode;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
@Epic("Contract Tests")
@Story("Get Employees")
public class GetEmployeesContractTest extends BaseTest {

    @Test
    @DisplayName("GET. Получение всех сотрудников, статус код " + HttpCode.OK)
    public void getEmployeesStatusCode200Test() {
        step("Получение всех сотрудников и проверка статус кода " + HttpCode.OK, () -> {
            given().
                    when().
                    get("/employees").
                    then().log().all().
                    statusCode(HttpCode.OK);
        });
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников, Content-Type")
    public void getEmployeesContentTypeTest() {
        step("Получение всех сотрудников и проверка Content-Type ", () -> {
            given().
                    when().
                    get("/employees").
                    then().log().all().
                    header("Content-Type", equalTo("application/json"));
        });
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников, в ответе приходит массив данных")
    public void getEmployeesСontainsListOfEmployeesTest() {
        step("Получение всех сотрудников и проверка, что в ответе приходит список и в нем есть хотя бы один сотрудник ", () -> {
            given().
                    when().
                    get("/employees").
                    then().log().all().
                    statusCode(HttpCode.OK).
                    body("$", is(instanceOf(java.util.List.class))).
                    body("size()", greaterThan(0));
        });
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников с проверкой первого, обязательные поля")
    public void getEmployeesStatusCode200AndFieldsTest() {
        step("Получение всех сотрудников и выборка первого с проверкой обязательного заполнения полей, что они не равны 0", () -> {
            given().
                    when().
                    get("/employees").
                    then().log().all().
                    body("$", is(instanceOf(java.util.List.class))).
                    body("size()", greaterThan(0)).
                    body("[0].id", notNullValue()).
                    body("[0].name", notNullValue()).
                    body("[0].surname", notNullValue()).
                    body("[0].city", notNullValue()).
                    body("[0].position", notNullValue());
        });
    }

    @Test
    @DisplayName("GET. Получение всех сотрудник, скорость ответа, не более 1300 миллисекунд")
    public void getEmployeesResponseTimeTest() {
        step("Получение информации о всех сотрудниках, проверка скорости ответа", () -> {
            given().
                    when().
                    get("/employees").
                    then().log().all().
                    time(lessThan(1300L), TimeUnit.MILLISECONDS);
        });
    }

}
