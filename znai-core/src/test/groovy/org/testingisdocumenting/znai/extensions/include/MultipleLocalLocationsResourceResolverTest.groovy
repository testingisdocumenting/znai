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

import org.junit.Test
import org.testingisdocumenting.znai.parser.TestComponentsRegistry
import org.testingisdocumenting.znai.resources.MultipleLocalLocationsResourceResolver

import java.nio.file.Paths

class MultipleLocalLocationsResourceResolverTest {
    def toc = TestComponentsRegistry.TEST_COMPONENTS_REGISTRY.docStructure().tableOfContents()

    @Test
    void "resolves against specified list of dirs"() {

        def resolver = new MultipleLocalLocationsResourceResolver(toc, Paths.get(""))
        resolver.initialize(["src/main/java/org/testingisdocumenting/znai",
                             "src/test/groovy/org/testingisdocumenting/znai"].stream())

        assert resolver.fullPath("core/AuxiliaryFile.java").toString() == 'src/main/java/org/testingisdocumenting/znai/core/AuxiliaryFile.java'
        assert resolver.fullPath("parser/MarkdownParserTest.groovy").toString() == 'src/test/groovy/org/testingisdocumenting/znai/parser/MarkdownParserTest.groovy'
    }

    @Test
    void "confirms if file is inside documentation"() {
        def resolver = new MultipleLocalLocationsResourceResolver(toc, Paths.get("/path/to/docs"))
        assert resolver.isInsideDoc(Paths.get("/path/to/docs/image.png"))
    }

    @Test
    void "treats parent traversal back into doc root as inside documentation"() {
        def resolver = new MultipleLocalLocationsResourceResolver(toc, Paths.get("/path/to/docs"))
        assert resolver.isInsideDoc(Paths.get("/path/to/docs/section/../image.png"))
    }

    @Test
    void "treats parent traversal escaping doc root as outside documentation"() {
        def resolver = new MultipleLocalLocationsResourceResolver(toc, Paths.get("/path/to/docs"))
        assert !resolver.isInsideDoc(Paths.get("/path/to/docs/../outside.png"))
    }

    @Test
    void "normalizes full path resolved relative to current markup file"() {
        def docRoot = Paths.get("src/test/resources").toAbsolutePath()
        def resolver = new MultipleLocalLocationsResourceResolver(toc, docRoot)
        resolver.setCurrentFilePath(docRoot.resolve("references/page.md"))

        def fullPath = resolver.fullPath("../images/png-test.png")
        assert !fullPath.toString().contains("..")
        assert fullPath.endsWith(Paths.get("images/png-test.png"))
    }

    @Test
    void "produces normalized doc relative path for parent traversal references"() {
        def docRoot = Paths.get("src/test/resources").toAbsolutePath()
        def resolver = new MultipleLocalLocationsResourceResolver(toc, docRoot)
        resolver.setCurrentFilePath(docRoot.resolve("references/page.md"))

        def auxiliaryFile = resolver.runtimeAuxiliaryFile("../images/png-test.png")
        // path used as a deploy URL must not contain ".." since some servers/CDNs
        // do not normalize URL path traversal during request handling
        assert !auxiliaryFile.deployRelativePath.toString().contains("..")
        assert auxiliaryFile.deployRelativePath == Paths.get("images/png-test.png")
    }
}
