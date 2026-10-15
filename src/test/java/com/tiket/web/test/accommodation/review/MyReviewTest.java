package com.tiket.web.test.accommodation.review;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.Unm;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.pojo.member.RegisterNewAccountData;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.page.accommodation.review.MyReviewPageWeb;
import com.tiket.web.page.common.home.HomePageWeb;
import com.tiket.web.page.common.home.LoginPageWeb;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import javax.security.auth.login.FailedLoginException;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class MyReviewTest<T extends WebDriver> extends BaseTest {

    @Author(names = {"Ashish"})
    @Module(name = "MyReview")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void testSubmitReviewHotelWeb() throws FailedLoginException, InterruptedException {
        Session<WebDriver> session = sessionThreadLocal.get();
        Map<String, String> map = getParam("testSubmitReviewHotelWeb");
        BaseTest.paramsThreadLocal.set(map);

        HomePageWeb<?> homePage = HomePageWeb.getInstance(session);
        LoginPageWeb<?> loginPage = LoginPageWeb.getInstance(session);
        MyReviewPageWeb<WebDriver> myReviewPage = MyReviewPageWeb.getInstance(session);
        Unm<?> unm = new Unm<>(session);

        step("User open home page");
        homePage.goToHomePage();

        step("Create new user from the API");
        RegisterNewAccountData accountData = unm.registerNewAccountViaApiUNM();
        String username = accountData.email;
        String password = accountData.password;

        step("Create dummy pending review from the API");
        String inventoryName = "Automation Review Hotel - " + CoreFunctions.randomInt(5);
        myReviewPage.postPendingReviewHotel(username, password, inventoryName);

        try {
            step("User open as logged in user");
            loginPage.loginWithSpecificAccountViaCookie(username, password);

            step("open my review page");
            homePage.goToReviewPage();

            step("fill review star");
            myReviewPage.tellTheStory(inventoryName);

            step("fill review text and send");
            myReviewPage.fillReviewItem();
            Assertion.assertThat("Review Submitted Successfully", myReviewPage.verifyAndClickReviewPopup(), is(true));

            step("assert redirection to review page after submit review");
            myReviewPage.isOnMyReviewForm();
        } finally {
            step("delete account");
            homePage.goToHomePage();
            loginPage.loginAsGuest();
            unm.deleteAccountViaApi(accountData.unmData);
        }
    }
}
