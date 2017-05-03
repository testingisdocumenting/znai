package com.twosigma.testing.expectation;

import com.twosigma.testing.expectation.code.ThrowExceptionMatcher;
import com.twosigma.testing.expectation.equality.EqualMatcher;

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

    public static ActualValueExpectations actual(Object actual) {
        return ActualValue.actual(actual);
    }

    public static EqualMatcher equal(Object expected) {
        return EqualMatcher.equal(expected);
    }
}
