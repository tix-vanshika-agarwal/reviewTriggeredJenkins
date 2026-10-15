package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AffiliateLinkCreationPage <T extends WebDriver> extends WebPage<T> {

    public AffiliateLinkCreationPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_NEW_BUTTON = new Locator("ADD_NEW_BUTTON", By.xpath("//button[normalize-space()='Add New']"));
    private static final Locator URL_NAME= new Locator("URL_NAME", By.xpath("//input[@id='url-name']"));
    private static final Locator DESTINATION_URL = new Locator("DESTINATION_URL", By.xpath("//input[@id='destination-url']"));
    private static final Locator PARTNER_ID_INPUT = new Locator("PARTNER_ID_INPUT", By.xpath("//input[@id='search-by-partners-nameids']"));
    private static final Locator UTM_SOURCE = new Locator("UTM_SOURCE", By.xpath("//input[@id='utm-source']"));
    private static final Locator PRODUCT_TYPE = new Locator("PRODUCT_TYPE",By.xpath("//label[3]//span[1]//span[1]"));
    private static final Locator PRODUCT_TERM = new Locator("PRODUCT_TERM", By.xpath("//span[text()='UTM Term']"));
    private static final Locator UTM_CAMPAIGN = new Locator("UTM_CAMPAIGN", By.xpath("//input[@id='utm-campaign']"));
    private static final Locator TEST_DESCRIPTION= new Locator("TEST_DESCRIPTION", By.xpath("//textarea[@id='description']"));
    private static final Locator CREATE_BUTTON = new Locator("CREATE_BUTTON",By.xpath("//button[normalize-space()='Create Link']"));
    private static final Locator SUCCESS_LINK = new Locator("SUCCESS_LINK", By.xpath("//p[contains(text(), 'https://tiket.com')]"));
    private static final Locator FIRST_DROPDOWN_OPTION = new Locator("FIRST_DROPDOWN_OPTION", By.xpath("//ul[contains(@class,'InputSearch_dropdown')]/li[1]"));

    public void clickAddNewButton() {
        driverTools.waitForElementToAppear(ADD_NEW_BUTTON);
        driverTools.click(ADD_NEW_BUTTON);
    }

    public void addURLName(String urlName){
        driverTools.waitForElementToAppear(URL_NAME);
        driverTools.click(URL_NAME);
        driverTools.sendKeys(URL_NAME, urlName);
    }

    public void addDestinationURL(String partialURL) {
        String fullURL = "https://" + partialURL;
        driverTools.waitForElementToAppear(DESTINATION_URL);
        driverTools.click(DESTINATION_URL);
        driverTools.sendKeys(DESTINATION_URL, fullURL);
    }

    public void enterPartnerId(String partnerId) {
        driverTools.waitForElementToAppear(PARTNER_ID_INPUT);
        driverTools.click(PARTNER_ID_INPUT);
        driverTools.sendKeys(PARTNER_ID_INPUT, partnerId);

        driverTools.waitForElementToAppear(FIRST_DROPDOWN_OPTION);
        driverTools.click(FIRST_DROPDOWN_OPTION);
    }

    public void addUTMSource(String utmSource){
        driverTools.waitForElementToAppear(UTM_SOURCE);
        driverTools.click(UTM_SOURCE);
        driverTools.sendKeys(UTM_SOURCE, utmSource);
    }

    public void selectProductTypeAndProductTerm(){
        driverTools.waitForElementToAppear(PRODUCT_TYPE, 10);
        driverTools.waitForElementToBeClickable(PRODUCT_TYPE, 10);
        driverTools.click(PRODUCT_TYPE);
        driverTools.waitForElementToAppear(PRODUCT_TERM);
        driverTools.scrollIntoWebView(PRODUCT_TERM);
        driverTools.click(PRODUCT_TERM);
    }

    public void enterUTMCampaign(String utmCampaign){
        driverTools.waitForElementToAppear(UTM_CAMPAIGN);
        driverTools.sendKeys(UTM_CAMPAIGN, utmCampaign);
    }

    public void enterTestDescription(String testDescription){
        driverTools.waitForElementToAppear(TEST_DESCRIPTION);
        driverTools.sendKeys(TEST_DESCRIPTION, testDescription);
    }

    public void clickCreateButton(){
        driverTools.waitForElementToAppear(CREATE_BUTTON,10);
        driverTools.clickWithJavascriptExecutor(CREATE_BUTTON);
    }

    public boolean isLinkCreated(){
        driverTools.waitForElementToAppear(SUCCESS_LINK,10);
        return driverTools.isElementPresent(SUCCESS_LINK);
    }
}
