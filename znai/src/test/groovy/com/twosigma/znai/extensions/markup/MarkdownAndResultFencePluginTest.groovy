package com.twosigma.znai.extensions.markup

import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

class MarkdownAndResultFencePluginTest {
    def plugin = new MarkdownAndResultFencePlugin()

    @Test
    void "should create markdown code snippets and result as one doc element"() {
        def result = plugin.process(TestComponentsRegistry.INSTANCE, Paths.get("test.md"),
                new PluginParams(plugin.id(), ""),
                "hello *world*")

        def asMap = result.docElements.collect { it.toMap() }
        asMap.should == [[markdown: [lang: 'markdown', type: 'Snippet', snippet: 'hello *world*'],
                          result:[[markup: 'hello *world*', type: 'TestMarkup']],
                          type: 'MarkdownAndResult']]
    }
}
