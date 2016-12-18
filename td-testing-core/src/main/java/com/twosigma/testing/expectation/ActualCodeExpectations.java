package com.twosigma.testing.expectation;

/**
 * @author mykola
 */
public interface ActualCodeExpectations {
    void should(CodeMatcher codeMatcher);
}
