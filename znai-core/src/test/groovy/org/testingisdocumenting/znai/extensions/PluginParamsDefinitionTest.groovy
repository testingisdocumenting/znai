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

package org.testingisdocumenting.znai.extensions

import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class PluginParamsDefinitionTest {
    static def paramsDefinition = new PluginParamsDefinition()
            .add("title", PluginParamType.STRING, "title of snippet", "\"example of API\"")
            .add("highlight", PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER,
                    "lines to highlight", "[4, \"class\"]")
            .add("autoTitle", PluginParamType.BOOLEAN, "auto title based on path", "true")

    @Test
    void "passes validation"() {
        paramsDefinition.validate(new PluginParams("file", [title: "my title"]))
    }

    @Test
    void "validates param names"() {
        code {
            paramsDefinition.validate(new PluginParams("file", [totle: "my title"]))
        } should throwException("unrecognized parameter(s): totle\n" +
                "\n" +
                "available plugin parameters:\n" +
                "title: title of snippet <string> (e.g. \"example of API\")\n" +
                "highlight: lines to highlight <list or a single value of either number(s) or string(s)> (e.g. [4, \"class\"])\n" +
                "autoTitle: auto title based on path <boolean> (e.g. true)")
    }

    @Test
    void "validates param types"() {
        code {
            paramsDefinition.validate(new PluginParams("file", [autoTitle: "false"]))
        } should throwException("type mismatches:\n" +
                "  autoTitle given: \"false\" <string>, expected: <boolean> (e.g. true)\n" +
                "\n" +
                "available plugin parameters:\n" +
                "title: title of snippet <string> (e.g. \"example of API\")\n" +
                "highlight: lines to highlight <list or a single value of either number(s) or string(s)> (e.g. [4, \"class\"])\n" +
                "autoTitle: auto title based on path <boolean> (e.g. true)")
    }
}
