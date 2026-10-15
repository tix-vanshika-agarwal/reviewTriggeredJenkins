package com.tiket.io.api;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public record OAuth1(
        String consumerKey,
        String consumerSecret,
        String accessToken,
        String secretToken
) implements IAuth {

    @Override
    public RequestSpecification auth() {
        return RestAssured.given().auth().oauth(consumerKey, consumerSecret, accessToken, secretToken);
    }
}
