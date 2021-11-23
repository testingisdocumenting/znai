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
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

class DoxygenReturnParserTest {
    @Test
    void "parse return block"() {
        def parsed = parseReturn("doxygen-return.xml")
        parsed.docElementList.collect { it.toMap() }.should == [
                [type: "Paragraph", content: [[type: "Emphasis", content: [[text: "sum", type: "SimpleText"]]],
                                              [text: " of " , type: "SimpleText"],
                                              [type: "StrongEmphasis", content: [[text: "two", type: "SimpleText"]]]]]]
        parsed.textForSearch.should == "sum of two"
    }

    private static DoxygenReturn parseReturn(String resourceName) {
        def root = XmlUtils.anyNestedNodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent(resourceName)),
                "simplesect")

        return DoxygenReturnParser.parse(TestComponentsRegistry.TEST_COMPONENTS_REGISTRY, root)
    }
}
