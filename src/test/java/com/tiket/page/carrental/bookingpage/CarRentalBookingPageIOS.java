package com.tiket.page.carrental.bookingpage;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.carrental.reviewbookingpage.CarRentalReviewBookingPage;
import com.tiket.pojo.carrental.CarRentalDetail;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class CarRentalBookingPageIOS <T extends WebDriver> extends CarRentalBookingPage<T> {
    private static final Locator SELECT_LOCATION_PICKUP = new Locator("SELECT_LOCATION_PICKUP", AppiumBy.name("pickup_location_card"));
    private static final Locator SELECT_LOCATION_DROP_OFF = new Locator("SELECT_LOCATION_DROP_OFF", AppiumBy.xpath("(//XCUIElementTypeButton[@name='body_btn'])[2]"));
    private static final Locator DELIVER_LOCATION = new Locator("DELIVER_LOCATION", AppiumBy.name("deliver_to_your_location"));
    private static final Locator ENTER_PICKUP = new Locator("ENTER_PICKUP", AppiumBy.name("enter_pickup_location"));
    private static final Locator ENTER_DROP_OFF = new Locator("ENTER_DROP_OFF", AppiumBy.name("enter_dropoff_location"));
    private static final Locator SET_PICKUP_LOCATION = new Locator("SET_PICKUP_LOCATION", AppiumBy.name("btn_set_pickup_location"));
    private static final Locator SET_DROP_OFF_LOCATION = new Locator("SET_DROP_OFF_LOCATION", AppiumBy.name("btn_dropoff_location"));
    private static final Locator RETURN_SWITCH = new Locator("RETURN_SWITCH", AppiumBy.name("switch"));
    private static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE", AppiumBy.accessibilityId("continue_button"));
    private static final Locator MR_RADIO_BUTTON = new Locator("MR_RADIO_BUTTON", AppiumBy.name("title_radio"));
    private static final Locator FULL_NAME = new Locator("FULL_NAME", AppiumBy.name("fullName"));
    private static final Locator PHONE = new Locator("PHONE", AppiumBy.name("phone"));
    private static final Locator EMAIL = new Locator("EMAIL", AppiumBy.name("email"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", AppiumBy.name("continue_payment_button"));
    private static final Locator CONFIRM_PAYMENT = new Locator("CONFIRM_PAYMENT", AppiumBy.xpath("//XCUIElementTypeButton[contains(@label, 'Continue to payment') or contains(@label, 'Lanjut Bayar')]"));
    private static final Locator SPECIAL_REQUEST = new Locator("SPECIAL_REQUEST", AppiumBy.name("add_on_special_request"));
    private static final Locator FIELD_REQUEST = new Locator("FIELD_REQUEST", AppiumBy.name("notes"));
    private static final Locator BTN_SAVE = new Locator("BTN_SAVE", AppiumBy.name("save_btn"));
    private static final Locator BTN_SAVE_ALL_IN = new Locator("BTN_SAVE_ALL_IN", AppiumBy.name("save_btn"));
    private static final Locator CHECKBOX_EXTRA_FACILITIES = new Locator("CHECKBOX_EXTRA_FACILITIES", AppiumBy.id("checkbox"));
    private static final Locator TNC_CHECKBOX = new Locator("TNC_CHECKBOX", AppiumBy.id("checkbox (selected)"));
    private static final Locator ALL_IN_PACKAGE = new Locator("ALL_IN_PACKAGE", AppiumBy.name("add_on_all_package"));
    private static final Locator ALL_IN_PACKAGE_TOGGLE = new Locator("ALL_IN_PACKAGE_TOGGLE", AppiumBy.name("switch"));
    private static final Locator PICKUP_AREA = new Locator("PICKUP_AREA", AppiumBy.accessibilityId("pickup_zone"));
    private static final Locator DROP_OFF_AREA = new Locator("DROP_OFF_AREA", AppiumBy.name("dropoff_zone"));
    private static final Locator PRICE_DETAIL_OPEN = new Locator("PRICE_DETAIL_OPEN", AppiumBy.accessibilityId("cr_price_detail"));
    private static final Locator TOTAL_PRICE_PRICE_DETAIL = new Locator("TOTAL_PRICE_PRICE_DETAIL", AppiumBy.name("total_price"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.id("ic cancel grey"));
    private static final Locator PASSENGER_DETAIL_NAME = new Locator("PASSENGER_DETAIL_NAME", AppiumBy.accessibilityId("fullName"));
    private static final Locator PASSENGER_DETAIL_EMAIL = new Locator("PASSENGER_DETAIL_EMAIL", AppiumBy.accessibilityId("email"));
    private static final Locator PASSENGER_DETAIL_PHONE = new Locator("PASSENGER_DETAIL_PHONE", AppiumBy.accessibilityId("phone")); 
    private static final Locator CONTACT_DETAIL_LOGIN_CONDITION = new Locator("CONTACT_DETAIL_LOGIN_CONDITION", AppiumBy.accessibilityId("body_btn"));
    private static final Locator CLOSE_CONTATC_DETAIL_LOGIN_CONDITION = new Locator("CONTACT_DETAIL_LOGIN_CONDITION", AppiumBy.accessibilityId("ic cancel grey"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD",AppiumBy.accessibilityId("body_btn"));
    private static final Locator PRICE_ITEM = new Locator("PRICE_ITEM",AppiumBy.accessibilityId("item_price"));
    private static final Locator PICKUP_SEARCH_PRICE = new Locator("PICKUP_SEARCH_PRICE",AppiumBy.xpath("//*[contains(@label,'Area')]//following::*[contains(@label,'+')]"));
    private static final Locator PICKUP_PRICE = new Locator("PICKUP_PRICE",AppiumBy.accessibilityId("pickup_price"));
    private static final Locator DROPOFF_PRICE = new Locator("DROPOFF_PRICE",AppiumBy.accessibilityId("drop_off_price"));
    private static final Locator AREA_PRICE = new Locator("AREA_PRICE",AppiumBy.xpath("//*[@name='coverage_area']/following-sibling::*[contains(@label,'+')]"));
    private static final Locator COVERAGE_AREA = new Locator("COVERAGE_AREA",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='coverage_area']//parent::*"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE",AppiumBy.xpath("//*[@name='cr_price_detail']//preceding-sibling::XCUIElementTypeStaticText"));

    public CarRentalBookingPageIOS(Session<T> session) {
        super(session);
    }
    private Locator getPickupLocationLocator(String pickupLocation) {
        return new Locator("CHOOSE_LOCATION", AppiumBy.xpath("//*[@name='search_result' and contains(@label,'" + pickupLocation + "')]"));
    }

    @Override
    public void searchChoosePickUpLocation(String pickupLocation) {
        final Locator PICKUP_LOCATION = getPickupLocationLocator(pickupLocation);
        driverTools.click(SELECT_LOCATION_PICKUP);
        driverTools.waitForElementToAppear(DELIVER_LOCATION).ifPresent(driverTools::click);
        driverTools.sendKeys(ENTER_PICKUP, pickupLocation);
        driverTools.click(PICKUP_LOCATION);
    }

    @Override
    public void confirmPickUpLocation() {
        driverTools.click(SET_PICKUP_LOCATION);
    }

    @Override
    public void clickSameReturn() {
        driverTools.click(RETURN_SWITCH);
    }


    private void agreeTermsAndConditions(){
        driverTools.swipeToElement(CONFIRM_PAYMENT,Swipe.UP,5);
        driverTools.click(CONFIRM_PAYMENT);
    }

    @Override
    public String addAllInPackage(String currency){
        WebElement allInPacElement = driverTools.swipeToElement(ALL_IN_PACKAGE,Swipe.UP,3).orElseThrow();
        driverTools.click(allInPacElement);
        WebElement toggleSwitch = driverTools.waitForElementToAppear(ALL_IN_PACKAGE_TOGGLE).orElseThrow();
        driverTools.click(toggleSwitch);
        WebElement totalPrice = driverTools.waitForElementToAppear(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        String price = driverTools.getValueAttribute(totalPrice,"label");
        driverTools.click(BTN_SAVE_ALL_IN);
        return price;
    }

    private Locator getUsageAreaLocator(int day,int area){
        return new Locator("USAGE_AREA_DAY" + day + "_AREA_" + area,AppiumBy.xpath("(//XCUIElementTypeStaticText[@name='coverage_area' and contains(@label,'"+area+"')])["+day+"]"));
    }

    private Locator getTitleOfSpecialReq(int index){
        return new Locator("SPECIAL_REQUEST_TITTLE_" + index,AppiumBy.xpath("(//*[@name='special_request_item'])["+index+"]"));
    }

    private Locator getPriceOfSpecialReq(int index,String currency){
        return new Locator("SPECIAL_REQUEST_PRICE_" + index,AppiumBy.xpath("(//*[@name='special_request_price'])["+index+"]"));
    }

    private Locator getCheckBoxOfSpecialReq(int index){
        return new Locator("SPECIAL_REQUEST_PRICE_" + index,AppiumBy.xpath("(//XCUIElementTypeOther[contains(@name,'checkbox')])["+index+"]"));
    }

    private String getString(String type, Locator locator){
        WebElement detailElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        String detail = driverTools.getText(detailElement);
        logger.log(type + " : " + detail);
        return detail;
    }

    @Override
    public CarRentalDetail addSpecialRequest(String currency){
        CarRentalDetail rentalDetail = new CarRentalDetail();
        WebElement specialRequestElement = driverTools.swipeToElement(SPECIAL_REQUEST,Swipe.UP,5).orElseThrow();
        driverTools.click(specialRequestElement);

        rentalDetail.extraFacilities1Title = getString("Title 1",getTitleOfSpecialReq(1));
        rentalDetail.extraFacilities1Price = getString("Price 1",getPriceOfSpecialReq(1,currency));
        driverTools.click(getCheckBoxOfSpecialReq(1));

        rentalDetail.extraFacilities2Title = getString("Title 2",getTitleOfSpecialReq(2));
        rentalDetail.extraFacilities2Price = getString("Price 2",getPriceOfSpecialReq(2,currency));
        driverTools.click(getCheckBoxOfSpecialReq(2));

        rentalDetail.extraFacilitiesTotalPrice = driverTools.getValueAttribute(TOTAL_PRICE_PRICE_DETAIL,"label");

        driverTools.click(BTN_SAVE);
        return rentalDetail;
    }

    private Locator getPickupLocation2ndPage(String location){
        return new Locator("PICKUP_LOCATION",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='pickup_location' and contains(@label,'"+location+"')]"));
    }

    private Locator getDropOffLocation2ndPage(String location){
        return new Locator("DROP_OFF_LOCATION",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='dropoff_location' and contains(@label,'"+location+"')]"));
    }

    private Locator getRentalArea2ndPage(String location){
        return new Locator("RENTAL_AREA",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='area_detail' and contains(@label,'"+location+"')]"));
    }

    private Locator getSpecialRequest2ndPage(String specialReq){
        return new Locator("SPECIAL_REQUEST",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='special_request_detail' and contains(@label,'"+specialReq+"')]"));
    }

    private boolean isDetailCorrect(String type, Locator locator){
        WebElement detailElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        boolean detail = driverTools.isElementDisplayed(detailElement);
        logger.log(type + "displayed : " + detail);
        return detail;
    }

    @Override
    public String getPricePickupSearch(){
        driverTools.waitForElementToPresence(SET_PICKUP_LOCATION).orElseThrow();
        String carPickupSearchPrice = driverTools.getValueAttribute(driverTools.onScreenElement(PICKUP_SEARCH_PRICE),"label");
        logger.log("Car Pickup Search Price: "+ carPickupSearchPrice);
        return carPickupSearchPrice;
    }

    @Override
    public String getPricePickup(){
        driverTools.waitForElementToAppear(PICKUP_PRICE,5).orElseThrow();
        String carPickupPrice = driverTools.getValueAttribute(PICKUP_PRICE,"label");
        logger.log("Car Pickup Price: "+ carPickupPrice);
        return carPickupPrice;
    }

    @Override
    public String getPriceDropOff(){
        String carDropOffPrice = driverTools.getValueAttribute(DROPOFF_PRICE,"label");
        logger.log("Car Dropoff Price: "+ carDropOffPrice);
        return carDropOffPrice;
    }

    @Override
    public String getPriceArea(){
        driverTools.swipeUp();
        String carAreaPrice = driverTools.getValueAttribute(DROPOFF_PRICE,"label");
        logger.log("Car Area Price: "+ carAreaPrice);
        return carAreaPrice;
    }

    @Override
    public void clickPriceDetail(){
        driverTools.swipeToElement(PRICE_DETAIL_OPEN,Swipe.UP,4);
        driverTools.click(PRICE_DETAIL_OPEN);
    }

    @Override
    public List<String> getPriceDetail(){
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICE_ITEM));
        logger.log("prices: "+priceItems);
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        String totalPrice = driverTools.getText(TOTAL_PRICE_PRICE_DETAIL).orElseThrow();
        priceItems.add(totalPrice);
        logger.log("Prices: " + priceItems);
        return priceItems;
    }

    @Override
    public void closePriceDetail(){
        driverTools.click(CLOSE_BTN);
    }

    @Override
    public String getTotalPrice(){
        return driverTools.getText(TOTAL_PRICE).orElseThrow();
    }
}
