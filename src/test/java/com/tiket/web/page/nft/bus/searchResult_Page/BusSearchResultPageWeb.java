package com.tiket.web.page.nft.bus.searchResult_Page;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.bus.BusSrpOneWayData;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;

public abstract class BusSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {

    public BusSearchResultPageWeb(Session<T> session) { super(session); }

    public static <T extends WebDriver> BusSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new BusSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new BusSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void verifySearchResultIsCorrect(String origin, String destination, String departureDate, String returnDate, int totalPassenger, String language);
    public abstract void selectFilterByExecutiveClass();
    public abstract void editPassengerQueryParam(String queryParam, String newValue);

    public static final Locator SEARCH_RESULT = new Locator("SEARCH_RESULT", new By.ByXPath("//*[contains(@class, 'ScheduleList_inner_container')]"));
    public static final Locator FILTER_SORT = new Locator("FILTER_SORT", new By.ByXPath("//*[contains(@class, 'Chip_icon')]"));
    private static final Locator LOWEST_PRICE_SORT = new Locator("LOWEST_PRICE_SORT", new By.ByXPath("//*[contains(@class, 'Radio_children') and (contains(text(), 'Harga') or contains(text(), 'price'))]"));
    private static final Locator SAVE_FILTER = new Locator("SAVE_FILTER", new By.ByXPath("//*[contains(@class, 'Button_variant_primary')]"));
    private static final Locator CARD_PRICE = new Locator("CARD_PRICE", new By.ByXPath("(//*[contains(@class,'main_price')]/*[contains(@class,'Text_variant_price')])[1]"));
    private static final Locator BUS_OPERATOR = new Locator("BUS_OPERATOR", new By.ByXPath("//*[contains(@class, 'ScheduleCard_bus_operator')]"));
    private static final Locator BUS_CLASS = new Locator("BUS_CLASS", new By.ByXPath("//*[contains(@class, 'ScheduleCard_bus_detail')]//*[contains(@class, 'Text_variant_lowEmphasis')]"));
    private static final Locator BUS_ORIGIN = new Locator("BUS_ORIGIN", new By.ByXPath("//*[contains(@class, 'TripRoute_departure')]//*[contains(@class, 'TripRoute_location_name')]"));
    private static final Locator BUS_DEPARTURE_TIME = new Locator("BUS_DEPARTURE_TIME", new By.ByXPath("//*[contains(@class, 'TripRoute_departure_checkpoint_wrapper')]//*[contains(@class, 'TripRoute_time')]//*[contains(@class, 'Text_text')]"));
    private static final Locator BUS_DESTINATION = new Locator("BUS_DESTINATION", new By.ByXPath("//*[contains(@class, 'TripRoute_arrival')]//*[contains(@class, 'TripRoute_location_name')]"));
    private static final Locator SELECT_FIRST_CARD = new Locator("SELECT_FIRST_CARD", new By.ByXPath("(//*[contains(@class, 'ScheduleCard_card')])[1]"));

    public String formatDate(String date, String language) {
        String[] dateArr = date.split(" ");
        String day = dateArr[0];
        String month = dateArr[1];
        if (language.equals("id"))
            month = CoreFunctions.monthEngToId(month);
        return day + " " + month.substring(0, 3);
    }

    public void openFilterSort() {
        driverTools.waitForElementToAppear(FILTER_SORT);
        driverTools.clickWithJavascriptExecutor(FILTER_SORT);
    }

    public boolean sortByLowestPrice() {
        driverTools.waitForElementToAppear(LOWEST_PRICE_SORT);
        driverTools.clickWithJavascriptExecutor(LOWEST_PRICE_SORT);
        driverTools.click(SAVE_FILTER);

        driverTools.waitForElementToDisappear(SAVE_FILTER);

        List<Double> priceList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Locator CARD_PRICE_TEXT = getCardPriceLocator(i);
            driverTools.scrollIntoWebView(CARD_PRICE_TEXT);
            driverTools.waitForElementToAppear(CARD_PRICE_TEXT);
            String priceText = String.valueOf(driverTools.getText(CARD_PRICE_TEXT));
            double price = Double.parseDouble(priceText.replaceAll("[^0-9]", ""));
            priceList.add(price);
        }

