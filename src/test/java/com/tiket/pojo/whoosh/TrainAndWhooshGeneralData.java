package com.tiket.pojo.whoosh;

import com.tiket.core.CoreFunctions;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainData;

import static com.tiket.core.CoreFunctions.log;

/**
 * Represents general data for a Whoosh train trip, including stations, passengers, trip type, and formatted dates.
 * Handles calculation and formatting of departure and return dates in both English and Indonesian formats.
 */
public class TrainAndWhooshGeneralData {
    /** Origin station code or name. */
    public String originStation;
    /** Destination station code or name. */
    public String destinationStation;
    /** Origin city name. */
    public String originCityName;
    /** Destination city name. */
    public String destinationCityName;
    /** Total number of passengers. */
    public int numberOfPassenger;
    /** Number of adult passengers. */
    public int adultPassenger;
    /** Number of infant passengers. */
    public int infantPassenger;
    /** Trip type: "oneway" or "return". */
    public String tripType;
    /** Days to add to current date for departure. */
    public int daysToAddForDeparture;
    /** Duration in days for return trip. */
    public int duration;
    /** Departure date in long format (e.g., "25 December 2023"). */
    public String departureDateLong;
    /** Return date in long format (e.g., "28 December 2023"). */
    public String returnDateLong;
    /** Departure date formatted (dd MMM) in Indonesian. */
    public String whooshDateFormatted_dd_MMM_Id;
    /** Departure date formatted (dd MMM) in English. */
    public String whooshDateFormatted_dd_MMM_En;
    /** Return date formatted (dd MMM) in Indonesian. */
    public String whooshReturnDateFormatted_dd_MMM_Id;
    /** Return date formatted (dd MMM) in English. */
    public String whooshReturnDateFormatted_dd_MMM_En;
    /** Departure date formatted (dd MMM yy) in Indonesian. */
    public String whooshDateFormatted_dd_MMM_yy_Id;
    /** Departure date formatted (dd MMM yy) in English. */
    public String whooshDateFormatted_dd_MMM_yy_En;
    /** Return date formatted (dd MMM yy) in Indonesian. */
    public String whooshReturnDateFormatted_dd_MMM_yy_Id;
    /** Return date formatted (dd MMM yy) in English. */
    public String whooshReturnDateFormatted_dd_MMM_yy_En;
    /** TrainData for booking page. */
    public TrainData trainDataBookingPage;
    /** TrainData for payment page. */
    public TrainData trainDataPaymentPage;
    /** Total payment amount on booking page. */
    public String totalPaymentBookingPage;
    /** Total payment amount on payment page. */
    public String totalPaymentPaymentPage;
    /** Price detail breakdown for departure and arrival stations. */
    public String departAndArrivalStationPriceDetail;
    /** SRP first card price per passenger for departure. */
    public String srpFirstCardPricePerPaxDepart;
    /** SRP first card price per passenger for return. */
    public String srpFirstCardPricePerPaxReturn;
    /** Insurance general data selected. */
    public InsuranceGeneralData insuranceGeneralData;
    public String slashedPricePerPaxDepart;
    public String slashedPricePerPaxReturn;

