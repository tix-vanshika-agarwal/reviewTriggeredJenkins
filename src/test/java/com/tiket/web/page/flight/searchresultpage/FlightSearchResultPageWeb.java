package com.tiket.web.page.flight.searchresultpage;

import com.google.gson.Gson;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.pojo.flight.FlightStreamResponse;
import com.tiket.pojo.flight.FlightStreamV4Response;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import io.appium.java_client.AppiumBy;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.SkipException;
import org.openqa.selenium.ElementNotInteractableException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public abstract class FlightSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {
    public FlightSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FlightSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FlightSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB -> {
                return new FlightSearchResultPageMWeb<>(session);
            }
            default -> throw new UnsupportedOperationException("Platform not supported");
        }
    }
    private static final Locator SEO_PRICE_CARDS = new Locator("SEO_PRICE_CARDS", new By.ByXPath("//div[@data-testid='seo-price-book-now-card']"));
    private static final Locator FIRST_SEO_CARD_ORIGIN = new Locator("FIRST_SEO_CARD_ORIGIN", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//div[contains(@class,'PriceTableV2_route_info_row')]//span[1]"));
    private static final Locator FIRST_SEO_CARD_DESTINATION = new Locator("FIRST_SEO_CARD_DESTINATION", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//div[contains(@class,'PriceTableV2_route_info_row')]//span[last()]"));
    private static final Locator FIRST_SEO_CARD_DATE = new Locator("FIRST_SEO_CARD_DATE", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//div[contains(@class,'PriceTableV2_airline_name_row')]//h4"));
    private static final Locator FIRST_SEO_CARD_BOOK_NOW = new Locator("FIRST_SEO_CARD_BOOK_NOW", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//a[@data-testid='seo-price-book-now']"));
    private static final Locator FIRST_SEO_CARD_PRICE = new Locator("FIRST_SEO_CARD_PRICE", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//h3[contains(@class,'PriceTableV2_price')]"));
    private static final Locator FIRST_SEO_CARD_AIRLINE = new Locator("FIRST_SEO_CARD_AIRLINE", new By.ByXPath("(//div[@data-testid='seo-price-book-now-card'])[1]//div[contains(@class,'PriceTableV2_airline_name_row')]//h3"));
    private static final Locator FIRST_STANDARD_SECTION_CARD_PRICE = new Locator("FIRST_STANDARD_SECTION_CARD_PRICE", new By.ByXPath("(//div[@data-testid='flight-section-standard']//div[@data-testid='srp-flight-card'])[1]//div[contains(@class,'FlightCard_price')]//span[contains(@class,'Text_variant_price')][1]"));
    private static final Locator SORT_FILTER_BUTTON = new Locator("SORT_FILTER_BUTTON", By.xpath("//button[contains(@class,'FilterTabNavigation_sidebar_item')][.//*[normalize-space()='Sort' or normalize-space()='Urutkan']]"));
    private static final Locator TRANSIT_FILTER_BUTTON = new Locator("TRANSIT_FILTER_BUTTON", By.xpath("//button[contains(@class,'FilterTabNavigation_sidebar_item')][.//*[normalize-space()='Transit']]"));
    private static final Locator SEO_PRICE_CARD_DATES = new Locator("SEO_PRICE_CARD_DATES", new By.ByXPath("//div[@data-testid='seo-price-book-now-card']//div[contains(@class,'PriceTableV2_airline_name_row')]//h4"));
    private static final Locator AIRLINES_FILTER_BUTTON = new Locator("AIRLINES_FILTER_BUTTON", By.xpath("//button[contains(@class,'FilterTabNavigation_sidebar_item')][.//*[normalize-space()='Airlines' or normalize-space()='Maskapai']]"));
    private static final Locator PROGRESS_BAR_FILL = new Locator("PROGRESS_BAR_FILL", By.xpath("//div[contains(@class,'ProgressBar_bar_track')]//div[contains(@class,'ProgressBar_bar_fill')]"));
    private static final Locator SearchResultLocator = new Locator("SearchResultLocator", new By.ByXPath("//div[@data-testid='srp-flight-card']"));
    public static final Locator PROGRESS_BAR_LOADING = new Locator("PROGRESS_BAR_LOADING", new By.ByXPath("//*[contains(@class,'ProgressBar_progress_wrapper')]"));
    private static final Locator AIRLINES_FILTER = new Locator("AIRLINES_FILTER", new By.ByXPath(
            "//div[@data-testid='quick-filter-root']//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Airlines' or normalize-space()='Maskapai']]"
                    + " | //button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Airlines' or normalize-space()='Maskapai']]"));
    private static final Locator AIRLINE_FILTER_DROPDOWN = new Locator("AIRLINE_FILTER_DROPDOWN", new By.ByXPath(
            "//div[contains(@class,'DropdownAirlines_wrap')]"
                    + " | //section[@data-section-key='airlines']"
                    + " | //div[contains(@class,'BaseDropdown_base_dropdown')][.//section[@data-section-key='airlines']]"));
    private static final String XPATH_SHOW_MORE_WITH_DIGITS =
            "starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'show ')"
                    + " and contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),' more')"
                    + " and string-length(normalize-space(substring-before(substring-after(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'show '),' more'))) > 0"
                    + " and string-length(translate(normalize-space(substring-before(substring-after(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'show '),' more')),'0123456789','')) = 0";
    private static final String XPATH_LIHAT_LAINNYA_WITH_DIGITS =
            "starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lihat ')"
                    + " and contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),' lainnya')"
                    + " and string-length(normalize-space(substring-before(substring-after(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lihat '),' lainnya'))) > 0"
                    + " and string-length(translate(normalize-space(substring-before(substring-after(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'lihat '),' lainnya')),'0123456789','')) = 0";
    private static final Locator AIRLINE_FILTER_SHOW_MORE = new Locator("AIRLINE_FILTER_SHOW_MORE", new By.ByXPath("//div[contains(@class,'DropdownAirlines')]//button[@data-testid='filter-checkbox-show-more' or contains(@class,'FilterCheckboxList_show_more') or contains(@class,'show_more_btn') or .//span[(" + XPATH_SHOW_MORE_WITH_DIGITS + ") or (" + XPATH_LIHAT_LAINNYA_WITH_DIGITS + ")]] | //div[contains(@class,'BaseDropdown')]//button[@data-testid='filter-checkbox-show-more']"));
    /** Safety cap only — UI label is dynamic, e.g. "Show 6 more", "Show 12 more". */
    private static final int AIRLINE_FILTER_SHOW_MORE_SAFETY_CAP = 12;
    private static final Locator CHANGE_DATE = new Locator("CHANGE_DATE", By.cssSelector("button[aria-label='Change']"));
    private static final Locator SAVE_AIRLINE_FILTER = new Locator("SAVE_AIRLINE_FILTER", new By.ByXPath("//*[contains(@class,'FilterCategoryDropdownFooter_footer') or contains(@class,'DropdownAirlines_footer') or contains(@class,'FilterFormSheet_button_container') or contains(@class,'BaseDropdown_footer')]//button[contains(@class,'Button_variant_primary') or contains(@class,'FilterFormSheet_button_save')] | //div[contains(@class,'DropdownAirlines')]//button[.//span[normalize-space()='Save' or normalize-space()='Simpan']]"));
    private static final Locator FLIGHT_FIRST_CARD = new Locator("FLIGHT_FIRST_CARD", new By.ByXPath("(//div[@data-testid='srp-flight-card'])[1]"));
    private static final Locator FLIGHT_FIRST_CARD_DEPART_ARRIVAL_TIME = new Locator("FLIGHT_FIRST_CARD_DEPART_ARRIVAL_TIME", new By.ByXPath(
            "(//div[@data-testid='srp-flight-card'])[1]//div[contains(@class,'ScheduleTime_endpoint')]//p[contains(@class,'Text_size_h3')]"
                    + " | (//div[@data-testid='srp-flight-card'])[1]//div[contains(@class,'FlightCard_time')]//p[contains(@class,'Text_size_h3')]"));
    private static final Locator FLIGHT_CARD_ALL = new Locator("FLIGHT_CARD_ALL", new By.ByXPath("//*[@data-testid='srp-flight-card']"));
    private static final Locator FLIGHT_CARD2 = new Locator("FLIGHT_CARD2", new By.ByXPath("(//*[@data-testid='srp-flight-card'])[2]"));
    private static final Locator MEET_TRAVEL_POLICY = new Locator("MEET_TRAVEL_POLICY", new By.ByXPath("//*[(contains(text(),'Meets Travel Policy') or contains(text(),'Sesuai Kebijakan'))]"));
    private static final Locator MEET_TRAVEL_POLICY_2 = new Locator("MEET_TRAVEL_POLICY", new By.ByXPath("(//*[@data-testid='srp-flight-card']//*[contains(@class,'Label_label') and (contains(text(),'Meets Travel Policy') or contains(text(),'Sesuai Kebijakan'))])[2]"));
    private static final Locator RE_SET_FILTER = new Locator("RE_SET_FILTER", new By.ByXPath("//div[contains(@class,'FilterCategoryDropdownFooter_footer') or contains(@class,'DropdownAirlines') or contains(@class,'BaseDropdown')]//button[contains(@class,'btn_reset') or .//span[normalize-space(text())='Reset' or normalize-space(text())='Atur ulang']] | //button[.//span[normalize-space(text())='Reset' or normalize-space(text())='Atur ulang']]"));
    private static final Locator NUMBER_OF_PASSENGER_AND_SEAT_TYPE = new Locator("NUMBER_OF_PASSENGER_AND_SEAT_TYPE", new By.ByXPath(
            "//span[contains(@class,'SearchWidget_passenger')]"
                    + " | //span[contains(@class,'ChangeSearchHeader_cabin_class_standard')]"));
    private static Locator activeDatePickerPriceLocator(boolean returnLeg) {
        String sliderPosition = returnLeg ? "last()" : "1";
        String locatorName = returnLeg ? "PRICE_SELECTED_RETURN_DATE" : "PRICE_SELECTED_DATE";
        return new Locator(locatorName, new By.ByXPath(String.format(
                "(//div[contains(@class,'LowestPrice_lowest_price_slider')])[%s]"
                        + "//button[contains(@class,'LowestPrice_card_active')]"
                        + "//span[contains(@class,'LowestPrice_price')]",
                sliderPosition)));
    }

    private static Locator activeDatePickerCardLocator(boolean returnLeg) {
        String sliderPosition = returnLeg ? "last()" : "1";
        String locatorName = returnLeg ? "ACTIVE_RETURN_DATE_PICKER_CARD" : "ACTIVE_DEPART_DATE_PICKER_CARD";
        return new Locator(locatorName, new By.ByXPath(String.format(
                "(//div[contains(@class,'LowestPrice_lowest_price_slider')])[%s]"
                        + "//button[contains(@class,'LowestPrice_card_active')]",
                sliderPosition)));
    }

    private static final Locator PRICE_SELECTED_DATE_ANY = new Locator("PRICE_SELECTED_DATE_ANY", new By.ByXPath(
            "(//div[contains(@class,'LowestPrice_lowest_price_slider')])[1]"
                    + "//button[contains(@class,'LowestPrice_card_active')]"
                    + "//span[contains(@class,'LowestPrice_price')]"));
    private static final Locator FLIGHT_FIRST_CARD_PER_PAX_PRICE = new Locator("FLIGHT_FIRST_CARD_PER_PAX_PRICE", new By.ByXPath(
            "(//div[@data-testid='srp-flight-card'])[1]//div[contains(@class,'FlightCard_price')]//span[contains(@class,'Text_variant_price')][1]"
                    + " | (//div[@data-testid='srp-flight-card'])[1]//span[contains(@class,'Pricing_pax')][1]/preceding-sibling::span[1]"));
    private static final Locator CHANGE_SEARCH_BTN_SRP = new Locator("CHANGE_SEARCH_BTN_SRP", new By.ByXPath("//*[contains(@class,'Button_variant_primary') and contains(text(),'Ubah Pencarian') or contains(text(),'Change Search')]"));
    private static final Locator REFUND_100P_SKIP_POPUP = new Locator("REFUND_100P_SKIP_POPUP", new By.ByXPath("//*[contains(@class,'Coachmark_coachmark_done_btn')]"));
    private static final Locator AIRLINE_NAME = new Locator("AIRLINE_NAME", new By.ByXPath(
            "//div[contains(@class,'AirlineSection_name_row')]//p[contains(@class,'Text_text')]"
                    + " | //*[contains(@class,'AirlinesAndFacilities_airline_text')]"));
    private static final Locator CORPORATE_DEALS_BANNER = new Locator("CORPORATE_DEALS_BANNER", new By.ByXPath(
            "(//div[@data-testid='srp-flight-card']//*[contains(normalize-space(.),'Corporate Deals')])[1]"
                    + " | (//div[contains(@class,'PriceModuleLabel_label_wrapper') and contains(@class,'PriceModuleLabel_with_icon')]//div[@title='Corporate Deals'])[1]"));
    private static final Locator CORPORATE_DEALS_BENEFIT_CARD = new Locator("CORPORATE_DEALS_BENEFIT_CARD", new By.ByXPath("//*[b[text()='Corporate Deal']]"));
    private static final Locator SELECT_TICKET_BUTTON = new Locator("SELECT_TICKET_BUTTON", By.xpath("//button[contains(@class,'Button_variant_primary') and (contains(text(),'Select') or contains(text(),'Pilih'))]"));
    private static final Locator TRANSIT_FILTER = new Locator("TRANSIT_FILTER", new By.ByXPath(
            "//div[@data-testid='quick-filter-root']//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Transit']]"
                    + " | //button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Transit']]"));
    private static final Locator TRANSIT_POPUP = new Locator("TRANSIT_POPUP", new By.ByXPath("//div[contains(@class, 'BaseDropdown_base_dropdown__')]"));
    private static final Locator SAVE_TRANSIT_FILTER = new Locator("SAVE_TRANSIT_FILTER", new By.ByXPath("//button[.//span[normalize-space()='Save']]"));
    private static final Locator FLIGHT_CARDS = new Locator("FLIGHT_CARDS", new By.ByXPath("//div[@data-testid='srp-flight-card']"));
    protected static final Locator CHECKBOX_TRANSIT_ONE = new Locator("CHECKBOX_TRANSIT_ONE", new By.ByXPath("//span[contains(normalize-space(),'1 Transit') or contains(normalize-space(),'1 Singgah')]"));
    private static final Locator CHECKBOX_TRANSIT_TWO = new Locator("CHECKBOX_TRANSIT_TWO", new By.ByXPath("//span[contains(@class,'Chip_text')][contains(normalize-space(),'2+ Transit') or contains(normalize-space(),'2+ Singgah')]"));
    private static final Locator SELECT_BUTTON_TRANSIT = new Locator("SELECT_BUTTON_TRANSIT", new By.ByXPath("//button[contains(@class, 'FilterFormSheet_button_save')]"));
    private static final Locator FILTER = new Locator("FILTER", new By.ByXPath(
            "//div[@data-testid='quick-filter-root']//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Filter' or normalize-space()='Saring']]"
                    + " | //button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text')][normalize-space()='Filter' or normalize-space()='Saring']]"));
    private static final Locator FILTER_POPUP = new Locator("FILTER_POPUP", new By.ByXPath("//section[contains(@class,'Modal_modal_popup')]"));
    private static final Locator PRICE_TABLE = new Locator("PRICE_TABLE", new By.ByXPath("//h2[contains(@class, 'PriceTableV2_title')]"));
    private static final Locator OTHER_FILTER = new Locator("OTHER_FILTER", new By.ByXPath("//button[.//span[(contains(normalize-space(),'Others') and contains(normalize-space(),'check')) or (contains(normalize-space(),'Lainnya') and contains(normalize-space(),'cek'))]]"));
    private static final Locator LOWEST_PRICE_GUARANTEE = new Locator("LOWEST_PRICE_GUARANTEE", new By.ByXPath("//label[.//span[text()='Lowest Price Guarantee' or text()='Jaminan Harga Termurah']]//input[@type='checkbox']"));
    private static final Locator LOWEST_PRICE_GUARANTEE_TAG = new Locator("LOWEST_PRICE_GUARANTEE TAG", new By.ByXPath("//span[contains(@class, 'Text_text') and (text()='Lowest Price Guarantee' or text()='Jaminan Harga Termurah')]"));
    private static final Locator SAVE = new Locator("SAVE", new By.ByXPath("(//button[contains(@class,'FilterFormSheet_button_save')])[1]"));
    private static final Locator ROUNDTRIP_DEALS_FILTER = new Locator("ROUNDTRIP_DEALS_FILTER", new By.ByXPath(
            "//button[contains(@class,'Chip_chip')][.//span[contains(normalize-space(.),'Round-trip Deals')]]"
                    + " | (//button[.//span[contains(text(),'Round-trip Deals')]])[1]"));
    private static final String ROUND_TRIP_DEALS_DEPARTURE_DELTA_SECTION_XPATH =
            "//div[@data-testid='flight-section-price_change_info']"
                    + "[.//span[contains(.,'Departure flight') or contains(.,'Penerbangan pergi')]"
                    + " and .//span[contains(@class,'Text_variant_alert')]"
                    + " and (.//p[contains(.,'If you return without') or contains(.,'Jika pulang tanpa penerbangan')])]"
                    + " | //p[contains(.,'If you return without') or contains(.,'Jika pulang tanpa penerbangan')]"
                    + "[.//strong[contains(.,'Round-trip Deals')]]/parent::div"
                    + "[.//span[contains(.,'Departure flight') or contains(.,'Penerbangan pergi')]"
                    + " and .//span[contains(@class,'Text_variant_alert')]]";
    private static final Locator ROUND_TRIP_DEALS_SRP_DEPARTURE_DELTA_BLOCK = new Locator("ROUND_TRIP_DEALS_SRP_DEPARTURE_DELTA_BLOCK",
            new By.ByXPath("(" + ROUND_TRIP_DEALS_DEPARTURE_DELTA_SECTION_XPATH + ")[1]"));
    private static final Locator DELTA_PRICE_DESCRIPTION = new Locator("DELTA_PRICE_DESCRIPTION", new By.ByXPath(
            "(" + ROUND_TRIP_DEALS_DEPARTURE_DELTA_SECTION_XPATH + "//p[contains(.,'If you return without')"
                    + " or contains(.,'Jika pulang tanpa penerbangan')][.//strong[contains(.,'Round-trip Deals')]])[1]"));
    private static final Locator FIRST_CARD_PRICE_AFTER_DELTA_DESC = new Locator("FIRST_CARD_PRICE_AFTER_DELTA_DESC", new By.ByXPath("//span[normalize-space()='Departure flight' or normalize-space()='Penerbangan keberangkatan']/ancestor::div[1]/following::div[@data-testid='srp-flight-card'][1]//span[contains(@class,'Text_variant_price')]"));
    private static final Locator ROUND_TRIP_DEALS_DEPARTURE_DELTA_PRICE_VALUE = new Locator("ROUND_TRIP_DEALS_DEPARTURE_DELTA_PRICE_VALUE",
            new By.ByXPath("(" + ROUND_TRIP_DEALS_DEPARTURE_DELTA_SECTION_XPATH
                    + "//span[contains(@class,'Text_variant_alert')][contains(normalize-space(.),'+')])[1]"));
    public static final Locator DIRECT_FLIGHT_FILTER = new Locator("DIRECT_FLIGHT_FILTER", new By.ByXPath(
            "//button[contains(@class,'Chip_chip')][.//span[contains(normalize-space(.),'Direct Flight') or contains(normalize-space(.),'Penerbangan Langsung')]]"
                    + " | (//button[.//span[contains(text(),'Direct Flight') or contains(text(),'Penerbangan Langsung')]])[1]"));
    private static final Locator NEXT_BUTTON_QUICK_FILTER = new Locator("NEXT_BUTTON_QUICK_FILTER", new By.ByXPath("(//*[contains(@class, 'QuickFilter_scroll_next_btn')])[1]"));
    private static final Locator CHANGE_BUTTON = new Locator("CHANGE_BUTTON", new By.ByXPath("//span[normalize-space()='Change']"));
    private static final Locator SEE_MORE_OTHER_FILTER = new Locator("SEE_MORE_OTHER_FILTER ", new By.ByXPath("(//span[contains(@class,'Text_variant_activity') and contains(normalize-space(),'Show') and contains(normalize-space(),'more')])[last()]"));
    private static final Locator SEE_MORE_SORT = new Locator("SEE_MORE_SORT ", new By.ByXPath("//section[@aria-label='Sort']//button[@data-testid='filter-radio-show-more']"));
    private static final Locator SEE_MORE_AIRLINES = new Locator("SEE_MORE_AIRLINES ", new By.ByXPath("//section[@data-section-key='airline']//button[@data-testid='filter-checkbox-show-more']"));
    private static final Locator SAVE_OTHER_FILTER = new Locator("SAVE_OTHER_FILTER", new By.ByXPath("//button[contains(@class,'FilterFormSheet_button_save') and (normalize-space()='Save' or normalize-space()='Simpan')]"));
    private static final Locator SHOW_DETAILS= new Locator("SHOW_DETAILS", new By.ByXPath("(//span[normalize-space()='Show Details'])[1]"));
    private static final Locator ACTIVE_DATE = new Locator("ACTIVE_DATE", AppiumBy.xpath("//button[contains(@class, 'LowestPrice_card_active')]//span[not(contains(@class, 'price'))]"));
    private static final Locator FACILITIES_SECTION = new Locator("FACILITIES_SECTION", new By.ByXPath("//button[.//span[contains(normalize-space(),'Facilities') or contains(normalize-space(),'Fasilitas')]]"));
    private static final Locator BAGGAGE_CHECKBOX = new Locator("BAGGAGE_CHECKBOX", new By.ByXPath("//label[contains(@class,'FilterOptionRow_row')][.//*[normalize-space()='Baggage' or normalize-space()='Bagasi']]"));
    private static final Locator FILTER_SAVE_BUTTON = new Locator("FILTER_SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'AdvancedFilter_save_button')][.//span[contains(text(),'flights')]]"));
    private static final Locator FLOATING_PROMO_BUTTON = new Locator("FLOATING_PROMO_BUTTON", new By.ByXPath("//img[@alt='Floating Promo Icon']/ancestor::button | //span[contains(@class,'FloatingButton_illustration')]/ancestor::button"));
    private static final Locator PROMO_PAGE_CONTAINER = new Locator("PROMO_PAGE_CONTAINER", new By.ByXPath("//div[@data-testid='voucher-card']"));
    private static final Locator PROMO_PAGE_TITLE = new Locator("PROMO_PAGE_TITLE", new By.ByXPath("//span[contains(text(),'Enjoy our great ongoing') or contains(text(),'promos')]"));
    private static final Locator PROMO_PAGE_BACK_BUTTON = new Locator("PROMO_PAGE_BACK_BUTTON", new By.ByXPath("//button[contains(@class,'MobileBackButton_back_button') or contains(@class,'back_button')]"));
    private static final Locator PROMO_FLOATING_BUTTON = new Locator("PROMO_FLOATING_BUTTON", new By.ByXPath("//button[contains(@class,'FloatingButton_btn')]"));
    private static final Locator FLIGHT_CARDS_WITH_TRANSIT = new Locator("FLIGHT_CARDS_WITH_TRANSIT", new By.ByXPath("//p[contains(normalize-space(),'transit')]"));
    private static final Locator MEAL_FILTER = new Locator("MEAL_FILTER", new By.ByXPath("//img[@alt='Free meals']"));
    private static final Locator TOTAL_PER_PAX = new Locator("TOTAL_PER_PAX", new By.ByXPath(
            "//div[@data-testid='srp-flight-card']//span[contains(@class,'Text_variant_price')]"
                    + " | //span[normalize-space()='/pax']"));
    private static final Locator INFINITY_PRESENT = new Locator("INFINITY_PRESENT", new By.ByXPath("//p[text()='Save more with the best promo' or text()='Lebih hemat pakai promo terbaik']"));
    private static final Locator APPLY_INFINITY = new Locator("APPLY_INFINITY", new By.ByXPath("//button//span[contains(@class, 'Text_text') and (contains(text(),'Apply') or contains(text(),'Pakai'))]"));
    private static final Locator SELF_TRANSFER = new Locator("SELF_TRANSFER", new By.ByXPath("(//label[contains(@class,'FilterOptionRow_row')][.//span[normalize-space()='Self-transfer']])[last()]"));
    private static final Locator SEE_FLIGHTS = new Locator("SEE_FLIGHTS", new By.ByXPath("//button[contains(@class,'AdvancedFilter_save_button')][.//span[contains(text(),'flights')]]"));
    private static final Locator TIKET_100_PERCENT_REFUND_QUICK_FILTER = new Locator("TIKET_100_PERCENT_REFUND_QUICK_FILTER", new By.ByXPath("//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Text_text') and contains(.,'100%') and (contains(translate(.,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'refund') or contains(.,'Pengembalian'))]]"));
    private static final Locator SORT_FILTER_CHIP = new Locator("SORT_FILTER_CHIP", new By.ByXPath("//button[contains(@class,'Chip_chip')][.//span[contains(@class,'Chip_text') and (contains(text(),'Sort') or contains(text(),'Urutkan'))]]"));
    private static final Locator SORT_OPTION_LOWEST_PRICE = new Locator("SORT_OPTION_LOWEST_PRICE", new By.ByXPath("//span[contains(@class,'Radio_children') and (normalize-space()='Lowest price' or normalize-space()='Harga terendah')]"));
    private static final Locator ORIGIN_FIELD_SR = new Locator("ORIGIN_FIELD_SR", new By.ByXPath("(//span[contains(@class,'SearchWidget_airport_depart')])[1]"));
    private static final Locator DESTINATION_FIELD_SR = new Locator("DESTINATION_FIELD_SR", new By.ByXPath("(//span[contains(@class,'SearchWidget_airport_arrival') and contains(@class,'Text_text')])[1]"));
    private static final Locator SEARCH_WIDGET_DATE_DEPART_SHORT = new Locator("SEARCH_WIDGET_DATE_DEPART_SHORT", new By.ByXPath("//div[contains(@class,'SearchWidget_date')]//span[contains(@class,'SearchWidget_date_depart')]//span[contains(@class,'SearchWidget_date_short')]"));
    private static final Locator SEARCH_WIDGET_DATE_DEPART_LONG = new Locator("SEARCH_WIDGET_DATE_DEPART_LONG", new By.ByXPath("//div[contains(@class,'SearchWidget_date')]//span[contains(@class,'SearchWidget_date_depart')]//span[contains(@class,'SearchWidget_date_long')]"));
    private static final Locator SEARCH_WIDGET_DATE_RETURN = new Locator("SEARCH_WIDGET_DATE_RETURN", new By.ByXPath("//div[contains(@class,'SearchWidget_date')]//span[contains(@class,'SearchWidget_date_return')]"));
    private static final Locator SEARCH_WIDGET_DATE_RETURN_SHORT = new Locator("SEARCH_WIDGET_DATE_RETURN_SHORT", new By.ByXPath("//div[contains(@class,'SearchWidget_date')]//span[contains(@class,'SearchWidget_date_return')]//span[contains(@class,'SearchWidget_date_short')]"));
    private static final Locator SEARCH_WIDGET_DATE_DIVIDER = new Locator("SEARCH_WIDGET_DATE_DIVIDER", new By.ByXPath("//div[contains(@class,'SearchWidget_date')]//span[contains(@class,'SearchWidget_date_divider')]"));
    private static final Locator INPUT_LOCATION = new Locator("INPUT_LOCATION", new By.ByXPath("//input[contains(@placeholder,'city or airport') or contains(@placeholder,'kota atau bandara')]"));
    private static final Locator SEARCH = new Locator("SEARCH", new By.ByXPath("(//button[contains(@class,'SearchWidget_btn_submit') and .//span[normalize-space()='Search' or normalize-space()='Cari']])[1]"));
    private static final Locator CORPORATE_DEALS_FLIGHT_CARD_LABEL = new Locator("CORPORATE_DEALS_FLIGHT_CARD_LABEL", new By.ByXPath(
            "(//div[@data-testid='srp-flight-card']//span[contains(@class,'Text_variant_price') and normalize-space()='Corporate Deals'])[1]"
                    + " | (//div[@data-testid='srp-flight-card']//span[contains(@class,'Text_size_b2') and normalize-space()='Corporate Deals'])[1]"));

    private Locator getLocatorLocationSearch(String location) {
        return new Locator("LOCATOR_SEARCH_SUGGESTION", new By.ByXPath("(//*[contains(@class,'AirportForm_airport')]//span[contains(text(),'" + location + "')])[1]"));
    }
    private Locator specialFilter(String otherFilter){
        return new Locator("SPECIAL_FILTER", new By.ByXPath("//label[contains(@class,'FilterForm_item')][.//span[normalize-space()='" + otherFilter + "']]"));
    }
    private static String exactAirportCodeTextEquals(String airportCode) {
        return "translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='"
                + airportCode.toLowerCase() + "'";
    }

    private Locator getLocatorBasedOnAirportCode(String airportCode) {
        return new Locator("LOCATOR_AIRPORT_CODE", new By.ByXPath(
                "//*[contains(@class,'AirportForm_airport_code') and " + exactAirportCodeTextEquals(airportCode) + "]"));
    }
    private Locator getLocatorBasedOnAirportCodeFlexible(String airportCode) {
        return new Locator("LOCATOR_AIRPORT_CODE_FLEXIBLE",
                new By.ByXPath("(//*[contains(@class,'AirportForm_airport')][.//*[contains(@class,'AirportForm_airport_code') and "
                        + exactAirportCodeTextEquals(airportCode) + "]])[1]"));
    }
    private Locator getFirstHighlightedSearchResult() {
        return new Locator("FIRST_HIGHLIGHTED_SEARCH_RESULT",
                new By.ByXPath("(//span[contains(@class,'highlighted')])[1]"));
    }
    private Locator getPopularCityByKeyword(String keyword) {
        return new Locator("POPULAR_CITY_BY_KEYWORD",
                new By.ByXPath("(//button[contains(@class,'AirportForm_chip_popular_cities')][contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'"
                        + keyword.toLowerCase() + "')])[1]"));
    }

    private Locator srpViNudges(String srpVi){
        return new Locator("SRP_VI_NUDGES", new By.ByXPath("(//span[normalize-space()='"+srpVi+"'])[1]"));
    }

    public void isSRPLoaded() {
        isSRPLoaded(45, false);
    }

    public void isSRPLoadedAndPollStreamingCaptures() {
        enableFlightStreamingCapture();
        isSRPLoaded(45, true);
        driverTools.waitForReturnStreamingCapturesToSettle("RT_RETURN_SMART_TRIP", 3, 30, 1);
    }

    public static final String SMART_TRIP_TAG = "SMART_TRIP";
    public static final long PRICE_ROUNDING_TOLERANCE = 1L;
    private static final int SRP_MAX_SCROLL_STEPS = 120;
    private static final int SRP_BOTTOM_CONFIRMATIONS = 4;
    private static final int SRP_SCROLL_STEP_PIXELS = 600;
    private static final int SRP_SCROLL_TO_TOP_PIXELS = 100000;

    private static final String CARD_BY_SCHEDULE_XPATH = "//div[@data-testid='srp-flight-card'][.//p[normalize-space()='%s']][.//p[normalize-space()='%s']]";
    private static final Locator PRICE_WITHIN_CARD = new Locator("PRICE_WITHIN_CARD", new By.ByXPath(".//div[contains(@class,'FlightCard_price')]//span[contains(@class,'Text_variant_price')]"));

    private String streamingGroupsSummary = "";
    private final Map<String, String> airlineNames = new LinkedHashMap<>();

    public record CapturedFlight(int captureIndex, FlightStreamV4Response.DepartureFlightV4 flight) { }

    public record SmartTripFarePair(String flightIdGeneral, FlightStreamV4Response.DepartureFlightV4 smartTrip, FlightStreamV4Response.DepartureFlightV4 plain) { }

    /**
     * @param pollStreamingCaptures when true, polls CDP performance logs while waiting so
     *                                streaming API bodies are captured before they expire.
     */
    public void isSRPLoaded(int timeoutSeconds, boolean pollStreamingCaptures) {
        long startMs = System.currentTimeMillis();
        long deadlineMs = startMs + (timeoutSeconds * 1000L);

        // Share the wait budget across both progress-bar disappearances so the
        // method honours its "total upper bound" contract instead of effectively
        // doubling the budget when both bars are slow to clear.
        int phase1Loading = remainingSeconds(deadlineMs);
        if (phase1Loading > 0) {
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, phase1Loading);
            pollStreamingCapturesIfNeeded(pollStreamingCaptures);
        }
        int phase1Fill = remainingSeconds(deadlineMs);
        if (phase1Fill > 0) {
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, phase1Fill);
            pollStreamingCapturesIfNeeded(pollStreamingCaptures);
        }

        for (int wave = 1; wave <= 2 && System.currentTimeMillis() < deadlineMs; wave++) {
            boolean reappeared = driverTools.waitForElementToPresence(PROGRESS_BAR_LOADING, 3).isPresent()
                    || driverTools.waitForElementToPresence(PROGRESS_BAR_FILL, 1).isPresent();
            if (!reappeared) {
                break;
            }
            logger.log("SRP secondary loading wave #" + wave + " detected; waiting for it to settle.");
            int remainingLoading = remainingSeconds(deadlineMs);
            if (remainingLoading <= 0) {
                break;
            }
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, remainingLoading);
            pollStreamingCapturesIfNeeded(pollStreamingCaptures);
            int remainingFill = remainingSeconds(deadlineMs);
            if (remainingFill <= 0) {
                break;
            }
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, remainingFill);
            pollStreamingCapturesIfNeeded(pollStreamingCaptures);
        }

        int waitForCardSecs = remainingSeconds(deadlineMs);
        if (waitForCardSecs <= 0) {
            logger.log("SRP load wait exhausted before flight cards became visible within "
                    + timeoutSeconds + "s - schedule cache may not be primed yet; caller should re-trigger the search.");
            return;
        }
        Optional<WebElement> firstCard = driverTools.waitForElementToAppear(FLIGHT_FIRST_CARD, waitForCardSecs);
        if (firstCard.isEmpty()) {
            logger.log("SRP loaded with no flight cards rendered within " + timeoutSeconds
                    + "s - schedule cache may not be primed yet; caller should re-trigger the search.");
            return;
        }

        waitUntilFlightCardCountStabilises(deadlineMs, pollStreamingCaptures);
        pollStreamingCapturesIfNeeded(pollStreamingCaptures);
        long elapsedMs = System.currentTimeMillis() - startMs;
        logger.log("SRP fully loaded with stable flight-card count in " + elapsedMs + "ms.");
    }

    private void pollStreamingCapturesIfNeeded(boolean pollStreamingCaptures) {
        if (pollStreamingCaptures) {
            driverTools.enableFlightSearchStreamingCapture();
            driverTools.pollFlightStreamingCapturesFromNetworkLogs();
        }
    }

    /**
     * Waits until the search-result page is fully loaded with a stable list of flight
     * cards. The Tiket SRP streams flight results in chunks while the backend ingests
     * schedules, so the primary {@link #PROGRESS_BAR_LOADING} can disappear, briefly
     * reappear, and the card list keeps growing for several seconds afterwards.
     */
    public void isSRPLoaded(int timeoutSeconds) {
        isSRPLoaded(timeoutSeconds, false);
    }

    /** True when SRP finished loading with zero flight cards (empty / no-result state). */
    public boolean hasNoFlightCards() {
        // Optional.empty means lookup failed (exception retries exhausted), not "0 cards".
        List<WebElement> cards = driverTools.elements(FLIGHT_CARDS, 2)
                .orElseThrow(() -> new IllegalStateException("Unable to query SRP flight cards"));
        int count = cards.size();
        logger.log("SRP flight card count: " + count);
        return count == 0;
    }

    /**
     * Waits for empty / no-result SRP: progress bars clear, then card count stays at 0.
     * Unlike {@link #isSRPLoaded()}, this does not wait for a first flight card.
     */
    public void isEmptySRPLoaded() {
        isEmptySRPLoaded(45);
    }

    public void isEmptySRPLoaded(int timeoutSeconds) {
        long startMs = System.currentTimeMillis();
        long deadlineMs = startMs + (timeoutSeconds * 1000L);

        int phase1Loading = remainingSeconds(deadlineMs);
        if (phase1Loading > 0) {
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, phase1Loading);
        }
        int phase1Fill = remainingSeconds(deadlineMs);
        if (phase1Fill > 0) {
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, phase1Fill);
        }

        for (int wave = 1; wave <= 2 && System.currentTimeMillis() < deadlineMs; wave++) {
            boolean reappeared = driverTools.waitForElementToPresence(PROGRESS_BAR_LOADING, 3).isPresent()
                    || driverTools.waitForElementToPresence(PROGRESS_BAR_FILL, 1).isPresent();
            if (!reappeared) {
                break;
            }
            logger.log("Empty SRP secondary loading wave #" + wave + " detected; waiting for it to settle.");
            int remainingLoading = remainingSeconds(deadlineMs);
            if (remainingLoading <= 0) {
                break;
            }
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, remainingLoading);
            int remainingFill = remainingSeconds(deadlineMs);
            if (remainingFill <= 0) {
                break;
            }
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, remainingFill);
        }

        waitUntilFlightCardCountStabilisesAt(0, deadlineMs);
        long elapsedMs = System.currentTimeMillis() - startMs;
        logger.log("Empty SRP settled with zero flight cards in " + elapsedMs + "ms.");
    }

    /**
     * Polls the flight-card count every second and returns once it stays the same
     * across two consecutive samples (or the deadline is hit). This marks the result
     * stream as settled - important before re-triggering a search to prime the schedule
     * cache, since interrupting an in-flight stream can prevent the cache write.
     */
    private void waitUntilFlightCardCountStabilises(long deadlineMs, boolean pollStreamingCaptures) {
        int previousCount = -1;
        int stableSamples = 0;
        final int requiredStableSamples = 2;
        while (System.currentTimeMillis() < deadlineMs) {
            pollStreamingCapturesIfNeeded(pollStreamingCaptures);
            int currentCount = driverTools.elements(FLIGHT_CARDS, 2).map(List::size).orElse(0);
            if (currentCount > 0 && currentCount == previousCount) {
                stableSamples++;
                if (stableSamples >= requiredStableSamples) {
                    return;
                }
            } else {
                stableSamples = 0;
            }
            previousCount = currentCount;
            driverTools.dummyWait(1);
        }
        logger.log("Flight-card count did not stabilise before deadline; proceeding with last known count = " + previousCount);
    }

    public boolean isPriceTableVisible() {

        driverTools.waitForElementToPresence(PRICE_TABLE);
        return driverTools.isElementDisplayed(PRICE_TABLE);
    }

    private void waitUntilFlightCardCountStabilisesAt(int expectedCount, long deadlineMs) {
        int previousCount = -1;
        int stableSamples = 0;
        final int requiredStableSamples = 2;
        while (System.currentTimeMillis() < deadlineMs) {
            int currentCount = driverTools.elements(FLIGHT_CARDS, 2).map(List::size).orElse(0);
            if (currentCount == expectedCount && currentCount == previousCount) {
                stableSamples++;
                if (stableSamples >= requiredStableSamples) {
                    return;
                }
            } else {
                stableSamples = 0;
            }
            previousCount = currentCount;
            driverTools.dummyWait(1);
        }
        logger.log("Flight-card count did not settle at " + expectedCount
                + " before deadline; last known count = " + previousCount);
    }

    /**
     * Returns the whole seconds remaining until {@code deadlineMs}, never negative.
     * Used by {@link #isSRPLoaded(int)} so each follow-up wait consumes only the
     * remaining slice of the overall budget instead of being floored to an
     * arbitrary minimum that could push the total past the stated upper bound.
     */
    private int remainingSeconds(long deadlineMs) {
        long remainingMs = deadlineMs - System.currentTimeMillis();
        if (remainingMs <= 0) {
            return 0;
        }
        return (int) (remainingMs / 1000L);
    }

    public void filterSortForNewUI(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        driverTools.waitForElementToPresence(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);

        driverTools.waitForElementToPresence(SORT_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(SORT_FILTER_BUTTON);

        driverTools.waitForElementToPresence(SEE_MORE_SORT);
        driverTools.clickWithJavascriptExecutor(SEE_MORE_SORT);

    }

    public void openSortFilter() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, 20);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 30);
        if(!driverTools.isElementDisplayed(SORT_FILTER_CHIP)){
            filterSortForNewUI();
            return;
        }
        driverTools.waitForElementToPresence(SORT_FILTER_CHIP);
        driverTools.clickWithJavascriptExecutor(SORT_FILTER_CHIP);
    }

    public void selectSortByLowestPrice() {
        driverTools.dummyWait(2);
        driverTools.waitForElementToPresence(SORT_OPTION_LOWEST_PRICE);
        driverTools.clickWithJavascriptExecutor(SORT_OPTION_LOWEST_PRICE);
        isSRPLoaded(45, false);
        if(driverTools.isElementDisplayed(SEE_FLIGHTS)){
            driverTools.clickWithJavascriptExecutor(SEE_FLIGHTS);
        }
    }

    private boolean isDateStripPriceLoaded(String priceText) {
        return priceText != null && !CoreFunctions.removeNonDigit(priceText).isEmpty();
    }

    private String waitForActiveDatePickerPrice(boolean returnLeg) {
        Locator priceLocator = activeDatePickerPriceLocator(returnLeg);
        Locator cardLocator = activeDatePickerCardLocator(returnLeg);
        driverTools.waitForElementToPresence(priceLocator, 20);
        driverTools.scrollIntoWebView(cardLocator);

        for (int attempt = 0; attempt < 20; attempt++) {
            String priceText = driverTools.getText(priceLocator).orElse("");
            if (isDateStripPriceLoaded(priceText)) {
                logger.log("Active date picker price (" + (returnLeg ? "return" : "depart") + "): " + priceText);
                return priceText;
            }
            if (attempt == 0) {
                driverTools.clickWithJavascriptExecutor(cardLocator);
                isSRPLoaded(15, false);
            }
            driverTools.dummyWait(1);
        }

        String lastText = driverTools.getText(priceLocator).orElse("(not found)");
        throw new RuntimeException("Active date picker price did not load for "
                + (returnLeg ? "return" : "departure") + ". Last text: " + lastText);
    }


    public void assertSelectedDatePriceMatchesFirstCardPerPaxPrice() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 30);
        driverTools.waitForElementToPresence(FLIGHT_FIRST_CARD_PER_PAX_PRICE);
        String dateStripPrice = waitForActiveDatePickerPrice(false);
        String firstCardPrice = driverTools.getText(FLIGHT_FIRST_CARD_PER_PAX_PRICE).orElseThrow();
        long amountDateStrip = Long.parseLong(CoreFunctions.removeNonDigit(dateStripPrice));
        long amountFirstCard = Long.parseLong(CoreFunctions.removeNonDigit(firstCardPrice));
        logger.log("PRICE_SELECTED_DATE: " + dateStripPrice + " -> " + amountDateStrip);
        logger.log("First card /pax price: " + firstCardPrice + " -> " + amountFirstCard);
        Assertion.assertThat("Active departure date picker price should match first flight card per-pax price",
                amountDateStrip, is(amountFirstCard));
    }


    public void assertReturnDateStripMatchesRtdCardBodyPerPaxPrice() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        WebElement deltaDescription = driverTools.waitForElementToAppear(DELTA_PRICE_DESCRIPTION, 20).orElseThrow();
        driverTools.scrollIntoWebView(deltaDescription);
        driverTools.waitForElementToPresence(FIRST_CARD_PRICE_AFTER_DELTA_DESC);
        driverTools.scrollIntoWebView(FIRST_CARD_PRICE_AFTER_DELTA_DESC);
        String returnCardPrice = driverTools.getText(FIRST_CARD_PRICE_AFTER_DELTA_DESC).orElseThrow();
        String dateStripPrice = waitForActiveDatePickerPrice(true);
        long strip = Long.parseLong(CoreFunctions.removeNonDigit(dateStripPrice));
        long cardPrice = Long.parseLong(CoreFunctions.removeNonDigit(returnCardPrice));
        logger.log("Return: active date picker -> " + strip + ", first schedule after RTD delta -> " + cardPrice);
        Assertion.assertThat(
                "Active return date picker price should match first schedule after Round-trip Deals delta disclaimer",
                strip, is(cardPrice));
    }

    public void assertRoundTripDealsDepartureDeltaBlockVisible() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        WebElement deltaBlock = driverTools.waitForElementToAppear(ROUND_TRIP_DEALS_SRP_DEPARTURE_DELTA_BLOCK, 20).orElseThrow();
        driverTools.scrollIntoWebView(deltaBlock);
        Assertion.assertThat("Round-trip Deals departure delta block (departure + delta + disclaimer) should be visible on SRP",
                driverTools.isElementDisplayed(ROUND_TRIP_DEALS_SRP_DEPARTURE_DELTA_BLOCK), is(true));
    }

    public void maximizeWindow() {
        driverTools.maximizeWindow();
    }

    public void enableFlightStreamingCapture() {
        driverTools.enableFlightSearchStreamingCapture();
    }

    public void clearFlightStreamingCaptures() {
        driverTools.clearFlightSearchStreamingCaptures();
    }

    private FlightStreamV4Response parseStreamingCapture(Map<String, Object> capture) {
        Object body = capture.get("body");
        if (body == null || body.toString().isBlank()) return null;
        try {
            return new Gson().fromJson(body.toString(), FlightStreamV4Response.class);
        } catch (Exception e) {
            logger.log("Skipped unparseable streaming capture: " + e.getMessage());
            return null;
        }
    }

    public void waitForSrpAndCaptureStreaming(int timeoutSeconds) {
        long deadlineMs = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        int previousCaptures = -1, stablePolls = 0;
        while (System.currentTimeMillis() < deadlineMs) {
            driverTools.enableFlightSearchStreamingCapture();
            driverTools.pollFlightStreamingCapturesFromNetworkLogs();
            int captures = driverTools.getFlightSearchStreamingCaptures().size();
            int cards = driverTools.elements(FLIGHT_CARDS, 1).map(List::size).orElse(0);
            logger.log("Polling streaming: captures=" + captures + ", srpCards=" + cards + ", stablePolls=" + stablePolls);
            stablePolls = (captures == previousCaptures && cards > 0) ? stablePolls + 1 : 0;
            if (stablePolls >= 5) {
                logger.log("Streaming settled with " + captures + " captures and " + cards + " SRP cards");
                return;
            }
            previousCaptures = captures;
            driverTools.dummyWait(1);
        }
        logger.log("Streaming did not settle within " + timeoutSeconds + "s; continuing with whatever was captured");
    }

    public void waitForStreamingToComplete(int timeoutSeconds) {
        long deadlineMs = System.currentTimeMillis() + (timeoutSeconds * 1000L);
        int previousCount = -1, stablePolls = 0;
        while (System.currentTimeMillis() < deadlineMs) {
            driverTools.enableFlightSearchStreamingCapture();
            List<Map<String, Object>> captures = driverTools.getFlightSearchStreamingCaptures();
            long completed = captures.stream().map(this::parseStreamingCapture).filter(r -> r != null && r.data != null && r.data.searchCompleted).count();
            logger.log("Streaming captures so far: " + captures.size() + ", searchCompleted responses: " + completed + ", stablePolls=" + stablePolls);
            if (completed > 0) return;
            stablePolls = captures.size() == previousCount ? stablePolls + 1 : 0;
            if (stablePolls >= 3 && captures.size() > 0) return;
            previousCount = captures.size();
            driverTools.dummyWait(2);
        }
        logger.log("Streaming did not settle within " + timeoutSeconds + "s; continuing with whatever was captured");
    }

    public Map<String, List<CapturedFlight>> groupStreamingDepartureFlightsByFlightIdGeneral() {
        List<Map<String, Object>> captures = driverTools.getFlightSearchStreamingCaptures();
        Map<String, List<CapturedFlight>> grouped = new LinkedHashMap<>();
        Set<String> seenFares = new HashSet<>();
        Set<String> streamTypes = new LinkedHashSet<>();
        Set<String> allTags = new LinkedHashSet<>();
        int parsedCaptures = 0, parsedFlights = 0, discardedFlights = 0, returnFlights = 0, srtReturnFlights = 0;
        long smartTripFlights = 0;

        for (Map<String, Object> capture : captures) {
            streamTypes.add(String.valueOf(capture.get("streamType")));
            FlightStreamV4Response response = parseStreamingCapture(capture);
            if (response == null || response.data == null || response.data.searchList == null || response.data.searchList.departureFlights == null) continue;
            parsedCaptures++;
            if (response.data.airlines != null) response.data.airlines.forEach((code, detail) -> airlineNames.putIfAbsent(code, detail.displayName != null && !detail.displayName.isBlank() ? detail.displayName : detail.name));
            logger.log("capture #" + parsedCaptures + " departureFlights=" + response.data.searchList.departureFlights.size() + ", searchCompleted=" + response.data.searchCompleted);
            returnFlights += response.data.searchList.returnFlights == null ? 0 : response.data.searchList.returnFlights.size();
            srtReturnFlights += response.data.searchList.srtReturnFlights == null ? 0 : response.data.searchList.srtReturnFlights.size();
            smartTripFlights += response.data.searchList.departureFlights.stream().filter(f -> f != null && f.hasTag(SMART_TRIP_TAG)).count();
            allTags.addAll(response.data.searchList.departureFlights.stream().filter(f -> f != null && f.tags != null).flatMap(f -> f.tags.stream()).collect(Collectors.toSet()));
            for (FlightStreamV4Response.DepartureFlightV4 flight : response.data.searchList.departureFlights) {
                if (flight == null || flight.flightIdGeneral == null) continue;
                if (!flight.isV4Shaped()) { discardedFlights++; continue; }
                if (!seenFares.add(parsedCaptures + "|" + flight.flightIdGeneral + "|" + flight.flightId + "|" + flight.trackerProperties.price + "|" + flight.tags + "|" + flight.roundTrip)) continue;
                grouped.computeIfAbsent(flight.flightIdGeneral, key -> new ArrayList<>()).add(new CapturedFlight(parsedCaptures, flight));
                parsedFlights++;
            }
        }

        StringBuilder summary = new StringBuilder("totalCaptures=" + captures.size() + ", parsedCaptures=" + parsedCaptures + ", streamTypesSeen=" + streamTypes + ", parsedFlights=" + parsedFlights + ", discardedNonV4Flights=" + discardedFlights + ", flightIdGeneralGroups=" + grouped.size() + ", smartTripTaggedDepartureFlights=" + smartTripFlights + ", returnFlightsSeen=" + returnFlights + ", srtReturnFlightsSeen=" + srtReturnFlights + ", allDepartureTagsSeen=" + allTags);
        grouped.forEach((flightIdGeneral, flights) -> {
            if (flights.size() > 1) summary.append("\n  ").append(flightIdGeneral).append(" x").append(flights.size()).append(" -> ").append(flights.stream().map(cf -> "capture#" + cf.captureIndex() + "{tags=" + cf.flight().tags + ", roundTrip=" + cf.flight().roundTrip + ", price=" + cf.flight().trackerProperties.price + "}").collect(Collectors.joining(" ")));
        });
        streamingGroupsSummary = summary.toString();
        logger.log("Streaming departure flights grouped by flightIdGeneral: " + streamingGroupsSummary);
        return grouped;
    }

    public String getStreamingGroupsSummary() {
        return streamingGroupsSummary;
    }

    private String describeCard(FlightStreamV4Response.DepartureFlightV4 flight) {
        String schedule = flight.card == null || flight.card.departure == null || flight.card.arrival == null ? "no-card" : flight.card.departure.time + " " + flight.card.departure.airportCode + " -> " + flight.card.arrival.time + " " + flight.card.arrival.airportCode;
        String airlines = flight.filterProperties == null ? "?" : String.valueOf(flight.filterProperties.airlines);
        return "{schedule=" + schedule + ", duration=" + (flight.card == null ? "?" : flight.card.duration) + ", transit=" + (flight.card == null ? "?" : flight.card.totalTransit) + ", roundTrip=" + flight.roundTrip + ", airlines=" + airlines + ", price=" + flight.trackerProperties.price + ", comparisonFare=" + flight.trackerProperties.comparisonFare + ", cardPrice=" + flight.mainPriceText() + ", tags=" + flight.tags + "}";
    }

    public List<SmartTripFarePair> findSmartTripAndPlainFarePairs() {
        Map<String, List<CapturedFlight>> grouped = groupStreamingDepartureFlightsByFlightIdGeneral();
        List<SmartTripFarePair> pairs = new ArrayList<>();
        for (Map.Entry<String, List<CapturedFlight>> entry : grouped.entrySet()) {
            FlightStreamV4Response.DepartureFlightV4 smartTrip = null, plain = null;
            for (CapturedFlight captured : entry.getValue()) {
                if (captured.flight().hasTag(SMART_TRIP_TAG)) {
                    smartTrip = captured.flight();
                } else {
                    plain = captured.flight();
                }
            }
            if (smartTrip == null || plain == null) continue;
            if (smartTrip.trackerProperties.comparisonFare <= plain.trackerProperties.comparisonFare) continue;
            logger.log("Candidate flightIdGeneral " + entry.getKey() + " SMART_TRIP -> " + describeCard(smartTrip) + " | plain -> " + describeCard(plain));
            pairs.add(new SmartTripFarePair(entry.getKey(), smartTrip, plain));
        }
        pairs.sort(Comparator.comparingLong((SmartTripFarePair pair) -> pair.plain().trackerProperties.comparisonFare - pair.smartTrip().trackerProperties.comparisonFare));
        logger.log("flightIdGeneral where the SMART_TRIP comparisonFare is higher than the non-SMART_TRIP one: " + pairs.size() + " (most meaningful first)");
        return pairs;
    }

    public Set<String> collectRenderedSchedules() {
        Set<String> schedules = new LinkedHashSet<>();
        int atBottomCount = 0;
        driverTools.scrollUp(SRP_SCROLL_TO_TOP_PIXELS);
        driverTools.dummyWait(1);
        for (int attempt = 1; attempt <= SRP_MAX_SCROLL_STEPS; attempt++) {
            for (String raw : readRenderedSchedules()) {
                String[] parts = raw.split("/");
                if (parts.length >= 3) schedules.add(parts[0] + "|" + parts[2]);
            }
            driverTools.scrollDown(SRP_SCROLL_STEP_PIXELS);
            driverTools.dummyWait(1);
            atBottomCount = driverTools.isPageScrolledToBottom() ? atBottomCount + 1 : 0;
            if (atBottomCount >= SRP_BOTTOM_CONFIRMATIONS) break;
        }
        logger.log("Schedules actually rendered on SRP (" + schedules.size() + "): " + schedules);
        return schedules;
    }

    public SmartTripFarePair requireDisplayedSmartTripAndPlainFarePair() {
        return findDisplayedSmartTripAndPlainFarePair().orElseThrow(() -> new SkipException("No SRP-rendered flightIdGeneral has a SMART_TRIP comparisonFare higher than its non-SMART_TRIP one, so Smart Trip prioritisation cannot be proven on this route/date. " + streamingGroupsSummary));
    }

    public String getDisplayedPriceForPair(SmartTripFarePair pair) {
        return getDisplayedPriceForSchedule(pair.smartTrip().card.departure.time, pair.smartTrip().card.arrival.time);
    }

    public boolean displayedPriceMatchesSmartTripFare(SmartTripFarePair pair, String displayedPrice) {
        long displayed = Long.parseLong(CoreFunctions.removeNonDigit(displayedPrice));
        long smartTrip = Long.parseLong(CoreFunctions.removeNonDigit(pair.smartTrip().mainPriceText()));
        logger.log("Displayed price " + displayed + " vs SMART_TRIP fare " + smartTrip + " (allowed rounding difference: " + PRICE_ROUNDING_TOLERANCE + ")");
        return Math.abs(displayed - smartTrip) <= PRICE_ROUNDING_TOLERANCE;
    }

    public String airlineNameFor(FlightStreamV4Response.DepartureFlightV4 flight) {
        if (flight.filterProperties == null || flight.filterProperties.airlines == null || flight.filterProperties.airlines.isEmpty()) return null;
        String code = flight.filterProperties.airlines.get(0);
        return airlineNames.getOrDefault(code, code);
    }

    public Optional<SmartTripFarePair> findDisplayedSmartTripAndPlainFarePair() {
        List<SmartTripFarePair> pairs = findSmartTripAndPlainFarePairs();
        for (SmartTripFarePair pair : pairs) {
            if (pair.smartTrip().card == null || pair.smartTrip().card.departure == null || pair.smartTrip().card.arrival == null) continue;
            String schedule = pair.smartTrip().card.departure.time + "|" + pair.smartTrip().card.arrival.time;
            String airline = airlineNameFor(pair.smartTrip());
            if (airline == null) {
                logger.log("Skipping flightIdGeneral " + pair.flightIdGeneral() + ": no airline in filterProperties");
                continue;
            }
            logger.log("Filtering SRP by airline '" + airline + "' to look for flightIdGeneral " + pair.flightIdGeneral() + " schedule " + schedule.replace("|", " - "));
            try {
                resetAirlineFilter();
                filterAirline(airline);
            } catch (Exception e) {
                logger.log("Could not apply airline filter '" + airline + "': " + e.getMessage());
                continue;
            }
            if (collectRenderedSchedules().contains(schedule)) {
                logger.log("Using flightIdGeneral " + pair.flightIdGeneral() + " schedule " + schedule.replace("|", " - ") + " after filtering by " + airline);
                return Optional.of(pair);
            }
            logger.log("Schedule " + schedule.replace("|", " - ") + " still not rendered after filtering by " + airline);
        }
        return Optional.empty();
    }

    public String buildFlightSearchUrl(Map<String, String> param) {
        String explicitUrl = param.get("url");
        if (explicitUrl != null && !explicitUrl.isBlank()) {
            logger.log("Using explicit flight search URL from test data: " + explicitUrl);
            return explicitUrl;
        }
        DateTimeFormatter urlDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String departureDate = LocalDate.now().plusDays(Long.parseLong(param.get("daysToDepartureDate"))).format(urlDateFormat);
        String daysToReturnDate = param.get("daysToReturnDate");
        String returnDate = daysToReturnDate == null || daysToReturnDate.isBlank() ? "" : "&ret_date=" + LocalDate.now().plusDays(Long.parseLong(daysToReturnDate)).format(urlDateFormat);
        String url = flightSearchBaseUrl() + "en-id/flights/search?d=" + param.get("origin") + "&a=" + param.get("destination") + "&date=" + departureDate + "&adult=" + param.get("adult") + "&child=" + param.get("child") + "&infant=" + param.get("infant") + "&class=economy&dType=AIRPORT&aType=AIRPORT&dLabel=" + param.get("originLabel") + "&aLabel=" + param.get("destinationLabel") + "&type=depart" + returnDate;
        logger.log("Built flight search URL: " + url);
        return url;
    }

    private String flightSearchBaseUrl() {
        return switch (Configs.ENVIRONMENT) {
            case GK -> "https://gatotkaca.tiket.com/";
            case PREPROD -> "https://preprod.tiket.com/";
            case PROD -> "https://www.tiket.com/";
            default -> throw new UnsupportedOperationException("Unimplemented environment for flight search URL!");
        };
    }

    private static final Locator CARD_SCHEDULE_TIMES = new Locator("CARD_SCHEDULE_TIMES", new By.ByXPath(".//div[contains(@class,'ScheduleTime_endpoint')]//p"));

    private List<String> readRenderedSchedules() {
        List<String> schedules = new ArrayList<>();
        for (WebElement card : driverTools.elements(FLIGHT_CARDS, 1).orElse(Collections.emptyList())) {
            List<WebElement> times = driverTools.elements(CARD_SCHEDULE_TIMES, card).orElse(Collections.emptyList());
            schedules.add(times.stream().map(WebElement::getText).map(String::trim).collect(Collectors.joining("/")));
        }
        return schedules;
    }

    public String getDisplayedPriceForSchedule(String departTime, String arriveTime) {
        String schedule = departTime + " - " + arriveTime;
        Locator cardsForSchedule = new Locator("FLIGHT_CARD_BY_SCHEDULE", new By.ByXPath(String.format(CARD_BY_SCHEDULE_XPATH, departTime, arriveTime)));
        Set<String> schedulesSeen = new LinkedHashSet<>();
        int atBottomCount = 0;

        driverTools.scrollUp(SRP_SCROLL_TO_TOP_PIXELS);
        driverTools.dummyWait(1);

        for (int attempt = 1; attempt <= SRP_MAX_SCROLL_STEPS; attempt++) {
            schedulesSeen.addAll(readRenderedSchedules());
            List<WebElement> cards = driverTools.elements(cardsForSchedule, 1).orElse(Collections.emptyList());
            if (!cards.isEmpty()) {
                logger.log("SRP card for schedule " + schedule + " found after " + attempt + " scroll steps (" + cards.size() + " match); reading the last one");
                WebElement lastCard = cards.get(cards.size() - 1);
                driverTools.scrollIntoWebView(lastCard);
                String price = driverTools.element(PRICE_WITHIN_CARD, lastCard).orElseThrow(() -> new NoSuchElementException("No price found on the SRP card for schedule " + schedule)).getText();
                logger.log("SRP card price for schedule " + schedule + ": " + price);
                return price;
            }
            driverTools.scrollDown(SRP_SCROLL_STEP_PIXELS);
            driverTools.dummyWait(1);
            atBottomCount = driverTools.isPageScrolledToBottom() ? atBottomCount + 1 : 0;
            if (atBottomCount >= SRP_BOTTOM_CONFIRMATIONS) {
                logger.log("Reached bottom of SRP after " + attempt + " scroll steps");
                break;
            }
        }

        throw new NoSuchElementException("No SRP card rendered for schedule " + schedule + ". Distinct schedules seen while scrolling the whole SRP (" + schedulesSeen.size() + "): " + schedulesSeen);
    }

    public void selectFirstFlightWithRoundTripDealsLabel() {
        selectFirstFlightWithRoundTripDealsLabel(null);
    }

    /**
     * Departure SRP cards no longer render a visible "Round-trip Deals" tag; apply the quick
     * filter first so only SMART_TRIP fares remain, then select the first card or a specific airline.
     */
    public void selectFirstFlightWithRoundTripDealsLabel(String airline) {
        driverTools.enableFlightSearchStreamingCapture();
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToAppear(FLIGHT_FIRST_CARD);

        roundTripDealsFilter();

        if (airline != null && !airline.isBlank()) {
            selectFlightByAirline(airline);
        } else {
            driverTools.waitForElementToPresence(FLIGHT_FIRST_CARD);
            driverTools.scrollIntoWebView(FLIGHT_FIRST_CARD);
            driverTools.clickWithJavascriptExecutor(FLIGHT_FIRST_CARD);
        }
        driverTools.enableFlightSearchStreamingCapture();
        driverTools.pollFlightStreamingCapturesFromNetworkLogs();
    }

    public long getRoundTripDealsDepartureDeltaPriceFromUi() {
        assertRoundTripDealsDepartureDeltaBlockVisible();
        driverTools.scrollIntoWebView(ROUND_TRIP_DEALS_DEPARTURE_DELTA_PRICE_VALUE);
        String deltaText = driverTools.getText(ROUND_TRIP_DEALS_DEPARTURE_DELTA_PRICE_VALUE)
                .orElseThrow(() -> new RuntimeException("Round-trip Deals departure delta price (+XXX) is not visible on SRP"));
        logger.log("Round-trip Deals departure delta price on UI: " + deltaText);
        return Long.parseLong(CoreFunctions.removeNonDigit(deltaText));
    }

    public long findMatchingDepartureFlightDeltaPriceFromReturnStreamingResponses(long uiDeltaPrice) {
        driverTools.enableFlightSearchStreamingCapture();
        Optional<Long> matchedDelta = driverTools.waitForMatchingDepartureFlightDeltaPrice(
                "RT_RETURN_SMART_TRIP", uiDeltaPrice, 30);
        int captureCount = driverTools.getFlightSearchStreamingCaptures().size();
        int returnCaptureCount = driverTools.countStreamingCapturesByType("RT_RETURN_SMART_TRIP");
        logger.log("Flight streaming captures available: " + captureCount + " (RT_RETURN_SMART_TRIP=" + returnCaptureCount + ")");
        if (matchedDelta.isEmpty()) {
            java.util.List<Long> capturedDeltas = driverTools.getDepartureFlightDeltaPricesByStreamType("RT_RETURN_SMART_TRIP");
            throw new RuntimeException("No valid departureFlightDeltaPrice captured from the latest RT_RETURN_SMART_TRIP streaming response. UI delta="
                    + uiDeltaPrice + " (captures=" + captureCount + ", RT_RETURN_SMART_TRIP=" + returnCaptureCount
                    + ", capturedDepartureFlightDeltaPrices=" + capturedDeltas + ")");
        }
        logger.log("departureFlightDeltaPrice matched from return streaming response: " + matchedDelta.get());
        return matchedDelta.get();
    }

    public void skipRefund100P() {
        driverTools.waitForElementToAppear(REFUND_100P_SKIP_POPUP,2);
        driverTools.waitForElementToDisappear(REFUND_100P_SKIP_POPUP,5);
        if (driverTools.isElementDisplayed(REFUND_100P_SKIP_POPUP)) {
            driverTools.clickWithJavascriptExecutor(REFUND_100P_SKIP_POPUP);
        } else {
            logger.log("100% Refund skip popup is not displayed.");
        }
        driverTools.waitForElementToDisappear(REFUND_100P_SKIP_POPUP);
    }

    public void openAirlineFilter(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        if (isAirlineFilterDropdownOpen()) {
            return;
        }
        ensureQuickFilterChipVisible(AIRLINES_FILTER);
        driverTools.waitForElementToAppear(AIRLINES_FILTER, 15);
        driverTools.clickWithJavascriptExecutor(AIRLINES_FILTER);
        if (waitForAirlineFilterDropdownOpen(10).isEmpty()) {
            driverTools.clickWithJavascriptExecutor(AIRLINES_FILTER);
            waitForAirlineFilterDropdownOpen(10).orElseThrow(() ->
                    new NoSuchElementException("Airline filter dropdown did not open"));
        }
    }

    private boolean isAirlineFilterDropdownOpen() {
        return driverTools.isElementDisplayed(AIRLINE_FILTER_DROPDOWN)
                || driverTools.isElementDisplayed(SAVE_AIRLINE_FILTER);
    }

    private Optional<WebElement> waitForAirlineFilterDropdownOpen(int seconds) {
        Optional<WebElement> dropdown = driverTools.waitForElementToAppear(AIRLINE_FILTER_DROPDOWN, seconds);
        if (dropdown.isPresent()) {
            return dropdown;
        }
        return driverTools.waitForElementToAppear(SAVE_AIRLINE_FILTER, seconds);
    }

    public void saveAirlineFilter(){
        driverTools.waitForElementToAppear(SAVE_AIRLINE_FILTER, 10);
        driverTools.scrollIntoWebView(SAVE_AIRLINE_FILTER);
        driverTools.clickWithJavascriptExecutor(SAVE_AIRLINE_FILTER);
        driverTools.waitForElementToDisappear(AIRLINE_FILTER_DROPDOWN, 15);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
    }

    private void ensureQuickFilterChipVisible(Locator chipLocator) {
        if (driverTools.isElementDisplayed(chipLocator)) {
            return;
        }
        int counter = 0;
        while (counter < 5
                && driverTools.element(NEXT_BUTTON_QUICK_FILTER).isPresent()
                && !driverTools.isElementDisplayed(chipLocator)) {
            driverTools.clickElementByCoordinateDWeb(NEXT_BUTTON_QUICK_FILTER);
            counter++;
        }
    }

    private boolean expandAirlineFilterShowMoreIfPresent() {
        if (!driverTools.isElementDisplayed(AIRLINE_FILTER_SHOW_MORE)) {
            return false;
        }
        String showMoreLabel = driverTools.getText(AIRLINE_FILTER_SHOW_MORE).orElse("Show more").trim();
        logger.log("Expanding airline filter via: " + showMoreLabel);
        driverTools.clickWithJavascriptExecutor(AIRLINE_FILTER_SHOW_MORE);
        driverTools.dummyWait(1);
        return true;
    }

    private void expandAirlineFilterUntilOptionVisible(Locator option) {
        int expandClicks = 0;
        while (driverTools.isElementDisplayed(AIRLINE_FILTER_SHOW_MORE) && expandClicks < AIRLINE_FILTER_SHOW_MORE_SAFETY_CAP) {
            if (driverTools.isElementDisplayed(option) || tryRevealAirlineFilterOption(option)) {
                return;
            }
            if (!expandAirlineFilterShowMoreIfPresent()) {
                return;
            }
            expandClicks++;
        }
    }

    private boolean isAirlineFilterOptionDisplayed(String airlineName) {
        Locator option = airlineNameLocator(airlineName);
        if (driverTools.isElementDisplayed(option)) {
            return true;
        }

        if (tryRevealAirlineFilterOption(option)) {
            return true;
        }

        expandAirlineFilterUntilOptionVisible(option);
        if (driverTools.isElementDisplayed(option) || tryRevealAirlineFilterOption(option)) {
            return true;
        }

        logger.log("Airline filter option not visible: " + airlineName);
        return false;
    }

    private boolean tryRevealAirlineFilterOption(Locator option) {
        if (driverTools.isElementDisplayed(option)) {
            return true;
        }
        try {
            driverTools.scrollUntilVisibleInElement(AIRLINE_FILTER_DROPDOWN, option, 200, 4);
            return driverTools.isElementDisplayed(option);
        } catch (Exception e) {
            logger.log("Could not reveal airline filter option via scroll in dropdown: " + e.getMessage());
            return false;
        }
    }

    private void selectAirlineFilterOption(String airlineName) {
        if (!isAirlineFilterOptionDisplayed(airlineName)) {
            throw new NoSuchElementException("Airline filter option not found: " + airlineName);
        }
        driverTools.clickWithJavascriptExecutor(airlineNameLocator(airlineName));
    }

    public void filterAirlineForNewUI(String airlineName){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        driverTools.waitForElementToPresence(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);

        driverTools.waitForElementToPresence(AIRLINES_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(AIRLINES_FILTER_BUTTON);

        driverTools.waitForElementToPresence(SEE_MORE_AIRLINES);
        driverTools.clickWithJavascriptExecutor(SEE_MORE_AIRLINES);

        logger.log("Airline to select : " + airlineName);
        selectAirlineFilterOptionInFilter(airlineName);

        if(driverTools.isElementDisplayed(SAVE_OTHER_FILTER)) {
            driverTools.waitForElementToPresence(SAVE_OTHER_FILTER);
            driverTools.clickWithJavascriptExecutor(SAVE_OTHER_FILTER);
        }

        driverTools.waitForElementToPresence(SEE_FLIGHTS);
        driverTools.clickWithJavascriptExecutor(SEE_FLIGHTS);

    }

    public void selectAirlineFilterOptionInFilter(String airlineName){
        Locator AIRLINE_CHECKBOX = new Locator("AIRLINE_CHECKBOX_" + airlineName, By.xpath("(//*[contains(@class,'FilterOptionRow')]//*[contains(text(),'" + airlineName + "')])[last()]"));
        driverTools.waitForElementToPresence(AIRLINE_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(AIRLINE_CHECKBOX);
    }

    /*
    * if you want to filter for multiple airline pass airline data as
    * Lion air-AirAsia-Garuda
    * if you want to select only one just pass as
    * Lion Air*/
    public void filterAirline(String airlineName){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        if(!driverTools.isElementDisplayed(AIRLINES_FILTER)){
            filterAirlineForNewUI(airlineName);
            return;
        }
        openAirlineFilter();
        String[] airlinesArray;
        if (airlineName.contains("-")) {
            airlinesArray = airlineName.split("-"); // Split by "-"
        } else {
            airlinesArray = new String[] { airlineName }; // Create a single-element array
        }

        if(airlinesArray.length==1){
            String airline = airlinesArray[0];
            logger.log("Airline to select : " + airline);
            airline = CoreFunctions.titleize(airline);
            if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
            Assertion.assertThat("is airline name { " + airline + " } displayed", isAirlineFilterOptionDisplayed(airline), is(true));
            selectAirlineFilterOption(airline);
        }else {
            for(String airline: airlinesArray){
                logger.log("Airline to select : " + airline);
                airline = CoreFunctions.titleize(airline);
                if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
                if (isAirlineFilterOptionDisplayed(airline)) {
                    selectAirlineFilterOption(airline);
                }
            }
        }
        saveAirlineFilter();
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
    }

    public void filterAirline(ArrayList<String> airlines){
        openAirlineFilter();
        ArrayList<String> sortedAirlines = new ArrayList<>(airlines);
        Collections.sort(sortedAirlines);

        Map<String, Boolean> selectedAirlines = new LinkedHashMap<>();
        for (String airlineName : sortedAirlines) {
            String airline = CoreFunctions.titleize(airlineName);
            if (airline.contains("nam")) {
                airline = airline.replace("Nam", "NAM");
            }
            String key = airline.toLowerCase(Locale.ROOT);
            if (selectedAirlines.containsKey(key)) {
                continue;
            }
            if (isAirlineFilterOptionDisplayed(airline)) {
                logger.log("Selecting airline from filter: " + airline);
                selectAirlineFilterOption(airline);
                selectedAirlines.put(key, true);
            }
        }

        if (selectedAirlines.isEmpty()) {
            throw new NoSuchElementException("required airline is not present " + airlines);
        }
        saveAirlineFilter();
    }

    public void resetAirlineFilter(){
        openAirlineFilter();
        driverTools.clickWithJavascriptExecutor(RE_SET_FILTER);
        saveAirlineFilter();
    }

    private Locator airlineNameLocator(String airlineName){
        String airlineKey = airlineName.toLowerCase(Locale.ROOT);
        String airlineKeyLit = CoreFunctions.toXPathLiteral(airlineKey);
        String airlineKeyWithCountLit = CoreFunctions.toXPathLiteral(airlineKey + " (");
        String airlineNameLit = CoreFunctions.toXPathLiteral(airlineName);
        String airlineNameWithCountLit = CoreFunctions.toXPathLiteral(airlineName + " (");
        return new Locator("AIRLINE_NAME_LOCATOR", new By.ByXPath("//div[contains(@class,'DropdownAirlines') or contains(@class,'BaseDropdown')]//label[contains(@class,'FilterOptionRow_row')][.//span[contains(@class,'FilterOptionRow_label_wrap')]//span[starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + airlineKeyLit + ") or starts-with(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," + airlineKeyWithCountLit + ")]] | //div[contains(@class,'BaseDropdown')]//label[contains(@class,'FilterForm_item')][.//span[starts-with(normalize-space(.)," + airlineNameWithCountLit + ") or normalize-space(.)=" + airlineNameLit + " or starts-with(normalize-space(.)," + airlineNameLit + ")]] | //*[contains(@class,'FilterForm_label')]//span[contains(normalize-space(.)," + airlineNameLit + ")]"));
    }

    public void selectFlight() {
        try {
            driverTools.enableFlightSearchStreamingCapture();
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(FLIGHT_FIRST_CARD);
            driverTools.scrollIntoWebView(FLIGHT_FIRST_CARD);
            driverTools.clickWithJavascriptExecutor(FLIGHT_FIRST_CARD);
        } catch (Exception e) {
            driverTools.waitForElementToPresence(FLIGHT_FIRST_CARD);
            driverTools.clickWithJavascriptExecutor(FLIGHT_FIRST_CARD);
        }
    }


    public void selectFlightSecondCard(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToPresence(FLIGHT_CARD2);
        driverTools.clickWithJavascriptExecutor(FLIGHT_CARD2);
    }

    public void selectFlightByAirline(String airline){
        airline = CoreFunctions.titleize(airline);
        if (airline.contains("nam")) airline = airline.replace("Nam", "NAM");
        try{
            driverTools.waitForElementToPresence(PROGRESS_BAR_LOADING,3);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineFlightCardLocator(airline));
            driverTools.clickWithJavascriptExecutor(airlineFlightCardLocator(airline));
        }catch(Exception e){
            logger.log("Exception retrying....");
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineFlightCardLocator(airline));
            driverTools.clickWithJavascriptExecutor(airlineFlightCardLocator(airline));
        }
    }

    public String selectFlightByAirlineAndGetName(ArrayList<String> airline){
        String airlineAvailable = "",selectedAirline="";
        Locator airlineLocator = null;
        for (int i=0;i<airline.size();i++){
            airlineAvailable = airline.get(i);
            if (airlineAvailable.contains("nam")) airlineAvailable = airlineAvailable.replace("Nam", "NAM");
            airlineAvailable = CoreFunctions.titleize(airlineAvailable);

            airlineLocator = airlineFlightCardLocator(airlineAvailable);
            if(driverTools.waitForElementToPresence(airlineLocator).isPresent()){
                logger.log("Airline available to select: " + airlineAvailable);
                selectedAirline = airlineAvailable;
                break;
            }
        }

        if (airlineLocator == null || selectedAirline.isEmpty()) {
            throw new NoSuchElementException("No flight card found for airlines: " + airline);
        }

        try{
            driverTools.waitForElementToPresence(PROGRESS_BAR_LOADING,3);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineLocator);
            driverTools.clickWithJavascriptExecutor(airlineLocator);
        }catch(Exception e){
            logger.log("Exception retrying....");
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineLocator);
            driverTools.clickWithJavascriptExecutor(airlineLocator);
        }
        return selectedAirline;
    }

    public void selectFlightByAirlineWithoutInclusiveBaggage(String airline){
        airline = CoreFunctions.titleize(airline);
        try{
            driverTools.waitForElementToPresence(PROGRESS_BAR_LOADING,3);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineFlightCardLocatorWithoutInclusiveBaggage(airline));
            driverTools.clickWithJavascriptExecutor(airlineFlightCardLocatorWithoutInclusiveBaggage(airline));
        }catch(Exception e){
            logger.log("Exception retrying....");
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            driverTools.waitForElementToPresence(airlineFlightCardLocatorWithoutInclusiveBaggage(airline));
            driverTools.clickWithJavascriptExecutor(airlineFlightCardLocatorWithoutInclusiveBaggage(airline));
        }
    }

    private Locator airlineFlightCardLocator(String airlineName){
        return new Locator("AIRLINE_FLIGHT_CARD_LOCATOR", new By.ByXPath(
                "(//div[@data-testid='srp-flight-card'][.//div[contains(@class,'AirlineSection_name_row')]"
                        + "//p[contains(normalize-space(.),'" + airlineName + "')]])[1]"
                        + " | (//*[contains(@class,'AirlinesAndFacilities_airline') and contains(text(),'" + airlineName + "')])[1]"));
    }

    private Locator airlineFlightCardLocatorWithoutInclusiveBaggage(String airlineName) {
        return new Locator("AIRLINE_FLIGHT_CARD_LOCATOR_WITHOUT_INCLUSIVE_BAGGAGE",
                new By.ByXPath("(//div[@data-testid='srp-flight-card'][.//div[contains(@class,'AirlineSection_name_row')]"
                        + "//p[contains(normalize-space(.),'" + airlineName + "')]"
                        + " and not(.//img[contains(@alt,'Baggage') or contains(@alt,'baggage') or @alt='Check-in baggage'])])[1]"
                        + " | (//*[contains(@class,'AirlinesAndFacilities_airline')"
                        + " and .//span[contains(text(),'" + airlineName + "')]"
                        + " and not(.//img[@alt='Check-in baggage'])])[1]"));
    }

    public int selectAirlineAndGetNumberOfFlights(String airlineName){
        airlineName = CoreFunctions.titleize(airlineName);
        if (airlineName.contains("nam")) airlineName = airlineName.replace("Nam", "NAM");
        Assertion.assertThat("is airline name { " + airlineName + " } displayed", isAirlineFilterOptionDisplayed(airlineName), is(true));
        selectAirlineFilterOption(airlineName);
        WebElement numberOfFlightsElement = driverTools.waitForElementToAppear(SAVE_AIRLINE_FILTER).orElseThrow();
        String numberOfFlights = driverTools.getText(numberOfFlightsElement);
        logger.log("number of flights: " + numberOfFlights);

        int flights = Integer.getInteger(numberOfFlights.replaceAll("\\D", ""));
        logger.log("only number of flight: " + flights);

        return flights;
    }

    public boolean isMeetTravelPolicy(){
        driverTools.waitForElementsToAppear(PROGRESS_BAR_LOADING,6);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        if(!driverTools.isElementDisplayed(MEET_TRAVEL_POLICY)){
            return false;
        }

        List<WebElement> flightCardsWithMeetTravelPolicy = driverTools.elements(MEET_TRAVEL_POLICY).orElseThrow();
        int numberOfFlightCardsWithMeetTravelPolicy = flightCardsWithMeetTravelPolicy.size();
        logger.log("numberOfFlightCardsWithMeetTravelPolicy: " + numberOfFlightCardsWithMeetTravelPolicy);

        return numberOfFlightCardsWithMeetTravelPolicy != 0;
    }

    public boolean isSearchResultPageEmptyForEconomy(String currency){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        String subHeader = driverTools.getText(NUMBER_OF_PASSENGER_AND_SEAT_TYPE).orElseThrow();
        boolean isSearchSubHeaderShowsEconomy = (subHeader.contains("Economy") || subHeader.contains("Ekonomi") )&& !subHeader.contains("Premium");
        logger.log("sub header : " + subHeader);
        Assertion.assertThat("sub header contains only Economy",isSearchSubHeaderShowsEconomy,is(true));

        String priceSelectedDate = driverTools.getText(PRICE_SELECTED_DATE_ANY).orElseThrow();
        boolean isPriceSelectedDateNotContainsCurrency = !priceSelectedDate.contains(currency);
        logger.log("price selected date : " + priceSelectedDate);
        Assertion.assertThat("Price selected date not contains currency",isPriceSelectedDateNotContainsCurrency,is(true));

        boolean isChangeSearchBtnDisplayed = driverTools.isElementDisplayed(CHANGE_SEARCH_BTN_SRP);
        logger.log("is change search btn displayed : " + isChangeSearchBtnDisplayed);
        Assertion.assertThat("Change search btn displayed",isChangeSearchBtnDisplayed,is(true));

        List<WebElement> flightCards = driverTools.elements(FLIGHT_CARD_ALL).orElseThrow();
        int numberOfFlightCards = flightCards.size();
        logger.log("numberOfFlightCards: " + numberOfFlightCards);

        return isSearchSubHeaderShowsEconomy && isPriceSelectedDateNotContainsCurrency && isChangeSearchBtnDisplayed && numberOfFlightCards == 0;
    }

    public void backToFlightPage(){
        driverTools.back();
    }

    public boolean isSearchResultShownForPremiumEconomy(String currency){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        String subHeader = driverTools.getText(NUMBER_OF_PASSENGER_AND_SEAT_TYPE).orElseThrow();
        boolean isSearchSubHeaderShowsPremiumEconomy = subHeader.contains("Premium");
        logger.log("subHeader title : " + subHeader);
        Assertion.assertThat("sub header contains Premium",isSearchSubHeaderShowsPremiumEconomy,is(true));

        String priceSelectedDate = driverTools.getText(PRICE_SELECTED_DATE_ANY).orElseThrow();
        if (!isDateStripPriceLoaded(priceSelectedDate)) {
            priceSelectedDate = waitForActiveDatePickerPrice(false);
        }
        boolean isPriceSelectedDateContainsCurrency = priceSelectedDate.contains(currency);
        logger.log("price selected date : " + priceSelectedDate);
        Assertion.assertThat("Price selected date contains currency",isPriceSelectedDateContainsCurrency,is(true));

        List<WebElement> flightCards = driverTools.elements(FLIGHT_CARD_ALL).orElseThrow();
        int numberOfFlightCards = flightCards.size();
        logger.log("numberOfFlightCards: " + numberOfFlightCards);

        return isSearchSubHeaderShowsPremiumEconomy && isPriceSelectedDateContainsCurrency && numberOfFlightCards > 0;
    }

    public List<String> getAirlineNames(String airline1, String airline2) {
        List<String> airlineNames = new ArrayList<>();
        int maxRetries = 3;
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                driverTools.waitForElementToAppear(AIRLINE_NAME, 10);
                List<WebElement> airlineElements = driverTools.elements(AIRLINE_NAME)
                        .orElseThrow(() -> new RuntimeException("Airline elements not found"));
                airlineNames = airlineElements.stream()
                        .map(WebElement::getText)
                        .map(name -> name == null ? "" : name.trim())
                        .collect(Collectors.toList());
                logger.log("Number of airline elements found: " + airlineNames.size());
                String airline1Trimmed = airline1 == null ? "" : airline1.trim();
                String airline2Trimmed = airline2 == null ? "" : airline2.trim();
                for (String name : airlineNames) {
                    logger.log("Found airline: " + name);
                    if (name.contains(airline1Trimmed) || name.contains(airline2Trimmed)) {
                        logger.log("Given airlines '" + airline1Trimmed + "' or '" + airline2Trimmed + "' found.");
                    }
                }
                Assertion.assertThat("Airline names should not be null or empty",
                        airlineNames, CoreMatchers.is(CoreMatchers.not(empty())));
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.log("StaleElementReferenceException encountered. Retrying... Attempt: " + attempts);
            } catch (Exception e) {
                logger.log("Unexpected error occurred: " + e.getMessage());
                throw new RuntimeException("Failed to retrieve airline names due to unexpected error.", e);
            }
        }

        if (airlineNames.isEmpty()) {
            logger.log("Unable to fetch airline names after " + maxRetries + " retries.");
            throw new RuntimeException("Failed to retrieve airline names after retries.");
        }

        return airlineNames;
    }

    public ArrayList<String> getAirlineNames(ArrayList<String> airlines) {
        ArrayList<String> airlineNames = new ArrayList<>();
        int maxRetries = 3;
        int attempts = 0;

        while (attempts < maxRetries) {
            try {
                driverTools.waitForElementToAppear(AIRLINE_NAME, 10);
                List<WebElement> airlineElements = driverTools.elements(AIRLINE_NAME)
                        .orElseThrow(() -> new RuntimeException("Airline elements not found"));
                for (WebElement airlineElement : airlineElements) {
                    String airlineName = driverTools.getText(airlineElement);

                    if (airlineName == null) continue;
                    airlineName = airlineName.replaceAll("\\s+", " ").trim();
                    if (airlineName.isEmpty()) continue;

                    if (airlineName.contains("+")) {
                        String[] parts = airlineName.split("\\s*\\+\\s*");
                        for (String name : parts) {
                            name = name.trim();
                            if (!name.isEmpty()) airlineNames.add(name);
                        }
                    } else {
                        airlineNames.add(airlineName);
                    }
                }

                logger.log("Number of airline elements found: " + airlineNames.size());
                for (String name : airlineNames) {
                    logger.log("Found airline: " + name);
                    if (airlines.contains(name)) {
                        logger.log("Given airlines '" + airlines + "' found.");
                    }
                }

                Assertion.assertThat("Airline names should not be null or empty",
                        airlineNames, CoreMatchers.is(CoreMatchers.not(empty())));
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
                logger.log("StaleElementReferenceException encountered. Retrying... Attempt: " + attempts);
            } catch (Exception e) {
                logger.log("Unexpected error occurred: " + e.getMessage());
                throw new RuntimeException("Failed to retrieve airline names due to unexpected error.", e);
            }
        }

        if (airlineNames.isEmpty()) {
            logger.log("Unable to fetch airline names after " + maxRetries + " retries.");
            throw new RuntimeException("Failed to retrieve airline names after retries.");
        }

        return airlineNames;
    }

    public boolean isCorporateDealsBannerPresent() {
        return driverTools.waitForElementToPresence(CORPORATE_DEALS_BANNER,20).isPresent();
    }

    public String getActiveDate() {
        return driverTools.getText(ACTIVE_DATE).orElseThrow();
    }

    public boolean gotoCorporateDealsBenefitCard() {
        return  driverTools.waitForElementToPresence(CORPORATE_DEALS_BENEFIT_CARD,5).isPresent();
    }

    public void goBackOnLandingPage(){

        driverTools.back();
        driverTools.waitForElementToDisappear(CHANGE_DATE,20);
    }


    public void selectTicket(int productNumberIndex) {
        productNumberIndex = productNumberIndex - 1;
        driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON);
        List<WebElement> selectTicketButtons = driverTools.elements(SELECT_TICKET_BUTTON).orElseThrow();
        if(productNumberIndex > selectTicketButtons.size() - 1) {
            throw new IndexOutOfBoundsException("Product number index is out of bounds. Available products: " + selectTicketButtons.size());
        }
        driverTools.click(selectTicketButtons.get(productNumberIndex));
    }

    public boolean isSearchResultPagePresent(){
        return driverTools.waitForElementToAppear(SearchResultLocator).isPresent();
    }

    public void openTransitFilter(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        ensureQuickFilterChipVisible(TRANSIT_FILTER);
        if(!driverTools.isElementDisplayed(TRANSIT_FILTER)){
            openTransitFilterInFilter();
            return;
        }
        driverTools.waitForElementToAppear(TRANSIT_FILTER, 15);
        driverTools.clickWithJavascriptExecutor(TRANSIT_FILTER);
        driverTools.waitForElementToAppear(TRANSIT_POPUP, 10).orElseThrow();
    }

    public void openTransitFilterInFilter() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        driverTools.waitForElementToPresence(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);

        driverTools.waitForElementToPresence(TRANSIT_FILTER_BUTTON);
        driverTools.clickWithJavascriptExecutor(TRANSIT_FILTER_BUTTON);
    }

    public void CheckTransit(){
        openTransitFilter();
        driverTools.waitForElementToAppear(TRANSIT_POPUP);
        driverTools.scrollIntoWebView(CHECKBOX_TRANSIT_TWO);
        driverTools.waitForElementToAppear(CHECKBOX_TRANSIT_ONE);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TRANSIT_ONE);
        driverTools.waitForElementToAppear(CHECKBOX_TRANSIT_TWO);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TRANSIT_TWO);
        driverTools.waitForElementToAppear(SAVE_TRANSIT_FILTER);
        driverTools.clickWithJavascriptExecutor(SAVE_TRANSIT_FILTER);
        driverTools.waitForElementToAppear(FLIGHT_CARDS);
    }

    public void selectFlightWithSingleTransit() {
        openTransitFilter();
        driverTools.waitForElementToAppear(CHECKBOX_TRANSIT_ONE,5);
        driverTools.clickWithJavascriptExecutor(CHECKBOX_TRANSIT_ONE);
        if(driverTools.isElementDisplayed(SAVE_TRANSIT_FILTER)){
            driverTools.waitForElementToAppear(SAVE_TRANSIT_FILTER);
            driverTools.clickWithJavascriptExecutor(SAVE_TRANSIT_FILTER);
        }
        else{
            driverTools.waitForElementToAppear(SEE_FLIGHTS);
            driverTools.clickWithJavascriptExecutor(SEE_FLIGHTS);
        }
    }

    public void selectDirectFlightFilter() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 30);
        driverTools.dummyWait(2);
        ensureDirectFlightFilterVisible();
        driverTools.scrollIntoWebView(DIRECT_FLIGHT_FILTER);
        driverTools.waitForElementToAppear(DIRECT_FLIGHT_FILTER, 15);
        driverTools.clickElementByCoordinateDWeb(DIRECT_FLIGHT_FILTER);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 30);
    }

    private void ensureDirectFlightFilterVisible() {
        if (driverTools.isElementDisplayed(DIRECT_FLIGHT_FILTER)) {
            return;
        }
        int counter = 0;
        while (counter < 5
                && driverTools.element(NEXT_BUTTON_QUICK_FILTER).isPresent()
                && !driverTools.isElementDisplayed(DIRECT_FLIGHT_FILTER)) {
            driverTools.clickElementByCoordinateDWeb(NEXT_BUTTON_QUICK_FILTER);
            counter++;
        }
    }

    public void selectTransitFilter(String transitType) {
        openTransitFilter();
        Locator transitTypeLocator = getTransitTypeLocator(transitType);
        driverTools.waitForElementToAppear(transitTypeLocator);
        driverTools.clickWithJavascriptExecutor(transitTypeLocator);
        saveAirlineFilter();
    }

    private Locator getTransitTypeLocator(String transitType) {
        switch (transitType) {
            case "direct":
                return new Locator("DIRECT_FILTER", new By.ByXPath("//*[contains(@class,'FilterForm_item')]//span[normalize-space()='Direct' or normalize-space()='Langsung']"));
            default:
                throw new UnsupportedOperationException("Unsupported transit type: " + transitType);
        }
    }

    public abstract void clickLowestPriceGuarantee();

    public void roundTripDealsFilter() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToPresence(ROUNDTRIP_DEALS_FILTER);
        driverTools.waitForElementToAppear(NEXT_BUTTON_QUICK_FILTER, 10);

        int counter = 0;
        while (driverTools.element(NEXT_BUTTON_QUICK_FILTER).isPresent() && counter < 3) {
            driverTools.clickElementByCoordinateDWeb(NEXT_BUTTON_QUICK_FILTER);
            if (driverTools.waitForElementToDisappear(NEXT_BUTTON_QUICK_FILTER)) break;
            counter++;
        }
        driverTools.waitForElementToAppear(ROUNDTRIP_DEALS_FILTER).orElseThrow();
        driverTools.click(ROUNDTRIP_DEALS_FILTER);
        driverTools.waitForElementToAppear(FLIGHT_FIRST_CARD);
        driverTools.waitForElementToAppear(NEXT_BUTTON_QUICK_FILTER, 2);
    }

    public String getDepartureOrArrivalTime(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Parameter 'type' must not be null");
        }
        driverTools.waitForElementToDisappear(SearchResultLocator, 5);

        List<WebElement> element = driverTools.elements(FLIGHT_FIRST_CARD_DEPART_ARRIVAL_TIME).orElseThrow();
        switch (type.toLowerCase()) {
            case "departure" -> {
                return element.get(0).getText();
            }
            case "arrival" -> {
                return element.get(1).getText();
            }
            default -> throw new UnsupportedOperationException("Unsupported trip type: " + type);
        }
    }

    public void clickOnChangeButton() {
        driverTools.waitForElementToPresence(CHANGE_BUTTON).ifPresent(driverTools::click);
    }

    public void otherFilters(String otherFilter) {
        Locator specialFilter=specialFilter(otherFilter);
        driverTools.waitForElementToAppear(SEE_MORE_OTHER_FILTER,3);
        driverTools.clickWithJavascriptExecutor(SEE_MORE_OTHER_FILTER);
        driverTools.scrollUntilElementFound(specialFilter);
        driverTools.click(specialFilter);
        driverTools.clickWithJavascriptExecutor(SAVE);
    }

    public void viFlightFilter(){
        driverTools.waitForElementToAppear(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);
        driverTools.waitForElementToAppear(OTHER_FILTER);
        driverTools.clickWithJavascriptExecutor(OTHER_FILTER);
    }

    public boolean viPresentOnSrp(String srpViNudges){
        Locator srpViNudgesCheck=srpViNudges(srpViNudges);
        return driverTools.waitForElementToAppear(srpViNudgesCheck).isPresent();
    }

    public void clickShowDetails(){
        driverTools.waitForElementToAppear(SHOW_DETAILS);
        driverTools.click(SHOW_DETAILS);
    }

    public boolean verifyLowestPriceGuaranteeTag() {
        return driverTools.waitForElementToAppear(LOWEST_PRICE_GUARANTEE_TAG).isPresent();
    }

    public String fetchURL(){
        String url = driverTools.getCurrentURL();
        return url;
    }

    public void clickFloatingPromoButton() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToAppear(FLOATING_PROMO_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(FLOATING_PROMO_BUTTON);
    }


    public boolean isPromoPageDisplayed() {
        return driverTools.waitForElementToAppear(PROMO_PAGE_CONTAINER, 15).isPresent();
    }


    public void closePromoPage(String url) {
        driverTools.waitForElementToAppear(PROMO_PAGE_BACK_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(PROMO_PAGE_BACK_BUTTON);
        driverTools.waitForElementToDisappear(PROMO_PAGE_CONTAINER);
        if (driverTools.isElementDisplayed(PROMO_PAGE_CONTAINER)) driverTools.openNewTabAndPasteUrl(url);
    }


    public void selectBaggageFilter() {
        driverTools.waitForElementToAppear(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);

        driverTools.waitForElementToAppear(FACILITIES_SECTION);
        driverTools.clickWithJavascriptExecutor(FACILITIES_SECTION);

        driverTools.waitForElementToAppear(BAGGAGE_CHECKBOX);
        driverTools.clickWithJavascriptExecutor(BAGGAGE_CHECKBOX);

        driverTools.waitForElementToAppear(FILTER_SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(FILTER_SAVE_BUTTON);

        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToAppear(FLIGHT_CARDS);
    }


    public String getCurrentUrlSRP(){
        String url = driverTools.getCurrentURL();
        logger.log("current url: " + url);
        return url;
    }

    public void openSRPFromUrl(String url){
        logger.log("open srp from url: " + url);
        driverTools.get(url);
    }

    public void clickPromoIcon(){
        driverTools.waitForElementToAppear(PROMO_FLOATING_BUTTON);
        driverTools.clickWithJavascriptExecutor(PROMO_FLOATING_BUTTON);
    }

    /**
     * Gets all available airlines from the airline filter
     * @return List of airline names available in the filter
     */
    public List<String> getAvailableAirlinesFromFilter() {
        openAirlineFilter();
        List<String> airlines = new ArrayList<>();

        // More specific locator for airline names in filter (exclude price elements)
        Locator airlineFilterNamesLocator = new Locator("AIRLINE_FILTER_NAMES", new By.ByXPath("//div[contains(@class,'DropdownAirlines') or contains(@class,'BaseDropdown')]//label[contains(@class,'FilterOptionRow_row')]//span[contains(@class,'FilterOptionRow_label_wrap')]//span[contains(@class,'Text_text')] | //div[contains(@class,'BaseDropdown')]//label[contains(@class,'FilterForm_item')]//span[contains(@class,'Text_text')] | //*[contains(@class,'FilterForm_label')]//span[contains(@class,'Text_text')]"));

        try {
            driverTools.waitForElementToAppear(airlineFilterNamesLocator, 5);
            List<WebElement> airlineElements = driverTools.elements(airlineFilterNamesLocator).orElse(new ArrayList<>());

            for (WebElement element : airlineElements) {
                String airlineName = driverTools.getText(element);
                if (airlineName != null && !airlineName.trim().isEmpty()) {
                    airlineName = airlineName.trim();
                    if (airlineName.contains("(")) {
                        airlineName = airlineName.substring(0, airlineName.indexOf('(')).trim();
                    }

                    boolean isValidAirline = true;

                    if (airlineName.toLowerCase().contains("start from") ||
                        airlineName.toLowerCase().contains("mulai dari") ||
                        airlineName.contains("IDR") ||
                        airlineName.contains("SGD") ||
                        airlineName.contains("USD") ||
                        airlineName.matches(".*\\d.*")) {
                        isValidAirline = false;
                    }

                    if (isValidAirline && !airlines.contains(airlineName)) {
                        airlines.add(airlineName);
                        logger.log("Available airline in filter: " + airlineName);
                    } else if (!isValidAirline) {
                        logger.log("Filtered out non-airline text: " + airlineName);
                    }
                }
            }
        } catch (Exception e) {
            logger.log("Error getting airlines from filter: " + e.getMessage());
        }

        try {
            if (driverTools.isElementDisplayed(AIRLINE_FILTER_DROPDOWN)) {
                driverTools.clickWithJavascriptExecutor(AIRLINES_FILTER);
                driverTools.waitForElementToDisappear(AIRLINE_FILTER_DROPDOWN, 5);
            }
        } catch (Exception e) {
            logger.log("Filter already closed or no close button found");
        }

        logger.log("Total unique airlines found in filter: " + airlines.size());
        return airlines;
    }

    /**
     * Selects a random airline from the available airlines
     * @param excludeAirline Optional airline name to exclude from selection (can be null)
     * @return The name of the selected airline
     */
    public String selectRandomAirline(String excludeAirline) {
        List<String> availableAirlines = getAvailableAirlinesFromFilter();

        if (availableAirlines.isEmpty()) {
            throw new RuntimeException("No airlines available in filter");
        }

        // Remove excluded airline if specified
        if (excludeAirline != null && !excludeAirline.isEmpty()) {
            availableAirlines.removeIf(airline -> airline.equalsIgnoreCase(excludeAirline) ||
                                                   airline.toLowerCase().contains(excludeAirline.toLowerCase()));
            logger.log("Excluded airline: " + excludeAirline + ", Remaining airlines: " + availableAirlines.size());
        }

        if (availableAirlines.isEmpty()) {
            throw new RuntimeException("No airlines available after excluding: " + excludeAirline);
        }

        // Select random airline
        int randomIndex = CoreFunctions.generateRandomInt(0, availableAirlines.size() - 1);
        String selectedAirline = availableAirlines.get(randomIndex);

        logger.log("Randomly selected airline: " + selectedAirline + " (index " + randomIndex + " from " + availableAirlines.size() + " options)");

        // Filter by the selected airline
        filterAirline(selectedAirline);

        return selectedAirline;
    }

    public void clickLowestPriceGuaranteeTag(){
        driverTools.waitForElementToAppear(LOWEST_PRICE_GUARANTEE_TAG);
        driverTools.clickWithJavascriptExecutor(LOWEST_PRICE_GUARANTEE_TAG);
    }

    public int getTotalFlights(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        List<WebElement> flightCards = driverTools.elements(FLIGHT_CARDS,20).orElseThrow();
        logger.log("Total flights: " + flightCards.size());
        return flightCards.size();
    }

    public int getTotalFlightsWithTransit(){
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        List<WebElement> flightCards = driverTools.elements(FLIGHT_CARDS_WITH_TRANSIT,20).orElseThrow();
        logger.log("Total flights with transit: " + flightCards.size());
        return flightCards.size();
    }

    public boolean isFilterApplied(String filterType){
        switch (filterType.toLowerCase()) {
            case "meals" -> {
                driverTools.waitForElementToAppear(MEAL_FILTER);
                List<WebElement> mealFilterElements = driverTools.elements(MEAL_FILTER).orElseThrow();
                Assertion.assertThat("Number of meals filter applied: ", mealFilterElements.size(), is(getTotalFlights()));
                return mealFilterElements.size() == getTotalFlights();
            }
            default -> throw new UnsupportedOperationException("Unsupported filter type: " + filterType);
        }
    }

    public boolean isTotalPaxAndTiketPointDisplayed(){
        return driverTools.waitForElementToAppear(TOTAL_PER_PAX,20).isPresent();
    }

    public boolean isInfinityPromoVisible(){
        return driverTools.waitForElementsToAppear(INFINITY_PRESENT,10).isPresent();
    }

    public void ApplyInfinityPromo(){
        if(isInfinityPromoVisible()){
            driverTools.clickWithJavascriptExecutor(APPLY_INFINITY);
        }
    }

    public void selectSelfTransferFlightFilter() {
        final int maxAttempts = 5;
        boolean found = false;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
            driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
            openOtherFiltersForSelfTransfer();

            if (isSelfTransferOptionVisible()) {
                found = true;
                break;
            }

            logger.log("SELF_TRANSFER not visible (attempt " + attempt + "/" + maxAttempts
                    + "); closing filter sheets and re-running search.");
            if (attempt == maxAttempts) {
                break;
            }
            resetSrpForSelfTransferRetry();
        }

        if (!found) {
            throw new RuntimeException("SELF_TRANSFER filter not found after " + maxAttempts + " attempts");
        }

        driverTools.click(SELF_TRANSFER);
        driverTools.clickWithJavascriptExecutor(SAVE_OTHER_FILTER);

        driverTools.waitForElementToAppear(SEE_FLIGHTS);
        driverTools.clickWithJavascriptExecutor(SEE_FLIGHTS);

        driverTools.waitForElementToDisappear(FILTER_POPUP);
    }

    private void openOtherFiltersForSelfTransfer() {
        driverTools.waitForElementToAppear(FILTER);
        driverTools.clickWithJavascriptExecutor(FILTER);
        driverTools.waitForElementToAppear(OTHER_FILTER);
        driverTools.clickWithJavascriptExecutor(OTHER_FILTER);
        driverTools.waitForElementToAppear(SEE_MORE_OTHER_FILTER, 3);
        driverTools.clickWithJavascriptExecutor(SEE_MORE_OTHER_FILTER);
        driverTools.scrollUntilElementFound(SELF_TRANSFER);
    }

    /**
     * Short-timeout check so the retry loop doesn't stall on the default presence wait
     * when the option is genuinely missing for the current SRP variant.
     */
    private boolean isSelfTransferOptionVisible() {
        return driverTools.waitForElementToAppear(SELF_TRANSFER, 5).isPresent()
                && driverTools.isElementDisplayed(SELF_TRANSFER);
    }

    /**
     * Recovery flow when SELF_TRANSFER is missing: close any nested filter sheets,
     * do refresh page to get new schedule in the current SRP.
     */
    private void resetSrpForSelfTransferRetry() {
        closeAllFilterPopups();
        driverTools.refreshWebpage();
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
    }

    /**
     * Closes nested bottom-sheet filter popups (Other Filters sheet → Main Filter sheet)
     * by clicking the same close-button locator while it is still displayed.
     */
    private void closeAllFilterPopups() {
        Locator closeFilterLocator = new Locator("CLOSE_FILTER",
                new By.ByXPath("//button[contains(@class,'BottomSheet') and contains(@class,'close')]"));

        for (int i = 0; i < 3; i++) {
            try {
                if (!driverTools.isElementDisplayed(closeFilterLocator)) {
                    break;
                }
                driverTools.clickWithJavascriptExecutor(closeFilterLocator);
                driverTools.waitForElementToDisappear(closeFilterLocator, 5);
            } catch (Exception e) {
                logger.log("Filter popup close attempt " + (i + 1) + " skipped: " + e.getMessage());
                break;
            }
        }
        driverTools.waitForElementToDisappear(FILTER_POPUP, 5);
    }

    public void clickTiket100PercentRefundQuickFilter() {
        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
        driverTools.waitForElementToAppear(TIKET_100_PERCENT_REFUND_QUICK_FILTER, 10);
        driverTools.clickWithJavascriptExecutor(TIKET_100_PERCENT_REFUND_QUICK_FILTER);
    }

    private void sendKeysToAirportSearchInput(String keys, Locator field) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                driverTools.waitForElementToBeClickable(INPUT_LOCATION).orElseThrow();
                driverTools.click(INPUT_LOCATION);
                try {
                    driverTools.sendKeys(INPUT_LOCATION, keys);
                } catch (StaleElementReferenceException e) {
                    logger.log("INPUT_LOCATION became stale during sendKeys; retrying with JS value + events: " + e.getMessage());
                    driverTools.waitForElementToBeClickable(INPUT_LOCATION).orElseThrow();
                    driverTools.clickWithJavascriptExecutor(INPUT_LOCATION);
                    driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INPUT_LOCATION, keys);
                } catch (ElementNotInteractableException e) {
                    logger.log("INPUT_LOCATION not interactable for sendKeys; using JS value + events: " + e.getMessage());
                    driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(INPUT_LOCATION, keys);
                }
                return;
            } catch (RuntimeException e) {
                logger.log("sendKeysToAirportSearchInput attempt " + attempt
                        + " failed (input likely re-rendered by SRP): " + e.getMessage());
                if (attempt == maxAttempts || field == null) {
                    throw e;
                }
                driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING, 5);
                driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL, 5);
                driverTools.clickWithJavascriptExecutor(field);
                clearSearchInputWithRetry(field);
            }
        }
    }

    public void selectOriginViaAirPortCode(String origin) {
        if (origin.length() <= 4) {
            sendKeysToAirportSearchInput(origin, ORIGIN_FIELD_SR);
            clickAirportCodeOnSearchResults(origin.toUpperCase());
        } else {
            String airportName = origin.split("/")[0];
            String airportCode = origin.split("/")[1].toUpperCase();
            sendKeysToAirportSearchInput(airportName, ORIGIN_FIELD_SR);
            driverTools.minimizeKeyboardMweb();
            clickAirportCodeOnSearchResults(airportCode);
        }
    }

    public void selectDestinationViaAirPortCode(String destination) {
        if (destination.length() <= 4) {
            sendKeysToAirportSearchInput(destination, DESTINATION_FIELD_SR);
            clickAirportCodeOnSearchResults(destination.toUpperCase());
        } else {
            String airportName = destination.split("/")[0];
            String airportCode = destination.split("/")[1].toUpperCase();
            sendKeysToAirportSearchInput(airportName, DESTINATION_FIELD_SR);
            driverTools.minimizeKeyboardMweb();
            clickAirportCodeOnSearchResults(airportCode);
        }
    }

    public void selectLocationOnSearchResults(Locator field, String location) {

        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);

        driverTools.clickWithJavascriptExecutor(field);

        try {
            driverTools.waitForElementToBeClickable(INPUT_LOCATION);
        } catch (Exception e) {
            logger.log("Retry clicking field due to: " + e.getMessage());
            driverTools.clickWithJavascriptExecutor(field);
        }

        clearSearchInputWithRetry(field);

        logger.log("Searching location: " + location);

        if (location.contains("/") || location.length() <= 4) {
            if (field == DESTINATION_FIELD_SR) {
                selectDestinationViaAirPortCode(location);
            } else {
                selectOriginViaAirPortCode(location);
            }
        } else {
            driverTools.sendKeys(INPUT_LOCATION, location);
            driverTools.click(getLocatorLocationSearch(CoreFunctions.capitalize(location)));
        }

        driverTools.waitForElementToDisappear(PROGRESS_BAR_LOADING);
        driverTools.waitForElementToDisappear(PROGRESS_BAR_FILL);
    }

    private void clearSearchInputWithRetry(Locator field) {
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                driverTools.waitForElementToBeClickable(INPUT_LOCATION).orElseThrow();
                driverTools.clear(INPUT_LOCATION);
                return;
            } catch (StaleElementReferenceException e) {
                logger.log("INPUT_LOCATION stale while clearing (attempt " + attempt + "). Refocusing field. " + e.getMessage());
                driverTools.clickWithJavascriptExecutor(field);
            } catch (Exception e) {
                logger.log("Unable to clear INPUT_LOCATION via standard clear (attempt " + attempt + "): " + e.getMessage());
                driverTools.clickWithJavascriptExecutor(field);
            }
        }

        logger.log("Falling back to JS clear for INPUT_LOCATION after clear retries.");
        driverTools.clearWithJsExecutor(INPUT_LOCATION);
    }

    private void clickAirportCodeOnSearchResults(String airportCode) {
        Locator airportCodeStrict = getLocatorBasedOnAirportCode(airportCode);
        if (driverTools.waitForElementToPresence(airportCodeStrict, 8).isPresent()) {
            driverTools.click(airportCodeStrict);
            return;
        }

        Locator airportCodeFlexible = getLocatorBasedOnAirportCodeFlexible(airportCode);
        if (driverTools.waitForElementToPresence(airportCodeFlexible, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(airportCodeFlexible);
            return;
        }

        Locator highlightedResult = getFirstHighlightedSearchResult();
        if (driverTools.waitForElementToPresence(highlightedResult, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(highlightedResult);
            return;
        }

        for (String keyword : getAirportCodeFallbackKeywords(airportCode)) {
            Locator popularCity = getPopularCityByKeyword(keyword);
            if (driverTools.waitForElementToPresence(popularCity, 4).isPresent()) {
                driverTools.clickWithJavascriptExecutor(popularCity);
                return;
            }
        }

        throw new RuntimeException("No airport option found on SRP for code: " + airportCode);
    }

    private List<String> getAirportCodeFallbackKeywords(String airportCode) {
        List<String> keywords = new ArrayList<>();
        keywords.add(airportCode.toLowerCase());
        switch (airportCode.toUpperCase()) {
            case "SUB" -> keywords.add("surabaya");
            case "CGK" -> keywords.add("jakarta");
            case "DPS" -> keywords.add("bali");
            case "KNO" -> keywords.add("medan");
            case "UPG" -> keywords.add("makassar");
            case "YIA" -> keywords.add("yogyakarta");
            case "MDC" -> keywords.add("manado");
            case "BPN" -> keywords.add("balikpapan");
            case "BTH" -> keywords.add("batam");
            case "PLM" -> keywords.add("palembang");
            default -> {
            }
        }
        return keywords;
    }

    public void selectOriginOnSearchResults(String origin) {
        logger.log("Selecting origin on search results page");
        selectLocationOnSearchResults(ORIGIN_FIELD_SR, origin);
    }

    public void selectDestinationOnSearchResults(String destination) {
        logger.log("Selecting destination on search results page");
        selectLocationOnSearchResults(DESTINATION_FIELD_SR, destination);

    }
    public void searchFlightSRP(){
        driverTools.waitForElementToAppear(SEARCH);
        driverTools.clickWithJavascriptExecutor(SEARCH);
    }

    public String getSearchWidgetOriginText() {
        driverTools.waitForElementToAppear(ORIGIN_FIELD_SR).orElseThrow();
        return driverTools.getText(ORIGIN_FIELD_SR).orElseThrow();
    }

    public String getSearchWidgetDestinationText() {
        driverTools.waitForElementToAppear(DESTINATION_FIELD_SR).orElseThrow();
        return driverTools.getText(DESTINATION_FIELD_SR).orElseThrow();
    }

    public String getSearchWidgetDepartDateShortText() {
        driverTools.waitForElementToPresence(SEARCH_WIDGET_DATE_DEPART_SHORT).orElseThrow();

        String shortDateText = normalizeSearchWidgetDate(
                driverTools.getAttributeTextContent(SEARCH_WIDGET_DATE_DEPART_SHORT).orElse(""));
        if (shortDateText.isEmpty()) {
            shortDateText = normalizeSearchWidgetDate(
                    driverTools.getText(SEARCH_WIDGET_DATE_DEPART_SHORT).orElse(""));
        }
        if (shortDateText.isEmpty()) {
            driverTools.waitForElementToPresence(SEARCH_WIDGET_DATE_DEPART_LONG).orElseThrow();
            String longDateText = driverTools.getText(SEARCH_WIDGET_DATE_DEPART_LONG)
                    .filter(text -> !text.isBlank())
                    .or(() -> driverTools.getAttributeTextContent(SEARCH_WIDGET_DATE_DEPART_LONG))
                    .map(this::normalizeSearchWidgetDate)
                    .orElse("");
            shortDateText = extractShortDateFromLongFormat(longDateText);
        }
        if (shortDateText.isEmpty()) {
            throw new RuntimeException("SRP search widget departure date not found");
        }
        return shortDateText;
    }

    public boolean isRoundTripDateRangeDisplayedOnSearchWidget() {
        return driverTools.isElementDisplayed(SEARCH_WIDGET_DATE_RETURN)
                || driverTools.isElementDisplayed(SEARCH_WIDGET_DATE_DIVIDER)
                || driverTools.isElementPresent(SEARCH_WIDGET_DATE_RETURN_SHORT);
    }

    public void verifySrpSearchWidgetLocations(String origin, String destination) {
        String originText = getSearchWidgetOriginText();
        String destinationText = getSearchWidgetDestinationText();
        String originCode = resolveAirportCode(origin);
        String destinationCode = resolveAirportCode(destination);
        String originCity = resolveAirportCity(origin);
        String destinationCity = resolveAirportCity(destination);

        logger.log("SRP search widget origin: " + originText);
        logger.log("SRP search widget destination: " + destinationText);

        Assertion.assertThat("SRP origin contains airport code " + originCode,
                originText, containsStringIgnoringCase(originCode));
        if (!originCity.isEmpty()) {
            Assertion.assertThat("SRP origin contains city " + originCity,
                    originText, containsStringIgnoringCase(originCity));
        }

        Assertion.assertThat("SRP destination contains airport code " + destinationCode,
                destinationText, containsStringIgnoringCase(destinationCode));
        if (!destinationCity.isEmpty()) {
            Assertion.assertThat("SRP destination contains city " + destinationCity,
                    destinationText, containsStringIgnoringCase(destinationCity));
        }
    }

    public void verifySrpOneWayDateMatches(String departureDateLong) {
        String departDateText = getSearchWidgetDepartDateShortText();
        String expectedShortDate = normalizeSearchWidgetDate(
                CoreFunctions.convertDateFormat(departureDateLong, "d MMMM yyyy", "d MMM"));

        logger.log("SRP search widget depart date: '" + departDateText + "'");
        logger.log("Expected one-way SRP depart date: '" + expectedShortDate + "'");

        Assertion.assertThat("SRP search widget shows selected departure date from landing page",
                departDateText, equalTo(expectedShortDate));
        Assertion.assertThat("SRP search widget does not show return date section",
                driverTools.isElementDisplayed(SEARCH_WIDGET_DATE_RETURN), is(false));
        Assertion.assertThat("SRP search widget does not show date divider between depart and return",
                driverTools.isElementDisplayed(SEARCH_WIDGET_DATE_DIVIDER), is(false));
        Assertion.assertThat("SRP search widget does not show return short date ",
                driverTools.isElementPresent(SEARCH_WIDGET_DATE_RETURN_SHORT), is(false));
    }

    public void verifySrpOneWayUrlParams(String origin, String destination, String departureDateLong) {
        String originCode = resolveAirportCode(origin);
        String destinationCode = resolveAirportCode(destination);
        String expectedUrlDate = CoreFunctions.convertDateFormat(departureDateLong, "d MMMM yyyy", "yyyy-MM-dd");
        String url = getCurrentUrlSRP();

        Assertion.assertThat("SRP URL contains origin airport code",
                url, containsString("d=" + originCode));
        Assertion.assertThat("SRP URL contains destination airport code",
                url, containsString("a=" + destinationCode));
        Assertion.assertThat("SRP URL contains departure date",
                url, containsString("date=" + expectedUrlDate));
        Assertion.assertThat("SRP URL does not contain return date for one-way",
                url, not(containsString("returndate=")));
    }

    public void verifyOneWaySearchWidgetDetails(String origin, String destination, String departureDateLong) {
        verifySrpSearchWidgetLocations(origin, destination);
        verifySrpOneWayDateMatches(departureDateLong);
        verifySrpOneWayUrlParams(origin, destination, departureDateLong);
    }

    private String normalizeSearchWidgetDate(String dateText) {
        return dateText == null ? "" : dateText.trim().replaceAll("\\s+", " ");
    }

    private String extractShortDateFromLongFormat(String longDateText) {
        if (longDateText == null || longDateText.isEmpty()) {
            return "";
        }
        String datePart = longDateText.contains(",")
                ? longDateText.substring(longDateText.indexOf(',') + 1).trim()
                : longDateText.trim();
        String[] parts = datePart.split("\\s+");
        if (parts.length >= 2) {
            return parts[0] + " " + parts[1];
        }
        return datePart;
    }

    private String resolveAirportCode(String airport) {
        if (airport == null) {
            return "";
        }
        String value = airport.trim();
        if (value.contains("/")) {
            return value.split("/")[1].trim().toUpperCase();
        }
        return value.toUpperCase();
    }

    private String resolveAirportCity(String airport) {
        if (airport == null || !airport.contains("/")) {
            return "";
        }
        return airport.split("/")[0].trim();
    }

    public boolean isCorporateDealsLabelOnFlightCardPresent() {
        return driverTools.waitForElementToPresence(CORPORATE_DEALS_FLIGHT_CARD_LABEL, 20).isPresent();
    }

    public String getCorporateDealsLabelOnFlightCardText() {
        WebElement corporateDealsLabel = driverTools.waitForElementToAppear(CORPORATE_DEALS_FLIGHT_CARD_LABEL).orElseThrow();
        String labelText = driverTools.getText(corporateDealsLabel);
        logger.log("Corporate Deals label on flight card: " + labelText);
        return labelText;
    }

    public long selectFlightWithPriceGreaterThanTenMillion() {
        List<WebElement> flightCards = driverTools.waitForElementsToAppear(FLIGHT_CARDS, 20).orElseThrow();

        for (int i = 1; i <= flightCards.size(); i++) {
            Locator cardLocator = getFlightCard(i);

            // Scroll to the card to trigger lazy-loading API calls or UI renders
            driverTools.scrollIntoWebView(cardLocator);

            Locator priceLocator = getFlightCardPrice(i);

            // Use Optional instead of .orElseThrow() to prevent crashes on skeleton cards
            Optional<WebElement> priceElementOpt = driverTools.waitForElementToAppear(priceLocator, 3);
            if (priceElementOpt.isEmpty()) {
                logger.log("Flight card " + i + " price not found (likely a skeleton loader or sold out). Skipping...");
                continue;
            }

            WebElement priceElement = priceElementOpt.get();
            String priceText = driverTools.getText(priceElement).trim();

            if (!priceText.isEmpty()) {
                long price = Long.parseLong(priceText.replaceAll("[^0-9]", ""));
                logger.log("Flight card " + i + " price: " + price);

                if (price >= 10_000_000) {
                    logger.log("Selecting flight card " + i + " with price greater than IDR 10,000,000: " + price);
                    driverTools.clickWithJavascriptExecutor(cardLocator);
                    return price;
                }
            }
        }
        throw new AssertionError("No flight found with price greater than IDR 10,000,000");
    }

    private Locator getFlightCardPrice(int index) {
        return new Locator("FLIGHT_CARD_PRICE_" + index, new By.ByXPath("(//div[@data-testid='srp-flight-card'])[" + index + "]"
                + "//div[contains(@class,'FlightCard_price')]"
                + "//span[contains(@class,'Text_variant_price')]"));
    }

    private Locator getFlightCard(int index) {
        return new Locator("FLIGHT_CARD_" + index, new By.ByXPath("(//div[@data-testid='srp-flight-card'])[" + index + "]"));
    }

    public void verifyNoSeoCardsHaveTodaysDate() {
        driverTools.waitForElementToAppear(PRICE_TABLE);

        List<WebElement> dateElements = driverTools.elements(SEO_PRICE_CARD_DATES)
                .orElseThrow(() -> new RuntimeException("No SEO price cards found on the page"));

        // Format today's date to match the UI format (e.g., "Wed, 16 Sep 26")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yy", Locale.ENGLISH);
        String todayFormatted = LocalDate.now().format(formatter);

        logger.log("Checking cards to ensure none contain today's date: " + todayFormatted);

        for (int i = 0; i < dateElements.size(); i++) {
            String displayedDate = driverTools.getText(dateElements.get(i)).trim();
            logger.log("Card " + (i + 1) + " Date: " + displayedDate);
            Assertion.assertThat("Card " + (i + 1) + " should not have today's date (" + todayFormatted + ")", displayedDate, not(equalTo(todayFormatted)));
        }
    }

    public String getFirstSeoCardOrigin() {
        driverTools.waitForElementToAppear(SEO_PRICE_CARDS);
        String origin = driverTools.getText(FIRST_SEO_CARD_ORIGIN).orElseThrow().trim();
        logger.log("First SEO Card Origin: " + origin);
        return origin;
    }

    public String getFirstSeoCardDestination() {
        String destination = driverTools.getText(FIRST_SEO_CARD_DESTINATION).orElseThrow().trim();
        logger.log("First SEO Card Destination: " + destination);
        return destination;
    }

    public String getFirstSeoCardDate() {
        String date = driverTools.getText(FIRST_SEO_CARD_DATE).orElseThrow().trim();
        date = date.replaceFirst("^(\\d)\\s", "0$1 ");
        logger.log("First SEO Card Date: " + date);
        return date;
    }

    public void clickBookNowOnFirstSeoCard() {
        logger.log("Clicking 'Book now' on the first SEO price card");
        driverTools.clickWithJavascriptExecutor(FIRST_SEO_CARD_BOOK_NOW);
    }

    public void verifySrpWidgetRouteAndDate(String expectedOrigin, String expectedDestination, String expectedDateText) {
        String srpOriginText = getSearchWidgetOriginText();
        String srpDestinationText = getSearchWidgetDestinationText();
        String srpDateText = getSearchWidgetDepartDateShortText();

        logger.log("SRP Search Widget -> Origin: " + srpOriginText + ", Destination: " + srpDestinationText + ", Date: " + srpDateText);

        Assertion.assertThat("SRP origin contains SEO card origin airport code (" + expectedOrigin + ")",
                srpOriginText, containsStringIgnoringCase(expectedOrigin));

        Assertion.assertThat("SRP destination contains SEO card destination airport code (" + expectedDestination + ")",
                srpDestinationText, containsStringIgnoringCase(expectedDestination));

        String normalizedSeoDate = extractShortDateFromLongFormat(expectedDateText);

        String cleanSrpDate = srpDateText.replaceFirst("^0", "");
        String cleanExpectedDate = normalizedSeoDate.replaceFirst("^0", "");

        Assertion.assertThat("SRP search widget departure date matches SEO card date",
                cleanSrpDate, containsStringIgnoringCase(cleanExpectedDate));
    }

    public int getSeoPriceCardCount() {
        driverTools.waitForElementToAppear(SEO_PRICE_CARDS).orElseThrow();
        int count = driverTools.elements(SEO_PRICE_CARDS).orElse(Collections.emptyList()).size();
        logger.log("SEO price cards available: " + count);
        return count;
    }

    public long getFirstSeoCardPrice() {
        driverTools.waitForElementToAppear(SEO_PRICE_CARDS).orElseThrow();
        driverTools.scrollIntoWebView(FIRST_SEO_CARD_PRICE);
        String priceText = driverTools.getText(FIRST_SEO_CARD_PRICE).orElseThrow().trim();
        logger.log("First SEO card price: " + priceText);
        return Long.parseLong(CoreFunctions.removeNonDigit(priceText));
    }

    public String getFirstSeoCardAirline() {
        driverTools.waitForElementToAppear(SEO_PRICE_CARDS).orElseThrow();
        String airline = driverTools.getText(FIRST_SEO_CARD_AIRLINE).orElseThrow().trim();
        logger.log("First SEO card airline: " + airline);
        return airline;
    }

    public long getFirstStandardSectionFlightPrice() {
        driverTools.scrollUp(SRP_SCROLL_TO_TOP_PIXELS);
        driverTools.waitForElementToAppear(FIRST_STANDARD_SECTION_CARD_PRICE, 30).orElseThrow();
        String priceText = driverTools.getText(FIRST_STANDARD_SECTION_CARD_PRICE).orElseThrow().trim();
        logger.log("First flight card price in the standard SRP section: " + priceText);
        return Long.parseLong(CoreFunctions.removeNonDigit(priceText));
    }
}
