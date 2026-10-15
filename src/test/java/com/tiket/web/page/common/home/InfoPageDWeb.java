package com.tiket.web.page.common.home;

import com.tiket.model.Locator;
import com.tiket.model.Session;
import com.tiket.testbase.Assertion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import static org.hamcrest.CoreMatchers.*;

public class InfoPageDWeb<T extends WebDriver> extends InfoPageWeb<T>{
    public InfoPageDWeb(Session<T> session) {
        super(session);
    }
    private static final String STAGING_DWEB_URL = "https://gatotkaca.tiket.com/";
    private static final String PREPROD_DWEB_URL = "https://preprod.tiket.com/";
    private static final String PROD_DWEB_URL = "https://tiket.com/";

    @Override
    public void clickBackButton() {}// No need for implementation on DWeb
}
