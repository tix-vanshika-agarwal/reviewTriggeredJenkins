package com.tiket.web.page.common.home;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.page.platform.eiffel.membership.AccountPageWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.ArrayList;
import org.openqa.selenium.*;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;

public class HomePageMWeb <T extends WebDriver> extends HomePageWeb<T> {

    public HomePageMWeb(Session<T> session) {
        super(session);
    }

    private static final String STAGING_DWEB_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_DWEB_URL = "https://preprod.tiket.com/";
    private static final String PROD_DWEB_URL = "https://tiket.com/";
    private static final Locator HOME_PAGE_LOGO = new Locator("HOME_PAGE_LOGO", By.xpath("//div[contains(@class,'AnimatedTiketLogo')]"));
    private static final Locator HOME_PAGE_HEADING_NEW = new Locator("HOME_PAGE_HEADING", By.xpath("//h2[contains(@class, 'GreetingText_greeting')]"));
    private static final Locator HOME_PAGE_HEADING_OLD = new Locator("HOME_PAGE_HEADING", By.xpath("//h2[contains(@class, 'GreetingText_greeting')]"));
    private static final Locator MULCUR_VERTICAL_ICONS_PAGECONTROL2 = new Locator("MULCUR_VERTICAL_ICONS_PAGECONTROL2", By.xpath("//button[contains(@class,'PageControl_dot_button')][2]"));
    private static final Locator CLOSE_POP_UP = new Locator("CLOSE_POP_UP", new By.ByXPath("//button[contains(@class, 'Button-module') and (normalize-space(text())='Close' or normalize-space(text())='Tutup')]"));
    private static final Locator X_CLOSE_POP_UP = new Locator("X_CLOSE_POP_UP", new By.ByXPath("//button[contains(@class,'BottomSheet-module__close_button')]"));
    private static final Locator HALO_TIKET_COACHMARK_CONTAINER = new Locator("HALO_TIKET_COACHMARK_CONTAINER", new By.ByXPath("//div[contains(@class,'Coachmark_coachmark_step')]"));
    private static final Locator HALO_TIKET_COACHMARK_OK_BUTTON = new Locator("HALO_TIKET_COACHMARK_OK_BUTTON", new By.ByXPath("//span[contains(@class, 'Button') and (normalize-space(text())='OK' or normalize-space(text())='Oke')]"));
    private static final Map<String, Boolean> sessionToHomeCoachmark = new ConcurrentHashMap<>();
    private static final Locator LANGUAGE_ID = new Locator("LANGUAGE_ID", new By.ByXPath("//h2[contains(text(), 'going anywhere?')]"));
    private static final Locator LANGUAGE_CURRENCY_DROPDOWN = new Locator("LANGUAGE_CURRENCY_DROPDOWN", new By.ByXPath("//*[contains(@class, 'LanguageAndCurrencyModal-module__drop_icon')]"));
    private static final Locator CURRENCY_HEADER_TEXT = new Locator("CURRENCY_HEADER_TEXT", By.xpath("(//span[contains(@class, 'settings_general_menu')])[2]"));
    private static final Locator CURRENCY = new Locator("CURRENCY", By.xpath("(//div[contains(@class, 'List_right_side_')]//span)[2]"));
    private static final Locator ACCOUNT_ICON = new Locator("ACCOUNT_ICON", new By.ByXPath("//a[@aria-label='nav-item-4']"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", new By.ByXPath("//*[contains(@class,'ActionBanner_wrapper_action_banner')]//button"));
    private static final Locator CONTINUE_WITHOUT_APP_POP_UP = new Locator("CONTINUE_WITHOUT_APP_POP_UP", new By.ByXPath("//*[contains(@data-qa-id , 'secondaryCtaClose-button')]"));
    private static final Locator YOUR_ORDER_ICON = new Locator("YOUR_ORDER_ICON", new By.ByXPath("//div[contains(@class,'BottomTabNavigation')]//*[contains(text(),'Your Order')]"));
    private static final Locator GS_INFO_CLOSE_BUTTON = new Locator("GS_INFO_CLOSE_BUTTON", By.xpath("(//button[contains(@class, 'InfoBanner_close_button')])[last()]"));
    private static final Locator GS_POP_TITLE = new Locator("GS_POP_TITLE", By.xpath("(//div[contains(@class, 'GSPopular_heading')])[1]"));
    private static final Locator GS_POP_BANNER = new Locator("GS_POP_BANNER", By.xpath("(//img[contains(@class, 'GSPopular_default_image')])[1]"));
    private static final Locator GS_MS_CHIPS = new Locator("GS_MS_CHIPS", By.xpath("(//section[contains(@class, 'GSMostSearch_container')]//div[contains(@class, 'GSMostSearch_chips')])[1]"));
    private static final Locator GS_MS_SEE_ALL_BUTTON = new Locator("GS_MS_SEE_ALL_BUTTON", By.xpath("(//a[contains(@class, 'GSMostSearch_see_all_bottom')])[1]"));
    private static final Locator GS_IS_PRODUCT_TITLE = new Locator("GS_IS_PRODUCT_TITLE", By.xpath("(//p[contains(@class, 'GSInspirationalSearch_title') and contains(text(), 'Automation')])[1]"));
    private static final Locator GS_IS_PRODUCT_SUBTITLE = new Locator("GS_IS_PRODUCT_SUBTITLE", By.xpath("(//p[contains(@class, 'GSInspirationalSearch_content_buttontext') and contains(text(), 'mock')])[1]"));
    private static final Locator VOUCHER_BOX = new Locator("VOUCHER_BOX", new By.ByXPath("//div[@id='__next']//span[contains(text(), 'Voucher')]"));
    private static final Locator VOUCHER_BOX_PAGE = new Locator("VOUCHER_BOX_PAGE", new By.ByXPath("//div[@id='__next']//span[text()='Voucher Box']"));
    private static final Locator ACCOUNT_BUTTON = new Locator("ACCOUNT_BUTTON", new By.ByXPath("//a[@data-testid='nav-item' and (.//p[normalize-space(text())='Account'] or .//p[normalize-space(text())='Akun'])]"));
    private static final Locator GS_INPUT_COLUMN_MOBILE = new Locator("GS_INPUT_COLUMN_LOCATOR", By.xpath("(//div[contains(@class,'SearchBox_placeholder')]//input[@aria-label='Search Text Input'])[3]"));
    private static final Locator SAVE_PASSENGER_DATA = new Locator("SAVE_PASSENGER_DATA", new By.ByXPath("//img[@alt='Save Passenger Data' or @alt='Simpan Data Penumpang']"));
    private static final Locator GLOBAL_SEARCH_MODAL = new Locator("GLOBAL_SEARCH_MODAL", By.xpath("//div[contains(@class, 'SearchBox_input_box__') and contains(@class, 'SearchBox_size_md__') and contains(@class, 'SearchBox_input_box_color_light__')]"));
    private static final Locator GLOBAL_SEARCH_BAR = new Locator("GLOBAL_SEARCH_BAR", By.xpath("//*[@aria-label[contains(.,'Search')]]"));
    private static final Locator GS_IMAGE_VERTICAL_ICON = new Locator("GS_IMAGE_VERTICAL_ICON", By.xpath("//img[contains(@class,'SStaticVertical_content_img')]"));
    private static final Locator GS_VERTICAL_TITLE = new Locator("GS_VERTICAL_TITLE", By.xpath("//p[contains(@class,'GSStaticVertical_content_title')]"));
    private static final Locator MOBILE_HAMBURGER_HEADER = new Locator("MOBILE_HAMBURGER_HEADER", By.xpath("//button[contains(@class,'MobileHamburgerMenus-module')]"));
    private static final Locator CURRENCY_MOBILE_BOTTOMSHEET_ENTRYPOINT = new Locator("CURRENCY_MOBILE_BOTTOMSHEET_ENTRYPOINT", By.xpath("//span[contains(text(), 'Currency')]"));
    private static final Locator CURRENCY_MOBILE_BOTTOMSHEET = new Locator("CURRENCY_MOBILE_BOTTOMSHEET", By.xpath("(//div[contains(@class,'BottomSheet-module__bottom_sheet')]//div[contains(@class,'BottomSheet-module')])[1]"));
    private static final Locator CHOSEN_IDR_CURRENCY = new Locator("CHOSEN_IDR_CURRENCY", By.xpath("//input[contains(@value, 'IDR')]"));
    private static final Locator WISHLIST_ICON = new Locator("WISHLIST_ICON", new By.ByXPath("//p[text()='Wishlist']"));
    private static final Locator HAMBURGER_MENU = new Locator("HAMBURGER_MENU", new By.ByXPath("//*[contains(@class,'MobileHamburgerMenus')]"));
    private static final Locator HAMBURGER_CLOSE = new Locator("HAMBURGER_CLOSE", new By.ByXPath("//*[contains(@class,'BottomSheet-module__close_button')]"));
    private static final Locator SAVED_PAYMENT_METHOD = new Locator("SAVED_PAYMENT_METHOD", By.xpath("//span[text()='Lihat semua' or text()='See all']"));
    private static final Locator ABOUT_TIKET = new Locator("ABOUT_TIKET", new By.ByXPath("//p[contains(text(), 'About tiket.com') or contains(text(), 'Tentang tiket.com')]"));
    private static final Locator OPEN_PRIVACY_POLICY = new Locator("OPEN_PRIVACY_POLICY", new By.ByXPath("//p[contains(text(), 'Privacy') or contains(text(), 'Kebijakan Privasi')]"));
    private static final Locator YOUR_REVIEW = new Locator("YOUR_REVIEW", new By.ByXPath("//div[contains(text(), 'Your Review') or contains(text(), 'Review Kamu')]"));
    private static final Locator YOUR_REVIEW_PAGE = new Locator("YOUR_REVIEW_PAGE", new By.ByXPath("//h1[contains(text(), 'Your Review') or contains(text(), 'Review Kamu')]"));
    private static final Locator LANGUAGE_ICON = new Locator("LANGUAGE_ICON", new By.ByXPath("//p[contains(text(), 'Language') or contains(text(), 'Bahasa')]"));
    private static final Locator BAHASA_LANGUAGE = new Locator("BAHASA_LANGUAGE", new By.ByXPath("//span[contains(text(),'Bahasa Indonesia')]"));
    private static final Locator ENGLISH_LANGUAGE = new Locator("BAHASA_LANGUAGE", new By.ByXPath("//span[contains(text(),'English')]"));
    private static final Locator HOME_ICON = new Locator("HOME_ICON", new By.ByXPath("//img[@alt='Home']"));
    private static final Locator PP_PAGE = new Locator("PP_PAGE", new By.ByXPath("//h3[normalize-space(text())='Privacy and Policy' or normalize-space(text())='Kebijakan Privasi']"));
    private static final Locator GS_INFO_TITLE = new Locator("GS_INFO_TITLE", By.xpath("(//p[contains(text(), 'Automation GS Info')])[1]"));
    private static final Locator GS_INFO_ICON = new Locator("GS_INFO_ICON", By.xpath("(//img[contains(@alt, 'Automation GS Info')])[1]"));
    private static final Locator INFO_BANNER_TEXT = new Locator("INFO_BANNER_TEXT", By.xpath("//div[contains(@class, 'InfoBanner') and contains(@class, 'Text_variant_activity')]"));
    private static final Locator CLOSE_ICON = new Locator("CLOSE_ICON", By.xpath("//button[contains(@class, 'InfoBanner_close_button')]\n"));
    private static final Locator POPULAR_EVENTS_TITLE = new Locator("POPULAR_EVENTS_TITLE", By.xpath("//h3[contains(@class, 'GSPopular_heading_title') and text()='[Automation] Highly popular events']"));
    private static final Locator KPOP_EVENT_IMAGE = new Locator("KPOP_EVENT_IMAGE", By.xpath("//img[@alt='Get Whoosh high-speed train tickets!']\n"));
    private static final Locator INSPIRED_TITLE = new Locator("INSPIRED_TITLE", By.xpath("//p[contains(@class, 'GSInspirationalSearch_title') and contains(text(), '[Automation] Inspired')]"));
    private static final Locator INSPIRED_IMAGE = new Locator("INSPIRED_IMAGE", By.xpath("//div[contains(@class, 'GSInspirationalSearch_icon')]"));
    private static final Locator HOTELICONTITLE = new Locator("HOMEICONTITLE", By.xpath("//p[normalize-space()='Hotel' or normalize-space()='Hotels']"));
    private static final Locator TRAINICONTITLE = new Locator("TRAINICONTITLE", By.xpath("//p[normalize-space()='Train' or normalize-space()='Kereta Api']"));
    private static final Locator CARICONTITLE = new Locator("CARICONTITLE", By.xpath("//p[normalize-space()='Car Rentals' or normalize-space()='Sewa Mobil']"));
    private static final Locator EVENTICONTITLE = new Locator("EVENTICONTITLE", By.xpath("//p[normalize-space()='Events' or normalize-space()='Event']"));
    private static final Locator SKIP_LOGIN_POPUP = new Locator("SKIP_LOGIN_POPUP", AppiumBy.xpath("//*[@name='Close']"));
    private static final Locator FLASHSALE = new Locator("SEE_FLASHSALE", By.xpath("(//div[contains(@class, 'FlashSale_flashsale_header')])[1]"));
    private static final Locator CLICK_FLASHSALE = new Locator("CLICK_FLASHSALE", By.xpath("//h2[text()='Alcazar Cabaret Show E-Voucher']"));
    private static final Locator SEE_ALL_PROMO = new Locator("SEE_ALL_PROMO", new By.ByXPath("//a[starts-with(@class, 'PromoCarouselLayout_cta_btn')]"));
    private static final Locator GS_HOTEL_ICON = new Locator("GS_HOTEL_ICON", new By.ByXPath("//a[contains(@class, 'GSStaticVertical_vertical')]//img[@alt='Hotel']"));
    private static final Locator GS_COLUMN_ON_MODAL = new Locator("GS_COLUMN_ON_MODAL", By.xpath("(//div[contains(@class,'SearchBox_placeholder')]//input[@aria-label='Search Text Input'])[3]"));
    private static final Locator BANNER_CAROUSEL_INSIDE_TITLE = new Locator("BANNER_CAROUSEL_INSIDE_TITLE", By.xpath("(//p[text()='Title Auto'])[1]"));
    private static final Locator BANNER_CAROUSEL_INSIDE_SUBTITLE = new Locator("BANNER_CAROUSEL_INSIDE_SUBTITLE", By.xpath("(//p[text()='Subtitle Ankit'])[1]"));
    private static final Locator BANNER_CAROUSEL_SCROLL = new Locator("BANNER_CAROUSEL_SCROLL", By.xpath("//a[.//img[@alt='Title auto']]"));
    private static final Locator SEE_ALL_BANNER_CAROUSEL = new Locator("SEE_ALL_BANNER_CAROUSEL", By.xpath("//span[contains(@class,'Text_text') and normalize-space(.)='SEE ALL']"));
    private static final Locator PROMO_LIST = new Locator("PROMO_LIST", new By.ByXPath("//span[text()='Voucher Box']"));
    private static final Locator TO_DO_ICON = new Locator("TO_DO_ICON", new By.ByXPath("//span[contains(@class,'VerticalIcons_vertical_name') and text()='To Dos']"));
    private static final Locator TRAIN_SEARCH_PAGE = new Locator("TRAIN_SEARCH_PAGE", new By.ByXPath(" //div[contains(@class, 'kereta-api_search')]"));
    private static final Locator EVENT_SEARCH_PAGE = new Locator("EVENT_SEARCH_PAGE", new By.ByXPath("//div[contains(@class, 'SRPSearchBox_custom_input')]"));
    private static final Locator AIRPORT_TRANSFER_SEARCH_PAGE = new Locator("AIRPORT_TRANSFER_SEARCH_PAGE", new By.ByXPath("//h1[contains(text(), 'Jemputan Bandara') or contains(text(), 'Airport Transfer')]"));
    private static final Locator FLIGHT_PAGE_BUTTON = new Locator("FLIGHT_PAGE_BUTTON", new By.ByXPath("//span[contains(@class,'vertical_name') and (normalize-space()='Flights' or normalize-space()='Pesawat')]"));
    private static final Locator FLIGHT_SEARCH_PAGE = new Locator("FLIGHT_SEARCH_PAGE", new By.ByXPath("//div[contains(@class,'flight_hero_wrapper')]"));
    private static final Locator HOTEL_SEARCH_PAGE = new Locator("HOTEL_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='hotel-hero-banner-container']"));
    private static final Locator TODO_SEARCH_PAGE = new Locator("TODO_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='home-location-label']//span[contains(@class, 'Text_size_h2')]"));
    private static final Locator CAR_RENTAL_SEARCH_PAGE = new Locator("CAR_RENTAL_SEARCH_PAGE", new By.ByXPath("//div[contains(@class,'car_searchform_wrapper')]"));
    private static final Locator FS_SECTION_NON_IDR = new Locator("FS_SECTION_NON_IDR", By.xpath("(//div[contains(@class, 'FlashSale_flashsale_header')])[1]"));
    private static final Locator FS_PC_NON_IDR = new Locator ("FS_PC_NON_IDR", By.xpath("//section[@id = '66d9788c1dd1c766785afe70']//div[contains(text(), 'SGD')]"));
    private static final Locator IWC_SECTION_NON_IDR = new Locator("IWC_SECTION_NON_IDR", By.xpath("//div[contains(@class, 'Layout_title') and contains(text(),'[Automation] Hotel & Homes IWC')]"));
    private static final Locator IWC_PC_NON_IDR = new Locator ("IWC_PC_NON_IDR", By.xpath("(//section[@id = '68998e3c8e0f8e3e822f94e5']//div[contains(text(), 'SGD')])[1]"));
    private static final Locator HDP_SECTION = new Locator("HDP_SECTION", By.xpath("//section[@id='685392beddef796fe28e8110']//div[contains(text(), '[Automation] HDP')]"));
    private static final Locator HDP_PC_NON_IDR = new Locator ("HDP_PC_NON_IDR", By.xpath("//span[normalize-space()='SGD']"));
    private static final Locator HELP_CENTER = new Locator("HELP_CENTER", new By.ByXPath("//*[contains(@class,'Footer-module')]/a[contains(@href,'help-center')]"));
    private static final Locator INBOX_ICON = new Locator("INBOX_ICON", new By.ByXPath("(//a[contains(@href,'/inbox')])[1]"));
    private static final Locator SEE_ALL_FLASH_SALE_BTN =  new Locator("SEE_ALL_FLASH_SALE_BTN", By.xpath("//a[contains(@class, 'FlashSaleTabContent_btn') and normalize-space(text())='See All']"));
    private static final Locator SEE_ALL_REDIRECTION = new Locator("SEE_ALL_REDIRECTION", By.xpath("//div[contains(normalize-space(text()), 'Showing') and contains(normalize-space(text()), 'results')]"));
    private static final Locator HDP_CURRENCY = new Locator("HDP_CURRENCY", By.xpath("(//div[contains(@class, 'HighlightDiscoverProduct')]/following::div[contains(@class, 'AccomProductCard_content_container') and .//div[contains(text(), 'IDR')]])[1]"));
    private static final Locator FLASH_SALE_CURRENCY = new Locator("FLASH_SALE_CURRENCY", By.xpath("(//div[contains(@class, 'FlashSale_flashsale_header')]/following::div[contains(@class, 'ProductCard_content_container') and .//div[contains(text(), 'IDR')]])[1]"));
    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//div[contains(@class,'blink_icon')]"));
    private static final Locator CLAIM_CENTER_DIV = new Locator("CLAIM_CENTER_DIV", new By.ByXPath("//a[text() = 'Claim Center']"));
    private static final Locator PROMO_LIST_HEADER_ENTRYPOINT = new Locator("PROMO_LIST_HEADER_ENTRYPOINT", new By.ByXPath("//a[contains(@class, 'MobileHeaderMenu_icon_button') and @aria-label='Promo']"));

