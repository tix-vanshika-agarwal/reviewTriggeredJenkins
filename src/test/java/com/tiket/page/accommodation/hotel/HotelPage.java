package com.tiket.page.accommodation.hotel;

import com.tiket.apiPage.HotelApi;
import com.tiket.model.Deeplink;
import com.tiket.model.Session;
import com.tiket.page.accommodation.hotel.detailpage.DetailPage;
import com.tiket.pojo.accommodation.hotel.*;
import com.tiket.pojo.accommodation.hotel.payathotel.PayAtHotelRoom;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.*;

import static com.tiket.core.CoreFunctions.*;
import static org.hamcrest.CoreMatchers.is;

public abstract class HotelPage <T extends WebDriver> extends BasePage<T> {

    public HotelPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> HotelPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {return new HotelPageIOS<>(session);}
            case ANDROID -> {return new HotelPageAndroid<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    @SneakyThrows
    public String expectedDateRoomGuest(String language) {
        String checkIn = getMonth("1-1", language);
        String checkOut = getMonth("2-2", language);
        String room = "1 Kamar";
        String guest = "1 Tamu";
        if (getLanguage(language).equals("en")) {
            room = "1 Room";
            guest = "1 Guest";
        }
        logger.log("expectedDateRoomGuest-> " + checkIn + " - " + checkOut + " • " + room + " • " + guest);
        return checkIn + " - " + checkOut + " • " + room + " • " + guest;
    }

    private String getMonth(String rangeAddDate, String language) {
        String date = generateRandomDate(rangeAddDate, "yyyy-M-d");
        String[] split = date.split("-");
        int month = Integer.parseInt(split[1]);
        String monthName = getMonthName(month, getLanguage(language));
        if (monthName.length() > 3) monthName = monthName.substring(0, 3);
        return split[2] + " " + monthName;
    }

    @SneakyThrows
    public void openSRPDeeplink(String accommodationType, int room, int adult, String locationId, String locationType, String locationName, String checkIn, String checkOut, String language) {
        String pathParams = accommodationType.toLowerCase() + "/search?room="+room+"&adult="+adult+"&id="+locationId.toLowerCase()+"&type="+locationType.toUpperCase()+"&q="+locationName+"&checkin="+checkIn+"&checkout="+checkOut+"&lang="+language.toLowerCase();
        Deeplink srpDeeplink = new Deeplink("SRP",
                "tiket://m.gatotkaca.tiket.com/" + pathParams,
                "tiket://m.preprod.tiket.com/" + pathParams,
                "tiket://m.tiket.com/" + pathParams);
        driverTools.deeplink(srpDeeplink);
        driverTools.switchToNative();
    }

    @SneakyThrows
    public void openSRPDeeplink(String accommodationType, int room, int adult, String locationId, String locationType, String locationName, String checkIn, String checkOut, String language, boolean isRedirect) {
        String pathParams = accommodationType.toLowerCase() + "/search?room="+room+"&adult="+adult+"&id="+locationId.toLowerCase()+"&type="+locationType.toUpperCase()+"&q="+locationName.replace(' ', '+')+"&checkin="+checkIn+"&checkout="+checkOut+"&lang="+language.toLowerCase()+"&isRedirect="+isRedirect;
        Deeplink srpDeeplink = new Deeplink("SRP",
                "tiket://m.gatotkaca.tiket.com/" + pathParams,
                "tiket://m.preprod.tiket.com/" + pathParams,
                "tiket://m.tiket.com/" + pathParams);
        driverTools.deeplink(srpDeeplink);
        driverTools.switchToNative();
    }

    @SneakyThrows
    public void openPDPDeeplink(String accommodationType, String country, String hotelId, int room, int adult, String checkIn, String checkOut) {
        if (accommodationType.equalsIgnoreCase("nha")) accommodationType = "homes";
        String pathParams = accommodationType.toLowerCase()+"/" + country.toLowerCase() + "/"+hotelId.toLowerCase()+"?room="+room+"&adult="+adult+"&checkin="+checkIn+"&checkout="+checkOut;
        Deeplink pdpDeeplink = new Deeplink("PDP",
                "tiket://m.gatotkaca.tiket.com/" + pathParams,
                "tiket://m.preprod.tiket.com/" + pathParams,
                "tiket://m.tiket.com/" + pathParams);
        driverTools.deeplink(pdpDeeplink);
        driverTools.switchToNative();
    }

    @SneakyThrows
    public void openPDPDeeplink(String accommodationType, String country, String hotelId, int room, int adult, String checkIn, String checkOut, int childAge) {
        if (childAge == 0) {
            logger.log("childAge is 0, opening PDP deeplink without child age");
            openPDPDeeplink(accommodationType, country, hotelId, room, adult, checkIn, checkOut);
        } else {
            logger.log("childAge is " + childAge + ", opening PDP deeplink with child age");
            openPDPDeeplinkWithChild(accommodationType, country, hotelId, room, adult, checkIn, checkOut, childAge);
        }
    }

    private void openPDPDeeplinkWithChild(String accommodationType, String country, String hotelId, int room, int adult, String checkIn, String checkOut, int childAge) {
        if (accommodationType.equalsIgnoreCase("nha")) accommodationType = "homes";
        String pathParams = accommodationType.toLowerCase()+"/" + country.toLowerCase() + "/"+hotelId.toLowerCase()+"?room="+room+"&adult="+adult+"&child="+childAge+"&checkin="+checkIn+"&checkout="+checkOut;
        Deeplink pdpDeeplink = new Deeplink("PDP",
                "tiket://m.gatotkaca.tiket.com/" + pathParams,
                "tiket://m.preprod.tiket.com/" + pathParams,
                "tiket://m.tiket.com/" + pathParams);
        driverTools.deeplink(pdpDeeplink);
        driverTools.switchToNative();
    }

    /**
     * GK / preprod helper for Pay at Hotel tests.
     * <p>
     * Generates the {@code === PAH Hotel Summary ===} via {@link HotelApi#findPayAtHotelRoom}, then
     * iterates the returned PDP candidates — opens each one by deeplink and checks
     * {@link DetailPage#isRoomAvailable()} — and stops on the first PDP that shows at least
     * one bookable room.
     *
     * @param dateRange     day-offset range passed to PAH finder, e.g. {@code "1-5"}.
     * @param totalNight    nights used for the check-out date on each deeplink ({@code checkIn + totalNight}).
     * @param totalRoom     rooms to pass into both the PAH finder and the deeplink query string.
     * @param totalAdult    adults to pass into both the PAH finder and the deeplink query string.
     * @return the {@link PayAtHotelRoom} that produced a PDP with an available room,
     *         or {@code null} if every candidate URL opened a PDP with no available room,
     *         or if the summary returned no candidates.
     */
    @SneakyThrows
    public PayAtHotelRoom openFirstAvailablePayAtHotelPdpDeeplink(
            String dateRange, int totalNight, int totalRoom, int totalAdult) {
        HotelApi<T> hotelApi = new HotelApi<>(session);
        DetailPage<T> detailPage = DetailPage.getInstance(session);

        List<PayAtHotelRoom> candidates = hotelApi.findPayAtHotelRoom(
                dateRange, String.valueOf(totalNight),
                String.valueOf(totalRoom), String.valueOf(totalAdult));
        if (candidates.isEmpty()) {
            logger.log("[PAH] findPayAtHotelRoom returned 0 candidates — nothing to iterate.");
            return null;
        }

        for (PayAtHotelRoom candidate : candidates) {
            String checkIn = candidate.getEarliestPahDate();
            String checkOut = java.time.LocalDate.parse(checkIn).plusDays(totalNight).toString();

            logger.log("[PAH] Opening PDP deeplink for " + candidate.vendorExternalIdLabel()
                    + " (" + candidate.getHotelName() + ") " + checkIn + " -> " + checkOut);
            openPDPDeeplink(
                    candidate.getAccommodationType(),
                    candidate.getCountryId(),
                    candidate.getPublicId(),
                    totalRoom, totalAdult, checkIn, checkOut);

            if (detailPage.isRoomAvailable()) {
                logger.log("[PAH] Found available PDP -> " + candidate.vendorExternalIdLabel()
                        + " / " + candidate.getHotelName());
                return candidate;
            }
            logger.log("[PAH] No rooms on PDP for " + candidate.vendorExternalIdLabel()
                    + " - trying next candidate");
        }
        logger.log("[PAH] Iterated " + candidates.size() + " candidate(s); none showed an available room.");
        return null;
    }

    @SneakyThrows
    public void openPDPDeeplink(List<String> hotelList, String username, String password, String accommodationType) {
        HotelApi<?> hotelApi = new HotelApi<>(session);
        HotelPage<T> hotelPage = HotelPage.getInstance(session);
        DetailPage<T> detailPage = DetailPage.getInstance(session);
        String[] dates = generateCheckInOutDate("1-2", 1);
        String checkInDate = dates[0];
        String checkOutDate = dates[1];

        for (String hotel : hotelList) {
            HotelAutoCompleteResponse hotelAutoCompleteResponse = hotelApi.getAutocompleteData(username, password, hotel, accommodationType.toUpperCase());
            String hotelId = hotelAutoCompleteResponse.data.contents.get(0).publicId;
            String country = hotelAutoCompleteResponse.data.contents.get(0).country.name;
            hotelPage.openPDPDeeplink(accommodationType.toLowerCase(), country.toLowerCase(), hotelId, 1, 1, checkInDate, checkOutDate);
            detailPage.isRoomAvailable();
            Assertion.assertThat("successfully open the PDP: " + hotel, detailPage.getHotelNameDetail().toLowerCase(), is(hotel.toLowerCase()));
        }
    }

    public String generateColorHex(String vendor) {
        return switch (vendor.toLowerCase()) {
            case "native" -> "0064D2";
            case "expedia" -> "30F639";
            case "hotelbeds" -> "FCA12F";
            case "agoda" -> "FF0000";
            default -> throw new IllegalStateException("Unexpected value: " + vendor);
        };
    }

    public String generateVendorCode(String vendor) {
        return switch (vendor.toLowerCase()) {
            case "native" -> "1";      // TIKET
            case "agoda" -> "3";       // AGODA
            case "hotelbeds" -> "7";   // HOTELBEDS
            case "expedia" -> "8";     // EXPEDIA_RAPID
            default -> throw new IllegalStateException("Unexpected value: " + vendor);
        };
    }
}
