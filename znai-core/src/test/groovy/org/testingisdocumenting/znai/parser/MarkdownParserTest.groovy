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

package org.testingisdocumenting.znai.parser

import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser
import org.junit.Test

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class MarkdownParserTest {
    static final TestComponentsRegistry componentsRegistry = TEST_COMPONENTS_REGISTRY
    static final MarkupParser parser = new MarkdownParser(componentsRegistry)

    private List<Map> content
    private MarkupParserResult parseResult

    @Test
    void "link"() {
        parse("[label **bold**](http://test)")

        content.should == [[type: 'Paragraph', content:[
                [url: 'http://test', type: 'Link', isFile: false,
                    content:[[text: 'label ' , type: 'SimpleText'], [type: 'StrongEmphasis', content:[
                            [text: 'bold', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "strike through"() {
        parse("~~test~~")
        content.should == [[type: 'Paragraph', content: [[type: 'StrikeThrough',
                                                          content: [[text: 'test', type: 'SimpleText']]]]]]
    }

    @Test
    void "link to a supported location"() {
        parse("[label](file:/test)")
        parse("[label](http:/test)")
        parse("[label](https:/test)")
        parse("[label](mailto:/test)")
    }

    @Test
    void "link to an unsupported location"() {
        code {
            parse("[label](../test)")
        } should throwException(~/Do not use .. based urls: ..\/test/)

        code {
            parse("[label](dir/page/extra)")
        } should throwException(~/Unexpected url pattern: <dir\/page\/extra>/)
    }

    @Test
    void "link to a non existing within documentation location"() {
        code {
            parse("# wrong link\n\n[label](dir-name/non-existing-file-name)")
        } should throwException(~/no valid link found in test.md, section title: wrong link: dir-name\/non-existing-file-name$/)

        code {
            parse("[label](dir-name/non-existing-file-name#page-section)")
        } should throwException(~/no valid link found in test\.md, section title: : dir-name\/non-existing-file-name#page-section$/)
    }

    @Test
    void "link to an existing within documentation location"() {
        componentsRegistry.docStructure().addValidLink("valid-dir-name/page-name")
        componentsRegistry.docStructure().addValidLink("valid-dir-name/page-name#page-section")

        parse("[label](valid-dir-name/page-name)")
        content.should == [[type: 'Paragraph', content:[[url: '/test-doc/valid-dir-name/page-name', isFile: false,
                                                         type: 'Link',
                                                         content:[[text: 'label' , type: 'SimpleText']]]]]]

        parse("[label](valid-dir-name/page-name#page-section)")
        content.should == [[type: 'Paragraph', content:[[url: '/test-doc/valid-dir-name/page-name#page-section',
                                                         isFile: false, type: 'Link',
                                                         content:[[text: 'label' , type: 'SimpleText']]]]]]
    }

    @Test
    void "link to a local file"() {
        parse("[download](file.txt)")
        content.should == [[type: 'Paragraph', content:[[url: '/test-doc/file.txt',
                                                         isFile: true, type: 'Link',
                                                         content:[[text: 'download' , type: 'SimpleText']]]]]]
    }

    @Test
    void "inlined code"() {
        parse("`InterfaceName`")

        content.should == [[type: 'Paragraph', content:[[type: 'InlinedCode', code: 'InterfaceName']]]]
    }

    @Test
    void "header badge"() {
        parse('# my header {badge: "v1.32"}')
        content.should == [[title: 'my header' , id: 'my-header', badge: 'v1.32', type: 'Section']]
    }

    @Test
    void "bullet list"() {
        parse("""* entry
* another entry
* hello
""")
        content.should == [[bulletMarker: '*', tight: true, type: 'BulletList',
                            content:[[type: 'ListItem', content: [[type: 'Paragraph',
                                                                  content:[[text: 'entry', type:'SimpleText']]]]],
                                     [type: 'ListItem', content: [[type: 'Paragraph',
                                                                  content:[[text: 'another entry', type: 'SimpleText']]]]],
                                     [type: 'ListItem', content: [[type: 'Paragraph',
                                                                   content:[[text: 'hello', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "ordered list"() {
        parse("""1. hello
2. world
3. of markdown
""")
        content.should == [[delimiter: '.', startNumber: 1, type: 'OrderedList',
                             content:[[type: 'ListItem', content: [[type: 'Paragraph',
                                                                    content:[[text: 'hello', type: 'SimpleText']]]]],
                                      [type: 'ListItem', content: [[type: 'Paragraph',
                                                                    content:[[text: 'world', type: 'SimpleText']]]]],
                                      [type: 'ListItem', content: [[type: 'Paragraph',
                                                                    content:[[text: 'of markdown', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "block quote"() {
        parse(""" > important message
> for a reader
""")

        content.should == [[type: 'BlockQuote',
                             content:[[type: 'Paragraph',
                                       content:[[text: 'important message', type: 'SimpleText'],
                                                [type: 'SoftLineBreak'],
                                                [text: 'for a reader', type: 'SimpleText']]]]]]
    }

    @Test
    void "thematic break"() {
        parse("""hello
****
world""")

        content.should == [[type: 'Paragraph', content: [[text: 'hello', type: 'SimpleText']]],
                           [type: 'ThematicBreak'],
                           [type: 'Paragraph', content:[[text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "soft line break"() {
        parse("hello\nworld")
        content.should == [[type: 'Paragraph', content:
                [[text: 'hello', type: 'SimpleText'], [type: 'SoftLineBreak'], [text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "hard line break"() {
        parse("hello\\\nworld")
        content.should == [[type: 'Paragraph', content:
                [[text: 'hello', type: 'SimpleText'], [type: 'HardLineBreak'], [text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "github flavored table"() {
        parse("""| Github        | Flavored       | Table  |
| ------------- |:-------------:| -----:|
| col 3 is      | right-aligned | \$1600 |
| col 2 is      | centered      |   \$12 |""")

        content.should == [[table:[columns:[[title: 'Github', align: 'left'],
                                            [title: 'Flavored', align: 'center'],
                                            [title: 'Table', align: 'right']],
                                   data:[[[[text:'col 3 is', type: 'SimpleText']],
                                          [[text: 'right-aligned', type: 'SimpleText']],
                                          [[text:'$1600', type: 'SimpleText']]],
                                         [[[text: 'col 2 is', type: 'SimpleText']],
                                          [[text: 'centered', type: 'SimpleText']],
                                          [[text: '$12', type: 'SimpleText']]]]],
                            type: 'Table']]
    }

    @Test
    void "top level sections"() {
        parse("# Section\ntext text")
        content.should == [[type: 'Section', id: "section", title: "Section", content:[
                [type: "Paragraph", content: [[type: "SimpleText", text: "text text"]]]]]]
    }

    @Test
    void "top level section without text"() {
        parse("# ")
        content.should == [[type: 'Section', id: "", title: ""]]
    }

    @Test
    void "second level section"() {
        parse("## Secondary Section \ntext text")
        content.should == [[type: 'SubHeading', level: 2, title: 'Secondary Section', id: 'secondary-section'],
                           [type: 'Paragraph', content: [[type: 'SimpleText', text: 'text text']]]]
    }

    @Test
    void "second level section without text"() {
        parse("## ")
        content.should == [[type: 'SubHeading', level: 2, title: '', id: '']]
    }

    @Test
    void "second level section with payload"() {
        parse('## Secondary Section {badge: "v2.0"}" \ntext text')
        content.should == [[type: 'SubHeading', level: 2, title: 'Secondary Section', id: 'secondary-section', badge: 'v2.0'],
                           [type: 'Paragraph', content: [[type: 'SimpleText', text: 'text text']]]]
    }

    @Test
    void "repeating sub headings within different parent sections"() {
        parse("""
# top level section
## example
#### java
### java
## constraint
### java
#### java
#### java
# another top level
## example
## example
        """)

        content.should == [[title: 'top level section', id: 'top-level-section', type: 'Section',
                            content: [[level: 2, title: 'example', id: 'top-level-section-example', type: 'SubHeading'],
                                      [level: 4, title: 'java', id: 'top-level-section-example-java', type: 'SubHeading'],
                                      [level: 3, title: 'java', id: 'top-level-section-example-java-2', type: 'SubHeading'],
                                      [level: 2, title: 'constraint', id: 'top-level-section-constraint', type: 'SubHeading'],
                                      [level: 3, title: 'java', id: 'top-level-section-constraint-java', type: 'SubHeading'],
                                      [level: 4, title: 'java', id: 'top-level-section-constraint-java-java', type: 'SubHeading'],
                                      [level: 4, title: 'java', id: 'top-level-section-constraint-java-java-2', type: 'SubHeading']]],
                           [title: 'another top level', id: 'another-top-level', type: 'Section',
                            content: [[level: 2, title: 'example', id: 'another-top-level-example', type: 'SubHeading'],
                                      [level: 2, title: 'example', id: 'another-top-level-example-2', type: 'SubHeading']]]]
    }

    @Test
    void "top level section with styles"() {
        code {
            parse("# title with **text**")
        } should throwException("only regular text is supported in headings")

        code {
            parse("# title *with*")
        } should throwException("only regular text is supported in headings")
    }

    @Test
    void "second level section with styles"() {
        code {
            parse("## title with **text**")
        } should throwException("only regular text is supported in headings")

        code {
            parse("## title *with*")
        } should throwException("only regular text is supported in headings")
    }

    @Test
    void "inlined image"() {
        TEST_COMPONENTS_REGISTRY.timeService().fakedFileTime = 300000

        parse("text ![alt text](images/png-test.png \"custom title\") another text")
        content.should == [[type: 'Paragraph', content:[
                [text: "text " , type: "SimpleText"],
                [title: "custom title", destination: '/test-doc/png-test.png', alt: 'alt text', type: 'Image', inlined: true,
                 width:762, height:581, timestamp: 300000],
                [text: " another text" , type: "SimpleText"]]]]
    }

    @Test
    void "standalone image"() {
        TEST_COMPONENTS_REGISTRY.timeService().fakedFileTime = 200000

        parse("![alt text](images/png-test.png \"custom title\")")
        content.should == [[title: "custom title", destination: '/test-doc/png-test.png',
                            alt: 'alt text', inlined: false,
                            width:762, height:581,
                            timestamp: 200000,
                            type: 'Image']]
    }


    @Test
    void "image with external ref"() {
        TEST_COMPONENTS_REGISTRY.timeService().fakedFileTime = 200000

        parse("![alt text](https://host/images/png-test.png \"custom title\")")
        content.should == [[title: "custom title", destination: 'https://host/images/png-test.png',
                            alt: 'alt text', inlined: false,
                            type: 'Image']]
    }

    @Test
    void "image with no alt text"() {
        TEST_COMPONENTS_REGISTRY.timeService().fakedFileTime = 200000

        parse("![](images/png-test.png \"custom title\")")
        content.should == [[title: "custom title", destination: '/test-doc/png-test.png',
                            alt: 'image', inlined: false,
                            width:762, height:581,
                            timestamp: 200000,
                            type: 'Image']]
    }

    @Test
    void "include plugin"() {
        parse(":include-dummy: free-form text {param1: 'v1', param2: 'v2'}")
        content.should == [[type: 'IncludeDummy', ff: 'free-form text', opts: [param1: 'v1', param2: 'v2']]]
    }

    @Test
    void "include plugin with any number of spaces in front"() {
        parse(" :include-dummy: free-form text {param1: 'v1', param2: 'v2'}")
        def expected = [[type: 'IncludeDummy', ff: 'free-form text', opts: [param1: 'v1', param2: 'v2']]]
        content.should == expected

        parse("   :include-dummy: free-form text {param1: 'v1', param2: 'v2'}")
        content.should == expected
    }

    @Test
    void "include plugin right after paragraph of text"() {
        parse("hello text\n:include-dummy: free-form text {param1: 'v1', param2: 'v2'}")

        content.should == [[type: 'Paragraph', content: [[text: 'hello text', type: 'SimpleText']]],
                           [ff: 'free-form text', opts: [param1: 'v1', param2: 'v2'], type: 'IncludeDummy']]
    }

    @Test
    void "include plugin inside nested code block"() {
        parse("    :include-dummy: free-form text {param1: 'v1', param2: 'v2'}")
        content.should == [[lang: "",
                            snippet: ":include-dummy: free-form text {param1: 'v1', param2: 'v2'}\n",
                            lineNumber: "", type: "Snippet"]]
    }

    @Test
    void "include plugin inside numbered list"() {
        parse("1. step one\n\n" +
                "    :include-dummy: free-form text1 {param1: 'v1', param2: 'v2'}\n" +
                "2. step two\n\n" +
                "    :include-dummy: free-form text2 {param1: 'v3', param2: 'v4'}\n")

        content.should == [[delimiter: '.', startNumber: 1, type: 'OrderedList',
                            content: [[type: 'ListItem', content: [[type: 'Paragraph',
                                                                    content: [[text: 'step one', type: 'SimpleText']]],
                                                                   [ff: 'free-form text1', opts: [param1: 'v1', param2: 'v2'], type: 'IncludeDummy']]],
                                      [type:  'ListItem', content: [[type: 'Paragraph', content: [[text: 'step two', type: 'SimpleText']]],
                                                                    [ff: 'free-form text2', opts: [param1: 'v3', param2: 'v4'], type: 'IncludeDummy']]]]]]
    }

    @Test
    void "include plugin without additional opts"() {
        parse(":include-dummy: free-form text\n\nhello world")
        content.should == [[type: 'IncludeDummy', ff: 'free-form text', opts: [:]],
                           [type: 'Paragraph', content: [[text: 'hello world', type: 'SimpleText']]]]
    }

    @Test
    void "include plugin with additional opts on the same line"() {
        parse(":include-dummy: free-form text {param1: 'v1', param2: 'v2'}\n")
        content.should == [[type: 'IncludeDummy', ff: 'free-form text', opts: [param1: 'v1', param2: 'v2']]]
    }

    @Test
    void "include plugin with additional opts on multiple lines"() {
        parse(":include-dummy: free-form text {param1: 'v1',\n param2: 'v2'}\n")
        content.should == [[type: 'IncludeDummy', ff: 'free-form text', opts: [param1: 'v1', param2: 'v2']]]
    }

    @Test
    void "include multiple plugins without empty line in between"() {
        parse(":include-dummy: free-form text1 {param1: 'v1', param2: 'v2'}\n" +
                ":include-dummy: free-form text2 {param3: 'v3', param4: 'v4'}\n\n" +
                "hello world")

        content.should == [
                [type: 'IncludeDummy', ff: 'free-form text1', opts: [param1: 'v1', param2: 'v2']],
                [type: 'IncludeDummy', ff: 'free-form text2', opts: [param3: 'v3', param4: 'v4']],
                [type: 'Paragraph', content: [[text: 'hello world', type: 'SimpleText']]]]
    }

    @Test
    void "include plugin error should provide context of the plugin"() {
        code {
            parse(":include-dummy: free-form text {param1: 'v1', param2: 'v2', throw: 'message to throw'}")
        } should throwException("error handling include plugin <dummy>: message to throw\n" +
                "  free param: free-form text\n" +
                "  opts: {\"param1\":\"v1\",\"param2\":\"v2\",\"throw\":\"message to throw\"}")
    }

    @Test
    void "code snippets by indentation"() {
        parse("    println 'hello world'")
        content.should == [[lang: '', snippet:"println 'hello world'\n", lineNumber: '', type: 'Snippet']]
    }

    @Test
    void "code snippets by fence"() {
        parse("```\n" +
                "println 'hello world'\n" +
                "```")
        content.should == [[lang: '', snippet:"println 'hello world'\n", lineNumber: '', type: 'Snippet']]
    }

    @Test
    void "code snippets by fence with highlight"() {
        parse("```script {highlight: \"hello\"}\n" +
                "println 'hello world'\n" +
                "```")
        content.should == [[lang: 'script', snippet:"println 'hello world'\n", lineNumber: '',
                            highlight: [0], type: 'Snippet']]
    }

    @Test
    void "fenced plugin"() {
        parse("~~~dummy\n" +
                "test\n" +
                "block\n" +
                "~~~")

        content.should == [[content: 'test\nblock\n', type: 'FenceDummy']]
    }

    @Test
    void "fenced plugin with params"() {
        parse("~~~dummy free-form {'p1': 'v1', 'p2': 'v2'}\n" +
                "test\n" +
                "block\n" +
                "~~~")

        content.should == [[content: 'test\nblock\n', 'freeParam': 'free-form',
                            'p1': 'v1', 'p2': 'v2',
                            type: 'FenceDummy']]
    }

    @Test
    void "fenced plugin error should provide context of the plugin"() {
        code {
            parse("~~~dummy {throw: \"some error\"}\n" +
                    "test\n" +
                    "block\n" +
                    "~~~")
        } should throwException("error handling fence plugin <dummy>: some error\n" +
                "  free param: \n" +
                "  opts: {\"throw\":\"some error\"}\n" +
                "  fence content:\n" +
                "test\n" +
                "block\n")
    }

    @Test
    void "inlined code plugin"() {
        parse("`:dummy: free-param {p1: 'v1'}`")

        content.should == [[type: 'Paragraph', content: [[type: 'InlinedCodeDummy', ff: 'free-param', opts: [p1: 'v1']]]]]
    }

    @Test
    void "inlined code plugin error should provide context of the plugin"() {
        code {
            parse("`:dummy: user-param {p1: 'v1', throw: 'process error'}`")
        } should throwException("error handling inline code plugin <dummy>: process error\n" +
                "  free param: user-param\n" +
                "  opts: {\"p1\":\"v1\",\"throw\":\"process error\"}")
    }

    @Test
    void "custom page data"() {
        parse("""---
title: custom title
---""")

        parseResult.pageMeta.toMap().title.should == ["custom title"]
    }

    private void parse(String markdown) {
        parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
