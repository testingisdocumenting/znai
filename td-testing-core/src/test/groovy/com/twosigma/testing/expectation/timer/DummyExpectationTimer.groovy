package com.twosigma.testing.expectation.timer

/**
 * @author mykola
 */
class DummyExpectationTimer implements ExpectationTimer {
    int currentTick
    int maxNumberOfTicks

    DummyExpectationTimer(int maxNumberOfTicks) {
        this.maxNumberOfTicks = maxNumberOfTicks
    }

    @Override
    void tick() {
        currentTick++
    }

    @Override
    boolean hasTimedOut() {
        return currentTick >= maxNumberOfTicks
    }
}
