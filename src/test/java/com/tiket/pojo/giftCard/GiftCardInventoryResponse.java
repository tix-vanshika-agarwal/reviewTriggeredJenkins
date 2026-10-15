package com.tiket.pojo.giftCard;

import java.util.List;

public class GiftCardInventoryResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public List<BookingForm> bookingForms;
        public String searchKey;
        public Object sources;
        public List<Inventory> inventories;
        public Object priceField;
        public Configuration configurations;
    }

    public static class BookingForm {
        public String referenceField;
        public List<Validator> validators;
        public String name;
        public List<InputSource> inputSources;
        public String inputSourceUrl;
        public String placeholder;
        public String title;
        public String type;
        public String prefix;
        public String caption;
    }

    public static class Validator {
        public String parameter;
        public String name;
        public String message;
    }

    public static class InputSource {
        public Object value;
        public Object name;
    }

    public static class Inventory {
        public TiketData tiketData;
        public String id;
    }

    public static class TiketData {
        public String cardName;
        public String occasionValue;
        public String cardImageUrl;
        public String occasionName;
        public String messageTemplate;
    }

    public static class Configuration {
        public int maxRecipientCount;
        public String cardWording;
        public String recipientWording;
    }
}
