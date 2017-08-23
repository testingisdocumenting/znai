package com.twosigma.testing.expectation;

import com.twosigma.testing.Ddjt;
import com.twosigma.testing.expectation.equality.EqualMatcher;

import java.util.function.Consumer;

/**
 * @author mykola
 */
public class ShouldAndWaitProperty<E> {
    private E actual;
    private Consumer<EqualMatcher> shouldHandler;

    public ShouldAndWaitProperty(E actual, Consumer<EqualMatcher> shouldHandler) {
        this.actual = actual;
        this.shouldHandler = shouldHandler;
    }

    public boolean equals(Object expected) {
        shouldHandler.accept(Ddjt.equal(expected));
        return true;
    }
}
