package com.twosigma.testing.reporter;

/**
 * @author mykola
 */
public interface StepReporter {
    void onStepStart(TestStep step);
    void onStepSuccess(TestStep step);
    void onStepFailure(TestStep step);
}