    private static Locator gsTopSearchResult(String keywords){
        return new Locator("GS_TOP_RESULT", By.xpath(String.format("(//div[@id='global-search-modal']//div[contains(@class,'GSResultSRP_text_container')and contains(., '%s')])[1]", keywords)));
    }

    private static Locator gsInfoSearchResult(String keywords){
        return new Locator("GS_INFO_RESULT", By.xpath(String.format("(//div[@id='global-search-modal']//div[contains(@class,'GSArticles_content_title')and contains(., '%s')])[1]", keywords)));
    }

    private Locator getLanguageSelector(String lang) {
        return new Locator("LANGUAGE_SELECTOR", new By.ByXPath("//*[contains(@class,'MobileMenuBottomSheet')][contains(text(),'" + lang + "')]"));
    }

    private static final Locator ARTICLE_TITLE = new Locator("ARTICLE_TITLE", By.xpath("(//div[contains(@class, 'GSArticles_item_container') and (contains(normalize-space(.), 'Hotel'))])[1]"));
    private static final Locator ARTICLE_IMAGE = new Locator("ARTICLE_IMAGE", By.xpath("//img[contains(@alt,'Info covid case tiket.com')]"));
    private static final Locator ARTICLE_SAMPLE = new Locator("ARTICLE_SAMPLE", By.xpath("//div[contains(@class,'HeaderMobile_header')]//span[contains(text(),'Info covid')]"));
    private static final Locator CLOSE_BOTTOMSHEET_PROMO = new Locator("CLOSE_BOTTOMSHEET_PROMO", By.xpath("//*[contains(@class, 'close_button') and contains(@tabindex, '-1')]"));

