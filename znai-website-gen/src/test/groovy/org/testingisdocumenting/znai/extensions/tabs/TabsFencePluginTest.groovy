/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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
import org.testingisdocumenting.znai.extensions.fence.FencePlugin
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.TestMarkupParser
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class TabsFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Test
    void "include markup per tab"() {
        def elements = process("java:test java markup\n" +
                "groovy:test groovy markup")

        elements.should == [[tabsContent: [[name: 'java', content: [[markup: 'test java markup', type: 'TestMarkup']]],
                                           [name: 'groovy', content: [[markup: 'test groovy markup', type: 'TestMarkup']]]],
                             type: 'Tabs']]
    }

    @Test
    void "support tab names with special sybols"() {
        def elements = process("java@8:test java markup\n" +
                "java@9:test groovy markup")

        elements[0].tabsContent.name.should == ['java@8', 'java@9']
    }

    @Test
    void "support tab names with spaces when quoted"() {
        def elements = process("\"python lang\":test java markup\n" +
                "\"java lang\":test groovy markup")

        elements[0].tabsContent.name.should == ['python lang', 'java lang']
    }

    @Test
    void "indexes text inside each tab"() {
        FencePlugin plugin = processAndGetPluginWithResult("java:test java markup\n" +
                "groovy:test groovy markup").plugin

        plugin.textForSearch().text.should == 'java test java markup groovy test groovy markup'
    }

    @Test
    void "collects auxiliary files from each tab"() {
        FencePlugin plugin = processAndGetPluginWithResult(
                "java:\n:include-file: a.java\n" +
                "groovy:\n:include-file:b.groovy\n", false).plugin


        def auxFiles = plugin.auxiliaryFiles(null).toList()
        auxFiles.path.fileName*.toString().should == ['a.java', 'b.groovy']
    }

    @Test
    void "handles rightSide shortcut converting it to meta"() {
        def plugin = new TabsFencePlugin()
        def result = plugin.process(TEST_COMPONENTS_REGISTRY,
                Paths.get("test.md"),
                pluginParamsFactory.create(plugin.id(), "{rightSide: true}"),
                "java: content for java")

        result.docElements*.toMap().should == [[meta: [rightSide: true], tabsContent: [
                [name: 'java', content: [[markup: ' content for java', type: 'TestMarkup']]]
        ], type: 'Tabs']]
    }

    @Test
    void "validates that at least one tab is present"() {
        code {
            processAndGetPluginWithResult("", false)
        } should throwException("no tabs are defined. if your tab names have spaces quote the tab name")
    }

    private static List<Map> processWithFakeMarkupParser(String markup) {
        def pluginWithResult = processAndGetPluginWithResult(markup)

        return pluginWithResult.result.docElements.collect { it.toMap() }
    }

    private static List<Map> process(String markup) {
        def pluginWithResult = processAndGetPluginWithResult(markup)

        return pluginWithResult.result.docElements.collect { it.toMap() }
    }

    private static Map processAndGetPluginWithResult(String markup, boolean isFakeParser = true) {
        def componentsRegistry = new TestComponentsRegistry()

        def markupParser = isFakeParser ?
                new TestMarkupParser() :
                new MarkdownParser(componentsRegistry)

        componentsRegistry.defaultParser = markupParser

        def plugin = new TabsFencePlugin()
        def result = plugin.process(componentsRegistry, Paths.get("test.md"), pluginParamsFactory.create(plugin.id(), ""),
                markup)

        return [plugin: plugin, result: result]
    }
}
