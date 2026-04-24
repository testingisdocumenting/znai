/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.javascript

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.testingisdocumenting.znai.search.SearchScore
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class JavascriptFunctionIncludePluginTest {
    @Test
    void "should pass function name and args to JavascriptFunction doc element"() {
        def elements = process('themeBox {title: "hello", size: 50}')
        elements.should == [[type: 'JavascriptFunction',
                             functionName: 'themeBox',
                             args: [title: 'hello', size: 50]]]
    }

    @Test
    void "should fail when function name is missing"() {
        code {
            process('')
        } should throwException(~/javascript function name must be provided as a free param/)
    }

    @Test
    void "should contribute function name and args values to search text"() {
        def plugin = PluginsTestUtils.processAndGetIncludePlugin(
                ':include-javascript-function: themeBox {title: "hello world", count: 42, tags: ["alpha", "beta"]}')

        def searchText = plugin.textForSearch()
        searchText.size().should == 1
        searchText[0].score.should == SearchScore.STANDARD
        searchText[0].text.should == 'themeBox hello world 42 alpha beta'
    }

    private static List<Map<String, ?>> process(params) {
        return PluginsTestUtils.processInclude(":include-javascript-function: $params")*.toMap()
    }
}
