/*
 * Copyright 2025 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.testingisdocumenting.znai.extensions.tabs

import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.TestMarkupParser
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class TabContentFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Test
    void "parses content and creates TabContent element with tabId"() {
        def elements = process("java", "test java markup")

        elements.should == [[tabId: 'java', content: [[markup: 'test java markup', type: 'TestMarkup']], type: 'TabContent']]
    }

    @Test
    void "tab id with spaces"() {
        def elements = process("my tab", "test markup")

        elements[0].tabId.should == 'my tab'
    }

    @Test
    void "strips quotes from tab id"() {
        def elements = process('"VS Code"', "test markup")

        elements[0].tabId.should == 'VS Code'
    }

    @Test
    void "requires tab id"() {
        code {
            process("", "content")
        } should throwException(~/tab-content requires a tab id/)
    }

    @Test
    void "indexes text inside content"() {
        def result = processAndGetPlugin("java", "test java markup")

        result.plugin.textForSearch().text.should == ['test java markup']
    }

    @Test
    void "collects auxiliary files from inner content"() {
        def plugin = processAndGetPlugin("java", "![dummy](dummy.png)", false).plugin

        def auxFiles = plugin.auxiliaryFiles(null).toList()
        auxFiles.path.fileName*.toString().should == ['dummy.png']
    }

    @Test
    void "image inside tab-content ends up in page level auxiliary files"() {
        def componentsRegistry = new TestComponentsRegistry()
        def parser = new MarkdownParser(componentsRegistry)
        componentsRegistry.defaultParser = parser

        def parseResult = parser.parse(Paths.get("test.md"),
                "shared text\n\n" +
                "```tab-content java\n" +
                "![dummy](dummy.png)\n" +
                "```\n")

        parseResult.auxiliaryFiles().path.fileName*.toString().should == ['dummy.png']
    }

    @Test
    void "image inside tab-content nested in attention-note ends up in page level auxiliary files"() {
        def componentsRegistry = new TestComponentsRegistry()
        def parser = new MarkdownParser(componentsRegistry)
        componentsRegistry.defaultParser = parser

        def parseResult = parser.parse(Paths.get("test.md"),
                "~~~~attention-note\n" +
                "```tab-content java\n" +
                "![dummy](dummy.png)\n" +
                "```\n" +
                "~~~~\n")

        parseResult.auxiliaryFiles().path.fileName*.toString().should == ['dummy.png']
    }

    private static List<Map> process(String tabId, String markup) {
        def result = processAndGetPlugin(tabId, markup)
        return result.result.docElements.collect { it.toMap() }
    }

    private static Map processAndGetPlugin(String tabId, String markup, boolean isFakeParser = true) {
        def componentsRegistry = new TestComponentsRegistry()
        componentsRegistry.defaultParser = isFakeParser ?
                new TestMarkupParser() :
                new MarkdownParser(componentsRegistry)

        def plugin = new TabContentFencePlugin()
        def result = plugin.process(componentsRegistry, Paths.get("test.md"),
                pluginParamsFactory.create(plugin.id(), tabId),
                markup)

        return [plugin: plugin, result: result]
    }
}
