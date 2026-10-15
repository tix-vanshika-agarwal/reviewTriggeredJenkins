package com.tiket.api.ttd;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.tiket.core.CoreFunctions;
import com.tiket.io.api.ApiTools;
import com.tiket.model.Environment;
import com.tiket.model.api.ApiRequest;
import com.tiket.model.api.ApiResponse;
import com.tiket.model.api.UriHelper;
import com.tiket.pojo.ttd.CreateOrderBody;
import com.tiket.testbase.BaseApi;
import com.tiket.testbase.Configs;

import lombok.SneakyThrows;

/**
 * @param useCookieAuth when {@code true}, the access token is sent as the {@code session_access_token} cookie
 *                      instead of the {@code Authorization} header (e.g. for B2B corporate accounts)
 */
public record TTDCreateOrderApi(Inputs inputs, boolean hitGateway, boolean useCookieAuth) implements BaseApi {

    /** Endpoint path shared by both variants; the gateway variant prefixes it with {@code ms-gateway/}. */
    private static final String CREATE_ORDER_PATH = "tix-events-v2-order/v3/orders";

    /**
     * Direct (cluster-internal) base URLs for the events-v2-order service, used when {@code hitGateway} is
     * {@code false} to bypass the public API gateway (e.g. when Cloudflare rate-limits it): GK hits the test
     * cluster, PREPROD the preprod cluster. There is no PROD direct endpoint, and the 2-arg {@link UriHelper}
     * would otherwise resolve PROD to the preprod cluster, so the bypass is refused on PROD (see
     * {@link #ensureDirectServiceSupportedForEnvironment()}) rather than silently creating orders in the wrong
     * environment.
     */
    private static final UriHelper DIRECT_SERVICE_URI = new UriHelper(
            "https://events-v2-order-svc.test-to-do-cluster.tiket.com",
            "https://events-v2-order-svc.preprod-to-do-cluster.tiket.com");

    /** Web origin sent with the B2B cookie-auth request (GK / PREPROD), same as the B2B interim APIs. */
    private static final UriHelper B2B_ORIGIN_URI = new UriHelper(
            "https://m.gatotkaca.tiket.com",
            "https://m.preprod.tiket.com");

    /** Defaults to routing through the public API gateway (the historic behaviour). */
    public TTDCreateOrderApi(Inputs inputs) {
        this(inputs, true);
    }

    /** Sends the access token in the {@code Authorization} header (the historic behaviour). */
    public TTDCreateOrderApi(Inputs inputs, boolean hitGateway) {
        this(inputs, hitGateway, false);
    }

    public record Inputs(
            String currency,
            String accessToken,
            String selectedPackageScheduleID,
            String contactFullName,
            String contactEmailAddress,
            String contactSalutation,
            String guestFullName,
            String guestEmailAddress,
            String guestSalutation,
            String selectedPriceTiers,
            int insuranceCode,
            int guestCount,
            /** Optional; omit from payload when {@code null}. Events create-order expects the member traveller profile ID. */
            Integer collectorProfileId,
            /** When non-null/non-blank, applied to collector, collectors[], and contact. */
            String phoneNumber,
            String countryCode,
            String nationality
    ) {

        /** Same fields as historic constructor; fills optional Events v2 payload fields via defaults / template. */
        public static Inputs legacy(
                String currency,
                String accessToken,
                String selectedPackageScheduleID,
                String contactFullName,
                String contactEmailAddress,
                String contactSalutation,
                String guestFullName,
                String guestEmailAddress,
                String guestSalutation,
                String selectedPriceTiers,
                int insuranceCode) {
            return legacy(currency, accessToken, selectedPackageScheduleID,
                    contactFullName, contactEmailAddress, contactSalutation,
                    guestFullName, guestEmailAddress, guestSalutation,
                    selectedPriceTiers, insuranceCode, 1);
        }

        public static Inputs legacy(
                String currency,
                String accessToken,
                String selectedPackageScheduleID,
                String contactFullName,
                String contactEmailAddress,
                String contactSalutation,
                String guestFullName,
                String guestEmailAddress,
                String guestSalutation,
                String selectedPriceTiers,
                int insuranceCode,
                int guestCount) {
            return new Inputs(currency, accessToken, selectedPackageScheduleID,
                    contactFullName, contactEmailAddress, contactSalutation,
                    guestFullName, guestEmailAddress, guestSalutation,
                    selectedPriceTiers, insuranceCode, Math.max(1, guestCount),
                    null, null, "+62", "Indonesia");
        }
    }

