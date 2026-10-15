package com.tiket.web.dashboard.page.ttd.Ignis.IgnisPartnerBookingFormPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class IgnisPartnerBookingFormPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisPartnerBookingFormPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisPartnerBookingFormPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisPartnerBookingFormPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisPartnerBookingFormPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    // contact section
    private static final Locator CONTACT_FIRST_NAME_INPUT = new Locator("CONTACT_FIRST_NAME_INPUT", By.xpath("//div[@class='order-recipient-card']//input[@name='firstName']"));
    private static final Locator CONTACT_LAST_NAME_INPUT = new Locator("CONTACT_LAST_NAME_INPUT", By.xpath("//div[@class='order-recipient-card']//input[@name='lastName']"));
    private static final Locator CONTACT_EMAIL_INPUT = new Locator("CONTACT_EMAIL_INPUT", By.xpath("//div[@class='order-recipient-card']//input[@name='emailAddress']"));
    private static final Locator CONTACT_PHONE_NUMBER_INPUT = new Locator("CONTACT_PHONE_NUMBER_INPUT", By.xpath("//div[@class='order-recipient-card']//input[@name='phoneNumber']"));

    // guest section
    private static final Locator GUEST_FIRST_NAME_INPUT = new Locator("GUEST_FIRST_NAME_INPUT", By.xpath("//div[@class='dialog-body']//input[@name='firstName']"));
    private static final Locator GUEST_LAST_NAME_INPUT = new Locator("GUEST_LAST_NAME_INPUT", By.xpath("//div[@class='dialog-body']//input[@name='lastName']"));
    private static final Locator GUEST_EMAIL_INPUT = new Locator("GUEST_EMAIL_INPUT", By.xpath("//div[@class='dialog-body']//input[@name='emailAddress']"));
    private static final Locator GUEST_PHONE_NUMBER_INPUT = new Locator("GUEST_PHONE_NUMBER_INPUT", By.xpath("//div[@class='dialog-body']//input[@name='phoneNumber']"));

    // main page
    private static final Locator SUBMIT_GUEST_FORM_BUTTON = new Locator("SUBMIT_GUEST_FORM_BUTTON", By.xpath("//div[@class='dialog-footer']//button[contains(@class,'variant-primary')]"));
    private static final Locator ADDITIONAL_INFO_COLLAPSIBLE = new Locator("ADDITIONAL_INFO_COLLAPSIBLE", By.xpath("//div[@class='collapsible-additional-information-header ']"));
    private static final Locator BOOKING_DETAIL_COLLAPSIBLE = new Locator("BOOKING_DETAIL_COLLAPSIBLE", By.xpath("//div[contains(@class,'collapsible collapsible-additional-information collapsible-booking-detail')]"));
    private static final Locator TICKET_ITEM = new Locator("TICKET_ITEM", By.xpath("//section[@class='booking-form-additional-information ']//div[@class='ticket-item']"));
    private static final Locator SUBMIT_BUTTON = new Locator("SUBMIT_BUTTON", By.xpath("//div[@class='submit-section']//button"));
    private static final Locator POPUP_SUBMIT_BUTTON = new Locator("POPUP_SUBMIT_BUTTON", By.xpath("//div[@class='modal']//button[contains(@class,'primary')]"));
    private static final Locator SELECT_SEAT_MAP = new Locator("SELECT_SEAT_MAP", By.xpath("//div[@class='seat-detail-section']//button"));

    // payment method section
    private static final Locator INTERNAL_PAYMENT = new Locator("INTERNAL_PAYMENT", By.xpath("//div[@class='internal-payment-section']"));
    private static final Locator LINK_PAYMENT = new Locator("LINK_PAYMENT", By.xpath("//div[@class='payment-via-link-section']"));
    private static final Locator TOTAL_PAYMENT = new Locator("TOTAL_PAYMENT", By.xpath("//div[@class='total-payment']/p[2]"));

    // select seat map
    private static final Locator SELECTED_CAT_CHIP = new Locator("SELECTED_CAT_CHIP", By.xpath("//button[@role='tab' and @aria-selected='true']/span"));
    private static final Locator AVAILABLE_SEAT = new Locator("AVAILABLE_SEAT", By.xpath("//div[@class='seat-container available']"));
    private static final Locator FIRST_SEAT_MAP = new Locator("FIRST_SEAT_MAP", By.xpath("//div[@class='seat-container available'][1]"));
    private static final Locator SUBMIT_SEAT_MAP = new Locator("SUBMIT_SEAT_MAP", By.xpath("//div[@class='seat-map-dialog-footer']/button[contains(@class,'primary')]"));
    private static final Locator CHIP_AFTER_SELECTED_CHIP = new Locator("CHIP_AFTER_SELECTED_CHIP", By.xpath("//button[@role='tab' and @aria-selected='true']/following-sibling::*"));
    private static final Locator NEXT_CAT_CHIP = new Locator("NEXT_CAT_CHIP", By.xpath("//button[@role='tab' and @aria-selected='true']/following-sibling::*[1]"));

    // questionnaire section
    private static final Locator QUESTIONNAIRE_DROPDOWN = new Locator("QUESTIONNAIRE_DROPDOWN", By.xpath("//div[@class='booking-detail-content']"));
    private static final Locator QUESTIONNAIRE_DROPDOWN_SELECTION = new Locator("QUESTIONNAIRE_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));

    public Map<String, Map<String, String>> fillBookingForm(Map<String, String> param) {
        Map<String, Map<String, String>> result = new HashMap<>();

        result.put("contact", fillContactForm(param));
        result.put("guest", fillGuestForm(param));

        boolean isSeatMap = Boolean.parseBoolean(param.get("isSeatMap"));
        if (isSeatMap) {
            result.put("seatMap", fillSeatMap(param));
        }

        boolean isQuestionnaire = Boolean.parseBoolean(param.get("isQuestionnaire"));
        if (isQuestionnaire) {
            result.put("questionnaire", fillQuestionnaire(param));
        }

        return result;
    }

    private Map<String, String> fillQuestionnaire(Map<String, String> param) {
        Map<String, String> answers = new HashMap<>();

        clickQuestionnaireFormCollapsibleComponent();

        answers.put("answer1", fillDropdownQuestionnaire());

        return answers;
    }

    private void clickQuestionnaireFormCollapsibleComponent() {
        driverTools.waitForElementToAppear(BOOKING_DETAIL_COLLAPSIBLE);
        driverTools.scrollElementToCenter(BOOKING_DETAIL_COLLAPSIBLE);
        driverTools.click(BOOKING_DETAIL_COLLAPSIBLE);
    }

    private String fillDropdownQuestionnaire() {
        driverTools.waitForElementToAppear(QUESTIONNAIRE_DROPDOWN);
        driverTools.click(QUESTIONNAIRE_DROPDOWN);
        driverTools.dummyWait(1);

        List<WebElement> answerList = driverTools.elementsUntilExist(QUESTIONNAIRE_DROPDOWN_SELECTION).get();

        int randomIndex = CoreFunctions.generateRandomInt(0,answerList.size()-1);
        String result = driverTools.getText(answerList.get(randomIndex));
        driverTools.click(answerList.get(randomIndex));

        return result;
    }

    private Map<String, String> fillSeatMap(Map<String, String> param) {
        Map<String, String> selectedSeatMap = new HashMap<>();
        int ticketQty = Integer.parseInt(param.get("ticketQty"));

        clickSelectSeatMap();

        if (!checkAvailableSeatCount(ticketQty)) {
            changeCategory(ticketQty);
        }

        for (int i = 0 ; i < ticketQty ; i++) {
            String selectedCatName = getSelectedCatTabText();
            String selectedSeat = selectAndGetSeatText();

            selectedSeatMap.put("seat" + i, selectedCatName + ":" + selectedSeat);
        }

        clickSubmitChosenSeatMap();

        return selectedSeatMap;
    }

    private void changeCategory(int ticketQty) {
        List<WebElement> chipsAfterSelectedChip = driverTools.elementsUntilExist(CHIP_AFTER_SELECTED_CHIP).get();

        for (int i = 0 ; i < chipsAfterSelectedChip.size() ; i++) {
            clickNextChip();
            if (checkAvailableSeatCount(ticketQty)) {
                break;
            } else {
                chipsAfterSelectedChip = driverTools.elementsUntilExist(CHIP_AFTER_SELECTED_CHIP).get();
            }
        }
    }

    private void clickNextChip() {
        driverTools.waitForElementToAppear(NEXT_CAT_CHIP);
        driverTools.click(NEXT_CAT_CHIP);
    }

    private boolean checkAvailableSeatCount(int ticketQty) {
        int availableSeatCount = driverTools.elementsUntilExist(AVAILABLE_SEAT).get().size();
        return availableSeatCount >= ticketQty;
    }

    private void clickSubmitChosenSeatMap() {
        driverTools.waitForElementToAppear(SUBMIT_SEAT_MAP);
        driverTools.click(SUBMIT_SEAT_MAP);
    }

    private String selectAndGetSeatText() {
        String seatCode = driverTools.getText(driverTools.waitForElementToAppear(FIRST_SEAT_MAP).orElseThrow());
        driverTools.click(FIRST_SEAT_MAP);

        return seatCode;
    }

    private String getSelectedCatTabText() {
        return driverTools.getText(driverTools.waitForElementToAppear(SELECTED_CAT_CHIP).orElseThrow());
    }

    private void clickSelectSeatMap() {
        driverTools.waitForElementToAppear(SELECT_SEAT_MAP);
        driverTools.click(SELECT_SEAT_MAP);
    }

    private Map<String, String> fillGuestForm(Map<String, String> param) {
        int ticketQty = Integer.parseInt(param.get("ticketQty"));
        Map<String, String> guestData = new HashMap<>();

        if (ticketQty == 1) {
            String guestSalutation = param.get("guestSalutation");
            String guestFirstName = param.get("guestFirstName");
            String guestLastName = param.get("guestLastName");
            String guestEmail = param.get("guestEmail");
            String guestPhoneNumber = param.get("guestPhoneNumber");

            clickGuestFormCollapsibleComponent();
            clickTicketItem();

            guestData.put("salutation", fillGuestSalutation(guestSalutation));
            guestData.put("firstName", fillGuestFirstName(guestFirstName));
            guestData.put("lastName", fillGuestLastName(guestLastName));
            guestData.put("email", fillGuestEmail(guestEmail));
            guestData.put("phoneNumber", fillGuestPhoneNumber(guestPhoneNumber));

            submitGuestForm();
        } else {
            clickGuestFormCollapsibleComponent();

            for (int i = 0 ; i < ticketQty ; i++) {
                String guestSalutation = param.get("guestSalutation" + (i+1));
                String guestFirstName = param.get("guestFirstName" + (i+1));
                String guestLastName = param.get("guestLastName" + (i+1));
                String guestEmail = param.get("guestEmail" + (i+1));
                String guestPhoneNumber = param.get("guestPhoneNumber" + (i+1));

                clickTicketItemByIndex(i+1);

                guestData.put("salutation" + (i+1), fillGuestSalutation(guestSalutation));
                guestData.put("firstName" + (i+1), fillGuestFirstName(guestFirstName));
                guestData.put("lastName" + (i+1), fillGuestLastName(guestLastName));
                guestData.put("email" + (i+1), fillGuestEmail(guestEmail));
                guestData.put("phoneNumber" + (i+1), fillGuestPhoneNumber(guestPhoneNumber));

                submitGuestForm();
            }
        }

        return guestData;
    }

    private void clickTicketItemByIndex(int index) {
        Locator locator = getTicketItemLocatorByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
        driverTools.dummyWait(1);
    }

    private Locator getTicketItemLocatorByIndex(int index) {
        return new Locator("TICKET_ITEM_"+index, By.xpath("(//section[@class='booking-form-additional-information ']//div[@class='ticket-item'])["+index+"]"));
    }

    private void clickTicketItem() {
        driverTools.waitForElementToAppear(TICKET_ITEM);
        driverTools.click(TICKET_ITEM);
        driverTools.dummyWait(1);
    }

    private void clickGuestFormCollapsibleComponent() {
        driverTools.waitForElementToAppear(ADDITIONAL_INFO_COLLAPSIBLE);
        driverTools.click(ADDITIONAL_INFO_COLLAPSIBLE);
        driverTools.dummyWait(1);
    }

    private void submitGuestForm() {
        driverTools.waitForElementToAppear(SUBMIT_GUEST_FORM_BUTTON);
        driverTools.click(SUBMIT_GUEST_FORM_BUTTON);
    }

    private String fillGuestPhoneNumber(String guestPhoneNumber) {
        if (guestPhoneNumber.equalsIgnoreCase("random")) {
            guestPhoneNumber = String.valueOf(CoreFunctions.randomInt(11));
        }

        driverTools.waitForElementsToAppear(GUEST_PHONE_NUMBER_INPUT);
        driverTools.sendKeys(GUEST_PHONE_NUMBER_INPUT, guestPhoneNumber);

        return guestPhoneNumber;
    }

    private String fillGuestEmail(String guestEmail) {
        if (guestEmail.equalsIgnoreCase("random")) {
            guestEmail = CoreFunctions.randomName() + "AUT@tiket.com";
        }

        driverTools.waitForElementsToAppear(GUEST_EMAIL_INPUT);
        driverTools.sendKeys(GUEST_EMAIL_INPUT, guestEmail);

        return guestEmail;
    }

    private String fillGuestLastName(String guestLastName) {
        if (guestLastName.equalsIgnoreCase("random")) {
            guestLastName = CoreFunctions.randomLastName();
        }

        driverTools.waitForElementsToAppear(GUEST_LAST_NAME_INPUT);
        driverTools.sendKeys(GUEST_LAST_NAME_INPUT, guestLastName);
        return guestLastName;
    }

    private String fillGuestFirstName(String guestFirstName) {
        if (guestFirstName.equalsIgnoreCase("random")) {
            guestFirstName = CoreFunctions.randomName();
        }

        driverTools.waitForElementsToAppear(GUEST_FIRST_NAME_INPUT);
        driverTools.sendKeys(GUEST_FIRST_NAME_INPUT, guestFirstName);

        return guestFirstName;
    }

    private String fillGuestSalutation(String guestSalutation) {
        if (guestSalutation.equalsIgnoreCase("random")) {
            int randomIndex = CoreFunctions.generateRandomInt(1, 3);

            Locator locator = getGuestSalutationLocatorByIndex(randomIndex);
            driverTools.waitForElementToAppear(locator);
            driverTools.click(locator);
        } else {
            String contactSalutationEn = guestSalutation.split("/")[0];
            String contactSalutationId = guestSalutation.split("/")[1];

            Locator locator = getGuestSalutationLocatorByText(contactSalutationEn, contactSalutationId);
            driverTools.waitForElementToAppear(locator);
            driverTools.click(locator);
        }

        return guestSalutation;
    }

    private Locator getGuestSalutationLocatorByText(String contactSalutationEn, String contactSalutationId) {
        return new Locator("SALUTATION_INPUT_"+contactSalutationEn.toUpperCase(), By.xpath("//div[@class='dialog-body']//div[@class='radio-button-group']//child::label/span[text()='"+contactSalutationEn+"' or text()='"+contactSalutationId+"']"));
    }

    private Locator getGuestSalutationLocatorByIndex(int index) {
        return new Locator("SALUTATION_INPUT_"+index, By.xpath("(//div[@class='dialog-body']//div[@class='radio-button-group']/child::*)["+index+"]"));
    }

    private Map<String, String> fillContactForm(Map<String, String> param) {
        String contactSalutation = param.get("contactSalutation");
        String contactFirstName = param.get("contactFirstName");
        String contactLastName = param.get("contactLastName");
        String contactEmail = param.get("contactEmail");
        String contactPhoneNumber = param.get("contactPhoneNumber");

        Map<String, String> contactData = new HashMap<>();

        contactData.put("salutation", fillContactSalutation(contactSalutation));
        contactData.put("firstName", fillContactFirstName(contactFirstName));
        contactData.put("lastName", fillContactLastName(contactLastName));
        contactData.put("email", fillContactEmail(contactEmail));
        contactData.put("phoneNumber", fillContactPhoneNumber(contactPhoneNumber));

        return contactData;
    }

    private String fillContactPhoneNumber(String contactPhoneNumber) {
        if (contactPhoneNumber.equalsIgnoreCase("random")) {
            contactPhoneNumber = String.valueOf(CoreFunctions.randomInt(11));
        }

        driverTools.waitForElementsToAppear(CONTACT_PHONE_NUMBER_INPUT);
        driverTools.sendKeys(CONTACT_PHONE_NUMBER_INPUT, contactPhoneNumber);

        return contactPhoneNumber;
    }

    private String fillContactEmail(String contactEmail) {
        if (contactEmail.equalsIgnoreCase("random")) {
            contactEmail = CoreFunctions.randomName() + "AUT@tiket.com";
        }

        driverTools.waitForElementsToAppear(CONTACT_EMAIL_INPUT);
        driverTools.sendKeys(CONTACT_EMAIL_INPUT, contactEmail);

        return contactEmail;
    }

    private String fillContactLastName(String contactLastName) {
        if (contactLastName.equalsIgnoreCase("random")) {
            contactLastName = CoreFunctions.randomLastName();
        }

        driverTools.waitForElementsToAppear(CONTACT_LAST_NAME_INPUT);
        driverTools.sendKeys(CONTACT_LAST_NAME_INPUT, contactLastName);

        return contactLastName;
    }

    private String fillContactFirstName(String contactFirstName) {
        if (contactFirstName.equalsIgnoreCase("random")) {
            contactFirstName = CoreFunctions.randomName();
        }

        driverTools.waitForElementsToAppear(CONTACT_FIRST_NAME_INPUT);
        driverTools.sendKeys(CONTACT_FIRST_NAME_INPUT, contactFirstName);

        return contactFirstName;
    }

    private String fillContactSalutation(String contactSalutation) {
        if (contactSalutation.equalsIgnoreCase("random")) {
            int randomIndex = CoreFunctions.generateRandomInt(1, 3);

            Locator locator = getContactSalutationLocatorByIndex(randomIndex);
            driverTools.waitForElementToAppear(locator);
            driverTools.click(locator);
        } else {
            String contactSalutationEn = contactSalutation.split("/")[0];
            String contactSalutationId = contactSalutation.split("/")[1];

            Locator locator = getContactSalutationLocatorByText(contactSalutationEn, contactSalutationId);
            driverTools.waitForElementToAppear(locator);
            driverTools.click(locator);
        }

        return contactSalutation;
    }

    private Locator getContactSalutationLocatorByText(String contactSalutationEn, String contactSalutationId) {
        return new Locator("SALUTATION_INPUT_"+contactSalutationEn.toUpperCase(), By.xpath("//div[@class='order-recipient-card']//div[@class='radio-button-group']//child::label/span[text()='"+contactSalutationEn+"' or text()='"+contactSalutationId+"']"));
    }

    private Locator getContactSalutationLocatorByIndex(int index) {
        return new Locator("SALUTATION_INPUT_"+index, By.xpath("(//div[@class='order-recipient-card']//div[@class='radio-button-group']/child::*)["+index+"]"));
    }

    public void choosePaymentMethod(String paymentType) {
        switch (paymentType.toLowerCase()) {
            case "internal" -> {
                driverTools.waitForElementToAppear(INTERNAL_PAYMENT);
                driverTools.click(INTERNAL_PAYMENT);
            }
            case "link" -> {
                driverTools.waitForElementToAppear(LINK_PAYMENT);
                driverTools.click(LINK_PAYMENT);
            }
            default -> {throw new NoSuchElementException("There is no paymentMethod such as '"+paymentType+"'");
            }
        }
    }

    public void submitBook() {
        driverTools.waitForElementToAppear(SUBMIT_BUTTON);
        driverTools.click(SUBMIT_BUTTON);

        driverTools.waitForElementToAppear(POPUP_SUBMIT_BUTTON);
        driverTools.click(POPUP_SUBMIT_BUTTON);

        driverTools.dummyWait(2);

        driverTools.waitForElementToAppear(POPUP_SUBMIT_BUTTON);
        driverTools.click(POPUP_SUBMIT_BUTTON);
    }

    public String getTotalPayment() {
        return driverTools.getText(driverTools.waitForElementToAppear(TOTAL_PAYMENT).orElseThrow());
    }
}
