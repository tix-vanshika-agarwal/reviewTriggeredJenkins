package com.tiket.web.page.nft.shinkansen.searchResult_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ShinkansenSearchResultPageMWeb<T extends WebDriver> extends ShinkansenSearchResultPageWeb<T> {

    public ShinkansenSearchResultPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    public String clickTrainNameFilter() {
        driverTools.swipeToElement(FIRST_NAME_FILTER, Swipe.UP, 5);
        String trainType = driverTools.getText(FIRST_NAME_FILTER).orElseThrow();
        driverTools.clickWithJavascriptExecutor(FIRST_NAME_FILTER);
        return trainType;
    }
}
