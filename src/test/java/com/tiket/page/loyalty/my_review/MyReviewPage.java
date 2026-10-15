package com.tiket.page.loyalty.my_review;

import com.google.gson.Gson;
import com.tiket.api.auth.PayExpressApi;
import com.tiket.api.loyalty.PendingReviewApi;
import com.tiket.api.loyalty.SubmitReviewApi;
import com.tiket.api.utilities.PendingReviewATApi;
import com.tiket.api.utilities.PendingReviewCarRentalApi;
import com.tiket.api.utilities.PendingReviewHotelApi;
import com.tiket.api.utilities.PendingReviewTTDApi;
import com.tiket.apiPage.Unm;
import com.tiket.appium.Swipe;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Platform;
import com.tiket.model.Session;
import com.tiket.model.SortItem;
import com.tiket.model.api.ApiResponse;
import com.tiket.page.accommodation.hotel.searchresultpage.SearchResultPageIOS;
import com.tiket.page.payment.PaymentPageAndroid;
import com.tiket.pojo.loyalty.PendingReviewResponse;
import com.tiket.pojo.loyalty.ReviewOrderSaveResponse;
import com.tiket.pojo.unm.LoginDataUnmResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONObject;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.tiket.core.CoreFunctions.generateRandomString;
import static com.tiket.model.SortItem.*;
import static com.tiket.page.accommodation.hotel.reviewpage.ReviewPage.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public abstract class MyReviewPage<T extends WebDriver> extends BasePage<T> {
    private static final Logger log = LoggerFactory.getLogger(MyReviewPage.class);

    public MyReviewPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> MyReviewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {
                return new MyReviewPageAndroid<>(session);
            }
            case IOS -> {
                return new MyReviewPageIOS<>(session);
            }
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator REVIEW_DATE = new Locator("REVIEW_DATE", AppiumBy.xpath("//*[contains(@class,'ReviewCard_date')]"));
    private static final Locator LIKE_AMOUNT = new Locator("LIKE_AMOUNT", AppiumBy.xpath("//*[contains(@class,'LikeReviewButton')]//span"));
    private static final Locator RATING_VALUE = new Locator("RATING_VALUE", AppiumBy.xpath("//*[contains(@class,'ReviewCard_user')]"));
    public static final Locator BACK = new Locator("BACK", AppiumBy.xpath("//*[contains(@class,'back')]"));
    public static final Locator FIRST_REVIEW_SLIDER = new Locator("FIRST_REVIEW_SLIDER", AppiumBy.xpath("(//*[@data-testid='lottie-rating-slider-thumb' or @data-testid='review-card-slider-thumb'])[1]"));
    public static final Locator MAX_REVIEW_POINT = new Locator("MAX_REVIEW_POINT", AppiumBy.xpath("(//*[contains(@class,'RatingSlider_rating_point')])[5]"));
    private static final Locator FIRST_HELPFULNESS_LIKE_BTN = new Locator("FIRST_HELPFULNESS_LIKE_BTN", AppiumBy.xpath("(//*[contains(@class,'LikeReviewButton')])[1]//*[name()='svg']"));
    private static final Locator FIRST_HELPFULNESS_WORDING = new Locator("FIRST_HELPFULNESS_WORDING", AppiumBy.xpath("(//*[contains(@class,'LikeReviewButton')])[1]//span"));
    private static final Locator MY_REVIEW_RATING_HEADER = new Locator("MY_REVIEW_RATING_HEADR", AppiumBy.xpath("//*[contains(@class,'ReviewSummary_review_rating')]"));
    private static final Locator REVIEW_TOPIC = new Locator("REVIEW_TOPIC", AppiumBy.xpath("//*[contains(@class,'ReviewTopics_review_topics')]"));
    private static final Locator REVIEW_CARDS = new Locator("REVIEW_CARDS", AppiumBy.xpath("//*[contains(@class,'ReviewCard_review_card')]"));
    private static final Locator REVIEW_ELLIPSES = new Locator("REVIEW_ELLIPSES", AppiumBy.xpath("//*[contains(@data-testid,'report-review') and contains(@class,'ReportReviewButton')]"));
    private static final Locator REPORT_REVIEW = new Locator("REPORT_REVIEW", AppiumBy.xpath("//button//*[contains(@class,'Button') and contains(text(),'Report') or contains(text(),'Laporkan')]"));
    private static final Locator REVIEW_BUTTON = new Locator("REVIEW_BUTTON", AppiumBy.xpath("//*[@data-testid='report-review']"));
    private static final Locator SUBMIT_REVIEW = new Locator("SUBMIT_REVIEW", AppiumBy.xpath("//*[contains(text(),'Submit') or contains(text(),'Kirim')]//parent::button"));
    private static final Locator REPORT_REASONS = new Locator("REPORT_REASONS", AppiumBy.xpath("//*[contains(@name,'report-reason')]"));
    private static final Locator REVIEW_REPORTED_SNACKBAR = new Locator("REVIEW_REPORTED_SNACKBAR", AppiumBy.xpath("//*[contains(@class, 'Snackbar_text_content') and (contains(text(), 'Kamu berhasil mengirim laporan.') or contains(text(), 'You have successfully sent your report.'))]"));
    private static final Locator CATEGORY_FILTER = new Locator("CATEGORY_FILTER", AppiumBy.xpath("//*[contains(@class,'Text_text') and (contains(text(),'Category') or contains(text(),'Kategori'))]"));
    private static final Locator CATEGORY_TYPE_REVIEW_CARD = new Locator("CATEGORY_TYPE_REVIEW_CARD", AppiumBy.xpath("//*[contains(@class,'ReviewCard_traveler_type')]"));
    public static final Locator CLOSE_FILTER = new Locator("CLOSE_FILTER", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button')]"));
    public static final Locator TELL_THE_STORY_ANDROID = new Locator("TELL_THE_STORY_ANDROID", AppiumBy.xpath("//*[@class='android.widget.Button' and (@text='Tell the story' or @text='Ceritain Selengkapnya') and @package='com.tiket.gits.preprod']"));
    public static final Locator TELL_THE_STORY_IOS = new Locator("TELL_THE_STORY_IOS", AppiumBy.xpath("//*[@type='XCUIElementTypeButton' and (contains(@name,'Tell the story') or contains(@name,'Ceritain Selengkapnya'))]"));
    public static final Locator TELL_THE_STORY_BUTTON = new Locator("TELL_THE_STORY_BUTTON", AppiumBy.xpath("//button[.//span[contains(text(),'Ceritain Selengkapnya') or contains(text(),'Tell the story')]]"));
    protected static final Locator SKIP_QUICK_REVIEW = new Locator("SKIP_QUICK_REVIEW", AppiumBy.xpath("//button[contains(@class,'coachmark_done_btn') or .//span[contains(text(),'OK, got it') or contains(text(),'Oke, mengerti')]]"));
    protected static final Locator TRAVEL_TYPE = new Locator("TRAVEL_TYPE", AppiumBy.xpath("(//*[contains(@data-testid,'form-review-traveller')]//*[contains(@class,'Chip_text')])[1]"));
    protected static final Locator REVIEW_CHIP = new Locator("REVIEW_CHIP", AppiumBy.xpath("//*[contains(@data-testid,'form-review-autotext')]//*[contains(@class,'Chip_text')]"));
    private static final Locator REVIEW_IFRAME = new Locator("REVIEW_IFRAME", AppiumBy.xpath("//iframe[@id='tiket-com-localstorage-iframe']"));
    protected static final Locator REVIEW_INPUT = new Locator("REVIEW_INPUT", AppiumBy.xpath("//textarea[@data-testid='form-review-comment']"));
    private static final Locator SAVE_REVIEW_BUTTON = new Locator("SAVE_REVIEW_BUTTON", AppiumBy.xpath("//*[contains(@class,'SubmissionForm_footer_button')]"));


    public void fillReviewItem() {
        driverTools.switchToWebViewUrl("myreview/form");
        if (driverTools.isElementDisplayed(SKIP_QUICK_REVIEW)) driverTools.click(SKIP_QUICK_REVIEW);
        driverTools.waitForElementToPresence(TRAVEL_TYPE, 10).ifPresent(driverTools::click);
        if (driverTools.isElementDisplayed(REVIEW_CHIP)) {
            driverTools.scrollIntoWebView(REVIEW_CHIP);
            List<WebElement> reviewChipList = driverTools.elements(REVIEW_CHIP).orElseThrow();
            for (WebElement webElement : reviewChipList) {
                driverTools.click(webElement);
            }
        } else {
            driverTools.scrollIntoWebViewMWeb(REVIEW_INPUT);
            driverTools.waitForElementToAppear(REVIEW_INPUT, 10);
            driverTools.sendKeys(REVIEW_INPUT, generateRandomString(80));
        }
        driverTools.click(SAVE_REVIEW_BUTTON);
        driverTools.switchToNative();
    }


    public abstract Boolean verifyAndClickReviewPopup();
    public abstract void clickReviewStar(String product);
    public abstract void swipeToCard(String product);

    public void openSortFilter() {
        driverTools.switchToWebViewUrl("review");
        driverTools.click(SORT_FILTER);
        driverTools.waitForElementToAppear(CLOSE_FILTER);
        driverTools.switchToNative();
    }

    public SortItem selectSortItem() {
        SortItem[] sortItemList = {LATEST_REVIEW, MOST_HELPFUL, RATING_HIGH_TO_LOW, RATING_LOW_TO_HIGH};
        SortItem selectedSortItem = CoreFunctions.getRandomObject(sortItemList);

        driverTools.switchToWebViewUrl("review");
        switch (selectedSortItem) {
            case LATEST_REVIEW -> {
                driverTools.click(SORT_LATEST_REVIEW);
            }
            case MOST_HELPFUL -> {
                driverTools.click(SORT_MOST_HELPFUL);
            }
            case RATING_HIGH_TO_LOW -> {
                driverTools.click(SORT_RATING_HIGH_TO_LOW);
            }
            case RATING_LOW_TO_HIGH -> {
                driverTools.click(SORT_RATING_LOW_TO_HIGH);
            }
            default ->
                    throw new NotImplementedException("Given sortItem: \"%s\" is not supported", String.valueOf(selectedSortItem));
        }
        driverTools.switchToNative();
        return selectedSortItem;
    }

    public boolean checkSortResult(SortItem sortItem) {
        driverTools.switchToWebViewUrl("review");

        switch (sortItem) {
            case LATEST_REVIEW:
            case OLDEST_REVIEW:
                checkDateSort(sortItem);
                break;
            case MOST_HELPFUL:
                checkLikeSort();
                break;
            case RATING_HIGH_TO_LOW:
            case RATING_LOW_TO_HIGH:
                checkRatingSort(sortItem);
                break;
            default:
                throw new NotImplementedException("Given sortItem: \"%s\" is not supported", String.valueOf(sortItem));
        }

        driverTools.switchToNative();
        return true;
    }

    private void checkDateSort(SortItem sortItem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        List<String> dateStrList = driverTools.getTexts(REVIEW_DATE);
        List<LocalDate> dateList = dateStrList.stream()
                .map(dateStr -> LocalDate.parse(dateStr, formatter))
                .toList();

        logger.log("Date list: " + dateList);
        switch (sortItem) {
            case LATEST_REVIEW ->
                    Assertion.assertThat("Sort should be valid " + LATEST_REVIEW, dateList, is(dateList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())));
            case OLDEST_REVIEW ->
                    Assertion.assertThat("Sort should be valid " + OLDEST_REVIEW, dateList, is(dateList.stream().sorted().collect(Collectors.toList())));
            default -> throw new NotImplementedException("Given sortItem: \"%s\" is not supported", String.valueOf(sortItem));
        }
    }

    private void checkLikeSort() {
        List<String> likeAmountStrList = driverTools.getTexts(LIKE_AMOUNT);
        likeAmountStrList.replaceAll(s -> s.split(" ")[0]);
        List<Integer> likeAmountList = likeAmountStrList.stream()
                .map(Integer::parseInt)
                .toList();

        logger.log("Like amount list: " + likeAmountList);
        Assertion.assertThat("Sort should be valid " + MOST_HELPFUL, likeAmountList, is(likeAmountList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())));
    }

    private void checkRatingSort(SortItem sortItem) {
        List<String> ratingValueStrList = driverTools.getTexts(RATING_VALUE);
        List<Float> ratingValueList = ratingValueStrList.stream()
                .map(Float::parseFloat)
                .toList();

        logger.log("Rating value list: " + ratingValueList);
        switch (sortItem) {
            case RATING_HIGH_TO_LOW ->
                    Assertion.assertThat("Sort should be valid " + RATING_HIGH_TO_LOW, ratingValueList, is(ratingValueList.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList())));
            case RATING_LOW_TO_HIGH ->
                    Assertion.assertThat("Sort should be valid " + RATING_LOW_TO_HIGH, ratingValueList, is(ratingValueList.stream().sorted().collect(Collectors.toList())));
            default -> throw new NotImplementedException("Given sortItem: \"%s\" is not supported", String.valueOf(sortItem));
        }
    }

    public void tellTheStory(String product) {
        if (Configs.PLATFORM == Platform.ANDROID) driverTools.waitForElementToDisappear(PaymentPageAndroid.LOADING_STATE);
        if (Configs.PLATFORM == Platform.IOS) driverTools.waitForElementToDisappear(SearchResultPageIOS.LOADING_STATE);
        driverTools.switchToWebViewUrl("myreview");
        driverTools.waitForElementToPresence(FIRST_REVIEW_SLIDER, 60).orElseThrow();
        driverTools.click(getReviewSliderLocator(product));
        if (Configs.PLATFORM == Platform.ANDROID){
            driverTools.switchToNative();
            driverTools.waitForElementToBeClickable(TELL_THE_STORY_ANDROID);
            driverTools.click(TELL_THE_STORY_ANDROID);
        }
        else {
            driverTools.waitForElementToBeClickable(TELL_THE_STORY_BUTTON);
            driverTools.click(TELL_THE_STORY_BUTTON);
            driverTools.switchToNative();
        }
    }

    public void isOnMyReviewForm(){
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("/myreview");
        String currentUrl = driverTools.getCurrentURL();
        Assertion.assertThat("User should be on review form page", currentUrl, containsString("/myreview"));
        driverTools.switchToNative();
    }

    private Locator getReviewMaxPointLocator(String product) {
        return new Locator(product+" MAX_POINT_LOCATOR", AppiumBy.xpath("(//*[contains(@class,'RatingSlider_white_dots')])[last()]"));
    }

    private Locator getReviewSliderLocator(String product) {
        return new Locator(product+" LOCATOR", AppiumBy.xpath("(//*[(text()='"+product+"')]//parent::*//parent::*[contains(@class,'LottieRatings_form_banner') or contains(@class,'review_card_header')]//following-sibling::*[contains(@class,'LottieRatings_wrapper') or contains(@class,'RatingSlider_rating_slider')]//*[@data-testid='lottie-rating-slider-thumb' or @data-testid='review-card-slider-thumb'])[1]"));
    }

    private void validateCreatePendingReview(ApiResponse apiResponse) {
        Gson gson = new Gson();
        ReviewOrderSaveResponse reviewOrderSaveResponse = gson.fromJson(apiResponse.getBody(), ReviewOrderSaveResponse.class);
        Assertion.assertThat("Success to create pending review", reviewOrderSaveResponse.message, is("SUCCESS"));
    }

    private String getCategoryName(String category) {
        return switch (category.toLowerCase()) {
            case "solo" -> "Solo Trip/Trip Solo";
            case "family" -> "Family Trip/Trip Keluarga";
            case "couple" -> "Couple Trip/Trip Pasangan";
            case "friends" -> "Trip with Friends/Trip Bareng Teman";
            case "business" -> "Business Trip/Perjalanan Bisnis";
            case "other" -> "Other/Lainnya";
            default -> throw new NotImplementedException("Given category: \"%s\" is not supported", category);
        };

    }

    public boolean verifyCategoryFilter(String category,int page){
        String[] categoryList = category.split("/");
        boolean isCategoryFilterValid = true;
        for (String categoryItem : categoryList) {
            logger.log("user select a filter on review details page: " + categoryItem);
            String categoryName = getCategoryName(categoryItem);
            logger.log("user select a filter on review details page: " + categoryName);
            isCategoryFilterValid = isCategoryFilterValid & openAndVerifyCategoryFilter(categoryName,page);
        }
        return isCategoryFilterValid;
    }

    public boolean openAndVerifyCategoryFilter(String categoryName,int page) {
        driverTools.switchToWebViewUrl("review");
        driverTools.click(CATEGORY_FILTER);
        driverTools.waitForElementToAppear(CLOSE_FILTER);

        logger.log("user select a filter on review details page: " + categoryName);
        String selectedCategory = driverTools.getText(getCategoryFilter(categoryName)).orElseThrow();
        driverTools.click(getCategoryFilter(categoryName));

        for(int i=1;i<=page;i++){
            logger.log("checking page review {" + i + " }");
            driverTools.waitForElementToAppear(CLOSE_FILTER, 10);
            List<String> categoryTypeList = driverTools.getTexts(CATEGORY_TYPE_REVIEW_CARD);
            if (selectedCategory.contains(" ")){
                String selectedCategory1, selectedCategory2;
                if (categoryName.contains("friends")){
                    selectedCategory1 = "Group";
                    selectedCategory2 = "Grup";
                }else{
                    selectedCategory1 = selectedCategory.split(" ")[0];
                    selectedCategory2 = selectedCategory.split(" ")[1];
                }
                Assertion.assertThat("category type should be for page number " + i + " " + selectedCategory, categoryTypeList, either(everyItem(is(selectedCategory1))).or(everyItem(is(selectedCategory2))));
            }else
                Assertion.assertThat("category type should be for page number " + i + " " +   selectedCategory, categoryTypeList, everyItem(is(selectedCategory)));

            //change page next page (i+1) -> 1+1 -> 2nd page and always stop at last page
            if(i < page){
                driverTools.swipeToElement(getPageNumberLocator(i+1),Swipe.UP,4);
                driverTools.clickWithJavascriptExecutor(getPageNumberLocator(i+1));
            }

        }

        driverTools.switchToNative();
        return true;
    }

    private Locator getCategoryFilter(String category){
        String categoryEn = category.split("/")[0];
        String categoryID = category.split("/")[1];
        return new Locator("CATEGORY_FILTER", AppiumBy.xpath("//*[(contains(text(),'"+categoryEn+"') or contains(text(),'"+categoryID+"'))]"));
    }

    private Locator getPageNumberLocator(int index){
        return new Locator("PAGE_LOCATOR", AppiumBy.xpath("(//*[@data-testid='page-number-pagination'])["+index+"]"));
    }

    @SneakyThrows
    public String postPendingReviewHotel(String username, String password, String inventoryName) {
        Unm<T> unm = new Unm<>(session);

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String token = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);

        long accountId = authData.accountId;
        String userFullName = authData.accountFirstName +" "+ authData.accountLastName;
        String orderId = randomOrderIDGenerator();
        String showDate = getReviewDate("show");
        String expiryDate = getReviewDate("expiry");

        PendingReviewHotelApi.Inputs pendingReviewHotelApiInputs = new PendingReviewHotelApi.Inputs(
                accountId, userFullName, orderId, inventoryName, showDate, expiryDate, username);
        PendingReviewHotelApi pendingReviewHotelApi = new PendingReviewHotelApi(pendingReviewHotelApiInputs);

        ApiResponse apiResponse = pendingReviewHotelApi.hitApi(apiTools);
        validateCreatePendingReview(apiResponse);
        return orderId;
    }

    @SneakyThrows
    public void postPendingReviewCarRental(
            String username, String password, String automationIdentifier
    ){
        Unm<T> unm = new Unm<>(session);

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String token = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);

        long accountId = authData.accountId;
        String userFullName = authData.accountFirstName +" "+ authData.accountLastName;
        String lang = authData.lang;

        logger.log("loginStatus-> " + username + ", " + loginStatus);
        logger.log("Account ID: "+ accountId);

        SimpleDateFormat formatter = new SimpleDateFormat("ddMMHHmmss");
        Date date = new Date();
        String orderID =  formatter.format(date) + "1";

        PendingReviewCarRentalApi.Inputs pendingReviewApiInputs = new PendingReviewCarRentalApi.Inputs(accountId, userFullName, username, orderID, orderID, lang,automationIdentifier,getReviewDate("show"),getReviewDate("expiry"));
        PendingReviewCarRentalApi pendingReviewCarRentalApi = new PendingReviewCarRentalApi(pendingReviewApiInputs);
        ApiResponse pendingReviewApiResponse = pendingReviewCarRentalApi.hitApi(apiTools);

        validateCreatePendingReview(pendingReviewApiResponse);
    }
    /**
     * Posts a pending review for TTD (Tiket Travel Document).
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param automationIdentifier The automation identifier.
     * @return The order ID of the pending review.
     * @throws Exception if an error occurs during the process.
     */
    @SneakyThrows
    public String postPendingReviewTTD(String username, String password, String automationIdentifier){
        Unm<T> unm = new Unm<>(session);

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String token = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);

        String userFullName = authData.accountFirstName + " " +authData.accountLastName;
        long accountId = authData.accountId;
        String lang = authData.lang;
        String orderID = randomOrderIDGenerator();
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        logger.log("Account ID: "+ accountId);
        logger.log("Order ID: "+ orderID);
        logger.log("User fullName: "+ userFullName);
        PendingReviewTTDApi.Inputs pendingReviewApiInputs = new PendingReviewTTDApi.Inputs(accountId, userFullName, orderID, orderID, username, lang,username,automationIdentifier,getReviewDate("show"),getReviewDate("expiry"));
        PendingReviewTTDApi pendingReviewCarRentalApi = new PendingReviewTTDApi(pendingReviewApiInputs);
        ApiResponse pendingReviewApiResponse = pendingReviewCarRentalApi.hitApi(apiTools);

        validateCreatePendingReview(pendingReviewApiResponse);
        return orderID;
    }

    @SneakyThrows
    public void postPendingReviewAT(String username, String password, String automationIdentifier){
        Unm<T> unm = new Unm<>(session);

        LoginDataUnmResponse.Data authData = unm.unmLoginApi(username, password);
        String token = authData.accessToken;
        boolean loginStatus = authData.accountUsername.equals(username);
        logger.log("loginStatus-> " + username + ", " + loginStatus);

        long accountId = authData.accountId;
        String userFullName = authData.accountFirstName + " " +authData.accountLastName;
        String lang = authData.lang;
        String orderID = randomOrderIDGenerator();
        logger.log("loginStatus-> " + username + ", " + loginStatus);
        logger.log("Account ID: "+ accountId);
        logger.log("Order ID: "+ orderID);
        logger.log("User fullName: "+ userFullName);
        PendingReviewATApi.Inputs pendingReviewApiInputs = new PendingReviewATApi.Inputs(accountId, userFullName, orderID, orderID, username, lang,username,automationIdentifier,getReviewDate("show"),getReviewDate("expiry"));
        PendingReviewATApi pendingReviewApi = new PendingReviewATApi(pendingReviewApiInputs);
        ApiResponse pendingReviewApiResponse = pendingReviewApi.hitApi(apiTools);

        validateCreatePendingReview(pendingReviewApiResponse);
    }

    private String getReviewDate(String type){
        String currentDate = CoreFunctions.getCurrentDateYYYY_MM_DD();
        if(type.equals("show")){
            return CoreFunctions.addToDateYYYY_MM_DD(currentDate, -5)+"T00:10:13.960Z";
        }
        return CoreFunctions.addToDateYYYY_MM_DD(currentDate, +5)+"T00:10:13.960Z";
    }

    public Locator getReviewLocator(String Review) {
        return new Locator("REVIEW_LOCATOR", AppiumBy.xpath("//*[(text()='"+Review+"')]"));
    }
    private String getToken() throws ExecutionException, InterruptedException {
        PayExpressApi payExpressApi = new PayExpressApi();
        ApiResponse payExpressResponse = payExpressApi.hitApi(apiTools);
        JSONObject tokenObject = new JSONObject(payExpressResponse.getBody());
        JSONObject dataObject = tokenObject.getJSONObject("data");
        String accessToken = dataObject.getString("accessToken");
        return "Bearer "+accessToken;
    }

    public Boolean verifyReview(String reviewText) {
        return verifyReviewFromAccount(reviewText);
    }

    public Boolean verifyReviewFromAccount(String reviewText) {
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("myreview");
        driverTools.swipeToElement(getReviewLocator(reviewText), Swipe.UP, 3);
        boolean isReviewShown = driverTools.isElementDisplayed(getReviewLocator(reviewText));
        driverTools.switchToNative();
        return isReviewShown;
    }

    private String randomOrderIDGenerator() {
        String orderID = String.valueOf(CoreFunctions.randomInt(11));
        logger.log("Generated order ID: " +orderID);
        return orderID;
    }

    /**
     * Retrieves the current pending review for a given access token.
     *
     * @param accessToken The access token used for authentication.
     * @return The pending review response.
     * @throws Exception if an error occurs while retrieving the pending review.
     */
    @SneakyThrows
    public PendingReviewResponse getCurrentPendingReview(String accessToken){
        PendingReviewApi.Inputs inputs = new PendingReviewApi.Inputs(accessToken);
        PendingReviewApi pendingReviewApi = new PendingReviewApi(inputs);
        ApiResponse pendingReviewApiResponse = pendingReviewApi.hitApi(apiTools);
        Gson gson = new Gson();
        PendingReviewResponse response = gson.fromJson(pendingReviewApiResponse.getBody(), PendingReviewResponse.class);
        return response;
    }

    /**
     * Retrieves the review token by order ID from the given PendingReviewResponse object.
     *
     * @param pendingReviewResponse The PendingReviewResponse object containing the review data.
     * @param orderId The order ID to search for.
     * @return The review token associated with the specified order ID.
     * @throws NoSuchElementException If no review content is found for the specified order ID.
     */
    public String getReviewTokenByOrderId(PendingReviewResponse pendingReviewResponse , String orderId){
        return pendingReviewResponse.data.content.stream().filter(content -> content.orderId.equals(orderId)).findFirst().get().token;
    }

    /**
     * Posts a submitted review.
     *
     * @param accessToken The access token for authentication.
     * @param reviewToken The review token.
     * @param accountId The account ID.
     * @return True if the review was successfully submitted, false otherwise.
     * @throws Exception if an error occurs while submitting the review.
     */
    @SneakyThrows
    public boolean postSubmitReview(String accessToken , String reviewToken,String accountId){
        SubmitReviewApi.Inputs inputs = new SubmitReviewApi.Inputs(accessToken,reviewToken,accountId);
        SubmitReviewApi submitReviewApi = new SubmitReviewApi(inputs);
        ApiResponse submitReviewApiResponse = submitReviewApi.hitApi(apiTools);
        return submitReviewApiResponse.getStatusCode() == 200;
    }

    /**
     * Clicks the helpfulness button on the MyReviewPage.
     * This method waits for the webview, switches to the webview, waits for the helpfulness button to appear,
     * clicks the helpfulness button, and then switches back to the native view.
     */
    public void clickHelpfulnessButton(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        driverTools.waitForElementToAppear(FIRST_HELPFULNESS_LIKE_BTN);
        driverTools.scrollIntoWebView(FIRST_HELPFULNESS_LIKE_BTN);
        try {
            driverTools.click(FIRST_HELPFULNESS_LIKE_BTN);
        } catch (ElementClickInterceptedException e) {
            logger.log("ElementClickInterceptedException caught, trying to click again...");
            driverTools.clickWithJavascriptExecutor(FIRST_HELPFULNESS_LIKE_BTN);
        }

        driverTools.switchToNative();
    }

    /**
     * Verifies if the product review page is open.
     *
     * @return true if the product review page is open, false otherwise.
     */
    public boolean verifyProductReviewPageOpen(){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        Assertion.assertThat("Verify my review rating header visible", driverTools.isElementDisplayed(MY_REVIEW_RATING_HEADER), is(true));
        Assertion.assertThat("Verify my review topic visible", driverTools.isElementDisplayed(REVIEW_TOPIC), is(true));
        Assertion.assertThat("Verify my review rating header visible", driverTools.isElementDisplayed(REVIEW_CARDS), is(true));
        driverTools.switchToNative();
        return true;
    }

    public Locator get(String Review) {
        return new Locator("REVIEW_LOCATOR", AppiumBy.xpath("//*[(text()='"+Review+"')]"));
    }

    /**
     * Checks if the helpfulness wording changes after it is clicked.
     *
     * @param expectedWording The expected wording after the click.
     * @return true if the helpfulness wording changes as expected, false otherwise.
     */
    public boolean checkHelfulnessWordingChangeAfterClicked(String expectedWording){
        driverTools.waitForWebview();
        driverTools.switchToWebView();
        String currentText = driverTools.getText(FIRST_HELPFULNESS_WORDING).orElseThrow();
        Assertion.assertThat("Verify helpfulness wording changed", currentText, containsStringIgnoringCase(expectedWording));
        driverTools.switchToNative();
        return true;

    }

    /**
     *
     * @return true if the review is reported successfully, false otherwise.
     * @throws NoSuchElementException if the required elements are not found on the page.
     */

    public boolean reportReviewTTD() {
        //same function is working for Hotel as well
        driverTools.waitForWebview();
        driverTools.switchToWebViewUrl("review");
        Optional<List<WebElement>> reviewsRandom = driverTools.elements(REVIEW_ELLIPSES);
        List<WebElement> reviewsList = reviewsRandom.orElseThrow();
        int totalReviews = reviewsList.size();
        logger.log("Total reviews: " + totalReviews);

        Locator randomReviewLocator;

        if (totalReviews > 0) {
            int randomIndexCard = CoreFunctions.generateRandomInt(1, totalReviews);
            randomReviewLocator = getReviewLocator(randomIndexCard);
            driverTools.scrollIntoWebView(randomReviewLocator);
            driverTools.waitForElementToBeClickable(randomReviewLocator);
            if (Configs.PLATFORM == Platform.IOS) {
                driverTools.clickElementByCoordinate(randomReviewLocator);
            } else {
                driverTools.click(randomReviewLocator);
            }
        } else {
            logger.log("No reviews found to report.");
            return false;
        }
        driverTools.waitForElementToAppear(REPORT_REVIEW);
        driverTools.click(REPORT_REVIEW);
        driverTools.waitForElementToAppear(REPORT_REASONS);
        Optional<List<WebElement>> reportReasons = driverTools.elements(REPORT_REASONS);
        List<WebElement> reportReasonsList = reportReasons.orElseThrow();
        int totalReportReasons = reportReasonsList.size();
        logger.log("Total report reasons: " + totalReportReasons);

        Locator randomReportReasonLocator;

        if (totalReportReasons > 0) {
            int randomIndex = CoreFunctions.generateRandomInt(1, totalReportReasons);
            randomReportReasonLocator = getReportReasonLocator(randomIndex);
            driverTools.waitForElementToAppear(randomReportReasonLocator);
            driverTools.waitForElementToBeClickable(randomReportReasonLocator);
            driverTools.click(randomReportReasonLocator);
        } else {
            logger.log("No report reasons found.");
            return false;
        }

        driverTools.click(SUBMIT_REVIEW);
        boolean isReviewReported = driverTools.isElementPresent(REVIEW_REPORTED_SNACKBAR);
        driverTools.switchToNative();

        return isReviewReported;
    }

    private Locator getReviewLocator(int index) {
        return new Locator("review_" + index, AppiumBy.xpath("(//*[contains(@class,'ReportReviewButton_report_review')])[" + (index) + "]"));
    }

    private Locator getReportReasonLocator(int index) {
        return new Locator("reportReason_" + index, AppiumBy.xpath("(//label[contains(@class,'ReasonForReport_report_radio_wrapper__')])[" + index + "]"));
    }
}
