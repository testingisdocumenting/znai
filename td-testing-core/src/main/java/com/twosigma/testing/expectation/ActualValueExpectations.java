package com.twosigma.testing.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.expectation.timer.ExpectationTimerConfigProvider;

/**
 * @author mykola
 */
public interface ActualValueExpectations {
    void should(ValueMatcher valueMatcher);
    void shouldNot(ValueMatcher valueMatcher);

    void waitTo(ValueMatcher valueMatcher, ExpectationTimer expectationTimer, long tickMillis, long timeOutMillis);

    default void waitTo(ValueMatcher valueMatcher) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                ExpectationTimerConfigProvider.defaultTimeoutMillis());
    }

    default void waitTo(ValueMatcher valueMatcher, long tickMillis, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                tickMillis,
                timeOutMillis);
    }

    default void waitTo(ValueMatcher valueMatcher, long timeOutMillis) {
        waitTo(valueMatcher, ExpectationTimerConfigProvider.createExpectationTimer(),
                ExpectationTimerConfigProvider.defaultTickMillis(),
                timeOutMillis);
    }
}
