package helper;

import entities.EmployeeRequest;
import entities.EmployeeResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;

import static io.restassured.RestAssured.given;

/** Инкапсулирует логику работы с REST API, предоставляя тестам методы для создания, получения и удаления сотрудников */
public class EmployeeHelper {

    private final AuthHelper authHelper;
    static EnvHelper envHelper;

    public EmployeeHelper() throws IOException {
        authHelper = new AuthHelper();
        RestAssured.baseURI = envHelper.getApiBaseUrl();
    }

    public int createEmployee(EmployeeRequest employee) {
        String token = authHelper.getToken("admin", "admin");

        JsonPath jsonPath = given()
                .body(employee)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token).
                when().
                post("/employee").jsonPath();
        try {
            return jsonPath.getInt("id");
        } catch (NullPointerException nullPointerException) {
            return -1;
        }
    }

    public EmployeeResponse getEmployee(int id) {
        Response response = given().
                when().
                get("/employee/" + id);
        try {
            return response.as(EmployeeResponse.class);
        } catch (IllegalStateException exception) {
            return new EmployeeResponse();
        }
    }

    public void deleteEmployee(int id) {
        given().
                when().
                delete("/employee/" + id);
    }
}
