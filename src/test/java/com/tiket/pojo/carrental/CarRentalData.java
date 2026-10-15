package com.tiket.pojo.carrental;

public class CarRentalData {
    private String carRegionalId;
    private String carName;
    private String carRentalLocation;
    private String carType;

    public CarRentalData(String carRegionalId, String carName, String carRentalLocation, String carType) {
        this.carRegionalId = carRegionalId;
        this.carName = carName;
        this.carRentalLocation = carRentalLocation;
        this.carType = carType;
    }

    public String getCarRegionalId() {
        return carRegionalId;
    }

    public void setCarRegionalId(String carRegionalId) {
        this.carRegionalId = carRegionalId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarRentalLocation() {
        return carRentalLocation;
    }

    public void setCarRentalLocation(String carRentalLocation) {
        this.carRentalLocation = carRentalLocation;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    @Override
    public String toString() {
        return "CarRentalData{" +
                "carRegionalId='" + carRegionalId + '\'' +
                ", carName='" + carName + '\'' +
                ", carRentalLocation='" + carRentalLocation + '\'' +
                ", carType='" + carType + '\'' +
                '}';
    }
}
