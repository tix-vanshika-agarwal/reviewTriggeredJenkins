package com.tiket.web.dashboard.test.hotel.supply.mmmodule;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.common.LoginMemberAdmin;
import com.tiket.web.dashboard.page.hotel.supply.mmmodule.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class SubsidyTest extends BaseTest {
    @Author(names = {"Venkat"})
    @Module(name = "Subsidy")
    @Tribe(name = "Supply")
    @Vertical(name = "Accommodation")
    @Test
    public void verifyMandatoryFieldWhenAddSubsidy() {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        LoginMemberAdmin<?> loginMemberAdmin = new LoginMemberAdmin<>(session);
        SubsidyPage<?> subsidyPage = new SubsidyPage<>(session);

        Map<String, String> param = getParam("verifyMandatoryFieldWhenAddSubsidy");
        BaseTest.paramsThreadLocal.set(param);

        String username = param.get("username");
        String password = param.get("password");
        String url = param.get("url");

        step("Go to member login page");
        loginMemberAdmin.gotoHome();

        step("Log In");
        boolean isLoggedInMemberAdmin = loginMemberAdmin.isUserLoggedIn(username, password);

        step("Assert that user is logged in to Member Admin");
        Assertion.assertThat("User is logged in", isLoggedInMemberAdmin, is(true));

        step("Go to View Order Dashboard");
        loginMemberAdmin.goToDashboard(url);

        step("click on Add Button");
        subsidyPage.createSubsidy();

        Assertion.assertThat("Verify Subsidy label form is opened",subsidyPage.isSubsidyLabelPageOpened(),is(true));

        step("Click save button without filling mandatory fields");
        subsidyPage.clickSaveButton();

        ArrayList<String> errorTexts=subsidyPage.getErrorTexts();

        Assertion.assertThat("Verify keyword field is not filled error is shown",errorTexts.get(0), containsString("Keyword field is required"));
        Assertion.assertThat("Verify Label ID field is not filled error is shown",errorTexts.get(1),containsString("Label.Id field is required"));
        Assertion.assertThat("Verify Label EN field is not filled error is shown",errorTexts.get(2),containsString("The Label.En field is required"));

    }
}