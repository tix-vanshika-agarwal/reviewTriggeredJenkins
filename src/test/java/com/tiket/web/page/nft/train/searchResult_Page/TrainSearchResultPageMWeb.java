package com.tiket.web.page.nft.train.searchResult_Page;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class TrainSearchResultPageMWeb <T extends WebDriver> extends TrainSearchResultPageWeb<T> {

    public TrainSearchResultPageMWeb(Session<T> session) {
        super(session);
    }

    public void selectTrainNameSectionInFilter() {
        driverTools.scrollIntoWebView(FILTER_TRAIN_NAME);
    }

    public void selectClassType() {
        driverTools.scrollIntoWebView(FILTER_CLASS_TYPE);
    }

}
