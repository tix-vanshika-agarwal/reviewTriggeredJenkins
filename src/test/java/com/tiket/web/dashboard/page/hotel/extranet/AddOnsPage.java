package com.tiket.web.dashboard.page.hotel.extranet;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class AddOnsPage<T extends WebDriver> extends BasePage<T> {
    public AddOnsPage(Session<T> session) {
        super(session);
    }

    private static final Locator ADD_ONS_PAGE = new Locator("ADD_ONS_PAGE", new By.ByXPath("//div[@class=\"add-ons-page\"]"));
    private static final Locator CREATE_ADD_ONS_BUTTON = new Locator("CREATE_ADD_ONS_BUTTON", new By.ByXPath("//*[contains(@class,'add-button')]"));
    private static final Locator CREATE_ADD_ONS_PAGE = new Locator("CREATE_ADD_ONS_PAGE", new By.ByXPath("//div[@class=\"the-create-addons\"]"));
    private static final Locator ADD_ONS_NAME_EN_INPUT = new Locator("ADD_ONS_NAME_EN_INPUT", new By.ByXPath("//input[@name=\"nameListEn\"]"));
    private static final Locator ADD_ONS_DESCRIPTION_EN_IFRAME = new Locator("ADD_ONS_DESCRIPTION_EN_IFRAME", new By.ById("descriptionListEn_ifr"));
    private static final Locator ADD_ONS_DESCRIPTION_EN_TEXTAREA = new Locator("ADD_ONS_DESCRIPTION_EN_TEXTAREA", new By.ByXPath("//body[@data-id=\"descriptionListEn\"]"));
    private static final Locator ADD_ONS_NAME_ID_INPUT = new Locator("ADD_ONS_NAME_ID_INPUT", new By.ByXPath("//input[@name=\"nameListId\"]"));
    private static final Locator ADD_ONS_DESCRIPTION_ID_IFRAME = new Locator("ADD_ONS_DESCRIPTION_ID_IFRAME", new By.ById("descriptionListId_ifr"));
    private static final Locator ADD_ONS_DESCRIPTION_ID_TEXTAREA = new Locator("ADD_ONS_DESCRIPTION_ID_TEXTAREA", new By.ByXPath("//body[@data-id=\"descriptionListId\"]"));
    private static final Locator ADD_ONS_PRICE_INPUT = new Locator("ADD_ONS_PRICE_INPUT", new By.ByXPath("//input[@name=\"price\"]"));
    private static final Locator ADD_ONS_MIN_QTY_INPUT = new Locator("ADD_ONS_MIN_QTY_INPUT", new By.ByName("minAmount"));
    private static final Locator ADD_ONS_MAX_QTY_INPUT = new Locator("ADD_ONS_MAX_QTY_INPUT", new By.ByName("maxAmount"));
    private static final Locator ADD_ONS_ADD_PHOTO_BUTTON = new Locator("ADD_ONS_ADD_PHOTO_BUTTON", new By.ByXPath("//button[@class=\"the-btn-upload \"]"));
    private static final Locator ADD_ONS_UPLOAD_PHOTO_MODAL = new Locator("ADD_ONS_UPLOAD_PHOTO_MODAL", new By.ByXPath("//div[@class=\"the-image-uploader\"]"));
    private static final Locator ADD_ONS_UPLOAD_PHOTO_SUBMIT_BUTTON = new Locator("ADD_ONS_UPLOAD_PHOTO_SUBMIT_BUTTON", new By.ByXPath("//div[@class=\"the-image-uploader\"]//button[@class=\"the-button primary button\"]"));
    private static final Locator ADD_ONS_UPLOADED_PHOTO = new Locator("ADD_ONS_UPLOADED_PHOTO", new By.ByXPath("//div[@class=\"the-image-section\"]//div[@class=\"the-image  \"]/img"));
    private static final Locator ADD_ONS_SUBMIT_BUTTON = new Locator("ADD_ONS_SUBMIT_BUTTON", new By.ByXPath("//button[@type=\"submit\"][@class=\"the-button yellow\"]"));
    private static final Locator SUCCESS_SNACKBAR = new Locator("SUCCESS_SNACKBAR", new By.ByXPath("//div[@class=\"the-toast success opened\"]"));
    private static final Locator ADD_ONS_NAME_FILTER_INPUT = new Locator("ADD_ONS_NAME_FILTER_INPUT", new By.ByXPath("//input[@name=\"addOnName\"]"));
    private static final Locator ADD_ONS_SUBMIT_FILTER_BUTTON = new Locator("ADD_ONS_SUBMIT_FILTER_BUTTON", new By.ByXPath("//button[@type=\"submit\"][contains(@class,'search-list')]"));
    private static final Locator ADD_ONS_SEARCH_RESULT_FIRST_ROW_NAME_LABEL = new Locator("ADD_ONS_SEARCH_RESULT_FIRST_ROW_NAME_LABEL", new By.ByXPath("//tr[@id=\"row-0\"]//td[@class=\"table-cell\"][2]"));
    private static final Locator ADD_ONS_DELETE_CONFIRMATION_MODAL = new Locator("ADD_ONS_DELETE_CONFIRMATION_MODAL", new By.ByXPath("//div[@class=\"addons-delete-modal-confirmation\"]"));
    private static final Locator ADD_ONS_DELETE_CONFIRMATION_BUTTON = new Locator("ADD_ONS_DELETE_CONFIRMATION_BUTTON", new By.ByXPath("//button[contains(@class,'delete-addons-button')]"));
    private static final Locator ADD_ONS_DELETE_SUCCESS_MODAL = new Locator("ADD_ONS_DELETE_SUCCESS_MODAL", new By.ByXPath("//div[@class=\"addons-delete-modal-confirmation\"]//div[@class=\"success-title\"]"));
    private static final Locator ADD_ONS_DELETE_SUCCESS_CLOSE_BUTTON = new Locator("ADD_ONS_DELETE_SUCCESS_CLOSE_BUTTON", new By.ById("button-success"));

    private static ArrayList<Locator> getAddOnsCategoryAndSubcategoryRadioButton(String category, String subcategory) {
        ArrayList<Locator> locators = new ArrayList<>();
        int categoryIndex;
        int subcategoryIndex;
        switch (category) {
            case "MEALS" -> {
                categoryIndex = 1;
                switch (subcategory) {
                    case "LUNCH" -> subcategoryIndex = 1;
                    case "BREAKFAST" -> subcategoryIndex = 2;
                    case "OTHERS" -> subcategoryIndex = 3;
                    case "DINNER" -> subcategoryIndex = 4;
                    default -> throw new IllegalStateException("Unexpected value: " + subcategory);
                }
            }
            case "ROOM_COMPLEMENTS" -> {
                categoryIndex = 2;
                switch (subcategory) {
                    case "EARLY_CHECK_IN" -> subcategoryIndex = 1;
                    case "LATE_CHECK_OUT" -> subcategoryIndex = 2;
                    case "OTHERS" -> subcategoryIndex = 3;
                    case "EXTRA_BED_WITH_BREAKFAST" -> subcategoryIndex = 4;
                    case "EXTRA_BED_ONLY" -> subcategoryIndex = 5;
                    default -> throw new IllegalStateException("Unexpected value: " + subcategory);
                }
            }
            case "TRANSPORT" -> {
                categoryIndex = 3;
                switch (subcategory) {
                    case "OTHERS" -> subcategoryIndex = 1;
                    case "BICYCLE_RENTAL" -> subcategoryIndex = 2;
                    case "MOTOR_RENTAL" -> subcategoryIndex = 3;
                    case "CAR_RENTAL" -> subcategoryIndex = 4;
                    case "HOTEL_TO_AIRPORT_TRANSFER" -> subcategoryIndex = 5;
                    case "AIRPORT_TO_HOTEL_TRANSFER" -> subcategoryIndex = 6;
                    default -> throw new IllegalStateException("Unexpected value: " + subcategory);
                }
            }
            case "ACTIVITIES" -> {
                categoryIndex = 4;
                switch (subcategory) {
                    case "OTHERS" -> subcategoryIndex = 1;
                    case "SIGHTSEEING_ADVENTURE" -> subcategoryIndex = 2;
                    case "HEALTH_WELLNESS" -> subcategoryIndex = 3;
                    case "WATER_SPORT" -> subcategoryIndex = 4;
                    case "KIDS_ACTIVITY" -> subcategoryIndex = 5;
                    case "SPA_MASSAGE" -> subcategoryIndex = 6;
                    default -> throw new IllegalStateException("Unexpected value: " + subcategory);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + category);
        }
        locators.add(new Locator("ADD_ONS_CATEGORY_RADIO_BUTTON", new By.ByXPath(String.format("(//div[@id=\"categoryId\"]//span)[%d]", categoryIndex))));
        locators.add(new Locator("ADD_ONS_SUBCATEGORY_RADIO_BUTTON", new By.ByXPath(String.format("(//div[@id=\"subCategoryId\"]//span)[%d]", subcategoryIndex))));
        return locators;
    }

    private static Locator getAddOnsPricingTypeRadioButton(String text) {
        int index;
        switch (text) {
            case "PER_ITEM" -> index = 1;
            case "PER_ROOM_PER_NIGHT" -> index = 2;
            default -> throw new IllegalStateException("Unexpected value: " + text);
        }
        return new Locator("ADD_ONS_PRICING_TYPE_RADIO_BUTTON", new By.ByXPath(String.format("(//div[@id=\"addOnType\"]//span)[%d]", index)));
    }

    private static Locator getAddOnsAvailabilityRadioButton(boolean isActive) {
        int index;
        if (isActive) {
            index = 1;
        } else {
            index = 2;
        }
        return new Locator("ADD_ONS_AVAILABILITY_RADIO_BUTTON", new By.ByXPath(String.format("(//div[@id=\"isActive\"]//span)[%d]", index)));
    }

    private static Locator getUploadAddOnsImageRadioButtonByIndex(int index) {
        return new Locator("ADD_ONS_IMAGE", new By.ByXPath(String.format("(//div[@class=\"the-image  single\"]//span)[%d]", index)));
    }

    private static Locator getAddOnsToggleOnButtonByName(String name) {
        return new Locator("ADD_ONS_TOGGLE_ON", new By.ByXPath(String.format("//tr[contains(@id,\"row-0\")]//td[@class=\"table-cell\"][text()='%s']/following-sibling::td[@class=\"table-cell sticky-col-1\"]//span[@class=\"the-toggle-btn the-toggle-btn-on\"]", name)));
    }

    private static Locator getAddOnsToggleOffButtonByName(String name) {
        return new Locator("ADD_ONS_TOGGLE_OFF", new By.ByXPath(String.format("//tr[contains(@id,\"row-0\")]//td[@class=\"table-cell\"][text()='%s']/following-sibling::td[@class=\"table-cell sticky-col-1\"]//span[@class=\"the-toggle-btn the-toggle-btn-off\"]", name)));
    }

    private static Locator getAddOnsActionButtonByName(String action, String name) {
        int index;
        switch (action) {
            case "VIEW" -> index = 1;
            case "EDIT" -> index = 2;
            case "COPY" -> index = 3;
            case "DELETE" -> index = 4;
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }
        return new Locator(String.format("ADD_ONS_ACTION_BUTTON_%s", action), new By.ByXPath(String.format("(//tr[contains(@id,\"row-0\")]//td[@class=\"table-cell\"][text()='%s']/following-sibling::td[@class=\"table-cell sticky-col-2\"]//div[@class=\"action-button-container\"])[%d]", name, index)));
    }

    public boolean isAddOnsPageDisplayed() {
        return driverTools.waitForElementsToAppear(ADD_ONS_PAGE).isPresent();
    }

    public void clickCreateNewAddOns() {
        WebElement button = driverTools.waitForElementToBeClickable(CREATE_ADD_ONS_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isCreateAddOnsPageDisplayed() {
        return driverTools.waitForElementsToAppear(CREATE_ADD_ONS_PAGE).isPresent();
    }

    public void enterAddOnsNameByLanguage(String lang, String text) {
        WebElement input = null;
        switch (lang) {
            case "EN" -> input = driverTools.waitForElementToAppear(ADD_ONS_NAME_EN_INPUT).orElseThrow();
            case "ID" -> input = driverTools.waitForElementToAppear(ADD_ONS_NAME_ID_INPUT).orElseThrow();
        }
        assert input != null;
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void enterAddOnsDescriptionByLanguage(String lang, String text) {
        WebElement iframe = null;
        WebElement textarea = null;
        switch (lang) {
            case "EN" -> {
                iframe = driverTools.waitForElementToAppear(ADD_ONS_DESCRIPTION_EN_IFRAME).orElseThrow();
                driverTools.switchToIframe(iframe);
                textarea = driverTools.waitForElementToPresence(ADD_ONS_DESCRIPTION_EN_TEXTAREA).orElseThrow();
            }
            case "ID" -> {
                iframe = driverTools.waitForElementToAppear(ADD_ONS_DESCRIPTION_ID_IFRAME).orElseThrow();
                driverTools.switchToIframe(iframe);
                textarea = driverTools.waitForElementToPresence(ADD_ONS_DESCRIPTION_ID_TEXTAREA).orElseThrow();
            }
        }
        assert textarea != null;
        driverTools.clearInput(textarea);
        driverTools.sendKeys(textarea, text);
        driverTools.switchBackFromIframe();
    }

    public void selectAddOnsCategoryAndSubcategory(String category, String subcategory) {
        ArrayList<Locator> locators = getAddOnsCategoryAndSubcategoryRadioButton(category, subcategory);
        WebElement categoryRadio = driverTools.waitForElementToBeClickable(locators.get(0)).orElseThrow();
        driverTools.click(categoryRadio);
        WebElement subcategoryRadio = driverTools.waitForElementToBeClickable(locators.get(1)).orElseThrow();
        driverTools.click(subcategoryRadio);
    }

    public void selectAndEnterAddOnsPrice(String type, int price) {
        WebElement radio = driverTools.waitForElementToBeClickable(getAddOnsPricingTypeRadioButton(type)).orElseThrow();
        driverTools.click(radio);
        WebElement input = driverTools.waitForElementToAppear(ADD_ONS_PRICE_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, String.valueOf(price));
    }

    public void enterMinAndMaxAddOnsQuantity(int min, int max) {
        WebElement minQty = driverTools.waitForElementToAppear(ADD_ONS_MIN_QTY_INPUT).orElseThrow();
        driverTools.clearInput(minQty);
        driverTools.sendKeys(minQty, String.valueOf(min));
        WebElement maxQty = driverTools.waitForElementToAppear(ADD_ONS_MAX_QTY_INPUT).orElseThrow();
        driverTools.clearInput(maxQty);
        driverTools.sendKeys(maxQty, String.valueOf(max));
    }

    public void selectAddOnsAvailability(boolean isAvailable) {
        WebElement radio = driverTools.waitForElementToBeClickable(getAddOnsAvailabilityRadioButton(isAvailable)).orElseThrow();
        driverTools.click(radio);
    }

    public void addAddOnsPhotoByIndex(int index) {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ONS_ADD_PHOTO_BUTTON).orElseThrow();
        driverTools.click(button);
        Assertion.assertThat("Upload photo modal is appeared", driverTools.waitForElementToAppear(ADD_ONS_UPLOAD_PHOTO_MODAL).isPresent(), is(true));
        WebElement radio = driverTools.waitForElementToBeClickable(getUploadAddOnsImageRadioButtonByIndex(index)).orElseThrow();
        driverTools.click(radio);
        WebElement submit = driverTools.waitForElementToBeClickable(ADD_ONS_UPLOAD_PHOTO_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(submit);
        Assertion.assertThat("Photo is selected successfully", driverTools.waitForElementsToAppear(ADD_ONS_UPLOADED_PHOTO).get().size(), equalTo(1));
    }

    public void submitAddOnsForm() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ONS_SUBMIT_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isSuccessSnackbarAppeared() {
        return driverTools.waitForElementToAppear(SUCCESS_SNACKBAR).isPresent();
    }

    public void enterFilterAddOnsName(String text) {
        WebElement input = driverTools.waitForElementToAppear(ADD_ONS_NAME_FILTER_INPUT).orElseThrow();
        driverTools.clearInput(input);
        driverTools.sendKeys(input, text);
    }

    public void submitFilterAddOns() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ONS_SUBMIT_FILTER_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public boolean isAddOnsSearchResultFoundByName(String text) {
        try {
            return driverTools.waitForElementToAppear(ADD_ONS_SEARCH_RESULT_FIRST_ROW_NAME_LABEL).orElseThrow().getText().contains(text);
        } catch (NoSuchElementException e) {
            logger.log("Add ons is not found");
            return false;
        }
    }

    public void switchAddOnsToggleByName(String text, boolean expectedStatus) {
        if (expectedStatus) {
            try {
                WebElement toggle = driverTools.waitForElementToBeClickable(getAddOnsToggleOffButtonByName(text)).orElseThrow();
                driverTools.click(toggle);
            } catch (Exception e) {
                logger.log("Add ons is already active");
            }
            Assertion.assertThat("Add ons is active", driverTools.waitForElementToAppear(getAddOnsToggleOnButtonByName(text)).isPresent(), is(true));
        } else {
            try {
                WebElement toggle = driverTools.waitForElementToBeClickable(getAddOnsToggleOnButtonByName(text)).orElseThrow();
                driverTools.click(toggle);
            } catch (Exception e) {
                logger.log("Add ons is already inactive");
            }
            Assertion.assertThat("Add ons is inactive", driverTools.waitForElementToAppear(getAddOnsToggleOffButtonByName(text)).isPresent(), is(true));
        }
    }

    public void clickAddOnsActionByName(String action, String name) {
        WebElement button = driverTools.waitForElementToBeClickable(getAddOnsActionButtonByName(action, name)).orElseThrow();
        driverTools.click(button);
    }

    public void confirmDeleteAddOns() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ONS_DELETE_CONFIRMATION_BUTTON).orElseThrow();
        driverTools.click(button);
    }

    public void closeSuccessDeleteAddOnsModal() {
        WebElement button = driverTools.waitForElementToBeClickable(ADD_ONS_DELETE_SUCCESS_CLOSE_BUTTON).orElseThrow();
        driverTools.click(button);
    }
}
