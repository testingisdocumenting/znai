package com.twosigma.testing.expectation.equality.handlers;

import com.twosigma.testing.data.live.LiveValue;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

/**
 * @author mykola
 */
public class LiveValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof LiveValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        LiveValue actualLiveValue = (LiveValue) actual;
        equalComparator.compare(actualPath, actualLiveValue.get(), expected);
    }
}
