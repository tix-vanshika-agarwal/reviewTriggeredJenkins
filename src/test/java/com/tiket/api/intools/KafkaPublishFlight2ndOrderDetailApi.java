package com.tiket.api.intools;

import com.tiket.io.api.ApiTools;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.BaseApi;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public record KafkaPublishFlight2ndOrderDetailApi(Inputs inputs) implements BaseApi {

    public record Inputs(String kafkaBrokers, String kafkaTopic, String kafkaPort) {}

    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers = getStaticMap("src/test/resources/api/intools/KafkaPublish.properties");

        UriHelper uriHelper = new UriHelper(
                "https://deposit-be-http-svc.test-platform-cluster.tiket.com",
                "https://deposit-be-http-svc.preprod-platform-cluster.tiket.com"
        );

        // Validate inputs
        if (inputs.kafkaBrokers() == null || inputs.kafkaTopic() == null) {
            throw new IllegalArgumentException("Kafka brokers, topic, or message cannot be null");
        }

        JSONObject body = getStaticJson("src/test/resources/api/intools/FlightKafkaMessage2.json");

        ApiRequest request = new ApiRequest.Builder()
                .setUri(uriHelper.withEndpoint("/tix-deposit/kafka/publish?brokers=" +
                        URLEncoder.encode(inputs.kafkaBrokers(), StandardCharsets.UTF_8) + ":" +
                        URLEncoder.encode(inputs.kafkaPort(), StandardCharsets.UTF_8) + "&topic=" +
                        URLEncoder.encode(inputs.kafkaTopic(), StandardCharsets.UTF_8)))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.post(request);
    }

}