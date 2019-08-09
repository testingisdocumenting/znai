/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twosigma.znai.parser.sphinx.python

import com.twosigma.znai.utils.ResourceUtils
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
