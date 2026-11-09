package com.vikneshgs.restbooker.tests;

import com.vikneshgs.restbooker.base.BaseTest;
import io.restassured.http.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class AuthTest extends BaseTest {

    // TC_02
    @Test
    public void validCredentialsReturnToken() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"username\":\"admin\",\"password\":\"password123\"}")
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token", not(emptyOrNullString()));
    }

    // TC_03 to TC_06
    @Test(dataProvider = "badCredentials")
    public void badCredentialsReturnReason(String testCase, String body) {
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/auth")
                .then()
                .statusCode(200)
                .body("reason", equalTo("Bad credentials"))
                .body("token", nullValue());
    }

    @DataProvider(name = "badCredentials")
    public Object[][] badCredentials() {
        return new Object[][] {
                {"TC_03 wrong password", "{\"username\":\"admin\",\"password\":\"wrong\"}"},
                {"TC_04 wrong username", "{\"username\":\"wrong\",\"password\":\"password123\"}"},
                {"TC_05 empty body", "{}"},
                {"TC_06 empty values", "{\"username\":\"\",\"password\":\"\"}"}
        };
    }
}