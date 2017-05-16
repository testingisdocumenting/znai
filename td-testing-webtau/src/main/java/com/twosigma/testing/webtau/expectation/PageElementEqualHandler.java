package com.twosigma.testing.webtau.expectation;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.equality.EqualComparator;
import com.twosigma.testing.expectation.equality.EqualComparatorHandler;
import com.twosigma.testing.webtau.page.ElementValue;
import com.twosigma.testing.webtau.page.PageElement;

/**
 * @author mykola
 */
public class PageElementEqualHandler implements EqualComparatorHandler {
    @Override
    public boolean handle(Object actual, Object expected) {
        return actual instanceof PageElement;
    }

    @Override
    public void compare(EqualComparator equalComparator, ActualPath actualPath, Object actual, Object expected) {
        PageElement actualPageElement = (PageElement) actual;
        ElementValue elementValue = actualPageElement.elementValue();
        Object actualValue = elementValue.get();

        equalComparator.compare(ActualPath.createActualPath(elementValue.getName()), actualValue, expected);
    }
}
