package com.tiket.web.page.flight.bookingForm;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.pojo.flight.FlightPaxDetail;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.core.Is.is;

public class FlightBookingFormPageDWeb <T extends WebDriver> extends FlightBookingFormPageWeb<T> {
    public FlightBookingFormPageDWeb(Session<T> session) { super(session);}

    private static final Locator PASSENGER_DATA_MODAL = new Locator("PASSENGER_DATA_MODAL", By.xpath("//p[contains(@class, 'BookingPassenger') and contains(@class, '_title_text')]"));

    @Override
    public void fillOutSalutation(String salutation) {
        driverTools.clickWithJavascriptExecutor(getSalutationLocator(salutation));
    }

    @Override
    public void goToPaymentPageFromBookingFormPage() {
        try{
            driverTools.scroll(30,1500);
        }catch (Exception e){
            driverTools.dummyWait(3);
        }
        WebElement continueToPayment = driverTools.waitForElementToAppear(NEXT_BUTTON).orElseThrow();
        driverTools.scrollIntoWebView(continueToPayment);
        driverTools.waitForElementToPresence(NEXT_BUTTON);
        driverTools.clickWithJavascriptExecutor(NEXT_BUTTON);
        driverTools.waitForElementToAppear(CONTINUE_TO_PAYMENT_PAGE_POPUP,10)
                .ifPresent(element -> driverTools.clickWithJavascriptExecutor(element));
        driverTools.waitForElementToAppear(I_DONT_WANT_IT_BUTTON, 2);
        if(driverTools.isElementDisplayed(I_DONT_WANT_IT_BUTTON)){
            driverTools.click(I_DONT_WANT_IT_BUTTON);
        } else {
            logger.log("I don't want it button is not displayed, skipping click.");
        }
    }

    @Override
    public void goToPaymentPageFromAddonsPage() {
        goToPaymentPageFromBookingFormPage();
    }

    public Locator checkHiddenAttributePaxFill(int index){
        return new Locator("CHECK_HIDDEN_ATTRIBUTE_PAX_FILL", By.xpath("(//*[contains(@class,'Collapse_collapse_with_transition')])["+index+"]"));
    }

    private void chooseDomesticOrInternational(int indexPax, FlightPaxDetail flightPaxDetail,String flightRoute) {
        try {
            logger.log("================ Filling passenger details for passenger " + indexPax + " ========");
            logger.log("  Type: " + flightPaxDetail.data.get(indexPax).paxType);
            logger.log("  Name: " + flightPaxDetail.data.get(indexPax).paxFirstName + " " + flightPaxDetail.data.get(indexPax).paxLastName);
            logger.log("  DOB: " + flightPaxDetail.data.get(indexPax).paxBirthDate);
        }catch (Exception e){
            logger.log("flightPaxDetail is null or empty for passenger " + indexPax);
        }

        ensurePassengerFormIsActive(indexPax);
        flightPaxDetail.data.get(indexPax).paxTitle = fillTitle(indexPax);
        driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 5);
        
        if (flightRoute.equals("domestic")) {
            fillPassengerDetailDomestic(indexPax,flightPaxDetail);
        } else if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax, flightPaxDetail, false);
        }
        
        waitForFormStability(indexPax);
        fillFullName(indexPax,flightPaxDetail);
        driverTools.waitForElementToAppear(getFullNameLocator(indexPax), 5);
        
        if (flightRoute.equals("international")) {
            fillPassengerDetailInternational(indexPax, flightPaxDetail, true);
        }
        
        waitForFormStability(indexPax);
        verifyAndRefillNameIfNeeded(indexPax, flightPaxDetail);
        logger.log("================= Filling passenger details for passenger " + indexPax + " completed ========");
    }

    private void openPassengerIfClosed(int passengerIndex) {
        String xpath = String.format("//div[@role='button' and " + ".//p[contains(normalize-space(), 'Passenger %d :')]]" + "//*[local-name()='svg']//*[local-name()='use']", passengerIndex);        Locator chevronLocator = new Locator("PASSENGER_CHEVRON_" + passengerIndex, By.xpath(xpath));
        WebElement chevron = driverTools.element(chevronLocator).orElseThrow();
        String transform = chevron.getAttribute("transform");
        if (transform != null && transform.contains("rotate(-90")) {
            logger.log("Passenger " + passengerIndex + " is closed. Opening it.");
            driverTools.clickWithJavascriptExecutor(chevronLocator);
        } else {
            logger.log("Passenger " + passengerIndex + " is already open. Skipping.");
        }
    }
    
    public FlightPaxDetail fillPassengerDetailsLogin(int adultPax, int childPax, int infantPax, String route, String airlineName) {
        logCartIdFromUrl();
        driverTools.waitForElementToAppear(PASSENGER_DATA_MODAL, 5);
        List<WebElement> passengerElements = driverTools.elements(PASSENGER_DATA_MODAL).orElseThrow();
        logger.log("Number of passenger elements found: " + passengerElements.size() + "number of passenger expected : " + (adultPax + childPax + infantPax));
        Assertion.assertThat("Number of passenger elements should be equal to total number of passengers", passengerElements.size(), is(adultPax + childPax + infantPax));
        //generate FlightPaxDetail for each passenger
        FlightPaxDetail flightPaxDetail = new FlightPaxDetail(adultPax, childPax, infantPax, route, airlineName, "Indonesia");

        int totalPax = adultPax + childPax + infantPax;
        for (int paxIndex = 1; paxIndex <= totalPax; paxIndex++) {
            openPassengerIfClosed(paxIndex);
            chooseDomesticOrInternational(paxIndex, flightPaxDetail, route);
        }
        return flightPaxDetail;
    }


    @Override
    public String getPriceText() {
        driverTools.waitForElementToAppear(PRICE_TEXT);
        driverTools.scrollElementToCenter(PRICE_TEXT);
        return driverTools.getText(driverTools.element(PRICE_TEXT).orElseThrow());
    }

    public void insuranceSelector(){
        driverTools.waitForElementToAppear(INSURANCE_SELECTOR);
        driverTools.scrollToElement(INSURANCE_SELECTOR);
        driverTools.clickWithJavascriptExecutor(INSURANCE_SELECTOR);
    }

    @Override
    public void scrollYearWheelTo(String year) {
        // not implemented
    }

    private Locator getContactDetail(String text,String type){
        return new Locator("Locator " + type.toUpperCase(),By.xpath("//input[contains(@id,'email')]"));
    }



    @Override
    public boolean verifyChangeEmail(String email){
        driverTools.waitForWebToLoad();
        boolean isEmailCorrect = driverTools.waitForElementToAppear(getContactDetail(email,"email")).isPresent();
        logger.log("is email correct after change: " + isEmailCorrect);
        return isEmailCorrect;
    }



}
