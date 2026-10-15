package com.tiket.web.dashboard.page.payment;

import com.mongodb.client.FindIterable;
import com.tiket.dbclient.TiketMongoDBManager;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.bson.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;

public class PaymentDisplayCustomConfiguration<T extends WebDriver> extends BasePage<T> {
    private static final Locator PAYMENT_DISPLAY_CUSTOM_CONFIGURATION = new Locator("PAYMENT_DISPLAY_CUSTOM_CONFIGURATION", By.xpath("//span[text()='Payment Display Custom Configuration']"));
    private static final Locator IDR_VERTICAL_EXPIRY_TIME = new Locator("IDR_VERTICAL_EXPIRY_TIME", By.xpath("(//span[@class='HcPVsG_text HcPVsG_size_b3'])[7]"));
    private static final Locator NON_IDR_VERTICAL_EXPIRY_TIME = new Locator("NON_IDR_VERTICAL_EXPIRY_TIME", By.xpath("(//span[@class='HcPVsG_text HcPVsG_size_b3'])[9]"));
    private static final Locator PRODUCT_TYPE = new Locator("PRODUCT_TYPE", By.xpath("(//input[contains(@class,'has_clickable_icon')])[1]"));
    private static final Locator SAVE_CHANGES = new Locator("SAVE_CHANGES", By.xpath("//button[contains(@class,'KV-nSG_variant_primary') and normalize-space()='Save Changes']"));
    private static final Locator SELECT_MINUTES = new Locator("SELECT_MINUTES", By.xpath("//input[@id='minutes']"));
    private static final Locator SAVE = new Locator("SAVE", By.xpath("//button[@type='submit']"));
    private static final Locator SUBMIT = new Locator("SUBMIT", By.xpath("//button[text()='Submit']"));
    private static final Locator FERRY = new Locator("FERRY", By.xpath("//*[text()='FERRY']"));
    private static final Locator ADD_NEW_EXPIRY_TIME = new Locator("ADD_NEW_EXPIRY_TIME", By.xpath("//button[text()='Add New Expiry Time']"));
    private static final Locator TOGGLE = new Locator("TOGGLE", By.xpath("//div[@class='n8SXEW_toggle n8SXEW_toggle_large']"));
    private static final Locator SUCCESS = new Locator("SUCCESS", By.xpath("(//div[text()='Successfully saved expiry data.'])[1]"));
    private static final Locator ENABLE = new Locator("ENABLE", By.xpath("//*[text()='Enable']"));
    private static final Locator SEARCH = new Locator("SEARCH", By.xpath("//button[text()='Search']"));
    private static final Locator EDIT = new Locator("EDIT", By.xpath("(//div[@class='cursor-pointer'])[1]"));
    private static final Locator DISABLE = new Locator("DISABLE", By.xpath("//*[text()='Disable']"));
    private static final String PAYMENTV2_URL = "https://preprod.tiket.com/admin/payment_v2";
    private static final Locator CHECK_STATUS = new Locator("CHECK_STATUS", By.xpath("(//div[contains(@class,'eTYLaa_label eTYLaa_variant_outline')])[1]"));
    private static final String FILE_PATH = "counter.txt";
    private int currentExpiryTime;

    public PaymentDisplayCustomConfiguration(Session<T> session) {
        super(session);
    }

    public void openCustomConfiguration() throws InterruptedException {
        driverTools.get(PAYMENTV2_URL);
        driverTools.waitForElementToAppear(PAYMENT_DISPLAY_CUSTOM_CONFIGURATION);
        driverTools.click(PAYMENT_DISPLAY_CUSTOM_CONFIGURATION);
        driverTools.waitForElementToAppear(IDR_VERTICAL_EXPIRY_TIME);
        driverTools.click(IDR_VERTICAL_EXPIRY_TIME);
    }

    public void openNonIDRCustomConfiguration() {
        driverTools.get(PAYMENTV2_URL);
        driverTools.waitForElementToAppear(PAYMENT_DISPLAY_CUSTOM_CONFIGURATION);
        driverTools.click(PAYMENT_DISPLAY_CUSTOM_CONFIGURATION);
        driverTools.waitForElementToAppear(NON_IDR_VERTICAL_EXPIRY_TIME);
        driverTools.click(NON_IDR_VERTICAL_EXPIRY_TIME);
    }

    public static int getNextNumber() throws IOException {
        int number = 1;
        if (Files.exists(Paths.get(FILE_PATH))) {
            String content = Files.readString(Paths.get(FILE_PATH)).trim();
            if (!content.isEmpty()) {
                number = Integer.parseInt(content) + 1;
            }
        }
        Files.write(Paths.get(FILE_PATH), String.valueOf(number).getBytes());
        return number;
    }


