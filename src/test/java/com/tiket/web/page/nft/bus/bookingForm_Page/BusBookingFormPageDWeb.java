package com.tiket.web.page.nft.bus.bookingForm_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class BusBookingFormPageDWeb <T extends WebDriver> extends BusBookingFormPageWeb<T> {

    private static final Locator SUMMARY_PRICE = new Locator("SUMMARY_PRICE", new By.ByXPath("//*[contains(@class, 'BookingReviewCards_total_price')]//*[contains(text(), 'IDR')]"));
    private static final Locator PRICE_DETAIL = new Locator("PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'ModalBody')]//*[contains(text(), 'IDR') and (not(contains(text(), 'Seat') or (contains(text(), 'Kursi'))))]"));
    private static final Locator CLOSE_PRICE_DETAIL = new Locator("CLOSE_PRICE_DETAIL", new By.ByXPath("//*[contains(@class, 'Modal_modal_close')]"));
    private static final Locator BIRTH_YEAR_DROPDOWN = new Locator("BIRTH_YEAR_DROPDOWN", new By.ByXPath("//*[contains(@class, 'Navigation_year_menu_button')]"));
    private static final Locator BIRTH_YEAR = new Locator("BIRTH_YEAR", new By.ByXPath("//*[contains(@class, 'month-year-menus_button') and text()='2011']"));
    private static final Locator BIRTH_DAY = new Locator("BIRTH_DAY", new By.ByXPath("//span[text()='1']"));
    private static final Locator DOB_SAVE_BUTTON = new Locator("DOB_SAVE_BUTTON", new By.ByXPath("//*[contains(@class, 'Button_horizontal_padding')]//*[ (text()='Save' or text()='Simpan')]"));
    private static final Locator ORDER_SUMMARY_LINK = new Locator("ORDER_SUMMARY_LINK", new By.ByXPath("//*[contains(@class, 'BookingReviewCards_details')]"));
    public BusBookingFormPageDWeb(Session<T> session) {
        super(session);
    }

    public boolean verifyPriceDetail() {
        String summaryPriceText = driverTools.waitForElementToAppear(SUMMARY_PRICE).orElseThrow().getText();
        double summaryPrice = CoreFunctions.convertPriceSafely(summaryPriceText);

        driverTools.click(SUMMARY_PRICE);
        driverTools.waitForElementToAppear(PRICE_DETAIL);

        List<WebElement> priceElements = driverTools.elements(PRICE_DETAIL).orElseThrow();
        List<Double> priceList = new ArrayList<>();
        for (WebElement priceElement : priceElements) {
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
        // dweb doesnt need to open passenger detail form
    }

    @Override
    public Locator getPassengerDateOfBirthInput(int index) {
        return new Locator("PASSENGER_DOB_INPUT_" + index, new By.ByXPath("(//input[(@id='date-of-birth' or @id='date-input' or @id='tanggal-lahir') and (@placeholder='Date of Birth' or @placeholder='Tanggal Lahir')])[" + index + "]"));
    }

    @Override
    public void enterPassengerDateOfBirth(int index) {
        Locator PASSENGER_DOB_INPUT = getPassengerDateOfBirthInput(index);
        if (driverTools.getValueAttribute(PASSENGER_DOB_INPUT, "value").isEmpty()) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_DOB_INPUT);
            driverTools.waitForElementToAppear(BIRTH_YEAR_DROPDOWN);
            driverTools.clickWithJavascriptExecutor(BIRTH_YEAR_DROPDOWN);
            driverTools.waitForElementToAppear(BIRTH_YEAR);
            driverTools.clickWithJavascriptExecutor(BIRTH_YEAR);
            driverTools.waitForElementToAppear(BIRTH_DAY);
            driverTools.clickWithJavascriptExecutor(BIRTH_DAY);
            driverTools.waitForElementToAppear(DOB_SAVE_BUTTON);
            driverTools.clickWithJavascriptExecutor(DOB_SAVE_BUTTON);
        }
    }
    @Override
    public void clickDetails() {
        driverTools.waitForElementToAppear(ORDER_SUMMARY_LINK);
        driverTools.clickWithJavascriptExecutor(ORDER_SUMMARY_LINK);
    }
}