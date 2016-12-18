package com.twosigma.testing.expectation.equality;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.ValueMatcher;

/**
 * @author mykola
 */
public class EqualMatcher implements ValueMatcher {
    private EqualComparator equalComparator;
    private Object expected;

    private EqualMatcher(Object expected) {
        this.expected = expected;
    }

    public static EqualMatcher equal(Object expected) {
        return new EqualMatcher(expected);
    }

    @Override
    public String matchingMessage() {
        return "to equal";
    }

    @Override
    public String matchedMessage(final ActualPath actualPath, final Object actual) {
        return "equals";
    }

    @Override
    public String mismatchedMessage(final ActualPath actualPath, final Object actual) {
        return equalComparator.generateMismatchReport();
    }

    @Override
    public boolean matches(final ActualPath actualPath, final Object actual) {
        equalComparator = EqualComparator.comparator();
        return compare(actualPath, actual);
    }

    @Override
    public String negativeMatchingMessage() {
        return "to not equal";
    }

    @Override
    public String negativeMatchedMessage(final ActualPath actualPath, final Object actual) {
        return "doesn't equal";
    }

    @Override
    public String negativeMismatchedMessage(final ActualPath actualPath, final Object actual) {
        return "equals";
    }

    @Override
    public boolean negativeMatches(final ActualPath actualPath, final Object actual) {
        equalComparator = EqualComparator.negativeComparator();
        return compare(actualPath, actual);
    }

    private boolean compare(final ActualPath actualPath, final Object actual) {
        equalComparator.compare(actualPath, actual, expected);
        return equalComparator.areEqual();
    }
}
