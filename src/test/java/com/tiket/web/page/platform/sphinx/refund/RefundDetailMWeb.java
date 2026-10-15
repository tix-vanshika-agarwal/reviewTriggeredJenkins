package com.tiket.web.page.platform.sphinx.refund;

import java.util.List;

import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;

import static org.hamcrest.Matchers.is;

public class RefundDetailMWeb<T extends WebDriver> extends RefundDetailWeb<T> {
    private static final Locator DETAILS_VERTICAL = new Locator("DETAILS_VERTICAL", new By.ByXPath("//*[contains(@class,'wrapper_order_detail')]//*[contains(@class,'activity')][contains(text(),'Details')]"));
    private static final Locator DETAILS_VERTICAL_BOTTOMSHEET = new Locator("DETAILS_VERTICAL_BOTTOMSHEET", new By.ByXPath("//*[contains(@data-testid,'order-breakdown-detail')]"));
    private static final Locator BOTTOMSHEET_CLOSE_BUTTON = new Locator("BOTTOMSHEET_CLOSE_BUTTON", new By.ByXPath("//*[contains(@class,'BottomSheet_close_button')]"));
    private static final Locator BACK_BUTTON = new Locator("BACK_BUTTON", new By.ByXPath("//*[@data-testid='icon-back']"));

    private static final Locator HOTEL_NAME = new Locator("HOTEL_NAME", By.xpath("//div[contains(@class, 'styles_title_wrapper__')]/span"));

    public RefundDetailMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public void closeModal() {
        driverTools.click(BOTTOMSHEET_CLOSE_BUTTON);
    }

    @Override
    public void clickBackButton() {
        driverTools.scrollIntoWebView(BACK_BUTTON);
        driverTools.click(BACK_BUTTON);
    }

    @Override
    public void openVerticalDetails() {
        driverTools.waitForElementToAppear(DETAILS_VERTICAL, 3).ifPresentOrElse(driverTools::scrollElementToCenter, () -> {
            throw new NoSuchElementException(DETAILS_VERTICAL+" element not found");
        });
        driverTools.click(DETAILS_VERTICAL);
    }

    @Override
    public boolean isVerticalDetailsOpened() {
        return driverTools.waitForElementToAppear(DETAILS_VERTICAL_BOTTOMSHEET, 3).isPresent();
    }

    @Override
    public void verifyReservationDetails(String hotelName) {
        Assertion.assertThat("Verify that hotel title is shown", driverTools.scrollUntilElementFound(HOTEL_ORDER_TITLE).isPresent(), is(true));
        Assertion.assertThat("Verify that hotel name is the same as ordered", driverTools.waitForElementToPresence(HOTEL_NAME).orElseThrow().getText(), is(hotelName));
        Assertion.assertThat("Verify that room type is shown", driverTools.waitForElementToPresence(ROOM_NAME).isPresent(), is(true));
        Assertion.assertThat("Verify that checkin date is shown", driverTools.waitForElementToPresence(CHECKIN_DATE).isPresent(), is(true));
    }
}
