package com.vikneshgs.restbooker.utils;

import com.vikneshgs.restbooker.models.Booking;
import com.vikneshgs.restbooker.models.BookingDates;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class BookingUtil {

    // A valid booking with the default test data; tests change only what they need
    public static Booking defaultBooking(String firstname, String lastname) {
        return new Booking(firstname, lastname, 111, true,
                new BookingDates("2026-01-01", "2026-01-05"), "Breakfast");
    }

    public static int createBooking(String firstname, String lastname) {
        return given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(defaultBooking(firstname, lastname))
                .when()
                .post("/booking")
                .then()
                .statusCode(200)
                .extract().path("bookingid");
    }
}