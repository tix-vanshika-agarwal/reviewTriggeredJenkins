package com.tiket.core;

import com.tiket.model.Exception.ForbiddenException;
import com.tiket.model.Session;
import com.tiket.testbase.BaseTest;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

public class ExceptionAnalyser {

    private static final Logger logger = LogManager.getLogger(ExceptionAnalyser.class);

    @SneakyThrows
    public static Exception analyse(Exception e, Class<? extends Exception>[] forbidden) {
        if(Arrays.asList(forbidden).contains(e.getClass())) {
            Session<WebDriver> session = BaseTest.sessionThreadLocal.get();
            // Skip quit for NoSuchSessionException - session is already gone; pool will replace session on put/take
            if (session != null && !(e instanceof NoSuchSessionException)) {
                try {
                    session.driver().quit();
                } catch (Exception exception) {
                    logger.error("Failed to quit driver");
                    logger.error(exception);
                }
            } else if (session == null) {
                logger.warn("Session is null, cannot quit driver");
            }
            throw new ForbiddenException(e);
        } else {
            return e;
        }
    }
}
