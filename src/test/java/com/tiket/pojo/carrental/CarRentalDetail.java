package com.tiket.pojo.carrental;

/**
 * Main Car Rental Detail class that aggregates data from different pages
 * Uses separate page-specific data classes for better organization
 */
public class CarRentalDetail {

    // ========== PAGE-SPECIFIC DATA OBJECTS ==========
    public CarRentalSrpData srpData;
    public CarRentalReviewPageData reviewPageData;
    public CarRentalBookingPageData bookingPageData;
    public CarRentalLandingPageData landingPageData;
    public int duration;
    public String location;

    // ========== BACKWARD COMPATIBILITY FIELDS ==========
    @Deprecated
    public String pickupLocation;
    @Deprecated
    public String dropOffLocation;
    @Deprecated
    public String extraFacilities1Title;
    @Deprecated
    public String extraFacilities1Price;
    @Deprecated
    public String extraFacilities2Title;
    @Deprecated
    public String extraFacilities2Price;
    @Deprecated
    public String allInPrice;
    @Deprecated
    public String extraFacilitiesTotalPrice;
    @Deprecated
    public String carRentalPackage;
    @Deprecated
    public String packagePrice;
    @Deprecated
    public String vendorNameSrp;
    @Deprecated
    public String carNameSrp;
    @Deprecated
    public String carPriceSrp;
    @Deprecated
    public String getPickupLocation;
    @Deprecated
    public String pickUpArea;
    @Deprecated
    public String getDropOffLocation;
    @Deprecated
    public String dropOffArea;
    @Deprecated
    public String pickUpPrice;
    @Deprecated
    public String dropOffPrice;

    // ========== CONSTRUCTORS ==========
    public CarRentalDetail() {
        this.srpData = new CarRentalSrpData();
        this.reviewPageData = new CarRentalReviewPageData();
        this.bookingPageData = new CarRentalBookingPageData();
        this.landingPageData = new CarRentalLandingPageData();
    }

    public CarRentalDetail(CarRentalSrpData srpData) {
        this.srpData = srpData;
        this.reviewPageData = new CarRentalReviewPageData();
        this.bookingPageData = new CarRentalBookingPageData();
        this.landingPageData = new CarRentalLandingPageData();
        // Set backward compatibility fields
        this.vendorNameSrp = srpData.vendorName;
        this.carNameSrp = srpData.carName;
        this.carPriceSrp = srpData.carPrice;
    }

    // ========== BUILDER METHODS ==========
    public static CarRentalDetail fromSrp(String vendorName, String carName, String carPrice,int duration) {
        CarRentalSrpData srpData = new CarRentalSrpData(vendorName, carName, carPrice,duration);
        return new CarRentalDetail(srpData);
    }

    public CarRentalDetail withReviewPageData(CarRentalReviewPageData reviewData) {
        this.reviewPageData = reviewData;
        // Set backward compatibility fields
        this.carRentalPackage = reviewData.carRentalPackage;
        this.packagePrice = reviewData.packagePrice;
        this.duration = reviewData.duration;
        this.allInPrice = reviewData.allInPrice;
        this.extraFacilitiesTotalPrice = reviewData.extraFacilitiesTotalPrice;
        this.extraFacilities1Title = reviewData.extraFacilities1Title;
        this.extraFacilities1Price = reviewData.extraFacilities1Price;
        this.extraFacilities2Title = reviewData.extraFacilities2Title;
        this.extraFacilities2Price = reviewData.extraFacilities2Price;
        this.pickUpArea = reviewData.pickUpArea;
        this.dropOffArea = reviewData.dropOffArea;
        return this;
    }

    public CarRentalDetail withBookingPageData(CarRentalBookingPageData bookingData) {
        this.bookingPageData = bookingData;
        // Set backward compatibility fields
        this.pickUpPrice = bookingData.pickUpPrice;
        this.dropOffPrice = bookingData.dropOffPrice;
        return this;
    }

    public CarRentalDetail withLandingPageData(CarRentalLandingPageData landingData) {
        this.landingPageData = landingData;
        return this;
    }

    // ========== LANDING PAGE DATA ACCESSORS ==========

    public CarRentalLandingPageData getLandingPageData() {
        return landingPageData;
    }

    public void setLandingPageData(CarRentalLandingPageData landingPageData) {
        this.landingPageData = landingPageData;
    }

    // ========== CONVENIENCE GETTERS ==========

    public String getVendorName() {
        return srpData != null && srpData.vendorName != null ? srpData.vendorName : vendorNameSrp;
    }

    public String getCarName() {
        return srpData != null && srpData.carName != null ? srpData.carName : carNameSrp;
    }

    public String getCarPrice() {
        return srpData != null && srpData.carPrice != null ? srpData.carPrice : carPriceSrp;
    }

    public String getCarRentalPackage() {
        return reviewPageData != null && reviewPageData.carRentalPackage != null ? reviewPageData.carRentalPackage : carRentalPackage;
    }

    public String getPackagePrice() {
        return reviewPageData != null && reviewPageData.packagePrice != null ? reviewPageData.packagePrice : packagePrice;
    }

    public int getDuration() {
        return reviewPageData != null && reviewPageData.duration > 0 ? reviewPageData.duration : duration;
    }

    public String getPickUpPrice() {
        return bookingPageData != null && bookingPageData.pickUpPrice != null ? bookingPageData.pickUpPrice : pickUpPrice;
    }

    public String getDropOffPrice() {
        return bookingPageData != null && bookingPageData.dropOffPrice != null ? bookingPageData.dropOffPrice : dropOffPrice;
    }
}
