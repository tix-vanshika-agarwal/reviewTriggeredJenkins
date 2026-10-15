package com.tiket.web.page.nft.ferry.BookingPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;

import io.appium.java_client.AppiumBy;

import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.core.Is.is;

public abstract class FerryBookingPageWeb<T extends WebDriver> extends WebPage<T> {

    public static <T extends WebDriver> FerryBookingPageWeb<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new FerryBookingPageDWeb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new FerryBookingPageMWeb<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator TNC_LINK = new Locator("TNC_LINK", new By.ByXPath("//span[text()='Terms & Conditions' or text()='Syarat & Ketentuan']"));
    private static final Locator TNC_POP_UP = new Locator("TNC_POP_UP", new By.ByXPath("//*[contains(@class, 'BottomSheet_body')]"));
    private static final Locator CLOSE_POP_UP_BUTTON = new Locator("CLOSE_POP_UP_BUTTON", new By.ByXPath("//*[contains(@class, 'BottomSheet_close_button')]"));
    private static final Locator CONTACT_CARD = new Locator("CONTACT_CARD", new By.ByXPath("//*[contains(@class, 'ContactDetailSection_card_padding')]"));
    private static final Locator CONTACT_SALUTATION = new Locator("CONTACT_SALUTATION", new By.ByXPath("(//*[contains(@class, 'Radio_checkbox')])[1]"));
    private static final Locator FULL_NAME_INPUT = new Locator("FULL_NAME_INPUT", new By.ByXPath("//input[@name='fullName']"));
    private static final Locator PHONE_NUMBER_INPUT = new Locator("PHONE_NUMBER_INPUT", new By.ByXPath("//input[@id='mobile-number' or @id='nomor-ponsel']"));
    private static final Locator EMAIL_DISABLED = new Locator("EMAIL_DISABLED", new By.ByXPath("//*[contains(@class, 'styles_state_disabled')]"));
    private static final Locator EMAIL_INPUT = new Locator("EMAIL_INPUT", new By.ByXPath("//input[@name='emailAddress']"));
    private static final Locator PASSENGER_TITLE = new Locator("PASSENGER_TITLE", new By.ByXPath("(//*[contains(@class, 'PassengerDetailSection_passenger_title')])[2]"));
    private static final Locator FIRST_CITY_TEXT = new Locator("FIRST_CITY_TEXT", new By.ByXPath("(//div[contains(@class, 'DropdownRadio_radio_label')])[1]"));
    private static final Locator SAVE_BUTTON = new Locator("SAVE_BUTTON", new By.ByXPath("//*[contains(@class, 'Button_variant_primary') and (text()='Save' or text()='Simpan')]"));
    private static final Locator COVID_TEST_VERIFICATION = new Locator("COVID_TEST_VERIFICATION", new By.ByXPath("//span[contains(@class, 'Text_size_b3') and contains(text(), 'Antigen')]"));
    private static final Locator VACCINE_VERIFIED = new Locator("VACCINE_VERIFIED", new By.ByXPath("//*[contains(@class, 'CovidSection_verified')]"));
    private static final Locator PLATE_NUMBER_INPUT = new Locator("PLATE_NUMBER_INPUT", new By.ByXPath("//input[@name='licensePlate']"));
    private static final Locator VACCINATION_VERIFICATION = new Locator("VACCINATION_VERIFICATION", new By.ByXPath("//span[contains(@class, 'Text_size_b3') and contains(text(), 'COVID')]"));
    private static final Locator VACCINE_DOCUMENT_REQUIRED = new Locator("VACCINE_DOCUMENT_REQUIRED", new By.ByXPath("//*[contains(@class, 'CovidSection_documents_required')]"));
    private static final Locator DOB_SAVE_BUTTON = new Locator("DOB_SAVE_BUTTON", new By.ByXPath("(//*[contains(@class, 'Button_horizontal_padding') and (text()='Save' or text()='Simpan')])[last()]"));
    private static final Locator TNC_CHECK_BOX = new Locator("TNC_CHECK_BOX", new By.ByXPath("//span[contains(@class, 'Check_custom_checkbox')]"));
    private static final Locator SUBMIT_BOOKING_BUTTON = new Locator("SUBMIT_BOOKING_BUTTON", new By.ByXPath("//*[contains(@class, 'SubmissionSection_button')]"));
    private static final Locator CONTINUE_PAYMENT_BUTTON = new Locator("CONTINUE_PAYMENT_BUTTON", new By.ByXPath("//*[contains(@class, 'pop_up')]//button[contains(@class, 'variant_primary')]"));
    private static final Locator TOTAL_PRICE_TEXT = new Locator("TOTAL_PRICE_TEXT", new By.ByXPath("//*[contains(@class, 'TotalPrice_price')]"));
    private static final Locator ALERT_CONTAINER = new Locator("ALERT_CONTAINER", new By.ByXPath("//*[contains(@class, 'PassengerDetailSection_card_error')]"));
    private static final Locator ID_NUMBER_INPUT_ALERT = new Locator("ID_NUMBER_INPUT_ALERT", new By.ByXPath("//*[contains(@class, 'styles_inline_input_wrapper')]//*[contains(@class, 'Text_variant_alert')]"));
    private static final Locator VERIFICATION_FAILED_TEXT = new Locator("VERIFICATION_FAILED_TEXT", new By.ByXPath("//*[contains(@class, 'styles_state_danger')]//span"));
    private static final Locator LETS_FILL_IN_TEXT = new Locator("LETS_FILL_IN_TEXT", new By.ByXPath("//*[contains(@class, 'PassengerDetailSection_card_error')]/following-sibling::p[contains(@class, 'Text_variant_alert')]"));
    private static final Locator BOOKING_FORM_WRAPPER = new Locator("BOOKING_FORM_WRAPPER", new By.ByXPath("//*[contains(@class, 'booking_form_wrapper')]"));

