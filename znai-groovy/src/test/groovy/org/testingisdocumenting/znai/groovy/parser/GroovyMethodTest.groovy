/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.groovy.parser

import org.junit.Test

class GroovyMethodTest {
    @Test
    void "ignores spaces in types definition when compares name and types"() {
        def method = new GroovyMethod(name: "name", nameWithTypes: "name(int,boolean)")
        method.matchesNameAndType("name").should == true
        method.matchesNameAndType("name(int,boolean)").should == true
        method.matchesNameAndType("name(int, boolean)").should == true

        method.matchesNameAndType("name()").should == false
    }
}
