package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Assert
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class TextFileIncludePluginTest {
    @Test
    void "should extract file snippet based on regex and number of lines"() {
        def text = process("{startLine: 'multiple lines', numberOfLines: 2}")

        Assert.assertEquals("a multiple lines\n" +
                "line number 4", text)
    }

    @Test
    void "should extract file snippet based on start and stop regex"() {
        def text = process("{startLine: 'multiple lines', numberOfLines: 2}")

        Assert.assertEquals("a multiple lines\n" +
                "line number 4", text)
    }

    @Test
    void "should extract file snippet based on regex only"() {
        def text = process("{startLine: 'multiple lines'}")

        Assert.assertEquals("a multiple lines\n" +
                "line number 4\n" +
                "and five", text)
    }

    private static String process(String value) {
        def plugin = new TextFileIncludePlugin()
        def result = plugin.process(new TestComponentsRegistry(), Paths.get(""), new IncludeParams("test-file.txt $value"))

        return result.docElements.get(0).getProps().componentProps.tokens[0].data
    }
}
