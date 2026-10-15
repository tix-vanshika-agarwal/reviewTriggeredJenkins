package com.costumeMatcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsSubstringOfMatcher extends TypeSafeMatcher<String>{
    private final String fullText;

    private IsSubstringOfMatcher(final String fullText){
        this.fullText = fullText;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("is substring of \"" + this.fullText + "\"");
    }

    @Override
    protected boolean matchesSafely(String actualString) {
        return this.fullText.contains(actualString);
    }

     @Factory
    public static Matcher<String> isSubsStringOf(final String fullText) {
        return new IsSubstringOfMatcher(fullText);
    }
    
}
