package com.twosigma.testing.expectation;

/**
 * @author mykola
 */
public interface ExpectationHandler {
    enum Flow {
        Terminate,
        PassToNext
    }

    Flow onValueMismatch(ActualPath actualPath, Object actualValue, String message);
//    Flow onCodeMismatch(ActualPath actualPath, CodeBlock codeBlock, String message);
}
