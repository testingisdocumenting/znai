/*
 * Copyright 2026 znai maintainers
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

package org.testingisdocumenting.znai.extensions.userdefined

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.parser.TestResourceResolver

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class UserDefinedPluginConfigTest {
    private TestResourceResolver resolver = new TestResourceResolver(Paths.get(""))

    @Test
    void "parses plugin config with inline available values"() {
        UserDefinedPluginConfig config = loadConfig("user-plugin-simple.json")

        config.id.should == "simple-note"
        config.role.should == UserDefinedPluginConfig.PluginRole.INCLUDE

        def freeForm = config.arguments["freeForm"]
        freeForm.freeForm.should == true
        freeForm.required.should == true

        def category = config.arguments["category"]
        category.availableValues.should == ["info", "warning"]
    }

    @Test
    void "resolves available values from a file reference"() {
        UserDefinedPluginConfig config = loadConfig("user-plugin-referenced.json")

        def category = config.arguments["category"]
        category.availableValues.should == ["info", "warning", "error"]
        category.availableValuesPath.fileName.toString().should == "user-plugin-categories.json"
    }

    @Test
    void "freeForm without required defaults to optional and skips type"() {
        UserDefinedPluginConfig config = loadConfig("user-plugin-optional-free-form.json")

        def freeForm = config.arguments["freeForm"]
        freeForm.freeForm.should == true
        freeForm.required.should == false
        freeForm.paramType.should == null
    }

    @Test
    void "supports fence type and fenceContent special arg"() {
        UserDefinedPluginConfig config = loadConfig("user-plugin-fence.json")

        config.role.should == UserDefinedPluginConfig.PluginRole.FENCE
        config.fenceContentArgument.fenceContent.should == true
    }

    @Test
    void "rejects freeForm arg on a fence plugin"() {
        code {
            loadRaw([id: "x", type: "fence", template: "t.ftl", arguments: [freeForm: [required: true]]])
        } should throwException(~/<freeForm> is only allowed for plugins with <type: include>/)
    }

    @Test
    void "rejects fenceContent arg on an include plugin"() {
        code {
            loadRaw([id: "x", type: "include", template: "t.ftl", arguments: [fenceContent: [:]]])
        } should throwException(~/<fenceContent> is only allowed for plugins with <type: fence>/)
    }

    @Test
    void "fenceContent required flag is honored at parse time"() {
        UserDefinedPluginConfig config = loadRaw([id: "x", type: "fence", template: "user-plugin-fence.ftl",
                                                  arguments: [fenceContent: [required: true]]])

        config.fenceContentArgument.required.should == true
    }

    @Test
    void "requires id, type, and template at parse time"() {
        code {
            loadRaw([type: "include", template: "t.ftl"])
        } should throwException(~/missing required string field <id>/)

        code {
            loadRaw([id: "x", template: "t.ftl"])
        } should throwException(~/missing <type>/)

        code {
            loadRaw([id: "x", type: "include"])
        } should throwException(~/missing required string field <template>/)
    }

    @Test
    void "rejects unknown plugin type"() {
        code {
            loadRaw([id: "x", type: "unknown", template: "t.ftl"])
        } should throwException(~/unknown plugin type/)
    }

    @Test
    void "rejects unknown argument type and malformed reference"() {
        code {
            loadRaw([id: "x", type: "include", template: "t.ftl", arguments: [bad: [type: "wrong"]]])
        } should throwException(~/unknown argument type/)

        code {
            loadRaw([id: "x", type: "include", template: "t.ftl", arguments: [bad: [type: "string", available: "no-dollar"]]])
        } should throwException(~/available reference must start with/)
    }

    @Test
    void "builds param definition and validates against available values"() {
        UserDefinedPluginConfig config = loadConfig("user-plugin-simple.json")
        def definition = config.buildParamsDefinition()

        def validParams = new PluginParams("simple-note", "hello", [category: "info"])
        definition.validateParamsAndHandleRenames(validParams).validationError.should == ""

        def invalidParams = new PluginParams("simple-note", "hello", [category: "nope"])
        def result = definition.validateParamsAndHandleRenames(invalidParams)
        (result.validationError.length() > 0).should == true
    }

    private UserDefinedPluginConfig loadConfig(String name) {
        return UserDefinedPluginConfig.load(resolver, name)
    }

    private UserDefinedPluginConfig loadRaw(Map<String, Object> raw) {
        return UserDefinedPluginConfig.parse(resolver, Paths.get("test.json"), raw)
    }
}
