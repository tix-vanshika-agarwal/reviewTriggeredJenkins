package com.tiket.web.dashboard.page.b2b_external;

import com.google.gson.Gson;
import com.tiket.api.b2b.GetEmployeeIdApi;
import com.tiket.api.b2b.GetEmployeeIdApi.Inputs;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.b2b.corporate.EmployeeData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;

public class EmployeeListPage<T extends WebDriver> extends BasePage<T> {
    private static final String GK_CORP_DASHBOARD = "https://corporate-gatotkaca.tiket.com";
    private static final String PREPROD_CORP_DASHBOARD = "https://corporate.preprod.tiket.com";
    private static final String PROD_CORP_DASHBOARD = "https://corporate.tiket.com";
    private static final Locator EMPLOYEE_LIST_SIDEBAR_ITEM = new Locator("EMPLOYEE_LIST_SIDEBAR_ITEM", new By.ByXPath("//*[text()='Employees']"));
    private static final Locator EMPLOYEE_LIST_HEADER = new Locator("EMPLOYEE_LIST_HEADER", new By.ByXPath("//h1[normalize-space()='Employees']"));
    private static final Locator CLOSE_DIALOG = new Locator("CLOSE_DIALOG", new By.ByXPath("//i[@class='tix tix-cancel']"));
    private static final Locator ADD_EMPLOYEE_BUTTON = new Locator("ADD_EMPLOYEE_BUTTON", new By.ByXPath("//button[@id='coachmark-employee-list-bulk-button']"));
    private static final Locator ADD_EMPLOYEE_POPUP = new Locator("ADD_EMPLOYEE_POPUP", new By.ByXPath("//section[contains(@class, 'modal_popup')]"));
    private static final Locator EMPLOYEE_NAME_INPUT = new Locator("EMPLOYEE_NAME_INPUT", new By.ByXPath("//input[@id='employee-name']"));
    private static final Locator EMPLOYEE_EMAIL_INPUT = new Locator("EMPLOYEE_NAME_INPUT", new By.ByXPath("//input[@id='email']"));
    private static final Locator ROLE_DROPDOWN = new Locator("ROLE_DROPDOWN", new By.ByXPath("//div[contains(@class,'styles_input_inner_wrapper')]/input[@id='user-role']"));
    private static final Locator DIVISION_DROPDOWN = new Locator("DIVISION_DROPDOWN", new By.ByXPath("//section[contains(@class, 'modal_popup')]//*[contains(text(), 'Division')]"));
    private static final Locator APPROVER_DROPDOWN = new Locator("APPROVER_DROPDOWN", new By.ByXPath("(//section[contains(@class, 'modal_popup')]//*[@class='transition duration-200'])[3]"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator SEARCH_NAME = new Locator("SEARCH_NAME", new By.ByXPath("//input[contains(@placeholder, 'name')]"));
    private static final Locator REGISTRATION_STATUS_FILTER = new Locator("REGISTRATION_STATUS_FILTER", new By.ByXPath("//input[contains(@placeholder, 'status')]"));
    private static final Locator SEARCH_NAME_AUTOCOMPLETE = new Locator("SEARCH_NAME_AUTOCOMPLETE", new By.ByXPath("(//div[contains(@class, 'list_box')])[1]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//button[text()='search']"));
    private static final Locator DOWNLOAD_EMPLOYEE_BUTTON = new Locator("DOWNLOAD_EMPLOYEE_BUTTON", new By.ByXPath("//button[normalize-space()='Download List']"));
    private static final Locator PAGINATION_DETAIL = new Locator("PAGINATION_DETAIL", new By.ByXPath("//span[contains(text(), 'Showing')]"));
    private static final Locator CANCEL_POPUP = new Locator("CANCEL_POPUP", new By.ByXPath("//button[normalize-space()='Cancel']"));
    private static final Locator SKIP_GROUP_POPUP = new Locator("SKIP_GROUP_POPUP", new By.ByXPath("//button[normalize-space()='Skip for now']"));
    private static final Locator DOWNLOAD_LIST_BUTTON = new Locator("DOWNLOAD_LIST_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Download List')]"));
    private static final Locator ACTION_BUTTON = new Locator("ACTION_BUTTON", new By.ByXPath("//tr[3]//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator DELETE_OPTION = new Locator("DELETE_OPTION", new By.ByXPath("//tr[3]//*[contains(text(), 'Delete')]"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//button[contains(@class, 'danger')]"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator ACTION_OPTION = new Locator("ACTION_OPTION", new By.ByXPath("//tr[1]//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator EDIT_OPTION = new Locator("EDIT_OPTION", new By.ByXPath("//li[.//span[text()='Edit']]"));
    private static final Locator RESEND_REGISTRATION_LINK_OPTION = new Locator("RESEND_REGISTRATION_LINK_OPTION", new By.ByXPath("//span[text()='Resend Registration Link']"));
    private static final Locator TRAVEL_POLICY = new Locator("TRAVEL_POLICY", new By.ByXPath(".//input[@id='travel-policy']"));
    private static final Locator NO_POLICY = new Locator("NO_POLICY", new By.ByXPath(".//p[text()='No Policy']"));
    private static final Locator EMPLOYEE_LIST_BUTTON = new Locator("EMPLOYEE_LIST_BUTTON", By.xpath("//button[.//span[normalize-space(text())='Employee List' or normalize-space(text())='Daftar Pegawai']]"));
    private static final Locator BULK_ADD_EMPLOYEES_BUTTON = new Locator("BULK_ADD_EMPLOYEES_BUTTON", By.id("coachmark-employee-list-add-button"));
    private static final Locator OK_GOT_IT_BUTTON = new Locator("OK_GOT_IT_BUTTON", By.xpath("//button[normalize-space(text())='Ok, Got It']"));
    private static final Locator BROWSE_FILE_BTN = new Locator("BROWSE_FILE_BTN", By.xpath("//input[@type='file']"));
    private static final Locator SAVE = new Locator("SAVE", new By.ByXPath("//button[normalize-space()='Save']"));
    private static final Locator NAME_ERROR = new Locator("NAME_ERROR", By.xpath("//span[text()='Minimum length of this field is 3 characters.']"));
    private static final Locator RESEND_SNACKBAR = new Locator("RESEND_SNACKBAR", new By.ByXPath("(//div[contains(@class, 'Snackbar_text_content') and normalize-space(text())='Registration link has been resent successfully.'])[2]"));
    private static final Locator MEMBER_STATUS_CHECKBOX = new Locator("MEMBER_STATUS_CHECKBOX", By.xpath("//div[contains(@class,'Toggle_toggle__') and contains(@class,'Toggle_toggle_small')]"));
    private static final Locator MEMBER_ACTION_BUTTON = new Locator("MEMBER_ACTION_BUTTON", By.xpath("//tr//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator MEMBER_EDIT_OPTION = new Locator("MEMBER_EDIT_OPTION", By.xpath("(//ul//li[.//span[normalize-space(text())='Edit']])[2]"));
    private static final Locator USER_ROLE_DROPDOWN = new Locator("USER_ROLE_DROPDOWN", By.xpath("//input[@id='user-role' and @type='text' and @placeholder='User Role']"));
    private static final Locator ADMIN_ROLE_OPTION = new Locator("ADMIN_ROLE_OPTION", By.xpath("//span[contains(text(), 'Set as Super Administrator')]"));
    private static final Locator DEACTIVATE_MEMBER_BUTTON = new Locator("DEACTIVATE_MEMBER_BUTTON", By.xpath("//section//button[contains(@class, 'Button_variant_primary')]"));
    private static final Locator DEACTIVATE_DASHBOARD = new Locator("DEACTIVATE_DASHBOARD", By.xpath("//header//h2"));
    private static final Locator SUPER_ADMIN = new Locator("SUPER_ADMIN", new By.ByXPath(" (//li[.//span[text()='Set as Super Administrator']])[1]"));
    private static final Locator CONTINUE_BUTTON = new Locator("CONTINUE_BUTTON", new By.ByXPath("//button[normalize-space()='Continue']"));
    private static final Locator SUPER_ADMIN_POPUP = new Locator("SUPER_ADMIN_POPUP", new By.ByXPath("//div[contains(@class, 'Snackbar_snackbar') and contains(@class, 'Snackbar_type_general')]//div[contains(text(), 'successfully set Dashboard Traveler as the new Super Administrator')]"));
    private static final Locator ACTION_BUTTON_ADMIN = new Locator("ACTION_BUTTON_ADMIN", By.xpath("//tr[1]//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator EDIT = new Locator("EDIT", By.xpath("//li[.//span[text()='Edit']]"));
    private static final Locator USER_ROLE = new Locator("USER_ROLE", By.xpath("//input[@id='user-role' and @placeholder='User Role']"));
    private static final Locator ADMIN_ROLE = new Locator("ADMIN_ROLE", By.xpath("//div[p[@class='text-base font-bold text-lg' and text()='Administrator']]"));
    private static final Locator ACTUAL_ROLE = new Locator("ACTUAL_ROLE", By.xpath("//h5[text()='Administrator']"));
    private static final Locator EMPLOYEE_ACTION_BTN = new Locator("EMPLOYEE_ACTION_BTN", By.xpath("//tr[1]//*[local-name()='svg' and contains(@class, 'cursor-pointer')]"));
    private static final Locator LINK_RESEND = new Locator("LINK_RESEND", By.xpath("//li[.//span[text()='Resend Registration Link']]"));
    private static final Locator MAIL_INPUT = new Locator("MAIL_INPUT", By.xpath("//input[@id='login' and @class='ycptinput']"));
    private static final Locator MAIL_SUCCESS_SNACKBAR = new Locator("MAIL_SUCCESS_SNAKBAR", By.xpath("(//div[contains(@class, 'Snackbar_text_content') and text()='Registration link has been resent successfully.'])[1]"));
    private static final Locator SEND_BUTTON = new Locator("SEND_BUTTON", By.xpath("//button[@title='Check Inbox @yopmail.com']"));
    private static final Locator REFRESH = new Locator("REFRESH", new By.ByXPath("//button[normalize-space()='Refresh']"));
    private static final Locator FILTER_OPTION = new Locator("FILTER_OPTION", new By.ByXPath("//input[@id='registration-status']"));
    private static final Locator EXPIRED = new Locator("EXPIRED", new By.ByXPath("//p[contains(text(), 'Expired')]/following-sibling::*[1]"));
    private static final Locator CURRENT_ROLE = new Locator("CURRENT_ROLE", new By.ByXPath(" //h5"));
    private static final Locator TEAM_MANAGEMENT = new Locator("TEAM_MANAGEMENT", new By.ByXPath("//*[text()='Team Management']"));

    public EmployeeListPage(Session<T> session) {
        super(session);
    }

    private String getEnvironmentUrl() {
        return switch (Configs.ENVIRONMENT) {
            case PROD -> PROD_CORP_DASHBOARD;
            case PREPROD -> PREPROD_CORP_DASHBOARD;
            case GK -> GK_CORP_DASHBOARD;
            default -> "";
        };
    }

    public void goToEmployeeListPage() {
        driverTools.waitForElementToAppear(TEAM_MANAGEMENT);
        driverTools.click(TEAM_MANAGEMENT);
        driverTools.waitForElementToAppear(EMPLOYEE_LIST_SIDEBAR_ITEM);
        driverTools.click(EMPLOYEE_LIST_SIDEBAR_ITEM);
        driverTools.waitForElementToAppear(EMPLOYEE_LIST_HEADER, 90).orElseThrow();
        closeOnBoardingDialog();
    }

    public void closeOnBoardingDialog() {
        driverTools.waitForElementToAppear(CLOSE_DIALOG, 2);
        if (driverTools.isElementDisplayed(CLOSE_DIALOG)) {
            driverTools.click(CLOSE_DIALOG);
        }
    }

    public int getNumberOfEmployees() {
        String paginationText = driverTools.getText(PAGINATION_DETAIL).orElseThrow();
        logger.log(paginationText);
        logger.log("Total: " + Integer.parseInt(paginationText.split("of ")[1]));
        return Integer.parseInt(paginationText.split("of ")[1]);
    }

    public void clickAddEmployee() {
        driverTools.maximizeWindow();
        driverTools.click(ADD_EMPLOYEE_BUTTON);
        driverTools.waitForElementToAppear(ADD_EMPLOYEE_POPUP);
    }

    public void fillEmployeeDetails(String role, String randomName) throws InterruptedException {
        fillName(randomName);
        fillEmail(randomName);
        chooseRole(role);
    }

    public void fillEmployeeDetailsWithoutName(String role, String randomName) throws InterruptedException {
        fillEmail(randomName);
        chooseRole(role);
    }

    public void fillName(String employeeName) {
        driverTools.sendKeys(EMPLOYEE_NAME_INPUT, employeeName);
    }

    public void fillEmail(String employeeName) {
        String yopmailAddress = employeeName.replace(" ", "_") + "_employee@yopmail.com";
        driverTools.sendKeys(EMPLOYEE_EMAIL_INPUT, yopmailAddress);
    }

    public void chooseRole(String role) {
        driverTools.waitForElementToAppear(ROLE_DROPDOWN,5);
        driverTools.clickWithJavascriptExecutor(ROLE_DROPDOWN);
        Locator ROLE_CHOICE = new Locator("ROLE_CHOICE", new By.ByXPath("//p[normalize-space()='" + role + "']"));
        driverTools.click(ROLE_CHOICE);
        driverTools.waitForElementToDisappear(ROLE_CHOICE);
    }

    public void chooseDivision() throws InterruptedException {
        driverTools.click(DIVISION_DROPDOWN);
        Locator DIVISION_CHOICE = new Locator("DIVISION_CHOICE", new By.ByXPath("//li[text()='No Division']"));
        driverTools.click(DIVISION_CHOICE);
        driverTools.waitForElementToDisappear(DIVISION_CHOICE);
    }

    public void chooseApprover() {
        try {
            logger.log("Default click");
            driverTools.click(APPROVER_DROPDOWN);
        } catch (Exception e) {
            logger.log("JS click");
            driverTools.clickWithJavascriptExecutor(APPROVER_DROPDOWN);
        }
        Locator APPROVER_CHOICE = new Locator("APPROVER_CHOICE", new By.ByXPath("//li[text()='BtoB Testing Admin New Corp']"));
        driverTools.click(APPROVER_CHOICE);
        driverTools.waitForElementToDisappear(APPROVER_CHOICE);
    }

    public void clickSubmit() {
        driverTools.click(SUBMIT_BUTTON);
        driverTools.waitForElementToPresence(SKIP_GROUP_POPUP).ifPresent(driverTools::clickWithJavascriptExecutor);
        driverTools.waitForElementToDisappear(ADD_EMPLOYEE_POPUP);
    }

    public void searchNewEmployee(String name) throws InterruptedException {
        String url = getEnvironmentUrl() + "/employee-list";
        driverTools.get(url);
        driverTools.waitForElementToAppear(SEARCH_NAME, 90);
        driverTools.sendKeys(SEARCH_NAME, name);
    }

    public void clickSearch() throws InterruptedException {
        driverTools.click(SEARCH_BUTTON);
    }

    public boolean checkIfExists(String name) {
        Locator NAME_IN_TABLE = new Locator("NAME_IN_TABLE", new By.ByXPath("//p[text()='" + name + "']"));
        driverTools.waitForElementToAppear(NAME_IN_TABLE);
        return driverTools.isElementDisplayed(NAME_IN_TABLE);
    }

    public void downloadCSV() throws InterruptedException {
        String url = getEnvironmentUrl() + "/employee-list";
        driverTools.get(url);
        driverTools.click(DOWNLOAD_EMPLOYEE_BUTTON);
    }

    private ArrayList<File> getBookingListCSV() {
        String dateStringFileFormat = CoreFunctions.getCurrentDateDD_MM_YYYY();
        Long currentTimeInMillis = System.currentTimeMillis();
        String home = System.getProperty("user.home");
        File downloadsDirectory = new File(home + "/Downloads/");
        ArrayList<File> files = new ArrayList<>();
        for (File fileEntry : downloadsDirectory.listFiles()) {
            boolean isDownloadedInPast60Seconds = fileEntry.lastModified() > (currentTimeInMillis - 60000);
            boolean isCorrectNamingConvention = fileEntry.getName().contains("booking_lists_" + dateStringFileFormat);
            boolean isNotDirectory = !fileEntry.isDirectory();
            if (isDownloadedInPast60Seconds && isCorrectNamingConvention && isNotDirectory) {
                files.add(fileEntry);
            }
        }
        return files;
    }

    public File getDownloadedCSV() throws FileNotFoundException, InterruptedException {
        String dateString = CoreFunctions.getCurrentDateTime();
        ArrayList<File> files = getBookingListCSV();
        int retryCounter = 0;
        while (files.isEmpty() && retryCounter < 5) {
            Thread.sleep(2000);
            files = getBookingListCSV();
            retryCounter++;
            logger.log("No.of Searches: " + retryCounter);
        }

        if (files.isEmpty()) throw new FileNotFoundException();
        files.sort(Comparator.comparingLong(File::lastModified).reversed());
        return files.get(0);
    }

    public boolean checkNewEmployeeCSV(String employeeName, List<List<String>> csvContent) {
        boolean isExist = false;

        for (int i = csvContent.size() - 1; i > 0; i--) {
            List<String> csvRow = csvContent.get(i);
            logger.log("Row" + i + ": " + csvRow.toString());
            if (csvRow.get(1).equals(employeeName)) {
                isExist = true;
                break;
            }
        }
        return isExist;
    }

    public boolean downloadList() throws InterruptedException {
        if (!driverTools.isElementDisplayed(DOWNLOAD_LIST_BUTTON)) {
            return false;
        }
        driverTools.click(DOWNLOAD_LIST_BUTTON);
        Thread.sleep(3000);
        return true;
    }

    @SneakyThrows
    public EmployeeData getEmployeeData(String email) {
        EmployeeData employeeData = getEmployeeId(email);
        logger.log("employeeId ->" + employeeData.data.employeeID);
        return employeeData;
    }

    private EmployeeData getEmployeeId(String email) throws InterruptedException, ExecutionException {
        Inputs inputs = new GetEmployeeIdApi.Inputs(email);
        GetEmployeeIdApi getEmployeeIdApi = new GetEmployeeIdApi(inputs);
        ApiResponse getEmployeeIDApiResponse = getEmployeeIdApi.hitApi(apiTools);
        Gson gson = new Gson();
        EmployeeData employeeData = gson.fromJson(getEmployeeIDApiResponse.getBody(), EmployeeData.class);
        logger.log("employee data->" + employeeData.success);
        return employeeData;
    }

    public void deleteEmployee() {
        driverTools.waitForElementToAppear(ACTION_BUTTON);
        driverTools.scrollIntoWebView(ACTION_BUTTON);
        WebElement ele = driver.findElement(ACTION_BUTTON.by());
        driverTools.clickSvgOrNonHtmlElements(ele);
        driverTools.clickWithJavascriptExecutor(DELETE_OPTION);
        driverTools.click(DELETE_BUTTON);
    }

    public String getSnackbarText() {
        driverTools.waitForElementToAppear(SNACKBAR, 5);
        Assertion.assertThat("Success SnackBar Appeared", driverTools.isElementDisplayed(SNACKBAR), is(true));
        return driverTools.getText(SNACKBAR).orElseThrow();
    }

    public void openTravelerDashboard() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://corporate-gatotkaca.tiket.com/purchase-list");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://corporate.preprod.tiket.com/purchase-list");
        } else {
            driverTools.get("https://corporate.tiket.com/purchase-list");
        }

    }

    public void clickActionButton() {
        driverTools.waitForElementToAppear(ACTION_OPTION, 10);
        driverTools.click(ACTION_OPTION);
    }

    public void clickEditEmployeeOption() {
        driverTools.waitForElementToAppear(EDIT_OPTION, 10);
        driverTools.clickWithJavascriptExecutor(EDIT_OPTION);
    }

    public void clickResendRegistrationLinkOption() {
        driverTools.waitForElementToPresence(RESEND_REGISTRATION_LINK_OPTION, 5);
        driverTools.isElementDisplayed(RESEND_REGISTRATION_LINK_OPTION);
        driverTools.clickWithJavascriptExecutor(RESEND_REGISTRATION_LINK_OPTION);
    }

    public boolean isTravelPolicyVisible() {
        driverTools.waitForElementToAppear(TRAVEL_POLICY, 10);
        return driverTools.isElementDisplayed(TRAVEL_POLICY);
    }

    public void selectTravelPolicy() {
        driverTools.waitForElementToAppear(TRAVEL_POLICY, 10);
        driverTools.clickWithJavascriptExecutor(TRAVEL_POLICY);

        driverTools.waitForElementToAppear(NO_POLICY, 10);
        driverTools.click(NO_POLICY);
    }

    public void clickSaveButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 10);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean isSuccessMessageVisible(String expectedPolicy) {
        driverTools.waitForElementToAppear(TRAVEL_POLICY, 10);
        return driverTools.getAttribute(TRAVEL_POLICY.by(), "value").contains(expectedPolicy);

    }

    public void goToEmployeeList() {
        driverTools.waitForElementToAppear(EMPLOYEE_LIST_BUTTON);
        driverTools.clickWithJavascriptExecutor(EMPLOYEE_LIST_BUTTON);
    }

    public void bulkAddEmployees(String filePath) {
        driverTools.waitForElementToAppear(BULK_ADD_EMPLOYEES_BUTTON);
        driverTools.click(BULK_ADD_EMPLOYEES_BUTTON);
        driverTools.waitForElementToAppear(OK_GOT_IT_BUTTON);
        if (driverTools.isElementDisplayed(OK_GOT_IT_BUTTON)) {
            driverTools.click(OK_GOT_IT_BUTTON);
        }
        driverTools.waitForElementToAppear(BROWSE_FILE_BTN);
        driverTools.sendKeys(BROWSE_FILE_BTN, new File(filePath).getAbsolutePath());
        driverTools.waitForElementToAppear(SAVE);
        driverTools.click(SAVE);
        driverTools.dummyWait(120); // dummy wait for the upload to complete
    }

    public boolean isBulkEmployeeUploadSuccess() {
        driverTools.waitForElementToAppear(OK_GOT_IT_BUTTON);
        return driverTools.isElementDisplayed(OK_GOT_IT_BUTTON);
    }

    public void clickAdminEditOption() {
        driverTools.waitForElementToAppear(MEMBER_ACTION_BUTTON, 10);
        driverTools.click(MEMBER_ACTION_BUTTON);
    }

    public void selectSuperAdmin() {
        driverTools.waitForElementToAppear(SUPER_ADMIN, 10);
        driverTools.clickWithJavascriptExecutor(SUPER_ADMIN);
        driverTools.waitForElementToAppear(CONTINUE_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(CONTINUE_BUTTON);
        driverTools.dummyWait(10);
    }

    public void clickEdit () {
        driverTools.waitForElementToAppear(EDIT, 10);
        driverTools.clickWithJavascriptExecutor(EDIT);
    }

    public void selectUserRole () {
        driverTools.waitForElementToAppear(USER_ROLE, 10);
        driverTools.click(USER_ROLE);
    }

    public void selectAdminRole () {
        driverTools.waitForElementToAppear(ADMIN_ROLE);
        driverTools.click(ADMIN_ROLE);
    }

    public void clickSave () {
        driverTools.waitForElementToAppear(SAVE, 10);
        driverTools.click(SAVE);
    }

    public boolean isUserAdmin (){
        return driverTools.isElementDisplayed(ACTUAL_ROLE);
    }


        public void toggleMember () {
            driverTools.waitForElementToAppear(MEMBER_STATUS_CHECKBOX, 10);
            driverTools.clickWithJavascriptExecutor(MEMBER_STATUS_CHECKBOX);

            driverTools.waitForElementToAppear(DEACTIVATE_DASHBOARD,5);
            if(driverTools.isElementDisplayed(DEACTIVATE_DASHBOARD)) {
                driverTools.waitForElementToAppear(DEACTIVATE_MEMBER_BUTTON, 10);
                driverTools.clickWithJavascriptExecutor(DEACTIVATE_MEMBER_BUTTON);
            }
        }

        public void clickActionOption () {
            driverTools.waitForElementToAppear(MEMBER_ACTION_BUTTON, 10);
            WebElement ele = driver.findElement(MEMBER_ACTION_BUTTON.by());
            driverTools.clickSvgOrNonHtmlElements(ele);
        }

        public boolean checkIfErrorAppears () {
            driverTools.waitForElementToAppear(NAME_ERROR);
            return driverTools.isElementDisplayed(NAME_ERROR);
        }

        public boolean checkResendLinkSnackbar () {
            driverTools.waitForElementToAppear(RESEND_SNACKBAR, 5);
            return driverTools.isElementDisplayed(RESEND_SNACKBAR);
        }

        public boolean isRegistrationStatusFilterPresent () {
            driverTools.waitForElementToAppear(REGISTRATION_STATUS_FILTER, 5);
            return driverTools.isElementDisplayed(REGISTRATION_STATUS_FILTER);
        }

        public boolean isAdminPresent () {
        driverTools.waitForElementToAppear(ADMIN_ROLE_OPTION,3);
            return driverTools.isElementPresent(ADMIN_ROLE_OPTION);
        }

        public String getCurrentRole (){
           driverTools.waitForElementToAppear(CURRENT_ROLE);
           return driverTools.getText(CURRENT_ROLE).orElseThrow();
        }

        public void searchNameEmployee(String name){
            driverTools.waitForElementToAppear(SEARCH_NAME,10);
            driverTools.click(SEARCH_NAME);
            driverTools.clearInput(SEARCH_NAME);
            driverTools.sendKeys(SEARCH_NAME,name);

            driverTools.dummyWait(5);
        }

        public void clickEmployeeActionButton()  {
            driverTools.dummyWait(10);
            driverTools.waitForElementToBeClickable(EMPLOYEE_ACTION_BTN, 10);
            driverTools.click(EMPLOYEE_ACTION_BTN);
        }

        public void sendMail()  {
            driverTools.waitForElementToAppear(LINK_RESEND, 10);
            driverTools.scrollIntoWebView(LINK_RESEND);
            driverTools.click(LINK_RESEND);
        }

        public boolean isMailSentSuccessfully(){
            return driverTools.isElementPresent(MAIL_SUCCESS_SNACKBAR);
        }

        public void openYopmailInNewTab(String email) {
            String yopmailUrl = "https://yopmail.com/en/";

            driverTools.openNewTabAndPasteUrl(yopmailUrl);

            driverTools.waitForElementToAppear(MAIL_INPUT,10);
            driverTools.sendKeys(MAIL_INPUT, email);

            driverTools.click(SEND_BUTTON);
        }

        public boolean isUNMRedirectionLinkPresent(String email2){
            Locator EMAIL_CONFORMATION = new Locator("EMAIL_CONFORMATION", By.xpath("//div[@class='bname' and contains(text(), '" + email2 + "')]"));
            driverTools.waitForElementToAppear(EMAIL_CONFORMATION, 10);
            return driverTools.isElementPresent(EMAIL_CONFORMATION);
        }

        public void setRegistrationStatusFilter(){
            driverTools.waitForElementToAppear(FILTER_OPTION,5);
            driverTools.click(FILTER_OPTION);

            driverTools.waitForElementToAppear(EXPIRED,10);
            driverTools.click(EXPIRED);

            driverTools.waitForElementToAppear(REFRESH,3);
        }

}

