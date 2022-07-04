/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.python

import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

class PythonDocParamsIncludePluginTest {
    @Test
    void "should render python doc params as api params"() {
        def props = resultingProps('pydoc-params.py', '{entry: "my_func"}')

        def expectedProps = [
                parameters: [[name: 'label', type: [[text: 'String', refId: '']], anchorId: 'my_func_label',
                              description: [[markdown: 'label to use to *render* item in the store', type: 'TestMarkdown']]],
                             [name: 'price', type: [[text: 'Money', refId: '']], anchorId: 'my_func_price',
                              description: [[markdown: 'price associated with the **item**', type: 'TestMarkdown']]]],
                entry: 'my_func']
        props.should == expectedProps

        // after second parsing no extra props should appear
        props = resultingProps('pydoc-params.py', '{entry: "my_func"}')
        props.should == expectedProps
    }

    @Test
    void "should use types from type hints for doc params"() {
        def props = resultingProps('pydoc-params-type-hints.py', '{entry: "my_func"}')

        def expectedProps = [
                parameters: [[name: 'label', type: [[text: 'string', refId: '']], anchorId: 'my_func_label',
                              description: [[markdown: 'label to use to *render* item in the store', type: 'TestMarkdown']]],
                             [name: 'price', type: [[text: 'Money', refId: '']], anchorId: 'my_func_price',
                              description: [[markdown: 'price associated with the **item**', type: 'TestMarkdown']]]],
                entry: 'my_func']
        props.should == expectedProps
    }

    private static Map<String, Object> resultingProps(String fileName, String value) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-python-doc-params: $fileName $value")
    }
}
