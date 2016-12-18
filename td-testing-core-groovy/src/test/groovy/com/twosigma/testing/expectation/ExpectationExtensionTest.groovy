package com.twosigma.testing.expectation

import org.junit.Ignore
import org.junit.Test

/**
 * @author mykola
 */
class ExpectationExtensionTest {
    @Test
    void "provides should equal shortcut"() {
        def value = 12
        value.should == 12
    }
}
