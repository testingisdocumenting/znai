package com.twosigma.documentation.extensions.include

import com.twosigma.documentation.extensions.ReactComponent
import org.junit.Assert
import org.junit.Test

/**
 * @author mykola
 */
class TextFileIncludePluginTest {
    @Test
    void "should have a component name"() {
        def component = process("")
        assert component.name == 'FileTextContent'
    }

    @Test
    void "should extract file snippet based on contains and number of lines"() {
        def component = process("{startLine: 'multiple lines', numberOfLines: 2}")

        Assert.assertEquals("a multiple lines\n" +
                "line number 4", component.props.text)
    }

    @Test
    void "should extract file snippet based on contains only"() {
        def component = process("{startLine: 'multiple lines'}")

        Assert.assertEquals("a multiple lines\n" +
                "line number 4\n" +
                "and five", component.props.text)
    }

    private static ReactComponent process(String value) {
        def plugin = new TextFileIncludePlugin()
        def component = plugin.process(new TestResourceResolver(), new IncludeParams("test-file.txt $value"))

        return component
    }
}
