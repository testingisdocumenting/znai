package com.twosigma.testing.expectation.contain;

import com.twosigma.testing.expectation.ActualPath;

/**
 * @author mykola
 */
public interface ContainHandler {
    boolean handle(Object actual, Object expected);

    void analyze(ContainAnalyzer containAnalyzer, ActualPath actualPath, Object actual, Object expected);
}
