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

package org.testingisdocumenting.znai.extensions.twosides

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class EmptyBlockIncludePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Test
    void "converts rightSide to meta-rightSide"() {
        def plugin = new EmptyBlockIncludePlugin()
        def result = plugin.process(null, null, null, pluginParamsFactory.create(plugin.id(), "",
                [rightSide: true]))

        result.docElements*.toMap().should == [[meta: [rightSide: true], type: 'EmptyBlock']]
    }
}
