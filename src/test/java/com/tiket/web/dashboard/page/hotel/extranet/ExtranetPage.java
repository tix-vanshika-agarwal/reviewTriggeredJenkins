package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Locale;

public class ExtranetPage<T extends WebDriver> extends BasePage<T> {

    private String extranetWindowHandle;

    public ExtranetPage(Session<T> session) {
        super(session);
    }

    private static final Locator SEE_PROPERTY_PAGE_LINK = new Locator("SEE_PROPERTY_PAGE_LINK", new By.ByXPath("//a[contains(@href,'tiket.com/hotel')]"));
    /** Language selector trigger in header only (avoids matching flag icons inside dropdown). */
    private static final Locator LANGUAGE_SELECTOR_BUTTON = new Locator("LANGUAGE_SELECTOR_BUTTON", new By.ByXPath("//header//*[contains(@class,'cursor-pointer') and .//img[contains(@alt,'flag')]]"));
    private static final Locator LANGUAGE_DROPDOWN_LIST = new Locator("LANGUAGE_DROPDOWN_LIST", new By.ByXPath("//ul[contains(@class,'absolute') and contains(@class,'z-')]//li"));
    private static final Locator HOTEL_NAME_HEADING_PDP = new Locator("HOTEL_NAME_HEADING_PDP", new By.ByCssSelector("h1[data-testid='name']"));
    private static final Locator PROPERTY_GENERAL_INFO_TAB = new Locator("PROPERTY_GENERAL_INFO_TAB",new By.ByXPath("(//*[self::a or self::button or self::div or self::span][normalize-space(text())='Property General Info' or normalize-space(text())='Info Umum Properti'])[1]"));
    private static final Locator PROPERTY_GENERAL_INFO_PROPERTY_NAME_LABEL = new Locator("PROPERTY_GENERAL_INFO_PROPERTY_NAME_LABEL",new By.ByXPath("//*[normalize-space(text())='Property Name' or normalize-space(text())='Nama Properti']"));
    private static final Locator PROPERTY_GENERAL_INFO_REQUEST_CHANGES_BUTTON = new Locator("PROPERTY_GENERAL_INFO_REQUEST_CHANGES_BUTTON", new By.ByXPath("//button[contains(@class,'the-button yellow button')]"));
    private static final Locator PROPERTY_GENERAL_INFO_UPDATE_PROPERTY_NAME_INPUT = new Locator("PROPERTY_GENERAL_INFO_UPDATE_PROPERTY_NAME_INPUT", new By.ByXPath("//input[@name='name']"));
    private static final Locator PROPERTY_GENERAL_INFO_UPDATE_ALIAS_INPUT = new Locator("PROPERTY_GENERAL_INFO_UPDATE_ALIAS_INPUT", new By.ByXPath("//input[@name='alias']"));
    private static final Locator PROPERTY_GENERAL_INFO_SAVE_CHANGES_BUTTON = new Locator("PROPERTY_GENERAL_INFO_SAVE_CHANGES_BUTTON", new By.ByXPath("//button[@type='submit' and contains(@class,'the-button yellow content-button')]"));
    private static final Locator PROPERTY_GENERAL_INFO_SUCCESS_TOAST = new Locator("PROPERTY_GENERAL_INFO_SUCCESS_TOAST", new By.ByXPath("//div[contains(@class,'the-toast success opened')]"));
    private static final Locator PROPERTY_GENERAL_INFO_SEE_CHANGE_LINK = new Locator("PROPERTY_GENERAL_INFO_SEE_CHANGE_LINK", new By.ByXPath("//button[contains(@class,'the-flat-button see-changes-button')]"));
    private static final Locator PROPERTY_GENERAL_INFO_CHANGES_MODAL_TITLE = new Locator("PROPERTY_GENERAL_INFO_CHANGES_MODAL_TITLE", new By.ByXPath("//*[contains(@class,'changes-title')]"));
    private static final Locator PROPERTY_GENERAL_INFO_CHANGES_MODAL_SAVE_BUTTON = new Locator("PROPERTY_GENERAL_INFO_CHANGES_MODAL_SAVE_BUTTON", new By.ByXPath("//button[contains(@class,'the-button primary approve-changes')]"));
    private static final Locator PDP_SEE_ROOM_BTN = new Locator("PDP_SEE_ROOM_BTN", new By.ByXPath("//*[@data-testid='button-see-room']"));
    public void clickSeePropertyPageLink() {
        WebElement link = driverTools.waitForElementToBeClickable(SEE_PROPERTY_PAGE_LINK).orElseThrow();
        driverTools.click(link);
    }

