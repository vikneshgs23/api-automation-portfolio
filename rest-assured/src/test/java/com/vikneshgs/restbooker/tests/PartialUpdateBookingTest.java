package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.AuthUtil;
import com.vikneshgs.restbooker.utils.BookingUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PartialUpdateBookingTest extends BaseTest {

    private String token;
    private String lastname;
    private int bookingId;

    @BeforeClass
    public void getAuthToken() {
        token = AuthUtil.getToken();
    }

    // A fresh booking for every test, so no test depends on what another one changed
    @BeforeMethod
    public void createFreshBooking() {
        lastname = "Tester" + System.currentTimeMillis();
        bookingId = BookingUtil.createBooking("Viknesh", lastname);
    }

    // cookie = null sends the request without any token
    private ValidatableResponse patch(String cookie, String body) {
        RequestSpecification spec = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(body);
        if (cookie != null) {
            spec = spec.header("Cookie", cookie);
        }
        return spec.when().patch("/booking/" + bookingId).then();
    }

    // TC_27
    @Test
    public void patchFirstnameOnlyKeepsOtherFields() {
        patch("token=" + token, "{\"firstname\":\"Sam\"}")
                .statusCode(200)
                .body("firstname", equalTo("Sam"))
                .body("lastname", equalTo(lastname))
                .body("totalprice", equalTo(111))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2026-01-01"))
                .body("bookingdates.checkout", equalTo("2026-01-05"))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    // TC_28
    @Test
    public void patchFirstAndLastNameKeepsOtherFields() {
        patch("token=" + token, "{\"firstname\":\"Sam\",\"lastname\":\"Lee\"}")
                .statusCode(200)
                .body("firstname", equalTo("Sam"))
                .body("lastname", equalTo("Lee"))
                .body("totalprice", equalTo(111))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2026-01-01"))
                .body("bookingdates.checkout", equalTo("2026-01-05"))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    // TC_29
    @Test
    public void patchWithoutTokenIsForbidden() {
        patch(null, "{\"firstname\":\"NoToken\"}")
                .statusCode(403)
                .body(equalTo("Forbidden"));
    }
}