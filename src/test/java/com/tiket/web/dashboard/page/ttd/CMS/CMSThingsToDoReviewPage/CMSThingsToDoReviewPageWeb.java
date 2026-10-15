package com.tiket.web.dashboard.page.ttd.CMS.CMSThingsToDoReviewPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Configs;
import com.tiket.web.WebPage;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CMSThingsToDoReviewPageWeb<T extends WebDriver> extends WebPage<T> {
    public CMSThingsToDoReviewPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> CMSThingsToDoReviewPageWeb<?> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case DWEB -> {
                return new CMSThingsToDoReviewPageDweb<>(session);
            }
            case AMWEB, IMWEB, ANDROID, IOS -> {
                return new CMSThingsToDoReviewPageMweb<>(session);
            }
            default -> throw new NotImplementedException("Unsupported platform: " + Configs.PLATFORM);
        }
    }

    private static final Locator STEPS_ELEMENT = new Locator("STEPS_ELEMENT", By.xpath("//button[@class='stepper__step-title']"));
    private static final Locator MAIN_PRIMARY_BUTTON = new Locator("MAIN_PRIMARY_BUTTON", By.xpath("//div[contains(@class,'review-') and contains(@class,'action')]/div/button[contains(@class,'primary')]"));
    private static final Locator POPUP_PRIMARY_BUTTON = new Locator("POPUP_PRIMARY_BUTTON", By.xpath("//div[contains(@class,'dialog-action')]/button[contains(@class,'primary')]"));

    public void continueUntilSubmit() {
        int stepElementCount = driverTools.elementsUntilExist(STEPS_ELEMENT).get().size();

        for (int i = 1 ; i < stepElementCount ; i++) {
            driverTools.waitForElementToAppear(MAIN_PRIMARY_BUTTON);
            driverTools.clickElementByCoordinateDWeb(MAIN_PRIMARY_BUTTON);
            driverTools.dummyWait(1);
        }
    }

    public void approveAndSubmitReview() {
        driverTools.waitForElementToAppear(MAIN_PRIMARY_BUTTON);
        driverTools.clickElementByCoordinateDWeb(MAIN_PRIMARY_BUTTON);

        driverTools.waitForElementToAppear(POPUP_PRIMARY_BUTTON);
        driverTools.click(POPUP_PRIMARY_BUTTON);
    }
}
