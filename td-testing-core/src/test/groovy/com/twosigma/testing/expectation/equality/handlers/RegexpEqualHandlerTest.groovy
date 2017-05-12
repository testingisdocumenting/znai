package com.twosigma.testing.expectation.equality.handlers

import org.junit.Test

import static com.twosigma.testing.expectation.ActualValue.actual
import static com.twosigma.testing.expectation.Matchers.equal

/**
 * @author mykola
 */
class RegexpEqualHandlerTest {
    @Test
    void "handles instances of String value as actual and Pattern as expected"() {
        def handler = new RegexpEqualHandler()

        assert handler.handle("test", ~/regexp/)

        assert ! handler.handle("test", "another text")
        assert ! handler.handle(100, ~/regexp/)
    }

    @Test
    void "uses regexp to compare values"() {
        actual("hello world").should(equal(~/wor.d/))
    }

    @Test(expected = AssertionError)
    void "shows regexp when comparison fails"() {
        actual("hello world").should(equal(~/wor1d/))
    }
}
