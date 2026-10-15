package com.tiket.pojo.flight;

import java.util.List;
import java.util.Map;

public class FlightStreamV4Response {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public long serverTime;

    public static class Data {
        public boolean searchCompleted;
        public List<String> requestItems;
        public Map<String, AirlineDetail> airlines;
        public Map<String, AirportDetail> airports;
        public Long departureFlightDeltaPrice;
        public String pricingPlacementId;
        public SearchList searchList;
    }

    public static class AirlineDetail {
        public String code;
        public String name;
        public String displayName;
        public String shortName;
        public String url;
        public String urlIcon;
    }

    public static class AirportDetail {
        public String airportCode;
        public String airportName;
        public String cityCode;
        public String cityName;
        public String countryCode;
    }

    public static class SearchList {
        public List<DepartureFlightV4> departureFlights;
        public List<DepartureFlightV4> returnFlights;
        public List<DepartureFlightV4> srtReturnFlights;
    }

    public static class DepartureFlightV4 {
        public String flightId;
        public String flightIdGeneral;
        public String fareId;
        public String supplierId;
        public String lyraKey;
        public boolean roundTrip;
        public boolean virtualInterlining;
        public int totalTravelTimeInMinutes;
        public List<String> tags;
        public List<String> campaignLabels;
        public TrackerProperties trackerProperties;
        public FilterProperties filterProperties;
        public Card card;

        public boolean hasTag(String tag) {
            return tags != null && tags.contains(tag);
        }

        public boolean isV4Shaped() {
            return card != null && trackerProperties != null;
        }

        public String mainPriceText() {
            if (card == null || card.priceComponents == null) return null;
            for (PriceComponent component : card.priceComponents) {
                if (component != null && component.text != null && "TextPrice".equals(component.text.color)) return component.text.value;
            }
            return null;
        }
    }

    public static class TrackerProperties {
        public long price;
        public long priceBeforeDiscount;
        public long totalPrice;
        public long comparisonFare;
    }

    public static class FilterProperties {
        public List<String> airlines;
        public long price;
    }

    public static class Card {
        public Endpoint departure;
        public Endpoint arrival;
        public String dayOffset;
        public String duration;
        public String selectedDepartTime;
        public int totalTransit;
        public List<FlightInfo> flightInfos;
        public List<String> facilities;
        public List<PriceComponent> priceComponents;
    }

    public static class Endpoint {
        public String date;
        public String time;
        public String airportCode;
        public String city;
    }

    public static class FlightInfo {
        public String icon;
        public String info;
    }

    public static class PriceComponent {
        public String icon;
        public TextValue text;
    }

    public static class TextValue {
        public String value;
        public String color;
        public String size;
        public String style;
    }
}
