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

package org.testingisdocumenting.znai.extensions.cli

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class CliOutputIncludePluginTest {
    @Test
    void "should split file content into lines"() {
        def elements = process('captured-output.out')
        elements.should == ['lines': ['line one', 'line two', 'line three'], type: 'CliOutput']
    }

    @Test
    void "should error when no text to highlight found"() {
        code {
            process('captured-output.out {highlight: "line x"}')
        } should throwException("highlight text <line x> is not found\n" +
                "check: captured-output.out\n" +
                "line one\n" +
                "line two\n" +
                "line three")
    }

    private static def process(String params) {
        def result = PluginsTestUtils.processInclude(":include-cli-output: $params")
        return result[0].toMap()
    }
}
