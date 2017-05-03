package com.twosigma.testing.expectation

import com.twosigma.testing.data.DummyLiveValue
import com.twosigma.testing.expectation.timer.DummyExpectationTimer
import org.junit.Test

import static com.twosigma.testing.expectation.Matchers.equal


/**
 * @author mykola
 */
class ActualValueTest {
    def liveValue = new DummyLiveValue([1, 10, 100, 1000])

    @Test(expected = AssertionError)
    void "waitTo fails when timer times out"() {
        def expectationTimer = new DummyExpectationTimer(3)
        ActualValue.actual(liveValue).waitTo(equal(1000), expectationTimer)
    }
}
