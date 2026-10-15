package com.tiket.web.dashboard.page.nft.car_rental;

import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.hamcrest.core.Is.is;

public class CarRentalVendorPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn']"));
    private static final Locator NAME_INPUT = new Locator("NAME_INPUT", new By.ByXPath("(//input[@type='text'])[1]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("(//input[@type='text'])[2]"));
    private static final Locator PHONE_INPUT = new Locator("PHONE_INPUT", new By.ByXPath("(//input[@type='text'])[3]"));
    private static final Locator SAVE_ACCOUNT_BUTTON = new Locator("SAVE_ACCOUNT_BUTTON", new By.ByXPath("//button[normalize-space()='SAVE' or normalize-space()='SIMPAN']"));
    private static final Locator EDIT_ACCOUNT_BUTTON = new Locator("EDIT_ACCOUNT_BUTTON", new By.ByXPath("(//i[@class='tixicon-edit'])[1]"));
    private static final Locator STATUS_DROPDOWN = new Locator("STATUS_DROPDOWN", new By.ByXPath("//div[text()='Order Status']"));
    private static final Locator SEARCH_PAYMENT_LIST_BUTTON = new Locator("SEARCH_PAYMENT_LIST_BUTTON", new By.ByXPath("//button[@class='tix-button v4 search desktop button waves-effect waves-default']"));
    private static final Locator EXPORT_BUTTON = new Locator("EXPORT_BUTTON", new By.ByXPath("//button[@class='tix-button v4 button-export desktop button waves-effect waves-danger danger']"));
    private static final Locator MODAL_POPUP = new Locator("MODAL_POPUP", new By.ByXPath("//div[@class='tix-figure v4 mobile']"));
    private static final Locator CLOSE_POPUP_BUTTON = new Locator("CLOSE_POPUP_BUTTON", new By.ByXPath("//button[@class='dialog-icon-close-wrapper']//*[name()='svg']"));
    private static final Locator ADD_CAR_POOL_BUTTON = new Locator("ADD_CAR_POOL_BUTTON", new By.ByXPath("//button[@class='tix-button v4 add-pool-button desktop button waves-effect waves-default']"));
    private static final Locator REGION_INPUT = new Locator("REGION_INPUT", new By.ByXPath("//div[(text()='Pilih Region')]"));
    private static final Locator CAR_POOL_NAME_INPUT = new Locator("CAR_POOL_NAME_INPUT", new By.ByXPath("//input[@name='poolName']"));
    private static final Locator ADD_ADDRESS_BUTTON = new Locator("ADD_ADDRESS_BUTTON", new By.ByXPath("//button[@class='tix-button v4 mobile button waves-effect waves-secondary block secondary']"));
    private static final Locator ADDRESS_INPUT = new Locator("ADDRESS_INPUT", new By.ByXPath("//div[@class='search-bar']//input"));
    private static final Locator FIRST_ADDRESS = new Locator("FIRST_ADDRESS", new By.ByXPath("//div[@id='modal-maps-carpool']//li[1]//div[1]"));
    private static final Locator SUBMIT_ADDRESS_BUTTON = new Locator("SUBMIT_ADDRESS_BUTTON", new By.ByXPath("(//button[@class='tix-button v4 mobile button waves-effect waves-default'])[2]"));
    private static final Locator ADDRESS_DETAILS = new Locator("ADDRESS_DETAILS", new By.ByXPath("//div[@class='maps-details']//p"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("(//button[@class='tix-button v4 mobile button waves-effect waves-default'])[1]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='tix-snackbar v4 type-default out-slide visible mobile']"));
    private static final Locator EDIT_CAR_POOL_BUTTON = new Locator("EDIT_CAR_POOL_BUTTON", new By.ByXPath("(//span[@class='b3 mobile high-emphasis hover tix-font v4' and text()='Edit'])[1]"));
    private static final Locator FIRST_CAR_POOL_NAME = new Locator("FIRST_CAR_POOL_NAME", new By.ByXPath("(//span[@class='b3 mobile high-emphasis hover tix-font v4'])[2]"));
    private static final Locator DELETE_CAR_POOL_BUTTON = new Locator("DELETE_CAR_POOL_BUTTON", new By.ByXPath("(//span[@class='b3 mobile high-emphasis hover tix-font v4' and text()='Delete'])[1]"));
    private static final Locator CAR_POOL_TOGGLE = new Locator("CAR_POOL_TOGGLE", new By.ByXPath("(//div[@class='lever'])[1]"));
    private static final Locator PROVINCE_DROPDOWN = new Locator("PROVINCE_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'react-select__value')])[1]"));
    private static final Locator CITY_DROPDOWN = new Locator("CITY_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'react-select__value')])[2]"));
    private static final Locator REGION_DROPDOWN = new Locator("REGION_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'react-select__value')])[3]"));
    private static final Locator REGIONAL_TOGGLE = new Locator("REGIONAL_TOGGLE", new By.ByXPath("(//div[@class='switch switch-blue'])//div[@class='lever']"));
    private static final Locator CONFIRM_POPUP_BUTTON = new Locator("CONFIRM_POPUP_BUTTON", new By.ByXPath("//button[@class='btn btn-grey btn-say-yes']"));
    private static final Locator CAR_MODEL_DROPDOWN = new Locator("CAR_MODEL_DROPDOWN", new By.ByXPath("//div[contains(@class, 'selected-model') and not(contains(@class, 'disabled'))]"));
    private static final Locator LOCATION_DROPDOWN = new Locator("LOCATION_DROPDOWN", new By.ByXPath("//div[contains(@class, 'selected-location') and not(contains(@class, 'disabled'))]"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn blue-btn']"));
    private static final Locator PRICE_CHECKBOX = new Locator("PRICE_CHECKBOX", new By.ByXPath("(//div[@class='check'])[1]"));
    private static final Locator ALLOTMENT_CHECKBOX = new Locator("ALLOTMENT_CHECKBOX", new By.ByXPath("(//div[@class='check'])[2]"));
    private static final Locator TOGGLE_DRIVER = new Locator("TOGGLE_DRIVER", new By.ByXPath("(//div[@class='check'])[3]"));
    private static final Locator PRICE_INPUT = new Locator("PRICE_INPUT", new By.ByXPath("(//input[@class='form-control input-curr input-currency checkPrice'])[1]"));
    private static final Locator ALLOTMENT_INPUT = new Locator("ALLOTMENT_INPUT", new By.ByXPath("//input[@placeholder='Allotment']"));
    private static final Locator PRICE_CHECKBOX_AUT = new Locator("PRICE_CHECKBOX_AUT", new By.ByXPath("//label[contains(text(), 'harga') and contains(text(), 'Automatic')]"));
    private static final Locator ALLOTMENT_CHECKBOX_AUT = new Locator("ALLOTMENT_CHECKBOX_AUT", new By.ByXPath("//label[contains(text(), 'allotment') and contains(text(), 'Automatic')]"));
    private static final Locator TOGGLE_AUT = new Locator("TOGGLE_AUT", new By.ByXPath("//label[contains(text(), 'switch') and contains(text(), 'Automatic')]"));
    private static final Locator PRICE_CHECKBOX_MANUAL = new Locator("PRICE_CHECKBOX_MANUAL", new By.ByXPath("//label[contains(text(), 'harga') and contains(text(), 'Manual')]"));
    private static final Locator ALLOTMENT_CHECKBOX_MANUAL = new Locator("ALLOTMENT_CHECKBOX_MANUAL", new By.ByXPath("//label[contains(text(), 'allotment') and contains(text(), 'Manual')]"));
    private static final Locator TOGGLE_MANUAL = new Locator("TOGGLE_MANUAL", new By.ByXPath("//label[contains(text(), 'switch') and contains(text(), 'Manual')]"));
    private static final Locator ALLOTMENT_INPUT_AUT = new Locator("ALLOTMENT_INPUT_AUT", new By.ByXPath("(//input[@placeholder='allotment'])[2]"));
    private static final Locator PRICE_INPUT_MANUAL = new Locator("PRICE_INPUT_MANUAL", new By.ByXPath("(//input[@class='form-control input-curr input-currency checkPrice'])[2]"));
    private static final Locator ALLOTMENT_INPUT_MANUAL = new Locator("ALLOTMENT_INPUT_MANUAL", new By.ByXPath("(//input[@placeholder='allotment'])[1]"));
    private static final Locator UPDATE_BUTTON = new Locator("UPDATE_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn update-price blue-btn']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn save-all']"));
    private static final Locator PRICE_TEXT = new Locator("PRICE_TEXT", new By.ByXPath("(//input[@placeholder='Price'])[2]"));
    private static final Locator ALLOTMENT_TEXT = new Locator("ALLOTMENT_TEXT", new By.ByXPath("(//input[contains(@class, 'form-control allotment-with')])[1]"));

    private static final Locator ADD_CAR_BUTTON = new Locator("ADD_CAR_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn btn-add-new-car']"));
    private static final Locator CAR_BRAND_DROPDOWN = new Locator("CAR_BRAND_DROPDOWN", new By.ByXPath("//div[contains(@class, 'selected-brand')]"));
    private static final Locator VENDOR_CAR_MODEL_DROPDOWN = new Locator("VENDOR_CAR_MODEL_DROPDOWN", new By.ByXPath("//div[contains(@class, 'selected-model')]"));
    private static final Locator SUBMIT_CAR_BUTTON = new Locator("SUBMIT_CAR_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn btn-submit-new-car']"));
    private static final Locator SEARCH_CAR_BRAND_DROPDOWN = new Locator("SEARCH_CAR_BRAND_DROPDOWN", new By.ByXPath("//div[contains(text(), 'All Brand') or contains(text(), 'Semua Brand')]"));
    private static final Locator REGIONAL_TITLE_TEXT = new Locator("REGIONAL_TITLE_TEXT", new By.ByXPath("//div[@class='regional-available']"));
    private static final Locator CAR_EDIT_BUTTON = new Locator("CAR_EDIT_BUTTON", new By.ByXPath("//i[@class='tixicon-edit']"));
    private static final Locator WITH_DRIVER_TAB = new Locator("WITH_DRIVER_TAB", new By.ByXPath("//a[contains(@class, 'nav-link') and contains(text(), 'With ')]"));
    private static final Locator WITHOUT_DRIVER_TAB = new Locator("WITHOUT_DRIVER_TAB", new By.ByXPath("//a[contains(@class, 'nav-link') and contains(text(), 'Without')]"));

    private static final Locator CONFIRM_BUTTON = new Locator("CONFIRM_BUTTON", new By.ByXPath("//span[@class='b2 mobile bold hover invert tix-font v4']"));
    private static final Locator CONFIRM_BUTTON_POPUP = new Locator("CONFIRM_BUTTON_POPUP", new By.ByXPath("//button[@class='tix-button v4 mobile button waves-effect waves-default block']"));
    private static final Locator SELECT_ADMIN_DROPDOWN = new Locator("SELECT_ADMIN_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'general-dropdown__value')])[1]"));
    private static final Locator ORDER_POPUP = new Locator("ORDER_POPUP", new By.ByXPath("//div[@class='modal-body']"));
    private static final Locator SUCCESS_CONFIRM_POPUP = new Locator("SUCCESS_CONFIRM_POPUP", new By.ByXPath("//div[@class='dialog-popup']"));
    private static final Locator OK_BUTTON_POPUP = new Locator("OK_BUTTON_POPUP", new By.ByXPath("//button[@class='tix-button v4 mobile button waves-effect waves-default large']"));
    private static final Locator SEARCH_ORDER_INPUT = new Locator("SEARCH_ORDER_INPUT", new By.ByXPath("//input[@name='orderIdOrName']"));
    private static final Locator SEARCH_ORDER_BUTTON = new Locator("SEARCH_ORDER_BUTTON", new By.ByXPath("//button[@class='tix-button v4 search desktop button waves-effect waves-default']"));
    private static final Locator ORDERS_PAGE = new Locator("ORDERS_PAGE", new By.ByXPath("//div[@class='manage-order-page']"));

    private static final Locator DELETE_ACCOUNT_BUTTON = new Locator("DELETE_ACCOUNT_BUTTON", new By.ByXPath("(//*[@class='tixicon-trash'])[1]"));
    private static final Locator ELLIPSIS_BUTTON = new Locator("ELLIPSIS_BUTTON", new By.ByXPath("//*[@class='more-horizontal-block']"));
    private static final Locator DOWNLOAD_EVOUCHER_BUTTON = new Locator("DOWNLOAD_EVOUCHER_BUTTON", new By.ByXPath("//*[@class='b2 mobile high-emphasis bold hover tix-font v4']"));

    public CarRentalVendorPage(Session<T> session) {
        super(session);
    }

    public void openManageAccountSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-ms/manage-account?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-ms/manage-account?businessId=" + businessProfileId);
        }
    }

    public boolean verifyResultIsDisplayed(String expectedResult) {
        Locator RESULT = new Locator("RESULT", new By.ByXPath("//td[contains(text(),'" + expectedResult + "')]"));
        driverTools.waitForElementToAppear(RESULT, 5);
        return driverTools.isElementDisplayed(RESULT);
    }

    public void addAccount(String name, String email, String phone) {
        driverTools.click(ADD_BUTTON);

        driverTools.click(NAME_INPUT);
        driverTools.sendKeys(NAME_INPUT, name);

        driverTools.click(EMAIL_INPUT);
        driverTools.sendKeys(EMAIL_INPUT, email);

        driverTools.click(PHONE_INPUT);
        driverTools.sendKeys(PHONE_INPUT, phone);

        driverTools.click(SAVE_ACCOUNT_BUTTON);
    }

    public boolean editAccount(String name) {
        driverTools.click(EDIT_ACCOUNT_BUTTON);
        driverTools.clear(NAME_INPUT);
        driverTools.click(NAME_INPUT);
        driverTools.sendKeys(NAME_INPUT, name);
        driverTools.click(SAVE_ACCOUNT_BUTTON);

        return verifyResultIsDisplayed(name);
    }

    public void openPaymentListSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-v4/finance/payment-list?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-v4/finance/payment-list?businessId=" + businessProfileId);
        }
    }

    public void exportPaymentList() {
        closePopup();
        driverTools.click(STATUS_DROPDOWN);
        WebElement statusDropdown = driver.switchTo().activeElement();
        statusDropdown.sendKeys(Keys.DOWN);
        statusDropdown.sendKeys(Keys.RETURN);
        driverTools.click(SEARCH_PAYMENT_LIST_BUTTON);
        driverTools.click(EXPORT_BUTTON);
    }

    public void closePopup() {
        driverTools.waitForElementToAppear(MODAL_POPUP, 2);
        if (driverTools.isElementDisplayed(MODAL_POPUP)) {
            driverTools.click(CLOSE_POPUP_BUTTON);
        }
    }

    public void openManageCarPoolSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-v4/manage-carpool?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-v4/manage-carpool?businessId=" + businessProfileId);
        }
        closePopup();
    }


    public boolean addCarPool(String region, String name) {
        driverTools.click(ADD_CAR_POOL_BUTTON);

        driverTools.click(REGION_INPUT);
        WebElement regionInput = driver.switchTo().activeElement();
        regionInput.sendKeys(Keys.RETURN);

        driverTools.click(CAR_POOL_NAME_INPUT);
        driverTools.sendKeys(CAR_POOL_NAME_INPUT, name);

        driverTools.click(ADD_ADDRESS_BUTTON);
        driverTools.sendKeys(ADDRESS_INPUT, region);
        driverTools.waitForElementToAppear(FIRST_ADDRESS, 5);
        driverTools.click(FIRST_ADDRESS);

        driverTools.waitForElementToAppear(ADDRESS_DETAILS, 5);
        driverTools.click(SUBMIT_ADDRESS_BUTTON);
        driverTools.click(SUBMIT_BUTTON);

        return verifyCarPoolResultIsDisplayed(name);
    }

    public boolean verifySuccessSnackbar() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public boolean editCarPool(String region) {
        driverTools.click(EDIT_CAR_POOL_BUTTON);

        driverTools.click(ADD_ADDRESS_BUTTON);
        driverTools.sendKeys(ADDRESS_INPUT, region);
        driverTools.waitForElementToAppear(FIRST_ADDRESS, 5);
        driverTools.click(FIRST_ADDRESS);

        driverTools.waitForElementToAppear(ADDRESS_DETAILS, 5);
        driverTools.click(SUBMIT_ADDRESS_BUTTON);
        driverTools.click(SUBMIT_BUTTON);

        return verifySuccessSnackbar() && verifyCarPoolResultIsDisplayed(region);
    }

    public boolean deleteCarPool() {
        String carPoolName = driverTools.waitForElementToAppear(FIRST_CAR_POOL_NAME).orElseThrow().getText();
        driverTools.click(DELETE_CAR_POOL_BUTTON);
        driverTools.click(SUBMIT_BUTTON);
        return verifySuccessSnackbar() && !verifyCarPoolResultIsDisplayed(carPoolName);
    }

    public void toggleCarPool() {
        driverTools.click(CAR_POOL_TOGGLE);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean deactivateCarPoolStatus() {
        toggleCarPool();
        return verifySuccessSnackbar() && verifyCarPoolResultIsDisplayed("Tidak Aktif");
    }

    public boolean activateCarPoolStatus() {
        toggleCarPool();
        return verifySuccessSnackbar() && verifyCarPoolResultIsDisplayed("Aktif");
    }

    public boolean verifyCarPoolResultIsDisplayed(String status) {
        Locator CAR_POOL_RESULT = new Locator("CAR_POOL_RESULT", new By.ByXPath("(//span[@class='b3 mobile high-emphasis hover tix-font v4' and text()='" + status + "'])[1]"));
        driverTools.waitForElementToAppear(CAR_POOL_RESULT, 5);
        return driverTools.isElementDisplayed(CAR_POOL_RESULT);
    }

    public void openManageRegionalSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-ms/regional?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-ms/regional?businessId=" + businessProfileId);
        }
    }

    public void addRegional(String province) {
        driverTools.click(PROVINCE_DROPDOWN);
        WebElement provinceDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(provinceDropdown, province);
        driverTools.sendKeys(provinceDropdown, Keys.RETURN.toString());

        driverTools.click(CITY_DROPDOWN);
        WebElement cityDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(cityDropdown, Keys.RETURN.toString());

        driverTools.click(REGION_DROPDOWN);
        WebElement regionDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(regionDropdown, Keys.RETURN.toString());

        driverTools.click(ADD_BUTTON);
    }

    public void toggleRegional() {
        driverTools.click(REGIONAL_TOGGLE);
        driverTools.click(CONFIRM_POPUP_BUTTON);
    }

    public boolean deactivateRegionalStatus() {
        toggleRegional();
        return verifyResultIsDisplayed("InActive") || verifyResultIsDisplayed("Tidak Aktif") ;
    }

    public boolean activateRegionalStatus() {
        toggleRegional();
        return verifyResultIsDisplayed("Active") || verifyResultIsDisplayed("Aktif") ;
    }

    public void openCarAllotmentSubMenu(String type, String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-ms/rates-allotment/" + type + "?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-ms/rates-allotment/" + type + "?businessId=" + businessProfileId);
        }
    }

    public void searchCarAllotment() {
        driverTools.click(CAR_MODEL_DROPDOWN);
        WebElement carModelDropdown = driver.switchTo().activeElement();
        carModelDropdown.sendKeys(Keys.RETURN);

        driverTools.waitForElementToAppear(LOCATION_DROPDOWN);
        driverTools.click(LOCATION_DROPDOWN);
        WebElement locationDropdown = driver.switchTo().activeElement();
        locationDropdown.sendKeys(Keys.DOWN);
        locationDropdown.sendKeys(Keys.RETURN);

        driverTools.click(SEARCH_BUTTON);
    }

    public void setCarAllotmentWithDriver(String price, String allotment) {
        driverTools.click(PRICE_CHECKBOX);
        driverTools.click(ALLOTMENT_CHECKBOX);
        driverTools.click(TOGGLE_DRIVER);

        driverTools.click(PRICE_INPUT);
        driverTools.sendKeys(PRICE_INPUT, price);

        driverTools.click(ALLOTMENT_INPUT);
        driverTools.sendKeys(ALLOTMENT_INPUT, allotment);

        driverTools.click(UPDATE_BUTTON);
        driverTools.click(CONFIRM_POPUP_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public void setCarAllotmentWithoutDriver(String price, String allotment) {
        driverTools.click(PRICE_CHECKBOX_AUT);
        driverTools.click(ALLOTMENT_CHECKBOX_AUT);
        driverTools.click(TOGGLE_AUT);

        driverTools.click(PRICE_CHECKBOX_MANUAL);
        driverTools.click(ALLOTMENT_CHECKBOX_MANUAL);
        driverTools.click(TOGGLE_MANUAL);

        driverTools.click(PRICE_INPUT);
        driverTools.sendKeys(PRICE_INPUT, price);
        driverTools.click(PRICE_INPUT_MANUAL);
        driverTools.sendKeys(PRICE_INPUT_MANUAL, price);

        driverTools.click(ALLOTMENT_INPUT_AUT);
        driverTools.sendKeys(ALLOTMENT_INPUT_AUT, allotment);
        driverTools.click(ALLOTMENT_INPUT_MANUAL);
        driverTools.sendKeys(ALLOTMENT_INPUT_MANUAL, allotment);

        driverTools.click(UPDATE_BUTTON);
        driverTools.click(CONFIRM_POPUP_BUTTON);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean verifyPriceAndAllotmentAreDisplayed(String price, String allotment) {
        boolean priceStatus = false;
        boolean allotmentStatus = false;

        String priceText = driverTools.getValueAttribute(PRICE_TEXT, "value").replace(".", "");
        logger.log("Price text: " + priceText);
        if (priceText.equals(price)) {
            priceStatus = true;
        }

        String allotmentText = driverTools.getValueAttribute(ALLOTMENT_TEXT, "value");
        logger.log("Allotment text: " + allotmentText);
        if (allotmentText.equals(allotment)) {
            allotmentStatus = true;
        }

        return priceStatus && allotmentStatus;
    }

    public void openManageCarSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-ms/manage-car?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-ms/manage-car?businessId=" + businessProfileId);
        }
    }

    public void addCar(String carBrand, String carModel) {
        driverTools.click(ADD_CAR_BUTTON);

        driverTools.click(CAR_BRAND_DROPDOWN);
        WebElement carBrandDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(carBrandDropdown, carBrand);
        driverTools.sendKeys(carBrandDropdown, Keys.RETURN.toString());

        driverTools.click(VENDOR_CAR_MODEL_DROPDOWN);
        WebElement carModelDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(carModelDropdown, carModel);
        driverTools.sendKeys(carModelDropdown, Keys.RETURN.toString());

        driverTools.scrollToElement(SUBMIT_CAR_BUTTON);
        driverTools.click(SUBMIT_CAR_BUTTON);
    }

    public boolean searchCarBrand(String carBrand) {
        driverTools.click(SEARCH_CAR_BRAND_DROPDOWN);
        WebElement searchCarBrandDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(searchCarBrandDropdown, carBrand);
        driverTools.sendKeys(searchCarBrandDropdown, Keys.RETURN.toString());

        return verifyResultIsDisplayed(carBrand);
    }

    public void addOrEditQuantity(String carType, String inputType, String qty) {
        String index = getIndexForLocator(carType);
        Locator QTY_INPUT = getInputLocator(inputType, index);
        driverTools.click(QTY_INPUT);
        driverTools.clear(QTY_INPUT);
        driverTools.sendKeys(QTY_INPUT, qty);

        WebElement element = driverTools.waitForElementToAppear(REGIONAL_TITLE_TEXT).orElseThrow();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();

        driverTools.click(CONFIRM_POPUP_BUTTON);
    }

    public String getIndexForLocator(String carType) {
        if (carType.equalsIgnoreCase("With Driver")) {
            return "1";
        } else if (carType.equalsIgnoreCase("Without Driver")) {
            return "2";
        }
        throw new IllegalArgumentException("Invalid carType: " + carType);
    }

    private Locator getInputLocator(String inputType, String index) {
        Locator QTY_INPUT = null;
        if (inputType.equals("price")) {
            QTY_INPUT = new Locator("QTY_INPUT", new By.ByXPath("(//input[@placeholder='Price'])[ " + index + "]"));
        } else if (inputType.equals("allotment")) {
            QTY_INPUT = new Locator("QTY_INPUT", new By.ByXPath("(//input[contains(@class, 'form-control allotment')])[" + index + "]"));
        }
        return QTY_INPUT;
    }

    public void toggleCar(String carType) {
        String index = getIndexForLocator(carType);
        Locator TAB = new Locator("TAB", new By.ByXPath("//ul[contains(@class,'nav-is-driver')]//a[normalize-space()='" + carType + "']"));
        WebElement element = driverTools.waitForElementToAppear(TAB).orElseThrow();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        driverTools.click(TAB);
        Locator CAR_TOGGLE = new Locator("CAR_TOGGLE", new By.ByXPath("(//div[contains(@class,'tab-pane') and contains(@class,'active')]//div[contains(@class,'switch switch-blue')])[" + index + "]"));
        driverTools.waitForElementToAppear(CAR_TOGGLE, 5);
        driverTools.click(CAR_TOGGLE);
        driverTools.click(CONFIRM_POPUP_BUTTON);
    }

    public void clickCarEditButton() {
        driverTools.click(CAR_EDIT_BUTTON);
    }

    public void openWithDriverTab() {
        WebElement element = driverTools.waitForElementToAppear(WITH_DRIVER_TAB).orElseThrow();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        driverTools.click(WITH_DRIVER_TAB);
    }

    public void openWithoutDriverTab() {
        WebElement element = driverTools.waitForElementToAppear(WITHOUT_DRIVER_TAB).orElseThrow();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        driverTools.click(WITHOUT_DRIVER_TAB);
    }

    public void openDriverTabMenu() {
        driverTools.click(
                new Locator(
                        "ALLOTMENT_CHECKBOX",
                        new By.ByXPath("//button[normalize-space(text())='Edit']")
                )
        );
    }

    public void openOrdersSubMenu(String businessProfileId) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/connect/car-v4/manage-orders/allOrders?businessId=" + businessProfileId);
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/connect/car-v4/manage-orders/allOrders?businessId=" + businessProfileId);
        }
    }

    private Locator getOrdersTabLocator(String type) {
        return new Locator("ORDER_TAB", new By.ByXPath("//span[@class='b1 mobile low-emphasis bold hover tix-font v4' and text()='" + type + "']"));
    }

    public void openOrdersTab(String type) {
        Locator ORDER_TAB = getOrdersTabLocator(type);
        driverTools.waitForElementToAppear(ORDER_TAB, 5);
        driverTools.click(ORDER_TAB);
    }

    public void confirmPickupOrder() {
        driverTools.click(CONFIRM_BUTTON);
        driverTools.click(CONFIRM_BUTTON_POPUP);
    }

    public void confirmOrder() {
        driverTools.click(CONFIRM_BUTTON);
        scrollDownOrderPopup();

        driverTools.click(SELECT_ADMIN_DROPDOWN);
        WebElement dropdown = driver.switchTo().activeElement();
        dropdown.sendKeys(Keys.RETURN);

        driverTools.click(CONFIRM_BUTTON_POPUP);
    }

    public void scrollDownOrderPopup() {
        driverTools.click(ORDER_POPUP);
        Actions action = new Actions(driver);
        for (int i = 0; i < 10; i++){
            action.sendKeys(Keys.ARROW_DOWN);
            action.perform();
        }
        driverTools.click(ORDER_POPUP);
    }

    public void verifySuccessConfirmPopupIsDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_CONFIRM_POPUP, 5);
        Assertion.assertThat("Success popup is displayed for successful confirm order", driverTools.isElementDisplayed(SUCCESS_CONFIRM_POPUP), is(true));
        driverTools.click(OK_BUTTON_POPUP);
    }

    private Locator getOrderIdTextLocator(String orderId) {
        return new Locator("ORDER_ID_TEXT", new By.ByXPath("//td[text()='" + orderId + "']"));
    }

    public boolean searchOrder(String orderId) {
        scrollUpOrdersPage();

        driverTools.click(SEARCH_ORDER_INPUT);
        driverTools.sendKeys(SEARCH_ORDER_INPUT, orderId);
        driverTools.click(SEARCH_ORDER_BUTTON);

        Locator ORDER_ID_TEXT = getOrderIdTextLocator(orderId);
        driverTools.waitForElementToAppear(ORDER_ID_TEXT, 5);
        return driverTools.isElementDisplayed(ORDER_ID_TEXT);
    }

    public void scrollUpOrdersPage() {
        driverTools.click(ORDERS_PAGE);
        Actions action = new Actions(driver);
        for (int i = 0; i < 10; i++){
            action.sendKeys(Keys.ARROW_UP);
            action.perform();
        }
        driverTools.click(ORDERS_PAGE);
    }

    public void deleteAccount() {
        driverTools.click(DELETE_ACCOUNT_BUTTON);
        driverTools.click(CONFIRM_POPUP_BUTTON);
    }

    public void downloadEvoucher() {
        driverTools.click(ELLIPSIS_BUTTON);
        driverTools.click(DOWNLOAD_EVOUCHER_BUTTON);
    }
}