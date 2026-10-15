package com.tiket.page.accommodation.hotel.reviewpage;

import com.tiket.api.loyalty.GetReviewApi;
import com.tiket.core.CoreFunctions;
import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.model.api.ApiResponse;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.BasePage;
import com.tiket.testbase.Configs;
import io.appium.java_client.AppiumBy;
import lombok.SneakyThrows;
import org.apache.commons.lang3.NotImplementedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

public class ReviewPage <T extends WebDriver> extends BasePage<T> {
    public ReviewPage(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> ReviewPage<T> getInstance(Session<T> session) {
        switch (Configs.PLATFORM) {
            case ANDROID -> {return new ReviewPageAndroid<>(session);}
            case IOS -> {return new ReviewPageIOS<>(session);}
            default -> throw new NotImplementedException("Not implemented");
        }
    }

    private static final Locator REVIEW_PAGE_BACK_BTN = new Locator("REVIEW_PAGE_BACK_BTN", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_left_slot')]"));
    private static final Locator REVIEW_PAGE_TITLE = new Locator("REVIEW_PAGE_TITLE", AppiumBy.xpath("//*[contains(@class,'ModifiedHeaderMobile_content_slot')]//span[contains(text(),'Review')]"));
    private static final Locator REVIEW_PAGE_FILTER = new Locator("REVIEW_PAGE_FILTER", AppiumBy.xpath("//*[contains(@class,'FilterNavigationBottom_config_icon')]"));
    private static final Locator REVIEW_PAGE_LIKE_BTN = new Locator("REVIEW_PAGE_LIKE_BTN", AppiumBy.xpath("(//*[contains(@class,'LikeReviewButton_like_button')])[1]"));
    private static final Locator LIKE_BTN_BOTTOM_SHEET_NOT_LOGIN_TITLE = new Locator("LIKE_BTN_BOTTOM_SHEET_NOT_LOGIN_TITLE", AppiumBy.xpath("//*[contains(@class,'Figure_title') and (contains(text(),'Oops, you are not logged in') or contains(text(),'Oops, kamu belum log in'))]"));
    private static final Locator BOTTOM_SHEET_LOGIN_BTN = new Locator("BOTTOM_SHEET_LOGIN_BTN", AppiumBy.xpath("//*[contains(@class,'Button_variant_primary')]"));
    private static final Locator BOTTOM_SHEET_CANCEL_LOGIN_BTN = new Locator("BOTTOM_SHEET_CANCEL_LOGIN_BTN", AppiumBy.xpath("//*[contains(@class,'Button_variant_secondary')]"));
    public static final Locator CLOSE_FILTER = new Locator("CLOSE_FILTER", AppiumBy.xpath("//*[contains(@class,'BottomSheet_close_button')]"));
    public static final Locator SORT_FILTER = new Locator("SORT_FILTER", AppiumBy.xpath("//*[contains(@class,'Text_text') and (contains(text(),'Sort') or contains(text(),'Urutkan'))]"));
    public static final Locator SORT_RATING_HIGH_TO_LOW = new Locator("SORT_RATING_HIGH_TO_LOW", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'High to Low') or contains(text(),'Tinggi ke Rendah'))]"));
    public static final Locator SORT_RATING_LOW_TO_HIGH = new Locator("SORT_RATING_LOW_TO_HIGH", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'Low to High') or contains(text(),'Rendah ke Tinggi'))]"));
    public static final Locator SORT_LATEST_REVIEW = new Locator("SORT_LATEST_REVIEW", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'Latest Review') or contains(text(),'Review Terbaru'))]"));
    public static final Locator SORT_MOST_HELPFUL = new Locator("SORT_MOST_HELPFUL", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'Most Helpful') or contains(text(),'Paling Membantu'))]"));
    private static final Locator SORT_FIRST_REVIEW = new Locator("SORT_FIRST_REVIEW", AppiumBy.xpath("(//*[contains(@class,'ReviewCard_user_review')])[1]"));
    private static final Locator SORT_REVIEW_CARD_ALL = new Locator("SORT_REVIEW_CARD_ALL", AppiumBy.xpath("//*[contains(@class,'ReviewCard_user_review')]"));
    private static final Locator CATEGORY_FILTER = new Locator("CATEGORY_FILTER", AppiumBy.xpath("//*[contains(@class,'Text_text') and (contains(text(),'Category') or contains(text(),'Kategori'))]"));
    private static final Locator CATEGORY_FILTER_SOLO = new Locator("CATEGORY_FILTER", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'Trip Solo') or contains(text(),'Solo Trip'))]"));
    private static final Locator REVIEW_TEXT = new Locator("REVIEW_TEXT", AppiumBy.xpath("//*[contains(@class,'review_card_comment')]"));
    private static final Locator CONTENT_LOADER = new Locator("CONTENT_LOADER", AppiumBy.xpath("//*[contains(@class,'ContentLoader')]"));
    private static final Locator CATEGORY_TYPE_REVIEW_CARD = new Locator("CATEGORY_TYPE_REVIEW_CARD", AppiumBy.xpath("//*[contains(@class,'ReviewCard_traveler_type')]"));

    public void clickBackButtonReviewPage(){
        driverTools.switchToWebView();
        logger.log("Click back button on review page");
        driverTools.click(REVIEW_PAGE_BACK_BTN);
        driverTools.switchToNative();
    }

    public boolean isReviewPageDisplayed(){
        driverTools.waitForElementToAppear(REVIEW_PAGE_BACK_BTN);
        driverTools.switchToWebView();
        logger.log("Check if review page is displayed");
        boolean reviewPageTitle = driverTools.isElementDisplayed(REVIEW_PAGE_TITLE);
        Assertion.assertThat("on review page: ",reviewPageTitle,is(true));
        driverTools.switchToNative();
        return reviewPageTitle;
    }

    public void clickLikeBtn(){
        driverTools.switchToWebView();
        logger.log("Click like button on review page");
        WebElement likeBtn = driverTools.waitForElementToBeClickable(REVIEW_PAGE_LIKE_BTN).orElseThrow();
        driverTools.click(likeBtn);
        driverTools.switchToNative();
    }

    public void verifyLoginPop(){
        driverTools.switchToWebView();
        Assertion.assertThat("login pop up should be displayed: ",driverTools.isElementDisplayed(LIKE_BTN_BOTTOM_SHEET_NOT_LOGIN_TITLE),is(true));
        driverTools.switchToNative();
    }

    public void closeLoginPopup(){
        driverTools.switchToWebView();
        logger.log("Close login pop up");
        driverTools.click(BOTTOM_SHEET_CANCEL_LOGIN_BTN);
        driverTools.switchToNative();
    }

    private Locator getCategoryFilter(String category){
        String categoryEn = category.split("/")[0];
        String categoryID = category.split("/")[1];
        return new Locator("CATEGORY_FILTER", AppiumBy.xpath("//*[contains(@class,'Radio_children') and (contains(text(),'"+categoryEn+"') or contains(text(),'"+categoryID+"'))]"));
    }

    public boolean verifyCategoryFilter(String category){
        driverTools.switchToWebViewUrl("review");
        driverTools.click(CATEGORY_FILTER);
        driverTools.waitForElementToAppear(CLOSE_FILTER);

        logger.log("user select a filter on review details page: " + category);
        String selectedCategory = driverTools.getText(getCategoryFilter(category)).orElseThrow();
        driverTools.click(getCategoryFilter(category));

        List<String> categoryTypeList = driverTools.getTexts(CATEGORY_TYPE_REVIEW_CARD);
        if (selectedCategory.contains(" ")){
            String selectedCategory1 = selectedCategory.split(" ")[0];
            String selectedCategory2 = selectedCategory.split(" ")[1];
            Assertion.assertThat("category type should be " + selectedCategory, categoryTypeList, either(everyItem(containsStringIgnoringCase(selectedCategory1))).or(everyItem(containsStringIgnoringCase(selectedCategory2))));
        }else
            Assertion.assertThat("category type should be " + category, categoryTypeList, everyItem(is(selectedCategory)));
        driverTools.switchToNative();
        return true;
    }

    private void selectSortFilter(Locator sortFilter,String sortFilterName){
        driverTools.switchToWebViewUrl("review");
        driverTools.click(SORT_FILTER);
        driverTools.waitForElementToAppear(CLOSE_FILTER);
        logger.log("select "+sortFilterName+" rating filter");
        driverTools.waitForElementToPresence(sortFilter);
        driverTools.click(sortFilter);
        if (driverTools.isElementDisplayed(sortFilter))
            driverTools.clickWithJavascriptExecutor(sortFilter);
        driverTools.switchToNative();
    }

    public boolean checkSortFilter() {
        selectSortFilter(SORT_RATING_LOW_TO_HIGH,"low to high");
        driverTools.switchToWebViewUrl("review");
        String firstReviewLowToHigh = driverTools.getText(SORT_FIRST_REVIEW).orElseThrow();
        logger.log("first review low to high rating: " + firstReviewLowToHigh);
        driverTools.switchToNative();

        selectSortFilter(SORT_RATING_HIGH_TO_LOW,"high to low");
        driverTools.switchToWebViewUrl("review");
        String firstReviewHighToLow = driverTools.getText(SORT_FIRST_REVIEW).orElseThrow();
        logger.log("first review high to low rating: " + firstReviewHighToLow);
        driverTools.switchToNative();

        Assertion.assertThat("first review high to low rating and low to high should be different: ", firstReviewHighToLow, is(not(firstReviewLowToHigh)));
        return !firstReviewHighToLow.equals(firstReviewLowToHigh);
    }

    public boolean checkSortedReviewCardLowToHigh() {
        logger.log("Check sorted review card low to high");
        selectSortFilter(SORT_RATING_LOW_TO_HIGH,"low to high");
        driverTools.switchToWebViewUrl("review");
        List<WebElement> allCardReviewItem =driverTools.elements(SORT_REVIEW_CARD_ALL).orElseThrow();
        logger.log("total review card: "+ allCardReviewItem.size());

        for (int i = 1; i < allCardReviewItem.size(); i++) {
            double rating = Double.parseDouble(driverTools.getText(getSortFilterCard(i)).orElseThrow());
            logger.log("rating "+ i +" : " + rating);
            double ratingNext = Double.parseDouble(driverTools.getText(getSortFilterCard(i+1)).orElseThrow());
            logger.log("rating "+ (i+1) +" : " + ratingNext);
            Assertion.assertThat("rating should be sorted low to high for "+ i +" and "+ (i+1) +" : current-> ("+rating+") next-> ("+ratingNext+")", rating, lessThanOrEqualTo(ratingNext));
        }
        driverTools.switchToNative();
        return true;
    }

    public boolean checkSortedReviewCardHighToLow() {
        logger.log("Check sorted review card high to low");
        selectSortFilter(SORT_RATING_HIGH_TO_LOW,"high to low");
        driverTools.switchToWebViewUrl("review");
        List<WebElement> allCardReviewItem =driverTools.elements(SORT_REVIEW_CARD_ALL).orElseThrow();
        logger.log("total review card: "+ allCardReviewItem.size());
        for (int i = 1; i < allCardReviewItem.size(); i++) {
            double rating = Double.parseDouble(driverTools.getText(getSortFilterCard(i)).orElseThrow());
            logger.log("rating "+ i +" : " + rating);
            double ratingNext = Double.parseDouble(driverTools.getText(getSortFilterCard(i+1)).orElseThrow());
            logger.log("rating "+ (i+1) +" : " + ratingNext);
            Assertion.assertThat("rating should be sorted high to low for "+ i +" and "+ (i+1) +" : current-> ("+rating+") next-> ("+ratingNext+")", rating, greaterThanOrEqualTo(ratingNext));
        }
        driverTools.switchToNative();
        return true;
    }

    private Locator getSortFilterCard(int index){
        return new Locator("SORT_REVIEW_CARD_" + index, AppiumBy.xpath("(//*[contains(@class,'ReviewCard_user_review')])["+index+"]"));
    }

    public List<String> getReviews(){
        driverTools.switchToWebView();
        driverTools.waitForElementToDisappear(CONTENT_LOADER);
        List<String> reviews = driverTools.getTexts(REVIEW_TEXT);
        driverTools.switchToNative();
        return reviews;
    }

    public void selectTopic(String topic){
        driverTools.switchToWebView();
        Locator chipLocator = new Locator(topic,AppiumBy.xpath("//*[contains(@class,'Chip')]//*[contains(text(),'"+topic+"')]"));
        driverTools.click(chipLocator);
        driverTools.switchToNative();
    }

    @SneakyThrows
    public String getReviewTopic(String accessToken,String vertical,String inventoryId,String lang){

        GetReviewApi.Inputs inputs = new GetReviewApi.Inputs(vertical,inventoryId,accessToken,lang,"");
        GetReviewApi reviewApi = new GetReviewApi(inputs);
        ApiResponse apiResponse = reviewApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());
        String jsonString = response.toString();
        String topics = CoreFunctions.getDataFromJsonString(jsonString,"data.summaryReview.topics");
        logger.log("review topics api response -> " + topics);
        String[] topicsArray = topics.split(",");
        String chosenTopic = CoreFunctions.getRandomObject(topicsArray).trim();
        logger.log("Chosen topic: "+chosenTopic);

        JSONObject data = response.getJSONObject("data");

        JSONArray topicsJsonArray = data.getJSONArray("topics");

        for (int i = 0; i < topicsJsonArray.length(); i++) {
            String topicCode = topicsJsonArray.getJSONObject(i).getString("code").trim();
            logger.log("Code -> "+topicCode);
            if (topicCode.equals(chosenTopic)) {
                String chosenTopicLabel = topicsJsonArray.getJSONObject(i).getString("label");
                logger.log("Chosen random topic -> " + chosenTopicLabel);
                return chosenTopicLabel+"-"+topicCode;
            }
        }
        Assertion.skip("topic not found");
        return "";
    }

    @SneakyThrows
    public List<String> getReviewFromTopic(String accessToken, String vertical, String inventoryId,String lang, String topic){
        GetReviewApi.Inputs inputs = new GetReviewApi.Inputs(vertical,inventoryId,accessToken,lang,topic);
        GetReviewApi reviewApi = new GetReviewApi(inputs);
        ApiResponse apiResponse = reviewApi.hitApi(apiTools);

        JSONObject response = new JSONObject(apiResponse.getBody());

        // Retrieve the 'data' JSONObject
        JSONObject data = response.getJSONObject("data");

        // Retrieve the 'userReviews' JSONObject
        JSONObject userReviews = data.getJSONObject("userReviews");

        // Retrieve the 'content' JSONArray
        JSONArray contentArray = userReviews.getJSONArray("content");

        List<String> reviews = new ArrayList<String>();

        // Iterate through the array and print each review
        for (int i = 0; i < contentArray.length(); i++) {
            JSONObject content = contentArray.getJSONObject(i);

            JSONArray comments = content.getJSONArray("comments");
            JSONObject commentObject = comments.getJSONObject(0);
            String reviewText = commentObject.getString("value");
            logger.log("Review: " + reviewText);
            reviews.add(reviewText);
            if(reviews.size() == 5){
                break;
            }
        }

        return reviews;
    }
}
