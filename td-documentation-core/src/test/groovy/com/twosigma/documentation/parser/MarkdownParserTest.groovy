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
    void "link"() {
        parse("""[label **bold**](http://test)""")

        content.should == [[type: 'Paragraph', content:[
                [url: 'http://test', type: 'Link',
                    content:[[text: 'label ' , type: 'SimpleText'], [type: 'StrongEmphasis', content:[
                            [text: 'bold', type: 'SimpleText']]]]]]]]
    }

    @Test
    void "inlined code"() {
        parse("""`InterfaceName`""")

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
    void "include plugin with text on top"() {
        parse("# section\n\nsimple text\n" +
                ":include-dummy: free-form text {param1: 'v1', param2: 'v2'}")

        content.should ==[[title: 'section', id: 'section', type: 'Section',
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

    private void parse(String markdown) {
        def parseResult = parser.parse(Paths.get("test.md"), markdown)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
