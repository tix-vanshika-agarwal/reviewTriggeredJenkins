package com.tiket.api.flight.dashboard;

import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import static com.tiket.core.CoreFunctions.log;

public record FlightEticketIssueApi(Inputs inputs) implements BaseApi {
    public record Inputs(
            String orderId,
            String bookingCode, 
            String email,
            String[] passengerNames,
            String title,
            int paxNumber
    ) {}

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        Map<String, Object> headers = getStaticMap("src/test/resources/api/flight/FlightEticketIssue_headers.properties");
        headers.put("username",inputs.email);

        JSONObject body = getStaticJson("src/test/resources/api/flight/FlightEticketIssue_body.json");
        body.put("orderId",inputs.orderId);
        JSONObject paxTemplate = body.getJSONArray("paxRequest").getJSONObject(0);
        JSONArray paxes = new JSONArray();
        for(int i=0; i<inputs.passengerNames.length; i++){
            JSONObject pax = new JSONObject(paxTemplate.toString()); // Copy the template
            pax.put("name", inputs.passengerNames[i]);
            pax.put("title", inputs.title);
            pax.put("paxNumber", inputs.paxNumber);
            pax.put("fullNameWithTitle", inputs.title + " " + inputs.passengerNames[i]);
            pax.put("ticketNumber",CoreFunctions.randomInt(3)+"-2123147319");
            paxes.put(pax);
        }
        body.put("paxRequest", paxes);

        log("Request body: " + body.toString());

        String urlGK = "https://flight-issuance-be-svc.test-flight-cluster.tiket.com/tix-flight-issuance/ticket-number/";
        String urlPreprod = "https://flight-issuance-be-svc.preprod-flight-cluster.tiket.com/tix-flight-issuance/ticket-number/";
        String url = (Configs.ENVIRONMENT == Environment.GK) ? urlGK : urlPreprod;

        ApiRequest request = new ApiRequest.Builder()
                .setUri((url+inputs.bookingCode))
                .setHeaderParams(headers)
                .setBody(body.toString())
                .build();

        return apiTools.put(request);
    }
}