        boolean isSorted = true;
        for (int i = 1; i < priceList.size(); i++) {
            if (priceList.get(i) < priceList.get(i - 1)) {
                isSorted = false;
                break;
            }
        }
        return isSorted;
    }

    private Locator getCardPriceLocator(int index) {
        return new Locator("CARD_PRICE_" + index, new By.ByXPath("(//*[contains(@class,'main_price')]/*[contains(@class,'Text_variant_price')])[" + index + "]"));
    }

    public void verifySrpPriceInIDR() {
        String priceText = driverTools.waitForElementToAppear(CARD_PRICE).orElseThrow().getText();
        Assertion.assertThat("Verify price in SRP is in IDR", priceText, containsString("IDR"));
    }

    public BusSrpOneWayData getFirstBusData(String departDate, String language) {
        BusSrpOneWayData busSrpData = new BusSrpOneWayData();

        String operatorName = driverTools.waitForElementToAppear(BUS_OPERATOR).orElseThrow().getText();
        String busClass = driverTools.waitForElementToAppear(BUS_CLASS).orElseThrow().getText();
        String origin = driverTools.waitForElementToAppear(BUS_ORIGIN).orElseThrow().getText();
        String departTime = driverTools.waitForElementToAppear(BUS_DEPARTURE_TIME).orElseThrow().getText();
        String destination = driverTools.waitForElementToAppear(BUS_DESTINATION).orElseThrow().getText();

        busSrpData.departDate = formatDate(departDate, language);
        busSrpData.operatorName = operatorName;
        busSrpData.busClass = busClass;
        busSrpData.origin = origin;
        busSrpData.departTime = departTime;
        busSrpData.destination = destination;
        return busSrpData;
    }

    public void selectFirstSrpCard() {
        driverTools.waitForElementToAppear(SELECT_FIRST_CARD);
        driverTools.clickWithJavascriptExecutor(SELECT_FIRST_CARD);
    }

    public boolean selectScheduleByOperator(String operator) {
        Locator card = new Locator("OPERATOR_CARD", new By.ByXPath(
                "(//*[contains(@class, 'ScheduleCard_card') and "
                        + ".//*[contains(@class, 'ScheduleCard_bus_operator') and contains(normalize-space(.), '" + operator + "')]])[1]"));
        if (driverTools.waitForElementToAppear(card, 5).isPresent()) {
            driverTools.scrollIntoWebView(card);
            driverTools.clickWithJavascriptExecutor(card);
            return true;
        }
        return false;
    }

    public boolean selectScheduleByOperatorAndType(String operator, String busType) {
        Locator card = getOperatorAndTypeCardLocator(operator, busType);
        if (driverTools.waitForElementToAppear(card, 5).isPresent()) {
            driverTools.scrollIntoWebView(card);
            driverTools.clickWithJavascriptExecutor(card);
            return true;
        }
        return false;
    }

    private Locator getOperatorAndTypeCardLocator(String operator, String busType) {
        String lower = busType.toLowerCase();
        String xpath = "(//*[contains(@class, 'ScheduleCard_card') and "
                + ".//*[contains(@class, 'ScheduleCard_bus_operator') and contains(normalize-space(.), '" + operator + "')] and "
                + ".//*[contains(@class, 'ScheduleCard_bus_detail') and "
                + "contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + lower + "')])[1]";
        return new Locator("OPERATOR_TYPE_CARD", new By.ByXPath(xpath));
    }

    public boolean isScheduleAvailable() {
        WebElement firstBusCard = driverTools.waitForElementToAppear(SELECT_FIRST_CARD).orElseThrow();

        return driverTools.isElementDisplayed(firstBusCard);
    }

    public boolean verifyFilterByExecutiveClass() {
        driverTools.waitForElementToAppear(BUS_CLASS, 5).orElseThrow();
        List<WebElement> busClassList = driverTools.elements(BUS_CLASS).orElseThrow();
        for (WebElement busClass : busClassList) {
            String classText = driverTools.getText(busClass);
            Assertion.assertThat("Class should contains text Eksekutif or Executive",
                    classText, either(containsString("Eksekutif")).or(containsString("Executive")));
        }
        return driverTools.isElementDisplayed(BUS_CLASS);
    }

    private Locator getCardClassLocator(int index) {
        return new Locator("CARD_CLASS_" + index, new By.ByXPath("(//*[contains(@class, 'ScheduleCard_bus_detail')]//*[contains(@class, 'Text_weight_semibold')])[" + index + "]"));
    }
}