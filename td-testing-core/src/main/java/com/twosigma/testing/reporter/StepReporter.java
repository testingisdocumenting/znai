package com.twosigma.testing.reporter;

/**
 * @author mykola
 */
public interface StepReporter {
    void onStart(TestStep step);
    void onSuccess(TestStep step);
    void onFailure(TestStep step);
}
