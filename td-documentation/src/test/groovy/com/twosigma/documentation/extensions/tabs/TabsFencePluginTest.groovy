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
        def plugin = new TabsFencePlugin()
        def result = plugin.process(new TestComponentsRegistry(), Paths.get("test.md"), new PluginParams("tabs", ""),
                "java:test java markup\n" +
                "groovy:test groovy markup")

        def asMap = result.docElements.collect { it.toMap() }
        asMap.should == [[tabsContent: [[name: 'java', content: [[markup: 'test java markup', type: 'TestMarkup']]],
                                        [name: 'groovy', content: [[markup: 'test groovy markup', type: 'TestMarkup']]]],
                                                              type: 'Tabs']]
    }
}
