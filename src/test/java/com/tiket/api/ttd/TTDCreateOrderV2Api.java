package com.tiket.api.ttd;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.ttd.CreateOrderV2Body;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

public record TTDCreateOrderV2Api(Inputs inputs, boolean hitGateway) implements BaseApi {

    /** Defaults to bypassing the gateway, since this endpoint gets Cloudflare-rate-limited under repeated hits. */
    public TTDCreateOrderV2Api(Inputs inputs) {
        this(inputs, false);
    }

    public record Inputs(
            String accessToken,
            /** data[0].id from the TTDGetProductSchedulesApi response (Step 2 of the TTD booking chain). */
            String productScheduleId,
            /** All fields below come from the test data sheet, not hardcoded. */
            String salutation,
            String firstName,
            String lastName,
            String emailAddress,
            String countryCode,
            String phoneNumber,
            String nationality,
            long profileId,
            String priceTierCode,
            int quantity,
            String currency,
            /** priceTiers[].finalPrice for {@code priceTierCode}, from TTDSelectProductApi's packages[]; 0 when unknown. */
            long finalPrice,
            /** Answers for packages with a required questionnaire (e.g. TTDSelectProductApi's packages[].questionnaires); empty when the package has none. */
            List<PerBookingAnswer> perBookingAnswers,
            /** UNM refresh token; sent as the {@code session_refresh_token} cookie alongside the access token. Optional. */
            String refreshToken,
            /** UNM device/unique id; sent as the {@code device_id}/{@code uniqueId} cookies. Optional. */
            String uniqueId
    ) {
        public Inputs(
                String accessToken,
                String productScheduleId,
                String salutation,
                String firstName,
                String lastName,
                String emailAddress,
                String countryCode,
                String phoneNumber,
                String nationality,
                long profileId,
                String priceTierCode,
                int quantity,
                String currency
        ) {
            this(accessToken, productScheduleId, salutation, firstName, lastName, emailAddress, countryCode,
                    phoneNumber, nationality, profileId, priceTierCode, quantity, currency, 0, List.of(), null, null);
        }

        public Inputs(
                String accessToken,
                String productScheduleId,
                String salutation,
                String firstName,
                String lastName,
                String emailAddress,
                String countryCode,
                String phoneNumber,
                String nationality,
                long profileId,
                String priceTierCode,
                int quantity,
                String currency,
                long finalPrice,
                List<PerBookingAnswer> perBookingAnswers
        ) {
            this(accessToken, productScheduleId, salutation, firstName, lastName, emailAddress, countryCode,
                    phoneNumber, nationality, profileId, priceTierCode, quantity, currency, finalPrice, perBookingAnswers, null, null);
        }

        /** One answer to a package's required questionnaire, keyed by {@code questionnaireCode}/{@code inputType} from TTDSelectProductApi. */
        public record PerBookingAnswer(String answer, String questionnaireCode, String questionnaireInputType, boolean isPerPax) {}
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {

        // Endpoint path shared by both variants; the gateway variant prefixes it with "ms-gateway/".
        String createOrderPath = "tix-events-v2-order/v2/orders";

        // Direct (cluster-internal) base URLs, used when hitGateway is false to bypass the public API
        // gateway's Cloudflare edge, which rate-limits (HTTP 429) this endpoint under automated/repeated hits.
        UriHelper helper = new UriHelper(
                "https://events-v2-order-svc.test-to-do-cluster.tiket.com",
                "https://events-v2-order-svc.preprod-to-do-cluster.tiket.com");

        Map<String, Object> headers = getStaticMap("src/test/resources/api/ttd/TTDOrder/TTDCreateOrderV2_headers.properties");
        String deviceId = inputs.uniqueId != null && !inputs.uniqueId.isBlank() ? inputs.uniqueId : UUID.randomUUID().toString();
        headers.put("deviceid", deviceId);
        headers.put("x-correlation-id", CoreFunctions.generateRandomString(6));
        headers.put("x-cookie-session-v2", "true");
        headers.put("Cookie", buildSessionCookieHeader(deviceId));

        CreateOrderV2Body body = new CreateOrderV2Body();

        body.contact = new CreateOrderV2Body.Contact();
        body.contact.salutation = inputs.salutation;
        body.contact.firstName = inputs.firstName;
        body.contact.lastName = inputs.lastName;
        body.contact.emailAddress = inputs.emailAddress;
        body.contact.countryCode = inputs.countryCode;
        body.contact.phoneNumber = inputs.phoneNumber;
        body.contact.nationality = inputs.nationality;

        CreateOrderV2Body.Collector collector = new CreateOrderV2Body.Collector();
        collector.profileId = inputs.profileId;
        collector.collectorCode = 1;
        collector.priceTierCode = inputs.priceTierCode;
        collector.salutation = inputs.salutation;
        collector.firstName = inputs.firstName;
        collector.lastName = inputs.lastName;
        collector.emailAddress = inputs.emailAddress;
        collector.countryCode = inputs.countryCode;
        collector.phoneNumber = inputs.phoneNumber;
        collector.nationality = "ID";
        collector.passportIssuingCountry = "";
        body.collector = collector;
        body.collectors = new ArrayList<>();
        body.collectors.add(collector);

        CreateOrderV2Body.PriceTierQuantity priceTierQuantity = new CreateOrderV2Body.PriceTierQuantity();
        priceTierQuantity.code = inputs.priceTierCode;
        priceTierQuantity.quantity = inputs.quantity;
        priceTierQuantity.freeQuantity = 0;
        priceTierQuantity.lastPrice = new CreateOrderV2Body.TierPrice();
        priceTierQuantity.lastPrice.finalPrice = inputs.finalPrice;
        body.priceTierQuantities = new ArrayList<>();
        body.priceTierQuantities.add(priceTierQuantity);

        body.productScheduleId = inputs.productScheduleId;

        body.perBookingAnswers = new ArrayList<>();
        for (Inputs.PerBookingAnswer answer : inputs.perBookingAnswers) {
            CreateOrderV2Body.PerBookingAnswer perBookingAnswer = new CreateOrderV2Body.PerBookingAnswer();
            perBookingAnswer.answer = answer.answer();
            perBookingAnswer.questionnaire = new CreateOrderV2Body.Questionnaire();
            perBookingAnswer.questionnaire.code = answer.questionnaireCode();
            perBookingAnswer.questionnaire.inputType = answer.questionnaireInputType();
            perBookingAnswer.questionnaire.isRequired = true;
            perBookingAnswer.questionnaire.isPerPax = answer.isPerPax();
            body.perBookingAnswers.add(perBookingAnswer);
        }

        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timestampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String timestamp = timestampFormat.format(new Date());

        body.lastPrice = new CreateOrderV2Body.LastPrice();
        body.lastPrice.scale = 0;
        body.lastPrice.currency = inputs.currency;
        body.lastPrice.timestamp = timestamp;

        if (!hitGateway) {
            ensureDirectServiceSupportedForEnvironment();
            applyDirectServiceHeaders(headers, inputs.emailAddress);
        }

        Gson gson = new Gson();
        String uri = hitGateway
                ? uriHelper.withEndpoint("ms-gateway/" + createOrderPath)
                : helper.withEndpoint(createOrderPath);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uri)
                .setHeaderParams(headers)
                .setBody(gson.toJson(body, CreateOrderV2Body.class))
                .build();

        return apiTools.post(request);
    }