    public FerryBookingPageWeb(Session<T> session) { super(session); }

    public abstract void clickAllowLocation();

    public void clickTncPopUp() {
        WebElement tncLink = driverTools.waitForElementToAppear(TNC_LINK).orElseThrow();
        driverTools.scrollIntoWebView(tncLink);
        driverTools.clickWithJavascriptExecutor(TNC_LINK);

        driverTools.waitForElementToAppear(TNC_POP_UP);
        Assertion.assertThat("Verify user is able to see TnC pop up on booking form", driverTools.isElementDisplayed(TNC_POP_UP), is(true));
    }

    public void closeTncPopUp() {
        driverTools.clickWithJavascriptExecutor(CLOSE_POP_UP_BUTTON);
        driverTools.waitForElementToDisappear(TNC_POP_UP);
        Assertion.assertThat("Verify user is able to close TnC pop up on booking form", !driverTools.isElementDisplayed(TNC_POP_UP), is(true));
    }

    public void fillContactDetail(String fullName, String phoneNumber, String email) {
        driverTools.waitForElementToAppear(CONTACT_CARD, 5);
        driverTools.click(CONTACT_SALUTATION);
        WebElement fullNameInput = driverTools.waitForElementToAppear(FULL_NAME_INPUT).orElseThrow();
        if (!driverTools.getValue(fullNameInput).equals(fullName)) {
            driverTools.clearInput(fullNameInput);
            driverTools.sendKeys(fullNameInput, fullName);
        }
        WebElement phoneNumberInput = driverTools.waitForElementToAppear(PHONE_NUMBER_INPUT).orElseThrow();
        if (!driverTools.getValue(phoneNumberInput).equals(phoneNumber)) {
            driverTools.clearInput(phoneNumberInput);
            driverTools.sendKeys(phoneNumberInput, phoneNumber);
        }
        WebElement emailInput = driverTools.waitForElementToAppear(EMAIL_INPUT).orElseThrow();
        if (!driverTools.isElementDisplayed(EMAIL_DISABLED)) {
            if (!driverTools.getValue(emailInput).equals(email)) {
                driverTools.clearInput(emailInput);
                driverTools.sendKeys(emailInput, email);
            }
        }
    }

