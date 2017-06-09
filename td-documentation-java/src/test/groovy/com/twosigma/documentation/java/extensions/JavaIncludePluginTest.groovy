package com.twosigma.documentation.java.extensions

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

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

    @Test
    void "includes overloaded entry of java method"() {
        process("Simple.java", "{entry: 'methodB', signatureOnly: true}").should == "void methodB(String p)"
        process("Simple.java", "{entry: 'methodB(String, Boolean)', signatureOnly: true}").should == "void methodB(String p, Boolean b)"

        code {
            process("Simple.java", "{entry: 'methodB(String2)', signatureOnly: true}")
        } should throwException("no method found: methodB(String2)")
    }

    private static String process(String fileName, String params) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-java: $fileName $params")
    }
}
