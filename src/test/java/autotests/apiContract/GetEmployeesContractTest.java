package autotests.apiContract;

import helper.HttpCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Slf4j
public class GetEmployeesContractTest extends BaseTest {

    @Test
    @DisplayName("GET. Получение всех сотрудников, статус код 200")
    public void getEmployeesStatusCode200Test() {
        given().
                when().
                get("/employees").
                then().log().all().
                statusCode(HttpCode.OK);
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников, Content-Type")
    public void getEmployeesContentTypeTest() {
        given().
                when().
                get("/employees").
                then().log().all().
                header("Content-Type", equalTo("application/json"));
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников, в ответе приходит массив данных")
    public void getEmployeesСontainsListOfEmployeesTest() {
        given().
                when().
                get("/employees").
                then().log().all().
                statusCode(HttpCode.OK).
                body("$", is(instanceOf(java.util.List.class))) // Проверка, что в ответе приходит список
                .body("size()", greaterThan(0)); // Проверка, что список не пустой (есть хотя бы один сотрудник)
    }

    @Test
    @DisplayName("GET. Получение всех сотрудников м проверкой первого, обязательные поля")
    public void getEmployeesStatusCode200AndFieldsTest() {
        given().
                when().
                get("/employees").
                then().log().all().
                body("$", is(instanceOf(java.util.List.class)))
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue())
                .body("[0].name", notNullValue())
                .body("[0].surname", notNullValue())
                .body("[0].city", notNullValue())
                .body("[0].position", notNullValue());
    }

    @Test
    @DisplayName("GET. Получение всех сотрудник, скорость ответа, не более 1300 миллисекунд")
    public void getEmployeesResponseTimeTest() {
        given().
                when().
                get("/employees").
                then().log().all().
                time(lessThan(1300L));
    }

}
