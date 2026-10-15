package com.tiket.page.train.searchresultpage;

import com.tiket.model.Session;
import com.tiket.page.train.searchpage.TrainSearchPage;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.Optional;

public abstract class TrainSearchResultPage <T extends WebDriver> extends BasePage<T> {

    public TrainSearchResultPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainSearchResultPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TrainSearchResultPageAndroid<>(session);}
            case IOS -> {return new TrainSearchResultPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public record TrainAndWhooshPrices(String pricePerPax, Optional<String> slashedPrice) {}

    public abstract boolean verifySearch(TrainAndWhooshGeneralData trainAndWhooshGeneralData);
    public abstract void backToSearchPage();
    public abstract boolean verifySRPHaveNoResult();
    public abstract void clickCrossSell();
    public abstract void selectTrain();
    public abstract TrainAndWhooshPrices selectTrainAndGetPricePerPax();
}
