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

class DoxygenIndexParserTest {
    @Test
    void "should extract method name and ids from index"() {
        def index = DoxygenIndexParser.parse(ResourceUtils.textContent("doxygen-basic-index.xml"))
        println index.getMemberById()

        def members = index.getMemberById().values()
        members.compound.kind.should == ["namespace", "namespace", "namespace", "namespace"]
        members.compound.name.should == ["utils", "utils", "utils", "utils"]

        members.name.should == ["my_func", "my_func", "my_func", "another_func"]
    }
}
