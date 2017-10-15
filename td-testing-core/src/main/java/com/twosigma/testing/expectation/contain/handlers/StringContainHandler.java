package com.twosigma.testing.expectation.contain.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.contain.ContainAnalyzer;
import com.twosigma.testing.expectation.contain.ContainHandler;

/**
 * @author mykola
 */
public class StringContainHandler implements ContainHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof String && expected instanceof String;
    }

    @Override
    public void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected) {
        String actualString = (String) actual;
        String expectedString = (String) expected;

        if (!actualString.contains(expectedString)) {
            containAnalyzer.reportMismatch(this, actualPath,
                    "             actual: " + actualString + "\n" +
                            "expected to contain: " + expectedString);
        }
    }
}
