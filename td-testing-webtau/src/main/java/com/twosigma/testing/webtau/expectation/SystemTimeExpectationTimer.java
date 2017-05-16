package com.twosigma.testing.webtau.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;

/**
 * @author mykola
 */
public class SystemTimeExpectationTimer implements ExpectationTimer {
    private long startTime;

    @Override
    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void tick(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasTimedOut(long millis) {
        return (System.currentTimeMillis() - startTime) > millis;
    }
}
