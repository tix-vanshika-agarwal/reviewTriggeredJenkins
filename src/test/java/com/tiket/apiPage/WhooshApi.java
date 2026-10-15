package com.tiket.apiPage;

import com.tiket.api.train.TrainOrderV7Api;
import com.tiket.api.train.WhooshCartApi;
import com.tiket.api.train.WhooshJourneySearchApi;
import com.tiket.api.train.WhooshPassengerApi;
import com.tiket.model.Exception.ApiEnvironmentException;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

public class WhooshApi<T extends WebDriver> extends BasePage<T> {

    /** Token of the login used by the last {@link #createOrderWhoosh}, reusable for paying that order. */
    public String accessToken;

    public WhooshApi(Session<T> session) { super(session); }

    public record Order(String orderId, String orderHash, long price) {}

    /**
     * Books a Whoosh order for adults and infants: search, cart, passengers, checkout. It is a round trip when
     * {@code returnDate} is given, otherwise one way. Dates are yyyy-MM-dd.
     * Returns null when a leg has no journey with enough seats.
     */
    @SneakyThrows
    public Order createOrderWhoosh(String email, String password, String originStation, String destinationStation,
                                   String departureDate, String returnDate, String phone, String[] adultNames, String[] infantNames) {
        accessToken = new Unm<>(session).getAccessToken(email, password, "B2C");
        int adult = adultNames.length;
        boolean roundTrip = returnDate != null && !returnDate.isBlank();

        JSONObject searchResponse = searchJourneysWithRetry(new WhooshJourneySearchApi.Inputs(
                accessToken, originStation, destinationStation, departureDate.replace("-", ""), roundTrip ? returnDate.replace("-", "") : "",
                adult, infantNames.length));
        JSONObject search = searchResponse.optJSONObject("data");
        if (search == null || !search.has("departJourneys") || (roundTrip && !search.has("returnJourneys"))) {
            // e.g. BUSINESS_ERROR / SCHEDULE_NOT_AVAILABLE or TEMPORARY_UNAVAILABLE: no usable journeys for these dates
            logger.log("Whoosh journey search has no journeys -> code " + searchResponse.optString("code")
                    + ", error " + searchResponse.optJSONObject("errorData"));
            return null;
        }
        JSONObject departSegment = firstSegmentWithSeats(search.getJSONObject("departJourneys").getJSONArray("journeys"), adult);
        JSONObject returnSegment = roundTrip ? firstSegmentWithSeats(search.getJSONObject("returnJourneys").getJSONArray("journeys"), adult) : null;
        if (departSegment == null || (roundTrip && returnSegment == null)) return null;
        logger.log("Whoosh journey -> " + departSegment.getString("id") + (roundTrip ? " / " + returnSegment.getString("id") : ""));

        JSONObject cart = new JSONObject(new WhooshCartApi(new WhooshCartApi.Inputs(accessToken, departSegment, returnSegment, adult, infantNames.length))
                .hitApi(apiTools).getBody()).getJSONObject("data");
        String cartId = cart.getString("id");
        String secret = cart.getString("secret");
        logger.log("Whoosh cart ID -> " + cartId);

        new WhooshPassengerApi(new WhooshPassengerApi.Inputs(accessToken, cartId, secret, email, phone, adultNames, infantNames)).hitApi(apiTools);

        JSONObject checkout = new JSONObject(new TrainOrderV7Api(new TrainOrderV7Api.Inputs(accessToken, cartId, secret, "whoosh"))
                .hitApi(apiTools).getBody()).getJSONObject("data");
        JSONObject order = checkout.getJSONObject("order");
        long price = checkout.getJSONObject("price").getLong("newPrice");
        logger.log("Whoosh order -> " + order.getLong("id") + ", price " + price);
        return new Order(String.valueOf(order.getLong("id")), order.getString("hash"), price);
    }

    /** Preprod's search service intermittently answers 5xx / TEMPORARY_UNAVAILABLE, so give it a few tries. */
    @SneakyThrows
    private JSONObject searchJourneysWithRetry(WhooshJourneySearchApi.Inputs inputs) {
        int attempts = 3;
        for (int attempt = 1; ; attempt++) {
            try {
                JSONObject response = new JSONObject(new WhooshJourneySearchApi(inputs).hitApi(apiTools).getBody());
                if (!"TEMPORARY_UNAVAILABLE".equals(response.optString("code")) || attempt == attempts) return response;
                logger.log("Whoosh journey search is temporarily unavailable (attempt " + attempt + "/" + attempts + "), retrying");
            } catch (ApiEnvironmentException e) {
                if (attempt == attempts) throw e;
                logger.log("Whoosh journey search failed with " + e.getMessage() + " (attempt " + attempt + "/" + attempts + "), retrying");
            }
            Thread.sleep(5000L * attempt);
        }
    }

    private static JSONObject firstSegmentWithSeats(JSONArray journeys, int seats) {
        for (int i = 0; i < journeys.length(); i++) {
            JSONObject segment = journeys.getJSONObject(i).getJSONArray("segmentSchedules").getJSONObject(0);
            if (segment.optInt("availableSeats") >= seats) return segment;
        }
        return null;
    }
}
