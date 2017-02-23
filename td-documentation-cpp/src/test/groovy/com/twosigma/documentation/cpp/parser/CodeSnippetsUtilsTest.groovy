package com.twosigma.documentation.cpp.parser

import com.twosigma.documentation.cpp.parser.CodeSnippetsUtils
import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class CodeSnippetsUtilsTest {
    @Test
    void "strip common indentation"() {
        def code = "    int a = 2;\n    int b = 3;"
        def stripped = CodeSnippetsUtils.stripIndentation(code)
        Assert.assertEquals("int a = 2;\nint b = 3;", stripped)
    }
}
