/*
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


import org.junit.BeforeClass
import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class OpenApiSpec3Test {
    static OpenApi3Spec spec

    @BeforeClass
    static void init() {
        spec = OpenApi3Spec.parse(TEST_COMPONENTS_REGISTRY.markdownParser(), ResourceUtils.textContent("petstore-openapi3.json"))
    }

    @Test
    void "query parameters"() {
        def findPet = spec.findById("findPetsByStatus")
        findPet.summary.should == "Finds Pets by status"
        findPet.description.should == "Multiple status values can be provided with comma separated strings"
        findPet.path.should == "/pet/findByStatus"
        findPet.tags.should == ["pet"]
        findPet.request.should == null
        println findPet
    }
}
