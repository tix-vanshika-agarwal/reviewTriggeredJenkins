package com.tiket.controller.trip;

import com.tiket.apiPage.OrderApi;
import com.tiket.logging.ILogger;
import com.tiket.model.TripModel;
import com.tiket.page.order.trip.TripPage;
import com.tiket.testbase.Assertion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static org.hamcrest.Matchers.*;

public class TripviewController {

    private final TripModel tripModel;
    private final ILogger logger;
    private final TripPage<?> tripPage;
    private final OrderApi<?> orderApi;
    private final String accessToken;

    private JSONArray trips;
    private JSONArray filteredTrips;

    public TripviewController(
            TripModel tripModel,
            TripPage<?> tripPage,
            OrderApi<?> orderApi,
            String accessToken,
            ILogger logger
    ) {
        this.tripModel = tripModel;
        this.tripPage = tripPage;
        this.orderApi = orderApi;
        this.accessToken = accessToken;
        this.logger = logger;
    }

    public boolean verifyTripViewNavigationAndOrderList() {
        tripPage.skipCoachmark();
        boolean orderListOpen = tripPage.openOrderListSection();
        boolean tripViewOpen = tripPage.openTripViewSection();
        return orderListOpen && tripViewOpen;
    }

    public boolean verifyTripsCanBeExpandedAndCollapsed() {
        return tripPage.verifyTripsCanBeExpandedAndCollapsed();
    }

    public boolean verifySeeAllOrdersButton() {
        tripPage.openTripViewSection();
        return tripPage.verifySeeAllOrdersButton();
    }

    public void verifyOrderDetailAndETiketNavigation(List<String> expectedVerticals) {
        this.trips = TripModel.findTrips(orderApi, accessToken, logger);
        this.filteredTrips = TripModel.filterTrips(trips, expectedVerticals);
        TripModel.Trip firstTrip = TripModel.extractFirstTrip(filteredTrips);
        tripPage.openTrip(firstTrip);
        String orderId = tripPage.openFirstOrder();
        tripPage.openTrip(firstTrip);
        String content = tripPage.openFirstETiket();
        Assertion.assertThat("Verify orderID is not null", orderId, not(nullValue()));
        Assertion.assertThat("Verify eTicket Content is not null", content, not(nullValue()));
        Assertion.assertThat("Verify eTicket Content contains orderID", content, containsString(orderId));
    }

    public boolean verifyGrouping() {
        var cards = TripModel.extractOrderCardsFromFirstTrip(filteredTrips);
        Assertion.assertThat("Verify that the api response contains at least one trip with the expected verticals", filteredTrips.length(), greaterThan(0));

        TripModel.Trip firstTrip = TripModel.extractFirstTrip(filteredTrips);
        tripPage.openTrip(firstTrip);

        return cards.toList().stream()
                .map(card -> new JSONObject((java.util.Map<?, ?>) card))
                .allMatch(card -> {
                    JSONObject orderDetail = card.getJSONObject("attrs").getJSONObject("orderDetail");
                    String title = orderDetail.getString("title");
                    String cardVertical = card.getJSONObject("attrs").getJSONObject("order").getJSONObject("trackers").getString("verticalOwner");
                    tripPage.gotoCard(title);
                    switch (cardVertical) {
                        case "event", "ttd" -> {
                            var cardDetailsTTD = tripModel.getCardDetailsTTD(card);
                            return tripPage.verifyTTDCardDetails(cardDetailsTTD);
                        }
                        case "hotel" -> {
                            var cardDetailsHotel = tripModel.getCardDetailsHotel(card);
                            return tripPage.verifyHotelCardDetails(cardDetailsHotel);
                        }
                        case "train" -> {
                            var cardDetailsTrain = tripModel.getCardDetailsTrain(card);
                            return tripPage.verifyTrainCardDetails(cardDetailsTrain);
                        }
                        default -> throw new IllegalArgumentException(String.format("%s vertical is not supported", cardVertical));
                    }
                });
    }

    public boolean verifyLowProfileCrossSell() {
        String title = filteredTrips.toList().stream()
                .findFirst()
                .map(trip -> new JSONObject((java.util.Map<?, ?>) trip))
                .map(trip -> trip.getJSONObject("tripView").getJSONArray("cards"))
                .orElseThrow()
                .toList().stream()
                .map(card -> new JSONObject((java.util.Map<?, ?>) card))
                .filter(card -> card.getString("cardType").equals("banner"))
                .findFirst()
                .map(card -> card.getJSONObject("attrs").getJSONObject("banner").getString("textContent"))
                .orElseThrow();

        tripPage.refreshTripView();
        TripModel.Trip firstTrip = TripModel.extractFirstTrip(filteredTrips);
        tripPage.openTrip(firstTrip);
        tripPage.gotoLowProfileCrossSell(title);
        return tripPage.verifyLowProfileCrossSell();
    }

    public boolean verifyHighProfileCrossSell() {
        String title = filteredTrips.toList().stream()
                .findFirst()
                .map(trip -> new JSONObject((java.util.Map<?, ?>) trip))
                .map(trip -> trip.getJSONObject("tripView").getJSONArray("cards"))
                .orElseThrow()
                .toList().stream()
                .map(card -> new JSONObject((java.util.Map<?, ?>) card))
                .filter(card -> card.getString("cardType").equals("pageModule"))
                .findFirst()
                .map(card -> card.getJSONObject("attrs").getJSONObject("marker").getString("title"))
                .orElseThrow();

        tripPage.refreshTripView();
        TripModel.Trip firstTrip = TripModel.extractFirstTrip(filteredTrips);
        tripPage.openTrip(firstTrip);
        tripPage.gotoHighProfileCrossSell();
        return tripPage.verifyHighProfileCrossSell();
    }
}
