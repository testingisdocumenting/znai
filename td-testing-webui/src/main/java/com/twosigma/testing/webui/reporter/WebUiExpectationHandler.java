package com.twosigma.testing.webui.reporter;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.ExpectationHandler;

/**
 * @author mykola
 */
public class WebUiExpectationHandler implements ExpectationHandler {
    @Override
    public Flow onValueMismatch(ActualPath actualPath, Object actualValue, String message) {
        System.out.println(actualValue);
        return Flow.PassToNext;
    }
}
