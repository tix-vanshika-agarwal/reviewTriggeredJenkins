package com.tiket.web.dashboard.page.b2bCorp_internal;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.Matchers.is;

public class DocumentSettingPage<T extends WebDriver> extends BasePage<T> {

    private static final Locator INDONESIAN_CORP = new Locator("CORPORATE_INFORMATION", new By.ByXPath("(//button[contains(@class, 'chip')])[1]"));
    private static final Locator FOREIGN_CORP = new Locator("CORPORATE_INFORMATION", new By.ByXPath("(//button[contains(@class, 'chip')])[2]"));
    private static final Locator GOVT_CORPORATE = new Locator("CORPORATE_INFORMATION", new By.ByXPath("(//button[contains(@class, 'chip')])[3]"));
    private static final Locator EDIT_DOC_BUTTON = new Locator("EDIT_DOC_BUTTON", new By.ByXPath("//button//span[contains(text(), 'Edit documents')]"));
    private static final Locator ADD_MORE_DOC = new Locator("ADD_MORE_DOC", new By.ByXPath("//button//span[contains(text(), ' Add more documents')]"));
    private static final Locator DOCUMENT_TAG = new Locator("DOCUMENT_TAG", new By.ByXPath("//input[@id='type-document-name' and @value='']"));
    private static final Locator PAYMENT_METHOD = new Locator("PAYMENT_METHOD", new By.ByXPath("//*[contains(text(), 'Choose method')]"));
    private static final Locator RADIO_PAYMENT_CLICK = new Locator("RADIO_PAYMENT_CLICK", new By.ByXPath("(//*[contains(text(), 'Instant')])[3]/parent::*/preceding-sibling::*"));
    private static final Locator SAVE_PAYMENT_CHOICE = new Locator("SAVE_PAYMENT_CHOICE", new By.ByXPath("(//button//span[contains(text(), 'Save')])[4]"));
    private static final Locator SAVE_DOCUMENT = new Locator("SAVE_DOCUMENT", new By.ByXPath("(//button//span[contains(text(), 'Save')])[1]"));
    private static final Locator NO_SEND_BUTTON = new Locator("NO_SEND_BUTTON", new By.ByXPath("//button//span[contains(text(), 'No')]"));
    private static final Locator DELETE_LOGO = new Locator("DELETE_LOGO", new By.ByXPath("(//*[name()='svg'][@class='cursor-pointer'])[4]"));
    private static final Locator POPUP_NOTIFY = new Locator("POPUP_NOTIFY", new By.ByXPath("//h2[contains(text(), 'Notify')]"));
    private static final Locator DOC_TO_EDIT = new Locator("DOC_TO_EDIT", new By.ByXPath("(//input[@id='type-document-name'])[2]"));
    private static final Locator EDITED_DOC = new Locator("EDITED_DOC", new By.ByXPath("//span[contains(text(), 'Edited')]"));
    private static final Locator SNACKBAR_TEXT = new Locator("SNACKBAR_TEXT", new By.ByXPath("//div[contains(@class, 'Snackbar_text')]"));
    private static final Locator BOOKING_LIST_SIDEBAR_ITEM = new Locator("BOOKING_LIST_SIDEBAR_ITEM", new By.ByXPath("//*[contains(text(), 'Booking List')]"));



    public DocumentSettingPage(Session<T> session) {
        super(session);
    }

    public String isIndonesianTabVisible(){
        driverTools.waitForElementToAppear(INDONESIAN_CORP);
        Assertion.assertThat("Indonesian corp tab is visible",driverTools.isElementDisplayed(INDONESIAN_CORP),is(true));
        return driverTools.getText(INDONESIAN_CORP).orElseThrow();
    }

    public String isForeignTabVisible(){
        driverTools.waitForElementToAppear(FOREIGN_CORP);
        Assertion.assertThat("Foreign corp tab is visible",driverTools.isElementDisplayed(FOREIGN_CORP),is(true));
        return driverTools.getText(FOREIGN_CORP).orElseThrow();
    }

    public String isGovtTabVisible(){
        driverTools.waitForElementToAppear(GOVT_CORPORATE);
        Assertion.assertThat("GOVT corp tab is visible",driverTools.isElementDisplayed(GOVT_CORPORATE),is(true));
        return driverTools.getText(GOVT_CORPORATE).orElseThrow();
    }

    public void addNewDoc(String docName) throws InterruptedException {
        driverTools.waitForElementToAppear(BOOKING_LIST_SIDEBAR_ITEM,5);
        Assertion.assertThat("Edit Document Button is visible",driverTools.isElementDisplayed(EDIT_DOC_BUTTON),is(true));
        driverTools.click(EDIT_DOC_BUTTON);
        driverTools.click(ADD_MORE_DOC);
        Assertion.assertThat("Add more Document Button is visible",driverTools.isElementDisplayed(ADD_MORE_DOC),is(true));
        driverTools.click(DOCUMENT_TAG);
        driverTools.sendKeys(DOCUMENT_TAG, docName);
        driverTools.click(PAYMENT_METHOD);
        driverTools.click(RADIO_PAYMENT_CLICK);
        driverTools.click(SAVE_PAYMENT_CHOICE);
        driverTools.click(SAVE_DOCUMENT);

        driverTools.waitForElementToAppear(POPUP_NOTIFY,3);
        Assertion.assertThat("Popup to send email update appeared",driverTools.isElementDisplayed(POPUP_NOTIFY),is(true));
        driverTools.click(NO_SEND_BUTTON);

    }

    private Locator getDocumentLocator(String name) {
        return new Locator("DocumentLocator", By.xpath("//span[contains(text(), '" + name + "')]"));
    }

    public boolean isNewDocumentAdded(String name){
        Locator docName=getDocumentLocator(name);
        driverTools.waitForElementToAppear(docName);
        return driverTools.isElementDisplayed(docName);
    }

    public String editDocument(){
        driverTools.waitForElementToAppear(BOOKING_LIST_SIDEBAR_ITEM,3);
        driverTools.click(EDIT_DOC_BUTTON);
        driverTools.click(DOC_TO_EDIT);
        driverTools.clearInput(DOC_TO_EDIT);
        driverTools.sendKeys(DOC_TO_EDIT, CoreFunctions.randomName()+"Edited");
        driverTools.click(SAVE_DOCUMENT);
        driverTools.click(NO_SEND_BUTTON);

        driverTools.waitForElementToAppear(SNACKBAR_TEXT);
        return driverTools.getText(SNACKBAR_TEXT).orElseThrow();
    }

    public boolean newlyAddedDocIsVisible(){
        driverTools.waitForElementToAppear(EDITED_DOC);
        return driverTools.isElementDisplayed(EDITED_DOC);
    }

    public String deleteDoc(){
        driverTools.click(EDIT_DOC_BUTTON);

        Assertion.assertThat("Verify Delete Option is present",driverTools.isElementDisplayed(DELETE_LOGO),is(true));
        driverTools.click(DELETE_LOGO);

        driverTools.click(SAVE_DOCUMENT);
        driverTools.waitForElementToAppear(SNACKBAR_TEXT);
        return driverTools.getText(SNACKBAR_TEXT).orElseThrow();
    }

    public void goToForeignCorp(){
        driverTools.click(FOREIGN_CORP);
    }

    public void goToGovtCorp(){
        driverTools.click(GOVT_CORPORATE);
    }

}
