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

package org.testingisdocumenting.znai.groovy.extensions

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class GroovyIncludePluginTest {
    @Test
    void "validates highlight"() {
        code {
            process("sample.groovy", "{highlight: 24}")
        } should throwException(IllegalArgumentException)
    }

    @Test
    void "excludes lines by regexp"() {
        def snippet = process("sample.groovy", "{excludeRegexp: 'println'}")
        snippet.should == 'def "hello world"() {\n' +
                '}'
    }

    @Test
    void "provides auto title"() {
        processAndGetProps("sample.groovy", "{autoTitle: true}").title.should == "sample.groovy"
    }

    private static Map<String, Object> processAndGetProps(String fileName, String params) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-groovy: $fileName $params")
    }

    private static String process(String fileName, String value) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-groovy: $fileName $value")
    }
}