    public void fillPassengerDetail(int adultPassenger, int childPassenger, int infantPassenger, int elderPassenger, String idNumberValid, String multiIdNumberType) {
        int childIndex = adultPassenger + 1;
        int infantIndex = adultPassenger + childPassenger + 1;
        int elderIndex = adultPassenger + childPassenger + infantPassenger + 1;

        int childLastIndex = adultPassenger + childPassenger;
        int infantLastIndex = adultPassenger + childPassenger + infantPassenger;
        int elderLastIndex = adultPassenger + childPassenger + infantPassenger + elderPassenger;

        fillAdultPassengerDetails(adultPassenger, idNumberValid, multiIdNumberType);
        fillChildPassengerDetails(childPassenger, childIndex, childLastIndex);
        fillInfantPassengerDetails(infantPassenger, infantIndex, infantLastIndex);
        fillElderPassengerDetails(elderPassenger, elderIndex, elderLastIndex);
    }

    private void fillAdultPassengerDetails(int adultPassenger, String idNumberValid, String multiIdNumberType) {
        for (int i = 0; i < adultPassenger; i++) {
            Locator PASSENGER_CONTAINER = getAdultPassengerContainerLocator(i + 1);
            driverTools.waitForElementToAppear(PASSENGER_CONTAINER, 5);
            if (driverTools.element(PASSENGER_CONTAINER).isPresent()) {
                clickPassengerContainer(PASSENGER_CONTAINER, i);
            } else if (driverTools.element(PASSENGER_CONTAINER).isEmpty() && i > 0) {
                driverTools.clickWithJavascriptExecutor(PASSENGER_TITLE);
            }

            driverTools.clickWithJavascriptExecutor(getPassengerSalutationInput(i + 1));
            enterPassengerFullName(i);
            enterPassengerAge(i, String.valueOf(CoreFunctions.generateRandomInt(18, 59)));
            enterPassengerCity(i + 1);
            // adult passenger 1 will always use valid ID number
            if (i == 0) {
                enterPassengerIdNumberValid(i, idNumberValid);
            }
            else {
                if (multiIdNumberType.equals("same")) {
                    enterPassengerIdNumberValid(i, idNumberValid);
                } else {
                    enterPassengerIdNumberRandom(i);
                }
            }
            clickSavePassengerDetail();
            enterPassengerPlateNumber();
        }
    }

    public abstract void clickSavePassengerDetail();

    private Locator getAdultPassengerContainerLocator(int index) {
        return new Locator("PASSENGER_CONTAINER_" + index, new By.ByXPath("//div[contains(@class, 'PassengerDetailSection_card')]//p[contains(text(), '" + index + "') and (contains(text(), 'Adult') or contains(text(), 'Dewasa'))]"));
    }

    public abstract void clickPassengerContainer(Locator passengerContainerLocator, int passengerIndex);

    public abstract Locator getPassengerSalutationInput(int index);

    private void enterPassengerFullName(int index) {
        Locator PASSENGER_FULL_NAME_INPUT = getPassengerFullNameInput(index);
        driverTools.waitForElementToAppear(PASSENGER_FULL_NAME_INPUT);
        if (driverTools.getValueAttribute(PASSENGER_FULL_NAME_INPUT, "value").isEmpty()) {
            driverTools.sendKeys(PASSENGER_FULL_NAME_INPUT, CoreFunctions.randomName());
        }
    }

    protected abstract Locator getPassengerFullNameInput(int index);

    private void enterPassengerAge(int index, String age) {
        Locator PASSENGER_AGE_INPUT = getPassengerAgeInput(index);
        if (driverTools.getValueAttribute(PASSENGER_AGE_INPUT, "value").isEmpty()) {
            driverTools.sendKeys(PASSENGER_AGE_INPUT, age);
        }
    }

