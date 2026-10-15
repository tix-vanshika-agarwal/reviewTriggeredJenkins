package com.tiket.web.dashboard.page.platform.discovery;

import com.tiket.api.discovery.PopupMultiApi;
import com.tiket.api.discovery.PopupOrderApi;
import com.tiket.common.RetryUtils;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.*;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class PopupSettingPage<T extends WebDriver> extends BasePage<T> {
    public PopupSettingPage(Session<T> session) {
        super(session);
    }

    private static final String POPUP_PAGE = "/admin/home/v2";

    private static final Locator GET_IN_BUTTON = new Locator("GET_IN_BUTTON", By.xpath("//button[normalize-space()='GET IN' and contains(@class,'Button_button__')]"));
    private static final Locator LOGGED_IN_INFO = new Locator("LOGGED_IN_INFO", By.xpath("//img[@alt='logo_tiket']"));
    private static final Locator POP_UP_SETTING_PAGE_MENU = new Locator("POP_UP_SETTING_PAGE_MENU", By.xpath("//span[normalize-space()='Pop Up Setting']"));
    private static final Locator POP_UP_LIST_TAB_MENU = new Locator("POP_UP_LIST_TAB_MENU", By.xpath("//p[normalize-space()='Pop Up List']"));
    private static final Locator ADD_POP_UP_BUTTON = new Locator("ADD_POP_UP_BUTTON", By.xpath("//button[contains(@class,'button') and normalize-space()='Add Pop Up']"));
    private static final Locator NEW_POP_UP_PAGE = new Locator("NEW_POP_UP_PAGE", By.xpath("//span[normalize-space(.)='New Pop Up']"));
    private static final Locator POP_UP_NAME_INPUT = new Locator("POP_UP_NAME_INPUT", new By.ById("enter-pop-up-name"));
    private static final Locator POP_UP_TYPE_INPUT = new Locator("POP_UP_TYPE_INPUT", new By.ById("enter-pop-up-type"));
    private static final Locator POP_UP_CURRENCY_INPUT = new Locator("POP_UP_CURRENCY_INPUT", new By.ById("currency"));
    private static final Locator POP_UP_LOGIN_CHECKBOX_INPUT = new Locator("POP_UP_LOGIN_CHECKBOX_INPUT", By.xpath("//input[@name='loginStates' and @type='checkbox' and @value='LOGIN']"));
    private static final Locator POP_UP_LOGOUT_CHECKBOX_INPUT = new Locator("POP_UP_LOGOUT_CHECKBOX_INPUT", By.xpath("//input[@name='loginStates' and @type='checkbox' and @value='LOGOUT']"));
    private static final Locator POP_UP_SCREEN_NAME_OR_PAGE_LINK_INPUT = new Locator("POP_UP_SCREEN_NAME_OR_PAGE_LINK_INPUT", new By.ById("screen-name-or-pagelink"));
    private static final Locator SAVE_AND_GO_TO_CONTENT_BUTTON = new Locator("SAVE_AND_GO_TO_CONTENT_BUTTON", By.xpath("//button[normalize-space(.)='Save and go to Content']"));;
    private static final Locator IMAGE_TYPE_INPUT = new Locator("IMAGE_TYPE_INPUT", new By.ById("image-type"));;
    private static final Locator IMAGE_RATIO_INPUT = new Locator("IMAGE_RATIO_INPUT", new By.ById("image-ratio"));;
    private static final Locator CTA_IMAGE_INPUT_EN = new Locator("CTA_IMAGE_INPUT_EN", By.xpath("//div[contains(@class,'file') and contains(normalize-space(.),'EN')]//input[@type='file' and @name='iconUrl']"));;
    private static final Locator CTA_IMAGE_INPUT_ID = new Locator("CTA_IMAGE_INPUT_ID", By.xpath("//div[contains(@class,'file') and contains(normalize-space(.),'ID')]//input[@type='file' and @name='iconUrl']"));;
    private static final Locator CTA_IMAGE_URL_INPUT_EN = new Locator("CTA_IMAGE_URL_INPUT_EN", By.xpath("//input[@id='destination-url-en']"));
    private static final Locator CTA_IMAGE_URL_INPUT_ID = new Locator("CTA_IMAGE_URL_INPUT_ID", By.xpath("//input[@id='destination-url-id']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[contains(@class,'button') and normalize-space()='Submit']"));
    private static final Locator POP_UP_SETTING_BREADCRUMB = new Locator("POP_UP_SETTING_BREADCRUMB", By.xpath("//span[contains(@class,'BreadcrumbItem_text__') and text()='Pop Up Setting']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class,'button') and normalize-space()='Search']"));
    private static final Locator LOADING_ICON = new Locator("LOADING_ICON", By.xpath("//div[contains(@class,'overlay_loading')]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class,'Snackbar_text_content') and contains(normalize-space(.),'Success')]"));
    private static final Locator SEARCH_POPUP_NAME_INPUT = new Locator("SEARCH_POPUP_NAME_INPUT", new By.ById("search-pop-up-name"));;
    private static final Locator TABLE_HEADINGS = new Locator("TABLE_HEADINGS", By.xpath("//table//thead//th"));
    private static final Locator TABLE_ROWS = new Locator("TABLE_ROWS", By.xpath("//table//tbody//tr"));
    private static final Locator ARCHIVE_POPUP_MODAL_HEADING = new Locator("ARCHIVE_POPUP_MODAL_HEADING", By.xpath("//div[contains(@class,'modal')]//h2[contains(normalize-space(.),'Archive')]"));
    private static final Locator ARCHIVE_POPUP_MODAL_BUTTON = new Locator("ARCHIVE_POPUP_MODAL_BUTTON", By.xpath("//div[contains(@class,'modal')]//button[contains(normalize-space(.),'Archive')]"));
    private static final Locator ADD_CONDITION_BUTTON = new Locator("ADD_CONDITION_BUTTON", By.xpath("//button[contains(@class,'condition_button_add_condition')]"));
    private static final Locator CONDITION_TYPE_DROPDOWN_ICON = new Locator("CONDITION_TYPE_DROPDOWN_ICON", By.xpath("//div[contains(@class,'condition_wrap_condition')]//div//div[contains(@class,'DropdownFormDisplay_icon_container')]//*[name()='svg']"));
    private static final Locator CONDITION_TYPE_UTM_OPTION = new Locator("CONDITION_TYPE_UTM_OPTION", By.xpath("//div[contains(@class,'BaseDropdown_base_dropdown')]//div[4]"));
    private static final Locator ADD_AND_CRITERIA_BUTTON = new Locator("ADD_AND_CRITERIA_BUTTON", By.xpath("//span[normalize-space()='AND']"));
    private static final Locator CONDITION_KEY_OPTION = new Locator("CONDITION_KEY_OPTION", By.xpath("//div[@data-testid='option-single-3']"));
    private static final Locator ADD_OR_VALUE_BUTTON = new Locator("ADD_OR_VALUE_BUTTON", By.xpath("//div[contains(@class,'Tabs_tab_panel_inner')]//div[1]//div[1]//button[1]//span[1]"));
    private static final Locator REMOVE_BUTTON_SECOND_CONDITION = new Locator("REMOVE_BUTTON_SECOND_CONDITION", By.xpath("((//div[contains(@class,'condition_container_criteria')])[2]//*[local-name()='svg' and .//*[local-name()='use']])[last()]"));
    private static final Locator FLOATING_POSITION_BOTTOM_RADIO = new Locator("FLOATING_POSITION_BOTTOM_RADIO", By.xpath("//label[contains(@class,'Radio_radio')]//span[contains(@class,'Radio_children')]//p[contains(text(),'Bottom')]"));
    private static final Locator FLOATING_ICON_UPLOAD_INPUT = new Locator("FLOATING_ICON_UPLOAD_INPUT", By.xpath("//div[contains(@class,'fileupload_icon_container')]/input[@type='file' and @name='icon']"));
    private static final Locator FLOATING_TITLE_INPUT_EN = new Locator("FLOATING_TITLE_INPUT_EN", By.xpath("//div[contains(@aria-label,'Rich Text Editor')]//p[@data-placeholder='Placeholder (EN)']"));
    private static final Locator FLOATING_TITLE_INPUT_ID = new Locator("FLOATING_TITLE_INPUT_ID", By.xpath("//div[contains(@aria-label,'Rich Text Editor')]//p[@data-placeholder='Placeholder (ID)']"));
    private static final Locator FLOATING_CTA_BUTTON_TEXT_INPUT_EN = new Locator("FLOATING_CTA_BUTTON_TEXT_INPUT_EN", By.xpath("//div[contains(@class,'styles_input_inner_wrapper')]/input[contains(@placeholder,'CTA Button (EN)')]"));
    private static final Locator FLOATING_CTA_BUTTON_TEXT_INPUT_ID = new Locator("FLOATING_CTA_BUTTON_TEXT_INPUT_ID", By.xpath("//div[contains(@class,'styles_input_inner_wrapper')]/input[contains(@placeholder,'CTA Button (ID)')]"));
    private static final Locator FLOATING_CTA_URL_INPUT_EN = new Locator("FLOATING_CTA_URL_INPUT_EN", By.xpath("//div[contains(@class,'styles_input_inner_wrapper')]/input[contains(@placeholder,'URL (EN)')]"));
    private static final Locator FLOATING_CTA_URL_INPUT_ID = new Locator("FLOATING_CTA_URL_INPUT_ID", By.xpath("//div[contains(@class,'styles_input_inner_wrapper')]/input[contains(@placeholder,'URL (ID)')]"));
    private static final Locator SET_ORDER_PRIORITY_BUTTON = new Locator("SET_ORDER_PRIORITY_BUTTON", By.xpath("//button[contains(@class,'Button_accessibility_button')]/span[contains(text(),'Set Order & Priority')]"));
    private static final Locator POPUP_POSITION_SETTING_TITLE = new Locator("POPUP_POSITION_SETTING_TITLE", By.xpath("//p[contains(@class,'Text_size_h3__') and normalize-space()='Pop Up Order & Priority']"));
    private static final Locator PRIORITY_DROPDOWN = new Locator("PRIORITY_DROPDOWN", By.xpath("//div[contains(@class,'DropdownFormDisplay_dropdown')]"));
    private static Locator getConditionCriteriaDropdownLocator(int index) {
        return new Locator("CONDITION_CRITERIA_DROPDOWN_" + index, By.xpath("(//div[contains(@class,'styles_container')]//input[@id='condition-criteria'])[%d]".formatted(index)));
    }

    private static Locator getConditionValueInputLocator(int rowIndex) {
        return new Locator("CONDITION_VALUE_INPUT_ROW_" + rowIndex, By.xpath("(//input[contains(@class,'styles_input') and @id='value'])[%d]".formatted(rowIndex)));
    }

    private static Locator getEditGeneralButtonLocator(int index) {
        return new Locator("EDIT_GENERAL_BUTTON", By.xpath("(//table//tbody//tr//td[%d]//div[contains(@class,'tooltip')]//*[name()='svg'])[1]".formatted(index)));
    }
    private static Locator getArchiveButtonLocator(int index) {
        return new Locator("ARCHIVE_BUTTON", By.xpath("(//table//tbody//tr//td[%d]//div[contains(@class,'tooltip')]//*[name()='svg'])[4]".formatted(index)));
    }

    private static Locator getTableColumnLocator(int index){
        return new Locator("TABLE_COLUMN_" + index, By.xpath("//table//tbody//tr//td[%d]".formatted(index)));
    }

    private static Locator getDropdownOptionByName(String name) {
        return new Locator("OPTION_" + name, By.xpath("//div[contains(@class,'dropdown')]//div[contains(@class,'option') and normalize-space(.)='%s']".formatted(name)));
    }
    private static Locator getPlatformOptionByName(String name) {
        return new Locator("POP_UP_PLATFORMS_%s_CHECKBOX_INPUT".formatted(name), By.xpath("//input[@name='platforms' and @type='checkbox' and @value='%s']".formatted(name)));
    }
    private static Locator getTopPriorityDropdownOptionByPopupName(String popupName) {
        return new Locator("TOP_PRIORITY_DROPDOWN_OPTION_" + popupName, By.xpath("//div[@data-testid='option-single-0']/p[contains(text(),'%s')]".formatted(popupName)));
    }

    public boolean isNewPopupPageVisible(){
        return driverTools.waitForElementsToAppear(NEW_POP_UP_PAGE).isPresent();
    }

    public void setPopupName(String popupName) {
        WebElement input = driverTools.waitForElementToBeClickable(POP_UP_NAME_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, popupName);
    }
    public void setPopUpTypeInput(String popupType) {
        driverTools.waitForElementToPresence(POP_UP_TYPE_INPUT);
        try {
            driverTools.click(POP_UP_TYPE_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(POP_UP_TYPE_INPUT);
        }
        Locator dropdownOptionLocator = getDropdownOptionByName(popupType);
        driverTools.waitForElementToAppear(dropdownOptionLocator);
        driverTools.clickWithJavascriptExecutor(dropdownOptionLocator);
    }
    public void setPopUpCurrencyInput(String popUpCurrency) {
        driverTools.waitForElementToAppear(POP_UP_CURRENCY_INPUT);
        try {
            driverTools.click(POP_UP_CURRENCY_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(POP_UP_CURRENCY_INPUT);
        }
        Locator dropdownOptionLocator = getDropdownOptionByName(popUpCurrency);
        driverTools.waitForElementToAppear(dropdownOptionLocator);
        driverTools.clickWithJavascriptExecutor(dropdownOptionLocator);
    }
    public void selectPopupShowLoginCheckbox() {
        driverTools.waitForElementToPresence(POP_UP_LOGIN_CHECKBOX_INPUT);
        try {
            driverTools.click(POP_UP_LOGIN_CHECKBOX_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(POP_UP_LOGIN_CHECKBOX_INPUT);
        }
    }
    public void selectPopupShowLogoutCheckbox() {
        driverTools.waitForElementToPresence(POP_UP_LOGOUT_CHECKBOX_INPUT);
        try {
            driverTools.click(POP_UP_LOGOUT_CHECKBOX_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(POP_UP_LOGOUT_CHECKBOX_INPUT);
        }
    }
    public void selectPopupAvailablePlatformOptionCheckbox(String option) {
        Locator platformOptionLocator = getPlatformOptionByName(option);
        driverTools.waitForElementToPresence(platformOptionLocator);
        try {
            driverTools.click(platformOptionLocator);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(platformOptionLocator);
        }
    }
    public void selectPopupScreenNameOrPageLinkInput(String input) {
        WebElement inputElement = driverTools.waitForElementToAppear(POP_UP_SCREEN_NAME_OR_PAGE_LINK_INPUT).orElseThrow();
        driverTools.sendKeys(inputElement, input + Keys.ENTER);
    }

    public void clickSaveAndGoToContentButton(){
        try {
            driverTools.scrollAndClick(SAVE_AND_GO_TO_CONTENT_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(SAVE_AND_GO_TO_CONTENT_BUTTON);
        }
    }
    public void selectImageTypeInput(String option) {
        driverTools.waitForElementToAppear(IMAGE_TYPE_INPUT);
        try {
            driverTools.click(IMAGE_TYPE_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(IMAGE_TYPE_INPUT);
        }
        Locator dropdownOptionLocator = getDropdownOptionByName(option);
        driverTools.waitForElementToAppear(dropdownOptionLocator);
        driverTools.clickWithJavascriptExecutor(dropdownOptionLocator);
    }
    public void selectImageRatioInput(String option) {
        driverTools.waitForElementToAppear(IMAGE_RATIO_INPUT);
        try {
            driverTools.click(IMAGE_RATIO_INPUT);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(IMAGE_RATIO_INPUT);
        }
        Locator dropdownOptionLocator = getDropdownOptionByName(option);
        driverTools.waitForElementToAppear(dropdownOptionLocator);
        driverTools.clickWithJavascriptExecutor(dropdownOptionLocator);
    }

    public void uploadCTAImage(String file) {
        String filePath = new File(file).getAbsolutePath();
        driverTools.scrollIntoWebView(CTA_IMAGE_INPUT_EN);
        for (Object[] upload : new Object[][]{
                {CTA_IMAGE_INPUT_EN, filePath},
                {CTA_IMAGE_INPUT_ID, filePath},
        }) {
            Locator input = (Locator) upload[0];
            WebElement inputElement = driverTools.waitForElementToPresence(input, 10).orElseThrow();
            driverTools.scrollIntoWebView(inputElement);
            driverTools.sendKeys(inputElement, (String) upload[1]);
            driverTools.waitForElementToAppear(LOADING_ICON, 20);
            driverTools.waitForElementToDisappear(LOADING_ICON, 20);
        }
    }
    public void setCTAImageUrl(String url) {
        driverTools.scrollIntoWebView(CTA_IMAGE_URL_INPUT_EN);
        for (Object[] upload : new Object[][]{
                {CTA_IMAGE_URL_INPUT_EN, url},
                {CTA_IMAGE_URL_INPUT_ID, url},
        }) {
            Locator input = (Locator) upload[0];
            WebElement inputElement = driverTools.waitForElementToPresence(input, 10).orElseThrow();
            driverTools.scrollIntoWebView(inputElement);
            driverTools.clearInput(inputElement);
            driverTools.sendKeys(inputElement, (String) upload[1]);
        }
    }

    public void selectFloatingPositionBottom() {
        driverTools.waitForElementToAppear(FLOATING_POSITION_BOTTOM_RADIO);
        try {
            driverTools.click(FLOATING_POSITION_BOTTOM_RADIO);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(FLOATING_POSITION_BOTTOM_RADIO);
        }
    }

    public void uploadFloatingIcon(String file) {
        String filePath = new File(file).getAbsolutePath();
        WebElement inputElement = driverTools.waitForElementToPresence(FLOATING_ICON_UPLOAD_INPUT, 10).orElseThrow();
        driverTools.scrollIntoWebView(inputElement);
        driverTools.sendKeys(inputElement, filePath);
        driverTools.waitForElementToAppear(LOADING_ICON, 20);
        driverTools.waitForElementToDisappear(LOADING_ICON, 20);
    }

    public void setFloatingTitle(String title) {
        for (Locator input : new Locator[]{FLOATING_TITLE_INPUT_EN, FLOATING_TITLE_INPUT_ID}) {
            WebElement inputElement = driverTools.waitForElementToBeClickable(input).orElseThrow();
            driverTools.scrollIntoWebView(inputElement);
            driverTools.click(inputElement);
            driverTools.sendKeys(inputElement, title);
        }
    }

    public void setFloatingCTAButtonText(String text) {
        for (Locator input : new Locator[]{FLOATING_CTA_BUTTON_TEXT_INPUT_EN, FLOATING_CTA_BUTTON_TEXT_INPUT_ID}) {
            WebElement inputElement = driverTools.waitForElementToBeClickable(input).orElseThrow();
            driverTools.scrollIntoWebView(inputElement);
            driverTools.clearInput(inputElement);
            driverTools.sendKeys(inputElement, text);
        }
    }

    public void setFloatingCTAButtonUrl(String url) {
        for (Locator input : new Locator[]{FLOATING_CTA_URL_INPUT_EN, FLOATING_CTA_URL_INPUT_ID}) {
            WebElement inputElement = driverTools.waitForElementToPresence(input, 10).orElseThrow();
            driverTools.scrollIntoWebView(inputElement);
            driverTools.clearInput(inputElement);
            driverTools.sendKeys(inputElement, url);
        }
    }

    public void clickSetOrderPriorityButton() {
        driverTools.waitForElementToAppear(SET_ORDER_PRIORITY_BUTTON);
        try {
            driverTools.scrollAndClick(SET_ORDER_PRIORITY_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(SET_ORDER_PRIORITY_BUTTON);
        }
    }

    public boolean isPopupPositionSettingTitleVisible() {
        return driverTools.waitForElementsToAppear(POPUP_POSITION_SETTING_TITLE).isPresent();
    }

    public void clickPriorityDropdown() {
        driverTools.waitForElementToAppear(PRIORITY_DROPDOWN);
        try {
            driverTools.click(PRIORITY_DROPDOWN);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(PRIORITY_DROPDOWN);
        }
    }

    public boolean isTopPriorityDropdownOptionEqualToPopupName(String popupName) {
        Locator topOptionLocator = getTopPriorityDropdownOptionByPopupName(popupName);
        return driverTools.waitForElementsToAppear(topOptionLocator, 3).isPresent();
    }

    public boolean isSnackbarSuccessAppear() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
        return driverTools.getText(SUCCESS_SNACKBAR).isPresent();
    }

    public void setSearchPopupName(String popupName) {
        WebElement inputElement = driverTools.waitForElementToBeClickable(SEARCH_POPUP_NAME_INPUT).orElseThrow();
        try {
            driverTools.clearInput(inputElement);
            driverTools.sendKeys(inputElement, popupName);
        } catch (Exception e) {
            driverTools.sendKeysWithJsExecutorForceInputValueAndTriggerEvents(inputElement, popupName);
        }
    }

    public void goToHomePage(String username, String password) {
        MemberLoginPage<?> memberLoginPage = new MemberLoginPage<>(session);
        memberLoginPage.adminLoginViaCookie(username, password);
        memberLoginPage.goToPage(POPUP_PAGE);
        if (!driverTools.getCurrentURL().contains(POPUP_PAGE)) {
            memberLoginPage.goToPage(POPUP_PAGE);
        }
    }

    public boolean isHomePageLoaded() {
        driverTools.waitForElementToAppear(LOGGED_IN_INFO, 2);
        return driverTools.isElementDisplayed(LOGGED_IN_INFO);
    }

    public void goBackToPopupList() {
        driverTools.waitForElementToAppear(POP_UP_SETTING_BREADCRUMB, 5);
        driverTools.scrollIntoWebViewOptional(POP_UP_SETTING_BREADCRUMB);
        driverTools.click(POP_UP_SETTING_BREADCRUMB);
        driverTools.waitForElementToAppear(POP_UP_LIST_TAB_MENU, 5);
    }

    public void clickGetInButton() {
        // Already logged in with the same session: GET IN prompt won't appear, so skip it.
        if (driverTools.waitForElementToAppear(GET_IN_BUTTON, 2).isEmpty()) {
            return;
        }
        if (driverTools.waitForElementToBeClickable(GET_IN_BUTTON).isEmpty()) {
            throw new NoSuchElementException("GET IN button was not found or is not clickable. Current URL: " + driverTools.getCurrentURL());
        }
        try {
            driverTools.click(GET_IN_BUTTON);
        } catch (Exception e) {
            try {
                driverTools.clickWithJavascriptExecutor(GET_IN_BUTTON);
            } catch (Exception jsException) {
                throw new IllegalStateException("Unable to click GET IN button. Current URL: " + driverTools.getCurrentURL(), jsException);
            }
        }
    }

    public void clickPopupSettingPageSideMenu() {
        driverTools.scrollAndClick(POP_UP_SETTING_PAGE_MENU);
    }

    public boolean isPopupSettingPageLoaded() {
        driverTools.waitForElementToAppear(POP_UP_LIST_TAB_MENU, 15);
        return driverTools.isElementDisplayed(POP_UP_LIST_TAB_MENU);
    }

    public void clickAddPopupButton() {
        driverTools.waitForElementToAppear(ADD_POP_UP_BUTTON);
        try {
            driverTools.scrollAndClick(ADD_POP_UP_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(ADD_POP_UP_BUTTON);
        }
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 10);
        driverTools.scrollIntoWebView(SUBMIT_BUTTON);
        try {
            driverTools.click(SUBMIT_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
        }
    }
    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BUTTON, 10);
        driverTools.scrollIntoWebView(SEARCH_BUTTON);
        try {
            driverTools.click(SEARCH_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(SEARCH_BUTTON);
        }
        driverTools.waitForElementToAppear(LOADING_ICON);
        driverTools.waitForElementToDisappear(LOADING_ICON);
    }

    public int getTableHeadingIndex(String heading) {
        driverTools.waitForElementToAppear(TABLE_HEADINGS);
        List<WebElement> headings = driverTools.elements(TABLE_HEADINGS).orElseThrow(() -> new IllegalStateException("No headings found"));
        if (headings.isEmpty()) {
            return -1;
        }

        for (int i = 0; i < headings.size(); i++) {
            String text = headings.get(i).getText().trim();
            if (text.isEmpty()) {
                text = headings.get(i).getAttribute("textContent").trim();
            }
            if (text.equalsIgnoreCase(heading)) {
                return i;
            }
        }

        return -1;
    }
    public String getTableValueByHeading(String heading) {
        int index = getTableHeadingIndex(heading);
        if (index == -1) {
            logger.log("Heading: %s not found returning empty value".formatted(heading));
            return "";
        }

        Locator COLUMN_VALUES = getTableColumnLocator(index + 1);
        driverTools.waitForElementToAppear(COLUMN_VALUES);
        int retry = 5;
        while (retry-- > 0 && driverTools.getAttributeTextContent(COLUMN_VALUES).orElseThrow().trim().isEmpty()){
            wait(1);
        }

        String text =  driverTools.getText(COLUMN_VALUES).orElseThrow().trim();
        if (text.isEmpty()) {
            text = driverTools.getAttributeTextContent(COLUMN_VALUES).orElseThrow().trim();
        }

        logger.log("Got (%s) value for %s".formatted(text, heading));
        return text;
    }

    public void clickEditGeneralButton() {
        int index = getTableHeadingIndex("Actions");
        if (index == -1) {
            throw new IllegalStateException("Heading 'Actions' not found in popup list table");
        }
        Locator editGeneralButtonLocator = getEditGeneralButtonLocator(index + 1);
        WebElement element = driverTools.waitForElementToAppear(editGeneralButtonLocator).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(element);
    }
    public void clickArchiveButton() {
        int index = getTableHeadingIndex("Actions");
        if (index == -1) {
            throw new IllegalStateException("Heading 'Actions' not found in popup list table");
        }
        Locator archiveButtonLocator = getArchiveButtonLocator(index + 1);
        WebElement element = driverTools.waitForElementToAppear(archiveButtonLocator).orElseThrow();
        driverTools.clickSvgOrNonHtmlElements(element);
    }

    public boolean isArchivePopupAppear(){
        return driverTools.waitForElementsToAppear(ARCHIVE_POPUP_MODAL_HEADING).isPresent();
    }
    public boolean isArchivePopupButtonClickable(){
        return driverTools.waitForElementToBeClickable(ARCHIVE_POPUP_MODAL_BUTTON).isPresent();
    }
    public void clickArchivePopupModalButton(){
        try {
            driverTools.click(ARCHIVE_POPUP_MODAL_BUTTON);
        }  catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(ARCHIVE_POPUP_MODAL_BUTTON);
        }
    }

    public void wait(int seconds) {
        driverTools.dummyWait(seconds);
    }

    public boolean isTableDataAppear() {
        return driverTools.waitForElementsToAppear(TABLE_ROWS).isPresent();
    }
    public boolean isTableDataNotAppear() {
        return !isTableDataAppear();
    }

    public void clickAddConditionButton() {
        driverTools.waitForElementToAppear(ADD_CONDITION_BUTTON);
        try {
            driverTools.scrollAndClick(ADD_CONDITION_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(ADD_CONDITION_BUTTON);
        }
    }

    public void selectConditionTypeUtmTrafficSource() {
        driverTools.waitForElementToAppear(CONDITION_TYPE_DROPDOWN_ICON);
        try {
            driverTools.click(CONDITION_TYPE_DROPDOWN_ICON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(CONDITION_TYPE_DROPDOWN_ICON);
        }
        driverTools.waitForElementToAppear(CONDITION_TYPE_UTM_OPTION);
        driverTools.clickWithJavascriptExecutor(CONDITION_TYPE_UTM_OPTION);
    }

    public void clickAddAndCriteriaButton() {
        driverTools.waitForElementToAppear(ADD_AND_CRITERIA_BUTTON);
        try {
            driverTools.scrollAndClick(ADD_AND_CRITERIA_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(ADD_AND_CRITERIA_BUTTON);
        }
    }

    public void selectConditionCriteriaKey(int index) {
        Locator conditionCriteriaDropdownLocator = getConditionCriteriaDropdownLocator(index);
        driverTools.waitForElementToAppear(conditionCriteriaDropdownLocator);
        try {
            driverTools.click(conditionCriteriaDropdownLocator);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(conditionCriteriaDropdownLocator);
        }
        driverTools.waitForElementToAppear(CONDITION_KEY_OPTION);
        driverTools.clickWithJavascriptExecutor(CONDITION_KEY_OPTION);
    }

    public void clickAddOrValueButton() {
        driverTools.waitForElementToAppear(ADD_OR_VALUE_BUTTON);
        try {
            driverTools.scrollAndClick(ADD_OR_VALUE_BUTTON);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(ADD_OR_VALUE_BUTTON);
        }
    }

    public void setConditionValue(int rowIndex, String value) {
        Locator inputLocator = getConditionValueInputLocator(rowIndex);
        WebElement inputElement = driverTools.waitForElementToBeClickable(inputLocator).orElseThrow();
        driverTools.scrollIntoWebView(inputElement);
        driverTools.sendKeys(inputElement, value);
    }

    public void removeSecondConditionValue() {
        driverTools.click(REMOVE_BUTTON_SECOND_CONDITION);
    }

    public String getConditionValue(int rowIndex) {
        Locator inputLocator = getConditionValueInputLocator(rowIndex);
        WebElement inputElement = driverTools.waitForElementToAppear(inputLocator).orElseThrow();
        return driverTools.getAttribute(inputElement, "value");
    }

    public String  getPopupIdFromUrl() {
        String currentUrl = driverTools.getCurrentURL();
        String[] segments = currentUrl.split("/");
        String popupId = segments[segments.length - 2];
        logger.log("Extracted popup id: %s from URL: %s".formatted(popupId, currentUrl));
        return popupId;
    }

    public boolean isPopupVisibleForEndUser(String popupId, String utmSource, String screenName, String url) {
        String sessionAccessToken = driverTools.getCookieValue("session_access_token");
        PopupMultiApi.Inputs input = new PopupMultiApi.Inputs(utmSource, screenName, url, sessionAccessToken);
        return RetryUtils.retryUntil(
                6,
                3000,
                () -> {
                    ApiResponse response = new PopupMultiApi(input).hitApi(apiTools);
                    JSONArray popups = new JSONObject(response.getBody()).getJSONObject("data").getJSONArray("popups");
                    for (int i = 0; i < popups.length(); i++) {
                        if (popups.getJSONObject(i).getString("popupId").equals(popupId)) {
                            return true;
                        }
                    }
                    return false;
                },
                visible -> visible,
                "Verify popup %s visible for end user (awaiting popup order/cache propagation)".formatted(popupId));
    }

    private String getBrowserCookieHeader() {
        StringBuilder cookieHeader = new StringBuilder();
        for (Cookie cookie : driver.manage().getCookies()) {
            if (!cookieHeader.isEmpty()) {
                cookieHeader.append("; ");
            }
            cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
        }
        return cookieHeader.toString();
    }

    public void setPopupOrderFirst(String popupId, String orderGroupId) {
        PopupOrderApi.Inputs input = new PopupOrderApi.Inputs(getBrowserCookieHeader(), popupId, orderGroupId);
        ApiResponse response = new PopupOrderApi(input).hitApi(apiTools);
        logger.log("Set popup order response: " + response.getBody());
        if (response.getStatusCode() < 200 || response.getStatusCode() >= 300) {
            throw new IllegalStateException("Set popup order failed: status=" + response.getStatusCode() + ", body=" + response.getBody());
        }
    }
}
