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
import org.testingisdocumenting.znai.utils.ResourceUtils

class DoxygenIndexParserTest {
    static DoxygenIndex index

    @BeforeClass
    static void init() {
        index = DoxygenIndexParser.parse(ResourceUtils.textContent("doxygen-index.xml"))
    }

    @Test
    void "should render all available entry names"() {
        index.renderAvailableNames().should == "free_func\n" +
                "utils\n" +
                "utils::another_func\n" +
                "utils::my_func\n" +
                "utils::second::MyClass\n" +
                "utils::second::MyClass::bark"
    }

    @Test
    void "should extract compound name and ids from index"() {
        def compounds = index.getCompoundById().values()
        compounds.kind.should == ["namespace", "class", "file"]
        compounds.name.should == ["utils", "utils::second::MyClass", "funcs.h"]
    }

    @Test
    void "should extract method name and ids from index"() {
        def members = index.getMemberById().values()
        members.compound.kind.should == ["namespace", "namespace", "namespace", "namespace", "class", "file"]
        members.compound.name.should == ["utils", "utils", "utils", "utils", "utils::second::MyClass", "funcs.h"]

        members.name.should == ["my_func", "my_func", "my_func", "another_func", "bark", "free_func"]
    }

    @Test
    void "should find a compound by name"() {
        def compound = index.findCompoundByName("utils::second::MyClass")
        compound.name.should == "utils::second::MyClass"

        index.findCompoundByName("utils::second::WrongClass").should == null
    }

    @Test
    void "should find a member by name"() {
        def member = index.findMemberByName("utils::second::MyClass::bark")
        member.compoundName.should == "utils::second::MyClass"
        member.name.should == "bark"

        index.findMemberByName("utils::second::MyClass::bleep").should == null
    }
}
