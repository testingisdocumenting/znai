/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.doxygen.parser

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

class DoxygenTextWithLinksParserTest {
    @Test
    void "should extract text with links"() {
        def root = XmlUtils.anyNestedNodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent("doxygen-text-with-ref.xml")),
                "type")

        def textWithLinks = DoxygenTextWithLinksParser.parse(root)

        textWithLinks.toListOfMaps().should == [[text: "const " , refId: ""],
                                                [text: "utils::second::MyClass",
                                                 refId: "classutils_1_1second_1_1MyClass"],
                                                [text: " &", refId: ""]]
    }
}
