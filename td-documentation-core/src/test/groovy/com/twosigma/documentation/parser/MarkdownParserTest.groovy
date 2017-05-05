package com.twosigma.documentation.parser

import com.twosigma.utils.JsonUtils
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class MarkdownParserTest {
    static final MarkupParser parser = new MarkdownParser(new TestComponentsRegistry())

    private List<Map> content

    @Test
    void "link"() {
        parse("""[label **bold**](http://test)""")

        assert content == [[type: 'Paragraph', content:[
                [url: 'http://test', type: 'Link',
                    content:[[text: 'label ' , type: 'SimpleText'], [type: 'StrongEmphasis', content:[
                            [text: 'bold', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "inlined code"() {
        parse("""`InterfaceName`""")

        assert content == [[type: 'Paragraph', content:[[type: 'InlinedCode', code: 'InterfaceName']]]]
    }

    @Test
    void "bullet list"() {
        parse("""* entry
* another entry
* hello
""")
        // TODO use should == for better reporting
        assert content == [[bulletMarker: '*', tight: true, type: 'BulletList',
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
        assert content == [[delimiter: '.', startNumber: 1, type: 'OrderedList',
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

        assert content == [[type: 'BlockQuote',
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

        assert content == [[type: 'Paragraph', content: [[text: 'hello', type: 'SimpleText']]],
                           [type: 'ThematicBreak'],
                           [type: 'Paragraph', content:[[text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "soft line break"() {
        parse("hello\nworld")
        assert content == [[type: 'Paragraph', content:
                [[text: 'hello', type: 'SimpleText'], [type: 'SoftLineBreak'], [text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "hard line break"() {
        parse("hello\\\nworld")
        assert content == [[type: 'Paragraph', content:
                [[text: 'hello', type: 'SimpleText'], [type: 'HardLineBreak'], [text: 'world', type: 'SimpleText']]]]
    }

    @Test
    void "inlined image"() {
        parse("text ![alt text](images/png-test.png \"custom title\") another text")
        assert content == [[type: 'Paragraph', content:[
                [text: "text " , type: "SimpleText"],
                [title: "custom title", destination: 'images/png-test.png', alt: 'alt text', type: 'Image', inlined: true,
                 width:762, height:581],
                [text: " another text" , type: "SimpleText"]]]]
    }

    @Test
    void "standalone image"() {
        parse("![alt text](images/png-test.png \"custom title\")")
        assert content == [[title: "custom title", destination: 'images/png-test.png',
                            alt: 'alt text', inlined: false,
                            width:762, height:581,
                            type: 'Image']]
    }

    @Test
    void "include plugin"() {
        parse(":include-dummy: free-form text {param1: 'v1', param2: 'v2'}")
        assert content == [[type: 'IncludeDummy', ff: 'free-form text', opts: [param1: 'v1', param2: 'v2']]]
    }

    @Test
    void "include plugin with text on top"() {
        parse("# section\n\nsimple text\n" +
                ":include-dummy: free-form text {param1: 'v1', param2: 'v2'}")

        assert content ==[[title: 'section', id: 'section', type: 'Section',
                           content:
                                   [[type: 'Paragraph', content:[
                                           [text: 'simple text', type: 'SimpleText'],
                                           [type: 'SoftLineBreak']]],
                                    [ff: 'free-form text', opts: [param1: 'v1', param2: 'v2'], type: 'IncludeDummy']]]]
    }

    @Test
    void "fenced plugin"() {
        parse("~~~dummy\n" +
                "test\n" +
                "block\n" +
                "~~~")

        assert content == [[content: 'test\nblock\n', type: 'FenceDummy']]
    }

    @Test
    void "fenced plugin with params"() {
        parse("~~~dummy free-form {'p1': 'v1', 'p2': 'v2'}\n" +
                "test\n" +
                "block\n" +
                "~~~")

        assert content == [[content: 'test\nblock\n', 'freeParam': 'free-form',
                            'p1': 'v1', 'p2': 'v2',
                            type: 'FenceDummy']]
    }

    private void parse(String markdown) {
        def parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
