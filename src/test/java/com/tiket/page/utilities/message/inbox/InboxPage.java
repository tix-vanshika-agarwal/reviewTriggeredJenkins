package com.tiket.page.utilities.message.inbox;

import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.utilities.InboxApi;
import com.tiket.apiPage.Unm;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.is;

public abstract class InboxPage <T extends WebDriver> extends BasePage<T> {

    public InboxPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> InboxPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new InboxPageAndroid<>(session);}
            case IOS -> {return new InboxPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    public record HotelDataSummary(String orderId,String iternaryNumber,String checkInDate, String checkOutDate,String timeZone,String specialRequest, String appLang) {
    }

    public abstract void openRefundNotification();
    public abstract void verifyOnInboxListPage(); // FIXME: Implementation pending
    public abstract void verifyOnInboxDetailPage(); // FIXME: Implementation pending
    public abstract void clickInbox(String deeplinkCondition); // FIXME: Implementation pending
    public abstract void verifyInboxSubtitle(); // FIXME: Implementation pending
    public abstract void verifyDeeplinkRedirect(); // FIXME: Implementation pending
    public abstract boolean verifyWeAreOnInboxPage();
    public abstract boolean verifyInboxList();
    public abstract boolean checkInboxListHeaderAndNavigationBar();
    public abstract void openInboxChatSection();
    public abstract void clickLegalBanner();
    public abstract boolean verifyLegalBannerAppear();
    public abstract boolean checkOrderSummaryByTitle(String title);
    public abstract void openSummaryOrderByTitle(String title);
    public abstract boolean verifyHotelOrderSummaryData(HotelDataSummary data);
    public abstract boolean verifyLegalBannerBottomSheetAppear();
    public abstract boolean verifyUnreadCount(Map<String,Integer> unreadData);
    public abstract void refresh();
    public abstract boolean isNewInboxAdded(String inboxIdentifier);
    public abstract void clickNewInboxAndBack(String inboxIdentifier);
    public abstract boolean verifyScrollAndTriggerPagination();
    public abstract void sendChat(String message);
    public abstract boolean checkMessageSent(String message);
    public abstract boolean cehckReplyMessageSent(String message);
    public abstract boolean replyChatInChatRoom(int pressDuration,String message);
    public abstract boolean isReplyChatOptionAvailable(int pressDuration);
    public abstract void clickThreeDotInChatRoom();
    public abstract boolean openHelpCenterFromChatRoom();
    public abstract boolean closeThreeElipsisBtmSheet();
    public abstract boolean isChatRoomOpen();
    public abstract boolean checkBackButton();
    public abstract boolean verifyUnreadCountDecreased(Map<String,Integer> unreadDataUpdated, Map<String,Integer> unreadData1);

    @SneakyThrows
    public void postNewInbox(String username, String password,String subtitle){

        Unm<T> unm = new Unm<>(session);
        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        long accountId = authData.accountId;

        InboxApi.Inputs inboxApiInput = new InboxApi.Inputs(accountId, subtitle);

        InboxApi inboxApi = new InboxApi(inboxApiInput);
        ApiResponse inboxResponse = inboxApi.hitApi(apiTools);

        Assertion.assertThat("Verify new inbox via api success", inboxResponse.getStatusCode(), is(200));
    }

    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();

        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }
}
