package com.tiket.web.dashboard.test.payment;

import com.tiket.annotation.Author;
import com.tiket.annotation.Module;
import com.tiket.annotation.Tribe;
import com.tiket.annotation.Vertical;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BaseTest;
import com.tiket.web.dashboard.page.order.CreateCarRentalOrderPage;
import com.tiket.web.dashboard.page.order.CreateFerryOrderPage;
import com.tiket.web.dashboard.page.order.orderCreationPage;
import com.tiket.web.dashboard.page.payment.PaymentDashboardPage;
import com.tiket.web.dashboard.page.payment.PaymentGroupingPage;
import com.tiket.web.dashboard.page.promo.PromoDashboard;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.tiket.core.CoreFunctions.*;
import static com.tiket.testdata.TestData.getObjectParams;
import static com.tiket.testdata.TestData.getParam;
import static org.hamcrest.core.Is.is;

public class PaymentGroupingTest extends BaseTest{

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test(dataProvider = "addPaymentRecommendationFerryVerticalDataProvider")
    public void addPaymentRecommendationFerryVertical(Map<String, String> map) throws InterruptedException {
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String colourEN = map.get("colourEN");
        String colourID = map.get("colourID");
        String LabelEN = map.get("LabelEN");
        String LabelID = map.get("LabelID");
        String TagEN = map.get("TagEN");
        String TagID = map.get("TagID");
        String paymentmethod = map.get("paymentmethod");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        int prevCount = paymentGroupingPage.getNumberOfPaymentRecommendations();
        step("Delete the payment recommendation if greater than 2");
        int countAfterDeletion = paymentGroupingPage.deleteThePaymentRecommendAndReduceCount(prevCount);
        
        step("Fetch payment recommendation from panel");
        List<String> paymentRecommendationsFromPanelSideAfterDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromPanelSideAfterDeletion.size(), is(2));

        step("Check payment recommendation is already present or not");
        if(paymentGroupingPage.shouldWeAddPaymentMethod(paymentmethod)){
                step("Click on add new button");
                paymentGroupingPage.clickOnAdd();

                step("Select payment method");
                paymentGroupingPage.clickAddNewPaymentMethod();
                paymentGroupingPage.ClickOnAddPaymentMethod(paymentmethod);

                step("Input the color");
                paymentGroupingPage.inputColors(colourEN,colourID);

                step("Input label name details");
                paymentGroupingPage.inputTitle(LabelEN,LabelID);

                step("Input tag line details");
                paymentGroupingPage.inputSubTitle(TagEN,TagID);

                step("Click on save button");
                paymentGroupingPage.clickAddNewSaveButton();

                step("Save the payment Recommendation");
                paymentGroupingPage.savePayment();
                paymentGroupingPage.clickOK();

                step("Verify the text and count of payment method added");
                String text = paymentGroupingPage.getStringPaymentRecommendation();
                int count = paymentGroupingPage.getNumberOfPaymentRecommendations();
                Assertion.assertThat("Verify the text", text,is(paymentmethod));
                Assertion.assertThat("Verify the count", count, is(countAfterDeletion+1));

                step("Fetch payment recommendation from mongo database");
                List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);

                step("Fetch payment recommendation from panel");
                List<String> paymentRecommendationsFromPanelSide = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

                step("Override the payment recommendation");
                paymentGroupingPage.scrollToTop();
                paymentGroupingPage.clickOverrideSwitch();
                paymentGroupingPage.clickYes();
                paymentGroupingPage.clickOK();

