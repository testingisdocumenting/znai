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
    void "include plugin per tab"() {
        def plugin = new TabsFencePlugin()
        def result = plugin.process(new TestComponentsRegistry(), Paths.get("test.md"), new PluginParams("tabs", ""),
                "java:include-dummy: ff1 {p1: 'v1'}\n" +
                "groovy:include-dummy:xz ff2 {p2: 'v2'}")

        result.docElements.collect { it.toMap() }.should == [[tabsContent: [[name: 'java', content: [[ff: 'ff1', opts: [p1: 'v1'], type: 'IncludeDummy']]],
                                                                            [name: 'groovy', content: [[ff: 'xz ff2', opts: [p2: 'v2'], type: 'IncludeDummy']]]],
                                                              type: 'Tabs']]
    }
}
