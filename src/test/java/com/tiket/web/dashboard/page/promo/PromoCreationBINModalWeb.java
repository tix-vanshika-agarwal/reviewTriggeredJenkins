package com.tiket.web.dashboard.page.promo;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.web.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class PromoCreationBINModalWeb<T extends WebDriver> extends WebPage<T> {
    public PromoCreationBINModalWeb(Session<T> session) {
        super(session);
    }

    private static final Locator BIN_MODAL = new Locator("BIN_MODAL", By.xpath("//section[contains(@class, 'Modal') and .//h2[.='Select BIN']]"));
    private static final Locator BIN_SEARCH_INPUT = new Locator("BIN_SEARCH_INPUT", By.xpath("//input[@id='bin']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", By.xpath("//button[contains(@class, 'primary') and text()='Search']"));
    private static final Locator FIRST_BIN_CHECKBOX = new Locator("FIRST_BIN_CHECKBOX", By.xpath("(//table[contains(@class, 'bin_table')]/tbody//input[@type='checkbox'])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", By.xpath("//button[contains(@class, 'primary') and text()='Save']"));
    private static final Locator BANK_BIN_TABLE = new Locator("BANK_BIN_TABLE", By.xpath("//table[contains(@class, 'bin_table')]"));
    private static final Locator BANK_NAME_INPUT = new Locator("BANK_NAME_INPUT", By.xpath("//input[@id='bank-name']"));
    private static final Locator COUNTRY_INPUT = new Locator("COUNTRY_INPUT", By.xpath("//input[@id='country']"));
    private static final Locator CARD_TYPE_INPUT = new Locator("CARD_TYPE_INPUT", By.xpath("//input[@id='card-type']"));
    private static final Locator CARD_SCHEME_INPUT = new Locator("CARD_SCHEME_INPUT", By.xpath("//input[@id='card-scheme']"));
    private static final Locator RESET_FILTER_BUTTON = new Locator("RESET_FILTER_BUTTON", By.xpath("//button[text()='Reset Filter']"));
    private static final Locator BIN_TABLE_DATA_ROW = new Locator("BIN_TABLE_DATA_ROW",
            By.xpath("//table[contains(@class, 'bin_table')]/tbody/tr[not(.//td[@colspan])]"));

    public int getBinTableColumnIndex(String colName) {
        Locator loc = new Locator("", By.xpath(
                "//table[contains(@class, 'bin_table')]/thead/tr/th[normalize-space()='" + colName + "']/preceding-sibling::*"));
        List<WebElement> webElements = driver.findElements(loc.by());
        return webElements.size() + 1;
    }

    private Locator getSelection(String text){
        return new Locator("SELECT_INSIDE_DROPDOWN_LOCATOR", By.xpath("//div[contains(@class, 'styles_dropdown')]//span[text()='" + text + "']"));
    }

    public void inputBin(String bin) {
        driverTools.waitForElementToAppear(BIN_MODAL);
        driverTools.scrollIntoWebView(BIN_SEARCH_INPUT);
        driverTools.waitForElementToBeClickable(BIN_SEARCH_INPUT, 10);
        driverTools.clickWithJavascriptExecutor(BIN_SEARCH_INPUT);
        WebElement input = driverTools.waitForElementToAppear(BIN_SEARCH_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, bin);
        input.sendKeys(Keys.ENTER);
        driverTools.dummyWait(3);
    }

    public void inputBankName(String bankName) {
        WebElement input = driverTools.waitForElementToAppear(BANK_NAME_INPUT).orElseThrow();
        driverTools.click(input);
        driverTools.clearInput(input);
        driverTools.sendKeys(input, bankName);
        input.sendKeys(Keys.ENTER);
        driverTools.dummyWait(2);
    }

    public void resetFilter() {
        driverTools.click(RESET_FILTER_BUTTON);
    }

    public void verifyFilterHasBeenReset() {
        boolean isBinEnabled = driver.findElement(BIN_SEARCH_INPUT.by()).isEnabled();
        boolean isBankEnabled = driver.findElement(BANK_NAME_INPUT.by()).isEnabled();
        boolean isCountryEnabled = driver.findElement(COUNTRY_INPUT.by()).isEnabled();
        boolean isCardTypeEnabled = driver.findElement(CARD_TYPE_INPUT.by()).isEnabled();
        boolean isCardSchemeEnabled = driver.findElement(CARD_SCHEME_INPUT.by()).isEnabled();

        Assertion.assertThat("Verify that BIN search input is enabled after reset", isBinEnabled, equalTo(true));
        Assertion.assertThat("Verify that Bank Name input is enabled after reset", isBankEnabled, equalTo(true));
        Assertion.assertThat("Verify that Country input is enabled after reset", isCountryEnabled, equalTo(true));
        Assertion.assertThat("Verify that Card Type input is enabled after reset", isCardTypeEnabled, equalTo(true));
        Assertion.assertThat("Verify that Card Scheme input is enabled after reset", isCardSchemeEnabled, equalTo(true));
    }

    public boolean verifyInputIsDisabled(String input) {
        return  switch (input) {
            case "BIN" -> !driver.findElement(BIN_SEARCH_INPUT.by()).isEnabled();
            case "BANK_NAME" -> !driver.findElement(BANK_NAME_INPUT.by()).isEnabled();
            case "COUNTRY" -> !driver.findElement(COUNTRY_INPUT.by()).isEnabled();
            case "CARD_TYPE" -> !driver.findElement(CARD_TYPE_INPUT.by()).isEnabled();
            case "CARD_SCHEME" -> !driver.findElement(CARD_SCHEME_INPUT.by()).isEnabled();
            default -> false;
        };
    }

    public void clickSearch() {
        WebElement element = driverTools.waitForElementToPresence(SEARCH_BUTTON).orElseThrow();
        driverTools.click(element);
    }

    public void selectTheFirstBin() {
        WebElement element = driverTools.waitForElementToPresence(FIRST_BIN_CHECKBOX).orElseThrow();
        driverTools.scrollElementToCenter(element);
        driverTools.clickElementByCoordinateDWeb(FIRST_BIN_CHECKBOX);
    }

    public String getTheFirstBinValue(String binColumnName) {
        int colIndex = getBinTableColumnIndex(binColumnName);
        Locator loc = new Locator("FIRST_BIN_VALUE", By.xpath("(//table[contains(@class, 'bin_table')]//td[" + colIndex + "])[1]"));
        return driverTools.getText(loc).orElseThrow();
    }

    public void clickSaveButton() {
        driverTools.clickWithJavascriptExecutor(SAVE_BUTTON);
    }

    private Locator binTableColumnLocator(int colIndex) {
        return new Locator("BIN_TABLE_COLUMN_" + colIndex,
                By.xpath("//table[contains(@class, 'bin_table')]/tbody/tr[not(.//td[@colspan])]//td[" + colIndex + "]"));
    }

    public void checkBinTableColumnsContainValue(String colName, String value) {
        driverTools.waitForElementsToAppear(BANK_BIN_TABLE);
        driverTools.waitForElementToAppear(BIN_TABLE_DATA_ROW);
        int colIndex = getBinTableColumnIndex(colName);
        List<WebElement> elements = driverTools.waitForElementsToAppear(binTableColumnLocator(colIndex)).orElseThrow();

        boolean result = false;
        for (WebElement element: elements) {
            if (driverTools.getText(element).equalsIgnoreCase(value)) {
                result = true;
                break;
            }
        }

        Assertion.assertThat("Verify that value " + value + " contained in the BIN Table in the column " + colName, result, equalTo(true));
    }
}