    public void createNewExpiryTime() throws InterruptedException, IOException {
        driverTools.waitForElementToAppear(ADD_NEW_EXPIRY_TIME);
        driverTools.click(ADD_NEW_EXPIRY_TIME);
        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        driverTools.click(FERRY);
        int newNumber = getNextNumber();
        this.currentExpiryTime = newNumber;
        driverTools.click(SELECT_MINUTES);
        driverTools.sendKeys(SELECT_MINUTES, String.valueOf(currentExpiryTime));
        driverTools.scroll(1, 300);
    }

    public void createNewExpiryTimeWithConfig(String productType, int minutes) throws InterruptedException {
        Locator productTypeOption = new Locator(productType, By.xpath("//*[text()='" + productType + "']"));
        driverTools.waitForElementToAppear(ADD_NEW_EXPIRY_TIME);
        driverTools.click(ADD_NEW_EXPIRY_TIME);

        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        driverTools.click(productTypeOption);

        driverTools.click(SELECT_MINUTES);
        driverTools.sendKeys(SELECT_MINUTES, String.valueOf(minutes));
        driverTools.scroll(1, 300);
    }

    public void setPaymentMethodStatus(String paymentMethod, String status) {
        Locator dropdown = new Locator("DROPDOWN_" + paymentMethod,
                By.xpath("//span[text()='" + paymentMethod + "']/ancestor::div[contains(@class,'flex justify-between')]//button[@class='XmDoza_clickable_icon']"));
        Locator option = new Locator("OPTION_" + status,
                By.xpath("//div[@data-testid='dropdown-item-" + status + "']"));
        driverTools.waitForElementToPresence(dropdown);
        driverTools.click(dropdown);

        driverTools.waitForElementToAppear(option);
        driverTools.click(option);
    }

    public void setPaymentMethodStatusForCurrency(String currency, String paymentMethod, String status) {
        Locator dropdown = new Locator("DROPDOWN_" + currency + "_" + paymentMethod,
                By.xpath("//span[normalize-space()='" + currency + "']" +
                        "/ancestor::div[contains(@class,'card_container')][1]" +
                        "//span[normalize-space()='" + paymentMethod + "']" +
                        "/ancestor::div[contains(@class,'justify-between')][1]" +
                        "//button//*[name()='svg']"));

        Locator option = new Locator("OPTION_" + currency + "_" + paymentMethod + "_" + status,
                By.xpath("//div[@data-testid='dropdown-item-" + status + "']"));

        driverTools.waitForElementToAppear(dropdown);
        driverTools.click(dropdown);

        driverTools.waitForElementToAppear(option);
        driverTools.click(option);
    }

    public void verifyExpiryTimeRowStatus(int minutes, String expectedStatus) {
        Locator statusLabel = new Locator("STATUS_LABEL_FOR_" + minutes + "_MINUTES",
                By.xpath("//tr[.//p[contains(text(),'< " + minutes + " Minutes')]]//div[contains(@class,'label_eda787')]"));
        driverTools.waitForElementToAppear(statusLabel);
        String actualStatus = driverTools.getText(statusLabel)
                .orElseThrow(() -> new TimeoutException("Status label not found for " + minutes + " minutes row"));
        Assertion.assertThat("Expiry row status for " + minutes + " minutes", actualStatus, equalTo(expectedStatus));
    }

    public void clickEditForExpiryTime(int minutes) {
        Locator editBtn = new Locator("EDIT_FOR_" + minutes + "_MINUTES",
                By.xpath("//tr[.//p[contains(text(),'< " + minutes + " Minutes')]]//*[@class='cursor-pointer']"));
        driverTools.waitForElementToAppear(editBtn);
        driverTools.click(editBtn);
    }

    public void expandCurrencySection(String paymentMethod) {
        Locator paymentMethodHeadeLocator = new Locator("CURRENCY_HEADER_" + paymentMethod,
                By.xpath("//span[contains(@class,'HcPVsG_weight_bold') and text()='" + paymentMethod + "']/ancestor::div[contains(@style,'cursor')]"));
        driverTools.waitForElementToAppear(paymentMethodHeadeLocator);
        driverTools.click(paymentMethodHeadeLocator);
    }

    public void changeToggleStatus() {
        driverTools.waitForElementToAppear(TOGGLE);
        driverTools.clickWithJavascriptExecutor(TOGGLE);

    }

    public void clickSave() {
        driverTools.waitForElementToAppear(SAVE);
        driverTools.clickWithJavascriptExecutor(SAVE);
    }

    public void clickSubmit() {
        driverTools.waitForElementToAppear(SUBMIT);
        driverTools.clickWithJavascriptExecutor(SUBMIT);
    }

    public void clickSaveChanges() {
        driverTools.waitForElementToAppear(SAVE_CHANGES);
        driverTools.clickWithJavascriptExecutor(SAVE_CHANGES);
    }

