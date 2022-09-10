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

package org.testingisdocumenting.znai.structure

import org.junit.Test

import java.nio.file.Paths

class UniqueAnchorIdGeneratorTest {
    @Test
    void "same anchor id"() {
        def path = Paths.get("")

        def generator = new UniqueAnchorIdGenerator()
        generator.generateId(path, "my-image").should == "my-image"

        generator.registerSectionOrSubHeading(path, 1, "section")
        generator.registerSectionOrSubHeading(path, 2, "sub-section")
        generator.generateId(path, "my-image").should == "section-sub-section-my-image"
        generator.generateId(path, "my-image").should == "section-sub-section-my-image-2"

        generator.registerSectionOrSubHeading(path, 2, "another-sub-section")
        generator.generateId(path, "my-image").should == "section-another-sub-section-my-image"

        generator.registerSectionOrSubHeading(path, 1, "another-section")
        generator.generateId(path, "my-image").should == "another-section-my-image"
    }

    @Test
    void "different paths"() {
        def generator = new UniqueAnchorIdGenerator()

        def path = Paths.get("")
        generator.registerSectionOrSubHeading(path, 1, "section")
        generator.registerSectionOrSubHeading(path, 2, "sub-section")

        def anotherPath = Paths.get("my-file.md")
        generator.generateId(anotherPath, "my-image").should == "my-image"
    }
}
