package com.tiket.page.train.bookingform;

import com.tiket.model.Session;
import com.tiket.pojo.common.InsuranceGeneralData;
import com.tiket.pojo.train.TrainData;
import com.tiket.pojo.train.TrainPriceDetail;
import com.tiket.pojo.whoosh.TrainAndWhooshGeneralData;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public abstract class TrainBookingPage<T extends WebDriver> extends BasePage<T> {

    public TrainBookingPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> TrainBookingPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new TrainBookingPageAndroid<>(session);}
            case IOS -> {return new TrainBookingPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public abstract void fillGuestDetail(String email);
    public abstract TrainData fillPassengerDetail(int adults, int infants);
    public abstract void selectSeat(String tripType, int adults);
    public abstract InsuranceGeneralData selectInsurance();
    public abstract void continueToPayment();
    public abstract void continueTo2ndHalf();
    public abstract void openPriceDetail(String tripType);
    public abstract void closePriceDetail();
    public abstract boolean isInsuranceShow();
    public abstract boolean isPriceBreakdownCorrect(TrainPriceDetail priceDetail);
    public abstract TrainData getDetailsTrainAndPassenger(String language);
    public abstract String getTotalPayment();
    public abstract boolean isLoggedIn();
    public abstract void openLoginPage();
    public abstract boolean verifyContactDetailData(String email,String phoneNumber,String fullName);
    public abstract String getPassengerId();
    public abstract List<String> getInsurancePrices();
    public abstract List<String> getPriceDetailPrices();
    public abstract boolean verifyFirstPageStepper();
    public abstract boolean verifySecondPageStepper();
    public abstract boolean verifyGeneralDataPriceDetail(TrainAndWhooshGeneralData trainAndWhooshGeneralData);
    public abstract boolean isPriceSumCorrect ();

    public String removeExtraData(String pricePerPax){
        String[] priceText = pricePerPax.split("/");
        return priceText[0];
    }
}