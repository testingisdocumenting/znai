/*
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

package com.twosigma.znai.extensions.tabs

import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.extensions.fence.FencePlugin
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

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

    @Test
    void "indexes text inside each tab"() {
        FencePlugin plugin = processAndGetPluginWithResult("java:test java markup\n" +
                "groovy:test groovy markup").plugin

        plugin.textForSearch().text.should == 'java test java markup groovy test groovy markup'
    }

    @Test
    void "handles rightSide shortcut converting it to meta"() {
        def plugin = new TabsFencePlugin()
        def result = plugin.process(TestComponentsRegistry.INSTANCE,
                Paths.get("test.md"),
                new PluginParams(plugin.id(), "{rightSide: true}"),
                "")

        result.docElements*.toMap().should == [[meta: [rightSide: true], tabsContent: [], type: 'Tabs']]
    }

    private static List<Map> process(String markup) {
        def pluginWithResult = processAndGetPluginWithResult(markup)
        return pluginWithResult.result.docElements.collect { it.toMap() }
    }

    private static Map processAndGetPluginWithResult(String markup) {
        def plugin = new TabsFencePlugin()
        def result = plugin.process(TestComponentsRegistry.INSTANCE, Paths.get("test.md"), new PluginParams(plugin.id(), ""),
                markup)

        return [plugin: plugin, result: result]
    }
}
