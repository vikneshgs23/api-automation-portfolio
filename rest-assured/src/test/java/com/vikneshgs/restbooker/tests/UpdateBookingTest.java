package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.AuthUtil;
import com.vikneshgs.restbooker.utils.BookingUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateBookingTest extends BaseTest {

    private static final String UPDATED = """
        {"firstname":"VikneshUpdated","lastname":"TesterUpdated","totalprice":222,"depositpaid":false,
         "bookingdates":{"checkin":"2026-02-01","checkout":"2026-02-05"},"additionalneeds":"Lunch"}""";

    private String token;
    private int bookingId;

    // The shared API wipes data, so create our own booking and token
    @BeforeClass
    public void setUpData() {
        token = AuthUtil.getToken();
        bookingId = BookingUtil.createBooking("Viknesh" + System.currentTimeMillis(), "Tester");
    }

    // cookie = null sends the request without any token
    private ValidatableResponse put(String path, String cookie, String body) {
        RequestSpecification spec = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(body);
        if (cookie != null) {
            spec = spec.header("Cookie", cookie);
        }
        return spec.when().put(path).then();
    }

    // TC_22
    @Test
    public void fullUpdateWithValidTokenUpdatesAllFields() {
        put("/booking/" + bookingId, "token=" + token, UPDATED)
                .statusCode(200)
                .body("firstname", equalTo("VikneshUpdated"))
                .body("lastname", equalTo("TesterUpdated"))
                .body("totalprice", equalTo(222))
                .body("depositpaid", equalTo(false))
                .body("bookingdates.checkin", equalTo("2026-02-01"))
                .body("bookingdates.checkout", equalTo("2026-02-05"))
                .body("additionalneeds", equalTo("Lunch"));
    }

    // TC_23
    @Test
    public void updateWithoutTokenIsForbidden() {
        put("/booking/" + bookingId, null, UPDATED)
                .statusCode(403)
                .body(equalTo("Forbidden"));
    }

    // TC_24
    @Test
    public void updateWithInvalidTokenIsForbidden() {
        put("/booking/" + bookingId, "token=invalid123", UPDATED)
                .statusCode(403)
                .body(equalTo("Forbidden"));
    }

    // TC_25: a missing booking gives 405 instead of 404 (known API quirk)
    @Test
    public void updateNonExistentBookingReturnsMethodNotAllowed() {
        put("/booking/99999999", "token=" + token, UPDATED)
                .statusCode(405)
                .body(equalTo("Method Not Allowed"));
    }

    // TC_26
    @Test
    public void updateWithMissingLastnameReturnsBadRequest() {
        put("/booking/" + bookingId, "token=" + token, """
            {"firstname":"Viknesh","totalprice":111,"depositpaid":true,
             "bookingdates":{"checkin":"2026-01-01","checkout":"2026-01-05"},"additionalneeds":"Breakfast"}""")
                .statusCode(400);
    }
}