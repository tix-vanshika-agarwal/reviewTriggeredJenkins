package com.tiket.page.utilities.message.help_center;

import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;


public class HelpCenterPageAndroid <T extends WebDriver> extends HelpCenterPage<T> {
    private static final Locator WHATSAPP = new Locator("WHATSAPP", AppiumBy.xpath("//*[contains(@text,'Whatsapp') or contains(@text,'WhatsApp')]"));
    private static final Locator EMAIL = new Locator("EMAIL", AppiumBy.xpath("//*[contains(@text,'Email')]"));
    private static final Locator PHONE_NUMBER_LOCAL = new Locator("PHONE_NUMBER_LOCAL", AppiumBy.xpath("//*[contains(@text,'Domestic call') or contains(@text,'Sambungan domestik')]"));
    private static final Locator PHONE_NUMBER_INTERNATIONAL = new Locator("PHONE_NUMBER_INTERNATIONAL", AppiumBy.xpath("//*[contains(@text,'Sambungan internasional') or contains(@text,'International call')]"));
    private static final Locator DYNAMIC_FORM_IFRAME = new Locator("DYNAMIC_FORM_IFRAME", AppiumBy.xpath("//*[@id='dynamic_form_iframe']"));
    private static final Locator SEND_QUESTION_DRAWER_TITLE = new Locator("SEND_QUESTION_DRAWER_TITLE", AppiumBy.xpath("//span[text()='Sampaikan Pengajuanmu']"));
    private static final Locator TEXT_LIGHT = new Locator("TEXT_LIGHT", AppiumBy.xpath("//*[@class='text-light']"));
    private static final Locator TEXT_BLACK = new Locator("TEXT_BLACK", AppiumBy.xpath("//*[contains(@class,'text-black')]"));
    private static final Locator FIELD_PRODUCT_IN_QUESTION_FORM = new Locator("FIELD_PRODUCT_IN_QUESTION_FORM", AppiumBy.xpath("//div[@class='form-group' and select[option[@value='General'] ] and select[option[@value='Hotel'] ]]"));
    private static final Locator FIELD_QUESTION_TYPE_IN_QUESTION_FORM = new Locator("FIELD_PRODUCT_IN_QUESTION_FORM", AppiumBy.xpath("//div[@class='form-group' and select[option[@value='Request'] ] and select[option[@value='Complaint']]]"));
    private static final Locator PRODUCT_OPTION_QUESTION_FORM = new Locator("PRODUCT_OPTION_QUESTION_FORM", AppiumBy.xpath("//option[@value='Entertainment']"));
    private static final Locator QUESTION_OPTION_QUESTION_FORM = new Locator("QUESTION_OPTION_QUESTION_FORM", AppiumBy.xpath("//option[@value='Complaint']"));
    private static final Locator DYNAMIC_FIELD_1_OPTIONS = new Locator("DYNAMIC_FIELD_1_OPTIONS", AppiumBy.xpath("//div[@class='form-group' and span[select[option[contains(@value,'Keluhan') or contains(@value,'Complaint')]]]]//option"));
    private static final Locator DYNAMIC_FIELD_2_BLOCKED_INPUT = new Locator("DYNAMIC_FIELD_2_BLOCKED_INPUT", AppiumBy.xpath("(//div[@class='form-group' and span[select[option[contains(@value,'Keluhan') or contains(@value,'Complaint')]]]]//following::div[contains(@class,'row') and not(@id='hiddenStyle')]//div[@class='form-group'])[1]//input[@type='hidden']"));
    private static final Locator DYNAMIC_FIELD_2_OPTIONS = new Locator("DYNAMIC_FIELD_2_OPTIONS", AppiumBy.xpath("(//div[@class='form-group' and span[select[option[contains(@value,'Keluhan') or contains(@value,'Complaint')]]]]//following::div[contains(@class,'row') and not(@id='hiddenStyle')]//div[@class='form-group'])[1]//option"));
    private static final Locator ORDER_ID_FIELD = new Locator("ORDER_ID_FIELD", AppiumBy.xpath("//div[@class='form-group' and label[contains(text(),'Order ID')]]//input"));
    private static final Locator DESCRIPTION_FIELD = new Locator("DESCRIPTION_FIELD", AppiumBy.xpath("//textarea"));
    private static final Locator SUBMIT_QUESTION_FORM = new Locator("SUBMIT_QUESTION_FORM", AppiumBy.xpath("//div[input[@type='submit']]"));

