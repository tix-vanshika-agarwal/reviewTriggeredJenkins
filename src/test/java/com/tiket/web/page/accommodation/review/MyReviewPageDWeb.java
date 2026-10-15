package com.tiket.web.page.accommodation.review;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class MyReviewPageDWeb<T extends WebDriver> extends MyReviewPageWeb<T> {

    public MyReviewPageDWeb(Session<T> session) {
        super(session);
    }

    @Override
    protected String getPendingReviewCardTestId() {
        return "pending-review-card-desktop";
    }

    @Override
    protected String getRatingStarTestId() {
        return "rating-star-pending-review-card-desktop";
    }
}
