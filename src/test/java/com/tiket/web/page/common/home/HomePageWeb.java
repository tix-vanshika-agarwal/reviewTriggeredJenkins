package com.tiket.web.page.common.home;

import com.tiket.apiPage.Unm;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.model.Platform;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public abstract class HomePageWeb<T extends WebDriver> extends WebPage<T> {

    public HomePageWeb(Session<T> session) {
        super(session);
    }

    public static final String STAGING_WEB_URL = "https://gatotkaca.tiket.com/";
    public static final String PREPROD_WEB_URL = "https://preprod.tiket.com/";
    public static final String PROD_WEB_URL = "https://tiket.com/";
    private static final Locator PAGE_NOT_FOUND_MESSAGE = new Locator("PAGE_NOT_FOUND_MESSAGE", By.xpath("//*[normalize-space(text())='This page could not be found.']"));
    private static final Locator GLOBAL_SEARCH_BAR = new Locator("GLOBAL_SEARCH_BAR", By.xpath("//*[@aria-label[contains(.,'Search')]]"));
    private static final Locator GLOBAL_SEARCH_MODAL = new Locator("GLOBAL_SEARCH_MODAL", By.xpath("//div[@id='global-search-modal']"));
    private static final Locator GS_INFO_ICON = new Locator("GS_INFO_ICON", By.xpath("(//img[contains(@alt, 'Automation GS Info')])[1]"));
    private static final Locator GS_INFO_TITLE = new Locator("GS_INFO_TITLE", By.xpath("(//p[contains(text(), 'Automation GS Info')])[1]"));
    private static final Locator GS_MS_TITLE = new Locator("GS_MS_TITLE", By.xpath("//div[contains(@class, 'GSMostSearch_module_title')]//p[contains(text(), 'Automation GS MS')]"));
    private static final Locator GS_MS_IMG_PREVIEW = new Locator("GS_MS_IMG_PREVIEW", By.xpath("(//div[contains(@class, 'GSMostSearch_slider_container')]//a[contains(@class, 'GSMostSearch_card')]//img[contains(@class, 'GSMostSearch_img_default')])[1]"));
    private static final Locator GS_IS_SECTION = new Locator("GS_IS_SECTION", By.xpath("(//section[contains(@class, 'GSInspirationalSearch')])[1]"));
    private static final Locator LANGUAGE_AND_CURRENCY_CLOSE_BTN = new Locator("LANGUAGE_AND_CURRENCY_CLOSE_BTN", By.xpath("//button[contains(@class, 'LanguageAndCurrencyContent-module__button_close')]"));
    private static final Locator PRODUCT_HOTEL_ICON = new Locator("PRODUCT_HOTEL_ICON", new By.ByXPath("//a[contains(@href, '/hotel') and contains(@class, 'HeaderBottom-module__option')]"));
    private static final Locator DATE_PICKER = new Locator("DATE_PICKER", new By.ByXPath("//*[@data-testid='date-picker']"));
    private static final Locator DESCRIPTION_HOLIDAY = new Locator("DESCRIPTION_HOLIDAY", new By.ByXPath("//*[contains(@class, 'HolidaysSection_item_text__')]"));
    private static final Locator CALENDAR_CLOSE_BTN = new Locator("CALENDAR_CLOSE_BTN", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    private static final Locator VILLA_TRANSFER_SEARCH_PAGE = new Locator("VILLA_SEARCH_PAGE", new By.ByXPath("//div[@class = 'HeroBannerNHA_form_container____tJw']"));
    private static final Locator TRAIN_SEARCH_PAGE = new Locator("TRAIN_SEARCH_PAGE", new By.ByXPath("//div[contains(@class, 'kereta-api_content_wrapper')]/h1[contains(@class, 'kereta-api_title')]"));
    private static final Locator EVENT_SEARCH_PAGE = new Locator("EVENT_SEARCH_PAGE", new By.ByXPath("(//li[contains(@class, 'BreadcrumbItem_breadcrumb_item_container')]//span[contains(@class, 'BreadcrumbItem_text')])[2]"));
    private static final Locator AIRPORT_TRANSFER_SEARCH_PAGE = new Locator("AIRPORT_TRANSFER_SEARCH_PAGE", new By.ByXPath("//h1[contains(@class, 'HeroImage_title')]"));
    private static final Locator TO_DO_ICON = new Locator("TO_DO_ICON", new By.ByXPath("//a[contains(@href, '/to-do') and contains(@class, 'HeaderBottom-module__option')]"));
    private static final Locator AIRPORT_TRANSFER_ICON = new Locator("AIRPORT_TRANSFER_ICON", new By.ByXPath("//a[@data-testid='verticalTab-airport-transfer']"));
    private static final Locator FLIGHT_PAGE_BUTTON = new Locator("FLIGHT_PAGE_BUTTON", new By.ByXPath("//a[contains(@href, '/pesawat')]"));
    private static final Locator FLIGHT_SEARCH_PAGE = new Locator("FLIGHT_SEARCH_PAGE", new By.ByXPath("//div[@class=\"flight_hero_title_wrapper__emkQg\"]"));
    private static final Locator HOTEL_SEARCH_PAGE = new Locator("HOTEL_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='hotel-hero-banner-container']"));
    private static final Locator TODO_SEARCH_PAGE = new Locator("TODO_SEARCH_PAGE", new By.ByXPath("//div[@data-testid='home-location-label']//span[contains(@class, 'Text_size_h2')]"));
    private static final Locator CAR_RENTAL_SEARCH_PAGE = new Locator("CAR_RENTAL_SEARCH_PAGE", new By.ByXPath("//div[@class='car_searchform_wrapper__ZRKBS']"));
    private static final Locator CAR_RENTAL_VERTICAL_ICON = new Locator("CAR_RENTAL_VERTICAL_ICON", new By.ByXPath("//span[normalize-space()='Car Rentals']"));
    private static Locator SEEALLPROMO_BUTTON = new Locator("SEEALLPROMO_BUTTON", By.xpath("(//a[contains(text(), 'See all promo')])[1]"));
    private static final Locator TTD_LP = new Locator("TTD_LP", By.xpath("//h1[@data-testid='seo-title']"));
    private static final Locator CLAIM_CENTER_MWEB = new Locator("CLAIM_CENTER_MWEB", new By.ByXPath("//a[@aria-label='Claim Center' and @href='https://m.preprod.tiket.com/insurance-claim?hideToolbar=true']"));
    private static final Locator RECENTLY_SEARCHED = new Locator("RECENTLY_SEARCHED", new By.ByXPath("//p[text()='Near me' or text()='Dekat saya']"));
    private static Locator IMAGE_WITH_CHIPS_INVENTORY;
    private static Locator IWC_HEADER = new Locator("IWC_HEADER", new By.ByXPath(".//div[contains(@class, 'Layout_title') and contains(@class, 'Text_size_h3')]"));
    private static WebElement IWCElement;
    private static Locator IWC_HEADER_SUBTITLE = new Locator("IWC_HEADER_SUBTITLE", new By.ByXPath(".//h3[contains(@class, 'Layout_subtext') and contains(@class, 'Text_size_b2')]"));
    private static Locator IWC_CHIPS = new Locator("IWC_CHIPS", new By.ByXPath("//span[contains(text(), 'Hotel')]"));
    private static Locator IWC_INVENTORIES = new Locator("IWC_INVENTORIES", new By.ByXPath(".//a[@data-testid='product-card']"));
    private static Locator IWC_IMG = new Locator("IWC_IMG", new By.ByXPath(".//img[@data-nimg='fill']"));
    private static Locator IWC_INVENTORIES_NAME = new Locator("IWC_INVENTORIES_NAME", new By.ByXPath(".//h2[contains(@class,product_name_product_name)]"));
    private static Locator IWC_PRICE = new Locator("IWC_PRICE", new By.ByXPath(".//div[contains(@class, 'Text_size_b2') and contains(@class, 'Text_variant_price')]"));
    private static Locator IWC_STRIKE_PRICE = new Locator("IWC_STRIKE_PRICE", new By.ByXPath(".//div[contains(@class, 'Text_diagonal_strikethrough') and contains(@class, 'Text_diagonal_size_b3')]"));
    private static Locator IWC_SEEALL_BUTTON = new Locator("IWC_SEEALL_BUTTON", new By.ByXPath(".//a[contains(@class, Button_variant_secondary)]"));
    private static Locator IWC_TITLE_PDP = new Locator("IWC_TITLE_PDP", new By.ByXPath("//h1[contains(@class, Text_size_h2)]"));
    private static final Locator BANNER_CAROUSEL = new Locator("BANNER_CAROUSEL", By.xpath("//div[contains(@class,'Layout_title') and contains(.,'Automation') and contains(.,'Banner Carousel')]"));
    private static final Locator BANNER_CAROUSEL_SUBTITLE = new Locator("BANNER_CAROUSEL_SUBTITLE", By.xpath("//h3[contains(@class,'Layout_subtext') and normalize-space(.)='autoamtion banner carousel']"));
    private static Locator REGISTER_NOW = new Locator("REGISTER_NOW", By.xpath("//*[contains(text(), \"Register now!\") or contains(text(), \"Daftar, yuk!\")]") );
    private static Locator BANNER_CAROUSEL_V2_INVENTORY;
    private static Locator BANNER_CAROUSEL_ANOTHER_CHIPS = new Locator("BANNER_CAROUSEL_ANOTHER_CHIPS", new By.ByXPath("(//button[contains(@class, 'Chip_chip')])[2]"));
    private static final Locator IWC_ANOTHER_LOCATOR = new Locator("IWC_ANOTHER_LOCATOR", new By.ByXPath("//h1[contains(@class, 'Text_size_big')]"));
    private static Locator BANNER_GRID_INVENTORY;
    private static Locator BANNER_GRID_ANOTHER_CHIPS;
    private static Locator HDP_INVENTORY;
    private static Locator GV_SECTION;
    private static final Locator VERTICAL_SCROLL_RIGHT_BUTTON = new Locator("VERTICAL_SCROLL_RIGHT_BUTTON", By.cssSelector("[data-testid='btnScrollright']"));
    private static Locator INSURANCE_HOMEPAGE = new Locator("INSURANCE_HOMEPAGE", new By.ByXPath("//img[@alt='vertical.icon.alternate.insurance']"));
    private static Locator INSURANCE_PAGE_LOGIN_BUTTON = new Locator("INSURANCE_PAGE_LOGIN_BUTTON", new By.ByXPath("//button[.//span[normalize-space(text())='Log in']]"));
    private static Locator INSURANCE_PAGE = new Locator("INSURANCE_PAGE", new By.ByXPath("//h1[text()='Insurance' or text()='Asuransi']"));
    private static Locator LOGIN_ICON = new Locator("LOGIN_ICON", new By.ByXPath("//img[@alt='Partner Logo']"));
    private static final Locator UPCOMING_OR_EMPTY_BOOKING = new Locator("UPCOMING_OR_EMPTY_BOOKING", By.xpath("//span[normalize-space()='Your Upcoming Booking' " + "or normalize-space()='Pesananmu selanjutnya' " + "or normalize-space()='No active booking']"));
    private static Locator EMAIL_LOGIN = new Locator("EMAIL_LOGIN", new By.ByXPath("//button[.//span[normalize-space(text())='Continue with Phone or Email' or normalize-space(text())='Lanjut dengan nomor HP atau email']]"));
    private static final Locator PROMO_LIST = new Locator("PROMO_LIST", new By.ByXPath("//*[text()='Voucher Box']"));
    private static Locator HDP_LABEL = new Locator("HDP_LABEL", new By.ByXPath("//p[contains(@class, 'Layout_label')]"));
    private static Locator HDP_TITLE = new Locator("HDP_TITLE", new By.ByXPath("//div[contains(@class, 'Layout_title')]"));
    private static Locator HDP_SUBTITLE = new Locator("HDP_SUBTITLE", new By.ByXPath("//h3[contains(@class, 'Layout_subtext')]"));
    private static Locator HDP_PRODUCT_NAME = new Locator("HDP_PRODUCT_NAME", new By.ByXPath("//a[contains(@class, 'ProductCard_product_card')]//h2[contains(@class, 'product_name_product_name')]"));
    private static Locator COUNTRY_CURRENCY_ENTRY_POINT = new Locator("COUNTRY_CURRENCY_ENTRY_POINT", new By.ByXPath("//button[contains(@class, 'LanguageAndCurrencyModal-module__trigger')]"));
    private static Locator COUNTRY_SELECTION_DROPDOWN = new Locator("COUNTRY_SELECTION_DROPDOWN", new By.ByXPath("//button[contains(@class, 'CountryDropdown-module__trigger')]"));
    private static Locator SAVE_BUTTON_ON_MODAL = new Locator("SAVE_BUTTON_ON_MODAL", new By.ByXPath("//button[.//span[text()='Save' or text()='Simpan']]"));
    private static final Locator LCC_ICON = new Locator("LCC_ICON", By.xpath("//button[contains(@class,'LanguageAndCurrencyModal-module__trigger')]//span"));
    private static final Locator COUNTRY_MODAL_LABEL = new Locator("COUNTRY_MODAL_LABEL", By.xpath("//h3[contains(@class, 'CountryContent-module__country_label')]"));
    private static final Locator COUNTRY_DROPDOWN_ICON = new Locator("COUNTRY_DROPDOWN_ICON", By.xpath("//button[contains(@class,'CountryDropdown-module__trigger')]"));
    private static final Locator COUNTRY_INDONESIA_OPTION = new Locator("COUNTRY_INDONESIA_OPTION", By.xpath("//span[contains(@class,'Text-module__text')][normalize-space()='Indonesia']"));
    private static final Locator SAVE_BUTTON_LCC = new Locator("SAVE_BUTTON_LCC", By.xpath("//button[contains(@class,'LanguageAndCurrencyModal-module__button')]"));
    protected static final Locator HELP_CENTER_RETRY = new Locator("HELP_CENTER_RETRY", By.xpath("//*[contains(text(),'Coba') or contains (text(),'Retry')]"));
    private static Locator PROMOTION_MODAL_POPUP = new Locator("PROMOTION_MODAL_POPUP", By.xpath("//section[contains(@class, 'Modal')]"));
    private static Locator PROMOTION_MODAL_POPUP_CLOSE_BUTTON = new Locator("PROMOTION_MODAL_POPUP_CLOSE_BUTTON", By.xpath("//button[@data-testid=\"modal-close-button\"]"));
    private static final Locator RIGHT_ARROW_LOCATOR = new Locator("RIGHT_ARROW_LOCATOR", By.xpath("//button[contains(@class, 'TabList_overflow_nav_right')]"));
    private static final Locator INSTALL_APP_BANNER_BUTTON = new Locator("INSTALL_APP_BANNER_BUTTON", By.xpath("//button[normalize-space()='Install' or normalize-space()='Instal' or .//span[normalize-space()='Install'] or .//span[normalize-space()='Instal']]"));
    private static final Locator INSTALL_APP_BANNER = new Locator("INSTALL_APP_BANNER", By.xpath("//button[normalize-space()='Install' or normalize-space()='Instal' or .//span[normalize-space()='Install'] or .//span[normalize-space()='Instal']]/ancestor::*[contains(normalize-space(.),'promo') or contains(normalize-space(.),'Promo')][1]"));
    private static final Locator QR_POPUP = new Locator("QR_POPUP", By.xpath("//*[@role='dialog' or contains(@class,'Modal')][.//*[contains(normalize-space(.),'app.tiket.com')]]"));
    private static final Locator QR_POPUP_TITLE = new Locator("QR_POPUP_TITLE", By.xpath("(//*[@role='dialog' or contains(@class,'Modal')][.//*[contains(normalize-space(.),'app.tiket.com')]]//*[self::h1 or self::h2 or self::h3 or contains(@class,'Text_size_h2') or contains(@class,'Text_size_h1')])[1]"));
    private static final Locator QR_POPUP_SCANNER_IMAGE = new Locator("QR_POPUP_SCANNER_IMAGE", By.xpath("//*[@role='dialog' or contains(@class,'Modal')][.//*[contains(normalize-space(.),'app.tiket.com')]]//img"));
    private static final Locator QR_POPUP_CLOSE_BUTTON = new Locator("QR_POPUP_CLOSE_BUTTON", By.xpath("//*[@role='dialog' or contains(@class,'Modal')][.//*[contains(normalize-space(.),'app.tiket.com')]]//button[@data-testid='modal-close-button']"));
    private static final Locator ORDER_STATUS_HOMEPAGE = new Locator("ORDER_STATUS_HOMEPAGE", By.xpath("//h1[contains(@class,'DesktopSuccessNavigation_desktop_header') and (normalize-space()='Order confirmed!' or normalize-space()='Pesanan dikonfirmasi!')]"));

    private static final Locator FIRST_TRANSACTION_PROMO_CONTINUE_BUTTON = new Locator("FIRST_TRANSACTION_PROMO_CONTINUE_BUTTON", By.xpath("//div[contains(@class,'styles-module')]//button[contains(@class,'Button-module__variant_secondary')]"));
    private static final Locator HOME_APP_INSTALL_BANNER_IMAGE = new Locator("HOME_APP_INSTALL_BANNER_IMAGE", By.xpath("//section//div[contains(@class,'BannerGrid')]//div[contains(@class,'CardWithText')]//img"));
    private static final Locator QR_CODE_POPUP_MODAL = new Locator("QR_CODE_POPUP_MODAL", By.xpath("//section[contains(@class,'QrCode_popup_modal')]"));
    private static final Locator QR_CODE_POPUP_CLOSE_BUTTON = new Locator("QR_CODE_POPUP_CLOSE_BUTTON", By.xpath("//section[contains(@class,'QrCode_popup_modal')]//button[@data-testid='modal-close-button']"));
    private static final Locator QR_CODE_POPUP_COPY_BUTTON = new Locator("QR_CODE_POPUP_COPY_BUTTON", By.xpath("//section[contains(@class,'QrCode_popup_modal')]//button[contains(@class,'QrCode_copy_button')]"));
    private static Locator getQrCodePopupStoreButton(String storeLabel) {
        int index = switch (storeLabel) {
            case "App Store" -> 1;
            case "Google Play" -> 2;
            case "AppGallery" -> 3;
            default -> throw new IllegalArgumentException("Unknown QR popup store label: " + storeLabel);
        };
        return new Locator("QR_CODE_POPUP_STORE_BUTTON_" + index, By.xpath(
                "//section[contains(@class,'QrCode_popup_modal')]//div[contains(@class,'QrCode_store')]//a[" + index + "]"));
    }
    private static Locator getLanguageOnModal(String language) {
        return new Locator("LANGUAGE_ON_MODAL", By.xpath(String.format("//label[.//span[normalize-space()='%s']]", language)));
    }

    private static Locator getCurrencyModal(String currency) {
        return new Locator("CURRENCY_ON_MODAL", By.xpath(String.format("//label[contains(@class, 'CurrencyContent')]//span[contains(text(), '%s')]", currency)));
    }

    private static Locator getCountryModal(String country) {
        return new Locator("COUNTRY_ON_MODAL", By.xpath(String.format("//label[contains(@class, 'CountryBottomSheet')]//span[text()='%s']", country)));
    }

    private static Locator getBannerCarouselList(String moduleId) {
        return new Locator("BANNER_CAROUSEL_LIST", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'Layout_children_top_margin')]", moduleId)));
    }

    private static Locator getBannerCarouselV2Card(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_CARD",  By.xpath(String.format("//section[@id='%s']//a[contains(@class,'CardWithTextOverlay_card')]", moduleId)));
    }
    private static Locator getBannerCarouselV2URL(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_URL",  By.xpath(String.format("//section[@id='%s']//a[contains(@class,'CardWithTextOverlay_card')]", moduleId)));
    }
    private static Locator getBannerCarouselV2SeeAllButton(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_SEE_ALL_BUTTON",  By.xpath(String.format("//section[@id='%s']//a[contains(@class, Button_variant_secondary)]", moduleId)));
    }
    private static Locator getBannerCarouselV2CardSubtitle(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_CARD_SUBTITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_subtext')]", moduleId)));
    }
    private static Locator getBannerCarouselV2CardTitle(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_CARD_TITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_title')]", moduleId)));
    }
    private static Locator getBannerCarouselV2Image(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_IMAGE", By.xpath(String.format("//section[@id='%s']//img[@data-nimg='fill']", moduleId)));
    }
    private static Locator getBannerCarouselV2Title(String moduleId) {
        return new Locator("BANNER_CAROUSEL_HEADER_TITLE", By.xpath(String.format("//section[@id='%s']//div[contains(@class, 'Layout_title') and contains(@class, 'Text_size_h3')]", moduleId)));
    }
    private static Locator getBannerCarouselV2Subtitle(String moduleId) {
        return new Locator("BANNER_CAROUSEL_HEADER_SUBTITLE", By.xpath(String.format("//section[@id='%s']//h3[contains(@class, 'Layout_subtext') and contains(@class, 'Text_size_b2')]", moduleId)));
    }
    private static Locator getBannerCarouselV2Chips(String moduleId) {
        return new Locator("BANNER_CAROUSEL_V2_CHIPS", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'BannerCarouselV2_edge_padding')]", moduleId)));
    }
    private static Locator getBannerCarouselV2Chips(String moduleId, int position) {
        return new Locator("BANNER_CAROUSEL_V2_CHIPS", By.xpath(String.format("(//section[@id='%s']//div[contains(@class,'BannerCarouselV2_edge_padding')])[%d]", moduleId, position)));
    }

    private static Locator getSearchKeywordsText(String keywords) {
        return new Locator("GS_VERTICAL_TEXT", By.xpath(String.format("//p[contains(@class, 'GSStaticVertical-module__content_title') and contains(text(), '%s')]", keywords)));
    }

    //Highlight Discovery Product
    private static Locator HDP_TITLE_PDP = new Locator("HDP_TITLE_PDP", new By.ByXPath("//h1[contains(@class, 'Text_variant_lowEmphasis')]"));
    private static Locator getHDPLabel(String moduleId){
        return new Locator("HDP_HEADER_LABEL", By.xpath(String.format("//section[@id='%s']//p[contains(@class, 'Layout_label') and contains(@class, 'Text_size_b3')]", moduleId)));
    }
    private static Locator getHDPTitle(String moduleId) {
        return new Locator("HDP_HEADER_TITLE", By.xpath(String.format("//section[@id='%s']//div[contains(@class, 'Layout_title') and contains(@class, 'Text_size_h3')]", moduleId)));
    }
    private static Locator getHDPSubtitle(String moduleId) {
        return new Locator("HDP_HEADER_SUBTITLE", By.xpath(String.format("//section[@id='%s']//h3[contains(@class, 'Layout_subtext') and contains(@class, 'Text_size_b2')]", moduleId)));
    }
    private static Locator getHDPInventory(String moduleId){
        return new Locator("HDP_Product_List", By.xpath(String.format("//section[@id='%s']//a[contains(@class,'ProductCard_product_card')]", moduleId)));
    }

    private static final Locator INFO_PAGE_ENTRY_POINT = new Locator("INFO_PAGE_ENTRY_POINT", By.xpath("//a[contains(text(), 'Terms & Conditions') or contains(text(),'Syarat & Ketentuan')]"));

    //BannerGrid
    private static Locator getBannerGridBelowText(String moduleId) {
        return new Locator("BANNER_GRID_BELOW_TEXT", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_subtext')]", moduleId)));
    }

    private static Locator getBannerGridMainText(String moduleId) {
        return new Locator("BANNER_GRID_MAIN_TEXT", By.xpath(String.format("//section[@id='%s']//h3[contains(@class,'Layout_title')]", moduleId)));
    }

    private static Locator getBannerGridTitle(String moduleId) {
        return new Locator("BANNER_GRID_TITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_title')]", moduleId)));
    }

    private static Locator getBannerGrid(String moduleId) {
        return new Locator("BANNER_GRID", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'BannerGrid_grid')]", moduleId)));
    }

    private static Locator getBannerGridLists(String moduleId) {
        return new Locator("BANNER_GRID_LIST", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'CardWithTextOverlay_card_border_radius')]", moduleId)));
    }

    private static Locator getBannerGridTitleHeader(String moduleId){
        return new Locator("BANNER_GRID_HEADER_TITLE", By.xpath(String.format("//section[@id='%s']//h3[contains(@class, 'Layout_title') and contains(@class, 'Text_size_h3')]", moduleId)));
    }
    private static Locator getBannerGridSubtitleHeader(String moduleId){
        return new Locator("BANNER_GRID_HEADER_SUBTITLE", By.xpath(String.format("//section[@id='%s']//div[contains(@class, 'Layout_subtext') and contains(@class, 'Text_size_b2')]", moduleId)));
    }
    private static Locator getBannerGridChips(String moduleId){
        return new Locator("BANNER_GRID_CHIPS", By.xpath(String.format("//section[@id='%s']//div[contains(@class, 'BannerGrid_edge_padding')]", moduleId)));
    }
    private static Locator getBannerGridList(String moduleId){
        return new Locator("BANNER_GRID_LIST", By.xpath(String.format("//section[@id='%s']//a[contains(@class,'CardWithTextOverlay_card')]", moduleId)));
    }
    private static Locator getBannerGridCardTitle(String moduleId) {
        return new Locator("BANNER_GRID_CARD_TITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_title')]", moduleId)));
    }
    private static Locator getBannerGridCardSubtitle(String moduleId) {
        return new Locator("BANNER_GRID_CARD_SUBTITLE", By.xpath(String.format("//section[@id='%s']//p[contains(@class,'CardWithTextOverlay_subtext')]", moduleId)));
    }
    private static Locator getBannerGridSeeAllButton(String moduleId) {
        return new Locator("BANNER_GRID_SEE_ALL_BUTTON",  By.xpath(String.format("//section[@id='%s']//a[contains(@class, Button_variant_secondary)]", moduleId)));
    }
    private static Locator getBannerGridV2URL(String moduleId) {
        return new Locator("BANNER_GRID_URL",  By.xpath(String.format("//section[@id='%s']//a[contains(@class,'CardWithTextOverlay_card')]", moduleId)));
    }

    //GV_SECTION
    private static Locator getGVSectionTitleHeader(String moduleId){
        return new Locator("GV_SECTION_HEADER_TITLE", By.xpath(String.format("//section[@id='%s']//div[contains(@class,'Layout_title')]", moduleId)));
    }
    private static Locator getGVSectionSubtitleHeader(String moduleId){
        return new Locator("GV_SECTION_HEADER_SUBTITLE", By.xpath(String.format("//section[@id='%s']//h3[contains(@class,'Layout_subtext')]", moduleId)));
    }
    private static Locator getGVSectionIcon(String moduleId){
        return new Locator("GV_SECTION_ICON_MODULE", By.xpath(String.format("//section[@id='%s' and contains(@class,'Layout_icon_size_default')]", moduleId)));
    }
    private static Locator getGVSectionVoucherCard(String moduleId){
        return new Locator("GV_SECTION_VOUCHER_CARD", By.xpath(String.format("//section[@id='%s']//div[contains(@class, 'VoucherCard_voucher_card')]", moduleId)));
    }
    private static Locator getGVSectionSeeAllButton(String moduleId) {
        return new Locator("GV_SECTION_SEE_ALL_BUTTON",  By.xpath(String.format("//section[@id='%s']//a[contains(@class, Button_variant_secondary)]", moduleId)));
    }
    private static Locator getGVSectionVertical(String moduleId) {
        return new Locator("GV_SECTION_VERTICAL",  By.xpath(String.format("//section[@id='%s']//p[contains(@class,'GiftVoucherCard_title')]", moduleId)));
    }
    private static Locator getGVSectionExpiryData(String moduleId) {
        return new Locator("GV_SECTION_EXPIRY_DATE",  By.xpath(String.format("//section[@id='%s']//p[contains(@class,'GiftVoucherCard_subtitle')]", moduleId)));
    }
    private static Locator getGVSectionClaimButton(String moduleId) {
        return new Locator("GV_SECTION_CLAIM_BUTTON",  By.xpath(String.format("//section[@id='%s']//button[contains(@class,'GiftVoucherCard_claim_button')]", moduleId)));
    }


    public static <T extends WebDriver> HomePageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new HomePageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new HomePageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    /**
     * Tears down a temporary test account created during a web test: logs out of the current web
     * session and deletes the account via the UNM API so it does not pollute future runs. Web logout is
     * platform-specific, so it is delegated to the web login page.
     *
     * <p>This whole teardown only runs on non-PROD environments (temporary accounts are not created on
     * PROD). Any teardown failure - including an {@link AssertionError} thrown by the UNM delete call -
     * is caught and logged so it can never overturn the original test result.
     *
     * @param accountData the account created earlier in the test (may be {@code null} if creation failed).
     */
    public void logoutAndDeleteTemporaryAccount(RegisterNewAccountData accountData) {
        if (Configs.ENVIRONMENT == Environment.PROD) {
            return;
        }
        try {
            LoginPageWeb.getInstance(session).logoutViaUrl();
            if (accountData != null && accountData.unmData != null) {
                goToHomePage();
                new Unm<>(session).deleteAccountViaApi(accountData.unmData);
            }
        } catch (Throwable cleanupError) {
            logger.warn("Cleanup failed (ignored to preserve the original test result): "
                    + cleanupError.getMessage());
            // Preserve the full stack trace so a swallowed teardown failure is still debuggable.
            logger.exception(cleanupError);
        }
    }

    public void clickSeeAllPromoButton() {
        driverTools.waitForElementToAppear(SEEALLPROMO_BUTTON);
        driverTools.scrollIntoWebView(SEEALLPROMO_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEEALLPROMO_BUTTON);
    }

    public void clickClaimCentre() {
        driverTools.swipeToElement(CLAIM_CENTER_MWEB, Swipe.LEFT, 5);
        driverTools.clickWithJavascriptExecutor(CLAIM_CENTER_MWEB);
    }

    public abstract boolean verifyVerticalIcons(String vertical);

    public boolean verifyVerticalIcons() {
        openVerticalPage("Flights");
        boolean isFlightVerticalPageOpen = driverTools.isElementDisplayed(FLIGHT_SEARCH_PAGE);
        goToHomePage();

        openVerticalPage("Hotels");
        boolean isHotelsVerticalPageOpen = driverTools.isElementDisplayed(HOTEL_SEARCH_PAGE);
        goToHomePage();

        openVerticalPage("To Dos");
        boolean isToDosVerticalPageOpen = driverTools.isElementDisplayed(TODO_SEARCH_PAGE);
        goToHomePage();

        openVerticalPage("Car Rentals");
        boolean isCarRentalsVerticalPageOpen = driverTools.isElementDisplayed(CAR_RENTAL_SEARCH_PAGE);
        goToHomePage();


        logger.log("is flight vertical open -> " + isFlightVerticalPageOpen);
        logger.log("is hotel vertical open -> " + isHotelsVerticalPageOpen);
        logger.log("is to do vertical open -> " + isToDosVerticalPageOpen);
        logger.log("is car rental vertical open -> " + isCarRentalsVerticalPageOpen);

        return isFlightVerticalPageOpen && isHotelsVerticalPageOpen && isToDosVerticalPageOpen
                && isCarRentalsVerticalPageOpen;
    }

    public abstract Locator getImgVerticalLocator(String verticalEn, String verticalId);

    public abstract boolean verifyHomeIconImage();

    public boolean verifyHomeIconTitle() {
        List<Locator> verticals = List.of(
                getTitleVerticalLocator("To Do", "To Do"),
                getTitleVerticalLocator("Hotel", "Hotels"),
                getTitleVerticalLocator("Train", "Kereta"),
                getTitleVerticalLocator("Car Rental", "Sewa Mobil")
        );

        driverTools.waitForElementToAppear(verticals.get(0), 5);

        boolean allFound = true;
        for (Locator vertical : verticals) {
            if (!driverTools.isElementDisplayed(vertical)) {
                if (driverTools.isElementDisplayed(RIGHT_ARROW_LOCATOR)) {
                    driverTools.clickWithJavascriptExecutor(RIGHT_ARROW_LOCATOR);
                    driverTools.waitForElementToAppear(vertical, 2);
                }
                if (!driverTools.isElementDisplayed(vertical)) {
                    logger.log("Vertical title not displayed: " + vertical);
                    allFound = false;
                }
            }
        }
        return allFound;
    }

    public Locator getTitleVerticalLocator(String verticalEn, String verticalId) {
        return new Locator("VERTICAL_TITLE", By.xpath("//span[contains(@class,'Text_text') and (contains(text(),'" + verticalEn + "') or contains(text(),'" + verticalId + "'))]"));
    }

    public void clickVerticalTitle(String verticalEn, String verticalId) {
        Locator vertical = getTitleVerticalLocator(verticalEn, verticalId);
        if (!driverTools.isElementDisplayed(vertical)) {
            if (driverTools.isElementDisplayed(RIGHT_ARROW_LOCATOR)) {
                driverTools.clickWithJavascriptExecutor(RIGHT_ARROW_LOCATOR);
                driverTools.waitForElementToAppear(vertical, 2);
            }
        }
        driverTools.clickWithJavascriptExecutor(vertical);
        driverTools.waitForWebToLoad();
    }

    public abstract boolean verifyHDPSectionNonIDR();

    public abstract boolean verifyIWCSectionNonIDR();

    public abstract boolean verifyFlashSaleSectionNonIDR();

    public abstract boolean isHomePageOpened();

    public abstract void clickOnLoginButton();

    public abstract void goToHomePage();

    public abstract void clickOnHomeIcon();

    public abstract void waitForHomePageToOpen();

    public abstract void clickRegisterButton();

    public abstract void selectCurrency(String idr);

    public void openVerticalPageViaURL(String vertical) {
        openVerticalPageViaURL(vertical, null);
    }

    public void openVerticalPageViaURL(String vertical, String currency) {
        String url = buildVerticalPageUrl(getVerticalPageBaseUrl(), vertical, currency);
        logger.log("Opening vertical page via URL: " + url);
        driverTools.get(url);
        onAfterOpenVerticalPageViaURL();
    }

    protected abstract String getVerticalPageBaseUrl();

    protected abstract void onAfterOpenVerticalPageViaURL();

    protected String buildVerticalPageUrl(String baseUrl, String vertical, String currency) {
        String endpoint = resolveVerticalEndpoint(vertical);
        endpoint = appendQueryParam(endpoint, "currency", currency);
        return baseUrl + endpoint;
    }

    protected String resolveVerticalEndpoint(String vertical) {
        String verticalLowerCase = vertical.toLowerCase();
        return switch (verticalLowerCase) {
            case "ferry", "airport-transfer", "hotel", "bus-travel", "homes", "sewa-mobil", "pesawat" ->
                    verticalLowerCase;
            case "flights" -> "pesawat";
            case "trains", "train" -> "kereta-api";
            case "whoosh" -> "kereta-api?productType=whoosh";
            case "claim center" -> "insurance-claim";
            case "events", "event", "attractions" -> "to-do";
            case "insurance" -> "insurance?hideToolbar=true";
            default -> throw new NotImplementedException("Unimplemented vertical type!");
        };
    }

    protected String appendQueryParam(String path, String key, String value) {
        if (value == null || value.isBlank()) {
            return path;
        }
        return path + (path.contains("?") ? "&" : "?") + key + "=" + value;
    }

    public abstract void goToAccountPage();

    public abstract void goToSavePassengerData();

    public abstract boolean verifyVerticalIsDisplayed(String car);

    public abstract void goToMyOrderPage();

    public abstract String getCurrentCurrency();

    public abstract String getPageLanguageCode();

    public abstract void goToFlightPage();

    public abstract void selectEnglishLanguageOnly();

    public abstract String getLanguage();

    public abstract void openHelpCenter();

    public abstract void clickLanguageSelector();

    public abstract void selectBahasaLanguage();

    public abstract String getImportantInfoMessage();

    public abstract void selectEnglishLanguage();

    /**
     * Switches the web app to the supplied language only when the current language differs.
     * <p>Accepts the ISO-style short code returned by {@link #getLanguage()}:
     * {@code "en"} / {@code "english"} for English and {@code "id"} / {@code "bahasa"} for
     * Bahasa Indonesia. After switching, the new language is asserted to match the request.
     * </p>
     */
    public void selectLanguage(String language) {
        String currentLang = getLanguage();
        boolean wantId = "id".equalsIgnoreCase(language) || "bahasa".equalsIgnoreCase(language);
        String desired = wantId ? "id" : "en";
        if (!desired.equalsIgnoreCase(currentLang)) {
            if (wantId) {
                selectBahasaLanguage();
            } else {
                selectEnglishLanguage();
            }
            currentLang = getLanguage();
        }
        Assertion.assertThat("Web language should be " + desired,
                desired.equalsIgnoreCase(currentLang), is(true));
    }

    public abstract boolean isCurrencyAvailable(String currencyCode);

    public abstract int numberOfAvailableCurrency();

    public abstract List<String> getSuggestedCurrencyNamesOnConsumerSide();

    public abstract void openVerticalPage(String vertical);

    public abstract void goToWishlistPage();

    public abstract void openSettings();

    public abstract boolean isClaimCenterPageOpened();

    public abstract void goToClaimCenterPage();

    public abstract boolean openTnC();

    public abstract boolean goToVoucherBoxPage();

    public abstract boolean goToReviewPage();

    public abstract String getRandomCurrency(String currentCurrency);

    public abstract boolean openPrivacyPolicy();

    public abstract boolean isOnInsurancePage();

    public abstract void goToSection(String sectionTitleHome);

    public abstract String getCampaignProduct(String productNameTtd);

    public abstract void openInbox();

    public abstract void goToTiketb2cPage();

    public abstract void goToTiketb2cPageIfPresent();

    public abstract void clickSeeAllFlashSale();

    public abstract void closeBottomsheetPromo();
    public abstract void openHotelPDPViaURL(String s);
    public abstract void openHotelPDPViaURLWithDate(String s, String startDate, String endDate);
    public abstract void continueWithoutApp(int waitInSeconds);

    public void openFlightStatusOrderPage(String orderId) {
        {
            String baseUrl = "";
            String endpoint = String.format("pesawat/flight-status?orderId=%s", orderId);

            if (Configs.ENVIRONMENT == Environment.GK) {
                baseUrl = STAGING_WEB_URL;
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                baseUrl = PREPROD_WEB_URL;
            }else if (Configs.ENVIRONMENT == Environment.PROD) {
                baseUrl = PROD_WEB_URL;
            }

            driverTools.get(baseUrl + endpoint);
        }
    };

    public void goToYourOrderUrl() {
        {
            String baseUrl = "";
            String endpoint = "yourorder";

            if (Configs.ENVIRONMENT == Environment.GK) {
                baseUrl = STAGING_WEB_URL;
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                baseUrl = PREPROD_WEB_URL;
            }else if (Configs.ENVIRONMENT == Environment.PROD) {
                baseUrl = PROD_WEB_URL;
            }

            driverTools.get(baseUrl + endpoint);
        }
    };

    public void clickSearchColumn() {
        driverTools.waitForElementToAppear(GLOBAL_SEARCH_BAR);
        driverTools.click(GLOBAL_SEARCH_BAR);
        driverTools.waitForElementToPresence(GLOBAL_SEARCH_BAR, 4);

        boolean gsModalAppear = driverTools.isElementDisplayed(GLOBAL_SEARCH_BAR);
        Assertion.assertThat("Verify Search Modal Appear", gsModalAppear, is(true));
    }

    public boolean verifyContentGS() {
        driverTools.waitForElementToAppear(GS_INFO_TITLE);
        boolean isGSInfoTitleAppear = driverTools.isElementDisplayed(GS_INFO_TITLE);
        boolean isGSInfoIconAppear = driverTools.isElementDisplayed(GS_INFO_ICON);

        driverTools.waitForElementToAppear(GS_MS_TITLE, 5);
        driverTools.scrollIntoWebView(GS_MS_TITLE);
        boolean isGSMSTitleAppear = driverTools.isElementDisplayed(GS_MS_TITLE);
        driverTools.scrollToElement(GS_MS_IMG_PREVIEW);
        boolean isGSMSImagePreviewAppear = driverTools.isElementDisplayed(GS_MS_IMG_PREVIEW);

        driverTools.waitForElementToAppear(GS_IS_SECTION, 2);
        driverTools.scrollToElement(GS_IS_SECTION);
        boolean isGSISTitleAppear = driverTools.isElementDisplayed(GS_IS_SECTION);

        Assertion.assertThat("Verify GS Info Title Appear", isGSInfoTitleAppear, is(true));
        Assertion.assertThat("Verify GS Info Icon Appear", isGSInfoIconAppear, is(true));
        Assertion.assertThat("Verify GS MS Title Appear", isGSMSTitleAppear, is(true));
        Assertion.assertThat("Verify GS MS Image Preview Appear", isGSMSImagePreviewAppear, is(true));
        Assertion.assertThat("Verify GS IS Section Appear", isGSISTitleAppear, is(true));

        return true;
    }

    public abstract void inputVerticalKeywords();

    public abstract void verifyVerticalIconText(String keywords);

    public abstract void verticalRedirectionFromGS(String keywords);

    public abstract boolean verifyContentGSModule();

    public abstract void clickOneOfProductIcon();

    public abstract void clickDatePicker();

    public abstract boolean isDescriptionHolidayDisplay();

    public abstract void closeCalendar();

    public abstract void closeLanguageCurrencyPopup();

    public abstract void flashSaleCheck();

    public abstract void goToSavedPaymentMethod();

    public abstract void verifyCurrencyHeaderIDR();

    public abstract void verifyIWCCurrencyIDR(String moduleId);

    public abstract void verifyHDPCurrencyIDR();

    public abstract void verifyFlashSaleCurrencyIDR();

    public abstract boolean verifyContentInspired();

    public abstract boolean verifyContentPopular();

    public abstract boolean verifyContentGS_Info();

    public abstract boolean checkBannerScroll(String moduleId);

    public abstract boolean checkBannerCarouselTitleInside(String moduleId);

    public abstract boolean checkBannerCarouselSubTitleInside(String moduleId);
    public abstract void clickSeeAllBannerCarousel(String moduleId);
    public abstract boolean  bannerCarouselRedirectionCheck();
    public boolean checkURLDoestMatch() {
        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            return !driverTools.getCurrentURL().equals("https://preprod.tiket.com/");
        } else if (Configs.ENVIRONMENT == Environment.GK) {
            return !driverTools.getCurrentURL().equals("https://gk.tiket.com/");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            return !driverTools.getCurrentURL().equals("https://prod.tiket.com/");
        }
        return false;
    }

    public boolean checkURLContainsEnvironment() {
        String currentUrl = driverTools.getCurrentURL();
        if (Configs.ENVIRONMENT == Environment.PREPROD) {
            return currentUrl.contains("preprod");
        } else if (Configs.ENVIRONMENT == Environment.GK) {
            return currentUrl.contains("gk");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            return currentUrl.contains("prod");
        }
        return false;
    }

    public boolean isRedirectedToUrlContaining(String... anyOfPaths) {
        String currentUrl = driverTools.getCurrentURL();
        for (String path : anyOfPaths) {
            if (currentUrl.contains(path)) {
                return true;
            }
        }
        return false;
    }

    public String getWebBaseUrl() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            return STAGING_WEB_URL;
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            return PROD_WEB_URL;
        }
        return PREPROD_WEB_URL;
    }

    public void openWebUrlPath(String path) {
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        String url = getWebBaseUrl() + normalizedPath;
        logger.log("Opening web URL: " + url);
        driverTools.get(url);
        driverTools.waitForWebToLoad();
    }

    public boolean isPageNotFoundMessageDisplayed() {
        return driverTools.waitForElementToAppear(PAGE_NOT_FOUND_MESSAGE, 10).isPresent();
    }

    public abstract void enterVerticalName(String value);

    public abstract boolean verifyVerticalSection();

    public abstract boolean verifyArticleSection();

    @Setter
    @Getter
    Map<String, String> sheetData = new HashMap<>();

    public boolean TTDCheck() {
        driverTools.waitForElementToAppear(TTD_LP);
        return driverTools.isElementDisplayed(TTD_LP);
    }

    public void clickInfoPage() {
        driverTools.scrollElementToCenter(INFO_PAGE_ENTRY_POINT);
        driverTools.waitForElementToAppear(INFO_PAGE_ENTRY_POINT);
        driverTools.clickWithJavascriptExecutor(INFO_PAGE_ENTRY_POINT);
    }

    public abstract void clickSeeAllPromo();


    public void goback() {
        driverTools.waitForElementToAppear(PROMO_LIST);
        driverTools.back();
    }

    public String checkRecentlySearchIsNearMe() {
        WebElement webElement = driverTools.waitForElementToAppear(RECENTLY_SEARCHED).orElseThrow();
        return driverTools.getText(webElement);
    }

    public void verifyIWCModuleVisible(String id) {
        IMAGE_WITH_CHIPS_INVENTORY = new Locator("IMAGE_WITH_CHIPS_INVENTORY", By.id(id));
        driverTools.waitForElementToPresence(IMAGE_WITH_CHIPS_INVENTORY).orElseThrow();
        boolean isIWCVisible = driverTools.isElementDisplayed(IMAGE_WITH_CHIPS_INVENTORY);
        if (isIWCVisible) {
            driverTools.scrollIntoWebView(IMAGE_WITH_CHIPS_INVENTORY);
            IWCElement = driverTools.element(IMAGE_WITH_CHIPS_INVENTORY).orElseThrow();
        } else {
            Assertion.fail("IWC Module is not Found");
        }
    }

    public void verifyModuleCurrency(String moduleName, String moduleId, String currency) {
        Locator moduleSection = new Locator("MODULE_SECTION", By.id(moduleId));
        boolean isModuleSectionAppeared = driverTools.waitForElementToAppear(moduleSection).isPresent();
        if (isModuleSectionAppeared) {
            driverTools.scrollIntoWebView(moduleSection);
        } else {
            Assertion.fail("Verify " + moduleName + " Module Currency " + currency + " - module section did not appear");
        }

        Locator moduleCurrency = new Locator("MODULE_CURRENCY", By.xpath("//section[@id='" + moduleId + "']//div[contains(@class, 'PriceModule_final_price_wrapper')]//div[contains(normalize-space(.),'" + currency + "')]"));
        boolean isModuleCurrencyAppeared = driverTools.waitForElementToAppear(moduleCurrency).isPresent();
        if (!isModuleCurrencyAppeared) {
            Assertion.fail("Verify " + moduleName + " Module Currency " + currency + " did not appear");
        }

        boolean isModuleCurrencyMatch = driverTools.isElementDisplayed(moduleCurrency);
        Assertion.assertThat("Verify " + moduleName + " Module Currency " + currency, isModuleCurrencyMatch, is(true));
    }

    public boolean verifyIWCHeaderTitle() {
        WebElement titleElement = driverTools.element(IWC_HEADER, IWCElement).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    public boolean verifyIWCHeaderSubtitle() {
        WebElement subtitleElement = driverTools.element(IWC_HEADER_SUBTITLE, IWCElement).orElseThrow();
        return driverTools.isElementDisplayed(subtitleElement);
    }

    public boolean verifyChipsIWC() {
        List<WebElement> chipsElements = driverTools.elements(IWC_CHIPS, IWCElement).orElseThrow();
        List<Boolean> areChipsClikable = new ArrayList<>();
        for (WebElement chipsElement : chipsElements) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                wait.until(ExpectedConditions.elementToBeClickable(chipsElement));
                areChipsClikable.add(true);
            } catch (TimeoutException e) {
                areChipsClikable.add(false);
            }
        }
        return !areChipsClikable.contains(false);

    }

    public boolean verifyIWCInventories() {
        List<WebElement> inventories = driverTools.elements(IWC_INVENTORIES, IWCElement).orElseThrow();
        List<Boolean> areInventoriesClickable = new ArrayList<>();
        for (WebElement inventory : inventories) {
            try {
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
                wait.until((ExpectedConditions.elementToBeClickable(inventory)));
                areInventoriesClickable.add(true);
            } catch (TimeoutException e) {
                areInventoriesClickable.add(false);
            }
        }
        return !areInventoriesClickable.contains(false);
    }

    public boolean verifyIWCIMG() {
        List<WebElement> IWCImages = driverTools.elements(IWC_IMG, IWCElement).orElseThrow();
        List<Boolean> areIWCImagesVisible = new ArrayList<>();
        for (WebElement IWCImage : IWCImages) {
            areIWCImagesVisible.add(driverTools.isElementDisplayed(IWCImage));
        }
        return !areIWCImagesVisible.contains(false);
    }

    public boolean verifyIWCInventoriesName() {
        List<WebElement> IWCInventoriesName = driverTools.elements(IWC_INVENTORIES_NAME, IWCElement).orElseThrow();
        List<Boolean> areIWCInventoriesNameVisible = new ArrayList<>();
        for (WebElement IWCInventoryName : IWCInventoriesName) {
            areIWCInventoriesNameVisible.add(driverTools.isElementDisplayed(IWCInventoryName));
        }
        return !areIWCInventoriesNameVisible.contains(false);
    }

    public boolean verifyIWCPrice() {
        List<WebElement> IWCPrices = driverTools.elements(IWC_PRICE, IWCElement).orElseThrow();
        List<Boolean> areIWCPriceVisible = new ArrayList<>();
        for (WebElement IWCPrice : IWCPrices) {
            areIWCPriceVisible.add(driverTools.isElementDisplayed(IWCPrice));
        }
        return !areIWCPriceVisible.contains(false);
    }

    public boolean verifyIWCStrikePrice() {
        List<WebElement> IWCStrikePrices = driverTools.elements(IWC_STRIKE_PRICE, IWCElement).orElseThrow();
        List<Boolean> areIWCStrikePriceVisible = new ArrayList<>();
        for (WebElement IWCStrikePrice : IWCStrikePrices) {
            areIWCStrikePriceVisible.add(driverTools.isElementDisplayed(IWCStrikePrice));
        }
        return !areIWCStrikePriceVisible.contains(false);
    }

    public boolean verifyIWCSeeAllButton() {
        WebElement IWCSeeAllButton = driverTools.element(IWC_SEEALL_BUTTON, IWCElement).orElseThrow();
        List<Boolean> isIWCSeeAllButtonVisibleAndHaveUrl = new ArrayList<>();
        isIWCSeeAllButtonVisibleAndHaveUrl.add(driverTools.isElementDisplayed(IWCSeeAllButton));
        String seeAllURL = driverTools.getValueAttribute(IWCSeeAllButton, "href");
        if (seeAllURL.isEmpty()) {
            isIWCSeeAllButtonVisibleAndHaveUrl.add(false);
        } else {
            isIWCSeeAllButtonVisibleAndHaveUrl.add(true);
        }
        return !isIWCSeeAllButtonVisibleAndHaveUrl.contains(false);
    }

    public String getIWCInventoryNameAndClick() {
        List<WebElement> IWCInventoriesName = driverTools.elements(IWC_INVENTORIES_NAME, IWCElement).orElseThrow();
        String inventoryName = driverTools.getText(IWCInventoriesName.get(0));
        driverTools.click(IWCInventoriesName.get(0));
        return inventoryName;
    }

    public String getIWCPDPTitle() {
        driverTools.waitForElementToAppear(IWC_TITLE_PDP);
        return driverTools.getText(IWC_TITLE_PDP).orElseThrow();
    }

    public abstract void verifyGS_Top_Result(String keywords);

    public abstract void verifyGS_Info_Result(String keywords);

    public boolean checkBannerCarousel() {
        driverTools.waitForElementToAppear(BANNER_CAROUSEL);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL);
    }

    public boolean checkBannerCarouselTitle() {
        driverTools.waitForElementToAppear(BANNER_CAROUSEL);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL);
    }

    public boolean checkBannerCarouselSubTitle() {
        driverTools.waitForElementToAppear(BANNER_CAROUSEL_SUBTITLE);
        return driverTools.isElementDisplayed(BANNER_CAROUSEL_SUBTITLE);
    }

    public boolean checkBannerList(String moduleId) {
        driverTools.waitForElementToAppear(getBannerCarouselList(moduleId));
        return driverTools.isElementDisplayed(getBannerCarouselList(moduleId));
    }

    public void openYourOrderViaURL() {
        Deeplink deepLink = new Deeplink("YOUR_ORDER_PAGE",
                "https://gatotkaca.tiket.com/yourorder",
                "https://preprod.tiket.com/yourorder",
                "https://tiket.com/yourorder");
        driverTools.deeplink(deepLink);
    }
    public boolean VerifyContentForBannerGrid(String moduleId) {
        driverTools.waitForElementToAppear(getBannerGrid(moduleId));
        driverTools.scrollToElement(getBannerGrid(moduleId));
        driverTools.waitForElementToAppear(getBannerGridMainText(moduleId), 10);

        boolean isBannerGridTitle = driverTools.isElementDisplayed(getBannerGridTitle(moduleId));
        boolean isBannerMainTextVisible = driverTools.isElementDisplayed(getBannerGridMainText(moduleId));
        boolean isBannerBelowTextVisible = driverTools.isElementDisplayed(getBannerGridBelowText(moduleId));

        List<WebElement> gridElements = driverTools.elements(getBannerGridLists(moduleId)).orElseThrow();
        int gridCount = gridElements.size();
        boolean isGridCountCorrect = gridCount > 1;

        Assertion.assertThat("Verify BANNER_GRID_MAIN_TEXT is visible", isBannerMainTextVisible, is(true));
        Assertion.assertThat("Verify Banner_Grid_Title is visible", isBannerGridTitle, is(true));
        Assertion.assertThat("Verify BANNER_GRID_BELOW_TEXT is visible", isBannerBelowTextVisible, is(true));
        Assertion.assertThat("Verify more than 1 grid is present", gridCount, greaterThan(1));

        if (gridCount > 0) {
            WebElement firstGridElement = gridElements.get(0);
            driverTools.waitForElementToBeClickable(firstGridElement, 10);
            driverTools.click(firstGridElement);
        }

        return isBannerMainTextVisible &&
                isBannerGridTitle &&
                isBannerBelowTextVisible &&
                isGridCountCorrect;
    }

    public void clickAnotherIWCChips(){
        List<WebElement> chipsElement = driverTools.elements(IWC_CHIPS, IWCElement).orElseThrow();
        if (chipsElement.size() > 1) {
            driverTools.click(chipsElement.get(1));
            driverTools.waitForElementToAppear(IWC_ANOTHER_LOCATOR, 3);
        } else {
            Assertion.skip("Chips not found");
        }
    }

    public void verifyBannerCarouselModuleVisible(String moduleId) {
        BANNER_CAROUSEL_V2_INVENTORY = new Locator("BANNER_CAROUSEL_V2_INVENTORY", By.id(moduleId));
        driverTools.waitForElementToPresence(BANNER_CAROUSEL_V2_INVENTORY).orElseThrow();
        boolean isBannerCarouselVisible = driverTools.isElementDisplayed(BANNER_CAROUSEL_V2_INVENTORY);
        if (isBannerCarouselVisible) {
            driverTools.scrollIntoWebView(BANNER_CAROUSEL_V2_INVENTORY);
        } else {
            Assertion.fail("Banner Carousel Module is not Found");
        }
    }

    public boolean verifyBannerCarouselHeaderTitle(String moduleId) {
        WebElement titleElement = driverTools.element(getBannerCarouselV2Title(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    public boolean verifyBannerCarouselHeaderSubtitle(String moduleId) {
        WebElement subtitleElement = driverTools.element(getBannerCarouselV2Subtitle(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(subtitleElement);
    }

    public boolean verifyChipsBannerCarousel(String moduleId) {
        List<WebElement> chipsElements = driverTools.elements(getBannerCarouselV2Chips(moduleId)).orElseThrow();
        List<Boolean> areChipsClickable = new ArrayList<>();
        for (WebElement chipsElement : chipsElements) {
            try {
                driverTools.waitForElementToBeClickable(getBannerCarouselV2Chips(moduleId));
                areChipsClickable.add(true);
            } catch (TimeoutException e) {
                areChipsClickable.add(false);
            }
        }
        return !areChipsClickable.contains(false);
    }

    public boolean verifyBannerCarouselList(String moduleId) {
        List<WebElement> bannerCarouselCard = driverTools.elements(getBannerCarouselV2Card(moduleId)).orElseThrow();
        List<Boolean> areBannerCarouselCardClickable = new ArrayList<>();
        for (WebElement bannerCard : bannerCarouselCard) {
            try {
                driverTools.waitForElementToBeClickable(getBannerCarouselV2Card(moduleId));
                areBannerCarouselCardClickable.add(true);
            } catch (TimeoutException e) {
                areBannerCarouselCardClickable.add(false);
            }
        }
        return !areBannerCarouselCardClickable.contains(false);
    }

    public boolean verifyBannerCarouselImage(String moduleId) {
        List<WebElement> bannerCarouselImage = driverTools.elements(getBannerCarouselV2Image(moduleId)).orElseThrow(); // IWC IMG is reusable
        List<Boolean> areBannerCarouselImagesVisible = new ArrayList<>();
        for (WebElement bannerImage : bannerCarouselImage) {
            areBannerCarouselImagesVisible.add(driverTools.isElementDisplayed(bannerImage));
        }
        return !areBannerCarouselImagesVisible.contains(false);
    }

    public boolean verifyBannerCarouselTitleCards(String moduleId) {
        List<WebElement> bannerCarouselTitle = driverTools.elements(getBannerCarouselV2CardTitle(moduleId)).orElseThrow();
        List<Boolean> areBannerCarouselTitleVisible = new ArrayList<>();
        for (WebElement bannerCardTitle : bannerCarouselTitle) {
            areBannerCarouselTitleVisible.add(driverTools.isElementDisplayed(bannerCardTitle));
        }
        return !areBannerCarouselTitleVisible.contains(false);
    }

    public boolean verifyBannerCarouselSubtitleCards(String moduleId) {
        List<WebElement> bannerCarouselSubtitle = driverTools.elements(getBannerCarouselV2CardSubtitle(moduleId)).orElseThrow();
        List<Boolean> areBannerCarouselSubtitleVisible = new ArrayList<>();
        for (WebElement bannerCardSubtitle : bannerCarouselSubtitle) {
            areBannerCarouselSubtitleVisible.add(driverTools.isElementDisplayed(bannerCardSubtitle));
        }
        return !areBannerCarouselSubtitleVisible.contains(false);
    }

    public boolean verifyBannerCarouselV2SeeAllButton(String moduleId) {
        WebElement BannerCarouselV2SeeAllButton = driverTools.element(getBannerCarouselV2SeeAllButton(moduleId)).orElseThrow(); // IWC See All Button is reusable
        List<Boolean> isBannerCarouselSeeAllButtonVisibleAndHaveUrl = new ArrayList<>();
        isBannerCarouselSeeAllButtonVisibleAndHaveUrl.add(driverTools.isElementDisplayed(BannerCarouselV2SeeAllButton));
        String seeAllURL = driverTools.getValueAttribute(BannerCarouselV2SeeAllButton, "href");
        if (seeAllURL.isEmpty()) {
            isBannerCarouselSeeAllButtonVisibleAndHaveUrl.add(false);
        } else {
            isBannerCarouselSeeAllButtonVisibleAndHaveUrl.add(true);
        }
        return !isBannerCarouselSeeAllButtonVisibleAndHaveUrl.contains(false);
    }

    public void clickAnotherBCChips(String moduleId){
        List<WebElement> chipsElement = driverTools.elements(getBannerCarouselV2Chips(moduleId)).orElseThrow();
        if(!chipsElement.isEmpty() && chipsElement.size() != 1){
            driverTools.clickOrFallbackToClickWithJavascriptExecutor(getBannerCarouselV2Chips(moduleId, 2));
            driverTools.waitForElementToAppear(BANNER_CAROUSEL_ANOTHER_CHIPS, 3);
        } else {
            Assertion.skip("Chips not found");
        }
    }

    public String getBannerCarouselURLAndClick(String moduleId) {
        List<WebElement> BannerCarouselURL = driverTools.elements(getBannerCarouselV2URL(moduleId)).orElseThrow();
        String urlFull = BannerCarouselURL.get(0).getAttribute("href");
        logger.log("URL on the banner : " + urlFull);
        driverTools.click(BannerCarouselURL.get(0));
        return urlFull;
    }

    public String bannerCarouselRedirectUrl(){
        driverTools.waitForWebToLoad();
        String redirectUrl = CoreFunctions.normalizeUrl(driverTools.getCurrentURL());
        logger.log("URL after redirect : " + redirectUrl);
        return redirectUrl;
    }

    public void waitForWebPageToLoad(){
        driverTools.waitForWebToLoad();
    }

    public void verifyBannerGridModuleVisible(String moduleId){
        BANNER_GRID_INVENTORY = new Locator("BANNER_GRID_INVENTORY", By.xpath(String.format("//section[contains(@class,'Layout_section') and @id='%s']", moduleId)));
        driverTools.waitForElementToPresence(BANNER_GRID_INVENTORY).orElseThrow();
        boolean isBannerGridVisible = driverTools.isElementDisplayed(BANNER_GRID_INVENTORY);
        if (isBannerGridVisible){
            driverTools.scrollIntoWebView(BANNER_GRID_INVENTORY);
        } else {
            Assertion.fail("Banner Grid Module is Not Found");
        }
    }

    public boolean verifyBannerGridTitleHeader(String moduleId) {
        WebElement bannerTitle = driverTools.element(getBannerGridTitleHeader(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(bannerTitle);
    }

    public boolean verifyBannerGridSubtitleHeader(String moduleId){
        WebElement bannerSubtitle = driverTools.element(getBannerGridSubtitleHeader(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(bannerSubtitle);
    }

    public boolean verifyBannerGridChips(String moduleId){
        List<WebElement> bannerGridChips = driverTools.elements(getBannerGridChips(moduleId)).orElseThrow();
        List<Boolean> areChipsClikable = new ArrayList<>();
        for (WebElement bannerGridChip : bannerGridChips){
            try {
                driverTools.waitForElementToBeClickable(getBannerGridChips(moduleId));
                areChipsClikable.add(true);
            } catch (TimeoutException e){
                areChipsClikable.add(false);
            }
        }
        return !areChipsClikable.contains(false);
    }

    public boolean verifyBannerGridList(String moduleId){
        List<WebElement> bannerGridLists = driverTools.elements(getBannerGridList(moduleId)).orElseThrow();
        List<Boolean> areBannerCardVisible = new ArrayList<>();
        for (WebElement bannerGridList : bannerGridLists){
            try {
                driverTools.waitForElementToBeClickable(getBannerGridList(moduleId));
                areBannerCardVisible.add(true);
            } catch (TimeoutException e){
                areBannerCardVisible.add(false);
            }
        }
        return !areBannerCardVisible.contains(false);
    }

    public boolean verifyBannerGridCardsTitle(String moduleId) {
        List<WebElement> bannerGridTitleCard = driverTools.elements(getBannerGridCardTitle(moduleId)).orElseThrow();
        List<Boolean> areBannerGridTitleVisible = new ArrayList<>();
        for (WebElement bannerCardTitle : bannerGridTitleCard) {
            areBannerGridTitleVisible.add(driverTools.isElementDisplayed(bannerCardTitle));
        }
        return !areBannerGridTitleVisible.contains(false);
    }

    public boolean verifyBannerGridCardsSubtitle(String moduleId) {
        List<WebElement> bannerGridSubtitle = driverTools.elements(getBannerGridCardSubtitle(moduleId)).orElseThrow();
        List<Boolean> areBannerGridSubtitleVisible = new ArrayList<>();
        for (WebElement bannerCardSubtitle : bannerGridSubtitle) {
            areBannerGridSubtitleVisible.add(driverTools.isElementDisplayed(bannerCardSubtitle));
        }
        return !areBannerGridSubtitleVisible.contains(false);
    }

    public boolean verifyBannerGridSeeAllButton(String moduleId) {
        WebElement BannerGridSeeAllButton = driverTools.element(getBannerGridSeeAllButton(moduleId)).orElseThrow();
        List<Boolean> isBannerGridSeeAllButtonVisibleAndHaveUrl = new ArrayList<>();
        isBannerGridSeeAllButtonVisibleAndHaveUrl.add(driverTools.isElementDisplayed(BannerGridSeeAllButton));
        String seeAllURL = driverTools.getValueAttribute(BannerGridSeeAllButton, "href");
        if (seeAllURL.isEmpty()) {
            isBannerGridSeeAllButtonVisibleAndHaveUrl.add(false);
        } else {
            isBannerGridSeeAllButtonVisibleAndHaveUrl.add(true);
        }
        return !isBannerGridSeeAllButtonVisibleAndHaveUrl.contains(false);
    }

    public void clickAnotherBannerGridChips(String moduleId){
        List<WebElement> chipsElement = driverTools.elements(getBannerGridChips(moduleId)).orElseThrow();
        if(!chipsElement.isEmpty() && chipsElement.size() != 1){
            driverTools.click(chipsElement.get(1));
            driverTools.waitForElementToAppear(BANNER_GRID_ANOTHER_CHIPS, 3);
        } else {
            Assertion.skip("Chips not found");
        }
    }

    public String getBannerGridURLAndClick(String moduleId) {
        List<WebElement> BannerGridURL = driverTools.elements(getBannerGridV2URL(moduleId)).orElseThrow();
        String urlFull = BannerGridURL.get(0).getAttribute("href");
        logger.log("URL on the banner : " + urlFull);
        driverTools.click(BannerGridURL.get(0));
        return CoreFunctions.normalizeUrl(urlFull);
    }

    public void verifyHDPVisible(String moduleId) {
        HDP_INVENTORY = new Locator("HDP_INVENTORY", By.id(moduleId));
        driverTools.waitForElementToPresence(HDP_INVENTORY).orElseThrow();
        boolean isBannerCarouselVisible = driverTools.isElementDisplayed(HDP_INVENTORY);
        if (isBannerCarouselVisible) {
            driverTools.scrollIntoWebView(HDP_INVENTORY);
        } else {
            Assertion.fail("Highlight Discovery Product is Not Found");
        }
    }

    public boolean verifyHDPHeaderLabel(String moduleId) {
        WebElement labelElement = driverTools.element(getHDPLabel(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(labelElement);
    }

    public boolean verifyHDPHeaderTitle(String moduleId) {
        WebElement titleElement = driverTools.element(getHDPTitle(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    public boolean verifyHDPHeaderSubtitle(String moduleId) {
        WebElement titleElement = driverTools.element(getHDPSubtitle(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    public abstract boolean verifyHDPProductImage(String moduleId);

    public boolean verifyHDPInventories(String moduleId){
        List<WebElement> HDPCard = driverTools.elements(getHDPInventory(moduleId)).orElseThrow();
        List<Boolean> areHDPCardClickable = new ArrayList<>();
        for (WebElement bannerCard : HDPCard){
            try {
                driverTools.waitForElementToBeClickable(getHDPInventory(moduleId));
                areHDPCardClickable.add(true);
            } catch (TimeoutException e){
                areHDPCardClickable.add(false);
            }
        }
        return !areHDPCardClickable.contains(false);
    }

    public abstract boolean verifyHDPInventoryName(String moduleId);

    public abstract boolean verifyHDPInventoryLocation(String moduleId);

    public abstract boolean verifyHDPPrice(String moduleId);

    public abstract boolean verifyHDPStrikePrice(String moduleId);

    public abstract String getHDPInventoryNameAndClick(String moduleId);

    public String getHDPPDPTitle() {
        driverTools.waitForElementToAppear(HDP_TITLE_PDP);
        return driverTools.getText(HDP_TITLE_PDP).orElseThrow();
    }

    public void clickRegisterNow() {
        driverTools.waitForElementToAppear(REGISTER_NOW) ;
        driverTools.clickWithJavascriptExecutor(REGISTER_NOW);
    }


    public void verifyGVSectionVisible(String moduleId) {
        GV_SECTION = new Locator("GV_SECTION", By.id(moduleId));
        driverTools.waitForElementToPresence(GV_SECTION).orElseThrow();
        boolean isGVSectionVisible = driverTools.isElementDisplayed(GV_SECTION);
        if (isGVSectionVisible) {
            driverTools.scrollIntoWebView(GV_SECTION);
        } else {
            Assertion.fail("GV Section is not appearing");
        }
    }

    public boolean verifyGVSectionHeaderTitle(String moduleId) {
        WebElement titleElement = driverTools.element(getGVSectionTitleHeader(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(titleElement);
    }

    public boolean verifyGVSectionHeaderSubtitle(String moduleId) {
        WebElement subtitleElement = driverTools.element(getGVSectionSubtitleHeader(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(subtitleElement);
    }

    public boolean verifyGVIcon(String moduleId){
        WebElement iconElement = driverTools.element(getGVSectionIcon(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(iconElement);
    }

    public boolean verifyGVCardAppearing(String moduleId){
        WebElement gvCardElement = driverTools.element(getGVSectionVoucherCard(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(gvCardElement);
    }

    public boolean verifyGVSectionSeeAllButton(String moduleId) {
        WebElement gvSectionSeeAllButton = driverTools.element(getGVSectionSeeAllButton(moduleId)).orElseThrow();
        List<Boolean> isGVSectionSeeAllButtonVisibleAndHaveUrl = new ArrayList<>();
        isGVSectionSeeAllButtonVisibleAndHaveUrl.add(driverTools.isElementDisplayed(gvSectionSeeAllButton));
        String seeAllURL = driverTools.getValueAttribute(gvSectionSeeAllButton, "href");
        if (seeAllURL.isEmpty()) {
            isGVSectionSeeAllButtonVisibleAndHaveUrl.add(false);
        } else {
            isGVSectionSeeAllButtonVisibleAndHaveUrl.add(true);
        }
        return !isGVSectionSeeAllButtonVisibleAndHaveUrl.contains(false);
    }

    public boolean verifyGVCardVertical(String moduleId){
        WebElement gvCardVerticalElement = driverTools.element(getGVSectionVertical(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(gvCardVerticalElement);
    }

    public boolean verifyGVCardExpiryDate(String moduleId){
        WebElement gvCardExpiryDateElement = driverTools.element(getGVSectionExpiryData(moduleId)).orElseThrow();
        return driverTools.isElementDisplayed(gvCardExpiryDateElement);
    }

    public boolean verifyGVCardClaimable(String moduleId) {
        List<WebElement> gvCardClaimable = driverTools.elements(getGVSectionClaimButton(moduleId)).orElseThrow();
        List<Boolean> aregvCardClaimable = new ArrayList<>();
        for (WebElement bannerCard : gvCardClaimable) {
            try {
                driverTools.waitForElementToBeClickable(getBannerCarouselV2Card(moduleId));
                aregvCardClaimable.add(true);
            } catch (TimeoutException e) {
                aregvCardClaimable.add(false);
            }
        }
        return !aregvCardClaimable.contains(false);
    }
    public void clickInsuranceIcon() {
        driverTools.waitForWebToLoad();
        for (int attempt = 0; attempt < 3 && !driverTools.isElementDisplayed(INSURANCE_HOMEPAGE); attempt++) {
            if (driverTools.waitForElementToBeClickable(VERTICAL_SCROLL_RIGHT_BUTTON).isPresent()) {
                driverTools.clickWithJavascriptExecutor(VERTICAL_SCROLL_RIGHT_BUTTON);
                driverTools.waitForWebToLoad();
            }
        }
        if (!driverTools.isElementDisplayed(INSURANCE_HOMEPAGE)) {
            Assertion.fail("Insurance vertical icon is not visible after scrolling the header product strip. "
                    + "Either the scroll-right control was absent, or the insurance vertical is not in the strip.");
        }
        driverTools.waitForElementToBeClickable(INSURANCE_HOMEPAGE);
        if (Configs.PLATFORM == Platform.IMWEB) {
            driverTools.click(INSURANCE_HOMEPAGE);
        } else {
            driverTools.clickWithJavascriptExecutor(INSURANCE_HOMEPAGE);
        }
    }
    public boolean checkInsuranceDetailPage() {
        driverTools.waitForElementToAppear(INSURANCE_PAGE);
        return driverTools.isElementDisplayed(INSURANCE_PAGE);
    }
    public void clickInsuranceLogin() {
        driverTools.waitForElementToBeClickable(INSURANCE_PAGE_LOGIN_BUTTON);
        driverTools.click(INSURANCE_PAGE_LOGIN_BUTTON);
    }

    public boolean loginPageOpened(){
        driverTools.waitForElementToAppear(LOGIN_ICON);
        return driverTools.isElementDisplayed(LOGIN_ICON);
    }

    public void clickOnEmailButton()
    {
        driverTools.waitForElementToAppear(EMAIL_LOGIN);
        driverTools.clickWithJavascriptExecutor(EMAIL_LOGIN);
    }

    public boolean InsuranceloginPageOpened(){
        driverTools.waitForElementToAppear(UPCOMING_OR_EMPTY_BOOKING);
        return driverTools.isElementDisplayed(UPCOMING_OR_EMPTY_BOOKING);
    }

    public abstract void clickPromoListHeaderEntrypoint();

    public void selectCurrencyLanguageAndCountry(String country, String currency, String language) {
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();

        driverTools.waitForElementToAppear(COUNTRY_CURRENCY_ENTRY_POINT);
        driverTools.waitForElementToBeClickable(COUNTRY_CURRENCY_ENTRY_POINT);
        driverTools.clickWithJavascriptExecutor(COUNTRY_CURRENCY_ENTRY_POINT);

        driverTools.waitForElementToAppear(getLanguageOnModal(language));
        driverTools.clickWithJavascriptExecutor(getLanguageOnModal(language));

        driverTools.waitForElementToAppear(getCurrencyModal(currency));
        driverTools.scrollIntoWebView(getCurrencyModal(currency));
        driverTools.clickWithJavascriptExecutor(getCurrencyModal(currency));

        driverTools.waitForElementToAppear(COUNTRY_SELECTION_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(COUNTRY_SELECTION_DROPDOWN);
        driverTools.waitForElementToAppear(getCountryModal(country));
        driverTools.clickWithJavascriptExecutor(getCountryModal(country));

        driverTools.waitForElementToAppear(SAVE_BUTTON_ON_MODAL);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON_ON_MODAL);

        driverTools.waitForWebToLoad();
        driverTools.waitForElementToAppear(COUNTRY_CURRENCY_ENTRY_POINT);
    }

    public void changeCountryToIndonesia() {
          if (isRedirectedToUrlContaining("/en-id", "/id-id")) {
            logger.log("Country is already Indonesia, skipping country change.");
            return;
          }

        driverTools.waitForElementToAppear(LCC_ICON);
        driverTools.clickWithJavascriptExecutor(LCC_ICON);

        driverTools.waitForElementToAppear(COUNTRY_MODAL_LABEL);
        Assertion.assertThat("Verify Country Modal Appear", driverTools.isElementDisplayed(COUNTRY_MODAL_LABEL), is(true));

        driverTools.waitForElementToAppear(COUNTRY_DROPDOWN_ICON);
        driverTools.click(COUNTRY_DROPDOWN_ICON);

        driverTools.waitForElementToAppear(COUNTRY_INDONESIA_OPTION);
        driverTools.clickWithJavascriptExecutor(COUNTRY_INDONESIA_OPTION);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON_LCC);

        driverTools.waitForElementToAppear(LCC_ICON, 2);
        Assertion.assertThat("Verify URL contains /en-id or /id-id after selecting Indonesia",
                isRedirectedToUrlContaining("/en-id", "/id-id"), is(true));
    }

    public void closeModalPopUpIfAppears(){
        driverTools.waitForElementToAppear(PROMOTION_MODAL_POPUP, 10)
                .ifPresent(modalPopUp -> driverTools.clickWithJavascriptExecutor(PROMOTION_MODAL_POPUP_CLOSE_BUTTON));
    }

    public void clickInstallAppBanner() {
        driverTools.waitForElementToAppear(INSTALL_APP_BANNER, 30).orElseThrow();
        driverTools.waitForElementToAppear(INSTALL_APP_BANNER_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(INSTALL_APP_BANNER_BUTTON);
    }

    public String getInstallAppBannerText() {
        driverTools.waitForElementToAppear(INSTALL_APP_BANNER, 30).orElseThrow();
        return driverTools.getText(INSTALL_APP_BANNER).orElse("");
    }

    public boolean isQrPopupDisplayed() {
        return driverTools.waitForElementToAppear(QR_POPUP, 10).isPresent()
                && driverTools.waitForElementToAppear(QR_POPUP_SCANNER_IMAGE, 10).isPresent();
    }

    public String getQrPopupTitle() {
        driverTools.waitForElementToAppear(QR_POPUP_TITLE, 10).orElseThrow();
        return driverTools.getText(QR_POPUP_TITLE).orElse("");
    }

    public void closeQrPopup() {
        driverTools.waitForElementToBeClickable(QR_POPUP_CLOSE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(QR_POPUP_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(QR_POPUP, 10);
    }

    public void scrollLittleToShowInstallBanner() {
        if (!driverTools.waitForElementToDisappear(SAVE_BUTTON_ON_MODAL, 10)) {
            throw new AssertionError("Language/currency modal did not close; cannot continue to Install App banner");
        }
        driverTools.waitForWebToLoad();
        driverTools.scrollDown(400);
        driverTools.waitForElementToAppear(INSTALL_APP_BANNER_BUTTON, 30).orElseThrow();
    }

    public String getCurrentUrl(){
        String currentURL = driver.getCurrentUrl();
        return currentURL;
    }

    public boolean checkOrderConfirmed(){
        return driverTools.waitForElementToAppear(ORDER_STATUS_HOMEPAGE, 20).isPresent();
    }
    public void closeFirstTransactionPromoPopupIfAppears() {
        driverTools.waitForElementToAppear(FIRST_TRANSACTION_PROMO_CONTINUE_BUTTON, 10)
                .ifPresent(promoPopup -> driverTools.clickWithJavascriptExecutor(FIRST_TRANSACTION_PROMO_CONTINUE_BUTTON));
    }

    public void scrollDownToShowHomeAppInstallBanner() {
        closeFirstTransactionPromoPopupIfAppears();
        driverTools.waitForElementToDisappear(SAVE_BUTTON_ON_MODAL, 10);
        driverTools.waitForWebToLoad();
        driverTools.scrollDown(400);
        driverTools.waitForElementToAppear(HOME_APP_INSTALL_BANNER_IMAGE, 30).orElseThrow();
    }

    public void clickHomeAppInstallBannerImage() {
        driverTools.waitForElementToBeClickable(HOME_APP_INSTALL_BANNER_IMAGE, 30).orElseThrow();
        driverTools.clickWithJavascriptExecutor(HOME_APP_INSTALL_BANNER_IMAGE);
    }

    public boolean isQrCodePopupModalDisplayed() {
        return driverTools.waitForElementToAppear(QR_CODE_POPUP_MODAL, 10).isPresent();
    }

    public void closeQrCodePopupModal() {
        driverTools.waitForElementToBeClickable(QR_CODE_POPUP_CLOSE_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(QR_CODE_POPUP_CLOSE_BUTTON);
        driverTools.waitForElementToDisappear(QR_CODE_POPUP_MODAL, 10);
    }

    public void clickQrCodePopupCopyButton() {
        driverTools.waitForElementToBeClickable(QR_CODE_POPUP_COPY_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(QR_CODE_POPUP_COPY_BUTTON);
    }

    public String getQrCodePopupStoreLinkUrl(String storeLabel) {
        Locator storeButton = getQrCodePopupStoreButton(storeLabel);
        driverTools.waitForElementToAppear(storeButton, 10).orElseThrow();
        return driverTools.getValueAttribute(storeButton, "href");
    }

    public void clickQrCodePopupStoreButtonAndRestoreTab(String storeLabel) {
        Locator storeButton = getQrCodePopupStoreButton(storeLabel);
        driverTools.waitForElementToBeClickable(storeButton, 10).orElseThrow();
        String originalHandle = driverTools.getDriver().getWindowHandle();
        int handlesBefore = driverTools.getWindowHandles().size();
        driverTools.clickWithJavascriptExecutor(storeButton);
        // Store badges may open in a new tab (target="_blank") or navigate the current tab away from tiket.com;
        // restore focus/state either way so the session pool hands back a usable tab to the next test.
        driverTools.waitForNewTabAndSwitchToLatest(handlesBefore);
        if (driverTools.getWindowHandles().size() > handlesBefore) {
            driverTools.getDriver().close();
            driverTools.switchToWindow(originalHandle);
        } else if (!driverTools.getDriver().getCurrentUrl().contains("tiket.com")) {
            goToHomePage();
        }
    }

}
