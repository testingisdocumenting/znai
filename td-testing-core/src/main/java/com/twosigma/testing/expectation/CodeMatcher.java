package com.twosigma.testing.expectation;

/**
 * @author mykola
 */
public interface CodeMatcher {
    /**
     * @return about to start matching message
     */
    String matchingMessage();

    /**
     * @param codeBlock matching code block
     * @return match message
     */
    String matchedMessage(CodeBlock codeBlock);

    /**
     * @param codeBlock matching code block
     * @return mismatch message
     */
    String mismatchedMessage(CodeBlock codeBlock);

    boolean matches(CodeBlock codeBlock);
}
