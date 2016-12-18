package com.twosigma.testing.expectation;

import com.twosigma.testing.expectation.code.ThrowExceptionMatcher;

/**
 * Convenient class for a single static * imports
 * @author mykola
 */
public class Matchers {
    private Matchers() {
    }

    public static ThrowExceptionMatcher throwException(final String expectedMessage) {
        return ThrowExceptionMatcher.throwException(expectedMessage);
    }

    public static ActualValueExpectations value(Object actual) {
        return ActualValue.value(actual);
    }

}
