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
    void "should trim empty lines from start and end"() {
        def text = resultingSnippet("file-with-empty-lines.txt", "")
        text.should == "this is a\n" +
                "\n" +
                "a multiple lines\n" +
                "\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "\n" +
                "and then six"
    }

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
    void "should fail when surrounded content is empty"() {
        code {
            resultingSnippet("file-with-similar-lines-empty.txt", "{surroundedBy: '\$prompt'}")
        } should throwException("no content present after surroundedBy \$prompt")

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
    void "should extract file snippet based on surrounding pattern and exclude the pattern"() {
        def text = resultingSnippet("file-with-surround-marker.txt", "{surroundedBy: '# concept-example'}")

        text.should == "foo()\n" +
                "bar()"
    }

    @Test
    void "should extract file snippet based on multiple surrounding patterns and indent each block"() {
        def text = resultingSnippet("file-with-multiple-surround-marker.txt",
                "{surroundedBy: ['# import-list', '# concept-example']}")

        text.should == "import abc\n" +
                "import def\n" +
                "foo()\n" +
                "bar()"
    }

    @Test
    void "should extract file snippet based on multiple surrounding patterns and add specified separator"() {
        def text = resultingSnippet("file-with-multiple-surround-marker.txt",
                "{surroundedBy: ['# import-list', '# concept-example', '# another-example', '# next-last-example', '# last-example', ]," +
                        " surroundedBySeparator: ['...', null, '%']}")

        text.should == "import abc\n" +
                "import def\n" +
                "...\n" +
                "foo()\n" +
                "bar()\n" +
                "foobar()\n" +
                "%\n" +
                "almostFinish()\n" +
                "%\n" +
                "finish()"
    }

    @Test
    void "should replace text by exact match"() {
        def text = resultingSnippet("file-replace-all.txt",
                "{replace: ['foo', 'foo2']}")

        text.should == "foo2 foo2 foo2\n" +
                "bar bar bar\n" +
                "test12 great16"
    }

    @Test
    void "should replace text by exact match using multiple pairs"() {
        def text = resultingSnippet("file-replace-all.txt",
                "{replace: [['foo', 'foo2'], ['bar', 'bar3']]}")

        text.should == "foo2 foo2 foo2\n" +
                "bar3 bar3 bar3\n" +
                "test12 great16"
    }

    @Test
    void "should replace text using match group"() {
        def text = resultingSnippet("file-replace-all.txt",
                "{replace: ['test(\\\\d+)', '\$1-TEST']}")

        text.should == "foo foo foo\n" +
                "bar bar bar\n" +
                "12-TEST great16"
    }

    @Test
    void "should replace text inside surroundBy extracted group"() {
        def text = resultingSnippet("file-with-multiple-surround-marker.txt",
                "{surroundedBy: ['# import-list', '# concept-example'], replace: [['abc', 'ABC'], ['bar', 'Bar']]}")

        text.should == 'import ABC\n' +
                'import def\n' +
                'foo()\n' +
                'Bar()'
    }

    @Test
    void "should validate replace parameters"() {
        def expectedError = "replace expects list with two values [from, to] or a list of pairs [[from1, to1], [from2, to2]]"

        code {
            resultingSnippet("file-replace-all.txt",
                    "{replace: ['a']}")
        } should throwException(expectedError)

        code {
            resultingSnippet("file-replace-all.txt",
                    "{replace: [['a']]}")
        } should throwException(expectedError)
    }

    @Test
    void "should validate replace actually replaced something"() {
        code {
            resultingSnippet("file-replace-all.txt",
                    "{replace: ['no-match', 'new-value']}")
        } should throwException("content was not modified using replace from: <no-match> to: <new-value>")
    }

    @Test
    void "should extract file and exclude first and last line when excludeStartEnd is set and no start end is set"() {
        def text = resultingSnippet("file.txt", "{excludeStartEnd: true}")

        text.should == "test file in\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five"
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
    void "should extract file snippet based on start line and exclude only start"() {
        def text = resultingSnippet("file.txt", "{startLine: 'this is a', excludeStart: true}")

        text.should == "test file in\n" +
                "a multiple lines\n" +
                "line number\n" +
                "--- stop\n" +
                "and five\n" +
                "and then six"
    }

    @Test
    void "should extract file snippet based on end line and exclude only end"() {
        def text = resultingSnippet("file.txt", "{endLine: '--- stop', excludeEnd: true}")

        text.should == "this is a\n" +
                "test file in\n" +
                "a multiple lines\n" +
                "line number"
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
    void "should only include lines containing text"() {
        def result = resultingSnippet("script.groovy", "{include: ['int', 'def']}")
        result.should == "def a\n" +
                "int b"
    }

    @Test
    void "should fail if none of the contains matches"() {
        code {
            resultingSnippet("script.groovy", "{include: ['in2t', 'de2f']}")
        } should throwException("there are no lines containing <in2t>, <de2f> in <script.groovy>:\n" +
                "import e.d.g.AnotherName\n" +
                "import a.b.c.ClassName\n" +
                "\n" +
                "class HelloWorld {\n" +
                "    def a\n" +
                "    int b\n" +
                "}")
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
        def result = resultingSnippet("script.groovy", "{includeRegexp: ['i.t', 'd.f']}")
        result.should == "def a\n" +
                "int b"
    }

    @Test
    void "should fail if none of the include regexps matches"() {
        code {
            resultingSnippet("script.groovy", "{includeRegexp: ['in2t', 'de2f']}")
        } should throwException("there are no lines matching regexp <in2t>, <de2f> in <script.groovy>:\n" +
                "import e.d.g.AnotherName\n" +
                "import a.b.c.ClassName\n" +
                "\n" +
                "class HelloWorld {\n" +
                "    def a\n" +
                "    int b\n" +
                "}")
    }

    @Test
    void "should exclude lines that don't contain text"() {
        def withoutMarkers = resultingSnippet("sample-with-marker.py", "{exclude: '# example'}")
        withoutMarkers.should == "print(\"hello\")"
    }

    @Test
    void "should fail if none of the exclude contains matches"() {
        code {
            resultingSnippet("sample-with-multi-marker.py",
                    '{exclude: ["# exam34ple", "# .roc3edur."]}')
        } should throwException("there are no lines containing <# exam34ple>, <# .roc3edur.> in <sample-with-multi-marker.py>:\n" +
                "# example: how to print\n" +
                "print(\"hello\")\n" +
                "# example-end\n" +
                "\n" +
                "# procedure: how to print\n" +
                "print(\"hello world\")\n" +
                "# procedure-end")
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
    void "should fail if none of the exclude regexp matches"() {
        code {
            resultingSnippet("sample-with-multi-marker.py",
                    '{excludeRegexp: ["# exam34ple", "# .roc3edur."]}')
        } should throwException("there are no lines matching regexp <# exam34ple>, <# .roc3edur.> in <sample-with-multi-marker.py>:\n" +
                "# example: how to print\n" +
                "print(\"hello\")\n" +
                "# example-end\n" +
                "\n" +
                "# procedure: how to print\n" +
                "print(\"hello world\")\n" +
                "# procedure-end")
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
    void "should highlight all lines based on contains"() {
        def props = resultingProps("script.groovy", "{highlight: 'import'}")
        props.highlight.should == [0, 1]
    }

    @Test
    void "should highlight lines from a highlight text file"() {
        def props = resultingProps("script.groovy", "{highlightPath: 'highlight.txt'}")
        props.highlight.should == [4, 5]
    }

    @Test
    void "when title is not set should auto generate title based file name"() {
        resultingProps("script.groovy", "{autoTitle: true}").title.should == "script.groovy"
        resultingProps("script.groovy", "{autoTitle: false}").title.should == null
    }

    @Test
    void "should not allow autoTitle and title"() {
        code {
            resultingProps("script.groovy", "{title: 'hello', autoTitle: false}").title.should == null
        } should throwException("Can't have both <title> and <autoTitle> specified")
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
