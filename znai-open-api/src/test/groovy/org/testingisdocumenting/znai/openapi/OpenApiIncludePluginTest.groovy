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

class OpenApiIncludePluginTest {
    @Test
    void "should automatically create a section for summary"() {
        def elements = process('open-api-spec.json {operationId: "addPet", autoSection: true}')

        elements[0].title.should == 'Add Pet'
        elements[0].id.should == 'add-pet'
        elements[0].type.should == 'Section'
    }

    @Test
    void "should create multiple entries by tags"() {
        def elements = process('open-api-spec.json {tags: "multiple", autoSection: true}')

        elements.should == ['type'    | 'title'] {
                            _______________________________________________________
                            'Section' | 'find all pets'
                            'Section' | 'find all customers ordered by last name'  }
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
