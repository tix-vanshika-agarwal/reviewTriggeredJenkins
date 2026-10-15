package com.tiket.apiPage;

import com.tiket.api.b2b.affiliate.AffiliateClearLockTransactionGETApi;
import com.tiket.api.b2b.affiliate.DeleteAffiliateOrderApi;
import com.tiket.api.b2b.affiliate.kafka.AffiliateDepositKafkaPublishPOSTApi;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.web.WebPage;
import lombok.SneakyThrows;
import org.openqa.selenium.WebDriver;

public class AffiliateApi<T extends WebDriver> extends WebPage<T> {

    public AffiliateApi(Session<T> session) {
        super(session);
    }

   @SneakyThrows
    public void getDeleteAffiliateOrderResponseCodeWithWarning(String transactionId, String orderID) {
        DeleteAffiliateOrderApi.Inputs deleteAffiliateOrder = new DeleteAffiliateOrderApi.Inputs(transactionId, orderID);
        DeleteAffiliateOrderApi deleteAffiliateOrderApi = new DeleteAffiliateOrderApi(deleteAffiliateOrder);
        ApiResponse response = deleteAffiliateOrderApi.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Response Code: " + statusCode);

        if (statusCode != 200) {
            logger.skip("SKIP WARNING: Unexpected status code: " + statusCode);
        }
    }

    @SneakyThrows
    public void getAffiliateClearLockTransactionResponseCode() {
        AffiliateClearLockTransactionGETApi clearLockTransactionApi = new AffiliateClearLockTransactionGETApi();
        ApiResponse response = clearLockTransactionApi.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Response Code: " + statusCode);

        if (statusCode != 200) {
            logger.skip("SKIP WARNING: Unexpected status code: " + statusCode);
        }
    }

    @SneakyThrows
    public void getAffiliateDepositKafkaPublishResponseCode(AffiliateDepositKafkaPublishPOSTApi.Inputs inputs) {
        AffiliateDepositKafkaPublishPOSTApi kafkaPublishApi = new AffiliateDepositKafkaPublishPOSTApi(inputs);
        ApiResponse response = kafkaPublishApi.hitApi(apiTools);
        int statusCode = response.getStatusCode();
        logger.log("Response Code: " + statusCode);

        if (statusCode != 200) {
            logger.skip("SKIP WARNING: Unexpected status code: " + statusCode);
        }
    }
}
