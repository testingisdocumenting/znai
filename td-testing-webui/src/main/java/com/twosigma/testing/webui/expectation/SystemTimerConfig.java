package com.twosigma.testing.webui.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.expectation.timer.ExpectationTimerConfig;

/**
 * @author mykola
 */
public class SystemTimerConfig implements ExpectationTimerConfig {
    @Override
    public ExpectationTimer createExpectationTimer() {
        return new SystemTimeExpectationTimer();
    }

    @Override
    public long defaultTimeoutMillis() {
        return 5000;
    }

    @Override
    public long defaultTickMillis() {
        return 100;
    }
}
