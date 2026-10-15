package com.tiket.logging;

public interface ILogger {
    void log(String msg);

    void fail(String reason);

    void skip(String detail);

    void warn(String detail);

    void softWarning(String detail);

    void step(String msg);

    void assertion(String msg, String detail, boolean passed);

    void exception(Throwable throwable);
}
