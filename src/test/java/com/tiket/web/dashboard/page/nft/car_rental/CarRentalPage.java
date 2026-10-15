package com.tiket.web.dashboard.page.nft.car_rental;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Environment;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.dashboard.page.member_dashboard.MemberLoginPage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;

public class CarRentalPage<T extends WebDriver> extends BasePage<T> {
    private static final Locator USERNAME_GREETING = new Locator("USERNAME_GREETING", new By.ByXPath("//div[contains(@class, 'header_dashboard')]"));
    private static final Locator ADD_BUTTON = new Locator("ADD_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn']"));
    private static final Locator CAR_BRAND_INPUT = new Locator("CAR_BRAND_INPUT", new By.ByXPath("//input[@class='form-control']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", new By.ByXPath("//button[@class='btn btn-blue']"));
    private static final Locator SUCCESS_ALERT = new Locator("SUCCESS_ALERT", new By.ByXPath("//div[contains(@class, 'toast--success')]"));
    private static final Locator SEARCH_INPUT = new Locator("SEARCH_INPUT", new By.ByXPath("//input[@class='form-control search-keyword']"));
    private static final Locator SEARCH_BUTTON = new Locator("SEARCH_BUTTON", new By.ByXPath("//i[@class='tixicon-search']"));
    private static final Locator BUSINESS_PROFILE_ID_TEXT = new Locator("BUSINESS_PROFILE_ID_TEXT", new By.ByXPath("//td[3]"));
    private static final Locator EDIT_BUTTON = new Locator("EDIT_BUTTON", new By.ByXPath("//i[@class='tixicon-edit']"));
    private static final Locator TOGGLE = new Locator("TOGGLE", new By.ByXPath("//div[@class='switch switch-blue']"));
    private static final Locator YES_BUTTON = new Locator("YES_BUTTON", new By.ByXPath("//button[@class='btn btn-grey grey btn-say-yes']"));
    private static final Locator CAR_BRAND_DROPDOWN = new Locator("CAR_BRAND_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'react-select__control')])[1]"));
    private static final Locator CAR_MODEL_DROPDOWN = new Locator("CAR_MODEL_DROPDOWN", new By.ByXPath("(//div[contains(@class, 'react-select__control')])[2]"));
    private static final Locator CAR_MODEL_INPUT = new Locator("CAR_MODEL_INPUT", new By.ByXPath("(//input[@class='form-control'])[1]"));
    private static final Locator MAX_PASSENGER_INPUT = new Locator("MAX_PASSENGER_INPUT", new By.ByXPath("(//input[@class='form-control'])[2]"));
    private static final Locator MAX_BAGGAGE_INPUT = new Locator("MAX_BAGGAGE_INPUT", new By.ByXPath("(//input[@class='form-control'])[3]"));
    private static final Locator FACTORY_INPUT = new Locator("FACTORY_INPUT", new By.ByXPath("(//input[@class='form-control'])[4]"));
    private static final Locator PRIORITY_SCORE_INPUT = new Locator("PRIORITY_SCORE_INPUT", new By.ByXPath("(//input[@class='form-control'])[5]"));
    private static final Locator IMAGE_UPLOAD = new Locator("IMAGE_UPLOAD", new By.ByXPath("//input[@class='input-primary-photo']"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//a[@class='this-app-add-new-row-btn float-right']"));
    private static final Locator SEARCH_FILTER_DROPDOWN = new Locator("SEARCH_FILTER_DROPDOWN", new By.ByXPath("//div[@class='col-3 col-dropdown']"));
    private static final Locator ADD_V4_BUTTON = new Locator("ADD_V4_BUTTON", new By.ByXPath("//button[@class='tix-button v4 desktop button waves-effect waves-default']"));
    private static final Locator EDIT_BUSINESS_PROFILE_BUTTON = new Locator("EDIT_BUSINESS_PROFILE_BUTTON", new By.ByXPath("//span[@class='b2 mobile high-emphasis activity hover tix-font v4']"));
    private static final Locator SEARCH_V4_INPUT = new Locator("SEARCH_V4_INPUT", new By.ByXPath("//input[@name='search-text']"));
    private static final Locator BUSINESS_NAME_INPUT = new Locator("BUSINESS_NAME_INPUT", new By.ByXPath("//input[@name='businessName']"));
    private static final Locator DEFAULT_CONTACT_CHECKBOX = new Locator("DEFAULT_CONTACT_CHECKBOX", new By.ByXPath("//input[@class='is-invalid form-check-input']"));
    private static final Locator CONTACT_NAME_INPUT = new Locator("CONTACT_NAME_INPUT", new By.ByXPath("//input[@name='contactPerson.0.name']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@name='contactPerson.0.mobile']"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@name='contactPerson.0.email']"));
    private static final Locator SAVE_BUSINESS_PROFILE_BUTTON = new Locator("SAVE_BUSINESS_PROFILE_BUTTON", new By.ByXPath("//button[@class='tix-button v4 button-no mobile button waves-effect waves-default block']"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//span[@class='b2 message mobile high-emphasis hover invert tix-font v4']"));
    private static final Locator FACILITY_NAME_ID_INPUT = new Locator("FACILITY_NAME_ID_INPUT", new By.ByXPath("//input[contains(@placeholder, 'Name Paid Facilities')]"));
    private static final Locator FACILITY_NAME_EN_INPUT = new Locator("FACILITY_NAME_EN_INPUT", new By.ByXPath("//input[contains(@placeholder, 'Name Paid Facilities (English)')]"));
    private static final Locator FACILITY_IMAGE_UPLOAD = new Locator("FACILITY_IMAGE_UPLOAD", new By.ByXPath("//input[@name='sourcefile']"));

    private static final Locator ADD_CAMPAIGN_LIST_BUTTON = new Locator("ADD_CAMPAIGN_LIST_BUTTON", new By.ByXPath("//button[@class='tix-button v4 mobile button waves-effect waves-default']"));
    private static final Locator CAMPAIGN_NAME_ID_FIELD = new Locator("CAMPAIGN_NAME_ID_FIELD", new By.ByXPath("//div[@class='id-language']//input[@name='campaign-name']"));
    private static final Locator CAMPAIGN_NAME_EN_FIELD = new Locator("CAMPAIGN_NAME_EN_FIELD", new By.ByXPath("//div[@class='en-language']//input[@name='campaign-name']"));
    private static final Locator REGIONAL_DROPDOWN = new Locator("REGIONAL_DROPDOWN", new By.ByXPath("//div[contains(text(), 'Regional') and contains(@class, 'Campaign')]"));
    private static final Locator CAR_DROPDOWN = new Locator("CAR_DROPDOWN", new By.ByXPath("//div[contains(text(), 'Mobil') and contains(@class, 'Campaign')]"));
    private static final Locator VENDOR_DROPDOWN = new Locator("VENDOR_DROPDOWN", new By.ByXPath("//div[contains(text(), 'Vendor') and contains(@class, 'Campaign')]"));

    private static final Locator CARD_LABEL_GIMMICK_CHECKBOX = new Locator("CARD_LABEL_GIMMICK_CHECKBOX", new By.ByXPath("//div[@class='tix-check v4 label-left cardLabelGimmick has-label']"));
    private static final Locator CAMPAIGN_ID_TEXT_FIELD_CL = new Locator("CAMPAIGN_ID_TEXT_FIELD_CL", new By.ByXPath("//div[@class='cardLabelGimmick-content-left']//div[@class='id-language']//textarea"));
    private static final Locator CAMPAIGN_EN_TEXT_FIELD_CL = new Locator("CAMPAIGN_EN_TEXT_FIELD_CL", new By.ByXPath("//div[@class='cardLabelGimmick-content-left']//div[@class='en-language']//textarea"));
    private static final Locator TEXT_COLOR_FIELD_CL = new Locator("TEXT_COLOR_FIELD_CL", new By.ByXPath("//input[@name='input-text-color-hex']"));
    private static final Locator LABEL_COLOR_FIELD = new Locator("LABEL_COLOR_FIELD", new By.ByXPath("//input[@name='label-color']"));
    private static final Locator CARD_LABEL_TEXT = new Locator("CARD_LABEL_TEXT", new By.ByXPath("(//small[@class='mobile low-emphasis hover small tix-font v4'])[4]"));
    private static final Locator LABEL_COLOR = new Locator("LABEL_COLOR", new By.ByXPath("//div[@class='tab-pane active']//div[@class='cards-label']"));

    private static final Locator SPECIAL_SECTION_CHECKBOX = new Locator("SPECIAL_SECTION_CHECKBOX", new By.ByXPath("//div[@class='tix-check v4 label-left specialSection has-label']"));
    private static final Locator CAMPAIGN_ID_TEXT_FIELD_SS = new Locator("CAMPAIGN_ID_TEXT_FIELD_SS", new By.ByXPath("//div[@class='specialSection-content-left']//div[@class='id-language']//textarea"));
    private static final Locator CAMPAIGN_EN_TEXT_FIELD_SS = new Locator("CAMPAIGN_EN_TEXT_FIELD_SS", new By.ByXPath("//div[@class='specialSection-content-left']//div[@class='en-language']//textarea"));
    private static final Locator BACKGROUND_COLOR_FIELD = new Locator("BACKGROUND_COLOR_FIELD", new By.ByXPath("//input[@name='input-background-color-hex']"));
    private static final Locator TEXT_COLOR_FIELD_SS = new Locator("TEXT_COLOR_FIELD_SS", new By.ByXPath("//input[@name='input-label-color-hex']"));
    private static final Locator BACKGROUND_COLOR = new Locator("BACKGROUND_COLOR", new By.ByXPath("//div[@class='padding-block']"));
    private static final Locator SPECIAL_SECTION_TEXT = new Locator("SPECIAL_SECTION_TEXT", new By.ByXPath("//span[@class='b2 mobile high-emphasis bold hover invert tix-font v4']"));

    private static final Locator STRIKEOUT_PRICE_CHECKBOX = new Locator("STRIKEOUT_PRICE_CHECKBOX", new By.ByXPath("//div[@class='tix-check v4 label-left hargaCoret has-label']"));
    private static final Locator CAMPAIGN_PRICE = new Locator("CAMPAIGN_PRICE", new By.ByXPath("//span[@class='b2 price mobile high-emphasis hover tix-font v4']"));
    private static final Locator BY_NOMINAL_RADIO_BUTTON = new Locator("BY_NOMINAL_RADIO_BUTTON", new By.ByXPath("(//span[@class='b1 mobile high-emphasis hover tix-font v4'][text()='By Nominal'])[1]"));
    private static final Locator MARKUP_PRICE_INPUT = new Locator("MARKUP_PRICE_INPUT", new By.ByXPath("//input[@name='mark-up-price']"));
    private static final Locator DISCOUNT_PRICE_INPUT = new Locator("DISCOUNT_PRICE_INPUT", new By.ByXPath("//input[@name='discount-price']"));

    private static final Locator CAMPAIGN_TOGGLE = new Locator("CAMPAIGN_TOGGLE", new By.ByXPath("//i[@class='tix-toggle-icon']"));
    private static final Locator CAMPAIGN_STATUS = new Locator("CAMPAIGN_STATUS", new By.ByXPath("//span[@class='b2 label mobile high-emphasis hover tix-font v4']"));

    private static final Locator PAYMENT_LIST_LOADING_POPUP = new Locator("PAYMENT_LIST_LOADING_POPUP", new By.ByXPath("//div[@class='tix-component-loading v4 fullscreen']"));
    private static final Locator VENDOR_NAME_FIELD = new Locator("VENDOR_NAME_FIELD", new By.ByXPath("//div[contains(@class,'general-dropdown') and contains(text(), 'Vendor')]"));
    private static final Locator ORDER_STATUS_DROPDOWN = new Locator("ORDER_STATUS_DROPDOWN", new By.ByXPath("//div[contains(@class,'general-dropdown') and contains(text(), 'Order')]"));
    private static final Locator PAYMENT_STATUS_DROPDOWN = new Locator("PAYMENT_STATUS_DROPDOWN", new By.ByXPath("//div[contains(@class,'general-dropdown') and contains(text(), 'Pembayaran')]"));
    private static final Locator SEARCH_PAYMENT_LIST_BUTTON = new Locator("SEARCH_PAYMENT_LIST_BUTTON", new By.ByXPath("//button[@type='submit']"));
    private static final Locator EXPORT_BUTTON = new Locator("EXPORT_BUTTON", new By.ByXPath("//button[@class='tix-button v4 button-export desktop button waves-effect waves-danger danger']"));
    private static final Locator UPLOAD_SUCCESS_SNACKBAR = new Locator("UPLOAD_SUCCESS_SNACKBAR", new By.ByXPath("//div[@class='tix-snackbar v4 type-success out-slide visible mobile']"));
    private static final Locator DELETE_BUTTON_CAR_BRAND = new Locator("DELETE_BUTTON_CAR_BRAND", new By.ByXPath("//*[@class='tixicon-trash']"));



    private static final Locator DELETE_BUTTON = new Locator("DELETE_BUTTON", new By.ByXPath("//*[@class='tixicon-trash']"));
    private static final Locator DELETE_BUSINESS_PROFILE_BUTTON = new Locator("DELETE_BUSINESS_PROFILE_BUTTON", new By.ByXPath("(//*[@class='b2 mobile high-emphasis activity hover tix-font v4'])[2]"));
    private static final Locator POPUP_DELETE_BUTTON = new Locator("POPUP_DELETE_BUTTON", new By.ByXPath("//*[@class='tix-button v4 mobile button waves-effect waves-default large']"));

    private static final Locator FINANCE_MENU = new Locator("FINANCE_MENU", new By.ByXPath("//a[contains(., 'Finance')]"));
    private static final Locator PAYMENT_LIST_MENU = new Locator("PAYMENT_LIST_MENU", new By.ByXPath("//a[contains(., 'Payment List')]"));
    private static final Locator ORDER_ID_SEARCH_INPUT = new Locator("ORDER_ID_SEARCH_INPUT", new By.ByXPath("//*[@class='input-text v4']"));
    private static final Locator EMPTY_TABLE = new Locator("EMPTY_TABLE", new By.ByXPath("//*[@class='empty-table']"));
    private static final Locator ORDER_ID_CELL = new Locator("ORDER_ID_CELL", new By.ByXPath("(//*[@class='sticky-col second-col'])[2]"));
    private static final Locator PAYMENT_INPUT = new Locator("PAYMENT_INPUT", new By.ByXPath("//*[@class='upload']"));

    private static final Locator CAR_RENTAL_HOMEPAGE_CARD = new Locator("CAR_RENTAL_HOMEPAGE_CARD", By.xpath("//a[contains(@href, '/admin/car-ms')]"));
    private static final Locator CAR_RENTAL_SIDEBAR = new Locator("CAR_RENTAL_SIDEBAR", By.xpath("//aside[@class='sidebar']"));
    private static final Locator ALL_BUTTONS = new Locator("ALL_BUTTONS", By.xpath("//button"));

    public CarRentalPage(Session<T> session) {
        super(session);
    }

    public boolean isCarRentalLoggedIn() {
        driverTools.waitForElementToAppear(USERNAME_GREETING, 2);
        return driverTools.isElementDisplayed(USERNAME_GREETING);
    }

    public void openCarRentalPage(String username, String password) {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-ms");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-ms");
        }
    }

    public void openCarRentalPageViaUI(String username, String password) {
        driverTools.maximizeWindow();

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/member");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/member");
        }

        if (!isCarRentalLoggedIn()) {
            CarRentalLoginPage<?> carLoginPage = new CarRentalLoginPage<>(session);
            carLoginPage.loginToCarRentalPage(username, password);
            driverTools.waitForElementToAppear(USERNAME_GREETING, 2);

            if (Configs.ENVIRONMENT == Environment.GK) {
                driverTools.get("https://gatotkaca.tiket.com/admin/car-ms");
            } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
                driverTools.get("https://preprod.tiket.com/admin/car-ms");
            }
        }
    }

    public void openCarBrandSubMenu() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-ms/manage-car/car-brand-profile");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-ms/manage-car/car-brand-profile");
        }
    }

    public void addCarBrand(String brand) {
        driverTools.click(ADD_BUTTON);
        driverTools.click(CAR_BRAND_INPUT);
        driverTools.clear(CAR_BRAND_INPUT);
        driverTools.sendKeys(CAR_BRAND_INPUT, brand);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean searchCarBrand(String brand) {
        driverTools.click(SEARCH_INPUT);
        driverTools.clear(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, brand);
        driverTools.click(SEARCH_BUTTON);

        return verifyResultIsDisplayed(brand);
    }

    public void editCarBrand(String brand) {
        driverTools.click(EDIT_BUTTON);
        driverTools.click(CAR_BRAND_INPUT);
        driverTools.clear(CAR_BRAND_INPUT);
        driverTools.sendKeys(CAR_BRAND_INPUT, brand);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean verifySuccessAlertIsDisplayed() {
        driverTools.waitForElementToAppear(SUCCESS_ALERT, 2);
        return driverTools.isElementDisplayed(SUCCESS_ALERT);
    }

    public void toggle() {
        driverTools.click(TOGGLE);
        driverTools.click(YES_BUTTON);
    }

    public boolean deactivateToggle() {
        toggle();
        return verifyResultIsDisplayed("InActive") || verifyResultIsDisplayed("Tidak Aktif");
    }

    public boolean activateToggle() {
        toggle();
        return verifyResultIsDisplayed("Active") || verifyResultIsDisplayed("Aktif");
    }

    public void openCarModelSubMenu() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-ms/manage-car/car-model");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-ms/manage-car/car-model");
        }
    }

    public void addCarModel(String model, String maxPassenger, String maxBaggage, String factory, String priorityScore, String filePath) {
        driverTools.click(ADD_BUTTON);

        driverTools.click(CAR_BRAND_DROPDOWN);
        WebElement carBrand = driver.switchTo().activeElement();
        carBrand.sendKeys(Keys.DOWN);
        carBrand.sendKeys(Keys.RETURN);

        driverTools.click(CAR_MODEL_DROPDOWN);
        WebElement carModel = driver.switchTo().activeElement();
        carModel.sendKeys(Keys.DOWN);
        carModel.sendKeys(Keys.RETURN);

        driverTools.click(CAR_MODEL_INPUT);
        driverTools.clear(CAR_MODEL_INPUT);
        driverTools.sendKeys(CAR_MODEL_INPUT, model);

        driverTools.click(MAX_PASSENGER_INPUT);
        driverTools.clear(MAX_PASSENGER_INPUT);
        driverTools.sendKeys(MAX_PASSENGER_INPUT, maxPassenger);

        driverTools.click(MAX_BAGGAGE_INPUT);
        driverTools.clear(MAX_BAGGAGE_INPUT);
        driverTools.sendKeys(MAX_BAGGAGE_INPUT, maxBaggage);

        driverTools.click(FACTORY_INPUT);
        driverTools.clear(FACTORY_INPUT);
        driverTools.sendKeys(FACTORY_INPUT, factory);

        driverTools.click(PRIORITY_SCORE_INPUT);
        driverTools.clear(PRIORITY_SCORE_INPUT);
        driverTools.sendKeys(PRIORITY_SCORE_INPUT, priorityScore);

        driverTools.sendKeys(IMAGE_UPLOAD, filePath);
        driverTools.click(SAVE_BUTTON);
    }

    public void editCarModel(String model) {
        driverTools.click(EDIT_BUTTON);
        driverTools.click(CAR_MODEL_INPUT);
        driverTools.clear(CAR_MODEL_INPUT);
        driverTools.sendKeys(CAR_MODEL_INPUT, model);
        driverTools.click(SAVE_BUTTON);
    }

    public boolean searchCarModel(String model) {
        driverTools.click(SEARCH_FILTER_DROPDOWN);
        WebElement dropdown = driver.switchTo().activeElement();
        dropdown.sendKeys(Keys.DOWN);
        dropdown.sendKeys(Keys.RETURN);

        driverTools.click(SEARCH_INPUT);
        driverTools.clear(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, model);
        driverTools.click(SEARCH_BUTTON);

        return verifyResultIsDisplayed(model);
    }

    public boolean verifyResultIsDisplayed(String expectedResult) {
        Locator RESULT = new Locator("RESULT", new By.ByXPath("//td[text()='" + expectedResult + "']"));
        driverTools.waitForElementToAppear(RESULT, 2);
        return driverTools.isElementDisplayed(RESULT);
    }

    public void openBusinessProfileSubMenu() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-v4/manage-business-profile/business-profile-car");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-v4/manage-business-profile/business-profile-car");
        }
    }

    public String getBusinessProfileId() {
        return driverTools.waitForElementToAppear(BUSINESS_PROFILE_ID_TEXT, 5).orElseThrow().getText();
    }

    public void addBusinessProfile(String type, String businessName, String phoneNumber, String email) {
        driverTools.waitForElementToAppear(ADD_V4_BUTTON, 5);
        driverTools.click(ADD_V4_BUTTON);

        Locator TYPE_CHECKBOX = new Locator("TYPE_CHECKBOX", new By.ByXPath("//input[@name='checkBox" + type + "']"));
        WebElement element = driverTools.waitForElementToAppear(TYPE_CHECKBOX).orElseThrow();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);

        driverTools.click(BUSINESS_NAME_INPUT);
        driverTools.sendKeys(BUSINESS_NAME_INPUT, businessName);

        driverTools.click(DEFAULT_CONTACT_CHECKBOX);

        driverTools.click(CONTACT_NAME_INPUT);
        driverTools.sendKeys(CONTACT_NAME_INPUT, businessName);

        driverTools.click(PHONE_NUMBER_INPUT);
        driverTools.sendKeys(PHONE_NUMBER_INPUT, phoneNumber);

        driverTools.click(EMAIL_INPUT);
        driverTools.sendKeys(EMAIL_INPUT, email);

        driverTools.click(SAVE_BUSINESS_PROFILE_BUTTON);
    }

    public void editBusinessProfile(String businessName) {
        driverTools.click(EDIT_BUSINESS_PROFILE_BUTTON);
        driverTools.click(BUSINESS_NAME_INPUT);
        driverTools.sendKeys(BUSINESS_NAME_INPUT, businessName);
        driverTools.click(SAVE_BUSINESS_PROFILE_BUTTON);
    }

    public boolean searchBusinessProfile(String businessName) {
        driverTools.waitForElementToAppear(SEARCH_V4_INPUT, 5);
        driverTools.clickWithJavascriptExecutor(SEARCH_V4_INPUT);
        driverTools.sendKeys(SEARCH_V4_INPUT, businessName);

        WebElement search = driver.switchTo().activeElement();
        search.sendKeys(Keys.RETURN);

        Locator BUSINESS_NAME_RESULT = new Locator("BUSINESS_NAME_RESULT", new By.ByXPath("//a[@class='tix-button v4 text-align-left desktop link'][contains(text(), '" + businessName + "')]"));
        driverTools.waitForElementToAppear(BUSINESS_NAME_RESULT);
        return driverTools.isElementDisplayed(BUSINESS_NAME_RESULT);
    }

    public boolean verifySuccessSnackbar() {
        driverTools.waitForElementToAppear(SUCCESS_SNACKBAR, 2);
        return driverTools.isElementDisplayed(SUCCESS_SNACKBAR);
    }

    public boolean verifyDownloadedExport(String fileName, String timeOut) {
        String downloadDir = System.getProperty("user.home") + File.separator + "Downloads";
        boolean fileDownloaded = false;

        long startTime = Instant.now().toEpochMilli();
        long waitTime = startTime + Integer.parseInt(timeOut);

        while (Instant.now().toEpochMilli() < waitTime) {
            File[] listFiles = new File(downloadDir).listFiles();
            for (File file : listFiles) {
                if (file.getName().contains(fileName)) {
                    String[] listFilesName = new File(downloadDir).list();
                    for (int i = 0; i < listFilesName.length; i++) {
                    }
                    fileDownloaded = true;
                    file.delete();
                    break;
                }
            }
            if (fileDownloaded) break;
        }
        return fileDownloaded;
    }

    public void openFacilitySubMenu(String type) {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-ms/manage-facilities/" + type + "-facilities");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-ms/manage-facilities/" + type + "-facilities");
        }
    }

    public void clickAddFacilityButton() {
        driverTools.click(ADD_BUTTON);
    }

    public void clickEditFacilityButton() {
        driverTools.click(EDIT_BUTTON);
    }

    public void addOrEditFacility(String facilityNameId, String facilityNameEn, String filePath) {
        driverTools.click(FACILITY_NAME_ID_INPUT);
        driverTools.clear(FACILITY_NAME_ID_INPUT);
        driverTools.sendKeys(FACILITY_NAME_ID_INPUT, facilityNameId);

        driverTools.click(FACILITY_NAME_EN_INPUT);
        driverTools.clear(FACILITY_NAME_EN_INPUT);
        driverTools.sendKeys(FACILITY_NAME_EN_INPUT, facilityNameEn);

        driverTools.sendKeys(FACILITY_IMAGE_UPLOAD, filePath);
        driverTools.click(SUBMIT_BUTTON);
    }

    public boolean searchFacility(String facility) {
        driverTools.click(SEARCH_INPUT);
        driverTools.clear(SEARCH_INPUT);
        driverTools.sendKeys(SEARCH_INPUT, facility);
        driverTools.click(SEARCH_BUTTON);
        return verifyResultIsDisplayed(facility);
    }

    public void openCampaignListSubMenu() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-v4/manage-campaign");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-v4/manage-campaign");
        }
    }

    public void addCampaignList(String campaignNameId, String campaignNameEn, String regional, String carType, String vendorName) {
        driverTools.click(ADD_CAMPAIGN_LIST_BUTTON);

        driverTools.click(CAMPAIGN_NAME_ID_FIELD);
        driverTools.sendKeys(CAMPAIGN_NAME_ID_FIELD, campaignNameId);

        driverTools.click(CAMPAIGN_NAME_EN_FIELD);
        driverTools.sendKeys(CAMPAIGN_NAME_EN_FIELD, campaignNameEn);

        driverTools.click(ADD_V4_BUTTON);

        driverTools.click(REGIONAL_DROPDOWN);
        WebElement regionalDropdown = driver.switchTo().activeElement();
        regionalDropdown.sendKeys(regional);
        regionalDropdown.sendKeys(Keys.RETURN);

        driverTools.click(CAR_DROPDOWN);
        WebElement carDropdown = driver.switchTo().activeElement();
        carDropdown.sendKeys(carType);
        carDropdown.sendKeys(Keys.RETURN);

        driverTools.click(VENDOR_DROPDOWN);
        WebElement vendorDropdown = driver.switchTo().activeElement();
        vendorDropdown.sendKeys(vendorName);
        vendorDropdown.sendKeys(Keys.RETURN);

        driverTools.click(VENDOR_DROPDOWN);
        driverTools.click(ADD_V4_BUTTON);
    }

    public void addCardLabelGimmickCampaign(String campaignNameId, String campaignNameEn, String textColor, String labelColor) {
        driverTools.click(CARD_LABEL_GIMMICK_CHECKBOX);

        driverTools.click(CAMPAIGN_ID_TEXT_FIELD_CL);
        driverTools.sendKeys(CAMPAIGN_ID_TEXT_FIELD_CL, campaignNameId);

        driverTools.click(CAMPAIGN_EN_TEXT_FIELD_CL);
        driverTools.sendKeys(CAMPAIGN_EN_TEXT_FIELD_CL, campaignNameEn);

        driverTools.click(TEXT_COLOR_FIELD_CL);
        driverTools.sendKeys(TEXT_COLOR_FIELD_CL, textColor);

        driverTools.click(LABEL_COLOR_FIELD);
        driverTools.sendKeys(LABEL_COLOR_FIELD, labelColor);

        String campaignTextColor = driverTools.waitForElementToAppear(CARD_LABEL_TEXT).orElseThrow().getCssValue("color");
        String textColorHex = Color.fromString(campaignTextColor).asHex();
        logger.log("Text color: " + textColorHex);

        String[] campaignLabelColor = driverTools.waitForElementToAppear(LABEL_COLOR).orElseThrow().getCssValue("background").split(" none ");
        String labelColorHex = Color.fromString(campaignLabelColor[0]).asHex();
        logger.log("Label color: " + labelColorHex);

        String campaignName = driverTools.waitForElementToAppear(LABEL_COLOR).orElseThrow().getText();
        logger.log("Campaign name: " + campaignName);

        Assertion.assertThat("Verify text color is correct", textColorHex, is(textColor));
        Assertion.assertThat("Verify label color is correct", labelColorHex, is(labelColor));
        Assertion.assertThat("Verify campaign name is correct", campaignName, is(campaignNameId));
    }

    public void addStrikeoutPriceCampaign(String markupPrice, String discountPrice) {
        driverTools.click(STRIKEOUT_PRICE_CHECKBOX);

        String initialPrice = driverTools.waitForElementToAppear(CAMPAIGN_PRICE).orElseThrow().getText();
        int initialPriceInt = Integer.parseInt(initialPrice);

        driverTools.click(BY_NOMINAL_RADIO_BUTTON);

        driverTools.click(MARKUP_PRICE_INPUT);
        driverTools.sendKeys(MARKUP_PRICE_INPUT, markupPrice);

        driverTools.click(DISCOUNT_PRICE_INPUT);
        driverTools.sendKeys(DISCOUNT_PRICE_INPUT, discountPrice);

        int markupPriceInt = Integer.parseInt(markupPrice);
        int discountPriceInt = Integer.parseInt(discountPrice);
        int campaignPrice = initialPriceInt + markupPriceInt - discountPriceInt;

        String currentPrice = driverTools.waitForElementToAppear(CAMPAIGN_PRICE).orElseThrow().getText();
        int currentPriceInt = Integer.parseInt(currentPrice);
        logger.log("Current price: " + currentPrice);

        Assertion.assertThat("Verify strikeout price is correct", currentPriceInt, is(campaignPrice));
    }

    public void addSpecialSectionCampaign(String campaignNameId, String campaignNameEn, String bgColor, String textColor) {
        driverTools.click(SPECIAL_SECTION_CHECKBOX);

        driverTools.click(CAMPAIGN_ID_TEXT_FIELD_SS);
        driverTools.sendKeys(CAMPAIGN_ID_TEXT_FIELD_SS, campaignNameId);

        driverTools.click(CAMPAIGN_EN_TEXT_FIELD_SS);
        driverTools.sendKeys(CAMPAIGN_EN_TEXT_FIELD_SS, campaignNameEn);

        driverTools.click(BACKGROUND_COLOR_FIELD);
        driverTools.sendKeys(BACKGROUND_COLOR_FIELD, bgColor);

        driverTools.click(TEXT_COLOR_FIELD_SS);
        driverTools.sendKeys(TEXT_COLOR_FIELD_SS, textColor);

        String[] campaignBgColor = driverTools.waitForElementToAppear(BACKGROUND_COLOR).orElseThrow().getCssValue("background").split(" none ");
        String bgColorHex = Color.fromString(campaignBgColor[0]).asHex();
        logger.log("Background color: " + bgColorHex);

        String campaignTextColor = driverTools.waitForElementToAppear(SPECIAL_SECTION_TEXT).orElseThrow().getCssValue("color");
        String textColorHex = Color.fromString(campaignTextColor).asHex();
        logger.log("Text color: " + textColorHex);

        String campaignName = driverTools.waitForElementToAppear(SPECIAL_SECTION_TEXT).orElseThrow().getText();
        logger.log("Campaign name: " + campaignName);

        Assertion.assertThat("Verify background color is correct", bgColorHex, is(bgColor));
        Assertion.assertThat("Verify text color is correct", textColorHex, is(textColor));
        Assertion.assertThat("Verify campaign name is correct", campaignName, is(campaignNameId));
    }

    public void submitCampaignList() {
        driverTools.click(ADD_V4_BUTTON);
    }

    public boolean searchCampaignList(String campaignList) {
        driverTools.click(SEARCH_V4_INPUT);
        driverTools.sendKeys(SEARCH_V4_INPUT, campaignList);
        WebElement search = driver.switchTo().activeElement();
        search.sendKeys(Keys.RETURN);
        return verifyResultIsDisplayed(campaignList);
    }

    public void toggleCampaignList() {
        WebElement element = driverTools.waitForElementToAppear(CAMPAIGN_TOGGLE).orElseThrow();
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    public boolean activateToggleCampaignList() {
        toggleCampaignList();
        driverTools.waitForElementToAppear(CAMPAIGN_STATUS, 5);
        return driverTools.isElementDisplayed(CAMPAIGN_STATUS);
    }

    public void openPaymentListSubMenu() {
        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/car-v4/finance/payment-list");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/car-v4/finance/payment-list");
        }
    }

    public void openPaymentListSubMenuViaSidebar() {
        driverTools.click(FINANCE_MENU);
        driverTools.waitForElementsToAppear(PAYMENT_LIST_MENU);
        driverTools.click(PAYMENT_LIST_MENU);
    }

    public void openCarRentalPageViaHomepageCard(String username, String password) throws InterruptedException {
        driverTools.maximizeWindow();
        new MemberLoginPage<>(session).adminLoginViaCookie(username, password);

        if (Configs.ENVIRONMENT == Environment.GK) {
            driverTools.get("https://gatotkaca.tiket.com/admin/nft");
        } else if (Configs.ENVIRONMENT == Environment.PREPROD) {
            driverTools.get("https://preprod.tiket.com/admin/nft");
        }

        int handleCountBeforeClick = driverTools.getWindowHandles().size();

        WebElement carRentalCard = driverTools.waitForElementToBeClickable(CAR_RENTAL_HOMEPAGE_CARD).orElseThrow(() -> new NoSuchElementException("Car Rental homepage card not found."));
        driverTools.scrollElementToCenter(carRentalCard);
        driverTools.click(carRentalCard);

        driverTools.waitForNewTabAndSwitchToLatest(handleCountBeforeClick);

        driverTools.waitForElementToAppear(CAR_RENTAL_SIDEBAR).orElseThrow(() -> new NoSuchElementException("Car Rental sidebar did not load after clicking the homepage card."));
    }

    public void verifyUploadXlsButtonNotPresent() {
        if (driverTools.isElementDisplayed(PAYMENT_LIST_LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(PAYMENT_LIST_LOADING_POPUP);
        }

        List<WebElement> allButtons = driver.findElements(ALL_BUTTONS.by());
        logger.log("Traversing " + allButtons.size() + " button(s) on the page to check for the 'Upload XSL' button.");
        boolean isUploadPresent = false;
        for (int i = 0; i < allButtons.size(); i++) {
            String buttonText = allButtons.get(i).getText().trim().toLowerCase();
            logger.log("Button [" + i + "]: \"" + buttonText + "\"");
            if (buttonText.contains("upload xsl")) {
                isUploadPresent = true;
                logger.log("'Upload XSL' button found at index " + i + ": \"" + buttonText + "\"");
                break;
            }
        }
        Assertion.assertThat("Verify no 'Upload XSL' button is present on the page", isUploadPresent, is(false));
    }

    public void exportPaymentList(String vendorName) {
        if (driverTools.isElementDisplayed(PAYMENT_LIST_LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(PAYMENT_LIST_LOADING_POPUP);
        }

        driverTools.click(VENDOR_NAME_FIELD);
        WebElement vendorNameField = driver.switchTo().activeElement();
        vendorNameField.sendKeys(vendorName);
        vendorNameField.sendKeys(Keys.RETURN);

        driverTools.click(ORDER_STATUS_DROPDOWN);
        WebElement orderStatusDropdown = driver.switchTo().activeElement();
        orderStatusDropdown.sendKeys(Keys.RETURN);

        driverTools.click(PAYMENT_STATUS_DROPDOWN);
        WebElement paymentStatusDropdown = driver.switchTo().activeElement();
        paymentStatusDropdown.sendKeys(Keys.RETURN);

        driverTools.click(SEARCH_PAYMENT_LIST_BUTTON);

        if (driverTools.isElementDisplayed(PAYMENT_LIST_LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(PAYMENT_LIST_LOADING_POPUP);
        }

        driverTools.click(EXPORT_BUTTON);
    }

    public boolean verifyUploadSuccessSnackbar() {
        driverTools.waitForElementToAppear(UPLOAD_SUCCESS_SNACKBAR, 5);
        return driverTools.isElementDisplayed(UPLOAD_SUCCESS_SNACKBAR);
    }

    public void deleteCarBrand () {
        driverTools.click (DELETE_BUTTON_CAR_BRAND);
        driverTools.waitForElementToAppear(YES_BUTTON);
        driverTools.click (YES_BUTTON);
    }

    public void deleteCarBrandModelOrFacility() {
        driverTools.click(DELETE_BUTTON);
        driverTools.waitForElementToAppear(YES_BUTTON);
        driverTools.click(YES_BUTTON);
    }

    public void deleteBusinessProfile() {
        driverTools.click(DELETE_BUSINESS_PROFILE_BUTTON);
        driverTools.click(POPUP_DELETE_BUTTON);
    }

    public void searchOrderId(String orderId, String orderStatus, String paymentStatus) {
        if (driverTools.isElementDisplayed(PAYMENT_LIST_LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(PAYMENT_LIST_LOADING_POPUP);
        }

        driverTools.click(ORDER_STATUS_DROPDOWN);
        WebElement orderStatusDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(orderStatusDropdown, orderStatus);
        driverTools.sendKeys(orderStatusDropdown, Keys.RETURN.toString());

        driverTools.click(PAYMENT_STATUS_DROPDOWN);
        WebElement paymentStatusDropdown = driver.switchTo().activeElement();
        driverTools.sendKeys(paymentStatusDropdown, paymentStatus);
        driverTools.sendKeys(paymentStatusDropdown, Keys.RETURN.toString());

        driverTools.sendKeys(ORDER_ID_SEARCH_INPUT, orderId);
        driverTools.click(SEARCH_PAYMENT_LIST_BUTTON);
    }

    public boolean isTableEmpty(){
        if (driverTools.isElementDisplayed(PAYMENT_LIST_LOADING_POPUP)) {
            driverTools.waitForElementToDisappear(PAYMENT_LIST_LOADING_POPUP);
        }
        return driverTools.isElementDisplayed(EMPTY_TABLE);
    }

    public boolean isOrderIdFound(String orderId){
        Optional<WebElement> element =  driverTools.element(ORDER_ID_CELL);
        return element.map(WebElement::isDisplayed).orElse(false);
    }

    public String downloadPaymentListFromDrive(String fileId, String downloadDirectory) throws FileNotFoundException, InterruptedException {
        driverTools.get("https://docs.google.com/spreadsheets/d/"+fileId+"/export?format=xlsx");
        return CoreFunctions.searchDownloadedFilePath("Payment List", downloadDirectory);
    }

    public void uploadPaymentList(String filepath) {
        driverTools.sendKeys(PAYMENT_INPUT, filepath);

    }

    public void deleteDownloadedFile(String filepath) {
        File fileToDelete = new File(filepath);
        if (fileToDelete.exists()) {
            boolean deleted = fileToDelete.delete();
            if (deleted) {
                System.out.println("File deleted: " + filepath);
            } else {
                System.out.println("Failed to delete the file: " + filepath);
            }
        } else {
            System.out.println("File not found: " + filepath);
        }
    }

    public void searchAndDeleteFile(String downloadDirectory){
        while (true){
            String filePath = CoreFunctions.searchDownloadedFilePath("Payment List", downloadDirectory);
            if (filePath.isEmpty()) break;
            deleteDownloadedFile(filePath);
        }
    }
}