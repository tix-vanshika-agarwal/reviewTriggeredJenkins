package com.tiket.web.page.nft.train.searchResult_Page;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TrainSearchResultPageDWeb <T extends WebDriver> extends TrainSearchResultPageWeb<T> {

    public TrainSearchResultPageDWeb(Session<T> session) {
        super(session);
    }

    public void selectTrainNameSectionInFilter() {
        driverTools.clickWithJavascriptExecutor(FILTER_TRAIN_NAME);
    }

    public void selectClassType() {
        driverTools.clickWithJavascriptExecutor(FILTER_CLASS_TYPE);
    }

}
