package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.data.render.DataRenderers;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

import static com.twosigma.utils.TraceUtils.renderValueAndType;

/**
 * @author mykola
 */
public class AnyEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return true;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        boolean areEqual = actual.equals(expected);
        if (areEqual)
            return;

        equalComparator.reportMismatch(this, mismatchMessage(actualPath, actual, expected));
    }

    private String mismatchMessage(ActualPath actualPath, Object actual, Object expected) {
        return actualPath + "   actual: " + renderValueAndType(actual) + "\n" +
               actualPath + " expected: " + renderValueAndType(expected);
    }
}
