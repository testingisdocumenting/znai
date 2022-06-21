/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.extensions.columns

import org.testingisdocumenting.znai.extensions.PluginParams
import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import java.nio.file.Paths

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class ColumnsFencePluginTest {
    @Test
    void "should index text from all columns"() {
        def plugin = new ColumnsFencePlugin()
        plugin.process(TEST_COMPONENTS_REGISTRY, Paths.get('test.md'), PluginParams.EMPTY,
                """left:text on the left
right:text on the right""")

        plugin.textForSearch().text.should == 'text on the left text on the right'
    }

    @Test
    void "render columns with links"() {
        def props = PluginsTestUtils.processFenceAndGetProps(new PluginParams("columns"),
        """left:
[left link](http://localhost:3030)

right:
[rightlink](http://localhost:3030) something something
""")
        props.should == [columns: [
                [content: [
                        [markup : "[left link](http://localhost:3030)\n", type: "TestMarkup"]
                ]],
                [content: [
                        [markup: "[rightlink](http://localhost:3030) something something", type: "TestMarkup"]]]],
                         config:[:]]
    }
}
