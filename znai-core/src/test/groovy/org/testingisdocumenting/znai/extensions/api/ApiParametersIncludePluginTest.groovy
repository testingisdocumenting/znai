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
import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException
import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class ApiParametersIncludePluginTest {
    @Before
    @After
    void init() {
        TEST_COMPONENTS_REGISTRY.docStructure().clear()
    }

    @Test
    void "should work without type in json"() {
        def props = PluginsTestUtils.processIncludeAndGetProps(
                ":include-api-parameters: api-params-no-type.json")

        props.should == [parameters: [[name: "firstName", type:[], anchorId: "firstName",
                                       description: [[markdown: "first name", type: "TestMarkdown"]]]]]
    }


    @Test
    void "should report all missing required fields"() {
        code {
            PluginsTestUtils.processIncludeAndGetProps(
                    ":include-api-parameters: api-params-missing-fields.json")
        } should throwException(IllegalArgumentException, "missing required fields: name, description\n" +
                "record: {}")
    }

    @Test
    void "should report specific missing required field"() {
        code {
            PluginsTestUtils.processIncludeAndGetProps(
                    ":include-api-parameters: api-params-missing-description.json")
        } should throwException(IllegalArgumentException, "missing required fields: description\n" +
                "record: {\"name\":\"firstName\"}")
    }

    @Test
    void "should provide search text"() {
        def plugin = PluginsTestUtils.processAndGetIncludePluginAndParserHandler(
                ":include-api-parameters: api-params-simple.json").includePlugin

         plugin.textForSearch().text.should == 'firstName String first name'
    }
    @Test
    void "should register local anchors"() {
        PluginsTestUtils.processAndGetIncludePluginAndParserHandler(
                ":include-api-parameters: api-params-simple.json {anchorPrefix: 'myPrefix'}").includePlugin

        TEST_COMPONENTS_REGISTRY.docStructure().registeredLocalLinks.should == ['myPrefix_firstName']
    }
}
