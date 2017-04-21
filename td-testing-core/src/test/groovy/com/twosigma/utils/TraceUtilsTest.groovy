package com.twosigma.utils

import org.junit.Test

/**
 * @author mykola
 */
class TraceUtilsTest {
    @Test
    void "converts exception to string"() {
        def e = new RuntimeException("for test")
        def stackTrace = TraceUtils.stackTrace(e)

        assert stackTrace.contains("at com.twosigma.utils.TraceUtilsTest.converts exception to string")
    }
}
