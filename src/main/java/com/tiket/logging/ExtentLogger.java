package com.tiket.logging;

import com.aventstack.extentreports.ExtentTest;

public class ExtentLogger implements ILogger {

    private final ExtentTest test;
    private ExtentTest node;

    public ExtentLogger(ExtentTest test) {
        this.test = test;
        this.node = test.createNode("Start");
    }

    @Override
    public void log(String msg) {
        node.info(msg.replaceAll("\n", "<br>"));
    }

    @Override
    public void fail(String reason) {
        node.fail(reason.replaceAll("\n", "<br>"));
    }

    @Override
    public void skip(String detail) {
        node.skip(detail.replaceAll("\n", "<br>"));
    }

    @Override
    public void warn(String detail) {
        node.warning(detail.replaceAll("\n", "<br>"));
    }

    @Override
    public void softWarning(String detail) {
        log("<span style='color: darkorange'>" + detail + "</span>");
    }

    @Override
    public void step(String msg) {
        node = test.createNode("<h4 style=\"color: blue\">" + msg + "</h4>");
    }

    @Override
    public void assertion(String msg, String detail, boolean passed) {
        log("[Assertion passed: " + msg + "]");
        log(detail);
        ExtentTest assertionNode;
        if (passed) {
            assertionNode = this.node.createNode("<h4 style=\"color: green\";>[Assertion passed: " + msg + "]</h4>");
            assertionNode.info(detail);
        } else {
            assertionNode = this.node.createNode("<h4 style=\"color: red\";>[Assertion failed: " + msg + "]</h4>");
            assertionNode.fail(detail);
        }
    }

    @Override
    public void exception(Throwable throwable) {
        ExtentTest exceptionNode = node.createNode("<h4 style=\"color: red\";>Exception</h4>");
        exceptionNode.fail(throwable);
    }
}
