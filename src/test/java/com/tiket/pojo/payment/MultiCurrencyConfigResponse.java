package com.tiket.pojo.payment;

import java.util.List;

public class MultiCurrencyConfigResponse {
    public String code;
    public String message;
    public Data data;

    public static class Data {
        public List<Currency> content;
        public int totalElements;
    }

    public static class Currency {
        public String id;
        public String code;
        public String symbol;
        public String titleEng;
        public String titleIdr;
        public String gbvCode;
        public String gbvCurrency;
        public boolean userIsActive;
        public int verticalIsActive;
        public boolean isSuggested;
        public boolean consumerVisible;
    }
}
