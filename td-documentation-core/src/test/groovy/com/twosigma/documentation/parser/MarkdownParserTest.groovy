package com.twosigma.documentation.parser

import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class MarkdownParserTest {
    static final MarkupParser parser = new MarkdownParser(new TestComponentsRegistry())

    private List<Map> content

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
        parse("text ![alt text](image/url \"custom title\") another text")
        assert content == [[type: 'Paragraph', content:[
                [text: "text " , type: "SimpleText"],
                [title: "custom title", destination: 'image/url', alt: 'alt text', type: 'Image', inlined: true],
                [text: " another text" , type: "SimpleText"]]]]
    }

    @Test
    void "standalone image"() {
        parse("![alt text](image/url \"custom title\")")
        assert content == [[title: "custom title", destination: 'image/url',
                                                             alt: 'alt text', inlined: false,
                                                         type: 'Image']]
    }

    private void parse(String markdown) {
        def parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
