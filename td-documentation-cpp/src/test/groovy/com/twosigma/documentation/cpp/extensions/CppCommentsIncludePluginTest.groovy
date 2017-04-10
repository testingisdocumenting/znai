package com.twosigma.documentation.cpp.extensions

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author mykola
 */
class CppCommentsIncludePluginTest {
    @Test
    void "should extract comments with @mdoc at the beginning from specified entry"() {
        def actualMarkup = process("code-with-docs.cpp", "class_name::func2")
        def expectedMarkup = "func2 special comment because it was marked as documentation related\n" +
                "\n" +
                "func2 comment that is across\n" +
                "multiple lines"

        assertEquals(expectedMarkup, actualMarkup)
    }

    private static def process(String fileName, String entry) {
        def result = PluginsTestUtils.process(":include-cpp-comments: $fileName {entry: \"$entry\"}")
        return result.docElements[0].getProp("markup")
    }

}
