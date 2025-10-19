/*
 * Copyright 2020 znai maintainers
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

package org.testingisdocumenting.znai.extensions.api

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.testingisdocumenting.znai.extensions.PluginParams
import org.testingisdocumenting.znai.extensions.PluginParamsFactory
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.testingisdocumenting.znai.parser.TestComponentsRegistry

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class ApiParametersFencePluginTest {
    static PluginParamsFactory pluginParamsFactory = TEST_COMPONENTS_REGISTRY.pluginParamsFactory()
    
    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clear()
    }

    @Test
    void "should work without type in csv"() {
        def props = PluginsTestUtils.processFenceAndGetProps(
                pluginParamsFactory.create("api-parameters", "", [:]),
        "firstName,,first name")

        props.should == [parameters: [[name: "firstName", type:[], anchorId: "firstName",
                                       description: [[markdown: "first name", type: "TestMarkdown"]]]]]
    }

    @Test
    void "should report missing fields in csv"() {
        code {
            PluginsTestUtils.processFenceAndGetProps(
                    pluginParamsFactory.create("api-parameters", "", [:]), "firstName")
        } should throwException("record mismatches header. header: [name, type, description]; record: [firstName]")
    }

    @Test
    void "should provide search text"() {
        def plugin = PluginsTestUtils.processAndGetFencePluginAndParserHandler(
                pluginParamsFactory.create("api-parameters", "", [:]),
                "firstName, String, first name").fencePlugin

        plugin.textForSearch().text.should == ["firstName String first name"]
    }

    @Test
    void "should register local anchors"() {
        PluginsTestUtils.processAndGetFencePluginAndParserHandler(
                pluginParamsFactory.create("api-parameters", "", [anchorPrefix: "myPrefix"]),
                "firstName, String, first name").fencePlugin

        TEST_COMPONENTS_REGISTRY.docStructure().registeredLocalLinks.should == ["myPrefix_firstName"]
    }
}
