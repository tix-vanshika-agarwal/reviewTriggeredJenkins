package com.tiket.test.platform.liberty.my_order;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.apiPage.OrderApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.TripModel;
import com.tiket.page.common.webview.WebViewPage;
import com.tiket.page.home.HomePage;
import com.tiket.page.order.trip.TripPage;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import org.testng.annotations.Test;

import java.util.List;

import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class TripTest extends BaseTest {

    @Author(names = {"Areeb"})
    @Module(name = "TripView")
    @Tribe(name = "Order")
    @Vertical(name = "Liberty")
    @Test
    public void testTripViewInDetail() throws Exception {
        var session = sessionThreadLocal.get();
        var param = getParam("testTripViewInDetail");
        BaseTest.paramsThreadLocal.set(param);

        var email = param.get("username");
        var password = param.get("password");

        var homepage = HomePage.getInstance(session);
        var orderApi = new OrderApi<>(session);
        var tripPage = TripPage.getInstance(session);
        var tripModel = new TripModel();
        var unm = new Unm<>(session);

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(email, password);
        String apiAccessToken = authData.accessToken;

        var tripViewController = new com.tiket.controller.trip.TripviewController(
                tripModel,
                tripPage,
                orderApi,
                apiAccessToken,
                mainLogger.get()
        );

        step("Goto Home");
        homepage.gotoHome();

        step("Login as " + email + " and change currency to IDR via deeplink");
        homepage.loginWithSpecificAccountViaDeeplink(email, password, "IDR");

        step("Open trip view");
        homepage.openMyOrderPage();

        step("Verify trip view navigation and order list");
        Assertion.assertThat("Navigation via switcher is working fine", tripViewController.verifyTripViewNavigationAndOrderList(), is(true));

        step("Verify trips can be expanded and collapsed");
        Assertion.assertThat("Trips can be expanded and collapsed", tripViewController.verifyTripsCanBeExpandedAndCollapsed(), is(true));

        step("Verify see all orders button");
        Assertion.assertThat("See all orders button is working fine", tripViewController.verifySeeAllOrdersButton(), is(true));

        step("Verify order detail and eTicket navigation");
        tripViewController.verifyOrderDetailAndETiketNavigation(List.of("event","hotel","train"));

        step("Verify grouping of trips based on verticals");
        Assertion.assertThat("Trips are grouped based on verticals", tripViewController.verifyGrouping(), is(true));

        step("Verify low profile cross sell");
        Assertion.assertThat("Low profile cross sell is working fine", tripViewController.verifyLowProfileCrossSell(), is(true));

        step("Verify high profile cross sell");
        Assertion.assertThat("High profile cross sell is working fine", tripViewController.verifyHighProfileCrossSell(), is(true));
    }
}
