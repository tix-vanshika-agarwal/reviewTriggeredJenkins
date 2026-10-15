package com.tiket.web.page.platform.sphinx.refund;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;

import static org.hamcrest.Matchers.is;

public class RefundDetailDWeb<T extends WebDriver> extends RefundDetailWeb<T> {
    private static final Locator CLOSE_MODAL = new Locator("CLOSE_MODAL", By.xpath("//*[@data-testid='modal-close-button']"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", By.xpath("//*[@data-testid='back-button']"));

    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", By.xpath("//div[contains(@class, 'styles_detail_hotel__')]/span"));

    public RefundDetailDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void closeModal() {
        driverTools.click(CLOSE_MODAL);
    }

    @Override
    public void clickBackButton() {
        driverTools.scrollIntoWebView(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void openVerticalDetails() {
        // no implementation for DWeb
    }

    @Override
    public boolean isVerticalDetailsOpened() {
        // no implementation for DWeb
        return true;
    }

    @Override
    public void verifyReservationDetails(String hotelName) {
        Assertion.assertThat("Verify that hotel title is shown", driverTools.scrollUntilElementFound(HOTEL_ORDER_TITLE).isPresent(), is(true));
        Assertion.assertThat("Verify that hotel name is the same as ordered", driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow().getText(), is(hotelName));
        Assertion.assertThat("Verify that room type is shown", driverTools.waitForElementToPresence(ROOM_NAME).isPresent(), is(true));
        Assertion.assertThat("Verify that checkin date is shown", driverTools.waitForElementToPresence(CHECKIN_DATE).isPresent(), is(true));
    }
}
