package com.twosigma.documentation.parser.sphinx

import com.twosigma.documentation.parser.MarkupParser
import com.twosigma.documentation.parser.TestComponentsRegistry
import org.junit.BeforeClass
import org.junit.Test

import java.nio.file.Paths

/**
 * @author mykola
 */
class SphinxDocTreeParserTest {
    static TestComponentsRegistry componentsRegistry
    static MarkupParser parser

    List<Map<String,Object>> content

    @BeforeClass
    static void init() {
        componentsRegistry = new TestComponentsRegistry()
        parser = new SphinxDocTreeParser(componentsRegistry)

        componentsRegistry.docStructure().addValidLink('chapter-two/page-four#send_message')
    }

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

    @Test
    void "reference"() {
        parse("""<reference internal="True" reftitle="send_message" 
            refuri="chapter-two/page-four#send_message">
            <literal classes="xref py py-func">send_message()</literal>
            </reference>""")

        content.should == [[type: 'Link', isFile: false, url:'/test-doc/chapter-two/page-four#send_message',
                            content: [[type: 'InlinedCode', code: 'send_message()']]]]
    }

    @Test
    void "desc signature"() {
        parse("""<desc_signature class="" first="False" fullname="send_message" 
            ids="send_message" module="True" names="send_message">
                      <desc_name xml:space="preserve">send_message</desc_name>
                      <desc_parameterlist xml:space="preserve">
                           <desc_parameter xml:space="preserve">sender</desc_parameter>
                           <desc_parameter xml:space="preserve">recipient</desc_parameter>
                           <desc_parameter xml:space="preserve">message_body</desc_parameter>
                           <desc_optional xml:space="preserve"><desc_parameter xml:space="preserve">priority=1</desc_parameter>
                          </desc_optional></desc_parameterlist>
                        </desc_signature>""")

        content.should == [[id: 'send_message', type: 'Anchor'],
                           [class: '', first: 'False',  fullname: 'send_message', ids: 'send_message' , module: 'True',
                            names: 'send_message', type: 'DocUtilsDescSignature',
                            content: [['xmlSpace': 'preserve', type: 'DocUtilsDescName', content: [[text: 'send_message', type: 'SimpleText']]],
                                      ['xmlSpace': 'preserve', type: 'DocUtilsDescParameterlist', content:
                                              [[xmlSpace: 'preserve', type: 'DocUtilsDescParameter', content:
                                                      [[text: 'sender', type: 'SimpleText']]],
                                               [xmlSpace: 'preserve', type: 'DocUtilsDescParameter', content:
                                                       [[text: 'recipient', type: 'SimpleText']]],
                                               [xmlSpace: 'preserve', type: 'DocUtilsDescParameter', content:
                                                       [[text: 'message_body', type: 'SimpleText']]],
                                               [xmlSpace: 'preserve',  type: 'DocUtilsDescOptional', content:
                                                       [[xmlSpace: 'preserve', type: 'DocUtilsDescParameter', content:
                                                               [[text: 'priority=1', type: 'SimpleText']]]]]]]]]]
    }

    private void parse(String xml) {
        def parseResult = parser.parse(Paths.get("test.xml"), xml)
        content = parseResult.docElement.getContent().collect { it.toMap() }
    }
}
