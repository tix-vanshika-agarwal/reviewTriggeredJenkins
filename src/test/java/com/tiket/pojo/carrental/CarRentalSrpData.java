package com.tiket.pojo.carrental;

/**
 * Car Rental data collected from Search Results Page (SRP)
 */
public class CarRentalSrpData {
    public String vendorName;
    public String carName;
    public String carPrice;
    public int duration;

    public CarRentalSrpData() {}

    public CarRentalSrpData(String vendorName, String carName, String carPrice, int duration) {
        this.vendorName = vendorName;
        this.carName = carName;
        this.carPrice = carPrice;
        this.duration = duration;
    }
}
