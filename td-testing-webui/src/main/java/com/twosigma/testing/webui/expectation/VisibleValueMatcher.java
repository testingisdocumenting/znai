package com.twosigma.testing.webui.expectation;

import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.ValueMatcher;
import com.twosigma.testing.webui.page.PageElement;

/**
 * @author mykola
 */
public class VisibleValueMatcher implements ValueMatcher {
    @Override
    public String matchingMessage() {
        return "to be visible";
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        PageElement pageElement = (PageElement) actual;
        return pageElement.isVisible();
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be hidden";
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return "is hidden";
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "is visible";
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        return ! matches(actualPath, actual);
    }
}
