package com.tiket.pojo.b2b.corporate;

import java.util.ArrayList;

public class PostInterimDetailsApiResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class CorporateInformation{
        public String serviceFee;
        public String serviceFeeType;
        public String transactionalStatus;
    }

    public static class Data{
        public String createTravelPlanURL;
        public boolean bookingRequiredTravelPlan;
        public String paymentPreference;
        public CorporateInformation corporateInformation;
        public ArrayList<Object> travelPlans;
    }
}
