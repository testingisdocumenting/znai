package com.twosigma.documentation.parser

import org.junit.Test

/**
 * @author mykola
 */
class MarkdownParserTest {
    static final MarkupParser parser = new MarkdownParser()
    private List<Map> content

    @Test
    void "should parse bullet list"() {
        parse("""* entry 
* another entry
* hello
""")
        // TODO use should == for better reporting
        assert content == [[bulletMarker: '*', tight: true, type: 'BulletList',
                            content:[[type: 'ListItem', content: [[type: 'Paragraph',
                                                                  content:[[text: 'entry', type:'SimpleText', content:[]]]]]],
                                     [type: 'ListItem', content: [[type: 'Paragraph',
                                                                  content:[[text: 'another entry', type: 'SimpleText', content:[]]]]]],
                                     [type: 'ListItem', content: [[type: 'Paragraph',
                                                                   content:[[text: 'hello', type: 'SimpleText', content:[]]]]]]]]]
    }

    private void parse(String markdown) {
        def docElement = parser.parse(markdown)
        content = docElement.getContent().collect { it.toMap() }
    }
}
