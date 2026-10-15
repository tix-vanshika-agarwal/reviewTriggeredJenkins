package com.tiket.pojo.b2b.corporate;

public class PostInterimDetailsApiHotel {
    public String productType;
    public String verticalCartID;
    public BookingCriteria bookingCriteria;
    public String notes;

    public static class BookingCriteria{
        public Hotel hotel;
    }

    public static class Hotel{
        public String cartId;
        public String publicId;
        public String startDate;
        public String endDate;
        public String nights;
        public String countryId;
        public String cityId;
        public String regionId;
        public String areaId;
    }
}
