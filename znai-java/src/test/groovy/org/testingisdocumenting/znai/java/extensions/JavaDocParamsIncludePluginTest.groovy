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

package com.twosigma.znai.java.extensions

import com.twosigma.znai.extensions.include.IncludePlugin
import com.twosigma.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JavaDocParamsIncludePluginTest {
    @Test
    void "text for search with no return"() {
        def plugin = process('{entry: "sampleMethod(String)"}')
        plugin.textForSearch().text.should == 'test String test param <code>package.Param</code>'
    }

    @Test
    void "text for search with return"() {
        def plugin = process('{entry: "sampleMethod(String, List)"}')
        plugin.textForSearch().text.should == 'test String test param name List name of the param return List<String> list of samples'
    }

    private static IncludePlugin process(String params) {
        return PluginsTestUtils.processAndGetIncludePlugin(":include-java-doc-params: WithJavaDocs.java " + params)
    }

}
