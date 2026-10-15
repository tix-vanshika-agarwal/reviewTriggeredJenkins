package com.tiket.pojo.carrental;

import lombok.Getter;
import lombok.Setter;

/**
 * POJO class to hold car rental landing page data
 */
public class CarRentalLandingPageData {
    public String driverType; // with driver or self drive
    @Setter
    @Getter
    public String dateSelected;
    public String timeSelected;
    @Setter
    @Getter
    public String locationSelected;
    @Setter
    @Getter
    public int timeDurationFormat; // e.g., "12h/per day", "24h/per day"

    public CarRentalLandingPageData() {
    }

    public CarRentalLandingPageData(String tripType, String dateSelected, String timeSelected, String locationSelected,String timeDurationFormat) {
        this.driverType = tripType;
        this.dateSelected = dateSelected;
        this.timeSelected = adjustTimeIfNeeded(timeSelected);
        this.locationSelected = locationSelected;
        this.timeDurationFormat = Integer.parseInt(timeDurationFormat.replaceAll("[^0-9]", ""));
    }

    // Getter and Setter methods
    public String getTripType() {
        return driverType;
    }

    public void setTripType(String tripType) {
        this.driverType = tripType;
    }

    public void setTimeSelected(String timeSelected) {
        this.timeSelected = adjustTimeIfNeeded(timeSelected);
    }

    public void setTimeDurationFormat(String timeDurationFormat) {
        this.timeDurationFormat = Integer.parseInt(timeDurationFormat.replaceAll("[^0-9]", ""));
    }

    /**
     * Adjusts the time if it's greater than or equal to "21:00".
     * If time >= "21:00", it will be changed to "20:59".
     *
     * @param time the input time string
     * @return adjusted time string
     */
    private String adjustTimeIfNeeded(String time) {
        if (time == null || time.trim().isEmpty()) {
            return time;
        }

        try {
            // Parse time in HH:mm format
            String[] timeParts = time.trim().split(":");
            if (timeParts.length == 2) {
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);

                // If time is >= 21:00, change to 20:59
                if (hour >= 21) {
                    return "20:59";
                }
            }
        } catch (NumberFormatException e) {
            // If parsing fails, return original time
            return time;
        }

        return time;
    }

    @Override
    public String toString() {
        return "CarRentalLandingPageData{" +
                "tripType='" + driverType + '\'' +
                ", dateSelected='" + dateSelected + '\'' +
                ", timeSelected='" + timeSelected + '\'' +
                ", locationSelected='" + locationSelected + '\'' +
                '}';
    }
}
