package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.BookingUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.notNullValue;

public class GetBookingIdsTest extends BaseTest {

    private String firstname;
    private String lastname;
    private int bookingId;

    // The shared API wipes data, so create our own booking with a unique name
    @BeforeClass
    public void createTestBooking() {
        long stamp = System.currentTimeMillis();
        firstname = "Viknesh" + stamp;
        lastname = "Tester" + stamp;
        bookingId = BookingUtil.createBooking(firstname, lastname);
    }

    // TC_07
    @Test
    public void getBookingIdsReturnsNonEmptyList() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("bookingid", everyItem(notNullValue()));
    }

    // TC_08
    @Test
    public void filterByFirstnameReturnsCreatedBooking() {
        given()
                .header("Accept", "application/json")
                .queryParam("firstname", firstname)
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", hasItem(bookingId));
    }

    // TC_09
    @Test
    public void filterByFullNameReturnsCreatedBooking() {
        given()
                .header("Accept", "application/json")
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("bookingid", hasItem(bookingId));
    }

    // TC_10
    @Test
    public void filterByUnknownNameReturnsEmptyList() {
        given()
                .header("Accept", "application/json")
                .queryParam("firstname", "zzzzzz")
                .when()
                .get("/booking")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}