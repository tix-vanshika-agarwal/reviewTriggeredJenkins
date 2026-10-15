package com.tiket.pojo.b2b.corporate;

public class CorporateCodeData {
    private String airlineCode;
    private String corporateCode;
    private String airlineName;

    public CorporateCodeData(String airlineName, String airlineCode, String corporateCode) {
        this.airlineCode = airlineCode;
        this.corporateCode = corporateCode;
        this.airlineName = airlineName;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getCorporateCode() {
        return corporateCode;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}
