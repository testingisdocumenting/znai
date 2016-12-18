package com.twosigma.testing.expectation.code;

import com.twosigma.testing.expectation.CodeBlock;
import com.twosigma.testing.expectation.CodeMatcher;

/**
 * @author mykola
 */
public class ThrowExceptionMatcher implements CodeMatcher {
    private String expectedMessage;


    public static ThrowExceptionMatcher throwException(final String expectedMessage) {
        return new ThrowExceptionMatcher(expectedMessage);
    }

    private ThrowExceptionMatcher(final String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    @Override
    public String matchingMessage() {
        return "TODO";
    }

    @Override
    public String matchedMessage(final CodeBlock codeBlock) {
        return null;
    }

    @Override
    public String mismatchedMessage(final CodeBlock codeBlock) {
        return null;
    }

    @Override
    public boolean matches(final CodeBlock codeBlock) {
//
//        try {
//            codeBlock.execute();
//        }

        return false;
    }
}
