package com.twosigma.testing.reporter;

/**
 * @author mykola
 */
public interface StepReporter<C> {
    void onStepStart(TestStep<C> step);
    void onStepSuccess(TestStep<C> step);
    void onStepFailure(TestStep<C> step);
}
