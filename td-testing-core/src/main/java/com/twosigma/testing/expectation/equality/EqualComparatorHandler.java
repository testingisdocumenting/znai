package com.twosigma.testing.expectation.equality;

import com.twosigma.testing.expectation.ActualPath;

/**
 * @author mykola
 */
public interface EqualComparatorHandler {
    boolean handle(final Object actual, final Object expected);

    default boolean handleNulls() {
        return false;
    }

    void compare(final EqualComparator equalComparator, final ActualPath actualPath, final Object actual, final Object expected);
}
