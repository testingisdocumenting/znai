package com.twosigma.documentation.java.extensions

import com.twosigma.documentation.extensions.include.PluginsTestUtils
import com.twosigma.documentation.parser.docelement.DocElement
import org.junit.Test

/**
 * @author mykola
 */
class JavaEnumEntriesIncludePluginTest {
    @Test
    void "should generate table component with enum entries"() {
        def result = process('Enum.java', '')
        result*.toMap().should == [[table: [data   : [[[[code: 'ENTRY_ONE_WITH_A_LONG_NAME', type: 'InlinedCode']],
                                                      [[type: 'Paragraph', content: [[text: 'description of ', type: 'SimpleText'],
                                                                                     [type: 'StrongEmphasis', content: [[text: 'entry one', type: 'SimpleText']]]]]]],
                                                     [[[code: 'ENTRY_TWO', type: 'InlinedCode']],
                                                      [[type: 'Paragraph', content: [[text: 'description of entry two', type: 'SimpleText']]]]]],
                                           columns: [[title: 'name', align: 'right', width: '30%'],
                                                     [title: 'description']],
                                           styles : ['middle-vertical-lines-only', 'no-header', 'no-vertical-padding']],
                                   type : 'Table']]

    }

    private static List<DocElement> process(String fileName, String params) {
        return PluginsTestUtils.process(":include-java-enum-entries: $fileName $params").docElements
    }
}
