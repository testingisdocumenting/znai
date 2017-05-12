package com.twosigma.testing.webui.expectation;

import com.twosigma.testing.expectation.timer.ExpectationTimer;
import com.twosigma.testing.expectation.timer.ExpectationTimerConfig;
import com.twosigma.testing.webui.cfg.WebUiTestConfig;

/**
 * @author mykola
 */
public class SystemTimerConfig implements ExpectationTimerConfig {
    private static WebUiTestConfig cfg = WebUiTestConfig.INSTANCE;

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
