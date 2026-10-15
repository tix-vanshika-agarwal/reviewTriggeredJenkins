package com.tiket.pojo.carrental;

/**
 * Car Rental data collected from Booking Page
 */
public class CarRentalBookingPageData {
    public String pickUpLocation;
    public String dropOffLocation;
    public String pickUpArea;
    public String dropOffArea;
    public String pickUpPrice;
    public String dropOffPrice;
    public String finalTotalPrice;

    public CarRentalBookingPageData() {}

    public CarRentalBookingPageData(String pickUpArea,String dropOffArea, String pickUpPrice, String dropOffPrice) {
        this.pickUpArea = pickUpArea;
        this.dropOffArea = dropOffArea;
        this.pickUpPrice = pickUpPrice;
        this.dropOffPrice = dropOffPrice;
    }

    public CarRentalBookingPageData(String pickUpLocation, String dropOffLocation, String pickUpArea,String dropOffArea, String pickUpPrice, String dropOffPrice, String finalTotalPrice) {
        this.pickUpLocation = pickUpLocation;
        this.dropOffLocation = dropOffLocation;
        this.pickUpArea = pickUpArea;
        this.dropOffArea = dropOffArea;
        this.pickUpPrice = pickUpPrice;
        this.dropOffPrice = dropOffPrice;
        this.finalTotalPrice = finalTotalPrice;
    }
}
