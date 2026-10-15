package com.tiket.apiPage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.ttd.TTDSupplierSyncStatusApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class TTDSupplierSyncApi<T extends WebDriver> extends BasePage<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final ConcurrentHashMap<String, CachedToken> tokenCache = new ConcurrentHashMap<>();

    public TTDSupplierSyncApi(Session<T> session) {
        super(session);
    }

    public boolean verifySupplierSyncApiStatus(String cmsUsername, String cmsPassword, String supplierProduct) {
        try {
            String accessToken = getAdminAccessToken(cmsUsername, cmsPassword);
            TTDSupplierSyncStatusApi.Inputs inputs = new TTDSupplierSyncStatusApi.Inputs(accessToken, supplierProduct);
            TTDSupplierSyncStatusApi api = new TTDSupplierSyncStatusApi(inputs);
            ApiResponse response = api.hitApi(apiTools);
            return validateResponse(response);
        } catch (Exception e) {
            logger.log("ERROR in verifySupplierSyncApiStatus: " + e.getMessage());
            return false;
        }
    }

    private record CachedToken(String token) {}

    @SneakyThrows
    private String getAdminAccessToken(String username, String password) {
        CachedToken cached = tokenCache.get(username);
        if (cached != null) return "Bearer " + cached.token();

        POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs = new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(username, password);
        POSTGetAdminAccessTokenDashboardDynamicApi api = new POSTGetAdminAccessTokenDashboardDynamicApi(inputs);
        ApiResponse response = api.hitApi(apiTools);

        String tokenValue = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(response);
        if (tokenValue != null) {
            tokenCache.put(username, new CachedToken(tokenValue));
            return "Bearer " + tokenValue;
        }

        throw new RuntimeException("Failed to get admin access token for user: " + username);
    }

    @SneakyThrows
    private boolean validateResponse(ApiResponse response) {
        logger.log("API Status Code: " + response.getStatusCode());
        
        if (response.getStatusCode() != 200) {
            logger.log("API failed with status code: " + response.getStatusCode());
            return false;
        }

        JsonNode dataNode = OBJECT_MAPPER.readTree(response.getBody()).path("data");
        String syncDateText = dataNode.path("lastSupplierSyncDate").asText("");
        
        // Validate date format before splitting to avoid ArrayIndexOutOfBoundsException
        if (syncDateText.isEmpty()) {
            logger.log("No sync date found in response");
            return false;
        }
        
        String lastSyncDate = syncDateText.contains(" ") ? syncDateText.split(" ")[0] : syncDateText;
        String today = LocalDate.now().format(DATE_FORMAT);
        
        logger.log("Last sync date: " + lastSyncDate + ", Today: " + today + ", Match: " + today.equals(lastSyncDate));
        
        return today.equals(lastSyncDate);
    }
}