                step("Verify payment recommendation on panel side and mongo db is same or not");
                Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromPanelSide, is(paymentRecommendationsFromMongoDb));

                step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
                if("OVO".equals(paymentmethod) || "ONEKLIK".equals(paymentmethod) || "PAY_LATER".equals(paymentmethod) || "BLIPAY".equals(paymentmethod)){
                    Logger.getLogger("This payment method id not supported on dweb or account is not registered");
                }
                else{
                    List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
                    step("Verify the payment recommendations from panel side and consumer side is same or not");
                    Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSide, is(paymentRecommendationsFromConsumerSide));
                }
        }
        else{
            Logger.getLogger("This payment method is already present");
        }
    }   
    @DataProvider(name = "addPaymentRecommendationFerryVerticalDataProvider")
    public Object[][] testPartialRefundHalfPromoGVDataProvider() {return getObjectParams("addPaymentRecommendationFerryVertical");}

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyDuplicatePaymentRecommendationFromOtherVerticalThenClickNoInSavePayment() throws InterruptedException {
        Map<String, String> map = getParam("verifyDuplicatePaymentRecommendationFromOtherVerticalThenClickNoInSavePayment");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));
        
        List<String> paymentRecommendationsFromPanelSideBeforeDuplication = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Click on duplicate payment recommendation vertical dropdown");
        paymentGroupingPage.clickDropdown();

        step("Select any vertical in dropdown");
        paymentGroupingPage.selectVerticalInDuplicateDropdown("FLIGHT");

        step("Click on yes button");
        paymentGroupingPage.clickYesAndWaitForRecommendations();
        List<String> paymentRecommendationsFromMongoDBAfterDuplication = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the data in mongo is not changed without saving the recommendations", paymentRecommendationsFromMongoDBAfterDuplication, is(paymentRecommendationsFromPanelSideBeforeDuplication));

        step("Save the grouping and click on no");
        paymentGroupingPage.clickOnSavePayment();
        paymentGroupingPage.clickNoButton();

        step("Refresh the payment grouping page to get the exact recommendations");
        promoDashboard.refreshPage();
        paymentGroupingPage.isPaymentRecommendationsDisplayed();

        step("Fetch payment recommendations on Panel side");
        List<String> paymentRecommendationsFromPanelSideAfterDuplication = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        Assertion.assertThat("Verify recommendations on panel side before duplication and after duplication is same", paymentRecommendationsFromPanelSideAfterDuplication, is(paymentRecommendationsFromPanelSideBeforeDuplication));

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);

        step("Verify payment recommendation on panel side and mongo db is same or not");
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromPanelSideBeforeDuplication, is(paymentRecommendationsFromMongoDb));
    
        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeDuplication.contains("OVO") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideBeforeDuplication, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyDuplicatePaymentRecommendationFromOtherVerticalThenClickNo() throws InterruptedException {
        Map<String, String> map = getParam("verifyDuplicatePaymentRecommendationFromOtherVerticalThenClickNo");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));
        
        List<String> paymentRecommendationsFromPanelSideBeforeDuplication = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Click on duplicate payment recommendation vertical dropdown");
        paymentGroupingPage.clickDropdown();

        step("Select any vertical in dropdown");
        paymentGroupingPage.selectVerticalInDuplicateDropdown("FLIGHT");

        step("Click on no button");
        paymentGroupingPage.clickNoAndWaitForRecommendations();
        List<String> paymentRecommendationsFromMongoDBAfterDuplication = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the data in mongo is not changed without saving the recommendations", paymentRecommendationsFromMongoDBAfterDuplication, is(paymentRecommendationsFromPanelSideBeforeDuplication));

        step("Save the grouping and click on ok button");
        paymentGroupingPage.savePayment();
        paymentGroupingPage.clickOK();

        step("Fetch payment recommendations on Panel side");
        List<String> paymentRecommendationsFromPanelSideAfterDuplication = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        Assertion.assertThat("Verify recommendations on panel side before duplication and after duplication is same", paymentRecommendationsFromPanelSideAfterDuplication, is(paymentRecommendationsFromPanelSideBeforeDuplication));

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);

        step("Verify payment recommendation on panel side and mongo db is same or not");
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromPanelSideBeforeDuplication, is(paymentRecommendationsFromMongoDb));
    
        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeDuplication.contains("OVO") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeDuplication.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideBeforeDuplication, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserCanCancelToAddPaymentRecommendation() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserCanCancelToAddPaymentRecommendation");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");
        String colourEN = map.get("colourEN");
        String colourID = map.get("colourID");
        String LabelEN = map.get("LabelEN");
        String LabelID = map.get("LabelID");
        String TagEN = map.get("TagEN");
        String TagID = map.get("TagID");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        List<String> paymentRecommendationsFromPanelSideBeforeAdding = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Click on add new button");
        paymentGroupingPage.clickOnAdd();

        step("Select payment method");
        paymentGroupingPage.clickAddNewPaymentMethod();
        paymentGroupingPage.clickFirstPaymentRecommendation();

        step("Input the color");
        paymentGroupingPage.inputColors(colourEN,colourID);

        step("Input label name details");
        paymentGroupingPage.inputTitle(LabelEN,LabelID);

        step("Input tag line details");
        paymentGroupingPage.inputSubTitle(TagEN,TagID);

        step("Click on cancel button");
        paymentGroupingPage.clickOnCancelButton();

        step("Save the payment Recommendation");
        paymentGroupingPage.savePayment();
        paymentGroupingPage.clickOK();

        List<String> paymentRecommendationsFromPanelSideAfterAdding = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        step("Verify that the paymentRecommendations before adding and after adding is same or not");
        Assertion.assertThat("Verify paymentRecommendations before adding and after adding is same or not", paymentRecommendationsFromPanelSideAfterAdding, is(paymentRecommendationsFromPanelSideBeforeAdding));

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromMongoDb, is(paymentRecommendationsFromPanelSideBeforeAdding));

        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeAdding.contains("OVO") || paymentRecommendationsFromPanelSideBeforeAdding.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeAdding.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeAdding.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideBeforeAdding, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserCancelToDeleteRecommendationByClickingNoInDeletePrompt() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserCancelToDeleteRecommendationByClickingNoInDeletePrompt");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        List<String> paymentRecommendationsFromPanelSideBeforeDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        step("Delete the payment recommendation and click on no");
        paymentGroupingPage.clickTrashButton();
        paymentGroupingPage.clickNoButton();

        step("Save the payment Recommendation");
        paymentGroupingPage.savePayment();
        paymentGroupingPage.clickOK();

        List<String> paymentRecommendationsFromPanelSideAfterDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        step("Verify that the paymentRecommendations before deletion and after deletion is same or not");
        Assertion.assertThat("Verify paymentRecommendations before deletion and after deletion is same or not", paymentRecommendationsFromPanelSideAfterDeletion, is(paymentRecommendationsFromPanelSideBeforeDeletion));

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromMongoDb, is(paymentRecommendationsFromPanelSideBeforeDeletion));

        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeDeletion.contains("OVO") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideBeforeDeletion, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserCancelToDeleteRecommendationByClickingNoInSaveConfirmationPrompt() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserCancelToDeleteRecommendationByClickingNoInSaveConfirmationPrompt");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        PromoDashboard<?> promoDashboard = new PromoDashboard<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        List<String> paymentRecommendationsFromPanelSideBeforeDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Delete the payment recommendation and click on yes");
        paymentGroupingPage.clickTrashButton();
        paymentGroupingPage.clickYes();

        step("Save the grouping and click on no");
        paymentGroupingPage.clickOnSavePayment();
        paymentGroupingPage.clickNoButton();

        step("Refresh the payment grouping page to get the exact recommendations");
        promoDashboard.refreshPage();
        paymentGroupingPage.isPaymentRecommendationsDisplayed();

        List<String> paymentRecommendationsFromPanelSideAfterDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        step("Verify that the paymentRecommendations before deletion and after deletion is same or not");
        Assertion.assertThat("Verify paymentRecommendations before deletion and after deletion is same or not", paymentRecommendationsFromPanelSideAfterDeletion, is(paymentRecommendationsFromPanelSideBeforeDeletion));

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromMongoDb, is(paymentRecommendationsFromPanelSideBeforeDeletion));

        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeDeletion.contains("OVO") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideBeforeDeletion, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyAddingTwoPaymentRecommendations() throws InterruptedException {
        Map<String, String> map = getParam("verifyAddingTwoPaymentRecommendations");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateFerryOrderPage<?> fer = new CreateFerryOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"FERRY\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("email");
        String passwordDweb = map.get("passwordDweb");
        String origin = map.get("origin");
        String destination = map.get("destination");
        String departureDate = map.get("departureDate");
        String departureTime = map.get("departureTime").replace("-", ":");
        String vehicleType = map.get("vehicleType");
        int adult = Integer.parseInt(map.get("adult"));
        int child = Integer.parseInt(map.get("child"));
        int infant = Integer.parseInt(map.get("infant"));
        int elderly = Integer.parseInt(map.get("elderly"));
        String accountId = map.get("accountId");
        String colourEN = map.get("colourEN");
        String colourID = map.get("colourID");
        String LabelEN = map.get("LabelEN");
        String LabelID = map.get("LabelID");
        String TagEN = map.get("TagEN");
        String TagID = map.get("TagID");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        List<String> paymentRecommendationsFromPanelSideBeforeDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Delete two times the payment recommendation and click on yes");
        paymentGroupingPage.clickTrashButton();
        paymentGroupingPage.clickYes();
        paymentGroupingPage.clickTrashButton();
        paymentGroupingPage.clickYes();

        List<String> paymentRecommendationsFromPanelSideAfterDeletion = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();
        Assertion.assertThat("Verify the payment recommendation count is equal to one ", paymentRecommendationsFromPanelSideAfterDeletion.size(), is(1));

        step("Add the first payment method");
        paymentGroupingPage.clickOnAdd();
        paymentGroupingPage.clickAddNewPaymentMethod();
        paymentGroupingPage.ClickOnAddPaymentMethod(paymentRecommendationsFromPanelSideBeforeDeletion.get(1));
        paymentGroupingPage.inputColors(colourEN,colourID);
        paymentGroupingPage.inputTitle(LabelEN,LabelID);
        paymentGroupingPage.inputSubTitle(TagEN,TagID);
        paymentGroupingPage.clickAddNewSaveButton();

        step("Add the second payment method");
        paymentGroupingPage.scrollToTop();
        paymentGroupingPage.clickOnAdd();
        paymentGroupingPage.clickAddNewPaymentMethod();
        paymentGroupingPage.ClickOnAddPaymentMethod(paymentRecommendationsFromPanelSideBeforeDeletion.get(2));
        paymentGroupingPage.inputColors(colourEN,colourID);
        paymentGroupingPage.inputTitle(LabelEN,LabelID);
        paymentGroupingPage.inputSubTitle(TagEN,TagID);
        paymentGroupingPage.clickAddNewSaveButton();

        step("Save the payment Recommendation");
        paymentGroupingPage.savePayment();
        paymentGroupingPage.clickOK();

        step("Fetch payment recommendataion from panel side");
        List<String> paymentRecommendationsFromPanelSideAfterDeletionAndAddition = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromMongoDb, is(paymentRecommendationsFromPanelSideAfterDeletionAndAddition));

        step("Override the last payment recommendation");
        paymentGroupingPage.scrollToTop();
        paymentGroupingPage.clickOnLastSecondOverrideSwitch();
        paymentGroupingPage.clickYes();
        paymentGroupingPage.clickOK();

        step("Override the last second payment recommendation");
        paymentGroupingPage.clickOverrideSwitch();
        paymentGroupingPage.clickYes();
        paymentGroupingPage.clickOK();

        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order ferry via API");
        if(paymentRecommendationsFromPanelSideBeforeDeletion.contains("OVO") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("ONEKLIK") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("PAY_LATER") || paymentRecommendationsFromPanelSideBeforeDeletion.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = fer.createWaitingPaymentOrderFerry(email,passwordDweb,origin,destination,departureDate,departureTime,vehicleType,adult,child,infant,elderly,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSideAfterDeletionAndAddition, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Hiten" })
    @Module(name = "Payment")
    @Tribe(name = "Payment")
    @Vertical(name = "PROP")
    @Test
    public void verifyUserCanDuplicatePaymentMethodsFromOtherVerticalByClickingYesInSaveConfirmation() throws InterruptedException {
        Map<String, String> map = getParam("verifyUserCanDuplicatePaymentMethodsFromOtherVerticalByClickingYesInSaveConfirmation");
        BaseTest.paramsThreadLocal.set(map);
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        CreateCarRentalOrderPage<?> orderCreationPage = new CreateCarRentalOrderPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String config = map.get("config");
        String database = map.get("database");
        String collectionName = map.get("collectionName");
        String queryString = "{ \"productType\": \"CAR\" }";
        String projectionString = "{ \"_id\": 0, \"recommendationList.paymentType\": 1 }";
        String email = map.get("username");
        String phoneNumber = map.get("phoneNumber");
        String passwordDweb = map.get("password");
        String driverOption = map.get("driverOption");
        String location = map.get("location");
        String startDate = generateRandomDate(map.get("rangeAddDate"));
        String type = map.get("type");
        String pickupTestData = map.get("pickupTime");
        StringBuilder pickup = new StringBuilder(pickupTestData);
        pickup.setCharAt(2, ':');
        String pickupTime = pickup.toString();
        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String fullName = randomName() + " Autorder";
        String nameTitle = getRandomObject(salutationList);
        int rentalDuration = Integer.parseInt(map.get("rentalDuration"));
        String accountId = map.get("accountId");

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Grouping Page");
        paymentGroupingPage.clickOnPaymentGrouping();

        step("Go to ferry vertical on Payment Grouping Page");
        paymentGroupingPage.clickFerryVertical();

        step("Checking payment recommendation is visble");
        Assertion.assertThat("Checking payment recommendation is visble", paymentGroupingPage.isPaymentRecommendationsDisplayed(), is(true));

        List<String> paymentRecommendationsFromFerryVerticalSide = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Now goto car rental vertical");
        paymentGroupingPage.clickCarRentalVertical();

        step("Click on duplicate payment recommendation vertical dropdown");
        paymentGroupingPage.clickDropdown();

        step("Select any vertical in dropdown");
        paymentGroupingPage.selectVerticalInDuplicateDropdown("FERRY");

        step("Click on yes button");
        paymentGroupingPage.clickYesAndWaitForRecommendations();
        List<String> paymentRecommendationsFromMongoDBAfterDuplication = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);

        step("Save the payment Recommendation");
        paymentGroupingPage.savePayment();
        paymentGroupingPage.clickOK();

        step("Fetch payment recommendataion from panel side");
        List<String> paymentRecommendationsFromPanelSide = paymentGroupingPage.getAllPaymentRecommendationsOnPanelSide();

        step("Fetch payment recommendation from mongo database");
        List<String> paymentRecommendationsFromMongoDb = paymentGroupingPage.getPaymentRecommendationFromMongoDB(config,database,collectionName,queryString,projectionString);
        Assertion.assertThat("Verify the payment recommendation from panel side and db side is same or not", paymentRecommendationsFromMongoDb, is(paymentRecommendationsFromPanelSide));
        Assertion.assertThat("Verify the payment recommendation from panel side of car rental vertical and ferry vertical is same", paymentRecommendationsFromFerryVerticalSide, is(paymentRecommendationsFromPanelSide));

        step("First check the order api if the order already present then pick it otherwise Create ttd waiting payment order car rental via API");
        if(paymentRecommendationsFromPanelSide.contains("OVO") || paymentRecommendationsFromPanelSide.contains("ONEKLIK") || paymentRecommendationsFromPanelSide.contains("PAY_LATER") || paymentRecommendationsFromPanelSide.contains("BLIPAY")){
            Logger.getLogger("This payment method id not supported on dweb or account is not registered");
        }
        else{
            List<String> paymentRecommendationsFromConsumerSide = orderCreationPage.createProductTillPaymentApi(email,phoneNumber,passwordDweb,driverOption,location,startDate,pickupTime,type,fullName,nameTitle,rentalDuration,accountId);
            step("Verify the payment recommendations from panel side and consumer side is same or not");
            Assertion.assertThat("Verify the payment recommendation from panel side and consumer side is same or not", paymentRecommendationsFromPanelSide, is(paymentRecommendationsFromConsumerSide));
        }
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Payment")
    @Tribe(name = "Payment Grouping")
    @Vertical(name = "Sphinx")
    @Test
    public void addNewPaymentGroup() throws InterruptedException {
        Map<String, String> map = getParam("addNewPaymentGroup");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String name = "automation" + CoreFunctions.randomName();

        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Group Display");
        paymentGroupingPage.clickPaymentMethodMasterConfiguration();
        paymentGroupingPage.clickPaymentGroupDisplay();

        step("Click on Add New Group");
        paymentGroupingPage.clickAddGroupButton();

        step("Fill the group details");
        paymentGroupingPage.fillGroupDetails(name, name);
        paymentGroupingPage.enterPaymentMethod();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("User verify the new payment group is added successfully");
        boolean isGroupAdded = paymentGroupingPage.isPaymentGroupingPageDisplayed(name);
        Assertion.assertThat("Verify the new payment group is added successfully", isGroupAdded, is(true));

        step("The user deletes the newly created payment group");
        paymentGroupingPage.deletePaymentGroup();

        step("Click on Save and Submit after deletion");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Payment")
    @Tribe(name = "Payment Grouping")
    @Vertical(name = "Sphinx")
    @Test
    public void deletePaymentGroup() throws InterruptedException {
        Map<String, String> map = getParam("deletePaymentGroup");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String name = "automation" + CoreFunctions.randomName();


        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Group Display");
        paymentGroupingPage.clickPaymentMethodMasterConfiguration();
        paymentGroupingPage.clickPaymentGroupDisplay();

        step("Click on Add New Group");
        paymentGroupingPage.clickAddGroupButton();

        step("Fill the group details");
        paymentGroupingPage.fillGroupDetails(name, name);
        paymentGroupingPage.enterPaymentMethod();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("The user deletes the newly created payment group");
        paymentGroupingPage.deletePaymentGroup();

        step("Click on Save and Submit after deletion");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("User verify the new payment group is added successfully");
        boolean isGroupAdded = paymentGroupingPage.isPaymentGroupingPageDisplayed(name);
        Assertion.assertThat("Verify the payment group is deleted successfully", isGroupAdded, is(false));
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Payment")
    @Tribe(name = "Payment Grouping")
    @Vertical(name = "Sphinx")
    @Test
    public void addNewPaymentMethod() throws InterruptedException {
        Map<String, String> map = getParam("addNewPaymentMethod");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String name = "automation" + CoreFunctions.randomName();


        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Group Display");
        paymentGroupingPage.clickPaymentMethodMasterConfiguration();
        paymentGroupingPage.clickPaymentGroupDisplay();

        step("Click on Add New Group");
        paymentGroupingPage.clickAddGroupButton();

        step("Fill the group details");
        paymentGroupingPage.fillGroupDetails(name, name);
        paymentGroupingPage.enterPaymentMethod();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("The user clicks on edit button");
        paymentGroupingPage.clickEditPaymentGroup();
        int previousBlocksize  = paymentGroupingPage.getBlockCount();

        step("The user adds new payment method");
        paymentGroupingPage.enterPaymentMethod();
        int newBlocksize  = paymentGroupingPage.getBlockCount();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("User verify the new payment method is added successfully");
        Assertion.assertThat("Verify the new payment method is added successfully", newBlocksize, is(previousBlocksize + 1));

        step("The user deletes the newly created payment group");
        paymentGroupingPage.deletePaymentGroup();

        step("Click on Save and Submit after deletion");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();
    }

    @Author(names = { "Ram Pratap Singh" })
    @Module(name = "Payment")
    @Tribe(name = "Payment Grouping")
    @Vertical(name = "Sphinx")
    @Test
    public void deletePaymentMethodOnGroup() throws InterruptedException {
        Map<String, String> map = getParam("deletePaymentMethodOnGroup");
        Session<?> session = BaseTest.sessionThreadLocal.get();
        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);

        String username = map.get("username");
        String password = map.get("password");
        String name = "automation" + CoreFunctions.randomName();


        step("Login into payment dashboard");
        paymentDashboardPage.goToAdminCarePage(username,password);
        paymentDashboardPage.openPaymentsPage();

        step("Go to Payment Group Display");
        paymentGroupingPage.clickPaymentMethodMasterConfiguration();
        paymentGroupingPage.clickPaymentGroupDisplay();

        step("Click on Add New Group");
        paymentGroupingPage.clickAddGroupButton();

        step("Fill the group details");
        paymentGroupingPage.fillGroupDetails(name, name);
        paymentGroupingPage.enterPaymentMethod();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("The user clicks on edit button");
        paymentGroupingPage.clickEditPaymentGroup();

        step("The user adds new payment method");
        paymentGroupingPage.enterPaymentMethod();
        paymentGroupingPage.enterPaymentMethod(); // for adding multiple payment methods
        int previousBlocksize  = paymentGroupingPage.getBlockCount();

        step("The user deletes the payment method");
        paymentGroupingPage.deletePaymentGroup();
        int newBlocksize  = paymentGroupingPage.getBlockCount();
        paymentGroupingPage.clickSave();

        step("Click on Save and Submit");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();

        step("User verify the payment method is deleted successfully");
        Assertion.assertThat("Verify the new payment method is added successfully", newBlocksize, is(previousBlocksize - 1));

        step("The user deletes the newly created payment group");
        paymentGroupingPage.deletePaymentGroup();

        step("Click on Save and Submit after deletion");
        paymentGroupingPage.clickAddNewSaveButton();
        paymentGroupingPage.clickSubmitButton();
    }

    @Author(names = {"Ashish Kumar Santu"})
    @Module(name = "Payment")
    @Tribe(name = "Sphinx")
    @Vertical(name = "Payment")
    @Test
    public void testPaymentGroupingDisplayFlow() {
        Map<String, String> param = getParam("testPaymentGroupingDisplayFlow");
        BaseTest.paramsThreadLocal.set(param);
        Session<?> session = BaseTest.sessionThreadLocal.get();

        PaymentDashboardPage<?> paymentDashboardPage = new PaymentDashboardPage<>(session);
        PaymentGroupingPage<?> paymentGroupingPage = new PaymentGroupingPage<>(session);
        orderCreationPage<?> orderCreation = new orderCreationPage<>(session);

        String username = param.get("username");
        String password = param.get("password");
        String userConsumer = param.get("userConsumer");
        String passConsumer = param.get("passConsumer");
        String currency = param.getOrDefault("currency", "IDR");
        String countryCode = param.getOrDefault("countryCode", "id");
        String hotelName = param.get("hotelName");
        String startDate = generateRandomDate(param.get("rangeAddDate"));
        int adult = Integer.parseInt(param.get("adult"));
        int night = Integer.parseInt(param.get("night"));
        int room = Integer.parseInt(param.get("room"));

        String editedGroupTitle = "automation-edit-" + CoreFunctions.randomName();
        String newGroupTitle = "automation-" + CoreFunctions.randomName();

        String[] salutationList = {"Mr", "Mrs", "Ms"};
        String orderFullName = randomName() + " autorder";
        String orderNameTitle = getRandomObject(salutationList);
        String guestFullName = randomName() + " autguest";
        String guestTitle = salutationList[0];

        step("Login to Payment Dashboard");
        paymentDashboardPage.goToAdminCarePage(username, password);
        paymentDashboardPage.openPaymentsPage();

        step("Open Payment Group Display page");
        paymentGroupingPage.openPaymentGroupDisplayPage();

        step("Create waiting payment hotel order");
        orderCreation.createWaitingPaymentOrderHotel(userConsumer, passConsumer, startDate, hotelName,
                adult, night, room, orderFullName, orderNameTitle, guestFullName, guestTitle, currency);
        String orderId = String.valueOf(orderCreation.getOrderId());
        String orderHash = orderCreation.getOrderHash();

        String existingLastGroupTitle = paymentGroupingPage.getLastPaymentGroupTitleFromApi(
                orderHash, orderId, currency, countryCode);

        step("Payment Group - edit existing group title and verify payment-group API");
        List<String> existingGroupPaymentMethods = paymentGroupingPage.getPaymentGroupPaymentMethodsFromApi(
                orderHash, orderId, currency, countryCode, existingLastGroupTitle);
        paymentGroupingPage.scrollToDisplayPaymentGroupSection();
        paymentGroupingPage.clickDisplayEditPaymentGroupByName(existingLastGroupTitle);
        PaymentGroupingPage.GroupFormDetails originalGroupDetails = paymentGroupingPage.getDisplayGroupFormDetails();
        paymentGroupingPage.fillGroupDetails(editedGroupTitle, editedGroupTitle);
        paymentGroupingPage.clickDisplayFormSave();
        paymentGroupingPage.saveDisplayChanges();
        paymentGroupingPage.verifyPaymentGroupOnApi(orderHash, orderId, currency, countryCode,
                editedGroupTitle, editedGroupTitle, existingGroupPaymentMethods);

        step("Payment Group - add new group with payment method and verify payment-group API");
        paymentGroupingPage.clickDisplayAddGroupButton();
        paymentGroupingPage.fillGroupDetails(newGroupTitle, newGroupTitle);
        String firstAddedPaymentMethod = paymentGroupingPage.enterPaymentMethodAndGetName();
        paymentGroupingPage.clickDisplayFormSave();
        paymentGroupingPage.saveDisplayChanges();
        Assertion.assertThat("New payment group is displayed on panel",
                paymentGroupingPage.isDisplayPaymentGroupOnList(newGroupTitle), is(true));
        paymentGroupingPage.verifyPaymentGroupOnApi(orderHash, orderId, currency, countryCode,
                newGroupTitle, newGroupTitle, List.of(firstAddedPaymentMethod));

        step("Payment Group - add payment method to new group and verify payment-group API");
        paymentGroupingPage.clickDisplayEditPaymentGroupByName(newGroupTitle);
        List<String> expectedPaymentMethodsAfterAdd = new ArrayList<>(
                paymentGroupingPage.getDisplayPaymentMethodsInGroupForm());
        String addedPaymentMethod = paymentGroupingPage.enterPaymentMethodAndGetName();
        expectedPaymentMethodsAfterAdd.add(addedPaymentMethod);
        paymentGroupingPage.clickDisplayFormSave();
        paymentGroupingPage.saveDisplayChanges();
        paymentGroupingPage.verifyPaymentGroupOnApi(orderHash, orderId, currency, countryCode,
                newGroupTitle, newGroupTitle, expectedPaymentMethodsAfterAdd);

        step("Payment Group - cancel edit on group form and verify panel");
        paymentGroupingPage.clickDisplayEditPaymentGroupByName(newGroupTitle);
        paymentGroupingPage.fillGroupDetails("temp-cancel-title", "temp-cancel-title");
        paymentGroupingPage.clickDisplayFormCancel();
        Assertion.assertThat("Payment group title unchanged after cancel edit",
                paymentGroupingPage.isDisplayPaymentGroupOnList(newGroupTitle), is(true));

        step("Payment Group - delete payment method from group and verify payment-group API");
        paymentGroupingPage.clickDisplayEditPaymentGroupByName(newGroupTitle);
        paymentGroupingPage.deleteDisplayPaymentMethodFromGroup();
        List<String> expectedPaymentMethodsAfterDelete = paymentGroupingPage.getDisplayPaymentMethodsInGroupForm();
        paymentGroupingPage.clickDisplayFormSave();
        paymentGroupingPage.saveDisplayChanges();
        paymentGroupingPage.verifyPaymentGroupOnApi(orderHash, orderId, currency, countryCode,
                newGroupTitle, newGroupTitle, expectedPaymentMethodsAfterDelete);

        step("Payment Group - delete newly created group and verify payment-group API");
        paymentGroupingPage.deleteDisplayPaymentGroupByName(newGroupTitle);
        paymentGroupingPage.saveDisplayChanges();
        paymentGroupingPage.verifyPaymentGroupNotOnApi(orderHash, orderId, currency, countryCode, newGroupTitle);

        step("Payment Group - restore original title of the existing group that was renamed");
        paymentGroupingPage.clickDisplayEditPaymentGroupByName(editedGroupTitle);
        paymentGroupingPage.fillGroupDetails(originalGroupDetails.titleEnglish(), originalGroupDetails.titleBahasa(),
                originalGroupDetails.subtitleEnglish(), originalGroupDetails.subtitleBahasa());
        paymentGroupingPage.clickDisplayFormSave();
        paymentGroupingPage.saveDisplayChanges();
        paymentGroupingPage.verifyPaymentGroupOnApi(orderHash, orderId, currency, countryCode,
                originalGroupDetails.titleEnglish(), originalGroupDetails.titleBahasa(), existingGroupPaymentMethods);
    }
}