package com.tiket.page.cartransfer.bookingform;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.page.nft.airport_transfer.booking_page.AirportTransferBookingPageWeb;
import io.appium.java_client.AppiumBy;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class CarTransferBookingPage <T extends WebDriver> extends BasePage<T>{

    public CarTransferBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CarTransferBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new CarTransferBookingPageAndroid <>(session);}
            case IOS -> {return new CarTransferBookingPageIOS <>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator BOOKING_PRICE = new Locator("BOOKING_PRICE", AppiumBy.xpath("//span[contains(@class, 'Text_variant_price')]"));
    public static final Locator FULLNAME = new Locator("FULLNAME",AppiumBy.xpath("//input[contains(@placeholder, 'Full Name According to ID/Passport') or contains(@placeholder, 'Nama Lengkap Sesuai')]"));
    public static final Locator MR_SALUTATION = new Locator("MR_SALUTATION",AppiumBy.xpath("//input[@type='radio' and @name='CONTACT_PERSON' and contains(@class, 'Radio_input')]"));
    public static final Locator EMAIL = new Locator("EMAIL",AppiumBy.xpath("//input[@type='email' and (contains(@placeholder, 'Email Address') or contains(@placeholder, 'Alamat Email'))]"));
    public static final Locator PHONE = new Locator("PHONE",AppiumBy.xpath("//input[@id='mobile-number' or contains(@placeholder, 'Nomor Ponsel')]"));
    private static final Locator DATE = new Locator("DATE", AppiumBy.xpath("//div[contains(@class, 'Sections_pickup_time_value')]//span[1]"));
    private static final Locator TIME = new Locator("TIME", AppiumBy.xpath("//span[contains(text(), ':')]"));
    private static final Locator SALUTATION = new Locator("SALUTATION", AppiumBy.xpath("//span[contains(@class, 'Radio_icon')]"));

    private String guestName = "";

    protected void setGuestName(String guestName){
        this.guestName = guestName;
    }

    public String getGuestName(){
        return guestName;
    }

    public String verifyPriceDetailIsInIDR() {
        WebElement bookingPriceElement = driverTools.waitForElementToAppear(BOOKING_PRICE).orElseThrow();
        return driverTools.getText(bookingPriceElement);
    }

    public void fillDetailOrder(String email,String phoneNumber) {
        String randomName = CoreFunctions.randomName();
        driverTools.swipeToElement(EMAIL, Swipe.UP,4).orElseThrow();
        driverTools.swipeToElement(MR_SALUTATION, Swipe.UP, 3);
        if(Configs.PLATFORM == Platform.ANDROID)
            driverTools.click(SALUTATION);
        else if (Configs.PLATFORM == Platform.IOS)
            driverTools.click(driverTools.lastElement(MR_SALUTATION).orElseThrow());
        driverTools.click(FULLNAME);
        driverTools.sendKeys(FULLNAME, randomName);
        driverTools.hideKeyboard(1);
        driverTools.sendKeys(EMAIL,email);
        driverTools.click(PHONE);
        driverTools.sendKeys(PHONE,phoneNumber);
        driverTools.hideKeyboard(1);
        setGuestName(randomName);
    }

    public void continueToPayment() {
        AirportTransferBookingPageWeb<?>  airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        airportTransferBookingPageWeb.goToPaymentPage();
    }

    public void openPriceDetail() {
        AirportTransferBookingPageWeb<?>  airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        airportTransferBookingPageWeb.openPriceBottomSheet();
    }

    public void closePriceDetail() {
        AirportTransferBookingPageWeb<?>  airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        airportTransferBookingPageWeb.closePriceBottomSheet();
    }

    public String verifyOrigin(String origin) {
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        return airportTransferBookingPageWeb.verifyOrigin(origin);
    }

    public String verifyDestination(String destination) {
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        return airportTransferBookingPageWeb.verifyDestination(destination);
    }

    public boolean verifyPriceDetail(String price, String carType) {
        AirportTransferBookingPageWeb<?> airportTransferBookingPageWeb = AirportTransferBookingPageWeb.getInstance(session);
        return airportTransferBookingPageWeb.verifyPriceDetail(price, carType);
    }

    public String verifyDate(String date) {
        WebElement tripDetailOrigin = driverTools.waitForElementToAppear(DATE, 2).orElseThrow();
        String fullDate = driverTools.getText(tripDetailOrigin);
        return fullDate.split(",\\s*")[1];
    }

    public String verifyTime(String time) {
        WebElement tripDetailOrigin = driverTools.waitForElementToAppear(TIME, 2).orElseThrow();
        return driverTools.getText(tripDetailOrigin);
    }
}
