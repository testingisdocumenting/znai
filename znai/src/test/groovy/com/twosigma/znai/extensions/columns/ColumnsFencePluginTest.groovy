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

package com.twosigma.znai.extensions.columns

import com.twosigma.znai.extensions.PluginParams
import com.twosigma.znai.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

class ColumnsFencePluginTest {
    @Test
    void "should index text from all columns"() {
        def plugin = new ColumnsFencePlugin()
        plugin.process(TestComponentsRegistry.INSTANCE, Paths.get('test.md'), PluginParams.EMPTY,
                """left:text on the left
right:text on the right""")

        plugin.textForSearch().text.should == 'text on the left text on the right'
    }
}
