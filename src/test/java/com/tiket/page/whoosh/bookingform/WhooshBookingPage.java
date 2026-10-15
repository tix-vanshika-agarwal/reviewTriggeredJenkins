package com.tiket.page.whoosh.bookingform;

import com.tiket.model.Session;
import com.tiket.page.train.bookingform.TrainBookingPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPage;
import com.tiket.page.whoosh.bookingform.WhooshBookingPageAndroid;
import com.tiket.page.whoosh.bookingform.WhooshBookingPageIOS;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainPriceDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

public abstract class WhooshBookingPage <T extends WebDriver> extends BasePage<T> {
    public WhooshBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> WhooshBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new WhooshBookingPageAndroid<>(session);}
            case IOS -> {return new WhooshBookingPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public TrainData fillPassengerDetail(int adults, int infants){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.fillPassengerDetail(adults,infants);
    }
    public void continueTo2ndHalf(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        trainBookingPage.continueTo2ndHalf();
    }
    public void continueToPayment(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        trainBookingPage.continueToPayment();
    }
    public abstract void selectTravelPlan(String planName);

    public void fillGuestDetail(String email){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        trainBookingPage.fillGuestDetail(email);
    }

    public TrainData getDetailsWhooshAndPassenger(String language){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.getDetailsTrainAndPassenger(language);
    }

    public String getTotalPayment(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.getTotalPayment();
    }

    public void openPriceDetail(String tripType){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        trainBookingPage.openPriceDetail(tripType);
    }

    public boolean isPriceBreakdownCorrect(TrainAndWhooshGeneralData generalData){
        TrainPriceDetail trainPriceDetail = new TrainPriceDetail();
        trainPriceDetail.tripType = generalData.tripType;
        trainPriceDetail.adultNumber = generalData.adultPassenger;
        trainPriceDetail.infantNumber = generalData.infantPassenger;

        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.isPriceBreakdownCorrect(trainPriceDetail);
    }

    public void closePriceDetail(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        trainBookingPage.closePriceDetail();
    }

    public InsuranceGeneralData selectInsurance(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.selectInsurance();
    }

    public boolean verifyGeneralDataPriceDetail(TrainAndWhooshGeneralData generalData){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.verifyGeneralDataPriceDetail(generalData);
    }

    public boolean isPriceSumCorrect(){
        TrainBookingPage<T> trainBookingPage = TrainBookingPage.getInstance(session);
        return trainBookingPage.isPriceSumCorrect();
    }
}
