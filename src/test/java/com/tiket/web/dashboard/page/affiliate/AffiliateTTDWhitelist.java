package com.tiket.web.dashboard.page.affiliate;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;

public class AffiliateTTDWhitelist<T extends WebDriver> extends BasePage<T> {
    public AffiliateTTDWhitelist(Session<T> session) {
        super(session);
    }

    private static final Locator TTD_API = new Locator("TTD_API", By.xpath("//p[text()='TTD API']"));
    private static final Locator WHITELIST_SECTION = new Locator("WHITELIST_SECTION", By.xpath("//div[text()='Whitelist']"));
    private static final Locator FILTER = new Locator("FILTER", By.xpath("//button[contains(@class, 'styles_clickable_icon__')]"));
    private static final Locator PRODUCT_NAME = new Locator("PRODUCT_NAME", By.xpath("(//li[text()='Product Name'])[1]"));
    private static final Locator ENTER_VALUE = new Locator("ENTER_VALUE", By.xpath("//div[contains(@class, 'styles_input_inner_wrapper__')]//input[@id='enter-value']"));
    private static final Locator SEARCH_BTN = new Locator("SEARCH_BTN", By.xpath("//button[text()='Search']"));
    private static final Locator REMOVE_BTN = new Locator("REMOVE_BTN",By.xpath("(//button[text()='Remove'])[1]"));
    private static final Locator CONFIRM_REMOVE_BTN = new Locator("CONFIRM_REMOVE_BTN", By.xpath("//div[contains(@class,'Figure_footer')]//button[text()='Remove']"));
    private static final Locator LIST_PRODUCT_NAME = new Locator("LIST_PRODUCT_NAME", By.xpath("(//td[text()='Konser Affi Part2'])[1]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", By.xpath("//div[contains(@class, 'Snackbar_text_content')]"));

    public void clickTTDWhitelist() {
        driverTools.waitForElementToAppear(TTD_API, 5);
        driverTools.click(TTD_API);
        driverTools.waitForElementToAppear(WHITELIST_SECTION, 5);
        driverTools.click(WHITELIST_SECTION);
    }

    public void inputProductName(String productName) {
        driverTools.waitForElementToAppear(FILTER, 5);
        driverTools.click(FILTER);
        driverTools.waitForElementToAppear(PRODUCT_NAME, 5);
        driverTools.click(PRODUCT_NAME);
        driverTools.waitForElementToAppear(ENTER_VALUE);
        driverTools.click(ENTER_VALUE);
        driverTools.sendKeys(ENTER_VALUE, productName);
    }

    public void searchByProductNameTTD(String listedProductName) {
        driverTools.waitForElementToAppear(SEARCH_BTN);
        driverTools.click(SEARCH_BTN);
        driverTools.waitForElementToAppear(LIST_PRODUCT_NAME, 5);
        Optional<String> actualMessage11 = driverTools.getText(LIST_PRODUCT_NAME);
        String actualMessage1 = actualMessage11.orElseThrow();
        Assertion.assertThat("Product Name present in list", actualMessage1, equalTo(listedProductName));
    }

    public void removeProductFromWhitelist() {
        driverTools.waitForElementToAppear(REMOVE_BTN, 5);
        driverTools.click(REMOVE_BTN);
        driverTools.waitForElementToAppear(CONFIRM_REMOVE_BTN, 5);
        driverTools.click(CONFIRM_REMOVE_BTN);
        logger.log("Product removed from whitelist successfully.");
    }

    public boolean isSnackbarAppeared() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 1);
        String snackbarText = driverTools.getText(SUCCESS_SNACKBAR).orElseThrow();
        logger.log("Snackbar text: " + snackbarText);
        return driverTools.isElementPresent(SUCCESS_SNACKBAR) && snackbarText.contains("sucessfully removed from whitelist");
    }
}