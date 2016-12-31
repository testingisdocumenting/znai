package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import static com.twosigma.utils.TraceUtils.renderValueAndType;

/**
 * @author mykola
 */
public class NullEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual == null || expected == null;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        if (actual == null && expected == null) {
            return;
        }

        if (actual == null) {
            equalComparator.reportMismatch(this, actualPath + "   actual: null\n" +
                actualPath + " expected: " + renderValueAndType(expected));
        } else {
            equalComparator.reportMismatch(this, actualPath + "   actual: " + renderValueAndType(actual) + "\n" +
                actualPath + " expected: null\n");
        }
    }
}