    @Override
    public boolean isAllContactCustomerCareOptionPresent(){
        boolean isWhatsAppDisplayed = driverTools.waitForElementToPresence(WHATSAPP).isPresent();
        logger.log("is whatsapp displayed on contact customer care page : " + isWhatsAppDisplayed);
        boolean isEmailDisplayed = driverTools.waitForElementToPresence(EMAIL).isPresent();
        logger.log("is email displayed on contact customer care page : " + isEmailDisplayed);
        boolean isPhoneNumberLocalDisplayed = driverTools.waitForElementToPresence(PHONE_NUMBER_LOCAL).isPresent();
        logger.log("is phone number local displayed on contact customer care page : " + isPhoneNumberLocalDisplayed);
        boolean isPhoneNumberInternationalDisplayed = driverTools.waitForElementToPresence(PHONE_NUMBER_INTERNATIONAL).isPresent();
        logger.log("is phone number international displayed on contact customer care page : " + isPhoneNumberInternationalDisplayed);
        return isWhatsAppDisplayed && isEmailDisplayed && isPhoneNumberLocalDisplayed && isPhoneNumberInternationalDisplayed;
    }

    private void reopenIframeContext(WebElement dynamicFormIframe){
        driverTools.switchBackFromIframe();
        driverTools.switchToIframe(dynamicFormIframe);
    }

    public HelpCenterPageAndroid(Session<T> session) {
        super(session);
    }

    @Override
    public void fillReportComplaint(String orderID){
         //Dynamic form reload everytime state change, hence we need to switch the context again after making change
        driverTools.switchToWebView();
        WebElement dynamicFormIframe = driverTools.element(DYNAMIC_FORM_IFRAME).orElseThrow();
        driverTools.switchToIframe(dynamicFormIframe);

        //choose product
        driverTools.click(PRODUCT_OPTION_QUESTION_FORM);

        //choose complaints
        driverTools.click(QUESTION_OPTION_QUESTION_FORM);
        reopenIframeContext(dynamicFormIframe);

        //choose value from dynamic field 1
        List<WebElement> dynamicFieldOption1Elements = driverTools.elements(DYNAMIC_FIELD_1_OPTIONS).orElseThrow();
        Random rand = new Random();
        driverTools.click(dynamicFieldOption1Elements.get(rand.nextInt(1,dynamicFieldOption1Elements.size())));
        reopenIframeContext(dynamicFormIframe);

        //choose value from dynamic field 2;
        if(!driverTools.isElementDisplayed(DYNAMIC_FIELD_2_BLOCKED_INPUT)){ //check if dynamic field 2 is present -> 
            List<WebElement> dynamicFieldOption2Elements = driverTools.elements(DYNAMIC_FIELD_2_OPTIONS).orElseThrow();
            driverTools.click(dynamicFieldOption2Elements.get(rand.nextInt(1,dynamicFieldOption2Elements.size())));
        }

        //input orderId
        driverTools.swipeToElement(ORDER_ID_FIELD,Swipe.UP,3);
        driverTools.sendKeys(ORDER_ID_FIELD,orderID);

        //input description
        driverTools.swipeToElement(DESCRIPTION_FIELD, Swipe.UP, 2);
        driverTools.sendKeys(DESCRIPTION_FIELD,"MR. AUTOMATION " + CoreFunctions.randomName() + " testing input question from help center ");
        driverTools.hideKeyboard(1);

        reopenIframeContext(dynamicFormIframe);
        driverTools.swipeToElement(SUBMIT_QUESTION_FORM,Swipe.UP,3);
        driverTools.click(SUBMIT_QUESTION_FORM);
        driverTools.switchBackFromIframe();
        driverTools.switchToNative();
    }

    @Override
    public boolean verifyReportComplaintField() {
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(SEND_QUESTION_DRAWER_TITLE,15);
        WebElement dynamicFormIframe = driverTools.element(DYNAMIC_FORM_IFRAME).orElseThrow();
        driverTools.switchToIframe(dynamicFormIframe);
        boolean isQuestionTypeVisible = driverTools.isElementDisplayed(FIELD_QUESTION_TYPE_IN_QUESTION_FORM);
        boolean isProductFieldVisible = driverTools.isElementDisplayed(FIELD_PRODUCT_IN_QUESTION_FORM);
        Assertion.assertThat("Verify field question type exist",isQuestionTypeVisible, is(true));
        Assertion.assertThat("Verify field product exist",isProductFieldVisible, is(true));
        driverTools.switchBackFromIframe();
        driverTools.switchToNative();
        return true;
    }

    @Override
    public boolean isRequestSubmitted() {
        driverTools.switchToWebView();
        WebElement dynamicFormIframe = driverTools.element(DYNAMIC_FORM_IFRAME).orElseThrow();
        driverTools.switchToIframe(dynamicFormIframe);
        driverTools.waitForElementToAppear(TEXT_LIGHT,15);
        boolean isSubtitleTextAppear = driverTools.isElementDisplayed(TEXT_LIGHT);
        boolean isTitleTextAppear = driverTools.isElementDisplayed(TEXT_BLACK);
        driverTools.switchBackFromIframe();
        driverTools.switchToNative();
        return isSubtitleTextAppear && isTitleTextAppear;
    }
}