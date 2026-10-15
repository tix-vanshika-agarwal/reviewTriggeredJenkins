package com.tiket.web.dashboard.page.platform.insurance;

import com.google.common.collect.Iterables;
import com.tiket.core.CoreFunctions;
import com.tiket.logging.ILogger;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;

public class CredentialDashboardPage<T extends WebDriver> extends BasePage<T> {
    public CredentialDashboardPage(Session<T> session) {
        super(session);
    }

    ILogger logger = BaseTest.mainLogger.get();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    private final String INSURANCE_CRED_AND_PREM_PATH = "admin/insurance/credential";
    private static final Locator CREDENTIAL_CONTAINER = new Locator("CREDENTIAL_CONTAINER", By.xpath("//div[contains(@class, 'Layout_layout_section_content')]"));
    private static final Locator SEARCH_MENU = new Locator("SEARCH_MENU", By.xpath("//div[contains(@class, 'Card_card_header') and contains(@class, 'credential_credential_filter')]"));
    private static final Locator LOADING = new Locator("LOADING", By.xpath("//div[@data-testid='loading-credential-and-group']"));
    private static final Locator CREDENTIAL_INPUT = new Locator("CREDENTIAL_INPUT", By.xpath("//*[@data-testid='credential-filter-name']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[@data-testid='credential-btn-filter-submit']"));
    private static final Locator TABLE_RESULT = new Locator("TABLE_RESULT", By.xpath("//tr[contains(@class, 'Table_table_tr') and contains(@class, 'Table_stripped_child')]"));
    private static final Locator TOOLTIPS = new Locator("TOOLTIPS", By.xpath(".//div[contains(@class, 'Tooltip_tooltip')]"));
    private static final Locator TABLE_CREDENTIALS_LIST = new Locator("TABLE_CREDENTIALS_LIST", By.xpath(".//tr[contains(@class, 'Table_table_tr') and contains(@class, 'Table_striped')]"));
    private static final Locator TABLE_CREDENTIALS = new Locator("TABLE_CREDENTIALS", By.xpath("//table[contains(@class, 'credential_credential_table')]"));
    private static final Locator CREDENTIAL_NAME = new Locator("CREDENTIAL_NAME", By.xpath(".//td[contains(@class, 'Table_table_tr_td') and contains(@class, 'Table_table_tr_td_sticky')]"));
    private static final Locator INSURANCE_GROUP_SPAN = new Locator("INSURANCE_GROUP_SPAN", By.xpath(".//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator MORE_ACTIONS = new Locator("MORE_ACTIONS", By.xpath(".//div[contains(@class, 'styles_input_field_wrapper') and contains(@class, 'DropdownFormDisplay_dropdown_form_display_input_wrapper')]"));
    private static final Locator MORE = new Locator("MORE", By.id("more"));
    private static final Locator MENU_ACTION_LIST = new Locator("MENU_ACTION_LIST", By.xpath("//div[@data-testid='menu-action-list']"));
    private static final Locator DETAILS_ACTION = new Locator("DETAIL_ACTIONS", By.xpath("//span[@data-testid='button-detail']"));
    private static final Locator POP_UP_DETAILS = new Locator("POP_UP_DETAILS", By.xpath("//section[contains(@class, 'Modal_modal_popup')]"));
    private static final Locator CLIENT_ID_ELEMENT = new Locator("CLIENT_ID", By.xpath("//div[text()='Client ID']/following-sibling::span"));
    private static final Locator CREDENTIAL_NAME_ELEMENT = new Locator("CREDENTIAL_NAME_ELEMENT", By.xpath("//div[text()='Credential Name']/following-sibling::span"));
    private static final Locator TAG_ELEMENT = new Locator("TAG_ELEMENT", By.xpath("//div[text()='Tag']/following-sibling::span"));
    private static final Locator PARTNER_NAME_ELEMENT = new Locator("PARTNER_NAME_ELEMENT", By.xpath("//div[text()='Partner Name']/following-sibling::span"));
    private static final Locator STATUS_ELEMENT = new Locator("STATUS_ELEMENT", By.xpath("//div[text()='Status']/following-sibling::span"));
    private static final Locator CLAIM_LINK_ELEMENT = new Locator("CLAIM_LINK_ELEMENT", By.xpath("//div[text()='Claim Link']/following-sibling::span"));
    private static final Locator TAX_ELEMENT = new Locator("TAX_ELEMENT", By.xpath("//div[text()='Tax']/following-sibling::span"));
    private static final Locator ENABLE_SEND_EMAIL_ELEMENT = new Locator("ENABLE_SEND_EMAIL_ELEMENT", By.xpath("//div[text()='Enable Send Email']/following-sibling::span"));
    private static final Locator INSURANCE_GROUP_FIELD = new Locator("INSURANCE_GROUP_FIELD", By.id("insurance-group"));
    private static final Locator ADD_CREDENTIAL_HEADER = new Locator("ADD_CREDENTIAL_HEADER", By.xpath("//div[contains(@class, 'page_header_container')]//p"));
    private static final Locator CREDENTIAL_NAME_FIELD = new Locator("CREDENTIAL_NAME_FIELD", By.id("credential-name"));
    private static final Locator PARTNER_NAME_CONTAINER = new Locator("PARTNER_NAME_CONTAINER", By.xpath("//input[@id='partner-name']"));
    private static final Locator INSURANCE_TYPE_CONTAINER = new Locator("INSURANCE_TYPE_CONTAINER", By.xpath("//input[@id='insurance-type']"));
    private static final Locator VARIABLE_NAME_CONTAINER = new Locator("VARIABLE_NAME_CONTAINER", By.xpath("//input[@id='variable-name']"));
    private static final Locator VARIABLE_VALUE_FIELD = new Locator("VARIABLE_VALUE_FIELD", By.xpath("//input[contains(@data-testid, 'external-rule-value')]"));
    private static final Locator OPERATOR_CONTAINER = new Locator("OPERATOR_CONTAINER", By.xpath("//input[@id='operator']"));
    private static final Locator PARTNER_NAME_LIST = new Locator("PARTNER_NAME_LIST", By.xpath("//div[@data-testid='input-select-item-credential-client-name']"));
    private static final Locator INSURANCE_TYPE_LIST = new Locator("INSURANCE_TYPE_LIST", By.xpath("//div[@data-testid='input-select-item-credential-insurance-type']"));
    private static final Locator VARIABLE_NAME_LIST = new Locator("VARIABLE_NAME_LIST", By.xpath("//div[contains(@data-testid, 'input-select-item-external-rule-variable-name')]"));
    private static final Locator OPERATOR_LIST = new Locator("OPERATOR_LIST", By.xpath("//div[contains(@data-testid, 'input-select-item-external-rule-operator')]"));
    private static final Locator VARIABLE_NAME_SEARCH = new Locator("VARIABLE_NAME_SEARCH", By.xpath("//input[contains(@data-testid, 'search-select-external-rule-variable-name')]"));
    private static final Locator CLIENT_ID_FIELD = new Locator("CLIENT_ID_FIELD", By.id("client-id"));
    private static final Locator CLIENT_SECRET_FIELD = new Locator("CLIENT_SECRET_FIELD", By.id("client-secret"));
    private static final Locator CLIENT_CODE_FIELD = new Locator("CLIENT_CODE_FIELD", By.id("client-code"));
    private static final Locator SUCCESS_TOAST = new Locator("SUCCESS_TOAST", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));
    private static final Locator NEXT_BUTTON = new Locator("NEXT_BUTTON", By.xpath("//button[@data-testid='credential-submit-button']"));
    private static final Locator INSURANCE_CRED_AND_PREM_LINK = new Locator("INSURANCE_CRED_AND_PREM_LINK", By.xpath("//span[@title='Insurance Credential & Premium']/parent::a"));
    private static final Locator CREDENTIAL_TABLE_CONTAINER = new Locator("CREDENTIAL_TABLE_CONTAINER", By.xpath("//div[contains(@class, 'credential_credential_table_container')]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", By.xpath("//span[@data-testid='button-edit']"));
    private static final Locator DELETE_CREDENTIAL_BUTTON = new Locator("DELETE_CREDENTIAL_BUTTON", By.xpath(".//span[@data-testid='button-delete']/parent::a"));
    private static final Locator INSURANCE_GROUP_NAME_SPAN = new Locator("INSURANCE_GROUP_NAME_SPAN", By.xpath(".//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3') and contains(@class, 'Text_weight_bold')]"));
    private static final Locator MODAL_POP_UP = new Locator("MODAL_POP_UP", By.xpath("//section[contains(@class, 'Modal_modal_popup') and contains(@class, 'Modal_modal_popup_width_default')]"));
    private static final Locator DELETE_BUTTON_IN_POP_UP = new Locator("DELETE_BUTTON_IN_POP_UP", By.xpath(".//button[normalize-space()='Delete']"));
    private static final Locator INSURANCE_GROUP_TR = new Locator("INSURANCE_GROUP_TR", By.xpath("//tr[.//span[contains(@class, 'Text_text') and contains(@class, 'Text_size_b3') and contains(@class, 'Text_weight_bold')]]"));
    private static final Locator CREDENTIAL_ACTION_GROUP = new Locator("CREDENTIAL_ACTION_GROUP", By.xpath(".//div[contains(@class, 'credential_action_group_list')]"));

    private static final By NESTED_CREDENTIAL_TABLE_UNDER_ROW = By.xpath(
            ".//table[contains(@class, 'credential_credential_table')]"
                    + " | ./following-sibling::tr//table[contains(@class, 'credential_credential_table')]"
                    + " | ./following::div[contains(@class, 'credential_credential_table_container')]//table[contains(@class, 'credential_credential_table')]"
    );

    private boolean isNestedCredentialTableVisibleUnderRow(WebElement groupRow) {
        return groupRow.findElements(NESTED_CREDENTIAL_TABLE_UNDER_ROW).stream()
                .anyMatch(e -> {
                    try {
                        return e.isDisplayed();
                    } catch (Exception ex) {
                        return false;
                    }
                });
    }

    private void waitUntilNestedCredentialTableUnderRow(int rowIndex) {
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        longWait.until(d -> {
            List<WebElement> rows = driverTools.elements(TABLE_RESULT).orElseThrow();
            if (rowIndex >= rows.size()) {
                return false;
            }
            return isNestedCredentialTableVisibleUnderRow(rows.get(rowIndex));
        });
    }

    public void goToCredentialDashboard() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(sheetData.get("username"), sheetData.get("password"));
        memberLoginPage.goToPage(INSURANCE_CRED_AND_PREM_PATH);
    }

    public void goToCredentialDashboardViaUI() {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.goToPage(INSURANCE_CRED_AND_PREM_PATH);

        if (!memberLoginPage.isLoginUsingExpectedEmail(sheetData.get("username"))) {
            memberLoginPage.logoutAdmin();
            memberLoginPage.adminLogin(sheetData.get("username"), sheetData.get("password"));
            memberLoginPage.goToPage(INSURANCE_CRED_AND_PREM_PATH);
        }
    }

    public void verifyCredentialDashboardPage() {
        driverTools.waitForElementToAppear(LOADING, 2);
        if (driverTools.isElementDisplayed(LOADING)) {
            driverTools.waitForElementToDisappear(LOADING);
        }

        driverTools.waitForElementToAppear(CREDENTIAL_CONTAINER);

        Assertion.assertThat("Verify url", driver.getCurrentUrl(), is(containsStringIgnoringCase("admin/insurance/credential")));
        Assertion.assertThat("Verify Credential menu container is visible", driverTools.isElementDisplayed(CREDENTIAL_CONTAINER), is(true));
        Assertion.assertThat("Verify Search menu container is visible", driverTools.isElementDisplayed(SEARCH_MENU), is(true));
    }

    public void inputCredential(String credential) {
        driverTools.waitForElementToAppear(CREDENTIAL_INPUT);
        driverTools.scrollElementToCenter(CREDENTIAL_INPUT);
        driverTools.clickWithJavascriptExecutor(CREDENTIAL_INPUT);
        driverTools.sendKeys(CREDENTIAL_INPUT, credential);
        driverTools.scrollElementToCenter(SEARCH_BUTTON);
        driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        driverTools.waitForElementToAppear(LOADING, 1);
        if (driverTools.isElementDisplayed(LOADING)) {
            driverTools.waitForElementToDisappear(LOADING);
        }
        driverTools.waitForElementToAppear(TABLE_CREDENTIALS, 15);
    }

    public void getSearchResult() {
        ((JavascriptExecutor) driver).executeScript("document.querySelector('.Footer_footer__KCpT9').style.display='none';");
        List<String> credNames = new ArrayList<>();
        List<WebElement> totalElements = driverTools.elements(TABLE_RESULT).orElseThrow();
        if (totalElements.isEmpty()) {
            Assertion.fail("No insurance group rows (TABLE_RESULT) after credential search. Check filter data or locators.");
        }
        for (int i = 0; i < totalElements.size(); i++) {
            credNames.clear();

            String insuranceGroup = driverTools.element(INSURANCE_GROUP_SPAN, totalElements.get(i)).orElseThrow().getText();
            List<WebElement> actionGroupLists = driverTools.elements(CREDENTIAL_ACTION_GROUP, totalElements.get(i)).orElseThrow();
            if (actionGroupLists.size() != 1) {
                Assertion.fail("Expected 1 action group list in row at index " + i + " ('" + insuranceGroup + "'), found " + actionGroupLists.size());
            }
            List<WebElement> tooltips = driverTools.elements(TOOLTIPS, actionGroupLists.get(0)).orElseThrow();
            if (tooltips.size() != 7) {
                Assertion.fail("Tooltips changed for row at index " + i + " ('" + insuranceGroup + "'): expected 7, found " + tooltips.size());
            }

            WebElement groupRow = totalElements.get(i);
            if (!isNestedCredentialTableVisibleUnderRow(groupRow)) {
                WebElement expandGroupTooltip = tooltips.get(6);
                driverTools.scrollElementToCenter(expandGroupTooltip);
                driverTools.click(expandGroupTooltip);
            }

            if (driverTools.isElementDisplayed(LOADING)) {
                driverTools.waitForElementToDisappear(LOADING);
            }
            waitUntilNestedCredentialTableUnderRow(i);

            List<WebElement> tableCredentials = driverTools.elements(TABLE_CREDENTIALS).orElseThrow();
            if (tableCredentials.isEmpty()) {
                Assertion.fail("No credential tables (TABLE_CREDENTIALS) after expanding group at index " + i + " ('" + insuranceGroup + "'). Check TABLE_CREDENTIALS locator or expand behavior.");
            }
            if (i >= tableCredentials.size()) {
                Assertion.fail(String.format(
                        "Fewer credential tables (%d) than insurance group rows (%d). Cannot use tableCredentials.get(%d). Expand groups or fix locator coupling.",
                        tableCredentials.size(), totalElements.size(), i));
            }

            List<WebElement> credentialLists = driverTools.elements(TABLE_CREDENTIALS_LIST, tableCredentials.get(i)).orElseThrow();
            credentialLists.stream().skip(1).forEach(result -> {
                List<WebElement> tableData = result.findElements(CREDENTIAL_NAME.by());
                if (tableData.isEmpty()) {
                    Assertion.fail("CREDENTIAL_NAME cell not found in a credential row for insurance group '" + insuranceGroup + "'. Check TABLE_CREDENTIALS_LIST / CREDENTIAL_NAME locators.");
                }
                credNames.add(tableData.get(0).getText());
                driverTools.scrollIntoWebView(result);
            });
            Assertion.assertThat(String.format("Verify insurance group '%s' with total data of '%d' all contains %s", insuranceGroup, credNames.size(), sheetData.get("credential")), credNames.stream().allMatch(s -> s.toLowerCase().contains(String.valueOf(sheetData.get("credential")))), is(true));
        }
    }

    public void clickDetailsOnAResult() {
        driverTools.scrollIntoWebView(SEARCH_MENU);
        List<WebElement> totalElements = driverTools.elements(TABLE_RESULT).orElseThrow();
        if (totalElements.isEmpty()) {
            Assertion.fail("No search result rows (TABLE_RESULT); cannot open credential details.");
        }
        List<WebElement> actionGroupLists = driverTools.elements(CREDENTIAL_ACTION_GROUP, totalElements.get(0)).orElseThrow();
        if (actionGroupLists.size() != 1) {
            Assertion.fail("Expected 1 action group list on first search result row, found " + actionGroupLists.size());
        }
        List<WebElement> tooltips = driverTools.elements(TOOLTIPS, actionGroupLists.get(0)).orElseThrow();
        if (tooltips.size() != 7) {
            Assertion.fail("Tooltips changed on first row: expected 7, found " + tooltips.size());
        }
        WebElement expandGroupTooltip = tooltips.get(6);
        driverTools.scrollElementToCenter(expandGroupTooltip);
        driverTools.clickWithJavascriptExecutor(expandGroupTooltip);

        if (driverTools.isElementDisplayed(LOADING)) {
            driverTools.waitForElementToDisappear(LOADING);
        }
        driverTools.waitForElementToAppear(TABLE_CREDENTIALS, 15);
        List<WebElement> tableCredentials = driverTools.elements(TABLE_CREDENTIALS).orElseThrow();
        if (tableCredentials.isEmpty()) {
            Assertion.fail("No credential table (TABLE_CREDENTIALS) after expanding first row; cannot open credential details.");
        }

        driverTools.clickWithJavascriptExecutor(driverTools.element(MORE_ACTIONS, tableCredentials.get(0)).orElseThrow());
        driverTools.waitForElementToAppear(MENU_ACTION_LIST);
        driverTools.click(DETAILS_ACTION);

        driverTools.waitForElementToDisappear(LOADING);

        Map<String, String> dataMap = getDataPopUP();
        Assertion.assertThat("Verify Client ID is present and not null", dataMap.get("clientId"), is(notNullValue()));
        Assertion.assertThat("Verify Credential Name is containing " + sheetData.get("credential"), dataMap.get("credentialName"), is(containsStringIgnoringCase(sheetData.get("credential"))));
        Assertion.assertThat("Verify Tag is present", dataMap.get("tag"), is(notNullValue()));
        Assertion.assertThat("Verify Partner Name is present", dataMap.get("partnerName"), is(notNullValue()));
        Assertion.assertThat("Verify status is present", dataMap.get("status"), is(notNullValue()));
        Assertion.assertThat("Verify Claim Link is present", dataMap.get("claimLink"), is(notNullValue()));
        Assertion.assertThat("Verify Tax is present", dataMap.get("tax"), is(notNullValue()));
        Assertion.assertThat("Verify Enable Send Email is present", dataMap.get("enableEmail"), is(notNullValue()));
    }

    private Map<String, String> getDataPopUP() {
        Map<String, String> result = new HashMap<>();
        driverTools.waitForElementToAppear(POP_UP_DETAILS);
        driverTools.waitForElementToAppear(CLIENT_ID_ELEMENT);

        final String clientId = driverTools.getText(CLIENT_ID_ELEMENT).orElseThrow();
        final String credentialName = driverTools.getText(CREDENTIAL_NAME_ELEMENT).orElseThrow();
        final String tag = driverTools.getText(TAG_ELEMENT).orElseThrow();
        final String partnerName = driverTools.getText(PARTNER_NAME_ELEMENT).orElseThrow();
        final String status = driverTools.getText(STATUS_ELEMENT).orElseThrow();
        final String claimLink = driverTools.getText(CLAIM_LINK_ELEMENT).orElseThrow();
        final String tax = driverTools.getText(TAX_ELEMENT).orElseThrow();
        final String enableEmail = driverTools.getText(ENABLE_SEND_EMAIL_ELEMENT).orElseThrow();

        result.put("clientId", clientId);
        result.put("credentialName", credentialName);
        result.put("tag", tag);
        result.put("partnerName", partnerName);
        result.put("status", status);
        result.put("claimLink", claimLink);
        result.put("tax", tax);
        result.put("enableEmail", enableEmail);

        return result;
    }

    public void searchInsuranceGroup(String insuranceGroupName) {
        driverTools.sendKeys(INSURANCE_GROUP_FIELD, insuranceGroupName);
        driverTools.click(SEARCH_BUTTON);
        driverTools.waitForElementToAppear(LOADING);
        driverTools.waitForElementToDisappear(LOADING);
    }

    public void clickOnAddCredentialButton() {
        clickOnToolTips("add", getSpecificInsuranceGroupElement(sheetData.get("insuranceGroupName")));
    }

    public void inputMandatoryFields() {
        driverTools.waitForElementToAppear(CREDENTIAL_NAME_FIELD, 3);

        final String pageTitle = driverTools.getText(ADD_CREDENTIAL_HEADER).orElse("");
        final String expectedPageTitle = sheetData.get("pageTitle");
        if (!pageTitle.equals(expectedPageTitle)) {
            Assertion.fail("Expected page title is " + expectedPageTitle + " but got " + pageTitle);
        }

        final String credentialName = sheetData.get("prefix") + CoreFunctions.generateRandomString(20);
        final String clientId = String.valueOf(CoreFunctions.generateRandomInt(8));
        final String clientSecret = CoreFunctions.generateRandomString(10);
        final String clientCode = CoreFunctions.generateRandomString(15);

        sheetData.put("credentialName", credentialName);
        sheetData.put("clientId", clientId);
        sheetData.put("clientSecret", clientSecret);
        sheetData.put("clientCode", clientCode);

        driverTools.scrollElementToCenter(CLIENT_ID_FIELD);
        driverTools.sendKeys(CREDENTIAL_NAME_FIELD, credentialName);

        driverTools.click(PARTNER_NAME_CONTAINER);
        driverTools.waitForElementToAppear(PARTNER_NAME_LIST, 3);
        List<WebElement> partnerList = driverTools.elements(PARTNER_NAME_LIST).orElseThrow();
        sheetData.put("partnerName", driverTools.getText(partnerList.get(0)));
        driverTools.click(partnerList.get(0));

        driverTools.click(INSURANCE_TYPE_CONTAINER);
        driverTools.waitForElementToAppear(INSURANCE_TYPE_LIST, 3);
        List<WebElement> insuranceTypeList = driverTools.elements(INSURANCE_TYPE_LIST).orElseThrow();
        sheetData.put("insuranceType", driverTools.getText(insuranceTypeList.get(0)));
        driverTools.click(insuranceTypeList.get(0));

        driverTools.sendKeys(CLIENT_ID_FIELD, clientId);
        driverTools.sendKeys(CLIENT_SECRET_FIELD, clientSecret);
        driverTools.sendKeys(CLIENT_CODE_FIELD, clientCode);

        driverTools.scrollElementToCenter(VARIABLE_NAME_CONTAINER);
        driverTools.click(VARIABLE_NAME_CONTAINER);
        driverTools.waitForElementToAppear(VARIABLE_NAME_LIST, 3);
        List<WebElement> variableNameListPrev = driverTools.elements(VARIABLE_NAME_LIST).orElseThrow();
        driverTools.sendKeys(VARIABLE_NAME_SEARCH, sheetData.get("variableName"));
        wait.until(driver -> driverTools.elements(VARIABLE_NAME_LIST).orElseThrow().size() < variableNameListPrev.size());
        List<WebElement> variableNameList = driverTools.elements(VARIABLE_NAME_LIST).orElseThrow();
        driverTools.click(variableNameList.get(0));

        driverTools.click(OPERATOR_CONTAINER);
        driverTools.waitForElementToAppear(OPERATOR_LIST, 3);
        List<WebElement> operatorList = driverTools.elements(OPERATOR_LIST).orElseThrow();
        sheetData.put("operator", driverTools.getText(operatorList.get(0)));
        driverTools.click(operatorList.get(0));

        driverTools.sendKeys(VARIABLE_VALUE_FIELD, sheetData.get("variableValue"));

        driverTools.scrollElementToCenter(NEXT_BUTTON);
        driverTools.click(NEXT_BUTTON);

        verifySuccessToast();
    }

    private void verifySuccessToast() {
        driverTools.waitForElementToAppear(SUCCESS_TOAST);
        if (driverTools.isElementDisplayed(SUCCESS_TOAST)) {
            successToastData.put("isShown", true);
        } else {
            successToastData.put("isShown", false);
        }

        String successMessage = driverTools.getText(SUCCESS_TOAST).orElse("");
        successToastData.put("successMsg", successMessage);
        driverTools.waitForElementToDisappear(SUCCESS_TOAST);
    }

    public void verifyCredentialCreated() {
        driverTools.click(INSURANCE_CRED_AND_PREM_LINK);
        driverTools.waitForElementToAppear(LOADING, 1);
        driverTools.waitForElementToDisappear(LOADING, 1);

        searchInsuranceGroup(sheetData.get("insuranceGroupName"));
        clickOnToolTips("expand", getSpecificInsuranceGroupElement(sheetData.get("insuranceGroupName")));

        WebElement moreElem = driverTools.element(MORE, getSpecificCredentialElement(sheetData.get("credentialName"))).orElseThrow();
        driverTools.click(moreElem);
        driverTools.waitForElementToAppear(MENU_ACTION_LIST);
        driverTools.click(driverTools.element(EDIT_BUTTON, driverTools.element(MENU_ACTION_LIST).orElseThrow()).orElseThrow());
        driverTools.waitForElementToAppear(CREDENTIAL_NAME_FIELD, 3);

        actualCredData.put("credentialName", driverTools.getValue(CREDENTIAL_NAME_FIELD));
        actualCredData.put("partnerName", driverTools.getValue(PARTNER_NAME_CONTAINER));
        actualCredData.put("insuranceType", driverTools.getValue(INSURANCE_TYPE_CONTAINER));
        actualCredData.put("clientId", driverTools.getValue(CLIENT_ID_FIELD));
        actualCredData.put("clientSecret", driverTools.getValue(CLIENT_SECRET_FIELD));
        actualCredData.put("clientCode", driverTools.getValue(CLIENT_CODE_FIELD));
        actualCredData.put("variableName", driverTools.getValue(VARIABLE_NAME_CONTAINER));
        List<WebElement> operatorElem = driverTools.elements(OPERATOR_CONTAINER).orElseThrow();
        driverTools.scrollElementToCenter(operatorElem.get(0));
        final String operatorValue = driverTools.getValue(operatorElem.get(0));
        actualCredData.put("operator", operatorValue);
        actualCredData.put("value", driverTools.getValue(VARIABLE_VALUE_FIELD));
    }

    public void normalizingTotalInsurance(int maxTotal) {
        driverTools.click(INSURANCE_CRED_AND_PREM_LINK);
        driverTools.waitForElementToAppear(LOADING, 1);
        driverTools.waitForElementToDisappear(LOADING, 1);
        searchInsuranceGroup(sheetData.get("insuranceGroupName"));
        clickOnToolTips("expand", getSpecificInsuranceGroupElement(sheetData.get("insuranceGroupName")));

        List<WebElement> credentialTableContainer = driverTools.elements(CREDENTIAL_TABLE_CONTAINER).orElseThrow();
        driverTools.scrollElementToCenter(credentialTableContainer.get(1));
        List<WebElement> credNameElements = driverTools.elements(CREDENTIAL_NAME, credentialTableContainer.get(1)).orElseThrow();

        List<String> nameToDelete = new ArrayList<>();
        for (int i = maxTotal; i < credNameElements.size(); i++) {
            String nameCred = credNameElements.get(i).getText();
            if (!nameCred.equalsIgnoreCase("more")) {
                nameToDelete.add(nameCred);
            }
        }
        logger.log("total credential to delete: " + nameToDelete.size());

        for (String name : nameToDelete) {
            deleteCredential(name);
        }
    }

    private WebElement getSpecificCredentialElement(String credentialName) {
        List<WebElement> credentialTableContainer = driverTools.elements(CREDENTIAL_TABLE_CONTAINER).orElseThrow();
        driverTools.scrollElementToCenter(credentialTableContainer.get(1));
        List<WebElement> credNameElements = driverTools.elements(CREDENTIAL_NAME, credentialTableContainer.get(1)).orElseThrow();
        if (credNameElements.isEmpty()) {
            Assertion.fail("No credential names found in the list.");
        }

        logger.log("Searching for '" + credentialName + "' in the credential name list");
        for (WebElement credNameElement : credNameElements) {
            String credName = credNameElement.getText();
            if (credName.equals(credentialName)) {
                logger.log("Found '" + credentialName + "' in the credential name list.");
                WebElement createdCredElement = driverTools.element(new Locator("createdCredElement", By.xpath("..")), credNameElement).orElseThrow();
                driverTools.scrollElementToCenter(createdCredElement);
                return createdCredElement;
            }
        }
        Assertion.fail("Credential name '" + credentialName + "' not found in the list.");
        return null;
    }

    private void clickOnToolTips(String tooltipName, WebElement insuranceGroupElement) {
        tooltipName = tooltipName.toLowerCase();
        List<WebElement> actionGroupLists = driverTools.elements(CREDENTIAL_ACTION_GROUP, insuranceGroupElement).orElseThrow();
        if (actionGroupLists.size() != 1) {
            Assertion.fail("Too many action group lists found: " + actionGroupLists.size() + ". Expected only 1.");
        }

        List<WebElement> tooltips = driverTools.elements(TOOLTIPS, actionGroupLists.get(0)).orElseThrow();
        if (tooltips.size() != 7) {
            Assertion.fail("Seems like the toolTips are changed, please check manually.");
        }

        switch (tooltipName) {
            case "add" -> driverTools.click(tooltips.get(1));
            case "edit" -> driverTools.click(tooltips.get(2));
            case "duplicate" -> driverTools.click(tooltips.get(3));
            case "delete" -> driverTools.click(tooltips.get(5));
            case "expand" -> driverTools.click(tooltips.get(6));
        }
    }

    private WebElement getSpecificInsuranceGroupElement(String insuranceGroupName) {
        List<WebElement> insuranceGroupElements = driverTools.elements(INSURANCE_GROUP_TR).orElseThrow();
        logger.log("total insurance group elements found: " + insuranceGroupElements.size());
        for (WebElement insuranceGroupElement : insuranceGroupElements) {
            WebElement insuranceGroupNameSpanElement = driverTools.element(INSURANCE_GROUP_NAME_SPAN, insuranceGroupElement).orElseThrow();
            String insuranceGroupNameText = insuranceGroupNameSpanElement.getText();
            if (insuranceGroupNameText.equals(insuranceGroupName)) {
                logger.log("Found specific insurance group name: " + insuranceGroupName);
                driverTools.scrollElementToCenter(insuranceGroupElement);
                return insuranceGroupElement;
            }
        }
        logger.log("Specific insurance group name '" + insuranceGroupName + "' not found in the list. Picking the first result.");
        driverTools.scrollElementToCenter(insuranceGroupElements.get(0));
        return insuranceGroupElements.get(0);
    }

    private void deleteCredential(String credentialName) {
        try {
            WebElement moreElem = driverTools.element(MORE, getSpecificCredentialElement(credentialName)).orElseThrow();
            driverTools.click(moreElem);

            WebElement deleteBtnElem = driverTools.element(DELETE_CREDENTIAL_BUTTON, driverTools.element(MENU_ACTION_LIST).orElseThrow()).orElseThrow();
            driverTools.click(deleteBtnElem);
            driverTools.waitForElementToAppear(MODAL_POP_UP);
            WebElement deleteButtonConfirm = driverTools.element(DELETE_BUTTON_IN_POP_UP, driverTools.element(MODAL_POP_UP).orElseThrow()).orElseThrow();
            driverTools.clickWithJavascriptExecutor(deleteButtonConfirm);
            driverTools.waitForElementToAppear(SUCCESS_TOAST);

            if (!driverTools.isElementDisplayed(SUCCESS_TOAST)) {
                logger.log("Delete credential failed. SUCCESS_TOAST is not displayed");
            } else {
                logger.log("Success delete credential: " + credentialName);
            }
        } catch (Exception e) {
            logger.log("Failed deleting credentials:\n" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
        }

    }

    @Getter
    @Setter
    Map<String, String> sheetData = new HashMap<>();
    @Getter
    Map<String, Object> successToastData = new HashMap<>();
    @Getter
    Map<String, String> actualCredData = new HashMap<>();
}
