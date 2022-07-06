/*
 * Copyright 2022 znai maintainers
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

package org.testingisdocumenting.znai.python

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PythonUtilsTest {
    @Test
    void "converts qualified name to a file path"() {
        PythonUtils.convertQualifiedNameToFilePath("fin.Money").should == "fin.py"
        PythonUtils.convertQualifiedNameToFilePath("fin.money.Money").should == "fin/money.py"
    }

    @Test
    void "requires at least a single module name"() {
        code {
            PythonUtils.convertQualifiedNameToFilePath("Money")
        } should throwException("expect the qualified name to be of a form: module.[optional.].name, given: Money")
    }

    @Test
    void "entity name from fully qualified name"() {
        PythonUtils.entityNameFromQualifiedName("").should == ""
        PythonUtils.entityNameFromQualifiedName("Money").should == "Money"
        PythonUtils.entityNameFromQualifiedName("fin.Money").should == "Money"
        PythonUtils.entityNameFromQualifiedName("fin.money.Money").should == "Money"
    }
}
