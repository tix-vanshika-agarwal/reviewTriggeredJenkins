package com.tiket.web.page.ttd.TtdProductDetailPage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.*;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPage;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPageAndroid;
import com.tiket.page.ttd.searchresultpage.TTDSearchResultPageIOS;
import com.tiket.page.ttd.selectedpackagedetailpage.TTDSelectedPackageDetailPageAndroid;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.common.LoginB2CInDWEB;
import com.tiket.web.page.ttd.TtdSearchResultPage.TtdSearchResultPageWeb;
import com.tiket.web.page.ttd.TtdSelectedPackageDetailPage.TtdSelectedPackageDetailPageWeb;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import javax.security.auth.login.FailedLoginException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.IntStream;

import static com.tiket.core.CoreFunctions.generateRandomInt;
import static com.tiket.core.CoreFunctions.monthEngToId;
import static com.tiket.page.payment.PaymentPageAndroid.LOADING_STATE;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public abstract class TtdProductDetailPageWeb<T extends WebDriver> extends WebPage<T> {

    public TtdProductDetailPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TtdProductDetailPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new TtdProductDetailPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new TtdProductDetailPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator SELECT_TICKET_BUTTON_REFUND = new Locator("SELECT_TICKET_BUTTON_REFUND", new By.ByXPath("//*[contains(@class,'RefundTierLabel_feature_with_dots')]//*[contains(text(),'100% Refund')]//following::*[contains(@class,'Button_variant_primary')][1]"));
    private static final Locator TOTAL_TICKET = new Locator("TOTAL_TICKET", AppiumBy.xpath("//input[contains(@class,'QuantityEditor_quantity_input')]"));
    private static final Locator INCREMENT_BUTTON = new Locator("INCREMENT_BUTTON", new By.ByXPath("(//button[contains(@class,'operation') and ./*[contains(@class,'plus')]])"));
    private static final Locator BOOK_BUTTON = new Locator("BOOK_BUTTON", new By.ByXPath("//*[contains(@class,'totalpayment_wrapper')]//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator PACKAGES_TAB = new Locator("PACKAGE_TAB", new By.ByXPath("//*[contains(@class,'Tabs_tab_list')]//*[text()='Packages' or text()='Paket']"));
    private static final Locator VISIT_DATE = new Locator("VISIT_DATE", new By.ByXPath("//*[contains(@class,'DateFilter_date_card__') and not(contains(@class,'active'))]//span[last()]"));
    private static final Locator SELECT_SESSION_BUTTON = new Locator("SELECT_SESSION_BUTTON", AppiumBy.xpath("//button[contains(@class,'ScheduleSession_selector')]"));
    public static final Locator PRODUCT_TITLE = new Locator("PRODUCT_TITLE", new By.ByXPath("//*[@data-testid='seo-title'] | //div[contains(@class,'ATFSection_info_section')]//h1[contains(@class,'Text_size_h2')]"));
    private static final Locator REFUND_100P = new Locator("REFUND_100P", new By.ByXPath("//*[contains(@class,'RefundTierLabel_feature_with_dots')]//*[contains(text(),'100% Refund')]"));
    private static final Locator BUNDLE_RECOMMENDATION = new Locator("BUNDLE_RECOMMENDATION", new By.ByXPath("//div[contains(@class,'PackageDetailBundleSection_carousel')]"));
    private static final Locator BUNDLE_TITLE = new Locator("BUNDLE_TITLE", new By.ByXPath("//div[contains(@class,'PackageCollapsibleSection_title__')]//span[contains(text(),'Bundle Pack')]"));
    private static final Locator PARTNER_FROM = new Locator("PARTNER_FROM", new By.ByXPath("//span[contains(text(), 'Product from') or contains(text(), 'Produk dari')]"));
    private static final Locator REVIEW_BUTTON = new Locator("REVIEW_BUTTON", new By.ByXPath("//span[contains(@class,'Tabs_tab_text') and (text()='Reviews' or text()='Review')]"));
    private static final Locator FIRST_REVIEW_TAB = new Locator("FIRST_REVIEW_TAB", new By.ByXPath("(//div[@data-testid='review-card'])[1]\n"));
    private static final Locator MASTER_CAMPAIGN_LABEL = new Locator("MASTER_CAMPAIGN_LABEL", new By.ByXPath("//*[contains(@class,'BenefitSection_card_wrapper_variant')]//div[contains(@class,'Label_truncation_lines')]"));
    private static final Locator CHILD_CAMPAIGN_LABEL = new Locator("CHILD_CAMPAIGN_LABEL", new By.ByXPath("//*[contains(@class,'PackageCard_package_info')]//div[contains(@class,'Label_truncation_lines')]"));
    private static final Locator PACKAGE_SECTION = new Locator("PACKAGE_SECTION", AppiumBy.xpath("//div[contains(@class, 'PackageGroupSelection_wrapper')]\n"));
    private static final Locator TIME_SLOTS = new Locator("TIME_SLOTS", AppiumBy.xpath("//*[contains(@class,'TimeSlot_time_wrapper')]/button/child::*"));
    private static final Locator PRICE_CHANGE_POPUP = new Locator("PRICE_CHANGE_POPUP", AppiumBy.xpath("//*[contains(@class,'PopupAppsOnlyCampaign_button_group')]//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator BUYXGETY_SECTION = new Locator("BUYXGETY_SECTION", AppiumBy.xpath("//*[contains(@class,'BuyXGetYBanner_content')]//span"));
    private static final Locator SEE_ALL_DATES = new Locator("SEE_ALL_DATES", AppiumBy.xpath("//*[contains(@class,'PriceTierCard_btn_see_all')]"));
    private static final Locator SEE_PACKAGES = new Locator("SEE_PACKAGES", new By.ByXPath("(//*[contains(@class, 'Button_truncation_not_applied') and (text()='See Packages' or text()='Lihat Paket' or text()='Buy ticket now' or text()='Beli tiket sekarang') ])[1]"));
    private static final Locator TIME_SLOT = new Locator("TIME_SLOT", new By.ByXPath("(//button[contains(@class,'Chip_chip')])[5]"));
    private static final Locator LPG_LABEL_PDP = new Locator("LPG_LABEL_PDP", new By.ByXPath("//div[contains(@class, 'Label_label') and (contains(text(), 'Lowest Price Guarantee') or contains(text(), 'Jaminan Harga Termurah'))]\n"));
    private static final Locator PDP_LOGIN_MESSAGE = new Locator("PDP_ERROR_MESSAGE", new By.ByXPath("//div[contains(@class, 'ActionBanner_elevation')]"));
    private static final Locator LOGIN_BUTTON = new Locator("LOGIN_BUTTON", By.xpath("//a[contains(@class, 'Button_variant_secondary') and (normalize-space()='Masuk' or normalize-space()='Log in')]\n"));
    private static final Locator CHECK_AVAILABLE_DATES = new Locator("CHECK_AVAILABLE_DATES", By.xpath("//span[contains(@class, 'PriceTierCard_btn_see_all') and (text()='Cek tanggal tersedia' or text()='Check available dates')]\n"));
    private static final Locator OPEN_CALENDER = new Locator("OPEN_CALENDER", By.xpath("//*[contains(@data-testid,'package-selection-date-filter-other-chip')]"));
    private static final Locator CLOSE_DATE = new Locator("CLOSE_DATE", By.xpath("//button[contains(@class, 'Modal_modal_close')]"));
    private static final Locator VISIT_DATE_LOYALTY = new Locator("VISIT_DATE_LOYALTY", By.xpath("(//div[@class='Text_text'])[2]"));
    private static final Locator FIRST_LOCATION = new Locator("FIRST_LOCATION", new By.ByXPath("(//div[@data-testid='product-card']//span[contains(@class,'product-info_info_text')]//span[contains(@style,'capitalize')])[1]"));
    private static final Locator FIRST_LOCATION_DISTANCE = new Locator("FIRST_LOCATION_DISTANCE", new By.ByXPath("(//div[@data-testid='product-card']//span[contains(@class,'product-info_info_text')]//span[contains(@style,'capitalize')])[1]//following-sibling::span[contains(@class,'Text_text') and contains(text(),'m+')]"));
    private static final Locator NEAREST_BUTTON = new Locator("NEAREST_BUTTON", new By.ByXPath("//span[contains(@class, 'Chip_text') and contains(@class, 'Text_text') and (text()='Nearest' or text()='Terdekat')]"));
    private static final Locator INSTANT_CONFIRMATION_PARTNER_PAGE = new Locator("INSTANT_CONFIRMATION_PARTNER_PAGE", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size') and contains(@class, 'Text_weight_bold') and text()='Instant Confirmation']"));
    private static final Locator TIKET_FLEXI_PARTNER_PAGE = new Locator("TIKET_FLEXI_PARTNER_PAGE", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size') and contains(@class, 'Text_weight_bold') and text()='tiket FLEXI']"));
    private static final Locator INSTANT_PASS_PARTNER_PAGE = new Locator("INSTANT_PASS_PARTNER_PAGE", new By.ByXPath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size') and contains(@class, 'Text_weight_bold') and text()='Instant Pass']"));
    private static final Locator BREADCRUMB_NAVBAR = new Locator("BREADCRUMB_NAVBAR", new By.ByXPath("//div[contains(@class, 'Breadcrumb_breadcrumb')]//a[contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size')]"));
    public static final Locator BUY_TICKET_NOW = new Locator("BUY_TICKET_NOW", By.xpath("//button[.//span[normalize-space()='Buy ticket now' or normalize-space()='Beli tiket sekarang']]"));
    private static final Locator NEAREST_SPECIAL_PRICE_DATE = new Locator("NEAREST_SPECIAL_PRICE_DATE", By.xpath("//span[contains(@class, 'Day_day_number_container') and following-sibling::small[contains(@class, 'Text_variant_positive')]]"));
    private static final Locator FIRST_SPECIAL_PRICE_DATE_BUTTON = new Locator("FIRST_SPECIAL_PRICE_DATE_BUTTON", By.xpath("(//button[contains(@class,'Day_day') and not(@disabled) and .//small[contains(@class,'Text_variant_positive')]])[1]"));
    private static final Locator FIRST_SPECIAL_PRICE_AMOUNT = new Locator("FIRST_SPECIAL_PRICE_AMOUNT", By.xpath("(//small[contains(@class,'Day_price_specific_label') and contains(@class,'Text_variant_positive')])[1]"));
    private static final Locator SELECTED_DATE_BUTTON = new Locator("SELECTED_DATE_BUTTON", By.xpath("//button[contains(@class,'Day_day_selected')]"));

    private Locator getSessionLocator(String sessionName) {
        return new Locator("SESSION_BUTTON", new By.ByXPath("//*[contains(@class,'ScheduleSession_session_item_heading')]//span[contains(text(),'" + sessionName + "')]"));
    }

    private static final Locator PACKAGE_GROUP = new Locator("PACKAGE_GROUP", AppiumBy.xpath("//button[contains(@data-testid, 'package-group-button')]"));
    private static final Locator LOYALTY_NOT_ELIGBLE = new Locator("LOYALTY_NOT_ELIGBLE", AppiumBy.xpath("//div[contains(@class,'PackageSelectionDefault_loyalty_not_eligible') and contains(text(),'This package is not available at your membership tier') or contains(text(),'Paket ini nggak tersedia untuk tier keanggotaanmu.')]"));

    private Locator getTicketBenefit(String packageName) {
        return new Locator("SELECT_TICKET_BUTTON_" + packageName, new By.ByXPath("//*[contains(@class,'package_title') and contains(text(),'" + packageName + "')]//parent::*//parent::*//following-sibling::*[contains(@class,'package_body')]//*[contains(@class,' PackageBenefit_desktop')]"));
    }

    private final String packageTitleClassName = "PackageSelectionDefault_package_title";

    private Locator getDetailOfPackageLocator(String packageName) {
        return new Locator("DETAIL_TICKET_BUTTON_" + packageName, new By.ByXPath("//*[contains(@class,'package_title') and contains(text(),'" + packageName + "')]//parent::*[contains(@class, 'PackageCard_wrapper_title')]//following-sibling::*[contains(@class, 'PackageCard_btn_see_all')]"));
    }

    private final String buyXGetY = "*[contains(@class,'PackageSelectionDefault_package_subtitle') and ((contains(text(),'Buy') and contains(text(),'Get')) or (contains(text(),'Beli') and contains(text(),'Gratis')))]";

    private Locator getSelectTicketCampaign(String packageName, String campaignName) {
        if (campaignName.equalsIgnoreCase("buyxgety")) {
            return new Locator("SELECT_TICKET_CAMPAIGN_BUTTON_" + campaignName, new By.ByXPath("(//*[contains(@class,'" + packageTitleClassName + "') and contains(text(),'" + packageName + "')]//following::" + buyXGetY + "//following::*[contains(@class,'Button_variant_primary')])[1]"));
        }
        return new Locator("SELECT_TICKET_CAMPAIGN_BUTTON_" + campaignName, new By.ByXPath("(//*[contains(@class,'" + packageTitleClassName + "') and contains(text(),'" + packageName + "')]//following::*[contains(@class,'PackageSelectionDefault_campaign_label') and text()='" + campaignName + "']//following::*[contains(@class,'Button_variant_primary')])[1]"));
    }

    private Locator getBuyXGetYStatus(String packageName) {
        return new Locator("BUY_X_GET_Y_STATUS", new By.ByXPath("//*[contains(@class,'" + packageTitleClassName + "') and contains(text(),'" + packageName + "')]//following-sibling::" + buyXGetY));
    }

    private final Locator BUY_X_GET_Y_PRODUCT_TITLE = new Locator("BUY_X_GET_Y_PRODUCT_TITLE", new By.ByXPath("//" + buyXGetY + "//preceding-sibling::*[contains(@class,'" + packageTitleClassName + "')]"));
    private final Locator KLOOK = new Locator("KLOOK", new By.ByXPath("//div[@data-testid='package-card']//div[contains(@class, 'BundleLabel_content__')]//span[text()='Klook Bundle']/ancestor::div[@data-testid='package-card']//button[text()='Book']"));

    private Locator getSelectTicketSupplier(String supplierName) {
        return new Locator("SELECT_TICKET_SUPPLIER_BUTTON_" + supplierName, new By.ByXPath("//div[contains(@class,'BundleLabel_content')]//span[contains(text(),'" + supplierName + "')]//ancestor::div[@data-testid='package-card']//button[contains(@class,'Button_variant_primary')]"));
    }


    public String getLocationFromLocator() {
        Optional<WebElement> element = driverTools.element(FIRST_LOCATION);
        return driverTools.getText(element.orElse(null));
    }

    public String getNearestLocationDistance(){
        if(driverTools.isElementDisplayed(FIRST_LOCATION_DISTANCE)){
            logger.log("Location distance is displayed");
            WebElement element = driverTools.waitForElementToPresence(FIRST_LOCATION_DISTANCE).orElseThrow();
            String distanceText = driverTools.getText(element);
            logger.log("Distance text: " + distanceText);
            return distanceText;
        }
        return null;
    }

    public static Locator getDynamicLocator(String displayText) {
        String locatorName = displayText.toUpperCase().replace(" ", "_") + "_LOCATOR";
        return new Locator(locatorName, AppiumBy.xpath("//span[contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size') and contains(@class, 'Text_weight_bold') and text()='" + displayText + "']"));
    }

    public boolean isLabelPresent(String label) {
        Locator labelTag = getDynamicLocator(label);
        driverTools.waitForElementToAppear(labelTag, 10);
        return driverTools.isElementDisplayed(labelTag);
    }

    public void clickNearestLocationButton() {
        driverTools.waitForElementToBeClickable(NEAREST_BUTTON);
        driverTools.click(NEAREST_BUTTON);
        driverTools.waitForElementToAppear(LOGIN_OVERLAY,2);
        driverTools.refreshWebpage();
    }

    public void chooseTimeSlot() {
        driverTools.scrollIntoWebView(TIME_SLOT);
        List<WebElement> timeSlots = driverTools.elements(TIME_SLOTS).orElseThrow();
        int randomTimeSlotIndex = generateRandomInt(1, timeSlots.size());
        driverTools.clickWithJavascriptExecutor(timeSlotLocator(randomTimeSlotIndex));
    }

    private Locator timeSlotLocator(int index) {
        return new Locator("TIME_SLOT", AppiumBy.xpath("(//*[contains(@class,'TimeSlot_time_wrapper')]/button/child::*)[" + index + "]"));
    }

    public void openPdpUrl(String slugProductName) {
        String url = "https://www.tiket.com/to-do/";
        if (Configs.ENVIRONMENT == Environment.GK) {
            url = "https://gatotkaca.tiket.com/to-do/";
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            url = "https://preprod.tiket.com/to-do/";
        }
        driverTools.get(url + slugProductName);
    }

    @SneakyThrows
    public void chooseTicket(String packageName) {
        driverTools.waitForElementToPresence(SELECT_TICKET_BUTTON);
        WebElement element;
        if (packageName.equals(String.valueOf(SortItem.RANDOM))) {
            List<WebElement> webElementList = driverTools.elements(SELECT_TICKET_BUTTON).orElseThrow();
            logger.log("Total package: " + webElementList.size());
            int randomInt = generateRandomInt(0, webElementList.size() - 1);
            // Sometimes the package button can be stale, so we will catch the exception and try to click again
            try {
                element = webElementList.get(randomInt);
                driverTools.waitForElementToAppear(element);
                driverTools.waitForElementToBeClickable(element);
                driverTools.clickWithJavascriptExecutor(element);
            }catch (StaleElementReferenceException e){
                logger.log("StaleElementReferenceException caught, retrying to click the package");
                Locator selectTicketLocator = new Locator("SELECT_TICKET_BUTTON_RANDOM", new By.ByXPath("(//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[contains(@class,'Button_variant_primary') and not(contains(@class,'disabled'))])[" + (randomInt + 1) + "]"));
                driverTools.waitForElementToAppear(selectTicketLocator);
                driverTools.waitForElementToBeClickable(selectTicketLocator);
                driverTools.clickWithJavascriptExecutor(selectTicketLocator);
            }

        } else {
            driverTools.scroll(2, 5);
            packageName = packageName.replaceAll("\\[", "(");
            packageName = packageName.replaceAll("]", ")");
            logger.log("Choosing package: " + packageName);
            List<WebElement> packageTitles = driverTools.elements(
                    new Locator("PACKAGE_TITLES",
                            new By.ByXPath("//*[contains(@class,'PackageCard_package_title')]"))
            ).orElseThrow();

            for (WebElement packageTitle : packageTitles) {
                logger.log("Available package: [" + packageTitle.getText() + "]");
            }
            Locator selectTicketLocator = getSelectTicket(packageName);
            driverTools.waitForElementToPresence(selectTicketLocator, 10);
            driverTools.scrollIntoWebView(selectTicketLocator);
            driverTools.waitForElementToAppear(selectTicketLocator);
            driverTools.waitForElementToBeClickable(selectTicketLocator);
            driverTools.clickWithJavascriptExecutor(selectTicketLocator);
        }

        if(driverTools.waitForElementToAppear(CLOSE_DATE, 5).isPresent()) {
            driverTools.click(CLOSE_DATE);
        }
    }

    // default value for isFullyBooked is false
    public String chooseTicketCampaign(String packageName, String campaignName, boolean isFullyBooked) {
        driverTools.waitForElementToAppear(getSelectTicketCampaign(packageName, campaignName));
        driverTools.scrollIntoWebView(getSelectTicketCampaign(packageName, campaignName));
        WebElement selectTicketButton = driverTools.element(getSelectTicketCampaign(packageName, campaignName)).orElseThrow();

        if (isFullyBooked) {
            if (isCampaignFullyBooked(packageName)) {
                logger.log("Campaign is fully booked");
            } else {
                logger.log("Campaign is still available");
                selectTicketButton = findAvailableCampaign(packageName, campaignName);
            }
        }

        driverTools.scrollIntoWebView(selectTicketButton);
        packageName = driverTools.getText(selectTicketButton);
        driverTools.clickWithJavascriptExecutor(selectTicketButton);
        return packageName;
    }

    private boolean isCampaignFullyBooked(String packageName) {
        String actualCampaign = driverTools.getText(getBuyXGetYStatus(packageName)).orElseThrow().toLowerCase();
        return actualCampaign.contains("quota runs out") || actualCampaign.contains("kuota habis");
    }

    private WebElement findAvailableCampaign(String packageName, String campaignName) {
        List<WebElement> buyXGetY = driverTools.elements(BUY_X_GET_Y_PRODUCT_TITLE).orElseThrow();
        for (WebElement buyXGetYProduct : buyXGetY) {
            packageName = driverTools.getText(buyXGetYProduct);
            if (isCampaignFullyBooked(packageName)) {
                logger.log("Campaign is fully booked for package: " + packageName);
            } else {
                logger.log("Campaign is available for package: " + packageName);
                break;
            }
        }
        return driverTools.element(getSelectTicketCampaign(packageName, campaignName)).orElseThrow();
    }

    public boolean isBuyXGetYSectionDisplayed() {
        return driverTools.isElementDisplayed(BUYXGETY_SECTION);
    }

    @SneakyThrows
    public boolean compareWeekdayAndWeekendPrice() {
        driverTools.scrollIntoWebView(CHECK_AVAILABLE_DATES);
        driverTools.waitForElementToBeClickable(CHECK_AVAILABLE_DATES);
        driverTools.clickWithJavascriptExecutor(CHECK_AVAILABLE_DATES);
        LocalDate today = LocalDate.now();
        int weekdayOffset = findNextWeekday(today);
        int weekdayIndex = today.getDayOfMonth() + weekdayOffset;

        int weekendOffset = findNextWeekend(today);
        int weekendIndex = today.getDayOfMonth() + weekendOffset;

        String weekdayXpath = String.format("(//small[contains(@class, 'Day_price_specific_label')])[%d]", weekdayIndex);
        String weekendXpath = String.format("(//small[contains(@class, 'Day_price_specific_label')])[%d]", weekendIndex);

        Locator weekdayLocator = new Locator("WEEKDAY_LOCATOR", By.xpath(weekdayXpath));
        Locator weekendLocator = new Locator("WEEKEND_LOCATOR", By.xpath(weekendXpath));

        int weekdayPrice = getPriceFromXpath(weekdayLocator);
        int weekendPrice = getPriceFromXpath(weekendLocator);

        logger.log("Weekday price: " + weekdayPrice);
        logger.log("Weekend price: " + weekendPrice);

        driverTools.waitForElementToBeClickable(CLOSE_DATE);
        driverTools.click(CLOSE_DATE);

        if (weekendPrice < weekdayPrice) {
            return true;
        } else {
            return false;
        }

    }

    private static int findNextWeekday(LocalDate date) {
        int offset = 1;
        while (offset <= 7) {
            DayOfWeek day = date.plusDays(offset).getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                break;
            }
            offset++;
        }
        return offset;
    }

    private static int findNextWeekend(LocalDate date) {
        int offset = 1;
        while (offset <= 7) {
            DayOfWeek day = date.plusDays(offset).getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                break;
            }
            offset++;
        }
        return offset;
    }

    public int getPriceFromXpath(Locator xpath) {
        Optional<String> priceText = driverTools.getText(xpath);
        return Integer.parseInt(priceText.get().replace(",", "").trim());
    }

    public void chooseTicketSupplier(String supplierName) throws InterruptedException {
        driverTools.waitForElementToAppear(getSelectTicketSupplier(supplierName));
        driverTools.scrollIntoWebView(getSelectTicketSupplier(supplierName));

        Locator selectTicketLocator = getSelectTicketSupplier(supplierName);
        WebElement element = driverTools.element(selectTicketLocator).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);

    }

    public void chooseTicketWithRefund () {
        driverTools.scrollIntoWebView(SELECT_TICKET_BUTTON_REFUND);
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET_BUTTON_REFUND);
    }

    private List<WebElement> getVisitDateElements () {
        if (driverTools.isElementDisplayed(PACKAGES_TAB)) driverTools.click(PACKAGES_TAB);
        driverTools.waitForElementsToAppear(VISIT_DATE).orElseThrow();
        return driverTools.elements(VISIT_DATE).orElseThrow();
    }

    private List<WebElement> getVisitDateElementsWithoutTab () {
        driverTools.waitForElementsToAppear(VISIT_DATE).orElseThrow();
        return driverTools.elements(VISIT_DATE).orElseThrow();
    }

    private String chooseRandomVisitDate ( int index, int totalVisitDate, List<WebElement > visitDateElements){
        if (index == -1) {
            logger.log("visit date will be selected randomly");
            index = generateRandomInt(0, totalVisitDate - 1);
        }
        logger.log("Selected index: " + index);
        WebElement selectedElement = visitDateElements.get(index);
        String dateText = selectedElement.getText();
        driverTools.clickWithJavascriptExecutor(selectedElement);
        return dateText;
    }

    public void chooseVisitDate ( int index){
        List<WebElement> visitDateElements = getVisitDateElements();
        int totalVisitDate = visitDateElements.size();
        logger.log("Total visitDate: " + totalVisitDate);
        chooseRandomVisitDate(index, totalVisitDate, visitDateElements);
    }

    public void chooseVisitDateWithoutTab ( int index){
        List<WebElement> visitDateElements = getVisitDateElementsWithoutTab();
        int totalVisitDate = visitDateElements.size();
        logger.log("Total visitDate: " + totalVisitDate);
        chooseRandomVisitDate(index, totalVisitDate, visitDateElements);
    }

    public void chooseVisitDate (String date){
        List<WebElement> visitDateElements = getVisitDateElements();
        int totalVisitDate = visitDateElements.size();
        logger.log("Total visitDate: " + totalVisitDate);
        for (int i = 0; i < totalVisitDate; i++) {
            String dateText = visitDateElements.get(i).getText();
            if (dateText.equals(date)) {
                logger.log("Selected date: " + date);
                driverTools.clickWithJavascriptExecutor(visitDateElements.get(i));
                break;
            }
            if (i == totalVisitDate - 1) {
                logger.log("Date: " + date + " not found");
                chooseRandomVisitDate(-1, totalVisitDate, visitDateElements);
            }
        }
    }

    public String chooseVisitDate ( boolean isOdd){
        List<WebElement> visitDateElements = getVisitDateElements();
        int totalVisitDate = visitDateElements.size();
        logger.log("Total visitDate: " + totalVisitDate);
        String dateText = "";
        for (int i = 0; i < totalVisitDate; i++) {
            dateText = visitDateElements.get(i).getText();
            int date = Integer.parseInt(dateText.replaceAll("[^0-9]", ""));
            if (isOdd & date % 2 == 1) {
                logger.log("Selected date: " + date);
                driverTools.clickWithJavascriptExecutor(visitDateElements.get(i + 3));
                break;
            }
            if (i == totalVisitDate - 1) {
                logger.log("No odd date found");
                dateText = chooseRandomVisitDate(-1, totalVisitDate, visitDateElements);
            }
        }
        return dateText;
    }

    public static Locator getPackageTitleLocator (String packageTitle){
        String xpath = String.format("//h3[contains(@class, 'PackageSelectionDefault_package_title') and text()='%s']", packageTitle);
        By loc = By.xpath(xpath);
        return new Locator(xpath, loc);
    }

    public void chooseVisitDateLoyaltyMember ( int index, String packageName) throws InterruptedException {
        Locator packageTitleLocator = getPackageTitleLocator(packageName);
        driverTools.scroll(2, 40);
        driverTools.isElementDisplayed(packageTitleLocator);
        Assertion.assertThat(packageName + " is present", driverTools.isElementDisplayed(packageTitleLocator), is(true));
        driverTools.waitForElementsToAppear(VISIT_DATE_LOYALTY, 20).orElseThrow();
        List<WebElement> visitDate = driverTools.elements(VISIT_DATE_LOYALTY).orElseThrow();
        int totalVisitDate = visitDate.size();
        logger.log("Total visitDate: " + totalVisitDate);
        if (index == -1) {
            logger.log("visit date will be selected randomly");
            index = generateRandomInt(0, totalVisitDate - 1);
        }
        logger.log("Selected index: " + index);
        driverTools.clickWithJavascriptExecutor(visitDate.get(index));
    }

    public abstract String getValidityPeriodText();

    public void setTimeslot () {
        driverTools.waitForElementToPresence(TIME_SLOT);
        driverTools.waitForElementToBeClickable(TIME_SLOT, 20);
        driverTools.clickWithJavascriptExecutor(TIME_SLOT);
        Assertion.assertThat("Time slot is present on the selected date", driverTools.isElementDisplayed(TIME_SLOT), is(true));
    }

    public void setTimeslotOptional () {
        if(driverTools.waitForElementToPresence(TIME_SLOT).isPresent()){
            driverTools.waitForElementToBeClickable(TIME_SLOT, 20);
            driverTools.clickWithJavascriptExecutor(TIME_SLOT);
            Assertion.assertThat("Time slot is present on the selected date", driverTools.isElementDisplayed(TIME_SLOT), is(true));
        }
    }

    /**
     * Sets the ticket quantity for the row at {@code ticketIndex} to exactly {@code qty} total tickets.
     * Reads the current input value and only clicks the + button the difference number of times,
     * so {@code setTicketQty(1, 0)} leaves the (default-1) input at 1 instead of incrementing to 2.
     * Logs a soft warning if {@code qty} is below the current value (decrement is not implemented here).
     */
    public void setTicketQty ( int qty, int ticketIndex){
        if(driverTools.waitForElementToPresence(PRICE_CHANGE_POPUP ,5).isPresent()) driverTools.clickWithJavascriptExecutor(PRICE_CHANGE_POPUP);
        if (driverTools.waitForElementToPresence(TIME_SLOTS, 5).isPresent()) chooseTimeSlot();

        driverTools.waitForElementToDisappear(LOADING_STATE);
        if(driverTools.waitForElementToAppear(TOTAL_TICKET).isPresent()){
            logger.log("Quantity selector is available");

            // Validate ticketIndex up-front against the rendered tier count. Inputs and increment
            // buttons are 1:1 per tier, so either count is a valid source of truth; we use the
            // input count because it's what we actually index into for the read below.
            int tierCount = driverTools.elements(TOTAL_TICKET).map(List::size).orElse(0);
            if (ticketIndex < 0 || ticketIndex >= tierCount) {
                throw new IndexOutOfBoundsException(
                        "ticketIndex " + ticketIndex + " is out of bounds; this package renders " + tierCount
                                + " ticket tier(s). Check the test-data 'ticketIndex' parameter — it is zero-based"
                                + " (use 0 for a single-tier package).");
            }

            // Per-row indexed locator so we read/inspect the qty input of the SAME tier we will
            // click. Reading the first match was incorrect when ticketIndex > 0 — the baseline
            // came from the wrong row, producing the wrong number of increment clicks.
            // XPath is 1-based.
            Locator indexedTotalTicket = new Locator("TOTAL_TICKET[" + ticketIndex + "]",
                    new By.ByXPath("(//input[contains(@class,'QuantityEditor_quantity_input')])[" + (ticketIndex + 1) + "]"));

            int currentTotal = Integer.parseInt(driverTools.getValue(indexedTotalTicket));
            logger.log("Current ticket count: " + currentTotal + ", target: " + qty + " (row " + ticketIndex + ")");

            if (qty < currentTotal) {
                logger.softWarning("setTicketQty: requested qty " + qty + " is below current value " + currentTotal
                        + "; this method only increments. Skipping clicks.");
            } else if (qty > currentTotal) {
                driverTools.waitForElementToPresence(INCREMENT_BUTTON).orElseThrow();
                driverTools.scrollIntoWebView(INCREMENT_BUTTON);

                for (int i = currentTotal; i < qty; i++) {
                    List<WebElement> webElementList = driverTools.elements(INCREMENT_BUTTON).orElseThrow();
                    driverTools.clickWithJavascriptExecutor(webElementList.get(ticketIndex));
                }
                driverTools.waitForWebToLoad();
                driverTools.waitForElementToBeClickable(INCREMENT_BUTTON).orElseThrow();
                List<WebElement> webElementList = driverTools.elements(INCREMENT_BUTTON).orElseThrow();
                driverTools.waitForElementToBeClickable(webElementList.get(ticketIndex)).orElseThrow();
            }

            int finalTotal = Integer.parseInt(driverTools.getValue(indexedTotalTicket));
            logger.log("Final ticket count selected: " + finalTotal + " (row " + ticketIndex + ")");
        }else{
            // if quantity selector is not available, it means we have to select the quantity from the automatic seating
            Locator automaticSeating = new Locator("automaticSeating", new By.ByXPath("(//*[contains(@class,'TicketQuantityEventPaxList_pax_item')])["+qty+"]"));
            driverTools.waitForElementToPresence(automaticSeating).orElseThrow();
            driverTools.clickWithJavascriptExecutor(automaticSeating);
            logger.log("Final ticket count selected (automatic seating): " + qty);
        }
    }

    /**
     * Sets the ticket quantity for the row at {@code ticketIndex} within the package identified by
     * {@code packageName}. Scopes quantity inputs and increment buttons to that package card/collapse
     * section so multi-package PDPs do not click the wrong tier controls.
     */
    public void setTicketQty(int qty, int ticketIndex, String packageName) {
        packageName = normalizePackageName(packageName);
        if (driverTools.waitForElementToPresence(PRICE_CHANGE_POPUP, 5).isPresent()) {
            driverTools.clickWithJavascriptExecutor(PRICE_CHANGE_POPUP);
        }
        if (driverTools.waitForElementToPresence(TIME_SLOTS, 5).isPresent()) {
            chooseTimeSlot();
        }

        driverTools.waitForElementToDisappear(LOADING_STATE);
        Locator packageTotalTickets = getPackageTotalTicketInputsLocator(packageName);
        if (driverTools.waitForElementToAppear(packageTotalTickets).isPresent()) {
            logger.log("Quantity selector is available for package: " + packageName);

            int tierCount = driverTools.elements(packageTotalTickets).map(List::size).orElse(0);
            if (ticketIndex < 0 || ticketIndex >= tierCount) {
                throw new IndexOutOfBoundsException(
                        "ticketIndex " + ticketIndex + " is out of bounds; package '" + packageName + "' renders "
                                + tierCount + " ticket tier(s). Check the test-data 'ticketIndex' parameter — it is zero-based"
                                + " (use 0 for a single-tier package).");
            }

            Locator indexedTotalTicket = getPackageTotalTicketLocator(packageName, ticketIndex);
            Locator indexedIncrementButton = getPackageIncrementButtonLocator(packageName, ticketIndex);

            int currentTotal = Integer.parseInt(driverTools.getValue(indexedTotalTicket));
            logger.log("Current ticket count for package '" + packageName + "': " + currentTotal + ", target: " + qty
                    + " (row " + ticketIndex + ")");

            if (qty < currentTotal) {
                logger.softWarning("setTicketQty: requested qty " + qty + " is below current value " + currentTotal
                        + " for package '" + packageName + "'; this method only increments. Skipping clicks.");
            } else if (qty > currentTotal) {
                driverTools.waitForElementToPresence(indexedIncrementButton).orElseThrow();
                driverTools.scrollIntoWebView(indexedIncrementButton);

                for (int i = currentTotal; i < qty; i++) {
                    driverTools.clickWithJavascriptExecutor(indexedIncrementButton);
                }
                driverTools.waitForWebToLoad();
                driverTools.waitForElementToBeClickable(indexedIncrementButton).orElseThrow();
            }

            int finalTotal = Integer.parseInt(driverTools.getValue(indexedTotalTicket));
            logger.log("Final ticket count selected for package '" + packageName + "': " + finalTotal + " (row " + ticketIndex + ")");
        } else {
            Locator automaticSeating = getPackageAutomaticSeatingLocator(packageName, qty);
            driverTools.waitForElementToPresence(automaticSeating).orElseThrow();
            driverTools.clickWithJavascriptExecutor(automaticSeating);
            logger.log("Final ticket count selected for package '" + packageName + "' (automatic seating): " + qty);
        }
    }

    private String normalizePackageName(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("packageName must not be null");
        }
        return packageName.replaceAll("\\[", "(").replaceAll("]", ")");
    }

    private String buildPackageTitleCondition(String packageName) {
        if (packageName.contains("/")) {
            String[] parts = packageName.split("/", 2);
            String en = parts[0].trim();
            String id = parts[1].trim();
            return "(contains(.,'" + en + "') or contains(.,'" + id + "'))";
        }
        return "contains(.,'" + packageName + "')";
    }

    private String getPackageQuantityScopeXPath(String packageName) {
        String titleCondition = buildPackageTitleCondition(packageName);
        return "//*[contains(@class,'package_title') and " + titleCondition + "]"
                + "/ancestor::*[@data-testid='package-card' or contains(@class,'PackageCollapse')][1]";
    }

    private Locator getPackageTotalTicketInputsLocator(String packageName) {
        return new Locator("PACKAGE_TOTAL_TICKETS_" + packageName,
                new By.ByXPath(getPackageQuantityScopeXPath(packageName)
                        + "//input[contains(@class,'QuantityEditor_quantity_input')]"));
    }

    private Locator getPackageTotalTicketLocator(String packageName, int ticketIndex) {
        return new Locator("PACKAGE_TOTAL_TICKET_" + packageName + "_" + ticketIndex,
                new By.ByXPath("(" + getPackageQuantityScopeXPath(packageName)
                        + "//input[contains(@class,'QuantityEditor_quantity_input')])[" + (ticketIndex + 1) + "]"));
    }

    private Locator getPackageIncrementButtonLocator(String packageName, int ticketIndex) {
        return new Locator("PACKAGE_INCREMENT_" + packageName + "_" + ticketIndex,
                new By.ByXPath("(" + getPackageQuantityScopeXPath(packageName)
                        + "//button[contains(@class,'operation') and ./*[contains(@class,'plus')]])[" + (ticketIndex + 1) + "]"));
    }

    private Locator getPackageAutomaticSeatingLocator(String packageName, int qty) {
        return new Locator("PACKAGE_AUTOMATIC_SEATING_" + packageName + "_" + qty,
                new By.ByXPath("(" + getPackageQuantityScopeXPath(packageName)
                        + "//*[contains(@class,'TicketQuantityEventPaxList_pax_item')])[" + qty + "]"));
    }

    /**
     * Reads the rendered ticket quantity for the row at {@code ticketIndex} (zero-based).
     * Mirrors the per-row indexed read used inside {@link #setTicketQty(int, int)} so callers can
     * assert post-conditions independently — useful for verifying that the right row was modified
     * (especially for multi-tier packages where {@code ticketIndex > 0}).
     *
     * @throws IndexOutOfBoundsException if {@code ticketIndex} is outside the rendered tier range
     * @throws java.util.NoSuchElementException if no quantity input is rendered (caller should
     *         only invoke this after {@code setTicketQty} has run successfully)
     */
    public int getTicketQty(int ticketIndex) {
        int tierCount = driverTools.elements(TOTAL_TICKET).map(List::size).orElse(0);
        if (ticketIndex < 0 || ticketIndex >= tierCount) {
            throw new IndexOutOfBoundsException(
                    "ticketIndex " + ticketIndex + " is out of bounds; this package renders " + tierCount + " ticket tier(s)");
        }
        Locator indexedTotalTicket = new Locator("TOTAL_TICKET[" + ticketIndex + "]",
                new By.ByXPath("(//input[contains(@class,'QuantityEditor_quantity_input')])[" + (ticketIndex + 1) + "]"));
        return Integer.parseInt(driverTools.getValue(indexedTotalTicket));
    }

    public void clickBookButton () {
        driverTools.waitForElementToAppear(BOOK_BUTTON);
        driverTools.clickWithJavascriptExecutor(BOOK_BUTTON);
    }

    public abstract void clickBuyTicketButton();

    protected abstract boolean isPromoBenefitsPopupDisplayed();

    public boolean isCorrectProductOpened (String productName) throws InterruptedException {
        Locator locator = generateSeoTitleLocator();
        driverTools.scroll(2, 10);

        Optional<WebElement> element = driverTools.waitForElementToAppear(locator);

        if (element.isPresent()) {
            return true;
        } else {
            logger.log("Product page not opened");
            return false;
        }
    }

    public boolean isProductDetailPageDisplayed() {
        return driverTools.waitForElementToAppear(PRODUCT_TITLE, 15).isPresent();
    }

    public String getProductTitle() {
        WebElement webElement = driverTools.waitForElementToPresence(PRODUCT_TITLE, 15).orElseThrow();
        return driverTools.getText(webElement);
    }

    public Locator generateSeoTitleLocator () {
        Locator existingLocator = new Locator("SEO_TITLE", By.xpath("//div[contains(@class,'ATFSection_info_section')]//h1[contains(@class,'Text_size_h2')]|//*[contains(@data-testid,'seo-title')]"));
        WebElement seoTitleElement = driverTools.waitForElementToPresence(existingLocator).orElseThrow();
        String titleText = driverTools.getText(seoTitleElement).trim();

        By xpath = By.xpath("//div[contains(@class,'ATFSection_info_section')]//h1[contains(@class,'Text_size_h2') and contains(text(),'" + titleText + "')]|//*[contains(@data-testid,'seo-title') and contains(text(),'" + titleText + "')]");
        return new Locator("SEO_TITLE", xpath);
    }


    public boolean isPartnerNamePresent () throws InterruptedException {
        driverTools.scroll(2, 400);
        if (driverTools.isElementDisplayed(PARTNER_FROM)) return true;
        return false;
    }

    public boolean isReviewSectionPresent () throws InterruptedException {
        driverTools.waitForElementToAppear(REVIEW_BUTTON);
        driverTools.click(REVIEW_BUTTON);
        if (driverTools.isElementDisplayed(FIRST_REVIEW_TAB)) return true;
        return false;
    }

    public boolean verifyPackageSection () {
        if (driverTools.isElementDisplayed(PACKAGES_TAB)) driverTools.click(PACKAGES_TAB);
        if (driverTools.isElementDisplayed(PACKAGE_SECTION)) {
            WebElement container = driverTools.element(PACKAGE_SECTION).orElseThrow(() -> new NoSuchElementException("Container not found"));
            List<WebElement> packageButtons = container.findElements(By.tagName("button"));
            if (packageButtons.size() > 0) {
                return true;
            }
        } else {
            logger.log("Package section not displayed");
            return false;
        }
        return false;
    }

    public boolean isLoginMessageDisplayed () {
        driverTools.waitForElementsToAppear(PDP_LOGIN_MESSAGE);
        return driverTools.waitForElementsToAppear(PDP_LOGIN_MESSAGE).isPresent();
    }

    public boolean isLPGLabelPresentOnPDP () {
        driverTools.waitForElementsToAppear(LPG_LABEL_PDP);
        return driverTools.waitForElementsToAppear(LPG_LABEL_PDP).isPresent();
    }

    public void clickSeePackagesButton () {
        if (driverTools.waitForElementToAppear(SEE_PACKAGES).isPresent()) {
            driverTools.scrollIntoWebView(SEE_PACKAGES);
            driverTools.clickWithJavascriptExecutor(SEE_PACKAGES);
        } else {
            logger.log("Button did not appear");
        }
    }

    public void openCalender(){
        driverTools.waitForElementToAppear(OPEN_CALENDER);
        driverTools.clickWithJavascriptExecutor(OPEN_CALENDER);
    }

    public Locator generateSeoTitleLocator (String titleText){
        By xpath = By.xpath("//h1[@data-testid='seo-title' and text()='" + titleText + "']");
        return new Locator("SEO_TITLE", xpath);
    }

    public String getPackageBenefit (String packageName){
        return driverTools.getText(getTicketBenefit(packageName)).orElseThrow();
    }

    public void openDetailPackage (String packageName, int maxTry){
        driverTools.swipeToElement(getSelectTicket(packageName), Swipe.UP, maxTry);
        driverTools.clickWithJavascriptExecutor(getDetailOfPackageLocator(packageName));
    }

    public boolean verifyBundleRecommendation () {
        return driverTools.waitForElementToPresence(BUNDLE_RECOMMENDATION).isPresent();
    }

    public boolean verifyBundleTitle () {
        return driverTools.waitForElementToPresence(BUNDLE_TITLE).isPresent();
    }

    public void selectSession (String sessionName){
        driverTools.waitForElementToPresence(SELECT_SESSION_BUTTON);
        driverTools.clickWithJavascriptExecutor(SELECT_SESSION_BUTTON);
        driverTools.clickWithJavascriptExecutor(getSessionLocator(sessionName));
    }

    public void validateGroupNavigation () throws Exception {
        List<WebElement> groups = driverTools.elements(PACKAGE_GROUP).orElseThrow();
        for (WebElement group : groups) {
            String groupName = group.getText();
            logger.log("Clicked Group Name: " + groupName);

            driverTools.click(group);

            WebElement subsequentGroup = driverTools.waitForElementToAppear(groupNameLocator(groupName)).orElseThrow(
                    () -> new Exception("Subsequent group with name '" + groupName + "' did not appear."));

            Assertion.assertThat("Subsequent group '" + groupName + "' is displayed.",
                    driverTools.isElementDisplayed(subsequentGroup), is(true));

            logger.log("Subsequent group '" + groupName + "' is displayed.");
        }
    }

    private Locator groupNameLocator (String groupName){
        return new Locator("GROUP_" + groupName, By.xpath("//div[contains(text(),'" + groupName + "')]"));
    }

    public void verifyLoyaltyNotEligible () {
        Assertion.assertThat("This package is not available at your membership tier Banner is displayed.",
                driverTools.waitForElementToPresence(LOYALTY_NOT_ELIGBLE).isPresent(), is(true));
    }

    public boolean isRefund100PVisible () {
        driverTools.waitForElementsToAppear(PRODUCT_TITLE);
        return driverTools.waitForElementsToAppear(REFUND_100P).isPresent();
    }

    public void clickOnLoginButtonPDP () {
        driverTools.waitForElementToAppear(LOGIN_BUTTON, 3).ifPresent(driverTools::click);
    }

    public void loginFromPDP (String email, String password, String otp ,int retryCounter) throws
    FailedLoginException, InterruptedException {
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        clickOnLoginButtonPDP();
        loginB2CInDWEB.inputEmail(email);
        loginB2CInDWEB.inputPassword(password);
        loginB2CInDWEB.inputOTPCode(otp, retryCounter);
    }

    public String getMasterCampaign () {
        driverTools.waitForElementToPresence(MASTER_CAMPAIGN_LABEL, 10);
        return driverTools.getText(MASTER_CAMPAIGN_LABEL).orElseThrow();
    }

    public String getChildCampaign () {
        driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON);
        driverTools.scrollIntoWebView(CHILD_CAMPAIGN_LABEL);
        return driverTools.getText(CHILD_CAMPAIGN_LABEL).orElseThrow();
    }

    public void clickCheckAvailableDates () {
        driverTools.waitForElementToPresence(CHECK_AVAILABLE_DATES);
        driverTools.clickWithJavascriptExecutor(CHECK_AVAILABLE_DATES);
    }

    public String getSpecialPriceForDate ( int date){
        Locator locator = getSpecialPriceForDateLocator(date);
        driverTools.waitForElementToPresence(locator);
        return driverTools.getText(locator).orElseThrow();
    }

    public int getNearestSpecialPriceDate() {
        return Integer.parseInt(driverTools.getText(NEAREST_SPECIAL_PRICE_DATE).orElseThrow());
    }

    public String clickFirstSpecialPriceDateAndGetPrice() {
        driverTools.waitForElementToPresence(FIRST_SPECIAL_PRICE_DATE_BUTTON);
        driverTools.waitForElementToPresence(FIRST_SPECIAL_PRICE_AMOUNT);
        WebElement priceElement = driverTools.element(FIRST_SPECIAL_PRICE_AMOUNT).orElseThrow();
        String specialPrice = driverTools.getText(priceElement);
        logger.log("First special price date amount: " + specialPrice);
        driverTools.clickWithJavascriptExecutor(FIRST_SPECIAL_PRICE_DATE_BUTTON);
        return specialPrice;
    }

    public boolean isSpecialPriceDateSelected() {
        boolean isSelected = driverTools.waitForElementToPresence(SELECTED_DATE_BUTTON).isPresent();
        logger.log("Is special price date selected: " + isSelected);
        return isSelected;
    }

    public void clickSpecialPriceForDate ( int date){
        driverTools.clickWithJavascriptExecutor(getSpecialPriceForDateLocator(date));
    }

    private Locator getSpecialPriceForDateLocator ( int date){
        return new Locator("SPECIAL_PRICE_FOR_DATE", AppiumBy.xpath("//*[contains(@class,'Day_day_number')and not(contains(@class,'disabled')) and text()='" + date + "']/parent::*/following-sibling::small"));
    }

    public String getCurrentURL () {
        String url = driverTools.getCurrentURL();
        return url;
    }

    public void pasteLinkInNewTab (String url){
        Assertion.assertThat("URL is pasted in a new browser tab", driverTools.openNewTabAndPasteUrl(url), is(true));
    }


    public boolean isBreadcrumbNavbarPresent () {
        driverTools.waitForElementToAppear(BREADCRUMB_NAVBAR);
        return driverTools.isElementDisplayed(BREADCRUMB_NAVBAR);
    }

    public void clickBreadcrumbNavbar ( int level) {
        Map<Integer, String> breadcrumbMap = new HashMap<>();

        breadcrumbMap.put(0, "Home");
        breadcrumbMap.put(1, "Country");
        breadcrumbMap.put(2, "Region");
        breadcrumbMap.put(3, "City");
        breadcrumbMap.put(4, "Area");

        Optional<List<WebElement>> breadcrumbItems = driverTools.elements(BREADCRUMB_NAVBAR);

        if (driverTools.isElementDisplayed(BREADCRUMB_NAVBAR) && breadcrumbItems.get().size() > level) {
            WebElement breadcrumbElement = breadcrumbItems.get().get(level);
            String breadcrumbText = driverTools.getText(breadcrumbElement).trim();

            driverTools.click(breadcrumbElement);
            String currentPageTitle = driverTools.getTitle();

            Assertion.assertThat("Navigated to the correct page for breadcrumb: " + breadcrumbMap.get(level) + ":" + breadcrumbText,
                    currentPageTitle, CoreMatchers.equalTo(breadcrumbText));
            logger.log("Breadcrumb navigation verified for: " + breadcrumbText);

        } else {
            throw new IllegalArgumentException("Invalid level: " + level);
        }
    }

    public void goBackToThePreviousPage(){
        driverTools.back();
    }

    public static final Locator PRODUCT_CATEGORY_WEBVIEW = new Locator("PRODUCT_CATEGORY_WEBVIEW", AppiumBy.xpath("//div[contains(@class,'SectionSummary_category_wrapper')]/*[contains(@class,'HcPVsG_text HcPVsG_variant_lowEmphasis')]"));
    public static final Locator PRODUCT_NAME_WEBVIEW = new Locator("PRODUCT_NAME_WEBVIEW", AppiumBy.xpath("//*[@class='HcPVsG_text HcPVsG_size_h2'] | //*[contains(@class,'pd__title mobile')] | //*[@data-testid='seo-title'] | //*[contains(@class,'ATFSection_info_section')]//h1"));
    private static final Locator BACK_BUTTON_WEBVIEW = new Locator("BACK_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'HeaderPDP_floating_content') or contains(@class,'HeaderPackageDetail_floating_arrow')]/button[1] | //*[contains(@class,'HeaderPDP_floating_icon') and @aria-label='back-button']"));
    private static final Locator HOUR_SECTION_WEBVIEW = new Locator("HOUR_SECTION_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'SectionSummary_overview_clock')]"));
    private static final Locator CLOSE_SECTION_BUTTON_WEBVIEW = new Locator("CLOSE_SECTION_BUTTON_WEBVIEW", AppiumBy.xpath("//*[contains(@class,'close_button')]"));
    private static final Locator SELECT_TICKET_BUTTON_NONPACKAGE = new Locator("SELECT_TICKET_BUTTON_NONPACKAGE", AppiumBy.xpath("(//*[contains(@class,'FooterPDP_action_section')]//button)[last()]"));
    private static final Locator FIRST_AVAILABLE_PACKAGE = new Locator("FIRST_AVAILABLE_PACKAGE", AppiumBy.xpath("(//label[contains(@class,'Radio_radio') and not(contains(@class,'Radio_state_disabled'))]//span[contains(@class,'Radio_icon')])[1]"));
    private static final Locator SELECT_TICKET_BUTTON = new Locator("SELECT_TICKET_BUTTON", AppiumBy.xpath("//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[contains(@class,'Button_variant_primary') and not(contains(@class,'disabled'))]"));
    private static final Locator PACKAGE_BOOK_BUTTON = new Locator("PACKAGE_BOOK_BUTTON", AppiumBy.xpath("//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[contains(@class,'Button_variant_primary') and not(contains(@class,'disabled'))]/*[text()='Book' or text()='Pesan']"));
    private static final Locator PACKAGE_OPTION = new Locator("PACKAGE_OPTION", AppiumBy.xpath("//*[contains(@class,'PackageCard_package_type_option')]"));
    private static final Locator PACKAGE_SELECT_NEXT_BUTTON = new Locator("PACKAGE_SELECT_NEXT_BUTTON", AppiumBy.xpath("//android.widget.Button[@text='Next' or @text='Lanjut']"));
    private static final Locator SELECT_TICKET_BUTTON_LOYALTY = new Locator("SELECT_TICKET_BUTTON_LOYALTY", AppiumBy.xpath("//*[contains(@data-testid, 'loyalty-icon')]//parent::*//parent::*//parent::*//parent::*[contains(@class,'package_info')]//following-sibling::button[not(contains(@class,'disabled'))]"));
    private static final Locator SELECT_TICKET_BUTTON_REFUNDABLE = new Locator("SELECT_TICKET_BUTTON_REFUNDABLE", AppiumBy.xpath("//*[@data-testid='refund-tier-label']//*[contains(@class,'Text_variant_positive')]//parent::div//parent::div//parent::div//parent::div//following-sibling::*[@data-testid='package-card-footer']//button/*[text()='Select' or text()='Pilih']"));
    private static final Locator SELECT_TICKET_BUTTON_COMMON = new Locator("SELECT_TICKET_BUTTON_COMMON", AppiumBy.xpath("//*[contains(@class,'KV-nSG_button') and (contains(text(),'Find Ticket') or contains(text(),'Tiket'))]"));
    private static final Locator SELECT_TICKET_BUTTON_EXPIRED_DATE = new Locator("SELECT_TICKET_BUTTON_EXPIRED_DATE", By.xpath("//div[@data-testid='package-card' and ((.//span[contains(text(), 'Berlaku hingga')] and not(.//span[contains(text(), 'sejak')])) or .//span[contains(text(), 'Valid until')])]//div[@data-testid='package-card-footer']//button"));
    private static final Locator SELECT_DETAIL_BUTTON_EXPIRED_DATE =  new Locator("SELECT_DETAIL_BUTTON_EXPIRED_DATE", By.xpath("//div[@data-testid='package-card' and ((.//span[contains(text(), 'Berlaku hingga')] and not(.//span[contains(text(), 'sejak')])) or .//span[contains(text(), 'Valid until')])]//span[contains(@class, 'PackageCard_btn_see_all')]"));
    private static final Locator CALENDAR_BUTTON = new Locator("CALENDAR_BUTTON", AppiumBy.xpath("//*[@data-testid='package-selection-date-filter-other-chip']"));
    private static final Locator SEE_ALL_REVIEW = new Locator("SEE_ALL_REVIEW", AppiumBy.xpath("//*[contains(@class,'ReviewWidget-module__button_see_all')]"));
    private static final Locator REVIEW_SECTION = new Locator("REVIEW_SECTION", AppiumBy.xpath("//*[contains(@class, \"module__review_header___Ftrj1\")]"));
    private static final Locator  REVIEW_SECTION_CARD= new Locator("REVIEW_SECTION_CARD", AppiumBy.xpath("//*[contains(@class,'rr___ReviewCard-module__review_content')]"));
    private static final Locator USER_RATING_REVIEW_PAGE = new Locator("USER_RATING_REVIEW_PAGE", AppiumBy.xpath("//*[contains(@class,'ReviewSummary_rating_score')]"));
    private static final Locator LIST_TAB_BUTTON = new Locator("LIST_TAB_BUTTON", AppiumBy.xpath("//*[contains(@class,'Tabs_tab_text')]"));
    private static final Locator OTHER_INFO_SECTION = new Locator("OTHER_INFO_SECTION", AppiumBy.xpath("//*[contains(@class,\"SectionDescription_description_heading__nBhGp\")]"));
    private static final Locator NEXT_MONTH_BUTTON = new Locator("NEXT_MONTH_BUTTON", AppiumBy.xpath("//*[contains(@class,'calendar_btn')]//button"));
    private static final Locator  DATE_BUTTON = new Locator("DATE_BUTTON", AppiumBy.xpath("//*[contains(@class, 'filter_list')]/*[contains(@class, 'Chip_active')]//*[contains(@class,'Text_text') and normalize-space(text())]"));
    private static final Locator LOCATION_SECTION = new Locator("LOCATION_SECTION", AppiumBy.xpath("//section[@role='contentinfo'][.//h2[normalize-space()='Location' or normalize-space()='Lokasi']]"));
    private static final Locator MAPS_DIRECTION_BUTTON = new Locator("MAPS_DIRECTION_BUTTON", AppiumBy.xpath("//section[@role='contentinfo'][.//h2[normalize-space()='Location' or normalize-space()='Lokasi']]//a[contains(@href,'maps')]//span"));
    private static final Locator HOW_TO_GET_THERE_BUTTON = new Locator("HOW_TO_GET_THERE_BUTTON", AppiumBy.xpath("//section[@role='contentinfo']//a[contains(@class,'SectionLocation_btn_info_detail')][.//span[normalize-space()='How to Get There' or normalize-space()='Panduan ke Lokasi'] or .//img[@alt='how to get there']]"));
    private static final Locator CONTACT_DETAILS_BUTTON = new Locator("CONTACT_DETAILS_BUTTON", AppiumBy.xpath("//section[@role='contentinfo']//a[contains(@class,'SectionLocation_btn_info_detail')][.//span[normalize-space()='Contact Details' or normalize-space()='Detail Kontak']]"));
    private static final Locator CONTACT_PHONE_OR_EMAIL = new Locator("CONTACT_PHONE_OR_EMAIL", AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[contains(@href,'tel:') or contains(@href,'mailto:') or contains(text(),'@') or contains(text(),'+')]"));
    private static final Locator BOTTOM_PRODUCT_SLIDER = new Locator("BOTTOM_PRODUCT_SLIDER", AppiumBy.xpath("//*[contains(@class, 'Carousel_slider')]"));
    private static final Locator BOTTOM_PRODUCT_LIST_NAMES = new Locator("BOTTOM_PRODUCT_LIST_NAMES", AppiumBy.xpath("//*[contains(@class,'product-name')]"));
    private static final Locator BREAD_CRUMB_NAV = new Locator("BREAD_CRUMB_NAV", AppiumBy.xpath("//*[contains(@class, 'Breadcrumb_breadcrumb')]//*[contains(@style,'text-decoration')]"));
    private static final Locator  PRODUCT_IMAGE = new Locator("PRODUCT_IMAGE", AppiumBy.xpath("//img[@data-testid='main-image']"));
    private static final Locator  TAB_CONTAINER = new Locator("TAB_CONTAINER",AppiumBy.xpath("//*[contains(@class,'-CoQhG_tab_list')] | //*[contains(@class,'Tabs_tab_list__')]"));
    private static final Locator LPG_BANNER_FOOTER = new Locator("LPG_BANNER_FOOTER", AppiumBy.xpath("//*[@data-testid='banner-lpg']"));
    private static final Locator LPG_BOTTOMSHEET = new Locator("LPG_BOTTOMSHEET", AppiumBy.xpath("(//span[contains(text(), \"Jaminan Harga Termurah\") or contains(text(), \"Lowest Price Guarantee\")])[2]"));
    private static final Locator LOYALTY_BADGE = new Locator("LOYALTY_BADGE", AppiumBy.xpath("//*[contains(@class,'LoyaltyLabel_loyalty_icon__')]"));
    private static final Locator CAMPAIGN_LABEL = new Locator("CAMPAIGN_LABEL", AppiumBy.xpath("//div[contains(@class,'PackageSelectionDefault_campaign_label')]"));
    private static final Locator PDP_FOOTER_FINAL_PRICE = new Locator("PDP_FOOTER_FINAL_PRICE", AppiumBy.xpath("//*[contains(@class,'FooterPDP_footer_content')]//*[contains(@class,'Text_variant_price')]"));
    private static final Locator PDP_FOOTER_CROSS_PRICE = new Locator("PDP_FOOTER_CROSS_PRICE", AppiumBy.xpath("//*[contains(@class,'FooterPDP_footer_content')]//span[contains(@style,'line-through')]"));
    private static final Locator  PARTNER_NAME = new Locator("PARTNER_NAME", AppiumBy.xpath("//*[contains(@class,'SpecialInfo_partner_name')]"));
    private static final Locator BENEFIT_TITLE = new Locator("BENEFIT_TITLE", AppiumBy.xpath("//*[contains(@class,'SpecialInfo_product_benefit')]/a/span"));
    private static final Locator INFO_PAGE_TITLE = new Locator("INFO_PAGE_TITLE", AppiumBy.xpath("//*[@data-testid='info-page-content']//*[contains(@class,'FullText_title_text')]"));
    private static final Locator  PDP_RECOMMENDATION_PRODUCT_CARDS_PRICE = new Locator("PDP_RECOMMENDATION_PRODUCT_CARDS_PRICE", AppiumBy.xpath("//*[contains(@class,'Carousel_slide')]//*[contains(@class,'PriceModule')]//*[contains(@class,'Text_variant_price')]"));
    private static final Locator  PDP_PACKAGE_CARD_PRICE_TEXT = new Locator("PDP_PACKAGE_CARD_PRICE_TEXT", AppiumBy.xpath("//*[contains(@class,'PackageSelection_package_info')]/span[contains(@style,'line-through')] | //*[contains(@class,'package_price')]"));
    private static final Locator  PACKAGE_DETAIL_PRICE_TEXT = new Locator("PACKAGE_DETAIL_PRICE_TEXT", AppiumBy.xpath("//*[contains(@class,'SectionGeneralInfo_main_price_wrapper')]//span[contains(@style,'line-through')] | //*[contains(@class,'SectionGeneralInfo_package_price')]//span[contains(@class,'Text_variant_alert')] | //*[contains(@class,'PackageDetailPrice_price')]//div"));
    private static final Locator  LOYALTY_SECTION = new Locator("LOYALTY_SECTION", AppiumBy.xpath("//*[contains(@class,'SectionLoyalty')]"));
    private static final Locator LOYALTY_LABEL = new Locator("LOYALTY_LABEL", AppiumBy.xpath("//*[contains(@class,'LoyaltyLabel')]"));
    private static final Locator  REVIEW_SUMMARY = new Locator("REVIEW_SUMMARY", AppiumBy.xpath("//*[contains(@class,'SectionSummary_text_container')]"));
    private  static final Locator  MORE_PACKAGES = new Locator("MORE_PACKAGES", AppiumBy.xpath("//*[contains(@class,'PackageCollapse_expandable_section')]//button"));
    private static final Locator PROMO_LOGIN_NOW_BUTTON = new Locator("PROMO_LOGIN_NOW_BUTTON", AppiumBy.xpath("(//*[contains(@class,'PopupBenefit_popup_footer_bottomsheet')]//button)[1]"));
    private static final Locator LOGIN_OVERLAY = new Locator("LOGIN_OVERLAY", new By.ByXPath("//*[contains(@class,'LoginOverlay_item_overlay')]//button[text()='Log in']"));
    private static final Locator  PROMO_BENEFITS_SECTION = new Locator("PROMO_BENEFITS_SECTION", AppiumBy.xpath("//*[contains(@class,'BenefitSection_benefit_section')]//*[contains(@class,'BenefitSection_title')]//*[contains(@class,'text')]"));
    private static final Locator  PROMO_BENEFITS_CARD = new Locator("PROMO_BENEFITS_CARD", AppiumBy.xpath("//*[contains(@class,'BenefitSection_card_title_icon')]"));
    private static final Locator BOOK_BUTTON_FOOTER = new Locator("BOOK_BUTTON_FOOTER", AppiumBy.xpath("(//*[contains(@class, 'Button') and contains(@class, 'primary')])[last()]"));
    private static final Locator HEART_ICON = new Locator("HEART_ICON", AppiumBy.xpath("//*[@data-testid='btn-headerpdp-wishlist']//*"));
    private static final Locator HEART_ICON_COLOR_RED = new Locator("HEART_ICON_COLOR_RED", AppiumBy.xpath("//*[@data-testid='btn-headerpdp-wishlist']//*[local-name()='circle' and @fill-opacity='1']"));
    private static final Locator HEART_ICON_COLOR_GREY = new Locator("HEART_ICON_COLOR_GREY", AppiumBy.xpath("//*[@data-testid='btn-headerpdp-wishlist']//*[local-name()='circle' and @fill-opacity='0']"));
    public static final Locator MANAGE_WISHLIST = new Locator("MANAGE_WISHLIST", AppiumBy.xpath("//button[normalize-space()='Atur' or normalize-space()='Manage']"));
    private static final Locator VIEW_ALL_WISHLIST_BUTTON = new Locator("VIEW_ALL_WISHLIST_BUTTON", AppiumBy.xpath("//*[contains(@class,'ViewAllWishlistButton')]"));
    private static final Locator LOYALTY_ICON_PACKAGE_DETAILS = new Locator("LOYALTY_ICON_PACKAGE_DETAILS", AppiumBy.xpath("//*[contains(@data-testid, 'loyalty-icon')]"));
    private static final Locator SHOW_ALL_PACKAGES_BUTTON = new Locator("SHOW_ALL_PACKAGES_BUTTON", new By.ByXPath("//span[contains(@class,'Text_text__') and (contains(normalize-space(text()),'Show all packages') or contains(normalize-space(text()),'Tampilkan semua paket'))]"));
    private static final Locator MAX_QUANTITY_MESSAGE = new Locator("MAX_QUANTITY_MESSAGE", new By.ByXPath("//div[contains(@class,'Snackbar_text_content') and (contains(text(),'maximum number') or contains(text(),'maksimal'))]"));
    private static final Locator FREE_SEATING_LABEL = new Locator("FREE_SEATING_LABEL", By.xpath("//div[contains(@class, 'PackageBenefit_feature_with_dots') and contains(text(), 'Free seating')]"));

    private Locator validityText(int index) {
        return new Locator("VALIDITY_TEXT", AppiumBy.xpath("(//div[@data-testid='package-card'])["+index+"]//div[contains(@class,'PackageBenefit_feature')]//span[contains(text(),'Berlaku') or contains(text(),'Valid')]"));
    }

    public String getValidityText(int index){
        Locator validityTextLocator = validityText(index);
        Optional<WebElement> validityElement = driverTools.waitForElementToAppear(validityTextLocator,2);
        String validityText;
        if(validityElement.isPresent()){
            validityText = driverTools.getText(validityElement.orElseThrow());
        }else{
            WebElement retryValidityElement = driverTools.swipeToElement(validityTextLocator, Swipe.UP,2).orElseThrow();
            validityText = driverTools.getText(retryValidityElement);
        }

        return validityText;
    }

    @SneakyThrows
    public boolean isLpgBannerFooterDisplayed() {
        boolean result = driverTools.waitForElementToAppear(LPG_BANNER_FOOTER).isPresent();
        return result;
    }

    @SneakyThrows
    public boolean isLpgBottomSheetDisplayed() {
        boolean result = driverTools.waitForElementToAppear(LPG_BOTTOMSHEET).isPresent();
        return result;
    }

    public String getProductType() {
        String result = driverTools.waitForElementToAppear(PRODUCT_CATEGORY_WEBVIEW,5).orElseThrow().getText();
        return result;
    }

    public String getProductName() {
        WebElement webElement = driverTools.waitForElementToAppear(PRODUCT_NAME_WEBVIEW, 15).orElseThrow();
        String result = driverTools.getText(webElement);
        return result;
    }

    public String getProductName(String slug) {
        driverTools.switchToWebViewUrl(slug);
        WebElement webElement = driverTools.waitForElementToAppear(PRODUCT_NAME_WEBVIEW, 15).orElseThrow();
        String result = driverTools.getText(webElement);
        return result;
    }

    public String getProductName(int webViewIndex) {
        driverTools.switchToWebViewIndex(webViewIndex);
        logger.log("Current url: "+driverTools.getCurrentURL());
        WebElement webElement = driverTools.waitForElementToPresence(PRODUCT_NAME_WEBVIEW, 5).orElseThrow();
        String result = driverTools.getText(webElement);
        return result;
    }

    public void clickBack() {
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToPresence(BACK_BUTTON_WEBVIEW, 5).orElseThrow());
    }

    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", AppiumBy.xpath("//*[@data-testid='seo-title']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", AppiumBy.xpath("//*[contains(@class,'HeaderPDP_floating') and @aria-label='back-button']"));
    private static final Locator SHARE_PRODUCT = new Locator("SHARE_PRODUCT", AppiumBy.xpath("//button[contains(@class,'HeaderPDP_floating_icon') and @aria-label='share-button']"));
    private static final Locator COPY_BUTTON = new Locator("COPY_BUTTON", AppiumBy.xpath("(//button[contains(@class,'SocialShare')])[4]"));

    @SneakyThrows
    public String getTTDName() {
        driverTools.waitForElementToDisappear(LOADING_STATE, 15);
        driverTools.switchToWebViewIndex(1);
        String productName = driverTools.getText(PRODUCT_NAME).orElseThrow();
        return productName;
    }

    @SneakyThrows
    public void clickBackButton() {
        driverTools.click(BACK_BUTTON);
    }

    public  void openHoursSection(){
        driverTools.waitForElementToPresence(HOUR_SECTION_WEBVIEW,30).orElseThrow();;
        driverTools.clickWithJavascriptExecutor(driverTools.waitForElementToBeClickable(HOUR_SECTION_WEBVIEW).orElseThrow());
    }

    public  boolean isBottomSheetOpen(String titleEN, String titleIN){
        Locator openHours = bottomSheetLocator(titleEN, titleIN);
        driverTools.waitForElementToAppear(openHours,10);
        boolean isBottomSheetDisplayed = driverTools.isElementDisplayed(openHours);
        return isBottomSheetDisplayed;

    }
    private Locator bottomSheetLocator(String titleEN, String titleIN) {
        return new Locator(titleEN,AppiumBy.xpath("//*[contains(@class,'bottom_sheet')]//*[contains(text(),'"+titleEN+"') or contains(text(),'"+titleIN+"')]"));
    }
    public  void closeSection(){
        if(driverTools.waitForElementToAppear(CLOSE_SECTION_BUTTON_WEBVIEW).isPresent()){
            driverTools.click(CLOSE_SECTION_BUTTON_WEBVIEW);
        }else{
            driverTools.back();
        }
    }

    public String checkShareProductButton(){
        driverTools.waitForElementToAppear(SHARE_PRODUCT);
        Assertion.assertThat("Share link button is visible", driverTools.isElementDisplayed(SHARE_PRODUCT), is(true));
        driverTools.click(SHARE_PRODUCT);
        driverTools.scrollIntoWebView(COPY_BUTTON);
        driverTools.waitForElementToAppear(COPY_BUTTON);
        driverTools.click(COPY_BUTTON);
        String copiedURL = driverTools.getCopiedUrlFromClipboard();
        logger.log("URL has been copied: "+ copiedURL);
        return copiedURL;
    }

    public boolean openCopiedLink(String url) {
        if (url.startsWith("https://m.")) {
            String cleanUrl = url.replace("https://m.", "");
            Deeplink homeDeeplink = new Deeplink("Home",
                    "tiket://" + cleanUrl,
                    "tiket://" + cleanUrl,
                    "tiket://" + cleanUrl);
            driverTools.deeplink(homeDeeplink);
            return true;
        }
        return false;
    }


    public boolean checkLabel(String labelName) {
        boolean present = driverTools.swipeToElement(getLabelPDPLocator(labelName), Swipe.UP, 5).isPresent();
        return present;
    }

    private Locator getLabelPDPLocator(String labelName) {
        if(labelName.contains("/")){
            String labelEng = labelName.split("/")[0];
            String labelInd = labelName.split("/")[1];
            return new Locator("PDP_LABEL", AppiumBy.xpath("//span[contains(@class,'SectionHighlight_instant_pass_text') and contains(text(), '"+labelEng+"') or contains(text(), '"+labelInd+"')] | //*[contains(@class,'SpecialInfo_instant_pass_text') and contains(text(), '"+labelEng+"') or contains(text(), '"+labelInd+"')]"));
        }
        return new Locator("PDP_LABEL", AppiumBy.xpath("//span[contains(@class,'SectionHighlight_instant_pass_text') and contains(text(), '"+labelName+"')] | //*[contains(@class,'SpecialInfo_instant_pass_text') and contains(text(), '"+labelName+"')]"));
    }

    public void goToPackagesList(){
        driverTools.waitForElementToAppear(HOUR_SECTION_WEBVIEW);
        driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON_NONPACKAGE);
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET_BUTTON_NONPACKAGE);
    }
    public void selectFirstPackage() {
        driverTools.clickWithJavascriptExecutor(FIRST_AVAILABLE_PACKAGE);
    }


    public void clickBookButtonFooter(){
        driverTools.click(BOOK_BUTTON_FOOTER);
    }

    public  boolean packageListShown(){
        boolean result = driverTools.waitForElementToPresence(SELECT_TICKET_BUTTON).isPresent();
        return result;
    }

    public void chooseTicket(){
        driverTools.waitForElementToBeClickable(SELECT_TICKET_BUTTON);
        List<WebElement> webElementList = driverTools.elements(SELECT_TICKET_BUTTON).orElseThrow();
        int randomInt = generateRandomInt(0, webElementList.size() - 1);
        WebElement selectedPackage = webElementList.get(randomInt);
        driverTools.scrollIntoWebView(selectedPackage);
        driverTools.clickWithJavascriptExecutor(selectedPackage);
        if (driverTools.isElementDisplayed(PACKAGE_OPTION)) {
            driverTools.scrollIntoWebView(PACKAGE_OPTION);
            driverTools.clickWithJavascriptExecutor(PACKAGE_OPTION);
            driverTools.scrollIntoWebView(selectedPackage);
            driverTools.clickWithJavascriptExecutor(selectedPackage);
        }
    }

    public  void chooseTicket(int packageNumber, boolean isMorePackage){
        if (isMorePackage) clickMorePackages();
        driverTools.waitForElementToAppear(getSelectTicket(packageNumber),10);
        try {
            driverTools.click(getSelectTicket(packageNumber));
        }catch (Exception e){
            driverTools.clickWithJavascriptExecutor(getSelectTicket(packageNumber));
        }
        if (driverTools.isElementDisplayed(PACKAGE_OPTION)) {
            driverTools.click(PACKAGE_OPTION);
            WebElement packageBookButton = driverTools.waitForElementToBeClickable(PACKAGE_BOOK_BUTTON).orElseThrow();
            driverTools.clickWithJavascriptExecutor(packageBookButton);
        }
    }

    public  void chooseTicket(int packageNumber){
        clickMorePackages();
        driverTools.waitForElementToAppear(getSelectTicket(packageNumber),10);
        try {
            driverTools.click(getSelectTicket(packageNumber));
        }catch (Exception e){
            driverTools.clickWithJavascriptExecutor(getSelectTicket(packageNumber));
        }
        if (driverTools.isElementDisplayed(PACKAGE_OPTION)) {
            driverTools.scrollIntoWebView(PACKAGE_OPTION);
            driverTools.clickWithJavascriptExecutor(PACKAGE_OPTION);
            driverTools.scrollIntoWebView(getSelectTicket(packageNumber));
            driverTools.clickWithJavascriptExecutor(getSelectTicket(packageNumber));
        }
    }

    private Locator getMipPackageRadio(int packageNumber) {
        return new Locator("MIP_PACKAGE_RADIO_" + packageNumber, AppiumBy.xpath(
                "(//*[contains(@class,'PackageSelectionMIPStandard_package_card_mip_wrapper')]//label[contains(@class,'Radio_radio')])[" + packageNumber + "]"));
    }

    private static final Locator MIP_SELECT_PACKAGE_BUTTON = new Locator("MIP_SELECT_PACKAGE_BUTTON", AppiumBy.xpath(
            "//*[contains(@class,'FooterMultiInventoryPasses')]//button"));

    public void chooseTicketWithMorePackages(int packageNumber) {
        if (Configs.PLATFORM != Platform.ANDROID && driverTools.isElementDisplayed(getMipPackageRadio(packageNumber))) {
            // Multi Inventory Passes PDP: radio package + footer "Select Package" button
            driverTools.scrollIntoWebView(getMipPackageRadio(packageNumber));
            driverTools.clickWithJavascriptExecutor(getMipPackageRadio(packageNumber));
            driverTools.scrollIntoWebView(MIP_SELECT_PACKAGE_BUTTON);
            driverTools.clickWithJavascriptExecutor(MIP_SELECT_PACKAGE_BUTTON);
            return;
        }
        clickMorePackages();
        Locator selectTicket = getSelectTicketWithMorePackages(packageNumber);
        driverTools.switchToNative();
        try {
            WebElement ticketOption = driverTools.waitForElementToPresence(selectTicket, 10).orElseThrow();
            if (!ticketOption.isDisplayed()) {
                ticketOption = driverTools.swipeToElement(selectTicket, Swipe.UP, 5)
                        .orElseGet(() -> driverTools.element(selectTicket).orElseThrow());
            }
            driverTools.click(ticketOption);

            driverTools.waitForElementToPresence(PACKAGE_SELECT_NEXT_BUTTON, 10).orElseThrow();
            WebElement nextButton = driverTools.swipeToElement(PACKAGE_SELECT_NEXT_BUTTON, Swipe.UP, 3)
                    .orElseGet(() -> driverTools.element(PACKAGE_SELECT_NEXT_BUTTON).orElseThrow());
            driverTools.click(nextButton);
        } finally {
            driverTools.switchToWebView();
        }
    }

    public void chooseTicketCampaign(String packageName, String campaignName) {
        driverTools.scrollIntoWebView(getSelectTicketCampaign(packageName, campaignName));
        WebElement element = driverTools.element(getSelectTicketCampaign(packageName, campaignName)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    public void clickMorePackages() {
        if (driverTools.isElementDisplayed(MORE_PACKAGES)) {
            driverTools.scrollIntoWebView(MORE_PACKAGES);
            driverTools.clickWithJavascriptExecutor(MORE_PACKAGES);
        } else {
            logger.log("More packages button is not there");
        }
    }

    public void chooseTicketWithRefundable() {
        driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON_REFUNDABLE);
        driverTools.scrollUntilElementFound(SELECT_TICKET_BUTTON_REFUNDABLE);
        Assertion.assertThat("100% refundable label is visible", driverTools.isElementDisplayed(SELECT_TICKET_BUTTON_REFUNDABLE), is(true));
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET_BUTTON_REFUNDABLE);
    }

    public void openTicketDetailWithExpiredDate() {
        driverTools.scrollIntoWebView(SELECT_DETAIL_BUTTON_EXPIRED_DATE);
        driverTools.clickWithJavascriptExecutor(SELECT_DETAIL_BUTTON_EXPIRED_DATE);
    }

    private Locator getSelectTicket(int packageNumber) {
        return new Locator("SELECT_TICKET_BUTTON_"+packageNumber, AppiumBy.xpath("(//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[not(@disabled) and contains(@class,'Button_variant_primary')]//*[contains(text(),'Select') or contains(text(),'Pilih')])["+packageNumber+"]"));
    }

    private Locator getSelectTicketWithMorePackages(int packageNumber) {
        if (Configs.PLATFORM == Platform.ANDROID) {
            return new Locator("SELECT_TICKET_BUTTON_" + packageNumber, AppiumBy.xpath(
                    "//android.widget.TextView[@text='Select Package' or @text='Pilih Paket']/following::android.widget.RadioButton[" + packageNumber + "]"));
        }
        return new Locator("SELECT_TICKET_BUTTON_"+packageNumber, AppiumBy.xpath("(//*[contains(@class,'PackageCollapse_card_list_wrapper')]//*[not(@disabled) and contains(@class,'Button_variant_primary')]//*[contains(text(),'Select') or contains(text(),'Pilih')])["+packageNumber+"]"));
    }

    private Locator getSelectTicket(String packageName) {
        String titleCondition;
        if (packageName != null && packageName.contains("/")) {
            String[] parts = packageName.split("/", 2);
            String en = parts[0].trim();
            String id = parts[1].trim();
            titleCondition = "(contains(normalize-space(.),'" + en + "') or contains(normalize-space(.),'" + id + "'))";
        }
        else {
            titleCondition = "contains(normalize-space(.),'" + String.valueOf(packageName).trim() + "')";
        }
        String xpath = "//*[@data-testid='package-card'" + " and .//*[contains(@class,'PackageCard_package_title') and " + titleCondition + "]" + "]//button[" + "not(@disabled)" + " and contains(@class,'Button_variant_primary')" + "]";

        return new Locator("SELECT_TICKET_BUTTON_" + packageName, AppiumBy.xpath(xpath));
    }
    public void chooseTicketRandom() {
        List<WebElement> packageName = driverTools.elements(SELECT_TICKET_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(packageName.get(generateRandomInt(0,packageName.size()-1)));
    }

    public void chooseTicketWithLoyalty() {
        driverTools.scrollIntoWebView(SELECT_TICKET_BUTTON_LOYALTY);
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET_BUTTON_LOYALTY);
    }

    public String getPackageName(int index){
        WebElement packageName = driverTools.waitForElementToPresence(packageName(index)).orElseThrow();
        String result = driverTools.getText(packageName);
        return result;
    }

    private Locator packageName(int index) {
        return new Locator("PACKAGE_NAME", AppiumBy.xpath("(//*[@data-testid='package-card']//*[contains(@class,'package_title')])["+index+"]"));
    }

    public String getCrossPrice(int index) {
        Optional<WebElement> crossPriceElement = driverTools.waitForElementToPresence(crossPrice(index));
        if (crossPriceElement.isPresent()) {
            String crossPriceText = driverTools.getText(crossPriceElement.orElseThrow());
            return crossPriceText;
        } else {
            return "";
        }
    }

    private Locator crossPrice(int index) {
        return new Locator("CROSS_PRICE", AppiumBy.xpath("(//*[contains(@class,'package_info')]//*[contains(@class,'Text_text')])["+index+"]"));
    }

    public  String getFinalPrice(int index){
        WebElement finalPrice = driverTools.waitForElementToPresence(finalPrice(index)).orElseThrow();
        String result = driverTools.getText(finalPrice);
        return result;
    }

    private Locator finalPrice(int index) {
        return new Locator("FINAL_PRICE", AppiumBy.xpath("(//*[@data-testid='package-card']//*[contains(@class,'Text_variant_price')])[" + index + "]"));
    }

    private Locator campaignLabel(int index) {
        return new Locator("CAMPAIGN_LABEL", AppiumBy.xpath("(//*[contains(@class,'PackageSelection_package_list')]//*[contains(@class,'card_container ')])["+index+"]//*[contains(@class,'PackageSelection_campaign_label')] | //*[contains(@class,'PackageCollapse_card_list_wrapper')]/child::*[" + index + "]//*[contains(@class,'PackageSelection_campaign_label')]"));
    }

    public  String getCampaignLabel(int index){
        Optional<WebElement> campaignLabelElement = driverTools.element(campaignLabel(index));
        if (campaignLabelElement.isPresent()) {
            String result = driverTools.getText(campaignLabelElement.orElseThrow());
            return result;
        } else {
            return "";
        }
    }

    public String getCampaignLabel() {
        driverTools.waitForElementToPresence(CAMPAIGN_LABEL, 20);
        driverTools.scrollIntoWebView(CAMPAIGN_LABEL);
        String result = driverTools.getText(CAMPAIGN_LABEL).orElseThrow();
        return result;
    }

    public  void clickDetailButton(int index){
        Locator detailButtonLocator = detailButton(index);
        WebElement detailButton = driverTools.swipeToElement(detailButtonLocator,Swipe.UP,2).orElseThrow();
        driverTools.clickWithJavascriptExecutor(detailButton);
    }

    public  void clickDetailButton(String name){
        Locator detailButtonLocator = detailButton(name);
        WebElement detailButton = driverTools.swipeToElement(detailButtonLocator,Swipe.UP,2).orElseThrow();
        driverTools.scrollIntoWebView(detailButtonLocator);
        driverTools.clickWithJavascriptExecutor(detailButton);
    }

    private Locator detailButton(int index) {
        return new Locator("DETAIL_BUTTON", AppiumBy.xpath("(//*[@data-testid='package-card'])["+index+"]//*[contains(@class,'btn_see_all')]"));
    }

    private Locator detailButton(String name) {
        return new Locator("DETAIL_BUTTON", AppiumBy.xpath("//*[contains(@class, 'PackageCard_package_title') and contains(text(),'"+name+"')]//parent::*//following-sibling::*[contains(@class, 'PackageCard_btn_see_all')]"));
    }

    public  boolean getTheTabSection() {
        if (driverTools.waitForElementToAppear(SELECT_TICKET_BUTTON_NONPACKAGE, 2).isPresent()) {
            driverTools.click(SELECT_TICKET_BUTTON_NONPACKAGE);
        } else {
            driverTools.swipeUp();
        }
        boolean pdpSectionVisible = false;
        int totalTabButton = driverTools.elements(LIST_TAB_BUTTON).orElseThrow().size();

        for (int i = 0; i < totalTabButton; i++) {
            String sectionTitle = clickTabButton(i);
            if (sectionTitle.equals("Reviews")) sectionTitle = "Review";
            pdpSectionVisible = isPdpSectionVisible(sectionTitle);
        }
        return pdpSectionVisible;
    }
    private String clickTabButton(int index) {
        int list = index + 1;

        String tabButtonLabel = String.valueOf(driverTools.getText(listTabButton(list)))
                .replaceAll("^Optional\\[(.*?)\\]$", "$1");

        logger.log("tab button on the \"" + list + "\" list is \"" + tabButtonLabel + "\"");

        driverTools.swipeToElement(visibleTabButton(tabButtonLabel), Swipe.LEFT, 3);
        driverTools.click(visibleTabButton(tabButtonLabel));
        return tabButtonLabel;
    }
    private Locator visibleTabButton(String tabButtonLabel) {
        return new Locator("VISIBLE_TAB_BUTTON", AppiumBy.xpath("//*[contains(@class,'Tabs_tab_text') and contains(text(),'" + tabButtonLabel + "')]"));
    }
    private Locator listTabButton(int index) {
        return new Locator("LIST_TAB_BUTTON", AppiumBy.xpath("(//*[contains(@class,'Tabs_tab_text')])[" + index + "]"));
    }
    private Locator titleSection(String tabButtonLabel) {
        if (tabButtonLabel.equals("Reviews")) tabButtonLabel = "Review";
        return new Locator("TITLE_SECTION", AppiumBy.xpath("//span[contains(@class,'Tabs_tab_text') and contains(., "+tabButtonLabel+")]"));
    }
    private Locator bodySection(String tabButtonLabel) {
        if (tabButtonLabel.equals("Reviews")) {
            return new Locator("BODY_SECTION", AppiumBy.xpath("//h2[contains(@class,'Text-module__text')]"));
        }

        return new Locator("BODY_SECTION", AppiumBy.xpath("//*[contains(@class,'Text_text__') and contains(@class,'Text_size_h3') and not(contains(@class,'variant_alert')) and contains(.,"+tabButtonLabel+")]"));
    }
    private boolean isPdpSectionVisible(String tabButtonLabel) {
        boolean sectionTitle;
        boolean sectionBody;
        if (tabButtonLabel.equals("Ringkasan") || tabButtonLabel.equals("Overview")) {
            sectionTitle = driverTools.isElementDisplayed(PRODUCT_NAME_WEBVIEW);
            sectionBody = sectionTitle;
        } else if (tabButtonLabel.equals("Lainnya") || tabButtonLabel.equals("More")) {
            sectionTitle = driverTools.isElementDisplayed(OTHER_INFO_SECTION);
            sectionBody = sectionTitle;
        } else {
            sectionTitle = driverTools.isElementDisplayed(titleSection(tabButtonLabel));
            sectionBody = driverTools.isElementDisplayed(bodySection(tabButtonLabel));
        }

        if (!sectionTitle) {
            logger.softWarning("\"" + tabButtonLabel + "\" is not showing as a TITLE on the PDP section");
        } else {
            logger.log("\"" + tabButtonLabel + "\" is showing as a TITLE on the PDP section");
        }

        if (!sectionBody) {
            logger.softWarning("\"" + tabButtonLabel + "\" is not showing as a BODY on the PDP section");
        } else {
            logger.log("\"" + tabButtonLabel + "\" is showing as a BODY on the PDP section");
        }
        return sectionTitle && sectionBody;
    }

    public  void clickTabSection(String titleId, String titleEn, String direction){
        driverTools.swipeUp(3);

        Swipe swipe;
        if (direction.equals("right")) {
            logger.log("Swipe right");
            swipe = Swipe.RIGHT;
        } else {
            logger.log("Swipe left");
            swipe = Swipe.LEFT;
        }

        WebElement tabTitle = driverTools.swipeToElement(tabButton(titleId, titleEn),TAB_CONTAINER, swipe, 7).orElseThrow();
        driverTools.click(tabTitle);

    }
    private Locator tabButton(String titleId, String titleEn) {
        return new Locator("TAB_BUTTON", AppiumBy.xpath("//span[contains(@class, 'Tabs_tab_text') and (text()='"+titleEn+"' or text()='"+titleId+"')]"));
    }
    public void openAllReview(){
        driverTools.waitForElementToPresence(SELECT_TICKET_BUTTON);
        driverTools.waitForElementToAppear(SEE_ALL_REVIEW);
        try {
            driverTools.scrollIntoWebView(SEE_ALL_REVIEW);
            driverTools.clickWithJavascriptExecutor(driverTools.element(SEE_ALL_REVIEW).orElseThrow());
        } catch (Exception e) {
            logger.log("failed to scroll into see all review button: " + e.getMessage());
            logger.log("then, click the element directly");
            driverTools.click(SEE_ALL_REVIEW);
        }
    }

    public void openMoreInfoSection(String titleEN, String titleIN){
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(openMoreInfoSectionLocator(titleEN, titleIN));

    }
    private Locator openMoreInfoSectionLocator(String titleEN, String titleIN) {
        return new Locator(titleEN, AppiumBy.xpath("//*[contains(@class,'HcPVsG_text HcPVsG_size_b2') and (text()='"+titleEN+"' or text()='"+titleIN+"') or contains(@class,'Text_text__') and (contains(text(),'"+titleEN+"') or contains(text(),'"+titleIN+"'))]"));
    }
    public void openCalendar(){
        WebElement calendarButton = driverTools.waitForElementToAppear(CALENDAR_BUTTON, 30).orElseThrow();
        driverTools.click(calendarButton);
    }
    public void selectDateCalendarPopup(int date){
        TtdSelectedPackageDetailPageWeb<?> ttdSelectedPackageDetailPageWeb = TtdSelectedPackageDetailPageWeb.getInstance(session);
        Locator SELECTED_START_DATE = ttdSelectedPackageDetailPageWeb.selectedDate(date);

        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        logger.log("currentDay-> " + currentDay);
        logger.log("date-> " + date);

        if (date < currentDay) {
            int nextMonthInt;

            int currentMonthInt = currentDate.getMonthValue();
            if (currentMonthInt == 12) {
                nextMonthInt = currentMonthInt - 11;
            } else {
                nextMonthInt = currentMonthInt + 1;
            }
            String nextMonthEng = String.valueOf(Month.of(nextMonthInt));
            nextMonthEng = nextMonthEng.substring(0, 1).toUpperCase() + nextMonthEng.substring(1).toLowerCase();
            String nextMonthId = monthEngToId(nextMonthEng);

            Locator NEXT_MONTH = getNextMonthElement();

            if (driverTools.isElementDisplayed(NEXT_MONTH)) driverTools.click(NEXT_MONTH_BUTTON);
        }
        driverTools.click(SELECTED_START_DATE);
    }

    private static Locator getNextMonthElement() {
        return new Locator("NEXT_MONTH_ELEMENT", AppiumBy.xpath("//div[contains(@class,'CalendarAvailability_calendar')]//*[contains(@class,'Button_accessibility_button')]"));
    }

    private static Locator bottomSheetDate(String titleDay) {
        return new Locator("BOTTOM_SHEET_DATE", AppiumBy.xpath("//*[contains(@class,'date_card')]//*[contains(text(),'"+titleDay+"')]//following-sibling::*[last()]"));
    }

    public  String  getDatePackage(){
        WebElement dateButtonElement = driverTools.element(DATE_BUTTON).orElseThrow();
        String date = driverTools.getText(dateButtonElement);
        if (!date.matches(".*\\d.*")) {
            driverTools.click(SELECT_TICKET_BUTTON);
            date = driverTools.getText(bottomSheetDate(date)).orElseThrow();
            driverTools.click(CLOSE_SECTION_BUTTON_WEBVIEW);
        }
        logger.log("date-> " + date);
        return date;
    }
    public void goToLocationSection() {
        driverTools.scrollIntoWebView(LOCATION_SECTION);
        driverTools.waitForElementToAppear(LOCATION_SECTION).orElseThrow();
    }

    public  void openMapsDirection(){
        goToLocationSection();
        WebElement mapsDirection = driverTools.waitForElementToAppear(MAPS_DIRECTION_BUTTON).orElseThrow();
        driverTools.clickElementByCoordinate(mapsDirection);
    }

    public void clickHowToGetThere() {
        goToLocationSection();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(HOW_TO_GET_THERE_BUTTON);
    }

    public void clickContactDetails() {
        goToLocationSection();
        driverTools.clickOrFallbackToClickWithJavascriptExecutor(CONTACT_DETAILS_BUTTON);
    }

    public boolean isContactDetailsContentDisplayed() {
        return isBottomSheetOpen("Contact Details", "Detail Kontak")
                && driverTools.waitForElementToAppear(CONTACT_PHONE_OR_EMAIL, 10).isPresent();
    }

    public String chooseBenefit() {
        List<WebElement> benefitCards = driverTools.elements(BENEFIT_TITLE).orElseThrow();
        int totalBenefit = benefitCards.size();
        int selectedIndex = generateRandomInt(0, totalBenefit - 1);
        WebElement selectedBenefit = benefitCards.get(selectedIndex);
        String selectedBenefitName = driverTools.getText(selectedBenefit);
        logger.log("selectedBenefitName-> " + selectedBenefitName);
        driverTools.scrollIntoWebView(selectedBenefit);
        driverTools.clickWithJavascriptExecutor(selectedBenefit);
        return selectedBenefitName;
    }

    public  boolean goToBottomSection(){
        driverTools.scrollIntoWebView(BREAD_CRUMB_NAV);
        WebElement bottomProductVisible = driverTools.element(BOTTOM_PRODUCT_SLIDER).orElseThrow();
        boolean result = driverTools.isElementDisplayed(bottomProductVisible);
        driverTools.swipeUp();
        return result;
    }
    public String selectBottomProduct() {
        driverTools.scrollIntoWebView(BREAD_CRUMB_NAV);
        driverTools.swipeUp(3);
        List<WebElement> productName = driverTools.elements(BOTTOM_PRODUCT_LIST_NAMES, 20).orElseThrow();
        logger.log("** product name **-> " + productName.stream().map(WebElement::getText).toList());
        if(productName.size() > 0 && productName.get(0).getText().equalsIgnoreCase(driverTools.getText(PRODUCT_NAME_WEBVIEW).orElseThrow())) {
            logger.log("** first product is the same as current product, removing it from the list **");
            productName.remove(0);
        }
        int totalProduct = productName.size();
        if (totalProduct >= 3) totalProduct = 2;
        int selectedIndex = generateRandomInt(0, totalProduct-1);
        logger.log("**index got**-> " + selectedIndex);
        WebElement selectedProduct = productName.get(selectedIndex);
        String selectedProductName = driverTools.getText(selectedProduct);

        logger.log("selectedProductName-> " + selectedProductName);
        driverTools.scrollIntoWebView(selectedProduct);
        driverTools.click(selectedProduct);
        driverTools.waitForElementToDisappear(selectedProduct, 10);
        return selectedProductName;
    }

    public boolean verifyDisplayedCurrency(String currency, String sectionType) {
        switch(sectionType.toLowerCase()) {
            case "display price":
                return verifyDisplayedCurrencyDisplayPrice(currency);
            case "pdp recommendation":
                return verifyDisplayedCurrencyPdpRecommendation(currency);
            case "package list":
                return verifyDisplayedCurrencyPackageList(currency);
            case "package detail":
                return verifyDisplayedCurrencyPackageDetail(currency);
            default:
                logger.fail("There is no option for sectionType '"+ sectionType +"'");
        }

        return false;
    }

    private boolean verifyDisplayedCurrencyPackageDetail(String currency) {

        List<String> priceTexts = driverTools.getTexts(PACKAGE_DETAIL_PRICE_TEXT);
        logger.log("PACKAGE_DETAIL_PRICE_TEXT -> " + Arrays.toString(priceTexts.toArray(new String[0])));


        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    private boolean verifyDisplayedCurrencyPackageList(String currency) {
        goToPackagesList();


        driverTools.waitForElementToAppear(PDP_PACKAGE_CARD_PRICE_TEXT);
        List<String> priceTexts = driverTools.getTexts(PDP_PACKAGE_CARD_PRICE_TEXT);
        logger.log("PDP_PACKAGE_CARD_PRICE_TEXT -> " + Arrays.toString(priceTexts.toArray(new String[0])));


        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    public List<String> getPackagesListPrice() {
        List<String> packagePrices = driverTools.getTexts(PDP_PACKAGE_CARD_PRICE_TEXT);
        Assertion.assertThat("Package prices are displayed", packagePrices,is(not(empty())));
        logger.log("Prices: "+packagePrices);
        return packagePrices;
    }

    private boolean verifyDisplayedCurrencyPdpRecommendation(String currency) {
        goToBottomSection();


        List<String> priceTexts = driverTools.getTexts(PDP_RECOMMENDATION_PRODUCT_CARDS_PRICE);
        logger.log("PDP_RECOMMENDATION_PRODUCT_CARDS_PRICE -> " + Arrays.toString(priceTexts.toArray(new String[0])));


        return CoreFunctions.checkCurrencyOnStringList(currency, priceTexts);
    }

    private boolean verifyDisplayedCurrencyDisplayPrice(String currency) {

        driverTools.waitForElementToAppear(PDP_FOOTER_FINAL_PRICE);
        boolean result = driverTools.getText(PDP_FOOTER_FINAL_PRICE).orElseThrow().toLowerCase().contains(currency.toLowerCase());


        return result;
    }

    public void clickProductPartner() {

        driverTools.scrollIntoWebView(PARTNER_NAME);
        driverTools.clickWithJavascriptExecutor(PARTNER_NAME);

    }

    public boolean verifyLoyaltySectionExists(){
        boolean isLoyaltySectionExists = driverTools.isElementPresent(LOYALTY_SECTION);
        return isLoyaltySectionExists;
    }

    public boolean verifyLoyaltyPackageExists(){
        boolean isLoyaltySectionExists = driverTools.isElementPresent(LOYALTY_LABEL);
        return isLoyaltySectionExists;
    }

    /**
     * This method is used to retrieve the displayed price text from the product detail page.
     *
     * @return String Returns the displayed price text.
     */
    public String getFooterFinalPriceText() {
        driverTools.refreshWebpage();
        driverTools.waitForElementToAppear(PDP_FOOTER_FINAL_PRICE);
        String result = driverTools.getText(PDP_FOOTER_FINAL_PRICE).orElseThrow();

        return result;
    }

    public String getFooterCrossPriceText() {

        driverTools.waitForElementToAppear(PDP_FOOTER_CROSS_PRICE);
        String result = driverTools.getText(PDP_FOOTER_CROSS_PRICE).orElseThrow();

        return result;
    }

    /**
     * Validates the review section's presence and functionality on the product detail page,
     * including review card navigation(Swipe left and right) and "See All Review" button interaction.
     */
    public void verifyReviewSectionExists() {
        driverTools.waitForElementToAppear(REVIEW_SECTION_CARD);
        driverTools.scrollIntoWebView(REVIEW_SECTION_CARD);
        boolean isReviewSectionExists = driverTools.isElementDisplayed(REVIEW_SECTION_CARD);
        Assertion.assertThat("Review section is displayed", isReviewSectionExists, is(true));
        List<WebElement> reviewCards = driverTools.elements(REVIEW_SECTION_CARD).orElseThrow();
        int totalSwipe = reviewCards.size();
        driverTools.waitForElementToAppear(SEE_ALL_REVIEW);
        driverTools.swipeToElement(REVIEW_SECTION_CARD,Swipe.LEFT,totalSwipe-1);
        Assertion.assertThat("First review section card is not visible after swiping left", driverTools.isElementDisplayed(reviewCards.get(totalSwipe - 1)), is(false));
        driverTools.swipeToElement(REVIEW_SECTION_CARD,Swipe.RIGHT,totalSwipe);
        Assertion.assertThat("Last review section card is not visible after swiping right", driverTools.isElementDisplayed(reviewCards.get(0)), is(true));
        boolean isSeeAllReviewExists = driverTools.isElementDisplayed(SEE_ALL_REVIEW);
        Assertion.assertThat("See all review is displayed", isSeeAllReviewExists, is(true));
        driverTools.clickWithJavascriptExecutor(SEE_ALL_REVIEW);
        driverTools.switchToWebViewUrl("review");
        driverTools.waitForElementToAppear(USER_RATING_REVIEW_PAGE,10);
        boolean seeAllReview = driverTools.isElementDisplayed(USER_RATING_REVIEW_PAGE);
        Assertion.assertThat("See all review is clicked", seeAllReview, is(true));
    }

    public boolean verifyPromoBenefitsSection() {
        driverTools.waitForElementToAppear(PROMO_BENEFITS_SECTION);
        boolean promoBenefitsCards = driverTools.elements(PROMO_BENEFITS_CARD).isPresent();
        logger.log("Promo benefits cards exists: " + promoBenefitsCards);
        boolean isPromoBenefitsSectionExists = driverTools.isElementDisplayed(PROMO_BENEFITS_SECTION);
        logger.log("Promo benefits section exists: " + isPromoBenefitsSectionExists);
        driverTools.click(PROMO_BENEFITS_SECTION);
        boolean isPromoBenefitsPopupDisplayed = isPromoBenefitsPopupDisplayed();
        logger.log("Promo benefits popup exists: " + isPromoBenefitsPopupDisplayed);
        return isPromoBenefitsSectionExists && promoBenefitsCards && isPromoBenefitsPopupDisplayed;
    }

    public void openLoginFromClaimBenefit() {
        driverTools.waitForElementToPresence(PROMO_BENEFITS_SECTION);
        driverTools.clickWithJavascriptExecutor(PROMO_BENEFITS_SECTION);
        driverTools.waitForElementToPresence(PROMO_LOGIN_NOW_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(PROMO_LOGIN_NOW_BUTTON);
    }

    public String getPromoBenefitTitle() {
        WebElement promoBenefitTitle = driverTools.waitForElementToAppear(PROMO_BENEFITS_SECTION).orElseThrow();
        String result = driverTools.getText(promoBenefitTitle);
        return result;
    }

    public void openLoginFromRequiredLogin() {
        driverTools.waitForElementToPresence(LOGIN_OVERLAY).orElseThrow();
        driverTools.clickWithJavascriptExecutor(LOGIN_OVERLAY);
    }

    public boolean isLoginOverlayDisplayed() {
        boolean isLoginRequired = driverTools.waitForElementToPresence(LOGIN_OVERLAY).isPresent();
        return isLoginRequired;
    }

    public boolean swipeToDestinationSectionBottom() {
        driverTools.scrollIntoWebView(BREAD_CRUMB_NAV);
        boolean isBottomProductSliderExists = driverTools.isElementDisplayed(BREAD_CRUMB_NAV);
        driverTools.swipeUp(2);
        return isBottomProductSliderExists;
    }

    private Locator getDestinationBreadcrumb(String level){
        return new Locator("DESTINATION_BREADCRUMB", AppiumBy.xpath("//a[contains(@href, '"+level.toLowerCase()+"') and contains(@class,'text')]"));
    }

    public String selectDestinationByHierarchy(String level){
        String destinationText = "";
        if(driverTools.isElementDisplayed(BREAD_CRUMB_NAV)){
           destinationText = String.valueOf(driverTools.getText(getDestinationBreadcrumb(level.toLowerCase())));
            driverTools.clickWithJavascriptExecutor(getDestinationBreadcrumb(level.toLowerCase()));
        }
        else {
            logger.fail("Destination breadcrumb for level " + level + " is not displayed");
        }
        driverTools.waitForElementToDisappear(getDestinationBreadcrumb(level));
        return destinationText;
    }

    public void clickHeartIconAndManageWishList(){
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToPresence(PRODUCT_NAME_WEBVIEW);
        WebElement heartIcon = driverTools.waitForElementToPresence(HEART_ICON).orElseThrow();
        driverTools.clickElementByCoordinate(heartIcon);
        driverTools.waitForElementToPresence(MANAGE_WISHLIST);
        driverTools.clickWithJavascriptExecutor(MANAGE_WISHLIST);
    }

    public void clickSeeAllWishlistAfterAdding(){
        driverTools.click(VIEW_ALL_WISHLIST_BUTTON);
    }

    public void TurnOffHeartIcon(){
        Assertion.assertThat("Heart icon is red colored", driverTools.isElementDisplayed(HEART_ICON_COLOR_RED), is(true));
        WebElement heartIcon = driverTools.waitForElementToPresence(HEART_ICON).orElseThrow();
        driverTools.clickElementByCoordinate(heartIcon);
        driverTools.waitForWebToLoad();
        Assertion.assertThat("Heart icon is grey colored", driverTools.isElementDisplayed(HEART_ICON_COLOR_GREY), is(true));
    }

    public boolean verifyLoyaltyOrCampaignInPackageDetails(String tier) {
        boolean result = driverTools.isElementDisplayed(getLoyaltyPackageDetailsTier(tier));
        return result;
    }

    private Locator getLoyaltyPackageDetailsTier(String tier) {
        String className = "";

        switch (tier) {
            case "silver": className = "level-1";
                break;
            case "gold": className = "level-2";
                break;
            case "platinum": className = "level-3";
                break;
            case "diamond": className = "level-4";
                break;
            default: throw new NotImplementedException("tier not implemented");
        }

        return new Locator("LOYALTY_ICON_PACKAGE_DETAILS", AppiumBy.xpath("//*[contains(@data-testid, 'loyalty-icon-"+className+"')]"));
    }

    public Locator getPackageNameCard(String name) {
        return new Locator("PACKAGE_CARD", AppiumBy.xpath("//*[contains(@class, 'PackageCard_package_title') and contains(text(),'"+name+"')]"));
    }

    public void findPackage(String packageName) {
        clickMorePackages();
        Locator packageCard = getPackageNameCard(packageName);
        driverTools.waitForElementToPresence(packageCard);
        driverTools.scrollIntoWebView(packageCard);
    }

    public void clickSeeAllPackagesButton() {
        driverTools.waitForElementToAppear(SHOW_ALL_PACKAGES_BUTTON);
        driverTools.scrollIntoWebView(SHOW_ALL_PACKAGES_BUTTON);
        driverTools.clickWithJavascriptExecutor(SHOW_ALL_PACKAGES_BUTTON);
    }

    public void clickAreaBreadcrumb(String area) {
        Locator areaLocator = getAreaOnBreadCrumb(area);
        driverTools.scrollIntoWebView(areaLocator);
        driverTools.click(areaLocator);
    }

    private static Locator getAreaOnBreadCrumb(String area) {
        return new Locator("AREA_ON_BREADCRUMB", AppiumBy.xpath("//*[contains(@class, 'Breadcrumb_breadcrumb')]//a[contains(text(), '"+area.toLowerCase()+"')]"));
    }

    public boolean isMaxQuantityMessageDisplayed() {
        return driverTools.waitForElementToPresence(MAX_QUANTITY_MESSAGE, 5).isPresent();
    }

    public void clickBuyTicket() {
        driverTools.waitForElementToAppear(BUY_TICKET_NOW);
        driverTools.clickWithJavascriptExecutor(BUY_TICKET_NOW);
        driverTools.waitForElementToDisappear(BUY_TICKET_NOW, 10);
    }

    public void selectPackage(String packageName) {
        Locator packageSelectButton = getSelectTicket(packageName);
        driverTools.scrollIntoWebView(packageSelectButton);
        driverTools.waitForElementToBeClickable(packageSelectButton);
        driverTools.clickWithJavascriptExecutor(packageSelectButton);
    }

    public boolean isFreeSeatingDisplayed() {
        driverTools.waitForElementToAppear(FREE_SEATING_LABEL, 5);
        return driverTools.isElementDisplayed(FREE_SEATING_LABEL);
    }
}

