package com.tiket.web.dashboard.page.refund;

import com.tiket.api.refund.FormMiddlewareApi;
import com.tiket.api.refund.PUTAutomationProcess;
import com.tiket.api.refund.POSTAutoRefundApi;
import com.tiket.api.refund.GETSvcRefundUserApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RefundCreation<T extends WebDriver> extends BasePage<T> {
    public RefundCreation(Session<T> session) { super(session); }

    String requestId;

    private String FormMiddlewareApi(FormMiddlewareApi.Inputs inputs) throws InterruptedException, ExecutionException {
        FormMiddlewareApi formMiddlewareApi = new FormMiddlewareApi(inputs);
        ApiResponse apiResponse = formMiddlewareApi.hitApi(apiTools);
        String requestId = new JSONObject(apiResponse.getBody()).getJSONObject("data").get("id").toString();
        return requestId;
    }

    public String createFormMiddlewareApi(
        String amountFromVendor,
        boolean isForceToBankTransfer,
        List<String> items,
        long orderDetailId,
        long orderId,
        String reasonId,
        String bankAccountName,
        String bankAccountNumber,
        String bankCodeTiketIdentifier,
        String remark,
        String usernameAdmin
    ) {
        try {
            BigDecimal amountFromVendorBd = new BigDecimal(amountFromVendor);
            FormMiddlewareApi.Inputs formMiddlewareApiInputs = new FormMiddlewareApi.Inputs(amountFromVendorBd, isForceToBankTransfer, items, 
                                        orderDetailId, orderId, reasonId, bankAccountName, bankAccountNumber, bankCodeTiketIdentifier, remark, usernameAdmin);
            requestId = FormMiddlewareApi(formMiddlewareApiInputs);
            logger.log("got Refund Request Id -> "+requestId);

            return requestId;
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Submit Form Middleware via API failed. Skipping the subsequent steps.");
        }
        return requestId;
    }

    private String PutAutomationProcess(PUTAutomationProcess.Inputs inputs) throws InterruptedException, ExecutionException {
        PUTAutomationProcess putAutomationProcess = new PUTAutomationProcess(inputs);
        ApiResponse apiResponse = putAutomationProcess.hitApi(apiTools);
        String code = new JSONObject(apiResponse.getBody()).get("code").toString();
        return code;
    }

    public String automationProcess(String automationCommand, long refundRequestId) {
        String code = "";
        try {
            PUTAutomationProcess.Inputs putAutomationProcessInputs = new PUTAutomationProcess.Inputs(automationCommand, refundRequestId);
            code = PutAutomationProcess(putAutomationProcessInputs);
            logger.log("Got response code from Automation Process -> "+code);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
        }
        return code;
    }

    private String createAutoRefundApi(POSTAutoRefundApi.Inputs inputs) throws ExecutionException, InterruptedException {
        POSTAutoRefundApi postAutoRefundApi = new POSTAutoRefundApi(inputs);
        ApiResponse postAutoRefundApiResponse = postAutoRefundApi.hitApi(apiTools);
        String code = new JSONObject(postAutoRefundApiResponse.getBody()).get("code").toString();
        return code;
    }

    public String createAutoRefund(String auth, String orderId, String orderDetailId, String refundReason, String refundSubmissionExpiry) {
        String code = "";
        try {
            POSTAutoRefundApi.Inputs inputs = new POSTAutoRefundApi.Inputs(auth, orderId, orderDetailId, refundReason, refundSubmissionExpiry);
            code = createAutoRefundApi(inputs);
            logger.log("Got response code from Auto Refund API -> " + code);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
            Assertion.skip("Create Auto Refund via API failed. Skipping the subsequent steps.");
        }
        return code;
    }

    private String getRequestHashApi(GETSvcRefundUserApi.Inputs inputs) throws ExecutionException, InterruptedException {
        GETSvcRefundUserApi getRequestHashApi = new GETSvcRefundUserApi(inputs);
        ApiResponse getRequestHashApiResponse = getRequestHashApi.hitApi(apiTools);
        String requestHash = new JSONObject(getRequestHashApiResponse.getBody())
                .getJSONArray("data")
                .getJSONObject(0)
                .get("requestHash")
                .toString();
        return requestHash;
    }

    public String getRequestHash(String auth, String orderId, String accountId) {
        String requestHash = "";
        try {
            GETSvcRefundUserApi.Inputs inputs = new GETSvcRefundUserApi.Inputs(auth, orderId, accountId);
            requestHash = getRequestHashApi(inputs);
            logger.log("Request hash is -> " + requestHash);
        } catch (Exception e) {
            logger.log("Error occurred: " + ExceptionUtils.getStackTrace(e));
        }
        return requestHash;
    }
}
