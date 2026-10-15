package com.tiket.web.page.accommodation.review;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.page.loyalty.my_review.MyReviewPage;
import com.tiket.testbase.Assertion;
import com.tiket.testbase.Configs;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.tiket.core.CoreFunctions.generateRandomString;
import static org.hamcrest.Matchers.containsString;

public abstract class MyReviewPageWeb<T extends WebDriver> extends MyReviewPage<T> {

    private static final Locator MY_REVIEW_PAGE_TITLE = new Locator("MY_REVIEW_PAGE_TITLE",
            By.xpath("//h1[contains(text(),'Your Review') or contains(text(),'Review Kamu')]"));
    private static final Locator WAITING_FOR_REVIEW_TAB = new Locator("WAITING_FOR_REVIEW_TAB",
            By.xpath("//button[contains(@class,'Tabs_tab')][.//*[contains(text(),'Waiting for Your Review') or contains(text(),'Menunggu Review Darimu') or contains(text(),'Menunggu Review')]]"));
    protected static final Locator SUBMIT_REVIEW_BUTTON = new Locator("SUBMIT_REVIEW_BUTTON",
            By.cssSelector("[data-testid='button-submission'] button"));
    private static final Locator THANKS_FOR_REVIEW_TITLE = new Locator("THANKS_FOR_REVIEW_TITLE",
            By.xpath("//*[contains(@class,'Hypergraphics_illustration_title') and (contains(.,'Thanks for your time') or contains(.,'Makasih udah sediain waktu isi review') or contains(.,'Terima kasih'))]"));
    private static final Locator SEE_ALL_REVIEWS_BUTTON = new Locator("SEE_ALL_REVIEWS_BUTTON",
            By.xpath("//div[contains(@class,'Hypergraphics_review_button')]//button[.//span[contains(.,'See All Reviews') or contains(.,'Lihat Semua Review-mu') or contains(.,'Lihat Semua Review')]]"));
    private static final Locator COACHMARK_DONE_BUTTON = new Locator("COACHMARK_DONE_BUTTON",
            By.xpath("//button[contains(@class,'coachmark_done_btn')]"));
    private static final Locator TRAVEL_TYPE_CHIP = new Locator("TRAVEL_TYPE_CHIP",
            By.cssSelector("[data-testid^='form-review-traveller-type_of_traveler-']"));
    private static final Locator REVIEW_AUTOTEXT_CHIP_BUTTON = new Locator("REVIEW_AUTOTEXT_CHIP_BUTTON",
            By.cssSelector("button[data-testid^='form-review-autotext-'], [data-testid^='form-review-autotext-']"));
    private static final int MIN_REVIEW_LENGTH = 40;
    private static final Locator COACHMARK_SHADE = new Locator("COACHMARK_SHADE",
            By.xpath("//*[contains(@class,'Coachmark_coachmark_focus_shade')]"));

    public MyReviewPageWeb(Session<T> session) {
        super(session);
    }

    public static <T extends WebDriver> MyReviewPageWeb<T> getInstance(Session<T> session) {
        return switch (Configs.PLATFORM) {
            case DWEB -> new MyReviewPageDWeb<>(session);
            case AMWEB, IMWEB -> new MyReviewPageMWeb<>(session);
            default -> throw new NotImplementedException("Not implemented for platform: " + Configs.PLATFORM);
        };
    }

    protected abstract String getPendingReviewCardTestId();

    protected abstract String getRatingStarTestId();

    private static final String ACTIVE_TAB_PANEL_XPATH = "//*[contains(@class,'Tabs_tab_panel_active')]";

    private void logPageSource(String step) {
        logger.log("[MyReviewPageWeb][" + step + "] URL: " + driverTools.getCurrentURL());
        driverTools.getPageSource();
    }

    protected Locator getPendingReviewCardLocator(String product) {
        return new Locator("PENDING_REVIEW_CARD_" + product, By.xpath(
                ACTIVE_TAB_PANEL_XPATH + "//*[@data-testid='" + getPendingReviewCardTestId()
                        + "'][.//*[contains(text(),'" + product + "')]]"));
    }

