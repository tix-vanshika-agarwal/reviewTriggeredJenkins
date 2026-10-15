package com.tiket.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;

public class Log4JLogger implements ILogger {

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Log4JLogger.class);

    public void log(String msg) {
        String callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.info(callingClass + "-" + lineNumber + " ==>> " + msg);
    }

    public void fail(String reason) {
        String callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.fatal(callingClass + "-" + lineNumber + " ==>> " + reason);
    }

    public void skip(String detail) {
        String callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.error(callingClass + "-" + lineNumber + " ==>> " + detail);
    }

    public void warn(String detail) {
        String callingClass = Thread.currentThread().getStackTrace()[2].getClassName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        logger.warn(callingClass + "-" + lineNumber + " ==>> " + detail);
    }

    @Override
    public void softWarning(String detail) {
        warn(detail);
    }

    public void step(String msg) {
        log("***** Test Step: " + msg + " *****");
    }

    @Override
    public void assertion(String msg, String detail, boolean passed) {
        step(msg);
        if (passed) {
            log(detail);
        } else {
            fail(detail);
        }
    }

    @Override
    public void exception(Throwable throwable) {
        assertion(throwable.getMessage(), ExceptionUtils.getStackTrace(throwable), false);
    }
}
