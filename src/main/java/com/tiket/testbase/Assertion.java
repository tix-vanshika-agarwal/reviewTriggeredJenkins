package com.tiket.testbase;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Assert;
import org.testng.SkipException;

public class Assertion {

    public static <T> void assertThat(String reason, T actual, Matcher<T> matcher) {

        Description description = new StringDescription();
        description.appendText("\nExpected: ");
        description.appendDescriptionOf(matcher);
        description.appendText("\nActual  : ");
        description.appendValue(actual);
        description.appendText("\n");

        String detail = description.toString().replaceAll("<", "[").replaceAll(">", "]");

        boolean passed = matcher.matches(actual);
        BaseTest.mainLogger.get().assertion(reason, detail, passed);

        Assert.assertThat(reason, actual, matcher);
    }

    public static void fail(String reason) {
        BaseTest.mainLogger.get().fail(reason);
        Assert.fail(reason);
    }

    public static void skip(String detail) {
        BaseTest.mainLogger.get().skip(detail);
        throw new SkipException(detail);
    }
}
