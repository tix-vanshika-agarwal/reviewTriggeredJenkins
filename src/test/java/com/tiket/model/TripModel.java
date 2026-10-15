package com.tiket.model;

import com.tiket.apiPage.OrderApi;
import com.tiket.logging.ILogger;
import com.tiket.testbase.Configs;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TripModel {

    public record Trip(String title, String subTitle){}
    public record Card(String title, String cardVertical){}
    public record TTDCard(String name, String date, String guestCount){}
    public record HotelCard(String hotelName,  String duration, String guestName){}
    public record TrainCard(String vendorName, String origin, String destination, String startTime, String endTime, String paxName){}

    public static JSONArray findTrips(OrderApi<?> orderApi, String accessToken, ILogger logger) {
        JSONObject trips = orderApi.getTrips(accessToken);
        if(Configs.DEBUG_MODE) {
            logger.log("Trips api response: " + trips.toString(2));
        }

        return trips.getJSONObject("data")
                .getJSONArray("list");
    }

    public static JSONArray filterTrips(JSONArray trips, List<String> expectedVerticals) {
        return trips.toList().stream()
                .map(trip -> new JSONObject((java.util.Map<?, ?>) trip))
                .filter(trip -> {
                    if(!trip.getString("viewType").equals("tripView")) {
                        return false;
                    }
                    String verticalOwners = trip.getJSONObject("tripView")
                            .getJSONObject("trackers")
                            .getString("verticalOwner");
                    return expectedVerticals.stream().allMatch(verticalOwners::contains);
                })
                .collect(JSONArray::new, JSONArray::put, JSONArray::put);
    }

    public static Trip extractFirstTrip(JSONArray trips) {
        return trips.toList().stream()
                .findFirst()
                .map(trip -> new JSONObject((java.util.Map<?, ?>) trip))
                .map(trip -> new Trip(
                        trip.getJSONObject("tripView").getString("title"),
                        trip.getJSONObject("tripView").getString("subtitle")
                ))
                .orElseThrow();
    }

    public static JSONArray extractCardsFromFirstTrip(JSONArray trips) {
        return trips.toList().stream()
                .findFirst()
                .map(trip -> new JSONObject((java.util.Map<?, ?>) trip))
                .map(trip -> trip.getJSONObject("tripView").getJSONArray("cards"))
                .orElseThrow();
    }

    public static JSONArray extractOrderCardsFromFirstTrip(JSONArray trips) {
        return extractCardsFromFirstTrip(trips)
                .toList().stream()
                .map(card -> new JSONObject((java.util.Map<?, ?>) card))
                .filter(card -> card.getString("cardType").equals("order"))
                .collect(JSONArray::new, JSONArray::put, JSONArray::put);
    }

    public TTDCard getCardDetailsTTD(JSONObject card) {
        JSONObject orderDetail = card.getJSONObject("attrs").getJSONObject("orderDetail");
        String name = orderDetail.getString("title");
        String date = orderDetail.getJSONArray("subtitles").getString(0);
        String guestCount = orderDetail.getJSONObject("beneficiaries").getJSONArray("names").getString(0);
        return new TTDCard(name, date, guestCount);
    }

    public HotelCard getCardDetailsHotel(JSONObject card) {
        JSONObject orderDetail = card.getJSONObject("attrs").getJSONObject("orderDetail");
        String hotelName = orderDetail.getString("title");
        String duration = orderDetail.getJSONArray("subtitles").getString(0);
        String guestName = orderDetail.getJSONObject("beneficiaries").getJSONArray("names").getString(0);
        return new HotelCard(hotelName, duration, guestName);
    }

    public TrainCard getCardDetailsTrain(JSONObject card) {
        JSONObject orderDetail = card.getJSONObject("attrs").getJSONObject("orderDetail");
        String vendorName = orderDetail.getString("title");
        String origin = orderDetail.getJSONArray("timelineSteps").getJSONObject(0).getString("subtitle");
        String destination = orderDetail.getJSONArray("timelineSteps").getJSONObject(1).getString("subtitle");
        String startTime = orderDetail.getJSONArray("timelineSteps").getJSONObject(0).getString("time");
        String endTime = orderDetail.getJSONArray("timelineSteps").getJSONObject(1).getString("time");
        String paxName = orderDetail.getJSONObject("beneficiaries").getJSONArray("names").getString(0);
        return new TrainCard(vendorName, origin, destination, startTime, endTime, paxName);
    }
}