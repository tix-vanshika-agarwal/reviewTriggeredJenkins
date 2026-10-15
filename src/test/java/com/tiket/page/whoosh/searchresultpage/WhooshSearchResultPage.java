package com.tiket.page.whoosh.searchresultpage;

import com.tiket.model.Session;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.page.train.searchresultpage.TrainSearchResultPage;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

public abstract class WhooshSearchResultPage <T extends WebDriver> extends BasePage<T> {

    public WhooshSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WhooshSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new WhooshSearchResultPageAndroid<>(session);}
            case IOS -> {return new WhooshSearchResultPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public boolean verifySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        return trainSearchResultPage.verifySearch(trainAndWhooshGeneralData);
    }
    public void backToSearchPage(){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        trainSearchResultPage.backToSearchPage();
    }

    public boolean verifySRPHaveNoResult(){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        return trainSearchResultPage.verifySRPHaveNoResult();
    }

    public void clickCrossSell(){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        trainSearchResultPage.clickCrossSell();
    }

    public void selectWhoosh(){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        trainSearchResultPage.selectTrain();
    }

    public String selectWhooshAndGetPricePerPax(){
        TrainSearchResultPage<T> trainSearchResultPage = TrainSearchResultPage.getInstance(session);
        return trainSearchResultPage.selectTrainAndGetPricePerPax().pricePerPax();
    }
}
