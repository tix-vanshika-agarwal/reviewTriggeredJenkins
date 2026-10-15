package com.tiket.apiPage;

import com.google.gson.Gson;
import com.tiket.api.auth.POSTGetAdminAccessTokenDashboardApi;
import com.tiket.api.refund.GetRefundStatusApi;
import com.tiket.api.refund.PostRefundForceSubmitApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.refund.RefundStatusApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.tiket.core.CoreFunctions.log;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class RefundApi<T extends WebDriver> extends BasePage<T> {

    public RefundApi(Session<T> session) {
        super(session);
    }
    @SneakyThrows
    public void approveEndToEndRefundFromAdminCareDashboard(String refundRequestId, String orderId){
        String accessToken = "Bearer "+getAdminAccessTokenDashboardAuth();
        log(accessToken);

        GetRefundStatusApi.Inputs inputs = new GetRefundStatusApi.Inputs(accessToken, refundRequestId);
        GetRefundStatusApi getRefundStatusApi = new GetRefundStatusApi(inputs);
        RefundStatusApiResponse refundStatusApiResponse = null;
        String lastStatus = null;
        for(int tries=0; tries<3; tries++){
            ApiResponse response = getRefundStatusApi.hitApi(apiTools);
            log(response.getBody());

            Gson gson = new Gson();
            refundStatusApiResponse = gson.fromJson(response.getBody(), RefundStatusApiResponse.class);
            lastStatus = refundStatusApiResponse.data.refunds.get(0).lastStatus;
            if(lastStatus.contains("on_process")){
                break;
            }
            Thread.sleep(3000);
        }

        int paymentMethodId = refundStatusApiResponse.data.refunds.get(0).groupedPaymentMethods.get(0).id;

        log("Payment id" + paymentMethodId);

        PostRefundForceSubmitApi.Inputs inputs1 = new PostRefundForceSubmitApi.Inputs(accessToken,lastStatus, orderId, refundRequestId, paymentMethodId);
        PostRefundForceSubmitApi postRefundForceSubmitApi = new PostRefundForceSubmitApi(inputs1);
        ApiResponse response1 = postRefundForceSubmitApi.hitApi(apiTools);
        log(response1.getBody());

        Assertion.assertThat("Refund status is correct", response1.getBody(), is(notNullValue()));
    }

    private String getAdminAccessTokenDashboardAuth() throws ExecutionException, InterruptedException{
        POSTGetAdminAccessTokenDashboardApi postGetAdminAccessTokenDashboardApi = new POSTGetAdminAccessTokenDashboardApi();
        ApiResponse response = postGetAdminAccessTokenDashboardApi.hitApi(apiTools);
        try{
            String isSuccessAuth = new JSONArray(response.getBody()).getJSONObject(0).getJSONObject("data").getJSONObject("loginUser").get("message").toString();
            logger.log("res : " + isSuccessAuth);
            if("SUCCESS".equals(isSuccessAuth)){
                Map<String,ArrayList<Object>> header = response.getHeaderParams();
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
        }catch(Exception e){
            logger.warn("failed getting admin access token, when trying to delete account");
            return null;
        }
    }
}