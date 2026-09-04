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

package org.testingisdocumenting.znai.extensions.checkboxes

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.parser.commonmark.MarkdownParser

import java.nio.file.Paths

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class CheckboxesFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Test
    void "build checkbox items from bullet points"() {
        def props = process("* buy milk\n" +
                "* write docs\n")

        props.checkboxItems.should == [
                [id: 'buy-milk', content: [[type: 'Paragraph', content: [[text: 'buy milk', type: 'SimpleText']]]]],
                [id: 'write-docs', content: [[type: 'Paragraph', content: [[text: 'write docs', type: 'SimpleText']]]]]]
    }

    @Test
    void "generate unique ids for items with the same text"() {
        def props = process("* buy milk\n" +
                "* buy milk\n")

        props.checkboxItems.id.should == ['buy-milk', 'buy-milk-2']
    }

    @Test
    void "derive block id from content so blocks with same item text have distinct identity"() {
        def props = process("* buy milk\n")
        def samePropsAgain = process("* buy milk\n")
        def differentProps = process("* buy milk\n* write docs\n")

        props.blockId.should == samePropsAgain.blockId
        props.blockId.shouldNot == differentProps.blockId
    }

    @Test
    void "include inline code text in generated ids"() {
        def props = process("* install `cli`\n")

        props.checkboxItems.id.should == ['install-cli']
    }

    @Test
    void "preserve complex content of a bullet point"() {
        def props = process("* buy milk\n" +
                "\n" +
                "  extra paragraph with details\n")

        props.checkboxItems[0].content.type.should == ['Paragraph', 'Paragraph']
    }

    @Test
    void "validate only bullet points are present"() {
        code {
            process("regular paragraph text\n")
        } should throwException("only bullet points are supported inside checkboxes fence block, found: Paragraph")
    }

    @Test
    void "validate at least one bullet point is present"() {
        code {
            process("")
        } should throwException("no bullet points found inside checkboxes fence block")
    }

    @Test
    void "indexes text of all items"() {
        def pluginAndProps = processAndGetPluginWithProps("* buy milk\n" +
                "* write docs\n")

        pluginAndProps.plugin.textForSearch().text.should == ['buy milk write docs']
    }

    private static Map process(String markup) {
        return processAndGetPluginWithProps(markup).props
    }

    private static Map processAndGetPluginWithProps(String markup) {
        def componentsRegistry = new TestComponentsRegistry()
        componentsRegistry.defaultParser = new MarkdownParser(componentsRegistry)

        def plugin = new CheckboxesFencePlugin()
        def result = plugin.process(componentsRegistry, Paths.get("test.md"),
                pluginParamsFactory.create(plugin.id(), ""), markup)

        return [plugin: plugin, props: result.docElements[0].toMap()]
    }
}
