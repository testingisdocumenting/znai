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

package org.testingisdocumenting.znai.doxygen

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

class DoxygenMemberParserTest {
    @Test
    void "should extract member definition"() {
        def member = DoxygenMemberParser.parse(ResourceUtils.textContent("doxygen-member-def.xml"),
                "namespaceutils_1_1nested",
                "funcs_8h_1a9fcf12f40086d563b0227b6d39b3ade7")

        member.definition.should == "void utils::nested::my_func"
    }
}
