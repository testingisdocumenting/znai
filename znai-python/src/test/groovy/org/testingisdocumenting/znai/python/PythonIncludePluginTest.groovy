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

package org.testingisdocumenting.znai.python

import org.junit.Test
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PythonIncludePluginTest {
    @Test
    void "should extract function full content"() {
        def props = resultingProps('example.py', '{entry: "Animal.says"}')
        props.should == [lang: 'python', entry: 'Animal.says', snippet: "def says(self):\n" +
                "    \"\"\"\n" +
                "    animal talks\n" +
                "    \"\"\"\n" +
                "    print(\"hello\")"]
    }

    @Test
    void "should extract function body only"() {
        def props = resultingProps('example.py', '{entry: "Animal.says", bodyOnly: true}')
        props.should == [lang: 'python', entry: 'Animal.says', bodyOnly: true,
                         snippet: "print(\"hello\")"]
    }

    @Test
    void "should validate entry value presence"() {
        code {
            resultingProps('example.py', '')
        } should throwException("'entry' is required for plugin: python")
    }

    @Test
    void "should validate entry presence"() {
        code {
            resultingProps('example.py', '{entry: "my_func_two"}')
        } should throwException("can't find entry: my_func_two in: example.py, available entries: MyClass, MyClass.__init__, AClass, AClass.foo, " +
                "ADataClass, ADataClassWithDocString, Animal, " +
                "Animal.says, a_method, func_no_docs, my_func, another_func, one_line_var, multi_line_var, MyClass.V")
    }

    private static Map<String, Object> resultingProps(String fileName, String value) {
        return PluginsTestUtils.processIncludeAndGetProps(":include-python: $fileName $value")
    }
}
