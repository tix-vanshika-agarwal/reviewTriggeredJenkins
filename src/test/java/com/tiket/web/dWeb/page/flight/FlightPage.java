package com.tiket.web.dWeb.page.flight;

import com.tiket.model.Session;
import com.tiket.testbase.BasePage;
import com.tiket.web.dashboard.page.common.LoginB2CInDWEB;
import com.tiket.web.page.flight.bookingForm.FlightBookingFormPageWeb;
import org.openqa.selenium.WebDriver;

import javax.security.auth.login.FailedLoginException;

public class FlightPage <T extends WebDriver> extends BasePage<T> {
    public FlightPage(Session<T> session) {
        super(session);
    }

    public void loginOnBookingPage(String username,String password,String otp)  throws FailedLoginException, InterruptedException{
        LoginB2CInDWEB<?> loginB2CInDWEB = new LoginB2CInDWEB<>(session);
        loginB2CInDWEB.inputEmail(username);
        loginB2CInDWEB.inputPassword(password);
        try{
            loginB2CInDWEB.inputOTPCode(otp, 3);
        }catch (Exception e){
            logger.log(e.getMessage());
        }
        FlightBookingFormPageWeb<?> flightBookingFormPage = FlightBookingFormPageWeb.getInstance(session);
        flightBookingFormPage.waitForBookingPageToLoad();
    }
}
