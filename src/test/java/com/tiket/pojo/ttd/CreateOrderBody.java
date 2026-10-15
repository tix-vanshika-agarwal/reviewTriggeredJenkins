package com.tiket.pojo.ttd;

import java.util.ArrayList;

public class CreateOrderBody {
    public Collector collector;
    public ArrayList<Collector> collectors;
    public Contact contact;
    public ArrayList<PerBookingAnswers> perBookingAnswers;
    public ArrayList<PerPaxAnswers> perPaxAnswers;
    public ArrayList<PriceTierQuantities> priceTierQuantities;
    public String productScheduleId;
    public AddOn addon;
    public static class Collector{
        /** Member profile identifier from account / Events API; required by current Events create-order payload. */
        public Integer profileId;
        /** Single-collector ordinal; serialized as JSON number */
        public Integer collectorCode;
        public String countryCode;
        public String emailAddress;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public String priceTierCode;
        public String salutation;
    }

    public static class Contact{
        public String countryCode;
        public String emailAddress;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public String salutation;
        /** e.g. "Indonesia"; included in Events v2 contact object */
        public String nationality;
    }

    public static class PerBookingAnswers{
        public String countryCode;
        public String emailAddress;
        public String firstName;
        public String lastName;
        public String phoneNumber;
        public String salutation;
    }

    public static class PerPaxAnswers{

    }

    public static class PriceTierQuantities{
        public String code;
        public int quantity;
    }

    public  static class AddOn{
        public ArrayList<Insurances> insurances;
    }

    public static class Insurances{
        public int credentialId;
    }
}
