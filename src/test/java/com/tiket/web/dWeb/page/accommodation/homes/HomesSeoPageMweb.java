package com.tiket.web.dWeb.page.accommodation.homes;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * mWeb implementation of {@link HomesSeoPage}.
 *
 * <p>Currently inherits the full implementation from {@link HomesSeoPage}.
 * Override methods here when mWeb behaviour needs to differ from the shared
 * base.</p>
 */
public class HomesSeoPageMweb<T extends WebDriver> extends HomesSeoPage<T> {

    public HomesSeoPageMweb(Session<T> session) {
        super(session);
    }
}
