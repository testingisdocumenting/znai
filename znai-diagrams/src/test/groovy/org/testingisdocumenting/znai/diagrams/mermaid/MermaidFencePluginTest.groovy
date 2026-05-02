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

package org.testingisdocumenting.znai.diagrams.mermaid

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.junit.After
import org.junit.Before
import org.junit.Test

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY
import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class MermaidFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clear()
    }

    @Test
    void "should process mermaid diagram without iconpacks parameter"() {
        def mermaidContent = '''graph TD
    A[Start] --> B[Process]
    B --> C[End]'''

        def elements = process(mermaidContent, [wide: true])

        elements.should == [
                mermaid: mermaidContent,
                wide: true,
                ]
    }

    @Test
    void "should process mermaid diagram with iconpacks parameter"() {
        def mermaidContent = '''graph TD
    A[Start] --> B[Process]
    B --> C[End]'''

        def elements = process(mermaidContent, [iconpacks: [[name: "test", url: "test-icons.json"]]])

        elements.should == [
                mermaid: mermaidContent,
                iconpacks: [[name: "test", url: "/test-doc/test-icons.json"]]]
    }

    @Test
    void "should process simple mermaid diagram without any parameters"() {
        def mermaidContent = '''graph TD
    A --> B'''

        def elements = process(mermaidContent, Collections.emptyMap())

        elements.should == [
                mermaid: mermaidContent
                ]
    }


    @Test
    void "should throw exception when iconpack missing name attribute"() {
        def mermaidContent = '''graph TD
        A[Start] --> B[Process]
        B --> C[End]'''

        code {
            process(mermaidContent, [iconpacks: [[url: "/test-icons.json"]]])
        } should throwException(IllegalArgumentException.class, "iconpack name is missing")
    }

    @Test
    void "should throw exception when iconpack missing url attribute"() {
        def mermaidContent = '''graph TD
        A[Start] --> B[Process]
        B --> C[End]'''

        code {
            process(mermaidContent, [iconpacks: [[name: "test"]]])
        } should throwException(IllegalArgumentException.class, "iconpack url is missing")
    }

    @Test
    void "should resolve relative links and keep external links"() {
        TEST_COMPONENTS_REGISTRY.docStructure().addValidLink("doc/page")
        TEST_COMPONENTS_REGISTRY.docStructure().addValidLink("ref/another#section")

        def mermaidContent = '''flowchart TD
    A[Start] --> B[Process] --> C[End]
    click A "doc/page"
    click B href "ref/another#section" "Go to page"
    click C "https://example.com"'''

        def elements = process(mermaidContent, Collections.emptyMap())

        elements.mermaid.should == '''flowchart TD
    A[Start] --> B[Process] --> C[End]
    click A "/test-doc/doc/page"
    click B href "/test-doc/ref/another#section" "Go to page"
    click C "https://example.com"'''
    }

    @Test
    void "should validate links and report invalid ones"() {
        def mermaidContent = '''flowchart TD
    A[Start] --> B[End]
    click A "doc/page"'''

        code {
            process(mermaidContent, Collections.emptyMap())
        } should throwException(~/no valid link.*doc\/page/)
    }

    private static def process(String content, Map<String, ?>  params) {
        return PluginsTestUtils.processFenceAndGetProps(pluginParamsFactory.create("mermaid", "", params), content)
    }
}