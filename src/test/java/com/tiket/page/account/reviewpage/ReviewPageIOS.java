package com.tiket.page.account.reviewpage;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

public class ReviewPageIOS <T extends WebDriver> extends ReviewPage<T> {
    public ReviewPageIOS(Session<T> session) {
        super(session);
    }
}