    /**
     * Constructs a TrainAndWhooshGeneralData object and calculates/assigns formatted dates.
     * @param originStation Origin station code or name
     * @param destinationStation Destination station code or name
     * @param numberOfPassenger Total number of passengers
     * @param adultPassenger Number of adult passengers
     * @param infantPassenger Number of infant passengers
     * @param tripType Trip type: "oneway" or "return"
     * @param daysToAddForDeparture Days to add to current date for departure
     * @param duration Duration in days for return trip
     */
    public TrainAndWhooshGeneralData(String originStation, String destinationStation, int numberOfPassenger, int adultPassenger, int infantPassenger, String tripType, int daysToAddForDeparture, int duration) {
        this.originStation = originStation.trim().toLowerCase();
        this.destinationStation = destinationStation.trim().toLowerCase();
        this.numberOfPassenger = numberOfPassenger;
        this.adultPassenger = adultPassenger;
        this.infantPassenger = infantPassenger;
        this.tripType = tripType;
        this.daysToAddForDeparture = daysToAddForDeparture;
        this.duration = duration;

        // Calculate departureDateLong and returnDateLong
        this.departureDateLong = CoreFunctions.addDaysToLongCurrentDate(daysToAddForDeparture);
        if(!tripType.equalsIgnoreCase("oneway")){
            this.returnDateLong = CoreFunctions.addToLongDate(this.departureDateLong,duration);
        } else {
            this.returnDateLong = "";
        }
        // Format dates
        String[] formattedDatesDepart = getDateInEnAndId(departureDateLong);
        this.whooshDateFormatted_dd_MMM_Id = formattedDatesDepart[0];
        this.whooshDateFormatted_dd_MMM_En = formattedDatesDepart[1];
        if(!tripType.equalsIgnoreCase("oneway")){
            String[] formattedDatesReturn = getDateInEnAndId(returnDateLong);
            this.whooshReturnDateFormatted_dd_MMM_Id = formattedDatesReturn[0];
            this.whooshReturnDateFormatted_dd_MMM_En = formattedDatesReturn[1];
        } else {
            this.whooshReturnDateFormatted_dd_MMM_Id = "";
            this.whooshReturnDateFormatted_dd_MMM_En = "";
        }

        String[] formattedDatesDepartWithYear = getDateInEnAndIdWithYear(departureDateLong);
        this.whooshDateFormatted_dd_MMM_yy_Id = formattedDatesDepartWithYear[0];
        this.whooshDateFormatted_dd_MMM_yy_En = formattedDatesDepartWithYear[1];
        if(!tripType.equalsIgnoreCase("oneway")){
            String[] formattedDatesReturnWithYear = getDateInEnAndIdWithYear(returnDateLong);
            this.whooshReturnDateFormatted_dd_MMM_yy_Id = formattedDatesReturnWithYear[0];
            this.whooshReturnDateFormatted_dd_MMM_yy_En = formattedDatesReturnWithYear[1];
        } else {
            this.whooshReturnDateFormatted_dd_MMM_yy_Id = "";
            this.whooshReturnDateFormatted_dd_MMM_yy_En = "";
        }
    }

    /**
     * Constructs a TrainAndWhooshGeneralData object and calculates/assigns formatted dates.
     * @param originStation Origin station code or name
     * @param destinationStation Destination station code or name
     * @param numberOfPassenger Total number of passengers
     * @param adultPassenger Number of adult passengers
     * @param infantPassenger Number of infant passengers
     * @param tripType Trip type: "oneway" or "return"
     * @param dateRange Days to add to current date for departure
     * @param duration Duration in days for return trip
     */
    public TrainAndWhooshGeneralData(String originStation, String destinationStation, int numberOfPassenger, int adultPassenger, int infantPassenger, String tripType, String dateRange, int duration) {
        this.originStation = originStation.trim().toLowerCase();
        this.destinationStation = destinationStation.trim().toLowerCase();
        this.numberOfPassenger = numberOfPassenger;
        this.adultPassenger = adultPassenger;
        this.infantPassenger = infantPassenger;
        this.tripType = tripType;
        this.daysToAddForDeparture = CoreFunctions.generateRandomInt(dateRange);
        this.duration = duration;

        // Calculate departureDateLong and returnDateLong
        this.departureDateLong = CoreFunctions.addDaysToLongCurrentDate(daysToAddForDeparture);
        if(!tripType.equalsIgnoreCase("oneway")){
            this.returnDateLong = CoreFunctions.addToLongDate(this.departureDateLong,duration);
            log("Duration: " + duration + " days" + " | Return Date Long: " + this.returnDateLong);
        } else {
            this.returnDateLong = "";
        }
        // Format dates
        String[] formattedDatesDepart = getDateInEnAndId(departureDateLong);
        this.whooshDateFormatted_dd_MMM_Id = formattedDatesDepart[0];
        this.whooshDateFormatted_dd_MMM_En = formattedDatesDepart[1];
        if(!tripType.equalsIgnoreCase("oneway")){
            String[] formattedDatesReturn = getDateInEnAndId(returnDateLong);
            this.whooshReturnDateFormatted_dd_MMM_Id = formattedDatesReturn[0];
            this.whooshReturnDateFormatted_dd_MMM_En = formattedDatesReturn[1];
        } else {
            this.whooshReturnDateFormatted_dd_MMM_Id = "";
            this.whooshReturnDateFormatted_dd_MMM_En = "";
        }

        String[] formattedDatesDepartWithYear = getDateInEnAndIdWithYear(departureDateLong);
        this.whooshDateFormatted_dd_MMM_yy_Id = formattedDatesDepartWithYear[0];
        this.whooshDateFormatted_dd_MMM_yy_En = formattedDatesDepartWithYear[1];
        if(!tripType.equalsIgnoreCase("oneway")){
            String[] formattedDatesReturnWithYear = getDateInEnAndIdWithYear(returnDateLong);
            this.whooshReturnDateFormatted_dd_MMM_yy_Id = formattedDatesReturnWithYear[0];
            this.whooshReturnDateFormatted_dd_MMM_yy_En = formattedDatesReturnWithYear[1];
        } else {
            this.whooshReturnDateFormatted_dd_MMM_yy_Id = "";
            this.whooshReturnDateFormatted_dd_MMM_yy_En = "";
        }
    }

