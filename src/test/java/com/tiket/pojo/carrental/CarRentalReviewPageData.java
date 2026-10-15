package com.tiket.pojo.carrental;

/**
 * Car Rental data collected from Review Page
 */
public class CarRentalReviewPageData {
    public String pickUpArea;
    public String dropOffArea;
    public String carRentalPackage;
    public String packagePrice;
    public int duration;
    public String allInPrice;
    public String extraFacilitiesTotalPrice;

    // Extra Facilities
    public String extraFacilities1Title;
    public String extraFacilities1Price;
    public String extraFacilities2Title;
    public String extraFacilities2Price;

    // Rental time information
    public String pickupTime;
    public String dropOffTime;
    public String pickupDate;
    public String dropOffDate;

    // Package details
    public String packageDescription;
    public String packageFeatures;

    public CarRentalReviewPageData() {}

    public CarRentalReviewPageData( String carRentalPackage, String packagePrice) {
        this.carRentalPackage = carRentalPackage;
        this.packagePrice = packagePrice;
    }

    public CarRentalReviewPageData(String pickupLocation, String dropOffLocation,
                                 String pickUpArea, String dropOffArea,
                                 String carRentalPackage, String packagePrice,
                                 int duration, String allInPrice) {
        this.pickUpArea = pickUpArea;
        this.dropOffArea = dropOffArea;
        this.carRentalPackage = carRentalPackage;
        this.packagePrice = packagePrice;
        this.duration = duration;
        this.allInPrice = allInPrice;
    }
}
