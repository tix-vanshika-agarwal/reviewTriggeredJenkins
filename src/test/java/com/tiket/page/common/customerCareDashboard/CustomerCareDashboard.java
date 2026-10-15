package com.tiket.page.common.customerCareDashboard;

import com.google.gson.Gson;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.customerCareDashboard.CustomerCareDashboardRequestSpecialRefundApi;
import com.tiket.api.customerCareDashboard.CustomerCareDashboardSearchOrderByOrderIDApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.auth.CustomerCareDashboardAuthResponse;
import com.tiket.pojo.customerCareDashboard.OrderDetailResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class CustomerCareDashboard<T extends WebDriver> extends BasePage<T> {

    public CustomerCareDashboard(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CustomerCareDashboard<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new CustomerCareDashboardAndroid<>(session);
            }
            case IOS -> {
                return new CustomerCareDashboardIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    @SneakyThrows
    public OrderDetailResponse searchOrderByOrderID(String orderId) {
        try {
            String dashboardToken = getTokenCustomerCareDashboard();
            logger.log("dashboardToken-> " + dashboardToken);

            CustomerCareDashboardSearchOrderByOrderIDApi.Inputs inputs = new CustomerCareDashboardSearchOrderByOrderIDApi.Inputs(dashboardToken, orderId);
            CustomerCareDashboardSearchOrderByOrderIDApi customerCareDashboardSearchOrderByOrderIDApi = new CustomerCareDashboardSearchOrderByOrderIDApi(inputs);

            ApiResponse orderDetailResponse = customerCareDashboardSearchOrderByOrderIDApi.hitApi(apiTools);
            logger.log("response-> " + orderDetailResponse.getBody());
            String dataResponse = new JSONArray(orderDetailResponse.getBody()).getJSONObject(0).toString();

            Gson gson = new Gson();

            return gson.fromJson(dataResponse, OrderDetailResponse.class);
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Searching order by order ID using API is failed");
        }
        return null;
    }

    @SneakyThrows
    public boolean requestSpecialRefund(String orderId, String orderDetailId, int amountFromVendor) {
        try {
            String dashboardToken = getTokenCustomerCareDashboard();
            logger.log("dashboardToken-> " + dashboardToken);

            CustomerCareDashboardRequestSpecialRefundApi.Inputs inputs = new CustomerCareDashboardRequestSpecialRefundApi.Inputs(dashboardToken, orderId, orderDetailId, amountFromVendor);
            CustomerCareDashboardRequestSpecialRefundApi customerCareDashboardSearchOrderByOrderIDApi = new CustomerCareDashboardRequestSpecialRefundApi(inputs);

            ApiResponse response = customerCareDashboardSearchOrderByOrderIDApi.hitApi(apiTools);
            logger.log("response-> " + response.getBody());

            if(response.getStatusCode() == 200) {
                return true;
            }
        } catch (Exception e) {
            logger.log("Error occured: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Searching order by order ID using API is failed");
        }
        return false;
    }

    @SneakyThrows
    private String getTokenCustomerCareDashboard() throws ExecutionException, InterruptedException{
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse apiRresponse = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        String dataString = new JSONArray(apiRresponse.getBody()).getJSONObject(0).toString();

        Gson gson = new Gson();
        CustomerCareDashboardAuthResponse customerCareDashboardAuthResponse = gson.fromJson(dataString, CustomerCareDashboardAuthResponse.class);

        String isSuccessAuth = customerCareDashboardAuthResponse.data.loginUser.message;
        if("SUCCESS".equals(isSuccessAuth)){
            Map<String, ArrayList<Object>> header = apiRresponse.getHeaderParams();
            ArrayList<Object>  listSetCookies = header.get("set-cookie");
            String accessToken = listSetCookies.get(0).toString().contains("session_access_token")?listSetCookies.get(0).toString():listSetCookies.get(1).toString();
            logger.log("token : " + accessToken);
            int start = accessToken.indexOf('=') + 1;
            int end= accessToken.indexOf(';');
            accessToken = accessToken.substring(start,end);
            logger.log("session_access_token= " + accessToken);
            return accessToken;
        }
        return null;
    }
}
