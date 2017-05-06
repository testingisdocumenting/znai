package com.twosigma.testing.expectation.timer;

/**
 * @author mykola
 */
public interface ExpectationTimerConfig {
    ExpectationTimer createExpectationTimer();
    long defaultTimeoutMillis();
    long defaultTickMillis();
}
