package com.tiket.web.page.accommodation.review;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class MyReviewPageMWeb<T extends WebDriver> extends MyReviewPageWeb<T> {

    public MyReviewPageMWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getPendingReviewCardTestId() {
        return "pending-review-card-mobile";
    }

    @Override
    protected String getRatingStarTestId() {
        return "rating-star-pending-review-card-mobile";
    }

    @Override
    protected void scrollToReviewChip() {
        driverTools.scrollIntoWebViewMWeb(REVIEW_CHIP);
    }

    @Override
    protected void scrollToReviewInput() {
        driverTools.scrollIntoWebViewMWeb(REVIEW_INPUT);
    }
}
