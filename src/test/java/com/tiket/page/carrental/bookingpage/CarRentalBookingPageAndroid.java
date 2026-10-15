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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class CarRentalBookingPageAndroid <T extends WebDriver> extends CarRentalBookingPage<T> {

    private static final Locator SELECT_LOCATION_PICKUP = new Locator("SELECT_LOCATION_PICKUP", AppiumBy.xpath("(//*[contains(@resource-id,'body_view')])[1]"));
    private static final Locator SELECT_LOCATION_DROP_OFF = new Locator("SELECT_LOCATION_DROP_OFF", AppiumBy.xpath("(//*[contains(@resource-id,'body_view')])[2]"));
    private static final Locator DELIVER_LOCATION = new Locator("DELIVER_LOCATION", AppiumBy.xpath("//*[@text='Deliver to Your Location' or @text='Diantar ke Lokasimu']"));
    private static final Locator PICKUP_LOCATION = new Locator("PICKUP_LOCATION", AppiumBy.xpath("//*[@text='Pick Up at Your Location' or @text='Diambil di Lokasimu']"));
    private static final Locator ENTER_SEARCH = new Locator("ENTER_SEARCH", AppiumBy.id("tds_search_et"));
    private static final Locator SET_LOCATION = new Locator("SET_LOCATION", AppiumBy.id("tds_button_view"));
    private static final Locator DROP_OFF_SAME_LOCATION_SWITCH = new Locator("RETURN_SWITCH", AppiumBy.id("tds_toggle_group"));
    private static final Locator BTN_CONTINUE = new Locator("BTN_CONTINUE", AppiumBy.id("btn_continue"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", AppiumBy.id("tds_snackbar_root"));
    private static final Locator CONTINUE_TO_PAYMENT = new Locator("CONTINUE_TO_PAYMENT", AppiumBy.id("tds_button_view"));
    private static final Locator CONFIRM_PAYMENT = new Locator("CONFIRM_PAYMENT", AppiumBy.xpath("//*[@text='Lanjut Bayar' or @text='Continue to Payment']"));
    private static final Locator SALUTATION_RADIO_BTN = new Locator("SALUTATION", AppiumBy.xpath("//*[contains(@resource-id,'view_booking_form_edit_contact')]//android.widget.TextView[@text='Tuan' or @text='Mr']"));
    private static final Locator FULL_NAME = new Locator("FULL_NAME", AppiumBy.id("tds_box_autocomplete_input_text"));
    private static final Locator PHONE_NUMBER_FIELD = new Locator("PHONE_NUMBER_FIELD", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[2]"));
    private static final Locator EMAIL_FIELD = new Locator("EMAIL_FIELD", AppiumBy.xpath("(//*[contains(@resource-id,'tds_box_input_text')])[3]"));
    private static final Locator ADD_EXTRA_FACILITIES_BUTTON = new Locator("ADD_EXTRA_FACILITIES_BUTTON", AppiumBy.xpath("//*[@text='Permintaan Khusus' or @text='Special Request']//following-sibling::*[contains(@resource-id,'iv_add')]"));
    private static final Locator EXTRA_FACILITIES_NOTES = new Locator("EXTRA_FACILITIES_NOTES", AppiumBy.id("tds_box_input_text"));
    private static final Locator SAVE_EXTRA_FACILITIES_BTN = new Locator("SAVE_EXTRA_FACILITIES_BTN", AppiumBy.id("tds_button_view"));
    private static final Locator PASSENGER_DETAIL_HEADER = new Locator("PASSENGER_DETAIL_HEADER", AppiumBy.id("tv_header"));
    private static final Locator AGREE_TOGGLE = new Locator("AGREE_TOGGLE", AppiumBy.id("cb_toggle"));
    private static final Locator ALL_IN_PACKAGE = new Locator("ALL_IN_PACKAGE", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and contains(@text,'All-in')]//following-sibling::*[contains(@resource-id,'iv_add')]"));
    private static final Locator ALL_IN_PACKAGE_TOGGLE = new Locator("ALL_IN_PACKAGE_TOGGLE", AppiumBy.xpath("//*[contains(@resource-id,'tg_booking_later')]//*[contains(@resource-id,'tds_toggle_group')]"));
    private static final Locator PRICE = new Locator("PRICE", AppiumBy.id("tv_price"));
    private static final Locator BTN_SAVE = new Locator("BTN_SAVE", AppiumBy.id("tds_button_view"));
    private static final Locator SPECIAL_REQUEST = new Locator("SPECIAL_REQUEST", AppiumBy.xpath("//*[contains(@resource-id,'tv_title') and (contains(@text,'Special Request') or contains(@text,'Permintaan Khusus'))]//following-sibling::*[contains(@resource-id,'iv_add')]"));
    private static final Locator PICKUP_AREA = new Locator("PICKUP_AREA", AppiumBy.xpath("(//*[contains(@resource-id,'tv_notes')]//following-sibling::*)//*[contains(@resource-id,'tv_area') and contains(@text,'Area')]"));
    private static final Locator DROP_OFF_AREA = new Locator("DROP_OFF_AREA", AppiumBy.xpath("(//*[contains(@resource-id,'toggle')]//following-sibling::*)//*[contains(@resource-id,'tv_area') and contains(@text,'Area')]"));
    private static final Locator PRICE_DETAIL_OPEN = new Locator("PRICE_DETAIL_OPEN", AppiumBy.xpath("//*[contains(@resource-id,'vg_price_and_button')]//*[contains(@resource-id,'tv_price')]"));
    private static final Locator CLOSE_BTN = new Locator("CLOSE_BTN", AppiumBy.id("iv_close"));
    private static final Locator PICKUP_LOCATION_2ND_PAGE = new Locator("PICKUP_LOCATION_2ND_PAGE", AppiumBy.id("tv_pickup_location"));
    private static final Locator DROP_OFF_LOCATION_2ND_PAGE = new Locator("DROP_OFF_LOCATION_2ND_PAGE", AppiumBy.id("tv_drop_off_location"));
    private static final Locator RENTAL_AREA_LOCATION_2ND_PAGE = new Locator("RENTAL_AREA_LOCATION_2ND_PAGE", AppiumBy.id("tv_rent_area_desc"));
    private static final Locator SPECIAL_REQUEST_2ND_PAGE = new Locator("SPECIAL_REQUEST_2ND_PAGE", AppiumBy.id("tv_special_request_desc"));
    private static final Locator PASSENGER_DETAIL_TITLE = new Locator("PASSENGER_DETAIL_TITLE", AppiumBy.xpath("//*[contains(@resource-id,'tv_header') and (contains(@text,'Passenger Details') or contains(@text,'Detail Pemesan'))]"));
    private static final Locator CONTACT_FIELD = new Locator("CONTACT_FIELD", AppiumBy.id("group_field_contact_detail"));
    private static final Locator CHECKBOX_EXTRA_FACILITIES = new Locator("CHECKBOX_EXTRA_FACILITIES", AppiumBy.xpath("(//*[contains(@resource-id,'cb_checkbox')])[1]"));
    private static final Locator PICKUP_SEARCH_PRICE = new Locator("PICKUP_SEARCH_PRICE", AppiumBy.id("tv_fee"));
    private static final Locator PICKUP_PRICE = new Locator("PICKUP_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'group_field_pickup')]//*[contains(@resource-id,'tv_fee')]"));
    private static final Locator DROPOFF_PRICE = new Locator("PICKUP_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'group_field_drop_off')]//*[contains(@resource-id,'tv_fee')]"));
    private static final Locator AREA_PRICE = new Locator("AREA_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'tv_covered_area')]//following-sibling::*[contains(@resource-id,'tv_price')]"));
    private static final Locator PRICES = new Locator("PRICES", AppiumBy.id("tv_right_text"));
    private static final Locator TOTAL_PRICE = new Locator("TOTAL_PRICE", AppiumBy.xpath("//*[contains(@resource-id,'fl_price')]//*[contains(@resource-id,'tv_price')]"));

    public CarRentalBookingPageAndroid(Session<T> session) {
        super(session);
    }

    private Locator getPickupLocationLocator(String pickUpLocation){
        return new Locator("PICKUP_LOCATION",AppiumBy.xpath("(//*[contains(@text,'"+pickUpLocation+"')])[2]"));
    }

    @Override
    public void searchChoosePickUpLocation(String pickupLocation) {
        final Locator PICKUP_LOCATION = getPickupLocationLocator(pickupLocation);
        driverTools.click(SELECT_LOCATION_PICKUP);
        driverTools.waitForElementToAppear(DELIVER_LOCATION,5).ifPresent(driverTools::click);
        driverTools.sendKeys(ENTER_SEARCH, pickupLocation);
        WebElement pickupLocationElement = driverTools.waitForElementToAppear(PICKUP_LOCATION,10).orElseThrow();
        driverTools.click(pickupLocationElement);
    }

    @Override
    public void confirmPickUpLocation() {
        WebElement setPickUpLocationElement = driverTools.waitForElementToAppear(SET_LOCATION,15).orElseThrow();
        driverTools.click(setPickUpLocationElement);
    }

    @Override
    public void clickSameReturn() {
        WebElement dropOfSameLocationSwitch = driverTools.waitForElementToAppear(DROP_OFF_SAME_LOCATION_SWITCH,10).orElseThrow();
        driverTools.click(dropOfSameLocationSwitch);
    }

    @Override
    public String addAllInPackage(String currency){
        WebElement allInPacElement = driverTools.swipeToElement(ALL_IN_PACKAGE,Swipe.UP,3).orElseThrow();
        driverTools.click(allInPacElement);
        WebElement toggleSwitch = driverTools.waitForElementToAppear(ALL_IN_PACKAGE_TOGGLE).orElseThrow();
        driverTools.click(toggleSwitch);
        WebElement totalPrice = driverTools.waitForElementToAppear(PRICE).orElseThrow();
        String price = driverTools.getText(totalPrice);
        driverTools.click(BTN_SAVE);
        return price;
    }

    private String getString(String type, Locator locator){
        WebElement detailElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        String detail = driverTools.getText(detailElement);
        logger.log(type + " : " + detail);
        return detail;
    }

    private Locator getTitleOfSpecialReq(int index){
        return new Locator("SPECIAL_REQUEST_TITTLE_" + index,AppiumBy.xpath("(//*[contains(@resource-id,'cb_checkbox')]/preceding-sibling::*[contains(@resource-id,'vg_text')]/*[contains(@resource-id,'tv_text')])["+index+"]"));
    }

    private Locator getPriceOfSpecialReq(int index,String currency){
        return new Locator("SPECIAL_REQUEST_PRICE_" + index,AppiumBy.xpath("(//*[contains(@resource-id,'tv_subtext')])["+index+"]"));
    }

    private Locator getCheckBoxOfSpecialReq(int index){
        return new Locator("SPECIAL_REQUEST_PRICE_" + index,AppiumBy.xpath("(//*[contains(@resource-id,'cb_checkbox')])["+index+"]"));
    }

    @Override
    public CarRentalDetail addSpecialRequest(String currency){
        CarRentalDetail rentalDetail = new CarRentalDetail();
        driverTools.swipeToElement(SPECIAL_REQUEST,Swipe.UP,3);
        driverTools.click(SPECIAL_REQUEST);

        rentalDetail.extraFacilities1Title = getString("Title 1",getTitleOfSpecialReq(1));
        rentalDetail.extraFacilities1Price = getString("Price 1",getPriceOfSpecialReq(1,currency));
        driverTools.click(getCheckBoxOfSpecialReq(1));

        rentalDetail.extraFacilities2Title = getString("Title 2",getTitleOfSpecialReq(2));
        rentalDetail.extraFacilities2Price = getString("Price 2",getPriceOfSpecialReq(2,currency));
        driverTools.click(getCheckBoxOfSpecialReq(2));

        rentalDetail.extraFacilitiesTotalPrice = driverTools.getText(PRICE).orElseThrow();

        driverTools.click(BTN_SAVE);
        return rentalDetail;
    }

    private Locator getPriceFromPriceDetail(String currency){
        return new Locator("PRICE_DETAIL",AppiumBy.xpath("//*[contains(@resource-id,'tv_right_text') and contains(@text,'"+currency+"')]"));
    }

    private boolean isDetailCorrect(String type, Locator locator){
        WebElement detailElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        boolean detail = driverTools.isElementDisplayed(detailElement);
        logger.log(type + "displayed : " + detail);
        return detail;
    }

    private boolean isDetailCorrectMatchWithText(String type, Locator locator, String expectedText){
        WebElement detailElement = driverTools.waitForElementToAppear(locator).orElseThrow();
        String actualText = driverTools.getText(detailElement);
        logger.log("expected text: " + expectedText);
        logger.log("actual text: " + actualText);

        boolean detail = actualText.contains(expectedText);
        logger.log(type + "displayed : " + detail);
        return detail;
    }

    @Override
    public String getPricePickupSearch(){
        String carPickupSearchPrice = driverTools.getText(PICKUP_SEARCH_PRICE).orElseThrow();
        logger.log("Car Pickup Search Price: "+ carPickupSearchPrice);
        return carPickupSearchPrice;
    }

    @Override
    public String getPricePickup(){
        String carPickupPrice = driverTools.getText(PICKUP_PRICE).orElseThrow();
        logger.log("Car Pickup Price: "+ carPickupPrice);
        return carPickupPrice;
    }

    @Override
    public String getPriceDropOff(){
        String carDropOffPrice = driverTools.getText(DROPOFF_PRICE).orElseThrow();
        logger.log("Car Dropoff Price: "+ carDropOffPrice);
        return carDropOffPrice;
    }

    @Override
    public String getPriceArea(){
        driverTools.swipeUp();
        String carAreaPrice = driverTools.getText(AREA_PRICE).orElseThrow();
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
        List<String> priceItems = new ArrayList<>(driverTools.getTexts(PRICES));
        logger.log("prices: "+priceItems);
        Assertion.assertThat("Prices exist", !priceItems.isEmpty(),is(true));
        WebElement totalPrice = driverTools.swipeToElement(PRICE,Swipe.UP,5).orElseThrow();
        String totalPriceText = driverTools.getText(totalPrice);
        priceItems.add(totalPriceText);
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