    private Locator getVisiblePendingReviewPanelLocator() {
        return new Locator("VISIBLE_PENDING_TAB_PANEL", By.xpath(
                ACTIVE_TAB_PANEL_XPATH + "//*[@data-testid='" + getPendingReviewCardTestId() + "']"));
    }

    private By getRatingStarBy() {
        return By.cssSelector("[data-testid='" + getRatingStarTestId() + "'], img[class*='RatingStars_rating_star']");
    }

    private void openWaitingForReviewTab() {
        driverTools.waitForElementToAppear(MY_REVIEW_PAGE_TITLE, 60).orElseThrow();
        WebElement tab = driverTools.waitForElementToAppear(WAITING_FOR_REVIEW_TAB, 30).orElseThrow();
        driverTools.clickWithJavascriptExecutor(tab);
        driverTools.waitForElementToAppear(getVisiblePendingReviewPanelLocator(), 30).orElseThrow();
        logPageSource("openWaitingForReviewTab");
    }

    private void clickPendingReviewStar(String product, int starIndex) {
        Locator pendingCard = getPendingReviewCardLocator(product);
        WebElement card = driverTools.waitForElementToAppear(pendingCard, 60).orElseThrow();
        driverTools.scrollIntoWebView(card);

        List<WebElement> stars = driverTools.waitForChildElementsCountAtLeast(
                pendingCard, getRatingStarBy(), starIndex, 30);

        if (stars.size() < starIndex) {
            logPageSource("clickPendingReviewStar-noStars-" + product + "-found:" + stars.size());
            throw new NoSuchElementException("Expected at least " + starIndex + " rating stars on pending card for: " + product);
        }

        WebElement starElement = stars.get(starIndex - 1);
        driverTools.scrollIntoWebView(starElement);
        try {
            driverTools.clickElementByCoordinateDWeb(starElement);
        } catch (Exception e) {
            driverTools.clickWithJavascriptExecutor(starElement);
        }
        logPageSource("clickPendingReviewStar-" + product + "-star" + starIndex);
    }

    @Override
    public void tellTheStory(String product) {
        openWaitingForReviewTab();
        clickPendingReviewStar(product, 5);
        driverTools.waitForElementToPresence(TRAVEL_TYPE, 60).orElseThrow();
        logPageSource("tellTheStory-" + product);
    }

    private void dismissCoachmarkIfPresent() {
        for (int attempt = 0; attempt < 3; attempt++) {
            Optional<WebElement> coachmarkButton = driverTools.waitForElementToAppear(COACHMARK_DONE_BUTTON, 5);
            if (coachmarkButton.isEmpty()) {
                break;
            }
            driverTools.clickWithJavascriptExecutor(coachmarkButton.get());
            driverTools.dummyWait(1);
        }
    }

    private void waitForCoachmarkToDisappear() {
        dismissCoachmarkIfPresent();
        if (driverTools.waitForElementToDisappear(COACHMARK_SHADE, 15)) {
            return;
        }
        dismissCoachmarkIfPresent();
        driverTools.waitForElementToDisappear(COACHMARK_SHADE, 5);
    }

    private void selectReviewAutotextChips() {
        waitForCoachmarkToDisappear();
        List<WebElement> chips = driverTools.elements(REVIEW_AUTOTEXT_CHIP_BUTTON).orElse(List.of());
        if (!chips.isEmpty()) {
            scrollToReviewChip();
            int clicked = 0;
            for (WebElement chip : chips) {
                driverTools.scrollIntoWebView(chip);
                driverTools.clickWithJavascriptExecutor(chip);
                clicked++;
                driverTools.dummyWait(1);
                if (getReviewInputLength() >= MIN_REVIEW_LENGTH) {
                    break;
                }
            }
            logPageSource("selectReviewAutotextChips-clicked:" + clicked + "-length:" + getReviewInputLength());
            if (getReviewInputLength() >= MIN_REVIEW_LENGTH) {
                return;
            }
        }
        scrollToReviewInput();
        driverTools.waitForElementToAppear(REVIEW_INPUT, 10);
        driverTools.sendKeys(REVIEW_INPUT, generateRandomString(80));
    }

