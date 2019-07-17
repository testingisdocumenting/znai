package com.twosigma.znai.parser.sphinx.python

import com.twosigma.utils.ResourceUtils
import com.twosigma.znai.utils.XmlUtils
import org.junit.Test

class PythonFunctionXmlParserTest {
    static def xml = ResourceUtils.textContent('func-with-params.xml')
    static def function = new PythonFunctionXmlParser().parse(XmlUtils.parseXml(xml))

    @Test
    void "should extract parameter types"() {
        function.params.should == ['name'         | 'type'] {
                                   ______________________________
                                   'sender'       | 'str'
                                   'recipient'    | 'str'
                                   'message_body' | 'str'
                                   'priority'     | 'integer or None'  }
    }

    @Test
    void "should extract given parameters signature"() {
        function.paramSignatures.should == ['givenExample' | 'optional'] {
                                            ______________________________
                                                  'sender' | false
                                               'recipient' | false
                                            'message_body' | false
                                              'priority=1' | true  }
    }
}
