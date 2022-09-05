import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("getOrderListAndCheckResponse")
    public void getOrderListAndCheckResponse() {
        Response response =
                given().get(ScooterAPI.GET_ORDERS_LIST_API);

        System.out.println(response.asPrettyString());
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }

    @Test
    @DisplayName("getOrderListByInvalidCourierIdAndCheckResponse")
    public void getOrderListByInvalidCourierIdAndCheckResponse() {
        int courierId = 1234567890;
        Response response =
                given().get(ScooterAPI.GET_ORDERS_LIST_API + "?courierId={id}", courierId);
        System.out.println(response.asPrettyString());
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
    }

    @Test
    @DisplayName("getOrderListByNearestStationнеобязательныйAndCheckResponse")
    public void getOrderListByNearestStationнеобязательныйAndCheckResponse() {
        Response response =
                given().get(ScooterAPI.GET_ORDERS_LIST_API + "?nearestStation=[\"2\", \"3\"]");
        System.out.println(response.asPrettyString());
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }

    @Test
    @DisplayName("getOrderListByLimitIdAndCheckResponse")
    public void getOrderListByLimitIdAndCheckResponse() {
        Response response =
                given().get(ScooterAPI.GET_ORDERS_LIST_API + "?limit=10");
        System.out.println(response.asPrettyString());
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }

    @Test
    @DisplayName("getOrderListByPageAndCheckResponse")
    public void getOrderListByPageAndCheckResponse() {
        Response response =
                given().get(ScooterAPI.GET_ORDERS_LIST_API + "?page={page}", 1);
        System.out.println(response.asPrettyString());
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());
    }

}
