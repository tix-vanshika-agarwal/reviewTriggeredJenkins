package com.tiket.pojo.ttd;

import java.util.ArrayList;

public class CreateOrderV2Body {
    public Contact contact;
    public Collector collector;
    public ArrayList<Collector> collectors;
    public ArrayList<PriceTierQuantity> priceTierQuantities;
    public String productScheduleId;
    public ArrayList<PerBookingAnswer> perBookingAnswers;
    public LastPrice lastPrice;

    public static class Contact {
        public String salutation;
        public String firstName;
        public String lastName;
        public String emailAddress;
        public String countryCode;
        public String phoneNumber;
        public String nationality;
    }

    public static class Collector {
        public long profileId;
        public int collectorCode;
        public String priceTierCode;
        public String salutation;
        public String firstName;
        public String lastName;
        public String emailAddress;
        public String countryCode;
        public String phoneNumber;
        public String dateOfBirth;
        public String personalId;
        public String nationality;
        public String passportNumber;
        public String passportIssuingDate;
        public String passportExpiredDate;
        public String passportIssuingCountry;
    }

    public static class PriceTierQuantity {
        public String code;
        public int quantity;
        public int freeQuantity;
        public TierPrice lastPrice;
    }

    public static class TierPrice {
        public long finalPrice;
        public ArrayList<Object> insurances = new ArrayList<>();
    }

    public static class LastPrice {
        public int scale;
        public String currency;
        public String timestamp;
    }

    public static class PerBookingAnswer {
        public String answer;
        public Questionnaire questionnaire;
    }

    public static class Questionnaire {
        public String code;
        public String inputType;
        public boolean isRequired;
        public boolean isPerPax;
    }
}
