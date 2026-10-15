package com.tiket.web.page.nft.ferry.BookingPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FerryBookingPageDWeb <T extends WebDriver> extends FerryBookingPageWeb<T> {

    public FerryBookingPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void clickPassengerContainer(Locator passengerContainerLocator, int passengerIndex) {
        WebElement passengerContainer = driverTools.waitForElementToAppear(passengerContainerLocator, 5).orElseThrow();
        driverTools.scrollIntoWebView(passengerContainer);
        if (passengerIndex > 0) {
            driverTools.clickWithJavascriptExecutor(passengerContainer);
        }
    }

    @Override
    public void clickSavePassengerDetail() {}

    @Override
    public void clickAllowLocation() {}

    @Override
    public Locator getPassengerSalutationInput(int index) {
        return new Locator("PASSENGER_SALUTATION_" + index, new By.ByXPath("((//*[contains(@class, 'FormFieldSwitch_radio')])[" + index + "]//*[contains(@class, 'Radio_checkbox')])[1]"));
    }

    @Override
    public Locator getPassengerFullNameInput(int index) {
        return new Locator("PASSENGER_FULL_NAME_INPUT_" + index, new By.ByXPath("//input[@name='fullName" + index + "']"));
    }

    @Override
    public Locator getPassengerAgeInput(int index) {
        return new Locator( "PASSENGER_AGE_INPUT_" + index, new By.ByXPath("//input[@name='age" + index + "']"));
    }

    @Override
    public Locator getPassengerCityInput(int index) {
        return new Locator("PASSENGER_CITY_INPUT_" + index, new By.ByXPath("(//*[@id='select-origin-city' or @id='pilih-kota-domisili'])[" + index + "]"));
    }

    @Override
    public Locator getPassengerIdNumberInput(int index) {
        return new Locator("ID_NUMBER_INPUT_" + index, new By.ByXPath("//input[@name='identityNumber" + index + "']"));
    }

    @Override
    public Locator getPassengerDateOfBirthInput(int index) {
        return new Locator( "PASSENGER_DOB_INPUT_" + index, new By.ByXPath("(//*[contains(@id, 'date-of-birth') or contains(@id, 'tanggal-lahir')])[" + index + "]"));
    }
}
