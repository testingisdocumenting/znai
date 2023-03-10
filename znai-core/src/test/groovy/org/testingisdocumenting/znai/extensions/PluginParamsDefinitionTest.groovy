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
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.*

class PluginParamsDefinitionTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    static def paramsDefinition = new PluginParamsDefinition()
            .add("title", PluginParamType.STRING, "title of snippet", "\"example of API\"")
            .add("highlight", PluginParamType.LIST_OR_SINGLE_STRING_OR_NUMBER,
                    "lines to highlight", "[4, \"class\"]")
            .add("autoTitle", PluginParamType.BOOLEAN, "auto title based on path", "true")
            .add("newList", PluginParamType.LIST_OR_SINGLE_STRING, "list of values")
            .addRequired("id", PluginParamType.STRING, "id of the concept", "true")
            .rename("oldTitle", "title")
            .rename("oldList", "newList")

    static def expectedPluginParametersHelp = "available plugin parameters:\n" +
            "  autoTitle: auto title based on path <boolean> (e.g. true)\n" +
            "  highlight: lines to highlight <list or a single value of either number(s) or string(s)> (e.g. [4, \"class\"])\n" +
            "  id: REQUIRED id of the concept <string> (e.g. true)\n" +
            "  title: title of snippet <string> (e.g. \"example of API\")\n"

    @Test
    void "passes validation"() {
        paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file",  "", [id: "id1", title: "my title"]))
    }

    @Test
    void "validates param names"() {
        def result = paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file", "", [totle: "my title", id: "id1"]))
        result.validationError.should == "unrecognized parameter(s): totle\n" +
                "\n" + expectedPluginParametersHelp
    }

    @Test
    void "validates param types"() {
        def result = paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file", "", [autoTitle: "false", id: "id2"]))
        result.validationError.should == "type mismatches:\n" +
                "  autoTitle given: \"false\" <string>, expected: <boolean> (e.g. true)\n" +
                "\n" + expectedPluginParametersHelp
    }

    @Test
    void "validates required parameters"() {
        def result = paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file", "", [:]))
        result.validationError.should == "missing required parameter(s): id\n" +
                "\n" + expectedPluginParametersHelp
    }

    @Test
    void "all validations at once"() {
        def result = paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file", "", [totle: "my title", autoTitle: "false"]))
        result.validationError.should == "missing required parameter(s): id\n" +
                "unrecognized parameter(s): totle\n" +
                "type mismatches:\n" +
                "  autoTitle given: \"false\" <string>, expected: <boolean> (e.g. true)\n" +
                "\n" + expectedPluginParametersHelp
    }

    @Test
    void "parameter renames should provide value access by using new name"() {
        def pluginParams = pluginParamsFactory.create("file", "", [oldTitle: "my custom title", autoTitle: "false"])
        paramsDefinition.validateParamsAndHandleRenames(pluginParams)
        pluginParams.getOpts().get("title").should == "my custom title"
    }

    @Test
    void "parameter should provide list value access by using new name when old name was used to set data"() {
        def pluginParams = pluginParamsFactory.create("file", "", [oldList: ["a", "b", "c"]])
        paramsDefinition.validateParamsAndHandleRenames(pluginParams)
        pluginParams.getOpts().getList("newList").should == ["a", "b", "c"]
    }

    @Test
    void "parameter should provide list value access by using new name when new name was used to set data"() {
        def pluginParams = pluginParamsFactory.create("file", "", [newList: ["a", "b", "c"]])
        paramsDefinition.validateParamsAndHandleRenames(pluginParams)
        pluginParams.getOpts().getList("newList").should == ["a", "b", "c"]
    }

    @Test
    void "parameter renames should generate warnings"() {
        def result = paramsDefinition.validateParamsAndHandleRenames(pluginParamsFactory.create("file", "", [oldTitle: "my title", autoTitle: "false"]))
        result.warnings.should == [
                [message: "parameter is renamed to <title>", parameterName: "oldTitle", pluginId: "file"]
        ]
    }

    @Test
    void "report name duplicate"() {
        code {
            def result = paramsDefinition.add("title", PluginParamType.BOOLEAN, "dummy", "true")
        } should throwException("parameter <title> is already registered")

        code {
            paramsDefinition.addRequired("title", PluginParamType.BOOLEAN, "dummy", "true")
        } should throwException("parameter <title> is already registered")

        code {
            def additional = new PluginParamsDefinition()
                    .add("hello", PluginParamType.BOOLEAN, "dummy", "dummy")
                    .add("id", PluginParamType.BOOLEAN, "dummy", "dummy")

            paramsDefinition.add(additional)
        } should throwException("parameter <id> is already registered")
    }
}
