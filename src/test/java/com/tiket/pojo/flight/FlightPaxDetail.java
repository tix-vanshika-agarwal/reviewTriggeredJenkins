package com.tiket.pojo.flight;

import com.tiket.core.CoreFunctions;

import java.util.ArrayList;

public class FlightPaxDetail {
    public FlightPaxDetail(int adult, int child, int infant, String flightRoute, String airline, String paxNationality) {
        data = new ArrayList<>();
        data.add(null); // index 0 is unused, so data starts from index 1
        addPax(adult, "ADULT", flightRoute, airline, paxNationality);
        addPax(child, "CHILD", flightRoute, airline, paxNationality);
        addPax(infant, "INFANT", flightRoute, airline, paxNationality);
    }

    private void addPax(int count, String paxType, String flightRoute, String airline, String paxNationality) {
        for (int i = 0; i < count; i++) {
            paxData pax = new paxData();
            pax.paxType = paxType;
            String firstName = CoreFunctions.randomNameWithoutTest();
            String lastName = CoreFunctions.randomLastName();
            if (airline.toLowerCase().contains("citilink") || airline.toLowerCase().contains("garuda")) {
                lastName = "Testing";
            }
            if (!flightRoute.equalsIgnoreCase("domestic")) {
                pax.paxFirstName = firstName;
                pax.paxLastName = lastName;
            } else {
                pax.paxFirstName = firstName + " " + lastName;
                pax.paxLastName = "";
            }
            pax.paxBirthDate = randomBirthDate(paxType);
            pax.paxNationality = paxNationality;
            //expiry date 2+ years from now
            pax.paxPassportExpiryDate = generatePassportExpiryDate(6);
            pax.paxPassportIssueDate = generatePassportExpiryDate(-2);
            pax.paxPassportCountry = paxNationality;
            pax.paxTitle = "";
            pax.paxPassportNumber = "";
            data.add(pax);
        }
    }
    public ArrayList<paxData> data;
    public static class paxData{
        public String paxType;
        public String paxFirstName;
        public String paxLastName;
        public String paxTitle;
        public String paxBirthDate;
        public String paxNationality;
        public String paxPassportNumber;
        public String paxPassportExpiryDate;
        public String paxPassportCountry;
        public String paxPassportIssueDate;
    }

    /**
     * Generate a random birth date string in format YYYY-MM-DD based on passenger type.
     * @param type "adult", "child", or "infant"
     * @return random birth date string
     */
    public static String randomBirthDate(String type) {
        java.util.Random rand = new java.util.Random();
        java.time.LocalDate today = java.time.LocalDate.now();
        int year, month, day;
        switch (type.toLowerCase()) {
            case "adult":
                // above 12 years old (13 to 70)
                int adultAge = 18 + rand.nextInt(58); // 18 to 70
                year = today.getYear() - adultAge;
                break;
            case "child":
                // 2 < age < 16
                int childAge = 2 + rand.nextInt(9); //  (2 - 11 years old)
                year = today.getYear() - childAge;
                break;
            case "infant":
                // below 2 years old (0 or 1)
                year = today.getYear() - rand.nextInt(2); // 0 or 1 years old
                break;
            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
        month = 1 + rand.nextInt(12); // 1-12
        // Get max day for the month/year
        java.time.YearMonth ym = java.time.YearMonth.of(year, month);
        day = 1 + rand.nextInt(ym.lengthOfMonth());
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * Generates a passport expiry date in the future.
     * @param numberOfYears Number of years into the future
     * @return Expiry date as a string in YYYY-MM-DD format
     */
    public static String generatePassportExpiryDate(int numberOfYears) {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate expiryDate = today.plusYears(numberOfYears);
        return expiryDate.toString(); // YYYY-MM-DD
    }
}
