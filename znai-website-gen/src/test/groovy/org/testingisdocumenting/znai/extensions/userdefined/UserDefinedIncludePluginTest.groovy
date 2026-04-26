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
import org.testingisdocumenting.znai.core.AuxiliaryFile
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.TestMarkupParser
import org.testingisdocumenting.znai.parser.TestResourceResolver

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class UserDefinedIncludePluginTest {
    private TestResourceResolver resolver = new TestResourceResolver(Paths.get(""))
    private TestComponentsRegistry componentsRegistry = registry()

    @Test
    void "processes template with freeForm and named args, and collects search text"() {
        def config = UserDefinedPluginConfig.load(resolver, "user-plugin-simple.json")
        def plugin = new UserDefinedIncludePlugin(config)
        def params = new PluginParams("simple-note", "hello there", [category: "info"])

        def result = plugin.process(componentsRegistry, null, Paths.get("test.md"), params)
        def elements = result.docElements.collect { it.toMap() }

        elements.size().should == 1
        def markup = elements[0].markup as String
        markup.contains("message: \"hello there\"").should == true
        markup.contains("category: \"info\"").should == true

        def searchText = plugin.textForSearch()[0].text
        searchText.contains("hello there").should == true
        searchText.contains("info").should == true
    }

    @Test
    void "exposes template and config files as auxiliary files"() {
        def config = UserDefinedPluginConfig.load(resolver, "user-plugin-simple.json")
        def plugin = new UserDefinedIncludePlugin(config)
        plugin.process(componentsRegistry, null, Paths.get("test.md"),
                new PluginParams("simple-note", "msg", [category: "info"]))

        def auxNames = plugin.auxiliaryFiles(componentsRegistry)
                .map { AuxiliaryFile f -> f.path.fileName.toString() }
                .collect(java.util.stream.Collectors.toList())

        auxNames.contains("user-plugin-simple.json").should == true
        auxNames.contains("user-plugin-simple.ftl").should == true
    }

    @Test
    void "available-values reference file is exposed as auxiliary file"() {
        def config = UserDefinedPluginConfig.load(resolver, "user-plugin-referenced.json")
        def plugin = new UserDefinedIncludePlugin(config)
        plugin.process(componentsRegistry, null, Paths.get("test.md"),
                new PluginParams("referenced-note", "msg", [category: "info"]))

        def auxNames = plugin.auxiliaryFiles(componentsRegistry)
                .map { AuxiliaryFile f -> f.path.fileName.toString() }
                .collect(java.util.stream.Collectors.toList())

        auxNames.contains("user-plugin-categories.json").should == true
    }

    @Test
    void "optional freeForm is allowed to be missing without error"() {
        def config = UserDefinedPluginConfig.load(resolver, "user-plugin-optional-free-form.json")
        def plugin = new UserDefinedIncludePlugin(config)
        def params = new PluginParams("optional-note", "", [category: "info"])

        def result = plugin.process(componentsRegistry, null, Paths.get("test.md"), params)
        def markup = result.docElements.collect { it.toMap() }[0].markup as String

        markup.contains('message: ""').should == true
        markup.contains('category: "info"').should == true
    }

    @Test
    void "fence plugin exposes fenceContent in the template"() {
        def config = UserDefinedPluginConfig.load(resolver, "user-plugin-fence.json")
        def plugin = new UserDefinedFencePlugin(config)

        def result = plugin.process(componentsRegistry, Paths.get("test.md"),
                new PluginParams("simple-fence", "", [lang: "java"]),
                "System.out.println();")

        def elements = result.docElements.collect { it.toMap() }
        def markup = elements[0].markup as String

        markup.contains("```java").should == true
        markup.contains("System.out.println();").should == true
    }

    @Test
    void "fence plugin with required fenceContent fails on empty body"() {
        def config = UserDefinedPluginConfig.parse(resolver, Paths.get("required-fence.json"),
                [id: "required-fence", type: "fence", template: "user-plugin-fence.ftl",
                 arguments: [fenceContent: [required: true],
                             lang: [type: "string", required: true, available: ["java"]]]])
        def plugin = new UserDefinedFencePlugin(config)

        code {
            plugin.process(componentsRegistry, Paths.get("test.md"),
                    new PluginParams("required-fence", "", [lang: "java"]),
                    "")
        } should throwException(~/requires non-empty fence content/)
    }

    private static TestComponentsRegistry registry() {
        def r = new TestComponentsRegistry()
        r.defaultParser = new TestMarkupParser()
        return r
    }
}