    /**
     * Clicks "See your property page", waits for new tab via DriverTools, and switches to it.
     * Stores extranet handle so closePDPTabAndReturnToExtranet can switch back deterministically.
     */
    public void clickSeePropertyPageLinkAndSwitchToPDPTab() {
        extranetWindowHandle = driver.getWindowHandle();
        int handleCountBeforeClick = driverTools.getWindowHandles().size();
        clickSeePropertyPageLink();
        driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);
    }

    /**
     * Closes current tab (PDP) and switches back to the extranet tab (stored handle).
     */
    public void closePDPTabAndReturnToExtranet() {
        driverTools.closeCurrentTab();
        if (extranetWindowHandle != null && driverTools.getWindowHandles().contains(extranetWindowHandle)) {
            driverTools.switchToWindow(extranetWindowHandle);
        } else if (!driverTools.getWindowHandles().isEmpty()) {
            driverTools.switchToWindow(driverTools.getWindowHandles().iterator().next());
        }
    }

    public boolean isSeePropertyPageLinkPresent() {
        return driverTools.waitForElementToAppear(SEE_PROPERTY_PAGE_LINK, 5).isPresent();
    }

    public void clickLanguageSelector() {
        WebElement button = driverTools.waitForElementToBeClickable(LANGUAGE_SELECTOR_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    /**
     * Select language by the exact name from testdata (e.g. "English", "Bahasa Indonesia", "Japanese").
     * Returns the option's @value (en, id, ja) for welcome assertions, or empty if the element has no value (use languageValue from testdata).
     */
    public String selectLanguageByName(String language) {
        if (language == null || language.isEmpty()) {
            throw new IllegalArgumentException("language must be non-empty");
        }
        String escaped = language.trim().replace("'", "''");
        String xpath = "//ul[contains(@class,'absolute') and contains(@class,'z-')]//li[.//*[normalize-space(.)='" + escaped + "']]";
        Locator locator = new Locator("LANGUAGE_OPTION", new By.ByXPath(xpath));
        WebElement el = driverTools.waitForElementToBeClickable(locator, 5).orElseThrow(() ->
                new IllegalStateException("Language option not found for: " + language));
        String value = el.getAttribute("value");
        driverTools.click(el);
        return value != null ? value : "";
    }

    public boolean isLanguageDropdownVisible() {
        return driverTools.waitForElementToAppear(LANGUAGE_DROPDOWN_LIST, 3).isPresent();
    }

    /** PDP (new tab) loaded - by data-testid on hotel name */
    public boolean isHotelPDPLoaded() {
        return driverTools.waitForElementToAppear(HOTEL_NAME_HEADING_PDP, 10).isPresent();
    }

    /** Hotel name on PDP (call when switched to PDP tab). */
    public String getHotelNameOnPDP() {
        WebElement el = driverTools.waitForElementToAppear(HOTEL_NAME_HEADING_PDP, 10).orElseThrow();
        return driverTools.getText(el).trim();
    }

    public void clickSeeRoom() {
        driverTools.waitForElementToAppear(PDP_SEE_ROOM_BTN, 10);
        driverTools.hoverToElement(PDP_SEE_ROOM_BTN);
        driverTools.clickWithJavascriptExecutor(PDP_SEE_ROOM_BTN);
    }

    public boolean isPropertyGeneralInfoPageLoaded() {
        boolean tabPresent = driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_TAB, 10).isPresent();
        boolean labelPresent = driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_PROPERTY_NAME_LABEL, 10).isPresent();
        return tabPresent && labelPresent;
    }

    public void clickPropertyGeneralInfoRequestChangesButton() {
        driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_REQUEST_CHANGES_BUTTON).orElseThrow();
        driverTools.scrollToElement(PROPERTY_GENERAL_INFO_REQUEST_CHANGES_BUTTON);
        WebElement button = driverTools.waitForElementToBeClickable(PROPERTY_GENERAL_INFO_REQUEST_CHANGES_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    private static Locator getPropertyGeneralInfoStarRatingOption(int starRating) {
        return new Locator("PROPERTY_GENERAL_INFO_STAR_RATING_OPTION", new By.ByXPath(String.format("(//div[@id='star']//div[contains(@class,'custom-radio-container')])[%d]", starRating + 1)));
    }

    private static final Locator PROPERTY_GENERAL_INFO_STAR_RATING_CHECKED_CONTAINER = new Locator(
            "PROPERTY_GENERAL_INFO_STAR_RATING_CHECKED_CONTAINER",
            new By.ByXPath("//div[@id='star']//div[contains(@class,'custom-radio-container') and contains(@class,'checked')]"));

    private static final Locator PROPERTY_GENERAL_INFO_STAR_RATING_ALL_CONTAINERS = new Locator(
            "PROPERTY_GENERAL_INFO_STAR_RATING_ALL_CONTAINERS",
            new By.ByXPath("//div[@id='star']//div[contains(@class,'custom-radio-container')]"));

    public void enterPropertyGeneralInfoPropertyName(String text) {
        WebElement input = driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_UPDATE_PROPERTY_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void enterPropertyGeneralInfoAlias(String text) {
        WebElement input = driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_UPDATE_ALIAS_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void selectPropertyGeneralInfoStarRating(int starRating) {
        WebElement button = driverTools.waitForElementToBeClickable(getPropertyGeneralInfoStarRatingOption(starRating)).orElseThrow();
        driverTools.click(button);
    }

    public void clickPropertyGeneralInfoSaveChangesButton() {
        driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_SAVE_CHANGES_BUTTON).orElseThrow();
        driverTools.scrollToElement(PROPERTY_GENERAL_INFO_SAVE_CHANGES_BUTTON);
        WebElement button = driverTools.waitForElementToBeClickable(PROPERTY_GENERAL_INFO_SAVE_CHANGES_BUTTON).orElseThrow();
        driverTools.clickWithJavascriptExecutor(button);
    }

    public boolean isPropertyGeneralInfoSuccessToastVisible() {
        return driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_SUCCESS_TOAST, 10).isPresent();
    }

    public boolean waitForPropertyGeneralInfoSuccessToastToDisappear() {
        return driverTools.waitForElementToDisappear(PROPERTY_GENERAL_INFO_SUCCESS_TOAST, 15);
    }

    public void clickPropertyGeneralInfoSeeChangeLink() {
        driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_SEE_CHANGE_LINK).orElseThrow();
        driverTools.scrollToElement(PROPERTY_GENERAL_INFO_SEE_CHANGE_LINK);
        WebElement link = driverTools.waitForElementToBeClickable(PROPERTY_GENERAL_INFO_SEE_CHANGE_LINK).orElseThrow();
        driverTools.clickWithJavascriptExecutor(link);
    }

    public boolean isPropertyGeneralInfoChangesModalVisible() {
        return driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_CHANGES_MODAL_TITLE, 10).isPresent();
    }

    public void clickPropertyGeneralInfoChangesModalSaveButton() {
        WebElement button = driverTools.waitForElementToBeClickable(PROPERTY_GENERAL_INFO_CHANGES_MODAL_SAVE_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    private static final int CHANGES_MODAL_ROW_INDEX_PROPERTY_NAME = 1;
    private static final int CHANGES_MODAL_ROW_INDEX_ALIASES = 2;
    private static final int CHANGES_MODAL_ROW_INDEX_STAR_RATING = 3;

    private static Locator getPropertyGeneralInfoChangesModalBeforeRowValueLocator(int rowIndex) {
        return new Locator("PROPERTY_GENERAL_INFO_CHANGES_MODAL_BEFORE_ROW_VALUE",
                new By.ByXPath(String.format(
                        "(//div[contains(@class,'gi-changes')]//div[contains(@class,'changes-before')]//div[contains(@class,'changes-item') and contains(@class,'the-row')])[%d]/div[2]",
                        rowIndex)));
    }

    private static Locator getPropertyGeneralInfoChangesModalAfterRowValueLocator(int rowIndex) {
        return new Locator("PROPERTY_GENERAL_INFO_CHANGES_MODAL_AFTER_ROW_VALUE",
                new By.ByXPath(String.format(
                        "(//div[contains(@class,'gi-changes')]//div[contains(@class,'changes-after')]//div[contains(@class,'changes-item') and contains(@class,'the-row')])[%d]/div[2]",
                        rowIndex)));
    }

    private String readChangesModalRowValueText(Locator locator) {
        return driverTools.waitForElementToAppear(locator, 10).map(driverTools::getText).map(String::trim).orElse("");
    }

    private int parseChangesModalRowValueAsInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getPropertyGeneralInfoChangesModalBeforePropertyName() {
        return readChangesModalRowValueText(getPropertyGeneralInfoChangesModalBeforeRowValueLocator(CHANGES_MODAL_ROW_INDEX_PROPERTY_NAME));
    }

    public String getPropertyGeneralInfoChangesModalBeforeAlias() {
        return readChangesModalRowValueText(getPropertyGeneralInfoChangesModalBeforeRowValueLocator(CHANGES_MODAL_ROW_INDEX_ALIASES));
    }

    public int getPropertyGeneralInfoChangesModalBeforeStarRating() {
        return parseChangesModalRowValueAsInt(readChangesModalRowValueText(getPropertyGeneralInfoChangesModalBeforeRowValueLocator(CHANGES_MODAL_ROW_INDEX_STAR_RATING)));
    }

    public String getPropertyGeneralInfoChangesModalAfterPropertyName() {
        return readChangesModalRowValueText(getPropertyGeneralInfoChangesModalAfterRowValueLocator(CHANGES_MODAL_ROW_INDEX_PROPERTY_NAME));
    }

    public String getPropertyGeneralInfoChangesModalAfterAlias() {
        return readChangesModalRowValueText(getPropertyGeneralInfoChangesModalAfterRowValueLocator(CHANGES_MODAL_ROW_INDEX_ALIASES));
    }

    public int getPropertyGeneralInfoChangesModalAfterStarRating() {
        return parseChangesModalRowValueAsInt(readChangesModalRowValueText(getPropertyGeneralInfoChangesModalAfterRowValueLocator(CHANGES_MODAL_ROW_INDEX_STAR_RATING)));
    }

    public String getDisplayedPropertyNameFromInput() {
        return driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_UPDATE_PROPERTY_NAME_INPUT)
                .map(driverTools::getValue).map(String::trim).orElse("");
    }

    public String getDisplayedAliasFromInput() {
        return driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_UPDATE_ALIAS_INPUT)
                .map(driverTools::getValue).map(String::trim).orElse("");
    }

    public int getDisplayedStarRating() {
        driverTools.waitForElementToAppear(PROPERTY_GENERAL_INFO_STAR_RATING_CHECKED_CONTAINER, 10);
        java.util.List<WebElement> containers = driverTools.elements(PROPERTY_GENERAL_INFO_STAR_RATING_ALL_CONTAINERS)
                .orElse(java.util.Collections.emptyList());
        for (int i = 0; i < containers.size(); i++) {
            String classAttr = containers.get(i).getAttribute("class");
            if (classAttr != null && classAttr.contains("checked")) {
                return i;
            }
        }
        return -1;
    }

    /** Expected welcome prefix from testdata language (e.g. English -> Welcome, Bahasa Indonesia -> Selamat datang, Japanese -> ようこそ). */
    public static String getExpectedWelcomePrefixForLanguage(String language) {
        if (language == null || language.isEmpty()) return "Welcome";
        String lower = language.toLowerCase(Locale.ROOT).trim();
        if (lower.contains("english") || "en".equals(lower)) return "Welcome";
        if (lower.contains("bahasa") || lower.contains("indonesia") || "id".equals(lower)) return "Selamat datang";
        if (lower.contains("japanese") || lower.contains("japan") || "ja".equals(lower) || lower.contains("日本語")) return "ようこそ";
        return "Welcome";
    }
}