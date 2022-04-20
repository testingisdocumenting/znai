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

    @Test
    void "extract body and signature by entry"() {
        def snippet = process("UnitTestExample.groovy", "{entry: 'example of api a'}")

        snippet.should == "void \"example of api a\"() {\n" +
                "    apiA.doAction()\n" +
                "}"
    }

    @Test
    void "extract body only by entry"() {
        def snippet = process("UnitTestExample.groovy", "{entry: 'example of api a', bodyOnly: true}")
        snippet.should == "apiA.doAction()"
    }

    @Test
    void "extract multiple bodies by entries"() {
        def snippet = process("UnitTestExample.groovy", "{entry: ['example of api a', 'example of api b'], bodyOnly: true}")
        snippet.should == "apiA.doAction()\n\n" +
                "apiB.doAction()"
    }

    @Test
    void "extract multiple bodies by entries with empty separator"() {
        def snippet = process("UnitTestExample.groovy", "{entry: ['example of api a', 'example of api b'], entrySeparator: '', bodyOnly: true}")
        snippet.should == "apiA.doAction()\n\n" +
                "apiB.doAction()"
    }

    private static Map<String, Object> processAndGetProps(String fileName, String params) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-groovy: $fileName $params")
    }

    private static String process(String fileName, String value) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-groovy: $fileName $value")
    }
}
