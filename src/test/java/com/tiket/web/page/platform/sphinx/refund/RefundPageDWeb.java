package com.tiket.web.page.platform.sphinx.refund;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.tiket.model.Locator;
import com.tiket.model.Session;
public class RefundPageDWeb<T extends WebDriver> extends RefundPageWeb<T> {
    @Override
    public Locator getPopUpBody() {
        return new Locator("POPUP_BODY", By.xpath("//*[contains(@class,'modal_header')]"));
    }

    @Override
    public Locator getClosePopupButton() {
        return new Locator("CLOSE_BREAKDOWN_POPUP", new By.ByXPath("//*[@data-testid='modal-close-button']"));
    }

    public RefundPageDWeb(Session<T> session) {
        super(session);
    }

    protected Locator headerBackRefund() {
        return new Locator("BACK_BUTTON_AT_REFUND", new By.ByXPath("//div[contains(@class,'styles_back_button')]"));
    }
}