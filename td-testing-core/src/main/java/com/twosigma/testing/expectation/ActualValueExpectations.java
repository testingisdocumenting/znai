package com.twosigma.testing.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;

/**
 * @author mykola
 */
public interface ActualValueExpectations {
    void should(ValueMatcher valueMatcher);
    void shouldNot(ValueMatcher valueMatcher);
    void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer);
}
