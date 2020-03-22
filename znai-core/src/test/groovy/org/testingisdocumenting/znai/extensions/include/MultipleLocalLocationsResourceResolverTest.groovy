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

package org.testingisdocumenting.znai.extensions.include

import org.testingisdocumenting.znai.extensions.MultipleLocalLocationsResourceResolver
import org.junit.Test

import java.nio.file.Paths

class MultipleLocalLocationsResourceResolverTest {
    @Test
    void "resolves against specified list of dirs"() {
        def resolver = new MultipleLocalLocationsResourceResolver(Paths.get(""))
        resolver.initialize(["src/main/java/org/testingisdocumenting/znai",
                             "src/test/groovy/org/testingisdocumenting/znai"].stream())

        assert resolver.fullPath("core/AuxiliaryFile.java").toString() == 'src/main/java/org/testingisdocumenting/znai/core/AuxiliaryFile.java'
        assert resolver.fullPath("parser/MarkdownParserTest.groovy").toString() == 'src/test/groovy/org/testingisdocumenting/znai/parser/MarkdownParserTest.groovy'
    }

    @Test
    void "confirms if file is inside documentation"() {
        def resolver = new MultipleLocalLocationsResourceResolver(Paths.get("/path/to/docs"))
        assert resolver.isInsideDoc(Paths.get("/path/to/docs/image.png"))
    }
}
