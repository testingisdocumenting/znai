package com.twosigma.testing.data.traceable;

/**
 * @author mykola
 */
public enum CheckLevel {
    None,
    FuzzyPassed,
    ExplicitPassed,
    FuzzyFailed,
    ExplicitFailed;

    public boolean isFailed() {
        return this != None && this != FuzzyPassed && this != ExplicitPassed;
    }

    public boolean isPassed() {
        return this != None && !isFailed();
    }
}
