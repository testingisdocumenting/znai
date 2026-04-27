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
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.extensions.PluginResult
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.TestMarkupParser
import org.testingisdocumenting.znai.parser.TestResourceResolver

import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

import static org.testingisdocumenting.webtau.Matchers.*
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class UserDefinedIncludePluginTest {
    private static final Path MARKUP_PATH = Paths.get("test.md")

    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    private TestResourceResolver resolver = new TestResourceResolver(Paths.get(""))
    private TestComponentsRegistry componentsRegistry = registry()

    @Test
    void "processes template, collects search text, and exposes template, config and available-values reference as auxiliary files"() {
        def plugin = loadIncludePlugin("user-plugin-referenced.json")
        def markup = firstMarkup(processInclude(plugin, "referenced-note", "hello there", [category: "info"]))

        markup.should contain('message: "hello there"')
        markup.should contain('category: "info"')

        def searchText = plugin.textForSearch()[0].text
        searchText.should contain("hello there")
        searchText.should contain("info")

        def auxNames = plugin.auxiliaryFiles(componentsRegistry)
                .map { AuxiliaryFile f -> f.path.fileName.toString() }
                .collect(Collectors.toList())

        auxNames.should contain("user-plugin-referenced.json")
        auxNames.should contain("user-plugin-simple.ftl")
        auxNames.should contain("user-plugin-categories.json")
    }

    @Test
    void "optional freeForm is allowed to be missing without error"() {
        def markup = firstMarkup(processInclude("user-plugin-optional-free-form.json", "optional-note", "", [category: "info"]))

        markup.should contain('message: ""')
        markup.should contain('category: "info"')
    }

    @Test
    void "fence plugin exposes fenceContent in the template"() {
        def plugin = new UserDefinedFencePlugin(UserDefinedPluginConfig.load(resolver, "user-plugin-fence.json"))

        def result = plugin.process(componentsRegistry, MARKUP_PATH,
                pluginParamsFactory.create("simple-fence", "", [lang: "java"]),
                "System.out.println();")

        def markup = firstMarkup(result)
        markup.should contain("```java")
        markup.should contain("System.out.println();")
    }

    @Test
    void "fence plugin with required fenceContent fails on empty body"() {
        def config = UserDefinedPluginConfig.parse(resolver, Paths.get("required-fence.json"),
                [id: "required-fence", type: "fence", template: "user-plugin-fence.ftl",
                 arguments: [fenceContent: [required: true],
                             lang: [type: "string", required: true, limitValuesTo: ["java"]]]])
        def plugin = new UserDefinedFencePlugin(config)

        code {
            plugin.process(componentsRegistry, MARKUP_PATH,
                    pluginParamsFactory.create("required-fence", "", [lang: "java"]),
                    "")
        } should throwException(~/requires non-empty fence content/)
    }

    private UserDefinedIncludePlugin loadIncludePlugin(String configName) {
        return new UserDefinedIncludePlugin(UserDefinedPluginConfig.load(resolver, configName))
    }

    private PluginResult processInclude(UserDefinedIncludePlugin plugin, String pluginId, String freeForm, Map<String, Object> namedArgs) {
        return plugin.process(componentsRegistry, null, MARKUP_PATH,
                pluginParamsFactory.create(pluginId, freeForm, namedArgs))
    }

    private PluginResult processInclude(String configName, String pluginId, String freeForm, Map<String, Object> namedArgs) {
        return processInclude(loadIncludePlugin(configName), pluginId, freeForm, namedArgs)
    }

    private static String firstMarkup(PluginResult result) {
        return result.docElements.collect { it.toMap() }[0].markup as String
    }

    private static TestComponentsRegistry registry() {
        def componentsRegistry = new TestComponentsRegistry()
        componentsRegistry.defaultParser = new TestMarkupParser()
        return componentsRegistry
    }
}
