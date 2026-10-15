package com.tiket.page.accommodation.hotel.reviewpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ReviewPageAndroid  <T extends WebDriver> extends ReviewPage<T> {

    public ReviewPageAndroid(Session<T> session) {
        super(session);
    }
}
