package com.twosigma.documentation.parser

import org.junit.Test

import java.nio.file.Paths

import static com.twosigma.testing.Ddjt.code
import static com.twosigma.testing.Ddjt.throwException

/**
 * @author mykola
 */
class MarkdownParserTest {
    static final TestComponentsRegistry componentsRegistry = new TestComponentsRegistry()
    static final MarkupParser parser = new MarkdownParser(componentsRegistry)

    private List<Map> content
    private MarkupParserResult parseResult

    @Test
    void "link"() {
        parse("[label **bold**](http://test)")

        content.should == [[type: 'Paragraph', content:[
                [url: 'http://test', type: 'Link',
                    content:[[text: 'label ' , type: 'SimpleText'], [type: 'StrongEmphasis', content:[
                            [text: 'bold', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "link to a supported location"() {
        parse("[label](file:/test)")
        parse("[label](http:/test)")
        parse("[label](https:/test)")
        parse("[label](mailto:/test)")
    }

    @Test
    void "link to a unsupported location"() {
        code {
            parse("[label](../test)")
        } should throwException(~/Do not use .. based urls: ..\/test/)

        code {
            parse("[label](dir/page/extra)")
        } should throwException(~/Unexpected url pattern: dir\/page\/extra/)
    }

    @Test
    void "link to a non existing within documentation location"() {
        code {
            parse("# wrong link\n\n[label](dir-name/non-existing-file-name)")
        } should throwException(~/no valid link found in section 'wrong link': dir-name\/non-existing-file-name$/)

        code {
            parse("[label](dir-name/non-existing-file-name#page-section)")
        } should throwException(~/no valid link found in section '': dir-name\/non-existing-file-name#page-section$/)
    }

    @Test
    void "link to an existing within documentation location"() {
        componentsRegistry.validator.addValidLink("valid-dir-name/page-name")
        componentsRegistry.validator.addValidLink("valid-dir-name/page-name#page-section")

        parse("[label](valid-dir-name/page-name)")
        content.should == [[type: 'Paragraph', content:[[url: '/test-doc/valid-dir-name/page-name', type: 'Link',
                                                         content:[[text: 'label' , type: 'SimpleText']]]]]]

        parse("[label](valid-dir-name/page-name#page-section)")
        content.should == [[type: 'Paragraph', content:[[url: '/test-doc/valid-dir-name/page-name#page-section', type: 'Link',
                                                         content:[[text: 'label' , type: 'SimpleText']]]]]]
    }

    @Test
    void "inlined code"() {
        parse("`InterfaceName`")

        content.should == [[type: 'Paragraph', content:[[type: 'InlinedCode', code: 'InterfaceName']]]]
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
    void "second level section"() {
        parse("## Secondary Section \ntext text")
        content.should == [[type: 'SubHeading', level: 2, content:[
                [type: "SimpleText", text: "Secondary Section"]]],
                           [type: "Paragraph", content: [[type: "SimpleText", text: "text text"]]]]
    }

    @Test
    void "inlined image"() {
        parse("text ![alt text](images/png-test.png \"custom title\") another text")
        content.should == [[type: 'Paragraph', content:[
                [text: "text " , type: "SimpleText"],
                [title: "custom title", destination: 'images/png-test.png', alt: 'alt text', type: 'Image', inlined: true,
                 width:762, height:581],
                [text: " another text" , type: "SimpleText"]]]]
    }

    @Test
    void "standalone image"() {
        parse("![alt text](images/png-test.png \"custom title\")")
        content.should == [[title: "custom title", destination: 'images/png-test.png',
                            alt: 'alt text', inlined: false,
                            width:762, height:581,
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
        content.should == [[lang: "", maxLineLength:59,
                            tokens:[[type: "text", content: ":include-dummy: free-form text {param1: 'v1', param2: 'v2'}\n"]],
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
    void "inlined code plugin"() {
        parse("`dummy:free-param {p1: 'v1'}`")

        content.should == [[type: 'Paragraph', content: [[type: 'InlinedCodeDummy', ff: 'free-param', opts: [p1: 'v1']]]]]
    }

    @Test
    void "custom page data"() {
        parse("""---
title: custom title
---""")

        parseResult.properties.title.should == ["custom title"]
    }

    private void parse(String markdown) {
        parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
