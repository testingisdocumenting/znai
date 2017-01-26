package com.twosigma.utils

import org.junit.Test

/**
 * @author mykola
 */
class StringUtilsTest {
    @Test
    void "should calculate max length on a line in multiline text"() {
        def maxLineLength = StringUtils.maxLineLength("""line #1
line #_2
line #_3\r""")

        assert maxLineLength == 8
    }
}
