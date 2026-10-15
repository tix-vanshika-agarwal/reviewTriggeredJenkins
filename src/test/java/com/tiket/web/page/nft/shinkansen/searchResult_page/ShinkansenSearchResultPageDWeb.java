package com.tiket.web.page.nft.shinkansen.searchResult_page;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ShinkansenSearchResultPageDWeb<T extends WebDriver> extends ShinkansenSearchResultPageWeb<T> {

    public ShinkansenSearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    public String clickTrainNameFilter() {
        driverTools.click(TRAIN_NAME_FILTER);
        driverTools.waitForElementToAppear(FIRST_NAME_FILTER);
        String trainType = driverTools.getText(FIRST_NAME_FILTER).orElseThrow();
        driverTools.click(FIRST_NAME_FILTER);
        return trainType;
    }
}