    public void enablePaymentOptions() throws InterruptedException {
        List<WebElement> PAYMENT_DROPDOWNS = driver.findElements(By.xpath("(//input[contains(@class,'has_clickable_icon')])"));

        for (int i = 27; i < PAYMENT_DROPDOWNS.size(); i++) {
            WebElement dropdown = PAYMENT_DROPDOWNS.get(i);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(dropdown));
            dropdown.click();
            driverTools.click(ENABLE);
        }
        driverTools.clickWithJavascriptExecutor(TOGGLE);
        driverTools.waitForElementToAppear(SAVE);
        driverTools.clickWithJavascriptExecutor(SAVE);
        driverTools.waitForElementToAppear(SUBMIT);
        driverTools.click(SUBMIT);
    }

    public boolean successSaved() {
        driverTools.waitForElementToAppear(SUCCESS, 5);
        String actualMessage = driverTools.getText(SUCCESS)
                .orElseThrow(() -> new TimeoutException("Snackbar did not appear or disappeared too quickly."));
        String expectedMessage = "Successfully saved expiry data.";
        Assertion.assertThat("SUCCESS", actualMessage, equalTo(expectedMessage));
        return true;
    }

    public void deactivateCustomConfiguration() throws InterruptedException {
        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        driverTools.click(FERRY);
        driverTools.click(SEARCH);
        driverTools.waitForElementToAppear(EDIT);
        driverTools.click(EDIT);
        driverTools.waitForElementToAppear(TOGGLE);
        driverTools.clickWithJavascriptExecutor(TOGGLE);
        driverTools.waitForElementToAppear(SAVE);
        driverTools.clickWithJavascriptExecutor(SAVE);
        driverTools.waitForElementToAppear(SAVE_CHANGES);
        driverTools.click(SAVE_CHANGES);

    }

    public void editCustomConfiguration() throws InterruptedException, IOException {
        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        Thread.sleep(500);
        driverTools.click(FERRY);
        driverTools.click(SEARCH);
        driverTools.waitForElementToAppear(EDIT);
        driverTools.click(EDIT);
        int newNumber1 = currentExpiryTime;
        String existingValue = driverTools.getValue(SELECT_MINUTES);
        String updatedValue = existingValue + "_" + newNumber1;
        driverTools.click(SELECT_MINUTES);
        driverTools.sendKeys(SELECT_MINUTES, updatedValue);
        WebElement value_enable = driver.findElement(By.xpath("(//input[@value='Enable'])[1]"));
        WebElement EDIT_PAYMENT_DROPDOWN = driver.findElement(By.xpath("(//input[contains(@class,'has_clickable_icon')])[2]"));
        EDIT_PAYMENT_DROPDOWN.click();
        if (value_enable.isDisplayed()) {
            driverTools.click(DISABLE);
        } else {
            driverTools.click(ENABLE);
        }
        driverTools.waitForElementToAppear(SAVE);
        driverTools.clickWithJavascriptExecutor(SAVE);
        driverTools.waitForElementToAppear(SAVE_CHANGES);
        driverTools.click(SAVE_CHANGES);
    }

    public boolean checkExpiryPageStatus() throws InterruptedException {
        Thread.sleep(500);
        driverTools.waitForElementToAppear(IDR_VERTICAL_EXPIRY_TIME);
        driverTools.click(IDR_VERTICAL_EXPIRY_TIME);
        driverTools.waitForElementToAppear(PRODUCT_TYPE);
        driverTools.click(PRODUCT_TYPE);
        driverTools.click(FERRY);
        driverTools.click(SEARCH);
        driverTools.waitForElementToAppear(CHECK_STATUS, 10);
        Optional<String> actualMessage11 = driverTools.getText(CHECK_STATUS);
        String actualMessage1 = actualMessage11.orElseThrow(() -> new TimeoutException("Status element not found"));
        String expectedMessage1 = "Inactive";
        Assertion.assertThat("CHECK_STATUS", actualMessage1, equalTo(expectedMessage1));
        return true;
    }

    public boolean fetchNewExpiryTimeFromMongoDB(String configType, String dataBase, String collectionName) {
        String query = "{\"productType\": \"FERRY\", \"expiredTime\": " + currentExpiryTime + "}";
        FindIterable<Document> result = TiketMongoDBManager
                .searchInMongoDB(configType, dataBase, collectionName, query);
        logger.log("this is the query -> " + query);
        return result.iterator().hasNext();

    }

    public boolean fetchInactiveExpiryTimeFromMongoDB(String configType, String dataBase, String collectionName) {
        String query = "{\"productType\": \"FERRY\", \"active\": false}";
        FindIterable<Document> result = TiketMongoDBManager
                .searchInMongoDB(configType, dataBase, collectionName, query);
        logger.log("this is the query -> " + query);
        return result.iterator().hasNext();

    }

    public boolean fetchEditedExpiryTimeFromMongoDB(String configType, String dataBase, String collectionName) {
        String query = "{\"productType\": \"FERRY\",\"listPaymentV1.CREDITCARD\":\"Disable\"}";
        FindIterable<Document> result = TiketMongoDBManager
                .searchInMongoDB(configType, dataBase, collectionName, query);
        logger.log("this is the query -> " + query);
        return result.iterator().hasNext();
    }
}


