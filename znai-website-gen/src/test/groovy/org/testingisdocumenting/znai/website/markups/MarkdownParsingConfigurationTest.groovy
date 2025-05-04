/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.website.markups

import org.junit.Test
import org.testingisdocumenting.znai.core.MarkupPathWithError
import org.testingisdocumenting.znai.structure.TableOfContents

import java.nio.file.Paths

import static org.testingisdocumenting.znai.parser.TestComponentsRegistry.TEST_COMPONENTS_REGISTRY

class MarkdownParsingConfigurationTest {
    @Test
    void "should detect index toc item"() {
        def toc = new TableOfContents("md")
        toc.addTocItem("chapter1", "page-a")
        toc.addTocItem("chapter1", "index")
        toc.addIndex()
        toc.resolveTocItemPathsAndReturnMissing { new MarkupPathWithError(Paths.get("/root/" + it.getFilePath()), null) }

        def config = new MarkdownParsingConfiguration()

        def tocItem = config.tocItemByPath(TEST_COMPONENTS_REGISTRY, toc, Paths.get("/root/chapter1/page-a.md"))
        tocItem.dirName.should == "chapter1"
        tocItem.fileNameWithoutExtension.should == "page-a"

        tocItem = config.tocItemByPath(TEST_COMPONENTS_REGISTRY, toc, Paths.get("/root/chapter1/index.md"))
        tocItem.isIndex().should == false
        tocItem.dirName.should == "chapter1"
        tocItem.fileNameWithoutExtension.should == "index"

        tocItem = config.tocItemByPath(TEST_COMPONENTS_REGISTRY, toc, Paths.get("/root/index.md"))
        tocItem.isIndex().should == true
        tocItem.dirName.should == ""
        tocItem.fileNameWithoutExtension.should == "index"

        config.tocItemResourceName(tocItem).should == "index.md"
    }
}
