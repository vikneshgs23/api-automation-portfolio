package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PingTest extends BaseTest {

    @Test
    public void healthCheckReturnsCreated() {
        given()
                .when()
                .get("/ping")
                .then()
                .statusCode(201)
                .body(equalTo("Created"));
    }
}