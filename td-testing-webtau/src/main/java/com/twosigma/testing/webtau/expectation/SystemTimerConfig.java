package com.twosigma.testing.webtau.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.expectation.timer.ExpectationTimerConfig;
import com.twosigma.testing.webtau.cfg.WebTauConfig;

/**
 * @author mykola
 */
public class SystemTimerConfig implements ExpectationTimerConfig {
    private static WebTauConfig cfg = WebTauConfig.INSTANCE;

    @Override
    public ExpectationTimer createExpectationTimer() {
        return new SystemTimeExpectationTimer();
    }

    @Override
    public long defaultTimeoutMillis() {
        return cfg.waitTimeout();
    }

    @Override
    public long defaultTickMillis() {
        return 100;
    }
}
