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

package org.testingisdocumenting.znai.extensions.markup

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory

import java.nio.file.Paths

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class MarkdownAndResultFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    def plugin = new MarkdownAndResultFencePlugin()

    @Test
    void "should create markdown code snippets and result as one doc element"() {
        def result = plugin.process(TEST_COMPONENTS_REGISTRY, Paths.get("test.md"),
                pluginParamsFactory.create(plugin.id(), ""),
                "hello *world*")

        def asMap = result.docElements.collect { it.toMap() }
        asMap.should == [[markdown: [lang: 'markdown', type: 'Snippet', snippet: 'hello *world*'],
                          result:[[markup: 'hello *world*', type: 'TestMarkup']],
                          type: 'MarkdownAndResult']]
    }
}
