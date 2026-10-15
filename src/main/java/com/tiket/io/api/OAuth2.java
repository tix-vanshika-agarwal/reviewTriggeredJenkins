package com.tiket.io.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public record OAuth2(String token) implements IAuth {

    @Override
    public RequestSpecification auth() {
        return RestAssured.given().auth().oauth2(token);
    }
}
