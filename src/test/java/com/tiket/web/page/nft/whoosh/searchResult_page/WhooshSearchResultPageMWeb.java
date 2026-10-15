package com.tiket.web.page.nft.whoosh.searchResult_page;

import com.tiket.appium.Swipe;
import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class WhooshSearchResultPageMWeb<T extends WebDriver> extends WhooshSearchResultPageWeb<T> {

    public WhooshSearchResultPageMWeb(Session<T> session) {
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