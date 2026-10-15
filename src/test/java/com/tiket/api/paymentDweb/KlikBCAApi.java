package com.tiket.api.paymentDweb;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;

import java.util.Map;

public record KlikBCAApi(Input inputs) implements BaseApi{
    public record Input(
        String referenceId,
        String amount
    ) {}
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/paymentDweb/KlikBCAApiHeaders.properties");

        String xmlPayload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
            + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:www.tiket.com\">"
            + "<soapenv:Header/>"
            + "<soapenv:Body>"
            + "<urn:PaymentB2C>"
            + "<InputPaymentB2C>"
            + "<merchantCode>5214</merchantCode>"
            + "<userId>rizkyhet3006</userId>"
            + "<transactionNo>"+inputs.referenceId+"</transactionNo>"
            + "<transactionDate>22/02/2023 18:29:52</transactionDate>"
            + "<amount>IDR"+inputs.amount+".00</amount>"
            + "<type>N</type>"
            + "<additionalData/>"
            + "</InputPaymentB2C>"
            + "</urn:PaymentB2C>"
            + "</soapenv:Body>"
            + "</soapenv:Envelope>";

        UriHelper helper = new UriHelper("https://lb1-testing.tiket.com","https://lb1-ms.preprod.tiket.com");
        
        ApiRequest request = new ApiRequest.Builder()
                .setUri(helper.withEndpoint("/tix-payment-callback/bca-ws"))
                .setBody(xmlPayload)
                .setHeaderParams(headers)
                .build();
        return apiTools.post(request);
    }
}