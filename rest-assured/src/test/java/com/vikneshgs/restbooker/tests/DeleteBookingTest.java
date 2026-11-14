package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.AuthUtil;
import com.vikneshgs.restbooker.utils.BookingUtil;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteBookingTest extends BaseTest {

    private String token;
    private int bookingId;

    @BeforeClass
    public void getAuthToken() {
        token = AuthUtil.getToken();
    }

    // TC_30 deletes its booking, so every test gets a fresh one
    @BeforeMethod
    public void createFreshBooking() {
        bookingId = BookingUtil.createBooking("Viknesh" + System.currentTimeMillis(), "Tester");
    }

    // cookie = null sends the request without any token
    private ValidatableResponse delete(String path, String cookie) {
        RequestSpecification spec = given();
        if (cookie != null) {
            spec = spec.header("Cookie", cookie);
        }
        return spec.when().delete(path).then();
    }

    // TC_30
    @Test
    public void deleteWithValidTokenReturnsCreated() {
        delete("/booking/" + bookingId, "token=" + token)
                .statusCode(201)
                .body(equalTo("Created"));

        // the booking should really be gone
        given().header("Accept", "application/json")
                .when().get("/booking/" + bookingId)
                .then().statusCode(404);
    }

    // TC_31
    @Test
    public void deleteWithoutTokenIsForbidden() {
        delete("/booking/" + bookingId, null)
                .statusCode(403)
                .body(equalTo("Forbidden"));
    }

    // TC_32: a missing booking gives 405 instead of 404 (known API quirk)
    @Test
    public void deleteNonExistentBookingReturnsMethodNotAllowed() {
        delete("/booking/99999999", "token=" + token)
                .statusCode(405)
                .body(equalTo("Method Not Allowed"));
    }
}