    private static Locator getProductImage(String moduleId){
        return new Locator("HDP_PRODUCT_IMAGE_MOBILE", By.xpath(String.format("//section[@id='%s']//img[contains(@class,'HighlightDiscoverMobileCarousel_image')]", moduleId)));
    }
    private static Locator getHDPInventoryName(String moduleId){
        return new Locator("HDP_INVENTORY_NAME_MOBILE", By.xpath(String.format("//section[@id='%s']//h2[contains(@class,'HighlightDiscoverProductName_product_card_title')]", moduleId)));
    }
    private static Locator getHDPInventoryLocation(String moduleId){
        return new Locator("HDP_INVENTORY_LOCATION_MOBILE", By.xpath(String.format("//section[@id='%s']//span[contains(@class,'ProductInfo_info_text')]", moduleId)));
    }
    private static Locator getHDPInventoryPrice(String moduleId){
        return new Locator("HDP_INVENTORY_PRICE_MOBILE", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'HighlightDiscoverProductPrice')]//span[contains(@class,'HighlightDiscoverProductPrice_main_price')]", moduleId)));
    }
    private static Locator getHDPInventoryStrikePrice(String moduleId){
        return new Locator("HDP_INVENTORY_STRIKE_PRICE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'HighlightDiscoverProductPrice_slash_price')]", moduleId)));
    }

    public void goToHomePage() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get(STAGING_DWEB_URL);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.getWithDynamicTimeout(PREPROD_DWEB_URL, 60);
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get(PROD_DWEB_URL);
        }
        try {
            driverTools.waitForElementToAppear(X_CLOSE_POP_UP, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        } catch (Exception e) {
        }
        try {
            driverTools.waitForElementToAppear(CLOSE_POP_UP, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        } catch (Exception e) {
        }
        try {
            dismissHomeCoachmark();
        } catch (Exception ignored) {
        }
    }

    public void dismissHomeCoachmark() {
        String sid = session.sessionId();
        if (Boolean.TRUE.equals(sessionToHomeCoachmark.get(sid))) {
            return;
        }
        if (driverTools.waitForElementToAppear(HALO_TIKET_COACHMARK_CONTAINER, 3).isEmpty()) {
            return;
        }
        try {
            driverTools.waitForElementToAppear(HALO_TIKET_COACHMARK_OK_BUTTON, 3)
                    .ifPresent(driverTools::clickWithJavascriptExecutor);
            driverTools.waitForElementToDisappear(HALO_TIKET_COACHMARK_CONTAINER, 5);
            sessionToHomeCoachmark.put(sid, true);
            logger.log("Home coachmark dismissed for session " + sid);
        } catch (Exception e) {
            logger.log("Home coachmark dismiss skipped: " + e.getMessage());
        }
    }

    public void waitForHomePageToOpen() {
        driverTools.waitForElementToAppear(HOME_PAGE_LOGO, 10);
    }

    @Override
    public void clickRegisterButton() {
        driverTools.waitForElementToAppear(HOME_PAGE_LOGO, 10);
    }

    public boolean isHomePageOpened() {
        boolean isLogoDisplayed = driverTools.isElementDisplayed(HOME_PAGE_LOGO);
        Environment env = Configs.ENVIRONMENT;

        boolean isHeadingDisplayed = driverTools.isElementDisplayed(
                env == Environment.GK || env == Environment.PREPROD ? HOME_PAGE_HEADING_NEW : HOME_PAGE_HEADING_OLD
        );
        return isLogoDisplayed && isHeadingDisplayed;
    }

    @SneakyThrows
    @Override
    public void clickOnLoginButton() {
        driverTools.switchToNative();
        driverTools.waitForElementToPresence(SKIP_LOGIN_POPUP, 5).ifPresent(driverTools::click);
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(ACCOUNT_ICON);
        dismissHomeCoachmark();
        driverTools.click(ACCOUNT_ICON);
        driverTools.waitForElementToAppear(LOGIN_BUTTON);
        driverTools.click(LOGIN_BUTTON);
        boolean loginBtnDisappear = driverTools.waitForElementToDisappear(LOGIN_BUTTON);
        int retry = 0;
        while (!loginBtnDisappear && retry <= 3) {
            driverTools.clickWithJavascriptExecutor(LOGIN_BUTTON);
            loginBtnDisappear = driverTools.waitForElementToDisappear(LOGIN_BUTTON);
            retry++;
        }
    }

    @SneakyThrows
    public void selectCurrency(String currency) {
        try {
            selectCurrencyStep(currency);
        } catch (StaleElementReferenceException e) {
            driver.navigate().refresh();
            driverTools.waitForWebToLoad();
            Thread.sleep(1000);
            selectCurrencyStep(currency);
        }
    }

    public void clickOnHomeIcon() {
        driverTools.waitForElementToPresence(HOME_ICON);
        driverTools.click(HOME_ICON);
    }

    public void selectCurrencyStep(String currency) {
        String currentCurrency = getCurrentCurrency();
        if (!currentCurrency.equals(currency)) {
            driverTools.click(CURRENCY_HEADER_TEXT);
            Locator CURRENCY_TEXT = getCurrencyTextLocator(currency);
            driverTools.waitForElementToAppear(CURRENCY_TEXT);
            driverTools.scrollIntoWebView(CURRENCY_TEXT);
            driverTools.clickWithJavascriptExecutor(CURRENCY_TEXT);
        }
        goToHomePage();
        driverTools.waitForWebToLoad();
    }

    public boolean verifyVerticalIsDisplayed(String vertical) {
        goToHomePage();
        final String verticalName = convertVerticalNameIfRequired(vertical);
        Locator HOME_PRODUCT_ICON = getVerticalLocator(verticalName);
        return driverTools.waitForElementToPresence(HOME_PRODUCT_ICON).isPresent();
    }

    @Override
    public void goToMyOrderPage() {
        driverTools.waitForElementToPresence(YOUR_ORDER_ICON);
        driverTools.click(YOUR_ORDER_ICON);
    }

    public String getCurrentCurrency() {
        driverTools.click(ACCOUNT_ICON);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToPresence(CURRENCY);
        driverTools.scrollIntoWebView(CURRENCY);
        WebElement currencyElement = driverTools.waitForElementToPresence(CURRENCY_HEADER_TEXT, 5).orElseThrow();
        String currencyText = currencyElement.getText();
        if (currencyText.contains("|")) {
            currencyText = currencyText.substring(currencyText.lastIndexOf("|") + 1).trim();
        }
        logger.log("Current currency: " + currencyText);
        return currencyText;
    }

    @Override
    public String getPageLanguageCode() {
        String langCode = "";
        driverTools.click(HAMBURGER_MENU);
        Optional<WebElement> languageSelector = driverTools.waitForElementToAppear(getLanguageSelector("Language"), 2);
        if (languageSelector.isPresent()) {
            langCode = "en";
        } else {
            langCode = "id";
        }
        driverTools.click(HAMBURGER_CLOSE);
        return langCode;
    }

    @Override
    public void goToFlightPage() {

    }

    @Override
    public void selectEnglishLanguageOnly() {

    }

    @Override
    public String getLanguage() {
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        return accountPageWeb.getCurrentLanguage();
    }

    @Override
    public void openHelpCenter() {
        try {
            driverTools.scrollIntoWebView(HELP_CENTER);
            driverTools.click(HELP_CENTER);
        }catch (ElementClickInterceptedException e){
            logger.log("Click intercepted, retrying");
            driverTools.clickWithJavascriptExecutor(HELP_CENTER);
        }
        if (driverTools.waitForElementToAppear(HELP_CENTER_RETRY).isPresent()) {
            driverTools.click(HELP_CENTER_RETRY);
        }
    }

    @Override
    public void clickLanguageSelector() {

    }

    @Override
    public void selectBahasaLanguage() {
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        accountPageWeb.changeLanguage("id");
    }

    @Override
    public String getImportantInfoMessage() {
        return null;
    }

    @Override
    public void selectEnglishLanguage() {
        AccountPageWeb<?> accountPageWeb = AccountPageWeb.getInstance(session);
        accountPageWeb.changeLanguage("en");
    }

    @Override
    public boolean isCurrencyAvailable(String currencyCode) {
        return false;
    }

    @Override
    public int numberOfAvailableCurrency() {
        return 0;
    }

    @Override
    public List<String> getSuggestedCurrencyNamesOnConsumerSide() {
        return null;
    }

    @Override
    public void openVerticalPage(String vertical) {
        final String verticalName = convertVerticalNameIfRequired(vertical);
        this.clickHomeProduct(verticalName);
    }

    public void clickHomeProduct(String vertical) {
        Locator HOME_PRODUCT_ICON = getVerticalLocator(vertical);
        if (driverTools.isElementDisplayed(HOME_PRODUCT_ICON) == false) {
            driverTools.click(MULCUR_VERTICAL_ICONS_PAGECONTROL2);
        }
        driverTools.click(HOME_PRODUCT_ICON);
    }

    @Override
    public void goToWishlistPage() {
        driverTools.click(WISHLIST_ICON);

    }

    @Override
    public void openSettings() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToPresence(ACCOUNT_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public boolean isClaimCenterPageOpened() {
        return false;
    }

    @Override
    public void goToClaimCenterPage() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement savePassenger = driverTools.waitForElementToAppear(CLAIM_CENTER_DIV, 2).orElseThrow();
                driverTools.click(savePassenger);
                return;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public boolean openTnC() {
        return false;
    }

    @Override
    public boolean goToVoucherBoxPage() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                goToAccountPage();
                driverTools.waitForElementToAppear(VOUCHER_BOX, 2).orElseThrow();
                driverTools.click(VOUCHER_BOX);
                driverTools.waitForElementToPresence(VOUCHER_BOX_PAGE);
                return driverTools.isElementDisplayed(VOUCHER_BOX_PAGE);
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
        }
        return false;
    }

    @Override
    public boolean goToReviewPage() {
        goToAccountPage();
        WebElement yourReview = driverTools.element(YOUR_REVIEW).orElseThrow();
        driverTools.scrollIntoWebView(yourReview);
        driverTools.clickWithJavascriptExecutor(yourReview);
        return driverTools.waitForElementToPresence(YOUR_REVIEW_PAGE).isPresent();
    }

    @Override
    public String getRandomCurrency(String currentCurrency) {
        AccountPageWeb<?> accountPage = AccountPageWeb.getInstance(session);
        return accountPage.getRandomCurrency(currentCurrency);
    }

    @Override
    public boolean openPrivacyPolicy() {
        goToAccountPage();
        WebElement aboutTiket = driverTools.element(ABOUT_TIKET).orElseThrow();
        driverTools.scrollIntoWebView(aboutTiket);
        driverTools.clickWithJavascriptExecutor(aboutTiket);
        driverTools.click(OPEN_PRIVACY_POLICY);
        return driverTools.waitForElementToPresence(PP_PAGE).isPresent();
    }

    @Override
    public boolean isOnInsurancePage() {
        return false;
    }

    @Override
    public void goToSection(String sectionTitleHome) {

    }

    @Override
    public String getCampaignProduct(String productNameTtd) {
        return null;
    }

    @Override
    public void openInbox() {
        WebElement inboxIcon = driverTools.waitForElementToAppear(INBOX_ICON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(inboxIcon);
    }

    @Override
    public void goToTiketb2cPage() {

    }

    @Override
    public void goToTiketb2cPageIfPresent() {
        logger.log("Go To tiket.com page button is not applicable on mWeb, skipping");
    }

    private Locator getCurrencyTextLocator(String currency) {
        return new Locator("CURRENCY_TEXT", new By.ByXPath("//div[contains(@class, 'CurrencyListSelection')]/span[contains(@class, 'Text_text') and contains(text(), '" + currency + "')]"));
    }

    private String convertVerticalNameIfRequired(String verticalName) {
        if (!isSessionInEnglish()) {
            verticalName = this.translateVerticalNameToBhs(verticalName);
        }
        return verticalName;
    }

    private boolean isSessionInEnglish() {
        driverTools.waitForElementToAppear(LANGUAGE_ID);
        return driverTools.isElementDisplayed(LANGUAGE_ID);
    }

    protected String translateVerticalNameToBhs(String vertical) {
        String translatedVerticalName = CoreFunctions.verticalEngToId(vertical);
        return translatedVerticalName == null ? vertical : translatedVerticalName;
    }

    private Locator getVerticalLocator(String vertical) {
        return new Locator("HOME_PRODUCT_ICON", By.xpath("//p[contains(@class, 'MulticurrencyVerticalIcons_vertical_name') and contains(text(), '" + vertical + "')]"));
    }

    private String removeTrailingS(String vertical) {
        if (vertical.endsWith("s") && vertical.length() > 1) {
            vertical = vertical.substring(0, vertical.length() - 1);
        }

        return vertical;
    }

    @Override
    protected String getVerticalPageBaseUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return STAGING_DWEB_URL;
        }
        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            return PREPROD_DWEB_URL;
        }
        if (Configs.ENVIRONMENT == Environment.PROD) {
            return PROD_DWEB_URL;
        }
        throw new NotImplementedException("Unimplemented environment for vertical page URL!");
    }

    @Override
    protected void onAfterOpenVerticalPageViaURL() {
        continueWithoutApp(10);
    }

    @Override
    public void continueWithoutApp(int timeoutInSeconds) {
        driverTools.waitForElementToAppear(CONTINUE_WITHOUT_APP_POP_UP, timeoutInSeconds);
        if (driverTools.isElementDisplayed(CONTINUE_WITHOUT_APP_POP_UP)) {
            driverTools.click(CONTINUE_WITHOUT_APP_POP_UP);
        }
    }

    public boolean verifyContentGSModule() {
        boolean isGSInfoCloseButtonAppear = driverTools.isElementDisplayed(GS_INFO_CLOSE_BUTTON);
        driverTools.scrollToElement(GS_POP_BANNER);
        boolean isGSPopBannerAppear = driverTools.isElementDisplayed(GS_POP_BANNER);
        boolean isGSPopTitleAppear = driverTools.isElementDisplayed(GS_POP_TITLE);

        boolean isGSMSChipsAppear = driverTools.isElementDisplayed(GS_MS_CHIPS);
        boolean isGSMSSeeAllButtonAppear = driverTools.isElementDisplayed(GS_MS_SEE_ALL_BUTTON);

        boolean isGSISProductTitleAppear = driverTools.isElementDisplayed(GS_IS_PRODUCT_TITLE);
        boolean isGSISProductSubtitleAppear = driverTools.isElementDisplayed(GS_IS_PRODUCT_SUBTITLE);

        Assertion.assertThat("Verify GS Info Close Button Appear", isGSInfoCloseButtonAppear, is(true));

        Assertion.assertThat("Verify GS Pop Banner Appear", isGSPopBannerAppear, is(true));
        Assertion.assertThat("Verify GS Pop Title Appear", isGSPopTitleAppear, is(true));

        Assertion.assertThat("Verify GS MS Chips Appear", isGSMSChipsAppear, is(true));
        Assertion.assertThat("Verify GS MS See All Button Appear", isGSMSSeeAllButtonAppear, is(true));

        Assertion.assertThat("Verify GS IS Product Title Appear", isGSISProductTitleAppear, is(true));
        Assertion.assertThat("Verify GS IS Product Subtitle Appear", isGSISProductSubtitleAppear, is(true));

        return true;
    }

    public void inputVerticalKeywords() {
        boolean gsModalAppear = driverTools.isElementDisplayed(GLOBAL_SEARCH_MODAL);
        if(gsModalAppear){
            String keywordsInput = sheetData.get("keywords").toLowerCase();
            logger.log("keywords input = " + keywordsInput);
            driverTools.sendKeys(GS_INPUT_COLUMN_MOBILE, keywordsInput);
        } else{
            System.out.println("Modal not appearing");
        }
    }

    public void verifyVerticalIconText(String keywords) {
        driverTools.waitForElementToPresence(GS_IMAGE_VERTICAL_ICON);
        driverTools.waitForElementToPresence(GS_VERTICAL_TITLE);
        boolean isGSVerticalIconVisible = driverTools.isElementDisplayed(GS_IMAGE_VERTICAL_ICON);
        boolean isGSVerticalTextVisible = driverTools.isElementDisplayed(GS_VERTICAL_TITLE);
        Assertion.assertThat("Verify Vertical Icon Exist", isGSVerticalIconVisible, is(true));
        Assertion.assertThat("Verify Vertical Text Exist", isGSVerticalTextVisible, is(true));
    }

    public void verifyCurrencyHeaderIDR() {
        driverTools.waitForElementToAppear(MOBILE_HAMBURGER_HEADER);
        driverTools.waitForElementToAppear(CURRENCY_MOBILE_BOTTOMSHEET_ENTRYPOINT, 2);
        driverTools.click(CURRENCY_MOBILE_BOTTOMSHEET_ENTRYPOINT);
        driverTools.waitForElementToAppear(CURRENCY_MOBILE_BOTTOMSHEET);
        boolean isCurrencyBottomSheetAppear = driverTools.isElementDisplayed(CURRENCY_MOBILE_BOTTOMSHEET);
        if (isCurrencyBottomSheetAppear == true) {
            Assertion.assertThat("Verify Chosen Currency is IDR", driverTools.isElementDisplayed(CHOSEN_IDR_CURRENCY), is(true));
        } else {
            logger.log("Currency not found");
        }
    }

    public void verifyIWCCurrencyIDR(String moduleId) {
        Assertion.assertThat("IWC moduleId must be provided", moduleId != null && !moduleId.isBlank(), is(true));

        Locator iwcCurrency = new Locator("IWC_CURRENCY", new By.ByXPath("(//section[@id='" + moduleId + "']//div[contains(text(), 'IDR')])[1]"));
        boolean isIWCCurrencyIDR = driverTools.waitForElementToAppear(iwcCurrency).isPresent();
        Assertion.assertThat("Verify IWC Currency IDR", isIWCCurrencyIDR, is(true));
    }

    public void verifyHDPCurrencyIDR() {
        boolean isHDPCurrencyIDR = driverTools.isElementDisplayed(HDP_CURRENCY);
        Assertion.assertThat("Verify HDP Currency IDR", isHDPCurrencyIDR, is(true));
    }

    public void verifyFlashSaleCurrencyIDR() {
        boolean isFlashSaleCurrencyIDR = driverTools.isElementDisplayed(FLASH_SALE_CURRENCY);
        Assertion.assertThat("Verify Flash Sale Currency IDR", isFlashSaleCurrencyIDR, is(true));
    }

    @Override
    public void goToAccountPage() {
        driverTools.waitForElementToPresence(ACCOUNT_BUTTON);
        driverTools.click(ACCOUNT_BUTTON);
    }

    @Override
    public void goToSavePassengerData() {
        goToAccountPage();
        WebElement save_passenger = driverTools.waitForElementToAppear(SAVE_PASSENGER_DATA).orElseThrow();
        driverTools.scrollIntoWebView(save_passenger);
        driverTools.clickWithJavascriptExecutor(save_passenger);
    }

    public void clickSeeAllFlashSale() {
        driverTools.waitForElementToPresence(SEE_ALL_FLASH_SALE_BTN);
        String initialUrl = driverTools.getCurrentURL();
        driverTools.clickWithJavascriptExecutor(SEE_ALL_FLASH_SALE_BTN);
        driverTools.waitForElementToAppear(SEE_ALL_REDIRECTION);
        String currentURL = driverTools.getCurrentURL();
        Assert.assertNotEquals("Verify URL Redirection.", initialUrl, currentURL);
    }

    public void clickOneOfProductIcon() {
        // TO BE DEVELOPED
    }

    public void clickDatePicker() {
        // TO BE DEVELOPED
    }

    public boolean isDescriptionHolidayDisplay() {
        // TO BE DEVELOPED
        return true;
    }

    public void closeCalendar() {
        // TO BE DEVELOPED
    }

    public void closeLanguageCurrencyPopup() {
        // TO BE DEVELOPED
    }

    public void verticalRedirectionFromGS(String keywords) {
        driverTools.click(GS_VERTICAL_TITLE);
        String url = driver.getCurrentUrl();
        Assertion.assertThat("Verify url contains " + keywords + " url", url, containsStringIgnoringCase(keywords));
    }

    @Override
    public boolean verifyHomeIconImage() {
        driverTools.waitForElementToAppear(getImgVerticalLocator("Events", "Event"));
        boolean isEventIconAppeared = driverTools.isElementDisplayed(getImgVerticalLocator("Events", "Event"));
        boolean isHotelIconAppeared = driverTools.isElementDisplayed(getImgVerticalLocator("Hotel", "Hotels"));
        boolean isTrainIconAppeared = driverTools.isElementDisplayed(getImgVerticalLocator("Train", "Kereta Api"));
        boolean isCarIconAppeared = driverTools.isElementDisplayed(getImgVerticalLocator("Car Rentals", "Sewa Mobil"));
        return isEventIconAppeared && isHotelIconAppeared && isTrainIconAppeared && isCarIconAppeared;
    }

    @Override
    public Locator getImgVerticalLocator(String verticalEn, String verticalId) {
        return new Locator("VERTICAL_IMG_MWEB", By.xpath("//span[contains(@class, 'VerticalIcons_vertical_name') and (contains(text(), '" + verticalEn + "') or contains(text(), '" + verticalId + "'))]"));
    }

    public void goToSavedPaymentMethod() {
        WebElement savedPaymentMethod = driverTools.waitForElementToAppear(SAVED_PAYMENT_METHOD, 30).orElseThrow();
        driverTools.scrollIntoWebView(savedPaymentMethod);
        driverTools.waitForElementToPresence(SAVED_PAYMENT_METHOD);
        try {
            driverTools.clickWithJavascriptExecutor(SAVED_PAYMENT_METHOD);
        } catch (Exception e) {
            logger.log("Exception occured. Retrying" + e.getMessage());
            driverTools.clickWithJavascriptExecutor(SAVED_PAYMENT_METHOD);
        }
    }

    public boolean verifyContentGS_Info() {
        driverTools.waitForElementToAppear(GS_INFO_TITLE);
        boolean isGSInfoTitleAppear;
        try {
            isGSInfoTitleAppear = driverTools.isElementDisplayed(GS_INFO_TITLE);
        } catch (StaleElementReferenceException e) {
            driverTools.waitForElementToAppear(GS_INFO_TITLE);
            isGSInfoTitleAppear = driverTools.isElementDisplayed(GS_INFO_TITLE);
        }
        boolean isGSInfoIconAppear = driverTools.isElementDisplayed(GS_INFO_ICON);
        boolean isTextLinkAppear = driverTools.isElementDisplayed(INFO_BANNER_TEXT);

        driverTools.click(INFO_BANNER_TEXT);
        boolean isTextLinkClickable = checkURLDoestMatch();

        goToHomePage();

        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_MODAL, 2);

        return (isGSInfoTitleAppear && isGSInfoIconAppear && isTextLinkAppear && isTextLinkClickable);
    }

    public boolean verifyContentPopular() {
        continueWithoutApp(5);

        driverTools.waitForElementToAppear(POPULAR_EVENTS_TITLE);
        String titleContainsPopular = String.valueOf(driverTools.getText(POPULAR_EVENTS_TITLE));
        boolean isPopularIcon = driverTools.isElementDisplayed(KPOP_EVENT_IMAGE);
        driverTools.click(KPOP_EVENT_IMAGE);
        boolean isPopularIconClickable = checkURLDoestMatch();

        return (titleContainsPopular.contains("popular") && isPopularIcon && isPopularIconClickable);
    }

    public boolean verifyContentInspired() {
        goToHomePage();
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        driverTools.hideKeyboard();
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_MODAL, 2);
        driverTools.scrollAndCheckElement(INSPIRED_TITLE);
        driverTools.waitForElementToAppear(INSPIRED_TITLE);
        String titleContainsPopular = String.valueOf(driverTools.getText(INSPIRED_TITLE));
        boolean isPopularIcon = driverTools.isElementDisplayed(INSPIRED_IMAGE);
        driverTools.click(INSPIRED_IMAGE);
        boolean isInspireIconClickable = checkURLDoestMatch();
        return (titleContainsPopular.contains("Inspired") && isPopularIcon && isInspireIconClickable);
    }


    @Override
    public void flashSaleCheck() {
        driverTools.isElementDisplayed(FLASHSALE);
        driverTools.waitForElementToBeClickable(CLICK_FLASHSALE);
        driverTools.clickWithJavascriptExecutor(CLICK_FLASHSALE);
    }

    @Override
    public void enterVerticalName(String keywords){
        boolean gsModalAppear = driverTools.isElementDisplayed(GLOBAL_SEARCH_MODAL);
        if(gsModalAppear){
            String keywordsInput = sheetData.get("keywords");
            logger.log("keywords input = " + keywordsInput);
            driverTools.sendKeys(GS_INPUT_COLUMN_MOBILE, keywordsInput);
        } else{
            System.out.println("Modal not appearing");
        }
    }

    @Override
    public boolean verifyVerticalSection() {
        String titleContainsVertical = String.valueOf(driverTools.getText(GS_VERTICAL_TITLE));
        boolean isVerticalIconAppear = driverTools.isElementDisplayed(GS_HOTEL_ICON);
        driverTools.click(GS_HOTEL_ICON);
        boolean isVerticalIconClickable = checkURLDoestMatch();

        Assertion.assertThat("Verify content search column is Hotel", titleContainsVertical, containsStringIgnoringCase("Hotel"));
        Assertion.assertThat("Verify vertical icon appear on the search column", isVerticalIconAppear, is(true));
        Assertion.assertThat("Verify vertical icon clickable", isVerticalIconClickable, is(true));

        return (titleContainsVertical.contains("Hotel") && isVerticalIconAppear && isVerticalIconClickable);
    }

    @Override
    public boolean verifyArticleSection() {
        driverTools.scrollIntoWebViewMWeb(ARTICLE_TITLE);
        boolean isArticleTitleVisible = driverTools.isElementDisplayed(ARTICLE_TITLE);
        boolean isArticleIconVisible = driverTools.isElementDisplayed(ARTICLE_IMAGE);
        driverTools.click(ARTICLE_IMAGE);
        driverTools.waitForElementToPresence(ARTICLE_SAMPLE,2);
        boolean isArticleClickable = driverTools.isElementDisplayed(ARTICLE_SAMPLE);

        Assertion.assertThat("Verify Article Title Appear", isArticleTitleVisible, is(true));
        Assertion.assertThat("Verify Article Icon Appear", isArticleIconVisible, is(true));
        Assertion.assertThat("Verify Article Info Clickable", isArticleClickable, is(true));
        return (isArticleTitleVisible && isArticleIconVisible && isArticleClickable);
    }

    @Override
    public void clickSeeAllPromo() {
        driverTools.scrollIntoWebView(SEE_ALL_PROMO);
        driverTools.waitForElementToBeClickable(SEE_ALL_PROMO);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_PROMO);
    }

    @Override
    public void verifyGS_Top_Result(String keywords){
        driverTools.waitForElementToPresence(gsTopSearchResult(keywords), 10);
        boolean isTopResultAppear = driverTools.isElementPresent(gsTopSearchResult(keywords));
        Assertion.assertThat("Verify top search result is appearing ", isTopResultAppear, is(true));
    }

    @Override
    public void verifyGS_Info_Result(String keywords){
        driverTools.scrollIntoWebView(gsInfoSearchResult(keywords));
        driverTools.waitForElementToPresence(gsInfoSearchResult(keywords));
        boolean isInfoResultAppear = driverTools.isElementInView(gsInfoSearchResult(keywords));
        Assertion.assertThat("Verify info search result is appearing ", isInfoResultAppear, is(true));
    }

    @Override
    public boolean checkBannerScroll(String moduleId) {
        driverTools.waitForElementToAppear(BANNER_CAROUSEL_SCROLL);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL_SCROLL);
    }
    @Override
    public boolean checkBannerCarouselTitleInside(String moduleId) {
        driverTools.isElementDisplayed(BANNER_CAROUSEL_INSIDE_TITLE);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL_INSIDE_TITLE);
    }
    @Override
    public boolean checkBannerCarouselSubTitleInside(String moduleId) {
        driverTools.isElementDisplayed(BANNER_CAROUSEL_INSIDE_SUBTITLE);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL_INSIDE_SUBTITLE);
    }
    @Override
    public void clickSeeAllBannerCarousel(String moduleId) {
        driverTools.waitForElementToAppear(SEE_ALL_BANNER_CAROUSEL);
        driverTools.waitForElementToBeClickable(SEE_ALL_BANNER_CAROUSEL);
        driverTools.clickWithJavascriptExecutor(SEE_ALL_BANNER_CAROUSEL);
    }
    @Override
    public boolean bannerCarouselRedirectionCheck() {
        driverTools.waitForElementToAppear(PROMO_LIST);
        return driverTools.isElementDisplayed(PROMO_LIST);
    }

    @Override
    public boolean verifyVerticalIcons(String vertical) {
        boolean isVerticalPageOpen = false;

        switch (vertical.toLowerCase()) {
            case "flight":
                driverTools.clickWithJavascriptExecutor(FLIGHT_PAGE_BUTTON);
                driverTools.waitForElementToPresence(FLIGHT_SEARCH_PAGE, 5);
                isVerticalPageOpen = driverTools.isElementDisplayed(FLIGHT_SEARCH_PAGE);
                goToHomePage();
                logger.log("is flight vertical open -> " + isVerticalPageOpen);
                break;

            case "hotel":
                clickVerticalTitle("Hotel", "Hotels");
                driverTools.waitForElementToPresence(HOTEL_SEARCH_PAGE, 2);
                isVerticalPageOpen = driverTools.isElementDisplayed(HOTEL_SEARCH_PAGE);
                goToHomePage();
                logger.log("is hotel vertical open -> " + isVerticalPageOpen);
                break;

            case "to do":
                driverTools.clickWithJavascriptExecutor(TO_DO_ICON);
                driverTools.waitForElementToPresence(TODO_SEARCH_PAGE, 2);
                isVerticalPageOpen = driverTools.isElementDisplayed(TODO_SEARCH_PAGE);
                goToHomePage();
                logger.log("is to do vertical open -> " + isVerticalPageOpen);
                break;

            case "car":
                clickVerticalTitle("Car Rentals", "Sewa Mobil");
                driverTools.waitForElementToPresence(CAR_RENTAL_SEARCH_PAGE, 2);
                isVerticalPageOpen = driverTools.isElementDisplayed(CAR_RENTAL_SEARCH_PAGE);
                goToHomePage();
                logger.log("is car rental vertical open -> " + isVerticalPageOpen);
                break;

            case "train":
                clickVerticalTitle("Train", "Kereta Api");
                driverTools.waitForElementToPresence(TRAIN_SEARCH_PAGE,2);
                isVerticalPageOpen = driverTools.isElementDisplayed(TRAIN_SEARCH_PAGE);
                goToHomePage();
                logger.log("is train vertical open -> " + isVerticalPageOpen);
                break;

            case "event":
                clickVerticalTitle("Events", "Event");
                String url = driverTools.getCurrentURL();
                driverTools.waitForElementToPresence(EVENT_SEARCH_PAGE, 10);
                isVerticalPageOpen = driverTools.isElementDisplayed(EVENT_SEARCH_PAGE) && url.toLowerCase().contains("event");
                goToHomePage();
                logger.log("is event vertical open -> " + isVerticalPageOpen);
                break;

            case "airport transfer":
                clickVerticalTitle("Airport Pickups", "Jemputan Bandara");
                driverTools.waitForElementToPresence(AIRPORT_TRANSFER_SEARCH_PAGE,2);
                isVerticalPageOpen = driverTools.isElementDisplayed(AIRPORT_TRANSFER_SEARCH_PAGE);
                goToHomePage();
                logger.log("is airport transfer vertical open -> " + isVerticalPageOpen);
                break;

            default:
                logger.log("Vertical not supported: " + vertical);
                break;
        }

        return isVerticalPageOpen;
    }

    @Override
    public boolean verifyIWCSectionNonIDR(){
        driverTools.refreshWebpage();
        driverTools.scrollIntoWebView(IWC_SECTION_NON_IDR);
        driverTools.waitForElementToPresence(IWC_SECTION_NON_IDR, 5);
        driverTools.waitForElementToPresence(IWC_PC_NON_IDR, 2);
        boolean isIWCSectionAppear = driverTools.isElementDisplayed(IWC_SECTION_NON_IDR);
        boolean isIWCNonIDR = driverTools.isElementDisplayed(IWC_PC_NON_IDR);
        Assertion.assertThat("Verify Image With Chips Section Appear" , isIWCSectionAppear, is(true));
        Assertion.assertThat("Verify Image With Chips Product Card Appear" , isIWCNonIDR, is(true));
        return isIWCSectionAppear && isIWCNonIDR;
    }

    @Override
    public boolean verifyHDPSectionNonIDR(){
        driverTools.waitForElementToAppear(CLOSE_POP_UP, 5).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.scrollIntoWebView(HDP_SECTION);
        driverTools.waitForElementToPresence(HDP_SECTION, 2);
        boolean isHDPSectionAppear = driverTools.isElementDisplayed(HDP_SECTION);
        driverTools.waitForElementToPresence(HDP_PC_NON_IDR, 2);
        boolean isHDPNonIDR = driverTools.isElementDisplayed(HDP_PC_NON_IDR);
        Assertion.assertThat("Verify Highlight Discovery Product Section Appear" , isHDPSectionAppear, is(true));
        Assertion.assertThat("Verify Highlight Discovery Product Card Appear" , isHDPNonIDR, is(true));
        return isHDPSectionAppear && isHDPNonIDR;
    }

    @Override
    public boolean verifyFlashSaleSectionNonIDR(){
        driverTools.refreshWebpage();
        driverTools.scrollIntoWebView(FS_SECTION_NON_IDR);
        driverTools.waitForElementToPresence(FS_SECTION_NON_IDR,2);
        driverTools.scrollIntoWebView(FS_PC_NON_IDR);
        driverTools.waitForElementToPresence(FS_PC_NON_IDR, 2);
        boolean isFlashSaleSectionAppear = driverTools.isElementDisplayed(FS_SECTION_NON_IDR);
        boolean isFlashSaleNonIDR = driverTools.isElementDisplayed(FS_PC_NON_IDR);
        Assertion.assertThat("Verify Flash Sale Section Appear" , isFlashSaleSectionAppear, is(true));
        Assertion.assertThat("Verify Flash Sale Product Card Appear" , isFlashSaleNonIDR, is(true));
        return isFlashSaleSectionAppear && isFlashSaleNonIDR;
    }

    public void closeBottomsheetPromo() {
        driverTools.waitForElementToAppear(CLOSE_BOTTOMSHEET_PROMO).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    @Override
    public boolean verifyHDPProductImage(String moduleId) {
        WebElement productImageElement = driverTools.element(getProductImage(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(productImageElement);
    }

    @Override
    public String getHDPInventoryNameAndClick(String moduleId){
        List<WebElement> HDPInventoriesName = driverTools.elements(getHDPInventoryName(moduleId)).orElseThrow();
        String inventoryName = driverTools.getText(HDPInventoriesName.get(0));
        driverTools.click(HDPInventoriesName.get(0));
        return inventoryName;
    }

    @Override
    public boolean verifyHDPInventoryLocation(String moduleId) {
        WebElement titleElement = driverTools.element(getHDPInventoryLocation(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    @Override
    public boolean verifyHDPInventoryName(String moduleId) {
        WebElement titleElement = driverTools.element(getHDPInventoryName(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    @Override
    public boolean verifyHDPPrice(String moduleId) {
        WebElement priceElement = driverTools.element(getHDPInventoryPrice(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(priceElement);
    }

    @Override
    public boolean verifyHDPStrikePrice(String moduleId) {
        WebElement priceElement = driverTools.element(getHDPInventoryStrikePrice(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(priceElement);
    }

    @Override
    public void openHotelPDPViaURL(String s) {
        String baseUrl = "";

        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = STAGING_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            baseUrl = PREPROD_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = PROD_DWEB_URL;
        }

        driverTools.get(baseUrl + "hotel" + s);
        continueWithoutApp(10);
    }

    @Override
    public void openHotelPDPViaURLWithDate(String s, String startDate, String endDate) {
        String baseUrl = "";

        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = STAGING_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            baseUrl = PREPROD_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = PROD_DWEB_URL;
        }

        driverTools.get(baseUrl + "hotel" + s + "?checkin=" + startDate + "&checkout=" + endDate);
        continueWithoutApp(10);
    }

    @Override
    public void clickPromoListHeaderEntrypoint() {
        driverTools.waitForElementToAppear(PROMO_LIST_HEADER_ENTRYPOINT);
        driverTools.clickWithJavascriptExecutor(PROMO_LIST_HEADER_ENTRYPOINT);
    }

}