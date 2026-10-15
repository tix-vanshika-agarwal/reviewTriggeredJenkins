package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;

public class RatesAndAvailabilityPage<T extends WebDriver> extends BasePage<T> {

    public RatesAndAvailabilityPage(Session<T> session) {
        super(session);
    }

    private static final Locator SURCHARGE_PAGE_TITLE = new Locator("SURCHARGE_PAGE_TITLE",
            By.xpath("//*[@data-testid='surcharge-setup-page'] | //*[@data-testid='surcharge-list'] | //a[contains(@href,'/rates-and-availability/surcharge') and not(contains(@href,'create'))]"));

    private static final Locator ADD_NEW_SURCHARGE_BUTTON = new Locator("ADD_NEW_SURCHARGE_BUTTON",
            By.xpath("//*[@data-testid='add-new-surcharge'] | //a[contains(@href,'surcharge/create')]"));

    private static final Locator SURCHARGE_SEARCH_INPUT = new Locator("SURCHARGE_SEARCH_INPUT",
            By.xpath("//*[contains(@class,'the-surcharge-filter') or @data-testid='surcharge-filter']//input[@name='name']"));
    private static final Locator SURCHARGE_SEARCH_GO_BUTTON = new Locator("SURCHARGE_SEARCH_GO_BUTTON",
            By.xpath("//*[contains(@class,'the-surcharge-filter') or @data-testid='surcharge-filter']//button[@type='button']"));

    private static final Locator NEW_SURCHARGE_FORM = new Locator("NEW_SURCHARGE_FORM",
            By.xpath("//form[@autocomplete='off'] | //*[contains(@class,'the-surcharge-form')]"));

    private static final Locator SURCHARGE_NAME_INPUT = new Locator("SURCHARGE_NAME_INPUT",
            By.xpath("//input[@name='name'] | //*[@id='name']//input"));

    private static final Locator STAY_DATE_START_INPUT = new Locator("STAY_DATE_START_INPUT",
            By.xpath("//input[@id='stayDateStart' or @name='stayDateStart']"));

    private static final Locator STAY_DATE_END_INPUT = new Locator("STAY_DATE_END_INPUT",
            By.xpath("//input[@id='stayDateEnd' or @name='stayDateEnd']"));

    private static final Locator STAY_DATE_CALENDAR = new Locator("STAY_DATE_CALENDAR",
            By.xpath("//div[contains(@class,'the-datepicker') or contains(@class,'DayPicker') or contains(@class,'calendar')]"));

    private static final Locator STAY_DATE_CALENDAR_PREV = new Locator("STAY_DATE_CALENDAR_PREV",
            By.xpath("//*[contains(@class,'the-navbar-prev')]"));

    private static final Locator STAY_DATE_CALENDAR_NEXT = new Locator("STAY_DATE_CALENDAR_NEXT",
            By.xpath("//*[contains(@class,'the-navbar-next')]"));

    private static final Locator STAY_DATE_CALENDAR_HEADER = new Locator("STAY_DATE_CALENDAR_HEADER",
            By.xpath("//*[contains(@class,'DayPicker-Caption')]"));

    private static final Locator SURCHARGE_TYPE_ROOM_RADIO = new Locator("SURCHARGE_TYPE_ROOM_RADIO",
            By.xpath("//div[@id='surchargeTypePrice']//label[.//input[@type='radio' and @name='type' and @value='ROOM']] | //label[.//input[@name='type' and @value='ROOM']] | (//input[@type='radio' and @name='type' and @value='ROOM'])[1]"));

    private static final Locator SURCHARGE_PRICE_INPUT = new Locator("SURCHARGE_PRICE_INPUT",
            By.xpath("//input[@name='value' and contains(@class,'the-price')] | //div[@id='value']//input | //input[@name='value']"));

    private static final Locator SURCHARGE_INFO_FIRST_RADIO = new Locator("SURCHARGE_INFO_FIRST_RADIO",
            By.xpath("(//label[.//input[@type='radio' and not(@name='type')]])[1] | //div[@id='surchargeTypePrice']/following::label[.//input[@type='radio' and not(@name='type')]][1] | //div[@id='surchargeTypePrice']/following::input[@type='radio' and not(@name='type')][1]"));

    private static final Locator NEW_SURCHARGE_SUBMIT_BUTTON = new Locator("NEW_SURCHARGE_SUBMIT_BUTTON",
            By.xpath("//*[contains(@class,'the-surcharge-form')]//button[@type='submit'] | //button[@type='submit' and contains(@class,'yellow')]"));

