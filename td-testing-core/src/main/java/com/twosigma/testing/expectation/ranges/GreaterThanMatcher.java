package com.twosigma.testing.expectation.ranges;

import com.twosigma.testing.data.converters.ToNumberConverters;
import com.twosigma.testing.data.render.DataRenderers;
import com.twosigma.testing.expectation.ActualPath;
import com.twosigma.testing.expectation.ValueMatcher;

/**
 * @author mykola
 */
public class GreaterThanMatcher implements ValueMatcher {
    private Comparable base;
    private int compareToRes;

    public GreaterThanMatcher(Comparable base) {
        this.base = base;
    }

    @Override
    public String matchingMessage() {
        return "to be greater than " + DataRenderers.render(base);
    }

    @Override
    public String matchedMessage(ActualPath actualPath, Object actual) {
        return "greater than " + DataRenderers.render(base) + renderActual(actual);
    }

    @Override
    public String mismatchedMessage(ActualPath actualPath, Object actual) {
        return equalsOrLessThenMessage(actual);
    }

    @Override
    public boolean matches(ActualPath actualPath, Object actual) {
        compareToRes = compareTo((Comparable) ToNumberConverters.convert(actual));
        return compareToRes < 0;
    }

    @Override
    public String negativeMatchingMessage() {
        return "to be less than or equal to " + DataRenderers.render(base);
    }

    @Override
    public String negativeMatchedMessage(ActualPath actualPath, Object actual) {
        return equalsOrLessThenMessage(actual);
    }

    @Override
    public String negativeMismatchedMessage(ActualPath actualPath, Object actual) {
        return "greater than " + DataRenderers.render(base) + renderActual(actual);
    }

    @Override
    public boolean negativeMatches(ActualPath actualPath, Object actual) {
        compareToRes = compareTo((Comparable) ToNumberConverters.convert(actual));
        return compareToRes >= 0;
    }

    @SuppressWarnings("unchecked")
    private int compareTo(Comparable actual) {
        return base.compareTo(actual);
    }

    private String equalsOrLessThenMessage(Object actual) {
        return (compareToRes == 0 ? "equals ": "less than ") +
                DataRenderers.render(base) + (compareToRes == 0 ? "" : renderActual(actual));
    }

    private String renderActual(Object actual) {
        return " (actual equals " + DataRenderers.render(actual) + ")";
    }
}
