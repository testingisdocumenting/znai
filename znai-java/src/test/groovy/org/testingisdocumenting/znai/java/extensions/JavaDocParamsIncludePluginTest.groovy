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

package org.testingisdocumenting.znai.java.extensions

import org.testingisdocumenting.znai.extensions.api.ApiLinkedText
import org.testingisdocumenting.znai.extensions.include.IncludePlugin
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JavaDocParamsIncludePluginTest {
    @Test
    void "text for search with no return"() {
        def plugin = processAndGetIncludePlugin('{entry: "sampleMethod(String)"}')
        plugin.textForSearch().text.should == 'test String test param <code>package.Param</code>'
    }

    @Test
    void "text for search with return"() {
        def plugin = processAndGetIncludePlugin('{entry: "sampleMethod(String, List)"}')
        plugin.textForSearch().text.should == 'test String test param name List name of the param return List<String> list of samples'
    }

    @Test
    void "function name and params should be used as api params anchor prefix"() {
        def props = processAndGetProps('{entry: "sampleMethod(String)"}')

        def emptyUrl = ApiLinkedText.EMPTY_STRING_SUPPLIER
        props.should == [parameters: [[name: 'test', type: [[text: 'String', url: emptyUrl]], anchorId: 'sampleMethod_test_String_test',
                                       description:[[type: 'Paragraph',
                                                     content: [[text: 'test param ', type: 'SimpleText'],
                                                               [code: 'package.Param', type: 'InlinedCode']]]]]],
                         entry: 'sampleMethod(String)']
    }

    private static IncludePlugin processAndGetIncludePlugin(String params) {
        return PluginsTestUtils.processAndGetIncludePlugin(":include-java-doc-params: WithJavaDocs.java " + params)
    }

    private static Map<String, Object> processAndGetProps(String params) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-java-doc-params: WithJavaDocs.java " + params)
    }
}
