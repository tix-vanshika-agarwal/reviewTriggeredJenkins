package com.tiket.io.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public record BasicAuth(String username, String password) implements IAuth {

    @Override
    public RequestSpecification auth() {
        return RestAssured.given().auth().basic(username, password);
    }
}
