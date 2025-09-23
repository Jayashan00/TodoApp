package com.example.todoapp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {
    private String authCookie;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        // Login to get authentication cookie
        this.authCookie = given()
            .contentType(ContentType.URLENC)
            .formParam("username", "user")
            .formParam("password", "user")
            .post("/login")
            .getCookie("JSESSIONID");
    }

    @Test
    public void testGetTasks() {
        given()
            .cookie("JSESSIONID", authCookie)
            .get("/api/tasks")
            .then()
            .statusCode(200);
    }
    @Test
    public void testAddInvalid() {
        given()
            .cookie("JSESSIONID", authCookie)
            .contentType(ContentType.JSON)
            .body("{\"description\":\"\"}")
            .post("/api/tasks")
            .then()
            .statusCode(302);
    }
}