    @SneakyThrows
    @Override
    public ApiResponse hitApi(ApiTools apiTools) {
        Map<String, Object> headers;
        if (useCookieAuth) {
            // B2B (corporate) web headers, same set as SubmitBookingApiV2B2B, with the token carried in the session cookie.
            headers = getStaticMap("src/test/resources/api/ttd/TTDOrder/TTDCreateOrderB2B_headers.properties");
            String deviceId = CoreFunctions.generateDeviceId();
            String correlationId = CoreFunctions.generateRandomString(6);
            String requestId = UUID.randomUUID().toString();
            headers.put("deviceid", deviceId);
            headers.put("x-correlation-id", correlationId);
            headers.put("x-request-id", requestId);
            
            headers.put("Cookie", "device_id=" + deviceId + "; session_access_token="
                    + CoreFunctions.stripBearerPrefix(inputs.accessToken()) + ";");
            headers.put("origin", B2B_ORIGIN_URI.getUri());
        } else {
            headers = getStaticMap("src/test/resources/api/ttd/TTDOrder/TTDCreateOrder_headers.properties");
            headers.put("Authorization", inputs.accessToken());
        }

        headers.put("currency", inputs.currency);
        if (!hitGateway) {
            ensureDirectServiceSupportedForEnvironment();
            applyDirectServiceHeaders(headers);
        }
        Gson gson = new Gson();

        String countryCode = defaultIfBlank(inputs.countryCode(), "+62");
        String nationality = defaultIfBlank(inputs.nationality(), "Indonesia");

        JSONObject body = getStaticJson("src/test/resources/api/ttd/TTDOrder/TTDCreateOrder_body.json");
        CreateOrderBody createOrderBody = gson.fromJson(body.toString(), CreateOrderBody.class);
        CreateOrderBody.Collector c = createOrderBody.collector;
        c.profileId = inputs.collectorProfileId();
        if (c.collectorCode == null) {
            c.collectorCode = 1;
        }
        c.emailAddress = inputs.guestEmailAddress();
        splitNameOnto(inputs.guestFullName(), c);
        c.priceTierCode = inputs.selectedPriceTiers();
        c.salutation = inputs.guestSalutation();
        c.countryCode = countryCode;

        CreateOrderBody.Contact contact = createOrderBody.contact;
        contact.countryCode = countryCode;
        contact.emailAddress = inputs.contactEmailAddress();
        splitNameOnto(inputs.contactFullName(), contact);
        contact.salutation = inputs.contactSalutation();
        contact.nationality = nationality;

        applyPhone(inputs.phoneNumber(), c);
        applyPhone(inputs.phoneNumber(), contact);

        int guestCount = Math.max(1, inputs.guestCount());
        createOrderBody.collectors.clear();
        for (int i = 1; i <= guestCount; i++) {
            CreateOrderBody.Collector collectorClone = gson.fromJson(gson.toJson(c), CreateOrderBody.Collector.class);
            collectorClone.collectorCode = i;
            createOrderBody.collectors.add(collectorClone);
        }

        CreateOrderBody.PriceTierQuantities quantities = new CreateOrderBody.PriceTierQuantities();
        quantities.code = inputs.selectedPriceTiers();
        quantities.quantity = guestCount;
        createOrderBody.priceTierQuantities.clear();
        createOrderBody.priceTierQuantities.add(quantities);

        CreateOrderBody.Insurances insurances = new CreateOrderBody.Insurances();

        if (inputs.insuranceCode() != 0) {
            CreateOrderBody.AddOn addOn = new CreateOrderBody.AddOn();
            insurances.credentialId = inputs.insuranceCode();
            addOn.insurances = new ArrayList<>();
            addOn.insurances.add(insurances);
            createOrderBody.addon = addOn;
        }

        createOrderBody.productScheduleId = inputs.selectedPackageScheduleID();

        String uri = hitGateway
                ? uriHelper.withEndpoint("ms-gateway/" + CREATE_ORDER_PATH)
                : DIRECT_SERVICE_URI.withEndpoint(CREATE_ORDER_PATH);
        ApiRequest request = new ApiRequest.Builder()
                .setUri(uri)
                .setHeaderParams(headers)
                .setBody(gson.toJson(createOrderBody, CreateOrderBody.class))
                .build();

        return apiTools.post(request);
    }

