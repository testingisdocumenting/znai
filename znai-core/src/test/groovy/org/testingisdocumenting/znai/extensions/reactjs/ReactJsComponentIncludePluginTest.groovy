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

package org.testingisdocumenting.znai.extensions.reactjs

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class ReactJsComponentIncludePluginTest {
    @Test
    void "should validate custom component name format"() {
        code {
            process("MyComponent")
        } should throwException("component path must be specified as <namespace>.<name>, given: MyComponent")
    }

    @Test
    void "should pass namespace, name and props to CustomReactJSComponent"() {
        def elements = process('myLib.MyComponent {title: "hello"}')
        elements.should == [[type: 'CustomReactJSComponent', namespace: 'myLib',
                             name: 'MyComponent',
                             props:[title: 'hello']]]
    }

    private static List<Map<String, ?>> process(params) {
        return PluginsTestUtils.processInclude(":include-reactjs-component: $params")*.toMap()
    }
}