    private int getReviewInputLength() {
        return driverTools.element(REVIEW_INPUT)
                .map(input -> {
                    String value = input.getAttribute("value");
                    return value == null ? 0 : value.length();
                })
                .orElse(0);
    }

    private void submitReviewAndWaitForThanks() {
        waitForCoachmarkToDisappear();
        driverTools.scrollIntoWebView(SUBMIT_REVIEW_BUTTON);
        WebElement submitButton = driverTools.waitForElementToPresence(SUBMIT_REVIEW_BUTTON, 10).orElseThrow();
        driverTools.clickWithJavascriptExecutor(submitButton);
        if (driverTools.waitForElementToAppear(THANKS_FOR_REVIEW_TITLE, 60).isPresent()) {
            return;
        }
        if (!driverTools.getCurrentURL().contains("/form")) {
            return;
        }
        logPageSource("submitReviewAndWaitForThanks-timeout");
        throw new NoSuchElementException("Review thank-you page did not appear after submit");
    }

    @Override
    public void fillReviewItem() {
        dismissCoachmarkIfPresent();
        logPageSource("fillReviewItem-afterCoachmark");
        selectFirstAvailableTravelType();
        driverTools.dummyWait(1);
        dismissCoachmarkIfPresent();
        selectReviewAutotextChips();
        submitReviewAndWaitForThanks();
        logPageSource("fillReviewItem");
    }

    private void selectFirstAvailableTravelType() {
        driverTools.waitForElementToPresence(TRAVEL_TYPE_CHIP, 10);
        List<WebElement> travelTypes = driverTools.elements(TRAVEL_TYPE_CHIP).orElse(List.of());
        if (travelTypes.isEmpty()) {
            logPageSource("selectFirstAvailableTravelType-none");
            return;
        }
        WebElement firstType = travelTypes.get(0);
        driverTools.scrollIntoWebView(firstType);
        driverTools.clickWithJavascriptExecutor(firstType);
        logPageSource("selectFirstAvailableTravelType-clicked:" + firstType.getAttribute("data-testid"));
    }

    protected void scrollToReviewChip() {
        driverTools.scrollIntoWebView(REVIEW_CHIP);
    }

    protected void scrollToReviewInput() {
        driverTools.scrollIntoWebView(REVIEW_INPUT);
    }

    @Override
    public Boolean verifyAndClickReviewPopup() {
        boolean isThankYouAppeared = driverTools.waitForElementToAppear(THANKS_FOR_REVIEW_TITLE, 30).isPresent();
        driverTools.waitForElementToAppear(SEE_ALL_REVIEWS_BUTTON, 30)
                .ifPresent(driverTools::clickWithJavascriptExecutor);
        logPageSource("verifyAndClickReviewPopup");
        return isThankYouAppeared;
    }

    @Override
    public void isOnMyReviewForm() {
        logPageSource("isOnMyReviewForm-beforeAssert");
        String currentUrl = driverTools.getCurrentURL();
        Assertion.assertThat("User should be on review page", currentUrl, containsString("/myreview"));
    }

    @Override
    public void clickReviewStar(String product) {
        openWaitingForReviewTab();
        driverTools.scrollIntoWebView(getPendingReviewCardLocator(product));
        clickPendingReviewStar(product, 5);
        logPageSource("clickReviewStar-" + product);
    }

    @Override
    public void swipeToCard(String product) {
        Locator pendingCard = getPendingReviewCardLocator(product);
        driverTools.scrollIntoWebView(pendingCard);
        driverTools.waitForElementToAppear(pendingCard, 30).orElseThrow();
        logPageSource("swipeToCard-" + product);
    }
}
