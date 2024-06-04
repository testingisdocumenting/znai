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
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.utils.ResourceUtils
import org.testingisdocumenting.znai.utils.XmlUtils

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class DoxygenMemberParserTest {
    @Test
    void "should parse member definition"() {
        def document = XmlUtils.parseXml(ResourceUtils.textContent("doxygen-member-def.xml"))
        def memberNode = XmlUtils.anyNestedNodeByName(document, "memberdef")
        def member = DoxygenMemberParser.parse(
                TEST_COMPONENTS_REGISTRY,
                memberNode)

        TEST_COMPONENTS_REGISTRY.docStructure().fakeGlobalAnchors = [
                "classutils_1_1second_1_1MyClass": "/ref/my-class",
                "classutils_1_1second_1_1AnotherClass": "/ref/another-class",
        ]

        member.id.should == 'funcs_8h_1a9fcf12f40086d563b0227b6d39b3ade7'
        member.name.should == 'my_func'
        member.declType.should == 'decltype(par1)'
        member.static.should == false
        member.virtual.should == false
        member.const.should == true
        member.noExcept.should == true

        member.normalizedParamsSignature.should == "const utils::second::MyClass&,const utils::second::AnotherClass*"

        PropsUtils.exerciseSuppliers(member.returnType.toListOfMaps()).should == [[text: "utils::second::MyClass", url: "/ref/my-class"]]
        PropsUtils.exerciseSuppliers(member.parameters.toListOfMaps()).should == [[name: "", type: [[text: "const ", url: ""],
                                                                   [text: "utils::second::MyClass", url: "/ref/my-class"],
                                                                   [text: " &", url: ""]]],
                                              [name: "two", type: [[text: "const ", url: ""],
                                                                   [text: "utils::second::AnotherClass", url: "/ref/another-class"],
                                                                   [text: " *", url: ""]]]]
    }
}
