package com.tiket.pojo.flight;

public class FlightDynamicData {
    public FlightDynamicData(String airlineDepart,String airlineReturn,
                             String originAirport, String destinationAirport,
                             String departureDate, String returnDate,
                             String departureTimeDepart, String arrivalTimeDepart,
                             String departureTimeReturn, String arrivalTimeReturn) {
        this.airlineDepart = airlineDepart;
        this.airlineReturn = airlineReturn;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.departureTimeDepart = departureTimeDepart;
        this.arrivalTimeDepart = arrivalTimeDepart;
        this.departureTimeReturn = departureTimeReturn;
        this.arrivalTimeReturn = arrivalTimeReturn;
    }
    public String airlineDepart;
    public String airlineReturn;
    public String airlineCodeDepart;
    public String airlineCodeReturn;
    public String originAirport;
    public String destinationAirport;
    public String departureDate;
    public String returnDate;
    public String departureTimeDepart;
    public String arrivalTimeDepart;
    public String departureTimeReturn;
    public String arrivalTimeReturn;
    public String supplierUrl;
    public int totalTransitDepart;
    public int totalTransitReturn;
}
