package com.tiket.web.dashboard.page.platform.eiffel.ggwp;

import com.tiket.model.Locator;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Session;
import static org.hamcrest.core.Is.is;

public class CampaignSetupProductTokenPage<T extends WebDriver> extends WebPage<T> {
    private static final String CAMPAIGN_SETUP_URL = "/admin/ggwp/v2/deal-dash/campaign-setup";
    private static final Locator ADD_NEW_PRODUCT_OVERRIDE_BUTTON = new Locator("ADD_NEW_PRODUCT_OVERRIDE_BUTTON", By.xpath("//button[contains(., 'Add product Override')]"));
    private static final Locator LOADING_CONTAINER = new Locator("LOADING_CONTAINER", By.xpath("//div[contains(@class, 'LoadingWithIllustration_container')]"));
    private static final int LOADING_WAIT_TIMEOUT = 2;
    private static Locator getDropdownOptionLocator(String optionText) {
        return new Locator("DROPDOWN_OPTION_" + optionText.toUpperCase().replace(" ", "_"), By.xpath("//div[contains(@class, 'menu') or contains(@class, 'dropdown') or @role='listbox']//*[normalize-space()=" + com.tiket.core.CoreFunctions.toXPathLiteral(optionText) + "]"));
    }
    private static final Locator PRODUCT_TYPE_INPUT = new Locator("PRODUCT_TYPE_INPUT", By.id("vertical"));
    private static final Locator INVENTORY_DROPDOWN = new Locator("INVENTORY_DROPDOWN", By.id("inventory-id"));
    private static final Locator INVENTORY_SEARCH = new Locator("INVENTORY_SEARCH", By.xpath("//input[@placeholder='Search']"));
    private static final Locator INPUT_OVERRIDE_TOKEN = new Locator("INPUT_OVERRIDE_TOKEN", By.xpath("//input[@placeholder='Override Token']"));
    private static final Locator ADD_PRODUCT_BUTTON = new Locator("ADD_PRODUCT_BUTTON", By.xpath("//button[contains(., 'Add Product')]"));
    private static Locator GET_SUCCESS_SNACKBAR(String expectedMessage) {
        return new Locator("GET_SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'snackbar') and contains(@class, 'type_success') and contains(., '" + expectedMessage + "')]"));
    }
    private static final int DEFAULT_WAIT_TIMEOUT = 10;
    private static Locator GET_NEWLY_ADDED_ROW(String productType, String inventoryId, String overrideToken) {
        return new Locator("GET_NEWLY_ADDED_ROW", By.xpath("//tr[contains(., '" + productType + "') and contains(., '" + inventoryId + "') and contains(., '" + overrideToken + "')]"));
    }
    private static final Locator FIRST_INVENTORY_ITEM = new Locator("FIRST_INVENTORY_ITEM", By.xpath("(//div[@data-testid='dropdown-item'])[1]"));

    public CampaignSetupProductTokenPage(Session<T> session) {
        super(session);
    }

    public void goToCampaignSetupProductTokenPageViaUI(String userName, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(CAMPAIGN_SETUP_URL);

        if (!memberLoginPage.isLoginUsingExpectedEmail(userName)) {
            memberLoginPage.logoutAdmin();
            memberLoginPage.adminLogin(userName, password);
            memberLoginPage.goToPage(CAMPAIGN_SETUP_URL);
        }
    }

    public void clickAddProductOverride() {
        waitForLoadingToDisappear();
        driverTools.click(ADD_NEW_PRODUCT_OVERRIDE_BUTTON);
    }

    private void waitForLoadingToDisappear() {
        driverTools.waitForElementToAppear(LOADING_CONTAINER, LOADING_WAIT_TIMEOUT).ifPresent(e -> driverTools.waitForElementToDisappear(e));
    }

    public void selectProductType(String vertical) {
        selectDropdownOption(PRODUCT_TYPE_INPUT, vertical);
    }

    private void selectDropdownOption(Locator dropdownLocator, String optionText) {
        driverTools.scrollElementToCenter(dropdownLocator);
        driverTools.click(dropdownLocator);
        driverTools.waitForElementToBeClickable(dropdownLocator, DEFAULT_WAIT_TIMEOUT);
        Locator optionLocator = getDropdownOptionLocator(optionText);
        driverTools.waitForElementToBeClickable(optionLocator, DEFAULT_WAIT_TIMEOUT);
        driverTools.click(optionLocator);
    }

    public void clickInventoryId() {
        driverTools.dummyWait(2);
        driverTools.scrollElementToCenter(INVENTORY_DROPDOWN);
        driverTools.click(INVENTORY_DROPDOWN);
    }

    public void inputOverrideToken(String overrideToken) {
        driverTools.sendKeys(INPUT_OVERRIDE_TOKEN, overrideToken);
    }

    public void clickAddProduct() {
        driverTools.click(ADD_PRODUCT_BUTTON);
    }

    public void verifySuccessSnackbarDisplayed(String expectedMessage) {
        Locator specificSnackbar = GET_SUCCESS_SNACKBAR(expectedMessage);
        boolean isDisplayed = driverTools.waitForElementToAppear(specificSnackbar, DEFAULT_WAIT_TIMEOUT).isPresent();
        Assertion.assertThat("Success snackbar with text '" + expectedMessage + "' should be displayed", isDisplayed, is(true));
        driverTools.waitForElementToDisappear(specificSnackbar);
    }

    public void verifyProductTokenList(String productType, String inventoryId, String overrideToken) {
        driverTools.dummyWait(1);
        waitForLoadingToDisappear();
        Locator newlyAddedRow = GET_NEWLY_ADDED_ROW(productType, inventoryId, overrideToken);

        boolean isRowDisplayed = driverTools.waitForElementToAppear(newlyAddedRow, DEFAULT_WAIT_TIMEOUT).isPresent();

        if (isRowDisplayed) {
            driverTools.scrollElementToCenter(newlyAddedRow);
        }
        Assertion.assertThat("The product token list should display a row with Type: " + productType + ", Inventory ID: " + inventoryId + " and Token: " + overrideToken, isRowDisplayed, is(true)
        );
    }

    public String selectFirstAvailableInventoryId() {
        driverTools.waitForElementToBeClickable(INVENTORY_SEARCH, DEFAULT_WAIT_TIMEOUT);
        driverTools.sendKeys(INVENTORY_SEARCH, "aut");
        driverTools.dummyWait(2);
        driverTools.waitForElementToBeClickable(FIRST_INVENTORY_ITEM, DEFAULT_WAIT_TIMEOUT);

        String rawName = driverTools.getAttribute(FIRST_INVENTORY_ITEM.by(), "innerText");
        String finalName = rawName;
        if (rawName != null && rawName.contains(" - ")) {
            finalName = rawName.split(" - ", 2)[1].trim();
        }

        driverTools.click(FIRST_INVENTORY_ITEM);
        return finalName;
    }
}