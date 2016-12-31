package com.twosigma.testing.expectation.equality.handlers;

import java.util.Iterator;

import com.twosigma.testing.data.render.DataRenderers;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;

/**
 * @author mykola
 */
public class IterableEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(final Object actual, final Object expected) {
        return actual instanceof Iterable && expected instanceof Iterable;
    }

    @Override
    public void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual, final Object expected) {
        Iterator actualIt = ((Iterable) actual).iterator();
        Iterator expectedIt  = ((Iterable) expected).iterator();

        int idx = 0;
        while (actualIt.hasNext() && expectedIt.hasNext()) {
            Object actualElement = actualIt.next();
            Object expectedElement = expectedIt.next();

            equalComparator.compare(actualPath.index(idx), actualElement, expectedElement);
            idx++;
        }

        while (actualIt.hasNext()) {
            Object actualElement = actualIt.next();
            equalComparator.reportMismatch(this, "extra element " + actualPath.index(idx).getPath() + ": " + DataRenderers.render(actualElement));
            idx++;
        }

        while (expectedIt.hasNext()) {
            Object expectedElement = expectedIt.next();
            equalComparator.reportMismatch(this, "missing element " + actualPath.index(idx).getPath() + ": " + DataRenderers.render(expectedElement));
            idx++;
        }
    }
}
