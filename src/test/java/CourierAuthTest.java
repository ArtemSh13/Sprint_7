import io.qameta.allure.junit4.DisplayName;
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
    String login = DataGenerator.getCourierCourierLogin();
    String password = DataGenerator.getRandomPassword();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Courier courier = new Courier(login, password, login + " Firstname");

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(ScooterAPI.CREATE_COURIER_API);

        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);
    }

    // курьер может авторизоваться
    @Test
    @DisplayName("authCourierAndCheckResponse")
    public void authCourierAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(DataGenerator.getLoginPasswordJson(login, password))
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);

        response.then().assertThat().body("id", notNullValue())
                .and().statusCode(200);

    }
    // для авторизации нужно передать все обязательные поля
    @Test
    @DisplayName("authCourierWithoutLoginAndCheckResponse")
    public void authCourierWithoutLoginAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(DataGenerator.getLoginPasswordJson("", password))
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("authCourierWithoutPasswordAndCheckResponse")
    public void authCourierWithoutPasswordAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(DataGenerator.getLoginPasswordJson(login, ""))
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"))
                .and().statusCode(400);
    }

    // если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
    @Test
    @DisplayName("authNonExistentCourierAndCheckResponse")
    public void authNonExistentCourierAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(DataGenerator.getLoginPasswordJson("nonExistingLogin", password))
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);

        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"))
                .and().statusCode(404);
    }

    @After
    public void cleanData() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(DataGenerator.getLoginPasswordJson(login, password))
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);
        System.out.println(response.asPrettyString());
        String id = Integer.toString(response.then().extract().path("id"));
        given()
                .delete(ScooterAPI.DELETE_COURIER_API + id)
                .then()
                .statusCode(200);
    }

}