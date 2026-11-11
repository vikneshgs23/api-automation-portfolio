package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.utils.BookingUtil;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesPattern;

public class GetBookingTest extends BaseTest {

    private String firstname;
    private String lastname;
    private int bookingId;

    // The shared API wipes data, so create our own booking
    @BeforeClass
    public void createTestBooking() {
        long stamp = System.currentTimeMillis();
        firstname = "Viknesh" + stamp;
        lastname = "Tester" + stamp;
        bookingId = BookingUtil.createBooking(firstname, lastname);
    }

    // TC_11
    @Test
    public void getBookingWithValidIdReturnsAllFields() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("firstname", equalTo(firstname))
                .body("lastname", equalTo(lastname))
                .body("totalprice", equalTo(111))
                .body("depositpaid", equalTo(true))
                .body("bookingdates.checkin", equalTo("2026-01-01"))
                .body("bookingdates.checkout", equalTo("2026-01-05"))
                .body("additionalneeds", equalTo("Breakfast"));
    }

    // TC_12
    @Test
    public void getBookingWithNonExistentIdReturnsNotFound() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/99999999")
                .then()
                .statusCode(404)
                .body(equalTo("Not Found"));
    }

    // TC_13
    @Test
    public void getBookingWithTextIdReturnsNotFound() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/abc")
                .then()
                .statusCode(404)
                .body(equalTo("Not Found"));
    }

    // TC_14
    @Test
    public void bookingFieldsHaveCorrectDataTypes() {
        given()
                .header("Accept", "application/json")
                .when()
                .get("/booking/{id}", bookingId)
                .then()
                .statusCode(200)
                .body("firstname", instanceOf(String.class))
                .body("lastname", instanceOf(String.class))
                .body("totalprice", instanceOf(Integer.class))
                .body("depositpaid", instanceOf(Boolean.class))
                .body("bookingdates.checkin", matchesPattern("\\d{4}-\\d{2}-\\d{2}"))
                .body("bookingdates.checkout", matchesPattern("\\d{4}-\\d{2}-\\d{2}"))
                .body("additionalneeds", instanceOf(String.class));
    }
}