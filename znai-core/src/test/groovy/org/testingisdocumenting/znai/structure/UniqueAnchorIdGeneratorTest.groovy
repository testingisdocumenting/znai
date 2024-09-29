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
    def path = Paths.get("")

    @Test
    void "same anchor id"() {
        def generator = new UniqueAnchorIdGenerator()
        generator.generateIds(path, "my-image").should == [main: "my-image"]

        generator.registerSectionOrSubHeading(path, 1, "section")
        generator.registerSectionOrSubHeading(path, 2, "sub-section")
        generator.generateIds(path, "my-image").should == [main: "section-sub-section-my-image", additional: ["sub-section-my-image"]]
        generator.generateIds(path, "my-image").should == [main: "section-sub-section-my-image-2", additional: ["sub-section-my-image-2"]]

        generator.registerSectionOrSubHeading(path, 2, "another-sub-section")
        generator.generateIds(path, "my-image").should == [main: "section-another-sub-section-my-image", additional: ["another-sub-section-my-image"]]

        generator.registerSectionOrSubHeading(path, 1, "another-section")
        generator.generateIds(path, "my-image").should == [main: "another-section-my-image", additional: []]
    }

    @Test
    void "different paths"() {
        def generator = new UniqueAnchorIdGenerator()

        generator.registerSectionOrSubHeading(path, 1, "section")
        generator.registerSectionOrSubHeading(path, 2, "sub-section")

        def anotherPath = Paths.get("my-file.md")
        generator.generateIds(anotherPath, "my-image").main().should == "my-image"
    }

    @Test
    void "empty non unique id"() {
        def generator = new UniqueAnchorIdGenerator()
        generator.registerSectionOrSubHeading(path, 1, "section")
        generator.registerSectionOrSubHeading(path, 2, "sub-section")
        generator.generateIds(path, "").should == [main: "section-sub-section", additional: ["sub-section"]]
    }
}
