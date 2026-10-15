package com.tiket.pojo.unm;

import com.tiket.core.CoreFunctions;

import java.util.List;

public class CountryListPojoResponse {
    private String code;
    private String message;
    private Object errors;
    private List<CountryData> data;
    private long serverTime;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Object getErrors() { return errors; }
    public void setErrors(Object errors) { this.errors = errors; }

    public List<CountryData> getData() { return data; }
    public void setData(List<CountryData> data) { this.data = data; }

    public long getServerTime() { return serverTime; }
    public void setServerTime(long serverTime) { this.serverTime = serverTime; }

    public static class CountryData {
        private int countryId;
        private String countriesIsoCode;
        private String countriesName;
        private String countriesIsdCode;
        private String countriesImage;

        public int getCountryId() { return countryId; }
        public void setCountryId(int countryId) { this.countryId = countryId; }

        public String getCountriesIsoCode() { return countriesIsoCode; }
        public void setCountriesIsoCode(String countriesIsoCode) { this.countriesIsoCode = countriesIsoCode; }

        public String getCountriesName() { return countriesName; }
        public void setCountriesName(String countriesName) { this.countriesName = countriesName; }

        public String getCountriesIsdCode() { return countriesIsdCode; }
        public void setCountriesIsdCode(String countriesIsdCode) { this.countriesIsdCode = countriesIsdCode; }

        public String getCountriesImage() { return countriesImage; }
        public void setCountriesImage(String countriesImage) { this.countriesImage = countriesImage; }
    }

    public CountryData getRandomCountryName() {
        if (data == null || data.isEmpty()) {
            return null; // or throw an exception based on your requirement
        }
        return data.get(CoreFunctions.generateRandomInt(0, data.size() - 1));
    }
}
