import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierAuthTest {
    Random random = new Random();
    String login = "courier" + random.nextInt(999999);
    String password = "3RuS)+7u[S";
    String firstName = login + " Firstname";
    Courier courier = new Courier(login, password, firstName);

    String createCourierAPI = "/api/v1/courier";
    String loginCourierAPI = "/api/v1/courier/login";
    String deleteCourierAPI = "/api/v1/courier/";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(createCourierAPI);

        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);
    }

    // курьер может авторизоваться
    @Test
    public void authCourierAndCheckResponse() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(loginCourierAPI);

        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);

    }
    // для авторизации нужно передать все обязательные поля
    @Test
    public void authCourierWithoutLoginAndCheckResponse() {
        String json = "{\"login\": \"\", \"password\": \"" + password + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(loginCourierAPI);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }

    @Test
    public void authCourierWithoutPasswordAndCheckResponse() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(loginCourierAPI);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }

    // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    @Test
    public void authNonExistentCourierAndCheckResponse() {
        String json = "{\"login\": \"" + login + random.nextInt(999999) + "\", \"password\": \"" + password + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(loginCourierAPI);

        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }

    @After
    public void cleanData() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(loginCourierAPI);
        System.out.println(response.asPrettyString());
        String id = Integer.toString(response.then().extract().path("id"));
        given()
                .delete(deleteCourierAPI + id)
                .then()
                .statusCode(200);
    }

}