    /**
     * Fail fast rather than silently create the order in the wrong environment. {@link #DIRECT_SERVICE_URI} is built
     * with the 2-arg {@link UriHelper}, which has no PROD base URL (there is no cluster-internal events-v2-order
     * endpoint exposed for PROD) and therefore resolves PROD to the <em>preprod</em> cluster. Bypassing the gateway
     * on a PROD run would thus create the order in PREPROD while the rest of the run targets PROD — an environment
     * mismatch that is hard to spot in the logs. The gateway bypass only exists to dodge Cloudflare rate-limiting on
     * non-prod runs, so it is refused on PROD; use the gateway ({@code hitGateway=true}) there instead.
     */
    private void ensureDirectServiceSupportedForEnvironment() {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            throw new IllegalStateException("TTDCreateOrderApi: the direct-service create-order path "
                    + "(hitGateway=false) has no PROD endpoint and would fall back to the PREPROD cluster, creating "
                    + "the order in the wrong environment. Use the API gateway on PROD (hitGateway=true).");
        }
    }

    /**
     * Headers the public API gateway normally injects from the session/token. When {@code hitGateway} is false the
     * request goes straight to the events-v2-order service, which rejects it (HTTP 400) without this identity/context.
     */
    private void applyDirectServiceHeaders(Map<String, Object> headers) {
        headers.putIfAbsent("accept", "application/json");
        headers.put("storeId", "TIKETCOM");
        headers.put("serviceId", "GATEWAY");
        headers.put("channelId", "WEB");
        headers.put("requestId", String.valueOf(System.currentTimeMillis()));
        headers.put("username", inputs.contactEmailAddress());
        headers.put("true-client-ip", "139.255.110.226");
        headers.put("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 "
                + "(KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        headers.put("language", "ID");
        headers.put("x-loyalty-level", "LV4");
        headers.put("isVerifiedPhoneNumber", "true");
    }

    private static String defaultIfBlank(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value;
    }

    private static void applyPhone(String phone, CreateOrderBody.Collector collector) {
        if (phone == null || phone.isBlank()) {
            return;
        }
        collector.phoneNumber = phone;
    }

    private static void applyPhone(String phone, CreateOrderBody.Contact contact) {
        if (phone == null || phone.isBlank()) {
            return;
        }
        contact.phoneNumber = phone;
    }

    /** Splits {@code fullName} on the first whitespace into firstName / lastName (legacy behaviour). */
    private static void splitNameOnto(String fullName, CreateOrderBody.Collector target) {
        String[] parts = fullName.trim().split("\\s+", 2);
        target.firstName = parts[0];
        target.lastName = parts.length > 1 ? parts[1] : "";
    }

    private static void splitNameOnto(String fullName, CreateOrderBody.Contact target) {
        String[] parts = fullName.trim().split("\\s+", 2);
        target.firstName = parts[0];
        target.lastName = parts.length > 1 ? parts[1] : "";
    }
}
