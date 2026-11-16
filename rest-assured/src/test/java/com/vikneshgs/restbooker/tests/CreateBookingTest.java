package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import com.vikneshgs.restbooker.models.Booking;
import com.vikneshgs.restbooker.models.BookingDates;
import com.vikneshgs.restbooker.models.BookingResponse;
import com.vikneshgs.restbooker.utils.BookingUtil;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.nullValue;

public class CreateBookingTest extends BaseTest {

    private static final BookingDates DATES = new BookingDates("2026-01-01", "2026-01-05");

    private ValidatableResponse postBooking(Object body) {
        return given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .body(body)
                .when()
                .post("/booking")
                .then();
    }

    // TC_15: what we get back is exactly what we sent
    @Test
    public void createBookingWithAllFieldsReturnsBookingId() {
        Booking sent = BookingUtil.defaultBooking("Viknesh", "Tester");

        BookingResponse response = postBooking(sent)
                .statusCode(200)
                .extract().as(BookingResponse.class);

        Assert.assertTrue(response.bookingid() > 0, "bookingid should be a positive number");
        Assert.assertEquals(response.booking(), sent);
    }

    // TC_16: additionalneeds is optional (null is left out of the request) and is left out of the response
    @Test
    public void createBookingWithoutAdditionalNeedsSucceeds() {
        Booking sent = new Booking("Viknesh", "Tester", 111, true, DATES, null);

        postBooking(sent)
                .statusCode(200)
                .body("bookingid", instanceOf(Integer.class))
                .body("booking", not(hasKey("additionalneeds")));
    }

    // TC_17
    @Test
    public void createBookingWithoutFirstnameReturnsServerError() {
        postBooking(new Booking(null, "Tester", 111, true, DATES, "Breakfast"))
                .statusCode(500);
    }

    // TC_18
    @Test
    public void createBookingWithoutDatesReturnsServerError() {
        postBooking(new Booking("Viknesh", "Tester", 111, true, null, "Breakfast"))
                .statusCode(500);
    }

    // TC_19 DEFECT: a text price should be rejected, but the API accepts it and stores null.
    // A typed Booking can't hold text in a number field, so this one uses a Map on purpose.
    @Test
    public void defect_nonNumericTotalPriceIsAcceptedAsNull() {
        Map<String, Object> badBooking = Map.of(
                "firstname", "Viknesh",
                "lastname", "Tester",
                "totalprice", "abc",
                "depositpaid", true,
                "bookingdates", Map.of("checkin", "2026-01-01", "checkout", "2026-01-05"),
                "additionalneeds", "Breakfast");

        postBooking(badBooking)
                .statusCode(200)
                .body("booking.totalprice", nullValue());
    }

    // TC_20: every field is null, so the request body is {}
    @Test
    public void createBookingWithEmptyBodyReturnsServerError() {
        postBooking(new Booking(null, null, null, null, null, null))
                .statusCode(500);
    }

    // TC_21 DEFECT: checkout before checkin should be rejected, but the API accepts it.
    @Test
    public void defect_checkoutBeforeCheckinIsAccepted() {
        postBooking(new Booking("Viknesh", "Tester", 111, true,
                new BookingDates("2026-01-10", "2026-01-05"), "Breakfast"))
                .statusCode(200);
    }
}