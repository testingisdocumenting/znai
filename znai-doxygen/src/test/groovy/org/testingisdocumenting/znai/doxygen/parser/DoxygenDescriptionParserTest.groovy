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
import org.testingisdocumenting.znai.parser.docelement.DocElement
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

import java.util.stream.Collectors

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class DoxygenDescriptionParserTest {
    @Test
    void "should parse and convert description to doc elements"() {
        DoxygenDescription desc = parseDescription("doxygen-description.xml")

        def descMaps = desc.docElements.stream().map(DocElement::toMap).collect(Collectors.toList())

        descMaps.should == [
                [type: "Paragraph", content: [[text: "top ", type: "SimpleText"], [type: "StrongEmphasis", content: [[text: "level", type: "SimpleText"]]],
                                              [text: " comment ", type: "SimpleText"],
                                              [type: "Emphasis", content: [[text: "of", type: "SimpleText"]]],
                                              [text: " important", type: "SimpleText"],
                [bulletMarker: "*", tight: false, type: "BulletList", content:
                        [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "list one", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[text: "list two", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[text: "list three", type: "SimpleText"]]]]]]],
                [delimiter: " " , startNumber: 1, type: "OrderedList", content:
                        [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "number one", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[text: "number two", type: "SimpleText"]]]]]]]]]]

        desc.searchTextWithoutParameters.should == "top level comment of important list one list two list three number one number two"
    }

    @Test
    void "should parse and convert params to api parameters"() {
        DoxygenDescription desc = parseDescription("doxygen-description.xml")
        println desc.apiParameters.combinedTextForSearch()

        desc.apiParameters.combinedTextForSearch().should == "a  description of param a item a item b b  description of param b"
        desc.apiParameters.toMap().should == [parameters: [[name: "a", type: "", anchorId: "params_anchor_a",
                                                            description: [[type: "Paragraph",
                                                                          content: [[text: "description of ", type: "SimpleText"],
                                                                                    [type: "Emphasis", content: [[text: "param", type: "SimpleText"]]],
                                                                                    [text: " a", type: "SimpleText"],
                                                                                    [delimiter: " ", startNumber:1, type: "OrderedList",
                                                                                     content: [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "item a", type: "SimpleText"]]]]],
                                                                                               [type: "ListItem", content: [[type: "Paragraph", content: [[text: "item b", type: "SimpleText"]]]]]]]]]]],
                                                           [name: "b", type: "", anchorId: "params_anchor_b",
                                                            description: [[type: "Paragraph",
                                                                           content: [[text: "description of param b", type: "SimpleText"]]]]]]]
    }

    private static DoxygenDescription parseDescription(String resourceName) {
        def descRoot = XmlUtils.nodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent(resourceName)),
                "detaileddescription")

        def desc = DoxygenDescriptionParser.parse(TEST_COMPONENTS_REGISTRY, "params_anchor", descRoot)
    }
}
