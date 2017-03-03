package com.twosigma.utils

import org.junit.Assert
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

    @Test
    void "strip common indentation"() {
        def code = "    int a = 2;\n    int b = 3;"
        def stripped = StringUtils.stripIndentation(code)
        Assert.assertEquals("int a = 2;\nint b = 3;", stripped)
    }
}
