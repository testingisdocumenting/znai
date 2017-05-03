package com.twosigma.testing.expectation.timer;

/**
 * @author mykola
 */
public interface ExpectationTimer {
    void tick();
    boolean hasTimedOut();
}
