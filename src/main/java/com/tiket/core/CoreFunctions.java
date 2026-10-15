package com.tiket.core;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.tiket.logging.ILogger;
import com.tiket.model.Environment;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.PriceType;
import com.tiket.model.api.UriHelper;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.text.Normalizer;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.tiket.model.PriceType.DECIMAL;
import static com.tiket.model.PriceType.THOUSAND;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class CoreFunctions {

    protected static ILogger logger = BaseTest.mainLogger.get();
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    private static final DateTimeFormatter SERVER_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.US);
    private static final DateTimeFormatter SERVER_TIME_UI_FORMATTER = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy, h:mm a 'UTC+0'", Locale.US);

    private static ZonedDateTime getUtcZonedDateTime(long serverTimeMilli) {
        return Instant.ofEpochMilli(serverTimeMilli).atZone(UTC_ZONE);
    }

    public static boolean isJenkinsSystem() {
        String pwd = System.getProperty("user.dir");
        return pwd != null && pwd.contains("jenkins");
    }

    public static boolean isMacSystem() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("mac");
    }

    public static int getTotalWords(String text) {
        if (text == null || text.trim().isEmpty()) return 0;
        return text.split("\\s+").length;
    }

    public static int generateRandomInt(int min, int max) {
        int num = (int) (Math.random() * (max - min + 1) + min);
        log("Got random number -> "+num);
        return num;
    }

    public static int generateRandomInt(String range) {
        // Example range = "3-7"
        String[] parts = range.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format! Expected 'min-max', got: " + range);
        }

        int min = Integer.parseInt(parts[0].trim());
        int max = Integer.parseInt(parts[1].trim());

        if (min > max) {
            throw new IllegalArgumentException("Invalid range: min cannot be greater than max");
        }

        int num = (int) (Math.random() * (max - min + 1) + min);
        log("Got random number -> " + num);
        return num;
    }

    /**
     * Strips a leading zero from the day after a weekday comma prefix (e.g. {@code Mon, 01 Jun 2026} → {@code Mon, 1 Jun 2026}).
     * Applies to every such segment in the string (e.g. date ranges). Idempotent for already-normalized dates.
     */
    public static String removeLeadingZeroFromDate(String date) {
        if (date == null || date.isEmpty()) {
            return date;
        }
        return date.replaceAll(", 0([1-9])", ", $1");
    }

    public static long generateRandomInt(int length) {
        long min = (long) Math.pow(10, length - 1);
        long max = (min * 10) - 1;

        return (long) (Math.random() * (max - min)) + min;
    }

    public static int getMonthNumber(String month) {
        HashMap<String, Integer> monthNumber = new HashMap<>();
        monthNumber.put("Januari", 1);
        monthNumber.put("Jan", 1);

        monthNumber.put("Februari", 2);
        monthNumber.put("Feb", 2);

        monthNumber.put("Maret", 3);
        monthNumber.put("Mar", 3);

        monthNumber.put("April", 4);
        monthNumber.put("Apr", 4);

        monthNumber.put("Mei", 5);
        monthNumber.put("May", 5);

        monthNumber.put("Juni", 6);
        monthNumber.put("Jun", 6);

        monthNumber.put("Juli", 7);
        monthNumber.put("Jul", 7);

        monthNumber.put("Agustus", 8);
        monthNumber.put("Agu", 8);
        monthNumber.put("Aug", 8);
        monthNumber.put("Agt", 8);

        monthNumber.put("September", 9);
        monthNumber.put("Sep", 9);

        monthNumber.put("Oktober", 10);
        monthNumber.put("Okt", 10);
        monthNumber.put("Oct", 10);

        monthNumber.put("November", 11);
        monthNumber.put("Nov", 11);

        monthNumber.put("Desember", 12);
        monthNumber.put("Des", 12);
        monthNumber.put("Dec", 12);

        Integer num = monthNumber.get(month);
        if (num == null) {
            return monthNumber.get(monthEngToId(month));
        } else {
            return num;
        }
    }

    public static String getMonthName(int month, String language) {
        HashMap<Integer, String> monthName = new HashMap<>();
        monthName.put(1, "Januari");
        monthName.put(2, "Februari");
        monthName.put(3, "Maret");
        monthName.put(4, "April");
        monthName.put(5, "Mei");
        monthName.put(6, "Juni");
        monthName.put(7, "Juli");
        monthName.put(8, "Agustus");
        monthName.put(9, "September");
        monthName.put(10, "Oktober");
        monthName.put(11, "November");
        monthName.put(12, "Desember");

        String name = monthName.get(month);
        if (language.equals("en")) {
            return monthIdToEng(name);
        } else {
            return name;
        }
    }

    /** Normalizes UI text for comparison (non-breaking spaces/hyphens, unicode dashes, leading underscores, etc.). */
    public static String normalizeNonBreakingSpaceText(String text) {
        if (text == null) {
            return "";
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFKC);
        normalized = normalized
                .replace('\u00A0', ' ')
                .replace('\u202F', ' ')
                .replace('\u2007', ' ')
                .replace('\u2011', '-')
                .replace('\u2010', '-')
                .replace('\u2012', '-')
                .replace('\u2013', '-')
                .replace('\u2014', '-')
                .replace('\u2212', '-');
        return normalized.replaceAll("^_+", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public static <T> T getRandomObject(T[] arrayList) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(arrayList.length);
        return arrayList[randomIndex];
    }

    public static <T> T getRandomObject(List<T> arrayList) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(arrayList.size());
        return arrayList.get(randomIndex);
    }

    public static String monthIdToEng(String month) {
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("Januari", "January");
        monthString.put("Februari", "February");
        monthString.put("Maret", "March");
        monthString.put("April", "April");
        monthString.put("Mei", "May");
        monthString.put("Juni", "June");
        monthString.put("Juli", "July");
        monthString.put("Agustus", "August");
        monthString.put("September", "September");
        monthString.put("Oktober", "October");
        monthString.put("November", "November");
        monthString.put("Desember", "December");
        return monthString.get(month);
    }

    public static String monthEngToId(String month) {
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("January", "Januari");
        monthString.put("February", "Februari");
        monthString.put("March", "Maret");
        monthString.put("April", "April");
        monthString.put("May", "Mei");
        monthString.put("June", "Juni");
        monthString.put("July", "Juli");
        monthString.put("August", "Agustus");
        monthString.put("September", "September");
        monthString.put("October", "Oktober");
        monthString.put("November", "November");
        monthString.put("December", "Desember");
        return monthString.get(month);
    }

    public static String dayEngToId(String day) {
        HashMap<String, String> dayString = new HashMap<>();
        dayString.put("Monday", "Senin");
        dayString.put("Tuesday", "Selasa");
        dayString.put("Wednesday", "Rabu");
        dayString.put("Thursday", "Kamis");
        dayString.put("Friday", "Jumat");
        dayString.put("Saturday", "Sabtu");
        dayString.put("Sunday", "Minggu");
        return dayString.get(day);
    }

    /** Short month name in the app language, e.g. {@code Oct} ("en") or {@code Okt} ("id"). */
    public static String getShortMonthName(int month, String language) {
        String longMonth = getMonthName(month, language);
        return "en".equalsIgnoreCase(language)
                ? monthLongToShortEn(longMonth)
                : monthLongToShortId(longMonth);
    }

    /** XPath expression of {@code attribute} in lowercase, for case-insensitive comparisons. */
    public static String lowerCaseOf(String attribute) {
        return "translate(" + attribute + ",'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')";
    }

    /** Lowercase XPath string literal of {@code value}, to compare against {@link #lowerCaseOf(String)}. */
    public static String lowerCaseLiteral(String value) {
        return "'" + value.toLowerCase() + "'";
    }

    public static String monthLongToShortId(String month) {
        HashMap<String, String> convertMonth = new HashMap<>();
        convertMonth.put("Januari", "Jan");
        convertMonth.put("Februari", "Feb");
        convertMonth.put("Maret", "Mar");
        convertMonth.put("April", "Apr");
        convertMonth.put("Mei", "Mei");
        convertMonth.put("Juni", "Jun");
        convertMonth.put("Juli", "Jul");
        convertMonth.put("Agustus", "Agu");
        convertMonth.put("September", "Sep");
        convertMonth.put("Oktober", "Okt");
        convertMonth.put("November", "Nov");
        convertMonth.put("Desember", "Des");

        return convertMonth.get(month);
    }

    public static String monthLongToShortEn(String month) {
        HashMap<String, String> convertMonth = new HashMap<>();
        convertMonth.put("January", "Jan");
        convertMonth.put("February", "Feb");
        convertMonth.put("March", "Mar");
        convertMonth.put("April", "Apr");
        convertMonth.put("May", "May");
        convertMonth.put("June", "Jun");
        convertMonth.put("July", "Jul");
        convertMonth.put("August", "Aug");
        convertMonth.put("September", "Sep");
        convertMonth.put("October", "Oct");
        convertMonth.put("November", "Nov");
        convertMonth.put("December", "Dec");

        return convertMonth.get(month);
    }

    public static String monthShortToLongId(String month) {
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("Jan", "Januari");
        monthString.put("Feb", "Februari");
        monthString.put("Mar", "Maret");
        monthString.put("Apr", "April");
        monthString.put("Mei", "Mei");
        monthString.put("Jun", "Juni");
        monthString.put("Jul", "Juli");
        monthString.put("Agu", "Agustus");
        monthString.put("Sep", "September");
        monthString.put("Okt", "Oktober");
        monthString.put("Nov", "November");
        monthString.put("Des", "Desember");

        return monthString.get(month);
    }

    public static String monthShortToLongEng(String month) {
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("Jan", "January");
        monthString.put("Feb", "February");
        monthString.put("Mar", "March");
        monthString.put("Apr", "April");
        monthString.put("May", "May");
        monthString.put("Jun", "June");
        monthString.put("Jul", "July");
        monthString.put("Aug", "August");
        monthString.put("Sep", "September");
        monthString.put("Oct", "October");
        monthString.put("Nov", "November");
        monthString.put("Dec", "December");

        return monthString.get(month);
    }

    public static String monthShortToShortEngToId(String month){
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("Jan", "Jan");
        monthString.put("Feb", "Feb");
        monthString.put("Mar", "Mar");
        monthString.put("Apr", "Apr");
        monthString.put("May", "Mei");
        monthString.put("Jun", "Jun");
        monthString.put("Jul", "Jul");
        monthString.put("Aug", "Agu");
        monthString.put("Sep", "Sep");
        monthString.put("Oct", "Okt");
        monthString.put("Nov", "Nov");
        monthString.put("Dec", "Des");
        return monthString.get(month);
    }

    public static String monthShortToShortIdToEng(String month) {
        HashMap<String, String> monthString = new HashMap<>();
        monthString.put("Jan", "Jan");
        monthString.put("Feb", "Feb");
        monthString.put("Mar", "Mar");
        monthString.put("Apr", "Apr");
        monthString.put("Mei", "May");
        monthString.put("Jun", "Jun");
        monthString.put("Jul", "Jul");
        monthString.put("Agu", "Aug");
        monthString.put("Ags", "Aug");
        monthString.put("Agt", "Aug");
        monthString.put("Sep", "Sep");
        monthString.put("Okt", "Oct");
        monthString.put("Nov", "Nov");
        monthString.put("Des", "Dec");
        return monthString.get(month.substring(0, 3));
    }

    public static int monthToMonthNumber(String month) {
        switch (month.substring(0,3)){
            case "Jan" -> {
                return 1;
            }
            case "Feb" -> {
                return 2;
            }
            case "Mar" -> {
                return 3;
            }
            case "Apr"->{
                return 4;
            }
            case "Mei","May" ->{
                return 5;
            }
            case "Jun" ->{
                return 6;
            }
            case "Jul" ->{
                return 7;
            }
            case "Agu","Aug","Ags","Agt" ->{
                return 8;
            }
            case "Sep" ->{
                return 9;
            }
            case "Okt","Oct" ->{
                return 10;
            }
            case "Nov" ->{
                return 11;
            }
            case "Dec","Des"->{
                return 12;
            }
            default -> throw new IllegalStateException("Unexpected value: " + month);
        }
    }

    public static String replaceShortMonth(String date, String containsText) {
        log("Initial date: " + date);
        if (date.contains(containsText)) {
            String shortMonth = date.split(" ")[1].replaceAll(date.split(" ")[1], containsText);
            date = date.replace(date.split(" ")[1], shortMonth);
        }
        log("Replaced date: " + date);
        return date;
    }

    public static String verticalEngToId(String vertical) {
        HashMap<String, String> verticalString = new HashMap<>();
        verticalString.put("Flights", "Pesawat");
        verticalString.put("Trains", "Kereta Api");
        verticalString.put("Car Rentals", "Sewa Mobil");
        verticalString.put("Airport Pickups", "Jemputan Bandara");
        verticalString.put("Car Transfers", "Jemputan Bandara");
        verticalString.put("car transfers", "Jemputan Bandara");
        verticalString.put("Hotels", "Hotel");
        verticalString.put("To Dos", "To Do");
        verticalString.put("Bus & Shuttle","Bus & Travel");
        verticalString.put("Insurance","Asuransi");

        if (verticalString.get(vertical) != null) {
            return verticalString.get(vertical);
        }

        return vertical;
    }

    public static String shorten(String str, int length) {
        if (str == null || str.isBlank() || str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 1) + "...";
    }

    public static String shorten(String str) {
        return shorten(str, 100);
    }

    /**
     * Builds a properly-escaped XPath 1.0 string literal for the given value.
     * <p>
     * XPath 1.0 has no way to escape a quote character inside a string literal,
     * so callers must alternate between single- and double-quoted literals or
     * splice them together with {@code concat(...)}. This helper picks the
     * cheapest representation that survives any combination of apostrophes
     * and double quotes the caller might pass in (e.g. an add-on name like
     * {@code Chef's Special} or a guest name like {@code O"Brien}).
     * <p>
     * Always use this helper when concatenating user-controlled strings into
     * an XPath expression so that the resulting locator stays valid for the
     * full character set the backend can produce.
     *
     * @param value the raw text to embed inside an XPath expression
     * @return an XPath sub-expression that evaluates to {@code value}
     */
    public static String toXPathLiteral(String value) {
        if (value == null) {
            return "''";
        }
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        StringBuilder concat = new StringBuilder("concat(");
        String[] parts = value.split("'", -1);
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                concat.append(", \"'\", ");
            }
            concat.append("'").append(parts[i]).append("'");
        }
        concat.append(")");
        return concat.toString();
    }

    public static String randomName() {
        Faker faker = new Faker();
        return faker.name().firstName()+"test";
    }

    /**
     * Generates a random string of the specified length. If possible, returns a real name using Faker.
     * Falls back to a random alphanumeric string if a real name of the exact length is not available.
     *
     * @param length the length of the random string to generate
     * @return a real name if possible, otherwise a random alphanumeric string
     */
    public static String randomName(int length) {
        if (length >= 3) {
            Faker faker = new Faker();
            String name = faker.name().firstName();
            if (name.length() == length) {
                return name;
            } else if (name.length() > length) {
                return name.substring(0, length);
            } else {
                // Try to append last name if it fits
                String lastName = faker.name().lastName();
                String fullName = name + lastName;
                if (fullName.length() >= length) {
                    return fullName.substring(0, length);
                }
            }
        }
        // Fallback to random alphanumeric string
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String randomNameWithoutTest() {
        Faker faker = new Faker();
        String name = faker.name().firstName();
        if(name.length()<3){
            name += faker.name().firstName();
        }
        return name;
    }

    public static String generateUniqueAlphaNumeric(int length) {
        if (length <= 0) return "";
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder value = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = java.util.concurrent.ThreadLocalRandom.current().nextInt(chars.length());
        }
        return value.toString();
    }

    public static List<String> generateMultipleRandomName(int amount) {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Name name = new Faker().name();
            names.add(name.firstName() + " " + name.lastName());
        }
        return names;
    }

    public static String randomLastName() {
        Faker faker = new Faker();
        return faker.name().lastName().replaceAll("[^A-Za-z]", "");
    }

    public static String randomSalutation() {
        String[] titles = {"MR", "MRS", "MS"};
        Random random = new Random();
        int randomIndex = random.nextInt(titles.length);
        return titles[randomIndex];
    }

    public static String randomEmail() {
        String randomEmail = "";
        if(Configs.ENVIRONMENT == Environment.PROD){
            randomEmail = "AutApps" + CoreFunctions.getCurrentTime() + "@tiket-dummy.com";
        }else {
            randomEmail = "AutApps" + CoreFunctions.getCurrentTime() + "@mailinator.com";
        }
        log("Generated random email -> "+randomEmail);
        return randomEmail;
    }

    public static String randomPhone() {
        return "893"+randomInt(8);
    }

    public static long randomInt(int length) {
        long min = (long) Math.pow(10, length - 1);
        long max = (min * 10) - 1;

        return (long) (Math.random() * (max - min)) + min;
    }

    public static String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Method to give date in format : 2022-10-20 12:00:00
     * @param format the format of the date
     */
    public static String getCurrentDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Method to give date in format : 20 October 2022
     */
    public static String getCurrentLongDate() {
        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String convertToLongDate(String date) {
        String[] formats = {"yyyy-MM-dd", "EEE, dd MMM yyyy"};
        DateFormat longDateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date parsedDate;

        for (String format: formats) {
            DateFormat parser = new SimpleDateFormat(format);

            try {
                parsedDate = parser.parse(date);
                return longDateFormat.format(parsedDate);
            } catch (ParseException ex) {
                log("format " + format + " failed, looking for format for " + date);
            }
        }

        throw new RuntimeException("No matching date format for value: " + date);
    }

    /**
     * Formats a loyalty blackout date range for e-ticket PDF assertions
     * (e.g. {@code 18-19 August 2026} or {@code 31 December 2025 - 2 January 2026}).
     */
    public static String getFullBlackoutDate(String startBlackoutDate, String endBlackoutDate) {
        String blackoutDateFullStart = convertToLongDate(startBlackoutDate);
        String blackoutDateFullEnd = convertToLongDate(endBlackoutDate);
        String[] startParts = blackoutDateFullStart.split(" ");
        String[] endParts = blackoutDateFullEnd.split(" ");
        String dayStart = startParts[0];
        String dayEnd = endParts[0];
        String monthStart = startParts[1];
        String monthEnd = endParts[1];
        String yearStart = startParts[2];
        String yearEnd = endParts[2];
        if (monthStart.equals(monthEnd)) {
            return String.format("%s-%s %s %s", dayStart, dayEnd, monthStart, yearEnd);
        }
        if (!yearStart.equals(yearEnd)) {
            return String.format("%s %s %s - %s %s %s", dayStart, monthStart, yearStart, dayEnd, monthEnd, yearEnd);
        }
        return String.format("%s %s - %s %s %s", dayStart, monthStart, dayEnd, monthEnd, yearEnd);
    }

    public static String addToLongDate(String date, int days) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format!" + date);
        }
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, days);
        Date addedDate = cal.getTime();
        return dateFormat.format(addedDate);
    }

    public static String addDaysToLongCurrentDate(int days) {
        String currentLongDate = CoreFunctions.getCurrentLongDate();
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(currentLongDate);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format!" + currentLongDate);
        }
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, days);
        Date addedDate = cal.getTime();
        return dateFormat.format(addedDate);
    }

    /**
     * Adds a random number of days within the specified range to the current long date
     * and returns the resulting date in "d MMMM yyyy" format.
     *
     * @param randomRange A string representing the range of days to add, formatted as "min-max"
     *                    (e.g., "5-10" to add between 5 and 10 days).
     * @return The new date after adding the random number of days, formatted as "d MMMM yyyy".
     * @throws IllegalArgumentException if the range format is invalid or if minDays is greater than maxDays.
     * @throws RuntimeException if the current long date cannot be parsed.
     *
     * @example
     * <pre>
     * // Assuming today's date is "15 October 2025"
     * String newDate = addToLongDate("5-10");
     * // newDate could be "20 October 2025" (if 5 days were added) or any date up to "25 October 2025" (if 10 days were added)
     * </pre>
     *
     * @since 1.0
     */
    public static String addToLongRandomDate(String randomRange) {
        String currentLongDate = CoreFunctions.getCurrentLongDate(); // e.g., "15 October 2025"

        String[] rangeParts = randomRange.split("-");
        if (rangeParts.length != 2) {
            throw new IllegalArgumentException("Invalid range format! Expected format: min-max, got: " + randomRange);
        }

        int minDays = Integer.parseInt(rangeParts[0].trim());
        int maxDays = Integer.parseInt(rangeParts[1].trim());
        if (minDays > maxDays) {
            throw new IllegalArgumentException("Invalid range: minDays cannot be greater than maxDays");
        }

        int days = generateRandomInt(minDays, maxDays);

        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);

        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(currentLongDate);
        } catch (ParseException e) {
            throw new RuntimeException("Wrong date format! Expected: 'd MMMM yyyy', got: " + currentLongDate);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, days);

        return dateFormat.format(cal.getTime());
    }


    /**
     * Shifts a given date by a specified number of days and returns it in the specified format.
     * This method handles different time duration formats (12-hour and 24-hour) and applies
     * different calculation logic based on the format.
     *
     * @param date The input date string to be shifted
     * @param days The number of days to shift the date (positive for future dates, negative for past dates)
     * @param dateFormat The desired output date format pattern (e.g., "d MMM", "d MMMM yyyy", "yyyy-MM-dd")
     * @param timeDuration12Or24HourFormat Time format indicator:
     *                                    - 12: Uses 12-hour format logic (days-1 calculation)
     *                                    - 24: Uses 24-hour format logic (direct days calculation)
     *
     * @return The shifted date formatted according to the specified dateFormat
     *
     * @throws RuntimeException if the input date cannot be parsed with the provided format or common fallback formats
     *
     * @example
     * <pre>
     * // Shift date by 2 days using 12-hour format
     * shiftDateByDaysWithFormat("12 Oct", 2, "d MMM", 12); // Returns "13 Oct"
     *
     * // Shift date by 2 days using 24-hour format
     * shiftDateByDaysWithFormat("12 Oct", 2, "d MMM", 24); // Returns "14 Oct"
     *
     * // Shift date backwards
     * shiftDateByDaysWithFormat("12 Oct", -2, "d MMM", 24); // Returns "10 Oct"
     *
     * // With full year format
     * shiftDateByDaysWithFormat("31 Dec 2025", 1, "d MMM yyyy", 24); // Returns "1 Jan 2026"
     *
     * // Bahasa Indonesia month names are also supported (output preserves the input language)
     * shiftDateByDaysWithFormat("19 Mei", 2, "d MMM", 24);           // Returns "21 Mei"
     * shiftDateByDaysWithFormat("12 Des 2025", 1, "d MMM yyyy", 24); // Returns "13 Des 2025"
     * </pre>
     *
     * @see SimpleDateFormat for date format patterns
     * @since 1.0
     */
    public static String shiftDateByDaysWithFormat(String date, int days, String dateFormat, int timeDuration12Or24HourFormat) {
        log("Shifting date: " + date + " by days: " + days + " and time format: " + timeDuration12Or24HourFormat + " using format: " + dateFormat);
        Calendar cal = Calendar.getInstance();
        // Try English first, then Bahasa Indonesia, so both "19 May" and "19 Mei" are accepted.
        Locale[] locales = {Locale.ENGLISH, new Locale("id", "ID")};
        DateFormat formatter = null;
        Date parsedDate = null;
        // Tiket's UI uses "Agt"/"Ags" for Agustus (August), but Java's "id" locale only
        // recognizes "Agu". Normalize so parsing succeeds; output is converted back below.
        String dateToParse = date.replaceAll("(?i)\\bAg[ts]\\b", "Agu");

        // Build the list of formats to try: provided format first, then common fallbacks.
        String[] commonFormats = {"d MMM", "d MMMM", "d MMM yyyy", "d MMMM yyyy", "dd/MM/yyyy", "MM/dd/yyyy", "yyyy-MM-dd"};
        List<String> formatsToTry = new ArrayList<>();
        formatsToTry.add(dateFormat);
        for (String f : commonFormats) {
            if (!f.equals(dateFormat)) formatsToTry.add(f);
        }

        outer:
        for (String format : formatsToTry) {
            for (Locale locale : locales) {
                try {
                    DateFormat trial = new SimpleDateFormat(format, locale);
                    parsedDate = trial.parse(dateToParse);
                    // If parsing succeeds, use this format (and matching locale) for output
                    formatter = trial;
                    break outer;
                } catch (ParseException ignored) {
                    // Continue to next locale / format
                }
            }
        }

        if (parsedDate == null || formatter == null) {
            throw new RuntimeException("Unable to parse date: " + date + " with format: " + dateFormat);
        }

        cal.setTime(parsedDate);
        int daysToAdd = (timeDuration12Or24HourFormat == 12) ? (days - 1) : days;
        cal.add(Calendar.DATE, daysToAdd);
        String result = formatter.format(cal.getTime());
        //Restore tiket ui abbreviation for Agustus
        if(result.contains("Agu")){
            result = result.replace("Agu","Agt");
        }
        return result;
    }

    public static String addToLongDate(String date, String randomRange) {
        int days = generateRandomInt(Integer.parseInt(randomRange.split("-")[0]), Integer.parseInt(randomRange.split("-")[1]));
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format!" + date);
        }
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, days);
        Date addedDate = cal.getTime();
        return dateFormat.format(addedDate);
    }

    /**
     * Method to give date in format : 2023-03-02
     */
    public static String getCurrentDateYYYY_MM_DD() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Base URL of the Corporate (B2B admin) dashboard for the currently configured env.
     * Used for headers like {@code referer}/{@code origin} on dashboard API requests so the
     * test doesn't hardcode the GK domain when running on preprod/prod.
     *
     * <p>Delegates to {@link UriHelper}'s 2-arg constructor semantics so that the referer
     * host always matches the request host. The corporate dashboard APIs in this codebase
     * uniformly use {@code new UriHelper(GK, PREPROD)}, which maps {@code PROD -> PREPROD}.
     * Returning {@code corporate.tiket.com} for PROD while the request went to
     * {@code corporate.preprod.tiket.com} would cause a host mismatch and the request would
     * be rejected. Migrate to the 3-arg overload if/when a real PROD corporate URL is wired.
     */
    public static String getCorporateDashboardBaseUrl() {
        return new UriHelper(
                "https://corporate-gatotkaca.tiket.com",
                "https://corporate.preprod.tiket.com"
        ).getUri();
    }

    /**
     * Tiket backend stores order/refund/reschedule dates in Asia/Jakarta.
     * When tests run from another timezone (e.g. IST is UTC+5:30 vs Jakarta UTC+7),
     * the JVM-local "today" can be the previous calendar day in Jakarta and dashboard
     * list queries filtered on {@code startDate}/{@code endDate} will return empty.
     * Use this constant whenever a date string is being sent to a Tiket backend.
     */
    public static final ZoneId JAKARTA_ZONE = ZoneId.of("Asia/Jakarta");

    /**
     * Today's date in Asia/Jakarta as "yyyy-MM-dd".
     */
    public static String getJakartaDateYYYY_MM_DD() {
        return getJakartaDateYYYY_MM_DD(0);
    }

    /**
     * Date in Asia/Jakarta offset by {@code offsetDays} as "yyyy-MM-dd"
     * (negative for past, positive for future).
     */
    public static String getJakartaDateYYYY_MM_DD(int offsetDays) {
        return LocalDate.now(JAKARTA_ZONE)
                .plusDays(offsetDays)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String getCurrentDateYYYYMMDD() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateDD_MM_YYYY() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentDateYYYYMMDDWithSlash() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getTodayDateD_MMMM_YYYY() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        log("Today Date -> "+date.format(formatter));
        return date.format(formatter);
    }

    public static String addToDateYYYY_MM_DD(String date, int days) {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;
        try {
            parsedDate = dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format!" + date);
        }
        cal.setTime(parsedDate);
        cal.add(Calendar.DATE, days);
        Date addedDate = cal.getTime();
        return dateFormat.format(addedDate);
    }

    /**
     * Method to get the number of days to departure date
     * @param dateStr in format yyyy-MM-dd
     * @return number of days to departure date
     */
    public static int getDaysToDepartureDate(String dateStr) {
        LocalDate targetDate;
        if (dateStr.length() == 10) {
            // Format: yyyy-MM-dd
            targetDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } else {
            // Format: yyyy-MM-dd'T'HH:mm:ss'Z'
            targetDate = OffsetDateTime.parse(dateStr, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate();
        }
        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(today, targetDate);
    }

    /**
     * Method to get day name from date, e.g getDayFromDate('4 November 2022','d MMMM yyyy') returns 'Friday'
     */
    public static String getDayFromDate(String date, String dateFormat) {
        DateFormat dateFormatted = new SimpleDateFormat(dateFormat);
        Date parsedDate;
        try {
            parsedDate = dateFormatted.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format! " + date);
        }
        return new SimpleDateFormat("EEEE").format(parsedDate);
    }

    public static String getRandomDateD_MMMM_YYYY(int prevMonth) {
        LocalDate date = LocalDate.now();
        date = date.minusMonths(prevMonth);
        int day = (prevMonth == 0) ? generateRandomInt(1, date.getDayOfMonth()) : generateRandomInt(1, date.lengthOfMonth());
        date = date.withDayOfMonth(day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        log("Random Date -> "+date.format(formatter));
        return date.format(formatter);
    }

    public static String getTodayDayOfMonth() {
        LocalDate today = LocalDate.now();
        String todayDate = String.valueOf(today.getDayOfMonth());
        log("Today day -> "+todayDate);
        return todayDate;
    }
    /**
     * <h4>Convert string date to string date time with a spesific timezone,format
     * and language</h4>
     *
     * @param date     string date
     * @param pattern  date time pattern
     * @param lang     language of the date
     * @return Date time string
     */
    public static String dateTimeToLocaleTimeZone(String date, String lang, String timeZone, String pattern) {
        String Digits = "(\\p{Digit}+)";
        String HexDigits = "(\\p{XDigit}+)";
        String Exp = "[eE][+-]?" + Digits;
        String fpRegex = ("[\\x00-\\x20]*" +
                "[+-]?(" +
                "NaN|" +
                "Infinity|" + "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" + "(\\.("
                + Digits + ")(" + Exp
                + ")?)|" + "((" +

                "(0[xX]" + HexDigits + "(\\.)?)|" + "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")"
                + ")[pP][+-]?"
                + Digits + "))" +
                "[fFdD]?))" +
                "[\\x00-\\x20]*");

        Locale locale;
        if (lang.equalsIgnoreCase("id")) {
            locale = new Locale("id", "ID");
        } else {
            locale = new Locale("en_US");
        }
        DateTimeZone dateTimeZone;
        if (Pattern.matches(fpRegex, timeZone)) {
            double tz = Double.parseDouble(timeZone);
            dateTimeZone = DateTimeZone.forOffsetHours((int) tz);
        } else {
            dateTimeZone = DateTimeZone.forID(timeZone);
        }
        // DateTimeFormat.forPattern("E, dd MMM yyyy - HH:mm").withLocale(locale)
        org.joda.time.format.DateTimeFormatter format = DateTimeFormat.forPattern(pattern).withLocale(locale)
                .withZone(dateTimeZone);
        date = date.replace(" ", "T");
        date = date + "+0000";
        org.joda.time.DateTime dateTime = new org.joda.time.DateTime(date);
        return dateTime.toString(format);
    }

    public static String getSectionOfString(String str, String separator, int index) {
        String[] arr = str.split(separator);
        return arr[index];
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Optional<Integer> getIntFromString(String text) {
        try {
            return Optional.of(Integer.parseInt(text.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Optional<Long> getLongFromString(String text) {
        try {
            return Optional.of(Long.parseLong(text.replaceAll("[^0-9]", "")));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static Integer getOrderId(String text) {
        String preProcess = text.replace("Order", "");
        String preProcess2 = preProcess.replace(":", "");
        String preProcess3 = preProcess2.replace("ID", "");
        String preProcess4 = preProcess3.replace(" ", "");
        return Integer.parseInt(preProcess4);
    }

    public static String convertTitle(String title, String lang) {
        String convertedTitle;
        if (lang.equalsIgnoreCase("id")) {
            convertedTitle = switch (title.toLowerCase()) {
                case "mr", "mr." -> "Tuan";
                case "mrs", "mrs." -> "Nyonya";
                case "ms", "ms." -> "Nona";
                default -> title;
            };
        } else {
            convertedTitle = switch (title.toLowerCase()) {
                case "mr.", "tuan" -> "Mr";
                case "mrs.", "nyonya" -> "Mrs";
                case "ms.", "nona" -> "Ms";
                default -> title;
            };
        }
        return convertedTitle;
    }

    private static boolean isTitle(String title){
        boolean ans= false;
        switch (title.toLowerCase()){
            case "tuan":
            case "mr" :
            case "nyonya":
            case "mrs":
            case "nona":
            case "mr." :
            case "mrs.":
            case "ms":
            case "ms.": ans = true;
        }
        return ans;
    }

    // convert Tuan Jhon Test -> Jhon Test
    public static String removeTitleFromFullName(String name){
        String arr[] = name.trim().split(" ");
        if(isTitle(arr[0])){
            StringBuilder nameBuilder = new StringBuilder();
            for (int i = 1; i<arr.length; i++){
                if (i > 1) {
                    nameBuilder.append(" ");
                }
                nameBuilder.append(arr[i]);
            }
            name = nameBuilder.toString();
        }
        return name;
    }

    //" keep this " -> "keep this"
    public static String removeTrailingOrStaringSpace(String data){
        return data.replaceAll("^\\s+|\\s+$", "");
    }
   
    public static double convertPrice(String price) {
        String preProcess = convertPriceToEng(price);
        return Double.parseDouble(preProcess);
    }

    public static double doubleRound(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static long removeText(String price) {
        String preProcess = price.replaceAll("[^0-9-]","");
        return Long.parseLong(preProcess);
    }

    public static String removeDiscount(String price) {
        return price.replaceAll("^-\\s*", "");
    }

    /**
     * Not safe for calculation, decimal valuewill lose when converting to double. Use {@link #convertPriceSafely(String price)} instead
     * @param price
     * @return double
     */
    public static double convertPriceSafely(String price) {
        String preProcess = price.replaceAll("[^0-9-,.]","");
        if(isStringNumberInEn(preProcess)){
            //english is just the opposite
            preProcess = preProcess.replace(",", "");
        }else{
            //in indonesian it use comma as seperator for decimal and dot as seperator for thousands 
            //eg: 1.0000 -> 1 thousand while 1,000 -> one. So 1.001,01 is one thousand and 1 point zero 1
            preProcess = preProcess.replace(".", "");
            preProcess = preProcess.replace(",", ".");
        }
        // Labels like "NORMAL." / sentence punctuation can leave one or more trailing dots after digit
        // cleanup (e.g. "384436..") which causes Double.parseDouble "multiple points".
        preProcess = preProcess.replaceAll("\\.+$", "");
        return Double.parseDouble(preProcess);
    }

    public static String setNumberAfterComma(Double number) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(number);
    }

    public static String formatRoomPrice(double roomPrice, String language, String currency) {
        Locale locale = language.equalsIgnoreCase("id") ? new Locale("id", "ID") : Locale.US;
        NumberFormat formatter = NumberFormat.getInstance(locale);
        formatter.setMaximumFractionDigits(0);
        return currency + " " + formatter.format(roomPrice);
    }

    public static String convertSquareBracketToRound(String airline){
        String preProcess = airline.replace("[","(");
        String preProcess1 = preProcess.replace("]",")");
        return preProcess1;
    }

    /**
     * Validates if a date string matches a dynamically provided format pattern.
     * * @param input  The date string to validate (e.g., "29 June 2027")
     * @param format The format pattern to check against (e.g., "d MMMM yyyy")
     * @return true if valid and real; false otherwise
     */
    public static boolean isDate(String input, String format) {
        if (input == null || format == null || input.isBlank() || format.isBlank()) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.trim(), Locale.ENGLISH);
            LocalDate.parse(input.trim(), formatter);
            return true;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
    }

    public static String convertDateFormat(String date, String oldFormat, String newFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
        Date dateParse;

        // First attempt to parse using English locale
        try {
            dateParse = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            // If parsing fails, fallback to Indonesian locale
            simpleDateFormat = new SimpleDateFormat(oldFormat, new Locale("id", "ID"));
            try {
                dateParse = simpleDateFormat.parse(date);
            } catch (ParseException e2) {
                throw new RuntimeException("Failed to parse date: " + date, e2);
            }
        }

        // Apply the new format (output in English locale or desired locale)
        simpleDateFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
        return simpleDateFormat.format(dateParse);
    }

        /**
     * Converts a date string in "yyyy-MM-dd" format to "yyyy-MM-dd 00:00:00" format.
     * @param date date string in "yyyy-MM-dd" format
     * @return date string in "yyyy-MM-dd 00:00:00" format
     */
    public static String convertToStartOfDay(String date) {
        return convertDateFormat(date, "yyyy-MM-dd", "yyyy-MM-dd 00:00:00");
    }


    public static String convertDateLang(String date, String dateFormat, String langCode) {
        Date dateParse;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            SimpleDateFormat newFormat = new SimpleDateFormat(dateFormat, Locale.forLanguageTag(langCode));
            dateParse = simpleDateFormat.parse(date);
            String formatted = newFormat.format(dateParse);
            log("Formatted date: "+ formatted);
            return formatted;
        } catch (NullPointerException e) {
            throw new RuntimeException("Given langCode not recognized. "+e.getLocalizedMessage());
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse given date. "+e.getLocalizedMessage());
        }
    }

    public static String titleize(String text) {
        // Check if the text contains any spaces (i.e., it's a multi-word airline name)
        if (text.contains(" ")) {
            String[] arr = text.split(" ");
            StringJoiner sj = new StringJoiner(" ");
            for (String s : arr) {
                String[] letterArray = s.split("");
                letterArray[0] = letterArray[0].toUpperCase(); // Capitalize the first letter
                StringBuilder sb = new StringBuilder();
                for (String value : letterArray) {
                    sb.append(value);
                }
                sj.add(sb.toString());
            }
            return sj.toString();
        } else {
            // If it's a single word, just capitalize the first letter
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
    }

    public static String capitalize(String string) {
        if (string == null || string.isEmpty()) return string;
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public static String thousandSeparator(String input, String separator) {
        String numericString = input.replaceAll("[^0-9]", "");
        BigInteger number = new BigInteger(numericString);
        String format = String.format("%,d",number);
        return format.replace(",", separator);
    }

    public static String summarizePrices(String initialPrice, String currentBalance, String currency) {
        long initial = Long.parseLong(initialPrice.replaceAll("[^0-9]", ""));
        long balance = Long.parseLong(currentBalance.replaceAll("[^0-9-]", ""));
        long sum = initial + balance;
        return String.format(currency + " %,d", sum);
    }

    public static void assertPriceEquals(String message, String actualPriceText, String expectedPriceText) {
        assertPriceEquals(message, convertPrice(actualPriceText), convertPrice(expectedPriceText));
    }

    public static void assertPriceEquals(String message, double actual, double expected) {
        Assertion.assertThat(message, actual, org.hamcrest.core.Is.is(org.hamcrest.Matchers.closeTo(doubleRound(expected), 0.001)));
    }

    /**
     * Returns true when the absolute difference between two price texts is within {@code tolerance}
     * (e.g. tolerance {@code 1} allows {@code 54,675} vs {@code 54,676}).
     */
    public static boolean isPriceWithinTolerance(String actualPriceText, String expectedPriceText, double tolerance) {
        return Math.abs(convertPrice(actualPriceText) - convertPrice(expectedPriceText)) <= tolerance;
    }

    /**
     * Asserts two price texts are equal within an absolute numeric tolerance (e.g. {@code 1} for ±1 rounding).
     */
    public static void assertPriceEqualsWithinTolerance(String message, String actualPriceText, String expectedPriceText, double tolerance) {
        double actual = convertPrice(actualPriceText);
        double expected = convertPrice(expectedPriceText);
        double difference = Math.abs(actual - expected);
        Assertion.assertThat(message + " (actual=" + actual + ", expected=" + expected + ", diff=" + difference + ", tolerance=" + tolerance + ")",
                difference, lessThanOrEqualTo(tolerance));
    }

    private static final Pattern CURRENCY_PRICE_PATTERN = Pattern.compile("[A-Z]{2,4}\\s*[\\d.,]+");

    /**
     * Extracts currency-formatted prices from the given text.
     * <p>
     * Matches patterns like "IDR 123.456", "SGD 158,24", "USD 99.99" (2–4 letter currency code
     * followed by optional whitespace and numeric value with decimal/thousand separators).
     *
     * @param text the text to search for prices (e.g. rate change, snack bar, or cashback message)
     * @return list of matched prices in order of appearance; empty if none found
     */
    public static List<String> extractPrices(String text) {
        List<String> prices = new ArrayList<>();
        if (text == null) {
            return prices;
        }
        Matcher matcher = CURRENCY_PRICE_PATTERN.matcher(text);
        while (matcher.find()) {
            prices.add(matcher.group().trim());
        }
        if (prices.size() >= 2) {
            logger.log("from " + prices.get(0) + " to " + prices.get(prices.size() - 1));
        }
        return prices;
    }

    /**
     * Returns the first currency-prefixed price (e.g. {@code IDR 1,234}) that appears after any
     * of the given {@code markers} in {@code text}.
     *
     * @param text    source text to search
     * @param markers one or more literal substrings; the segment after the first matching marker is scanned for a price
     * @return trimmed price string
     * @throws AssertionError if no marker matches or no price is found after a matched marker
     */
    public static String extractPriceAfterMarkers(String text, String... markers) {
        for (String marker : markers) {
            int index = text.indexOf(marker);
            if (index >= 0) {
                String afterMarker = text.substring(index + marker.length()).trim();
                Matcher matcher = CURRENCY_PRICE_PATTERN.matcher(afterMarker);
                if (matcher.find()) {
                    return matcher.group().trim();
                }
            }
        }
        throw new AssertionError("No price found after any of the given markers in text: " + text);
    }

    public static String normalizeNonIdrPrice(boolean correctSeparator, double price) {
        if (!correctSeparator) price = price / 100;
        double roundPrice = price > 10 ? CoreFunctions.roundPrice(price) : price;
        long result = (long) roundPrice;
        return String.valueOf(result);
    }

    public static double roundPrice(double priceAmount) {
        return priceAmount % 1 < 0.5 ? Math.floor(priceAmount) : Math.ceil(priceAmount);
    }

    public static String roundOffPrice(String priceStr) {
        double value = Double.parseDouble(priceStr);
        long rounded = Math.round(value);
        return String.valueOf(rounded);
    }

    public static String roundPrice(String price) {
        String[] priceStringSplit = price.split(" ");
        String currency = priceStringSplit[0];
        double priceAmount = Double.parseDouble(priceStringSplit[1]);
        return currency + " " + Math.round(priceAmount % 1 < 0.5 ? Math.floor(priceAmount) : Math.ceil(priceAmount));
    }

    public static String[] generateCheckInOutDate(String rangeAddDate, int stayDuration) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] date = rangeAddDate.split("-");
        int startDate = Integer.parseInt(date[0]);
        int endDate = Integer.parseInt(date[1]);
        int addDate = generateRandomInt(startDate, endDate);

        Calendar calendar = Calendar.getInstance();
        LocalDate currentDate = LocalDate.now();

        try {
            calendar.setTime(simpleDateFormat.parse(String.valueOf(currentDate)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.add(Calendar.DATE, addDate);
        String[] dates = new String[2];
        dates[0] = simpleDateFormat.format(calendar.getTime());

        calendar.add(Calendar.DATE, stayDuration);
        dates[1] = simpleDateFormat.format(calendar.getTime());

        return dates;
    }

    /**
     * Splits a delimited list of pairs into an ordered list of two-element arrays,
     * e.g. {@code splitPairs("CGK-DPS|CGK-SUB", "\\|", "-")} yields [[CGK, DPS], [CGK, SUB]].
     */
    public static List<String[]> splitPairs(String value, String listSeparator, String pairSeparator) {
        List<String[]> pairs = new ArrayList<>();
        for (String entry : value.split(listSeparator)) {
            String[] pair = entry.trim().split(pairSeparator);
            if (pair.length != 2) {
                throw new IllegalArgumentException("Expected a '" + pairSeparator + "' separated pair but got: " + entry);
            }
            pairs.add(new String[]{pair[0].trim(), pair[1].trim()});
        }
        return pairs;
    }

    /**
     * Splits a delimited list to exactly {@code size} entries. A single value is repeated for
     * every slot, so {@code expandToSize("Citilink", "\\|", 2)} yields [Citilink, Citilink].
     */
    public static List<String> expandToSize(String value, String separator, int size) {
        String[] values = value.split(separator);
        if (values.length != 1 && values.length != size) {
            throw new IllegalArgumentException("Expected 1 or " + size + " values but got " + values.length + " in: " + value);
        }
        List<String> expanded = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            expanded.add(values[values.length == 1 ? 0 : i].trim());
        }
        return expanded;
    }

    /**
     * Builds {@code count} strictly ascending long-format dates ("d MMMM yyyy"). The first is a
     * random date within {@code rangeAddDate} ("30-60") from today, and each following date adds a
     * random gap within {@code gapRange} ("3-10"), so ordering is always valid for multi-leg trips.
     */
    public static List<String> generateAscendingRandomLongDates(String rangeAddDate, String gapRange, int count) {
        String[] gap = gapRange.split("-");
        List<String> dates = new ArrayList<>();
        String date = generateRandomDate(rangeAddDate, "d MMMM yyyy");
        for (int i = 0; i < count; i++) {
            dates.add(date);
            date = addToLongDate(date, generateRandomInt(Integer.parseInt(gap[0].trim()), Integer.parseInt(gap[1].trim())));
        }
        return dates;
    }

    public static String generateRandomDate(String rangeAddDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] date = rangeAddDate.split("-");
        int startDate = Integer.parseInt(date[0]);
        int endDate = Integer.parseInt(date[1]);
        int addDate = generateRandomInt(startDate, endDate);

        Calendar calendar = Calendar.getInstance();
        LocalDate currentDate = LocalDate.now();

        try {
            calendar.setTime(simpleDateFormat.parse(String.valueOf(currentDate)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.add(Calendar.DATE, addDate);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String generateRandomDate(String rangeAddDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String[] date = rangeAddDate.split("-");
        int startDate = Integer.parseInt(date[0]);
        int endDate = Integer.parseInt(date[1]);
        int addDate = generateRandomInt(startDate, endDate);

        Calendar calendar = Calendar.getInstance();
        LocalDate currentDate = LocalDate.now();

        calendar.set(currentDate.getYear(), currentDate.getMonthValue()-1, currentDate.getDayOfMonth());

        calendar.add(Calendar.DATE, addDate);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Generate random date from a date range separated by _ (underscore)
     *
     * @param rangeMinDate example -15_-20
     * @param format any date format
     */
    public static String generateRandomDateMinus(String rangeMinDate, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-M-d");
        String[] date = rangeMinDate.split("_");
        int startDate = Integer.parseInt(date[0]);
        int endDate = Integer.parseInt(date[1]);
        int minDate = generateRandomInt(startDate, endDate);

        Calendar calendar = Calendar.getInstance();
        LocalDate currentDate = LocalDate.now();

        try {
            calendar.setTime(localDateFormat.parse(String.valueOf(currentDate)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        calendar.add(Calendar.DATE, minDate);
        String generatedDate = simpleDateFormat.format(calendar.getTime());
        log("generated date -> "+generatedDate);
        return generatedDate;
    }

    public static boolean containsAny(String text, String[] tokens) {
        for(String token : tokens) {
            if(text.contains(token)) {
                return true;
            }
        }
        return false;
    }

    public static String removeSuffixIfExists(String key, String suffix) {
        return key.endsWith(suffix)
                ? key.substring(0, key.length() - suffix.length())
                : key;
    }

    public static String processPng(File source, String name) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        long threadId = Thread.currentThread().getId();
        File dir = new File(System.getProperty("user.dir") + "/screenshots/");
        if (!dir.exists()) dir.mkdirs();
        String destination = System.getProperty("user.dir") + "/screenshots/" + name + "_"
                + dateName + "_" + threadId + ".png";
        File finalDestination = new File(destination);
        try {
            FileHandler.copy(source, finalDestination);
        } catch (IOException ioe) {
            throw new DriverToolsException(ioe);
        }
        return destination;
    }

    public static String processPng(byte[] imageBytes, String name, String subfolder) {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        long threadId = Thread.currentThread().getId();
        String dirPath = System.getProperty("user.dir") + "/screenshots/" + subfolder + "/";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        String destination = dirPath + name + "_" + dateName + "_" + threadId + ".png";
        File finalDestination = new File(destination);
        try {
            java.nio.file.Files.write(finalDestination.toPath(), imageBytes);
        } catch (IOException ioe) {
            throw new DriverToolsException(ioe);
        }
        return destination;
    }

    private static final int EXTENT_REPORT_IMAGE_MAX_LONG_EDGE = 1366;

    private static final float EXTENT_REPORT_JPEG_QUALITY = 0.84f;

    /**
     * Encodes a screenshot or image file as a compact JPEG data URI for HTML reports (e.g. Extent).
     * Large images are scaled so the longest edge is at most 1366px; quality is tuned for readable UI
     * text while keeping payload small. Returns null if encoding fails.
     */
    public static String encodeImageFileForExtentReport(File imageFile) {
        if (imageFile == null || !imageFile.isFile()) {
            return null;
        }
        try {
            BufferedImage original = ImageIO.read(imageFile);
            if (original == null) {
                return null;
            }
            BufferedImage rgb = toRgbForJpeg(original);
            BufferedImage scaled = scaleDownLongEdgeIfNeeded(rgb, EXTENT_REPORT_IMAGE_MAX_LONG_EDGE);
            byte[] jpegBytes = writeJpegWithQuality(scaled, EXTENT_REPORT_JPEG_QUALITY);
            if (jpegBytes == null || jpegBytes.length == 0) {
                return null;
            }
            return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(jpegBytes);
        } catch (Exception e) {
            log("encodeImageFileForExtentReport failed: " + e.getMessage());
            return null;
        }
    }

    private static BufferedImage toRgbForJpeg(BufferedImage src) {
        BufferedImage rgb = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = rgb.createGraphics();
        try {
            g.setColor(java.awt.Color.WHITE);
            g.fillRect(0, 0, src.getWidth(), src.getHeight());
            g.drawImage(src, 0, 0, null);
        } finally {
            g.dispose();
        }
        return rgb;
    }

    private static BufferedImage scaleDownLongEdgeIfNeeded(BufferedImage src, int maxLongEdge) {
        int w = src.getWidth();
        int h = src.getHeight();
        int longEdge = Math.max(w, h);
        if (longEdge <= maxLongEdge) {
            return src;
        }
        double scale = (double) maxLongEdge / longEdge;
        int nw = Math.max(1, (int) Math.round(w * scale));
        int nh = Math.max(1, (int) Math.round(h * scale));
        BufferedImage out = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = out.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawImage(src, 0, 0, nw, nh, null);
        } finally {
            g.dispose();
        }
        return out;
    }

    private static byte[] writeJpegWithQuality(BufferedImage image, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            return null;
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
        return baos.toByteArray();
    }

    public static String getLanguage(String language) {
        if (language == null || language.equals("") || language.isBlank() || language.isEmpty()) language = "id";
        return language;
    }

    /*
    sample argument: "2023-05-11", "2023-05-13"
    sample return: 2
     */
    public static int getDaysBetweenDates(String dateStart, String dateEnd) {
        LocalDate ld1 = LocalDate.parse(dateStart);
        LocalDate ld2 = LocalDate.parse(dateEnd);
        long daysBetween = ChronoUnit.DAYS.between(ld1, ld2);
        return (int) daysBetween;
    }

    public static String decodeUrlEncoding(String url) {
        try {
            String prevURL = "";
            String decodeURL = url;
            while (!prevURL.equals(decodeURL)) {
                prevURL = decodeURL;
                decodeURL = decodeURL.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                decodeURL = decodeURL.replaceAll("\\+", "%2B");
                decodeURL = URLDecoder.decode(decodeURL, "UTF-8");
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean randomBoolean() {
        Random random = new Random();
        return random.nextBoolean();
    }
    public static String userAgentValue() {
        String value = "";
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return "tiketcom/android-version";
            }
            case IOS -> {
                return "tiketcom/ios-version";
            }
            case DWEB -> {
                return "tiketcom/dweb-version";
            }
        }
        return value;
    }

    public static int findIndex(String[] arr, String t) {
        int len = arr.length;
        return IntStream.range(0, len)
                .filter(i -> t.equals(arr[i]))
                .findFirst()
                .orElse(-1);
    }

    public static int findIndexContains(String[] arr, String t) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contains(t)) {
                return i;
            }
        }
        return -1;
    }

    public static String[] splitLanguageData(String data, String separator) {
        String id;
        String en;
        if (data.contains(separator)) {
            String[] dataSplit = data.split(separator);
            id = dataSplit[0];
            en = dataSplit[1];
        } else {
            id = data;
            en = data;
        }
        return new String[] {id, en};
    }

    public static String removeUrlProtocol(String url) {
        return url.
                replace("https://www.", "").
                replace("http://www.", "").
                replace("https://m.", "").
                replace("http://m.","");
    }

    public static String generateRandomString(int count) {
        String generatedString = RandomStringUtils.randomAlphabetic(count);
        return generatedString;
    }

    public static String generateRandomAlphanumericsymbol(int count) {
        return RandomStringUtils.randomAscii(count);
    }

    public static String generateRandomNumeric(int count) {
        return RandomStringUtils.randomNumeric(count);
    }

    public static String generateRandomAlphanumeric(int count) {
        return RandomStringUtils.randomAlphanumeric(count);
    }

    public static String updateString(String oldString, String newString) {
        return (!newString.isEmpty()) ? newString : oldString;
    }

    public static int getValueFromPercentage(int totalValue, int percentage) {
        return totalValue * percentage/100;
    }

    public static double sumUpStringNumbers(List<String> numbers){
        double sum = 0;
        for(String number:numbers){
            try {
                double num = convertPrice(number);
                sum += num;
            }catch (NumberFormatException nfe){
                //do nothing skip non numeric string
            }
        }
        return sum;
    }

    public static double sumUpDouble(List<Double> numbers){
        double sum = 0;
        for(Double number:numbers){
            sum += number;
        }
        return sum;
    }
    
    public static String generateRandomBirthDateString(int lowerAgeLimit,int upperAgeLimit, String inputDateRange, DateFormat dateFormat) {
        String[] dateRange = inputDateRange.split("-");
        int lowerLimitDate = Integer.parseInt(dateRange[0]);
        int upperLimitDate = Integer.parseInt(dateRange[1]);
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.YEAR, -generateRandomInt(lowerAgeLimit,upperAgeLimit));
        calendar.add(Calendar.DATE, generateRandomInt(lowerLimitDate,upperLimitDate));
        Date chosenDateByRange = calendar.getTime();
        return dateFormat.format(chosenDateByRange);
    }

    public static String normalizePrice(String price) {
        if(price.contains(".") && price.contains(",")){
            if(price.indexOf(".") < price.indexOf(",")) {
                price = price.replace(".", "");
                price = price.replace(",", ".");
            } else if (price.indexOf(".") > price.indexOf(",")) {
                price = price.replace(",", "");
            }
        }else if(price.contains(".") && !(price.contains(","))) {
            String[] sArray = price.split("\\.");
            for(int i = 1; i < sArray.length; i++) {
                if(sArray[i].length() < 3) {
                    return price;
                }
            }

            price = price.replace(".", "");
            return price;

        }else if(price.contains(",") && !(price.contains("."))) {
            String[] priceArray = price.split(",");

            for(int i = 1; i < priceArray.length; i++) {
                if(priceArray[i].length() < 3) {
                    price = price.replace(",", ".");
                    return price;
                }
            }

            price = price.replace(",", "");
            return price;
        }
        return price;
    }

    public static String convertPriceToEng(String e) {
        String s = e.replaceAll("[^0-9-,.]", "");
        if(s.contains(".") && s.contains(",")){
            if(s.indexOf(".") < s.indexOf(",")) {
                s = s.replace(".", "");
                s = s.replace(",", ".");
            } else if (s.indexOf(".") > s.indexOf(",")) {
                s = s.replace(",", "");
            }
        }else if(s.contains(".") && !(s.contains(","))) {
            String[] sArray = s.split("\\.");
            for(int i = 1; i < sArray.length; i++) {
                if(sArray[i].length() < 3) {
                    return s;
                }
            }

            s = s.replace(".", "");
            return s;

        }else if(s.contains(",") && !(s.contains("."))) {
            String[] sArray = s.split(",");

            for(int i = 1; i < sArray.length; i++) {
                if(sArray[i].length() < 3) {
                    s = s.replace(",", ".");
                    return s;
                }
            }

            s = s.replace(",", "");
            return s;
        }
        return s;
    }

    public static double safeDivide(double numerator, int denominator){
        BigDecimal divided = BigDecimal.valueOf(numerator / denominator);
        divided = divided.setScale(2, RoundingMode.HALF_UP);
        return Double.parseDouble(String.valueOf(divided));
    }

    public static boolean isStringNumberInEn(String number){
        int indexOfDot = number.indexOf(".");
        int indexOfComma = number.indexOf(",");

        // Split with negative limit keeps trailing empty segments — needed so e.g. "457,927." does not yield
        // a single-element split("\\.") and throw on [1] (Java discards trailing empty strings by default).
        if (number.contains(",")) {
            String[] commaSegs = number.split(",", -1);
            if (commaSegs.length > 1 && commaSegs[1].length() == 2) {
                return false;
            }
        }
        if (number.contains(".")) {
            String[] dotSegs = number.split("\\.", -1);
            if (dotSegs.length > 1 && dotSegs[1].length() == 2) {
                return true;
            }
        }

        if(number.contains(".") && number.contains(",") && indexOfDot<indexOfComma) return false;
        else if(number.contains(",") && number.contains(".") && indexOfDot>indexOfComma) return true;
        else if(indexOfDot==-1 && indexOfComma!=-1) {
            //sample: 1,111 -> one thousand and hundered and eleven
            //1,2223 -> not valid in english hence its indonesian, which read as one point two two two three
            //1,2 -> deffinetely in id
            String spllitedNumber[] = number.split(",");
            for(int i = 1; i<spllitedNumber.length;i++){
                if(spllitedNumber[i].length() != 3) return false;
             }
            return true;
        }
        else if(indexOfComma==-1 && indexOfDot!=-1){
            //sample: 1.111 -> one thousand and hundered and eleven in ID
            //1.2223 -> not valid in id hence its en, which read as one point two two two three
            //1.2 -> deffinetely in en
            String spllitedNumber[] = number.split(".");
            for(int i = 1; i<spllitedNumber.length;i++){
                if(spllitedNumber[i].length() != 3) return true;
             }
            return false;
        }
        //if no condition is fulfilled then it can't be determine what is
        //1,2 -> indo valid
        return false;
    }

    public static List<List<String>>  getCSVContent(File FileCSV) {
        List<List<String>> csvContent = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FileCSV.toURI()))) {
            while (scanner.hasNextLine()) {
                csvContent.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  csvContent;
    }

    private static List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }


    private static ArrayList<File> searchDownloadedFileMacOS(String namingConvention){
        String dateStringFileFormat = CoreFunctions.getCurrentDateDD_MM_YYYY();
        Long currentTimeInMillis = System.currentTimeMillis();
        String home = System.getProperty("user.home");
        File downloadsDirectory = new File(home+"/Downloads/");
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : downloadsDirectory.listFiles()) {
            boolean isDownloadedInPast60Seconds = fileEntry.lastModified() > (currentTimeInMillis - 60000);
            boolean isCorrectNamingConvention = fileEntry.getName().contains(namingConvention);
            boolean isNotDirectory = !fileEntry.isDirectory();
            if (isDownloadedInPast60Seconds && isCorrectNamingConvention && isNotDirectory){
                files.add(fileEntry);
            }
        }
        return files;
    }

    public static File getDownloadedFileMacOS(String namingConvention) throws FileNotFoundException, InterruptedException {
        String dateString = CoreFunctions.getCurrentDateTime();
        ArrayList<File> files = searchDownloadedFileMacOS(namingConvention);
        int retryCounter = 0;
        while (files.isEmpty() && retryCounter < 5) {
            Thread.sleep(2000);
            files = searchDownloadedFileMacOS(namingConvention);
            retryCounter++;
        }
        if (files.isEmpty()) throw new FileNotFoundException();
        files.sort(Comparator.comparingLong(File::lastModified).reversed());
        return files.get(0);
    }

    public static  int checkNoOfRowsCSV(List<List<String>> records) throws Exception {
        int lastRecordNumber = Integer.parseInt(records.get(records.size()-1).get(0));
        return lastRecordNumber;
    }

    public static boolean checkCurrencyOnStringList(String currency, List<String> priceTexts) {
        for (String priceText : priceTexts) {
            if (priceText.matches(".*\\d.*")) {
                if (priceText.toLowerCase().contains(currency.toLowerCase())) {
                } else {
                    return false;
                }
            }
        }

        return true;
    }

    public static String removeNonDigit(String price) {
        return price.replaceAll("[^0-9]", "");
    }

    public static String extractCurrency(String price) {
        return price.split(" ")[0].replace("+", "");
    }

    public static String normalizeDateNumber(String date) {
        //change the date from Mon, 1 month 2020 to Mon, 01 month 2020
        String[] dateRawArray = date.split(" ");
        if (dateRawArray[1].length() == 1) dateRawArray[1] = "0" + dateRawArray[1];
        return String.join(" ", dateRawArray);
    }

    public static String getPrice(String priceNumber, PriceType priceType) {
        if (priceType.equals(DECIMAL) && priceNumber.length() == 4) {
            return priceNumber;
        } else if(priceType.equals(THOUSAND) && priceNumber.length() >= 5) {
            return priceNumber;
        } else {
            return "";
        }
    }

    public static boolean verifySeparatorRegex(List<String> prices, String lang, String currency, int decimal){
        String pattern = " [0-9]\\d{0,2}(,\\d{3})*\\.\\d{"+decimal+"}$";
        if(lang.equalsIgnoreCase("IDN") || lang.equalsIgnoreCase("ID")){
            pattern = " [0-9]\\d{0,2}(\\.\\d{3})*,\\d{"+decimal+"}$";
        }

        String regex = "^"+currency+pattern;

        if (decimal == 0) {
            log("original pattern: "+regex);
            if(lang.equalsIgnoreCase("IDN") || lang.equalsIgnoreCase("ID")){
                regex = regex.replace(",\\d{"+decimal+"}", "");
            } else{
                regex = regex.replace("\\.\\d{"+decimal+"}", "");
            }
        }

        for(String price : prices){
            log("Verifying price "+price+" and language "+lang+" with regex "+regex);
            boolean match = price.matches(regex);
            log("Regex match? "+match);
            if(!match){
                return false;
            }
        }
        return true;
    }

    public static void log(String message) {
        final ILogger logger = BaseTest.mainLogger.get();
        if (logger != null) {
            logger.log(message);
        } else {
            // Fallback to System.out when logger is not available (e.g., during static initialization or post-processing)
            System.out.println("[CoreFunctions] " + message);
        }
    }

    /**
     * This method is used to extract data from a JSON string based on a provided JSON path.
     * The JSON path is a string of keys separated by dots, representing the path to the desired data in the JSON structure.
     * For example, if the JSON string is {"key1":{"key2":"value"}}, the JSON path "key1.key2" would return "value".
     *
     * @param response The JSON string from which to extract data.
     * @param jsonPath The JSON path to the desired data.
     * @return The data extracted from the JSON string at the specified JSON path.
     */
    public static String getDataFromJsonString(String response, String jsonPath){
        JSONObject jsonObject = new JSONObject(response);
        String[] keys = jsonPath.split("\\.");
        String expectedData = getNestedValue(jsonObject, keys, 0);
        log("Got Data: " + expectedData);
        return expectedData;
    }

    /**
     * This is a helper method used by getDataFromJsonString to recursively navigate through the JSON structure.
     * It takes a JSONObject, an array of keys representing the JSON path, and an index representing the current depth in the JSON structure.
     * It returns the data at the specified JSON path.
     *
     * @param jsonObject The JSONObject currently being inspected.
     * @param keys The array of keys representing the JSON path.
     * @param index The current depth in the JSON structure.
     * @return The data at the specified JSON path.
     */
    private static String getNestedValue(JSONObject jsonObject, String[] keys, int index) {
        Object value = jsonObject.get(keys[index]);

        if (value instanceof JSONObject) {
            return getNestedValue((JSONObject) value, keys, index + 1);
        } else if (value instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) value;
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object arrayItem = jsonArray.get(i);
                if (arrayItem instanceof JSONObject) {
                    JSONObject jsonItem = (JSONObject) arrayItem;
                    if (jsonItem.has(keys[index + 1])) {
                        return getNestedValue(jsonItem, keys, index + 1);
                    }
                } else if (arrayItem instanceof String) {
                    result.append(arrayItem.toString());
                    if (i < jsonArray.length() - 1) {
                        result.append(", ");
                    }
                }
            }
            return result.toString();
        } else if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Integer) {
            return Integer.toString((Integer) value);
        } else {
            return value.toString();
        }
    }

    /**
     * Finds the index of the highest integer in the provided list.
     *
     * @param numbers the list of integers to search.
     * @return the index of the highest integer in the list.
     * @throws NoSuchElementException if the list is empty.
     */
    public static int findIndexOfHighestInteger(List<Integer> numbers) {
        return numbers.stream()
                .max(Comparator.naturalOrder())
                .map(numbers::indexOf)
                .orElseThrow(() -> new NoSuchElementException("The list is empty"));
    }

    public static String getEndOfCurrentMonth(String datePattern) {
        LocalDate now = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(now.getYear(), now.getMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return yearMonth.atEndOfMonth().format(formatter);
    }

    /**
     * Performs division operation and rounds up the result.
     * It takes two integers as input, performs division, and then rounds up the result to the nearest integer.
     *
     * @param dividend The number to be divided.
     * @param divisor The number by which the dividend is divided.
     * @return The result of the division operation, rounded up to the nearest integer.
     */
    public static int divisionAndRoundUp(int dividend,int divisor){
        return (int) Math.ceil((double) dividend / divisor);
    }

    public static String getFileName(String filePath) {
        Path path = Paths.get(filePath);
        return path.getFileName().toString();

    }
      
    public static int compareDateTime(String departureDateTime, String todayDateTime) {
        String departureDate = departureDateTime.split(" ")[0];

        String todayDate = todayDateTime.split(" ")[0];

        LocalDate departure = LocalDate.parse(departureDate.replace("/", "-"));
        LocalDate today = LocalDate.parse(todayDate.replace("/", "-"));

        if(departure.compareTo(today) == 0) {
            String departureTime = departureDateTime.split(" ")[1];
            String todayTime = todayDateTime.split(" ")[1];

            todayTime = todayTime.split(":")[0] + ":" + todayTime.split(":")[1];

            int departureTimeHour = Integer.parseInt(departureTime.split(":")[0]);
            int departureTimeMinute = Integer.parseInt(departureTime.split(":")[1]);

            int todayTimeHour = Integer.parseInt(todayTime.split(":")[0]);
            int todayTimeMinute = Integer.parseInt(todayTime.split(":")[1]);

            if(departureTimeHour > todayTimeHour) {
            } else if(departureTimeHour == todayTimeHour) {
                if(departureTimeMinute - todayTimeMinute > 15) {
                    return departure.compareTo(today);
                } else {
                    return -1;
                }
            }
        }

        return departure.compareTo(today);
    }

    /**
     * using compareTo to compare date level, not time
     *
     * @param startDate date format follow dateFormat param
     * @param endDate date format follow dateFormat param
     * @param dateFormat param specify format the dates
     */
    public static int compareDate(String startDate, String endDate, String dateFormat) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate start = LocalDate.parse(startDate, format);
            LocalDate end = LocalDate.parse(endDate, format);
            return start.compareTo(end);
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    public static String getPrevMonthYear(String lang, int prevMonth, int prevYear) {
        LocalDate today = LocalDate.now();
        today = today.minusMonths(prevMonth);
        today = today.minusYears(prevYear);
        String todayMonth = getMonthName(today.getMonthValue(), lang);
        String monthYear = todayMonth+" "+String.valueOf(today.getYear());
        log("Today Month Year -> "+monthYear);
        return monthYear;
    }
    
    public static String toCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;

        for (char c : input.toCharArray()) {
            if (c == ' ' || c == '-' || c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(Character.toLowerCase(c));
                }
            }
        }
        return result.toString();
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (!file.delete()) {
            log("Failed to delete file -> "+file.getName());
        }
    }

    public static String getCurrentDate(String format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static String formatNumberBasedOnLanguage(String numberStr, String language) {
        Locale locale;

        // Set the locale based on the language input
        switch (language.toLowerCase()) {
            case "en":
                locale = Locale.US;
                break;
            case "id":
                locale = new Locale("id", "ID"); // Indonesian locale
                break;
            // Add other languages as needed
            default:
                locale = Locale.getDefault(); // Fallback to default locale
        }

        if (numberStr == null) numberStr = "0";
        try {
            // Parse the string number to double (or long based on your input type)
            double number = Double.parseDouble(numberStr);

            // Create a number format instance based on the locale
            NumberFormat numberFormat = NumberFormat.getInstance(locale);

            // Format the number and return it
            return numberFormat.format(number);
        } catch (NumberFormatException e) {
            // Handle the case where the input string is not a valid number
            throw new IllegalArgumentException("Invalid number: " + numberStr, e);
        }
    }
    public static String convertDateFormat(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("d MMMM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate;

        try {
            parsedDate = inputFormat.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException("Wrong date format: " + date);
        }

        return outputFormat.format(parsedDate);
    }

    public static String searchDownloadedFilePath(String namingConvention, String downloadDirectory) {
        File downloadsDirectory = new File(downloadDirectory);
        String filePath = "";
        if (downloadsDirectory.exists() && downloadsDirectory.isDirectory()) {
            File[] files = downloadsDirectory.listFiles();
            if (files != null) {
                for (File fileEntry : files) {
                    boolean isCorrectNamingConvention = fileEntry.getName().contains(namingConvention);
                    boolean isNotDirectory = !fileEntry.isDirectory();

                    log("Checking file: " + fileEntry.getName());
                    if (isCorrectNamingConvention && isNotDirectory) {
                        filePath = fileEntry.getAbsolutePath();
                        log("File matches criteria: " + filePath);
                        return filePath;
                    }
                }
            } else {
                log("No files in directory or cannot read directory.");
            }
        } else {
            log("Directory does not exist or is not a directory: " + downloadDirectory);
        }
        return filePath;
    }

    public static String normalizeString(String input){
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        String normalized = input.replace("All", "").trim();
        if (normalized.endsWith("s")) {
            normalized = normalized.substring(0, normalized.length() - 1).trim();
        }
        return normalized;
    }


    /*
       addToDateInt(31, 1);  // Should return 1
       addToDateInt(1, -1);  // Should return 31
       addToDateInt(15, 10); // Should return 25
       addToDateInt(5, -7);  // Should return 29
     */
    public static int addToDateInt(int day, int daysToAdd) {
        // Assuming a simple calendar with 31 days in a month
        int totalDays = 31;

        // Calculate the new day considering wrapping around
        int newDay = (day + daysToAdd - 1) % totalDays + 1;

        // Handle negative wrapping around
        if (newDay <= 0) {
            newDay += totalDays;
        }

        return newDay;
    }

    public static boolean isMoreThan24Hours(String date) {
        // Define the date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");

        try {
            // Parse the given date
            Date parsedDate = dateFormat.parse(date);

            // Get the current date
            Date currentDate = new Date();

            // Calculate the difference in milliseconds
            long differenceInMillis = Math.abs(parsedDate.getTime() - currentDate.getTime());

            // Convert milliseconds to hours
            long differenceInHours = differenceInMillis / (1000 * 60 * 60);

            // Check if the difference is more than 24 hours
            return differenceInHours > 24;

        } catch (ParseException e) {
            // Handle parsing errors
            throw new IllegalArgumentException("Invalid date format. Please use 'dd MMMM yyyy'.", e);
        }
    }

    public static String getValueFromXPath(Locator locator) {
        return locator.by().toString().replace("By.xpath: ", "").trim();
    }

    public static double parseDecimalNumber(String value) {
        return Double.parseDouble(value.replace(',', '.'));
    }
  
     public static String rgbToHex(String rgb) {
        rgb = rgb.replace("rgb(", "").replace(")", "").replace(" ", "");
        String[] values = rgb.split(",");
        int r = Integer.parseInt(values[0]);
        int g = Integer.parseInt(values[1]);
        int b = Integer.parseInt(values[2]);
        return String.format("#%02X%02X%02X", r, g, b);
    }

    public static List<String> splitText(String text, String separator) {
        return Arrays.asList(text.split(separator));
    }

    public static String getNearestNextWeekend() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return today.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        } else {
            LocalDate nextSaturday = today.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
            return nextSaturday.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        }
    }

    /**
     * Finds the next occurrence of Monday, Wednesday, or Friday from today.
     * If today is one of these days, returns today's date.
     * Otherwise, returns the nearest upcoming Monday, Wednesday, or Friday.
     * 
     * @return Date string in format "d MMMM yyyy" (e.g., "23 January 2026")
     */
    public static String getNextMondayWednesdayOrFriday() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        // If today is Monday, Wednesday, or Friday, return today
        if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
            return today.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
        }
        
        // Find the next Monday, Wednesday, or Friday
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextWednesday = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate nextFriday = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        
        // Return the earliest of the three
        LocalDate nearestDate = nextMonday;
        if (nextWednesday.isBefore(nearestDate)) {
            nearestDate = nextWednesday;
        }
        if (nextFriday.isBefore(nearestDate)) {
            nearestDate = nextFriday;
        }
        
        return nearestDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
    }

    /**
     * Calculates the number of days from today to the next Monday, Wednesday, or Friday.
     * Useful for setting daysToDepartureDate parameter in flight tests.
     * 
     * @return Number of days to the next Monday/Wednesday/Friday (0 if today is one of these days)
     */
    public static int getDaysToNextMondayWednesdayOrFriday() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        
        // If today is Monday, Wednesday, or Friday, return 0
        if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
            return 0;
        }
        
        // Find the next Monday, Wednesday, or Friday
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextWednesday = today.with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        LocalDate nextFriday = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        
        // Return the earliest of the three
        LocalDate nearestDate = nextMonday;
        if (nextWednesday.isBefore(nearestDate)) {
            nearestDate = nextWednesday;
        }
        if (nextFriday.isBefore(nearestDate)) {
            nearestDate = nextFriday;
        }
        
        return (int) java.time.temporal.ChronoUnit.DAYS.between(today, nearestDate);
    }

    /**
     * Finds the next occurrence of Monday, Wednesday, or Friday that is at least n days away from today.
     * 
     * @param n Minimum number of days from today (must be >= 0)
     * @return Date string in format "d MMMM yyyy" (e.g., "23 January 2026")
     */
    public static String getNextMondayWednesdayOrFriday(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(n);
        
        // Start searching from the target date (at least n days away)
        LocalDate searchDate = targetDate;
        
        // Find all MWF dates starting from targetDate, going up to 2 weeks ahead
        for (int i = 0; i < 14; i++) {
            DayOfWeek dayOfWeek = searchDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
                return searchDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
            }
            searchDate = searchDate.plusDays(1);
        }
        
        // Fallback: if not found in 2 weeks, return the next Monday
        return targetDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).format(DateTimeFormatter.ofPattern("d MMMM yyyy"));
    }

    /**
     * Calculates the number of days from today to the next Monday, Wednesday, or Friday 
     * that is at least n days away from today.
     * 
     * @param n Minimum number of days from today (must be >= 0)
     * @return Number of days to the next Monday/Wednesday/Friday (at least n days away)
     */
    public static int getDaysToNextMondayWednesdayOrFriday(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.plusDays(n);
        
        // Start searching from the target date (at least n days away)
        LocalDate searchDate = targetDate;
        
        // Find all MWF dates starting from targetDate, going up to 2 weeks ahead
        for (int i = 0; i < 14; i++) {
            DayOfWeek dayOfWeek = searchDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.MONDAY || dayOfWeek == DayOfWeek.WEDNESDAY || dayOfWeek == DayOfWeek.FRIDAY) {
                return (int) java.time.temporal.ChronoUnit.DAYS.between(today, searchDate);
            }
            searchDate = searchDate.plusDays(1);
        }
        
        // Fallback: if not found in 2 weeks, return days to next Monday
        LocalDate nextMonday = targetDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return (int) java.time.temporal.ChronoUnit.DAYS.between(today, nextMonday);
    }

    public static double convertToThousand(String input) {
        input = input.replace("K", "000").replace("rb", "000");
        input = input.replace(",", ".");
        try {
            if (input.contains(".")) {
                return Double.parseDouble(input) * 1000;
            } else {
                return Double.parseDouble(input);
            }
        } catch (NumberFormatException e) {
            log("Failed to convert input to thousand: " + input);
            throw e;
        }
    }

    /**
     * Compares two dates given in different string formats.
     *
     * <p>This method supports the following date formats:
     * <ul>
     *   <li>"d MMMM yyyy" (e.g., "1 January 2023")</li>
     *   <li>"E, d MMM yyyy" in English locale (e.g., "Mon, 1 Jan 2023")</li>
     *   <li>"E, d MMM yyyy" in Indonesian locale (e.g., "Sen, 1 Jan 2023")</li>
     * </ul>
     *
     * <p>If the second date string does not match the English locale format, it will attempt to parse it using the Indonesian locale format.
     *
     * @param date1 the first date string in the format "d MMMM yyyy"
     * @param date2 the second date string in the format "E, d MMM yyyy" (either in English or Indonesian locale)
     * @return {@code true} if the dates are equal, {@code false} otherwise
     * @throws DateTimeParseException if either date string cannot be parsed
     */
    public static boolean compareDates(String date1, String date2) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d MMMM yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("E, d MMM yyyy", Locale.ENGLISH);
        DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("E, d MMM yyyy", new Locale("id", "ID"));
        LocalDate parsedDate1 = LocalDate.parse(date1, formatter1);
        LocalDate parsedDate2;
        try {
            parsedDate2 = LocalDate.parse(date2, formatter2);
        } catch (DateTimeParseException e) {
            parsedDate2 = LocalDate.parse(date2, formatter3);
        }
        boolean result = parsedDate1.isEqual(parsedDate2);
        log("Comparing dates: " + date1 + " and " + date2 + " -> Result: " + result);
        return result;
    }

    public static int compareMonth(String startDate, String endDate, String dateFormat) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(dateFormat);
        try {
            LocalDate start = LocalDate.parse(startDate, format);
            LocalDate end = LocalDate.parse(endDate, format);

            return ((end.getYear() * 12) + end.getMonthValue()) - ((start.getYear() * 12) + start.getMonthValue());
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * Converts a string of airline codes separated by "-" or "/" into an ArrayList.
     *
     * @param airlineString The string containing airline codes.
     * @return An ArrayList of individual airline codes.
     */
    public static ArrayList<String> makeAirlineArrayList(String airlineString){
        airlineString = airlineString.replaceAll("[\\[\\]]", "").trim();
        // Split using "-" or "/"
        String[] airlines = airlineString.split("[-/]");

        // Convert to ArrayList and trim spaces
        ArrayList<String> airlineList = new ArrayList<>();
        for (String airline : airlines) {
            airlineList.add(airline.trim());
        }
        return airlineList;
    }

    /**
     * Generates a random date within a specified range from the current date.
     *
     * @param daysToDepartureDate The number of days to the departure date.
     * @param range The range in days to add to the departure date.
     * @return A random date as a string in "long" format.
     */
    public static String getRandomLongDateOnRange(int daysToDepartureDate, int range) {
        int randomNumber = generateRandomInt(daysToDepartureDate, daysToDepartureDate + range);
        String currentDate = CoreFunctions.getCurrentLongDate();
        return addToLongDate(currentDate,randomNumber);
    }

   /**
     * Generates a random date within a specified range from the current date.
     *
     * @param daysToDepartureDate The number of days to the departure date.
     * @param range The range in days to add to the departure date.
     * @param dateFormat The format of the date to return ("yyyy-MM-dd" or "long").
     * @return A random date as a string in the specified format.
     */
    public static String getRandomLongDateOnRange(int daysToDepartureDate, int range,String dateFormat) {
        int randomNumber = generateRandomInt(daysToDepartureDate, daysToDepartureDate + range);
        if(dateFormat.equals("yyyy-MM-dd")) {
            String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
            return addToDateYYYY_MM_DD(currentDate,randomNumber);
        } else{
            String currentDate = CoreFunctions.getCurrentLongDate();
            return addToLongDate(currentDate,randomNumber);
        }
    }

    /**
     * Normalizes the access token to ensure it starts with "Bearer".
     *
     * @param token The access token to normalize.
     * @return The normalized token starting with "Bearer".
     */
    public static String normalizeToken(String token) {
        if (!token.contains("Bearer")) {
            return "Bearer " + token;
        }
        return token;
    }

    /**
     * Strips the "Bearer" prefix from an access token, for the endpoints that carry the token in a
     * session cookie instead of an Authorization header.
     *
     * @param token The access token, with or without the prefix.
     * @return The bare access token.
     */
    public static String stripBearerPrefix(String token) {
        if (token == null) {
            return null;
        }
        String trimmed = token.trim();
        if (trimmed.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
            return trimmed.substring("Bearer ".length()).trim();
        }
        return trimmed;
    }

    public static boolean isBrowserAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public static int[] setStayDateRange(String date) {
        String[] stayDate = date.split("-");

        if(stayDate[0].equals("0")){
            throw new DriverToolsException("0 is not a valid start date");
        }

        int startDate = Integer.parseInt(stayDate[0]);
        int endDate = Integer.parseInt(stayDate[1]);

        if (startDate <= 0
                || startDate > 31
                || startDate > endDate
                || endDate > 31
                || endDate <= 0) {
            throw new DriverToolsException("Test data 'date' with value '" + date + "' is not valid, please check again");
        }

        int randomStartDate = generateRandomInt(startDate, endDate);
        int randomEndDate = generateRandomInt(randomStartDate + 1, endDate);
        int[] startAndEndDate = new int[2];
        startAndEndDate[0] = randomStartDate;
        startAndEndDate[1] = randomEndDate;

        return startAndEndDate;

    }

    /**
     * Parses a delimited section enclosed within one of the following brackets: [], (), or {}.
     *
     * <p>The method attempts to extract key-value pairs from the enclosed content by:
     * <ul>
     *   <li>Detecting the block boundaries and extracting the content inside.</li>
     *   <li>Identifying possible delimiters used between keys and values, and between pairs.</li>
     *   <li>Automatically determining the best key-value delimiter by scoring which delimiter
     *       consistently splits the content into key-value pairs.</li>
     *   <li>Identifying the delimiter that separates each key-value pair (pair delimiter).</li>
     *   <li>If key-value delimiters are found, parses the content into a Map of keys and values.</li>
     *   <li>If no key-value delimiter is detected, treats the content as a list of values and
     *             generates keys automatically as "key1", "key2", etc.</li>
     * </ul>
     *
     * <p>Examples:
     * <pre>
     *   Input: "[key1:value1,key2:value2]"
     *   Output: { "key1"="value1", "key2"="value2" }
     *
     *   Input: "[value1/value2/value3]"
     *   Output: { "key1"="value1", "key2"="value2", "key3"="value3" }
     *
     *   Input: "[cgk=dps/sub=sin]"
     *   Output: { "cgk"="dps", "sub"="sin" }
     *
     *   Input: "locationData:[cgk-mdc=Citilink/lop-cgk=Garuda Indonesia/cgk-dps=Garuda Indonesia]"
     *   Output: { "cgk-mdc"="Citilink", "lop-cgk"="Garuda Indonesia", "cgk-dps"="Garuda Indonesia" }
     * </pre>
     *
     * @param input the input string containing the delimited section to parse
     * @return a LinkedHashMap containing the parsed key-value pairs, or automatically generated keys with values;
     *         returns an empty map if no valid block is found or the content is empty
     */
    public static Map<String, String> parseDelimitedSectionToMap(String input) {
        Map<String, String> result = new LinkedHashMap<>();
        if (input == null || input.isEmpty()) return result;

        String content = input.trim();

        // Step 1: Extract content from brackets if present
        int openIndex = -1, closeIndex = -1;
        if ((openIndex = content.indexOf('[')) != -1 && (closeIndex = content.indexOf(']', openIndex)) != -1) {
            content = content.substring(openIndex + 1, closeIndex).trim();
        } else if ((openIndex = content.indexOf('(')) != -1 && (closeIndex = content.indexOf(')', openIndex)) != -1) {
            content = content.substring(openIndex + 1, closeIndex).trim();
        } else if ((openIndex = content.indexOf('{')) != -1 && (closeIndex = content.indexOf('}', openIndex)) != -1) {
            content = content.substring(openIndex + 1, closeIndex).trim();
        }
        if (content.isEmpty()) return result;

        // Step 2: Detect pair delimiter by priority
        String pairDelimiter = ",";
        if (content.contains("/")) pairDelimiter = "/";
        else if (content.contains(";")) pairDelimiter = ";";
        else if (content.contains("|")) pairDelimiter = "|";

        String[] pairs = content.split(Pattern.quote(pairDelimiter));

        // Step 3: Detect key-value delimiter with improved logic to avoid ambiguity
        // Priority: "=" and ":" are checked first as they're most common and unambiguous
        // "->" is checked before single "-" to handle arrow notation
        // Single "-" is only used if no other delimiter is found (to handle cases where keys contain hyphens)
        String keyValueDelimiter = null;
        String[] commonDelims = {"=", ":", "->", "-"};
        for (String delim : commonDelims) {
            // For single hyphen, use more sophisticated detection to avoid false positives
            if (delim.equals("-")) {
                // Count occurrences in each pair - if every pair has exactly one hyphen
                // and no other delimiter was found, treat it as a delimiter
                boolean allPairsHaveOne = Arrays.stream(pairs)
                    .filter(p -> !p.isEmpty())
                    .allMatch(p -> {
                        long count = p.chars().filter(ch -> ch == '-').count();
                        return count == 1;
                    });
                if (allPairsHaveOne && pairs.length > 0) {
                    keyValueDelimiter = delim;
                    break;
                }
            } else {
                // For other delimiters, use simple containment check
                boolean found = Arrays.stream(pairs).anyMatch(p -> p.contains(delim));
                if (found) {
                    keyValueDelimiter = delim;
                    break;
                }
            }
        }

        // Step 4: Parse into map
        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) continue;

            if (keyValueDelimiter != null && pair.contains(keyValueDelimiter)) {
                String[] kv = pair.split(Pattern.quote(keyValueDelimiter), 2);
                result.put(kv[0].trim(), kv.length > 1 ? kv[1].trim() : "");
            } else {
                // Treat as value-only list (key = value)
                result.put(pair, pair);
            }
        }

        return result;
    }


    public static File getLastModified(String directoryFilePath) {
        File directory = new File(directoryFilePath);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if (files != null)
        {
            for (File file : files)
            {
                if (file.lastModified() > lastModifiedTime)
                {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }

    public static File createFile(String directoryPath, String fileName) throws IOException {
        File file = new File(directoryPath + fileName);

        ILogger logger = BaseTest.mainLogger.get();
        if (logger != null) {
            if (file.createNewFile()) {
                logger.log("File '"+fileName+"' has been created");
            } else {
                logger.softWarning("File '"+fileName+"' not created, it's already exist");
            }
        } else {
            // Fallback when logger is not available
            if (file.createNewFile()) {
                System.out.println("[CoreFunctions] File '"+fileName+"' has been created");
            } else {
                System.out.println("[CoreFunctions] File '"+fileName+"' not created, it's already exist");
            }
        }

        return file;
    }

    public static File createCSV(String directoryPath, String fileName, List<String> fileContent) throws IOException {
        File file = CoreFunctions.createFile(directoryPath, fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()))) {
            for (String e : fileContent) {
                writer.write(e + "\n");
            }
        } catch (IOException e) {
            throw new IOException("Getting IOException");
        }

        return file;
    }

    public static <T extends TemporalAccessor> T convertStringToTemporal(
            String dateString, String dateFormat, Class<T> targetType) {

        if (dateString == null || dateString.trim().isEmpty()) {
            throw new IllegalArgumentException("Date string cannot be null or empty.");
        }
        if (dateFormat == null || dateFormat.trim().isEmpty()) {
            throw new IllegalArgumentException("Date format cannot be null or empty.");
        }
        if (targetType == null) {
            throw new IllegalArgumentException("Target type cannot be null.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        try {
            TemporalAccessor temporalAccessor = formatter.parse(dateString);

            if (targetType == LocalDate.class) {
                return targetType.cast(LocalDate.from(temporalAccessor));
            } else if (targetType == LocalDateTime.class) {
                try {
                    return targetType.cast(LocalDateTime.from(temporalAccessor));
                } catch (java.time.DateTimeException e) {
                    return targetType.cast(LocalDate.from(temporalAccessor).atStartOfDay());
                }
            } else {
                throw new IllegalArgumentException("Unsupported target type: " + targetType.getName());
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date string or format. Expected format: '" + dateFormat + "'. Received date: '" + dateString + "'", e);
        }
    }


    public static int getMonthDifference(LocalDate startDate, LocalDate endDate) {
        if (startDate.isEqual(endDate)) {
            return 0;
        }

        int totalMonthsStart = startDate.getYear() * 12 + startDate.getMonthValue();
        int totalMonthsEnd = endDate.getYear() * 12 + endDate.getMonthValue();

        return totalMonthsEnd - totalMonthsStart;
    }

    public static boolean checkLocalDateTimeIsBetween(LocalDateTime targetDate, LocalDateTime startDate, LocalDateTime endDate) {
        if (targetDate == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("All date parameters must not be null.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        return (!targetDate.isBefore(startDate) && !targetDate.isAfter(endDate));
    }

    public static String generateDeviceId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Parses a date string in the format "yyyy-MM-dd" to a LocalDate object.
     */
    public static LocalDate parseDateStringToLocalDate(String date) {
        String[] dateSplit = date.split("-");
        int year = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int day = Integer.parseInt(dateSplit[2]);
        return LocalDate.of(year, month, day);
    }

    public static String convertFullDateToShortDate(String fullDate) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM");

        Date date = inputFormat.parse(fullDate);
        return outputFormat.format(date);
    }

    public static String convertFullDateToShortDateIndonesian(String fullDate) throws ParseException {
        SimpleDateFormat inputFormatId = new SimpleDateFormat("d MMMM yyyy", new java.util.Locale("id", "ID"));
        SimpleDateFormat inputFormatEn = new SimpleDateFormat("d MMMM yyyy", java.util.Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM", new java.util.Locale("id", "ID"));
        Date date;
        try {
            date = inputFormatId.parse(fullDate);
        } catch (ParseException e) {
            date = inputFormatEn.parse(fullDate);
        }
        String formattedDate = outputFormat.format(date);
        if (formattedDate.contains("Agu")) {
            formattedDate = formattedDate.replace("Agu", "Agt");
        }
        return formattedDate;
    }

    public static String normalizeUrl(String url) {
        if (url.startsWith("https://m.")) {
            url = url.replace("https://m.", "https://");
        }
        url = url.replaceAll("&lang=[^&]+", "");
        return url;
    }
    /**
     * Updates or adds a query parameter in the given URL.
     * This method is particularly useful for deeplink manipulation in car rental searches.
     *
     * @param url the original URL to modify
     * @param paramName the name of the query parameter to update/add
     * @param paramValue the new value for the query parameter
     * @return the modified URL with the updated query parameter
     *
     * @example
     * Original URL: https://m.tiket.com/sewa-mobil/cari?totalDays=2&from=Bali
     * updateQueryParameter(url, "totalDays", "3")
     * Result: https://m.tiket.com/sewa-mobil/cari?totalDays=3&from=Bali
     */
    public static String updateQueryParameter(String url, String paramName, String paramValue) {
        if (url == null || url.trim().isEmpty()) {
            log("URL is null or empty, returning original URL");
            return url;
        }

        if (paramName == null || paramName.trim().isEmpty()) {
            log("Parameter name is null or empty, returning original URL");
            return url;
        }

        try {
            // Split URL into base and query parts
            String[] urlParts = url.split("\\?", 2);
            String baseUrl = urlParts[0];
            String queryString = urlParts.length > 1 ? urlParts[1] : "";

            // Handle duration parameter mapping for car rental
            String actualParamName = mapDurationParameter(paramName);

            Map<String, String> queryParams = new HashMap<>();

            // Parse existing query parameters
            if (!queryString.isEmpty()) {
                String[] params = queryString.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length == 2) {
                        queryParams.put(keyValue[0], keyValue[1]);
                    } else if (keyValue.length == 1) {
                        queryParams.put(keyValue[0], "");
                    }
                }
            }

            // Update or add the parameter
            queryParams.put(actualParamName, paramValue);

            // Rebuild query string
            StringBuilder newQueryString = new StringBuilder();
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (newQueryString.length() > 0) {
                    newQueryString.append("&");
                }
                newQueryString.append(entry.getKey());
                if (!entry.getValue().isEmpty()) {
                    newQueryString.append("=").append(entry.getValue());
                }
            }

            // Combine base URL with updated query string
            String updatedUrl = baseUrl + (newQueryString.length() > 0 ? "?" + newQueryString.toString() : "");

            log("Updated URL parameter '" + actualParamName + "' to '" + paramValue + "'");
            log("Original URL: " + url);
            log("Updated URL: " + updatedUrl);

            return updatedUrl;

        } catch (Exception e) {
            log("Error updating query parameter: " + e.getMessage());
            return url;
        }
    }

    /**
     * Extracts and returns the value of a query parameter from a URL.
     * This method is useful for retrieving query parameter values from URLs,
     * particularly for deeplink validation and parameter extraction in test automation.
     *
     * @param url the URL to extract the parameter from
     * @param paramName the name of the query parameter to extract
     * @return the value of the query parameter as a String if found, null otherwise
     *
     * @example
     * URL: https://m.tiket.com/bus-travel/search?origin=123&destination=456&adult=2
     * String adultValue = getQueryParameterValue(url, "adult");
     * // adultValue = "2"
     *
     * @example
     * URL: https://m.tiket.com/sewa-mobil/cari?totalDays=3&from=Bali
     * String daysValue = getQueryParameterValue(url, "totalDays");
     * // daysValue = "3"
     *
     * @example
     * URL: https://m.tiket.com/bus-travel/search?origin=123&adult=1
     * String adultValue = getQueryParameterValue(url, "adult");
     * // adultValue = "1"
     *
     * @example
     * URL: https://m.tiket.com/bus-travel/search?origin=123
     * String adultValue = getQueryParameterValue(url, "adult");
     * // adultValue = null (parameter not found)
     */
    public static String getQueryParameterValue(String url, String paramName) {
        if (url == null || url.trim().isEmpty()) {
            log("URL is null or empty, returning null");
            return null;
        }

        if (paramName == null || paramName.trim().isEmpty()) {
            log("Parameter name is null or empty, returning null");
            return null;
        }

        try {
            // Split URL into base and query parts
            String[] urlParts = url.split("\\?", 2);
            String queryString = urlParts.length > 1 ? urlParts[1] : "";

            // Handle duration parameter mapping for car rental (same as updateQueryParameter)
            String actualParamName = mapDurationParameter(paramName);

            // Parse existing query parameters
            if (!queryString.isEmpty()) {
                String[] params = queryString.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=", 2);
                    if (keyValue.length == 2) {
                        String key = keyValue[0];
                        String value = keyValue[1];
                        
                        // Check if parameter name matches (case-insensitive)
                        if (key.equalsIgnoreCase(actualParamName)) {
                            log("Found query parameter '" + actualParamName + "' with value '" + value + "' in URL");
                            return value;
                        }
                    } else if (keyValue.length == 1) {
                        // Parameter without value (e.g., "param" instead of "param=value")
                        String key = keyValue[0];
                        if (key.equalsIgnoreCase(actualParamName)) {
                            log("Found query parameter '" + actualParamName + "' without value in URL");
                            return "";
                        }
                    }
                }
            }

            log("Query parameter '" + actualParamName + "' not found in URL");
            return null;

        } catch (Exception e) {
            log("Error extracting query parameter: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * @deprecated Use {@link #getQueryParameterValue(String, String)} instead.
     * This method name is misleading as it returns a value, not a boolean.
     */
    @Deprecated
    public static String containsQueryParameter(String url, String paramName) {
        return getQueryParameterValue(url, paramName);
    }

    /**
     * Maps duration parameter name to the correct query parameter used in car rental URLs.
     * For car rental deeplinks, duration is represented as 'totalDays' parameter.
     *
     * @param paramName the input parameter name
     * @return the mapped parameter name for car rental URLs
     */
    private static String mapDurationParameter(String paramName) {
        if ("duration".equalsIgnoreCase(paramName)) {
            return "totalDays";
        }
        return paramName;
    }

    /**
     * Extracts the guest name from a guest detail string like "Room 1: ash/Kamar 1: ash".
     * Returns the substring after the last colon and space.
     *
     * @param guestDetail the guest detail string
     * @return the extracted guest name
     */
    public static String getNameOnGuestDetail(String guestDetail) {
        if (guestDetail == null || !guestDetail.contains(": ")) {
            return guestDetail;
        }
        String[] parts = guestDetail.split(": ");
        return parts[parts.length - 1].trim();
    }

    /**
     * Extracts origin and destination station names from a route info string.
     * Supports both ' to ' and ' ke ' as separators, and strips ' (All)' or similar from station names.
     * @param routeInfoText the route info string (e.g. "Bandung (All) to Jakarta (All)", "Halim ke Padalarang Station")
     * @return String array: [origin, destination]
     */
    public static String[] getOriginAndDestinationFromRouteInfo(String routeInfoText) {
        String[] routeParts = routeInfoText.split("\\s+(to|ke)\\s+");
        String origin = routeParts[0].replaceAll("\\s*\\(.*\\)$", "").trim();
        String destination = routeParts.length > 1 ? routeParts[1].replaceAll("\\s*\\(.*\\)$", "").trim() : "";
        return new String[] { origin.toLowerCase(), destination.toLowerCase() };
    }

    /* Extracts and title-cases the city name from a label text like "All stations in Medan" or "Semua stasiun di Medan"
      Handles both English ("in") and Indonesian ("di") prepositions.
      Throws IllegalArgumentException if no city name is found.
    */
    public static String extractCityName(String labelText) {
        // Normalize the text (remove extra spaces)
        labelText = labelText.trim();

        // Regex: captures word(s) after "in " (English) or "di " (Indonesian)
        Matcher matcher = Pattern.compile("(?i)(?:in|di)\\s+([A-Za-z\\s]+)$").matcher(labelText);

        if (matcher.find()) {
            // Return exactly as it appears in the text
            return matcher.group(1).trim();
        }

        // If no "in" or "di", assume the input is already the city name
        return labelText;
    }

    /**
     * Common timing utility methods
     */
    public static long startTiming(String methodName) {
        // HTML styling for ExtentReports: Blue bold for method name with emoji indicator
        String methodHtml = "<span style='color: #0066CC; font-weight: bold; font-size: 1.1em; background-color: #E6F2FF; padding: 2px 6px; border-radius: 3px;'>" + methodName + "</span>";
        String perfLabel = "<span style='color: #663399; font-weight: bold;'>[PERF]</span>";
        
        String formattedMessage = String.format("%s %s - Starting execution", 
            perfLabel, methodHtml);
        
        log(formattedMessage);
        return System.currentTimeMillis();
    }

    public static void endTiming(String methodName, long startTime) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long minutes = duration / 60000;
        long seconds = (duration % 60000) / 1000;
        long milliseconds = duration % 1000;
        
        // HTML styling for ExtentReports
        String methodHtml = "<span style='color: #0066CC; font-weight: bold; background-color: #E6F2FF; padding: 2px 6px; border-radius: 3px;'>" + methodName + "</span>";
        String perfLabel = "<span style='color: #663399; font-weight: bold;'>[PERF]</span>";
        String timeHtml = "<span style='color: #009900; font-weight: bold; font-size: 1.05em; background-color: #E6FFE6; padding: 2px 6px; border-radius: 3px;'>";
        String timeHtmlEnd = "</span>";
        
        String timeDisplay;
        if (minutes > 0) {
            timeDisplay = minutes + " min " + seconds + " sec";
        } else if (seconds > 0) {
            timeDisplay = seconds + " sec " + milliseconds + " ms";
        } else {
            timeDisplay = milliseconds + " ms";
        }
        
        String formattedMessage = String.format("%s %s - Execution time: %s%s%s", 
            perfLabel, methodHtml, timeHtml, timeDisplay, timeHtmlEnd);
        
        log(formattedMessage);
    }

    public static void endTimingWithResult(String methodName, long startTime, Object result) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long minutes = duration / 60000;
        long seconds = (duration % 60000) / 1000;
        long milliseconds = duration % 1000;
        
        // HTML styling for ExtentReports
        String methodHtml = "<span style='color: #0066CC; font-weight: bold; background-color: #E6F2FF; padding: 2px 6px; border-radius: 3px;'>" + methodName + "</span>";
        String perfLabel = "<span style='color: #663399; font-weight: bold;'>[PERF]</span>";
        String timeHtml = "<span style='color: #009900; font-weight: bold; background-color: #E6FFE6; padding: 2px 6px; border-radius: 3px;'>";
        String timeHtmlEnd = "</span>";
        String resultHtml = "<span style='color: #663399; font-style: italic; background-color: #F0E6FF; padding: 2px 6px; border-radius: 3px;'>" + result + "</span>";
        
        String timeDisplay;
        if (minutes > 0) {
            timeDisplay = minutes + " min " + seconds + " sec";
        } else if (seconds > 0) {
            timeDisplay = seconds + " sec " + milliseconds + " ms";
        } else {
            timeDisplay = milliseconds + " ms";
        }
        
        String formattedMessage = String.format("%s %s - Execution time: %s%s%s - Result: %s", 
            perfLabel, methodHtml, timeHtml, timeDisplay, timeHtmlEnd, resultHtml);
        
        log(formattedMessage);
    }

    public static void endTimingWithSuccess(String methodName, long startTime, boolean success) {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long minutes = duration / 60000;
        long seconds = (duration % 60000) / 1000;
        long milliseconds = duration % 1000;
        
        // HTML styling for ExtentReports
        String methodHtml = "<span style='color: #0066CC; font-weight: bold; background-color: #E6F2FF; padding: 2px 6px; border-radius: 3px;'>" + methodName + "</span>";
        String perfLabel = "<span style='color: #663399; font-weight: bold;'>[PERF]</span>";
        String timeHtml = "<span style='color: #009900; font-weight: bold; background-color: #E6FFE6; padding: 2px 6px; border-radius: 3px;'>";
        String timeHtmlEnd = "</span>";
        String successColor = success ? "#00AA00" : "#CC0000";
        String successBgColor = success ? "#E6FFE6" : "#FFE6E6";
        String successText = success ? "SUCCESS" : "FAILED";
        String successHtml = String.format("<span style='color: %s; font-weight: bold; background-color: %s; padding: 2px 6px; border-radius: 3px;'>%s</span>", 
            successColor, successBgColor, successText);
        
        String timeDisplay;
        if (minutes > 0) {
            timeDisplay = minutes + " min " + seconds + " sec";
        } else if (seconds > 0) {
            timeDisplay = seconds + " sec " + milliseconds + " ms";
        } else {
            timeDisplay = milliseconds + " ms";
        }
        
        String formattedMessage = String.format("%s %s - Execution time: %s%s%s - Success: %s", 
            perfLabel, methodHtml, timeHtml, timeDisplay, timeHtmlEnd, successHtml);
        
        log(formattedMessage);
    }

    /**
     * Logs the total test execution time from TestNG ITestResult
     * This helps identify the difference between total test time and sum of individual step timings
     */
    public static void logTotalTestTime(long durationMillis) {
        long minutes = durationMillis / 60000;
        long seconds = (durationMillis % 60000) / 1000;
        long milliseconds = durationMillis % 1000;
        
        String perfLabel = "<span style='color: #663399; font-weight: bold;'>[PERF]</span>";
        String timeHtml = "<span style='color: #0066CC; font-weight: bold; font-size: 1.1em; background-color: #E6F2FF; padding: 2px 6px; border-radius: 3px;'>";
        String timeHtmlEnd = "</span>";
        
        String timeDisplay;
        if (minutes > 0) {
            timeDisplay = minutes + " min " + seconds + " sec " + milliseconds + " ms";
        } else if (seconds > 0) {
            timeDisplay = seconds + " sec " + milliseconds + " ms";
        } else {
            timeDisplay = milliseconds + " ms";
        }
        
        String formattedMessage = String.format("%s <span style='color: #0066CC; font-weight: bold;'>Total Test Execution Time</span>: %s%s%s", 
            perfLabel, timeHtml, timeDisplay, timeHtmlEnd);
        
        log(formattedMessage);
    }

    public static String getProjectName(){
        String projectName =  Configs.PROPERTIES_DATA.get("vertical") != null ? Configs.PROPERTIES_DATA.get("vertical"):"Default" ;
        String env = switch (Configs.ENVIRONMENT) {
            case PROD -> "PROD";
            case PREPROD -> "PREPROD";
            case GK -> "GK";
        };

        String platform = Configs.PLATFORM.name();
        String rawPlatform = System.getProperty("platform");

        if (platform.toLowerCase().contains("web") && !rawPlatform.equalsIgnoreCase("panel")) {
            projectName = String.format("WEB %s %s %s", projectName, env, platform);
        } else if(rawPlatform.equalsIgnoreCase("panel")) {
            projectName = String.format("PANEL %s %s", projectName, env);
        }else{
            projectName = String.format("APP %s %s %s", projectName, env, platform);
        }
        return projectName;
    }

    /**
     * Retrieves Klov server and MongoDB configuration data based on the current platform,
     * vertical, and environment settings.
     *
     * <p>This method determines the appropriate configuration by following these rules:
     * <ul>
     *   <li>If the vertical is "Monitoring" OR the platform contains "web",
     *       it uses Web/Panel (Dweb) configuration</li>
     *   <li>Otherwise, it uses App configuration</li>
     * </ul>
     *
     * <p>The configuration properties are selected based on the environment:
     * <ul>
     *   <li>PREPROD/PROD environments → uses "PreProd" configuration</li>
     *   <li>GK environment → uses "GK" configuration</li>
     *   <li>Other environments → not supported, returns null</li>
     * </ul>
     *
     * <p>Example property keys resolved:
     * <ul>
     *   <li>Web/PreProd: klovServerDwebPreProd, klovMongoDbDwebPreProd, klovMongoDbPortDwebPreProd</li>
     *   <li>App/GK: klovServerAppGK, klovMongoDbAppGK, klovMongoDbPortAppGK</li>
     * </ul>
     *
     * @return A map containing the following keys:
     *         <ul>
     *           <li>"klovServer" - The Klov server URL</li>
     *           <li>"mongoHost" - The MongoDB host address</li>
     *           <li>"mongoPort" - The MongoDB port number as a string</li>
     *           <li>"mongoUri" - The complete MongoDB connection URI</li>
     *         </ul>
     *         Returns {@code null} if the environment is not supported for the determined config type.
     *
     * @see Configs#PLATFORM
     * @see Configs#ENVIRONMENT
     * @see Configs#PROPERTIES_DATA
     */
    public static Map<String, String> getKlovAndMongoData() {
        String platform = Configs.PLATFORM.name();
        String vertical = Configs.PROPERTIES_DATA.get("vertical");

        // Determine config type: use Web config for Monitoring vertical OR web/panel platforms
        boolean isMonitoring = vertical != null && vertical.equalsIgnoreCase("Monitoring");
        boolean isCritical = vertical != null && vertical.equalsIgnoreCase("CriticalPathTests");
        boolean isWebOrPanel = platform.toLowerCase().contains("web");
        boolean useWebConfig = isMonitoring || isWebOrPanel || isCritical;

        // Determine config key suffix based on platform type
        String configType = useWebConfig ? "Dweb" : "App";

        // Determine environment suffix
        String envSuffix;
        switch (Configs.ENVIRONMENT) {
            case PREPROD, PROD -> envSuffix = "PreProd";
            case GK -> envSuffix = "GK";
            default -> {
                safeLog("Environment not supported for " + configType + ": " + Configs.ENVIRONMENT);
                return null;
            }
        }

        if (isMonitoring || isCritical) {
            safeLog("Using Web/Panel Klov config for Monitoring or Critical vertical");
        }

        // Build property keys dynamically
        String klovServer = Configs.CONFIG.getProperty("klovServer" + configType + envSuffix);
        String mongoHost = Configs.CONFIG.getProperty("klovMongoDb" + configType + envSuffix);
        int mongoPort = Integer.parseInt(Configs.CONFIG.getProperty("klovMongoDbPort" + configType + envSuffix));

        // Create and return the map with configuration data
        Map<String, String> configData = new HashMap<>();
        configData.put("klovServer", klovServer);
        configData.put("mongoHost", mongoHost);
        configData.put("mongoPort", String.valueOf(mongoPort));
        configData.put("mongoUri", "mongodb://" + mongoHost + ":" + mongoPort);

        return configData;
    }

    /**
     * Safe logging helper that handles null logger gracefully
     * Used during static initialization when logger may not be available
     */
    private static void safeLog(String message) {
        try {
            //log(message);
        } catch (Exception e) {
            // Silent fail - don't break initialization
            System.out.println("[CoreFunctions] " + message);
        }
    }

    /**
     * Builds a nested map structure for Grafana panel IDs from parameter map.
     * Extracts panel IDs for different platforms (Dweb, Android, iOS) and metric types
     * (ExecutionStatus, ExecutionTime, Screens).
     *
     * @param param Map containing panel ID parameters with keys like "DwebExecutionStatus", "AndroidExecutionTime", etc.
     * @return Nested map structure: Map<Platform, Map<MetricType, PanelId>>
     *
     * @example
     * <pre>
     * Map<String, String> param = new HashMap<>();
     * param.put("DwebExecutionStatus", "123");
     * param.put("DwebExecutionTime", "456");
     * // ... more params
     * Map<String, Map<String, Integer>> panelIds = buildGrafanaPanelIds(param);
     * Integer panelId = panelIds.get("Dweb").get("ExecutionStatus"); // Returns 123
     * </pre>
     */
    public static Map<String, Map<String, Integer>> buildGrafanaPanelIds(Map<String, String> param) {
        Map<String, Map<String, Integer>> panelIds = new HashMap<>();

        // Dweb panel IDs
        Map<String, Integer> dwebPanels = new HashMap<>();
        dwebPanels.put("ExecutionStatus", Integer.parseInt(param.get("DwebExecutionStatus")));
        dwebPanels.put("ExecutionTime", Integer.parseInt(param.get("DwebExecutionTime")));
        dwebPanels.put("Screens", Integer.parseInt(param.get("DwebScreens")));
        panelIds.put("Dweb", dwebPanels);

        // Android panel IDs
        Map<String, Integer> androidPanels = new HashMap<>();
        androidPanels.put("ExecutionStatus", Integer.parseInt(param.get("AndroidExecutionStatus")));
        androidPanels.put("ExecutionTime", Integer.parseInt(param.get("AndroidExecutionTime")));
        androidPanels.put("Screens", Integer.parseInt(param.get("AndroidScreens")));
        panelIds.put("Android", androidPanels);

        // iOS panel IDs
        Map<String, Integer> iosPanels = new HashMap<>();
        iosPanels.put("ExecutionStatus", Integer.parseInt(param.get("iOSExecutionStatus")));
        iosPanels.put("ExecutionTime", Integer.parseInt(param.get("iOSExecutionTime")));
        iosPanels.put("Screens", Integer.parseInt(param.get("iOSScreens")));
        panelIds.put("iOS", iosPanels);

        log("Built Grafana panel IDs map for 3 platforms with 3 metrics each");
        return panelIds;
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    /**
     * Converts a hex color (e.g. {@code "#30F639"} or {@code "30F639"}) to the CSS rgb form
     * used in vendor indicator {@code style} attributes (e.g. {@code " rgb(48, 246, 57)"}).
     */
    public static String hexColorToRgbCss(String hexColor) {
        String hex = hexColor.trim();
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex color: " + hexColor);
        }
        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);
        return String.format(" rgb(%d, %d, %d)", red, green, blue);
    }

    /**
     * Parses table Long Lat values such as {@code "110.368185, -7.835805"} (longitude, latitude).
     *
     * @return {@code [latitude, longitude]}
     */
    public static double[] parseTableLongLat(String longLat) {
        if (longLat == null || longLat.isBlank()) {
            throw new IllegalArgumentException("Long Lat value is empty");
        }
        String[] parts = longLat.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Long Lat format: " + longLat);
        }
        double longitude = Double.parseDouble(parts[0].trim());
        double latitude = Double.parseDouble(parts[1].trim());
        return new double[]{latitude, longitude};
    }

    public static double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        final double earthRadiusMeters = 6_371_000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusMeters * c;
    }

    // Convert Server time into this format: 2026-07-01 09:45:10
    public static String convertServerTimeToUTC(long serverTimeMilli) {
        return getUtcZonedDateTime(serverTimeMilli).format(SERVER_TIME_FORMATTER);
    }

    // Convert Server time into this format: "Wednesday, Jul 1, 2026, 9:45 AM UTC+0"
    public static String convertServerTimeToUTCFormat(long serverTimeMilli) {
        return getUtcZonedDateTime(serverTimeMilli).format(SERVER_TIME_UI_FORMATTER);
    }

    /**
     * Decodes a JWT (optionally prefixed with {@code "Bearer "}) and returns its payload claims so any
     * API/page can read custom claims (e.g. {@code accountid}, {@code email}) without re-implementing
     * the base64url decoding.
     *
     * @param token the JWT access token; must not be null or blank
     * @return the decoded payload as a {@link JSONObject}
     * @throws IllegalArgumentException if the token is null/blank or is not a well-formed JWT
     */
    public static JSONObject decodeJwtPayload(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("JWT access token must not be null or blank.");
        }
        String raw = token.startsWith("Bearer ") ? token.substring(7) : token;
        String[] parts = raw.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT access token");
        }
        String payload = new String(Base64.getUrlDecoder().decode(padBase64(parts[1])), StandardCharsets.UTF_8);
        return new JSONObject(payload);
    }

    /** Right-pads a base64url segment to a multiple of 4 chars so {@link Base64.Decoder} can decode it. */
    private static String padBase64(String value) {
        int remainder = value.length() % 4;
        return remainder == 0 ? value : value + "====".substring(remainder);
    }

    /** Strips currency/formatting and returns the numeric value of a price string (e.g. "IDR 1,323,108" -> 1323108). */
    public static long parsePriceToNumber(String price) {
        String digits = price == null ? "" : price.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0L : Long.parseLong(digits);
    }

    /**
     * Asserts the displayed prices strictly increase across the room list, booking form and payment page
     * (room list &lt; booking form &lt; payment), reflecting the rate increases applied via the allotment API.
     */
    public static void assertPriceIncreasing(String label, String roomListPrice, String bookingFormPrice, String paymentPrice) {
        long roomList = parsePriceToNumber(roomListPrice);
        long bookingForm = parsePriceToNumber(bookingFormPrice);
        long payment = parsePriceToNumber(paymentPrice);
        Assertion.assertThat(label + ": room list price (" + roomList + ") should be less than booking form price (" + bookingForm + ")",
            roomList, lessThan(bookingForm));
        Assertion.assertThat(label + ": booking form price (" + bookingForm + ") should be less than payment page price (" + payment + ")",
            bookingForm, lessThan(payment));
    }
}
