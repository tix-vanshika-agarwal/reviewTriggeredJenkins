package com.tiket.model;

public record DBProperties(
        String environment,
        String tribe,
        String dbHost,
        String dbPort,
        String dbUsername,
        String dbPassword) {

}
