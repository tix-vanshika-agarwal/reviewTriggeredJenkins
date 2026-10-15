package com.tiket.web.page.nft.bus.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BusBookingFormPageMWeb <T extends WebDriver> extends BusBookingFormPageWeb<T> {

    private static final Locator SUMMARY_PRICE = new Locator("SUMMARY_PRICE", new By.ByXPath("//*[contains(@class, 'BookingFooter_price')]//*[contains(text(), 'IDR')]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'PriceContent_price_detail')]//*[contains(text(), 'IDR') and (not(contains(text(), 'Seat') or (contains(text(), 'Kursi'))))]"));
    private static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator PASSENGER_DETAIL_SAVE_BUTTON = new Locator("DOB_SAVE_BUTTON", new By.ByXPath("//*[contains(@class, 'Button_horizontal_padding') and (text()='Save' or text()='Simpan')]"));
    private static final Locator DOB_SAVE_BUTTON = new Locator("DOB_SAVE_BUTTON",By.xpath("//button[contains(@class, 'DatePicker_datepicker_button') and (text()='Simpan' or text()='Save')]"));
    private static final Locator ORDER_SUMMARY_LINK = new Locator("ORDER_SUMMARY_LINK", new By.ByXPath("//div[@data-testid='booking-review-card-depart']"));
    public BusBookingFormPageMWeb(Session<T> session) {
        super(session);
    }

    public boolean verifyPriceDetail() {
        String summaryPriceText = driverTools.waitForElementToAppear(SUMMARY_PRICE).orElseThrow().getText();
        double summaryPrice = CoreFunctions.convertPriceSafely(summaryPriceText);

        driverTools.click(SUMMARY_PRICE);
        driverTools.waitForElementToAppear(PRICE_DETAIL);

        List<WebElement> priceElements = driverTools.elements(PRICE_DETAIL).orElseThrow();
        List<Double> priceList = new ArrayList<>();
        for (WebElement priceElement: priceElements) {
            String priceText = driverTools.getText(priceElement);
            double price = CoreFunctions.convertPriceSafely(priceText);
            priceList.add(price);
        }

        double totalPrice = priceList.subList(0, priceList.size() - 1).stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        driverTools.click(CLOSE_PRICE_DETAIL);

        return totalPrice == summaryPrice;
    }

    @Override
    public void pressFillButtonPassengerDetail(int passengerIndex) {
        driverTools.waitForElementToAppear(getFillButtonPassengerDetailLocator(passengerIndex)).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    private Locator getFillButtonPassengerDetailLocator(int index) {
        return new Locator("FILL_PASSENGER_DETAIL_BUTTON_" + index,new By.ByXPath("(//div[contains(@class, 'PassengersDetailSection_card_inner')])["+ index + "]"));
    }

    @Override
    public Locator getPassengerDateOfBirthInput(int index) {
        index = 1;
        return new Locator("PASSENGER_DOB_INPUT_" + index, new By.ByXPath("(//input[(@id='date-of-birth' or @id='date-input') and @placeholder='Date of Birth'])[" + index + "]"));
    }

    @Override
    public void enterPassengerDateOfBirth(int index) {
        Locator PASSENGER_DOB_INPUT = getPassengerDateOfBirthInput(index);
        driverTools.waitForElementToAppear(PASSENGER_DOB_INPUT).ifPresent(driverTools::clickWithJavascriptExecutor);
        Locator PASSENGER_DOB_YEAR_TEXT = getPassengerDateOfBirthYearText(2011);
        WebElement yearText = driverTools.waitForElementToAppear(PASSENGER_DOB_YEAR_TEXT).orElseThrow();
        Locator PASSENGER_DOB_MONTH_TEXT = getPassengerDateOfBirthMonthText("April");
        WebElement monthText = driverTools.waitForElementToAppear(PASSENGER_DOB_MONTH_TEXT).orElseThrow();
        Locator PASSENGER_DOB_DAY_TEXT = getPassengerDateOfBirthDayText(1);
        WebElement dayText = driverTools.waitForElementToAppear(PASSENGER_DOB_DAY_TEXT).orElseThrow();
        driverTools.scrollIntoWebView(yearText);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DOB_YEAR_TEXT);
        driverTools.scrollIntoWebView(monthText);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DOB_MONTH_TEXT);
        driverTools.scrollIntoWebView(dayText);
        driverTools.clickWithJavascriptExecutor(PASSENGER_DOB_DAY_TEXT);
        driverTools.waitForElementToAppear(DOB_SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToAppear(PASSENGER_DETAIL_SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }
    @Override
    public void clickDetails() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_LINK);
        driverTools.clickWithJavascriptExecutor(ORDER_SUMMARY_LINK);
    }

    public Locator getPassengerDateOfBirthYearText(int year){
        return new Locator("PASSENGER_DOB_YEAR_TEXT_" + year,By.xpath("(//div[contains(@class, 'DateWheel_wheel__icBVd')])[3]//span[contains(@class, 'DateWheel_wheel_slide') and text()='" + year + "']"));
    }

    public Locator getPassengerDateOfBirthMonthText(String month){
        return new Locator("PASSENGER_DOB_MONTH_TEXT_" + month,By.xpath("(//div[contains(@class, 'DateWheel_wheel__icBVd')])[2]//span[contains(@class, 'DateWheel_wheel_slide') and text()='" + month + "']"));
    }
    public Locator getPassengerDateOfBirthDayText(int day) {
        return new Locator("PASSENGER_DOB_DAY_TEXT_" + day,By.xpath("(//div[contains(@class, 'DateWheel_wheel__icBVd')])[1]//span[contains(@class, 'DateWheel_wheel_slide') and text()='" + day + "']"));
    }
}
