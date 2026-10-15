package com.tiket.io.network;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ImpersonatedCredentials;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetApi2 {

    private static final Logger logger = LogManager.getLogger(GoogleSheetApi2.class);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TARGET_SERVICE_ACCOUNT = Configs.CREDENTIALS.getProperty("google.sa.email");
    private static final String spreadsheetId = Configs.CREDENTIALS.getProperty("google.sheet.id");

    @SneakyThrows
    public static List<List<String>> getData(int index) {
        logger.debug("Using service account impersonation: " + TARGET_SERVICE_ACCOUNT);
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final HttpRequestInitializer requestInitializer = getCredentials();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer).build();
        Spreadsheet response = service.spreadsheets().get(spreadsheetId).execute();
        ValueRange valueRange = service.spreadsheets().values()
                .get(spreadsheetId, response.getSheets().get(index).getProperties().getTitle())
                .execute();
        List<List<Object>> values = valueRange.getValues();
        return transform(values);
    }

    private static HttpRequestInitializer getCredentials() {
        try {
            GoogleCredentials sourceCredentials = GoogleCredentials.getApplicationDefault();
            ImpersonatedCredentials impersonatedCredentials = ImpersonatedCredentials.newBuilder()
                    .setSourceCredentials(sourceCredentials)
                    .setTargetPrincipal(TARGET_SERVICE_ACCOUNT)
                    .setScopes(Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY))
                    .build();
            return new HttpCredentialsAdapter(impersonatedCredentials);
        } catch (IOException e) {
            logger.error("Failed to obtain impersonated credentials for: " + TARGET_SERVICE_ACCOUNT, e);
            throw new RuntimeException(e);
        }
    }

    private static List<List<String>> transform(List<List<Object>> values) {
        return values.stream()
                .map(GoogleSheetApi2::stringFromObject)
                .toList();
    }

    private static List<String> stringFromObject(List<Object> objects) {
        return objects.stream().map(String::valueOf).toList();
    }
}