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

import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.utils.ResourceUtils

import java.util.stream.Collectors

class DoxygenCompoundParserTest {
    static DoxygenCompound compound

    @BeforeClass
    static void parse() {
        compound = DoxygenCompoundParser.parse(
                TestComponentsRegistry.TEST_COMPONENTS_REGISTRY,
                ResourceUtils.textContent("doxygen-class.xml"),
                "classutils_1_1second_1_1AnotherClass")
    }

    @Test
    void "should extract name, kind, id"() {
        compound.id.should == "classutils_1_1second_1_1AnotherClass"
        compound.kind.should == "class"
        compound.name.should == "utils::second::AnotherClass"
    }

    @Test
    void "should extract compound doc"() {
        compound.description.searchText.should == "Domain specific context setting. Describes business setting and requirements."
    }

    @Test
    void "should extract members"() {
        def members = compound.membersStream().collect(Collectors.toList())
        members.should == ["name"              | "kind"     | "visibility" | "virtual" | "static" | "const"] {
                         __________________________________________________________________________________
                           "number_of_sounds"  | "variable" | "public"     | false     | false    | false
                           "counter"           | "variable" | "public"     | false     | true     | false
                           "sing"              | "function" | "public"     | false     | false    | true
                           "bark"              | "function" | "public"     | false     | false    | false
                           "help"              | "function" | "protected"  | true      | false    | false }
    }
}
