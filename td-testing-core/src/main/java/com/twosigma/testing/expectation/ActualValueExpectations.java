package com.twosigma.testing.expectation;

/**
 * @author mykola
 */
public interface ActualValueExpectations {
    void should(ValueMatcher valueMatcher);
    void shouldNot(ValueMatcher valueMatcher);
}
