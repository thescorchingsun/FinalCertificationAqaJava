package helper;

import entities.User;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class AuthHelper {

   //private static EnvHelper envHelper;
    private final EnvHelper envHelper = new EnvHelper();

    public AuthHelper() throws IOException {
        RestAssured.baseURI = envHelper.getApiBaseUrl();
    }

    public String getToken(String username, String password) {
        return given().
                body(new User(username, password)).contentType(ContentType.JSON).
                when().
                post("/login").jsonPath().getString("token");
    }

}
