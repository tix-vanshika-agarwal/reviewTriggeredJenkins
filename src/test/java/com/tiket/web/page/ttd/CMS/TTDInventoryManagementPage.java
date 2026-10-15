package com.tiket.web.page.ttd.CMS;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.WebPage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class TTDInventoryManagementPage <T extends WebDriver> extends WebPage<T> {

    public TTDInventoryManagementPage(Session<T> session) {
        super(session);
    }

    private static final Locator INVENTORY_MANAGEMENT=new Locator("INVENTORY_MANAGEMENT", By.xpath("//span[normalize-space()='Inventory Management']"));
    private static final Locator PRODUCT_LIST = new Locator("PRODUCT_LIST", By.xpath("//a[@class='sidebar__nav-submenu-item' and normalize-space(text())='Product List']"));
    private static final Locator PRODUCT_URL_DROPDOWN = new Locator("PRODUCT_URL_DROPDOWN", By.xpath("//div[contains(@class, 'tix-react-select__value-container') and .//div[text()='Product Name']]"));
    private static final Locator FULL_URL= new Locator("FULL_URL", By.xpath("//div[text()='Full URL']"));
    private static final Locator SEARCH_BOX= new Locator("SEARCH_BOX", By.xpath("//div[contains(@class, 'input-box')][div[normalize-space()='Search']]/input"));
    private static final Locator SEARCH_BTN= new Locator("SEARCH_BTN", By.xpath("//button[normalize-space()='Search']"));
    private static final Locator FIRST_ROW_DATA= new Locator("FIRST_ROW_DATA", By.xpath("//tbody/tr[1]"));
    private static final Locator FIRST_ROW_DATA_NAME = new Locator("FIRST_ROW_DATA_URL", By.xpath("//tbody/tr[1]//td[2]"));
    private static final Locator FIRST_ROW_DATA_ID = new Locator("FIRST_ROW_DATA_ID", By.xpath("//tbody/tr[1]//td[3]"));
    private static final Locator FIRST_ROW_DATA_SUPPLIER_ID = new Locator("FIRST_ROW_DATA_SUPPLIER_ID", By.xpath("//tbody/tr[1]//td[4]"));
    private static final Locator FIRST_ROW_DATA_URL = new Locator("FIRST_ROW_DATA_URL", By.xpath("//tbody/tr[1]//td[5]"));
    private static final Locator PROMO_LOYALTY =new Locator("PROMO_LOYALTY", By.xpath("//span[normalize-space()='Promo & Loyalty']"));
    private static final Locator CAMPAIGN_MENU = new Locator("CAMPAIGN_MENU", By.xpath("//a[@class='sidebar__nav-submenu-item' and normalize-space(text())='Campaign']"));
    private static final Locator CAMPAIGN_SEARCH_NAME = new Locator("CAMPAIGN_SEARCH_NAME", By.xpath("//div[contains(@class,'input-wrapper')]//input"));
    private static final Locator CAMPAIGN_SEARCH_BTN = new Locator("CAMPAIGN_SEARCH_BTN", By.xpath("//button[contains(@class,'mobile button waves') and contains(text(), 'Search')]"));
    private static final Locator CAMPAIGN_FIRST_ROW_NAME = new Locator("FIRST_ROW_DATA_CAMPAIGN", By.xpath("//tbody/tr[1]//td[2]"));
    private static final Locator CAMPAIGN_STATUS = new Locator("CAMPAIGN_STATUS", By.xpath("//div[contains(@class,'tix-react-select__indicators')]"));
    private static final Locator CAMPAIGN_STATUS_DROPDOWN = new Locator("CAMPAIGN_STATUS_DROPDOWN", By.xpath("//div[contains(@class,'tix-react-select__option')]"));
    private static final Locator CAMPAIGN_TOGGLE = new Locator("CAMPAIGN_TOGGLE", By.xpath("//button[contains(@class,'table-item')]"));
    private static final Locator CAMPAIGN_ACTIVE_TOGGLE = new Locator("CAMPAIGN_ACTIVE_TOGGLE", By.xpath("//button[contains(@class,'table-item')]/div[contains(@class,'tix-toggle') and contains(@class,'checked')]"));
    private static final Locator SUPPLIER_PRODUCT_ID= new Locator("SUPPLIER_PRODUCT_ID", By.xpath("//div[text()='Supplier Product ID']"));
    private static final Locator PRODUCT_ID= new Locator("PRODUCT_ID", By.xpath("//div[text()='Product ID']"));

    public void  gotoToDoAdminPage(){
        if (Configs.ENVIRONMENT == Environment.GK) {
            driver.get("https://gatotkaca.tiket.com/admin/event-ms/to-do/dashboard");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driver.get("https://preprod.tiket.com/admin/event-ms/to-do/dashboard");
        } else if (Configs.ENVIRONMENT == Environment.PROD) {
            driver.get("https://tiket.com/admin/event-ms/to-do/dashboard");
        }
    }


    public void selectProductList(){
        driverTools.waitForElementToAppear(INVENTORY_MANAGEMENT);
        driverTools.click(INVENTORY_MANAGEMENT);
        driverTools.waitForElementToAppear(PRODUCT_LIST, 20);
        driverTools.clickWithJavascriptExecutor(PRODUCT_LIST);
        driverTools.refreshWebpage();
    }

    public void enterProductNameInSearch(String productName) {
        driverTools.waitForElementToAppear(SEARCH_BOX);
        driverTools.click(SEARCH_BOX);

        driverTools.sendKeys(SEARCH_BOX,productName);
    }

    public void enterFullURL(String productUrl){
        driverTools.waitForElementToAppear(PRODUCT_URL_DROPDOWN);
        driverTools.click(PRODUCT_URL_DROPDOWN);

        driverTools.waitForElementToAppear(FULL_URL);
        driverTools.click(FULL_URL);

        driverTools.waitForElementToAppear(SEARCH_BOX);
        driverTools.click(SEARCH_BOX);
        driverTools.sendKeys(SEARCH_BOX,productUrl);
    }

    public void clickSearchButton() {
        driverTools.waitForElementToAppear(SEARCH_BTN);
        driverTools.clickWithJavascriptExecutor(SEARCH_BTN);
    }

    public Boolean isFirstRowProductVisible(
            String searchKey,
            String productName,
            String productUrl,
            String productID,
            String supplierId) {
        driverTools.waitForElementToAppear(FIRST_ROW_DATA,30).orElseThrow();

        driverTools.dummyWait(3);
        String searchName = driverTools.getText(FIRST_ROW_DATA_NAME).orElseThrow();
        String searchUrl = driverTools.getText(FIRST_ROW_DATA_URL).orElseThrow();
        String searchId = driverTools.getText(FIRST_ROW_DATA_ID).orElseThrow();
        String searchSupplierId = driverTools.getText(FIRST_ROW_DATA_SUPPLIER_ID).orElseThrow();

        logger.log("searchKey result: "+searchKey);

        switch (searchKey) {
            case "productName" -> {
                logger.log("Product name : "+ productName + ", searchName: " + searchName);
                if (searchName.equalsIgnoreCase(productName)){
                    return true;
                }
                return false;
            } case "productUrl" -> {
                logger.log("Product URL : "+ productUrl + ", searchUrl: " + searchUrl);
                if (searchUrl.equalsIgnoreCase(productUrl)){
                    return true;
                }
                return false;
            } case "supplierId" -> {
                logger.log("Supplier product Id : "+ supplierId + ", searchSupplierId: " + searchSupplierId);
                if (searchSupplierId.equalsIgnoreCase(supplierId)){
                    return true;
                }
            return false;
            } case "productID" -> {
                logger.log("Product Id : "+ productID + ", searchId: " + searchId);
                if (searchId.equalsIgnoreCase(productID)){
                    return true;
                }
            return false;
            } default -> {
                logger.log("Invalid search key: " + searchKey);
                return false;
            }
        }
    }

    public void selectCampaignMenu(){
        driverTools.waitForElementToAppear(PROMO_LOYALTY);
        driverTools.click(PROMO_LOYALTY);
        driverTools.waitForElementToAppear(CAMPAIGN_MENU, 20);
        driverTools.clickWithJavascriptExecutor(CAMPAIGN_MENU);
        driverTools.refreshWebpage();
    }

        public void inputCampaignName(String campaignName) {
        driverTools.waitForElementToAppear(CAMPAIGN_SEARCH_NAME);
        driverTools.clickWithJavascriptExecutor(CAMPAIGN_SEARCH_NAME);
        driverTools.sendKeys(CAMPAIGN_SEARCH_NAME,campaignName);
    }

        public void clickSearchCampaign() {
        driverTools.waitForElementToAppear(CAMPAIGN_SEARCH_BTN);
        driverTools.scrollToElement(CAMPAIGN_SEARCH_BTN);
        driverTools.clickWithJavascriptExecutor(CAMPAIGN_SEARCH_BTN);
    }

    public String getCampaignName() {
        driverTools.waitForElementToPresence(CAMPAIGN_FIRST_ROW_NAME);
        String result = driverTools.getText(CAMPAIGN_FIRST_ROW_NAME).orElseThrow();
        logger.log("Got campaign name "+result);
        return result;
    }

    public Boolean checkCampaignResultName(String campaignName) {
        driverTools.waitForElementToAppear(CAMPAIGN_FIRST_ROW_NAME,10).orElseThrow();
        String searchData = driverTools.getText(CAMPAIGN_FIRST_ROW_NAME).orElseThrow();
        return searchData.contains(campaignName);
    }

    public void chooseCampaignStatus(int statusIndex){
        driverTools.waitForElementToPresence(CAMPAIGN_STATUS, 10).orElseThrow();
        driverTools.click(CAMPAIGN_STATUS);
        List<WebElement> dropdowns = driverTools.elements(CAMPAIGN_STATUS_DROPDOWN).orElseThrow();
        driverTools.click(dropdowns.get(statusIndex));
    }
    public int countAllCampaign(){
        driverTools.waitForElementToPresence(CAMPAIGN_TOGGLE, 10).orElseThrow();
        List<WebElement> campaignToggle = driverTools.elements(CAMPAIGN_TOGGLE).orElseThrow();
        return campaignToggle.size();
    }
    public int countAllActiveCampaign() {
        driverTools.waitForElementToPresence(CAMPAIGN_ACTIVE_TOGGLE, 10).orElseThrow();
        List<WebElement> campaignActiveToggle = driverTools.elements(CAMPAIGN_ACTIVE_TOGGLE).orElseThrow();
        return campaignActiveToggle.size();
    }

    public void enterSupplierProductIdInSearch(String supplierId) {
        driverTools.waitForElementToAppear(SEARCH_BOX);
        driverTools.click(SEARCH_BOX);

        driverTools.sendKeys(SEARCH_BOX,supplierId);
    }

    public void enterSupplierProductID(){
        driverTools.waitForElementToAppear(PRODUCT_URL_DROPDOWN);
        driverTools.click(PRODUCT_URL_DROPDOWN);

        driverTools.waitForElementToAppear(SUPPLIER_PRODUCT_ID);
        driverTools.click(SUPPLIER_PRODUCT_ID);
    }

    public void productID(){
        driverTools.waitForElementToAppear(PRODUCT_URL_DROPDOWN);
        driverTools.click(PRODUCT_URL_DROPDOWN);

        driverTools.waitForElementToAppear(PRODUCT_ID);
        driverTools.click(PRODUCT_ID);
    }
}



