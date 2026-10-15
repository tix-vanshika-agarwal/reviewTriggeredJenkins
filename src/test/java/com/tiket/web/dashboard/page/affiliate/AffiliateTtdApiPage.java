package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;



public class AffiliateTtdApiPage<T extends WebDriver> extends BasePage<T> {
    public AffiliateTtdApiPage(Session<T> session) {
        super(session);
    }
    private static final Locator TTD_API = new Locator("TTD_API", By.xpath("//p[normalize-space()='TTD API']"));
    private static final Locator TTD_WHITELIST = new Locator("TTD_WHITELIST", By.xpath("(//div[normalize-space()='Whitelist'])[last()]"));
    private static final Locator TTD_BUSINESS_LOG = new Locator("TTD_BUSINESS_LOG", By.xpath("(//div[normalize-space()='Business Log'])[2]"));
    private static final Locator TTD_WHITELIST_FILTER = new Locator("TTD_WHITELIST_FILTER", By.xpath("//button[@type='button']"));
    private static final Locator TTD_WHITELIST_FILTER_PRODUCTID = new Locator("TTD_WHITELIST_FILTER", By.xpath("//li[normalize-space()='Product ID']"));
    private static final Locator TTD_WHITELIST_FILTER_PRODUCTID_VALUE = new Locator("TTD_WHITELIST_FILTER_PRODUCTID_VALUE", By.xpath("//input[@id='enter-value']"));
    private static final Locator TTD_WHITELIST_SEARCH = new Locator("TTD_WHITELIST_SEARCH", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator TTD_API_ADD_SINGLE= new Locator("TTD_API_ADD_SINGLE", By.xpath("//button[normalize-space()='Add Single']"));
    private static final Locator TTD_API_ADD_SINGLE_PRODUCTID= new Locator("TTD_API_ADD_SINGLE_PRODUCTID", By.xpath("//input[@placeholder='Product ID']"));
    private static final Locator TTD_API_ADD_SINGLE_PRODUCTID_SNACKBAR= new Locator("TTD_API_ADD_SINGLE_PRODUCTID_SNACKBAR", By.xpath("//div[normalize-space()='Product is successfully whitelisted']"));
    private static final Locator TTD_API_ADDTOWHITELIST_BTN= new Locator("TTD_API_ADDTOWHITELIST_BTN", By.xpath("//button[normalize-space()='Add to Whitelist']"));


    private Locator getFirstDataRowLocator(String firstPartnerId) {
        String xpath = String.format("//td[normalize-space()='%s']", firstPartnerId);
        return new Locator("FIRST_DATA_ROW", new By.ByXPath(xpath));
    }

    public void clickTtdApiSection(){
        driverTools.click(TTD_API);
    }

    public void clickTtdApiWhitelist(){
        driverTools.waitForElementToPresence(TTD_WHITELIST);
        driverTools.click(TTD_WHITELIST);
    }

    public void clickTtdApiWhitelistFilter(){
        driverTools.click(TTD_WHITELIST_FILTER);
    }

    public void clickTtdApiWhitelistFilterProductId(){
        driverTools.click(TTD_WHITELIST_FILTER_PRODUCTID);
    }

    public void enterProductIDValue(String partnerId) {
        driverTools.sendKeys(TTD_WHITELIST_FILTER_PRODUCTID_VALUE,partnerId);

    }

    public void clickTtdApiWhitelistSearch(){
        driverTools.click(TTD_WHITELIST_SEARCH);
    }

    public String getFirstRowProductTypeTTDAPI(String firstPartnerId) {
        Locator firstRowData = getFirstDataRowLocator(firstPartnerId);
        driverTools.waitForElementToAppear(firstRowData, 4).orElseThrow();
        return driverTools.getText(firstRowData).orElseThrow();
    }

    public void clickTtdApiAddSingleProduct(){
        driverTools.click(TTD_API_ADD_SINGLE);
    }

    public void enterTtdApiAddSingleProductProductId(String partnerID){
        driverTools.waitForElementToAppear(TTD_API_ADD_SINGLE_PRODUCTID);
        driverTools.click(TTD_API_ADD_SINGLE_PRODUCTID);
        driverTools.sendKeys(TTD_API_ADD_SINGLE_PRODUCTID,partnerID);
    }

    public String getTextAddProductSnackBar() {
        driverTools.waitForElementToAppear(TTD_API_ADD_SINGLE_PRODUCTID_SNACKBAR);
        driverTools.scrollToElement(TTD_API_ADD_SINGLE_PRODUCTID_SNACKBAR);
        return driverTools.getText(TTD_API_ADD_SINGLE_PRODUCTID_SNACKBAR).orElseThrow();
    }

    public void clickTtdApiAddToWhiteListBtn(){
        driverTools.click(TTD_API_ADDTOWHITELIST_BTN);
    }
    public void clickTtdApiBusinessLog() {
        driverTools.click(TTD_BUSINESS_LOG);
    }
}