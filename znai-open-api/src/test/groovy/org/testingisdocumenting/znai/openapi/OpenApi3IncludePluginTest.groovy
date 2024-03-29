/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

package org.testingisdocumenting.znai.openapi

import org.testingisdocumenting.znai.extensions.include.PluginsTestUtils
import org.junit.Test

import static org.testingisdocumenting.webtau.Matchers.code
import static org.testingisdocumenting.webtau.Matchers.throwException

class OpenApi3IncludePluginTest {
    @Test
    void "should automatically create a section for summary"() {
        def elements = process('test-openapi3.json {operationId: "addPet", autoSection: true}')

        elements[0].title.should == 'Add a new pet to the store'
        elements[0].type.should == 'Section'
    }

    @Test
    void "should validate summary field presence when create a section"() {
        code {
            process('test-openapi3.json {operationId: "findPetsByStatusNoSummary", autoSection: true}')
        } should throwException("summary is missing for operation <findPetsByStatusNoSummary>")
    }

    @Test
    void "should create multiple entries by tags"() {
        def elements = process('test-openapi3.json {tags: "user", autoSection: true}')

        elements[0].title.should == 'Create user'
        elements[1].title.should == 'Creates list of users with given input array'
    }

    @Test
    void "should detect extension and use appropriate parser, json or yaml"() {
        def elements = process('open-api-spec.yaml {operationId: "addPet", autoSection: true}')
        elements[0].title.should == 'Add Pet'
    }

    private static def process(String params) {
        def result = PluginsTestUtils.processInclude(":include-open-api: $params")
        return result*.toMap()
    }
}
