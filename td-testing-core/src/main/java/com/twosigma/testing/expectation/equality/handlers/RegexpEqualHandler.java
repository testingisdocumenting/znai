package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import java.util.regex.Pattern;

/**
 * @author mykola
 */
public class RegexpEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof String && expected instanceof Pattern;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        Pattern expectedPattern = (Pattern) expected;

        boolean areEqual = expectedPattern.matcher(actual.toString()).find();
        if (areEqual) {
            return;
        }

        equalComparator.reportMismatch(this, mismatchMessage(actualPath, actual, expected));
    }

    private String mismatchMessage(ActualPath actualPath, Object actual, Object expected) {
        return actualPath + "    actual string: " + actual.toString() + "\n" +
               actualPath + " expected pattern: " + expected.toString();
    }
}
