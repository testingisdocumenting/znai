package com.twosigma.testing.webtau.expectation;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;
import com.twosigma.testing.webtau.page.ElementValue;

import static com.twosigma.testing.Ddjt.createActualPath;

/**
 * @author mykola
 */
public class ElementValueEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof ElementValue;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        ElementValue actualElementValue = (ElementValue) actual;
        Object actualValue = actualElementValue.get();

        equalComparator.compare(createActualPath(actualElementValue.getName()), actualValue, expected);
    }
}
