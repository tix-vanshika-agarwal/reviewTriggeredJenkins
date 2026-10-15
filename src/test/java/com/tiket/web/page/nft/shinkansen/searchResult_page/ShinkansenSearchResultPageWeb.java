package com.tiket.web.page.nft.shinkansen.searchResult_page;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ShinkansenSearchResultPageWeb<T extends WebDriver> extends WebPage<T> {
    public ShinkansenSearchResultPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ShinkansenSearchResultPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new ShinkansenSearchResultPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new ShinkansenSearchResultPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    // NOTE: Locators reused from Whoosh as placeholders.
    // Replace with dedicated Shinkansen UI locators once available.
    private static final Locator FIRST_SHINKANSEN = new Locator("FIRST_SHINKANSEN", new By.ByXPath("(//div[contains(@class, 'ScheduleCard_train_info')])[1]"));
    private static final Locator SKIP_BTN = new Locator("SKIP_BTN", new By.ByXPath("//button[text()='Skip' or text()='Lewati']"));
    private static final Locator FILTER_CHECK = new Locator("FILTER_CHECK", new By.ByXPath("(//section[contains(@class, 'QuickFilter_container')]//span[contains(text(), 'Filter')])[1]"));
    private static final Locator RETURN_CHECK = new Locator("RETURN_CHECK", new By.ByXPath("//div[contains(@class, 'SelectedDepartureSchedule_selected_train_name')]"));
    private static final Locator CLASS_BTN = new Locator("CLASS_BTN", new By.ByXPath("//span[(text()='Class Type' or text()='Tipe Kelas') and contains(@class, 'Text_variant_lowEmphasis')]"));
    private static final Locator FIRST_CLASS_TYPE = new Locator("FIRST_CLASS_TYPE", new By.ByXPath("(//section[@data-section='seatClass']//label)[1]"));
    private static final Locator SAVE_BTN = new Locator("SAVE_BTN", new By.ByXPath("//footer//button[contains(@class, 'Button_button')]"));
    private static final Locator UPDATED_SHINKANSEN_FILTER = new Locator("UPDATED_SHINKANSEN_FILTER", new By.ByXPath("(//div[contains(@class, 'ScheduleList_train_class')])[1]"));
    public static final Locator TRAIN_NAME_FILTER = new Locator("TRAIN_NAME_FILTER", new By.ByXPath("//span[text()='Train Name' and contains(@class, 'Text_text') and contains(@class, 'Text_variant_lowEmphasis') and contains(@class, 'Text_size_b3') and contains(@class, 'Text_weight_regular')]"));
    public static final Locator FIRST_NAME_FILTER = new Locator("FIRST_NAME_FILTER", new By.ByXPath("(//section[@data-section='trainName']//label)[1]"));
    private static final Locator FILTERED_SHINKANSEN = new Locator("FILTERED_SHINKANSEN", new By.ByXPath("(//div[contains(@class, 'ScheduleList_train_name')])[1]"));
    private static final Locator PRICE_OF_SHINKANSEN = new Locator("PRICE_OF_SHINKANSEN", new By.ByXPath("(//*[contains(@class,'main_price')]/*[contains(@class,'Text_variant_price')])[1]"));
    private static final Locator SELECT_TICKET_TYPE_BTN = new Locator("SELECT_TICKET_TYPE_BTN", new By.ByXPath(
            "(//button[contains(@class, 'Button_accessibility_button') and contains(@class, 'Button_variant_primary')]"
                    + "[.//span[normalize-space(.)='Select' or normalize-space(.)='Pilih']])[1]"));

    public void clickSkip() {
        driverTools.waitForElementToAppear(SKIP_BTN);
        if (driverTools.isElementDisplayed(SKIP_BTN)) {
            driverTools.click(SKIP_BTN);
        }
    }

    public String clickFirstShinkansen() {
        driverTools.waitForElementToAppear(FIRST_SHINKANSEN);
        String departureShinkansen = driverTools.getText(FIRST_SHINKANSEN).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_SHINKANSEN);
        return departureShinkansen;
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
            logger.warn("RETURN_CHECK not found: " + e.getMessage());
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
        driverTools.waitForElementToAppear(UPDATED_SHINKANSEN_FILTER, 3);
        return driverTools.getText(UPDATED_SHINKANSEN_FILTER).orElseThrow();
    }

    public String checkFilteredShinkansenName() {
        driverTools.waitForElementToAppear(FILTERED_SHINKANSEN, 3);
        return driverTools.getText(FILTERED_SHINKANSEN).orElseThrow();
    }

    public String getPrice() {
        driverTools.waitForElementToAppear(PRICE_OF_SHINKANSEN);
        String priceText = driverTools.getText(PRICE_OF_SHINKANSEN).orElseThrow();
        return priceText.replaceAll("[^0-9]", "");
    }

    public boolean checkShinkansen() {
        driverTools.waitForElementToAppear(FIRST_SHINKANSEN);
        return !driverTools.isElementPresent(FIRST_SHINKANSEN);
    }

    public void selectTicketType() {
        driverTools.waitForElementToAppear(SELECT_TICKET_TYPE_BTN);
        driverTools.waitForElementToBeClickable(SELECT_TICKET_TYPE_BTN);
        driverTools.clickWithJavascriptExecutor(SELECT_TICKET_TYPE_BTN);
    }

    // On the SRP, read the current address-bar URL, shift departDate forward by daysToAdd (based on the date already in the URL),
    // and navigate to the edited URL. For a round-trip URL (returnDate present), returnDate is set to 1 week after the new departDate.
    // A one-way URL (no returnDate) stays one-way - only departDate is edited.
    public void shiftTravelDateByDays(int daysToAdd) {
        driverTools.waitForElementToAppear(FIRST_SHINKANSEN); // ensure SRP results are loaded so the address bar holds the final search URL
        String currentUrl = driver.getCurrentUrl();
        logger.log("SRP URL before date shift: " + currentUrl);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Matcher departMatcher = Pattern.compile("departDate=(\\d{4}-\\d{2}-\\d{2})").matcher(currentUrl);
        if (!departMatcher.find()) {
            throw new IllegalStateException("departDate not found in SRP URL: " + currentUrl);
        }
        LocalDate newDepartDate = LocalDate.parse(departMatcher.group(1), formatter).plusDays(daysToAdd);
        String newUrl = currentUrl.replaceAll("departDate=\\d{4}-\\d{2}-\\d{2}", "departDate=" + newDepartDate.format(formatter));

        // Round trip only: if returnDate is present in the URL, set it to 1 week after the new departDate.
        if (Pattern.compile("returnDate=(\\d{4}-\\d{2}-\\d{2})").matcher(currentUrl).find()) {
            LocalDate newReturnDate = newDepartDate.plusWeeks(1);
            newUrl = newUrl.replaceAll("returnDate=\\d{4}-\\d{2}-\\d{2}", "returnDate=" + newReturnDate.format(formatter));
        }

        logger.log("Navigating to shifted SRP URL: " + newUrl);
        driverTools.get(newUrl);
    }
}
