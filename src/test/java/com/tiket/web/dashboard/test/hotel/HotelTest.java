package com.tiket.web.dashboard.test.hotel;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.home.HomePage;
import com.tiket.web.dashboard.page.hotel.HotelPage;
import org.testng.annotations.Test;

import java.util.Map;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class HotelTest extends BaseTest {

    @Author(names = {"Areeb"})
    @Module(name = "Hotel")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void sampleTest() {
        Map<String, String> map = getParam("sampleTest");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelPage<?> hotelPage = new HotelPage<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        step("Goto hotel");
        hotelPage.gotoHotelLandingPage();

        Assertion.assertThat("Hotel page is loaded", hotelPage.isHotelPageLoaded(), is(true));

        step("Click on tiket logo");
        hotelPage.clickOnTiketLogo();

        Assertion.assertThat("Homepage is loaded", homePage.isHomePageLoaded(), is(true));
    }

    @Author(names = {"Areeb"})
    @Module(name = "Hotel")
    @Tribe(name = "Hotel")
    @Vertical(name = "Accommodation")
    @Test
    public void sampleTest2() {
        Map<String, String> map = getParam("sampleTest2");
        BaseTest.paramsThreadLocal.set(map);

        Session<?> session = BaseTest.sessionThreadLocal.get();
        HotelPage<?> hotelPage = new HotelPage<>(session);
        HomePage<?> homePage = new HomePage<>(session);

        step("Goto hotel");
        hotelPage.gotoHotelLandingPage();

        Assertion.assertThat("Hotel page is loaded", hotelPage.isHotelPageLoaded(), is(true));

        step("Click on tiket logo");
        hotelPage.clickOnTiketLogo();

        Assertion.assertThat("Homepage is loaded", homePage.isHomePageLoaded(), is(true));
        Assertion.fail("Intentional failure");
    }
}
