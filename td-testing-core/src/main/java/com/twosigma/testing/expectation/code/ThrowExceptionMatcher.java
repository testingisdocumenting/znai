package com.twosigma.testing.expectation.code;

import com.twosigma.testing.expectation.CodeBlock;
import com.twosigma.testing.expectation.CodeMatcher;
import com.twosigma.testing.expectation.equality.EqualComparator;

import java.util.regex.Pattern;

import static com.twosigma.testing.expectation.ActualPath.createActualPath;

/**
 * @author mykola
 */
public class ThrowExceptionMatcher implements CodeMatcher {
    private String expectedMessage;
    private Pattern expectedMessageRegexp;
    private Class expectedClass;
    private String thrownMessage;
    private Class thrownClass;
    private EqualComparator comparator;

    public ThrowExceptionMatcher(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    public ThrowExceptionMatcher(Pattern expectedMessageRegexp) {
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class expectedClass) {
        this.expectedClass = expectedClass;
    }

    public ThrowExceptionMatcher(Class expectedClass, Pattern expectedMessageRegexp) {
        this.expectedClass = expectedClass;
        this.expectedMessageRegexp = expectedMessageRegexp;
    }

    public ThrowExceptionMatcher(Class expectedClass, String expectedMessage) {
        this.expectedClass = expectedClass;
        this.expectedMessage = expectedMessage;
    }

    @Override
    public String matchingMessage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String matchedMessage(final CodeBlock codeBlock) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String mismatchedMessage(final CodeBlock codeBlock) {
        return comparator.generateMismatchReport();
    }

    @Override
    public boolean matches(final CodeBlock codeBlock) {
        try {
            codeBlock.execute();
        } catch (Throwable t) {
            thrownMessage = t.getMessage();
            thrownClass = t.getClass();
        }

        comparator = EqualComparator.comparator();

        if (expectedMessage != null) {
            comparator.compare(createActualPath("expected exception message"), thrownMessage, expectedMessage);
        }

        if (expectedMessageRegexp != null) {
            comparator.compare(createActualPath("expected exception message"), thrownMessage, expectedMessageRegexp);
        }

        if (expectedClass != null) {
            comparator.compare(createActualPath("expected exception class"), thrownClass, expectedClass);
        }

        return comparator.areEqual();
    }
}