    /**
     * The gateway authenticates this endpoint off the {@code x-cookie-session-v2} cookie session, not a
     * bearer {@code Authorization} header - mirrors the real dweb request captured for this endpoint.
     */
    private String buildSessionCookieHeader(String deviceId) {
        StringBuilder cookie = new StringBuilder();
        appendCookie(cookie, "device_id", deviceId);
        appendCookie(cookie, "userlang", "en");
        appendCookie(cookie, "tiket_currency", inputs.currency == null || inputs.currency.isBlank() ? "IDR" : inputs.currency.toUpperCase());
        appendCookie(cookie, "uniqueId", deviceId);
        // The cookie carries the bare JWT; leaving the "Bearer " prefix on it makes the gateway reject the session with a 401.
        appendCookie(cookie, "session_access_token", stripBearer(inputs.accessToken));
        appendCookie(cookie, "session_refresh_token", stripBearer(inputs.refreshToken));
        return cookie.toString();
    }

    private String stripBearer(String token) {
        return token == null ? null : token.replace("Bearer ", "");
    }

    private void appendCookie(StringBuilder cookie, String name, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (!cookie.isEmpty()) {
            cookie.append("; ");
        }
        cookie.append(name).append("=").append(value);
    }

    /**
     * Fail fast rather than silently create the order in the wrong environment; mirrors
     * {@link com.tiket.api.ttd.TTDCreateOrderApi#ensureDirectServiceSupportedForEnvironment()}.
     */
    private void ensureDirectServiceSupportedForEnvironment() {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            throw new IllegalStateException("TTDCreateOrderV2Api: the direct-service create-order path "
                    + "(hitGateway=false) has no PROD endpoint and would fall back to the PREPROD cluster, creating "
                    + "the order in the wrong environment. Use the API gateway on PROD (hitGateway=true).");
        }
    }

    /**
     * Headers the public API gateway normally injects from the session/token. When {@code hitGateway} is false the
     * request goes straight to the events-v2-order service, which rejects it (HTTP 400) without this identity/context.
     */
    private void applyDirectServiceHeaders(Map<String, Object> headers, String username) {
        headers.putIfAbsent("accept", "application/json");
        headers.put("storeId", "TIKETCOM");
        headers.put("serviceId", "GATEWAY");
        headers.put("channelId", "WEB");
        headers.put("requestId", String.valueOf(System.currentTimeMillis()));
        headers.put("username", username);
        headers.put("true-client-ip", "139.255.110.226");
        headers.put("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        headers.put("language", "ID");
        headers.put("x-loyalty-level", "LV4");
        headers.put("isVerifiedPhoneNumber", "true");
    }
}
