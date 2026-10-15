package com.tiket.web.dashboard.page.payment;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.tiket.api.payment.PaymentGroupApi;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Deeplink;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.payment.PaymentGroupResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;

public class PaymentGroupingPage <T extends WebDriver> extends BasePage<T> {

    private static final Locator PAYMENT_BAR = new Locator("PAYMENT_BAR",By.xpath("//a[contains(@id,'Link-2')]"));
    private static final Locator PAYMENT_GROUPING = new Locator("PAYMENT_GROUPING",By.xpath("//a[contains(@href,'payment-groupin')]"));
    private static final Locator FERRY = new Locator("FERRY",By.xpath("//a[contains(text(),'FERRY')]"));
    private static final Locator CAR_RENTAL = new Locator("CAR_RENTAL",By.xpath("//a[contains(text(),'CAR')]"));
    private static final Locator FERRY_ADD_NEW = new Locator("FERRY_ADD_NEW",By.xpath("//div[contains(@class,'recommendation')]//button[text()='Add new']"));
    private static final Locator SAVE_ADD_NEW = new Locator("SAVE_ADD_NEW",By.xpath("//button[contains(text(),'Save')]"));
    private static final Locator ADD_PAYMENT_METHOD = new Locator("ADD_PAYMENT_METHOD",By.xpath("//div[contains(@class,'container form')]"));
    private static final Locator TITLE_INPUTS = new Locator("TITLE_INPUTS", By.xpath("//input[contains(@class , 'form-control')]"));    
    private static final Locator SUBTITLE_INPUTS = new Locator("SUBTITLE_INPUTS", By.xpath("//textarea[contains(@class , 'form')]"));
    private static final Locator COLOR_INPUTS = new Locator("COLOR_INPUTS", By.xpath("//div[contains(@class,'color')]//input[@value='ffffff']"));  
    private static final Locator PAYMENT_RECOMMEND = new Locator("PAYMENT_RECOMMEND", By.xpath("//div[contains(@class,'recommendation')]//div[contains(@class,'carina')]")); 
    private static final Locator PAYMENT_RECOMMEND_TRASH = new Locator("PAYMENT_RECOMMEND_TRASH", By.xpath("//div[contains(@class,'recommendation')]//span[contains(@class,'trash')]"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", By.xpath("//button[contains(text(),'Yes')]"));
    private static final Locator SAVE_OK = new Locator("SAVE_OK", By.xpath("//button[contains(text(),'OK')]")); 
    private static final Locator OVERRIDE_SWITCH = new Locator("OVERRIDE_SWITCH", By.xpath("//div[contains(@class,'switch')]")); 
    private static final Locator PAYMENT_RECOMMED_DROPDOWN = new Locator("PAYMENT_RECOMMED_DROPDOWN", By.xpath("//div[contains(@class,'recommendation')]//div[contains(@class,'duplicate')]//div[contains(@class,'container')]"));
    private static final Locator NO_BUTTON = new Locator("NO_BUTTON", By.xpath("//button[contains(text(),'No')]"));
    private static final Locator CANCEL_BUTTON = new Locator("CANCEL_BUTTON", By.xpath("//button[contains(text(),'Cancel')]"));
    private static final Locator FIRST_PAYMENT_RECOMMEND = new Locator("FIRST_PAYMENT_RECOMMEND", By.xpath("//div[contains(@class,'menu')]//div[1]"));
    public static final Locator PAYMENT_METHOD_CONFIG_BUTTON = new Locator("PAYMENT_METHOD_CONFIG_BUTTON", By.xpath("//div[@role='button']//span[normalize-space()='Payment Method Master Configuration']"));
    private static final Locator PAYMENT_METHOD_MASTER_SIDEMENU = new Locator("PAYMENT_METHOD_MASTER_SIDEMENU", By.xpath("//*[@data-testid='sidemenu']//*[contains(text(),'Payment Method Master')]"));
    public static final Locator PAYMENT_GROUP_DISPLAY = new Locator("PAYMENT_GROUP_DISPLAY", By.xpath("//*[contains(@data-testid,'submenu')]//*[contains(text(),'Payment Group Display')]"));
    private static final Locator PAYMENT_GROUP_DISPLAY_LINK = new Locator("PAYMENT_GROUP_DISPLAY_LINK", By.xpath("//a[contains(@href,'payment-group-display')]"));
    private static final String PAYMENT_GROUP_DISPLAY_PATH = "/admin/payment_v2/payment-method-master-config/payment-group-display";
    public static final Locator ADD_GROUP_BUTTON = new Locator("ADD_GROUP_BUTTON", By.xpath("//button[normalize-space()='Add Group']"));
    public static final Locator GROUP_TITLE_ENGLISH = new Locator("GROUP_TITLE_ENGLISH", By.id("group-title-in-english"));
    public static final Locator GROUP_TITLE_BAHASA = new Locator("GROUP_TITLE_BAHASA", By.id("group-title-in-bahasa-indonesia"));
    public static final Locator GROUP_SUBTITLE_ENGLISH = new Locator("GROUP_SUBTITLE_ENGLISH", By.id("group-subtitle-in-english"));
    public static final Locator GROUP_SUBTITLE_BAHASA = new Locator("GROUP_SUBTITLE_BAHASA", By.id("group-subtitle-in-bahasa-indonesia"));
    public static final Locator PAYMENT_METHOD_DROPDOWN_INPUT = new Locator("PAYMENT_METHOD_DROPDOWN_INPUT", By.xpath("(//div[@class='XmDoza_inline_input_wrapper']//button)[last()]"));
    public static final Locator PAYMENT_METHOD_FIRST_OPTION = new Locator("PAYMENT_METHOD_FIRST_OPTION", By.xpath("(//div[contains(@class,'styles_dropdown_option__hC7A5')])[1]"));
    public static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", By.xpath("//button[contains(@class,'KV-nSG_button') and contains(@class,'KV-nSG_mode_inverted')]"));
    public static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[@data-testid='btn-save']"));
    private static final Locator DISPLAY_FORM_CANCEL = new Locator("DISPLAY_FORM_CANCEL", By.xpath("//button[@data-testid='btn-save']/preceding-sibling::button[normalize-space()='Cancel']"));
    public static final Locator DELETE_GROUP = new Locator("DELETE_GROUP", By.xpath("(//*[name()='use' and @fill='red']/ancestor::*[name()='svg'])[last()]"));
    public static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//button[normalize-space()='Submit']"));
    public static final Locator EDIT_LAST = new Locator("EDIT_LAST", By.xpath("(//p[normalize-space()='Edit'])[last()]"));
    public static final Locator BLOCKS = new Locator("BLOCKS", By.xpath("(//span[normalize-space(text())='Payment Method']/following-sibling::div[contains(@class,'bg-[#F4F7FE]') and contains(@class,'rounded-lg')])"));
    private static final Locator DISPLAY_FIRST_PAYMENT_METHOD_OPTION = new Locator("DISPLAY_FIRST_PAYMENT_METHOD_OPTION", By.xpath("(//div[contains(@class,'styles_dropdown_option')])[1]"));

    private static final Gson gson = new Gson();
    private static final String DEFAULT_PLATFORM = "ANDROID";
    private static final long CLIENT_API_DELAY_MS = 3000;
    private static final String DISPLAY_RECOMMENDATION_CARD_XPATH = "//h1[normalize-space(text())='Payment Recommendation']/ancestor::div[contains(@class,'card_container')][1]";
    private static final String DISPLAY_PAYMENT_GROUP_CARD_XPATH = "//h1[normalize-space(text())='Payment Group']/ancestor::div[contains(@class,'card_container')][1]";

    private static final Locator DISPLAY_PAGE_TITLE = new Locator("DISPLAY_PAGE_TITLE", By.xpath("//h1[contains(@class,'styles_page_title') and normalize-space(text())='Payment Grouping']"));
    private static final Locator DISPLAY_RECOMMENDATION_SECTION = new Locator("DISPLAY_RECOMMENDATION_SECTION", By.xpath(DISPLAY_RECOMMENDATION_CARD_XPATH));
    private static final Locator DISPLAY_PAYMENT_GROUP_SECTION = new Locator("DISPLAY_PAYMENT_GROUP_SECTION", By.xpath(DISPLAY_PAYMENT_GROUP_CARD_XPATH));
    private static final Locator DISPLAY_SAVE_CHANGES = new Locator("DISPLAY_SAVE_CHANGES", By.xpath("//button[normalize-space()='Save Changes']"));
    private static final Locator DISPLAY_SAVE_CONFIRMATION_MODAL = new Locator("DISPLAY_SAVE_CONFIRMATION_MODAL", By.xpath("//section[contains(@class,'modal_popup')]//h2[normalize-space()='Save Confirmation']"));
    private static final Locator DISPLAY_ADD_GROUP_BUTTON = new Locator("DISPLAY_ADD_GROUP_BUTTON", By.xpath("//h1[normalize-space(text())='Payment Group']/ancestor::div[contains(@class,'card_container')][1]//button[contains(@class,'KV-nSG_variant_primary') and contains(normalize-space(.),'Add Group')]"));
    private static final Locator DISPLAY_DELETE_GROUP_METHOD = new Locator("DISPLAY_DELETE_GROUP_METHOD", By.xpath("//div[contains(@class,'bg-[#F4F7FE]') and contains(@class,'rounded-lg')]//*[local-name()='use' and @fill='red']/ancestor::*[local-name()='svg']"));

    private Locator GETPAYMENT(String str) {
        return new Locator("GETPAYMENT", new By.ByXPath(String.format("//div[contains(@class,'menu')]//div[text()='%s']", str))); 
    };
    
    public PaymentGroupingPage(Session<T> session) {
        super(session);
    }

    public void clickYes(){
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void clickFerryVertical(){
        driverTools.waitForElementToAppear(FERRY, 10).ifPresent(driverTools::click);
    }

    public void clickCarRentalVertical(){
        driverTools.waitForElementToAppear(CAR_RENTAL, 10).ifPresent(driverTools::click);
    }

    public void clickOnAdd(){
        driverTools.waitForElementToAppear(FERRY_ADD_NEW, 10).ifPresent(driverTools::click);
    }
    
    public void clickAddNewSaveButton(){
        driverTools.waitForElementToAppear(SAVE_ADD_NEW, 10).ifPresent(driverTools::click);
    }

    public void clickAddNewPaymentMethod(){
        driverTools.waitForElementToAppear(ADD_PAYMENT_METHOD, 10).ifPresent(driverTools::click);
    }

    public void ClickOnAddPaymentMethod(String entry){
        driverTools.waitForElementToAppear(GETPAYMENT(entry), 10).ifPresent(driverTools::click);
    }

    public void clickOK(){
        driverTools.waitForElementToAppear(SAVE_OK, 10).ifPresent(driverTools::click);
    }

    public void clickTrashButton(){
        List<WebElement> trashButtons = driverTools.elements(PAYMENT_RECOMMEND_TRASH).orElseThrow();
        WebElement trashButton = trashButtons.get(trashButtons.size()-1);
        driverTools.click(trashButton);
    }

    public void clickOverrideSwitch(){
        List<WebElement> overrideSwitches = driverTools.elements(OVERRIDE_SWITCH).orElseThrow();
        WebElement overrideSwitch = overrideSwitches.get(overrideSwitches.size()-1);
        driverTools.click(overrideSwitch);
    }

    public int deleteThePaymentRecommendAndReduceCount(int paymentCount) throws InterruptedException{
        if(paymentCount >= 3){
            clickTrashButton();
            clickYes();
            paymentCount = paymentCount-1;
        }
        return paymentCount;
    }
    
    public void clickOnPaymentGrouping(){
        driverTools.waitForElementToAppear(PAYMENT_BAR, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PAYMENT_GROUPING, 10).ifPresent(driverTools::click);
    }

    public void inputTitle(String en , String id){
        List<WebElement> inputTitleInputs = driverTools.elements(TITLE_INPUTS).orElseThrow();
        WebElement inputTitleInput1 = inputTitleInputs.get(0);
        WebElement inputTitleInput2 = inputTitleInputs.get(1);
        driverTools.scrollIntoWebView(inputTitleInput1);
        driverTools.sendKeys(inputTitleInput1, en);
        driverTools.sendKeys(inputTitleInput2, id);
    }

    public void inputSubTitle(String en , String id){
        List<WebElement> inputSubTitleInputs = driverTools.elements(SUBTITLE_INPUTS).orElseThrow();
        WebElement eleinputSubTitleInput1 = inputSubTitleInputs.get(0);
        WebElement eleinputSubTitleInput2 = inputSubTitleInputs.get(1);
        driverTools.scrollIntoWebView(eleinputSubTitleInput1);
        driverTools.sendKeys(eleinputSubTitleInput1, en);
        driverTools.sendKeys(eleinputSubTitleInput2, id);
    }

    public void inputColors(String en , String id){
        List<WebElement> colorInputs = driverTools.elements(COLOR_INPUTS).orElseThrow();
        WebElement colorInput1 = colorInputs.get(0);
        WebElement colorInput2 = colorInputs.get(1);
        driverTools.scrollIntoWebView(colorInput1);
        driverTools.clear(colorInput1);
        driverTools.sendKeys(colorInput1, en);
        driverTools.scrollIntoWebView(colorInput2);
        driverTools.clear(colorInput2);
        driverTools.sendKeys(colorInput2, id);
    }

    public int getNumberOfPaymentRecommendations(){
        driverTools.waitForElementToAppear(PAYMENT_RECOMMEND);
        List<WebElement> paymentRecommendations = driverTools.elements(PAYMENT_RECOMMEND).orElseThrow();
        return paymentRecommendations.size();
    }

    public boolean shouldWeAddPaymentMethod(String paymentMethod){
        List<WebElement> paymentRecommendations = driverTools.elements(PAYMENT_RECOMMEND).orElseThrow();
        for(WebElement paymentRecommend:paymentRecommendations){
            String paymnentType = paymentRecommend.getText();
            String paymentName = paymnentType.split("\\s+")[0];
            if(paymentName.equals(paymentMethod)){
                return false;
            }
        }
        return true;
    }

    public String getStringPaymentRecommendation(){
        List<WebElement> paymentRecommendations = driverTools.elements(PAYMENT_RECOMMEND).orElseThrow();
        int lastElement = paymentRecommendations.size()-1;
        driverTools.scrollIntoWebView(paymentRecommendations.get(lastElement));
        String fullText = paymentRecommendations.get(lastElement).getText();
        String paymentMethod = fullText.split("\\s+")[0];
        return paymentMethod;
    }

    public boolean isPaymentRecommendationsDisplayed(){
        WebElement paymentRecommend = driverTools.waitForElementToAppear(PAYMENT_RECOMMEND).orElseThrow();
        return driverTools.isElementDisplayed(paymentRecommend);
    }

    public void savePayment(){
        driverTools.scrollIntoWebView(SAVE_ADD_NEW);
        driverTools.waitForElementToAppear(SAVE_ADD_NEW, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    public List<String> getAllPaymentRecommendationsOnPanelSide() {
        List<WebElement> paymentRecommend = driverTools.elements(PAYMENT_RECOMMEND).orElseThrow();
        List<String> allPaymentRecommendations = new ArrayList<>();
        for (WebElement element : paymentRecommend) {
            String[] words = element.getText().split("\\s+");
            if (words.length > 0) {
                allPaymentRecommendations.add(words[0]);
            }
        }
        return allPaymentRecommendations;
    }

    public List<String> getPaymentRecommendationFromMongoDB(String configType, String dataBase, String collectionName, String queryString, String projectionString) {
        FindIterable<Document> result = TiketMongoDBManager.searchInMongoDBWithProjectionString(configType, dataBase, collectionName, queryString, projectionString);
        List<String> mongoDBPayments = new ArrayList<>();
        for (Document document : result) {
            if (document.containsKey("recommendationList") && document.get("recommendationList") instanceof List) {
                List<Document> recommendationList = (List<Document>) document.get("recommendationList");
                for (Document recommendationItem : recommendationList) {
                    String paymentType = recommendationItem.getString("paymentType");
                    if (paymentType != null) {
                        mongoDBPayments.add(paymentType);
                    }
                }
            }
        }
        return mongoDBPayments;
    }  

    public void clickDropdown(){
        driverTools.waitForElementToAppear(PAYMENT_RECOMMED_DROPDOWN);
        driverTools.click(PAYMENT_RECOMMED_DROPDOWN);
    }

    public void clickYesAndWaitForRecommendations(){
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PAYMENT_RECOMMED_DROPDOWN);
        driverTools.waitForElementToBeClickable(PAYMENT_RECOMMED_DROPDOWN);
    }

    public void clickNoAndWaitForRecommendations(){
        driverTools.waitForElementToAppear(NO_BUTTON, 10).ifPresent(driverTools::click);
        driverTools.waitForElementToAppear(PAYMENT_RECOMMED_DROPDOWN);
        driverTools.waitForElementToBeClickable(PAYMENT_RECOMMED_DROPDOWN);
    }
  
    public void selectVerticalInDuplicateDropdown(String entry){
        driverTools.waitForElementToAppear(GETPAYMENT(entry), 10).ifPresent(driverTools::click);
    }
    
    public void clickNoButton(){
        driverTools.waitForElementToAppear(NO_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void clickOnSavePayment(){
        driverTools.scrollIntoWebView(SAVE_ADD_NEW);
        driverTools.waitForElementToAppear(SAVE_ADD_NEW, 10).ifPresent(driverTools::click);
    }

    public void clickOnCancelButton(){
        driverTools.waitForElementToAppear(CANCEL_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void clickFirstPaymentRecommendation(){
        driverTools.waitForElementToAppear(FIRST_PAYMENT_RECOMMEND, 10).ifPresent(driverTools::click);
    }

    public void clickOnLastSecondOverrideSwitch(){
        List<WebElement> overrideSwitches = driverTools.elements(OVERRIDE_SWITCH).orElseThrow();
        WebElement overrideSwitch = overrideSwitches.get(overrideSwitches.size()-2);
        driverTools.click(overrideSwitch);
    }

    public void clickPaymentMethodMasterConfiguration() {
        if (driverTools.waitForElementToAppear(PAYMENT_METHOD_CONFIG_BUTTON, 5).isPresent()) {
            driverTools.click(PAYMENT_METHOD_CONFIG_BUTTON);
            return;
        }
        WebElement masterMenu = driverTools.waitForElementToAppear(PAYMENT_METHOD_MASTER_SIDEMENU, 15).orElseThrow();
        driverTools.scrollElementToCenter(masterMenu);
        driverTools.clickWithJavascriptExecutor(masterMenu);
    }

    public void clickPaymentGroupDisplay() {
        if (driverTools.waitForElementToAppear(PAYMENT_GROUP_DISPLAY, 5).isPresent()) {
            WebElement submenu = driverTools.waitForElementToAppear(PAYMENT_GROUP_DISPLAY, 15).orElseThrow();
            driverTools.scrollElementToCenter(submenu);
            driverTools.clickWithJavascriptExecutor(submenu);
            return;
        }
        WebElement displayLink = driverTools.waitForElementToAppear(PAYMENT_GROUP_DISPLAY_LINK, 15).orElseThrow();
        driverTools.scrollElementToCenter(displayLink);
        driverTools.clickWithJavascriptExecutor(displayLink);
    }

    public void clickAddGroupButton() {
        driverTools.waitForElementToAppear(ADD_GROUP_BUTTON);
       // driverTools.scrollElementToCenter(ADD_GROUP_BUTTON);
        driverTools.clickWithJavascriptExecutor(ADD_GROUP_BUTTON);
    }

    public void fillGroupDetails(String groupTitleEnglish, String groupTitleBahasa) {
        fillGroupDetails(groupTitleEnglish, groupTitleBahasa, groupTitleEnglish, groupTitleBahasa);
    }

    public void fillGroupDetails(String groupTitleEnglish, String groupTitleBahasa,
                                  String groupSubtitleEnglish, String groupSubtitleBahasa) {
        fillReactControlledInput(GROUP_TITLE_ENGLISH, groupTitleEnglish);
        fillReactControlledInput(GROUP_TITLE_BAHASA, groupTitleBahasa);
        fillReactControlledInput(GROUP_SUBTITLE_ENGLISH, groupSubtitleEnglish);
        fillReactControlledInput(GROUP_SUBTITLE_BAHASA, groupSubtitleBahasa);
    }

    public record GroupFormDetails(String titleEnglish, String titleBahasa,
                                    String subtitleEnglish, String subtitleBahasa) {}

    public GroupFormDetails getDisplayGroupFormDetails() {
        return new GroupFormDetails(
                driverTools.getValue(GROUP_TITLE_ENGLISH),
                driverTools.getValue(GROUP_TITLE_BAHASA),
                driverTools.getValue(GROUP_SUBTITLE_ENGLISH),
                driverTools.getValue(GROUP_SUBTITLE_BAHASA)
        );
    }

    public void clickSave(){
        driverTools.click(SAVE_BUTTON);
    }

    public void clickDisplayFormSave() {
        driverTools.scrollElementToCenter(SAVE_BUTTON);
        WebElement saveButton = driverTools.waitForElementToAppear(SAVE_BUTTON, 15).orElseThrow();
        try {
            driverTools.click(saveButton);
        } catch (org.openqa.selenium.ElementClickInterceptedException | org.openqa.selenium.StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(saveButton);
        }
        driverTools.waitForElementToDisappear(SAVE_BUTTON, 10);
    }

    public void clickDisplayFormCancel() {
        driverTools.scrollElementToCenter(DISPLAY_FORM_CANCEL);
        WebElement cancelButton = driverTools.waitForElementToAppear(DISPLAY_FORM_CANCEL, 15).orElseThrow();
        try {
            driverTools.click(cancelButton);
        } catch (org.openqa.selenium.ElementClickInterceptedException | org.openqa.selenium.StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(cancelButton);
        }
    }

    public void enterPaymentMethod(){
        enterPaymentMethodAndGetName();
    }

    public String enterPaymentMethodAndGetName() {
        driverTools.scrollElementToCenter(PAYMENT_METHOD_DROPDOWN_INPUT);
        driverTools.click(PAYMENT_METHOD_DROPDOWN_INPUT);
        WebElement option = driverTools.waitForElementToAppear(PAYMENT_METHOD_FIRST_OPTION, 10)
                .or(() -> driverTools.waitForElementToAppear(DISPLAY_FIRST_PAYMENT_METHOD_OPTION, 5))
                .orElseThrow();
        String paymentMethodName = normalizeDisplayName(getDisplayElementText(option));
        driverTools.click(option);
        driverTools.click(ADD_BUTTON);
        return paymentMethodName;
    }

    public List<String> getDisplayPaymentMethodsInGroupForm() {
        List<WebElement> blocks = driverTools.elements(BLOCKS).orElseThrow();
        List<String> paymentMethods = new ArrayList<>();
        for (WebElement block : blocks) {
            paymentMethods.add(getDisplayPaymentMethodNameFromBlock(block));
        }
        return paymentMethods;
    }

    public boolean isPaymentGroupingPageDisplayed(String name) {
          Locator PAYMENT_GROUP = new Locator("PAYMENT_GROUP", By.xpath("//div[contains(@class,'w-1/2')]//h1[normalize-space(text())='" + name + "']"));
            driverTools.waitForElementToAppear(PAYMENT_GROUP, 2);
            return driverTools.isElementDisplayed(PAYMENT_GROUP);
    }

    public void deletePaymentGroup() {
        driverTools.waitForElementToAppear(DELETE_GROUP);
        driverTools.scrollElementToCenter(DELETE_GROUP);
        driverTools.click(DELETE_GROUP);
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
    }

    public void clickSubmitButton() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(SUBMIT_BUTTON);
    }

    public void clickEditPaymentGroup() {
        driverTools.waitForElementToAppear(EDIT_LAST, 10);
        driverTools.scrollElementToCenter(EDIT_LAST);
        driverTools.click(EDIT_LAST);
    }

    public int getBlockCount() {
        List<WebElement> blocks = driverTools.elements(BLOCKS).orElseThrow();
        return blocks.size();
    }

    public void openPaymentGroupDisplayPage() {
        if (!driverTools.waitForElementToAppear(DISPLAY_PAGE_TITLE, 3).isPresent()) {
            try {
                clickPaymentMethodMasterConfiguration();
                clickPaymentGroupDisplay();
            } catch (RuntimeException e) {
                navigateToPaymentGroupDisplayUrl();
            }
        }
        driverTools.waitForElementToAppear(DISPLAY_PAGE_TITLE, 15);
        driverTools.waitForElementToAppear(DISPLAY_RECOMMENDATION_SECTION, 15);
    }

    public void saveDisplayChanges() {
        WebElement saveButton = driverTools.waitForElementToAppear(DISPLAY_SAVE_CHANGES, 15).orElseThrow();
        driverTools.scrollElementToCenter(saveButton);
        if (!saveButton.isEnabled()) {
            logger.log("Save Changes button is disabled; skipping page save.");
            return;
        }
        clickDisplaySaveChanges();
        confirmDisplaySaveConfirmation();
    }

    public void scrollToDisplayPaymentGroupSection() {
        driverTools.scrollElementToCenter(DISPLAY_PAYMENT_GROUP_SECTION);
    }

    public void clickDisplaySaveChanges() {
        driverTools.scrollElementToCenter(DISPLAY_SAVE_CHANGES);
        WebElement saveButton = driverTools.waitForElementToAppear(DISPLAY_SAVE_CHANGES, 15).orElseThrow();
        try {
            driverTools.click(saveButton);
        } catch (org.openqa.selenium.ElementClickInterceptedException | org.openqa.selenium.StaleElementReferenceException e) {
            driverTools.clickWithJavascriptExecutor(saveButton);
        }
    }

    public void clickDisplayAddGroupButton() {
        scrollToDisplayPaymentGroupSection();
        driverTools.waitForElementToAppear(DISPLAY_ADD_GROUP_BUTTON, 10);
        driverTools.clickWithJavascriptExecutor(DISPLAY_ADD_GROUP_BUTTON);
    }

    public void clickDisplayEditPaymentGroupByName(String groupName) {
        scrollToDisplayPaymentGroupSection();
        Locator editLink = new Locator("DISPLAY_EDIT_PAYMENT_GROUP_BY_NAME", By.xpath(DISPLAY_PAYMENT_GROUP_CARD_XPATH + "//div[contains(@class,'bg-[#F4F7FE]')][.//h1[contains(normalize-space(text()),'" + groupName + "')]]//p[normalize-space(text())='Edit']"));
        WebElement element = driverTools.waitForElementToAppear(editLink, 10).orElseThrow();
        driverTools.scrollElementToCenter(element);
        driverTools.click(element);
    }

    public void deleteDisplayPaymentGroupByName(String groupName) {
        scrollToDisplayPaymentGroupSection();
        Locator deleteIcon = new Locator("DISPLAY_DELETE_PAYMENT_GROUP_BY_NAME", By.xpath(DISPLAY_PAYMENT_GROUP_CARD_XPATH + "//div[contains(@class,'bg-[#F4F7FE]')][.//h1[contains(normalize-space(text()),'" + groupName + "')]]//*[local-name()='use'][@fill='red']/ancestor::*[local-name()='svg'][1]"));
        WebElement element = driverTools.waitForElementToAppear(deleteIcon, 10).orElseThrow(
                () -> new org.openqa.selenium.NoSuchElementException("Delete icon not found for payment group: " + groupName));
        driverTools.scrollElementToCenter(element);
        driverTools.click(element);
        clickYes();
    }

    public void deleteDisplayPaymentMethodFromGroup() {
        List<WebElement> deleteIcons = driverTools.elements(DISPLAY_DELETE_GROUP_METHOD).orElseThrow();
        WebElement deleteIcon = deleteIcons.get(deleteIcons.size() - 1);
        driverTools.scrollElementToCenter(deleteIcon);
        driverTools.click(deleteIcon);
        driverTools.waitForElementToAppear(YES_BUTTON, 10).ifPresent(driverTools::click);
    }

    public boolean isDisplayPaymentGroupOnList(String name) {
        Locator paymentGroup = new Locator("DISPLAY_PAYMENT_GROUP_ON_LIST", By.xpath(DISPLAY_PAYMENT_GROUP_CARD_XPATH + "//div[contains(@class,'bg-[#F4F7FE]')]//h1[contains(normalize-space(text()),'" + name + "')]"));
        return driverTools.waitForElementToAppear(paymentGroup, 2).isPresent();
    }

    public List<String> getPaymentGroupPaymentMethodsFromApi(String orderHash, String orderId, String currency,
                                                              String countryCode, String groupTitleEnglish) {
        PaymentGroupResponse response = getPaymentGroupFromApi(orderHash, orderId, currency, countryCode);
        PaymentGroupResponse.PaymentGroup group = findPaymentGroupByTitle(response, groupTitleEnglish);
        if (group == null || group.items == null) {
            return List.of();
        }
        return group.items.stream()
                .map(this::getPaymentMethodDisplayName)
                .collect(Collectors.toList());
    }

    public String getLastPaymentGroupTitleFromApi(String orderHash, String orderId, String currency, String countryCode) {
        PaymentGroupResponse response = getPaymentGroupFromApi(orderHash, orderId, currency, countryCode);
        if (response.data == null || response.data.commonGroup == null || response.data.commonGroup.isEmpty()) {
            throw new org.openqa.selenium.NoSuchElementException("No payment groups found in payment-group API");
        }
        PaymentGroupResponse.PaymentGroup lastGroup = response.data.commonGroup.get(response.data.commonGroup.size() - 1);
        return lastGroup.title.en;
    }

    public void verifyPaymentGroupOnApi(String orderHash, String orderId, String currency, String countryCode,
                                        String expectedTitleEn, String expectedTitleId, List<String> expectedPaymentMethods) {
        PaymentGroupResponse response = getPaymentGroupFromApi(orderHash, orderId, currency, countryCode);
        Assertion.assertThat("Payment group API code", response.code, is("SUCCESS"));

        PaymentGroupResponse.PaymentGroup group = findPaymentGroupByTitle(response, expectedTitleEn);
        Assertion.assertThat("Verify payment group exists on payment-group API", group != null, is(true));

        Assertion.assertThat("Verify group title EN on payment-group API", group.title.en, is(expectedTitleEn));
        Assertion.assertThat("Verify group title ID on payment-group API", group.title.id, is(expectedTitleId));
        Assertion.assertThat("Verify group status on payment-group API", group.status, is(true));

        List<PaymentGroupResponse.PaymentMethodItem> items = group.items != null ? group.items : List.of();
        Assertion.assertThat("Verify payment method count on payment-group API", items.size(), is(expectedPaymentMethods.size()));

        for (String expectedPaymentMethod : expectedPaymentMethods) {
            PaymentGroupResponse.PaymentMethodItem item = findPaymentMethodItemInGroup(group, expectedPaymentMethod);
            Assertion.assertThat("Verify payment method " + expectedPaymentMethod + " exists on payment-group API", item != null, is(true));
            Assertion.assertThat("Verify payment method status for " + expectedPaymentMethod + " on payment-group API", item.status, is(true));
            Assertion.assertThat("Verify payment method code for " + expectedPaymentMethod + " on payment-group API", item.paymentMethod != null && !item.paymentMethod.isBlank(), is(true));
            Assertion.assertThat("Verify payment name for " + expectedPaymentMethod + " on payment-group API", item.paymentName != null && !item.paymentName.isBlank(), is(true));
            Assertion.assertThat("Verify payment method display name matches panel for " + expectedPaymentMethod + " on payment-group API",
                    matchesPaymentMethodName(getPaymentMethodDisplayName(item), expectedPaymentMethod), is(true));
        }
    }

    public void verifyPaymentGroupNotOnApi(String orderHash, String orderId, String currency, String countryCode,
                                           String groupTitleEnglish) {
        PaymentGroupResponse response = getPaymentGroupFromApi(orderHash, orderId, currency, countryCode);
        Assertion.assertThat("Verify payment group is deleted from payment-group API",
                isPaymentGroupPresentOnApi(response, groupTitleEnglish), is(false));
    }

    @SneakyThrows
    public PaymentGroupResponse getPaymentGroupFromApi(String orderHash, String orderId, String currency, String countryCode) {
        sleepQuietly(CLIENT_API_DELAY_MS);
        PaymentGroupApi paymentGroupApi = new PaymentGroupApi(
                new PaymentGroupApi.Inputs(orderHash, orderId, DEFAULT_PLATFORM, null, currency, countryCode)
        );
        ApiResponse apiResponse = paymentGroupApi.hitApi(apiTools);
        logger.log("payment-group api response : " + apiResponse.getBody());
        return gson.fromJson(apiResponse.getBody(), PaymentGroupResponse.class);
    }

    private String getPaymentGroupDisplayUrl() {
        Deeplink deeplink = new Deeplink("ADMIN_CARE", "https://gatotkaca.tiket.com", "https://preprod.tiket.com", "https://tiket.com");
        return switch (Configs.ENVIRONMENT) {
            case PROD -> deeplink.prodDeeplink() + PAYMENT_GROUP_DISPLAY_PATH;
            case PREPROD -> deeplink.preprodDeeplink() + PAYMENT_GROUP_DISPLAY_PATH;
            case GK -> deeplink.gkDeeplink() + PAYMENT_GROUP_DISPLAY_PATH;
            default -> deeplink.gkDeeplink() + PAYMENT_GROUP_DISPLAY_PATH;
        };
    }

    private void navigateToPaymentGroupDisplayUrl() {
        driverTools.get(getPaymentGroupDisplayUrl());
    }

    private void fillReactControlledInput(Locator locator, String value) {
        WebElement input = driverTools.waitForElementToAppear(locator, 15).orElseThrow();
        driverTools.scrollElementToCenter(input);
        driverTools.click(input);
        setReactControlledInputValue(input, value);
    }

    private void setReactControlledInputValue(WebElement input, String value) {
        ((JavascriptExecutor) driver).executeScript(
                "const input = arguments[0];"
                        + "const nextValue = arguments[1];"
                        + "const nativeInputValueSetter = Object.getOwnPropertyDescriptor("
                        + "window.HTMLInputElement.prototype, 'value').set;"
                        + "if (input._valueTracker) { input._valueTracker.setValue(''); }"
                        + "nativeInputValueSetter.call(input, nextValue);"
                        + "input.dispatchEvent(new Event('input', { bubbles: true }));"
                        + "input.dispatchEvent(new Event('change', { bubbles: true }));",
                input, value);
    }

    private String getDisplayPaymentMethodNameFromBlock(WebElement block) {
        List<String> candidateXpaths = List.of(".//h1[contains(@class,'weight_bold')]", ".//h1");
        for (String xpath : candidateXpaths) {
            for (WebElement candidate : block.findElements(By.xpath(xpath))) {
                String text = normalizeDisplayName(getDisplayElementText(candidate));
                if (!text.isBlank()) {
                    return text;
                }
            }
        }
        throw new org.openqa.selenium.NoSuchElementException("Payment method name not found in group form block");
    }

    private String getDisplayElementText(WebElement element) {
        String text = normalizeDisplayName(driverTools.getText(element));
        if (!text.isEmpty()) {
            return text;
        }
        text = normalizeDisplayName(element.getAttribute("textContent"));
        if (!text.isEmpty()) {
            return text;
        }
        text = normalizeDisplayName(element.getAttribute("innerText"));
        if (!text.isEmpty()) {
            return text;
        }
        Object jsText = ((JavascriptExecutor) driver).executeScript(
                "return (arguments[0].textContent || arguments[0].innerText || '').trim();", element);
        return normalizeDisplayName(jsText == null ? "" : String.valueOf(jsText));
    }

    private String normalizeDisplayName(String name) {
        return name == null ? "" : name.trim();
    }

    private String normalizePaymentMethodName(String name) {
        return name == null ? "" : name.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    private String normalizePaymentMethodToken(String name) {
        return normalizePaymentMethodName(name).replace("_", "").replace("-", "");
    }

    private boolean matchesPaymentMethodName(String left, String right) {
        String leftToken = normalizePaymentMethodToken(left);
        String rightToken = normalizePaymentMethodToken(right);
        if (leftToken.isEmpty() || rightToken.isEmpty()) {
            return false;
        }
        if (leftToken.equals(rightToken)) {
            return true;
        }
        String normalizedLeft = normalizePaymentMethodName(left);
        String normalizedRight = normalizePaymentMethodName(right);
        return normalizedLeft.contains(normalizedRight) || normalizedRight.contains(normalizedLeft);
    }

    private String getPaymentMethodDisplayName(PaymentGroupResponse.PaymentMethodItem item) {
        if (item.paymentName != null && !item.paymentName.isBlank()) {
            return item.paymentName;
        }
        return item.paymentMethod;
    }

    private PaymentGroupResponse.PaymentMethodItem findPaymentMethodItemInGroup(
            PaymentGroupResponse.PaymentGroup group, String paymentMethodName) {
        if (group.items == null) {
            return null;
        }
        for (PaymentGroupResponse.PaymentMethodItem item : group.items) {
            if (matchesPaymentMethodName(item.paymentMethod, paymentMethodName)
                    || matchesPaymentMethodName(item.paymentName, paymentMethodName)) {
                return item;
            }
        }
        return null;
    }

    private boolean isPaymentGroupPresentOnApi(PaymentGroupResponse response, String groupTitleEnglish) {
        return findPaymentGroupByTitle(response, groupTitleEnglish) != null;
    }

    private PaymentGroupResponse.PaymentGroup findPaymentGroupByTitle(PaymentGroupResponse response, String groupTitleEnglish) {
        if (response.data == null || response.data.commonGroup == null) {
            return null;
        }
        return response.data.commonGroup.stream()
                .filter(group -> group.title != null && Objects.equals(groupTitleEnglish, group.title.en))
                .findFirst()
                .orElse(null);
    }

    private Locator getDisplayModalButton(String buttonText) {
        return new Locator("DISPLAY_MODAL_BUTTON", By.xpath("//section[contains(@class,'modal_popup')]//button[normalize-space()='" + buttonText + "']"));
    }

    private void clickDisplayModalButton(String buttonText) {
        Locator button = getDisplayModalButton(buttonText);
        WebElement element = driverTools.waitForElementToAppear(button, 15).orElseThrow();
        driverTools.clickWithJavascriptExecutor(element);
    }

    private void confirmDisplaySaveConfirmation() {
        if (!confirmDisplaySaveConfirmationIfPresent()) {
            throw new IllegalStateException("Save Changes was clicked but Save Confirmation modal did not appear.");
        }
    }

    private boolean confirmDisplaySaveConfirmationIfPresent() {
        if (driverTools.waitForElementToAppear(DISPLAY_SAVE_CONFIRMATION_MODAL, 5).isPresent()) {
            clickDisplayModalButton("Submit");
            return true;
        }
        Locator submitButton = getDisplayModalButton("Submit");
        if (driverTools.waitForElementToAppear(submitButton, 3).isPresent()) {
            clickDisplayModalButton("Submit");
            return true;
        }
        return false;
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}