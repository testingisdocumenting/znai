package com.twosigma.testing.expectation.equality;

/**
 * @author mykola
 */
public class ComparatorResult {
    private boolean isMismatch;

    public ComparatorResult(boolean isMismatch) {
        this.isMismatch = isMismatch;
    }

    public boolean isMismatch() {
        return isMismatch;
    }
}
