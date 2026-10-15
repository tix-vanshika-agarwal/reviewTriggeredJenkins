package com.tiket.web.dashboard.test.home;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.home.HomePage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HomeTest extends BaseTest {

    @Author(names = {"Areeb"})
    @Module(name = "Home")
    @Tribe(name = "Home")
    @Vertical(name = "Platform")
    @Test
    public void sampleTest() {
        Map<String, String> map = getParam("sampleTest");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("Click on tiket logo");
        homePage.clickOnTiketLogo();

        Assertion.assertThat("Homepage is loaded", homePage.isHomePageLoaded(), is(true));
    }

    @Author(names = {"Areeb"})
    @Module(name = "Home")
    @Tribe(name = "Home")
    @Vertical(name = "Platform")
    @Test
    public void sampleTest2() {
        Map<String, String> map = getParam("sampleTest2");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HomePage<?> homePage = new HomePage<>(session);

        step("Goto home");
        homePage.gotoHome();

        step("Click on tiket logo");
        homePage.clickOnTiketLogo();

        Assertion.fail("Intentional failure");
        Assertion.assertThat("Homepage is loaded", homePage.isHomePageLoaded(), is(true));
    }
}
