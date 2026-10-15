package com.tiket.web.dashboard.page.ttd.CMS.CMSPromoAndLoyaltyPromotionPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.*;

import java.util.Arrays;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.HashMap;
import org.openqa.selenium.By;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

public class CMSPromoAndLoyaltyPromotionPageWeb<T extends WebDriver> extends WebPage<T> {

    public CMSPromoAndLoyaltyPromotionPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSPromoAndLoyaltyPromotionPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSPromoAndLoyaltyPromotionPageDweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator PROMO_AND_LOYALTY_PAGE = new Locator("PROMO_AND_LOYALTY_PAGE", By.xpath("//span[normalize-space()='Promo & Loyalty']"));
    private static final Locator PROMOTION_MENU_ITEM = new Locator("PROMOTION_MENU_ITEM", By.xpath("//a[contains(@class,'sidebar__nav-submenu-item') and normalize-space(text())='Promotion']"));
    private static final Locator BULK_CREATE_BTN = new Locator("BULK_CREATE_BTN", By.xpath("//button[.//span[text()='Bulk create']]"));
    private static final Locator MULTICHANNEL_GEOFENCING_DOWNLOAD_BTN = new Locator("MULTICHANNEL_GEOFENCING_DOWNLOAD_BTN", By.xpath("//div[contains(., 'Multichannel or Geofencing')]//button[.//span[text()='Select product and download file']]"));
    private static final Locator STATUS_DROPDOWN = new Locator("STATUS_DROPDOWN", By.xpath("//div[contains(@class, 'styles_right_slot_wrapper')]/div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator SEARCH_MODAL_BTN = new Locator("SEARCH_MODAL_BTN", By.xpath("//button[.//span[text()='Search']]"));
    private static final Locator PRODUCT_CHECKBOX_LIST = new Locator("PRODUCT_CHECKBOX_LIST", By.xpath("//div[contains(@class, 'ProductList_product_list__')]//input[@type='checkbox' or @type='radio']"));
    private static final Locator STATUS_ACTIVE_OPTION = new Locator("STATUS_ACTIVE_OPTION", By.xpath("//li[@title='Active' or contains(text(), 'Active')]"));
    private static final Locator FIRST_ACTIVE_PACKAGE_CHECKBOX = new Locator("FIRST_ACTIVE_PACKAGE_CHECKBOX", By.xpath("(//div[contains(@class, 'PackageList_package_list_section')]//span[contains(@class, 'Text_variant_positive') and contains(text(), 'Active')]/ancestor::label[1]//input[@type='checkbox' or @type='radio'])[1]"));
    private static final Locator DOWNLOAD_FILE_MODAL_BTN = new Locator("DOWNLOAD_FILE_MODAL_BTN", By.xpath("//button[.//span[normalize-space(text())='Download file']]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'SnackbarsPositioner')]"));
    private static final Locator PRODUCT_LIST_SCROLL_CONTAINER = new Locator("PRODUCT_LIST_SCROLL_CONTAINER", By.xpath("//div[contains(@class, 'ProductList_scroll_container')]"));

    private static final Locator UPLOAD_FILE_TAB = new Locator("UPLOAD_FILE_TAB", By.xpath("//button[@role='tab' and .//span[normalize-space(text())='Upload File']]"));
    private static final Locator UPLOAD_FILE_INPUT = new Locator("UPLOAD_FILE_INPUT", By.xpath("//input[@type='file']"));
    private static final Locator PROMOTION_TYPE_DROPDOWN_INPUT = new Locator("PROMOTION_TYPE_DROPDOWN_INPUT", By.id("select-promotion-type"));
    private static final Locator TARGET_SELECTION_DROPDOWN_INPUT = new Locator("TARGET_SELECTION_DROPDOWN_INPUT", By.id("select-channel"));

    private static final Locator TARGET_COUNTRIES_DROPDOWN_INPUT = new Locator("TARGET_COUNTRIES_DROPDOWN_INPUT", By.id("select-countries"));
    private static final Locator TARGET_COUNTRIES_SEARCH_BOX = new Locator("TARGET_COUNTRIES_SEARCH_BOX", By.xpath("//input[@placeholder='Search country']"));
    private static final Locator TARGET_COUNTRIES_SAVE_BTN = new Locator("TARGET_COUNTRIES_SAVE_BTN", By.xpath("//div[contains(@class, 'CountryMultiSelectField_footer')]//button[.//span[normalize-space(text())='Save']]"));

    private static final Locator START_DATE_CALENDAR_ICON = new Locator("START_DATE_CALENDAR_ICON", By.xpath("//input[@id='start-date']/following-sibling::div[contains(@class, 'styles_right_slot_wrapper')]//div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator END_DATE_CALENDAR_ICON = new Locator("END_DATE_CALENDAR_ICON", By.xpath("//input[@id='end-date']/following-sibling::div[contains(@class, 'styles_right_slot_wrapper')]//div[contains(@class, 'DropdownFormDisplay_icon_container')]"));
    private static final Locator MODAL_BODY = new Locator("MODAL_BODY", By.xpath("//div[contains(@class, 'PromotionBulkCreateUploadTab_form_fields') or contains(@class, 'EarlybookPromotionForm_form') or contains(@class, 'GeofencingPromotionForm_form') or contains(@class, 'MultichannelPromotionForm_form')]"));

    // Date Picker Widget Locators (month/year dropdown-menu style picker, DatePickerDesktop)
    private static final Locator CALENDAR_MONTH_MENU_BTN = new Locator("CALENDAR_MONTH_MENU_BTN", By.xpath("//button[contains(@class, 'Navigation_month_menu_button')]"));
    private static final Locator CALENDAR_YEAR_MENU_BTN = new Locator("CALENDAR_YEAR_MENU_BTN", By.xpath("//button[contains(@class, 'Navigation_year_menu_button')]"));
    private static final Locator CALENDAR_SAVE_BTN = new Locator("CALENDAR_SAVE_BTN", By.xpath("//footer[contains(@class, 'DatePickerDesktop_action_area')]//button[.//span[normalize-space(text())='Save']]"));
    private static final Locator YES_SAVE_PROMOTIONS_BTN = new Locator("YES_SAVE_PROMOTIONS_BTN", By.xpath("//button[.//span[text()='Yes, save promotions']]"));
    private static final Locator CONTINUE_BTN = new Locator("CONTINUE_BTN", By.xpath("//button[.//span[normalize-space(text())='Continue']]"));

    // --- TIME PICKER WIDGET LOCATORS ---
    private static final Locator TIME_PICKER_CONTAINER = new Locator("TIME_PICKER_CONTAINER", By.xpath("//div[contains(@class, 'TimePickerDesktop_time_picker')]"));
    private static final Locator TIME_HOUR_INPUT = new Locator("TIME_HOUR_INPUT", By.xpath("(//input[contains(@class, 'TimePickerDesktop_input')])[1]"));
    private static final Locator TIME_MINUTE_INPUT = new Locator("TIME_MINUTE_INPUT", By.xpath("(//input[contains(@class, 'TimePickerDesktop_input')])[2]"));
    private static final Locator TIME_SECOND_INPUT = new Locator("TIME_SECOND_INPUT", By.xpath("(//input[contains(@class, 'TimePickerDesktop_input')])[3]"));
    private static final Locator TIME_PICKER_SAVE_BTN = new Locator("TIME_PICKER_SAVE_BTN", By.xpath("//div[contains(@class, 'PickerAction_picker_action')]//button[.//span[normalize-space(text())='Save']]"));

    private static final Locator CREATE_NEW_BTN = new Locator("CREATE_NEW_BTN", By.xpath("//button[.//span[text()='Create new']]"));
    private static final Locator ADD_THINGS_TO_DO_BTN = new Locator("ADD_THINGS_TO_DO_BTN", By.xpath("//button[.//span[normalize-space(text())='Add Things To Do']]"));

    // Earlybook Form specific locators
    private static final Locator ADD_TIER_BTN = new Locator("ADD_TIER_BTN", By.xpath("//button[.//span[normalize-space(text())='Add tier']]"));
    private static final Locator EARLYBOOK_DAYS_INPUT = new Locator("EARLYBOOK_DAYS_INPUT", By.xpath("//input[contains(@aria-label, 'Days value')]"));
    private static final Locator EARLYBOOK_SUBSIDY_INPUT = new Locator("EARLYBOOK_SUBSIDY_INPUT", By.xpath("//input[contains(@aria-label, 'Partner subsidy')]"));

    private static final Locator SAVE_EARLYBOOK_BTN = new Locator("SAVE_EARLYBOOK_BTN", By.xpath("//button[contains(@class, 'Button_variant_primary') and .//span[normalize-space(text())='Save']]"));
    private static final Locator CONFIRM_SAVE_PROMOTION_BTN = new Locator("CONFIRM_SAVE_PROMOTION_BTN", By.xpath("//div[contains(@class, 'Dialog_pop_up')]//button[.//span[normalize-space(text())='Save promotion']]"));

    // Promotion List & Action Locators
    private static final Locator EARLYBOOK_TAB = new Locator("EARLYBOOK_TAB", By.xpath("//button[.//span[normalize-space(text())='Earlybook']]"));
    private static final Locator MAIN_SEARCH_BOX = new Locator("MAIN_SEARCH_BOX", By.xpath("//input[@placeholder='Search']"));

    private static final Locator FIRST_ROW_ACTION_BTN = new Locator("FIRST_ROW_ACTION_BTN", By.xpath("(//tbody/tr[1]//button[contains(@class, 'PromotionActionCell_action_btn')])[1]"));
    private static final Locator EDIT_OPTION = new Locator("EDIT_OPTION", By.xpath("//button[.//span[normalize-space(text())='Edit']]"));
    private static final Locator DELETE_OPTION = new Locator("DELETE_OPTION", By.xpath("//button[.//span[normalize-space(text())='Delete']]"));
    private static final Locator CONFIRM_DELETE_BTN = new Locator("CONFIRM_DELETE_BTN", By.xpath("//button[.//span[contains(text(), 'Yes, delete')]]"));

    // Geofencing Locators
    private static final Locator GEOFENCING_TAB = new Locator("GEOFENCING_TAB", By.xpath("//button[.//span[normalize-space(text())='Geofencing']]"));
    private static final Locator GEOFENCING_COUNTRY_DROPDOWN = new Locator("GEOFENCING_COUNTRY_DROPDOWN", By.id("select-country"));
    private static final Locator GEOFENCING_SUBSIDY_INPUT = new Locator("GEOFENCING_SUBSIDY_INPUT", By.xpath("//input[starts-with(@aria-label, 'Partner subsidy')]"));

    //Multichannel Locators
    private static final Locator MULTICHANNEL_TAB = new Locator("MULTICHANNEL_TAB", By.xpath("//button[.//span[normalize-space(text())='Multichannel']]"));


    private static final String TTD_RESOURCES_DIR = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "test"
            + File.separator + "resources"
            + File.separator + "dashboard"
            + File.separator + "ttd";

    public void goToPromotionMenu() {
        driverTools.waitForElementToAppear(PROMO_AND_LOYALTY_PAGE);
        driverTools.clickWithJavascriptExecutor(PROMO_AND_LOYALTY_PAGE);

        driverTools.waitForElementToAppear(PROMOTION_MENU_ITEM, 20);
        driverTools.clickWithJavascriptExecutor(PROMOTION_MENU_ITEM);
        driverTools.refreshWebpage();
    }

    public void clickBulkCreateButton() {
        driverTools.waitForElementToAppear(BULK_CREATE_BTN, 10);
        driverTools.clickWithJavascriptExecutor(BULK_CREATE_BTN);
    }

    public File downloadFormatFile(String expectedFileName) throws Exception {
        File targetDir = new File(TTD_RESOURCES_DIR);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        WebDriver rawDriver = driverTools.getDriver();
        if (rawDriver instanceof ChromeDriver) {
            Map<String, Object> commandParams = new HashMap<>();
            commandParams.put("behavior", "allow");
            commandParams.put("downloadPath", targetDir.getAbsolutePath());
            ((ChromeDriver) rawDriver).executeCdpCommand("Page.setDownloadBehavior", commandParams);
        } else {
            System.out.println(" Not using ChromeDriver. File might go to default Downloads folder.");
        }

        driverTools.waitForElementToAppear(MULTICHANNEL_GEOFENCING_DOWNLOAD_BTN, 10);
        driverTools.clickWithJavascriptExecutor(MULTICHANNEL_GEOFENCING_DOWNLOAD_BTN);

        driverTools.waitForElementToAppear(STATUS_DROPDOWN, 10);
        driverTools.clickWithJavascriptExecutor(STATUS_DROPDOWN);

        driverTools.waitForElementToAppear(STATUS_ACTIVE_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(STATUS_ACTIVE_OPTION);

        driverTools.clickWithJavascriptExecutor(SEARCH_MODAL_BTN);
        //wait for list to be loaded
        driverTools.dummyWait(3);

        String downloadedProductName = scanProductsForActivePackage(DOWNLOAD_FILE_MODAL_BTN);
        boolean fileDownloaded = downloadedProductName != null;

        if (!fileDownloaded) {
            throw new Exception("No product with an active package found to download.");
        }

        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 15);

        return waitForDownloadedFile(expectedFileName, 30, targetDir);
    }

    /**
     * Scans products and clicks the very first active package encountered.
     *
     * @return -1 if an active package was found and Download was clicked;
     *         otherwise returns the count of scanned products.
     */
    private String scanProductsForActivePackage(Locator targetButtonToClick) {
        int checkedCount = 0;
        int previousTotal = -1;
        int maxProductsToScan = 500;

        while (checkedCount < maxProductsToScan) {
            int currentTotal = driverTools.elementsUntilExist(PRODUCT_CHECKBOX_LIST).get().size();

            if (currentTotal == checkedCount) {
                if (currentTotal == previousTotal) {
                    break;
                }
                previousTotal = currentTotal;
                driverTools.scrollToBottomOfTable(PRODUCT_LIST_SCROLL_CONTAINER);
                driverTools.dummyWait(2);
                continue;
            }

            for (int i = checkedCount + 1; i <= currentTotal; i++) {
                Locator currentProductCheckbox = new Locator(
                        "PRODUCT_CHECKBOX_" + i,
                        By.xpath("(//div[contains(@class, 'ProductList_product_list__')]//input[@type='checkbox' or @type='radio'])[" + i + "]")
                );

                driverTools.scrollToElement(currentProductCheckbox);
                driverTools.clickWithJavascriptExecutor(currentProductCheckbox);
                //waiting for list to be loaded
                driverTools.dummyWait(2);

                if (driverTools.isElementPresent(FIRST_ACTIVE_PACKAGE_CHECKBOX)) {
                    driverTools.clickWithJavascriptExecutor(FIRST_ACTIVE_PACKAGE_CHECKBOX);

                    // CAPTURE THE NAME BEFORE CLICKING ADD/DOWNLOAD
                    // 1. Define the Locator
                    Locator nameLoc = new Locator("PRODUCT_NAME", By.xpath("(//div[contains(@class, 'ProductList_product_list__')]//input[@type='checkbox' or @type='radio'])[" + i + "]/ancestor::label//div[contains(@class, 'Text_weight_bold')]"));
                    // 2. Convert Locator to WebElement (just like your reference method!)
                    WebElement nameElement = driverTools.waitForElementToAppear(nameLoc).orElseThrow();
                    // 3. Get the text using the WebElement
                    String productName = driverTools.getText(nameElement);

                    driverTools.waitForElementToBeClickable(targetButtonToClick, 10);
                    driverTools.click(targetButtonToClick);
                    return productName;
                } else {
                    driverTools.clickWithJavascriptExecutor(currentProductCheckbox);
                    driverTools.dummyWait(1);
                }
            }

            checkedCount = currentTotal;
        }

        return null;
    }

    /**
     * Dynamically polls the Downloads directory for the downloaded file.
     */
    /**
     * Dynamically polls the target directory for the downloaded file.
     */
    private File waitForDownloadedFile(String fileNamePart, int timeoutSeconds, File targetDir) throws Exception {
        String cleanName = fileNamePart.trim().toLowerCase();

        for (int i = 0; i < timeoutSeconds; i++) {
            File[] files = targetDir.listFiles((dir, name) ->
                    name.toLowerCase().contains(cleanName) &&
                            !name.endsWith(".crdownload") &&
                            !name.endsWith(".part") &&
                            !name.endsWith(".tmp"));

            if (files != null && files.length > 0) {
                Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
                System.out.println("File found successfully: " + files[0].getAbsolutePath());
                return files[0];
            }
            driverTools.dummyWait(1);
        }

        throw new FileNotFoundException("File containing name '" + cleanName + "' was NOT found in " + targetDir.getAbsolutePath() + " after " + timeoutSeconds + " seconds.");
    }

    public void goToUploadTab() {
        driverTools.waitForElementToAppear(UPLOAD_FILE_TAB, 10);
        driverTools.clickWithJavascriptExecutor(UPLOAD_FILE_TAB);
        driverTools.dummyWait(1);
    }

    public void uploadPromotionFile(String absolutePath) {
        driverTools.waitForElementToPresence(UPLOAD_FILE_INPUT, 10);

        try {
            driverTools.makeElementVisible(UPLOAD_FILE_INPUT);
        } catch (Exception e) {
            logger.log("Could not alter input visibility via JS, attempting standard sendKeys");
        }

        driverTools.sendKeys(UPLOAD_FILE_INPUT, absolutePath);
        //waiting to file to be uploaded
        driverTools.dummyWait(3);
    }

    public void clickContinueButton() {
        driverTools.waitForElementToBeClickable(CONTINUE_BTN, 10);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BTN);
    }

    public void fillPromotionFormatFile(File file, Map<String, String> param) {
        logger.log("Processing and filling data for file: " + file.getAbsolutePath());
    }

    public void selectPromotionTypeAndTarget(String promotionType, String targetSelection) {
        driverTools.waitForElementToAppear(PROMOTION_TYPE_DROPDOWN_INPUT, 10);
        driverTools.scrollToElement(PROMOTION_TYPE_DROPDOWN_INPUT);
        driverTools.clickWithJavascriptExecutor(PROMOTION_TYPE_DROPDOWN_INPUT);

        Locator promoTypeOption = new Locator("PROMO_TYPE_OPTION_" + promotionType, By.xpath("//li[contains(text(), '" + promotionType + "')]"));
        driverTools.waitForElementToAppear(promoTypeOption, 5);
        driverTools.clickWithJavascriptExecutor(promoTypeOption);
        driverTools.dummyWait(1);

        driverTools.waitForElementToAppear(TARGET_SELECTION_DROPDOWN_INPUT, 10);
        driverTools.scrollToElement(TARGET_SELECTION_DROPDOWN_INPUT);
        driverTools.clickWithJavascriptExecutor(TARGET_SELECTION_DROPDOWN_INPUT);

        Locator targetOption = new Locator("TARGET_OPTION_" + targetSelection, By.xpath("//li[contains(text(), '" + targetSelection + "')]"));
        driverTools.waitForElementToAppear(targetOption, 5);
        driverTools.clickWithJavascriptExecutor(targetOption);
        driverTools.dummyWait(1);
    }

    public void selectPromotionTypeAndTargetGeofencing(String promotionType, String targetCountries) {
        driverTools.waitForElementToAppear(PROMOTION_TYPE_DROPDOWN_INPUT, 10);
        driverTools.scrollToElement(PROMOTION_TYPE_DROPDOWN_INPUT);
        driverTools.clickWithJavascriptExecutor(PROMOTION_TYPE_DROPDOWN_INPUT);

        Locator promoTypeOption = new Locator("PROMO_TYPE_OPTION_" + promotionType, By.xpath("//li[contains(text(), '" + promotionType + "')]"));
        driverTools.waitForElementToAppear(promoTypeOption, 5);
        driverTools.clickWithJavascriptExecutor(promoTypeOption);
        driverTools.dummyWait(1);

        driverTools.waitForElementToAppear(TARGET_COUNTRIES_DROPDOWN_INPUT, 10);
        driverTools.scrollToElement(TARGET_COUNTRIES_DROPDOWN_INPUT);
        driverTools.clickWithJavascriptExecutor(TARGET_COUNTRIES_DROPDOWN_INPUT);
        driverTools.dummyWait(1);

        String[] countries = targetCountries.split(",");
        for (String country : countries) {
            String trimmedCountry = country.trim();

            if (driverTools.isElementPresent(TARGET_COUNTRIES_SEARCH_BOX)) {
                driverTools.clear(TARGET_COUNTRIES_SEARCH_BOX);
                driverTools.sendKeys(TARGET_COUNTRIES_SEARCH_BOX, trimmedCountry);
                driverTools.dummyWait(1);
            }

            Locator countryCheckbox = new Locator("COUNTRY_CHECKBOX_" + trimmedCountry, By.xpath("//input[@aria-label='Toggle " + trimmedCountry + "']"));

            driverTools.waitForElementToAppear(countryCheckbox, 5);
            driverTools.clickWithJavascriptExecutor(countryCheckbox);
            driverTools.dummyWait(1);
        }

        driverTools.waitForElementToBeClickable(TARGET_COUNTRIES_SAVE_BTN, 5);
        driverTools.clickWithJavascriptExecutor(TARGET_COUNTRIES_SAVE_BTN);
        driverTools.dummyWait(1); // Brief pause to allow the dropdown to close
    }

    public void fillBookingPeriodDatesAndTimes(String startDate, String startTime, String endDate, String endTime) {

        driverTools.scrollToElement(START_DATE_CALENDAR_ICON);
        driverTools.clickWithJavascriptExecutor(START_DATE_CALENDAR_ICON);

        selectDateInPicker(startDate);
        logger.log("Expected Start Date: " + startDate);

        selectTimeInPicker(startTime);
        logger.log("Expected Start Time: " + startTime);

        driverTools.scrollToElement(END_DATE_CALENDAR_ICON);
        driverTools.clickWithJavascriptExecutor(END_DATE_CALENDAR_ICON);

        selectDateInPicker(endDate);
        logger.log("Expected End Date: " + endDate);

        selectTimeInPicker(endTime);
        logger.log("Expected End Time: " + endTime);

        driverTools.clickWithJavascriptExecutor(MODAL_BODY);
        driverTools.dummyWait(3);
    }

    /**
     * Handles the time picker popup by splitting the time string (HH:mm or HH:mm:ss)
     * and injecting the values into the respective React inputs.
     */
    private void selectTimeInPicker(String time) {
        driverTools.waitForElementToAppear(TIME_PICKER_CONTAINER, 10);

        String[] timeParts = time.trim().split(":");
        String hour = timeParts.length > 0 ? timeParts[0] : "00";
        String minute = timeParts.length > 1 ? timeParts[1] : "00";
        String second = timeParts.length > 2 ? timeParts[2] : "00";

        driverTools.waitForElementToAppear(TIME_HOUR_INPUT, 5);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(TIME_HOUR_INPUT, hour);

        driverTools.waitForElementToAppear(TIME_MINUTE_INPUT, 5);
        driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(TIME_MINUTE_INPUT, minute);

        if (timeParts.length > 2 && driverTools.isElementPresent(TIME_SECOND_INPUT)) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(TIME_SECOND_INPUT, second);
        }

        driverTools.waitForElementToBeClickable(TIME_PICKER_SAVE_BTN, 5);
        driverTools.clickWithJavascriptExecutor(TIME_PICKER_SAVE_BTN);
        driverTools.waitForElementToDisappear(TIME_PICKER_CONTAINER, 5);
    }

    /**
     * Selects a date (format "dd MMMM yyyy" with the English month name, e.g. "07 October 2026")
     * in the DatePickerDesktop widget: opens the month dropdown and picks the target month, opens
     * the year dropdown and picks the target year, then clicks the day button matched by its
     * aria-label, and confirms with Save.
     */
    private void selectDateInPicker(String date) {
        String[] parts = date.trim().split("\\s+");
        String day = parts[0].replaceFirst("^0(?=\\d)", ""); // aria-label uses non zero-padded day, e.g. "7" not "07"
        String monthEng = parts[1];
        String year = parts[2];
        String monthId = CoreFunctions.monthEngToId(monthEng);

        driverTools.waitForElementToAppear(CALENDAR_MONTH_MENU_BTN, 5);
        driverTools.clickWithJavascriptExecutor(CALENDAR_MONTH_MENU_BTN);
        Locator monthOption = new Locator("CALENDAR_MONTH_OPTION_" + monthEng, By.xpath("//div[contains(@class, 'month-year-menus_month_grid')]//button[normalize-space(text())='" + monthEng + "' or normalize-space(text())='" + monthId + "']"));
        driverTools.waitForElementToAppear(monthOption, 5);
        driverTools.clickWithJavascriptExecutor(monthOption);

        driverTools.waitForElementToAppear(CALENDAR_YEAR_MENU_BTN, 5);
        driverTools.clickWithJavascriptExecutor(CALENDAR_YEAR_MENU_BTN);
        Locator yearOption = new Locator("CALENDAR_YEAR_OPTION_" + year, By.xpath("//div[contains(@class, 'month-year-menus_year_grid')]//button[normalize-space(text())='" + year + "']"));
        driverTools.waitForElementToAppear(yearOption, 5);
        driverTools.clickWithJavascriptExecutor(yearOption);

        Locator dayOption = new Locator("CALENDAR_DAY_OPTION_" + day + "_" + monthEng + "_" + year, By.xpath("//button[contains(@class, 'Day_day__')][.//span[contains(@aria-label, '" + day + " " + monthEng + " " + year + "') or contains(@aria-label, '" + day + " " + monthId + " " + year + "')]]"));
        driverTools.waitForElementToAppear(dayOption, 5);
        driverTools.clickWithJavascriptExecutor(dayOption);

        if (driverTools.isElementPresent(CALENDAR_SAVE_BTN)) {
            driverTools.clickWithJavascriptExecutor(CALENDAR_SAVE_BTN);
        }
    }

    public void clickYesSavePromotionsButton() {
        driverTools.waitForElementToBeClickable(YES_SAVE_PROMOTIONS_BTN, 10);
        driverTools.clickWithJavascriptExecutor(YES_SAVE_PROMOTIONS_BTN);
        driverTools.waitForElementToDisappear(YES_SAVE_PROMOTIONS_BTN);
    }

    public boolean isSuccessAlertDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 15);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public void waitForSnackbarToDisappear() {
        driverTools.waitForElementToDisappear(SUCCESS_SNACKBAR);
    }

    /**
     * Clicks "Create new" and selects the option from the dropdown (e.g. "Earlybook")
     */
    public void clickCreateNewPromotionOption(String optionName) {
        driverTools.waitForElementToBeClickable(CREATE_NEW_BTN, 10);
        driverTools.click(CREATE_NEW_BTN);

        Locator dropdownOption = new Locator("CREATE_NEW_OPTION_" + optionName.toUpperCase(),
                By.xpath("//button[contains(@class, 'CreateNewDropdown_dropdown_item')]//span[normalize-space(text())='" + optionName + "']"));
        driverTools.waitForElementToAppear(dropdownOption, 5);
        driverTools.click(dropdownOption);
    }

    /**
     * Executes the flow to set Status to Active, Search, scan the list,
     * check an active product/package, and click "Add Things To Do".
     */
    public String searchAndSelectActiveProductAndPackage() throws Exception {
        driverTools.waitForElementToAppear(STATUS_DROPDOWN, 10);
        driverTools.clickWithJavascriptExecutor(STATUS_DROPDOWN);


        driverTools.waitForElementToAppear(STATUS_ACTIVE_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(STATUS_ACTIVE_OPTION);

        driverTools.clickWithJavascriptExecutor(SEARCH_MODAL_BTN);
        //wait for list to be loaded
        driverTools.dummyWait(3);

        String activeProductName = scanProductsForActivePackage(ADD_THINGS_TO_DO_BTN);

        if (activeProductName == null) {
            throw new Exception("No product with an active package found to add.");
        }

        driverTools.dummyWait(2); // Wait for modal to close
        return activeProductName;
    }

    /**
     * Fills the "Days" and "Partner Subsidy" inputs for an Earlybook promotion.
     */
    public void fillEarlybookTierDetails(String days, String partnerSubsidy) {
        driverTools.waitForElementToBeClickable(ADD_TIER_BTN, 10);
        driverTools.clickWithJavascriptExecutor(ADD_TIER_BTN);
        driverTools.dummyWait(1);

        driverTools.waitForElementToAppear(EARLYBOOK_DAYS_INPUT, 5);
        driverTools.clearInput(EARLYBOOK_DAYS_INPUT);
        driverTools.sendKeys(EARLYBOOK_DAYS_INPUT, days);

        driverTools.waitForElementToAppear(EARLYBOOK_SUBSIDY_INPUT, 5);
        driverTools.clearInput(EARLYBOOK_SUBSIDY_INPUT);
        driverTools.sendKeys(EARLYBOOK_SUBSIDY_INPUT, partnerSubsidy);
    }

    /**
     * Clicks the final Save button on the form, and confirms the save modal.
     */
    public void submitAndSavePromotion() {
        driverTools.waitForElementToBeClickable(SAVE_EARLYBOOK_BTN, 10);
        driverTools.clickWithJavascriptExecutor(SAVE_EARLYBOOK_BTN);

        driverTools.waitForElementToBeClickable(CONFIRM_SAVE_PROMOTION_BTN, 10);
        driverTools.clickWithJavascriptExecutor(CONFIRM_SAVE_PROMOTION_BTN);
    }

    public void clickEarlybookTab() {
        driverTools.waitForElementToBeClickable(EARLYBOOK_TAB, 10);
        driverTools.clickWithJavascriptExecutor(EARLYBOOK_TAB);
        driverTools.dummyWait(2);
    }

    public void searchPromotionByName(String productName) {
        driverTools.waitForElementToAppear(MAIN_SEARCH_BOX, 10);
        driverTools.clearInput(MAIN_SEARCH_BOX);

        driverTools.sendKeys(MAIN_SEARCH_BOX, productName + "\n");

        // Wait for table to filter
        driverTools.dummyWait(4);
    }

    /**
     * Edits the "Days" and "Partner Subsidy" inputs for an existing Earlybook promotion.
     * Skips clicking "Add tier" because the row already exists in Edit mode.
     */
    public void editEarlybookTierDetails(String days, String partnerSubsidy) {
        driverTools.waitForElementToAppear(EARLYBOOK_DAYS_INPUT, 5);
        driverTools.clearInput(EARLYBOOK_DAYS_INPUT);
        driverTools.sendKeys(EARLYBOOK_DAYS_INPUT, days);

        driverTools.waitForElementToAppear(EARLYBOOK_SUBSIDY_INPUT, 5);
        driverTools.clearInput(EARLYBOOK_SUBSIDY_INPUT);
        driverTools.sendKeys(EARLYBOOK_SUBSIDY_INPUT, partnerSubsidy);
    }

    public void clickEditOnFirstRow() {
        driverTools.waitForElementToBeClickable(FIRST_ROW_ACTION_BTN, 10);
        driverTools.clickWithJavascriptExecutor(FIRST_ROW_ACTION_BTN);
        driverTools.dummyWait(1);

        driverTools.waitForElementToBeClickable(EDIT_OPTION, 5);
        driverTools.clickWithJavascriptExecutor(EDIT_OPTION);
    }

    public void clickDeleteOnFirstRowAndConfirm() {
        driverTools.waitForElementToBeClickable(FIRST_ROW_ACTION_BTN, 10);
        driverTools.clickWithJavascriptExecutor(FIRST_ROW_ACTION_BTN);
        driverTools.dummyWait(1);

        driverTools.waitForElementToBeClickable(DELETE_OPTION, 5);
        driverTools.clickWithJavascriptExecutor(DELETE_OPTION);

        driverTools.waitForElementToBeClickable(CONFIRM_DELETE_BTN, 5);
        driverTools.clickWithJavascriptExecutor(CONFIRM_DELETE_BTN);
    }

    public void clickGeofencingTab() {
        driverTools.waitForElementToBeClickable(GEOFENCING_TAB, 10);
        driverTools.clickWithJavascriptExecutor(GEOFENCING_TAB);
        driverTools.dummyWait(2);
    }

    public void fillGeofencingPartnerSubsidy(String partnerSubsidy) {
        driverTools.waitForElementToAppear(GEOFENCING_SUBSIDY_INPUT, 10);
        driverTools.clearInput(GEOFENCING_SUBSIDY_INPUT);
        driverTools.sendKeys(GEOFENCING_SUBSIDY_INPUT, partnerSubsidy);
    }

    public void selectGeofencingTargetCountries(String targetCountries) {
        driverTools.waitForElementToAppear(GEOFENCING_COUNTRY_DROPDOWN, 10);
        driverTools.scrollToElement(GEOFENCING_COUNTRY_DROPDOWN);
        driverTools.clickWithJavascriptExecutor(GEOFENCING_COUNTRY_DROPDOWN);
        driverTools.dummyWait(1);

        String[] countries = targetCountries.split(",");
        for (String country : countries) {
            String trimmedCountry = country.trim();

            if (driverTools.isElementPresent(TARGET_COUNTRIES_SEARCH_BOX)) {
                driverTools.clear(TARGET_COUNTRIES_SEARCH_BOX);
                driverTools.sendKeys(TARGET_COUNTRIES_SEARCH_BOX, trimmedCountry);
                driverTools.dummyWait(1);
            }

            Locator countryCheckbox = new Locator("COUNTRY_CHECKBOX_" + trimmedCountry, By.xpath("//input[@aria-label='Toggle " + trimmedCountry + "']"));
            driverTools.waitForElementToAppear(countryCheckbox, 5);
            driverTools.clickWithJavascriptExecutor(countryCheckbox);
            driverTools.dummyWait(1);
        }

        driverTools.waitForElementToBeClickable(TARGET_COUNTRIES_SAVE_BTN, 5);
        driverTools.clickWithJavascriptExecutor(TARGET_COUNTRIES_SAVE_BTN);
        driverTools.dummyWait(1); // Brief pause to allow the dropdown to close
    }

    public void clickMultichannelTab() {
        driverTools.waitForElementToBeClickable(MULTICHANNEL_TAB, 10);
        driverTools.clickWithJavascriptExecutor(MULTICHANNEL_TAB);
        driverTools.dummyWait(2);
    }

    public void fillMultichannelPartnerSubsidy(String partnerSubsidy) {
        driverTools.waitForElementToAppear(GEOFENCING_SUBSIDY_INPUT, 10);
        driverTools.clearInput(GEOFENCING_SUBSIDY_INPUT);
        driverTools.sendKeys(GEOFENCING_SUBSIDY_INPUT, partnerSubsidy);
    }
}