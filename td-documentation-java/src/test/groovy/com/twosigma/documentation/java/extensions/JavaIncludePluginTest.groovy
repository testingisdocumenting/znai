package com.twosigma.documentation.java.extensions

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

/**
 * @author mykola
 */
class JavaIncludePluginTest {
    @Test
    void "includes multiple entries of java method signatures"() {
        def result = process("Simple.java", "{entries: ['methodA', 'methodB'], signatureOnly: true}")
        result.should == "void methodA()\n" +
                "void methodB(String p)"
    }

    private static String process(String fileName, String params) {
        def result = PluginsTestUtils.process(":include-java: $fileName $params")
        return result.docElements.get(0).getProps().tokens[0].content
    }
}
