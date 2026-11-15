package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.AuthUtil;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookingLifecycleTest extends BaseTest {

    // TC_33: create, get, update, patch, delete, then confirm the booking is gone
    @Test
    public void fullBookingLifecycle() {
        String token = AuthUtil.getToken();

        // 1. Create
        int id = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body("""
                    {"firstname":"E2E","lastname":"Flow","totalprice":100,"depositpaid":true,
                     "bookingdates":{"checkin":"2026-06-01","checkout":"2026-06-05"},"additionalneeds":"Breakfast"}""")
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");

        // 2. Get: the data matches what was created
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + id)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("E2E"))
                .body("lastname", equalTo("Flow"))
                .body("totalprice", equalTo(100));

        // 3. Full update
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body("""
                {"firstname":"E2EUpdated","lastname":"FlowUpdated","totalprice":200,"depositpaid":false,
                 "bookingdates":{"checkin":"2026-07-01","checkout":"2026-07-05"},"additionalneeds":"Lunch"}""")
                .when()
                .put("/booking/" + id)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("E2EUpdated"))
                .body("totalprice", equalTo(200));

        // 4. Partial update: only firstname changes, the rest keeps the update from step 3
        given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body("{\"firstname\":\"E2EPatched\"}")
                .when()
                .patch("/booking/" + id)
                .then()
                .statusCode(200)
                .body("firstname", equalTo("E2EPatched"))
                .body("lastname", equalTo("FlowUpdated"))
                .body("totalprice", equalTo(200))
                .body("additionalneeds", equalTo("Lunch"));

        // 5. Delete
        given()
                .header("Cookie", "token=" + token)
                .when()
                .delete("/booking/" + id)
                .then()
                .statusCode(201);

        // 6. The booking is gone
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + id)
                .then()
                .statusCode(404);
    }
}