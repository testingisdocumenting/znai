package com.twosigma.documentation.extensions.tabs

import com.twosigma.documentation.extensions.PluginParams
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class TabsFencePluginTest {
    @Test
    void "include markup per tab"() {
        def elements = process("java:test java markup\n" +
                "groovy:test groovy markup")

        elements.should == [[tabsContent: [[name: 'java', content: [[markup: 'test java markup', type: 'TestMarkup']]],
                                           [name: 'groovy', content: [[markup: 'test groovy markup', type: 'TestMarkup']]]],
                             type: 'Tabs']]
    }

    @Test
    void "support tab names with special names"() {
        def elements = process("java@8:test java markup\n" +
                "java@9:test groovy markup")

        elements[0].tabsContent.name.should == ['java@8', 'java@9']
    }

    private static List<Map> process(String markup) {
        def plugin = new TabsFencePlugin()
        def result = plugin.process(new TestComponentsRegistry(), Paths.get("test.md"), new PluginParams(plugin.id(), ""),
                markup)

        return result.docElements.collect { it.toMap() }
    }
}
