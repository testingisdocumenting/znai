package com.twosigma.documentation.parser.sphinx

import com.twosigma.documentation.parser.MarkupParser
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class SphinxDocTreeParserTest {
    static final TestComponentsRegistry componentsRegistry = new TestComponentsRegistry()
    static final MarkupParser parser = new SphinxDocTreeParser(componentsRegistry)
    List<Map<String,Object>> content

    @Test
    void "section"() {
        parse("""
<document source="path">
    <section ids="indices-and-tables" names="indices\\ and\\ tables">
        <title>Indices and tables</title>
        <paragraph>Hello World for real</paragraph>
    </section>
</document>
""")

        content.should == [[title: 'Indices and tables', id: 'indices-and-tables', type: 'Section',
                            content:[[type: 'Paragraph', content: [[text: 'Hello World for real', type: 'SimpleText']]]]]]
    }

    @Test
    void "snippet"() {
        parse("""<literal_block highlight_args="{}" language="java" 
linenos="False" xml:space="preserve">System.out.println("hello world");</literal_block>""")

        content.should == [[lang: 'java', maxLineLength: 34,
                            tokens: [[type: 'text',
                                      content: 'System.out.println("hello world");']],
                            lineNumber: '', type: 'Snippet']]
    }

    @Test
    void "emphasis and strong text"() {
        parse('<paragraph><emphasis>mdoc</emphasis> <strong>table of contents</strong></paragraph>')

        content.should == [[type: 'Paragraph', content: [[type: 'Emphasis', content:[[text: 'mdoc', type: 'SimpleText']]], [text: ' ', type: 'SimpleText'],
                                                         [type: 'StrongEmphasis', content:[[text: 'table of contents', type: 'SimpleText']]]]]]
    }

    @Test
    void "bullet list"() {
        parse("""
        <bullet_list bullet="*">
            <list_item>
                <paragraph>this is</paragraph>
                <bullet_list bullet="*">
                    <list_item>
                        <paragraph>with a nested list</paragraph>
                    </list_item>
                </bullet_list>
            </list_item>
            <list_item>
                <paragraph>and here the parent list continues</paragraph>
            </list_item>
        </bullet_list> """)

        content.should == [[bulletMarker: '*', tight: false, type: 'BulletList',
                            content:[[type: 'ListItem',
                                      content:[[type: 'Paragraph', content: [[text:'this is', type: 'SimpleText']]],
                                               [bulletMarker:'*', tight: false, type: 'BulletList',
                                                content:[[type: 'ListItem', content:[[type: 'Paragraph',
                                                                                      content:[[text: 'with a nested list', type: 'SimpleText']]]]]]]]],
                                     [type: 'ListItem',
                                      content:[[type: 'Paragraph', content:[[text: 'and here the parent list continues', type: 'SimpleText']]]]]]]]

    }

    @Test
    void "numbered list"() {
        parse("""    
        <enumerated_list enumtype="arabic" prefix="" suffix=".">
            <list_item>
                <paragraph>numbered list</paragraph>
            </list_item>
            <list_item>
                <paragraph>of two items</paragraph>
            </list_item>
        </enumerated_list> """)

        content.should == [[delimiter: '.', startNumber:1, type:'OrderedList',
                            content:[[type: 'ListItem',
                                      content:[[type: 'Paragraph', content:[[text: 'numbered list', type: 'SimpleText']]]]],
                                     [type: 'ListItem',
                                      content:[[type: 'Paragraph', content:[[text: 'of two items', type: 'SimpleText']]]]]]]]

    }

    @Test
    void "literal"() {
        parse('<literal>markup</literal>')

        content.should == [[type: 'InlinedCode', code: 'markup']]
    }

    private void parse(String xml) {
        def parseResult = parser.parse(Paths.get("test.xml"), xml)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
