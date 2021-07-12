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
        def props = resultingProps('example.py', '{entry: "Animal.says"}')

        def expectedProps = [
                parameters: [[name: 'message', type: 'string', anchorId: 'Animal_says_message',
                              description: [[markdown: 'message to say', type: 'TestMarkdown']]],
                             [name: 'volume', type: 'int (default 0)', anchorId: 'Animal_says_volume',
                              description: [[markdown: 'how loud it is', type: 'TestMarkdown']]]],
                entry: 'Animal.says']
        props.should == expectedProps

        // after second parsing no extra props should appear
        props = resultingProps('example.py', '{entry: "Animal.says"}')
        props.should == expectedProps
    }

    private static Map<String, Object> resultingProps(String fileName, String value) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-python-doc-params: $fileName $value")
    }
}
