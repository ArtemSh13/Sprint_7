import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreateTest {

    Random random = new Random();
    String login = "courier" + random.nextInt(999999);
    String password = "3RuS)+7u[S";
    String firstName = login + " Firstname";
    Courier courier = new Courier(login, password, firstName);


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
        Gson gson = new Gson();
        String json = gson.toJson(courier);
        System.out.println(json);
    }

    public void cleanData() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"" + password + "\"}";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(ScooterAPI.LOGIN_COURIER_API);
        System.out.println(response.asPrettyString());
        String id = Integer.toString(response.then().extract().path("id"));
        given()
                .delete(ScooterAPI.DELETE_COURIER_API + id)
                .then()
                .statusCode(200);
    }

    // курьера можно создать
    @Test
    @DisplayName("createCourierAndCheckResponse")
    public void createCourierAndCheckResponse() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(ScooterAPI.CREATE_COURIER_API);

        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(201);

        cleanData();
    }

    // нельзя создать двух одинаковых курьеров
    @Test
    @DisplayName("createDuplicateCourierAndCheckResponse")
    public void createDuplicateCourierAndCheckResponse() {
        // создаем первого курьера
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(ScooterAPI.CREATE_COURIER_API)
                .then()
                .assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);

        // создаем дубликат
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(courier)
                        .when()
                        .post(ScooterAPI.CREATE_COURIER_API);

        response.then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                .and().statusCode(409);

        cleanData();
    }

    // чтобы создать курьера, нужно передать в ручку все обязательные поля
    @Test
    @DisplayName("createCourierWithoutLoginAndCheckResponse")
    public void createCourierWithoutLoginAndCheckResponse() {
        String json = "{\"login\": \"\", \"password\": \"" + password + "\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(ScooterAPI.CREATE_COURIER_API);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);
    }

    @Test
    @DisplayName("createCourierWithoutPasswordAndCheckResponse")
    public void createCourierWithoutPasswordAndCheckResponse() {
        String json = "{\"login\": \"" + login + "\", \"password\": \"\"}";

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post(ScooterAPI.CREATE_COURIER_API);

        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                .and().statusCode(400);

    }

}
