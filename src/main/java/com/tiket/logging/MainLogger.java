package com.tiket.logging;

public record MainLogger(ExtentLogger extentLogger, Log4JLogger log4JLogger) implements ILogger {

    @Override
    public void log(String msg) {
        log4JLogger.log(msg);
        extentLogger.log(msg);
    }

    @Override
    public void fail(String reason) {
        log4JLogger.fail(reason);
        extentLogger.fail(reason);
    }

    @Override
    public void skip(String detail) {
        log4JLogger.skip(detail);
        extentLogger.skip(detail);
    }

    @Override
    public void warn(String detail) {
        log4JLogger.warn(detail);
        extentLogger.warn(detail);
    }

    @Override
    public void softWarning(String detail) {
        log4JLogger.warn(detail);
        extentLogger.softWarning(detail);
    }

    @Override
    public void step(String msg) {
        log4JLogger.step(msg);
        extentLogger.step(msg);
    }

    @Override
    public void assertion(String msg, String detail, boolean passed) {
        log4JLogger.assertion(msg, detail, passed);
        extentLogger.assertion(msg, detail, passed);
    }

    @Override
    public void exception(Throwable throwable) {
        log4JLogger.exception(throwable);
        extentLogger.exception(throwable);
    }
}