    protected abstract Locator getPassengerAgeInput(int index);

    private void enterPassengerCity(int index) {
        WebElement passengerCityInput = driverTools.waitForElementToAppear(getPassengerCityInput(index)).orElseThrow();
        driverTools.scrollIntoWebView(passengerCityInput);

        if (driverTools.getValueAttribute(passengerCityInput, "value").isEmpty()) {
            driverTools.clickWithJavascriptExecutor(passengerCityInput);
            WebElement firstCityText = driverTools.waitForElementToAppear(FIRST_CITY_TEXT).orElseThrow();
            driverTools.scrollIntoWebView(firstCityText);
            driverTools.clickWithJavascriptExecutor(firstCityText);
        }
    }

    protected abstract Locator getPassengerCityInput(int index);

    private void enterPassengerIdNumberValid(int index, String idNumberValid) {
        Locator ID_NUMBER_INPUT = getPassengerIdNumberInput(index);
        WebElement idNumberInput = driverTools.waitForElementToAppear(ID_NUMBER_INPUT).orElseThrow();
        if (driverTools.getValueAttribute(idNumberInput, "value").isEmpty() || !driverTools.getValueAttribute(idNumberInput, "value").equals(idNumberValid) ) {
            driverTools.clearInput(idNumberInput);
            driverTools.sendKeys(idNumberInput, idNumberValid);
        }

        Locator VERIFY_ID_NUMBER_BUTTON = getVerifyIdNumberButton(index + 1);
        boolean isButtonPresent = driverTools.element(VERIFY_ID_NUMBER_BUTTON).isPresent();
        if (isButtonPresent) {
            boolean isButtonDisabled = driverTools.getValueAttribute(VERIFY_ID_NUMBER_BUTTON, "class").contains("disabled");
            if (!isButtonDisabled) {
                driverTools.clickWithJavascriptExecutor(VERIFY_ID_NUMBER_BUTTON);
                driverTools.waitForElementToAppear(COVID_TEST_VERIFICATION).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(VACCINE_VERIFIED);
                boolean isVaccineVerified = driverTools.isElementDisplayed(VACCINE_VERIFIED);
                boolean isButtonUpdated = driverTools.getValueAttribute(VERIFY_ID_NUMBER_BUTTON, "class").contains("disabled");
                Assertion.assertThat("Passenger ID number is already verified for vaccination", isVaccineVerified || isButtonUpdated, is(true));
            } else {
                Assertion.assertThat("Passenger ID number is already verified for vaccination", isButtonDisabled, is(true));
            }
        }
    }

    protected abstract Locator getPassengerIdNumberInput(int index);

    private Locator getVerifyIdNumberButton(int index) {
        return new Locator("VERIFY_ID_NUMBER_BUTTON_" + index, new By.ByXPath("(//button[contains(text(), 'Verify') or contains(text(), 'Verifikasi')])[" + index + "]"));
    }

    private void enterPassengerPlateNumber() {
        if (driverTools.waitForElementToAppear(PLATE_NUMBER_INPUT).isPresent()) {
            WebElement element = driverTools.element(PLATE_NUMBER_INPUT).orElseThrow();
            driverTools.scrollIntoWebView(element);
            driverTools.click(PLATE_NUMBER_INPUT);
            driverTools.clearInput(element);
            driverTools.sendKeys(PLATE_NUMBER_INPUT, "DR" + CoreFunctions.randomInt(4) + "JY");
        }
    }

