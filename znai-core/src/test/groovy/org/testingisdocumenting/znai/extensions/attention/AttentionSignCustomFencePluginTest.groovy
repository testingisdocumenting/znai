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

package org.testingisdocumenting.znai.extensions.attention

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class AttentionSignCustomFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()

    @Test
    void "uses free form parameter as the attention type"() {
        def props = process("my-type", [:], "hello world")
        props.attentionType.should == "my-type"
    }

    @Test
    void "trims the free form type"() {
        def props = process("  my-type  ", [:], "hello world")
        props.attentionType.should == "my-type"
    }

    @Test
    void "supports optional label"() {
        def props = process("my-type", [label: "Consider"], "hello world")
        props.attentionType.should == "my-type"
        props.label.should == "Consider"
    }

    @Test
    void "supports optional icon"() {
        def props = process("my-type", [icon: "zap"], "hello world")
        props.attentionType.should == "my-type"
        props.icon.should == "zap"
    }

    @Test
    void "has no icon by default"() {
        def props = process("my-type", [:], "hello world")
        props.containsKey("icon").should == false
    }

    @Test
    void "fails when type is not provided"() {
        code {
            process("", [:], "hello world")
        } should throwException(~/attention-custom requires a type/)
    }

    private static def process(String type, Map<String, ?> params, String content) {
        return PluginsTestUtils.processFenceAndGetProps(
                pluginParamsFactory.create("attention-custom", type, params), content)
    }
}
