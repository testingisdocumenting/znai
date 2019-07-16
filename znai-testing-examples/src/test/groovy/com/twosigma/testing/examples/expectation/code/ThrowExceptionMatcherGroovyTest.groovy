package com.twosigma.testing.examples.expectation.code

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

import static com.twosigma.webtau.Ddjt.*

class ThrowExceptionMatcherGroovyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none()

    @Test
    void examples() {
        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, "negative not allowed")

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException, ~/negative.*not allowed/)

        code {
            businessLogic(-10)
        } should throwException(IllegalArgumentException)
    }

    private static businessLogic(num) {
        if (num < 0) {
            throw new IllegalArgumentException("negative not allowed")
        }
    }

}
