package com.twosigma.testing.expectation.contain.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.contain.ContainAnalyzer;
import com.twosigma.testing.expectation.contain.ContainHandler;

/**
 * @author mykola
 */
public class NullContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        containAnalyzer.reportMismatch(this, actualPath, actual + " doesn't contain " + expected);
    }
}
