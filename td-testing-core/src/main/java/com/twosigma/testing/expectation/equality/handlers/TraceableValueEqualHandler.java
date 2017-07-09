package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.data.traceable.CheckLevel;
import com.twosigma.testing.data.traceable.TraceableValue;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.ComparatorResult;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

/**
 * @author mykola
 */
public class TraceableValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(final Object actual, final Object expected) {
        return actual instanceof TraceableValue;
    }

    @Override
    public void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual,
        final Object expected) {

        TraceableValue traceableValue = (TraceableValue) actual;
        ComparatorResult result = equalComparator.compare(actualPath, traceableValue.getValue(), expected);

        if (result.isMismatch()) {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.FuzzyFailed:
                CheckLevel.ExplicitFailed);
        } else {
            traceableValue.updateCheckLevel(equalComparator.isNegative() ?
                CheckLevel.FuzzyPassed:
                CheckLevel.ExplicitPassed);
        }
    }
}
