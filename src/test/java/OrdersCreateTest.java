import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

import java.util.Random;

public class OrdersCreateTest {

    String createOrdersAPI = "/api/v1/orders";
    String cancelOrdersAPI = "/api/v1/orders/cancel";

    int track;



    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    // можно указать один из цветов — BLACK или GREY
    @Test
    public void createOrdersWithBLACKAndCheckResponse() {
        Random random = new Random();
        String firstName = "firstName" + random.nextInt(999999);
        String lastName = "lastName" + random.nextInt(999999);
        String address = "address" + random.nextInt(999999);
        String metroStation = "metroStation" + random.nextInt(999999);
        String phone = "phone" + random.nextInt(999999);
        int rentTime = random.nextInt(999);
        String deliveryDate = "2020-06-06";
        String comment = "comment" + random.nextInt(999999);
        String[] color = {"BLACK"};

        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orders)
                        .when()
                        .post(createOrdersAPI);

        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);

        track = response.then().extract().path("track");

    }

    @Test
    public void createOrdersWithGREYAndCheckResponse() {
        Random random = new Random();
        String firstName = "firstName" + random.nextInt(999999);
        String lastName = "lastName" + random.nextInt(999999);
        String address = "address" + random.nextInt(999999);
        String metroStation = "metroStation" + random.nextInt(999999);
        String phone = "phone" + random.nextInt(999999);
        int rentTime = random.nextInt(999);
        String deliveryDate = "2020-06-06";
        String comment = "comment" + random.nextInt(999999);
        String[] color = {"GREY"};

        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orders)
                        .when()
                        .post(createOrdersAPI);
        System.out.println(response.asPrettyString());
        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);

        track = response.then().extract().path("track");

    }

    // можно указать оба цвета
    @Test
    public void createOrdersWithBLACKAndGREYAndCheckResponse() {
        Random random = new Random();
        String firstName = "firstName" + random.nextInt(999999);
        String lastName = "lastName" + random.nextInt(999999);
        String address = "address" + random.nextInt(999999);
        String metroStation = "metroStation" + random.nextInt(999999);
        String phone = "phone" + random.nextInt(999999);
        int rentTime = random.nextInt(999);
        String deliveryDate = "2020-06-06";
        String comment = "comment" + random.nextInt(999999);
        String[] color = {"BLACK", "GREY"};

        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orders)
                        .when()
                        .post(createOrdersAPI);

        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);

        track = response.then().extract().path("track");

    }
    // можно совсем не указывать цвет
    @Test
    public void createOrdersWithoutColorAndCheckResponse() {
        Random random = new Random();
        String firstName = "firstName" + random.nextInt(999999);
        String lastName = "lastName" + random.nextInt(999999);
        String address = "address" + random.nextInt(999999);
        String metroStation = "metroStation" + random.nextInt(999999);
        String phone = "phone" + random.nextInt(999999);
        int rentTime = random.nextInt(999);
        String deliveryDate = "2020-06-06";
        String comment = "comment" + random.nextInt(999999);
        String[] color = {};

        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orders)
                        .when()
                        .post(createOrdersAPI);

        response.then().assertThat().body("track", notNullValue())
                .and().statusCode(201);

        track = response.then().extract().path("track");

    }

    @After
    public void cancelOrders() {
        String json = "{\"track\": " + track + " }";
        System.out.println(json);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .put(cancelOrdersAPI);
        System.out.println(response.asPrettyString());
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(200);
    }
}
