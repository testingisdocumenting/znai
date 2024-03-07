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
        DoxygenFullDescription desc = parseDescription("doxygen-description.xml")

        def descMaps = desc.descriptionElements.stream().map(DocElement::toMap).collect(Collectors.toList())

        descMaps.should == [
                [type: "Paragraph", content: [[text: "top ", type: "SimpleText"], [type: "StrongEmphasis", content: [[text: "level", type: "SimpleText"]]],
                                              [text: " comment ", type: "SimpleText"],
                                              [type: "Emphasis", content: [[text: "of", type: "SimpleText"]]],
                                              [text: " important", type: "SimpleText"],
                [bulletMarker: "*", tight: false, type: "BulletList", content:
                        [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "list one", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[text: "list two", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[code: "list three", type: "InlinedCode"]]]]]]],
                [delimiter: " " , startNumber: 1, type: "OrderedList", content:
                        [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "number one", type: "SimpleText"]]]]],
                         [type: "ListItem", content: [[type: "Paragraph", content: [[text: "number two", type: "SimpleText"]]]]]]],
                 [type: "Snippet", lang: "cpp", snippet: "utils::second::AnotherClass* instance;\ninstance->help(10, 20);\n", lineNumber: ""]]]]

        desc.searchTextWithoutParameters.should == "top level comment of important list one list two list three number one number two"
    }

    @Test
    void "should parse description with note"() {
        DoxygenFullDescription desc = parseDescription("doxygen-description-with-note.xml")
        def descMaps = desc.descriptionElements.stream().map(DocElement::toMap).collect(Collectors.toList())
        descMaps.should == [["type"   : "Paragraph",
                             "content": [["type": "StrongEmphasis", "content": [["text": "Domain", "type": "SimpleText"]]],
                                         ["text": " specific context setting. Describes business setting and requirements.",
                                          "type": "SimpleText"]]],
                            ["type"   : "Paragraph",
                             "content": [["attentionType": "note",
                                          "type"         : "AttentionBlock",
                                          "content"      : [["type"   : "Paragraph",
                                                             "content": [["text": "Do not reveal your password. Use alternatives:", "type": "SimpleText"],
                                                                         ["delimiter"  : " ",
                                                                          "startNumber": 1,
                                                                          "type"       : "OrderedList",
                                                                          "content"    : [["type"   : "ListItem",
                                                                                           "content": [["type": "Paragraph", "content": [["text": "option one", "type": "SimpleText"]]]]],
                                                                                          ["type"   : "ListItem",
                                                                                           "content": [["type": "Paragraph", "content": [["text": "option two", "type": "SimpleText"]]]]]]]]]]],
                                         ["text": "\n        more context ",
                                          "type": "SimpleText"]]]]
    }

    @Test
    void "should parse and convert params to api parameters"() {
        DoxygenFullDescription desc = parseDescription("doxygen-description.xml")

        desc.apiParameters.combinedTextForSearch().should == "a  description of param a item a item b b  description of param b"
        desc.apiParameters.toMap().should == [parameters: [[name: "a", type: [], anchorId: "name_a",
                                                            description: [[type: "Paragraph",
                                                                          content: [[text: "description of ", type: "SimpleText"],
                                                                                    [type: "Emphasis", content: [[text: "param", type: "SimpleText"]]],
                                                                                    [text: " a", type: "SimpleText"],
                                                                                    [delimiter: " ", startNumber:1, type: "OrderedList",
                                                                                     content: [[type: "ListItem", content: [[type: "Paragraph", content: [[text: "item a", type: "SimpleText"]]]]],
                                                                                               [type: "ListItem", content: [[type: "Paragraph", content: [[text: "item b", type: "SimpleText"]]]]]]]]]]],
                                                           [name: "b", type: [], anchorId: "name_b",
                                                            description: [[type: "Paragraph",
                                                                           content: [[text: "description of param b", type: "SimpleText"]]]]]]]
    }

    @Test
    void "should parse and convert template params to api parameters"() {
        DoxygenFullDescription desc = parseDescription("doxygen-description.xml")

        desc.apiTemplateParameters.combinedTextForSearch().should == "T1  type of the value one to print T2  type of the value two to print"
        desc.apiTemplateParameters.toMap().should == [parameters: [[name: "T1", type: [], anchorId: "name_template_T1",
                                                                    description: [[type: "Paragraph", content: [[text: "type of the value one to print ", type: "SimpleText"]]]]],
                                                                   [name: "T2", type: [], anchorId: "name_template_T2",
                                                                    description:[[type: "Paragraph", content: [[text: "type of the value two to print ", type: "SimpleText"]]]]]]]
    }

    private static DoxygenFullDescription parseDescription(String resourceName) {
        def descRoot = XmlUtils.anyNestedNodeByName(
                XmlUtils.parseXml(ResourceUtils.textContent(resourceName)),
                "detaileddescription")

        return DoxygenDescriptionParser.parseFull(TEST_COMPONENTS_REGISTRY, new DoxygenParameterList(),"name", descRoot)
    }
}
