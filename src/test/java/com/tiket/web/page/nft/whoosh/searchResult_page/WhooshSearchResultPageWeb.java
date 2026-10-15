package com.tiket.web.page.nft.whoosh.searchResult_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class WhooshSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {
    public WhooshSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WhooshSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new WhooshSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new WhooshSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator FIRST_WHOOSH = new Locator("FIRST_WHOOSH", new By.ByXPath("(//div[contains(@class, 'ScheduleList_train_name')])[1]"));
    private static final Locator SKIP_BTN = new Locator("SKIP_BTN", new By.ByXPath("//button[.//span[normalize-space()='Skip' or normalize-space()='Lewati']]"));
    private static final Locator FILTER_CHECK = new Locator("FILTER_CHECK", new By.ByXPath("(//section[contains(@class, 'QuickFilter_container')]//span[contains(text(), 'Filter')])[1]"));
    private static final Locator RETURN_CHECK = new Locator("RETURN_CHECK", new By.ByXPath("//div[contains(@class, 'SelectedDepartureSchedule_selected_train_name') and contains(text(), 'Whoosh')]"));
    private static final Locator CLASS_BTN = new Locator("CLASS_BTN", new By.ByXPath("//span[(text()='Class Type' or text()='Tipe Kelas') and contains(@class, 'Text_variant_lowEmphasis')]"));
    private static final Locator FIRST_CLASS_TYPE = new Locator("FIRST_CLASS_TYPE", new By.ByXPath("(//section[@data-section='seatClass']//label)[1]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("//footer//button[contains(@class, 'Button_button')]"));
    private static final Locator UPDATED_WHOOSH_FILTER = new Locator("UPDATED_WHOOSH_FILTER", new By.ByXPath("(//div[contains(@class, 'ScheduleList_train_class')])[1]"));
    public static final Locator TRAIN_NAME_FILTER = new Locator("TRAIN_NAME_FILTER", new By.ByXPath("//span[text()='Train Name' and contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size_b3') and contains(@class, 'Text_weight_regular')]"));
    public static final Locator FIRST_NAME_FILTER = new Locator("FIRST_NAME_FILTER", new By.ByXPath("(//section[@data-section='trainName']//label)[1]"));
    private static final Locator FILTERED_WHOOSH = new Locator("FILTERED_WHOOSH", new By.ByXPath("//div[contains(@class, 'ScheduleList_train_name') and contains(text(), 'Whoosh')]"));
    private static final Locator PRICE_OF_WHOOSH = new Locator("PRICE_OF_WHOOSH", new By.ByXPath("(//*[contains(@class,'main_price')]/*[contains(@class,'Text_variant_price')])[1]"));
    private static final Locator SEARCH_BUS_BTN = new Locator("SEARCH_BUS_BTN", new By.ByXPath("//button[.//span[contains(normalize-space(.), 'Search bus') or contains(normalize-space(.), 'Cari bus')]]"));
    private static final Locator BUS_SECTION_CONTAINER = new Locator("BUS_SECTION_CONTAINER", new By.ByXPath("//section[@id='coachmark-srp-step3' or contains(@class, 'LocationPointFilter_section')]"));
    private static final Locator ORIGIN_STATION_ON_BUS_SRP = new Locator("ORIGIN_STATION_ON_BUS_SRP", new By.ByXPath("(//span[contains(@class, 'ChangeSearch_origin')])[2]"));
    private static final Locator DESTINATION_STATION_ON_BUS_SRP = new Locator("DESTINATION_STATION_ON_BUS_SRP", new By.ByXPath("(//span[contains(@class, 'ChangeSearch_destination')])[2]"));

    public void clickSkip() {
        driverTools.waitForElementToAppear(SKIP_BTN);
        if (driverTools.isElementDisplayed(SKIP_BTN)){
            driverTools.click(SKIP_BTN);
        }
    }

    public String clickFirstWhoosh() {
        driverTools.waitForElementToAppear(FIRST_WHOOSH);
        String departureWhoosh = driverTools.getText(FIRST_WHOOSH).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_WHOOSH);
        return departureWhoosh;
    }

    public boolean checkSRP() {
        try {
            driverTools.waitForElementToAppear(FILTER_CHECK, 5);
            return driverTools.isElementPresent(FILTER_CHECK);
        } catch (Exception e) {
            logger.warn("FILTER_CHECK not found: " + e.getMessage());
            return false;
        }
    }

    public boolean checkReturn() {
        driverTools.dummyWait(4);
        try {
            driverTools.waitForElementToAppear(RETURN_CHECK, 5);
            return driverTools.isElementPresent(RETURN_CHECK);
        } catch (Exception e) {
            logger.warn("FILTER_CHECK not found: " + e.getMessage());
            return false;
        }
    }


    public void getBack() {
        driverTools.back();
    }

    public void clickFilterBtn() {
        driverTools.waitForElementToAppear(FILTER_CHECK);
        driverTools.click(FILTER_CHECK);
    }

    public String clickFirstClassType() {
        driverTools.waitForElementToAppear(CLASS_BTN);
        driverTools.clickWithJavascriptExecutor(CLASS_BTN);
        driverTools.waitForElementToAppear(FIRST_CLASS_TYPE);
        String classType = driverTools.getText(FIRST_CLASS_TYPE).orElseThrow();
        driverTools.click(FIRST_CLASS_TYPE);
        return classType;
    }

    public abstract String clickTrainNameFilter();

    public void clickSave() {
        driverTools.waitForElementToAppear(SAVE_BTN);
        driverTools.clickWithJavascriptExecutor(SAVE_BTN);
    }

    public String checkFilter() {
        driverTools.waitForElementToAppear(UPDATED_WHOOSH_FILTER, 3);
        return driverTools.getText(UPDATED_WHOOSH_FILTER).orElseThrow();
    }

    public String checkFilteredWhooshName() {
        driverTools.waitForElementToAppear(FILTERED_WHOOSH, 3);
        return driverTools.getText(FILTERED_WHOOSH).orElseThrow();
    }

    public String getPrice(){
        driverTools.waitForElementToAppear(PRICE_OF_WHOOSH);
        String priceText = driverTools.getText(PRICE_OF_WHOOSH).orElseThrow();
        return priceText.replaceAll("[^0-9]", "");
    }
    public boolean checkWhoosh(){
        driverTools.waitForElementToAppear(FIRST_WHOOSH);
        return !driverTools.isElementPresent(FIRST_WHOOSH);
    }

    public void clickBusBtn(){
        driverTools.waitForElementToAppear(SEARCH_BUS_BTN);
        driverTools.waitForElementToBeClickable(SEARCH_BUS_BTN);
        driverTools.click(SEARCH_BUS_BTN);
    }

    public boolean verifyBusPage(){
        driverTools.waitForElementToAppear(BUS_SECTION_CONTAINER);
        return driverTools.isElementPresent(BUS_SECTION_CONTAINER);
    }

    public String checkOriginOnBusSRP(){
        driverTools.waitForElementToAppear(ORIGIN_STATION_ON_BUS_SRP);
        return driverTools.getText(ORIGIN_STATION_ON_BUS_SRP).orElseThrow();
    }

    public String checkDestinationOnBusSRP(){
        driverTools.waitForElementToAppear(DESTINATION_STATION_ON_BUS_SRP);
        return driverTools.getText(DESTINATION_STATION_ON_BUS_SRP).orElseThrow();
    }
}
