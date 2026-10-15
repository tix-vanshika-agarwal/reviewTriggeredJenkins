package com.tiket.web.page.platform.sphinx.refund;

import com.tiket.model.Locator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.tiket.model.Session;

public class RefundPageMWeb<T extends WebDriver> extends RefundPageWeb<T> {
    @Override
    public Locator getPopUpBody() {
        return new Locator("POPUP_BODY", By.xpath("//*[@data-testid='bottom-sheet-body']"));
    }

    @Override
    public Locator getClosePopupButton() {
        return new Locator("CLOSE_BREAKDOWN_POPUP", new By.ByXPath("//*[contains(@class,'BottomSheet_close_button')]"));
    }
    
    public RefundPageMWeb(Session<T> session) {
        super(session);
    }

    protected Locator headerBackRefund() {
        return new Locator("BACK_BUTTON_AT_REFUND", new By.ByXPath("//*[@data-testid='icon-back']"));
    }
}
