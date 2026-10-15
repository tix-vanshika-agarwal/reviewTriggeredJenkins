package com.tiket.web.dashboard.page.ttd.Ignis.IgnisPartnerBookingPage;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.hamcrest.CoreMatchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class IgnisPartnerBookingPageWeb<T extends WebDriver> extends WebPage<T> {
    public IgnisPartnerBookingPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> IgnisPartnerBookingPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new IgnisPartnerBookingPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new IgnisPartnerBookingPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator CREATE_NEW_BOOKING_BUTTON = new Locator("CREATE_NEW_BOOKING_BUTTON", By.xpath("//div[@class='header-row']/button"));
    private static final Locator POPUP_PRODUCT_SEARCH_BAR = new Locator("POPUP_PRODUCT_SEARCH_BAR", By.xpath("//div[@class='booking-report-dialog-product']//input"));
    private static final Locator POPUP_PRODUCT_CARDS = new Locator("POPUP_PRODUCT_CARDS", By.xpath("//div[@class='product-content']//h2"));
    private static final Locator PACKAGE_SEARCH_BAR = new Locator("PACKAGE_SEARCH_BAR", By.xpath("//input[@name='search-package']"));
    private static final Locator PACKAGE_CARDS = new Locator("PACKAGE_CARDS", By.xpath("//div[@class='partner-booking-package']/p[@class='package-title']"));
    private static final Locator NEXT_FORM_BUTTON = new Locator("NEXT_FORM_BUTTON", By.xpath("//div[@class='submit-button']/button"));
    private static final Locator ORDER_DETAILS_QUEUE_NUMBER = new Locator("ORDER_DETAILS_QUEUE_NUMBER", By.xpath("//div[@class='ticket-info-text ticket-type']/span"));
    private static final Locator APPLY_FILTER_BUTTON = new Locator("APPLY_FILTER_BUTTON", By.xpath("//div[@class='filter-row action-row']//button[contains(@class,'secondary')]"));
    private static final Locator PRODUCT_DROPDOWN_SEARCH_BAR = new Locator("PRODUCT_DROPDOWN_SEARCH_BAR", By.xpath("//input[@name='filterable-select-listitem-search']"));
    private static final Locator PRODUCT_DROPDOWN = new Locator("PRODUCT_DROPDOWN", By.xpath("//div[contains(@class,'product-filter')]"));
    private static final Locator PRODUCT_DROPDOWN_SELECTION = new Locator("PRODUCT_DROPDOWN_SELECTION", By.xpath("//div[@class='menu-item']"));
    private static final Locator ORDER_DETAIL_TICKET_QTY = new Locator("ORDER_DETAIL_TICKET_QTY", By.xpath("//div[@class='order-details']//tbody//tr[./td[contains(text(),'Total Tiket') or contains(text(),'Total Ticket')]]/td[last()]"));
    private static final Locator ORDER_DETAIL_CONTACT_NAME = new Locator("ORDER_DETAIL_CONTACT_NAME", By.xpath("//div[@class='order-details']//tbody//tr[./td[contains(text(),'Nama') or contains(text(),'Name')]]/td[last()]"));
    private static final Locator ORDER_DETAIL_ISSUED_BADGE = new Locator("ORDER_DETAIL_ISSUED_BADGE", By.xpath("//div[@class='order-details']//tbody//tr[./td[contains(text(),'Status Pesanan') or contains(text(),'Order Status')]]//div[@class='badge badge-green']"));
    private static final Locator FIRST_DATE_CHIPS_TEXT = new Locator("FIRST_DATE_CHIPS_TEXT", By.xpath("//div[@class='date-wrap']/button[@name='0']/span"));
    private static final Locator DETAIL_POPUP_CROSS_BUTTON = new Locator("DETAIL_POPUP_CROSS_BUTTON", By.xpath("//div[@class='order-details']//button[@data-testid='btn-close']"));
    private static final Locator ORDER_DETAIL_TOTAL_PAYMENT = new Locator("ORDER_DETAIL_TOTAL_PAYMENT", By.xpath("//tbody/tr[./td[text()='Total Payment' or text()='Total Pembayaran']]/td[last()]"));
    private static final Locator GUEST_CARD = new Locator("ORDER_DETAIL_TOTAL_PAYMENT", By.xpath("//div[@class='order-details__ticket-list-item ticket-info']"));
    private static final Locator QUESTIONNAIRE_ANSWER_1 = new Locator("QUESTIONNAIRE_ANSWER_1", By.xpath("//div[@class='order-details__additional-info-item info-item']/div[@class='info-item__answer']"));

    public void goToPage() {
        switch (Configs.ENVIRONMENT) {
            case GK -> {
                driver.get("https://gatotkaca.tiket.com/ignis/partner-booking");
            }
            case PREPROD -> {
                driver.get("https://preprod.tiket.com/ignis/partner-booking");
            }
            case PROD -> {
                driver.get("https://www.tiket.com/ignis/partner-booking");
            }
        }
        driverTools.waitForWebToLoad();
    }

    public Map<String, String> createNewPartnerBooking(String productName, String packageName, int ticketQty, String rangeBookDate) {
        Map<String, String> bookingData = new HashMap<>();

        clickCreateBookingButton();
        inputProductSearchBar(productName);
        chooseProduct(productName);
        inputPackageSearchBar(packageName);
        choosePackage(packageName);
        bookingData.put("date", chooseDate(rangeBookDate));
        adjustTicketQty(ticketQty);
        clickNextFormButton();

        return bookingData;
    }

    private void clickNextFormButton() {
        driverTools.waitForElementToAppear(NEXT_FORM_BUTTON);
        driverTools.click(NEXT_FORM_BUTTON);
    }

    private void adjustTicketQty(int ticketQty) {
        if (ticketQty > 1) {
            //TODO: Improve If Needed
            increaseTicket(ticketQty, 1);
        } else {
            increaseTicket(ticketQty, 1);
        }
    }

    private void increaseTicket(int ticketQty, int index) {
        Locator locator = getIncreaseTicketButtonLocatorByIndex(index);

        for (int i = 0; i < ticketQty; i++) {
            driverTools.waitForElementToAppear(locator);
            driverTools.click(locator);
        }
    }

    private Locator getIncreaseTicketButtonLocatorByIndex(int index) {
        return new Locator("INCREASE_BUTTON_"+index, By.xpath("(//div[@class='quantity-editor-container']/button[2])["+index+"]"));
    }

    private String chooseDate(String rangeBookDate) {
        int minAddDate = Integer.parseInt(rangeBookDate.split("-")[0]);
        int maxAddDate = Integer.parseInt(rangeBookDate.split("-")[1]);
        int addDate = CoreFunctions.generateRandomInt(minAddDate, maxAddDate);

        String selectedDate = "";
        if (addDate > 6) {
            //TODO: Implement If Needed
        } else {
            Locator locator = getDateSelectionLocatorByAddedDate(addDate);
            selectedDate = driverTools.getText(driverTools.waitForElementToAppear(locator).orElseThrow());
            driverTools.click(locator);
        }

        return selectedDate;
    }

    private Locator getDateSelectionLocatorByAddedDate(int addDate) {
        return new Locator("DATE_CHIPS_"+addDate, By.xpath("//div[@class='date-wrap']/button[@name='"+addDate+"']/span"));
    }

    public void choosePackage(String packageName) {
        List<WebElement> packageCards = driverTools.elementsUntilExist(PACKAGE_CARDS).get();

        boolean found = false;
        for (WebElement e : packageCards) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(packageName)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no package title '" + packageName + "'");
        }
    }

    public void inputPackageSearchBar(String packageName) {
        driverTools.waitForElementToAppear(PACKAGE_SEARCH_BAR);
        driverTools.clearInput(PACKAGE_SEARCH_BAR);
        driverTools.sendKeys(PACKAGE_SEARCH_BAR, packageName);
        driverTools.dummyWait(1);
    }

    public void chooseProduct(String productName) {
        List<WebElement> productCards = driverTools.elementsUntilExist(POPUP_PRODUCT_CARDS).get();

        boolean found = false;
        for (WebElement e : productCards) {
            String elementText = driverTools.getText(e);
            if (elementText.equalsIgnoreCase(productName)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no product title '" + productName + "'");
        }
    }

    public void inputProductSearchBar(String productName) {
        driverTools.waitForElementToAppear(POPUP_PRODUCT_SEARCH_BAR);
        driverTools.clearInput(POPUP_PRODUCT_SEARCH_BAR);
        driverTools.sendKeys(POPUP_PRODUCT_SEARCH_BAR, productName);
        driverTools.dummyWait(1);
    }

    public void clickCreateBookingButton() {
        driverTools.waitForElementToAppear(CREATE_NEW_BOOKING_BUTTON);
        driverTools.click(CREATE_NEW_BOOKING_BUTTON);
    }

    public void chooseProductFilter(String productName) {
        driverTools.waitForElementToAppear(PRODUCT_DROPDOWN);
        driverTools.click(PRODUCT_DROPDOWN);
        driverTools.dummyWait(1);

        inputDropdownSearchBar(productName.toLowerCase());
        driverTools.dummyWait(1);

        List<WebElement> dropdownSelection = driverTools.elementsUntilExist(PRODUCT_DROPDOWN_SELECTION).orElseThrow();
        boolean found = false;
        for (WebElement e : dropdownSelection) {
            String elementText = driverTools.getText(e).toLowerCase();
            if (elementText.equalsIgnoreCase(productName)) {
                found = true;
                driverTools.click(e);
                break;
            }
        }

        if (!found) {
            throw new NoSuchElementException("There is no product '"+productName+"'");
        }
    }

    private void inputDropdownSearchBar(String productName) {
        driverTools.waitForElementToAppear(PRODUCT_DROPDOWN_SEARCH_BAR);
        driverTools.clearInput(PRODUCT_DROPDOWN_SEARCH_BAR);
        driverTools.sendKeys(PRODUCT_DROPDOWN_SEARCH_BAR, productName);
    }

    public void applyFilter() {
        driverTools.waitForElementToAppear(APPLY_FILTER_BUTTON);
        driverTools.click(APPLY_FILTER_BUTTON);
    }

    public void openOrderDataByIndex(int index) {
        Locator locator = getTableOrderRowByIndex(index);
        driverTools.waitForElementToAppear(locator);
        driverTools.click(locator);
    }

    private Locator getTableOrderRowByIndex(int index) {
        return new Locator("TABLE_ORDER_ROW_"+index, By.xpath("//tbody/tr["+index+"]"));
    }

    public int getQueueNumber() {
        driverTools.waitForElementToAppear(ORDER_DETAILS_QUEUE_NUMBER);
        return Integer.parseInt(driverTools.getText(ORDER_DETAILS_QUEUE_NUMBER).orElseThrow().split(": ")[1]);
    }

    public void waitAndRefresh(int seconds) {
        driverTools.dummyWait(seconds);
        driverTools.refreshWebpage();
        driverTools.waitForWebToLoad();
    }

    public int getOrderTicketQty() {
        return Integer.parseInt(driverTools.getText(driverTools.waitForElementToAppear(ORDER_DETAIL_TICKET_QTY).orElseThrow()));
    }

    public String getOrderContactName() {
        return driverTools.getText(driverTools.waitForElementToAppear(ORDER_DETAIL_CONTACT_NAME).orElseThrow());
    }

    public boolean isOrderIssued() {
        return driverTools.waitForElementToAppear(ORDER_DETAIL_ISSUED_BADGE).isPresent();
    }

    public boolean isTodayDatePickerAvailable() {
        LocalDate todayDateComplete = LocalDate.now();
        String todayDate = String.valueOf(todayDateComplete.getDayOfMonth());
        String todayMonth = String.valueOf(todayDateComplete.getMonthValue());

        String displayedDateMonth = driverTools.getText(driverTools.waitForElementToAppear(FIRST_DATE_CHIPS_TEXT).orElseThrow());
        String displayedDate = String.valueOf(displayedDateMonth.split(" ")[0]);
        String displayedMonth = String.valueOf(CoreFunctions.getMonthNumber(displayedDateMonth.split(" ")[1]));

        Assertion.assertThat("Today date is correct", todayDate + "-" + todayMonth, is(displayedDate + "-" + displayedMonth));
        return true;
    }

    public boolean checkOrderDetailMultiguest(int ticketQty, Map<String, String> bookingFormGuestData) {
        if (ticketQty == 1) {
            throw new RuntimeException("1 Ticket Qty is not multi-guest");
        }

        List<String> displayedGuestsName = new ArrayList<>();
        List<String> displayedGuestsEmail = new ArrayList<>();
        List<String> displayedGuestsPhoneNumber = new ArrayList<>();
        List<String> bookedGuestsName = new ArrayList<>();
        List<String> bookedGuestsEmail = new ArrayList<>();
        List<String> bookedGuestsPhoneNumber = new ArrayList<>();
        for (int i = 0 ; i < ticketQty ; i++) {
            String[] guestName = getOrderDetailGuestNameByIndex(i+1).split(" ");

            displayedGuestsName.add(String.join(" ", Arrays.copyOfRange(guestName, 1, guestName.length)));
            displayedGuestsEmail.add(getOrderDetailGuestEmailByIndex(i+1));
            displayedGuestsPhoneNumber.add(getOrderDetailGuestPhoneNumberByIndex(i+1).split(" ")[1]);

            bookedGuestsName.add(bookingFormGuestData.get("firstName" + (i+1)) + " " + bookingFormGuestData.get("lastName" + (i+1)));
            bookedGuestsEmail.add(bookingFormGuestData.get("email" + (i+1)));
            bookedGuestsPhoneNumber.add(bookingFormGuestData.get("phoneNumber" + (i+1)));
        }

        Assertion.assertThat("Multiguest guest name check is passed", displayedGuestsName.toArray(), arrayContainingInAnyOrder(bookedGuestsName.toArray()));
        Assertion.assertThat("Multiguest check is passed", displayedGuestsEmail.toArray(), arrayContainingInAnyOrder(bookedGuestsEmail.toArray()));
        Assertion.assertThat("Multiguest check is passed", displayedGuestsPhoneNumber.toArray(), arrayContainingInAnyOrder(bookedGuestsPhoneNumber.toArray()));

        return true;
    }

    private String getOrderDetailGuestPhoneNumberByIndex(int index) {
        Locator locator = getOrderDetailGuestPhoneNumberTextLocatorByIndex(index);
        return driverTools.getText(locator).get();
    }

    private Locator getOrderDetailGuestPhoneNumberTextLocatorByIndex(int index) {
        return new Locator("GUEST_"+index+"_PHONE_NUMBER", By.xpath("(//div[@class='order-details__ticket-list-item ticket-info']["+index+"]//div[@class='info-item__answer'])[2]"));
    }

    private String getOrderDetailGuestEmailByIndex(int index) {
        Locator locator = getOrderDetailGuestEmailTextLocatorByIndex(index);
        return driverTools.getText(locator).get();
    }

    private Locator getOrderDetailGuestEmailTextLocatorByIndex(int index) {
        return new Locator("GUEST_"+index+"_EMAIL", By.xpath("(//div[@class='order-details__ticket-list-item ticket-info']["+index+"]//div[@class='info-item__answer'])[3]"));
    }

    private String getOrderDetailGuestNameByIndex(int index) {
        Locator locator = getOrderDetailGuestNameTextLocatorByIndex(index);
        return driverTools.getText(locator).get();
    }

    private Locator getOrderDetailGuestNameTextLocatorByIndex(int index) {
        return new Locator("GUEST_"+index+"_NAME", By.xpath("(//div[@class='order-details__ticket-list-item ticket-info']["+index+"]//div[@class='info-item__answer'])[1]"));
    }

    public boolean checkOrderDetailSeatMap(int ticketQty, Map<String, String> bookingFormGuestSeatData) {
        if (ticketQty == 1) {
            String categoryName = bookingFormGuestSeatData.get("seat0").split(":")[0];
            String seatCode = bookingFormGuestSeatData.get("seat0").split(":")[1];
            Assertion.assertThat("Category is matched", getOrderDetailCategoryNameByIndex(1), is(categoryName));
            Assertion.assertThat("Seat Code is matched", getOrderDetailSeatCodeByIndex(1), is(seatCode));

            return true;
        }

        List<String> displayedCategory = new ArrayList<>();
        List<String> displayedSeatCode = new ArrayList<>();
        List<String> bookedCategory = new ArrayList<>();
        List<String> bookedSeatCode = new ArrayList<>();
        for (int i = 0 ; i < ticketQty ; i++) {
            displayedCategory.add(getOrderDetailCategoryNameByIndex(i+1));
            displayedSeatCode.add(getOrderDetailSeatCodeByIndex(i+1));

            bookedCategory.add(bookingFormGuestSeatData.get("seat" + i).split(":")[0]);
            bookedSeatCode.add(bookingFormGuestSeatData.get("seat" + i).split(":")[1]);
        }

        Assertion.assertThat("Category is matched", displayedCategory.toArray(), arrayContainingInAnyOrder(bookedCategory.toArray()));
        Assertion.assertThat("Seat Code is matched", displayedSeatCode.toArray(), arrayContainingInAnyOrder(bookedSeatCode.toArray()));

        return true;
    }

    private String getOrderDetailSeatCodeByIndex(int index) {
        Locator locator = getOrderDetailGuestSeatCodeTextLocatorByIndex(index);
        return driverTools.getText(locator).get();
    }

    private Locator getOrderDetailGuestSeatCodeTextLocatorByIndex(int index) {
        return new Locator("GUEST_"+index+"_CAT_NAME", By.xpath("//div[@class='order-details__ticket-list-item ticket-info']["+index+"]//div[@class='seat-map-wrapper']/p[2]"));
    }

    private String getOrderDetailCategoryNameByIndex(int index) {
        Locator locator = getOrderDetailGuestCategoryNameTextLocatorByIndex(index);
        return driverTools.getText(locator).get();
    }

    private Locator getOrderDetailGuestCategoryNameTextLocatorByIndex(int index) {
        return new Locator("GUEST_"+index+"_SEAT_CODE", By.xpath("//div[@class='order-details__ticket-list-item ticket-info']["+index+"]//div[@class='seat-map-wrapper']/p[1]"));
    }

    public void closeDetailPopup() {
        driverTools.waitForElementToAppear(DETAIL_POPUP_CROSS_BUTTON);
        driverTools.click(DETAIL_POPUP_CROSS_BUTTON);
    }

    public String getOrderIdByIndex(int index) {
        Locator locator = getOrderIdTextLocatorByIndex(index);
        return driverTools.getText(driverTools.waitForElementToAppear(locator).orElseThrow());
    }

    private Locator getOrderIdTextLocatorByIndex(int index) {
        return new Locator("ORDER_ID_"+index, By.xpath("//tbody/tr["+index+"]/td[1]"));
    }

    public String getTotalPaymentText() {
        return driverTools.getText(driverTools.waitForElementToAppear(ORDER_DETAIL_TOTAL_PAYMENT).orElseThrow());
    }

    public boolean checkOrderDetailNonMultiguest(Map<String, String> guest) {
        Assertion.assertThat("Guest Card is only 1", getGuestCardCount(), is(1));
        Assertion.assertThat("Guest name check is passed", getOrderDetailGuestNameByIndex(1), containsString(guest.get("firstName") + " " + guest.get("lastName")));
        Assertion.assertThat("Guest email check is passed", getOrderDetailGuestEmailByIndex(1), containsString(guest.get("email")));
        Assertion.assertThat("Guest phone number check is passed", getOrderDetailGuestPhoneNumberByIndex(1), containsString(guest.get("phoneNumber")));

        return true;
    }

    private Integer getGuestCardCount() {
        List<WebElement> guestCard = driverTools.elementsUntilExist(GUEST_CARD).get();
        return guestCard.size();
    }

    public boolean checkOrderDetailQuestionnaire(Map<String, String> questionnaire) {
        String displayedAnswer = driverTools.getText(driverTools.waitForElementToAppear(QUESTIONNAIRE_ANSWER_1).orElseThrow());

        Assertion.assertThat("Questionnaire answer check is passed", displayedAnswer, containsString(questionnaire.get("answer1")));

        return true;
    }
}