package com.twosigma.testing.expectation.timer;

/**
 * @author mykola
 */
public interface ExpectationTimer {
    void start();
    void tick(long millis);
    boolean hasTimedOut(long millis);
}
