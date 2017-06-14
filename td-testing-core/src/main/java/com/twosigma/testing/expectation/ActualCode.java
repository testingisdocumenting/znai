package com.twosigma.testing.expectation;

/**
 * @author mykola
 */
public class ActualCode implements ActualCodeExpectations {
    private CodeBlock actual;

    public ActualCode(final CodeBlock actual) {
        this.actual = actual;
    }

    @Override
    public void should(final CodeMatcher codeMatcher) {
        boolean matches = codeMatcher.matches(actual);
        if (! matches) {
            throw new AssertionError("\n" + codeMatcher.mismatchedMessage(actual));
        }
    }
}
