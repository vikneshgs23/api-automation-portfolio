package com.vikneshgs.restbooker.utils;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class BookingUtil {

    public static int createBooking(String firstname, String lastname) {
        String body = "{\"firstname\":\"" + firstname + "\",\"lastname\":\"" + lastname + "\","
                + "\"totalprice\":111,\"depositpaid\":true,"
                + "\"bookingdates\":{\"checkin\":\"2026-01-01\",\"checkout\":\"2026-01-05\"},"
                + "\"additionalneeds\":\"Breakfast\"}";

        return given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(body)
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");
    }
}