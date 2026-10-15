package com.tiket.pojo.giftCard;

import java.util.List;

public class GiftCardCartResponse {
    public String code;
    public String message;
    public Object errors;
    public Data data;
    public String serverTime;

    public static class Data {
        public List<BookingForm> bookingForms;
        public String bookingStatus;
        public String cartSecret;
        public List<Inventory> inventories;
        public PriceDetail priceDetail;
        public String cartId;
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
        public String caption;
    }

    public static class Validator {
        public String parameter;
        public String name;
        public String message;
    }

    public static class InputSource {
        public String name;
        public String value;
    }

    public static class Inventory {
        public String id;
        public TiketData tiketData;
        public Object vendorData;
        public InventoryPrice inventoryPrice;
        public String vertical;
        public String status;
        public Object bookingType;
        public BookingCount bookingCount;
        public BookingPrice bookingPrice;
        public BookingData bookingData;
        public Object bookingExpiryDate;
        public Object orderItemId;
        public Object bookingId;
        public List<BookingItem> bookingItems;
    }

    public static class TiketData {
        public String occasionImageUrl;
        public String cardName;
        public String cardId;
        public String occasionId;
        public String cardImageUrl;
        public String occasionName;
        public String messageTemplate;
    }

    public static class InventoryPrice {
        public int totalPrice;
    }

    public static class BookingCount {
        public int recipientCount;
    }

    public static class BookingPrice {
        public int totalPrice;
    }

    public static class BookingData {
        public String tncVersion;
    }

    public static class BookingItem {
        public String voucherMessage;
        public int voucherAmount;
        public int paxId;
    }

    public static class PriceDetail {
        public String currency;
        public String name;
        public double value;
        public List<Detail> details;
    }

    public static class Detail {
        public String name;
        public List<SubDetail> details;
        public Object additionalAttributes;
    }

    public static class SubDetail {
        public String name;
        public List<Object> details;
        public String textValue;
        public String textStyle;
        public int value;
        public String textColor;
    }
}
