package com.twosigma.testing.webtau.page;

import com.twosigma.testing.Ddjt;
import com.twosigma.testing.expectation.equality.EqualMatcher;

import java.util.function.BiConsumer;

/**
 * @author mykola
 */
class ShouldAndWaitProperty<E> {
    private E actual;
    private BiConsumer<E, EqualMatcher> shouldHandler;

    public ShouldAndWaitProperty(E actual, BiConsumer<E, EqualMatcher> shouldHandler) {
        this.actual = actual;
        this.shouldHandler = shouldHandler;
    }

    public boolean equals(Object expected) {
        shouldHandler.accept(actual, Ddjt.equal(expected));
        return true;
    }
}
