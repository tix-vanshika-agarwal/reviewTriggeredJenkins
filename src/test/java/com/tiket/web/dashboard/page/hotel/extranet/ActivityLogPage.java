package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class ActivityLogPage<T extends WebDriver> extends BasePage<T> {
    public ActivityLogPage(Session<T> session) {
        super(session);
    }
    private static final Locator ACTIVITY_LOG_TITLE = new Locator("ACTIVITY_LOG_TITLE", new By.ByXPath("//div[contains(@class, 'page-header')]//label[text()='Activity Log' or text()='Log Aktivitas']"));
    private static final Locator START_DATE_FILTER = new Locator("START_DATE_FILTER", By.xpath("//div[contains(@class, 'label') and (text()='Start date' or text()='Tanggal mulai')]"));
    private static final Locator END_DATE_FILTER = new Locator("END_DATE_FILTER", By.xpath("//div[contains(@class, 'label') and (text()='End date' or text()='Tanggal selesai')]"));
    private static final Locator ACTIVITY_TYPE_FILTER = new Locator("ACTIVITY_TYPE_FILTER", By.xpath("//div[@class='dropdown-label' and (text()='Activity Type' or text()='Tipe Aktivitas')]"));
    private static final Locator FORM_TYPE_FILTER = new Locator("FORM_TYPE_FILTER", By.xpath("//div[contains(@class, 'label') and (text()='Form Type' or text()='Tipe Form')]"));
    private static final Locator SEARCH_USER_FILTER = new Locator("SEARCH_USER_FILTER", By.xpath("//div[contains(@class, 'label') and (text()='Search User' or text()='Cari pengguna')]"));
    private static final Locator GO_BUTTON = new Locator("GO_BUTTON", By.xpath("//button[contains(@class, 'btn-go') and text()='GO']"));
    private static final Locator EXPORT_TO_EXCEL_BUTTON = new Locator("EXPORT_TO_EXCEL_BUTTON", By.xpath("//div[@class='export-button']//button[contains(., 'Export to excel') or contains(., 'Ekspor ke excel')]"));
    private static final Locator ACTIVITY_LOG_TABLE = new Locator("ACTIVITY_LOG_TABLE", By.xpath("//table[contains(@class, 'table-component2')]"));
    private static final Locator START_DATE_INPUT = new Locator("START_DATE_INPUT", By.xpath("//div[contains(@class, 'label') and (text()='Start date' or text()='Tanggal mulai')]/following-sibling::div/input"));
    private static final Locator END_DATE_INPUT = new Locator("END_DATE_INPUT", By.xpath("//div[contains(@class, 'label') and (text()='End date' or text()='Tanggal selesai')]/following-sibling::div/input"));
    private static final Locator ACTIVITY_TYPE_INPUT = new Locator("ACTIVITY_TYPE_INPUT", By.xpath("//input[@placeholder='Activity Type' or @placeholder='Tipe Aktivitas']"));
    private static final Locator ACTIVITY_TYPE_MENU = new Locator("ACTIVITY_TYPE_MENU", By.xpath("//div[contains(@class, 'the-dropdown') and .//input[@placeholder='Activity Type' or @placeholder='Tipe Aktivitas']]//div[contains(@class, 'popoverlist-menu')]"));
    private static final Locator FORM_TYPE_INPUT = new Locator("FORM_TYPE_INPUT", By.xpath("//input[@placeholder='Form Type' or @placeholder='Tipe Form']"));
    private static final Locator FORM_TYPE_MENU = new Locator("FORM_TYPE_MENU", By.xpath("//div[contains(@class, 'the-autocomplete') and .//input[@placeholder='Form Type' or @placeholder='Tipe Form']]//div[contains(@class, 'popoverlist-menu')]"));
    private static final Locator SEARCH_USER_INPUT = new Locator("SEARCH_USER_INPUT", By.xpath("//input[@placeholder='Search User' or @placeholder='Cari pengguna']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[contains(@class, 'clear-button') and (text()='Reset filter' or text()='Atur ulang filter')]"));
    public void verifyActivityLogPageLoaded() {
        boolean isTitleVisible = driverTools.waitForElementToAppear(ACTIVITY_LOG_TITLE).isPresent();
        Assertion.assertThat("Verify Activity Log header title is visible", isTitleVisible, is(true));
    }

    public void verifyFilterElementsAndButtonsDisplayed() {
        boolean isStartDateVisible = driverTools.waitForElementToAppear(START_DATE_FILTER, 5).isPresent();
        Assertion.assertThat("Verify Start Date filter is displayed", isStartDateVisible, is(true));

        boolean isEndDateVisible = driverTools.waitForElementToAppear(END_DATE_FILTER, 5).isPresent();
        Assertion.assertThat("Verify End Date filter is displayed", isEndDateVisible, is(true));

        boolean isActivityTypeVisible = driverTools.waitForElementToAppear(ACTIVITY_TYPE_FILTER, 5).isPresent();
        Assertion.assertThat("Verify Activity Type filter is displayed", isActivityTypeVisible, is(true));

        boolean isFormTypeVisible = driverTools.waitForElementToAppear(FORM_TYPE_FILTER, 5).isPresent();
        Assertion.assertThat("Verify Form Type filter is displayed", isFormTypeVisible, is(true));

        boolean isSearchUserVisible = driverTools.waitForElementToAppear(SEARCH_USER_FILTER, 5).isPresent();
        Assertion.assertThat("Verify Search User filter is displayed", isSearchUserVisible, is(true));

        boolean isGoButtonVisible = driverTools.waitForElementToAppear(GO_BUTTON, 5).isPresent();
        Assertion.assertThat("Verify GO button is displayed", isGoButtonVisible, is(true));

        boolean isExportExcelVisible = driverTools.waitForElementToAppear(EXPORT_TO_EXCEL_BUTTON, 5).isPresent();
        Assertion.assertThat("Verify Export to Excel hyperlink is displayed", isExportExcelVisible, is(true));
    }

    public void verifyActivityResultTableAndColumns() {
        boolean isTableVisible = driverTools.waitForElementToAppear(ACTIVITY_LOG_TABLE, 5).isPresent();
        Assertion.assertThat("Verify the activity result table is displayed", isTableVisible, is(true));
        int rowCount = driverTools.getDriver().findElements(By.xpath("//table[contains(@class, 'table-component2')]//tbody/tr")).size();
        Assertion.assertThat("Verify that by default some activities are retrieved in the table. Rows retrived: "+rowCount, rowCount, is(greaterThan(0)));
        if (rowCount > 0) {
            List<String> expectedColumnXPaths = Arrays.asList(
                    "//thead//td[contains(., 'Time') or contains(., 'Waktu')]",
                    "//thead//td[contains(., 'Request ID')]",
                    "//thead//td[contains(., 'User') or contains(., 'Pengguna')]",
                    "//thead//td[contains(., 'Email')]",
                    "//thead//td[contains(., 'Activity Type') or contains(., 'Tipe Aktivitas')]",
                    "//thead//td[contains(., 'Form Type') or contains(., 'Tipe Form')]",
                    "//thead//td[contains(., 'Action') or contains(., 'Tindakan')]",
                    "//thead//td[contains(., 'Before') or contains(., 'Sebelum')]",
                    "//thead//td[contains(., 'After') or contains(., 'Sesudah')]"
            );
            for (String colXpath : expectedColumnXPaths) {
                Locator columnLoc = new Locator("TABLE_COLUMN", By.xpath(colXpath));
                boolean isColumnVisible = driverTools.isElementPresent(columnLoc);
                String colName = colXpath.split("'")[1];
                Assertion.assertThat("Verify column header '" + colName + "' is present", isColumnVisible, is(true));
            }
       }
    }

    public void clickExportToExcelButton() throws IOException {
        WebElement exportBtn = driverTools.waitForElementToAppear(EXPORT_TO_EXCEL_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Export to Excel button not found"));
        exportBtn.click();
        if(!CoreFunctions.isJenkinsSystem()){
            driverTools.dummyWait(2); // wait for the dialog box to appear
        String[] cmd = {"osascript", "-e", "tell application \"System Events\" to keystroke return"};
        Runtime.getRuntime().exec(cmd);
        }
    }

    public void verifyExcelFileDownloaded() throws InterruptedException {
        String downloadPath = System.getProperty("user.home") + File.separator + "Downloads";
        File dir = new File(downloadPath);

        File downloadedFile = null;
        int maxWaitSeconds = 15;
        boolean isDownloaded = false;
        for (int elapsedSeconds = 0; elapsedSeconds < maxWaitSeconds; elapsedSeconds++) {
            File[] files = dir.listFiles((d, name) -> name.startsWith("ActivityLog") && name.endsWith(".xlsx"));

            if (files != null && files.length > 0) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                downloadedFile = files[0];
                if (downloadedFile.length() > 0) {
                    isDownloaded = true;
                    break;
                }
            }
            driverTools.dummyWait(1); // wait for the file to be downloaded.
        }
        Assertion.assertThat("Verify the Excel file is downloaded successfully within " + maxWaitSeconds + " seconds", isDownloaded, is(true));
        if (isDownloaded) {
            String fileName = downloadedFile.getName();
            Assertion.assertThat("Verify the file name starts with ActivityLog", fileName, startsWith("ActivityLog"));
            Assertion.assertThat("Verify the downloaded file format is .xlsx", fileName, endsWith(".xlsx"));
            downloadedFile.delete();
        }
    }

    public void verifyCalendarOnlyAllowsCurrentMonth(Locator calendarInputLoc, String fieldName) {
        WebElement inputField = driverTools.waitForElementToAppear(calendarInputLoc, 5).orElseThrow(() -> new NoSuchElementException(fieldName + " input field not found"));
        inputField.click();
        By currentMonthActiveDatesXPath = By.xpath("//div[contains(@class, 'DayPicker-Day') and @aria-disabled='false' and not(contains(@class, 'DayPicker-Day--outside'))]");
        By currentMonthInActiveDatesXPath = By.xpath("//div[contains(@class, 'DayPicker-Day') and @aria-disabled='true' and not(contains(@class, 'DayPicker-Day--outside'))]");
        int validActiveCount = driverTools.getDriver().findElements(currentMonthActiveDatesXPath).size();
        int invalidActiveCount = driverTools.getDriver().findElements(currentMonthInActiveDatesXPath).size();
        int totalDisplayedDays = validActiveCount + invalidActiveCount;
        int expectedDaysInMonth = YearMonth.now().lengthOfMonth();
        Assertion.assertThat("Verify there are selectable dates in the current month for " + fieldName+" is: "+validActiveCount+" And non-selectable dates are: "+invalidActiveCount, validActiveCount, is(greaterThan(0)));
        Assertion.assertThat("Verify total displayed current month dates (active + inactive) equals exactly " + expectedDaysInMonth + " days", totalDisplayedDays, is(expectedDaysInMonth));
    }

    public void verifyStartDateCalendar() {
        verifyCalendarOnlyAllowsCurrentMonth(START_DATE_INPUT, "Start Date");
    }

    public void verifyEndDateCalendar() {
        verifyCalendarOnlyAllowsCurrentMonth(END_DATE_INPUT, "End Date");
    }

    private boolean isElementScrollable(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driverTools.getDriver();
        Boolean isScrollable = (Boolean) js.executeScript("return arguments[0].scrollHeight > arguments[0].clientHeight;", element);
        return isScrollable != null && isScrollable;
    }

    public void verifyActivityTypeDropdown() {
        WebElement inputElement = driverTools.waitForElementToAppear(ACTIVITY_TYPE_INPUT, 5).orElseThrow();
        inputElement.click();
        WebElement menuElement = driverTools.waitForElementToAppear(ACTIVITY_TYPE_MENU, 5).orElseThrow();
        Assertion.assertThat("Verify Activity Type dropdown menu is displayed", menuElement.isDisplayed(), is(true));
        boolean isScrollable = isElementScrollable(menuElement);
        Assertion.assertThat("Verify Activity Type dropdown is scrollable", isScrollable, is(true));
        String activityOptionsXpath = "//div[contains(@class, 'the-dropdown') and .//input[@placeholder='Activity Type' or @placeholder='Tipe Aktivitas']]//span[contains(@class, 'popoverlist-list')]/label";
        List<WebElement> options = driverTools.getDriver().findElements(By.xpath(activityOptionsXpath));
        Assertion.assertThat("Verify there are at least 5 Activity Type options", options.size(), is(greaterThanOrEqualTo(5)));
        List<String> expectedEnglishOptions = Arrays.asList("add", "clone", "edit", "delete", "create");
        List<String> expectedIndonesianOptions = Arrays.asList("tambah", "duplikat", "ubah", "hapus", "buat");

        for (int i = 0; i < 5; i++) {
            String actualText = options.get(i).getText().trim().toLowerCase();
            boolean matchesExpected = expectedEnglishOptions.contains(actualText) || expectedIndonesianOptions.contains(actualText);
            Assertion.assertThat("Verify option " + (i+1) + " matches expected List English =" + expectedEnglishOptions + "expected List Bahasa =" + expectedIndonesianOptions +".(actual: " + actualText + ")", matchesExpected, is(true));
        }
        inputElement.click();
    }

    public void verifyFormTypeDropdown() {
        WebElement inputElement = driverTools.waitForElementToAppear(FORM_TYPE_INPUT, 5).orElseThrow();
        inputElement.click();
        WebElement menuElement = driverTools.waitForElementToAppear(FORM_TYPE_MENU, 5).orElseThrow();
        Assertion.assertThat("Verify Form Type dropdown menu is displayed", menuElement.isDisplayed(), is(true));
        boolean isScrollable = isElementScrollable(menuElement);
        Assertion.assertThat("Verify Form Type dropdown is scrollable", isScrollable, is(true));
        String formOptionsXpath = "//div[contains(@class, 'the-autocomplete') and .//input[@placeholder='Form Type' or @placeholder='Tipe Form']]//span[contains(@class, 'popoverlist-list')]/label";
        List<WebElement> options = driverTools.getDriver().findElements(By.xpath(formOptionsXpath));
        Assertion.assertThat("Verify at least the first five Form Type options are displayed. Actual List Size: "+options.size(), options.size(), is(greaterThanOrEqualTo(5)));
        inputElement.click();
    }

    public String getFirstUserIdFromTable() {
        By firstRowUserXpath = By.xpath("//table[contains(@class, 'table-component2')]//tbody/tr[1]/td[3]");
        WebElement firstRowUserCell = driverTools.getDriver().findElement(firstRowUserXpath);
        String userId = firstRowUserCell.getText().trim();
        Assertion.assertThat("Verify first user ID is not empty. UserId: "+userId, userId.isEmpty(), is(false));
        return userId;
    }

    public void searchAndVerifyUser(String targetUserId) {
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_USER_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Search User input field not found"));
        searchInput.clear();
        searchInput.sendKeys(targetUserId);
        WebElement goBtn = driverTools.waitForElementToAppear(GO_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("GO button not found"));
        goBtn.click();
        driverTools.dummyWait(2); // wait for 2 sec so that search records are updated.
        By allUserCellsXpath = By.xpath("//table[contains(@class, 'table-component2')]//tbody/tr/td[3]");
        List<WebElement> userCells = driverTools.getDriver().findElements(allUserCellsXpath);
        Assertion.assertThat("Verify that results are displayed after clicking GO", userCells.size(), is(greaterThan(0)));
        for (int i = 0; i < userCells.size(); i++) {
            String actualUserId = userCells.get(i).getText().trim();
            Assertion.assertThat("Verify row " + (i + 1) + " matches the searched User ID: "+targetUserId+" Actual Id: "+actualUserId, actualUserId, is(targetUserId));
        }
    }

    public void clickResetFilterAndVerifySearchUserCleared() {
        WebElement resetBtn = driverTools.waitForElementToAppear(RESET_FILTER_BUTTON, 5).orElseThrow(() -> new NoSuchElementException("Reset filter button not found"));
        resetBtn.click();
        WebElement searchInput = driverTools.waitForElementToAppear(SEARCH_USER_INPUT, 5).orElseThrow(() -> new NoSuchElementException("Search User input field not found"));
        String currentInputValue = searchInput.getAttribute("value");
        if (currentInputValue == null) {
            currentInputValue = "";
        }
        Assertion.assertThat("Verify the Search User field is completely empty after clicking Reset filter.", currentInputValue, equalTo(""));
    }

}
