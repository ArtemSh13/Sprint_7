import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


@RunWith(Parameterized.class)
public class OrdersCreateTest {

    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;
    private int track;

    public OrdersCreateTest(String[] color) {
        this.firstName = DataGenerator.getRandomFirstName();
        this.lastName = DataGenerator.getRandomLastName();
        this.address = DataGenerator.getRandomAddress();
        this.metroStation = DataGenerator.getRandomMetroStation();
        this.phone = DataGenerator.getRandomPhone();
        this.rentTime = DataGenerator.getRandomRentTime();
        this.deliveryDate = DataGenerator.getRandomDeliveryDate();
        this.comment = DataGenerator.getRandomComment();
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getRequestData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}

        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    // можно указать один из цветов — BLACK или GREY
    @Test
    @DisplayName("createOrdersAndCheckResponse")
    public void createOrdersAndCheckResponse() {

        Orders orders = new Orders(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        Gson gson = new Gson();
        String json = gson.toJson(orders);
        System.out.println(json);

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(orders)
                        .when()
                        .post(ScooterAPI.CREATE_ORDERS_API);

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
                        .put(ScooterAPI.CANCEL_ORDERS_API);
        System.out.println(response.asPrettyString());
        response.then().assertThat().body("ok", equalTo(true))
                .and().statusCode(200);
    }
}
