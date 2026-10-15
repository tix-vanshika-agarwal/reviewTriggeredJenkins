package com.tiket.testbase;

import com.tiket.appium.DriverTools;
import com.tiket.io.api.ApiTools;
import com.tiket.logging.ILogger;
import com.tiket.model.Session;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class BasePage<T extends WebDriver> {

    protected T driver;
    protected Session<T> session;
    protected DriverTools<T> driverTools;
    protected ILogger logger = BaseTest.mainLogger.get();
    protected ApiTools apiTools;

    public BasePage(Session<T> session) {
        Objects.requireNonNull(session);
        this.session = session;
        this.driver = session.driver();
        this.driverTools = new DriverTools<>(driver);
        this.apiTools = new ApiTools(logger);
    }
}
