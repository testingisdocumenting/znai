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

package org.testingisdocumenting.znai.extensions.cli

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static com.twosigma.webtau.Matchers.code
import static com.twosigma.webtau.Matchers.throwException

class CliOutputIncludePluginTest {
    @Test
    void "should split file content into lines"() {
        def elements = process('captured-output.out')
        elements.should == ['lines': ['line one', 'line two', 'line three'], highlight: [], type: 'CliOutput']
    }

    @Test
    void "should convert text to indexes with full match"() {
        def elements = process('captured-output.out {highlight: "line two"}')
        elements.highlight.should == [1]
    }

    @Test
    void "should convert text to indexes with partial match"() {
        def elements = process('captured-output.out {highlight: "two"}')
        elements.highlight.should == [1]
    }

    @Test
    void "should error when no text to highlight found"() {
        code {
            process('captured-output.out {highlight: "line x"}')
        } should throwException("can't find line: line x")
    }

    @Test
    void "should read lines to highlight from file"() {
        def elements = process('captured-output.out {highlightFile: "captured-matched-lines.txt"}')
        elements.highlight.should == [1, 2]
    }

    @Test
    void "should error when lines to highlight from file are not present"() {
        code {
            process('captured-output.out {highlightFile: "captured-matched-lines-error.txt"}')
        } should throwException("can't find line: wrong line two")
    }

    private static def process(String params) {
        def result = PluginsTestUtils.process(":include-cli-output: $params")
        return result[0].toMap()
    }
}
