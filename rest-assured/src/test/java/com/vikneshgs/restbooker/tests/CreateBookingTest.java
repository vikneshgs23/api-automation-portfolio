package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class CreateBookingTest extends BaseTest {

    private static final String VALID = """
        {"firstname":"Viknesh","lastname":"Tester","totalprice":111,"depositpaid":true,
         "bookingdates":{"checkin":"2026-01-01","checkout":"2026-01-05"},"additionalneeds":"Breakfast"}""";

    private ValidatableResponse postBooking(String body) {
        return given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(body)
                .when()
                .post("/booking")
                .then();
    }

    // TC_15
    @Test
    public void createBookingWithAllFieldsReturnsBookingId() {
        postBooking(VALID)
                .statusCode(200)
                .body("bookingid", instanceOf(Integer.class))
                .body("booking.firstname", equalTo("Viknesh"))
                .body("booking.lastname", equalTo("Tester"))
                .body("booking.totalprice", equalTo(111))
                .body("booking.depositpaid", equalTo(true))
                .body("booking.bookingdates.checkin", equalTo("2026-01-01"))
                .body("booking.bookingdates.checkout", equalTo("2026-01-05"))
                .body("booking.additionalneeds", equalTo("Breakfast"));
    }

    // TC_16: additionalneeds is optional and is left out of the response
    @Test
    public void createBookingWithoutAdditionalNeedsSucceeds() {
        postBooking("""
            {"firstname":"Viknesh","lastname":"Tester","totalprice":111,"depositpaid":true,
             "bookingdates":{"checkin":"2026-01-01","checkout":"2026-01-05"}}""")
                .statusCode(200)
                .body("bookingid", instanceOf(Integer.class))
                .body("booking", not(hasKey("additionalneeds")));
    }

    // TC_17
    @Test
    public void createBookingWithoutFirstnameReturnsServerError() {
        postBooking("""
            {"lastname":"Tester","totalprice":111,"depositpaid":true,
             "bookingdates":{"checkin":"2026-01-01","checkout":"2026-01-05"},"additionalneeds":"Breakfast"}""")
                .statusCode(500);
    }

    // TC_18
    @Test
    public void createBookingWithoutDatesReturnsServerError() {
        postBooking("""
            {"firstname":"Viknesh","lastname":"Tester","totalprice":111,"depositpaid":true,
             "additionalneeds":"Breakfast"}""")
                .statusCode(500);
    }

    // TC_19 DEFECT: a text price should be rejected, but the API accepts it and stores null.
    // This test documents the current behaviour, so it will fail once the API is fixed.
    @Test
    public void defect_nonNumericTotalPriceIsAcceptedAsNull() {
        postBooking("""
            {"firstname":"Viknesh","lastname":"Tester","totalprice":"abc","depositpaid":true,
             "bookingdates":{"checkin":"2026-01-01","checkout":"2026-01-05"},"additionalneeds":"Breakfast"}""")
                .statusCode(200)
                .body("booking.totalprice", nullValue());
    }

    // TC_20
    @Test
    public void createBookingWithEmptyBodyReturnsServerError() {
        postBooking("{}")
                .statusCode(500);
    }

    // TC_21 DEFECT: checkout before checkin should be rejected, but the API accepts it.
    @Test
    public void defect_checkoutBeforeCheckinIsAccepted() {
        postBooking("""
            {"firstname":"Viknesh","lastname":"Tester","totalprice":111,"depositpaid":true,
             "bookingdates":{"checkin":"2026-01-10","checkout":"2026-01-05"},"additionalneeds":"Breakfast"}""")
                .statusCode(200);
    }
}