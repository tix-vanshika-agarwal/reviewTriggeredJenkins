package com.tiket.web.dWeb.page.accommodation.homes;

import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

/**
 * dWeb implementation of {@link HomesSeoPage}.
 *
 * <p>Currently inherits the full implementation from {@link HomesSeoPage}.
 * Override methods here when dWeb behaviour needs to differ from the shared
 * base.</p>
 */
public class HomesSeoPageDWeb<T extends WebDriver> extends HomesSeoPage<T> {

    public HomesSeoPageDWeb(Session<T> session) {
        super(session);
    }
}
