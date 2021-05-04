/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.typescript

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class TypeScriptIncludePluginTest {
    @Test
    void "should extract jsx declarations"() {
        def elements = process('Sample.tsx {jsxElementsFrom: "demo"}')

        elements.should == [[type: 'JsxGroup', declarations: [
                [tagName: 'Declaration', attributes: [[name: 'firstName', value: '"placeholder"'],
                                                      [name: 'lastName', value: '{this.lastName}']]]]]]
    }

    private static def process(String params) {
        def result = PluginsTestUtils.processInclude(":include-typescript: $params")
        return result*.toMap()
    }
}
