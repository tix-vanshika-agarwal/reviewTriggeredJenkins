package com.tiket.io.network;

import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class GoogleSheetApi {

    private static final Logger logger = LogManager.getLogger(GoogleSheetApi.class);

    private static final String API_KEY = Configs.CREDENTIALS.getProperty("google.sheet.api.key");
    private static final String SHEET_ID = Configs.CREDENTIALS.getProperty("google.sheet.id");
    private static final String SHEET_NAME = Configs.CREDENTIALS.getProperty("google.sheet.name");
    private static final String URI_FORMAT = "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s?key=%s";

    @SneakyThrows
    public static JSONObject getData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(URI_FORMAT, SHEET_ID, SHEET_NAME, API_KEY)))
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .get();
        logger.debug("Got google sheet data: \n" + response);
        return new JSONObject(response);
    }
}
