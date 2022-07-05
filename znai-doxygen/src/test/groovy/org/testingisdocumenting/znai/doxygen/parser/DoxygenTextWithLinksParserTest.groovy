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
import org.testingisdocumenting.znai.extensions.PropsUtils
import org.testingisdocumenting.znai.extensions.api.ApiLinkedText
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class DoxygenTextWithLinksParserTest {
    @Test
    void "should extract text with links"() {
        def root = XmlUtils.anyNestedNodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent("doxygen-text-with-ref.xml")),
                "type")

        TEST_COMPONENTS_REGISTRY.docStructure().fakeGlobalAnchors = [
                "classutils_1_1second_1_1MyClass": "/ref/my-class",
        ]

        def textWithLinks = DoxygenTextWithLinksParser.parse(TEST_COMPONENTS_REGISTRY.docStructure(), root)

        PropsUtils.exerciseSuppliers(textWithLinks.toListOfMaps()).should == [[text: "const ", url: ""],
                                                                              [text: "utils::second::MyClass",
                                                                               url: "/ref/my-class"],
                                                                              [text: " &", url: ""]]
    }
}
