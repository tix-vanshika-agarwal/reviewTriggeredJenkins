package com.tiket.web.dashboard.page.accommodation.internal_supplier.dashboard;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.NoSuchElementException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.tiket.testbase.Assertion;
import static org.hamcrest.Matchers.containsString;

public class InternalSupplierDashboardPage<T extends WebDriver> extends WebPage<T> {
    public InternalSupplierDashboardPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InternalSupplierDashboardPage<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new InternalSupplierDashboardPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new InternalSupplierDashboardPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }
    
    private static final Locator ROOMS_MAPPING_MENU = new Locator("ROOMS_MAPPING_MENU", By.xpath("//span[text()='Rooms Mapping']"));
    private static final Locator MAP_ROOMS_BUTTON = new Locator("MAP_ROOMS_BUTTON", By.xpath("//span[text()='Map Rooms']"));
    private static final Locator ROOMS_MAPPING_LOG_BUTTON = new Locator("ROOMS_MAPPING_LOG_BUTTON", By.xpath("//span[text()='Rooms Mapping Log']"));
    private static final Locator VENDOR_DROPDOWN_INPUT = new Locator("VENDOR_DROPDOWN_INPUT", By.xpath("//input[@id='vendor' and @name='businessId']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator VENDOR_BUTTON = new Locator("VENDOR_BUTTON", By.xpath("//button[@data-testid='vendor']"));
    private static final Locator LOCATION_BUTTON = new Locator("LOCATION_BUTTON", By.xpath("//button[@data-testid='location']"));
    private static final Locator SEARCH_INPUT_VENDOR_PROPERTY_ID = new Locator("SEARCH_INPUT", By.xpath("//input[@name='vendorPropertyId']"));
    private static final Locator MAPPING_STATUS_HEADER = new Locator("MAPPING_STATUS_HEADER", By.xpath("//div[@data-testid='test-id-sort--mappingPercentage']"));
    private static final Locator SNACKBAR_MESSAGE = new Locator("SNACKBAR_MESSAGE", By.xpath("//div[contains(@class,'Snackbar_text_content')]"));
    private static final Locator RESET_BUTTON = new Locator("RESET_BUTTON", By.xpath("//button[normalize-space()='Reset']"));
    private static final Locator SEARCH_INPUT_VENDOR_PROPERTY_ID_LOG = new Locator("SEARCH_INPUT_VENDOR_PROPERTY_ID_LOG", By.xpath("//input[@id='search-by-vendor-property-id']"));
    private static final Locator SEARCH_INPUT_VENDOR_ROOM_ID_LOG = new Locator("SEARCH_INPUT_VENDOR_ROOM_ID_LOG", By.xpath("//input[@id='search-by-vendor-room-id']"));
    private static final Locator DATE_RANGE_INPUT = new Locator("DATE_RANGE_INPUT", By.xpath("//input[@id='start--end-date']"));
    private static final Locator CONTINUE_POPUP_BUTTON = new Locator("CONTINUE_POPUP_BUTTON", By.xpath("//div[contains(@class, 'Dialog_pop_up') or contains(@class,'Modal_modal_footer_button_group')]//button[normalize-space()='Continue']"));
    private static final Locator OVERVIEW_MENU = new Locator("OVERVIEW_MENU", By.xpath("//button[.//span[@data-testid='nav-label' and normalize-space()='Overview']]"));
    private static final Locator OVERVIEW_PAGE_TITLE = new Locator("OVERVIEW_PAGE_TITLE", By.xpath("//span[@id='title-page-id' and normalize-space()='Overview']"));
    private static final Locator PROPERTY_OVERVIEW_SECTION = new Locator("PROPERTY_OVERVIEW_SECTION", By.xpath("//p[normalize-space()='Property Overview']"));
    private static final Locator PROPERTY_OVERVIEW_CARDS_CONTAINER = new Locator("PROPERTY_OVERVIEW_CARDS_CONTAINER", By.xpath("//p[normalize-space()='Property Overview']/ancestor::div[contains(@class,'flex-col')][1]//div[contains(@class,'justify-between') and contains(@class,'flex-wrap')]"));
    private static final Locator ROOM_OVERVIEW_SECTION = new Locator("ROOM_OVERVIEW_SECTION", By.xpath("//p[normalize-space()='Room Overview']"));
    private static final Locator ROOM_OVERVIEW_CARDS_CONTAINER = new Locator("ROOM_OVERVIEW_CARDS_CONTAINER", By.xpath("//p[normalize-space()='Room Overview']/ancestor::div[contains(@class,'flex-col')][1]//div[contains(@class,'justify-between') and contains(@class,'flex-wrap')]"));

    public void goToOverviewPage() {
        if (driverTools.waitForElementToAppear(OVERVIEW_MENU, 5).isPresent()) {
            driverTools.waitForElementToBeClickable(OVERVIEW_MENU, 10).ifPresent(driverTools::click);
        }
        driverTools.waitForElementToAppear(OVERVIEW_PAGE_TITLE, 15);
    }

    public boolean isOverviewPageLoaded() {
        return driverTools.waitForElementToAppear(OVERVIEW_PAGE_TITLE, 15).isPresent();
    }

    public boolean isPropertyOverviewDisplayed() {
        boolean sectionVisible = driverTools.waitForElementToAppear(PROPERTY_OVERVIEW_SECTION, 15).isPresent();
        boolean hasData = driverTools.waitForElementToAppear(PROPERTY_OVERVIEW_CARDS_CONTAINER, 10).isPresent();
        logger.log("[PropertyOverview] section visible: " + sectionVisible + ", has data cards: " + hasData);
        return sectionVisible && hasData;
    }

    public boolean isRoomOverviewDisplayed() {
        boolean sectionVisible = driverTools.waitForElementToAppear(ROOM_OVERVIEW_SECTION, 15).isPresent();
        boolean hasData = driverTools.waitForElementToAppear(ROOM_OVERVIEW_CARDS_CONTAINER, 10).isPresent();
        logger.log("[RoomOverview] section visible: " + sectionVisible + ", has data cards: " + hasData);
        return sectionVisible && hasData;
    }

    public boolean isVendorNameSelected(String vendorName) {
        return driverTools.waitForElementToAppear(VENDOR_DROPDOWN_INPUT, 5)
                .map(element -> {
                    String selectedValue = driverTools.getValue(element);
                    if (selectedValue == null || selectedValue.isBlank()) {
                        selectedValue = element.getAttribute("value");
                    }
                    logger.log("Selected vendor value: " + selectedValue);
                    return selectedValue != null && selectedValue.equalsIgnoreCase(vendorName);
                })
                .orElse(false);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static Locator vendorOption(String vendorName) {
        String upper = vendorName.toUpperCase();
        String lower = vendorName.toLowerCase();
        String abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String abcLower = "abcdefghijklmnopqrstuvwxyz";
        return new Locator("VENDOR_OPTION_" + vendorName, By.xpath(String.format(
                "//li[(starts-with(@data-testid,'dropdown-test-id') and normalize-space()='%1$s')"
                        + " or @data-testid='dropdown-test-id-%1$s'"
                        + " or translate(@data-value,'%2$s','%3$s')='%4$s'"
                        + " or translate(@data-label,'%2$s','%3$s')='%4$s'"
                        + " or translate(normalize-space(),'%2$s','%3$s')='%4$s']",
                upper, abc, abcLower, lower)));
    }

    public void clickRoomsMappingMenu() {
        driverTools.click(ROOMS_MAPPING_MENU);
    }

    public void clickMapRoomsButton() {
        driverTools.click(MAP_ROOMS_BUTTON);
    }

    public void clickRoomsMappingLogButton() {
        driverTools.click(ROOMS_MAPPING_LOG_BUTTON);
    }

    public void goToMapRoomsPage() {
        clickRoomsMappingMenu();
        driverTools.waitForElementToBeClickable(MAP_ROOMS_BUTTON, 10);
        clickMapRoomsButton();
    }

    public void goToRoomsMappingLogPage() {
        clickRoomsMappingMenu();
        clickRoomsMappingLogButton();
    }

    public void filterByVendorName(String vendorName) {
        // Vendor may already be preselected (e.g. AGODA by default): nothing to do.
        // The default value is filled in after the page loads, so wait (up to 5s) until the input has a value.
        // Not an error if it never fills: the dropdown is then used below.
        driverTools.waitUntil(driver -> {
            List<WebElement> inputs = driver.findElements(VENDOR_DROPDOWN_INPUT.by());
            return !inputs.isEmpty() && !isBlank(inputs.get(0).getAttribute("value"));
        }, 5);
        if (isVendorNameSelected(vendorName)) {
            logger.log("Vendor already selected, skipping dropdown: " + vendorName);
            return;
        }
        WebElement vendorDropdown = driverTools.waitForElementToAppear(VENDOR_DROPDOWN_INPUT, 10).orElseThrow();
        driverTools.scrollElementToCenter(vendorDropdown);
        driverTools.waitForElementToBeClickable(VENDOR_DROPDOWN_INPUT, 10);
        Locator vendorOption = vendorOption(vendorName);
        // This dropdown component opens from its chevron container (same approach as the User Management page)
        try {
            WebElement chevron = vendorDropdown.findElement(By.xpath(
                    "./ancestor::div[contains(@class,'input_inner_wrapper')]"
                            + "//div[contains(@class,'DropdownFormDisplay_icon_container')]"));
            driverTools.clickWithJavascriptExecutor(chevron);
        } catch (Exception e) {
            driverTools.clickWithActions(VENDOR_DROPDOWN_INPUT);
        }
        driverTools.waitForElementToAppear(vendorOption, 10).orElseThrow();
        driverTools.scrollElementToCenter(driverTools.waitForElementToAppear(vendorOption, 5).orElseThrow());
        driverTools.clickWithJavascriptExecutor(vendorOption);
    }

    public void clickVendorButton() {
        driverTools.waitForElementToBeClickable(VENDOR_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(VENDOR_BUTTON);
        driverTools.dummyWait(1);
    }

    public void clickLocationButton() {
        driverTools.waitForElementToBeClickable(LOCATION_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(LOCATION_BUTTON);
        driverTools.dummyWait(1);
    }

    public void inputVendorPropertyId(String vendorPropertyId) {
        // keep the input away from the fixed header, which otherwise intercepts the click in clearInput
        driverTools.scrollElementToCenter(SEARCH_INPUT_VENDOR_PROPERTY_ID);
        driverTools.waitForElementToBeClickable(SEARCH_INPUT_VENDOR_PROPERTY_ID, 5);
        driverTools.clearInput(SEARCH_INPUT_VENDOR_PROPERTY_ID);
        driverTools.sendKeys(SEARCH_INPUT_VENDOR_PROPERTY_ID, vendorPropertyId);
    }

    public void clickSearch() {
        driverTools.click(SEARCH_BUTTON);
    }

    public void clickResetButton() {
        if(driverTools.isElementDisplayed(RESET_BUTTON)) {
            driverTools.click(RESET_BUTTON);
        }
    }

    public void clickMapButtonByPropertyId(String propertyId) {
        int propertyIdIndex = getIndexOfColumnTitle("Vendor Property ID");
        int actionIndex = getIndexOfColumnTitle("Action"); 
        String xpath = "//tr[./td[" + propertyIdIndex + "][normalize-space()='" + propertyId + "']]/td[" + actionIndex + "]//a[normalize-space()='Map']";
        Locator mapButtonLocator = new Locator("MAP_BUTTON_" + propertyId, By.xpath(xpath));
        driverTools.waitForElementToBeClickable(mapButtonLocator, 5);
        driverTools.scrollElementToCenter(mapButtonLocator);
        driverTools.clickWithJavascriptExecutor(mapButtonLocator);
        driverTools.switchToNewTab(); 
        driverTools.waitForWebToLoad();
    }

    public int getIndexOfColumnTitle(String columnTitle) {
        Locator headersLocator = new Locator("ALL_HEADERS", By.xpath("//thead//th"));
        driverTools.waitForElementToPresence(headersLocator, 15);
        driverTools.dummyWait(1); 
        List<WebElement> headers = driverTools.getDriver().findElements(By.xpath("//thead//th"));
        for (int i = 0; i < headers.size(); i++) {
            try {
                String currentText = headers.get(i).getText().trim();
                if (currentText.equalsIgnoreCase(columnTitle)) {
                    return i + 1; 
                }
            } catch (StaleElementReferenceException e) {
                headers = driverTools.getDriver().findElements(By.xpath("//thead//th"));
                i--;
            }
        }
        throw new NoSuchElementException("There is no data for " + columnTitle);
    }

    public void sortMappingStatus() {
        driverTools.click(MAPPING_STATUS_HEADER);
        driverTools.dummyWait(2);
    }

    public double getFirstMappingStatusValue() {
        int colIndex = getIndexOfColumnTitle("Room Mapping Status");
        String value = driverTools.getText(new Locator("FIRST_MAPPING_STATUS_VALUE", By.xpath("//tbody/tr[1]/td[" + colIndex + "]"))).orElse("0");
        return parseValue(value);
    }

    private double parseValue(String text) {
        String clean = text.replaceAll("[^0-9.]", "");
        return clean.isEmpty() ? 0.0 : Double.parseDouble(clean);
    }

    public boolean isSorted(double before, double after, boolean expectAscending) {
        if (expectAscending) {
            return after >= before; 
        } else {
            return after <= before;
        }
    }

    private static Locator getSearchInputName(String tableLabel) {
        String xpath = "//span[contains(text(), '" + tableLabel + "')]" +
                    "/ancestor::div[contains(@class, 'Card_card_container')]" +
                    "//input[@placeholder='Search by  vendor room name']";
        return new Locator("SEARCH_INPUT_" + tableLabel.toUpperCase().replace(" ", "_"), By.xpath(xpath));
    }

     private static Locator getSearchInputId(String tableLabel) {
        String xpath = "//span[contains(text(), '" + tableLabel + "')]" +
                    "/ancestor::div[contains(@class, 'Card_card_container')]" +
                    "//input[(@placeholder='Search by  vendor room name') or (@placeholder='Search by  vendor room ID')]";
        return new Locator("SEARCH_INPUT_" + tableLabel.toUpperCase().replace(" ", "_"), By.xpath(xpath));
    }

    public void searchByVendorRoomName(String tableLabel, String roomName) {
        Locator searchInput = getSearchInputName(tableLabel);
        // the card re-renders after an unmap, so wait until the input is really usable
        driverTools.scrollElementToCenter(driverTools.waitForElementToAppear(searchInput, 15).orElseThrow());
        driverTools.waitForElementToBeClickable(searchInput, 15);
        driverTools.clearWithJsExecutor(searchInput);
        driverTools.sendKeys(searchInput, roomName);
        String searchBtnXpath = String.format(
            "//span[contains(text(), '%s')]/ancestor::div[contains(@class, 'Card_card_container')]//button[normalize-space()='Search']", 
            tableLabel
        );
        Locator scopedSearchButton = new Locator("SCOPED_SEARCH_BUTTON_" + tableLabel.toUpperCase(), By.xpath(searchBtnXpath));
        driverTools.waitForElementToBeClickable(scopedSearchButton, 10);
        driverTools.clickWithJavascriptExecutor(scopedSearchButton);
    }

   public void searchByVendorRoomId(String tableLabel, String roomId) {
        Locator searchInput = getSearchInputId(tableLabel);
        // the card re-renders after an unmap, so wait until the input is really usable
        driverTools.scrollElementToCenter(driverTools.waitForElementToAppear(searchInput, 15).orElseThrow());
        driverTools.waitForElementToBeClickable(searchInput, 15);
        driverTools.clearWithJsExecutor(searchInput);
        driverTools.sendKeys(searchInput, roomId);
        String searchBtnXpath = String.format(
            "//span[contains(text(), '%s')]/ancestor::div[contains(@class, 'Card_card_container')]//button[normalize-space()='Search']", 
            tableLabel
        );
        Locator scopedSearchButton = new Locator("SCOPED_SEARCH_BUTTON_" + tableLabel.toUpperCase(), By.xpath(searchBtnXpath));
        driverTools.waitForElementToBeClickable(scopedSearchButton, 10);
        driverTools.clickWithJavascriptExecutor(scopedSearchButton);
    }

    public boolean isResultAppeared(String tableLabel, String roomId) {
        String xpath = "//span[contains(text(), '" + tableLabel + "')]" +
                    "/ancestor::div[contains(@class, 'Card_card_container')]" +
                    "//td[contains(., '" + roomId + "')]";
        Locator resultLocator = new Locator("RESULT_" + tableLabel.toUpperCase() + "_" + roomId.toUpperCase(), By.xpath(xpath));
        return driverTools.waitForElementToAppear(resultLocator, 5).isPresent();
    }

    public void clearSearchVendorRoomId(String tableLabel) {
        Locator searchInput = getSearchInputId(tableLabel);
        driverTools.clearWithJsExecutor(searchInput);
        driverTools.sendKeys(searchInput, " "); 
        driverTools.clearWithJsExecutor(searchInput);
        String searchBtnXpath = String.format(
            "//span[contains(text(), '%s')]/ancestor::div[contains(@class, 'Card_card_container')]//button[normalize-space()='Search']", 
            tableLabel
        );
        Locator scopedSearchButton = new Locator("SCOPED_SEARCH_BUTTON_" + tableLabel.toUpperCase(), By.xpath(searchBtnXpath));
        
        driverTools.waitForElementToBeClickable(scopedSearchButton, 10);
        driverTools.clickWithJavascriptExecutor(scopedSearchButton);
        
        driverTools.dummyWait(3); 
    }

    public void clickSegmentedButton(String label) {
        Locator button = new Locator("SEGMENTED_BUTTON_" + label.toUpperCase().replace(" ", "_"), By.xpath("//button[.//span[text()='" + label + "']]"));
        driverTools.waitForElementToBeClickable(button, 5);
        driverTools.clickWithJavascriptExecutor(button);
        driverTools.dummyWait(2);
    }

    /** Clicks the Name/ID toggle of one specific table card ("Mapped List" or "Unmapped Rooms"). */
    public void clickSegmentedButton(String tableLabel, String label) {
        String xpath = "//span[contains(text(),'" + tableLabel + "')]/ancestor::div[contains(@class,'title-toolbar')]"
                + "//button[.//span[normalize-space()='" + label + "']]";
        Locator button = new Locator("SEGMENTED_BUTTON_" + tableLabel.toUpperCase().replace(" ", "_") + "_" + label.toUpperCase(), By.xpath(xpath));
        driverTools.waitForElementToBeClickable(button, 5);
        driverTools.scrollElementToCenter(button);
        driverTools.clickWithJavascriptExecutor(button);
        // the toggle is applied once the card's search box switches to "...vendor room <label>"
        By searchInput = getSearchInputId(tableLabel).by();
        driverTools.waitUntil(driver -> {
            List<WebElement> inputs = driver.findElements(searchInput);
            if (inputs.isEmpty()) return false;
            String placeholder = inputs.get(0).getAttribute("placeholder");
            return placeholder != null && placeholder.trim().toLowerCase().endsWith(label.toLowerCase());
        }, 5);
    }

    public void clickUnmapButton(String tableLabel, String label) {
        int index = tableLabel.equals("Mapped List") ? 2 : 1;
        Locator button = new Locator("SEGMENTED_BUTTON_" + label.toUpperCase().replace(" ", "_"), By.xpath("(//button[.//span[text()='" + label + "']])["+index+"]"));
        driverTools.waitForElementToBeClickable(button, 5);
        driverTools.clickWithJavascriptExecutor(button);
        driverTools.dummyWait(2);
    }
  
    private static Locator getUnmapButton(String vendorRoomID) {
        String xpath = "//td[contains(., '" + vendorRoomID + "')]" +
                    "/ancestor::tr" +
                    "//span[contains(text(), 'Unmap')]";
        return new Locator("UNMAP_BUTTON_" + vendorRoomID.toUpperCase().replace(" ", "_"), By.xpath(xpath));
    }

    public void clickUnmapByRoomId(String vendorRoomID) {
        Locator unmapButton = getUnmapButton(vendorRoomID);
        driverTools.waitForElementToBeClickable(unmapButton, 5);
        driverTools.clickWithJavascriptExecutor(unmapButton);
    }

    public void clickContinueOnPopup() {
        driverTools.waitForElementToAppear(CONTINUE_POPUP_BUTTON, 5);
        driverTools.clickWithJavascriptExecutor(CONTINUE_POPUP_BUTTON);
        driverTools.waitForElementToDisappear(CONTINUE_POPUP_BUTTON, 5); 
    }

    private static Locator getDropdown(String labelName) {
        String xpath = String.format(
            "//div[contains(@class, 'flex') and .//button[normalize-space()='Search']]//" +
            "div[contains(@class, 'Dropdown') and (.//label[normalize-space()='%s'] or .//div[normalize-space()='%s'])]",
            labelName, labelName
        );
        return new Locator("DROPDOWN_" + labelName.toUpperCase().replace(" ", "_"), By.xpath(xpath));
    }

    private static Locator getSearchInputForDropdown(String labelName) {
        String xpath = "//label[normalize-space()='"+ labelName + "']//following::input[1]";
        return new Locator("SEARCH_INPUT_" + labelName.toUpperCase(), By.xpath(xpath));
    }

    public void selectDropdownOption(String dropdownLabel, String dataName, String optionToSearch) {
        Locator dropdown = getDropdown(dropdownLabel);
        WebElement dropdownElement = driverTools.waitForElementToAppear(dropdown, 5).orElseThrow();
        StringBuilder currentValue = new StringBuilder(dropdownElement.getText());
        for (WebElement input : dropdownElement.findElements(By.tagName("input"))) {
            String value = input.getAttribute("value");
            if (value != null) currentValue.append(' ').append(value);
        }
        if (currentValue.toString().toLowerCase().contains(optionToSearch.toLowerCase())) {
            logger.log(dropdownLabel + " already selected, skipping dropdown: " + optionToSearch);
            return;
        }
        driverTools.scrollElementToCenter(dropdownElement);
        try {
            driverTools.click(dropdown);
        } catch (Exception e) {
            // fixed header may still overlap; the open handler lives on the inner clickable div
            driverTools.clickWithJavascriptExecutor(dropdownElement.findElement(By.cssSelector("div.cursor-pointer")));
        }
        Locator searchLocator = getSearchInputForDropdown(dropdownLabel);
        driverTools.waitForElementToAppear(searchLocator, 5);
        driverTools.click(searchLocator);
        driverTools.clearInput(searchLocator);
        driverTools.sendKeys(searchLocator, optionToSearch);
        driverTools.dummyWait(2);
        Locator firstResult = new Locator("FIRST_RESULT_DROPDOWN", By.xpath("//li[@data-name='" + dataName + "'][1]"));
        driverTools.waitForElementToAppear(firstResult, 5);
        driverTools.clickWithJavascriptExecutor(firstResult);
    }

    public Locator getCheckboxByRoomId(String roomID) {
        String xpath = "//tr[contains(., '" + roomID + "')]//input[@type='checkbox']";
        return new Locator("CHECKBOX_" + roomID, By.xpath(xpath));
    }
    
    public void selectRooms(List<String> roomIDs) {
        for (String room : roomIDs) {
            Locator checkbox = getCheckboxByRoomId(room);
            driverTools.waitForElementToAppear(checkbox, 10);
            driverTools.clickWithJavascriptExecutor(checkbox);
            driverTools.dummyWait(2);
        }
    }

    public void selectRoomById(String roomId) {
        Locator checkbox = getCheckboxByRoomId(roomId);
        driverTools.waitForElementToAppear(checkbox, 5);
        driverTools.clickWithJavascriptExecutor(checkbox);
    }

    public void clickMapButtonByRoomGroupId(String roomGroupId) {
        String xpath = "//div[contains(., '" + roomGroupId + "')]/ancestor::div[contains(@class, 'justify-between')]//button";
        Locator mapButton = new Locator("MAP_BUTTON_" + roomGroupId, By.xpath(xpath));
        driverTools.waitForElementToPresence(mapButton, 15);
        // Center it (not just scroll into view) so the fixed top header can't cover the button
        driverTools.scrollElementToCenter(mapButton);
        driverTools.waitForElementToBeClickable(mapButton, 5);
        driverTools.clickWithJavascriptExecutor(mapButton);
    }

    public boolean verifySnackbarMessage(String expectedMessage) {
        driverTools.waitForElementToAppear(SNACKBAR_MESSAGE);
        String actualMessage = driverTools.getText(SNACKBAR_MESSAGE).toString();
        Assertion.assertThat("Snackbar message should contain expected message", actualMessage, containsString(expectedMessage));
        driverTools.waitForElementToDisappear(SNACKBAR_MESSAGE);
        return actualMessage.contains(expectedMessage);
    }

    public void inputVendorPropertyIdInLog(String vendorPropertyId) {
        // keep the input away from the fixed header, which otherwise intercepts the click in clearInput
        driverTools.scrollElementToCenter(SEARCH_INPUT_VENDOR_PROPERTY_ID_LOG);
        driverTools.clearInput(SEARCH_INPUT_VENDOR_PROPERTY_ID_LOG);
        driverTools.sendKeys(SEARCH_INPUT_VENDOR_PROPERTY_ID_LOG, vendorPropertyId);
    }

    public void inputVendorRoomIdInLog(String vendorRoomId) {
        // keep the input away from the fixed header, which otherwise intercepts the click in clearInput
        driverTools.scrollElementToCenter(SEARCH_INPUT_VENDOR_ROOM_ID_LOG);
        driverTools.clearInput(SEARCH_INPUT_VENDOR_ROOM_ID_LOG);
        driverTools.sendKeys(SEARCH_INPUT_VENDOR_ROOM_ID_LOG, vendorRoomId);
    }

    public void clickDateRangeInLog() {
        driverTools.waitForElementToPresence(DATE_RANGE_INPUT, 5);
        driverTools.clickWithJavascriptExecutor(DATE_RANGE_INPUT);
    }

    public void clickDateInCalendarLog(String dateString) {
        String xpath = "//span[contains(@aria-label, '" + dateString + "')]/ancestor::button";
        Locator dateLocator = new Locator("DATE_" + dateString, By.xpath(xpath));
        driverTools.waitForElementToAppear(dateLocator, 5);
        driverTools.click(dateLocator);
    }

    private String getCalendarDateFormat(int dayOffset) {
        LocalDate targetDate = LocalDate.now().plusDays(dayOffset);
        return targetDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy EEEE", Locale.ENGLISH));
    }

    private String getTableDateFormat(int dayOffset) {
        // The log table shows a zero-padded day, e.g. "Thu, 08 Oct 2026"
        LocalDate targetDate = LocalDate.now().plusDays(dayOffset);
        return targetDate.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy", Locale.ENGLISH));
    }

    public void selectDateRangeInLog(int startOffset, int endOffset) {
        clickDateRangeInLog();
        String startDateStr = getCalendarDateFormat(startOffset);
        String endDateStr = getCalendarDateFormat(endOffset);
        clickDateInCalendarLog(startDateStr);
        driverTools.dummyWait(1);
        clickDateInCalendarLog(endDateStr);
    }

    public boolean isDatePresentInTable(int dayOffset) {
        String expectedDateTxt = getTableDateFormat(dayOffset);
        return isTextPresentInTable(expectedDateTxt);
    }

    public boolean isPropertyVisible(String propertyId) {
        try {
            Locator propertyRow = new Locator("PROPERTY_ROW", By.xpath("//td[contains(text(), '" + propertyId + "')]"));
            return driverTools.isElementDisplayed(propertyRow);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifySuccessMessage(String expectedMessage) {
        return verifySnackbarMessage(expectedMessage);
    }

    public int getTableRowCount() {
        return driverTools.getDriver().findElements(By.xpath("//tbody/tr")).size();
    }

    public boolean isTextPresentInTable(String text) {
        String xpath = "//tbody//td[contains(normalize-space(), '" + text + "')]";
        Locator tableRow = new Locator("TABLE_ROW_" + text.toUpperCase().replace(" ", "_"), By.xpath(xpath));
        return driverTools.waitForElementToAppear(tableRow, 5).isPresent();
    }

    public boolean isTextPresentInTableHeader(String text) {
        String xpath = "//th[contains(normalize-space(), '" + text + "')]";
        Locator tableHeader = new Locator("TABLE_HEADER_" + text.toUpperCase().replace(" ", "_"), By.xpath(xpath));
        return driverTools.waitForElementToAppear(tableHeader, 5).isPresent();
    }

    // for map and unmap table
    public boolean isTextPresentInTableMap(String tableLabel, String text) {
        String xpath = "//span[contains(text(), '" + tableLabel + "')]" +
                    "/ancestor::div[contains(@class, 'Card_card_container')]" +
                    "//tr[contains(., '" + text + "')]";
        Locator tableRow = new Locator("TABLE_ROW_" + tableLabel.toUpperCase().replace(" ", "_"), By.xpath(xpath));
        return driverTools.waitForElementToAppear(tableRow, 5).isPresent();
    }

}

