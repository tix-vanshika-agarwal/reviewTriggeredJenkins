package com.tiket.page.utilities.message.help_center;

import com.tiket.appium.Swipe;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;

public class HelpCenterPageIOS <T extends WebDriver> extends HelpCenterPage<T>{
    private static final Locator WHATSAPP = new Locator("WHATSAPP", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'WhatsApp') or contains(@label,'Whatsapp')]"));
    private static final Locator EMAIL = new Locator("EMAIL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Email')]"));
    private static final Locator PHONE_NUMBER_LOCAL = new Locator("PHONE_NUMBER_LOCAL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Domestic call') or contains(@label,'Sambungan domestik')]"));
    private static final Locator PHONE_NUMBER_INTERNATIONAL = new Locator("PHONE_NUMBER_INTERNATIONAL", AppiumBy.xpath("//XCUIElementTypeStaticText[contains(@label,'Sambungan internasional') or contains(@label,'International call')]"));
    private static final Locator  FIELD_PRODUCT_IN_QUESTION_FORM= new Locator("FIELD_PRODUCT_IN_QUESTION_FORM", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']//XCUIElementTypeOther[3]"));
    private static final Locator FIELD_QUESTION_TYPE_IN_QUESTION_FORM = new Locator("FIELD_QUESTION_TYPE_IN_QUESTION_FORM", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']//XCUIElementTypeOther[4]"));
    private static final Locator DYNAMIC_FORM_DROPDOWN_LIST = new Locator("DYNAMIC_FORM_DROPDOWN_LIST", AppiumBy.xpath("//XCUIElementTypeCollectionView//XCUIElementTypeCell"));
    private static final Locator FIELD_DYNAMIC_1 = new Locator("FIELD_DYNAMIC_1", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']//XCUIElementTypeOther[5]"));
    private static final Locator FIELD_DYNAMIC_2 = new Locator("FIELD_DYNAMIC_2", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']//XCUIElementTypeOther[7]"));
    private static final Locator ORDER_ID_FIELD = new Locator("ORDER_ID_FIELD", AppiumBy.xpath("//XCUIElementTypeOther[@name='Order ID']//parent::*//following-sibling:: XCUIElementTypeTextField"));
    private static final Locator DESCRIPTION_FIELD = new Locator("DESCRIPTION_FIELD", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']/XCUIElementTypeTextView"));
    private static final Locator SUBMIT_QUESTION_FORM = new Locator("SUBMIT_QUESTION_FORM",AppiumBy.xpath("//XCUIElementTypeButton[@name='Lanjutkan' or @name='Continue']"));
    private static final Locator DYNAMIC_FORM_DESCRIPTION_LABEL = new Locator("DYNAMIC_FORM_DESCRIPTION_LABEL", AppiumBy.xpath("//XCUIElementTypeOther[@name='dynamic_form_iframe']/XCUIElementTypeTextView//preceding-sibling::*[XCUIElementTypeOther[@label='Deskripsi' or @label='Description']]"));
    private static final Locator DYNAMIC_FORM_SUBMIT_SUCCESS = new Locator("",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Pesanmu berhasil terkirim' or @name='You message' or @name='Message has been sent']"));
    private static final Locator DYNAMIC_FORM_SUBMIT_SUCCESS_SUBTITLE = new Locator("",AppiumBy.xpath("//XCUIElementTypeStaticText[@name='Kami sudah menerima pesanmu dan akan segera menindaklanjutinya. Terima kasih!' or @name='We have received your message and will help you shortly. Thank you!']"));


    public HelpCenterPageIOS(Session<T> session) {
        super(session);
    }

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

    @Override
    public boolean verifyReportComplaintField() {
        driverTools.waitForElementToAppear(FIELD_QUESTION_TYPE_IN_QUESTION_FORM,15);
        boolean isQuestionTypeVisible = driverTools.isElementDisplayed(FIELD_QUESTION_TYPE_IN_QUESTION_FORM);
        boolean isProductFieldVisible = driverTools.isElementDisplayed(FIELD_PRODUCT_IN_QUESTION_FORM);
        Assertion.assertThat("Verify field question type exist",isQuestionTypeVisible, is(true));
        Assertion.assertThat("Verify field product exist",isProductFieldVisible, is(true));
        return true;
    }

    private void sendKeyToQuestionDescription(){
        for(int i=0;i<5;i++){
            Locator keyboardKeys = new Locator("KEYBOARD_KEYS_"+(i+1), AppiumBy.xpath("//XCUIElementTypeKey["+(i+1)+"]"));
            driverTools.click(keyboardKeys);
        }
    }

    @Override
    public void fillReportComplaint(String orderID) {
        //choose product
        Random rand = new Random();
        driverTools.click(FIELD_PRODUCT_IN_QUESTION_FORM);
        List<WebElement> productOptionsQuestionForm = driverTools.elements(DYNAMIC_FORM_DROPDOWN_LIST).orElseThrow();
        driverTools.click(productOptionsQuestionForm.get(rand.nextInt(1,productOptionsQuestionForm.size())));

        //choose complaints
        driverTools.click(FIELD_QUESTION_TYPE_IN_QUESTION_FORM);
        List<WebElement> dynamicQuetionTypeOption = driverTools.elements(DYNAMIC_FORM_DROPDOWN_LIST).orElseThrow();
        driverTools.click(dynamicQuetionTypeOption.get(rand.nextInt(dynamicQuetionTypeOption.size())));

        //choose value from dynamic field 1
        driverTools.click(FIELD_DYNAMIC_1);
        List<WebElement> dynamicFieldOption1Elements = driverTools.elements(DYNAMIC_FORM_DROPDOWN_LIST).orElseThrow();
        driverTools.click(dynamicFieldOption1Elements.get(rand.nextInt(1,dynamicFieldOption1Elements.size())));

        boolean isDynamicField2Enabled = driverTools.getValueAttribute(FIELD_DYNAMIC_2, "enabled").equals("true");
        //choose value from dynamic field 2;
        if(isDynamicField2Enabled){ //check if dynamic field 2 is present
            driverTools.click(FIELD_DYNAMIC_2);
            List<WebElement> dynamicFieldOption2Elements = driverTools.elements(DYNAMIC_FORM_DROPDOWN_LIST).orElseThrow();
            driverTools.click(dynamicFieldOption2Elements.get(rand.nextInt(1,dynamicFieldOption2Elements.size())));
        }

        // //input orderId
        driverTools.swipeToElement(ORDER_ID_FIELD,Swipe.UP,3);
        driverTools.clickElementByCoordinate(ORDER_ID_FIELD);
        sendKeyToQuestionDescription();
        driverTools.hideKeyboard(1);

        //input description
        driverTools.swipeToElement(DESCRIPTION_FIELD, Swipe.UP, 2);
        driverTools.clickElementByCoordinate(DESCRIPTION_FIELD);
        sendKeyToQuestionDescription();
        driverTools.clickElementByCoordinate(DYNAMIC_FORM_DESCRIPTION_LABEL);

        driverTools.swipeToElement(SUBMIT_QUESTION_FORM, Swipe.UP, 2);
        driverTools.clickElementByCoordinate(SUBMIT_QUESTION_FORM);
    }

    @Override
    public boolean isRequestSubmitted() {
        driverTools.waitForElementToAppear(DYNAMIC_FORM_SUBMIT_SUCCESS,15);
        boolean isSubtitleTextAppear = driverTools.isElementDisplayed(DYNAMIC_FORM_SUBMIT_SUCCESS_SUBTITLE);
        boolean isTitleTextAppear = driverTools.isElementDisplayed(DYNAMIC_FORM_SUBMIT_SUCCESS);
        Assertion.assertThat("Verify field question type exist",isSubtitleTextAppear, is(true));
        Assertion.assertThat("Verify field product exist",isTitleTextAppear, is(true));
        return isSubtitleTextAppear && isTitleTextAppear;
    }
}