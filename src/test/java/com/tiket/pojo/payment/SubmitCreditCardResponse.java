package com.tiket.pojo.payment;

public class SubmitCreditCardResponse {

    public String code;
    public String message;
    public Object errors;
    public Data data;

    public static class Data {
        public ThirdPartyResponse thirdPartyResponse;
    }

    public static class ThirdPartyResponse {
        public DataThirdParty data;
    }

    public static class DataThirdParty {
        public String acsUrl;
    }

}
