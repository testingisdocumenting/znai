package com.twosigma.testing.expectation

import com.twosigma.testing.Ddjt

/**
 * @author mykola
 */
class ShouldNot {
    private Object actual

    ShouldNot(Object actual) {
        this.actual = actual
    }

    boolean equals(Object expected) {
        new ActualValue(actual).shouldNot(Ddjt.equal(expected))
        return true
    }
}
