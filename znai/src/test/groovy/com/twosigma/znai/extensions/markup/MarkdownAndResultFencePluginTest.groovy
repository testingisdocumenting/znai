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