    /**
     * Constructs a TrainAndWhooshGeneralData object and calculates/assigns formatted dates.
     * @param originStation Origin station code or name
     * @param destinationStation Destination station code or name
     * @param numberOfPassenger Total number of passengers
     * @param adultPassenger Number of adult passengers
     * @param infantPassenger Number of infant passengers
     * @param tripType Trip type: "oneway" or "return"
     * @param daysToAddForDeparture Days to add to current date for departure
     */
    public TrainAndWhooshGeneralData(String originStation, String destinationStation, int numberOfPassenger, int adultPassenger, int infantPassenger, String tripType, int daysToAddForDeparture) {
        this(originStation, destinationStation, numberOfPassenger, adultPassenger, infantPassenger, tripType, daysToAddForDeparture, 0);
    }

    /**
     * Constructs a TrainAndWhooshGeneralData object and calculates/assigns formatted dates.
     * @param originStation Origin station code or name
     * @param destinationStation Destination station code or name
     * @param numberOfPassenger Total number of passengers
     * @param adultPassenger Number of adult passengers
     * @param infantPassenger Number of infant passengers
     * @param tripType Trip type: "oneway" or "return"
     * @param dateRange Days to add to current date for departure
     */
    public TrainAndWhooshGeneralData(String originStation, String destinationStation, int numberOfPassenger, int adultPassenger, int infantPassenger, String tripType, String dateRange) {
        this(originStation, destinationStation, numberOfPassenger, adultPassenger, infantPassenger, tripType, dateRange, 0);
    }

    /**
     * Converts a date string (e.g., "25 December 2023") to Indonesian and English formats (dd MMM).
     * @param selectedDate Date string in format "dd MMMM yyyy"
     * @return Array: [Indonesian format, English format]
     * ex: "25 December 2023" -> ["25 Des", "25 Dec"]
     */
    public String[] getDateInEnAndId(String selectedDate){
        String[] dateArray = selectedDate.split(" ");
        if (dateArray.length < 2) {
            log("Invalid date format: " + selectedDate);
            return new String[]{selectedDate, selectedDate};
        }
        String monthEngToId = CoreFunctions.monthEngToId(dateArray[1]).substring(0,3);
        String selectedDateFormattedId = dateArray[0] + " " + monthEngToId;
        String selectedDateFormattedEn = dateArray[0] + " " + dateArray[1].substring(0,3);
        log("Selected date formatted id: " + selectedDateFormattedId);
        log("Selected date formatted en: " + selectedDateFormattedEn);
        return new String[]{selectedDateFormattedId, selectedDateFormattedEn};
    }

    /**
     * Converts a date string (e.g., "25 December 2023") to Indonesian and English formats (dd MMM yy).
     * @param selectedDate Date string in format "dd MMMM yyyy"
     * @return Array: [Indonesian format, English format]
     * ex: "25 December 2023" -> ["25 Des 23", "25 Dec 23"]
     */
    public String[] getDateInEnAndIdWithYear(String selectedDate){
        String[] dateArray = selectedDate.split(" ");
        if (dateArray.length < 3) {
            log("Invalid date format: " + selectedDate);
            return new String[]{selectedDate, selectedDate};
        }
        String yearTwoDigits = dateArray[2].length() == 4 ? dateArray[2].substring(2) : dateArray[2];
        String monthEngToId = CoreFunctions.monthEngToId(dateArray[1]).substring(0,3);
        String selectedDateFormattedId = dateArray[0] + " " + monthEngToId + " " + yearTwoDigits;
        String selectedDateFormattedEn = dateArray[0] + " " + dateArray[1].substring(0,3) + " " + yearTwoDigits;
        log("Selected date formatted id: " + selectedDateFormattedId);
        log("Selected date formatted en: " + selectedDateFormattedEn);
        return new String[]{selectedDateFormattedId, selectedDateFormattedEn};
    }

}
