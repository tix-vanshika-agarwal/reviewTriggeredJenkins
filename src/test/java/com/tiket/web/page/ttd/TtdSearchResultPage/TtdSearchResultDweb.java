package com.tiket.web.page.ttd.TtdSearchResultPage;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.web.page.ttd.TtdLandingPage.TtdLandingPageWeb;
import org.openqa.selenium.WebDriver;

public class TtdSearchResultDweb<T extends WebDriver> extends TtdSearchResultPageWeb<T> {

    public TtdSearchResultDweb(Session<T> session) {
        super(session);
    }

    @Override
    public void filterDate(int date) {
        driverTools.waitForElementsToAppear(FILTER_BUTTON);
        driverTools.click(FILTER_BUTTON);
        driverTools.waitForElementToPresence(DATE_BUTTON);
        driverTools.clickWithJavascriptExecutor(DATE_BUTTON);
        driverTools.waitForWebToLoad();
        driverTools.waitForElementToPresence(OTHER_DATE);
        driverTools.clickWithJavascriptExecutor(OTHER_DATE);

        Locator dateLocator = getDateLocator(date);
        driverTools.waitForElementToPresence(dateLocator);
        driverTools.clickWithJavascriptExecutor(dateLocator);
        driverTools.clickWithJavascriptExecutor(SAVE_DATE_BUTTON);
        driverTools.clickWithJavascriptExecutor(APPLY_BUTTON);
    }
}
