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

package org.testingisdocumenting.znai.extensions.file

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class FileIncludePluginTest {
    @Test
    void "should extract file snippet based on start line and number of lines"() {
        def text = resultingSnippet("file.txt", "{startLine: 'multiple lines', numberOfLines: 2}")

        text.should == "a multiple lines\n" +
                "line number"
    }

    @Test
    void "should extract file snippet based on start and stop end lines"() {
        def text = resultingSnippet("file.txt", "{startLine: 'multiple lines', endLine: 'stop'}")

        text.should == "a multiple lines\n" +
                "line number\n" +
                "--- stop"
    }

    @Test
    void "should extract not match start line to the end line pattern but start from the one below"() {
        def text = resultingSnippet("file-with-similar-lines.txt",
                "{startLine: '\$prompt >', endLine: '\$prompt >'}")

        text.should == '$prompt >\n' +
                'some output\n' +
                'goes here\n' +
                '$prompt >'
    }

    @Test
    void "should validate start and stop end lines"() {
        code {
            resultingSnippet("file.txt", "{startLine: '1multiple lines', endLine: 'stop'}")
        } should throwException("there is no line containing \"1multiple lines\" in <file.txt>:\n" +
                "this is a\n" +
                "test file in\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "and then six")

        code {
            resultingSnippet("file.txt", "{startLine: 'multiple lines', endLine: 'stop2'}")
        } should throwException("there is no line containing \"stop2\" in <file.txt>:\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "and then six")
    }

    @Test
    void "should extract file snippet based on start and stop end lines excluding them"() {
        def text = resultingSnippet("file.txt", "{startLine: 'number', endLine: 'stop', excludeStartEnd: true}")

        text.should == ""
    }

    @Test
    void "should extract file snippet based on start line only"() {
        def text = resultingSnippet("file.txt", "{startLine: 'multiple lines'}")

        text.should == "a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "and then six"
    }

    @Test
    void "should extract file snippet based on end line only"() {
        def text = resultingSnippet("file.txt", "{endLine: 'stop'}")

        text.should == "this is a\n" +
                "test file in\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop"
    }

    @Test
    void "should automatically strip extra indentation"() {
        def text = resultingSnippet("script.groovy", "{startLine: 'class', endLine: '}', excludeStartEnd: true}")

        text.should ==
                "def a\n" +
                "int b"
    }

    @Test
    void "should only include lines matching regexp"() {
        def singleImport = resultingSnippet("script.groovy", "{includeRegexp: 'import.*ClassName'}")
        singleImport.should == "import a.b.c.ClassName"

        def allImports = resultingSnippet("script.groovy", "{includeRegexp: 'import'}")
        allImports.should ==
                "import e.d.g.AnotherName\n" +
                "import a.b.c.ClassName"
    }

    @Test
    void "should only include lines matching regexps list"() {
        def result = resultingSnippet("script.groovy", "{includeRegexp: ['int', 'def']}")
        result.should == "def a\n" +
                "int b"
    }

    @Test
    void "should exclude lines matching regexp"() {
        def withoutMarkers = resultingSnippet("sample-with-marker.py", "{excludeRegexp: '# exa..le'}")
        withoutMarkers.should == "print(\"hello\")"
    }

    @Test
    void "should exclude lines matching regexps list"() {
        def withoutMarkers = resultingSnippet("sample-with-multi-marker.py",
                '{excludeRegexp: ["# example", "# .rocedur."]}')
        withoutMarkers.should == "print(\"hello\")\n" +
                "\n" +
                "print(\"hello world\")"
    }

    @Test
    void "should validate highlight lines presence when highlight is a line idx"() {
        resultingSnippet("script.groovy", "{highlight: 3}")
        code {
            resultingSnippet("script.groovy", "{highlight: 7}")
        } should throwException("highlight idx is out of range: 7\n" +
                "check: script.groovy")
    }

    @Test
    void "should validate highlight lines presence when highlight is a text"() {
        resultingSnippet("script.groovy", "{highlight: 'def a'}")
        code {
            resultingSnippet("script.groovy", "{highlight: 'def c'}")
        } should throwException("highlight text <def c> is not found\n" +
                "check: script.groovy\n" +
                "import e.d.g.AnotherName\n" +
                "import a.b.c.ClassName\n" +
                "\n" +
                "class HelloWorld {\n" +
                "    def a\n" +
                "    int b\n" +
                "}")
    }

    @Test
    void "should highlight lines based on contains"() {
        def props = resultingProps("script.groovy", "{highlight: 'class'}")
        props.highlight.should == [3]
    }

    @Test
    void "should highlight lines from a highlight text file"() {
        def props = resultingProps("script.groovy", "{highlightPath: 'highlight.txt'}")
        props.highlight.should == [4, 5]
    }

    @Test
    void "should validate lines from a highlight text file"() {
        code {
            resultingProps("script.groovy", "{highlightPath: 'missing-highlight.txt'}")
        } should throwException(~/highlight text <def g> is not found/)
    }

    private static String resultingSnippet(String fileName, String value) {
        return PluginsTestUtils.processAndGetSimplifiedCodeBlock(":include-file: $fileName $value")
    }

    private static Map<String, Object> resultingProps(String fileName, String value) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-file: $fileName $value")
    }
}
