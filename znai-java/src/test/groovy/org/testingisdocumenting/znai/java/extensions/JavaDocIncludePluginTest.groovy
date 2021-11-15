/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.java.extensions

import org.testingisdocumenting.znai.extensions.include.IncludePlugin
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

class JavaDocIncludePluginTest {
    @Test
    void "method in doc with signature"() {
        def plugin = process('{entry: "sampleMethod(String, List)"}')
        plugin.textForSearch().text.should == 'overloaded method java doc'
    }

    @Test
    void "method in doc without signature"() {
        def plugin = process('{entry: "sampleMethod"}')
        plugin.textForSearch().text.should == 'method level java doc package.Class'
    }

    @Test
    void "method in doc inner class"() {
        def plugin = process('{entry: "InnerClass.sampleMethod"}')
        plugin.textForSearch().text.should == 'inner class method level java doc package.Class'
    }

    @Test
    void "static variable in doc"() {
        def plugin = process('{entry: "SAMPLE_CONST"}')
        plugin.textForSearch().text.should == 'variable level java doc'
    }

    @Test
    void "variable in doc with clashing name in inner class"() {
        def plugin = process('{entry: "myField"}')
        plugin.textForSearch().text.should == 'outer field description'
    }

    @Test
    void "variable in doc with inner class name qualifier"() {
        def plugin = process('{entry: "InnerClass.myField"}')
        plugin.textForSearch().text.should == 'inner field description'
    }

    @Test
    void "variable with a link in doc"() {
        def plugin = process('{entry: "SAMPLE_CONST_WITH_LINK"}')
        plugin.textForSearch().text.should == 'variable level java doc {@link package.Class}'
    }

    private static IncludePlugin process(String params) {
        return PluginsTestUtils.processAndGetIncludePlugin(":include-java-doc: WithJavaDocs.java " + params)
    }
}