    // ————— Pricing Model (Rates & Availability --> Pricing Model) ————— LOCATORS START
    private static final Locator PRICING_MODEL_PAGE = new Locator("PRICING_MODEL_PAGE",
            By.xpath("//main//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator PRICING_MODEL_CHANGE_BUTTON = new Locator("PRICING_MODEL_CHANGE_BUTTON",
            By.xpath("//main//button[contains(@class,'Button_variant_secondary')]"));
    private static final Locator PRICING_MODEL_FIRST_OPTION_CARD = new Locator("PRICING_MODEL_FIRST_OPTION_CARD",
            By.xpath("(//main//div[contains(@class,'Card_card_container')]//div[contains(@class,'flex-col') and contains(@class,'gap-2')]/div[contains(@class,'items-center') and contains(@class,'gap-2')])[1]"));
    private static final Locator PRICING_MODEL_SECOND_OPTION_CARD = new Locator("PRICING_MODEL_SECOND_OPTION_CARD",
            By.xpath("(//main//div[contains(@class,'Card_card_container')]//div[contains(@class,'flex-col') and contains(@class,'gap-2')]/div[contains(@class,'items-center') and contains(@class,'gap-2')])[2]"));
    private static final Locator PRICING_MODEL_FIRST_OPTION_ACTIVE = new Locator("PRICING_MODEL_FIRST_OPTION_ACTIVE",
            By.xpath("(//main//div[contains(@class,'Card_card_container')]//div[contains(@class,'items-center') and contains(@class,'gap-2')])[1][.//span[contains(@class,'text-[#0064D2]')]]"));
    private static final Locator PRICING_MODEL_FIRST_RADIO = new Locator("PRICING_MODEL_FIRST_RADIO",
            By.xpath("(//main//label[contains(@class,'Radio_radio')]//span[contains(@class,'Radio_checkbox')])[1]"));
    private static final Locator PRICING_MODEL_SECOND_RADIO = new Locator("PRICING_MODEL_SECOND_RADIO",
            By.xpath("(//main//label[contains(@class,'Radio_radio')]//span[contains(@class,'Radio_checkbox')])[2]"));
    private static final Locator PRICING_MODEL_SAVE_BUTTON = new Locator("PRICING_MODEL_SAVE_BUTTON",
            By.xpath("//main//div[contains(@class,'justify-end') and contains(@class,'gap-3')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator PRICING_MODEL_YES_CONTINUE_BUTTON = new Locator("PRICING_MODEL_YES_CONTINUE_BUTTON",
            By.xpath("//div[contains(@class,'Dialog_dialog')]//div[contains(@class,'grid-cols-2')]//button[contains(@class,'Button_variant_primary')]"));
    private static final Locator PRICING_MODEL_SECOND_OPTION_ACTIVE = new Locator("PRICING_MODEL_SECOND_OPTION_ACTIVE",
            By.xpath("(//main//div[contains(@class,'Card_card_container')]//div[contains(@class,'items-center') and contains(@class,'gap-2')])[2][.//span[contains(@class,'text-[#0064D2]')]]"));

    // ————— Pricing Model (Rates & Availability --> Pricing Model) ————— LOCATORS END

    private static String getSurchargeRowByNameXpath(String name) {
        return String.format("//div[contains(@class,'the-surcharge-list')]//tr[.//td[contains(., '%s')]] | //table[contains(@class,'table')]//tr[.//td[contains(., '%s')]]", name, name);
    }

    private static Locator getSurchargeToggleOnByName(String name) {
        return new Locator("SURCHARGE_TOGGLE_ON",
                By.xpath("//span[contains(@class,'the-toggle-btn-on')]"));
    }

    private static Locator getSurchargeToggleOffByName(String name) {
        return new Locator("SURCHARGE_TOGGLE_OFF",
                By.xpath("//span[contains(@class,'the-toggle-btn-off')]"));
    }

    private static Locator getSurchargeCopyButtonByName(String name) {
        return new Locator("SURCHARGE_COPY",
                By.xpath("//*[contains(@class,'tixicon-copy')]"));
    }

    private static Locator getSurchargeDeleteButtonByName(String name) {
        return new Locator("SURCHARGE_DELETE",
                By.xpath("//button[contains(@class,'delete')]"));
    }

    private static Locator getSurchargeRowByName(String name) {
        return new Locator("SURCHARGE_ROW",
                By.xpath(getSurchargeRowByNameXpath(name)));
    }

    private static final Locator DELETE_SURCHARGE_MODAL = new Locator("DELETE_SURCHARGE_MODAL",
            By.xpath("//*[contains(@class,'surcharge-delete-modal-confirmation')] | //*[@data-testid='the-modal' and contains(@class,'show')]"));

    private static final Locator DELETE_SURCHARGE_CONFIRM_BUTTON = new Locator("DELETE_SURCHARGE_CONFIRM",
            By.xpath("//*[contains(@class,'surcharge-delete-modal-confirmation')]//button[contains(@class,'delete-surcharge-button')] | //*[@data-testid='the-modal']//button[contains(@class,'delete-surcharge-button')] | //button[contains(@class,'delete-surcharge-button')]"));

    private static final Locator SURCHARGE_SUCCESS_BACK_TO_LIST_BUTTON = new Locator("SURCHARGE_SUCCESS_BACK_TO_LIST",
            By.xpath("//*[@id='button-success'] | //*[@data-testid='the-modal']//button[@id='button-success'] | //*[contains(@class,'the-modal')]//button[@id='button-success' and contains(@class,'primary')]"));

    public boolean isSurchargePageLoaded() {
        driverTools.waitForElementToAppear(SURCHARGE_PAGE_TITLE, 5);
        return driverTools.isElementDisplayed(SURCHARGE_PAGE_TITLE);
    }

    public void clickAddNewSurchargeButton() {
        driverTools.waitForElementToAppear(ADD_NEW_SURCHARGE_BUTTON);
        driverTools.click(ADD_NEW_SURCHARGE_BUTTON);
        driverTools.waitForElementToAppear(NEW_SURCHARGE_FORM, 10);
    }

    public boolean isNewSurchargeFormLoaded() {
        driverTools.waitForElementToAppear(NEW_SURCHARGE_FORM, 5);
        return driverTools.isElementDisplayed(NEW_SURCHARGE_FORM);
    }

    public void fillSurchargeName(String name) {
        WebElement input = driverTools.waitForElementToBeClickable(SURCHARGE_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
    }

    private int[] parseDateString(String dateValue) {
        if (dateValue == null || dateValue.isBlank()) return new int[]{0, 0, 0};
        String normalized = dateValue.trim().replace("/", "-");
        String[] parts = normalized.split("-");
        if (parts.length != 3) return new int[]{0, 0, 0};
        int year, month, day;
        if (parts[0].length() == 4) {
            year = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            day = Integer.parseInt(parts[2]);
        } else {
            day = Integer.parseInt(parts[0]);
            month = Integer.parseInt(parts[1]);
            year = Integer.parseInt(parts[2]);
        }
        return new int[]{year, month, day};
    }

    private static Locator getStayDateCalendarDay(int day) {
        return new Locator("STAY_DATE_CALENDAR_DAY",
                By.xpath(String.format("//div[contains(@class,'the-datepicker') or contains(@class,'DayPicker')]//*[contains(@class,'Day') and (@aria-disabled='false' or not(@aria-disabled))]//*[text()='%d'] | //div[contains(@class,'the-datepicker') or contains(@class,'DayPicker')]//button[text()='%d']", day, day)));
    }

    private int parseMonthYearFromHeader(String headerText) {
        if (headerText == null || headerText.isBlank()) return 0;
        String[] parts = headerText.trim().split("\\s+");
        if (parts.length < 2) return 0;
        String monthName = parts[0].trim();
        if (monthName.length() < 2) return 0;
        String monthPrefix = monthName.length() >= 3 ? monthName.substring(0, 3) : monthName;
        for (int m = 1; m <= 12; m++) {
            String idName = CoreFunctions.getMonthName(m, "id");
            String enName = CoreFunctions.getMonthName(m, "en");
            if (idName.equalsIgnoreCase(monthName) || enName.equalsIgnoreCase(monthName)) return m;
            if (idName.length() >= 3 && idName.substring(0, 3).equalsIgnoreCase(monthPrefix)) return m;
            if (enName.length() >= 3 && enName.substring(0, 3).equalsIgnoreCase(monthPrefix)) return m;
        }
        return 0;
    }

    private int parseYearFromHeader(String headerText) {
        if (headerText == null || headerText.isBlank()) return 0;
        String[] parts = headerText.trim().split("\\s+");
        if (parts.length < 2) return 0;
        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void selectDateInOpenCalendar(int year, int month, int day) {
        driverTools.waitForElementToAppear(STAY_DATE_CALENDAR, 5);
        for (int i = 0; i < 24; i++) {
            WebElement headerEl = driverTools.waitForElementToAppear(STAY_DATE_CALENDAR_HEADER, 3).orElse(null);
            if (headerEl == null) break;
            String headerText = driverTools.getText(headerEl);
            int currentMonth = parseMonthYearFromHeader(headerText);
            int currentYear = parseYearFromHeader(headerText);
            if (currentMonth == 0 || currentYear == 0) break;
            if (currentMonth == month && currentYear == year) break;
            int targetMonths = year * 12 + month;
            int currentMonths = currentYear * 12 + currentMonth;
            if (currentMonths < targetMonths) {
                WebElement nextBtn = driverTools.waitForElementToBeClickable(STAY_DATE_CALENDAR_NEXT, 5).orElse(null);
                if (nextBtn == null) break;
                driverTools.scrollIntoWebView(nextBtn);
                driverTools.clickWithJavascriptExecutor(nextBtn);
                driverTools.waitForElementToAppear(STAY_DATE_CALENDAR_HEADER, 3);
            } else if (currentMonths > targetMonths) {
                WebElement prevBtn = driverTools.waitForElementToBeClickable(STAY_DATE_CALENDAR_PREV, 5).orElse(null);
                if (prevBtn == null) break;
                driverTools.scrollIntoWebView(prevBtn);
                driverTools.clickWithJavascriptExecutor(prevBtn);
                driverTools.waitForElementToAppear(STAY_DATE_CALENDAR_HEADER, 3);
            } else break;
        }
        Locator dayLocator = getStayDateCalendarDay(day);
        WebElement dayEl = driverTools.waitForElementToBeClickable(dayLocator, 5).orElseThrow();
        driverTools.scrollIntoWebView(dayEl);
        driverTools.click(dayEl);
    }

    public void fillStayDateStart(String dateValue) {
        int[] ymd = parseDateString(dateValue);
        if (ymd[0] == 0) return;
        WebElement input = driverTools.waitForElementToBeClickable(STAY_DATE_START_INPUT).orElseThrow();
        driverTools.clickWithJavascriptExecutor(input);
        selectDateInOpenCalendar(ymd[0], ymd[1], ymd[2]);
    }

    public void fillStayDateEnd(String dateValue) {
        int[] ymd = parseDateString(dateValue);
        if (ymd[0] == 0) return;
        WebElement input = driverTools.waitForElementToBeClickable(STAY_DATE_END_INPUT).orElseThrow();
        driverTools.click(input);
        selectDateInOpenCalendar(ymd[0], ymd[1], ymd[2]);
    }

    public void fillStayDatesFromToday(int startDaysFromToday, int endDaysFromStart) {
        LocalDate initialDate = LocalDate.now().plusDays(startDaysFromToday);
        LocalDate finalDate = initialDate.plusDays(endDaysFromStart);
        fillStayDateStart(initialDate.toString());
        fillStayDateEnd(finalDate.toString());
    }

    public void selectSurchargeTypeRoom() {
        WebElement el = driverTools.waitForElementToBeClickable(SURCHARGE_TYPE_ROOM_RADIO, 10).orElseThrow();
        driverTools.click(el);
    }

    public void fillSurchargePrice(String price) {
        if (price == null || price.isBlank()) return;
        WebElement input = driverTools.waitForElementToBeClickable(SURCHARGE_PRICE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, price);
    }

    public void selectFirstSurchargeInfo() {
        WebElement el = driverTools.waitForElementToBeClickable(SURCHARGE_INFO_FIRST_RADIO, 10).orElseThrow();
        driverTools.click(el);
    }

    public void clickNewSurchargeSubmit() {
        WebElement btn = driverTools.waitForElementToBeClickable(NEW_SURCHARGE_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(btn);
    }

    public void clickBackToListFromSuccessModal() {
        WebElement btn = driverTools.waitForElementToBeClickable(SURCHARGE_SUCCESS_BACK_TO_LIST_BUTTON, 10).orElseThrow();
        driverTools.click(btn);
        driverTools.waitForElementToAppear(SURCHARGE_PAGE_TITLE, 10);
        driverTools.waitForElementToBeClickable(ADD_NEW_SURCHARGE_BUTTON, 5);
    }

    public void searchSurcharge(String name) {
        WebElement input = driverTools.waitForElementToBeClickable(SURCHARGE_SEARCH_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, name);
        WebElement goBtn = driverTools.waitForElementToBeClickable(SURCHARGE_SEARCH_GO_BUTTON).orElseThrow();
        driverTools.click(goBtn);
        driverTools.waitForElementToAppear(SURCHARGE_PAGE_TITLE, 8);
    }

    public boolean isSurchargeDisplayed(String name) {
        return driverTools.waitForElementToAppear(getSurchargeRowByName(name), 5).isPresent();
    }

    public boolean isSurchargeActiveByName(String name) {
        return driverTools.waitForElementToAppear(getSurchargeToggleOnByName(name), 3).isPresent();
    }

    public void clickSurchargeToggle(String name, boolean wantActive) {
        boolean current = isSurchargeActiveByName(name);
        if (current != wantActive) {
            Locator spanToClick = wantActive ? getSurchargeToggleOffByName(name) : getSurchargeToggleOnByName(name);
            WebElement span = driverTools.waitForElementToBeClickable(spanToClick).orElseThrow();
            driverTools.scrollIntoWebView(span);
            driverTools.click(span);
            driverTools.waitForElementToAppear(wantActive ? getSurchargeToggleOnByName(name) : getSurchargeToggleOffByName(name), 5);
        }
    }

    public void clickCopySurcharge(String name) {
        WebElement copyBtn = driverTools.waitForElementToBeClickable(getSurchargeCopyButtonByName(name)).orElseThrow();
        driverTools.scrollIntoWebView(copyBtn);
        driverTools.clickWithJavascriptExecutor(copyBtn);
    }

    public void clickDeleteSurcharge(String name) {
        WebElement deleteBtn = driverTools.waitForElementToBeClickable(getSurchargeDeleteButtonByName(name)).orElseThrow();
        driverTools.clickWithJavascriptExecutor(deleteBtn);
    }

    public void confirmDeleteSurchargeIfPresent() {
        driverTools.waitForElementToAppear(DELETE_SURCHARGE_MODAL, 5);
        driverTools.waitForElementToAppear(DELETE_SURCHARGE_CONFIRM_BUTTON, 10).ifPresent(el -> driverTools.click(el));
        driverTools.waitForElementToDisappear(DELETE_SURCHARGE_MODAL, 10);
    }

    /** If a surcharge with the given name exists, deletes it (search → delete → confirm → back to list). */
    public void deleteSurchargeByNameIfPresent(String name) {
        searchSurcharge(name);
        if (isSurchargeDisplayed(name)) {
            clickDeleteSurcharge(name);
            confirmDeleteSurchargeIfPresent();
            clickBackToListFromSuccessModal();
        }
    }

    // ————— Pricing Model (Rates & Availability --> Pricing Model) ————— START
    public boolean isPricingModelPageLoaded() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_PAGE, 8).isPresent()
                && driverTools.waitForElementToAppear(PRICING_MODEL_CHANGE_BUTTON, 5).isPresent();
    }

    public boolean isPricingModelChangeButtonDisplayed() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_CHANGE_BUTTON, 3).isPresent()
                && driverTools.isElementDisplayed(PRICING_MODEL_CHANGE_BUTTON);
    }

    public boolean isPricingModelRoomBasedSectionDisplayed() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_FIRST_OPTION_CARD, 3).isPresent();
    }

    public boolean isPricingModelOccupancyBasedSectionDisplayed() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_SECOND_OPTION_CARD, 3).isPresent();
    }

    public boolean isPricingModelRoomBasedCurrentlyActive() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_FIRST_OPTION_ACTIVE, 3).isPresent();
    }

    public void clickPricingModelChangeButton() {
        WebElement btn = driverTools.waitForElementToBeClickable(PRICING_MODEL_CHANGE_BUTTON).orElseThrow();
        driverTools.click(btn);
    }

    public void clickPricingModelFirstRadio() {
        WebElement radio = driverTools.waitForElementToBeClickable(PRICING_MODEL_FIRST_RADIO).orElseThrow();
        driverTools.click(radio);
    }

    public void clickPricingModelSecondRadio() {
        WebElement radio = driverTools.waitForElementToBeClickable(PRICING_MODEL_SECOND_RADIO).orElseThrow();
        driverTools.click(radio);
    }

    public void clickPricingModelSaveButton() {
        WebElement btn = driverTools.waitForElementToBeClickable(PRICING_MODEL_SAVE_BUTTON).orElseThrow();
        driverTools.click(btn);
    }

    public void clickPricingModelYesContinue() {
        WebElement btn = driverTools.waitForElementToBeClickable(PRICING_MODEL_YES_CONTINUE_BUTTON).orElseThrow();
        driverTools.click(btn);
    }

    public boolean isPricingModelSecondOptionCurrentlyActive() {
        return driverTools.waitForElementToAppear(PRICING_MODEL_SECOND_OPTION_ACTIVE, 8).isPresent();
    }
    // ————— Pricing Model (Rates & Availability --> Pricing Model) ————— END
}