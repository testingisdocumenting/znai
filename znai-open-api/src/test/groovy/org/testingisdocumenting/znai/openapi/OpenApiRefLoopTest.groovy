/*
 * Copyright 2022 znai maintainers
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

import org.junit.Test
import org.testingisdocumenting.znai.utils.ResourceUtils

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class OpenApiRefLoopTest {
    @Test
    void "parse"() {
        def spec = OpenApiSpec.fromJson(
                TEST_COMPONENTS_REGISTRY.markdownParser(),
                ResourceUtils.textContent("open-api-spec-loop.json"))

        def operation = spec.findOperationById('findPets')
        def properties = operation.responses[0].schema.items.properties
        properties.keySet().should == ["name", "friend", "tag", "id"]
        properties.friend.should == [
                type: "NewPet",
                description: [[markdown: "pet friend of type Pet", type: "TestMarkdown"]],
                properties: [:]]
    }
}
