package com.tiket.web.dashboard.page.nft.airport_transfer;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.is;

public class AirportTransferBusinessProfilePage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator SEARCH_INPUT_FIELD = new Locator("SEARCH_INPUT_FIELD", new By.ByXPath("//input[@name='searchBox']"));
    private static final Locator RESULT_TABLE = new Locator("RESULT_TABLE", new By.ByXPath("//div[@class='ant-table-wrapper table-v4']"));
    private static final Locator TRANSFER_IN = new Locator("TRANSFER_IN", new By.ByXPath("//div[contains(text(),'Transfer IN')]"));
    private static final Locator TRANSFER_OUT = new Locator("TRANSFER_OUT", new By.ByXPath("//div[contains(text(),'Transfer OUT')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("(//button[@type='button'])[2]"));
    private static final Locator EDIT_PROFILE_BUTTON = new Locator("EDIT_PROFILE_BUTTON", new By.ByXPath("//button[contains(@class,'edit-manage-account-action-btn') and .//span[normalize-space() = 'Manage Account' or normalize-space() = 'Edit Account']]"));
    private static final Locator ADD_ACCOUNT_BUTTON = new Locator("ADD_ACCOUNT_BUTTON", new By.ByXPath("(//button[@class='tix-button v4 btn-new-account mobile button waves-effect waves-default'])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("(//button[@class='tix-button v4 btn-add-account mobile button waves-effect waves-default block'])[1]"));
    private static final Locator FIRST_NAME_INPUT = new Locator("FIRST_NAME_INPUT", new By.ByXPath("//input[@name='firstName']"));
    private static final Locator LAST_NAME_INPUT = new Locator("LAST_NAME_INPUT", new By.ByXPath("//input[@name='lastName']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@name='email']"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", new By.ByXPath("//input[@name='phoneNumber']"));
    private static final Locator COUNTRY_CODE_DROPDOWN = new Locator("COUNTRY_CODE_DROPDOWN", new By.ByXPath("(//div[@class='phoneCode'])"));
    private static final Locator SNACKBAR = new Locator("SNACKBAR", new By.ByXPath("//div[@class='tix-snackbar v4 type-default out-slide visible mobile']"));
    private static final Locator ACCOUNT_TABLE = new Locator("ACCOUNT_TABLE", new By.ByXPath("//div[@class='table table-striped car-table manage-account-business']"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", new By.ByXPath("//button[normalize-space() = 'Ya' or normalize-space() = 'Yes']"));
    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("(//button[@type='button'])[3]"));
    private static final Locator EDIT_COMMISSION = new Locator("EDIT_COMMISSION", new By.ByXPath("(//input[@class='input-text v4 mobile'])"));
    private static final Locator INPUT_COMMISSION = new Locator("INPUT_COMMISSION", new By.ByXPath("(//input[@name='commission'])"));
    private static final Locator BTN_SAVECOMMISSION = new Locator("BTN_SAVECOMMISSION", new By.ByXPath("//button[@class='tix-button v4 confirmation-btn-commission mobile button waves-effect waves-default block']"));
    private static final Locator FIRST_COUNTRY_CODE = new Locator("FIRST_COUNTRY_CODE", new By.ByXPath("(//div[contains(@class,'phoneCode__menu')]//div[contains(@class,'phoneCode__option')])[1]"));
    private static final Locator DROPDOWN_INPUT = new Locator("DROPDOWN_INPUT", new By.ByXPath("//input[contains(@class,'phoneCode__input')]"));
    private static final Locator DROPDOWN_INDICATOR = new Locator("DROPDOWN_INDICATOR", new By.ByXPath("//div[contains(@class,'phoneCode__dropdown-indicator')]"));
    private static final Locator AIRPORT_TRANSFER_MENU = new Locator("AIRPORT_TRANSFER_MENU", By.xpath("//a[contains(@href, '/admin/nft/airport-transfer')]"));
    private static final Locator BUSINESS_PROFILE_PAGE_TITLE = new Locator("BUSINESS_PROFILE_PAGE_TITLE", By.xpath("//h2[contains(@class, 'BusinessProfileContent_title')]"));
    private static final Locator FINANCE_MENU = new Locator("FINANCE_MENU", By.xpath("//button[.//span[text()='Finance']]"));
    private static final Locator FINANCE_PAGE_TITLE = new Locator("FINANCE_PAGE_TITLE", By.xpath("//h2[contains(@class, 'FinanceContent_title')]"));
    private static final Locator TABLE_ACTION_BUTTONS = new Locator("TABLE_ACTION_BUTTONS", By.xpath("//div[contains(@class, 'ListPageBuilder_table_actions')]//button"));
    private static final Locator ALL_BUTTONS = new Locator("ALL_BUTTONS", By.xpath("//button"));

    public AirportTransferBusinessProfilePage(Session<T> session) {
        super(session);
    }

    public void goToATPage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer");
        }
    }

    public void goToNFTDashboardPage(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/nft");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/nft");
        }
    }


    public boolean isAirportTransferLoggedIn() {
        return driverTools.waitForElementToAppear(USERNAME_GREETING, 2).isPresent();
    }

    public boolean verifySuccessSnackbarDisplayed(String expectedText){
        WebElement modal = driverTools.waitForElementToAppear(SNACKBAR).get();
        return driverTools.getText(modal).contains(expectedText);
    }

    public boolean verifySRPContainsCorrectName(String name) {
        List<WebElement> businessProfileResults = driverTools.elements(RESULT_TABLE).orElseThrow();
        for (WebElement e : businessProfileResults) {
            String businessProfileNameValue = driverTools.getText(e);
            logger.log("Business Profile name value : " + businessProfileNameValue);
            if (!businessProfileNameValue.contains(name)) {
                return false;
            }
        }
        return true;
    }

    public boolean verifyAccountTableContainsCorrectName(String name) {
        List<WebElement> businessProfileResults = driverTools.elements(ACCOUNT_TABLE).orElseThrow();
        for (WebElement e : businessProfileResults) {
            String businessProfileNameValue = driverTools.getText(e);
            logger.log("Business Profile name value : " + businessProfileNameValue);
            if (!businessProfileNameValue.contains(name)) {
                return false;
            }
        }
        return true;
    }

    public void goToBusinessProfileTab() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/airport-transfer/business-profile");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/airport-transfer/business-profile");
        }
    }

    public void searchBusinessProfileByName(String name){
        WebElement searchField = driverTools.waitForElementToAppear(SEARCH_INPUT_FIELD).get();
        driverTools.sendKeys(searchField, name);
        driverTools.sendKeys(searchField, Keys.RETURN.toString());
    }

    public void filterTransferIn(){
        driverTools.clickWithJavascriptExecutor(TRANSFER_IN);
    }

    public void filterTransferOut(){
        driverTools.clickWithJavascriptExecutor(TRANSFER_OUT);
    }

    public void addNewAccount(String firstName, String lastName, String phoneNumber){
        String email = generateRandomEmail();
        driverTools.waitForElementToAppear(EDIT_PROFILE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_PROFILE_BUTTON);
        WebElement addAccountButton = driverTools.waitForElementToAppear(ADD_ACCOUNT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(addAccountButton);

        driverTools.sendKeys(FIRST_NAME_INPUT, firstName);
        driverTools.sendKeys(LAST_NAME_INPUT, lastName);
        driverTools.sendKeys(EMAIL_INPUT, email);

        new AirportTransferFleetPage<>(session).selectFirstDropdownOption(COUNTRY_CODE_DROPDOWN, FIRST_COUNTRY_CODE, DROPDOWN_INPUT, DROPDOWN_INDICATOR);

        driverTools.sendKeys(PHONE_INPUT, phoneNumber);

        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void editAccount(String firstName, String lastName, String phoneNumber){
        driverTools.waitForElementToAppear(EDIT_PROFILE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_PROFILE_BUTTON);
        WebElement editAccountButton = driverTools.waitForElementToAppear(EDIT_BUTTON).get();
        driverTools.clickWithJavascriptExecutor(editAccountButton);

        driverTools.waitForElementToAppear(FIRST_NAME_INPUT);
        driverTools.sendKeys(FIRST_NAME_INPUT, firstName);
        driverTools.sendKeys(LAST_NAME_INPUT, lastName);
        driverTools.sendKeys(PHONE_INPUT, phoneNumber);

        new AirportTransferFleetPage<>(session).selectFirstDropdownOption(COUNTRY_CODE_DROPDOWN, FIRST_COUNTRY_CODE, DROPDOWN_INPUT, DROPDOWN_INDICATOR);

        driverTools.waitForElementToBeClickable(SAVE_BUTTON);
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    public void deleteAccount(){
        driverTools.waitForElementToAppear(EDIT_PROFILE_BUTTON);
        driverTools.clickWithJavascriptExecutor(EDIT_PROFILE_BUTTON);
        driverTools.waitForElementToAppear(DELETE_BUTTON);
        driverTools.clickWithJavascriptExecutor(DELETE_BUTTON);
        driverTools.waitForElementToAppear(YES_BUTTON);
        driverTools.clickWithJavascriptExecutor(YES_BUTTON);
    }

    public void clickCommission() {
        WebElement clickCommision = driverTools.waitForElementToAppear(EDIT_COMMISSION).get();
        driverTools.clickWithJavascriptExecutor(clickCommision);
    }

    public void inputCommission(String commission) {
        driverTools.clickWithJavascriptExecutor(INPUT_COMMISSION);
        driverTools.sendKeys(INPUT_COMMISSION, Keys.COMMAND + "a");
        driverTools.sendKeys(INPUT_COMMISSION, commission);
        driverTools.clickWithJavascriptExecutor(BTN_SAVECOMMISSION);
    }

    private String generateRandomEmail() {
        final String prefix = "automation";
        final String domain = "gmail.com";
        final String ALPHANUM = "abcdefghijklmnopqrstuvwxyz0123456789";
        java.util.Random rnd = new java.util.Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(ALPHANUM.charAt(rnd.nextInt(ALPHANUM.length())));
        }
        String randomSuffix = System.currentTimeMillis() + "_" + sb.toString();
        return String.format("%s+%s@%s", prefix, randomSuffix, domain);
    }

    public void clickAirportTransferMenu() {
        int handleCountBeforeClick = driverTools.getWindowHandles().size();

        WebElement menuBtn = driverTools.waitForElementToBeClickable(AIRPORT_TRANSFER_MENU).orElseThrow(() -> new NoSuchElementException("Airport Transfer menu not found."));
        driverTools.scrollElementToCenter(menuBtn);
        driverTools.click(menuBtn);

        driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);

        driverTools.waitForElementToAppear(BUSINESS_PROFILE_PAGE_TITLE).orElseThrow(() -> new NoSuchElementException("Business Profile page did not load after clicking Airport Transfer menu."));
    }

    public void clickFinanceMenu() {
        WebElement financeBtn = driverTools.waitForElementToBeClickable(FINANCE_MENU).orElseThrow(
                () -> new NoSuchElementException("Finance menu not found by SVG icon.")
        );
        driverTools.scrollElementToCenter(financeBtn);
        driverTools.click(financeBtn);
    }

    public void verifyFinancePageOpened() {
        WebElement title = driverTools.waitForElementToAppear(FINANCE_PAGE_TITLE).orElseThrow(
                () -> new NoSuchElementException("Finance page title not found by class.")
        );
        Assertion.assertThat("Verify Finance page title is displayed", title.isDisplayed(), is(true));
    }

    public void verifyUploadXlsButtonNotPresent() {
        driverTools.waitForElementToAppear(TABLE_ACTION_BUTTONS);
        List<WebElement> allButtons = driver.findElements(ALL_BUTTONS.by());
        logger.log("Traversing " + allButtons.size() + " button(s) on the page to check for the 'Upload XSL' button.");
        boolean isUploadPresent = false;
        for (int i = 0; i < allButtons.size(); i++) {
            String buttonText = allButtons.get(i).getText().trim().toLowerCase();
            logger.log("Button [" + i + "]: \"" + buttonText + "\"");
            if (buttonText.contains("upload xsl")) {
                isUploadPresent = true;
                logger.log("'Upload XSL' button found at index " + i + ": \"" + buttonText + "\"");
                break;
            }
        }
        Assertion.assertThat("Verify no 'Upload XSL' button is present on the page", isUploadPresent, is(false));
    }
}