/*
 * Copyright 2021 znai maintainers
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

package org.testingisdocumenting.znai.extensions.cli

import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.parser.TestComponentsRegistry

import static org.testingisdocumenting.znai.extensions.include.PluginsTestUtils.processAndGetFencePluginAndParserHandler
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class CliFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()
    
    @Test
    void "create cli commands element from a single line"() {
        def pluginParams = pluginParamsFactory.create("cli", "", [highlight: "prob"])
        def result = processAndGetFencePluginAndParserHandler(pluginParams,  " my-script prob twice\n ")

        result.fencePlugin.textForSearch().text.should == "my-script prob twice"
        result.parserHandler.docElement.contentToListOfMaps().should == [
                [command: "my-script prob twice", paramsToHighlight: ["prob"], type: "CliCommand"]]
    }

    @Test
    void "create cli commands element from multiple lines"() {
        def pluginParams = pluginParamsFactory.create("cli", "", [highlight: "prob"])
        def result = processAndGetFencePluginAndParserHandler(pluginParams,
                " my-script1 prob twice\n " +
                " my-script2 three\n ")

        result.fencePlugin.textForSearch().text.should == "my-script1 prob twice my-script2 three"
        result.parserHandler.docElement.contentToListOfMaps().should == [
                [command: "my-script1 prob twice", paramsToHighlight: ["prob"], type: "CliCommand"],
                [command: "my-script2 three", paramsToHighlight: ["prob"], type: "CliCommand"]
        ]
    }
}
