package com.twosigma.znai.parser.sphinx.xml

import org.junit.Test

class DocUtilsXmlFixerTest {
    @Test
    void "should replace bogus docutils module without a value with an empty value"() {
        def fixed = DocUtilsXmlFixer.fixDocUtilsIncorrectXml('<root>' +
                '<desc_signature good="value" module="test-module"></desc_signature>\n' +
                '<desc_signature good="value" module names="test">' +
                '<another_node/>\n' +
                '</desc_signature>\n')

        fixed.should == '<root><desc_signature good="value" module="test-module"></desc_signature>\n' +
                '<desc_signature good="value"  module="" names="test"><another_node/>\n' +
                '</desc_signature>\n'
    }
}
