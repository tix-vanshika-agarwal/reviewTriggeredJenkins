package com.tiket.page.ferry.FerrySearchresultPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Exception.DriverToolsException;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.core.Every;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class FerrySearchResultPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator FLEET_OPTION_PRICES = new Locator("FLEET_OPTION_PRICES",AppiumBy.xpath("//div[contains(@class,'ScheduleCard_price')]//span"));
    private static final Locator PRICE_DETAIL_CHEVRON = new Locator("PRICE_DETAIL_CHEVRON",AppiumBy.xpath("(//div[contains(@class,'ScheduleCard_price')]//div[contains(@class,'chevron')])[1]"));
    private static final Locator COACHMARK_OK_BTN = new Locator("COACHMARK_OK_BTN",AppiumBy.xpath("//button//span[contains(text(),'Ok') or contains(text(),'OK')]"));
    private static final Locator PRICE_DETAIL_COMPONENTS = new Locator("PRICE_DETAIL_COMPONENTS",AppiumBy.xpath("//div[contains(@class,'price_item')]//span[contains(@class,'PriceDetail_price')]"));
    private static final Locator PRICE_DETAIL_TOTAL_PRICE_VALUE = new Locator("PRICE_DETAIL_TOTAL_PRICE_VALUE",AppiumBy.xpath("//span[contains(@class,'PriceDetail_price')]//span"));
    private static final Locator SELECT_FLEET_BTN_1 = new Locator("SELECT_FLEET_BTN_1", AppiumBy.xpath("(//button[contains(@class,'ScheduleCard_select_button')])[1]"));
    private static final Locator CLOSE_PRICE_DETAIL_SRP = new Locator("CLOSE_PRICE_DETAIL_SRP", AppiumBy.xpath("//button[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator FERRY_CARD = new Locator("FERRY_CARD", AppiumBy.xpath("//section[contains(@class,'search_schedules')]/child::*"));
    private static final Locator BOTTOMSHEET_FERRY_CLASS_NAME = new Locator("BOTTOMSHEET_FERRY_CLASS_NAME", AppiumBy.xpath("//*[contains(@id,'modal-root')]//*[contains(@class,'PriceDetail_ship_class')]/child::span[1]"));
    private static final Locator FERRY_CARD_IMAGE = new Locator("FERRY_CARD_IMAGE", AppiumBy.xpath("(//div[contains(@class, 'ScheduleCard_multiple_images_container')]/div[contains(@class, 'ScheduleCard_multiple_images__')]/span/img)[1]"));
    private static final Locator FERRY_BOTTOM_IMAGES = new Locator("FERRY_BOTTOM_IMAGES", AppiumBy.xpath("//div[contains(@class, 'ImageThumbnailMobile_image_thumbnail_offset')]/button/img"));
    private static final Locator FERRY_IMAGES_COUNT = new Locator("FERRY_IMAGES_COUNT", AppiumBy.xpath("//div[contains(@class, 'Text_text__')]"));
    private static final Locator FERRY_IMAGE_INSIDE = new Locator("FERRY_IMAGE_INSIDE", AppiumBy.xpath("//div[contains(@class,'keen-slider__slide')]/img"));
    private static final Locator SRP_CHANGE_BTN = new Locator("SRP_CHANGE_BTN", AppiumBy.xpath("//button[contains(@class, 'Button_button') and contains(text(), 'Change') or contains(text(), 'Ubah')]"));
    private static final Locator SRP_HEADER_ORIGIN = new Locator("SRP_HEADER_ORIGIN", AppiumBy.xpath("//div[contains(@class,'SRPHeader_origin_destination__')]//*[contains(@class,'Text_text__')][1]"));
    private static final Locator SRP_HEADER_DESTINATION = new Locator("SRP_HEADER_DESTINATION", AppiumBy.xpath("//div[contains(@class,'SRPHeader_origin_destination__')]//*[contains(@class,'Text_text__')][2]"));
    private static final Locator SRP_HEADER_DATE = new Locator("SRP_HEADER_DATE", AppiumBy.xpath("//div[contains(@class,'SRPHeader_date_and_vehicle')]//*[contains(@class,'Text_text')][last()-1]"));
    private static final Locator SRP_HEADER_TICKET_TYPE = new Locator("SRP_HEADER_TICKET_TYPE", AppiumBy.xpath("//div[contains(@class,'SRPHeader_date_and_vehicle')]//*[contains(@class,'Text_text')][last()]"));
    private static final Locator SEARCH_FERRY_BTN = new Locator("SEARCH_FERRY_BTN", AppiumBy.xpath("//*[contains(@class,'SearchForm_submit_button__')]"));
    private static final Locator ADJUST_DATES = new Locator("ADJUST_DATES", AppiumBy.xpath("//button[contains(@class, 'ErrorState_button') and (normalize-space(text())='Adjust the dates' or normalize-space(text())='Ubah tanggal')]"));

    private static Map<String,String> ferryTicketTypeIdToEn = Map.of(
            "Mobil Penumpang", "Passenger Car",
            "Pejalan Kaki", "Pedestrian",
            "Sepeda", "Bicycle",
            "Truk Pickup", "Pickup Truck"
    );

    private static Map<String,String> ferryTicketTypeEnToId = Map.of(
            "Passenger Car", "Mobil Penumpang",
            "Pedestrian", "Pejalan Kaki",
            "Bicycle", "Sepeda",
            "Pickup Truck", "Truk Pickup"
    );
    public FerrySearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> FerrySearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case IOS -> {
                return new FerrySearchResultPageIOS<>(session);
            }
            case ANDROID -> {
                return new FerrySearchResultPageAndroid<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public boolean verifyPriceInSrpIsInIDR() {
        driverTools.switchToWebViewUrl("ferry/search");
        if (driverTools.waitForElementToAppear(FLEET_OPTION_PRICES, 2).isPresent()) {
            List<String> carTypePrices = driverTools.getTexts(FLEET_OPTION_PRICES);
            logger.log("DEBUG prices in SRP: " + carTypePrices);
            Assertion.assertThat("Verify all fleet in srp is in IDR", carTypePrices,
                    Every.everyItem(containsString("IDR")));
        } else {
            throw new DriverToolsException("No vendor availaible, Skipping assertion");
        }
        driverTools.switchToNative();
        return true;
    }

    public boolean verifyFirstFleetPriceDetailIDR() {
        driverTools.switchToWebViewUrl("ferry/search");
        driverTools.click(PRICE_DETAIL_CHEVRON);
        driverTools.waitForElementToAppear(PRICE_DETAIL_COMPONENTS);
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();
        // assertion price components should be in IDR
        Assertion.assertThat("Verify price components are in idr", priceComponentsInPriceDetail,
                Every.everyItem(anyOf(containsString("IDR"),equalTo("Termasuk"),equalTo("Included"))));
        Assertion.assertThat("Verify total price is in IDR", totalPrice, containsString("IDR"));

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPrice(n)).toList();// remove currency word from the value. Eg ->
                                                                        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
        driverTools.click(CLOSE_PRICE_DETAIL_SRP);
        driverTools.switchToNative();
        return true;
    }

    public void skipCoachMark() {
        driverTools.waitForElementToAppear(COACHMARK_OK_BTN,5);
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/ferry/search");
        if(driverTools.isElementDisplayed(COACHMARK_OK_BTN))
            driverTools.click(COACHMARK_OK_BTN);
        driverTools.switchToNative();
    }

    public void chooseFirstFleet() {
        driverTools.switchToWebViewUrl("ferry/search");
        driverTools.click(SELECT_FLEET_BTN_1);
        driverTools.switchToNative();
    }

    public Boolean verifyAllFerryCardPriceDetailBottomSheet() {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("ferry/search");
        driverTools.waitForElementToAppear(CLOSE_PRICE_DETAIL_SRP, 10);
        int cardCount = driverTools.elements(FERRY_CARD).orElseThrow().size();
        logger.log("cardCount -> " + cardCount);

        for(int i = 0; i < cardCount; i++) {
            driverTools.switchToWebViewUrl("ferry/search");
            driverTools.scrollIntoWebView(getFerryCardPriceLocator(i+1));
            String ferryCardClassName = driverTools.getText(getFerryCardClassNameLocator(i+1)).orElseThrow();
            String ferryCardTotalPrice = driverTools.getText(getFerryCardPriceLocator(i+1)).orElseThrow();

            driverTools.click(getFerryCardPriceChevronLocator(i+1));

            driverTools.waitForElementToAppear(BOTTOMSHEET_FERRY_CLASS_NAME,3);
            String bottomsheetClassName = driverTools.getText(BOTTOMSHEET_FERRY_CLASS_NAME).orElseThrow();
            driverTools.waitForElementToAppear(PRICE_DETAIL_TOTAL_PRICE_VALUE,3);
            String bottomsheetTotalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();

            Assertion.assertThat("Ferry card " + (i+1) + " Class name is same",bottomsheetClassName,is(ferryCardClassName));
            Assertion.assertThat("Ferry card " + (i+1) + " Total price amount is same",bottomsheetTotalPrice,is(ferryCardTotalPrice));

            verifyPriceDetail();

            driverTools.click(CLOSE_PRICE_DETAIL_SRP);
        }

        driverTools.switchToNative();
        return true;
    }

    private Locator getFerryCardPriceChevronLocator(int index) {
        return new Locator("FERRY_CARD_PRICE_CHEVRON_" + index, AppiumBy.xpath("(//*[contains(@class,'ScheduleCard_schedule')])[" + index + "]//*[contains(@class,'ScheduleCard_icon_chevron')]"));
    }

    private Locator getFerryCardPriceLocator(int index) {
        return new Locator("FERRY_CARD_PRICE_" + index, AppiumBy.xpath("(//*[contains(@class,'ScheduleCard_price_label_container')])[" + index + "]/*[2]"));
    }

    private Locator getFerryCardClassNameLocator(int index) {
        return new Locator("FERRY_CARD_CLASS_" + index, AppiumBy.xpath("(//p[contains(@class,'ScheduleCard_ship_class')])[" + index + "]"));
    }

    private void verifyPriceDetail() {
        List<String> priceComponentsInPriceDetail = driverTools.getTexts(PRICE_DETAIL_COMPONENTS);
        logger.log("number of price component present : " + priceComponentsInPriceDetail.size());
        String totalPrice = driverTools.getText(PRICE_DETAIL_TOTAL_PRICE_VALUE).orElseThrow();

        // assertion for total price same as manual sum calculation
        List<Double> priceComponentsNumbersValue = priceComponentsInPriceDetail.stream()
                .filter(n -> n.matches(".*[0-9]+.*"))
                .map(n -> CoreFunctions.convertPriceSafely(n)).toList();// remove currency word from the value. Eg ->
        // IDR 5000 into 5000 (with data type double)
        // sum price components
        double sumOfPriceDetail = CoreFunctions.sumUpDouble(priceComponentsNumbersValue);
        // compare sumOfPriceDetail with actual value in ui
        Assertion.assertThat("Verify total sum is the same", CoreFunctions.convertPriceSafely(totalPrice),
                equalTo(sumOfPriceDetail));
    }
     public void ClickFerryImageSRP(){
         driverTools.switchToWebViewUrl("/ferry/search");
        driverTools.click(FERRY_CARD_IMAGE);
        driverTools.switchToNative();
     }

    public boolean verifyImageContents() {
        driverTools.switchToWebViewUrl("/ferry/search");
        Optional<List<WebElement>> totalImagesByLocator = driverTools.elements(FERRY_BOTTOM_IMAGES);
        if (totalImagesByLocator.isPresent()) {
            int expectedImageCount = Integer.parseInt(String.valueOf(totalImagesByLocator.get().size()));
            String countText = String.valueOf(driverTools.getText(FERRY_IMAGES_COUNT));
            String[] parts = countText.split("/");
            int actualImageCount = parts.length > 1 ? Integer.parseInt(parts[1].replaceAll("\\D", "")) : 0;
            boolean isFerryImageGalleryDisplayed = driverTools.isElementDisplayed(FERRY_IMAGE_INSIDE);
            if (isFerryImageGalleryDisplayed) {
                driverTools.swipeLeft(getFerryImageLocatorByIndex(1));
                logger.log("User is able to swipe Image by left");
                driverTools.switchToNative();
            }
            return actualImageCount == expectedImageCount && isFerryImageGalleryDisplayed;
        } else {
            logger.log("No images found.");
            return false;
        }
    }

    private Locator getFerryImageLocatorByIndex(int index) {
        return new Locator("FERRY_IMAGE " + index, AppiumBy.xpath("(//div[contains(@class,'keen-slider__slide')]/img)[" + index + "]"));
    }

    public void clickChangeButton(){
        driverTools.switchToWebViewUrl("/ferry/search");
        driverTools.clickWithJavascriptExecutor(SRP_CHANGE_BTN);
        driverTools.switchToNative();
    }

    public boolean verifySRPHeaderContent(String origin, String destination, String vehicleType) {
        driverTools.switchToWebViewUrl("/ferry/search");
        driverTools.waitForElementToAppear(SRP_HEADER_ORIGIN,20);
        String originSRP = driverTools.getText(SRP_HEADER_ORIGIN).orElseThrow();
        String destinationSRP = driverTools.getText(SRP_HEADER_DESTINATION).orElseThrow();
        String ticketTypeSRP = driverTools.getText(SRP_HEADER_TICKET_TYPE).orElseThrow();


        String vehicleTypeEn = ferryTicketTypeIdToEn.get(vehicleType) == null ? vehicleType : ferryTicketTypeIdToEn.get(vehicleType);
        String vehicleTypeId = ferryTicketTypeEnToId.get(vehicleType) == null ? vehicleType : ferryTicketTypeEnToId.get(vehicleType);

        Assertion.assertThat("Origin matches", originSRP, is(origin));
        Assertion.assertThat("Destination matches", destinationSRP, is(destination));
        Assertion.assertThat("Date visible", driverTools.isElementDisplayed(SRP_HEADER_DATE), is(true));
        Assertion.assertThat("Ticket type matches", ticketTypeSRP, anyOf(containsString(vehicleTypeEn), containsString(vehicleTypeId)));

        driverTools.switchToNative();
        return true;
    }

    public void clickSearchFerryButton(){
        driverTools.switchToWebViewUrl("/ferry/search");
        driverTools.waitForElementToAppear(SEARCH_FERRY_BTN).ifPresent(driverTools::click);

        driverTools.switchToNative();
    }

    public boolean isFerryCardDisplayed() {
        driverTools.switchToWebViewUrl("/ferry/search");
        boolean isDisplayed = driverTools.isElementDisplayed(ADJUST_DATES);
        if (isDisplayed) {
            logger.log("No Ferry Card is displayed");
        }
        driverTools.switchToNative();
        return isDisplayed;
    }
}