    private void enterPassengerIdNumberRandom(int index) {
        Locator ID_NUMBER_INPUT = getPassengerIdNumberInput(index);
        driverTools.waitForElementToAppear(ID_NUMBER_INPUT);
        if (driverTools.getValueAttribute(ID_NUMBER_INPUT, "value").isEmpty()) {
            driverTools.sendKeys(ID_NUMBER_INPUT, "327" + CoreFunctions.randomInt(13));
        }

        Locator VERIFY_ID_NUMBER_BUTTON = getVerifyIdNumberButton(index + 1);
        boolean isButtonPresent = driverTools.element(VERIFY_ID_NUMBER_BUTTON).isPresent();
        if (isButtonPresent) {
            boolean isButtonDisabled = driverTools.getValueAttribute(VERIFY_ID_NUMBER_BUTTON, "class").contains("disabled");
            if (!isButtonDisabled) {
                driverTools.clickWithJavascriptExecutor(VERIFY_ID_NUMBER_BUTTON);
                driverTools.waitForElementToAppear(VACCINATION_VERIFICATION).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(COVID_TEST_VERIFICATION).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(SAVE_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
                driverTools.waitForElementToAppear(VACCINE_DOCUMENT_REQUIRED);
                boolean isVaccineRequired = driverTools.isElementDisplayed(VACCINE_DOCUMENT_REQUIRED);
                boolean isButtonUpdated = driverTools.getValueAttribute(VERIFY_ID_NUMBER_BUTTON, "class").contains("disabled");
                Assertion.assertThat("Passenger ID number is unverified for vaccination", isVaccineRequired || isButtonUpdated, is(true));
            } else {
                Assertion.assertThat("Passenger ID number is unverified for vaccination", isButtonDisabled, is(true));
            }
        }
    }

    private void fillChildPassengerDetails(int childPassenger, int childIndex, int childLastIndex) {
        for (int i = 0; i < childPassenger; i++) {
            Locator PASSENGER_CONTAINER = getChildPassengerContainerLocator(i + 1);
            while (childIndex <= childLastIndex) {
                if (driverTools.element(PASSENGER_CONTAINER).isEmpty()) {
                    driverTools.clickWithJavascriptExecutor(PASSENGER_TITLE);
                }
                else {
                    clickPassengerContainer(PASSENGER_CONTAINER, childIndex);
                }
                driverTools.clickWithJavascriptExecutor(getPassengerSalutationInput(childIndex));
                enterPassengerFullName(childIndex - 1);
                enterPassengerDateOfBirth(childIndex);
                enterPassengerCity(childIndex);
                clickSavePassengerDetail();
                childIndex++;
            }
        }
    }

    private Locator getChildPassengerContainerLocator(int index) {
        return new Locator("PASSENGER_CONTAINER_" + index, new By.ByXPath("(//div[contains(@class, 'PassengerDetailSection_card')]//p[contains(text(), 'Child') or contains(text(), 'Anak')])[" + index + "]"));
    }

    private void enterPassengerDateOfBirth(int index) {
        Locator PASSENGER_DOB_INPUT = getPassengerDateOfBirthInput(index - 1);
        if (driverTools.getValueAttribute(PASSENGER_DOB_INPUT, "value").isEmpty()) {
            driverTools.clickWithJavascriptExecutor(PASSENGER_DOB_INPUT);
            driverTools.clickWithJavascriptExecutor(DOB_SAVE_BUTTON);
        }
    }

    protected abstract Locator getPassengerDateOfBirthInput(int index);

    private void fillInfantPassengerDetails(int infantPassenger, int infantIndex, int infantLastIndex) {
        for (int i = 0; i < infantPassenger; i++) {
            Locator PASSENGER_CONTAINER = getInfantPassengerContainerLocator(i + 1);
            while (infantIndex <= infantLastIndex) {
                if (driverTools.element(PASSENGER_CONTAINER).isEmpty()) {
                    driverTools.clickWithJavascriptExecutor(PASSENGER_TITLE);
                } else {
                    clickPassengerContainer(PASSENGER_CONTAINER, infantIndex);
                }
                driverTools.clickWithJavascriptExecutor(getPassengerSalutationInput(infantIndex));
                enterPassengerFullName(infantIndex - 1);
                enterPassengerDateOfBirth(infantIndex);
                enterPassengerCity(infantIndex);
                infantIndex++;
            }
            clickSavePassengerDetail();
        }
    }

    private Locator getInfantPassengerContainerLocator(int index) {
        return new Locator("PASSENGER_CONTAINER_" + index, new By.ByXPath("(//div[contains(@class, 'PassengerDetailSection_card')]//p[contains(text(), 'Infant') or contains(text(), 'Bayi')])[" + index + "]"));
    }

    private void fillElderPassengerDetails(int elderPassenger, int elderIndex, int elderLastIndex) {
        for (int i = 0; i < elderPassenger; i++) {
            Locator PASSENGER_CONTAINER = getElderPassengerContainerLocator(i + 1);
            while (elderIndex <= elderLastIndex) {
                if (driverTools.element(PASSENGER_CONTAINER).isEmpty()) {
                    driverTools.clickWithJavascriptExecutor(PASSENGER_TITLE);
                } else {
                    clickPassengerContainer(PASSENGER_CONTAINER, elderIndex);
                }
                driverTools.clickWithJavascriptExecutor(getPassengerSalutationInput(elderIndex));
                enterPassengerFullName(elderIndex - 1);
                enterPassengerAge(elderIndex - 1, String.valueOf(CoreFunctions.generateRandomInt(61, 99)));
                enterPassengerCity(elderIndex);
                enterPassengerIdNumberRandom(elderIndex - 1);
                elderIndex++;
            }
        }
    }

    private Locator getElderPassengerContainerLocator(int index) {
        return new Locator("PASSENGER_CONTAINER_" + index, new By.ByXPath("(//div[contains(@class, 'PassengerDetailSection_card')]//p[contains(text(), 'Senior') or contains(text(), 'Lansia') or contains(text(), 'Elderly')])[" + index + "]"));
    }

    public void clickTncCheckBox() {
        WebElement tncCheckBox = driverTools.waitForElementToAppear(TNC_CHECK_BOX).orElseThrow();
        driverTools.scrollIntoWebView(tncCheckBox);
        driverTools.clickWithJavascriptExecutor(TNC_CHECK_BOX);
    }

    public void continueToPaymentPage() {
        driverTools.clickWithJavascriptExecutor(SUBMIT_BOOKING_BUTTON);
        driverTools.waitForElementToAppear(CONTINUE_PAYMENT_BUTTON).ifPresent(driverTools::clickWithJavascriptExecutor);
    }

    public boolean verifyBookingPriceInIDR() {
        List<WebElement> priceList = driverTools.elements(TOTAL_PRICE_TEXT).orElseThrow();
        for (WebElement priceElement : priceList) {
            String priceText = driverTools.getText(priceElement);
            if (!priceText.contains("IDR")) {
                return false;
            }
        }
        return true;
    }

    public void verifyAlertSameIdNumber() {
        driverTools.waitForElementToAppear(ALERT_CONTAINER);
        Assertion.assertThat("Red outline is displayed for alert on passenger detail form", driverTools.isElementDisplayed(ALERT_CONTAINER), is(true));

        driverTools.waitForElementToAppear(ID_NUMBER_INPUT_ALERT);
        Assertion.assertThat("ID number input is displayed with alert on passenger detail form", driverTools.isElementDisplayed(ID_NUMBER_INPUT_ALERT), is(true));

        driverTools.waitForElementToAppear(VERIFICATION_FAILED_TEXT);
        Assertion.assertThat("Verification failed text is displayed on passenger detail form", driverTools.isElementDisplayed(VERIFICATION_FAILED_TEXT), is(true));

        driverTools.waitForElementToAppear(LETS_FILL_IN_TEXT);
        Assertion.assertThat("Let's fill in text is displayed on passenger detail form", driverTools.isElementDisplayed(LETS_FILL_IN_TEXT), is(true));
    }

    public boolean isBookingPageOpened() {
        driverTools.waitForElementToAppear(BOOKING_FORM_WRAPPER);
        return driverTools.isElementDisplayed(BOOKING_FORM_WRAPPER);
    }
}