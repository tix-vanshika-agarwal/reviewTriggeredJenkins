package com.tiket.web.page.common.home;

import com.tiket.appium.PopupObserver;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.model.Platform;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.page.common.home.Setting_Page.SettingsPageWeb;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;
import org.openqa.selenium.grid.config.MapConfig;
import org.testng.Assert;
import org.testng.SkipException;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.WebElement;
import static org.hamcrest.Matchers.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HomePageDWeb<T extends WebDriver> extends HomePageWeb<T> {

    public HomePageDWeb(Session<T> session) {
        super(session);
    }

    private static final String STAGING_DWEB_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_DWEB_URL = "https://preprod.tiket.com/";
    private static final String PROD_DWEB_URL = "https://www.tiket.com/";
    private static final Locator HOME_PAGE_LOGO = new Locator("HOME_PAGE_LOGO", By.xpath("//img[contains(@class , 'DesktopLogo')]"));
    private static final Locator HOME_PAGE_HEADING_NEW = new Locator("HOME_PAGE_HEADING", By.xpath("//div[contains(@class,'HomeDesktopHeaderSection_greeting')]//h2"));
    private static final Locator HOME_PAGE_HEADING_OLD = new Locator("HOME_PAGE_HEADING", By.xpath("//div[contains(@class,'search_content')]"));
    private static final Locator LANGUAGE_SELECTOR_BUTTON = new Locator("LANGUAGE_SELECTOR_BUTTON", By.xpath("//button[contains(@class,'LanguageAndCurrencyModal')]"));
    private static final Locator COUNTRY_CURRENCY_LANG_SAVE_BUTTON = new Locator("COUNTRY_CURRENCY_LANG_SAVE_BUTTON", By.xpath("//button[contains(@class,'LanguageAndCurrencyModal') and (contains(.,'Save') or contains(.,'Simpan'))]"));
    private static final Locator CLOSE_LANGUAGE_CURRENCY_DIALOG = new Locator("CLOSE_LANGUAGE_CURRENCY_DIALOG", By.xpath("//button[contains(@class,'button_close')]//*[local-name()='svg']"));
    private static final Locator IMPORTANT_INFO_MESSAGE = new Locator("IMPORTANT_INFO_MESSAGE", By.xpath("(//*[contains(@class,'info_content')]//span)[last()]"));
    private static final Locator LANGUAGE_SECTION = new Locator("LANGUAGE_SECTION", By.xpath("//div[contains(@class,'modal_language')]"));
    private static final Locator ENGLISH_LANGUAGE = new Locator("ENGLISH_LANGUAGE", By.xpath("//*[contains(@class,'Radio-module') and contains(@value, 'en')]"));
    private static final Locator BAHASA_LANGUAGE = new Locator("BAHASA_LANGUAGE", By.xpath("//*[contains(@class,'Radio-module') and contains(@value, 'id')]"));
    private static final Locator LANGUAGE_IDENTIFIER = new Locator("LANGUAGE_IDENTIFIER", By.xpath("//h3[contains(@class,'LanguageContent')]"));
    private static final Locator AVAILABLE_CURRENCY = new Locator("AVAILABLE_CURRENCY", By.xpath("//label[contains(@class,\"it rr___CurrencyContent\")]"));
    private static final Locator SUGGESTED_CURRENCY_AVAILABLE = new Locator("SUGGESTED_CURRENCY_AVAILABLE", By.xpath("(//div[contains(@class,'radio_b')])[1]//span[contains(@class,'CurrencyContent')]"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//a[contains(@href,'tiket" +
            ".com/login') or contains(.,'Login') or contains(.,'Masuk')]"));
    private static final Locator GREETING_HEADING = new Locator("GREETING_HEADING", new By.ByXPath("//h2[contains(@class,'greeting')]"));
    private static final Locator LOGIN_PROFILE_ICON = new Locator("LOGIN_PROFILE_ICON", new By.ByXPath("//div[contains(@class,'blink_icon')]"));
    private static final Locator INBOX_ICON = new Locator("INBOX_ICON", new By.ByXPath("//a[contains(@href,'/inbox')]"));
    private static final Locator YOUR_ORDERS_DIV = new Locator("YOUR_ORDERS_DIV", new By.ByXPath("//a[text() = 'Your Orders']"));
    private static final Locator YOUR_ORDER_BUTTON = new Locator("YOU_ORDER_BUTTON", new By.ByXPath("//a[contains(@href,'myorder')]"));
    private static final Locator LANGUAGE_CURRENCY_DROPDOWN = new Locator("LANGUAGE_CURRENCY_DROPDOWN", new By.ByXPath("//button[contains(@class, 'LanguageAndCurrencyModal')]"));
    private static final Locator CURRENCY_HEADER_TEXT = new Locator("CURRENCY_HEADER_TEXT", new By.ByXPath("//button[contains(@class,'LanguageAndCurrencyModal-module__trigger')]//span[last()]"));
    private static final Locator GO_TO_TIKETB2C_PAGE = new Locator("GO_TO_TIKETB2C_PAGE", new By.ByXPath("//button[contains(@class,'Button_variant_secondary') and contains(normalize-space(),'Go To tiket.com page')]"));
    private static final Locator WISHLIST_DIV = new Locator("WISHLIST_DIV", new By.ByXPath("//a[text() = 'Wishlist']"));
    private static final Locator SAVE_PASSENGER_DIV = new Locator("SAVE_PASSENGER_DIV", new By.ByXPath("//a[text() = 'Save Passenger Data' or text()='Simpan Data Penumpang']"));
    private static final Locator CLAIM_CENTER_DIV = new Locator("CLAIM_CENTER_DIV", new By.ByXPath("//a[text() = 'Claim Center']"));
    private static final Locator SETTINGS_DIV = new Locator("SETTINGS_DIV", new By.ByXPath("//a[text() = 'Settings' or text()='Pengaturan']"));
    private static final Locator HOTEL_SEARCH_PAGE = new Locator("HOTEL_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='hotel-hero-banner-container']"));
    private static final Locator TODO_SEARCH_PAGE = new Locator("TODO_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='home-location-label']//span[contains(@class, 'Text_size_h2')]"));
    private static final Locator CAR_RENTAL_SEARCH_PAGE = new Locator("CAR_RENTAL_SEARCH_PAGE", new By.ByXPath("//div[@class='car_searchform_wrapper__ZRKBS']"));
    private static final Locator HELP_CENTER = new Locator("HELP_CENTER", new By.ByXPath("//*[contains(@class,'Footer-module')]/a[contains(@href,'help-center')]"));
    private static final Locator HELP_CENTER_UNABLE_LOAD_HEADING = new Locator("HELP_CENTER_UNABLE_LOAD_HEADING", new By.ByXPath("//*[self::h1 or self::h2 or self::h3][contains(normalize-space(.),'Unable to load content')" + " or contains(normalize-space(.),'Tidak dapat memuat konten')" + " or contains(normalize-space(.),'Gagal memuat konten')]"));
    private static final Locator CLOSE_POP_UP = new Locator("CLOSE_POP_UP", new By.ByXPath("//button[contains(@class, 'Button-module') and (normalize-space(text())='Close' or normalize-space(text())='Tutup')]"));
    private static final Locator CLOSE_POP_UP_IMAGE = new Locator("CLOSE_POP_UP_IMAGE", new By.ByXPath("//*[contains(@class,'module__close_button') and not(ancestor::*[contains(@class,'InfoBanner')])]"));
    private static final Locator CLAIM_PAGE_ORDER_TAB = new Locator("CLAIM_PAGE_ORDER_TAB", new By.ByXPath("//div[contains(@class,'OrderSection_tab_list')]"));
    private static final Locator VB_DIV = new Locator("VB_DIV", new By.ByXPath("//a[text() = 'Voucher Box']"));
    private static final Locator VB_PAGE = new Locator("VB_PAGE", new By.ByXPath("//p[contains(text(), 'Voucher Box')]"));
    private static final Locator REVIEW_DIV = new Locator("REVIEW_DIV", new By.ByXPath("//a[text()='My Review' or text()='Review Kamu' or contains(text(),'Review')]"));
    private static final Locator REVIEW_PAGE = new Locator("REVIEW_PAGE", new By.ByXPath("//h1[contains(text(), 'Your Review') or contains(text(), 'Review Kamu')]"));
    private static final Locator OPEN_PRIVACY_POLICY = new Locator("OPEN_PRIVACY_POLICY", new By.ByXPath("//a[contains(@href, '/info/privacy-policy') and (contains(text(), 'Kebijakan Privasi') or contains(text(), 'Privacy Policy'))]"));
    private static final Locator PP_PAGE = new Locator("PP_PAGE", new By.ByXPath("//h3[normalize-space(text())='Privacy and Policy' or normalize-space(text())='Kebijakan dan Privasi']"));
    private static final Locator OPEN_T_N_C = new Locator("OPEN_T_N_C", new By.ByXPath("//a[text()='Terms & Conditions' or text()='Syarat & Ketentuan']"));
    private static final Locator T_N_C_PAGE = new Locator("T_N_C_PAGE", new By.ByXPath("//div[@data-testid='content']//h1[contains(text(),'Terms and Condition') or contains(text(),'Syarat dan Ketentuan Umum')]"));
    private static final Locator SKIP = new Locator("SKIP", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_skip_btn')]"));
    private static final Locator ACCOUNT_POP_UP = new Locator("ACCOUNT_POP_UP", new By.ByXPath("//button[contains(@class, 'Coachmark_coachmark_done_btn')]"));
    private static final Locator REGISTER_BUTTON = new Locator("REGISTER_BUTTON", new By.ByXPath("//a[contains(@class, 'Button-module__button') and contains(text(),'Register') or contains(text(),'Daftar')]"));
    private static final Locator REGISTER_NOW = new Locator("REGISTER_NOW", new By.ByXPath("//span[contains(@class, 'LoginLayout_redirection_link')]"));
    private static final Locator GS_INFO_CLOSE_BUTTON = new Locator("GS_INFO_CLOSE_BUTTON", By.xpath( "(//div[contains(@class, 'GlobalSearchModal')]//button[contains(@class, 'InfoBanner') and contains(@class, 'close')])[1]"));
    private static final Locator GS_INFO_NEXT_BUTTON = new Locator ("GS_INFO_NEXT_BUTTON", By.xpath("(//div[contains(@class, 'GSInfo-module__keen-slider')])[1]"));
    private static final Locator GS_POP_TITLE = new Locator("GS_POP_TITLE", By.xpath("(//div[contains(@class, 'GSPopular_heading') or contains(@class, 'GSPopular-module__heading')])[1]"));
    private static final Locator GS_POP_BANNER = new Locator("GS_POP_BANNER", By.xpath("(//img[contains(@class, 'GSPopular_default_image') or contains(@class, 'GSPopular-module__default_image')])[1]"));
    private static final Locator GS_MS_CHIPS = new Locator ("GS_MS_CHIPS", By.xpath("//div[contains(@class, 'GSMostSearch-module__chips_carousel_container')]"));
    private static final Locator GS_MS_SEE_ALL_BUTTON = new Locator ("GS_MS_SEE_ALL_BUTTON", By.xpath("(//a[contains(@class, 'GSMostSearch-module__see_all_bottom')])[1]"));
    private static final Locator GS_IS_PRODUCT_TITLE = new Locator ("GS_IS_PRODUCT_TITLE", By.xpath("(//p[contains(@class, 'GSInspirationalSearch-module__content_title') and contains(text(), 'Automation')])[1]"));
    private static final Locator GS_IS_PRODUCT_SUBTITLE = new Locator ("GS_IS_PRODUCT_SUBTITLE", By.xpath("(//p[contains(@class, 'GSInspirationalSearch-module__content_buttontext') and contains(text(), 'Automation')])[1]"));
    private static final Locator CURRENCY_LOCATOR = new Locator("CURRENCY_LOCATOR", By.xpath("//span[contains(@class, 'CurrencyContent-module__currency_code')]"));
    private static final Locator CURRENCY_CONTAINER = new Locator("CURRENCY_CONTAINER", By.xpath("//div[contains(@class, 'modal_currency')]"));
    private static final Locator LANGUAGE_AND_CURRENCY_MENU = new Locator("LANGUAGE_AND_CURRENCY_MENU", By.xpath("//button[contains(@class, 'LanguageAndCurrencyModal-module__trigger')]"));
    private static final Locator PRODUCT_HOTEL_ICON = new Locator("PRODUCT_HOTEL_ICON", new By.ByXPath("//a[contains(@href, '/hotel') and contains(@class, 'HeaderBottom-module__option')]"));
    private static final Locator LANGUAGE_AND_CURRENCY_CLOSE_BTN = new Locator("LANGUAGE_AND_CURRENCY_CLOSE_BTN", By.xpath("//button[contains(@class, 'LanguageAndCurrencyContent-module__button_close')]"));
    private static final Locator SAVE_CURRENCY_BTN = new Locator("SAVE_CURRENCY_BTN", By.xpath("//button[contains(@class, 'LanguageAndCurrencyModal-module__button')]"));
    private static Locator getBannerCarouselInsideTitle(String moduleId) {
        return new Locator("BANNER_CAROUSEL_INSIDE_TITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_title')]", moduleId)));
    }

    private static Locator getBannerCarouselInsideSubtitle(String moduleId) {
        return new Locator("BANNER_CAROUSEL_INSIDE_SUBTITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_subtext')]", moduleId)));
    }

    private static Locator getBannerCarouselScroll(String moduleId) {
        return new Locator("BANNER_CAROUSEL_SCROLL", By.xpath(String.format("(//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_subtext')])[last()]", moduleId)));
    }

    private static Locator getSeeAllBannerCarousel(String moduleId) {
        return new Locator("SEE_ALL_BANNER_CAROUSEL", By.xpath(String.format("//section[@id='%s']//a[contains(@href,'/promo') and normalize-space(.)='See All']", moduleId)));
    }

    private static Locator getSeeAllFlashSaleBtn(String moduleId) {
        return new Locator("SEE_ALL_FLASH_SALE_BTN",
                By.xpath(String.format(
                        "(//section[@id='%s']//div[contains(@class,'FlashSaleTabContent_cta')]//a)[1]", moduleId)));
    }
    private static final Locator SEE_ALL_REDIRECTION = new Locator("SEE_ALL_REDIRECTION",By.xpath("//span[text()='To Dos']"));
    private static final Locator DATE_PICKER = new Locator("DATE_PICKER", new By.ByXPath("//*[@data-testid='date-picker']"));
    private static final Locator DESCRIPTION_HOLIDAY = new Locator("DESCRIPTION_HOLIDAY", new By.ByXPath("//*[contains(@class, 'HolidaysSection_item_text')]"));
    private static final Locator CALENDAR_CLOSE_BTN = new Locator("CALENDAR_CLOSE_BTN", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    private static final Locator RIGHT_ARROW_LOCATOR = new Locator("RIGHT_ARROW_LOCATOR", By.xpath("//button[contains(@class, 'TabList_overflow_nav_right')]"));
    private static final Locator GLOBAL_SEARCH_MODAL = new Locator ("GLOBAL_SEARCH_MODAL", By.xpath("//div[contains(@class, 'GlobalSearchModal')]"));
    private static final Locator GLOBAL_SEARCH_BAR = new Locator ("GLOBAL_SEARCH_BAR", By.xpath("(//input[contains(@aria-label, 'Search Text Input')])[1]"));
    private static final Locator FLIGHT_PAGE_BUTTON = new Locator("FLIGHT_PAGE_BUTTON", new By.ByXPath("(//a[contains(@href, '/flights')])[1]"));

    private static final Locator FLIGHT_SEARCH_PAGE = new Locator("FLIGHT_SEARCH_PAGE", new By.ByXPath("//div[contains(@class,'flight_hero_title_wrapper')]"));
    private static final Locator HOTELICONTITLE = new Locator("HOMEICONTITLE", By.xpath("//p[normalize-space()='Hotel' or normalize-space()='Hotels']"));
    private static final Locator TRAINICONTITLE = new Locator("TRAINICONTITLE", By.xpath("//p[normalize-space()='Train' or normalize-space()='Kereta Api']"));
    private static final Locator CARICONTITLE = new Locator("CARICONTITLE", By.xpath("//p[normalize-space()='Car Rentals' or normalize-space()='Sewa Mobil']"));
    private static final Locator EVENTICONTITLE = new Locator("EVENTICONTITLE", By.xpath("//p[normalize-space()='Events' or normalize-space()='Event']"));
    private static final Locator AIRPORTTRANSFERICONTITLE = new Locator("AIRPORTTRANSFERICONTITLE", By.xpath("//p[normalize-space()='Airport Pickups' or normalize-space()='Jemputan Bandara']"));
    private static final Locator CURRENCY_IDR_HEADER = new Locator ("CURRENCY_IDR_HEADER", By.xpath("//div[contains(@class, 'LanguageAndCurrencyModal-module') and //span[contains(text(), 'IDR')]]"));
    private static final Locator FLASH_SALE_CURRENCY = new Locator("FLASH_SALE_CURRENCY", By.xpath("(//div[contains(@class, 'FlashSale_flashsale_header')]/following::div[contains(@class, 'ProductCard_content_container') and .//div[contains(text(), 'IDR')]])[1]"));
    private static final Locator HDP_CURRENCY = new Locator("HDP_CURRENCY", By.xpath("(//div[contains(@class, 'HighlightDiscoverProduct')]/following::div[contains(@class, 'AccomProductCard_content_container') and .//div[contains(text(), 'IDR')]])[1]"));
    private static final Locator GS_INFO_TITLE = new Locator ("GS_INFO_TITLE", By.xpath("//p[contains(text(), 'Automation GS Info')]"));
    private static final Locator GS_INFO_ICON = new Locator ("GS_INFO_ICON", By.xpath("(//div[contains(@class,'InfoBanner-module__container')]//img[contains(@alt, 'Automation GS Info')])[1]"));
    private static final Locator INFO_BANNER_TEXT = new Locator("INFO_BANNER_TEXT", new By.ByXPath("(//div[contains(@class, 'InfoBanner')]//*[contains(@class, 'variant_activity')])[1]"));
    private static final Locator CLOSE_ICON = new Locator("CLOSE_ICON", new By.ByXPath("//button[contains(@class, 'InfoBanner') and contains(@class, 'close')]"));
    private static final Locator POPULAR_EVENTS_TITLE = new Locator("POPULAR_EVENTS_TITLE", new By.ByXPath("//div[contains(@class,'GSPopular-module__heading')]//h3[contains(@class, 'GSPopular') and contains(text(),'GS POP')]"));
    private static final Locator POPULAR_EVENT_ANY_IMAGE = new Locator("POPULAR_EVENT_ANY_IMAGE", new By.ByXPath("(//section[contains(@class,'GSPopular-module__container')])[1]"));
    private static final Locator POPULAR_EVENT_IMAGE = new Locator("POPULAR_EVENT_IMAGE", new By.ByXPath("(//section[contains(@class,'GSPopular-module__container')])[1]"));
    private static final Locator INSPIRED_TITLE = new Locator("INSPIRED_TITLE", new By.ByXPath("//p[contains(@class, 'GSInspirationalSearch-module__title')]"));
    private static final Locator INSPIRED_IMAGE = new Locator("INSPIRED_IMAGE", new By.ByXPath("//div[contains(@class, 'GSInspirationalSearch-module__icon')]"));
    private static final Locator SAVED_PAYMENT_METHOD_DIV = new Locator("SAVED_PAYMENT_METHOD_DIV", new By.ByXPath("//a[text() = 'Metode Pembayaran' or text()='Payment Method']"));
    private static final Locator HOTEL_TITLE = new Locator("HOTEL_TITLE", By.xpath("//p[contains(@class, 'rr___GSStaticVertical-module__content_title') and (text()='Hotel')]"));
    private static final Locator HOTEL_IMAGE = new Locator("HOTEL_IMAGE", By.xpath("//img[@alt='[Automation GS Info] Save your top picks on Wishlist' or @alt='[Automation GS Info] Simpan pilihan teratas kamu di Wishlist']"));
    private static final Locator TO_DO_ICON = new Locator("TO_DO_ICON", new By.ByXPath("//a[contains(@href, '/to-do') and contains(@class, 'HeaderBottom-module__option')]"));
    private static final Locator TRAIN_SEARCH_PAGE = new Locator("TRAIN_SEARCH_PAGE", new By.ByXPath("//div[contains(@class, 'kereta-api_content_wrapper')]/h1[contains(@class, 'kereta-api_title')]"));
    private static final Locator EVENT_SEARCH_PAGE = new Locator("EVENT_SEARCH_PAGE", new By.ByXPath("//span[contains(@class,'HomeMenuSelection_menu_item_text') and text()='Event']"));
    private static final Locator AIRPORT_TRANSFER_SEARCH_PAGE = new Locator("AIRPORT_TRANSFER_SEARCH_PAGE", new By.ByXPath("//h1[contains(@class, 'HeroImage_title')]"));

    private static final Locator ARTICLE_TITLE = new Locator("ARTICLE_TITLE", By.xpath( "//div[contains(@class, 'rr___GSArticles-module__content_title') and (contains(normalize-space(.), 'Make Hotel Reservation Seamlessly with Pay at Hotel') or contains(normalize-space(.), 'Reservasi Hotel Makin Mudah dengan Bayar di Hotel'))]" ));
    private static final Locator ARTICLE_IMAGE = new Locator("ARTICLE_IMAGE", By.xpath("//img[@alt='Make Hotel Reservation Seamlessly with Pay at Hotel | tiket.com' or @alt='Reservasi Hotel Makin Mudah dengan Bayar di Hotel | tiket.com']" ));
    private static final Locator PROMO_LIST = new Locator("PROMO_LIST", new By.ByXPath("//span[contains(@class, 'VoucherComponent_title') and text()='Voucher Box']"));
    private static final Locator PAY_AT_HOTEL = new Locator("PAY_AT_HOTEL", By.xpath("//h3[@data-testid='section-title' and (normalize-space(text())='What is Pay at Hotel?' or normalize-space(text())='Apa itu Bayar di Hotel?')]"));
    private static final Locator SEE_ALL_PROMO = new Locator("SEE_ALL_PROMO", new By.ByXPath("//a[starts-with(@class, 'PromoCarouselLayout_cta_btn')]"));
    private static final Locator FS_SECTION_NON_IDR = new Locator("FS_SECTION_NON_IDR", By.xpath("(//div[contains(@class, 'FlashSale_flashsale_header')])[1]"));
    private static Locator getFlashSaleActiveTabProductImage(String moduleId) {
        return new Locator("FLASHSALE_ACTIVE_TAB_PRODUCT_IMAGE",
                By.xpath(String.format(
                        "(//section[@id='%s']//div[contains(@class, 'AccomProductCard_image_container')])[1]", moduleId)));
    }
    private static final Locator FS_PC_NON_IDR = new Locator ("FS_PC_NON_IDR", By.xpath("//section[@id = '65e191b948584f000124ba18']//div[contains(text(), 'SGD')]"));
    private static final Locator IWC_SECTION_NON_IDR = new Locator("IWC_SECTION", By.xpath("//div[contains(@class, 'Layout_title') and contains(text(),'[Automation] Hotel & Homes IWC')]"));
    private static final Locator IWC_PC_NON_IDR = new Locator ("IWC_PC_IDR", By.xpath("//section[@id='675fc3f2e823fe2cc74fb57c']//div[contains(text(),'SGD')]"));
    private static final Locator HDP_SECTION = new Locator("HDP_SECTION", By.xpath("//section[@id='69b4229c2c5d3b1277650347']"));
    private static final Locator HDP_PC_NON_IDR = new Locator ("HDP_PC_IDR", By.xpath("(//section[@id='69b4229c2c5d3b1277650347']//div[contains(text(),'SGD')])[1]"));
    private static final Locator PROMO_LIST_HEADER_ENTRYPOINT = new Locator("PROMO_LIST_HEADER_ENTRYPOINT", new By.ByXPath("//div[contains(@class, 'promo_icon')]"));
    private static final Locator SAVE_CURRENCY = new Locator ("SAVE_CURRENCY", By.xpath("//button//span[text()='Save']"));

    private static Locator getSearchKeywordsIcon(String keywords) {
        return new Locator("GS_VERTICAL_ICON", By.xpath(String.format("//img[contains(@alt, '%s')]", keywords)));
    }

    private static Locator getSearchKeywordsText(String keywords) {
        return new Locator("GS_VERTICAL_TEXT", By.xpath(String.format("//p[contains(@class, 'GSStaticVertical-module__content_title') and contains(text(), '%s')]", keywords)));
    }

    private Locator getSectionTitle(String sectionTitle) {
        if (sectionTitle.contains("/")) {
            String sectionEn = sectionTitle.split("/")[0];
            String sectionId = sectionTitle.split("/")[1];
            return new Locator("SECTION_TITLE", By.xpath("//*[contains(@class,'Layout_header')]//*[contains(@class,'Layout_title') and (contains(text(),'" + sectionEn + "') or contains(text(),'" + sectionId + "'))]"));
        } else {
            return new Locator("SECTION_TITLE", By.xpath("//*[contains(@class,'Layout_header')]//*[contains(@class,'Layout_title') and contains(text(),'" + sectionTitle + "')]"));
        }
    }

    private Locator getCampaignLabelProduct(String productName) {
        if (productName.contains("/")) {
            String productEn = productName.split("/")[0];
            String productId = productName.split("/")[1];
            return new Locator("CAMPAIGN_LABEL_PRODUCT", By.xpath("//*[contains(@class,'product-name_product_name') and (contains(text(),'" + productEn + "') or contains(text(),'" + productId + "'))]//parent::*//preceding-sibling::*[contains(@class,'ProductCard_ribbon')]//span"));
        } else {
            return new Locator("CAMPAIGN_LABEL_PRODUCT", By.xpath("//*[contains(@class,'product-name_product_name') and contains(text(),'" + productName + "')]//parent::*//preceding-sibling::*[contains(@class,'ProductCard_ribbon')]//span"));
        }
    }

    private static Locator gsTopSearchResult(String keywords){
        return new Locator("GS_TOP_RESULT", By.xpath(String.format("(//div[@id='global-search-modal']//div[contains(@class,'GSResultSRP-module') and contains(., '%s')])[1]", keywords)));
    }

    private static Locator gsInfoSearchResult(String keywords){
        return new Locator("GS_INFO_RESULT", By.xpath(String.format("(//div[@id='global-search-modal']//div[contains(@class,'GSArticles-module') and contains(., '%s')])[1]", keywords)));
    }

    private static Locator getProductImage(String moduleId){
        return new Locator("HDP_PRODUCT_IMAGE", By.xpath(String.format("//section[@id='%s']//img[contains(@class,'ProductCard_default_image')]", moduleId)));
    }
    private static Locator getHDPInventoryName(String moduleId){
        return new Locator("HDP_INVENTORY_NAME", By.xpath(String.format("//*[@id='%s']//h2[contains(@class, 'name')]", moduleId)));
    }
    private static Locator getHDPInventoryLocation(String moduleId){
        return new Locator("HDP_INVENTORY_LOCATION", By.xpath(String.format("//*[@id='%s']//p[contains(@class, 'location')]", moduleId)));
    }
    private static Locator getHDPInventoryPrice(String moduleId){
        return new Locator("HDP_INVENTORY_PRICE", By.xpath(String.format("//div[contains(@class, 'price') and contains(text(), '%s')]", moduleId)));
    }
    private static Locator getHDPInventoryStrikePrice(String moduleId){
        return new Locator("HDP_INVENTORY_STRIKE_PRICE", By.xpath(String.format("//*[@id='%s']//div[contains(@class, 'strike')]", moduleId)));
    }
    //map to store session id
    Map<String, String> sessionMap = new ConcurrentHashMap<>();

    @Override
    public void continueWithoutApp(int timeoutInSeconds) {
        logger.log("Not implemented: mWeb only");
    }

    public void goToSection(String sectionTitle) {
        driverTools.waitForElementsToAppear(getSectionTitle(sectionTitle));
        Locator section = getSectionTitle(sectionTitle);
        driverTools.scrollIntoWebView(section);
    }

    public String getCampaignProduct(String productName) {
        Locator campaignLabel = getCampaignLabelProduct(productName);
        driverTools.scrollIntoWebView(campaignLabel);
        return driverTools.getText(campaignLabel).orElseThrow();
    }

    public boolean isHomePageOpened() {
        boolean isLogoDisplayed = driverTools.isElementDisplayed(HOME_PAGE_LOGO);
        Environment env = Configs.ENVIRONMENT;

        boolean isHeadingDisplayed = driverTools.isElementDisplayed(
                env == Environment.GK || env == Environment.PREPROD ? HOME_PAGE_HEADING_NEW : HOME_PAGE_HEADING_OLD
        );
        return isLogoDisplayed && isHeadingDisplayed;
    }

    public void goToHomePage() {
        driverTools.maximizeWindow();
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get(STAGING_DWEB_URL);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.getWithDynamicTimeout(PREPROD_DWEB_URL, 60);
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driverTools.get(PROD_DWEB_URL, 3);
        }
        PopupObserver.inject(driver, List.of(CLOSE_POP_UP, CLOSE_POP_UP_IMAGE));
    }

    @Override
    public void clickSearchColumn() {
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR, 2);
        driverTools.waitForElementToBeClickable(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        boolean gsModalAppear = driverTools.waitForElementToAppear(GLOBAL_SEARCH_MODAL, 8).isPresent();

        if (!gsModalAppear) {
            logger.log("Search modal did not open on first click, retrying.");
            driverTools.click(GLOBAL_SEARCH_BAR);
            gsModalAppear = driverTools.waitForElementToAppear(GLOBAL_SEARCH_MODAL, 8).isPresent();
        }

        Assertion.assertThat("Verify Search Modal Appear", gsModalAppear, is(true));
        driverTools.waitForElementToBeClickable(GLOBAL_SEARCH_BAR, 5);
    }

    public void goToAccountPage() {
        driverTools.maximizeWindow();
        String baseUrl = switch (Configs.ENVIRONMENT) {
            case GK -> STAGING_DWEB_URL;
            case PREPROD -> PREPROD_DWEB_URL;
            default -> "";
        };

        if (!baseUrl.isEmpty()) {
            driverTools.get(baseUrl + "myaccount");
        }
        PopupObserver.inject(driver, List.of(ACCOUNT_POP_UP));
    }

    public void goToTiketb2cPage() {
        driverTools.clickWithJavascriptExecutor(GO_TO_TIKETB2C_PAGE);
        switchToTiketConsumerWindow();
    }

    @Override
    public void goToTiketb2cPageIfPresent() {
        if (driverTools.waitForElementToAppear(GO_TO_TIKETB2C_PAGE, 5).isPresent()) {
            logger.log("Go To tiket.com page button is present, clicking it");
            goToTiketb2cPage();
        } else {
            logger.log("Go To tiket.com page button is not present, skipping");
        }
    }

    private void switchToTiketConsumerWindow() {
        String currentWindow = driver.getWindowHandle();
        for (String window : driver.getWindowHandles()) {
            driver.switchTo().window(window);

            if (!driver.getTitle().toLowerCase().contains("corporate")) {
                return;
            }
        }
        driver.switchTo().window(currentWindow);
        throw new IllegalStateException("Could not find the target window without 'corporate' in the title.");
    }

    @Override
    public void clickLanguageSelector() {
        driverTools.waitForElementToAppear(LANGUAGE_SELECTOR_BUTTON);
        driverTools.click(LANGUAGE_SELECTOR_BUTTON);
    }

    public void closeLanguageCurrencyDialog() {
        driverTools.click(CLOSE_LANGUAGE_CURRENCY_DIALOG);
    }

    public void selectEnglishLanguage() {
        clickLanguageSelector();
        driverTools.waitForElementToAppear(LANGUAGE_SECTION).orElseThrow();
        driverTools.waitForElementToPresence(ENGLISH_LANGUAGE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(ENGLISH_LANGUAGE);
        driverTools.waitForElementToBeClickable(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.click(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    public void selectBahasaLanguage() {
        clickLanguageSelector();
        driverTools.waitForElementToAppear(LANGUAGE_SECTION).orElseThrow();
        driverTools.waitForElementToPresence(BAHASA_LANGUAGE).orElseThrow();
        driverTools.clickWithJavascriptExecutor(BAHASA_LANGUAGE);
        driverTools.waitForElementToBeClickable(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.click(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.waitForElementToDisappear(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    @Override
    public void clickOnHomeIcon(){}

    @Override
    public String getPageLanguageCode() {
        clickLanguageSelector();
        String lang = driverTools.waitForElementToAppear(LANGUAGE_IDENTIFIER).map(driverTools::getText).orElseThrow();
        String langCode = "";
        if (lang.equals("Language")) {
            langCode = "en";
        } else if (lang.equals("Bahasa")) {
            langCode = "id";
        } else {
            Assertion.skip("Page language not recognized");
        }
        closeLanguageCurrencyDialog();
        return langCode;
    }

    public void openInbox() {
        WebElement inboxIcon = driverTools.waitForElementToAppear(INBOX_ICON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(inboxIcon);
    }

    @Override
    public String getImportantInfoMessage() {
        clickLanguageSelector();
        WebElement importantInfo = driverTools.waitForElementToAppear(IMPORTANT_INFO_MESSAGE).orElseThrow();
        closeLanguageCurrencyDialog();
        return importantInfo.getText();
    }

    public int numberOfAvailableCurrency() {
        driverTools.waitForElementToAppear(AVAILABLE_CURRENCY);
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_AVAILABLE);
        List<WebElement> availableCurrencies = driverTools.elements(AVAILABLE_CURRENCY)
                .orElseThrow();
        return availableCurrencies.size();
    }

    public boolean isCurrencyAvailable(String currencyName) {
        driverTools.waitForElementToAppear(AVAILABLE_CURRENCY);
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_AVAILABLE);
        List<WebElement> availableCurrencies = driverTools.elements(AVAILABLE_CURRENCY)
                .orElseThrow();
        for (WebElement currency : availableCurrencies) {
            String name = driverTools.getText(currency);
            if (name.contains(currencyName)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getSuggestedCurrencyNamesOnConsumerSide() {
        driverTools.waitForElementToAppear(SUGGESTED_CURRENCY_AVAILABLE);
        List<WebElement> suggestedCurrencies = driverTools.elements(SUGGESTED_CURRENCY_AVAILABLE)
                .orElseThrow();
        List<String> currencyNames = new ArrayList<>();
        for (WebElement currency : suggestedCurrencies) {
            String name = driverTools.getText(currency);
            currencyNames.add(name);
        }
        Collections.sort(currencyNames);
        return currencyNames;
    }

    public void clickOnLoginButton() {
        driverTools.waitForElementToAppear(LOGIN_BUTTON, 5).orElseThrow();
        driverTools.click(LOGIN_BUTTON);
    }

    public void waitForHomePageToOpen() {
        driverTools.waitForElementToAppear(HOME_PAGE_LOGO, 10);
    }

    public void goToMyOrderPage() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement yourOrder = driverTools.waitForElementToAppear(YOUR_ORDERS_DIV, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(yourOrder);
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

    public void goToFlightPage() {
        WebElement flightIcon = driverTools.waitForElementToAppear(FLIGHT_PAGE_BUTTON, 2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(flightIcon);
        driverTools.click(FLIGHT_PAGE_BUTTON);
    }

    public void goToSavePassengerData() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement savePassenger = driverTools.waitForElementToAppear(SAVE_PASSENGER_DIV, 2).orElseThrow();
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

    public void openSettings() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToPresence(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement savePassenger = driverTools.waitForElementToPresence(SETTINGS_DIV, 2).orElseThrow();
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

    public void selectEnglishLanguageOnly() {
        WebElement greetingHeading = driverTools.waitForElementToAppear(GREETING_HEADING).orElseThrow();
        String greetingHeadingText = driverTools.getText(greetingHeading);
        if (!greetingHeadingText.contains("Hey")) {
            selectEnglishLanguage();
        }
    }

    @SneakyThrows
    public void selectCurrency(String currency) {
        String sessionId = this.session.sessionId();
        if(Configs.ENVIRONMENT == Environment.PROD && sessionMap.get(sessionId)==null){
            driverTools.dummyWait(5);
            driverTools.get(PROD_DWEB_URL, 3);
            driverTools.waitForWebToLoad();
            sessionMap.put(sessionId, sessionId);
        }

        try {
            selectCurrencyStep(currency);
        } catch (StaleElementReferenceException e) {
            driver.navigate().refresh();
            driverTools.waitForWebToLoad();
            selectCurrencyStep(currency);
        }
    }

    public void selectCurrencyStep(String currency) {
        String currentCurrency = getCurrentCurrency();
        if (!currentCurrency.equals(currency)) {
            int maxRetries = 3;
            for (int attempt = 1; attempt <= maxRetries; attempt++) {
                driverTools.clickWithJavascriptExecutor(LANGUAGE_CURRENCY_DROPDOWN);
                Locator CURRENCY_TEXT = getCurrencyTextLocator(currency);
                driverTools.waitForElementToAppear(CURRENCY_TEXT);
                driverTools.scrollToElement(CURRENCY_TEXT);
                driverTools.clickWithJavascriptExecutor(CURRENCY_TEXT);

                driverTools.waitForElementToAppear(COUNTRY_CURRENCY_LANG_SAVE_BUTTON, 10);
                driverTools.scrollIntoWebView(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);
                driverTools.clickWithJavascriptExecutor(COUNTRY_CURRENCY_LANG_SAVE_BUTTON);

                driverTools.waitForElementToDisappear(COUNTRY_CURRENCY_LANG_SAVE_BUTTON, 15);
                driverTools.waitForWebToLoad();
                if (driverTools.waitForTextToBePresent(CURRENCY_HEADER_TEXT, currency, 15)) {
                    break;
                }
                logger.log("Currency change attempt " + attempt + " did not reflect in header, retrying...");
                if (attempt == maxRetries) {
                    Assert.fail(
                            "After " + maxRetries + " attempts, header did not show \"" + currency + "\" (confirm selection applied).");
                }
                driver.navigate().refresh();
                driverTools.waitForWebToLoad();
            }
        }
        driver.navigate().refresh();
        waitForHeaderCurrencyAfterRefresh(currency);
    }

    /** After {@code driver.navigate().refresh()}, wait until header shows the selected currency. */
    private void waitForHeaderCurrencyAfterRefresh(String selectedCurrency) {
        driverTools.waitForWebToLoad();
        if (!driverTools.waitForTextToBePresent(CURRENCY_HEADER_TEXT, selectedCurrency, 30)) {
            Assert.fail(
                    "After refresh, header did not show \"" + selectedCurrency + "\" within 30s.");
        }
    }


    public String getCurrentCurrency() {
        driverTools.waitForElementToPresence(CURRENCY_HEADER_TEXT).orElseThrow();
        String currencyText = driverTools.getText(CURRENCY_HEADER_TEXT).orElseThrow();
        if (currencyText.contains("|")) {
            currencyText = currencyText.substring(currencyText.lastIndexOf("|") + 1).trim();
        }
        logger.log("Current currency: " + currencyText);
        return currencyText;
    }

    private Locator getCurrencyTextLocator(String currency) {
        return new Locator("CURRENCY_TEXT", new By.ByXPath("//*[contains(@class, 'CurrencyContent-module') and normalize-space(text())='" + currency + "']"));
    }

    public void openVerticalPage(String vertical) {
        final String verticalName = convertVerticalNameIfRequired(vertical);
        this.clickHomeProduct(verticalName);
    }

    private String convertVerticalNameIfRequired(String verticalName) {
        if (!isSessionInEnglish()) {
            verticalName = this.translateVerticalNameToBhs(verticalName);
        }
        return verticalName;
    }

    private boolean isSessionInEnglish() {
        return driverTools.waitForTextToBePresent(CURRENCY_HEADER_TEXT, "EN");
    }

    protected String translateVerticalNameToBhs(String vertical) {
        String translatedVerticalName = CoreFunctions.verticalEngToId(vertical);
        return translatedVerticalName == null ? vertical : translatedVerticalName;
    }

    private void clickHomeProduct(String vertical) {
        Locator HOME_PRODUCT_ICON = getVerticalLocator(vertical);
        driverTools.waitForElementToAppear(HOME_PRODUCT_ICON);
        driverTools.scrollIntoWebView(HOME_PRODUCT_ICON);
        driverTools.clickWithJavascriptExecutor(HOME_PRODUCT_ICON);
    }

    private String removeTrailingS(String vertical) {
        if (vertical.endsWith("s") && vertical.length() > 1) {
            vertical = vertical.substring(0, vertical.length() - 1);
        }

        return vertical;
    }

    private Locator getVerticalLocator(String vertical) {
        return new Locator("HOME_PRODUCT_ICON", new By.ByXPath("//a[@data-testid='verticalTab-" + vertical.toLowerCase() + "' or contains(@data-testid,'verticalTab-" + removeTrailingS(vertical).toLowerCase() + "')]"));
    }

    public boolean verifyVerticalIsDisplayed(String vertical) {
        final String verticalName = convertVerticalNameIfRequired(vertical);
        Locator HOME_PRODUCT_ICON = getVerticalLocator(verticalName);
        return driverTools.isElementDisplayed(HOME_PRODUCT_ICON);
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
        PopupObserver.inject(driver, List.of(CLOSE_POP_UP, CLOSE_POP_UP_IMAGE));
    }

    public String getLanguage() {
        String language;
        if (isSessionInEnglish()) {
            language = "en";
        } else {
            language = "id";
        }
        logger.log("language is: " + language);
        return language;
    }

    public void goToWishlistPage() {
        WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(loginProfile);
        driverTools.click(WISHLIST_DIV);
    }

    public boolean isOnInsurancePage() {
        String url = driverTools.getCurrentURL();
        return url.contains("insurance-claim");
    }

    private String buildHelpCenterChatOpenUrl() {
        String base = switch (Configs.ENVIRONMENT) {
            case GK ->
                STAGING_DWEB_URL;
            case PREPROD ->
                PREPROD_DWEB_URL;
            case PROD ->
                PROD_DWEB_URL;
        };
        if (!base.endsWith("/")) {
            base = base + "/";
        }
        return base + "en-sg/help-center?chat=open&source=help_center";
    }

    private void openHelpCenterViaDirectChatUrlIfShellFailedToLoad() {
        if (driverTools.waitForElementToAppear(HELP_CENTER_UNABLE_LOAD_HEADING, 10).isEmpty()) {
            return;
        }
        String fallback = buildHelpCenterChatOpenUrl();
        logger.log("Help Center shell failed to load; navigating to chat deep-link: " + fallback);
        driverTools.get(fallback);
        SettingsPageWeb.getInstance(session).skipCoachmarkBySkipOrLewatiText();
    }

    public void openHelpCenter() {
        try {
            driverTools.scrollIntoWebView(HELP_CENTER);
            driverTools.click(HELP_CENTER);
        } catch (ElementClickInterceptedException e){
            logger.log("Click intercepted, retrying");
            driverTools.clickWithJavascriptExecutor(HELP_CENTER);
        }

        SettingsPageWeb.getInstance(session).skipCoachmarkBySkipOrLewatiText();
        if (driverTools.waitForElementToAppear(HELP_CENTER_RETRY).isPresent()) {
            try {
                driverTools.click(HELP_CENTER_RETRY);
            } catch (ElementClickInterceptedException e) {
                logger.log("Help center retry click intercepted, dismissing coachmark and retrying with JS");
                SettingsPageWeb.getInstance(session).skipCoachmarkBySkipOrLewatiText();
                driverTools.clickWithJavascriptExecutor(HELP_CENTER_RETRY);
            }
        }
        openHelpCenterViaDirectChatUrlIfShellFailedToLoad();
    }

    public boolean isClaimCenterPageOpened() {
        String url = driverTools.getCurrentURL();
        return url.contains("insurance-claim")
                && driverTools.waitForElementToAppear(CLAIM_PAGE_ORDER_TAB, 2).isPresent();
    }

    public boolean goToVoucherBoxPage() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement savePassenger = driverTools.waitForElementToAppear(VB_DIV, 2).orElseThrow();
                driverTools.click(savePassenger);
                driverTools.waitForElementToPresence(VB_PAGE);
                return driverTools.isElementDisplayed(VB_PAGE) ? true : false;
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

    public boolean goToReviewPage() {
        int maxRetries = 3;
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
                driverTools.clickWithJavascriptExecutor(loginProfile);
                WebElement review_ = driverTools.waitForElementToAppear(REVIEW_DIV, 2).orElseThrow();
                driverTools.click(review_);
                driverTools.waitForElementToPresence(REVIEW_PAGE);
                return driverTools.isElementDisplayed(REVIEW_PAGE) ? true : false;
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

    public boolean openPrivacyPolicy(){
        driverTools.swipeToElement(OPEN_PRIVACY_POLICY, Swipe.UP, 5);
        driverTools.clickWithJavascriptExecutor(OPEN_PRIVACY_POLICY);
        return driverTools.isElementDisplayed(PP_PAGE);
    }

    public boolean openTnC(){
        driverTools.swipeToElement(OPEN_T_N_C, Swipe.UP, 5);
        driverTools.clickWithJavascriptExecutor(OPEN_T_N_C);
        return driverTools.isElementDisplayed(T_N_C_PAGE);
    }

    public String getRandomCurrency(String currentCurrency){
        List<String> currencies = new ArrayList<>();
        driverTools.click(LANGUAGE_AND_CURRENCY_MENU);
        driverTools.waitForElementToAppear(CURRENCY_CONTAINER).orElseThrow((() -> new SkipException("Seems like currency list is down. Please contact payment team!")));
        List<WebElement> currencyElements = driverTools.elements(CURRENCY_LOCATOR).orElseThrow();
        for (WebElement currencyElement : currencyElements) {
            String currencyText = driverTools.getText(currencyElement);
            String cssClass = currencyElement.getAttribute("class");
            boolean isDisabled = cssClass != null && cssClass.contains("disabled");
            if (!currencyText.isEmpty() && !isDisabled) {
                currencies.add(currencyText.trim());
            }
        }
        currencies = currencies.stream().distinct().collect(Collectors.toList());
        currencies.removeIf(currency -> currency.equals(currentCurrency));
        String randomCurrency = "";
        if (!currencies.isEmpty()) {
            randomCurrency = currencies.get(CoreFunctions.generateRandomInt(0,currencies.size()-1));
        }
        driverTools.click(LANGUAGE_AND_CURRENCY_CLOSE_BTN);
        return randomCurrency;
    }

    public void clickRegisterButton() {
        driverTools.waitForElementToAppear(REGISTER_BUTTON);
        driverTools.waitForElementToBeClickable(REGISTER_BUTTON);
        driverTools.click(REGISTER_BUTTON);
        driverTools.click(REGISTER_NOW);
    }

    public boolean verifyContentGSModule(){
        boolean isGSInfoCloseButtonAppear = driverTools.isElementDisplayed(GS_INFO_CLOSE_BUTTON);
        boolean isGSInfoNextButtonAppear = driverTools.isElementDisplayed(GS_INFO_NEXT_BUTTON);
        driverTools.scrollToElement(GS_POP_BANNER);
        boolean isGSPopBannerAppear = driverTools.isElementDisplayed(GS_POP_BANNER);
        boolean isGSPopTitleAppear = driverTools.isElementDisplayed(GS_POP_TITLE);

        boolean isGSMSChipsAppear = driverTools.isElementDisplayed(GS_MS_CHIPS);
        boolean isGSMSSeeAllButtonAppear = driverTools.isElementDisplayed(GS_MS_SEE_ALL_BUTTON);

        boolean isGSISProductTitleAppear = driverTools.isElementDisplayed(GS_IS_PRODUCT_TITLE);
        boolean isGSISProductSubtitleAppear = driverTools.isElementDisplayed(GS_IS_PRODUCT_SUBTITLE);

        Assertion.assertThat("Verify GS Info Close Button Appear", isGSInfoCloseButtonAppear, is(true));
        Assertion.assertThat("Verify GS Info Next Button Appear", isGSInfoNextButtonAppear, is(true));

        Assertion.assertThat("Verify GS Pop Banner Appear", isGSPopBannerAppear, is(true));
        Assertion.assertThat("Verify GS Pop Title Appear", isGSPopTitleAppear, is(true));

        Assertion.assertThat("Verify GS MS Chips Appear", isGSMSChipsAppear, is(true));
        Assertion.assertThat("Verify GS MS See All Button Appear", isGSMSSeeAllButtonAppear, is(true));

        Assertion.assertThat("Verify GS IS Product Title Appear", isGSISProductTitleAppear, is(true));
        Assertion.assertThat("Verify GS IS Product Subtitle Appear", isGSISProductSubtitleAppear, is(true));

        return true;
    }

    public void clickSeeAllFlashSale (){
        String moduleId = sheetData.getOrDefault("moduleId", sheetData.get("ModuleId"));
        Assertion.assertThat("Flash Sale V2 Module is Exist & Clickable",
                moduleId != null && !moduleId.isBlank(), is(true));

        Locator seeAllFlashSaleBtn = getSeeAllFlashSaleBtn(moduleId);
        driverTools.waitForElementToPresence(seeAllFlashSaleBtn);
        driverTools.click(seeAllFlashSaleBtn);
        driverTools.waitForElementToAppear(SEE_ALL_REDIRECTION);

        String currentURL = driverTools.getCurrentURL();
        String initialUrl = driverTools.getCurrentURL();
        Assert.assertNotEquals("Verify URL Redirection.", initialUrl, currentURL);
    }

    public void clickOneOfProductIcon() {
        driverTools.waitForElementToAppear(PRODUCT_HOTEL_ICON);
        driverTools.scrollToElement(PRODUCT_HOTEL_ICON);
        driverTools.click(PRODUCT_HOTEL_ICON);
    }

    public void clickDatePicker() {
        driverTools.waitForElementToAppear(DATE_PICKER);
        driverTools.scrollToElement(DATE_PICKER);
        driverTools.click(DATE_PICKER);
    }

    public boolean isDescriptionHolidayDisplay() {
        driverTools.waitForElementToAppear(DESCRIPTION_HOLIDAY);
        boolean isImageVisible = driverTools.isElementDisplayed(DESCRIPTION_HOLIDAY);
        return isImageVisible;
    }

    public void closeCalendar() {
        driverTools.waitForElementToAppear(CALENDAR_CLOSE_BTN);
        driverTools.scrollToElement(CALENDAR_CLOSE_BTN);
        driverTools.click(CALENDAR_CLOSE_BTN);
    }

    public void closeLanguageCurrencyPopup() {
        driverTools.waitForElementToAppear(LANGUAGE_AND_CURRENCY_CLOSE_BTN);
        driverTools.scrollToElement(LANGUAGE_AND_CURRENCY_CLOSE_BTN);
        driverTools.click(LANGUAGE_AND_CURRENCY_CLOSE_BTN);
    }

    public void inputVerticalKeywords(){
        boolean gsModalAppear = driverTools.isElementDisplayed(GLOBAL_SEARCH_MODAL);
        if(gsModalAppear == true){
            String keywordsIput = sheetData.get("keywords").toLowerCase();
            logger.log("keywords input = " + keywordsIput);
            int matchingInputs = driverTools.elements(new Locator("ALL_SEARCH_TEXT_INPUTS",
                    By.xpath("//input[contains(@aria-label, 'Search Text Input')]"))).map(List::size).orElse(0);
            logger.log("Elements matching 'Search Text Input' aria-label on page: " + matchingInputs);
            boolean loggedInBeforeTyping = driverTools.isElementDisplayed(LOGIN_PROFILE_ICON);
            logger.log("Login state right before typing starts (LOGIN_PROFILE_ICON displayed): " + loggedInBeforeTyping);
            driverTools.waitForElementToBeClickable(GLOBAL_SEARCH_BAR, 5);
            waitForVerticalKeywordsToLoad(keywordsIput);
        } else{
            System.out.println("Modal not appearing");
        }
    }

    private void waitForVerticalKeywordsToLoad(String keywords) {
        int maxRetries = 5;
        boolean valueStable = false;
        for (int attempt = 0; attempt < maxRetries && !valueStable; attempt++) {
            Object hasFocus = driverTools.focusWindow();
            logger.log("Window focus diagnostic before typing: " + hasFocus);
            driverTools.clear(GLOBAL_SEARCH_BAR);
            for (char c : keywords.toCharArray()) {
                driverTools.sendKeys(GLOBAL_SEARCH_BAR, String.valueOf(c));
                try {
                    Thread.sleep(120);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ex);
                }
            }
            String firstRead = driverTools.getValue(GLOBAL_SEARCH_BAR);
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
            String secondRead = driverTools.getValue(GLOBAL_SEARCH_BAR);
            valueStable = keywords.equalsIgnoreCase(firstRead) && keywords.equalsIgnoreCase(secondRead);
            if (!valueStable) {
                String otherInputValue = driverTools.element(new Locator("SEARCH_TEXT_INPUT_2",
                        By.xpath("(//input[contains(@aria-label, 'Search Text Input')])[2]")))
                        .map(driverTools::getValue)
                        .orElse("N/A");
                boolean modalStillOpen = driverTools.isElementDisplayed(GLOBAL_SEARCH_MODAL);
                boolean stillLoggedIn = driverTools.isElementDisplayed(LOGIN_PROFILE_ICON);
                logger.softWarning("Search keywords were reset while typing, retrying (attempt " + (attempt + 1) + "): "
                        + "first read = (" + firstRead + "), second read = (" + secondRead + "), "
                        + "other matching input's value = (" + otherInputValue + "), "
                        + "modal still open = " + modalStillOpen + ", still logged in = " + stillLoggedIn);
                if (attempt < maxRetries - 1) {
                    driverTools.refreshWebpage();
                    driverTools.waitForWebToLoad();
                    clickSearchColumn();
                    driverTools.waitForElementToAppear(POPULAR_EVENTS_TITLE, 5);
                }
            }
        }
        Assertion.assertThat("Search keywords stayed in the box after typing", valueStable, is(true));
        driverTools.waitForElementToPresence(getSearchKeywordsIcon(keywords), 10);
    }

    public void verifyVerticalIconText(String keywords){
        driverTools.waitForElementToPresence(getSearchKeywordsIcon(keywords));
        logger.log("Vertical icon : " + getSearchKeywordsIcon(keywords));
        driverTools.waitForElementToPresence(getSearchKeywordsText(keywords));
        logger.log("Vertical text : " + getSearchKeywordsText(keywords));
        boolean isGSVerticalIconVisible = driverTools.isElementDisplayed(getSearchKeywordsIcon(keywords));
        boolean isGSVerticalTextVisible = driverTools.isElementDisplayed(getSearchKeywordsText(keywords));
        Assertion.assertThat("Verify Vertical Icon Exist", isGSVerticalIconVisible, is(true));
        Assertion.assertThat("Verify Vertical Text Exist", isGSVerticalTextVisible, is(true));
    }

    public void verticalRedirectionFromGS(String keywords) {
        driverTools.click(getSearchKeywordsText(keywords));
        String url = driver.getCurrentUrl();
        Assertion.assertThat("Verify url contains " + keywords + " url", url, containsStringIgnoringCase(keywords));
    }

    @Override
    public boolean verifyHomeIconImage() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(getImgVerticalLocator("Events", "Event"));
        boolean isEventsIconAppeared = checkVerticalIcon(getImgVerticalLocator("Events", "Event"));
        boolean isHotelIconAppeared = checkVerticalIcon(getImgVerticalLocator("Hotel", "Hotels"));
        boolean isTrainIconAppeared = checkVerticalIcon(getImgVerticalLocator("Train", "Kereta Api"));
        boolean isCarIconAppeared = checkVerticalIcon(getImgVerticalLocator("Car Rentals", "Sewa Mobil"));
        return isEventsIconAppeared && isHotelIconAppeared && isTrainIconAppeared && isCarIconAppeared;
    }

    private boolean checkVerticalIcon(Locator verticalLocator) {
        if (driverTools.isElementDisplayed(verticalLocator)) {
            return true;
        }
        if (driverTools.isElementDisplayed(RIGHT_ARROW_LOCATOR)) {
            driverTools.clickWithJavascriptExecutor(RIGHT_ARROW_LOCATOR);
            driverTools.waitForElementToAppear(verticalLocator, 2);
            return driverTools.isElementDisplayed(verticalLocator);
        }
        return false;
    }

    @Override
    public Locator getImgVerticalLocator(String verticalEn, String verticalId) {
        if ("events".equalsIgnoreCase(verticalEn) || "events".equalsIgnoreCase(verticalId)) {
            return new Locator(
                    "VERTICAL_IMG",
                    By.xpath("//a[@data-testid='verticalTab-event']//img")
            );
        }
        return new Locator(
                "VERTICAL_IMG",
                By.xpath("//*[contains(@class, 'Text_size_b3') and (text() = '" + verticalEn + "' or text() = '" + verticalId + "')]/preceding-sibling::img")
        );
    }

    public void goToSavedPaymentMethod() {
        WebElement loginProfile = driverTools.waitForElementToAppear(LOGIN_PROFILE_ICON, 2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(loginProfile);
        driverTools.click(SAVED_PAYMENT_METHOD_DIV);
    }

    public void verifyCurrencyHeaderIDR(){
        boolean isCurrencyIDR = driverTools.isElementDisplayed(CURRENCY_IDR_HEADER);
        Assertion.assertThat("Verify Currency Header IDR", isCurrencyIDR, is(true));
    }

    public void verifyIWCCurrencyIDR(String moduleId){
        Locator iwcCurrency = new Locator("IWC_CURRENCY", By.xpath("//section[@id='" + moduleId + "']//div[contains(@class, 'PriceModule_final_price')]//div[contains(text(),'IDR')]"));
        driverTools.waitForElementToAppear(iwcCurrency);
        boolean isIWCCurrencyIDR = driverTools.isElementDisplayed(iwcCurrency);
        Assertion.assertThat("Verify IWC Currency IDR", isIWCCurrencyIDR, is(true));
    }

    public void verifyHDPCurrencyIDR(){
        driverTools.scrollToElement(HDP_CURRENCY);
        boolean isHDPCurrencyIDR = driverTools.isElementDisplayed(HDP_CURRENCY);
        Assertion.assertThat("Verify HDP Currency IDR", isHDPCurrencyIDR, is(true));
    }

    public void verifyFlashSaleCurrencyIDR(){
        boolean isFlashSaleCurrencyIDR = driverTools.isElementDisplayed(FLASH_SALE_CURRENCY);
        Assertion.assertThat("Verify Flash Sale Currency IDR", isFlashSaleCurrencyIDR, is(true));
    }

    public boolean verifyContentGS_Info(){
        driverTools.waitForElementToAppear(GS_INFO_TITLE, 2);
        boolean isGSInfoTitleAppear = driverTools.isElementDisplayedWithStaleRetry(GS_INFO_TITLE);
        boolean isGSInfoIconAppear = driverTools.isElementDisplayedWithStaleRetry(GS_INFO_ICON);
        boolean isTextLinkAppear = driverTools.isElementDisplayedWithStaleRetry(INFO_BANNER_TEXT);
        driverTools.click(INFO_BANNER_TEXT);
        boolean isTextLinkClickable = checkURLDoestMatch();

        return (isGSInfoTitleAppear && isGSInfoIconAppear && isTextLinkAppear && isTextLinkClickable);
    }

    public boolean verifyContentPopular(){
        goToHomePage();
        driverTools.waitForWebToLoad();
        clickSearchColumn();

        driverTools.waitForElementToAppear(POPULAR_EVENTS_TITLE);
        String titleContainsPopular = String.valueOf(driverTools.getText(POPULAR_EVENTS_TITLE));
        Optional<WebElement> popularImage = driverTools.waitForElementToAppear(POPULAR_EVENT_IMAGE, 5)
                .or(() -> driverTools.waitForElementToAppear(POPULAR_EVENT_ANY_IMAGE, 5))
                .or(() -> driverTools.element(POPULAR_EVENT_ANY_IMAGE));
        boolean isPopularIcon = popularImage.isPresent();
        String urlBeforeClick = driverTools.getCurrentURL();
        popularImage.ifPresent(image -> {
            driverTools.scrollIntoWebView(image);
            try {
                driverTools.click(image);
            } catch (Exception e) {
                logger.log("Popular event image not clickable normally, falling back to JS click: " + e.getMessage());
                driverTools.clickWithJavascriptExecutor(image);
            }
        });
        driverTools.waitUntil(d -> !driverTools.getCurrentURL().equals(urlBeforeClick), 10);
        boolean isPopularIconClickable = !driverTools.getCurrentURL().equals(urlBeforeClick) && checkURLContainsEnvironment();

        return (titleContainsPopular.contains("GS POP") && isPopularIcon && isPopularIconClickable);
    }

    public boolean verifyContentInspired(){
        goToHomePage();
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_MODAL, 2);
        driverTools.waitForElementToAppear(INSPIRED_TITLE);
        String titleContainsPopular = String.valueOf(driverTools.getText(INSPIRED_TITLE));
        boolean isPopularIcon = driverTools.isElementDisplayed(INSPIRED_IMAGE);
        driverTools.click(INSPIRED_IMAGE);
        boolean isInspireIconClickable = checkURLContainsEnvironment();

        return (titleContainsPopular.contains("Inspired") && isPopularIcon && isInspireIconClickable);
    }

    @Override
    public void enterVerticalName(String value){
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        driverTools.waitForElementToPresence(GLOBAL_SEARCH_BAR,2 );
        driverTools.sendKeys(GLOBAL_SEARCH_BAR,value);
    }

    @Override
    public boolean verifyVerticalSection(){
        enterVerticalName("HOTEL");
        driverTools.waitForElementToAppear(HOTEL_TITLE);
        String titleContainsPopular = String.valueOf(driverTools.getText(HOTEL_TITLE));
        boolean isPopularIcon = driverTools.isElementDisplayed(HOTEL_IMAGE);
        driverTools.click(HOTEL_IMAGE);
        boolean isPopularIconClickable = checkURLDoestMatch();
        return (titleContainsPopular.contains("Hotel") && isPopularIcon && isPopularIconClickable);
    };

    @Override
    public boolean verifyArticleSection(){
        goToHomePage();
        enterVerticalName("HOTEL");
        driverTools.waitForElementToAppear(ARTICLE_TITLE);
        boolean titleContainsPopular = driverTools.isElementDisplayed(ARTICLE_TITLE);
        boolean isPopularIcon = driverTools.isElementDisplayed(ARTICLE_IMAGE);
        driverTools.click(ARTICLE_IMAGE);
        boolean isPopularIconClickable = driverTools.isElementDisplayed(PAY_AT_HOTEL);
        return (titleContainsPopular && isPopularIcon && isPopularIconClickable);
    };

    @Override
    public void flashSaleCheck() {
        String moduleId = sheetData.getOrDefault("moduleId", sheetData.get("ModuleId"));
        Assertion.assertThat("Flash Sale moduleId must be provided in sheet data (moduleId/ModuleId)",
                moduleId != null && !moduleId.isBlank(), is(true));
        Locator flashSaleProductImage = getFlashSaleActiveTabProductImage(moduleId);

        driverTools.waitForElementToAppear(flashSaleProductImage);
        driverTools.scrollIntoWebView(flashSaleProductImage);
        driverTools.clickWithJavascriptExecutor(flashSaleProductImage);
    }
  
    @Override
    public void clickSeeAllPromo(){
        driverTools.scrollToElement(SEE_ALL_PROMO);
        driverTools.waitForElementToBeClickable(SEE_ALL_PROMO);
        driverTools.click(SEE_ALL_PROMO);
    }

    @Override
    public void verifyGS_Top_Result(String keywords){
        driverTools.waitForElementsToAppear(gsTopSearchResult(keywords), 10);
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

    public boolean verifyVerticalIcons(String vertical) {
        boolean isVerticalPageOpen = false;

        switch (vertical.toLowerCase()) {
            case "flight":
                driverTools.click(FLIGHT_PAGE_BUTTON);
                isVerticalPageOpen = driverTools.isElementDisplayed(FLIGHT_SEARCH_PAGE);
                goToHomePage();
                logger.log("is flight vertical open -> " + isVerticalPageOpen);
                break;

            case "hotel":
                clickVerticalTitle("Hotel", "Hotels");
                isVerticalPageOpen = driverTools.isElementDisplayed(HOTEL_SEARCH_PAGE);
                goToHomePage();
                logger.log("is hotel vertical open -> " + isVerticalPageOpen);
                break;

            case "to do":
                driverTools.click(TO_DO_ICON);
                driverTools.waitForElementToPresence(TODO_SEARCH_PAGE);
                isVerticalPageOpen = driverTools.isElementDisplayed(TODO_SEARCH_PAGE);
                goToHomePage();
                logger.log("is to do vertical open -> " + isVerticalPageOpen);
                break;

            case "car":
                clickVerticalTitle("Car Rentals", "Sewa Mobil");
                isVerticalPageOpen = driverTools.isElementDisplayed(CAR_RENTAL_SEARCH_PAGE);
                goToHomePage();
                logger.log("is car rental vertical open -> " + isVerticalPageOpen);
                break;

            case "train":
                openVerticalPage("Train");
                isVerticalPageOpen = driverTools.isElementDisplayed(TRAIN_SEARCH_PAGE);
                goToHomePage();
                logger.log("is train vertical open -> " + isVerticalPageOpen);
                break;

            case "event":
                clickVerticalTitle("To Dos", "To Dos");
                String url = driverTools.getCurrentURL();
                driverTools.waitForElementToPresence(EVENT_SEARCH_PAGE, 10);
                isVerticalPageOpen = driverTools.isElementDisplayed(EVENT_SEARCH_PAGE);
                goToHomePage();
                logger.log("is event vertical open -> " + isVerticalPageOpen);
                break;

            case "airport transfer":
                clickVerticalTitle("Airport Pickups", "Jemputan Bandara");
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
    public boolean checkBannerScroll(String moduleId) {
        driverTools.isElementDisplayed(getBannerCarouselScroll(moduleId));
        return driverTools.isElementDisplayed(getBannerCarouselScroll(moduleId));
    }
    @Override
    public boolean checkBannerCarouselTitleInside(String moduleId) {
        driverTools.isElementDisplayed(getBannerCarouselInsideTitle(moduleId));
        return driverTools.isElementDisplayed(getBannerCarouselInsideTitle(moduleId));
    }
    @Override
    public boolean checkBannerCarouselSubTitleInside(String moduleId) {
        driverTools.isElementDisplayed(getBannerCarouselInsideSubtitle(moduleId));
        return driverTools.isElementDisplayed(getBannerCarouselInsideSubtitle(moduleId));
    }
    @Override
    public void clickSeeAllBannerCarousel(String moduleId) {
        driverTools.waitForElementToBeClickable(getSeeAllBannerCarousel(moduleId));
        driverTools.clickWithJavascriptExecutor(getSeeAllBannerCarousel(moduleId));
    }
    @Override
    public boolean bannerCarouselRedirectionCheck() {
        driverTools.waitForElementToAppear(PROMO_LIST);
        return driverTools.isElementDisplayed(PROMO_LIST);
    }
    @Override
    public boolean verifyIWCSectionNonIDR(){
        driverTools.refreshWebpage();
        driverTools.scrollIntoWebView(IWC_SECTION_NON_IDR);
        driverTools.waitForElementToPresence(IWC_SECTION_NON_IDR, 4);
        driverTools.waitForElementToPresence(IWC_PC_NON_IDR, 4);
        boolean isIWCSectionAppear = driverTools.isElementDisplayed(IWC_SECTION_NON_IDR);
        boolean isIWCNonIDR = driverTools.isElementDisplayed(IWC_PC_NON_IDR);
        Assertion.assertThat("Verify Image With Chips Section Appear" , isIWCSectionAppear, is(true));
        Assertion.assertThat("Verify Image With Chips Product Card Appear" , isIWCNonIDR, is(true));
        return isIWCSectionAppear && isIWCNonIDR;
    }

    @Override
    public boolean verifyHDPSectionNonIDR(){
        driverTools.refreshWebpage();
        driverTools.scrollIntoWebView(HDP_SECTION);
        driverTools.waitForElementToPresence(HDP_SECTION, 2);
        driverTools.waitForElementToPresence(HDP_PC_NON_IDR, 2);
        boolean isHDPSectionAppear = driverTools.isElementDisplayed(HDP_SECTION);
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
        driverTools.waitForElementToPresence(FS_PC_NON_IDR, 2);
        boolean isFlashSaleSectionAppear = driverTools.isElementDisplayed(FS_SECTION_NON_IDR);
        boolean isFlashSaleNonIDR = driverTools.isElementDisplayed(FS_PC_NON_IDR);
        Assertion.assertThat("Verify Flash Sale Section Appear" , isFlashSaleSectionAppear, is(true));
        Assertion.assertThat("Verify Flash Sale Product Card Appear" , isFlashSaleNonIDR, is(true));
        return isFlashSaleSectionAppear && isFlashSaleNonIDR;
    }

    public void closeBottomsheetPromo() {
        logger.log("On dWeb, do nothing");
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
        driverTools.clickWithJavascriptExecutor(HDPInventoriesName.get(0));
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
        List<WebElement> HDPPrices = driverTools.elements(getHDPInventoryPrice(moduleId)).orElseThrow();
        List<Boolean> areHDPPriceVisible = new ArrayList<>();
        for (WebElement HDPPrice : HDPPrices) {
            areHDPPriceVisible.add(driverTools.isElementDisplayed(HDPPrice));
        }
        return !areHDPPriceVisible.contains(false);
    }

    @Override
    public boolean verifyHDPStrikePrice(String moduleId) {
        List<WebElement> HDPStrikePrices = driverTools.elements(getHDPInventoryStrikePrice(moduleId)).orElseThrow();
        List<Boolean> areHDPStrikePriceVisible = new ArrayList<>();
        for (WebElement HDPStrikePrice : HDPStrikePrices) {
            areHDPStrikePriceVisible.add(driverTools.isElementDisplayed(HDPStrikePrice));
        }
        return !areHDPStrikePriceVisible.contains(false);
    }

    @Override
    public void openHotelPDPViaURL(String s) {
        String baseUrl = "";

        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = STAGING_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            baseUrl = PREPROD_DWEB_URL;
        }else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = PROD_DWEB_URL;
        }

        driverTools.get(baseUrl + "hotel" + s);
    }

    @Override
    public void openHotelPDPViaURLWithDate(String s, String startDate, String endDate) {
        String baseUrl = "";

        if (Configs.ENVIRONMENT == Environment.GK) {
            baseUrl = STAGING_DWEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            baseUrl = PREPROD_DWEB_URL;
        }else if (Configs.ENVIRONMENT == Environment.PROD) {
            baseUrl = PROD_DWEB_URL;
        }

        driverTools.get(baseUrl + "hotel" + s + "?checkin=" + startDate + "&checkout=" + endDate);
    }

    public void clickPromoListHeaderEntrypoint() {
        driverTools.waitForElementToAppear(PROMO_LIST_HEADER_ENTRYPOINT);
        driverTools.clickWithJavascriptExecutor(PROMO_LIST_HEADER_ENTRYPOINT);
    }



}