/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.utils

import org.junit.Test

import java.text.NumberFormat

class StringUtilsTest {
    @Test
    void "convert null as empty"() {
        StringUtils.nullAsEmpty(null).should == ""
        StringUtils.nullAsEmpty(20).should == "20"
        StringUtils.nullAsEmpty("hello").should == "hello"
    }

    @Test
    void "should calculate max length on a line in multiline text"() {
        def maxLineLength = StringUtils.maxLineLength("""line #1
line #_2
line #_3\r""")

        maxLineLength.should == 8
    }

    @Test
    void "strip common indentation"() {
        def code = "    int a = 2;\n    int b = 3;"
        def stripped = StringUtils.stripIndentation(code)
        stripped.should == "int a = 2;\nint b = 3;"
    }

    @Test
    void "extracts inside curly braces"() {
        def code = "{\n    statement1;\n    statement2}"
        def stripped = StringUtils.extractInsideCurlyBraces(code)
        stripped.should == "\n    statement1;\n    statement2"

        StringUtils.extractInsideCurlyBraces("").should == ""
    }

    @Test
    void "removes content inside brackets and brackets"() {
        StringUtils.removeContentInsideBracketsInclusive("hello <world>").should == "hello "
    }

    @Test
    void "concat prefix and multiline text preserving prefix size indentation"() {
        def concatenated = StringUtils.concatWithIndentation("a prefix:", "line1 line1\nline2\nline #3")

        concatenated.should == "a prefix:line1 line1\n" +
                "         line2\n" +
                "         line #3"
    }

    @Test
    void "remove quotes"() {
        StringUtils.removeQuotes('""').should == ""
        StringUtils.removeQuotes("''").should == ""
        StringUtils.removeQuotes("'hello world'").should == "hello world"
        StringUtils.removeQuotes('"hello world"').should == "hello world"
        StringUtils.removeQuotes('"hello\\" world\\\'s"').should == "hello\" world's"
    }

    @Test
    void "wrap in double quotes"() {
        StringUtils.wrapInDoubleQuotes('""').should == '""'
        StringUtils.wrapInDoubleQuotes("hello world").should == '"hello world"'
    }

    @Test
    void "is number"() {
        def format = NumberFormat.getInstance()
        StringUtils.isNumeric(format, "100").should == true
        StringUtils.isNumeric(format, "100.34").should == true
        StringUtils.isNumeric(format, "100a34").should == false
    }
}
