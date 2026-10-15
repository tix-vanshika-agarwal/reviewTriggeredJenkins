package com.tiket.page.loyalty.ulp_tiket;

import com.google.gson.Gson;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.auth.AdminDashboardLoginResponseParser;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardDynamicApi;
import com.tiket.api.customerCareDashboard.CustomerCareLoyaltyPointBliBliTiketApi;
import com.tiket.api.loyalty.AddActivePointsApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public abstract class UlpPage<T extends WebDriver> extends BasePage<T> {
    public UlpPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> UlpPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new UlpPageAndroid<>(session);
            }
            case IOS -> {
                return new UlpPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public void injectTiketPoint(int pdAccountId, int pdPoint, String pdProcessType, int pdRatio, String username)
            throws InterruptedException, ExecutionException {
        AddActivePointsApi.Inputs inputs = new AddActivePointsApi.Inputs(pdAccountId, pdPoint, pdProcessType, pdRatio,
                username);
        AddActivePointsApi api = new AddActivePointsApi(inputs);
        ApiResponse apiResponse = api.hitApi(apiTools);
        logger.log("response : " + apiResponse);
        Assertion.assertThat("Inject api success", apiResponse.getStatusCode(), is(200));
    }

    public String injectTiketPointsBasedOnCondition(int pdAccountId, int pdPoint, String pdProcessType, int pdRatio,
            String username, String totalPoint) throws InterruptedException, ExecutionException {
        totalPoint = totalPoint.replaceAll("[^0-9]", "");
        int totalPointInt = Integer.parseInt(totalPoint);
        if (totalPointInt < pdPoint) {
            logger.log("total tiket points available : " + totalPoint);
            logger.log("total tiket point required : " + pdPoint);

            int newPdPoint = pdPoint - totalPointInt;
            injectTiketPoint(pdAccountId, newPdPoint, pdProcessType, pdRatio, username);
            return String.valueOf(pdPoint);
        }
        return totalPoint;
    }

    public abstract boolean isOnEliteRewardPage();

    public abstract void verifyTixPointHistory(); // FIXME: Implementation pending

    public abstract void clickEntryPointParityBanner(); // FIXME: Implementation pending

    public abstract void clickMatchNowFromBlibli(); // FIXME: Implementation pending

    public abstract void verifyUnregisteredBlibliAccountNotif(); // FIXME: Implementation pending

    public abstract void openInfoPageEliteRewards(); // FIXME: Implementation pending

    public abstract void verifyRedirectInfoPage(); // FIXME: Implementation pending

    public abstract void clickCrossSellAT(); // FIXME: Implementation pending

    public abstract void verifyCrossSellRedirectAT(); // FIXME: Implementation pending

    public abstract void verifyMemberBenefit(); // FIXME: Implementation pending

    public abstract void verifyBottomSheet(); // FIXME: Implementation pending

    public abstract void verifyWrongOTP(); // FIXME: Implementation pending

    /**
        * Retrieves the admin access token for the dashboard authentication.
        *
        * @return The admin access token as a String.
        * @throws Exception If failed to get the admin access token.
        */
    @SneakyThrows
    private String getAdminAccessTokenDashboardAuth() {
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data")
                .getJSONObject("loginUser").get("message").toString();
        if ("SUCCESS".equals(isSuccessAuth)) {
            Map<String, ArrayList<Object>> header = response.getHeaderParams();
            ArrayList<Object> listSetCookies = header.get("set-cookie");
            String accessToken = listSetCookies.get(0).toString().contains("session_access_token")
                    ? listSetCookies.get(0).toString()
                    : listSetCookies.get(1).toString();
            logger.log("token : " + accessToken);
            int start = accessToken.indexOf('=') + 1;
            int end = accessToken.indexOf(';');
            accessToken = accessToken.substring(start, end);
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        throw new Exception("Failed to get admin access token");
    }

    private String getAdminAccessTokenDashboardAuth(String usernameAdmin, String passwordAdmin) {
        POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs = new POSTGetAdminAccessTokenDashboardDynamicApi.Inputs(
                usernameAdmin, passwordAdmin);
        return getAdminAccessTokenDashboardAuth(inputs);
    }

    @SneakyThrows
    private String getAdminAccessTokenDashboardAuth(POSTGetAdminAccessTokenDashboardDynamicApi.Inputs inputs) {
        POSTGetAdminAccessTokenDashboardDynamicApi postGetAdminAccessTokenDashboardDynamicApi = new POSTGetAdminAccessTokenDashboardDynamicApi(inputs);
        ApiResponse response = postGetAdminAccessTokenDashboardDynamicApi.hitApi(apiTools);
        String accessToken = AdminDashboardLoginResponseParser.resolveSessionTokenOrNull(response);
        if (accessToken != null) {
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        throw new Exception("Failed to get admin access token");
    }

    /**
     * Retrieves loyalty data for a given account ID.
     *
     * @param accountId        The ID of the account for which to retrieve loyalty data.
     * @param usernameAdmin
     * @param passwordAdmin
     * @return A JSONArray containing the loyalty data.
     */
    @SneakyThrows
    public JSONArray getLoyaltyData(String accountId, String usernameAdmin, String passwordAdmin) {
        String dashboardToken = getAdminAccessTokenDashboardAuth(usernameAdmin, passwordAdmin);
        CustomerCareLoyaltyPointBliBliTiketApi.Inputs input = new CustomerCareLoyaltyPointBliBliTiketApi.Inputs(
                dashboardToken, accountId);
        CustomerCareLoyaltyPointBliBliTiketApi customerCareLoyaltyPointBliBliTiketApi = new CustomerCareLoyaltyPointBliBliTiketApi(
                input);
        ApiResponse apiResponse = customerCareLoyaltyPointBliBliTiketApi.hitApi(apiTools);
        Gson gson = new Gson();
        logger.log("response : " + apiResponse.getBody());
        JSONArray jsonArr = new JSONArray(apiResponse.getBody());
        return jsonArr;
    }

    /**
     * Checks if the order ID's point earning exists in the dashboard.
     *
     * @param ulpJsonArray   The JSON array containing the ULP data.
     * @param orderId        The order ID to check.
     * @param expectedPoint  The expected point value.
     * @return               True if the point earning exists and matches the expected point value, false otherwise.
     * @throws Throwable    If an error occurs while checking the point earning.
     */
    @SneakyThrows
    public boolean checkIfOrderIdPointEarningExistInDashboard(JSONArray ulpJsonArray, String orderId,
            Integer expectedPoint) {
        // Check in pointPendingULPLoyalty
        JSONArray pendingPoints = ((JSONObject) (ulpJsonArray.get(0))).getJSONObject("data")
                .getJSONObject("pointPendingULPLoyalty")
                .getJSONObject("data").getJSONArray("content");
        JSONArray historyPoints = ((JSONObject) ulpJsonArray.get(1)).getJSONObject("data")
                .getJSONObject("pointHistoryULPLoyalty")
                .getJSONObject("data").getJSONArray("content");

        Integer pointValue = findPointsByOrderId(pendingPoints, orderId);
        logger.log("Expected point : " + expectedPoint);
        if (pointValue != null) {
            logger.log("Points for orderId " + orderId + " in pointPendingULPLoyalty: " + pointValue);
            Assertion.assertThat("Verify point is the same", pointValue, equalTo(expectedPoint));
            return true;
        } else {
            // Check in pointHistoryULPLoyalty
            pointValue = findPointsByOrderId(historyPoints, orderId);
            if (pointValue != null) {
                logger.log("Points for orderId " + orderId + " in pointHistoryULPLoyalty: " + pointValue);
                Assertion.assertThat("Verify point is the same", pointValue, equalTo(expectedPoint));
                return expectedPoint == pointValue;
            } else {
                logger.log("OrderId " + orderId
                        + " not found in either pointPendingULPLoyalty or pointHistoryULPLoyalty.");
            }
        }
        return false;
    }

    /**
     * Finds the points associated with a specific order ID in a JSON array.
     *
     * @param jsonArray The JSON array to search in.
     * @param orderId   The order ID to find points for.
     * @return The points associated with the given order ID, or null if not found.
     */
    private static Integer findPointsByOrderId(JSONArray jsonArray, String orderId) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            if (obj.getString("orderId").equals(orderId)) {
                return obj.getInt("point");
            }
        }
        return null;
    }

}